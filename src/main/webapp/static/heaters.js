const HeaterCreate = {
			  name : "heater-create",
			  template: `
			  		<q-page padding>
			  			<heater-detail v-bind:heater="heater" v-bind:is-edit="true" >
						</heater-detail>
					</q-page>
				 `,
			  data: function () {
				  return {
					  heater:  {}
			  	  }
			  }
			}
	
const HeaterEdit = {
	  name : "heater-edit",
	  template: `
	  		<q-page padding>
	  			<heater-detail v-bind:heater="heater"  v-bind:is-edit="false" >
				</heater-detail>
				<q-page-sticky position="bottom-right" :offset="[18, 18]">
					  <q-fab color="purple" icon="keyboard_arrow_up" direction="up" >
					  <q-tooltip slot="tooltip"  anchor="center left" self="center right" :offset="[20, 0]" >Modifier le mode</q-tooltip>
					  	<q-fab-action  color="primary" @click="changeMode('arret')" icon="fas fa-ban" >
					  		<q-tooltip anchor="center left" self="center right" :offset="[20, 0]">Arrêter</q-tooltip>
					  	</q-fab-action>
					  	<q-fab-action  color="primary" @click="changeMode('confort')" icon="fas fa-fire" >
					  		<q-tooltip anchor="center left" self="center right" :offset="[20, 0]">Passer en mode confort</q-tooltip>
					  	</q-fab-action>
					  	<q-fab-action  color="primary" @click="changeMode('confort')" icon="fas fa-leaf" >
					  		<q-tooltip anchor="center left" self="center right" :offset="[20, 0]">Passer en mode éco</q-tooltip>
					  	</q-fab-action>
					  	<q-fab-action  color="primary" @click="changeMode('confort')" icon="fas fa-leaf" >
					  		<q-tooltip anchor="center left" self="center right" :offset="[20, 0]">Passer en mode hors-gel</q-tooltip>
					  	</q-fab-action>
					  </q-fab>
				  </q-page-sticky>
			</q-page>
		 `,
	  beforeRouteEnter (to, from, next) {
		 Vue.http.get("api/heaters/"+ to.params.id).then( function (response) { 
	      next(vm => vm.setHeater(response.body))
	    })
	  },
	  beforeRouteUpdate (to, from, next) {
		  Vue.http.get("api/heaters/"+ to.params.id).then( function (response) { 
		      this.setHeater(response.body);
		    })
	  },
	  methods: {
		  setHeater : function (data) {
			  this.$data.heater  = data
		  },
		  changeMode : function (mode) {
			  this.$http.post("api/heaters/" + this.$props.heater.heaId +"/_changeMode", null ,{params:  {mode: mode}}).then( function (response) { 
				  //nothing
			  }, 'json');
			  
		  }
	  },
	  data: function () {
		  return {
	  		  heater:  {}
	  	  }
	  }
}
	
	
const HeaterDetailView = {
	  name : "heater-detail",
	  template: `
	  	<div>
		  <q-card>
		  	<q-card-title>Informations générales</q-card-title>
		  	<q-card-separator></q-card-separator>
		  	<q-card-main>
				  <section >
				  	<q-field label="Nom" >
			            <q-input v-if="edition" v-model="heater.name"></q-input>
			            <span v-else>{{heater.name}}</span>
			        </q-field>
			        <q-field label="Nom DNS ou IP" >
			            <q-input v-if="edition" v-model="heater.dnsName"></q-input>
			            <span v-else>{{heater.dnsName}}</span>
			        </q-field>
			        <q-field label="Calendrier" >
			            <q-select v-if="edition" v-model="heater.wcaId" :options="calendarsSelect"></q-select>
			            <span v-else>{{calendarLabel}}</span>
			        </q-field>
			         <q-field label="Protocol" >
			            <q-select v-if="edition" v-model="heater.proCd" :options="protocolsSelect"></q-select>
			            <span v-else>{{protocolLabel}}</span>
			        </q-field>
				  </section>
			</q-card-main>
		  	<q-card-separator></q-card-separator>
		  	<q-card-actions>
			  <q-btn  v-if="edition" @click="save">Sauvegarder</q-btn>
			  <q-btn  v-else @click="toogleEdit">Modifier</q-btn>
			</q-card-actions>
		  </q-card>
	  	</div>	
		 `,
	  props: {
		  heater: Object,
		  isEdit: Boolean
	  },
	  computed: {
	    // a computed getter
	    edition: function () {
	      return this.$props.isEdit || this.$data.edit
	    },
		calendarLabel: function () {
		     return this.calendars[0] ? this.calendars[0].name : ''
		},
		protocolLabel: function () {
		     return this.protocols[0] ? this.protocols[0].label : ''
		},
		calendarsSelect  : function (){
			return this.calendars.map(function (calendar) {
				return { label : calendar.name, value : calendar.wcaId }
			})
		},
		protocolsSelect  : function (){
			return this.protocols.map(function (protocol) {
				return { label : protocol.label, value : protocol.proCd }
			})
		}  
	  },
	  beforeMount : function () {
	      this.loadCalendars();
	      this.loadProtocols();

	  },
	  methods: {
		  save : function () {
			  this.$http.post("api/heaters/", JSON.stringify(this.$props.heater)).then( function (response) { 
				  //router.push({ path: '/heaters/'});
				  this.toogleEdit();
			  }, 'json');
			  
		  },
		  toogleEdit : function () {
			  this.$data.edit = !this.$data.edit;
			  this.loadCalendars();
			  this.loadProtocols();
		  },
		  loadCalendars : function () {
			  if(this.edition) {
				  this.$http.get("api/weeklyCalendars").then( function (response) { 
						if(!!response.body) {
							this.$data.calendars = response.body;
						}
					});
			  } else {
				  if(this.$props.heater.wcaId) {
					  this.$http.get("api/weeklyCalendars/"+ this.$props.heater.wcaId).then( function (response) { 
							if(!!response.body) {
								this.$data.calendars = [];
								this.$data.calendars.push(response.body);
							}
					}); 
				  }
			  }
		  }, 
		  loadProtocols : function () {
			  if(this.edition) {
				  this.$http.get("api/protocols").then( function (response) { 
						if(!!response.body) {
							this.$data.protocols = response.body;
						}
					});
			  } else {
				  if(this.$props.heater.wcaId) {
					  this.$http.get("api/protocols/"+ this.$props.heater.proCd).then( function (response) { 
							if(!!response.body) {
								this.$data.protocols = [];
								this.$data.protocols.push(response.body);
							}
					}); 
				  }
			  }
		  }
	  },
	  watch : {
		  heater : function (newHeater, oldHeater) {
		      this.loadCalendars();
		      this.loadProtocols();

		  }
	  },
	  data: function () {
		  return {
	  		  edit: false,
	  		  calendars: [],
	  		  protocols: []
	  	  }
	  }
}
		
const HeaterList = {
	  template: `
	  	<q-page padding>
	  		<q-card v-for="heater in heaters" :key="heater.heaId" inline class="standard q-ma-sm" >
				<q-card-title>
					<router-link :to="{ path: '/heaters/'+ heater.heaId}">{{ heater.name}}</router-link>
				</q-card-title>
				<q-card-separator />
				<q-card-main>
					{{ heater.dnsName}}
				</q-card-main>
			</q-card>
			<q-page-sticky position="bottom-right" :offset="[18, 18]">
		  		<q-btn round color="primary" icon="add" :to="{ path: '/heaters/new'}" ></q-btn>
		  	</q-page-sticky>
	  	</q-page>	
		 `,
	  beforeRouteEnter (to, from, next) {
		Vue.http.get("api/heaters").then( function (response) { 
	      next(vm => vm.setHeaters(response.body))
	    })
	  },
	  methods: {
		  setHeaters : function (data) {
			  this.$data.heaters  = data
		  }
	  },
	  data: function () {
		  return {
	  		  heaters:  []
	  	  }
	  }
}