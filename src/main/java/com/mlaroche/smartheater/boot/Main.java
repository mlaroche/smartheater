/**
 * Copyright 2018 Matthieu Laroche - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mlaroche.smartheater.boot;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.SpringServletContainerInitializer;

public class Main {

	private static List<ContainerInitializer> springInitializers() {
		final SpringServletContainerInitializer sci = new SpringServletContainerInitializer();
		final ContainerInitializer initializer = new ContainerInitializer(sci, null);
		initializer.addApplicableTypeName(SmartheaterVSpringWebApplicationInitializer.class.getCanonicalName());
		final List<ContainerInitializer> initializers = new ArrayList<>();
		initializers.add(initializer);
		return initializers;
	}

	private static ClassLoader getUrlClassLoader() {
		return new URLClassLoader(new URL[0], Main.class.getClassLoader());
	}

	private static File getScratchDir() throws IOException {
		final File tempDir = new File(System.getProperty("java.io.tmpdir"));
		final File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");

		if (!scratchDir.exists()) {
			if (!scratchDir.mkdirs()) {
				throw new IOException("Unable to create scratch directory: " + scratchDir);
			}
		}
		return scratchDir;
	}

	public static void main(final String[] args) throws IOException, Exception {
		final Server server = new Server(8080);
		final WebAppContext context = new WebAppContext(getUrlClassLoader().getResource("com/mlaroche/smartheater/webapp").toExternalForm(), "/smartheater");
		System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");
		//context.setAttribute("jacoco.exclClassLoaders", "*");

		context.setAttribute("javax.servlet.context.tempdir", getScratchDir());
		context.setAttribute("org.eclipse.jetty.containerInitializers", springInitializers());

		context.setInitParameter("boot.paramsUrl", args[0]);
		context.addBean(new ServletContainerInitializersStarter(context), true);
		context.setClassLoader(getUrlClassLoader());
		context.setClassLoader(new WebAppClassLoader(Main.class.getClassLoader(), context));

		server.setHandler(context);
		server.start();

		server.join();

	}

}
