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
	class="axela.sales.Enquiry_Dash_Methods" scope="request"/>
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
<link href="css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">


<!-- <link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" /> -->
<!-- <link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" /> -->
<!-- <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"> -->
<link href="css/select2-bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="css/select2.min.css" rel="stylesheet" type="text/css" />


	
<script>
	// $(function() {
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
	text-decoration:none;
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

 .select2-container{
 width:100%;
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

<body onLoad="TestdriveReq();">
	<div class="header-wrap">
		<div class="panel-heading"
			style="margin-bottom: 20px; background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);">
			<span class="panel-title">
				<center>
					<strong>Enquiry Dashboard</strong>
				</center>
			</span>
		</div>
	</div>
	<div class="col-md-12" style="margin-top: 40px;">
		<form role="form" class="form-horizontal" name="form1" id="form1"
			method="post">
			<div class="form-body">
				<div class="form-group">
					<div class="form-group form-md-line-input">
					
					
					
					
					<!-- ===============for preowned modal============================ -->

<div class="container-fluid" style="min-height:10px"></div>
<div class="modal fade" id="Hintclicktocall" role="basic" aria-hidden="true" style="transform:translate(0,50%)">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
						 <span> &nbsp;&nbsp;Loading... </span>
						 <br><br>
						 
				</div>
			</div>
		</div>
	</div>





<!-- ===================================================== -->
					
					
					
					
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1"><b>Enquiry ID: </b></label>
						<div class="col-md-8 col-xs-8">
							<label for="id" class="form-control"><%=mybean.enquiry_id%></label>
							<input name="enquiry_id" type="hidden" id="enquiry_id"
								 value="<%=mybean.enquiry_id%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1"><b>Date: </b></label>
						<div class="col-md-8 col-xs-8">
							<label for="date" class="form-control"><%=mybean.date%></label> <input
								name="txt_enquiry_date" type="hidden" class="form-control"
								id="txt_enquiry_date" value="<%=mybean.date%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3  control-label"
							for="form_control_1"><b>Customer: </b></label>
						<div class="col-md-8 col-xs-8">
							<label for="customer" class="form-control"> <%=mybean.enquiry_customer_name%></label>
							<input type="hidden" class="form-control"
								name="txt_enquiry_customer_name" id="txt_enquiry_customer_name"
								value="<%=mybean.enquiry_customer_name%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3  control-label"
							for="form_control_1"><b>Contact: </b> </label>
						<div class="col-md-8 col-xs-8">
							<label for="customer" class="form-control"><%=mybean.title_desc%><%=mybean.contact_fname%>&nbsp;<%=mybean.contact_lname%></label>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3  control-label"
							for="form_control_1"><b>Mobile: </b></label>
						<div class="col-md-8 col-xs-8">
							<label for="mobile" class="form-control"><%=mybean.contact_mobile1%></label>
							<span style="position:absolute;left:160px; top:10px" onclick="callNo('<%=mybean.contact_mobile1%>')"> <img src="ifx/icon-call.png" class="img-responsive"></span>
							<input type="hidden" class="form-control"
								name="txt_contact_mobile1" id="txt_contact_mobile1"
								value="<%=mybean.contact_mobile1%>" size="32" maxlength="10"
								style="width: 250px">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3  control-label"
							for="form_control_1"><b>Branch: </b></label>
						<div class="col-md-8 col-xs-8">
							<label for="mobile" class="form-control"><%=mybean.branch_name%></label>
							<input type="hidden" class="form-control"
								value="<%=mybean.branch_name%>">
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
	<div class="portlet light">
		<div class="portlet-body">
			<div class="panel-group accordion" id="accordion3">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion3"
								href="#collapse_3_1"><strong>ENQUIRY DETAILS</strong></a>
						</h4>
					</div>
					<div id="collapse_3_1" class="panel-collapse collapse">
						<div class="panel-body">
							<form role="form" class="form-horizontal" method="post"
								name="frm" id="frm">
								<div class="form-body">

									<!--  						///////////////////////		ENQUIRY Details   ////////////////////// -->

									<div class="form-group form-md-line-input">
										<label for="form_control_1 col-md-6"><div class="hint"
												id="hint_txt_enquiry_exp_close_date"></div>Closing Date<span>*</span>:</label>
										<input type="" class="form-control"
											name="txt_enquiry_exp_close_date"
											id="txt_enquiry_exp_close_date" value="<%=mybean.closedate%>"
											size="12" maxlength="10"
											onfocusout="SecurityCheck('txt_enquiry_exp_close_date',this,'hint_txt_enquiry_exp_close_date');"
											onclick="datePicker('txt_enquiry_exp_close_date');" >

									</div>

									<!-- 									<div class="form-group form-md-line-input"> -->
									<!-- 										<label for="form_control_1"><div class="hint" -->
									<!-- 												id="hint_txt_days_diff"></div>Days left: </label> <label -->
									<%-- 											for="form_control_1"><span style="color: #000"><%=mybean.days_diff%></span></label> --%>
									<!-- 									</div> -->
									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_txt_enquiry_title"></div>Title<span>*</span>:</label> <input
											type="text" class="form-control" name="txt_enquiry_title"
											id="txt_enquiry_title" value="<%=mybean.enquiry_title%>"
											size="52" maxlength="255"
											onchange="SecurityCheck('txt_enquiry_title',this,'hint_txt_enquiry_title')" />

									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_enquiry_desc"></div>Description:
										</label>
										<textarea class="form-control"
											name="txt_enquiry_desc" cols="50" rows="4"
											id="txt_enquiry_desc" value=""
											onChange="SecurityCheck('txt_enquiry_desc',this,'hint_txt_enquiry_desc')" ><%=mybean.enquiry_desc%></textarea>

									</div>


									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_txt_enquiry_value"></div>Budget:</label> <input type="tel"
											class="form-control" name="txt_enquiry_value"
											id="txt_enquiry_value" value="<%=mybean.enquiry_value%>"
											onKeyUp="toInteger('txt_enquiry_value','Value')"
											onchange="SecurityCheck('txt_enquiry_value',this,'hint_txt_enquiry_value')"
											size="10" maxlength="10" />
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_dr_enquiry_emp_id"></div> Sales Consultant<span>*</span>:</label>
										<select class="form-control" name="dr_enquiry_emp_id"
											id="dr_enquiry_emp_id"
											onChange="SecurityCheck('dr_enquiry_emp_id',this,'hint_dr_enquiry_emp_id');">
											<%=mybean.PopulateExecutive()%>
										</select>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_dr_enquiry_model_id"></div>Model<span>*</span>:</label> <select
											<%if (mybean.enquiry_enquirytype_id.equals("2")) {%>
											disabled="disabled" <%}%> class="form-control"
											name="dr_enquiry_model_id" id="dr_enquiry_model_id"
											onChange="populateItem();SecurityCheck('dr_enquiry_model_id',this,'hint_dr_enquiry_model_id')">
											<%=mybean.PopulateModel()%>
										</select>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_dr_enquiry_item_id"></div>Variant<span>*</span>: </label> <span
											id="modelitem"><select
											<%if (mybean.enquiry_enquirytype_id.equals("2")) {%>
											disabled="disabled" <%}%> class="form-control"
											name="dr_enquiry_item_id" id="dr_enquiry_item_id"
											onChange="SecurityCheck('dr_enquiry_item_id',this,'hint_dr_enquiry_item_id');">
												<%=mybean.PopulateItem()%>
										</select> </span>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_dr_enquiry_add_model_id"></div>Additional Model: </label> <span
											id="modelitem"><select class="form-control"
											name="dr_enquiry_add_model_id" id="dr_enquiry_add_model_id"
											onChange="SecurityCheck('dr_enquiry_add_model_id',this,'hint_dr_enquiry_add_model_id');">
												<%=mybean.PopulateAdditionalModel(mybean.comp_id)%>
										</select> </span>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_dr_enquiry_option_id"></div>Colour<span>*</span>: </label> <select
											class="form-control" name="dr_enquiry_option_id"
											id="dr_enquiry_option_id"
											onChange="SecurityCheck('dr_enquiry_option_id',this,'hint_dr_enquiry_option_id');">

											<%-- <%=mybean.PopulateColor()%> --%>
											<%=mybean.PopulateOption(mybean.comp_id)%>
										</select>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_dr_enquiry_age_id"></div>Age<span>*</span>: </label> <select
											class="form-control" name="dr_enquiry_age_id"
											id="dr_enquiry_age_id"
											onChange="SecurityCheck('dr_enquiry_age_id',this,'hint_dr_enquiry_age_id');">
											<%-- <%=mybean.PopulateAge()%> --%>
											<%=mybean.PopulateAge(mybean.comp_id)%>
										</select>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_dr_enquiryocc_id"></div>Occupation<span>*</span>:</label> <select
											class="form-control" name="dr_enquiry_occ_id"
											id="dr_enquiry_occ_id"
											onchange="SecurityCheck('dr_enquiry_occ_id',this,'hint_dr_enquiry_occ_id');">
											<%=mybean.PopulateOccupation()%>
										</select>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_dr_enquiry_custtype_id"></div>Type of Customer<span>*</span>:</label>
										<select class="form-control" name="dr_enquiry_custtype_id"
											id="dr_enquiry_custtype_id"
											onchange="SecurityCheck('dr_enquiry_custtype_id',this,'hint_dr_enquiry_custtype_id');">
											<%=mybean.PopulateCustomerType()%>
										</select>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_txt_enquiry_fuelallowance"></div>Fuel Allowance:</label> <input
											type="text" class="form-control"
											name="txt_enquiry_fuelallowance"
											id="txt_enquiry_fuelallowance"
											value="<%=mybean.enquiry_fuelallowance%>" size="10"
											maxlength="10"
											onKeyUp="toInteger('txt_enquiry_fuelallowance','Fuel')"
											onChange="SecurityCheck('txt_enquiry_fuelallowance',this,'hint_txt_enquiry_fuelallowance')">

									</div>

									<!-- 							//////////////////////	Pre Owned Details  ////////////////// -->

									
<!-- 									<div class="form-group form-md-line-input"> -->
<!-- 										<label for="form_control_1"> -->
<!-- 											<div class="hint" id="hint_modelvariant"></div> Pre Owned model: -->
<!-- 										</label> <select class="form-control" name="modelvariant" -->
<!-- 											id="modelvariant" style="width: 300px" type="hidden" -->
<!-- 											onChange="SecurityCheck('modelvariant',this,'hint_modelvariant')"> -->
<%-- 											<%=mybean.PopulatePreOwnedModel(mybean.comp_id)%> --%>
<!-- 										</select> -->
<!-- 									</div> -->

<%
  									if (mybean.enquiry_enquirytype_id.equals("2")) {
								%>  
 									<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Pre-Owned</strong>
											</h4>
										</center>
									</span>
								</div>
<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_dr_enquiry_fueltype_id"></div>
											Pre-Owned Model<font color="#ff0000">*</font>:
										</label>
										 <select class="form-control select2" id="preownedvariant1" name="preownedvariant1" onchange="SecurityCheck('preownedvariant1',this,'hint_preownedvariant1')" >
																<%=mybean.variantcheck.PopulateVariant(mybean.enquiry_preownedvariant_id)%>
																</select>
																</div>
<!--                                                                 <div class="hint" id="hint_preownedvariant1"></div> -->
									




									<%-- <div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_dr_enquiry_preownedvariant_id"></div>
											Pre-Owned Variant:
										</label> <span id="modelvariant"><select class="form-control"
											name="dr_enquiry_preownedvariant_id"
											id="dr_enquiry_preownedvariant_id"
											onChange="SecurityCheck('dr_enquiry_preownedvariant_id',this,'hint_dr_enquiry_preownedvariant_id');">
												<%=mybean.PopulatePreOwnedVariant()%>
										</select> </span>
									</div> --%>

									<div class="form-group form-md-line-input">
										<label for="form_control_1">
<!-- 											<div class="hint" id="hint_dr_enquiry_fueltype_id"></div> -->
											Fuel Type:
										</label> <select class="form-control" name="dr_enquiry_fueltype_id"
											id="dr_enquiry_fueltype_id"
											onChange="SecurityCheck('dr_enquiry_fueltype_id',this,'hint_dr_enquiry_fueltype_id');">
											<%=mybean.PopulateFuelType()%>
										</select>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1">
<!-- 											<div class="hint" id="hint_dr_enquiry_prefreg_id"></div> -->
											Pref. Reg.:
										</label> <select class="form-control" name="dr_enquiry_prefreg_id"
											id="dr_enquiry_prefreg_id"
											onChange="SecurityCheck('dr_enquiry_prefreg_id',this,'hint_dr_enquiry_prefreg_id');">
											<%=mybean.PopulatePrefReg()%>
										</select>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1">
<!-- 											<div class="hint" id="hint_txt_enquiry_presentcar"></div> -->
											Present Car:
										</label> <input type="text" class="form-control"
											name="txt_enquiry_presentcar" id="txt_enquiry_presentcar"
											value="<%=mybean.enquiry_presentcar%>" size="32"
											maxlength="255"
											onchange="SecurityCheck('txt_enquiry_presentcar',this,'hint_txt_enquiry_presentcar')">
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1">
<!-- 											<div class="hint" id="hint_dr_enquiry_finance"></div> -->
											Finance:
										</label> <select class="form-control" name="dr_enquiry_finance"
											id="dr_enquiry_finance"
											onChange="SecurityCheck('dr_enquiry_finance',this,'hint_dr_enquiry_finance');">
											<%=mybean.PopulateFinance()%>
										</select>
									</div>
									<% } %>
									<%-- <div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_dr_enquiry_budget_id"></div>Budget:
										</label> <select class="form-control" name="dr_enquiry_budget_id"
											id="dr_enquiry_budget_id"
											onChange="SecurityCheck('dr_enquiry_budget_id',this,'hint_dr_enquiry_budget_id');">
											<%=mybean.PopulateBudget()%>
										</select>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_dr_enquiry_prefregyear"></div>Pref.
											Reg. Year:
										</label> <select class="form-control" name="dr_enquiry_prefregyear"
											id="dr_enquiry_prefregyear"
											onChange="SecurityCheck('dr_enquiry_prefregyear',this,'hint_dr_enquiry_prefregyear');">
											<%=mybean.PopulateRegYear()%>
										</select>
									</div>
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_dr_enquiry_prefmileage_id"></div>Mileage:
										</label> <select class="form-control" name="dr_enquiry_prefmileage_id"
											id="dr_enquiry_prefmileage_id"
											onChange="SecurityCheck('dr_enquiry_prefmileage_id',this,'hint_dr_enquiry_prefmileage_id');">
											<%=mybean.PopulateMileage()%>
										</select>
									</div>
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_dr_enquiry_extcolour_id"></div>Exterior:
										</label> <select class="form-control" name="dr_enquiry_extcolour_id"
											id="dr_enquiry_extcolour_id"
											onChange="SecurityCheck('dr_enquiry_extcolour_id',this,'hint_dr_enquiry_extcolour_id');">
											<%=mybean.PopulateExterior()%>
										</select>
									</div>
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_dr_enquiry_intcolour_id"></div>Interior:
										</label> <select class="form-control" name="dr_enquiry_intcolour_id"
											id="dr_enquiry_intcolour_id"
											onChange="SecurityCheck('dr_enquiry_intcolour_id',this,'hint_dr_enquiry_intcolour_id');">
											<%=mybean.PopulateInterior()%>
										</select>

									</div>--%>




									<!-- 		////////////////////  Understand your Customer   /////////////////////    -->



								</div>



								<%
									if (mybean.branch_brand_id.equals("1")
											|| mybean.branch_brand_id.equals("2"))
									{
								%>

								<!-- 		////////////////////  Start Maruti Fields /////////////////////    -->

										<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Understand Your Customer </strong>
											</h4>
										</center>
									</span>
								</div>
								
								
								
								
								
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_monthkms_id"></div>How many kilometers
										you drive in a month?<span>*</span></label> <select
										class="form-control" name="dr_enquiry_monthkms_id"
										id="dr_enquiry_monthkms_id" maxlength="4"
										onchange="SecurityCheck('dr_enquiry_monthkms_id',this,'hint_dr_enquiry_monthkms_id');">
										<%=mybean.PopulateMonthKms(mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_purchasemode_id"></div>What will be your
										mode of purchase?<span>*</span></label> <select class="form-control"
										name="dr_enquiry_monthkms_id" id="dr_enquiry_purchasemode_id"
										onchange="SecurityCheck('dr_enquiry_purchasemode_id',this,'hint_dr_enquiry_purchasemode_id');">
										<%=mybean.PopulatePurchaseMode(mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_income_id"></div>What is your approximate
										annual household income (Rs)?<span>*</span></label> <select
										class="form-control" name="dr_enquiry_income_id"
										id="dr_enquiry_income_id"
										onchange="SecurityCheck('dr_enquiry_income_id',this,'hint_dr_enquiry_income_id');">
										<%=mybean.PopulateIncome(mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_enquiry_familymember_count"></div>How
										many members are there in your family?:
									</label> <input type="text" class="form-control"
										name="txt_enquiry_familymember_count"
										id="txt_enquiry_familymember_count"
										value="<%=mybean.enquiry_familymember_count%>" size="5"
										maxlength="3"
										onchange="SecurityCheck('txt_enquiry_familymember_count',this,'hint_txt_enquiry_familymember_count')">
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_expectation_id"></div>What is top most
										priority expectations from the car?<span>*</span></label> <select
										class="form-control" name="dr_enquiry_expectation_id"
										id="dr_enquiry_expectation_id"
										onchange="SecurityCheck('dr_enquiry_expectation_id',this,'hint_dr_enquiry_expectation_id');">
										<%=mybean.PopulateExpectation(mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_enquiry_familymember_count"></div>Any
										other car you have in mind?<span>*</span>
									</label> <input type="text" class="form-control"
										name="txt_enquiry_othercar" id="txt_enquiry_othercar"
										value="<%=mybean.enquiry_othercar%>" size="32" maxlength="255"
										onchange="SecurityCheck('txt_enquiry_othercar',this,'hint_txt_enquiry_othercar')">
								</div>
								<%} %>
								<br>
<!-- --                      End Maruti Fields -->

		  <!--            Start Maruti and Nexa Fields -->
                <%if( mybean.branch_brand_id.equals("10")){ %>    
											
											<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Customer Profile (Maruti-Nexa)</strong>
											</h4>
										</center>
									</span>
								</div>
								
		
								
								
											<div class="form-group form-md-line-input">
									<label for="form_control_1">Gender: </label> <select class="form-control"
										name="dr_enquiry_nexa_gender" id="dr_enquiry_nexa_gender"
										onchange="SecurityCheck('dr_enquiry_nexa_gender',this,'hint_dr_enquiry_nexa_gender');">
										<%=mybeanenqdashmethods.PopulateEnquiryNexaGender(mybean.enquiry_nexa_gender, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_nexa_gender"></div>
								</div>
								
						<div class="form-group form-md-line-input">
									<label for="form_control_1">Beverage Choice: </label> <select class="form-control"
										name="dr_enquiry_nexa_beveragechoice" id="dr_enquiry_nexa_beveragechoice"
										onchange="SecurityCheck('dr_enquiry_nexa_beveragechoice',this,'hint_dr_enquiry_nexa_beveragechoice');">
										<%=mybeanenqdashmethods.PopulateEnquiryNexaBeveragechoice(mybean.enquiry_nexa_beveragechoice, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_nexa_beveragechoice"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Interested in Autocard: </label> <select class="form-control"
										name="dr_enquiry_nexa_autocard" id="dr_enquiry_nexa_autocard"
										onchange="SecurityCheck('dr_enquiry_nexa_autocard',this,'hint_dr_enquiry_nexa_autocard');">
										<%=mybeanenqdashmethods.PopulateEnquiryNexaAutocard(mybean.enquiry_nexa_autocard, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_nexa_autocard"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Fuel Type: </label> <select class="form-control"
										name="dr_enquiry_nexa_fueltype" id="dr_enquiry_nexa_fueltype"
										onchange="SecurityCheck('dr_enquiry_nexa_fueltype',this,'hint_dr_enquiry_nexa_fueltype');">
										<%=mybeanenqdashmethods.PopulateEnquiryNexaFueltype(mybean.enquiry_nexa_fueltype, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_nexa_fueltype"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Specific requirement from the car: </label> <select class="form-control"
										name="dr_enquiry_nexa_specreq" id="dr_enquiry_nexa_specreq"
										onchange="SecurityCheck('dr_enquiry_nexa_specreq',this,'hint_dr_enquiry_nexa_specreq');">
										<%=mybeanenqdashmethods.PopulateEnquiryNexaSpecreq(mybean.enquiry_nexa_specreq, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_nexa_specreq"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Test Drive Required: </label> <select class="form-control"
										name="dr_enquiry_nexa_testdrivereq" id="dr_enquiry_nexa_testdrivereq"
										onchange="TestdriveReq('dr_enquiry_nexa_testdrivereq',this,'hint_dr_enquiry_nexa_testdrivereq');">
										<%=mybeanenqdashmethods.PopulateEnquiryNexaTestdrivereq(mybean.enquiry_nexa_testdrivereq, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_nexa_testdrivereq"></div>
								</div>
								
							<div class="form-group form-md-line-input"  id="reason">
										<label for="form_control_1">Reason:</label> <input
											type="text" class="form-control" name="txt_enquiry_nexa_testdrivereqreason" id="txt_enquiry_nexa_testdrivereqreason"
											value="<%=mybean.enquiry_nexa_testdrivereqreason%>" size="10" maxlength="255"
											onChange="SecurityCheck('txt_enquiry_nexa_testdrivereqreason',this,'hint_txt_enquiry_nexa_testdrivereqreason')"/>
											<div class="hint" id="hint_txt_enquiry_nexa_testdrivereqreason"></div>

									</div>

								
					   <%} %>  
					<br/>
								<!-- 		////////////////////  Exchange Details  /////////////////////    -->
    <%if(mybean.branch_brand_id.equals("1") || mybean.branch_brand_id.equals("2") || mybean.branch_brand_id.equals("10")) {%>
                      
											
											<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Exchange Details </strong>
											</h4>
										</center>
									</span>
								</div>
								
								
								<!-- 								============================================================ -->
								
					<div class="container-fluid">
<br>
	                                         <div class="form-group">
											   <div class="col-md-6 col-xs-12"> 
						                     <div class="control-label col-md-4">
												<label class="">
<font style="color:#8E44AD">Trade-In Model:</font></label>
												</div>
												<div class="col-md-8">
													<select class="form-control select2" id="preownedvariant"
														name="preownedvariant" onchange="SecurityCheck('preownedvariant',this,'hint_preownedvariant')">
														<%=mybean.variantcheck.PopulateVariant(mybean.enquiry_tradein_preownedvariant_id)%>
														
													</select>
													<div class="hint" id="hint_preownedvariant"></div>
												</div><br>
											    </div>  
<!--                                                    <div class="col-md-6 col-xs-6" style="text-align: center"><br>  -->
                                                      <center> <a class=" btn1" href="../preowned/preowned-dash-add.jsp?enquiry_id=<%=mybean.enquiry_id%>" data-target="#Hintclicktocall" data-toggle="modal">Add Evaluation</a></center>
<!--                                                       </div>  -->
                                                       </div>
                                                       </div>
					
					
					
					
					
					<!-- 								============================================================ -->	
								
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_expectation_id"></div>Type of Buyer:</label> <select
										class="form-control" name="dr_enquiry_buyertype_id"
										id="dr_enquiry_buyertype_id" onchange="SecurityCheck('dr_enquiry_buyertype_id',this,'hint_dr_enquiry_buyertype_id');">
										<%=mybean.PopulateBuyerType(mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_ownership_id"></div>Ownership:</label> <select
										class="form-control" name="dr_enquiry_ownership_id"
										id="dr_enquiry_ownership_id"
										onchange="SecurityCheck('dr_enquiry_ownership_id',this,'hint_dr_enquiry_ownership_id');">
										<%=mybean.PopulateOwnership(mybean.comp_id)%>
									</select>
								</div>

<!-- 								<div class="form-group form-md-line-input"> -->
<!-- 									<label for="form_control_1"> -->
<!-- 										<div class="hint" id="hint_txt_enquiry_familymember_count"></div>Existing -->
<!-- 										Vehicle Make: -->
<!-- 									</label> <input type="text" class="form-control" -->
<!-- 										name="txt_enquiry_existingvehicle" -->
<!-- 										id="txt_enquiry_existingvehicle" -->
<%-- 										value="<%=mybean.enquiry_existingvehicle%>" size="32" --%>
<!-- 										maxlength="255" onchange="ExistingVehicleCheck(this);"> -->
<!-- 								</div> -->

								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_enquiry_purchasemonth"></div>Purchase Month:
									</label> <input type="text" class="form-control"
										name="txt_enquiry_purchasemonth"
										id="txt_enquiry_purchasemonth"
										value="<%=mybean.enquiry_purchasemonth%>" size="12" "maxlength="10"
										onfocusout="SecurityCheck('txt_enquiry_purchasemonth',this,'hint_txt_enquiry_purchasemonth');"
										onclick="datePicker('txt_enquiry_purchasemonth');" readonly>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_enquiry_loancompletionmonth"></div>Loan Completion:
									</label> <input type="text" class="form-control"
										name="txt_enquiry_loancompletionmonth"
										id="txt_enquiry_loancompletionmonth" value="<%=mybean.enquiry_loancompletionmonth%>" size="12"
										maxlength="14"
										onfocusout="SecurityCheck('txt_enquiry_loancompletionmonth',this,'hint_txt_enquiry_loancompletionmonth');"
										onclick="datePicker('txt_enquiry_loancompletionmonth');" readonly>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_enquiry_loancompletionmonth"></div>Current
										EMI (Rs):
									</label> <input type="text" class="form-control"
										name="txt_enquiry_currentemi" id="txt_enquiry_currentemi"
										value="<%=mybean.enquiry_currentemi%>" size="10"
										maxlength="10"
										onKeyUp="toInteger('txt_enquiry_currentemi','EMI')"
										onchange="SecurityCheck('txt_enquiry_currentemi',this,'hint_txt_enquiry_currentemi');">
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_enquiry_loanfinancer"></div>Financer
										Name (Loan):
									</label> <input type="text" class="form-control"
										name="txt_enquiry_loanfinancer" id="txt_enquiry_loanfinancer"
										value="<%=mybean.enquiry_loanfinancer%>" size="32"
										maxlength="255"
										onchange="SecurityCheck('txt_enquiry_loanfinancer',this,'hint_txt_enquiry_loanfinancer');">
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_enquiry_kms"></div>Kms:
									</label> <input type="tel" class="form-control" name="txt_enquiry_kms"
										id="txt_enquiry_kms" value="<%=mybean.enquiry_kms%>" size="10"
										maxlength="10" onKeyUp="toInteger('txt_enquiry_kms','Kms')"
										onchange="SecurityCheck('txt_enquiry_kms',this,'hint_txt_enquiry_kms');">
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_enquiry_kms"></div>Expected
										Price:
									</label> <input type="text" class="form-control"
										name="txt_enquiry_expectedprice"
										id="txt_enquiry_expectedprice"
										value="<%=mybean.enquiry_expectedprice%>" size="10"
										maxlength="10"
										onKeyUp="toInteger('txt_enquiry_expectedprice','Exprice')"
										onchange="SecurityCheck('txt_enquiry_expectedprice',this,'hint_txt_enquiry_expectedprice');">
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_enquiry_kms"></div>Quoted
										Price:
									</label> <input type="text" class="form-control"
										name="txt_enquiry_quotedprice" id="txt_enquiry_quotedprice"
										value="<%=mybean.enquiry_quotedprice%>" size="10"
										maxlength="10"
										onKeyUp="toInteger('txt_enquiry_quotedprice','Qprice')"
										onchange="SecurityCheck('txt_enquiry_quotedprice',this,'hint_txt_enquiry_quotedprice');">
								</div>

								<div class="form-group form-md-line-input">
									
										<label for="form_control_1"  left:20px;">
										<div class="hint" id="chk_enquiry_evaluation"></div>Evaluation:
									
									<input type="checkbox" name="chk_enquiry_evaluation"
										id="chk_enquiry_evaluation" 
									
										<%=mybean.PopulateCheck(mybean.enquiry_evaluation)%>
										onchange="SecurityCheck('chk_enquiry_evaluation',this,'hint_chk_enquiry_evaluation');">
										</label> 
								</div>

								<%
									}
								%>
					<!-- --                      End Maruti and Nexa Exchange Fields -->
								<br>

								<!---- Start Hyundai Fields -->
								<%
									if (mybean.branch_brand_id.equals("6")) {
								%>


								<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Need Assessment</strong>
											</h4>
										</center>
									</span>
								</div>


<!-- <div class="form-group form-md-line-input"> -->
<!-- 									<label for="form_control_1"><div class="hint" -->
<!-- 											id="hint_dr_enquiry_hyundai_chooseoneoption"></div> -->
<!-- 											Trade-In Model: </label>  -->
<!-- 										<select class="form-control select2" id="preownedvariant" -->
<!-- 														name="preownedvariant" onchange="SecurityCheck('preownedvariant',this,'hint_preownedvariant')"> -->
<%-- 														<%=mybean.variantcheck.PopulateVariant(mybean.enquiry_tradein_preownedvariant_id)%> --%>
														
<!-- 													</select> -->
<!-- 													<div class="hint" id="hint_preownedvariant"></div> -->
<!-- 												</div> -->
<!-- 											    </div>  -->
<!--                                                    <div class="col-md-6 col-xs-6" style="text-align: center"> -->
<%--                                                        <a class=" btn1" href="../preowned/preowned-dash-add.jsp?enquiry_id=<%=mybean.enquiry_id%>" data-target="#Hintclicktocall" data-toggle="modal">Add Pre-Owned</a> --%>
<!--                                                        </div> -->









								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_hyundai_chooseoneoption"></div>Please
										choose one option: </label> <select class="form-control"
										name="dr_enquiry_hyundai_chooseoneoption"
										id="dr_enquiry_hyundai_chooseoneoption"
										onchange="SecurityCheck('dr_enquiry_hyundai_chooseoneoption',this,'hint_dr_enquiry_hyundai_chooseoneoption');">
										<%=mybeanenqdashmethods.PopulateHyundaiChooseOneOption(mybean.enquiry_hyundai_chooseoneoption, mybean.comp_id)%>
									</select>
								</div>


								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_hyundai_kmsinamonth"></div>How many
										kilometers you drive in a month?: </label> <select
										class="form-control" name="dr_enquiry_hyundai_kmsinamonth"
										id="dr_enquiry_hyundai_kmsinamonth"
										onChange="SecurityCheck('dr_enquiry_hyundai_kmsinamonth',this,'hint_dr_enquiry_hyundai_kmsinamonth')">
										<%=mybeanenqdashmethods.PopulateHyundaiKmsInAMonth(mybean.enquiry_hyundai_kmsinamonth, mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_hyundai_membersinthefamily"></div>How
										many members are there in your family?: </label> <select
										class="form-control"
										name="dr_enquiry_hyundai_membersinthefamily"
										id="dr_enquiry_hyundai_membersinthefamily"
										onChange="SecurityCheck('dr_enquiry_hyundai_membersinthefamily',this,'hint_dr_enquiry_hyundai_membersinthefamily')">
										<%=mybeanenqdashmethods.PopulateHyundaiMembersInTheFamily(mybean.enquiry_hyundai_membersinthefamily, mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_hyundai_topexpectation"></div>What is
										your top most priority expectation from your car?: </label> <select
										class="form-control" name="dr_enquiry_hyundai_topexpectation"
										id="dr_enquiry_hyundai_topexpectation" class="selectbox"
										onChange="SecurityCheck('dr_enquiry_hyundai_topexpectation',this,'hint_dr_enquiry_hyundai_topexpectation')">
										<%=mybeanenqdashmethods.PopulateHyundaiTopExpectation(mybean.enquiry_hyundai_topexpectation, mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_hyundai_finalizenewcar"></div>By when are
										you expecting to finalize your new car?: </label> <select
										class="form-control" name="dr_enquiry_hyundai_finalizenewcar"
										id="dr_enquiry_hyundai_finalizenewcar" class="selectbox"
										onChange="SecurityCheck('dr_enquiry_hyundai_finalizenewcar',this,'hint_dr_enquiry_hyundai_finalizenewcar')">
										<%=mybeanenqdashmethods.PopulateHyundaiFinalizeNewCar(mybean.enquiry_hyundai_finalizenewcar, mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_hyundai_modeofpurchase"></div>What will
										be your mode of purchase?: </label> <select class="form-control"
										name="dr_enquiry_hyundai_modeofpurchase"
										id="dr_enquiry_hyundai_modeofpurchase"
										onChange="SecurityCheck('dr_enquiry_hyundai_modeofpurchase',this,'hint_dr_enquiry_hyundai_modeofpurchase')">
										<%=mybeanenqdashmethods.PopulateHyundaiModeOfPurchase(mybean.enquiry_hyundai_modeofpurchase, mybean.comp_id)%>
									</select>
								</div>



								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_hyundai_annualincome"></div>What is your
										appropriate annual household income (INR)?: </label> <select
										class="form-control" name="dr_enquiry_hyundai_annualincome"
										id="dr_enquiry_hyundai_annualincome"
										onChange="SecurityCheck('dr_enquiry_hyundai_annualincome',this,'hint_dr_enquiry_hyundai_annualincome')">
										<%=mybeanenqdashmethods.PopulateHyundaiAnnualIncome(mybean.enquiry_hyundai_annualincome, mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_txt_enquiry_hyundai_othercars"></div>Which other
										cars are you considering?:</label> <input type="text"
										class="form-control" name="txt_enquiry_hyundai_othercars"
										id="txt_enquiry_hyundai_othercars"
										value="<%=mybean.enquiry_hyundai_othercars%>" size="52" maxlength="255"
										onchange="SecurityCheck('txt_enquiry_hyundai_othercars',this,'hint_txt_enquiry_hyundai_othercars')" />
								</div>

								<br>
								<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Exchange Details</strong>
											</h4>
										</center>
									</span>
								</div>
								
								
								<!-- 								============================================================ -->
								
					<div class="container-fluid">
<br>
	                                         <div class="form-group">
											   <div class="col-md-6 col-xs-12"> 
						                     <div class="control-label col-md-4">
												<label class="">
<font style="color:#8E44AD">Trade-In Model:</font></label>
												</div>
												<div class="col-md-8">
													<select class="form-control select2" id="preownedvariant"
														name="preownedvariant" onchange="SecurityCheck('preownedvariant',this,'hint_preownedvariant')">
														<%=mybean.variantcheck.PopulateVariant(mybean.enquiry_tradein_preownedvariant_id)%>
														
													</select>
													<div class="hint" id="hint_preownedvariant"></div>
												</div><br>
 											    </div>  
<!--                                                     <div class="col-md-6 col-xs-6" style="text-align: center"><br>  -->
                                                      <center> <a class=" btn1" href="../preowned/preowned-dash-add.jsp?enquiry_id=<%=mybean.enquiry_id%>" data-target="#Hintclicktocall" data-toggle="modal">Add Evaluation</a></center>
<!--                                                        </div>  -->
                                                       </div>
                                                       </div>
					
					
					
					
					
					<!-- 								============================================================ -->	

<!-- 								<div class="form-group form-md-line-input"> -->
<!-- 									<label for="form_control_1"><div class="hint" -->
<!-- 											id="hint_txt_enquiry_hyundai_ex_manuf"></div>Manufacturer:</label><input -->
<!-- 										name="txt_enquiry_hyundai_ex_manuf" type="text" -->
<!-- 										class="form-control" id="txt_enquiry_hyundai_ex_manuf" -->
<%-- 										value="<%=mybean.enquiry_hyundai_ex_manuf%>" size="32" --%>
<!-- 										maxlength="255" -->
<!-- 										onChange="SecurityCheck('txt_enquiry_hyundai_ex_manuf',this,'hint_txt_enquiry_hyundai_ex_manuf')" /> -->
<!-- 								</div> -->

<!-- 								<div class="form-group form-md-line-input"> -->
<!-- 									<label for="form_control_1"><div class="hint" -->
<!-- 											id="hint_txt_enquiry_hyundai_ex_model"></div>Model / Variant:</label><input -->
<!-- 										name="txt_enquiry_hyundai_ex_model" type="text" -->
<!-- 										class="form-control" id="txt_enquiry_hyundai_ex_model" -->
<%-- 										value="<%=mybean.enquiry_hyundai_ex_model%>" size="32" --%>
<!-- 										maxlength="255" -->
<!-- 										onChange="SecurityCheck('txt_enquiry_hyundai_ex_model',this,'hint_txt_enquiry_hyundai_ex_model')" /> -->
<!-- 								</div> -->

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_hyundai_ex_manufyear"></div>Year of
										Manufacture: </label> <select class="form-control"
										name="dr_enquiry_hyundai_ex_manufyear"
										id="dr_enquiry_hyundai_ex_manufyear"
										onChange="SecurityCheck('dr_enquiry_hyundai_ex_manufyear',this,'hint_dr_enquiry_hyundai_ex_manufyear')">
										<%=mybeanenqdashmethods.PopulateHyundaiExManufYear(mybean.enquiry_hyundai_ex_manufyear, mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_txt_enquiry_hyundai_ex_purchasedate"></div>Purchase
										Month / Year:</label><input name="txt_enquiry_hyundai_ex_purchasedate"
										type="text" class="form-control"
										id="txt_enquiry_hyundai_ex_purchasedate"
										value="<%=mybean.enquiry_hyundai_ex_purchasedate%>" size="32" maxlength="255"
										onclick="datePicker('txt_enquiry_hyundai_ex_purchasedate');"  
										onfocusout="SecurityCheck('txt_enquiry_hyundai_ex_purchasedate',this,'hint_txt_enquiry_hyundai_ex_purchasedate')"  />
								</div>




								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_txt_enquiry_hyundai_ex_owner"></div>Owner:</label><input
										name="txt_enquiry_hyundai_ex_owner" type="text"
										class="form-control" id="txt_enquiry_hyundai_ex_owner"
										value="<%=mybean.enquiry_hyundai_ex_owner%>" size="32"
										maxlength="255"
										onChange="SecurityCheck('txt_enquiry_hyundai_ex_owner',this,'hint_txt_enquiry_hyundai_ex_owner')" />
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_txt_enquiry_hyundai_ex_loancompletion"></div>Loan
										completion Month / Year (if any):</label><input
										name="txt_enquiry_hyundai_ex_loancompletion" type="text"
										class="form-control"
										id="txt_enquiry_hyundai_ex_loancompletion"
										value="<%=mybean.enquiry_hyundai_ex_loancompletion%>"
										size="32" maxlength="255"
										onfocusout="SecurityCheck('txt_enquiry_hyundai_ex_loancompletion',this,'hint_txt_enquiry_hyundai_ex_loancompletion')"
										onclick="datePicker('txt_enquiry_hyundai_ex_loancompletion');" readonly />
								</div>


								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_txt_enquiry_hyundai_ex_kmsdone"></div>Kms. Done:</label> <input
										name="txt_enquiry_hyundai_ex_kmsdone" type="tel"
										class="form-control" id="txt_enquiry_hyundai_ex_kmsdone"
										value="<%=mybean.enquiry_hyundai_ex_kmsdone%>" size="32"
										maxlength="7"
										onChange="SecurityCheck('txt_enquiry_hyundai_ex_kmsdone',this,'hint_txt_enquiry_hyundai_ex_kmsdone')" />
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_txt_enquiry_hyundai_ex_financer"></div>Financer
										Name:</label><input name="txt_enquiry_hyundai_ex_financer" type="text"
										class="form-control" id="txt_enquiry_hyundai_ex_financer"
										value="<%=mybean.enquiry_hyundai_ex_financer%>" size="32"
										maxlength="255"
										onChange="SecurityCheck('txt_enquiry_hyundai_ex_financer',this,'hint_txt_enquiry_hyundai_ex_financer')" />
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_txt_enquiry_hyundai_ex_expectedprice"></div>Expected
										Price (INR)(A):</label><input
										name="txt_enquiry_hyundai_ex_expectedprice" type="tel"
										class="form-control" id="txt_enquiry_hyundai_ex_expectedprice"
										value="<%=mybean.enquiry_hyundai_ex_expectedprice%>" size="32"
										maxlength="9"
										onChange="SecurityCheck('txt_enquiry_hyundai_ex_expectedprice',this,'hint_txt_enquiry_hyundai_ex_expectedprice')" />
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_txt_enquiry_hyundai_ex_quotedprice"></div>Quoted
										Price (INR)(B):</label><input
										name="txt_enquiry_hyundai_ex_quotedprice" type="tel"
										class="form-control" id="txt_enquiry_hyundai_ex_quotedprice"
										value="<%=mybean.enquiry_hyundai_ex_quotedprice%>" size="32" maxlength="9"
										onChange="SecurityCheck('txt_enquiry_hyundai_ex_quotedprice',this,'hint_txt_enquiry_hyundai_ex_quotedprice')" />
								</div>
								<%
									}
								%>
						<!---- End Hyundai Fields --->
							
							<!----- Start Ford Fields -->
								 <%if(mybean.branch_brand_id.equals("7")) {%>
							<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Need Assessment of Customer (Ford)</strong>
											</h4>
										</center>
									</span>
								</div>
								
<!-- 								============================================================== -->
								
						
								
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Type of Customer<span>*</span>:</label> <select
										class="form-control" name="dr_enquiry_ford_custtype_id"
										id="dr_enquiry_ford_custtype_id"
										onChange="SecurityCheck('dr_enquiry_ford_custtype_id',this,'hint_dr_enquiry_ford_custtype_id')">
										<%=mybeanenqdashmethods.PopulateFordCustomerType(mybean.enquiry_ford_customertype, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_ford_custtype_id"></div>
								</div>
							
									<div class="form-group form-md-line-input">
									<label for="form_control_1">Intention to purchase:</label> <select
										class="form-control" name="dr_enquiry_ford_intentionpurchase"
										id="dr_enquiry_ford_intentionpurchase"
										onChange="SecurityCheck('dr_enquiry_ford_intentionpurchase',this,'hint_dr_enquiry_ford_intentionpurchase')">
										<%=mybeanenqdashmethods.PopulateFordIntentionPurchase(mybean.enquiry_ford_intentionpurchase, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_ford_intentionpurchase"></div>
								</div>
								
								
									<div class="form-group form-md-line-input">
									<label for="form_control_1">No. of Kms driven every day?:</label>
										<input name=txt_enquiry_ford_kmsdriven type="tel" class="form-control" id="txt_enquiry_ford_kmsdriven"
										value="<%=mybean.enquiry_ford_kmsdriven%>" size="32" maxlength="4"
										onChange="SecurityCheck('txt_enquiry_ford_kmsdriven ',this,'hint_txt_enquiry_ford_kmsdriven ')" />
										<div class="hint" id="hint_txt_enquiry_ford_kmsdriven "></div>
								</div>
									
							<div class="form-group form-md-line-input">
									<label for="form_control_1">New vehicle for self or someone else?:</label> <select
										class="form-control" name="dr_enquiry_ford_newvehfor" id="dr_enquiry_ford_newvehfor"
										onChange="SecurityCheck('dr_enquiry_ford_newvehfor',this,'hint_dr_enquiry_ford_newvehfor')">
										<%=mybeanenqdashmethods.PopulateFordNewVehFor(mybean.enquiry_ford_newvehfor, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_ford_newvehfor"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Amount of investment in new car?:</label>
										<input name=txt_enquiry_ford_investment type="tel" class="form-control" id="txt_enquiry_ford_investment"
										value="<%=mybean.enquiry_ford_investment%>" size="32" maxlength="9"
										onChange="SecurityCheck('txt_enquiry_ford_investment ',this,'hint_txt_enquiry_ford_investment')" />
										<div class="hint" id="hint_txt_enquiry_ford_investment"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Any specific colour choice?:</label> <select
										class="form-control" name="dr_enquiry_ford_colourofchoice" id="dr_enquiry_ford_colourofchoice"
										onChange="SecurityCheck('dr_enquiry_ford_colourofchoice',this,'hint_dr_enquiry_ford_colourofchoice')">
										<%=mybeanenqdashmethods.PopulateFordColourOfChoice(mybean.enquiry_ford_colourofchoice, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_ford_colourofchoice"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Cash / Finance?:</label> <select
										class="form-control" name="dr_enquiry_ford_cashorfinance" id="dr_enquiry_ford_cashorfinance"
										onChange="SecurityCheck('dr_enquiry_ford_cashorfinance',this,'hint_dr_enquiry_ford_cashorfinance')">
										<%=mybeanenqdashmethods.PopulateFordCashOrFinance(mybean.enquiry_ford_cashorfinance, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_ford_cashorfinance"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Which car you driving now?:</label>
										<input name=txt_enquiry_ford_currentcar type="text" class="form-control" id="txt_enquiry_ford_currentcar"
										value="<%=mybean.enquiry_ford_currentcar%>" size="32" maxlength="255"
										onChange="SecurityCheck('txt_enquiry_ford_currentcar',this,'hint_txt_enquiry_ford_currentcar')" />
										<div class="hint" id="hint_txt_enquiry_ford_currentcar"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Do you want to exchange your old car?:</label> <select
										class="form-control" name="dr_enquiry_ford_exchangeoldcar" id="dr_enquiry_ford_exchangeoldcar"
										onChange="SecurityCheck('dr_enquiry_ford_exchangeoldcar',this,'hint_dr_enquiry_ford_exchangeoldcar')">
										<%=mybeanenqdashmethods.PopulateFordExchangeOldCar(mybean.enquiry_ford_exchangeoldcar, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_ford_exchangeoldcar"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Which other cars you considering?:</label>
										<input name=txt_enquiry_ford_othercarconcideration type="text" class="form-control" id="txt_enquiry_ford_othercarconcideration"
										value="<%=mybean.enquiry_ford_othercarconcideration%>" size="32" maxlength="255"
										onChange="SecurityCheck('txt_enquiry_ford_othercarconcideration ',this,'hint_txt_enquiry_ford_othercarconcideration')" />
										<div class="hint" id="hint_txt_enqusiry_ford_othercarconcideration"></div>
								</div>
						<!-- Start Trade-in Details(ford) -->
						
						<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Trade-in Details (Ford)</strong>
											</h4>
										</center>
									</span>
								</div>
								
								
								<!-- 								<div class="container-fluid"> -->
<br>
	                                         <div class="form-group">
 											   <div class="col-md-6 col-xs-12"> 
						                     <div class="control-label col-md-4"><br>
												<label class="form_control_1"><font style="color:#8E44AD">Trade-In Model:</font></label>
												</div>
												<div class="col-md-8">
													<select class="form-control select2" id="preownedvariant"
														name="preownedvariant" onchange="SecurityCheck('preownedvariant',this,'hint_preownedvariant')">
														<%=mybean.variantcheck.PopulateVariant(mybean.enquiry_tradein_preownedvariant_id)%>
														
													</select>
													<div class="hint" id="hint_preownedvariant"></div>
												</div><br>
											    </div>  
<!--                                                     <div class="col-md-6 col-xs-6" style="text-align: center"> -->
                                                    <br> 
                                                      <center> <a class=" btn1" href="../preowned/preowned-dash-add.jsp?enquiry_id=<%=mybean.enquiry_id%>" data-target="#Hintclicktocall" data-toggle="modal">Add Evaluation</a></center>
<!--                                                        </div>  -->
                                                       </div>
                                                       </div>
								
								
								
								
<!-- 								============================================================== -->		
								
<!-- 							<div class="form-group form-md-line-input"> -->
<!-- 									<label for="form_control_1">Make:</label> -->
<!-- 										<input name=txt_enquiry_ford_ex_make type="text" class="form-control" id="txt_enquiry_ford_ex_make" -->
<%-- 										value="<%=mybean.enquiry_ford_ex_make%>" size="32" maxlength="255" --%>
<!-- 										onChange="SecurityCheck('txt_enquiry_ford_ex_make ',this,'hint_txt_enquiry_ford_ex_make')" /> -->
<!-- 										<div class="hint" id="hint_txt_enquiry_ford_ex_make "></div> -->
<!-- 								</div>	 -->
								
<!-- 								<div class="form-group form-md-line-input"> -->
<!-- 									<label for="form_control_1">Model:</label> -->
<!-- 										<input name=txt_enquiry_ford_ex_model type="text" class="form-control" id="txt_enquiry_ford_ex_model" -->
<%-- 										value="<%=mybean.enquiry_ford_ex_model%>" size="32" maxlength="255" --%>
<!-- 										onChange="SecurityCheck('txt_enquiry_ford_ex_model ',this,'hint_txt_enquiry_ford_ex_model')" /> -->
<!-- 										<div class="hint" id="hint_txt_enquiry_ford_ex_model "></div> -->
<!-- 								</div> -->
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Derivative:</label>
										<input name=txt_enquiry_ford_ex_derivative type="text" class="form-control" id="txt_enquiry_ford_ex_derivative"
										value="<%=mybean.enquiry_ford_ex_derivative%>" size="32" maxlength="255"
										onChange="SecurityCheck('txt_enquiry_ford_ex_derivative ',this,'hint_txt_enquiry_ford_ex_derivative')" />
										<div class="hint" id="hint_txt_enquiry_ford_ex_derivative "></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Year:</label> <select
										class="form-control" name="dr_enquiry_ford_ex_year" id="dr_enquiry_ford_ex_year"
										onChange="SecurityCheck('dr_enquiry_ford_ex_year',this,'hint_dr_enquiry_ford_ex_year')">
										<%=mybeanenqdashmethods.PopulateFordExYear(mybean.enquiry_ford_ex_year, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_ford_ex_year"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Odo KM reading:</label>
										<input name=txt_enquiry_ford_ex_odoreading type="tel" class="form-control" id="txt_enquiry_ford_ex_odoreading"
										value="<%=mybean.enquiry_ford_ex_derivative%>"
										onChange="SecurityCheck('txt_enquiry_ford_ex_odoreading ',this,'hint_txt_enquiry_ford_ex_odoreading')" />
										<div class="hint" id="hint_txt_enquiry_ford_ex_odoreading "></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Doors:</label> <select
										class="form-control" name="dr_enquiry_ford_ex_doors" id="dr_enquiry_ford_ex_doors"
										onChange="SecurityCheck('dr_enquiry_ford_ex_doors',this,'hint_dr_enquiry_ford_ex_doors')">
										<%=mybeanenqdashmethods.PopulateFordExDoors(mybean.enquiry_ford_ex_doors, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_ford_ex_doors"></div>
								</div>
								
							<div class="form-group form-md-line-input">
									<label for="form_control_1">Body Style:</label> <select
										class="form-control" name="dr_enquiry_ford_ex_bodystyle" id="dr_enquiry_ford_ex_bodystyle"
										onChange="SecurityCheck('dr_enquiry_ford_ex_bodystyle',this,'hint_dr_enquiry_ford_ex_bodystyle')">
										<%=mybeanenqdashmethods.PopulateFordExBodyStyle(mybean.enquiry_ford_ex_bodystyle, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_ford_ex_bodystyle"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Engine Size:</label>
										<input name=txt_enquiry_ford_ex_enginesize type="text" class="form-control" id="txt_enquiry_ford_ex_enginesize"
										value="<%=mybean.enquiry_ford_ex_enginesize%>" size="32" maxlength="255"
										onChange="SecurityCheck('txt_enquiry_ford_ex_enginesize',this,'hint_txt_enquiry_ford_ex_enginesize')" />
										<div class="hint" id="hint_txt_enquiry_ford_ex_enginesize "></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Fuel Type:</label> <select
										class="form-control" name="dr_enquiry_ford_ex_fueltype" id="dr_enquiry_ford_ex_fueltype"
										onChange="SecurityCheck('dr_enquiry_ford_ex_fueltype',this,'hint_dr_enquiry_ford_ex_fueltype')">
										<%=mybeanenqdashmethods.PopulateFordExFuelType(mybean.enquiry_ford_ex_fueltype, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_ford_ex_fueltype"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Drive:</label> <select
										class="form-control" name="dr_enquiry_ford_ex_drive" id="dr_enquiry_ford_ex_drive"
										onChange="SecurityCheck('dr_enquiry_ford_ex_drive',this,'hint_dr_enquiry_ford_ex_drive')">
										<%=mybeanenqdashmethods.PopulateFordExDrive(mybean.enquiry_ford_ex_drive, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_ford_ex_drive"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Transmission:</label> <select
										class="form-control" name="dr_enquiry_ford_ex_transmission" id="dr_enquiry_ford_ex_transmission"
										onChange="SecurityCheck('dr_enquiry_ford_ex_transmission',this,'hint_dr_enquiry_ford_ex_transmission')">
										<%=mybeanenqdashmethods.PopulateFordExTransmition(mybean.enquiry_ford_ex_transmission, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_ford_ex_transmission"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Colour:</label> <select
										class="form-control" name="dr_enquiry_ford_ex_colour" id="dr_enquiry_ford_ex_colour"
										onChange="SecurityCheck('dr_enquiry_ford_ex_colour',this,'hint_dr_enquiry_ford_ex_colour')">
										<%=mybeanenqdashmethods.PopulateFordExColour(mybean.enquiry_ford_ex_colour, mybean.comp_id)%>
									</select>
									<div class="hint" id="hint_dr_enquiry_ford_ex_colour"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Price Offered:</label>
										<input name=txt_enquiry_ford_ex_priceoffered type="tel" class="form-control" id="txt_enquiry_ford_ex_priceoffered"
										value="<%=mybean.enquiry_ford_ex_priceoffered%>" size="32" maxlength="9"
										onChange="SecurityCheck('txt_enquiry_ford_ex_priceoffered ',this,'hint_txt_enquiry_ford_ex_priceoffered')" />
										<div class="hint" id="hint_txt_enquiry_ford_ex_priceoffered"></div>
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Estimated Price:</label>
										<input name=txt_enquiry_ford_ex_estmtprice type="tel" class="form-control" id="txt_enquiry_ford_ex_estmtprice"
										value="<%=mybean.enquiry_ford_ex_estmtprice%>" size="32" maxlength="9"
										onChange="SecurityCheck('txt_enquiry_ford_ex_estmtprice ',this,'hint_txt_enquiry_ford_ex_estmtprice')" />
										<div class="hint" id="hint_txt_enquiry_ford_ex_estmtprice"></div>
								</div>
							<!-- End Ford Fields -->
							
							<%
									}
								%>
							
								<!-- 	//	////////////////////  Customer Details  /////////////////////    -->


								<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Customer</strong>
											</h4>
										</center>
									</span>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"
										value="<%=mybean.enquiry_customer_name%>"><div
											class="hint" id="hint_txt_enquiry_customer_name"></div>Customer:
									</label> <input type="text" class="form-control"
										name="txt_enquiry_customer_name"
										id="txt_enquiry_customer_name" size="32" maxlength="255"
										value="<%=mybean.enquiry_customer_name%>"
										onchange="SecurityCheck('txt_enquiry_customer_name',this,'hint_txt_enquiry_customer_name')">
								</div>

								<%-- <div class="form-group form-md-line-input">
										<label for="form_control_1"
											value="<%=mybean.enquiry_customer_name%>"><div
												class="hint" id="hint_txt_enquiry_customer_name"></div>Customer:
										</label> <input type="text" class="form-control"
											name="txt_enquiry_customer_name"
											id="txt_enquiry_customer_name"
											value="<%=mybean.enquiry_customer_name%>"
											onchange="SecurityCheck('txt_enquiry_customer_name',this,'hint_txt_enquiry_customer_name')">
									</div> --%>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_title"></div>Contact<span>*</span>:</label> <select
										class="form-control" name="dr_title" id="dr_title"
										onChange="SecurityCheck('dr_title',this,'hint_dr_title')">
										<%=mybean.PopulateContactTitle()%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_txt_contact_fname"></div>First Name: </label> <input
										type="text" class="form-control" name="txt_contact_fname"
										id="txt_contact_fname" value="<%=mybean.contact_fname%>"
										size="32" maxlength="255"
										onChange="SecurityCheck('txt_contact_fname',this,'hint_txt_contact_fname')">

								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_txt_contact_lname"></div>Last Name: </label> <input
										type="text" class="form-control" name="txt_contact_lname"
										id="txt_contact_lname" value="<%=mybean.contact_lname%>"
										size="32" maxlength="255"
										onchange="SecurityCheck('txt_contact_lname',this,'hint_txt_contact_lname')">

								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_txt_contact_mobile1"></div>Mobile 1:</label> <input
										type="tel" class="form-control" name="txt_contact_mobile1"
										id="txt_contact_mobile1" value="<%=mybean.contact_mobile1%>"
										placeholder="(91-9999999999)" maxlength="13" size="32"
										maxlength="10"
										onchange="SecurityCheck('txt_contact_mobile1',this,'hint_txt_contact_mobile1')">


								</div>
								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_txt_contact_mobile2"></div>Mobile 2:</label> <input
										type="tel" class="form-control" name="txt_contact_mobile2"
										id="txt_contact_mobile2" value="<%=mybean.contact_mobile2%>"
										placeholder="(91-9999999999)" maxlength="13" size="32"
										maxlength="10"
										onchange="SecurityCheck('txt_contact_mobile2',this,'hint_txt_contact_mobile2')">


								</div>
								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_contact_phone1"></div>Phone 1:
									</label> <input type="tel" class="form-control"
										name="txt_contact_phone1" id="txt_contact_phone1"
										value="<%=mybean.contact_phone1%>"
										placeholder="(080-33333333)" size="32" maxlength="12"
										onchange="SecurityCheck('txt_contact_phone1',this,'hint_txt_contact_phone1')">

								</div>
								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_contact_phone2"></div>Phone 2:
									</label> <input type="tel" class="form-control"
										name="txt_contact_phone2" id="txt_contact_phone2"
										value="<%=mybean.contact_phone2%>"
										placeholder="(080-33333333)" size="32" maxlength="12"
										onchange="SecurityCheck('txt_contact_phone2',this,'hint_txt_contact_phone2')">

								</div>
								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_contact_email1"></div>Email 1:
									</label> <input type="text" class="form-control"
										name="txt_contact_email1" id="txt_contact_email1"
										value="<%=mybean.contact_email1%>" size="35" maxlength="100"
										onchange="SecurityCheck('txt_contact_email1',this,'hint_txt_contact_email1')">

								</div>
								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_contact_email2"></div>Email 2:
									</label> <input type="text" class="form-control"
										name="txt_contact_email2" id="txt_contact_email2"
										value="<%=mybean.contact_email2%>" size="35" maxlength="100"
										onchange="SecurityCheck('txt_contact_email2',this,'hint_txt_contact_email2')">

								</div>
								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_contact_address"></div>Address<span>*</span>:
									</label> <input type="text" class="form-control"
										name="txt_contact_address" id="txt_contact_address"
										value="<%=mybean.contact_address%>" cols="40" rows="4"
										onchange="SecurityCheck('txt_contact_address',this,'hint_txt_contact_address')">

								</div>
								<%-- 
									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_dr_customer_zone_id"></div>Zone<span>*</span>:</label> <input
											type="text" class="form-control" name="mainzone"
											id="mainzone" value="<%=mybean.customer_zone_id%>"
											onchange="SecurityCheck('mainzone',this,'hint_dr_customer_zone_id')">
									</div> --%>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_txt_contact_pin"></div>Pin/Zip<span>*</span>:</label> <input
										type="text" class="form-control" name="txt_contact_pin"
										id="txt_contact_pin" value="<%=mybean.contact_pin%>"
										onChange="SecurityCheck('txt_contact_pin',this,'hint_txt_contact_pin');"
										onKeyUp="toInteger('txt_contact_pin','Pin')" size="10"
										maxlength="6">
											<br/>
								</div>

								<!-- 	//////////////////////////	Additional info  //////////////////////////   -->


								<%-- <div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_dr_enquirycustomertype_id"></div>Type:
										</label> <select class="form-control" name="dr_enquirycustomertype_id"
											id="dr_enquirycustomertype_id"
											onchange="SecurityCheck('dr_enquirycustomertype_id',this,'hint_dr_enquirycustomertype_id');">
											<%=mybean.PopulateCustomerType()%>
										</select>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_dr_enquiryincome_id"></div>Income:</label> <select
											class="form-control" name="dr_enquiryincome_id"
											id="dr_enquiryincome_id"
											onchange="SecurityCheck('dr_enquiryincome_id',this,'hint_dr_enquiryincome_id');">
											<%=mybean.PopulateIncome()%>
										</select>
									</div>
									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_dr_carusage_id"></div>Car Usage Conditions:</label> <select
											class="form-control" name="dr_carusage_id"
											id="dr_carusage_id"
											onchange="SecurityCheck('dr_carusage_id',this,'hint_dr_carusage_id');">
											<%=mybean.PopulateCarUsage()%>
										</select>
									</div>
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_dr_enquiry_drivetype"></div>Driving
											Pattern:
										</label> <select class="form-control" name="dr_enquiry_drivetype"
											id="dr_enquiry_drivetype"
											onChange="SecurityCheck('dr_enquiry_drivetype',this,'hint_dr_enquiry_drivetype');">
											<%=mybean.PopulateDrivingPattern()%>
										</select>
									</div>
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_dr_monthlydrive_id"></div>Avg.
											Driving (kms/month):
										</label> <select class="form-control" name="dr_monthlydrive_id"
											id="dr_monthlydrive_id"
											onchange="SecurityCheck('dr_monthlydrive_id',this,'hint_dr_monthlydrive_id');">
											<%=mybean.PopulateIncome()%>
										</select>
									</div>

									<!-- 								////////////////////   Also Considering Details /////////////////////// -->
									<!-- 									<div class="form-group form-md-line-input"> -->
									<!-- 										<label for="form_control_1"> -->
									<!-- 											<div class="hint" id="hint_enquiry_compbrand_id"></div>Brand: -->
									<!-- 										</label> <select class="form-control" name="dr_enquiry_compbrand_id" -->
									<!-- 											id="dr_enquiry_compbrand_id" -->
									<!-- 											onchange="SecurityCheck('dr_enquiry_compbrand_id',this,'hint_enquiry_compbrand_id');"> -->
																				<%=mybean.PopulateCompBrand()%>
									<!-- 										</select> -->
									<!-- 									</div> -->

									<!-- 									<div class="form-group form-md-line-input"> -->
									<!-- 										<label for="form_control_1" -->
																				value="<%=mybean.enquiry_compbrand_others%>">
									<!-- 											<div class="hint" id="hint_txt_enquiry_compbrand_others"></div>Other -->
									<!-- 											Brand: -->
									<!-- 										</label> <input type="text" class="form-control" -->
									<!-- 											name="txt_enquiry_compbrand_others" -->
									<!-- 											id="txt_enquiry_compbrand_others" -->
									<!-- 											onchange="SecurityCheck('txt_enquiry_compbrand_others',this,'hint_txt_enquiry_compbrand_others')"> -->

									<!-- 									</div> -->
									<!-- 									<div class="form-group form-md-line-input"> -->
									<!-- 										<label for="form_control_1" -->
																				value="<%=mybean.enquiry_compbrand_model%>"><div
									<!-- 												class="hint" id="hint_txt_enquiry_compbrand_model"></div>Brand -->
									<!-- 											Model:</label> <input type="text" class="form-control" -->
									<!-- 											name="txt_enquiry_compbrand_model" id="txt_enquiry_compbrand_model" -->
									<!-- 											onchange="SecurityCheck('txt_enquiry_compbrand_model',this,'hint_txt_enquiry_compbrand_model')"> -->

									<!-- 									</div> -->
									<div class="form-group form-md-line-input">
										<label for="form_control_1"
											value="<%=mybean.enquiry_compbrand_variant%>"><div
												class="hint" id="hint_txt_enquiry_compbrand_variant"></div>Brand
											Grade:</label> <input type="text" class="form-control"
											name="txt_enquiry_compbrand_variant"
											id="txt_enquiry_compbrand_variant"
											onchange="SecurityCheck('txt_enquiry_compbrand_variant',this,'hint_txt_enquiry_compbrand_variant')">
									</div>
									<br>

									<!-- 		//////////////////////	Presently Owned Details  /////////////////////////////// -->
									<div colspan="4" class="control-list">
										<b>Presently Owned:</b>
									</div>
									<!-- 									<div class="form-group form-md-line-input"> -->
									<!-- 										<div class="checkbox-list"> -->
									<!-- 											<div class="col-md-6 col-xs-6"> -->
									<!-- 												<input id="chk_so_critical" type="checkbox" -->
									<!-- 													name="chk_so_critical" -->
									<%-- 													<%=mybean.PopulateCheck(mybean.so_critical)%>
									<!-- 													onchange="SecurityCheck('chk_so_critical',this,'hint_chk_so_critical');" /> -->
									<!-- 												<label style="position: relative; bottom: 2px;">Sales Order Critical</label> -->
									<!-- 												<div class="hint" id="hint_chk_so_critical"></div> -->
									<!-- 											</div> -->
									<!-- 										</div> -->
									<!-- 									</div> -->
									<%=mybean.PopulateBrandsOwned()%> --%>

								<!-- 		//////////////////////	Status Details    ///////////////////////////////  -->
	
								<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
									
									<span class="panel-title">
										<center>
											<h4>
												<strong>Status</strong>
											</h4>
										</center>
									</span>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1">Stage: <%=mybean.stage_name%>
								</div>
								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_dr_priorityenquiry_id"></div>
										Priority<span>*</span>:
									</label> <select class="form-control" name="drop_priorityenquiry_id"
										id="drop_priorityenquiry_id"
										onchange="SecurityCheck('drop_priorityenquiry_id',this,'hint_drop_priorityenquiry_id');">
										<%=mybean.PopulateEnquiryPriority()%>
									</select>
								</div>
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Status<span>*</span>:
									</label> <select class="form-control" name="dr_enquiry_status_id"
										id="dr_enquiry_status_id" onChange="StatusUpdate();">
										<%=mybean.PopulateStatus()%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"
										value="<%=mybean.enquiry_status_desc%>"><div
											class="hint" id="hint_txt_enquiry_status_desc"></div> Status
										comments<span>*</span>: </label>
									<textarea class="form-control" rows="1"
										name="txt_enquiry_status_desc" id="txt_enquiry_status_desc"
										onKeyUp="charcount('txt_enquiry_status_desc', 'span_txt_enquiry_status_desc','<font color=red>({CHAR} characters left)</font>', '1000')"
										onchange="SecurityCheck('txt_enquiry_status_desc',this,'hint_txt_enquiry_status_desc');StatusUpdate();"><%=mybean.enquiry_status_desc%></textarea>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_lostcase1_id"></div>Lost Case 1<span>*<span>:</label>
									<span id="span_lostcase1"> <select class="form-control"
										name="dr_enquiry_lostcase1_id" id="dr_enquiry_lostcase1_id"
										onchange="populateCase2()">
											<%=mybean.PopulateLostCase1()%>
									</select>
									</span>
								</div>
								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_dr_enquiry_lostcase2_id"></div>Lost
										Case 2<span>*</span>:
									</label> <span id="span_lostcase2"> <select class="form-control"
										name="dr_enquiry_lostcase2_id" id="dr_enquiry_lostcase2_id"
										onchange="populateCase3()">
											<%=mybean.PopulateLostCase2()%>
									</select>
									</span>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_lostcase3_id"></div> Lost Case 3:</label> <span
										id="span_lostcase3"> <select class="form-control"
										name="dr_enquiry_lostcase3_id" id="dr_enquiry_lostcase3_id"
										onchange="StatusUpdate();">
											<%=mybean.PopulateLostCase3()%>
									</select>
									</span>
								</div>


								<div class="form-group form-md-line-input">
									<label for="form_control_1" value="<%=mybean.enquiry_dmsno%>"><div
											class="hint" id="hint_txt_enquiry_dmsno"></div>DMS No.<span>*<span>:</label>
									<input type="text" class="form-control"
										name="txt_enquiry_dmsno" id="txt_enquiry_dmsno"
										value="<%=mybean.enquiry_dmsno%>" size="32" maxlength="50"
										onchange="SecurityCheck('txt_enquiry_dmsno',this,'hint_txt_enquiry_dmsno')">

								</div>

								<%-- <div class="form-group form-md-line-input">
									<label for="form_control_1 col-md-12"
										value="<%=mybean.enquiry_action%>">
										<div class="hint" id="hint_txt_enquiry_action"></div>
										Corrective Action<span>*</span>:
									</label>
									<textarea class="form-control" rows="1"
										name="txt_enquiry_action" id="txt_enquiry_action"
										onchange="SecurityCheck('txt_enquiry_action',this,'hint_txt_enquiry_action');StatusUpdate();" /></textarea>

								</div> --%>
								<div class="form-group form-md-line-input">
									<label for="form_control_1">SOE: </label><label
										for="form_control_1"><span style="color: #000"><%=mybean.soe_name%></span></label>

								</div>
								<!-- 								<div class="form-group form-md-line-input"> -->
								<!-- 									<label for="form_control_1">SOB: </label><label -->
								<%-- 										for="form_control_1"><span style="color: #000"><%=mybean.sob_name%></span></label> --%>

								<!-- 								</div> -->
								<%-- 			<div class="form-group form-md-line-input">
									<label for="form_control_1">Campaign: </label><label
										for="form_control_1"><span style="color: #000"><%=mybean.campaign_name%></span></label>

								</div> --%>
								<%-- <div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_enquirycat_id"></div>Category<span>*</span>:</label>
									<select class="form-control" name="dr_enquiry_enquirycat_id"
										id="dr_enquiry_enquirycat_id"
										onchange="SecurityCheck('dr_enquiry_enquirycat_id',this,'hint_dr_enquiry_enquirycat_id')">
										<%=mybean.PopulateCategory()%>
									</select>
								</div> --%>
								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_enquiry_notes"></div> Notes<span>*</span>:
									</label>
									<textarea class="form-control" rows="1"
										name="txt_enquiry_notes" id="txt_enquiry_notes" cols="70"
										rows="4"
										onChange="SecurityCheck('txt_enquiry_notes',this,'hint_txt_enquiry_notes')"><%=mybean.enquiry_notes%></textarea>

								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1">Entre By:</label><%=mybean.entry_by%>
									<input type="hidden" class="form-control" name="entry_by"
										value="<%=mybean.entry_by%>">
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1">Entry Date:</label><%=mybean.entry_date%>
									<input type="hidden" class="form-control" name="entry_date"
										value="<%=mybean.entry_date%>">
								</div>
								<%
									if (mybean.modified_by != null && !mybean.modified_by.equals("")) {
								%>
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Modified by:</label> <input
										type="text" class="form-control"
										value="<%=mybean.modified_by%>">
								</div>
								
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Modified Date:</label> <input
										type="text" class="form-control"
										value="<%=mybean.modified_date%>">
								</div>
								
								<%
									}
								%>

							</form>
						</div>
						<!-- ----------- -->
					</div>
				</div>

				<!-- accordian 2-->
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion3"
								href="#collapse_3_2"><strong>FOLLOW-UP</strong></a>
						</h4>
					</div>
					<%if(mybean.followuptab.equals("active")){%>
					<div id="collapse_3_2" class="panel-collapse collapse in">
					<%}else{ %>
					<div id="collapse_3_2" class="panel-collapse collapse">
					<%}%>
						<div class="panel-body">

							<form role="form" class="form-horizontal" name="addfollowup"
								id="addfollowup" method="post">
								<center>
									<span><%=mybeanfollowup.msg%></span>
								</center>
								<%
									if (!mybeanfollowup.status.equals("")) {
								%>
								<%
									if (mybeanfollowup.status.equals("Update")) {
								%>

								<center>Follow-Up List</center>
								<%
									}
								%>
								<%=mybeanfollowup.followupHTML%><br>
								<center><%=mybeanfollowup.status%>
									Follow-Up
								</center>
								<div class="form-body">
									<%
										if (mybeanfollowup.status.equals("Update")) {
									%>
									<div class="form-group form-md-line-input">
										<label for="form_control_1">Action Taken<span>*</span>:
										</label> <select class="form-control" name="dr_actiontaken"
											id="dr_actiontaken">
											<%=mybeanfollowup
							.PopulateFollowupDesc(mybean.comp_id)%>
										</select>
									</div>



									<!-- 									<div class="form-group form-md-line-input"> -->
									<!-- 										<label for="form_control_1">Current Follow-up Status<span>*</span>: -->
									<!-- 										</label> <select class="form-control" name="dr_followupstatus" -->
									<%-- 											id="dr_followupstatus"><%=mybeanfollowup.PopulateFollowupStatus()%> --%>
									<!-- 										</select> -->
									<!-- 									</div> -->

									<!-- 									<div class="form-group form-md-line-input"> -->
									<!-- 										<label for="form_control_1">Current Feedback Type<span>*</span>: -->
									<!-- 										</label> <select class="form-control" name="dr_feedbacktype" -->
									<!-- 											type="date" id="dr_feedbacktype"> -->
									<%-- 											<%=mybeanfollowup.PopulateFeedbacktype()%> --%>
									<!-- 										</select> -->
									<!-- 									</div> -->

									<div class="form-group form-md-line-input">
										<label for="form_control_1"> Feedback Description:
										</label>
										<textarea class="form-control" rows="1"
											name="txt_followup_desc" id="txt_followup_desc" /><%=mybeanfollowup.followup_desc%></textarea>

									</div>
									<%
										}
									%>

									<div class="form-group form-md-line-input">
										<label for="form_control_1"
											value="<%=mybeanfollowup.followup_date%>"> Next
											Follow-up Date<span>*</span>:
										</label> <input type="" class="form-control"
											name="txt_followup_date" id="txt_followup_date"
											onclick="datePicker('txt_followup_date');" readonly>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1"
											value="<%=mybeanfollowup.temp_time%>"> Next
											Follow-up Time<span>*</span>:
										</label> <input type="" class="form-control"
											name="txt_followup_time" id="txt_followup_time" onclick="timePicker('txt_followup_time')" readonly>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1">Next Follow-up Type<span>*</span>:
										</label> <select class="form-control" name="dr_followuptype"
											id="dr_followuptype"><%=mybeanfollowup.PopulateFollowuptype(mybean.comp_id)%>
										</select>
									</div>
								</div>
								<br>

								<div class="form-actions noborder">
									<center>
										<input name="submitbutton" type="submit" class="btn1"
											id="submitbutton" value="Submit"
											onClick="return SubmitFormOnce(document.addfollowup, this);" />
										<input type="hidden" name="submit_button" value="yes">
									</center>
									<%} %>
									<br>
								</div>
							</form>




							<!-- 							<div class="row"> -->
							<!-- 								<div class="col-md-12 col-xs-12" style="border: 1px solid #8E44AD"> -->
							<!-- 								<br> -->
							<!-- 									<b>Time:</b>&nbsp; <span style="color: #000">3456557</span><br> -->
							<!-- 									<b>Follow-up Type:</b>&nbsp; <span style="color: #000">3456557</span><br> -->
							<!-- 									<b>Follow-up Description:</b>&nbsp; <span style="color: #000">3456557</span><br> -->
							<!-- 									<b>Feedback Type:</b>&nbsp; <span style="color: #000">3456557</span><br> -->
							<!-- 									<b>Follow-up Status:</b>&nbsp; <span style="color: #000">3456557</span><br> -->
							<!-- 									<b>Consultant:</b>&nbsp; <span style="color: #000">3456557</span><br> -->
							<!-- 									<b>Feedback By:</b>&nbsp; <span style="color: #000">3456557</span><br><br> -->
							<!-- 								</div> -->
							<!-- 							</div> -->

							<!-- 							<table class="table"> -->
							<!-- 								<tr> -->
							<%-- 									<td colspan="2" align="center"><b><%=mybeanfollowup.msg%></b></td> --%>



							<!-- 								</tr> -->
							<!-- 							</table> -->
							<%-- 							<div style="border: 1px solid #8E44AD"><%=mybeanfollowup.followupHTML%></div> --%>


						</div>

					</div>
					
					
				</div>
				<!-- end accordian 2-->


				<!-- accordian 3-->
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion3"
								href="#collapse_3_3"><strong>CRM FOLLOW-UP</strong></a>
						</h4>
					</div>
					
<%-- 					<%if(mybean.followuptab.equals("active")){%> --%>
<!-- 					<div id="collapse_3_2" class="panel-collapse collapse in"> -->
<%-- 					<%}else{ %> --%>
<!-- 					<div id="collapse_3_2" class="panel-collapse collapse"> -->
<%-- 					<%}%> --%>
					
						<%if(mybean.crmtab.equals("active")){%>
						<div id="collapse_3_3" class="panel-collapse collapse in">
						<%}else{ %>
						<div id="collapse_3_3" class="panel-collapse collapse">
						<%}%>
						<div class="panel-body">
						<center>
									<span><%=mybean.msg%></span>
								</center>
							<center>CRM</center>
<%-- 						<font color="#ff0000"><b><%=mybean.msg%></b></font>  --%>
<%-- <%=mybeancrmfollowup.crmfollowupHTML%> --%>
							<div>
							<%=mybeancrmfollowup.crmfollowupHTML %>
							</div>
						</div> 
					</div>
				</div>
				<!--end accordian 3-->

			</div>

		</div>
	</div>
	
	
<!-- 	<script src="../assets/js/jquery.min.js" type="text/javascript"></script> -->
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/jquery.app.js" type="text/javascript"></script>

<!-- 	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script> -->
<script src="js/jquery.min.js" type="text/javascript"></script>
	
	
	
	<script type="text/javascript" src="../Library/dynacheck-post.js"></script>
	<script src="js/dynacheck-post.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	 	<script src="js/axelamobilecall.js" type="text/javascript"></script> 
	 	<script src="js/components-select2.min.js" type="text/javascript"></script>
	 	<script src="js/select2.full.min.js" type="text/javascript"></script>
<script>












$(function() { 
$(".select2-container").css("width","100%");
});
</script>
</body>
<!-- END BODY -->
</html>