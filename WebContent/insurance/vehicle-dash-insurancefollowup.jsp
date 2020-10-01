<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.insurance.Vehicle_Dash_Insurancefollowup" scope="request" />
<%
	mybean.doPost(request, response);
%>
<html>
<head>
<!-- <style>
#disposition {
	display: none;
}

#inspection {
	display: none;
}

#inspection_result {
	display: none;
}

#offer {
	display: none;
}

#appointment {
	display: none;
}

#insurnextfollowuptime {
	display: none;
}

#insurnextfollowuptype {
	display: none;
}

#fieldexe {
	display: none;
}

#insurremarks {
	display: none;
}

#notinserestedreason {
	display: none;
}
</style> -->
</head>



<body class="page-container-bg-solid page-header-menu-fixed" >

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

				<label class="col-md-1 text-right" style="top: 8px"> Reg. Number<b>:&nbsp; </b> </label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_reg_no"
						id="txt_insurfollowup_reg_no" type="text" class="form-control"
						onChange="SecurityCheck2('txt_insurfollowup_reg_no',this,'hint_txt_insurfollowup_reg_no');"
						value="<%=mybean.veh_insur_reg_no%>" size="20" maxlength="20" />
					<span class="hint" id="hint_txt_insurfollowup_reg_no"></span>
				</div>

				<label class="col-md-1 text-right" style="top: 8px"> Chassis Number<b>:&nbsp;</b> </label>
				<div class="col-md-2">
					<input name="txt_insurfollowup_chassis_no"
						id="txt_insurfollowup_chassis_no"
						onChange="SecurityCheck2('txt_insurfollowup_chassis_no',this,'hint_txt_insurfollowup_chassis_no');"
						type="text" class="form-control"
						value="<%=mybean.veh_insur_chassis_no%>" size="20"
						maxlength="25" /> 
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

	<div class="portlet box">
		<div class="portlet-title" style="text-align: center">
			<div class="caption" style="float: none">Add Insurance Follow-up</div>
		</div>
		<div class="portlet-body portlet-empty">
			<center style="text-align: right">
				<%-- 													<a href="../insurance/insurance-update.jsp?add=yes&insurpolicy_insurenquiry_id=<%=mybean.insurenquiry_id%>" --%>
				<!-- 														target="_blank\">Add New Insurance... </a> -->
			</center>
			<center>
				<font color="red"><%=mybean.insur_msg%></font>
			</center>
			<%-- 												<center><%=mybean.insurcustomerdetail%></center> --%>




			<!-- START PORTLET BODY -->
			<form class="form-horizontal" name="Frmcontact" method="post"
				action="../insurance/vehicle-dash-insurancefollowup.jsp?insurenquiry_id=<%=mybean.insurenquiry_id%>#tabs-10"
				onsubmit="return insurvalidation();">

				<!-- 												<form class="form-horizontal"  -->
				<!-- 												onsubmit="return validation();"> -->
				
				<div class="form-group" id="inspection">
					<label class="control-label col-md-3">Feedback Type<font color="red">*</font>:&nbsp;
					</label>
					<div class="col-md-6 " style="top: 8px">
						<select name="dr_feedback_id" class="form-control"
							id="dr_feedback_id" visible="true">
							<option value=0>Select</option>
							<%=mybean.PopulateFeedbackType(mybean.comp_id, mybean.dr_feedbacktype_id)%>
						</select>
					</div>
				</div>
				<span id="feedbacktypeerrormsg" style="margin-left: 26%"></span>
				
				<div class="form-group" id="insurremarks">
					<label class="control-label col-md-3">Feedback<font
						color="red">*</font>:&nbsp;
					</label>
					<div class="col-md-6 " style="top: 8px">
						<textarea name="txt_feedback_desc" cols="50" rows="4"
							class="form-control" id="txt_feedback_desc"
							onKeyUp="charcount('txt_feedback_desc', 'span_txt_feedback_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.feedback_desc%></textarea>
						<span id="span_txt_feedback_desc">(1000 characters)</span>
					    <span id="feedbackdescerrormsg" style="margin-left: 26%"></span>
					</div>
				</div>
				
				<div class="form-group" id="insurnextfollowuptime">
					</label> <label class="control-label col-md-3" id="reason1">Next Follow-up Time :&nbsp;</label>
					<div class="col-md-6 " style="top: 8px">
						<input name="txt_insurfollowup_time" type="text"
							class="form-control date form_datetime insurfollowuptime"
							id="txt_insurfollowup_time" onclick="callme();"
							value="<%=mybean.txt_nextfollowup_time%>" size="20"
							maxlength="16"
							onchange="NextFollowupTimeValidation();">
					</div>
				</div>
				<span id="insurnextfollowerrormsg" style="margin-left: 26%"></span>

				<div class="form-group" id="insurnextfollowuptype">
					</label> <label class="control-label col-md-3" id="type1">Next Follow-up Type :&nbsp;</label>
					<div class="col-md-6 " style="top: 8px">
						<select name="dr_insurfollowup_type" class="form-control"
							id="dr_insurfollowup_type" visible="true">
							<%=mybean.PopulateNextFollowupType(mybean.comp_id, mybean.dr_followuptype_id)%>
						</select>
					</div>
				</div>
				<span id="insurnextfollowtypeerrormsg" style="margin-left: 26%"></span>
				
				
				<%-- <div class="form-group">
					<label class="control-label col-md-3">Contactable<font
						color="red">*</font>:&nbsp;
					</label>
					<div class="col-md-6 " style="top: 8px">
						<select name="dr_insurfollowup_contactable_id"
							class="form-control" id="dr_insurfollowup_contactable_id"
							visible="true"
							onChange="InsurContactableValidation();InsurContactableBand();PopulateDisposition();">
							<option value=0>Select</option>
							<%=mybean.PopulateInsurContactable(mybean.comp_id, mybean.insurfollowup_contactable_id)%>
						</select>
					</div>
				</div>
				<span id="insurerrormsg" style="margin-left: 26%"></span>

				<div class="form-group" id="disposition">
					<label class="control-label col-md-3">Disposition<font
						color="red">*</font>:&nbsp;
					</label>
					<div class="col-md-6 " style="top: 8px">
						<span id="HintDisposition"> <%=mybean.mischeck.PopulateDisposition(mybean.insurfollowup_contactable_id, mybean.disposition_id, mybean.comp_id, request)%>
						</span>
					</div>
				</div>
				<span id="dispositionerrormsg" style="margin-left: 26%"></span>

				<div class="form-group" id="notinserestedreason">
					<label class="control-label col-md-3">Reason<font
						color="red">*</font>:&nbsp;
					</label>
					<div class="col-md-6 " style="top: 8px">
						<select name="dr_reason_id" class="form-control" id="dr_reason_id"
							visible="true">
							<%=mybean.PopulateReason(mybean.comp_id)%>
						</select>
					</div>
				</div>
				<span id="notinserestedreasonerrormsg" style="margin-left: 26%"></span>

				<div class="form-group" id="inspection">
					<label class="control-label col-md-3">Inspection<font
						color="red">*</font>:&nbsp;
					</label>
					<div class="col-md-6 " style="top: 8px">
						<select name="dr_inspection_id" class="form-control"
							id="dr_inspection_id" visible="true"
							onChange="InspectionBand();InspectionValidation();">
							<option value=0>Select</option>
							<%=mybean.PopulateInspection(mybean.comp_id, mybean.dr_inspection_id)%>
						</select>
					</div>
				</div>
				<span id="inspectionerrormsg" style="margin-left: 26%"></span>

				<div class="form-group" id="inspection_result">
					<label class="control-label col-md-3">Inspection Result
						:&nbsp;</label>
					<div class="col-md-6 " style="top: 8px">
						<select name="dr_inspection_result_id" class="form-control"
							id="dr_inspection_result_id" visible="true"
							onChange="InspectionResultBand();">
							<option value=0>Select</option>
							<%=mybean.PopulateInspectionResult(mybean.comp_id, mybean.dr_inspection_result_id)%>
						</select>
					</div>
				</div>

				<div class="form-group" id="offer">
					<label class="control-label col-md-3">Offer/Commitment
						:&nbsp;</label>
					<div class="col-md-6 " style="top: 8px">
						<%=mybean.PopulateOffer(mybean.comp_id, mybean.dr_offer_id)%>
					</div>
				</div>

				<div class="form-group" id="appointment">
					<label class="control-label col-md-3">Appointment
						Verification :&nbsp;</label>
					<div class="col-md-6 " style="top: 8px">
						<select name="dr_appoint_verification_id" class="form-control"
							id="dr_appoint_verification_id" visible="true"
							onchange="AppVerificationBand();">
							<option value=0>Select</option>
							<%=mybean.PopulateAppointVerification(mybean.comp_id, mybean.dr_appoint_verification_id)%>
						</select>
					</div>
				</div>

				<div class="form-group" id="insurnextfollowuptime">
					<label class="control-label col-md-3" id="reason2">Next
						Follow-up Time<font color="red">*</font>:&nbsp;
					</label> <label class="control-label col-md-3" id="reason1">Next
						Follow-up Time :&nbsp;</label>
					<div class="col-md-6 " style="top: 8px">
						<input name="txt_insurfollowup_time" type="text"
							class="form-control date form_datetime insurfollowuptime"
							id="txt_insurfollowup_time" onclick="callme();"
							value="<%=mybean.txt_nextfollowup_time%>" size="20"
							maxlength="16"
							onchange="NextFollowupTimeBand();NextFollowupTimeValidation();">
					</div>
				</div>
				<span id="insurnextfollowerrormsg" style="margin-left: 26%"></span>

				<div class="form-group" id="insurnextfollowuptype">
					<label class="control-label col-md-3" id="type">Next
						Follow-up Type<font color="red">*</font>:&nbsp;
					</label> <label class="control-label col-md-3" id="type1">Next
						Follow-up Type :&nbsp;</label>
					<div class="col-md-6 " style="top: 8px">
						<select name="dr_insurfollowup_type" class="form-control"
							id="dr_insurfollowup_type" visible="true"
							onchange="NextFollowupTypeBand();NextFollowupTypeValidation();">
							<%=mybean.PopulateFollowuptype(mybean.comp_id)%>
						</select>
					</div>
				</div>
				<span id="insurnextfollowtypeerrormsg" style="margin-left: 26%"></span>

				<div class="form-group" id="fieldexe">
					<label class="control-label col-md-3">Field Executive
						:&nbsp;</label>
					<div class="col-md-6 " style="top: 8px">
						<select name="dr_field_emp_id" class="form-control"
							id="dr_field_emp_id" visible="true" onchange="FieldExeBand();">
							<%=mybean.PopulateFieldExecutive(mybean.comp_id)%>
						</select>
					</div>
				</div>

				<div class="form-group" id="insurremarks">
					<label class="control-label col-md-3">Remarks<font
						color="red">*</font>:&nbsp;
					</label>
					<div class="col-md-6 " style="top: 8px">
						<textarea name="txt_insurfollowup_desc" cols="50" rows="4"
							class="form-control" id="txt_insurfollowup_desc"
							onKeyUp="charcount('txt_insurfollowup_desc', 'span_txt_insurfollowup_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.insurfollowup_desc%></textarea>
						<span id="span_txt_insurfollowup_desc">(1000 characters)</span> <span
							id="remarkserrormsg" style="margin-left: 26%"></span>
					</div>
				</div> --%>
				
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
		<center><%=mybean.followup_info%></center>
	</div>
	<input id="currenttime" type="hidden"
		value='<%=mybean.currenttimevalidate%>' />


</body>
</html>
