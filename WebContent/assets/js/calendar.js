	var AppCalendar = function() {
	    
	    return {
	        //  main function to initiate the module
	        init: function() {
	            this.initCalendar();
	        },
	        
	        initCalendar: function() {

	            if (!jQuery().fullCalendar) {
	                return;
	            }

	            var date = new Date();
	            var d = date.getDate();
	            var m = date.getMonth();
	            var y = date.getFullYear();

	            var h = {};
	           
	            if (App.isRTL()) {
	                if ($('#calendar').parents(".portlet").width() <= 720) {
	                    $('#calendar').addClass("mobile");
	                    h = {
	                        right: 'title, prev, next',
	                        center: '',
	                        left: 'agendaDay, agendaWeek, month, today',
	                        
	                        	
	                    };
	                } else {
	                    $('#calendar').removeClass("mobile");
	                    h = {
	                        right: 'title',
	                        center: '',
	                        left: 'agendaDay, agendaWeek, month, today, prev,next'
	                    };
	                }
	            } else {
	                if ($('#calendar').parents(".portlet").width() <= 720) {
	                    $('#calendar').addClass("mobile");
	                    h = {
	                        left: 'title, prev, next',
	                        center: '',
	                        right: 'today,month,agendaWeek,agendaDay',
	                       
	                    };
	                } else {
	                    $('#calendar').removeClass("mobile");
	                    h = {
	                        left: 'title',
	                        center: '',
	                        right: 'prev,next,today,month,agendaWeek,agendaDay'
	                    };
	                }
	            }
	           
	            var initDrag = function(el) {
	                // create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
	                // it doesn't need to have a start or end
	                var eventObject = {
	                    title: $.trim(el.text()) // use the element's text as the event title
	                };
	                // store the Event Object in the DOM element so we can get to it later
	                el.data('eventObject', eventObject);
	                // make the event draggable using jQuery UI
	                el.draggable({
	                    zIndex: 999,
	                    revert: true, // will cause the event to go back to its
	                    revertDuration: 0 //  original position after the drag
	                });
	            };

	            var addEvent = function(title) {
	                title = title.length === 0 ? "Untitled Event" : title;
	                var html = $('<div class="external-event label label-default col-xs-12">' + title + '</div>');
	                jQuery('#event_box').append(html);
	                initDrag(html);
	            };

	            $('#external-events div.external-event').each(function() {
	                initDrag($(this));
	            });

	            $('#event_add').unbind('click').click(function() {
	                var title = $('#event_title').val();
	                addEvent(title);
	            });

	            $('#calendar').fullCalendar('destroy'); // destroy the calendar
	            $('#calendar').fullCalendar({ //re-initialize the calendar
	                header: h,
	                defaultView: 'agendaDay', // change default view with available options from http://arshaw.com/fullcalendar/docs/views/Available_Views/ 
	                slotMinutes: 15,
	                editable: true,
	                eventSources: [
	                               {
	                                   url: 'activity-events.jsp',
	                   				textColor: 'white'
	                               }
	                           ],
	                droppable: true, // this allows things to be dropped onto the calendar !!!
	                drop: function(date, allDay) { // this function is called when something is dropped

	                    // retrieve the dropped element's stored Event Object
	                    var originalEventObject = $(this).data('eventObject');
	                    // we need to copy it, so that multiple events don't have a reference to the same object
	                    var copiedEventObject = $.extend({}, originalEventObject);

	                    // assign it the date that was reported
	                    copiedEventObject.start = date;
	                    copiedEventObject.allDay = allDay;
	                    copiedEventObject.className = $(this).attr("data-class");

	                    // render the event on the calendar
	                    // the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
	                    $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

	                    // is the "remove after drop" checkbox checked?
	                    if ($('#drop-remove').is(':checked')) {
	                        // if so, remove the element from the "Draggable Events" list
	                        $(this).remove();
	                    }
	                },
				loading: function(bool) {
					if (bool) $('#loading').show();
					else $('#loading').hide();
				},
				
				eventRender: function (event, element, view) {
					if(view.name == "agendaDay"){
						element.find('.fc-event-time').append("  " + event.title);
					}
					
	       		element.qtip({    
	           		content: {    
	               		title: { 
								text: ' ' + ($.fullCalendar.formatDate(event.start, 'dd/MM/yyyy hh:mmtt')) + ' - ' + ($.fullCalendar.formatDate(event.end, 'dd/MM/yyyy hh:mmtt')) + '<br>' + event.title
							}, 						
	               		text: event.description					      
	           		},
						
						position: {
							my: 'top left',
							target: 'mouse',
							viewport: $(window),
							adjust: {
								x:10, y:10
							}
						},
						
						effect: false,
		
						show: {
							solo: true,
							delay: 1
						},
				
	           		style: { 
	               		width: 250,
	               		padding: 5,
	               		color: 'black',
							background: '#ffc',
	               		textAlign: 'left',
							fontSize: 10,
							//fontFamily: 'arial',
					
	           			border: {
	               			width: 1,
	               			radius: 1,
								color: '#09F'
	           			},
							
							title: {
								background: '#fcc',
								fontSize: 10
							},
							
							//tip: true,
							
							hide: {
								fixed: true
							},
							
							classes: {
								tooltip: 'ui-widget',
								tip: 'ui-widget',
								title: 'ui-widget-header',
								content: 'ui-widget-content'
							}
	           		} 
	       		});
	   		} 
	            });

	        }

	    };

	}();

	jQuery(document).ready(function() {    
	   AppCalendar.init(); 
	});