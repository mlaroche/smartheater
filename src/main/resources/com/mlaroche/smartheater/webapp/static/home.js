const Home = {
  name : "home",
  template: `
  		<q-page padding>
  			<div class="row">
  				<div class="col-xl-4 col-md-6 col-sm-12 col-xs-12 ">
  					<q-card class="q-ma-sm">
  						<q-card-title>
							<div class="row items-end" ><span class="q-display-1 text-primary" >{{summary.heaterCount}}</span><span>radiateur géré</span></div>
						</q-card-title>
						<q-card-separator></q-card-separator>
  						<q-card-main>
  							<dl class="horizontal">
  							  <div v-for="countByMode in countsByMode" >
								  <dt>{{modeLabel(countByMode.mode)}}</dt>
								  <dd>{{countByMode.count}}</dd>
							  </div>
							</dl>
  						</q-card-main>
  					</q-card>
  				</div>
  				<div class="col-xl-4 col-md-6 col-sm-12 col-xs-12 ">
  					<q-card class="q-ma-sm">
  						<q-card-title>
							Température moyenne
						</q-card-title>
						<q-card-separator></q-card-separator>
  						<q-card-main>
  							<span class="q-display-1 text-primary" >{{summary.meanTemperature}}°C</span>
  						</q-card-main>
  					</q-card>
  				</div>
  				<div class="col-xl-4 col-md-6 col-sm-12 col-xs-12 ">
  				<q-card class="q-ma-sm">
  						<q-card-title>Météo exterieure</q-card-title>
  						<q-card-separator></q-card-separator>
  						<q-card-main>
							<div class="row items-center">
								<div class="col-4">
									<div class="row justify-end"><q-icon size="6em" :name="'fas '+getIconName(weatherInfo.icon)" ></q-icon></div>
								</div>
								<div class="col-8">
									<div class="row justify-center q-display-1 text-primary" >{{weatherInfo.temperature}}°C</div>
									<div class="row justify-center q-headline text-weight-light">{{weatherInfo.humidity}}% d'humidité</span></div>
									<div class="row justify-center q-headline text-weight-light" >{{weatherInfo.description}}</div>
									<div class="row justify-center q-headline text-weight-light text-italic" >{{weatherInfo.location}}</div>
								</div>
							</div>
						</q-card-main>
  					</q-card>
  				</div>
  			</div>
		</q-page>
	 `,
  created () {
    this.fetchData()
  },
  watch: {
    // call again the method if the route changes
    '$route': 'fetchData'
  },
  methods: {
	  fetchData : function () {
		  this.loadWeatherInfo();
		  this.loadCountsByMode();
	  },
	  loadWeatherInfo : function () {
		  this.$http.get("api/weather/info").then( function (response) { 
			  this.$data.weatherInfo = response.body
		  });
	  },
	  loadCountsByMode : function () {
		  this.$http.get("api/heaters/countByMode").then( function (response) { 
			  this.$data.countsByMode = response.body
		  });
	  },
	  modeLabel: function (mode) {
	      return heaterModeLabel(mode)
	  },
	  getIconName : function (icon) {
		  switch (icon) {
		  	case "01n":
		  		return "fa-moon";
		  	case "02n":
		  		return "fa-cloud-moon";
		  	case "03n":
		  		return "fa-cloud";
		  	case "04n":
		  		return "fa-cloud";
		  	case "09n":
		  		return "fa-cloud-moon-rain";
		  	case "10n":
		  		return "fa-cloud-showers-heavy";
		  	case "11n":
		  		return "fa-bolt";
		  	case "13n":
		  		return "fa-snowflake";
		  	case "50n":
		  		return "fa-cloud";
		  	case "01d":
		  		return "fa-sun";
		  	case "02d":
		  		return "fa-cloud-sun";
		  	case "03d":
		  		return "fa-cloud";
		  	case "04d":
		  		return "fa-cloud";
		  	case "09d":
		  		return "fa-cloud-sun-rain";
		  	case "10d":
		  		return "fa-cloud-showers-heavy";
		  	case "11d":
		  		return "fa-bolt";
		  	case "13d":
		  		return "fa-snowflake";
		  	case "50d":
		  		return "fa-cloud";
		}
		  
	  }
  },
  data: function () {
	  return {
		  weatherInfo : {},
		  summary: {
			  heaterCount : 1,
			  meanTemperature : 22
		  },
		  countsByMode : []
  	  }
  }
}
