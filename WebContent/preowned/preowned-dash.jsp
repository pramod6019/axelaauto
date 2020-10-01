<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Dash"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />

<%@ include file="../Library/css.jsp"%>

</HEAD>

<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
	<%@include file="../portal/header.jsp"%>
	<%@ include file="../Library/preowned-dash.jsp"%>

	<!-- BEGIN CONTAINER -->
	<!-- BEGIN CONTENT -->
	<div class="page-content-wrapper">
		<!-- BEGIN CONTENT BODY -->
		<!-- BEGIN PAGE HEAD-->
		<%-- <div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">11111
												<h1>	<%=mybean.preowned_title%> (<%=mybean.preowned_id%>) </h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div> --%>
		<!-- END PAGE HEAD-->
		<!-- BEGIN PAGE CONTENT BODY -->
		<font color="#ff0000"><b> <span id="history_span"><%=mybean.msg%></span></b></font>
		<div class="page-content">

			<!--<ul class="page-breadcrumb breadcrumb"> -->
			<!--	<li><a href="../portal/home.jsp">Home</a> &gt;</li> -->
			<!--	<li><a href="../preowned/index.jsp">Pre Owned</a> &gt;</li> -->
			<!--	<li><a href="../preowned/preowned-list.jsp?all=yes">List Pre Owned</a> </li> -->
			<%--	<li><a href="../preowned/preowned-dash.jsp?pop=yes&amp;preowned_id=<%=mybean.preowned_id%>"><%=mybean.preowned_title%> (<%=mybean.preowned_id%>)</a>: </li> --%>
			<!--</ul> -->
			<!-- END PAGE BREADCRUMBS -->


			<!-- <div class="tab-content"> -->
			<%-- <div class="tab-pane active"
							id="../preowned/preowned-dash.jsp?pop=yes&amp;preowned_id=<%=mybean.preowned_id%>"> --%>
					<form name="form1" id="form1" method="post" class="form-horizontal">
						<div class="portlet box">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Pre-Owned Details</div>
							</div>
							<div class="portlet-body portlet-empty container-fluid">
		
								<!-- START PORTLET BODY -->
								<form name="form1" id="form1" method="post"
									class="form-horizontal">
									<% 
										if (mybean.msg.equals("")) { 
									%>
									<div class="row">
										<div class="form-element6 form-element-margin">
											<label>Pre-Owned No:</label>
											<span>
												<a href="preowned-list.jsp?preowned_id=<%=mybean.preowned_id%>"><b><%=mybean.preowned_no%></b></a>
												<input name="preowned_id" type="hidden" id="preowned_id" value="<%=mybean.preowned_id%>" />
											</span>
										</div>
			
										<div class="form-element6 form-element-margin">
											<label>Branch:</label>
											<span>
												<a href="../portal/branch-summary.jsp?branch_id=<%=mybean.preowned_branch_id%>"><%=mybean.branch_name%></a>
											</span>
										</div>
									</div>
									<div class="row">
										<div class="form-element6 form-element-margin">
											<label>Date:</label>
											<span>
												<input name="txt_preowned_date" type="hidden" class="form-control"
													id="txt_preowned_date" value="<%=mybean.date%>" />
												<%=mybean.date%>
											</span>
										</div>
			
										<div class="form-element6">
											<label>Closing Date<font color="#ff0000">*</font>: </label>
											<div>
												<input name="txt_preowned_exp_close_date" id="txt_preowned_exp_close_date"
													class="form-control datepicker" type="text"
													value="<%=mybean.closedate%>" size="12" maxlength="10" />
												<!--onChange="SecurityCheck('txt_preowned_exp_close_date',this,'hint_txt_preowned_exp_close_date');" -->
												<span class="hint" id="hint_txt_preowned_exp_close_date">
													(Days left: <%=mybean.days_diff%>)
												</span>
											</div>
										</div>
									</div>
									
									<div class="row">
										<div class="form-element6 form-element">
											<div class="form-element12">
												<label>Title<font color="#ff0000">*</font>: </label>
												<div>
													<input name="txt_preowned_title" type="text" class="form-control" id="txt_preowned_title"
														value="<%=mybean.preowned_title%>" size="52" maxlength="255"
														onchange="SecurityCheck('txt_preowned_title',this,'hint_txt_preowned_title')" />
													<span class="hint" id="hint_txt_preowned_title"></span>
												</div>
											</div>
			
											<div class="form-element12">
												<label>Pre-Owned Consultant<font color="#ff0000">*</font>: </label>
												<div>
													<select name="dr_preowned_emp_id" id="dr_preowned_emp_id" class="form-control"
														onChange="SecurityCheck('dr_preowned_emp_id',this,'hint_dr_preowned_emp_id');">
														<%=mybean.PopulateExecutive()%>
													</select>
													<span class="hint" id="hint_dr_preowned_emp_id"></span>
												</div>
											</div>
										</div>
										
										<div class="form-element6">
											<label>Description<font color="#ff0000">*</font>: </label>
											<div>
												<textarea name="txt_preowned_desc" cols="50" rows="5" class="form-control" id="txt_preowned_desc"
													onChange="SecurityCheck('txt_preowned_desc',this,'hint_txt_preowned_desc')"><%=mybean.preowned_desc%></textarea>
												<span class="hint" id="hint_txt_preowned_desc"></span>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="form-element6">
											<label>Model<font color="#ff0000">*</font>: </label>
											<div>
												<select class="form-control select2" id="preownedvariant" name="preownedvariant"
													onchange="SecurityCheck('preownedvariant',this,'hint_dr_variant_id');">
													<%=mybean.modelcheck.PopulateVariant(mybean.preowned_variant_id)%>
												</select>
												<!--<input tabindex="-1" class="bigdrop select2-offscreen" -->
												<!--	id="modelvariant" name="modelvariant" -->
												<%--	value="<%=mybean.preowned_variant_id%>" type="hidden" --%>
												<!--	onchange="SecurityCheck('modelvariant',this,'hint_dr_variant_id')" /> -->
												<span class="hint" id="hint_dr_variant_id"></span>
											</div>
										</div>
			
										<div class="form-element6">
											<label>Sub Variant: </label>
											<input name="txt_preowned_sub_variant" class="form-control" id="txt_preowned_sub_variant"
												value="<%=mybean.preowned_sub_variant%>" size="52" maxlength="255" type="text"
												onchange="SecurityCheck('txt_preowned_sub_variant',this,'hint_txt_preowned_sub_variant')" />
											<span class="hint" id="hint_txt_preowned_sub_variant"></span>
										</div>
									</div>
		
									<div class="row">
										<div class="form-element6">
											<label>Fuel Type<font color="#ff0000">*</font>: </label>
											<div>
												<select name="dr_preowned_fueltype_id" id="dr_preowned_fueltype_id" class="form-control"
													onchange="SecurityCheck('dr_preowned_fueltype_id',this,'hint_dr_preowned_fueltype_id');">
													<%=mybean.PopulateFuel()%>
												</select>
												<span class="hint" id="hint_dr_preowned_fueltype_id"></span>
											</div>
										</div>
			
										<div class="form-element6">
											<label>Exterior<font color="#ff0000">*</font>: </label>
											<div>
												<select name="dr_preowned_extcolour_id" id="dr_preowned_extcolour_id" class="form-control"
													onchange="SecurityCheck('dr_preowned_extcolour_id',this,'hint_dr_preowned_extcolour_id');">
													<%=mybean.PopulateExterior()%>
												</select>
												<div class="admin-master">
													<a href="../preowned/manageextcolour.jsp?all=yes" title="Manage Exterior"></a>
												</div>
												<span class="hint" id="hint_dr_preowned_extcolour_id"></span>
											</div>
										</div>
									</div>
									
									<div class="row">
										<div class="form-element6 form-element">
											<div class="form-element12">
												<label> Interior<font color="#ff0000">*</font>: </label>
												<div>
													<select name="dr_preowned_intcolour_id" id="dr_preowned_intcolour_id" class="form-control"
														onchange="SecurityCheck('dr_preowned_intcolour_id',this,'hint_dr_preowned_intcolour_id');">
														<%=mybean.PopulateInterior()%>
													</select>
													<div class="admin-master">
														<a href="../preowned/manageinteriorcolour.jsp?all=yes" title="Manage Interior"></a>
													</div>
													<span class="hint" id="hint_dr_preowned_intcolour_id"></span>
												</div>
											</div>
											
											<div class="form-element12">
												<label>Manuf. Year<font color="#ff0000">*</font>: </label>
												<div>
													<select name="txt_preowned_manufyear" type="text"
														class="form-control" id="txt_preowned_manufyear" maxlength="4"
														onchange="SecurityCheck('txt_preowned_manufyear',this,'hint_txt_preowned_manufyear')">
														<%=mybean.PopulateManufYear(mybean.preowned_manufyear)%>
													</select>
													<span class="hint" id="hint_txt_preowned_manufyear"></span>
												</div>
											</div>
										</div>
			
										<div class="form-element6">
											<label>Options: </label>
											<textarea name="txt_preowned_options" cols="50" rows="5" class="form-control" id="txt_preowned_options"
												onchange="SecurityCheck('txt_preowned_options',this,'hint_txt_preowned_options')"><%=mybean.preowned_options%></textarea>
											<span class="hint" id="hint_txt_preowned_options"></span>
										</div>
									
									</div>
		
									<div class="row">
										<div class="form-element6">
											<label> Regd. Year:</label>
											<div>
												<select name="txt_preowned_regdyear" type="text"
													class="form-control" id="txt_preowned_regdyear" maxlength="4"
													onchange="SecurityCheck('txt_preowned_regdyear',this,'hint_txt_preowned_regdyear')">
													<%=mybean.PopulateRegYear(mybean.preowned_regdyear)%>
												</select>
												<!--<input name="txt_preowned_regdyear" type="text" class="form-control"  id ="txt_preowned_regdyear" value = "<%//=mybean.preowned_regdyear%>" size="4" maxlength="4"  onchange="SecurityCheck('txt_preowned_regdyear',this,'hint_txt_preowned_regdyear')"  />-->
												<span class="hint" id="hint_txt_preowned_regdyear"></span>
											</div>
										</div>
			
										<div class="form-element6">
											<label> Registration No<font color=#ff0000><b>*</b></font>: </label>
											<div>
												<input name="txt_preowned_regno" class="form-control" id="txt_preowned_regno"
													value="<%=mybean.preowned_regno%>" size="15" maxlength="10" type="text"
													onchange="SecurityCheck('txt_preowned_regno',this,'hint_txt_preowned_regno')" />
												<span class="hint" id="hint_txt_preowned_regno"></span>
											</div>
										</div>
									</div>
									
									<div class="row">
										<div class="form-element6">
											<label>Kms: </label>
											<div>
												<input name="txt_preowned_kms" type="text" class="form-control"
													id="txt_preowned_kms" value="<%=mybean.preowned_kms%>" size="15" maxlength="10"
													onKeyUp="toInteger('txt_preowned_kms','kms')"
													onchange="SecurityCheck('txt_preowned_kms',this,'hint_txt_preowned_kms')" />
												<span class="hint" id="hint_txt_preowned_kms"></span>
											</div>
										</div>
			
										<div class="form-element6">
											<label>Foreclosure Amount: </label>
											<div>
												<input name="txt_preowned_fcamt" type="text"
													class="form-control" id="txt_preowned_fcamt"
													value="<%=mybean.preowned_fcamt%>" size="15" maxlength="10"
													onKeyUp="toInteger('txt_preowned_fcamt','Famt')"
													onchange="SecurityCheck('txt_preowned_fcamt',this,'hint_txt_preowned_fcamt')" />
												<span class="hint" id="hint_txt_preowned_fcamt"></span>
											</div>
										</div>
									</div>
									
									<div class="row">
										<div class="form-element6">
											<label>NOC: </label>
											<div>
												<select name="dr_preowned_noc" id="dr_preowned_noc" class="form-control"
													onchange="SecurityCheck('dr_preowned_noc',this,'hint_dr_preowned_noc')">
													<%=mybean.PopulateNoc()%>
												</select>
												<span class="hint" id="hint_dr_preowned_noc"></span>
											</div>
										</div>
			
										<div class="form-element6">
											<label>Funding Bank: </label>
											<div>
												<input name="txt_preowned_funding_bank" type="text"
													class="form-control" id="txt_preowned_funding_bank"
													value="<%=mybean.preowned_funding_bank%>" size="52" maxlength="255"
													onchange="SecurityCheck('txt_preowned_funding_bank',this,'hint_txt_preowned_funding_bank')" />
												<span class="hint" id="hint_txt_preowned_funding_bank"></span>
												<input name="preowned_funding_bank" type="hidden" class="form-control"
													id="preowned_funding_bank" value="<%=mybean.preowned_funding_bank%>" />
											</div>
										</div>
									</div>
									
									<div class="row">
										<div class="form-element6">
											<label>Loan No.: </label>
											<div>
												<input name="txt_preowned_loan_no" class="form-control" id="txt_preowned_loan_no"
													value="<%=mybean.preowned_loan_no%>" size="15" maxlength="20" type="text"
													onchange="SecurityCheck('txt_preowned_loan_no',this,'hint_txt_preowned_loan_no')" />
												<span class="hint" id="hint_txt_preowned_loan_no"></span>
											</div>
										</div>
			
										<div class="form-element6">
											<label>Insurance Date<font color=#ff0000><b>*</b></font>: </label>
											<div>
												<input name="txt_preowned_insur_date" id="txt_preowned_insur_date" maxlength="10"
													class="form-control datepicker" type="text" value="<%=mybean.insurdate%>" size="12" />
												<!--onChange="SecurityCheck('txt_preowned_insur_date',this,'hint_txt_preowned_insur_date');" /> -->
												<span class="hint" id="hint_txt_preowned_insur_date"></span>
											</div>
										</div>
									</div>
									
									<div class="row">
										<div class="form-element6">
											<label>Insurance Type: </label>
											<div>
												<select name="dr_preowned_insurance_id" id="dr_preowned_insurance_id" class="form-control"
													onChange="SecurityCheck('dr_preowned_insurance_id',this,'hint_dr_preowned_insurance_id');">
													<%=mybean.PopulateInsuranceType()%>
												</select>
												<span class="hint" id="hint_dr_preowned_insurance_id"></span>
											</div>
										</div>
			
										<div class="form-element6">
											<label> Ownership<font color="#ff0000">*</font>: </label>
											<div>
												<select name="dr_preowned_ownership_id" id="dr_preowned_ownership_id" class="form-control"
													onChange="SecurityCheck('dr_preowned_ownership_id',this,'hint_dr_preowned_ownership_id');">
													<%=mybean.PopulateOwnership()%>
												</select>
												<span class="hint" id="hint_dr_preowned_ownership_id"></span>
											</div>
										</div>
									</div>
									
									<div class="row">
										<div class="form-element6">
											<label> Invoice Value:</label>
											<div>
												<input name="txt_preowned_invoicevalue" type="text"
													class="form-control" id="txt_preowned_invoicevalue"
													value="<%=mybean.preowned_invoicevalue%>" size="15" maxlength="10"
													onKeyUp="toInteger('txt_preowned_invoicevalue','invoicevalue')"
													onchange="SecurityCheck('txt_preowned_invoicevalue',this,'hint_txt_preowned_invoicevalue')" />
												<span class="hint" id="hint_txt_preowned_invoicevalue"></span>
											</div>
										</div>
			
										<div class="form-element6">
											<label>Expected Price: </label>
											<div>
												<input name="txt_preowned_expectedprice" class="form-control" id="txt_preowned_expectedprice"
													value="<%=mybean.preowned_expectedprice%>" size="15" maxlength="10" type="text"
													onKeyUp="toInteger('txt_preowned_expectedprice','expectedvalue')"
													onchange="SecurityCheck('txt_preowned_expectedprice',this,'hint_txt_preowned_expectedprice')" />
												<span class="hint" id="hint_txt_preowned_expectedprice"></span>
											</div>
										</div>
									</div>
		
										<!--<div> -->
										<!--	<label>Quoted Price: </label> -->
										<!--	<div> -->
										<!--		<input name="txt_preowned_quotedprice" type="text" -->
										<!--			class="form-control" id="txt_preowned_quotedprice" -->
										<%--			value="<%=mybean.preowned_quotedprice%>" size="15" --%>
										<!--			maxlength="10" -->
										<!--			onKeyUp="toInteger('txt_preowned_quotedprice','quotedprice')" -->
										<!--			onchange="SecurityCheck('txt_preowned_quotedprice',this,'hint_txt_preowned_quotedprice')" /> -->
										<!--		<span class="hint" id="hint_txt_preowned_quotedprice"></span> -->
										<!--	</div> -->
										<!--</div> -->
		
						</div>
		
						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Customer</div>
							</div>
							<div class="portlet-body portlet-empty container-fluid">
								<div class="tab-pane" id="">
									<!-- START PORTLET BODY -->
									
									<div class="form-element6">
										<label>Customer<font color="#ff0000">*</font>: </label>
										<div>
											<input name="txt_preowned_customer_name" type="text"
												class="form-control" id="txt_preowned_customer_name"
												value="<%=mybean.preowned_customer_name%>" size="32" maxlength="255"
												onchange="SecurityCheck('txt_preowned_customer_name',this,'hint_txt_preowned_customer_name')" />
											<span class="hint" id="hint_txt_preowned_customer_name"></span>
										</div>
									</div>
									<div class="row">
										<div class="form-element12 form-element">
											<div class="form-element2">
												<label>Contact<font color="#ff0000">*</font>: </label>
												<div>
													<select name="dr_title" class="form-control" id="dr_title"
														onChange="SecurityCheck('dr_title',this,'hint_dr_title')">
														<%=mybean.PopulateContactTitle()%>
													</select>
													<span>Title</span>
													<span class="hint" id="hint_dr_title"></span>
												</div>
											</div>
				
											<div class="form-element5 form-element-margin">
												<input name="txt_contact_fname" type="text" class="form-control"
													value="<%=mybean.contact_fname%>" onkeyup="ShowNameHint()"
													size="32" maxlength="255" id="txt_contact_fname"
													onChange="SecurityCheck('txt_contact_fname',this,'hint_txt_contact_fname')" />
												<span>First Name</span>
												<span class="hint" id="hint_txt_contact_fname"></span>
											</div>
				
											<div class="form-element5 form-element-margin">
												<input name="txt_contact_lname" type="text" class="form-control"
													value="<%=mybean.contact_lname%>" size="30" maxlength="255"
													onkeyup="ShowNameHint()" id="txt_contact_lname"
													onchange="SecurityCheck('txt_contact_lname',this,'hint_txt_contact_lname')" />
												<span>Last Name</span>
												<span class="hint" id="hint_txt_contact_lname"></span>
											</div>
										</div>
									</div>
		
									<div class="row">
										<div class="form-element6">
											<label> Mobile 1<font color="#ff0000">*</font>: </label>
											<input name="txt_contact_mobile1" data-code="91-" type="text" class="form-control"
												id="txt_contact_mobile1" value="<%=mybean.contact_mobile1%>" size="32" maxlength="13"
												onKeyUp="showHint('../preowned/preowned-check.jsp?contact_mobile=' + GetReplace(this.value),'showcontacts');toPhone('txt_contact_mobile1','Mobile1');"
												onchange="SecurityCheck('txt_contact_mobile1',this,'hint_txt_contact_mobile1')" />
											(91-9999999999)
											<span class="hint" id="hint_txt_contact_mobile1"></span>
										</div>
			
										<div class="form-element6">
											<label>Mobile 2: </label>
											<div>
												<input name="txt_contact_mobile2" type="text" class="form-control" maxlength="13"
													value="<%=mybean.contact_mobile2%>" size="32" id=txt_contact_mobile1
													onKeyUp="showHint('../preowned/preowned-check.jsp?contact_mobile=' + GetReplace(this.value),'showcontacts');toPhone('txt_contact_mobile2','Mobile2');"
													onchange="SecurityCheck('txt_contact_mobile2',this,'hint_txt_contact_mobile2')" />
												(91-9999999999)
												<span class="hint" id="hint_txt_contact_mobile2"></span>
											</div>
										</div>
									</div>
		
									<div class="row">
										<div class="form-element6">
											<label>Email 1: </label>
											<div>
												<input name="txt_contact_email1" type="text" class="form-control" id="txt_contact_email1"
													value="<%=mybean.contact_email1%>" size="35" maxlength="100"
													onKeyUp="showHint('../preowned/preowned-check.jsp?contact_email=' + GetReplace(this.value),'showcontacts');"
													onchange="SecurityCheck('txt_contact_email1',this,'hint_txt_contact_email1')">
												<span class="hint" id="hint_txt_contact_email1"></span>
											</div>
										</div>
			
										<div class="form-element6">
											<label> Email 2: </label>
											<div>
												<input name="txt_contact_email2" type="text" class="form-control" maxlength="100"
													value="<%=mybean.contact_email2%>" size="35" id="txt_contact_email2"
													onKeyUp="showHint('../preowned/preowned-check.jsp?contact_email=' + GetReplace(this.value),'showcontacts');"
													onchange="SecurityCheck('txt_contact_email2',this,'hint_txt_contact_email2')">
												<span class="hint" id="hint_txt_contact_email2"></span>
											</div>
										</div>
									</div>
		
									
									<div class="row">
										<div class="form-element6">
											<label>Address<font color="#ff0000">*</font>: </label>
											<div>
												<textarea name="txt_contact_address" cols="40" rows="4" class="form-control" id="txt_contact_address"
													onchange="SecurityCheck('txt_contact_address',this,'hint_txt_contact_address')"
													onKeyUp="charcount('txt_contact_address', 'span_txt_contact_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.contact_address%></textarea>
												<span id="span_txt_contact_address"> (255 Characters)</span>
												<span class="hint" id="hint_txt_contact_address"></span>
											</div>
										</div>
			
										<div class="form-element6">
											<label>City<font color="#ff0000">*</font>: </label>
											<div>
												<select class="form-control select2" id="maincity"
													name="maincity"
													onchange="SecurityCheck('maincity',this,'hint_maincity')">
													<%=mybean.citycheck.PopulateCities(mybean.contact_city_id, mybean.comp_id)%>
												</select>
												<!--<input tabindex="-1" class="bigdrop select2-offscreen" -->
												<!--id="maincity" name="maincity" style="width: 250px" -->
												<%--value="<%=mybean.contact_city_id)%>" type="hidden" --%>
												<!--onChange="SecurityCheck('maincity',this,'hint_maincity')"> -->
												<span class="hint" id="hint_maincity"></span>
											</div>
										</div>
										
										<div class="form-element6">
											<label>Pin/Zip<font color="#ff0000">*</font>: </label>
											<div>
												<input name="txt_contact_pin" type="text" class="form-control"
													id="txt_contact_pin" value="<%=mybean.contact_pin%>"
													onChange="SecurityCheck('txt_contact_pin',this,'hint_txt_contact_pin');"
													size="10" onKeyUp="toInteger('txt_contact_pin','pincode')" maxlength="6" />
												<span class="hint" id="hint_txt_contact_pin"></span>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
				</div>
		
				<div class="portlet box">
					<div class="portlet-title" style="text-align: center">
						<div class="caption" style="float: none">Status</div>
					</div>
					<div class="portlet-body portlet-empty container-fluid">
						<div class="tab-pane" id="">
							<!-- START PORTLET BODY -->
							<!--<div class="container-fluid "> -->
							<div class="row">
								<div class="form-element6">
									<label> Status<font color="#ff0000">*</font>: </label>
									<div>
										<select name="dr_preowned_preownedstatus_id"
											onChange="StatusUpdate();" class="form-control"
											id="dr_preowned_preownedstatus_id">
											<%=mybean.PopulateStatus(mybean.comp_id)%>
										</select>
										<span class="hint" id="hint_dr_preowned_preownedstatus_id"></span>
									</div>
								</div>
			
								<div class="form-element6">
									<label>Priority<font color="#ff0000">*</font>: </label>
									<div>
										<select name="dr_preowned_prioritypreowned_id"
											class="form-control" id="dr_preowned_prioritypreowned_id"
											onChange="SecurityCheck('dr_preowned_prioritypreowned_id',this,'hint_dr_preowned_prioritypreowned_id');">
											<%=mybean.PopulatePreownedPriority()%>
										</select>
										<span class="hint" id="hint_dr_preowned_prioritypreowned_id"></span>
									</div>
								</div>
							</div>
		
							<div class="row">
								<div class="form-element6 form-element">
									<div class="form-element12">
										<label> Lost Case 1<font color="#ff0000">*</font>: </label>
										<div>
											<span id="span_lostcase1">
												<select name="dr_preowned_lostcase1_id" class="form-control" id="dr_preowned_lostcase1_id"
													onchange="populateLostCase2('dr_preowned_lostcase1_id',this,'span_lostcase2');StatusUpdate();">
														<%=mybean.PopulateLostCase1(mybean.comp_id)%>
												</select>
											</span>
											<span class="hint" id="hint_dr_preowned_lostcase1_id"></span>
										</div>
									</div>
				
									<div class="form-element12">
										<label>Lost Case 2<font color="#ff0000">*</font>: </label>
										<div>
											<span id="span_lostcase2">
												<select name="dr_preowned_lostcase2_id" class="form-control" id="dr_preowned_lostcase2_id"
													onchange="populateLostCase3('dr_preowned_lostcase2_id',this,'span_lostcase3');StatusUpdate();">
														<%=mybean.PopulateLostCase2(mybean.comp_id)%>
												</select>
											</span>
											<span class="hint" id="hint_dr_preowned_lostcase2_id"></span>
										</div>
									</div>
								</div>
								
								<div class="form-element6">
									<label>Status Comments<font color="#ff0000">*</font>: </label>
									<div>
										<textarea name="txt_preowned_preownedstatus_desc" cols="50"
											rows="5" class="form-control" id="txt_preowned_preownedstatus_desc"
											onKeyUp="charcount('txt_preowned_preownedstatus_desc', 'span_txt_preowned_preownedstatus_desc','<font color=red>({CHAR} characters left)</font>', '1000')"
											onChange="SecurityCheck('txt_preowned_preownedstatus_desc',this,'hint_txt_preowned_preownedstatus_desc');StatusUpdate();"><%=mybean.preowned_preownedstatus_desc%></textarea>
										<span id="span_txt_preowned_preownedstatus_desc"> (1000 Characters)</span>
										<span class="hint" id="hint_txt_preowned_preownedstatus_desc"></span>
									</div>
								</div>
								
							</div>
		
							
							<div class="row">
								<div class="form-element6 form-element">
									<div class="form-element12">
										<label>Lost Case 3<font color="#ff0000">*</font>: </label>
										<div>
											<span id="span_lostcase3">
												<select name="dr_preowned_lostcase3_id" class="form-control"
													id="dr_preowned_lostcase3_id" onchange="StatusUpdate();">
														<%=mybean.PopulateLostCase3(mybean.comp_id)%>
												</select>
											</span>
											<span class="hint" id="hint_dr_preowned_lostcase3_id"></span>
										</div>
									</div>
									
									<% if (mybean.config_preowned_soe.equals("1")) { %>
									
										<div class="form-element6">
											<label>SOE:&nbsp; </label>
											<span>
												<%=mybean.soe_name%></td>
											</span>
										</div>
									
									<%} %>
									
									<% if (mybean.config_preowned_campaign.equals("1")) { %>
									
										<div class="form-element6">
											<label>Campaign:&nbsp; </label>
											<span>
												<%=mybean.campaign_name%>
											</span>
										</div>
									
									<%} %>
								</div>
							
								<div class="form-element6">
									<label>Notes: </label>
									<div>
										<textarea name="txt_preowned_notes" cols="70" rows="5" class="form-control" id="txt_preowned_notes"
											onChange="SecurityCheck('txt_preowned_notes',this,'hint_txt_preowned_notes')"><%=mybean.preowned_notes%></textarea>
										<span class="hint" id="hint_txt_preowned_notes"></span>
									</div>
								</div>
							</div>
		
							<div class="row">
								<div class="form-element6">
									<label>Entry By:&nbsp; </label>
									<span>
										<%=mybean.entry_by%>
									</span>
									<input type="hidden" name="entry_by" value="<%=mybean.entry_by%>">
								</div>
			
								<div class="form-element6">
									<label> Entry Date:&nbsp;</label>
									<span>
										<%=mybean.entry_date%>
									</span>
									<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
								</div>
							</div>
		
							<% if (mybean.modified_by != null && !mybean.modified_by.equals("")) {%>
								<div class="row">
									<div class="form-element6">
										<label>Modified By:&nbsp; </label>
										<span>
											<%=mybean.modified_by%>
										</span>
										<input type="hidden" name="modified_by" value="<%=mybean.modified_by%>">
									</div>
				
									<div class="form-element6">
										<label>Modified Date:&nbsp; </label>
										<span>
											<%=mybean.modified_date%>
										</span>
										<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>">
									</div>
								</div>
							<%} %>
		
						</div>
					</div>
				</div>
		
				<% } %>
				
				</form>
			</div>
		</div>
	</div>

	<!-- END CONTAINER -->

	</div>
	<%@ include file="../Library/admin-footer.jsp"%>

	<%@ include file="../Library/js.jsp"%>

	<script type="text/javascript">
	
		$(document).ready(
				function() {
					//document.getElementById("txt_preowned_regdyear").style.zIndex =1;
					/* $('#txt_preowned_regdyear').datepicker(
									{
										showButtonPanel : true,
										changeMonth : true,
										changeYear : true,
										dateFormat : 'MM yy',
										onChangeMonthYear : function(year,
												month) {
											if (month < 10) {
												month = "0" + month;
											}
											var preowned_id = GetReplace(document.form1.preowned_id.value);
											var value = year + "" + month
													+ "01000000";
											var url = "../preowned/preowned-dash-check.jsp?";
											var param = "name=txt_preowned_regdyear&value="
													+ value
													+ "&preowned_id="
													+ preowned_id;
											showHint(url + param, 
													'hint_txt_preowned_regdyear');
										},
										onClose : function() {
											var iMonth = $(
													"#ui-datepicker-div .ui-datepicker-month :selected")
													.val();
											var iYear = $(
													"#ui-datepicker-div .ui-datepicker-year :selected")
													.val();
											$(this).datepicker(
													'setDate',
													new Date(iYear, iMonth,
															1));
										},

										beforeShow : function() {
											if ((selDate = $(this).val()).length > 0) {
												iYear = selDate.substring(
														selDate.length - 4,
														selDate.length);
												iMonth = jQuery
														.inArray(
																selDate
																		.substring(
																				0,
																				selDate.length - 5),
																$(this)
																		.datepicker(
																				'option',
																				'monthNames'));
												$(this).datepicker(
														'option',
														'defaultDate',
														new Date(iYear,
																iMonth, 1));
												$(this).datepicker(
														'setDate',
														new Date(iYear,
																iMonth, 1));
											}
										}
									}).focus(function() {
								$(".ui-datepicker-calendar").hide();
							}); */

					$('#txt_preowned_exp_close_date').on('change', function() {
						SecurityCheck('txt_preowned_exp_close_date', this, 'hint_txt_preowned_exp_close_date');
					});
					$('#txt_preowned_insur_date').on('change', function() {
						SecurityCheck('txt_preowned_insur_date', this, 'hint_txt_preowned_insur_date');
					});

				});
	</script>

	<script language="JavaScript" type="text/javascript">
		function daysdiff(name, obj, hint) {
			var names = name.split(", ");
			var dt1 = names[0];
			var dt2 = names[1];
			var date1 = document.getElementById(dt1).value;
			var date2 = document.getElementById(dt2).value;
			var value = date1 + ", " + date2;
			var checked = '';
			var preowned_id = GetReplace(document.form1.preowned_id.value);
			var url = "../preowned/preowned-dash-check.jsp?";
			var param = "name=" + name + "&value=" + value + "&checked=" + checked + "&preowned_id=" + preowned_id;
			var str = "123";
			showHint(url + param, hint);
			setTimeout('RefreshHistory()', 1000);
		}
	
		function SecurityCheck(name, obj, hint) {
			var value = GetReplace(obj.value);
			var msg = "";
			var preowned_id = GetReplace(document.form1.preowned_id.value);
			var dat = document.getElementById("txt_preowned_date").value;
			//var fcamt = document.getElementById("txt_preowned_fcamt").value;
			//var fbank = document.getElementById("txt_preowned_funding_bank").value;
			//var floan = document.getElementById("txt_preowned_loan_no").value;
			var url = "../preowned/preowned-dash-check.jsp?";
			var param = "name=" + name + "&value=" + value + "&preowned_dat=" + dat + "&preowned_id=" + preowned_id;
			showHint(url + param, hint);
			setTimeout('RefreshHistory()', 1000);
		}
	
		function RefreshHistory() {
			var preowned_id = document.form1.preowned_id.value;
		}
	
		function SecurityDoubleCheck(name1, obj1, name2, obj2, hint) {
			var value1 = GetReplace(obj1.value);
			var value2 = GetReplace(obj2.value);
			var preowned_id = GetReplace(document.form1.preowned_id.value);
			var url = "../preowned/preowned-dash-check.jsp?";
			var param = "name1=" + name1 + "&value1=" + value1 + "&name2=" + name2
					+ "&value2=" + value2 + "&preowned_id=" + preowned_id;
			showHint(url + param, hint);
	
			setTimeout('RefreshHistory()', 1000);
		}
	
		function RefreshHistory() {
			var preowned_id = document.form1.preowned_id.value;
		}
	
		function PopulateCheckVariant() {
			var preowned_id = GetReplace(document.form1.preowned_id.value);
			var model_id = document.getElementById('dr_preowned_preownedmodel_id').value;
			var param = "preowned_preownedmodel_id=" + model_id + "&preowned_id=" + preowned_id;
			var str = "123";
			showHint('../preowned/preowned-dash-check.jsp?preowned_preownedmodel_id='
							+ model_id + "&preowned_id=" + preowned_id, 'modelitem');
		}
	
		function populateLostCase2(name, obj, hint) {
			var preowned_id = document.getElementById('preowned_id').value;
			var value = obj.value;
			var url = "../preowned/preowned-dash-check.jsp?"
			var param = "name=dr_preowned_lostcase1_id&value=" + value + "&preowned_id=" + preowned_id;
			var str = "123";
			setTimeout("showHint('" + url + param + "','" + hint + "')", 500);
			//setTimeout('StatusUpdate()', 1000);
		}
	
		function populateLostCase3(name, obj, hint) {
			var preowned_id = document.getElementById('preowned_id').value;
			var value = obj.value;
			var status_id = document.getElementById('dr_preowned_preownedstatus_id').value;
			var url = "../preowned/preowned-dash-check.jsp?"
			var param = "name=dr_preowned_lostcase2_id&value=" + value
			             + "&status_id=" + status_id +"&preowned_id=" + preowned_id;
			var str = "123";
			setTimeout("showHint('" + url + param + "','" + hint + "')", 500);
			//setTimeout('StatusUpdate()', 1000);
		}
	
		function StatusUpdate() {
			var lostcase1 = "", lostcase2 = "", lostcase3 = "";
			var status_id = document.getElementById('dr_preowned_preownedstatus_id').value;
			var status_desc = document.getElementById('txt_preowned_preownedstatus_desc').value;
			lostcase1 = document.getElementById('dr_preowned_lostcase1_id').value;
			lostcase2 = document.getElementById('dr_preowned_lostcase2_id').value;
			lostcase3 = document.getElementById('dr_preowned_lostcase3_id').value;
			var url = "../preowned/preowned-dash-check.jsp?";
			var param;
			var str = "123";
			var preowned_id = document.form1.preowned_id.value;
			param = "name=dr_preowned_preownedstatus_id&status_id="+ status_id
					+ "&status_desc="+ status_desc
					+ "&lostcase1="+ lostcase1
					+ "&lostcase2="+ lostcase2
					+ "&lostcase3="+ lostcase3 
					+ "&preowned_id=" + preowned_id;
			setTimeout(showHint(url + param, "hint_dr_preowned_preownedstatus_id"),1000);
	// 		setTimeout('RefreshHistory()', 1000);
		}
	</script>

	<script>
		// $(document).ready(function(){ 
		// // 	alert("library");
		// 	$(".page-content").css({'min-height' : '20px'})
		// });
	</script>
</body>
</HTML>
