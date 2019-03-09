const WeeklyCalendarEdit = {
	  name : "weekly-calendar-edit",
	  template: `
	  		<q-page padding>
	  			<weekly-calendar-detail v-bind:weeklyCalendar="weeklyCalendar" v-bind:is-edit="false" >
				</weekly-calendar-detail>
			</q-page>
		 `,
	  beforeRouteEnter (to, from, next) {
		Vue.http.get("api/weeklyCalendars/"+ to.params.id).then( function (response) { 
	      next(vm => vm.setWeeklyCalendar(response.body))
	    })
	  },
	  beforeRouteUpdate (to, from, next) {
		  Vue.http.get("api/weeklyCalendars/"+ to.params.id).then( function (response) { 
		      this.setWeeklyCalendar(response.body)
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
	  		<q-page padding>
	  			<weekly-calendar-detail v-bind:weeklyCalendar="weeklyCalendar" v-bind:is-edit="true" >
				</weekly-calendar-detail>
			</q-page>
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
		  <section >
		  	<q-field label="Nom" orientation="vertical">
	            <q-input v-if="edition" v-model="weeklyCalendar.name"></q-input>
	            <span v-else>{{weeklyCalendar.name}}</span>
	        </q-field>
		  </section>
		  <div id="schedule"></div>
		  <q-btn  v-if="edition" @click="save">Sauvegarder</q-btn>
		  <q-btn  v-else @click="toogleEdit">Modifier</q-btn>
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
			  //translate the format and then save the calendar
			  var weeklyCalendarJsonValue = convertToWeeklyCalendarJsonValue(jqueryWeeklyCalendar);
			  var weeklyCalendarToSave = {
					"wcaId" : this.$props.weeklyCalendar.wcaId,
					"jsonValue" : weeklyCalendarJsonValue,
					"name" : this.$props.weeklyCalendar.name
					
			  }
			  this.$http.post("api/weeklyCalendars/", JSON.stringify(weeklyCalendarToSave)).then( function (response) { 
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
	  	<q-page padding>
			<q-card v-for="weeklyCalendar in weeklyCalendars" :key="weeklyCalendar.wcaId" class="standard q-ma-sm">
				<q-card-title>
					<router-link :to="{ path: '/weeklyCalendars/'+ weeklyCalendar.wcaId}">{{ weeklyCalendar.name}}</router-link>
				</q-card-title>
				<q-card-separator />
				<q-card-main>
					{{ weeklyCalendar.name}}
				</q-card-main>
			</q-card>
			<q-btn :to="{ path: '/weeklyCalendars/new'}">Nouveau</q-btn>
	  	</q-page>	
		 `,
	  beforeRouteEnter (to, from, next) {
		 Vue.http.get("api/weeklyCalendars").then( function (response) { 
	      next(vm => vm.setWeeklyCalendars(response.body))
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