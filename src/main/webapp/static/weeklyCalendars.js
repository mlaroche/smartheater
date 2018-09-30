const WeeklyCalendarEdit = {
			  name : "weekly-calendar-edit",
			  template: `
			  		<div>
			  			<weekly-calendar-detail v-bind:weeklyCalendar="weeklyCalendar" v-bind:is-edit="false" >
						</weekly-calendar-detail>
					</div>
				 `,
			  beforeRouteEnter (to, from, next) {
				 $.get("api/weeklyCalendars/"+ to.params.id, data => {
			      next(vm => vm.setWeeklyCalendar(data))
			    })
			  },
			  beforeRouteUpdate (to, from, next) {
				  $.get("api/weeklyCalendars/"+ to.params.id, data => {
				      this.setWeeklyCalendar(data)
				    })
			  },
			  methods: {
				  setWeeklyCalendar : function (data) {
					  this.$data.weeklyCalendar  = data
				  }
			  },
			  data: function () {
				  return {
			  		  weeklyCalendar:  {}
			  	  }
			  }
			}
	
	const WeeklyCalendarCreate = {
			  name : "weekly-calendar-create",
			  template: `
			  		<div>
			  			<weekly-calendar-detail v-bind:weeklyCalendar="weeklyCalendar" v-bind:is-edit="true" >
						</weekly-calendar-detail>
					</div>
				 `,
			  data: function () {
				  return {
			  		  weeklyCalendar:  {}
			  	  }
			  }
			}
		
	
		const WeeklyCalendarDetailView = {
		  name : "weekly-calendar-detail",
		  template: `
		  	<div>
			  <form>
				  <div class="form-group row">
				  <label for="name" class="col-sm-2 col-form-label">Nom</label>
				    <div class="col-sm-10">
				    	<input v-if="edition" v-model:value="weeklyCalendar.name"
				                class="form-control" 
				                name="name" 
				                placeholder="">
				    	<input v-else type="text" readonly class="form-control-plaintext" v-bind:value="weeklyCalendar.name">
				    </div>
				  </div>
				</form>
			  <div id="schedule"></div>
			  <button  v-if="edition" v-on:click="save">Sauvegarder</button>
			  <button  v-else v-on:click="toogleEdit">Modifier</button>
		  	</div>	
			 `,
		  props: {
			  weeklyCalendar: Object,
			  isEdit: Boolean
		  },
		  mounted: function () {
			  this.$nextTick(function () {
				  $('#schedule').jqs();
			  });
		  },
		  beforeUpdate: function () {
			  this.$nextTick(function () {
				if(this.$props.weeklyCalendar.jsonValue) {
					$('#schedule').after('<div id="schedule"></div>');
					$('#schedule')[0].remove();
					$('#schedule').jqs({
						mode : this.edition ? 'edit' : 'read'
					});
				  	$('#schedule').jqs('import', convertToJqueryCalendar(JSON.parse(this.$props.weeklyCalendar.jsonValue)));
				}
			  });
		  },
		  computed: {
		    // a computed getter
		    edition: function () {
		      return this.$props.isEdit || this.$data.edit
		    }
		  },
		  methods: {
			  save : function () {
				  var jqueryWeeklyCalendar = JSON.parse($('#schedule').jqs('export'));
				  var weeklyCalendarJsonValue = convertToWeeklyCalendarJsonValue(jqueryWeeklyCalendar);
				  var weeklyCalendarToSave = {
						"wcaId" : this.$props.weeklyCalendar.wcaId,
						"jsonValue" : weeklyCalendarJsonValue,
						"name" : this.$props.weeklyCalendar.name
						
				  }
				  
				console.log(weeklyCalendarToSave);
				  //translate the format and then save the calendar
				  $.post("api/weeklyCalendars/", JSON.stringify(weeklyCalendarToSave) , function( data, textStatus, jQxhr ){
					  router.push({ path: '/weeklyCalendars/'});
				  }, 'json');
				  
			  },
			  toogleEdit : function () {
				  this.$data.edit = !this.$data.edit;
			  }
		  },
		  data: function () {
			  return {
		  		  edit: false
		  	  }
		  }
		}
		
		const WeeklyCalendarList = {
				  template: `
				  	<div>
						<table class="table">
						    <thead>
						      <tr>
						        <th scope="col">Id</th>
						        <th scope="col">Nom</th>
						      </tr>
						    </thead>
							<tbody>
								<tr v-for="weeklyCalendar in weeklyCalendars">
									<td>
									 	<router-link :to="{ path: '/weeklyCalendars/'+ weeklyCalendar.wcaId}">{{ weeklyCalendar.wcaId}}</router-link>
									</td>
									<td>
										{{ weeklyCalendar.name }}
									</td>
								</tr>
							</tbody>
						</table>
						<router-link :to="{ path: '/weeklyCalendars/new'}">Nouveau</router-link>
				  	</div>	
					 `,
				  beforeRouteEnter (to, from, next) {
					 $.get("api/weeklyCalendars", data => {
				      next(vm => vm.setWeeklyCalendars(data))
				    })
				  },
				  methods: {
					  setWeeklyCalendars : function (data) {
						  this.$data.weeklyCalendars  = data
					  }
				  },
				  data: function () {
					  return {
				  		  weeklyCalendars:  []
				  	  }
				  }
				}