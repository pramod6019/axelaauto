<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.mktg.Lead_Update" scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet"
	type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../assets/css/footable.core.css">
<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/multi-select.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/select2.min.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/select2-bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<script>
	$(function(){
		$("#txt_lead_date").datepicker(){
			showButtonPanel : true;
		dateFormat: "dd/mm/yy"
		});
	});
</script>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.formcontact.txt_customer_name.focus();
	}
</script>
<!-- <script> -->
<!-- // $("#txt_customer_since")({ // todayHighlight:true; // }) -->
<!-- </script> -->
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<script language="JavaScript" type="text/javascript">
        function FormFocus() { //v1.0
            document.formcontact.txt_lead_fname.focus();
        }
      
    </script>

<body onLoad="FormFocus();"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%> Lead</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="marketing.jsp">Marketing</a> &gt;</li>
						<li><a href="../mktg/lead.jsp">Leads</a> &gt;</li>
						<li><a href="../mktg/lead-list.jsp?all=yes">List Leads</a> &gt;</li>
						<li><a href="lead-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Lead</a>: </li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Lead
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="formcontact" method="post" class="form-horizontal">
											<div class="form-body">
												<div class="form-group">
													<label class="col-md-4 control-label">Branch<font
														color=red>*</font>:
													</label>
													<div class="col-md-6">
														<% if (mybean.branch_id.equals("0")) { %>
														<select name="dr_lead_branch_id" class="form-control"
															id="dr_lead_branch_id">
															<%=mybean.PopulateBranch()%>
														</select>
														<% } else { %>
														<b><%=mybean.branch_name%></b> <input type="hidden"
															id="branch_id" name="branch_id"
															value="<%=mybean.lead_branch_id%>"> 
															<% } %>
													</div>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4 col-xs-12 col-sm-2">
													Date<font color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
													<input name="txt_lead_date" id="txt_lead_date"
														value="<%=mybean.leaddate%>"
														class="form-control date-picker"
														data-date-format="dd/mm/yyyy" type="text" maxlength="10" />
												</div>
											</div>
											
											<div class="form-group">
												<label class="control-label col-md-4"> Contact<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<table>
														<tr>
															<td><select name="dr_title" class="form-control"
																id="dr_title">
																	<%=mybean.PopulateTitle(mybean.lead_title_id, mybean.comp_id)%>
															</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>Title</span></td>
															<td><input name="txt_lead_fname" type="text"
																class="form-control " id="txt_lead_fname"
																value="<%=mybean.lead_fname%>" size="30"
																maxlength="255" onkeyup="ShowNameHint()" />&nbsp;&nbsp;&nbsp;&nbsp;
																&nbsp;&nbsp;&nbsp;&nbsp; <span>First Name</span></td>
															<td><input name="txt_lead_lname" type="text"
																class="form-control " id="txt_lead_lname"
																value="<%=mybean.lead_lname%>" size="30"
																maxlength="255" onkeyup="ShowNameHint()" />
																&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; <span>Last
																	Name</span></td>
														</tr>
													</table>

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4 col-sm-2  col-xs-12">Job
													Title:</label>
												<div class="col-md-6 col-sm-10 col-xs-12" id="emprows">
													<input class="form-control" name="txt_lead_jobtitle"
														type="text" id="txt_lead_jobtitle"
														value="<%=mybean.lead_jobtitle%>" size="32"
														maxlength="255">
												</div>
											</div>
											
											<div class="form-group">
												<label class="control-label col-md-4 col-sm-2 col-xs-12">Mobile 1<font color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-sm-10 col-xs-12" id="emprows">
													<input name="txt_lead_mobile" type="text"
														class="form-control" id="txt_lead_mobile"
														onKeyUp="toPhone('txt_lead_mobile','Contact Mobile');"
														value="<%=mybean.lead_mobile%>" size="32"
														maxlength="13" /> (91-9999999999)
<!-- 														<br> -->
														<span id="showcontacts"></span>
												</div>
												
											</div>
											<div class="form-group">
												<label class="control-label col-md-4 col-sm-2 col-xs-12">Phone 1<font color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-sm-10 col-xs-12" id="emprows">
													<input name="txt_lead_phone" type="text"
														class="form-control" id="txt_lead_phone"
														onChange="toPhone('txt_lead_phone','Contact Phone');"
														value="<%=mybean.lead_phone%>" size="32"
														maxlength="14" /> (91-80-33333333)
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4 col-xs-12 col-sm-2">Email
													1:</label>
												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
													<input name="txt_lead_email" type="text"
														class="form-control" id="txt_lead_email"
														value="<%=mybean.lead_email%>" size="32"
														maxlength="100">
												</div>
											</div>
											<div class="form-body">
												<div class="form-group">
													<label class="col-md-4 control-label">Website:</label>
													<div class="col-md-6">
														<input name="txt_lead_website" type="text"
															class="form-control" id="txt_lead_website"
															value="<%=mybean.lead_website%>" size="40"
															MaxLength="100">
													</div>
												</div>
											</div>
											<div class="form-body">
												<div class="form-group">
													<label class="col-md-4 control-label">Company<font color=red>*</font>:</label>
													<div class="col-md-6">
														<input name="txt_lead_company" type="text"
															class="form-control" id="txt_lead_company"
															value="<%=mybean.lead_company%>" size="40"
															MaxLength="100">
													</div>
												</div>
											</div>
											<div class="form-body">
												<div class="form-group">
													<label class=" col-md-4 control-label">Employee Count<font color=red>*</font>:
													</label>
													<div class="col-md-6">
														<select name="dr_lead_empcount_id" class="form-control" id="dr_lead_empcount_id">
															<%=mybean.PopulateEmpCount()%>
														</select>
													</div>

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4 col-xs-12 col-sm-2"> Requirment: </label>
												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
													<textarea name="txt_lead_req" cols="40" rows="4"
														class="form-control" id="txt_lead_req"
														onKeyUp="charcount('txt_lead_req', 'span_txt_lead_req','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.lead_req%></textarea>
													<span id="span_txt_lead_req"> (8000 Characters)</span>
												</div>
											</div>
											
											<div class="form-body">
												<div class="form-group">
													<label class=" col-md-4 control-label"> Source Of Enquiry<font color=red>*</font>:
													</label>
													<div class="col-md-6">
														<select name="dr_lead_leadsoe_id" class="form-control" id="dr_lead_leadsoe_id">
															 <%=mybean.PopulateSoe()%>
														</select>
													</div>

												</div>
											</div>
											
											<div class="container-fluid">
												<div class="form-group">
													<label class="control-label col-md-4"> Source of Business<font color=red>*</font>: </label>
													<div class="col-md-6" id="emprows">
														<select name="dr_lead_leadsob_id" class="form-control"
															id="dr_lead_leadsob_id">
															<%=mybean.PopulateSob()%>
														</select>
													</div>
												</div>
												</div>
											
											<div class="container-fluid">
												<div class="form-group">
													<div class="col-md-12 col-xs-12"
														style="vertical-align: middle">
														<label class="control-label col-md-4"> Executive<font color="red">*</font>:</label>
														<div class="col-md-6 col-xs-12" id="emprows">
															<select name="dr_lead_emp_id" class="form-control" id="dr_lead_emp_id">
																 <%=mybean.PopulateExecutive()%>
															</select>
														</div>

													</div>
												</div>
											</div>
												<%
													if (mybean.status.equals("Update") && !(mybean.entry_by == null)
															&& !(mybean.entry_by.equals(""))) {
												%>
												<div class="form-group">
													<label class="control-label col-md-4">Entry By:</label>
													<div class="txt-align">
														<%=mybean.unescapehtml(mybean.entry_by)%>
														<input name="entry_by" type="hidden" id="entry_by"
															value="<%=mybean.entry_by%>">
													</div>
												</div>
												<div class="form-group">
													<label class="control-label col-md-4">Entry Date:</label>
													<div class="txt-align">
														<%=mybean.entry_date%>
														<input type="hidden" name="entry_date"
															value="<%=mybean.entry_date%>">
													</div>
												</div>

												<%
													}
												%>
												<%
													if (mybean.status.equals("Update") && !(mybean.modified_by == null)
															&& !(mybean.modified_by.equals(""))) {
												%>
												<div class="form-group">
													<label class="control-label col-md-4">Modified By:</label>
													<div class="txt-align">
														<%=mybean.unescapehtml(mybean.modified_by)%>
														<input name="modified_by" type="hidden" id="modified_by"
															value="<%=mybean.modified_by%>">
													</div>
												</div>
												<div class="form-group">
													<label class="control-label col-md-4">Modified
														Date:</label>
													<div class="txt-align">
														<%=mybean.modified_date%>
														<input type="hidden" name="modified_date"
															value="<%=mybean.modified_date%>">
													</div>
												</div>
												<%
													}
												%>
												<center>
													<%
														if (mybean.status.equals("Add")) {
													%>
													<input name="addbutton" type="submit"
														class="btn btn-success" id="addbutton"
														value="Add Lead"
														onClick="retrive(); onPress(); return SubmitFormOnce(document.formcontact, this);" />
													<input type="hidden" name="add_button" value="yes">
													<%
														} else if (mybean.status.equals("Update")) {
													%>
													<input type="hidden" name="update_button" value="yes">
													<input name="update_button" type="submit"
														class="btn btn-success" id="update_button"
														value="Update Lead"
														onClick="onPress(); return SubmitFormOnce(document.formcontact, this);" />
													<input name="delete_button" type="submit"
														class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)"
														value="Delete Lead" />
													<%
														}
													%>
												</center>
										</form>
									</div>
								</div>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-date-time-pickers.js"
		type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datepicker.js"
		type="text/javascript"></script>
	<script src="../assets/js/jquery.multi-select.js"
		type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/select2.full.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-select2.min.js"
		type="text/javascript"></script>
</body>
</HTML>
