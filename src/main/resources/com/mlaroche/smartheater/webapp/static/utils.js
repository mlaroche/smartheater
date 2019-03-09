function convertToLocalTime(timeAsString) {
	var timeArray  = timeAsString.split(':');
	return  {
			 "hour" : parseInt(timeArray[0]),
			 "minute" : parseInt(timeArray[1]),
			 "second" : 0,
			 "nano" : 0
	 }
}
		
function convertToWeeklyCalendarJsonValue (jqueryCalendar) {
	var dailyCalendars = {};
	jqueryCalendar.forEach( function(wk) {
		 dailyCalendar = {}
		 var days = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
		 day = days[wk.day];
		 dailyCalendar.timeSlots = [];
		 wk.periods.forEach(function(period) {
			 var timeSlot = {}
			 timeSlot.begin = convertToLocalTime(period.start);
			 timeSlot.end = convertToLocalTime(period.end);
			 timeSlot.mode = period.title;
			 dailyCalendar.timeSlots.push(timeSlot);
		 });
		 dailyCalendars[day] = dailyCalendar;
	  });
	var objectToReturn = {"dailyCalendars" : dailyCalendars};
	return JSON.stringify(objectToReturn);
}
		
function convertToJqueryCalendar (weeklyCalendar) {
	var jQueryCalendar = [];
	 var days = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
	 for (var i = 0; i < 7 ; i++) {
		 dailyCalendar = {}
		 dailyCalendar.day = i;
		 dailyCalendar.periods = [];
		 weeklyCalendar.dailyCalendars[days[i]].timeSlots.forEach(function(timeSlot) {
			 var period = {
				"start" : pad2(timeSlot.begin.hour) + ':' + pad2(timeSlot.begin.minute),
				"end" : pad2(timeSlot.end.hour) + ':' + pad2(timeSlot.end.minute),
				"title" : timeSlot.mode
			 }
			 dailyCalendar.periods.push(period);
		 });
		 jQueryCalendar.push(dailyCalendar);
	}
	return jQueryCalendar;
}
		
function pad2(number) {
     return (number < 10 ? '0' : '') + number
}


function heaterModeLabel(mode) {
    switch (mode) {
		case "arret":
			return "Arrêt"
		case "confort":
			return "Confort"
		case "eco":
			return "Eco"
		case "horsgel":
			return "Hors-gel"
		}
  }