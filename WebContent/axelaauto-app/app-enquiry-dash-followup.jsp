<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Enquiry_Dash"
	scope="request" />
<jsp:useBean id="mybeanfollowup"
	class="axela.axelaauto_app.App_Enquiry_Dash_Followup" scope="request" />
<jsp:useBean id="mybeancrmfollowup"
	class="axela.axelaauto_app.App_Enquiry_Dash_CRMFollowup"
	scope="request" />
<jsp:useBean id="mybeanenqdash" class="axela.sales.Enquiry_Dash"
	scope="request" />
<jsp:useBean id="mybeanenqdashmethods"
	class="axela.sales.Enquiry_Dash_Methods" scope="request" />
<%
	mybean.doPost(request, response);
	mybeanfollowup.doPost(request, response);
	mybeancrmfollowup.doPost(request, response);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>Enquiry Dash | Axelaauto</title>
<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">


<!-- <link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" /> -->
<!-- <link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" /> -->
<!-- <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"> -->
<link href="css/select2-bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<link href="css/select2.min.css" rel="stylesheet" type="text/css" />



<script>
	/// $(function() {
	// 		$("#txt_enquiry_exp_close_date").datepicker({  ////////////////
	// 			showButtonPanel : true,
	// 	//		dateFormat : "dd/mm/yy"
	// 		});
	// 		$("#txt_enquiry_travel_fromdate").datepicker({
	// 			showButtonPanel : true,
	// 			dateFormat : "dd/mm/yy"
	// 		});
	// 		$("#txt_enquiry_travel_todate").datepicker({
	// 			showButtonPanel : true,
	// 			dateFormat : "dd/mm/yy" ///  /////
	// 		});
	/// 	});
	
// 	$(function() {				
// 		$("#txt_followup_date").datetimepicker({
// 			controlType : 'select',
// 			stepMinute : 5,
// 			dateFormat : 'dd/mm/yy',
// 			// 			timeFormat : 'HH:mm',
// 			hour : 10,
// 			minute : 00
// 		});
// 	});

// 	$(function() {
// 		$("#txt_crmfollowup_time").datetimepicker({
// 			addSliderAccess : true,
// 			sliderAccessArgs : {
// 				touchonly : false
// 			},
// 			dateFormat : "dd/mm/yy"
// 		});
// 	});

// 	$(function() {
// 		$("#txt_enquiry_hyundai_ex_purchasedate").datepicker({
// 			showButtonPanel : true,
// 			dateFormat : "dd/mm/yy"
// 		});
// 	});
// 	$(function() {
// 		$("#txt_enquiry_loancompletionmonth").datepicker({
// 			showButtonPanel : true,
// 			dateFormat : "dd/mm/yy"
// 		});
// 	});
	/* $(function() {
		 $( "#txt_enquiry_loancompletionmonth" ).datepicker({
		      showButtonPanel: true,
		      sliderAccessArgs : {
					touchonly : false
		      },
		      dateFormat: "dd/mm/yy"
		    });
		}); */

	function daysdiff(name, obj, hint) {
		var enquiry_id = GetReplacePost(document.form1.txt_enquiry_id.value);
		var value = GetReplacePost(obj.value);
		var param = "name=" + name + "&value=" + value
				+ "&daysdiff=yes&enquiry_id=" + enquiry_id;
		var url = "../sales/enquiry-dash-check.jsp?";
		var str = "123";
		showHintPost(url + param, str, param, hint);
		setTimeout('RefreshHistory()', 1000);
	}

	function BuyerTypeCheck(name) {
		var buyertype_id = document.getElementById("dr_enquiry_buyertype_id").value;
		var existingvehicle = document .getElementById("txt_enquiry_existingvehicle").value;
// 		if (buyertype_id == "2" && existingvehicle == "") {
// 			showToast("Enter existing vehicle make!");
// 		} else {
			SecurityCheck('dr_enquiry_buyertype_id', name,
					'hint_dr_enquiry_buyertype_id');
// 		}
	}

	function ExistingVehicleCheck(name) {
		var buyertype_id = document.getElementById("dr_enquiry_buyertype_id").value;
		var existingvehicle = document
				.getElementById("txt_enquiry_existingvehicle").value;
		if (buyertype_id == "2" && existingvehicle == "") {
			showToast("Enter existing vehicle make!");
		} else {
			SecurityCheck('txt_enquiry_existingvehicle', name,
					'hint_txt_enquiry_existingvehicle');
		}
	}

	function StatusUpdate() {
		var lostcase1 = "", lostcase2 = "", lostcase3 = "";
		var status_id = document.getElementById('dr_enquiry_status_id').value;
		var status_desc = document.getElementById('txt_enquiry_status_desc').value;
		lostcase1 = document.getElementById('dr_enquiry_lostcase1_id').value;
		lostcase2 = document.getElementById('dr_enquiry_lostcase2_id').value;
		lostcase3 = document.getElementById('dr_enquiry_lostcase3_id').value;
		var url = "../sales/enquiry-dash-check.jsp?";
		var param;
		var str = "123";
		var enquiry_id = document.form1.enquiry_id.value;
		param = "name=dr_enquiry_status_id&status_id=" + status_id
				+ "&status_desc=" + status_desc + "&lostcase1=" + lostcase1
				+ "&lostcase2=" + lostcase2 + "&lostcase3=" + lostcase3
				+ "&enquiry_id=" + enquiry_id;
		// 				showHintPost(url+param, str, param, "hint_dr_enquiry_status_id");				
		///               setTimeout('RefreshHistory()', 1000);	
		var strtoast = '';
		$.post(url, param, function(data, status) {
			//alert(data.trim());
			if (status == 'success') {
				strtoast = data.trim().replace('<font color=\"red\">', '')
						.replace('</font>', '');
				showToast(strtoast.trim());
			}
		})

	}

// 	function changedateformat(str){
// 		if(str.length==10)
// 			{
// 			var datePart=str.match(/\d+/g),
// 			year=datePart[0].substring(),
// 			month=datePart[1],
// 			day=datePart[2];
// 			return day+'/'+month+'/'+year;
			
// 			}
//s	}
		
	function SecurityCheck(name, obj, hint) {
		var value = '';
		var param = '';
		var enquiry_id = GetReplacePost(document.form1.enquiry_id.value);
// 		param = "enquiry_id=" + enquiry_id;
		var url = "../sales/enquiry-dash-check.jsp?";
		var dat = document.getElementById("txt_enquiry_date").value;
		var str = "123";
		var status_id = document.getElementById("dr_enquiry_status_id").value;
		if(name == "txt_enquiry_exp_close_date" || name == "txt_enquiry_hyundai_ex_purchasedate" || name == "txt_enquiry_hyundai_ex_loancompletion" || name == "txt_enquiry_loancompletionmonth" || name == "txt_enquiry_purchasemonth"){
			value = GetReplacePost(obj.value);
// 			alert(value);
			//value = changedateformat(value);
			param = "name=" + name + "&value=" + value + "&enquiry_dat=" + dat + "&enquiry_id=" + enquiry_id;
		}
		else if (dat != "" ) {
			value = GetReplacePost(obj.value);
			param = "name=" + name + "&value=" + value + "&enquiry_dat=" + dat + "&enquiry_id=" + enquiry_id;
		}
		else if (name != "chk_enquiry_avpresent"
				&& name != "chk_enquiry_manager_assist"
				&& name != "chk_customer_dnd"
				&& name != "chk_enquiry_evaluation") {
			value = GetReplacePost(obj.value);
			param = "name=" + name + "&value=" + value + "&enquiry_dat=" + dat + "&enquiry_id=" + enquiry_id;
		} else {
			if (obj.checked == true) {
				value = "1";
			} else {
				value = "0";
			}
			param = "name=" + name + "&value=" + value + "&enquiry_dat=" + dat + "&enquiry_id=" + enquiry_id;
		}
		 if (name == "dr_enquiry_model_id") {
			value = GetReplacePost(obj.value);
			var stage_id;
			if (value == 0) {
				stage_id = 1;
			} else {
				stage_id = 2;
			}

			param = "name=" + name + "&value=" + value + "&enquiry_dat=" + dat
					+ "&enquiry_id=" + enquiry_id + "&stage_id=" + stage_id;
		}
		 if ((name == "dr_enquiry_lostcase1_id")
				|| (name == "dr_enquiry_lostcase2_id")
				|| (name == "dr_enquiry_lostcase3_id")) {
			value = GetReplacePost(obj.value);
			param = "name=" + name + "&value=" + value + "&status_id="
					+ status_id + "&enquiry_dat=" + dat + "&enquiry_id=" + enquiry_id;
		}
		
		var strtoast = '';
		$.post(url, param, function(data, status) {
			if (status == 'success') {
				strtoast = data.trim().replace('<font color=\"red\">', '') .replace('</font>', '');
				showToast(strtoast.trim());
				
			}
		})

		// //			alert("valueafter==="+url+param);
		// 		showHintPost(url + param, str, param, hint);
		// 		setTimeout('RefreshHistory()', 1000);
	}

	//////////////////////// eof security check /////////////////////

	function RefreshHistory() {
		var enquiry_id = document.form1.txt_enquiry_id.value;
	}

	function populateItem() {
		var enquiry_id = GetReplacePost(document.form1.enquiry_id.value);
		var model_id = document.getElementById('dr_enquiry_model_id').value;

		//	var param = "enquiry_model_id=" + model_id + "&enquiry_id=" + enquiry_id;
		//	var str = "123";
		showHint('app-enquiry-check.jsp?enquiry_id=' + enquiry_id
				+ '&enquiry_model_id=' + model_id, 'modelitem');
		// 		document.getElementById("hint_dr_enquiry_item_id").innerHTML = "Variant Updated!";
		// 		showHintPost('../axelaauto-app/enquiry-dash-check.jsp?enquiry_model_id='
		// 				+ model_id + "&enquiry_id=" + enquiry_id, "123", param, 'modelitem');
	}

	function populateVariant() {
		var enquiry_id = GetReplacePost(document.form1.txt_enquiry_id.value);
		var preownedmodel_id = document
				.getElementById('dr_enquiry_preownedmodel_id').value;
		//	var param = "enquiry_preownedmodel_id=" + preownedmodel_id + "&enquiry_id=" + enquiry_id;
		//\\var str = "123";
		// 		showHintPost(
		//// 				'../axelaauto-app/enquiry-dash-check.jsp?enquiry_preownedmodel_id='
		// 						+ preownedmodel_id + "&enquiry_id=" + enquiry_id, "123",
		// 				param, 'modelvariant');
		showHint('enquiry-check.jsp?enquiry_id=' + enquiry_id
				+ '&enquiry_preownedmodel_id=' + preownedmodel_id,
				'modelvariant');
	}

	function populateCase2() {

		var lostcaseid = document.getElementById('dr_enquiry_lostcase1_id').value;
		showHint('app-enquiry-check.jsp?caseid1=' + lostcaseid,
				'span_lostcase2');

	}
	function populateCase3() {
		var lostcaseid2 = document.getElementById('dr_enquiry_lostcase2_id').value;
		showHint('app-enquiry-check.jsp?caseid2=' + lostcaseid2,
				'span_lostcase3');

	}

	function RefreshHistory() {
		var enquiry_id = document.form1.txt_enquiry_id.value;
	}

	function PopulateBrandsOwned(name, obj) {
		var value = 0;
		var enquiry_id = GetReplacePost(document.form1.txt_enquiry_id.value);
		var check1;
		value = obj.value;
		if (obj.checked == true) {
			check1 = "1";
		} else {
			check1 = "0";
		}
		var str = "123";
		var param = "name=" + name + "&compbrand_id=" + value + "$enquiry_id="
				+ enquiry_id + "&checked=" + check1;
		showHintPost('../sales/enquiry-dash-check.jsp?name=' + name + '&value='
				+ value + '&enquiry_id=' + enquiry_id + '&checked=' + check1,
				"123", param, 'hint_chk_compbrand_id');
		setTimeout('RefreshHistory()', 1000);
	}

	function populateLostCase2(name, obj, hint) {
		var enquiry_id = GetReplacePost(document.form1.enquiry_id.value);
		var value = obj.value;
		var url = "../sales/enquiry-dash-check.jsp?"
		var param = "name=dr_enquiry_lostcase1_id&value=" + value
				+ "&enquiry_id=" + enquiry_id;
		var str = "123";
		showHintPost(url + param, str, param, hint);
		setTimeout('RefreshHistory()', 1000);
	}

	function populateLostCase3(name, obj, hint) {
		var enquiry_id = GetReplacePost(document.form1.enquiry_id.value);
		var value = obj.value;
		var status_id = document.getElementById('dr_enquiry_status_id').value;
		var url = "../sales/enquiry-dash-check.jsp?"
		var param = "name=dr_enquiry_lostcase2_id&value=" + value
				+ "&status_id=" + status_id + "&enquiry_id=" + enquiry_id;
		var str = "123";
		setTimeout("showHintPost('" + url + param + "', '" + str + "', '"
				+ param + "', '" + hint + "')", 500);
		setTimeout('RefreshHistory()', 1000);
	}
</script>

<script>
        function TestdriveReq(name,obj,hint)
        {
        	 var testdrive_yes=document.getElementById("dr_enquiry_nexa_testdrivereq").value;
        	 if(testdrive_yes=="2")
        	  {
        		  document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.display='';
        		  document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.visibility='visible';
        		  document.getElementById("reason").style.display='';
        		  document.getElementById("hint_txt_enquiry_nexa_testdrivereqreason").style.display='';
        		  
        		  SecurityCheck(name,obj,hint);
        	  }
        	 else if(testdrive_yes=="0")
        		  {
        			  document.getElementById("reason").style.display='None';
             		 document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.display='None';
        		  }
        	 else
        		 {
        		 
        		 document.getElementById("reason").style.display='None';
        		 document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.display='None';
        		 document.getElementById("hint_txt_enquiry_nexa_testdrivereqreason").style.display='None';
        		 
        		 SecurityCheck(name,obj,hint);
        		 }
        }
        
        </script>
<style>
a {
	text-shadow: none;
	color: black;
	text-decoration: none;
}

b {
	color: #8E44AD;
}

strong {
	color: #fff;
}

center {
	color: #8E44AD;
	font-weight: bold;
}

span {
	color: red;
	/* 	width:100%; */
}

.select2-container {
	width: 100%;
}

.header-wrap {
	position: fixed;
	width: 100%;
	top: 0;
	z-index: 1;
	/* 	margin-left: 5px;  */
	/* 	margin-top: 45px;  */
}

.select2-container {
	width: 300px;
}

.label {
	font-size: 14px;
	color: #8E44AD;
}
</style>

</head>

<body>
	<div class="header-wrap">
		<div class="panel-heading"
			style="margin-bottom: 20px; background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05); padding: 18px">
			<span class="panel-title">
				<center>
					<strong>Enquiry Dashboard</strong>
				</center>
			</span>
		</div>
	</div>
	<div class="col-md-6" style="margin-top: 40px; margin-left: 4px;">
		<form role="form" class="form-horizontal" name="form2" id="form2" method="post">
			<div class="form-body">
				<div class="form-group">
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4 control-label"
							for="form_control_1"><b>Enquiry ID: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="id" class="form-control"><%=mybean.enquiry_id%></label>
							<input name="enquiry_id" type="hidden" id="enquiry_id"
								value="<%=mybean.enquiry_id%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4 control-label"
							for="form_control_1"><b>Date: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="date" class="form-control"><%=mybean.date%></label> <input
								name="txt_enquiry_date" type="hidden" class="form-control"
								id="txt_enquiry_date" value="<%=mybean.date%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4  control-label"
							for="form_control_1"><b>Customer: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="customer" class="form-control"> <%=mybean.enquiry_customer_name%></label>
							<input type="hidden" class="form-control"
								name="txt_enquiry_customer_name" id="txt_enquiry_customer_name"
								value="<%=mybean.enquiry_customer_name%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4  control-label"
							for="form_control_1"><b>Contact: </b> </label>
						<div class="col-md-8 col-xs-7">
							<label for="customer" class="form-control"><%=mybean.title_desc%><%=mybean.contact_fname%>&nbsp;<%=mybean.contact_lname%></label>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4  control-label"
							for="form_control_1"><b>Mobile: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="mobile" class="form-control"><%=mybean.contact_mobile1%></label>
							<span style="position: absolute; left: 130px; top: 10px"
								onclick="callNo('<%=mybean.contact_mobile1%>')"> <img
								src="ifx/icon-call.png" class="img-responsive"></span> <input
								type="hidden" class="form-control" name="txt_contact_mobile1"
								id="txt_contact_mobile1" value="<%=mybean.contact_mobile1%>"
								size="32" maxlength="10" style="width: 250px">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4  control-label"
							for="form_control_1"><b>Branch: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="mobile" class="form-control"><%=mybean.branch_name%></label>
							<input type="hidden" class="form-control"
								value="<%=mybean.branch_name%>">
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
	<div class="panel-heading"
		style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
		<span class="panel-title">
			<center>
				<h4>
					<strong>Follow-up </strong>
				</h4>
			</center>
		</span>
	</div>

	<!-- accordian 2-->
	<!-- 				<h4> -->
	<!-- 							<a href="#"><strong>FOLLOW-UP</strong></a> -->
	<!-- 						</h4> -->

	<form role="form" class="form-horizontal" name="form1"
		id="form1" method="post">

		<!-- 			<div class="form-body"> -->
		<!-- 				<div class="form-group"> -->
		<%-- 					<%if(mybean.followuptab.equals("active")){%> --%>
		<!-- 					<div id="collapse_3_2" class="panel-collapse collapse in"> -->
		<%-- 					<%}else{ %> --%>
		<!-- 					<div id="collapse_3_2" class="panel-collapse collapse"> -->
		<%-- 					<%}%> --%>
		<div style="margin-top: 15px; margin-left: 20px;">
			<%
									if (!mybeanfollowup.status.equals("")) {
								%>
			<%
									if (mybeanfollowup.status.equals("Update")) {
								%>
			<center>Follow-up List</center>
			<%
									}
								%>

			<div style="margin-right: 20px;">
				<div><%=mybeanfollowup.followupHTML%></div>



				<div>
					<center>&nbsp;</center>
				</div>

			</div>
		</div>




		<div class="  panel-heading"
			style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
			<span class="caption panel-title" style="float: none">
				<center>
					<h4>
						<strong><%=mybeanfollowup.status%> Follow-up </strong>
					</h4>
				</center>
			</span>
		</div>

		<!-- 								<div class="form-body"> -->
		<%
										if (mybeanfollowup.status.equals("Update")) {
									%>
		<center>
			<span><%=mybeanfollowup.msg%></span>
		</center>



		<div class="col-md-12" style="margin-top: 0px; margin-left: 4px;">
			<% if (mybeanfollowup.status.equals("Update") && mybean.branch_brand_id.equals("55")) { %>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Current Follow-up Status<font
					color="#ff0000">*</font>:
				</label> <select name="dr_followupstatus" id="dr_followupstatus"
					class="form-control">
					<%=mybeanenqdashmethods.PopulateFollowupStatus(mybean.comp_id, mybeanfollowup.followup_followupstatus_id)%>
				</select>
			</div>
			<%} %>


			<div class="form-group form-md-line-input">
				<label for="form_control_1">Action Taken<span>*</span>:
				</label> <select class="form-control" name="dr_followup_feedbacktype_id"
					id="dr_followup_feedbacktype_id">
					<%=mybeanfollowup
							.PopulateFollowupDesc(mybean.comp_id)%>
				</select>
			</div>
			<%
										}
									%>
				<%
					if (mybean.branch_brand_id.equals("60")) {
				%>
				<div class="form-group form-md-line-input">
				  <label for="form_control_1">Enquiry Status<span>*</span>:&nbsp; </label>
							<select class="form-control" name="drop_enquiry_jlr_status"
									id="drop_enquiry_jlr_status">
										<%=mybeanenqdashmethods.PopulateEnquiryStatus(mybeanfollowup.enq_jlr_enquirystatus, mybean.comp_id)%>
							</select>
				</div>
															
				<!--  added to store history old value -->
				<input type="hidden" id="jlr_enquirystatus" name="jlr_enquirystatus" value="<%=mybean.jlr_enquirystatus%>"/>
				<%
				}
				%>



<%
																if ((mybeanfollowup.enquiry_status_id.equals("1") && mybeanfollowup.status
																		.equals("Update")) || mybeanfollowup.status.equals("Add")) {
															%>
			<div class="form-group form-md-line-input">
				<label for="form_control_1"
					value="<%=mybeanfollowup.followup_date%>"> Next Follow-up
					Date<span>*</span>:
				</label> <input type="" class="form-control" name="txt_followup_date"
					id="txt_followup_date" onclick="datePicker('txt_followup_date');"
					readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1" value="<%=mybeanfollowup.temp_time%>">
					Next Follow-up Time<span>*</span>:
				</label> <input type="" class="form-control" name="txt_followup_time"
					id="txt_followup_time" onclick="timePicker('txt_followup_time')"
					readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Next Follow-up Type<span>*</span>:
				</label> <select class="form-control" name="dr_followuptype"
					id="dr_followuptype"><%=mybeanfollowup.PopulateFollowuptype(mybean.comp_id)%>
				</select>
			</div>
			<!-- 								</div> -->
			<%} %>
			
			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Feedback<span>*</span>:
				</label>
				<textarea class="form-control" rows="1" name="txt_followup_desc"
					id="txt_followup_desc" /><%=mybeanfollowup.followup_desc%></textarea>

			</div>
			<br>

			<div class="form-actions noborder">
				<center>
<!-- 					<input name="submitbutton" type="submit" class="btn1" id="submitbutton" value="Submit" onClick="return SubmitFormOnce(document.addfollowup, this);" /> -->
					<button type="button" class="btn1" name="submitbutton" id="submitbutton" >Submit</button> 
					<input type="hidden" name="submit_button" id="submit_button" value="yes">
				</center>
				<%} %>
				<br>
			</div>

		</div>


	</form>
	</div>
		<!-- 	<script src="js/jquery.min.js" type="text/javascript"></script> -->
<!-- <script src="js/bootstrap.js" type="text/javascript"></script> -->
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<!-- 	<script src="../assets/js/select2.full.min.js" type="text/javascript"></script> -->
<!-- <script src="../assets/js/components-select2.min.js" type="text/javascript"></script> -->
<!--  <script src="js/jquery-ui.js" type="text/javascript"></script>	 -->
<script src="js/jquery-ui.js" type="text/javascript"></script>	
<script src="js/jquery.app.js" type="text/javascript"></script>
<script src="js/jquery.min.js" type="text/javascript"></script>
<!-- 	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script> -->
<script src="js/select2.full.min.js" type="text/javascript"></script>
<script src="js/components-select2.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Validate.js"></script>
<script src="js/axelamobilecall.js" type="text/javascript"></script>

	<script>
$(function() { 
$(".select2-container").css("width","100%");
});
</script>

<script>

$(document).ready(function() {
	$("#submitbutton").click(function() {
		checkForm();
	});
});

</script>

<script>
	var msg = "";
	function checkForm() {
		msg = "";
		<%if(mybeanfollowup.status.equals("Update") && mybeanfollowup.branch_brand_id.equals("55")){%>
		var followup_status = document.getElementById("dr_followupstatus").value;
		if (followup_status == '0') {
			msg += '<br>Select Current Follow-up Status!';
		}
		<%}%>
		
		<%if(mybeanfollowup.status.equals("Update")){%>
		var feedback_type = document.getElementById("dr_followup_feedbacktype_id").value;
		if (feedback_type == '0') {
			msg += '<br>Select Action Taken!';
		}
		<%}%>
		
     <%if(mybeanfollowup.status.equals("Update") && mybean.branch_brand_id.equals("60")){%>
     var enquiry_jlr_status = document.getElementById("drop_enquiry_jlr_status").value;
 		if (enquiry_jlr_status == '') {
           msg += "<br>Select Enquiry Status!";
 				}
           <%}%>



		<%if(mybeanfollowup.status.equals("Update")){%>
		var followup_desc = document.getElementById("txt_followup_desc").value;
		if (followup_desc == '') {
			msg += '<br>Enter Feedback!';
		}
		<%}%>
		
		<%if(mybeanfollowup.enquiry_status_id.equals("1")){%>
		var followup_date = document.getElementById("txt_followup_date").value;
		if (followup_date == '') {
			msg += '<br>Select Next Follow-up Date!';
		}
		
		var followup_time = document.getElementById("txt_followup_time").value;
		if (followup_time == '') {
			msg += '<br>Select Next Follow-up Time!';
		}
		
		var followup_type = document.getElementById("dr_followuptype").value;
		if (followup_type == '0') {
			msg += '<br>Select Next Follow-up Type!';
		}
		<%}%>
		if (msg != '') {
			showToast(msg);
			return false;
		} else {
			document.getElementById('submit_button').value = "yes";
			document.getElementById('form1').submit();
		}
		
	}
		</script>

</body>
<!-- END BODY -->
</html>