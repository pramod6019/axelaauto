<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.insurance.New_Vehicle_Dash_Insurancefollowup"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" type="image/x-icon" href="../admin-ifx/axela.ico">
<link href="../assets/css/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />

<link rel="shortcut icon" type="image/x-icon" href="../admin-ifx/axela.ico">
<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />

<!-- tag CSS -->
<link href="../assets/css/bootstrap-tagsinput.css" rel="stylesheet" type="text/css" />
<!-- tag CSS -->

<link rel="stylesheet" type="text/css" href="../assets/css/footable.core.css"/>
<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap-timepicker.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/select2.min.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/select2-bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />

</head>


<body class="page-container-bg-solid page-header-menu-fixed">

	<div class="portlet box">
		<div class="portlet-title" style="text-align: center">
			<div class="caption" style="float: none">Insurance Details</div>
		</div>
		<div class="portlet-body portlet-empty" style="height: 400px;">
			<center><%=mybean.insurcustomerdetail%></center>
			<div class="form-group">

				<label class="col-md-1 text-right">Previous Insurance
					Company Name<b>:&nbsp;</b>
				</label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_previouscompname"
						id="txt_insurfollowup_previouscompname" type="text"
						class="form-control"
						value="<%=mybean.veh_insur_previouscompname%>" size="25"
						maxlength="20"
						onchange="SecurityCheck2('txt_insurfollowup_previouscompname',this,'hint_txt_insurfollowup_previouscompname')" />
					<span class="hint" id="hint_txt_insurfollowup_previouscompname"></span>
				</div>

				<label class="col-md-1 text-right" style="top: 6px;">Previous
					Year IDV<b>:&nbsp;</b>
				</label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_previousyearidv"
						id="txt_insurfollowup_previousyearidv" type="text"
						class="form-control" value="<%=mybean.veh_insur_previousyearidv%>"
						size="25" maxlength="20"
						onchange="SecurityCheck2('txt_insurfollowup_previousyearidv',this,'hint_txt_insurfollowup_previousyearidv')" />
					<span class="hint" id="hint_txt_insurfollowup_previousyearidv"></span>
				</div>

				<label class="col-md-1 text-right">Previous Gross Premium<b>:&nbsp;</b></label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_previousgrosspremium"
						id="txt_insurfollowup_previousgrosspremium" type="text"
						class="form-control"
						value="<%=mybean.veh_insur_previousgrosspremium%>" size="25"
						maxlength="20"
						onchange="SecurityCheck2('txt_insurfollowup_previousgrosspremium',this,'hint_txt_insurfollowup_previousgrosspremium')" />
					<span class="hint" id="hint_txt_insurfollowup_previousgrosspremium"></span>
				</div>

				<label class="col-md-1 text-right">Previous Plan Name<b>:&nbsp;</b></label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_previousplanname"
						id="txt_insurfollowup_previousplanname" type="text"
						class="form-control"
						value="<%=mybean.veh_insur_previousplanname%>" size="25"
						maxlength="20"
						onchange="SecurityCheck2('txt_insurfollowup_previousplanname',this,'hint_txt_insurfollowup_previousplanname')" />
					<span class="hint" id="hint_txt_insurfollowup_previousplanname"></span>
				</div>

			</div>
			<div class="col-md-12"></div>
			<br></br>

			<div class="form-group">
				<label class="col-md-1 text-right">Premium With Zero Dept<b>:&nbsp;</b></label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_premiumwithzerodept"
						id="txt_insurfollowup_premiumwithzerodept" type="text"
						class="form-control"
						value="<%=mybean.veh_insur_premiumwithzerodept%>" size="25"
						maxlength="20"
						onchange="SecurityCheck2('txt_insurfollowup_premiumwithzerodept',this,'hint_txt_insurfollowup_premiumwithzerodept')" />
					<span class="hint" id="hint_txt_insurfollowup_premiumwithzerodept"></span>
				</div>

				<label class="col-md-1 text-right" style="top: 6px;">Policy
					Expiry Date<b>:&nbsp;</b>
				</label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_policyexpirydate"
						id="txt_insurfollowup_policyexpirydate"
						value="<%=mybean.veh_insur_policyexpirydate%>" size="12"
						onclick="expirytime();expiryDate();" maxlength="14"
						class="form-control date-picker" data-date-format="dd/mm/yyyy"
						type="text" /> <span class="hint"
						id="hint_txt_insurfollowup_policyexpirydate"></span>
				</div>

				<label class="col-md-1 text-right" style="top: 6px;">Current
					IDV<b>:&nbsp;</b>
				</label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_currentidv"
						id="txt_insurfollowup_currentidv" type="text" class="form-control"
						value="<%=mybean.veh_insur_currentidv%>" size="25" maxlength="20"
						onchange="SecurityCheck2('txt_insurfollowup_currentidv',this,'hint_txt_insurfollowup_currentidv')" />
					<span class="hint" id="hint_txt_insurfollowup_currentidv"></span>
				</div>

				<label class="col-md-1 text-right" style="top: 6px;">Premium<b>:&nbsp;</b></label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_premium"
						id="txt_insurfollowup_premium" type="text" class="form-control"
						value="<%=mybean.veh_insur_premium%>" size="25" maxlength="20"
						onchange="SecurityCheck2('txt_insurfollowup_premium',this,'hint_txt_insurfollowup_premium')" />
					<span class="hint" id="hint_txt_insurfollowup_premium"></span>
				</div>

			</div>
			<div class="col-md-12"></div>
			<br></br>

			<div class="form-group">

				<label class="col-md-1 text-right" style="top: 8px">NCB<b>:&nbsp;</b></label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_ncb" id="txt_insurfollowup_ncb"
						type="text" class="form-control" value="<%=mybean.veh_insur_ncb%>"
						size="25" maxlength="20"
						onchange="SecurityCheck2('txt_insurfollowup_ncb',this,'hint_txt_insurfollowup_ncb')" />
					<span class="hint" id="hint_txt_insurfollowup_ncb"></span>
				</div>

				<label class="col-md-1 text-right">Insurance Company Offered<b>:&nbsp;</b></label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_companyoffered"
						id="txt_insurfollowup_companyoffered" type="text"
						class="form-control" value="<%=mybean.veh_insur_compoffered%>"
						size="25" maxlength="20"
						onchange="SecurityCheck2('txt_insurfollowup_companyoffered',this,'hint_txt_insurfollowup_companyoffered')" />
					<span class="hint" id="hint_txt_insurfollowup_companyoffered"></span>
				</div>

				<label class="col-md-1 text-right" style="top: 6px;">Plan
					Suggested<b>:&nbsp;</b>
				</label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_plansuggested"
						id="txt_insurfollowup_plansuggested" type="text"
						class="form-control" value="<%=mybean.veh_insur_plansuggested%>"
						size="25" maxlength="20"
						onchange="SecurityCheck2('txt_insurfollowup_plansuggested',this,'hint_txt_insurfollowup_plansuggested')" />
					<span class="hint" id="hint_txt_insurfollowup_plansuggested"></span>
				</div>

				<label class="col-md-1 text-right" style="top: 6px;">Variant<b>:&nbsp;</b></label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_variant"
						id="txt_insurfollowup_variant" type="text" class="form-control"
						value="<%=mybean.veh_insur_variant%>" size="25" maxlength="20"
						onchange="SecurityCheck2('txt_insurfollowup_variant',this,'hint_txt_insurfollowup_variant')" />
					<span class="hint" id="hint_txt_insurfollowup_variant"></span>
				</div>
			</div>
			<div class="col-md-12"></div>
			<br></br>

			<div class="form-group">

				<label class="col-md-1 text-right" style="top: 8px"> Reg.
					Number<b>:&nbsp; </b>
				</label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_reg_no"
						id="txt_insurfollowup_reg_no" type="text" class="form-control"
						onChange="SecurityCheck2('txt_insurfollowup_reg_no',this,'hint_txt_insurfollowup_reg_no');"
						value="<%=mybean.veh_insur_reg_no%>" size="20" maxlength="20" />
					<span class="hint" id="hint_txt_insurfollowup_reg_no"></span>
				</div>

				<label class="col-md-1 text-right" style="top: 8px"> Chassis
					Number<b>:&nbsp;</b>
				</label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_chassis_no"
						id="txt_insurfollowup_chassis_no"
						onChange="SecurityCheck2('txt_insurfollowup_chassis_no',this,'hint_txt_insurfollowup_chassis_no');"
						type="text" class="form-control"
						value="<%=mybean.veh_insur_chassis_no%>" size="20" maxlength="25" />
					<span class="hint" id="hint_txt_insurfollowup_chassis_no"></span>
				</div>

				<label class="col-md-1 text-right" style="top: 8px">Address<b>:&nbsp;</b></label>
				<div class="col-md-5">
					<textarea name="txt_insurfollowup_address" cols="50" rows="3"
						class="form-control" id="txt_insurfollowup_address"
						onchange="SecurityCheck2('txt_insurfollowup_address',this,'hint_txt_insurfollowup_address')"
						onKeyUp="charcount('txt_insurfollowup_address', 'span_txt_insurfollowup_address','<font color=red>({CHAR} characters left)</font>', '500')"><%=mybean.veh_insur_address%></textarea>
					<span id="span_txt_insurfollowup_address">(500 characters)</span><br></br>
					<span class="hint" id="hint_txt_insurfollowup_address"></span>

				</div>

			</div>
		</div>
	</div>
	
	<center><%=mybean.followup_info%></center>

	<div class="portlet box">
		<div class="portlet-title" style="text-align: center">
			<div class="caption" style="float: none">Add Insurance
				Follow-up</div>
		</div>
		<div class="portlet-body portlet-empty">
			<center style="text-align: right"></center>
			<center>
<%-- 				<%mybean.SOP("insur_msg==jsp=="+mybean.insenqdash.msg);%> --%>
				<font color="red"><%=mybean.insur_msg%></font>
<%-- 				<font color="red"><%=mybean.insenqdash.msg%></font> --%>
			</center>

			<!-- START PORTLET BODY -->
			<form class="form-horizontal" name="Frmcontact" method="post"
				action="../insurance/new-vehicle-dash-insurancefollowup.jsp?insurenquiry_id=<%=mybean.insurenquiry_id%>"
				onsubmit="return insurvalidation();">

				<div class="form-group" id="feedbacktype">
					<label class="control-label col-md-3">Feedback Type<font color="red">*</font>:&nbsp; </label>
					<div class="col-md-6 " style="top: 8px">
						<select name="dr_feedbacktype_id" class="form-control"
							id="dr_feedbacktype_id" visible="true">
							<option value=0>Select</option>
							<%=mybean.PopulateFeedbackType(mybean.comp_id, mybean.dr_feedbacktype_id)%>
						</select>
					</div>
				</div>
				<span id="feedbacktypeerrormsg" style="margin-left: 26%"></span>

				<div class="form-group" id="feedbackdesc">
					<label class="control-label col-md-3">Feedback<font color="red">*</font>:&nbsp; </label>
					<div class="col-md-6 " style="top: 8px">
						<textarea name="txt_feedback_desc" cols="50" rows="4"
							class="form-control" id="txt_feedback_desc"
							onKeyUp="charcount('txt_feedback_desc', 'span_txt_feedback_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.feedback_desc%></textarea>
						<span id="span_txt_feedback_desc">(1000 characters)</span><br>
						<span id="feedbackdescerrormsg"></span>
					</div>
				</div>

				<div class="form-group" id="insurnextfollowuptime">
					</label> <label class="control-label col-md-3" id="reason1">Next Follow-up Time <font color="red">*</font>:&nbsp; </label>
					<div class="col-md-6 " style="top: 8px">
						<input name="txt_insurfollowup_time" type="text"
							class="form-control date form_datetime insurfollowuptime"
							id="txt_insurfollowup_time" onclick="callme();"
							value="<%=mybean.txt_nextfollowup_time%>" size="20"
							maxlength="16">
					</div>
				</div>
				<span id="insurnextfollowerrormsg" style="margin-left: 26%"></span>

				<div class="form-group" id="insurnextfollowuptype">
					</label> <label class="control-label col-md-3" id="type1">Next Follow-up Type <font color="red">*</font>:&nbsp; </label>
					<div class="col-md-6 " style="top: 8px">
						<select name="dr_nextfollowup_type_id" class="form-control"
							id="dr_nextfollowup_type_id" visible="true">
							<%=mybean.PopulateNextFollowupType(mybean.comp_id, mybean.dr_nextfollowup_type)%>
						</select>
					</div>
				</div>
				<span id="insurnextfollowtypeerrormsg" style="margin-left: 26%"></span>

				<input id="service" name="service" type="hidden"
					value='<%=mybean.service%>' />
				<center>
					<input name="add_insurfollowup_button" type="submit"
						class="btn btn-success" id="add_insurfollowup_button" value="Add" />
					<input type="hidden" name=insurenquiry_id id="insurenquiry_id"
						value="<%=mybean.insurenquiry_id%>" />
				</center>
			</form>
		</div>
	</div>
	<input id="currenttime" type="hidden"
		value='<%=mybean.currenttimevalidate%>' />


<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script src="../assets/js/footable.js" type="text/javascript"></script>
<script src="../assets/js/components-date-time-pickers.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap-datepicker.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap-timepicker.js" type="text/javascript"></script>
<script src="../assets/js/components-date-time-pickers.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
<script src="../assets/js/select2.full.min.js" type="text/javascript"></script>
<script src="../assets/js/components-select2.min.js" type="text/javascript"></script>
<!-- <script type="text/javascript" src="../Library/Validate.js"></script> -->
<!-- <script type="text/javascript" src="../Library/dynacheck.js"></script> -->


	<script type="text/javascript">
		$(function() {
			$('table').footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>
	<script type="text/javascript">
		function insurvalidation(){
			var feedback_type = $("#dr_feedbacktype_id").val();
			var feedback_desc = $("#txt_feedback_desc").val();
			var nextfollowup_time = $("#txt_insurfollowup_time").val();
			var nextfollowup_type = $("#dr_nextfollowup_type_id").val();
			
			if(feedback_type == "0"){
				$("#feedbacktypeerrormsg").html("<font color='red'><b>Select Feedback Type!<b></font>");
				return false;
			}
			if(feedback_desc == ""){
				$("#feedbackdescerrormsg").html("<font color='red'><b>Enter Feedback!<b></font>");
				return false;
			}
			if(nextfollowup_time == ""){
				$("#insurnextfollowerrormsg").html("<font color='red'><b>Enter Next Followup Time!<b></font>");
				return false;
			}
			if(nextfollowup_type == "0"){
				$("#insurnextfollowtypeerrormsg").html("<font color='red'><b>Select Next Followup Type!<b></font>");
				return false;
			}
			
			
		}
	</script>


</body>
</html>
