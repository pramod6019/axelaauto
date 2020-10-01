<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybeancrmfollowup"
	class="axela.axelaauto_app.App_Preowned_Dash_CRMFollowup"
	scope="request" />
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Preowned_Dash"
	scope="request" />
<%
	mybeancrmfollowup.doPost(request, response);
	mybean.doPost(request, response);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>Pre-Owned Dash | Axelaauto</title>
<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">
<link href="css/select2-bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<link href="css/select2.min.css" rel="stylesheet" type="text/css" />

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

<body onLoad="TestdriveReq();">
	<div class="header-wrap">
		<div class="panel-heading"
			style="margin-bottom: 20px; background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05); padding: 18px;">
			<span class="panel-title">
				<center>
					<strong>Pre-Owned Dashboard</strong>
				</center>
			</span>
		</div>
	</div>
	<div class="col-md-6" style="margin-top: 40px; margin-left: 4px;">
		<form role="form" class="form-horizontal" name="form1" id="form1"
			method="post">
			<div class="form-body">
				<div class="form-group">
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-5 control-label"
							for="form_control_1"><b>Pre-Owned No: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="id" class="form-control"><%=mybean.preowned_id%></label>
							<input name="preowned_id" type="hidden" id="preowned_id"
								value="<%=mybean.preowned_id%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-5 control-label"
							for="form_control_1"><b>Date: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="date" class="form-control"><%=mybean.date%></label> <input
								name="txt_preowned_date" type="hidden" class="form-control"
								id="txt_preowned_date" value="<%=mybean.date%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-5 control-label"
							for="form_control_1"><b>Customer: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="customer" class="form-control"> <%=mybean.preowned_customer_name%></label>
							<input type="hidden" class="form-control"
								name="txt_preowned_customer_name"
								id="txt_preowned_customer_name"
								value="<%=mybean.preowned_customer_name%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-5 control-label"
							for="form_control_1"><b>Contact: </b> </label>
						<div class="col-md-8 col-xs-7">
							<label for="customer" class="form-control"><%=mybean.preowned_title%><%=mybean.contact_fname%>&nbsp;<%=mybean.contact_lname%></label>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-5  control-label"
							for="form_control_1"><b>Mobile: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="mobile" class="form-control"><%=mybean.contact_mobile1%></label>
							<span style="position: absolute; left: 130px; top: 10px"
								onclick="callNo('<%=mybean.contact_mobile1%>')"> <img
								src="ifx/icon-call.png" class="img-responsive"></span> <input
								type="hidden" class="form-control" name="txt_contact_mobile1"
								id="txt_contact_mobile1" value="<%=mybean.contact_mobile1%>"
								size="32" maxlength="13" style="width: 250px">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-5  control-label"
							for="form_control_1"><b>Branch: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="mobile" class="form-control"><%=mybean.branch_name%></label>
							<input type="hidden" class="form-control"
								value="<%=mybean.branch_name%>">
						</div>
					</div>
					<br>



				</div>
			</div>
		</form>
	</div>
	<!-- accordian 3-->
	<!-- 							<a class="accordion-toggle accordion-toggle-styled collapsed" -->
	<!-- 								data-toggle="collapse" data-parent="#accordion3" -->
	<!-- 								href="#collapse_3_3"><strong>CRM FOLLOW-UP</strong></a> -->
	<!-- 						</h4> -->

	<%-- 					<%if(mybean.followuptab.equals("active")){%> --%>
	<!-- 					<div id="collapse_3_2" class="panel-collapse collapse in"> -->
	<%-- 					<%}else{ %> --%>
	<!-- 					<div id="collapse_3_2" class="panel-collapse collapse"> -->
	<%-- 					<%}%> --%>


	<!-- 							<center>CRM Follow-up</center> -->
	<%-- 						<font color="#ff0000"><b><%=mybean.msg%></b></font>  --%>
	<%-- <%=mybeancrmfollowup.crmfollowupHTML%> --%>
	<div class="  panel-heading"
		style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; margin-left: 0px">
		<span class="panel-title">
			<center>
				<h4>
					<strong>CRM Follow-up </strong>
				</h4>
			</center>
		</span>
	</div>
	<br>
	<center>
		<span><%=mybean.msg%></span>
	</center>
	<div class="col-md-12" style="margin-top: 40px;">
		<form role="form" class="form-horizontal" name="addfollowup"
			id="addfollowup" method="post">
			<div class="form-body">
				<div class="form-group">
					<%=mybeancrmfollowup.crmfollowupHTML%>
				</div>
			</div>
		</form>
	</div>

	<!-- 	<script src="../assets/js/jquery.min.js" type="text/javascript"></script> -->
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<script src="js/jquery.app.js" type="text/javascript"></script>
	<!-- 	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script> -->
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<!-- 	<script type="text/javascript" src="../Library/dynacheck-post.js"></script> -->
	<script src="js/dynacheck-post.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>
	<script src="js/components-select2.min.js" type="text/javascript"></script>
	<script src="js/select2.full.min.js" type="text/javascript"></script>
	<script>
		$(function() {
			$(".select2-container").css("width", "100%");
		});
	</script>
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

		function RefreshHistory() {
			var enquiry_id = document.form1.txt_enquiry_id.value;
		}
		// 	function PopulateBrandsOwned(name, obj) {
		// 		var value = 0;
		// 		var enquiry_id = GetReplacePost(document.form1.txt_enquiry_id.value);
		// 		var check1;
		// 		value = obj.value;
		// 		if (obj.checked == true) {
		// 			check1 = "1";
		// 		} else {
		// 			check1 = "0";
		// 		}
		// 		var str = "123";
		// 		var param = "name=" + name + "&compbrand_id=" + value + "$enquiry_id="
		// 				+ enquiry_id + "&checked=" + check1;
		// 		showHintPost('../sales/enquiry-dash-check.jsp?name=' + name + '&value='
		// 				+ value + '&enquiry_id=' + enquiry_id + '&checked=' + check1,
		// 				"123", param, 'hint_chk_compbrand_id');
		// 		setTimeout('RefreshHistory()', 1000);
		// 	}
	</script>

	<script>
		//         function TestdriveReq(name,obj,hint)
		//         {
		//         	 var testdrive_yes=document.getElementById("dr_enquiry_nexa_testdrivereq").value;
		//         	 if(testdrive_yes=="2")
		//         	  {
		//         		  document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.display='';
		//         		  document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.visibility='visible';
		//         		  document.getElementById("reason").style.display='';
		//         		  document.getElementById("hint_txt_enquiry_nexa_testdrivereqreason").style.display='';

		//         		  SecurityCheck(name,obj,hint);
		//         	  }
		//         	 else if(testdrive_yes=="0")
		//         		  {
		//         			  document.getElementById("reason").style.display='None';
		//              		 document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.display='None';
		//         		  }
		//         	 else
		//         		 {

		//         		 document.getElementById("reason").style.display='None';
		//         		 document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.display='None';
		//         		 document.getElementById("hint_txt_enquiry_nexa_testdrivereqreason").style.display='None';

		//         		 SecurityCheck(name,obj,hint);
		//         		 }
		//         }
	</script>
</body>
<!-- END BODY -->
</html>