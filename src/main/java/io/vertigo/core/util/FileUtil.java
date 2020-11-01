/**
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2020, Vertigo.io, team@vertigo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.WrappedException;

public final class FileUtil {
	/**
	 * Constructeur privé pour classe utilitaire
	 */
	private FileUtil() {
		//rien
	}

	public static String read(final URL url) {
		Assertion.check().isNotNull(url);
		//---
		try {
			try (final BufferedReader reader = new BufferedReader(
					new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
				final StringBuilder buff = new StringBuilder();
				String line = reader.readLine();
				while (line != null) {
					buff.append(line);
					line = reader.readLine();
					buff.append("\r\n");
				}
				return buff.toString();
			}
		} catch (final IOException e) {
			throw WrappedException.wrap(e, "Error when reading file : '{0}'", url);
		}
	}
}
