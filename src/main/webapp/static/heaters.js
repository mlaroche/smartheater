const HeaterCreate = {
			  name : "heater-create",
			  template: `
			  		<div>
			  			<heater-detail v-bind:heater="heater" v-bind:is-edit="true" >
						</heater-detail>
					</div>
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
			  		<div>
			  			<heater-detail v-bind:heater="heater"  v-bind:is-edit="false" >
						</heater-detail>
					</div>
				 `,
			  beforeRouteEnter (to, from, next) {
				 $.get("api/heaters/"+ to.params.id, data => {
			      next(vm => vm.setHeater(data))
			    })
			  },
			  beforeRouteUpdate (to, from, next) {
				  $.get("api/heaters/"+ to.params.id, data => {
				      this.setHeater(data);
				    })
			  },
			  methods: {
				  setHeater : function (data) {
					  this.$data.heater  = data
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
				  <form>
					  <div class="form-group row">
					  <label for="name" class="col-sm-2 col-form-label">Nom</label>
					    <div class="col-sm-10">
					    	<input v-if="edition" v-model:value="heater.name"
					                class="form-control" 
					                name="name" 
					                placeholder="">
					    	<input v-else type="text" readonly class="form-control-plaintext" v-bind:value="heater.name">
					    </div>
					    <label for="dnsName" class="col-sm-2 col-form-label">Nom DNS ou IP</label>
					    <div class="col-sm-10">
					    	<input v-if="edition" v-model:value="heater.dnsName"
					                class="form-control" 
					                name="dnsName" 
					                placeholder="">
					    	<input v-else type="text" readonly class="form-control-plaintext" v-bind:value="heater.dnsName">
					    </div>
					    <label for="wcaId" class="col-sm-2 col-form-label">Calendrier</label>
					    <div class="col-sm-10">
					    	<select v-if="edition" v-model="heater.wcaId" name="wcaId">
					    	  <option v-for="calendar in calendars" v-bind:value="calendar.wcaId">
					    	    {{ calendar.name }}
					    	  </option>
					    	</select>
					    	<input v-else type="text" readonly class="form-control-plaintext"  v-bind:value="calendarLabel" >
					    </div>
					    <label for="proCd" class="col-sm-2 col-form-label">Protocol</label>
					    <div class="col-sm-10">
					    	<select v-if="edition" v-model="heater.proCd" name="proCd">
					    	  <option v-for="protocol in protocols" v-bind:value="protocol.proCd">
					    	    {{ protocol.label }}
					    	  </option>
					    	</select>
					    	<input v-else type="text" readonly class="form-control-plaintext" v-bind:value="protocolLabel" >
					    </div>
					  </div>
					</form>
				  <button  v-if="edition" v-on:click="save">Sauvegarder</button>
				  <button  v-else v-on:click="toogleEdit">Modifier</button>
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
				}
			  },
			  beforeMount : function () {
			      this.loadCalendars();
			      this.loadProtocols();

			  },
			  methods: {
				  save : function () {
					  $.post("api/heaters/", JSON.stringify(this.$props.heater) , function( data, textStatus, jQxhr ){
						  router.push({ path: '/heaters/'});
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
			  	<div>
					<table class="table">
					    <thead>
					      <tr>
					        <th scope="col">Id</th>
					        <th scope="col">Nom</th>
					        <th scope="col">Nom DNS</th>
					      </tr>
					    </thead>
						<tbody>
							<tr v-for="heater in heaters">
								<td>
								 	<router-link :to="{ path: '/heaters/'+ heater.heaId}">{{ heater.heaId}}</router-link>
								</td>
								<td>
									{{ heater.name }}
								</td>
								<td>
									{{ heater.dnsName }}
								</td>
							</tr>
						</tbody>
					</table>
					<router-link :to="{ path: '/heaters/new'}">Nouveau</router-link>
			  	</div>	
				 `,
			  beforeRouteEnter (to, from, next) {
				 $.get("api/heaters", data => {
			      next(vm => vm.setHeaters(data))
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