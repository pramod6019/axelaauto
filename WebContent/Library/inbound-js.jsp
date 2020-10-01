<script language="JavaScript" type="text/javascript">
	function check(type) {
		var contact_id = $("#contact_id").val();
		var customer_id = $("#customer_id").val();
		var call_id = $("#call_id").val();
		var call_no = $("#call_no").val();
		var Hint = 'addenquiry';
		
		console.log("contact_id========"+contact_id);
		
		if(type == 'enquiry') {
			url = '../inbound/enquiry-quickadd.jsp?contact_id='+contact_id;
		} else if(type == 'preowned') {
			url = '../inbound/preowned-quickadd.jsp?contact_id='+contact_id;
		} else if(type == 'service') {
			url = '../inbound/booking-enquiry.jsp?contact_id='+contact_id;
		} else if(type == 'insurance') {
			url = '../portal/ecover-signin.jsp?contact_id='+contact_id;
		} else if(type == 'ticket') {
			url = '../inbound/ticket-add.jsp?add=yes&contact_id='+contact_id;
		} else if(type == 'button') {
			url = '../inbound/enquiry-quickadd.jsp?contact_id='+contact_id;
		} else if(type == 'callback') {
			url = '../inbound/branch.jsp?call_id='+call_id+'&customer_id='+customer_id+'&contact_id='+contact_id;
		} else if(type == 'canned') {
			url = '../inbound/canned.jsp?canned=yes&home=yes&call_no='+call_no+'&contact_id='+contact_id;
		}
		showHintUrl(url, Hint);
	}
	
	 function showHintUrl(url, Hint) {
		 console.log("url======"+url);
				$('#'+Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
					$.ajax({
						url: url,
						type: 'GET',
						success: function (data){
							console.log("data======"+data);
								if(data.trim() != 'SignIn'){
								$('#'+Hint).show();
								$('#'+Hint).fadeIn(500).html('' + data.trim() + '');
								FormElements();
								$('.datetimepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY HH:mm',switchOnClick : true, clearButton : true });
								$('.datepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY',switchOnClick : true,weekStart : 0, time: false, clearButton : true  });
								} else{
								window.location.href = "../portal/";
								}
						}
					});
			}
	
	function submitB(page, Hint) {
		var formdata = $('#frmenquiry').serialize();
		var url = '../inbound/inbound-check.jsp?page='+page+'&'+formdata;
			console.log(url);
		var Hint = Hint;
		$.ajax({
			url: url,
			type: 'GET',
			success: function (data){
					console.log("data======"+data);
				if(data.includes("Error"))
				{		
					$("#errorMsg").html(data);
				}
				else if(data.includes("success"))
				{	
					$('#'+ Hint).html(data);
				}	
			}
		});
	}
	
	function branchUpdate(page) {
		var branch_id = document.getElementById('dr_branch_id').value;
		var contact_id = $("#contact_id").val();
		var url = '';
		var hint = '';
		if(page == 'enquiry') { 
		 url = '../inbound/enquiry-quickadd.jsp?dr_branch_id='+branch_id+'&contact_id='+contact_id;
		 hint = 'addenquiry';
		} else if(page == 'preowned') {
			url = '../inbound/preowned-quickadd.jsp?dr_branch_id='+branch_id+'&contact_id='+contact_id;
			hint = 'addenquiry';
		}
		 $.ajax({
			url: url,
			type: 'GET',
			success: function (data){
				$('#'+hint).html(data);
				FormElements();
			}
		});
	}
</script>
	
<script language="JavaScript" type="text/javascript">
// start of script of Enquiry_Quickadd
	        function FormFocus() { 
				document.frmenquiry.txt_customer_name.focus();   			
	        }
					      
	        function PopulateExecutive() {	
	            var enquiry_branch_id = document.getElementById('txt_branch_id').value;	
				var team_id = document.getElementById('dr_enquiry_team').value;
				showHint('../sales/enquiry-check.jsp?executive=yes&active=1&team_id=' + team_id+'&enquiry_branch_id='+enquiry_branch_id,'teamexe');
			}	  
			  		  
			function populateItem() {
				var enquiry_model_id = document.getElementById('dr_enquiry_model_id').value;
				showHint('../sales/enquiry-check.jsp?enquiry=yes&item=yes&enquiry_model_id='+enquiry_model_id, 'modelitem');
			}
			
			function populateSob() {
				var enquiry_soe_id = document.getElementById('dr_enquiry_soe_id').value;
				showHint('../sales/enquiry-check.jsp?dr_enquiry_sob_id=yes&enquiry_soe_id='+enquiry_soe_id, 'dr_enquiry_sob_id');
				  
			}
		
			function DisplayModel() {
				var str1=document.getElementById('dr_enquiry_enquirytype_id').value;	
			
				if(str1=="1") {
					displayRow('model');
					displayRow('package');
				} else{
					hideRow('model');
					hideRow('package');
				}
			}
		
			$(document).ready(function() {
				$('#txt_contact_email1, #txt_contact_mobile1, #txt_contact_mobile2, #txt_contact_phone1, #txt_contact_phone2').focusout(function(){
					setTimeout(function(){
						$(".hint").hide();
					},100);
					
				});
			});
// end of script of Enquiry_Quickadd
</script>

<script language="JavaScript" type="text/javascript">
// Start of script of preowned_Quickadd
		function yearPicker() {
			$('#txt_preowned_regdyear').datepicker({
			   showButtonPanel: true,
			    changeMonth: true,
			    changeYear: true,
			    dateFormat: 'MM yy',
			    onClose: function() {
			       var iMonth = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
			       var iYear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
			       $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
			    },
			
			    beforeShow: function() {
			      if ((selDate = $(this).val()).length > 0) 
			      {
			         iYear = selDate.substring(selDate.length - 4, selDate.length);
			         iMonth = jQuery.inArray(selDate.substring(0, selDate.length - 5), 
			                  $(this).datepicker('option', 'monthNames'));
			         $(this).datepicker('option', 'defaultDate', new Date(iYear, iMonth, 1));
			         $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
			      }
			 }
			   }).focus(function(){
			   $(".ui-datepicker-calendar").hide();
			  });   
		}
	
       function PopulateExecutive(){
		var preowned_branch_id = document.getElementById('txt_branch_id').value;
		var team_id = document.getElementById('dr_preowned_team').value;
		showHint('../preowned/preowned-check.jsp?preexecutive=yes&team_id=' + team_id+'&preowned_branch_id='+preowned_branch_id,'teamexe');
	  }	
       
       function ShowNameHint(){
    	    var fname = document.getElementById("txt_contact_fname").value;
    	    var lname = document.getElementById("txt_contact_lname").value;
    	    showHint('../service/report-check.jsp?contact_fname='+fname+'&contact_lname='+lname,'jobcard_details');
    	}
// end of script of preowned_Quickadd
</script>

<script language="JavaScript" type="text/javascript">
// Start of script of service booking_enquiry	
        function PopulateModel(branch_id){
        	showHint("../service/booking-enquiry-check.jsp?veh_branch_id="+branch_id+"&model=yes", "Hintmodel");
        }
         function PopulateItem(model_id){
        	showHint("../service/booking-enquiry-check.jsp?item_model_id="+model_id+"&list_model_item=yes","model-item");
        }
         function PopulateCampaign(branch_id){
 			showHint("../service/booking-enquiry-check.jsp?veh_branch_id="+branch_id+"&campaign=yes", "campaign");
 		  }
	function populateSobBooking(){
		 var booking_soe_id = document.getElementById('dr_booking_soe_id').value;
		 showHint('../service/booking-enquiry-check.jsp?dr_booking_sob_id=yes&booking_soe_id='+booking_soe_id, 'HintSob');
	}
	
    function PopulateBookingTime(){
    	var pickup=$("#dr_booking_type").val();
    	if(pickup!='0' ){
    		$("#followuptime").hide();
    		$("#bookingtimeband").show();
    	}
    	else{
    		$("#followuptime").show();
    		$("#bookingtimeband").hide();
    	}
    }
 // end of script of service booking_enquiry
</script>

<script type="text/javascript">
// Start of script of ticket_add
	 function populateCatogery(){
		var  department_id= $('#dr_ticket_dept_id').val();
		  showHint('../service/report-check.jsp?ticket_category=ticket_add&ticket_dept_id='+department_id, 'categoryHint');
	} 
	// end of script of ticket_add
</script>

<script language="JavaScript" type="text/javascript">	
// Start of script of Search Employees branch.jsp
	function BranchCheck(name,obj,hint) {
			var value = document.getElementById("txt_search").value;
			var type_id = document.getElementById("dr_type_id").value;
            var url = "../inbound/inbound-check.jsp?page=callback";
	        var param="&type_id=" + type_id + "&branch="+ value;
            var str = "123"; 
			showHint(url+param, hint);
        }
	
	function SendSms(){
		var contact_id = $("#contact_id").val();
		var customer_id = $("#customer_id").val();
		var call_id = $("#call_id").val();
		var contact_mobile = $("#contact_mobile").val();
		var contact_name = $("#contact_name").val();
		var exe_id = $("#allexecutives").val();
		showHint('../inbound/inbound-check.jsp?page=callback&executivesendsms=yes&call_id='+call_id+'&customer_id='+customer_id+'&contact_id='+contact_id+'&exe_id=' + exe_id+'&contact_name='+contact_name+'&contact_mobile=' + contact_mobile , 'addenquiry');
// 		setTimeout('hideform();', 400);
	}						

	function hideform() {
		var sentmsg = $("#sentmsg").text().trim();
		if (sentmsg == 'SMS Sent Succesfully!') {
			$('#form1').hide();
			$('#sendSms').hide();
		}
}

function smsPage(emp_id) {
	var url = '../inbound/executive-send-sms.jsp?contact_id=0&exe_id='+emp_id;
	var Hint = 'addenquiry';
	showHintUrl(url, Hint);
}
// End of script of Search Employees branch.jsp
</script>

<script language="JavaScript" type="text/javascript">	
// Start of script of Canned Messages

function PopulateCannedEmail(){
	var brand_id = document.getElementById("dr_brand").value;
	var branchtype_id = document.getElementById("dr_branchtype").value;
	var value = document.getElementById("txt_search").value;
	console.log("111");
	if(brand_id != ""){
		showHint('../inbound/inbound-check.jsp?page=canned&listemail=yes&home=yes&brand_id=' + brand_id + '&branchtype_id=' + branchtype_id + '&txt_search_email=' + value +'&canned=yes', 'listEmail');
	}
}

function PopulateCannedSMS(){
	var brand_id = document.getElementById("dr_brand").value;
	var branchtype_id = document.getElementById("dr_branchtype").value;
	var value = document.getElementById("txt_search").value;
	console.log("222");
	if(brand_id != ""){
		showHint('../inbound/inbound-check.jsp?page=canned&listsms=yes&home=yes&brand_id=' + brand_id + '&branchtype_id=' + branchtype_id + '&txt_search_sms=' + value +'&canned=yes', 'listSMS');
	}
}

function getEmail(name, obj, hint) {
	var value = document.getElementById("txt_search").value;
	var brand_id = document.getElementById("dr_brand").value;
	var branchtype_id = document.getElementById("dr_branchtype").value;
	var url = "../inbound/inbound-check.jsp?page=canned&listemail=yes&home=yes&canned=yes";
	var param = "&txt_search_email=" + value + '&brand_id=' + brand_id+ '&branchtype_id=' + branchtype_id;
	var str = "123";
	console.log(url + param);
// 	if(value != ""){
	showHint(url + param, hint);
// 	}
}

function getSMS(name, obj, hint) {
	var value = document.getElementById("txt_search").value;
	var brand_id = document.getElementById("dr_brand").value;
	var branchtype_id = document.getElementById("dr_branchtype").value;
	var url = "../inbound/inbound-check.jsp?page=canned&listsms=yes&home=yes&canned=yes";
	var param = "&txt_search_sms=" + value + '&brand_id=' + brand_id+ '&branchtype_id=' + branchtype_id;
	var str = "123";
	console.log(url + param);
// 	if(value != ""){
	showHint(url + param, hint);
// 	}
}

function SendEmail(email_id){
	var customer_email_to = $('#txt_email').val();
	if(!customer_email_to == ''){
		showHint('../inbound/inbound-check.jsp?page=canned&home=yes&email=yes&email_id=' + email_id + '&customer_email_to=' + customer_email_to, 'sentmsg');
	}else{
		$("#sentmsg").html("<b> Enter Email ID!").css("color", "red");
	}
}

function SendSMSC(sms_id){
	var customer_mobile_no = $('#txt_mobile').val();
	if(customer_mobile_no.length == 13){
		showHint('../inbound/inbound-check.jsp?page=canned&home=yes&sms=yes&sms_id=' + sms_id + '&customer_mobile_no=' + customer_mobile_no, 'sentmsg');
	}else{
		$("#sentmsg").html("<b> Incorrect Mobile No.!").css("color", "red");
	}
}
//End of script of Canned Messages	
</script>
