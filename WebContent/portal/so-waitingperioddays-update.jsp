<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.SO_WaitingPeriodDays_Update"
	scope="request" />
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

<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
	
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_camptype_desc.focus()
	}

	function DisplayType() {

		var str1 = document.getElementById('dr_crmdays_crmtype_id').value;
		if (str1 == "1") {
			$('#lostfollowup').show();
		} else {
			$('#lostfollowup').hide();
		}
		if (str1 == "2") {
			$('#SOInactive').show();
		} else {
			$('#SOInactive').hide();
		}
	}
</script>
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
	<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed"
	onLoad="DisplayType();FormFocus();" leftmargin="0" rightmargin="0"
	topmargin="0" bottommargin="0">
	<%@include file="../portal/header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%> SO Waiting Period</h1>
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
						<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
						<li><a href="so-waitingperioddays-list.jsp?dr_brand=<%=mybean.dr_brand_id%>&brandconfig_id=<%=mybean.brandconfig_id%>">List SO Waiting Period</a> &gt;</li>
						<li><a href="so-waitingperioddays-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
								SO Waiting Period</a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div align="center">
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</div>
							<form name="form1" method="post" class="form-horizontal">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"><%=mybean.status%>&nbsp;SO Waiting Period
										</div>

									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div align="center">
												<font size="1">Form fields marked with a red asterisk
													<b><font color="#ff0000">*</font></b> are required.<br>
												</font>
											</div><br>
											
											<div class="form-body">
												<div class="form-group">
													<label class="col-md-3 control-label">Brand<b><font
															color="#ff0000">*</font></b>:
													</label>
													<div class="col-md-6">
														<select name="dr_sowaitingperiod_brand_id" class="form-control"
															id="dr_sowaitingperiod_brand_id">
															<%=mybean.PopulateBrand(mybean.comp_id)%>
														</select>
													</div>
												</div>
											</div>

											


											

											<div class="form-body">
												<div class="form-group">
													<label class="col-md-3 control-label">Days<b><font
															color="#ff0000">*</font></b>:
													</label>
													<div class="col-md-6">
														<input name="txt_sowaitingperiod_days" type="text"
															class="form-control" id="txt_sowaitingperiod_days"
															onKeyUp="toInteger('txt_sowaitingperiod_days','Days')"
															value="<%=mybean.sowaitingperiod_days%>" size="10"
															maxlength="6" />
													</div>
												</div>
											</div>
											
											<div class="form-body">
											<div class="form-group">
												<label class="col-md-3 control-label" > SO Waiting Period Active: </label>
												<div class="col-md-6" style="top:8px">
													<input id="chk_sowaitingperiod_enable" type="checkbox"
														name="chk_sowaitingperiod_enable"
														<%=mybean.PopulateCheck(mybean.sowaitingperiod_enable)%> />
												</div>
											</div>
										</div>

										</div>
										
										<!-- <div class="form-group">
												<label class="control-label col-md-3"></label>
												<div class="col-md-6">
													<table
														class="table table-responsive table-hover table-bordered">
														<thead>
															<tr>
															<th colspan="2">Subtitution Variables:</th>
															</tr>
														</thead>
														<tbody>

															<tr>
																<td align="right">Salutation:</td>
																<td align="left">[SALUTATION]</td>
															</tr>
															<tr>
																<td align="right">Contact Name:</td>
																<td align="left">[CONTACTNAME]</td>
															</tr>
															<tr>
																<td align="right">Executive:</td>
																<td align="left">[EXENAME]</td>
															</tr>
														</tbody>
													</table>
												</div>
											</div> -->
										<%if (mybean.status.equals("Update")) { %>
										<div class="form-group">
 												<label class="control-label col-md-3"> Automated 
 													Tasks: </label>
												<div class="col-md-6">
 												<table class="table table-responsive table-hover table-bordered">
														<thead>
 															<tr>
																<th>SO Waiting Period</th>
																<th>Format Email</th> 
																<th>Format SMS</th> 
 															</tr>
														</thead>
														<tbody>
                                                         <tr> 
																<td align="left">SO Waiting Period:</td>
 																<td align="center"><a
																	href="so-waitingperioddays-format.jsp?sowaitingperiod_id=<%=mybean.sowaitingperiod_id%>&email=yes&status=SO WaitingPeriodDays&opt=sowaitingperiod_email_format">Format</a></td>
																<td align="center"><a href="so-waitingperioddays-format.jsp?sowaitingperiod_id=<%=mybean.sowaitingperiod_id%>&sms=yes&status=SO WaitingPeriodDays&opt=sowaitingperiod_sms_format">Format</a></td>
															</tr>
															
														</tbody>
													</table>
												</div>
											</div>
											<% }%>		
										
										

										
										<%
											if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
										%>



										<div class="form-body">
											<div class="form-group" >
												<label class="col-md-3 control-label">Entry By: </label>
												<div class="col-md-6" style="top:8px">
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>">
												</div>
											</div>
										</div>

										<%
											}
										%>
										<%
											if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
										%>


										<div class="form-body">
											<div class="form-group">
												<label class="col-md-3 control-label">Entry Date:</label>
												<div class="col-md-6" style="top:8px">
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>">
												</div>
											</div>
										</div>

										<%
											}
										%>
										<%
											if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
										%>
										<div class="form-body">
											<div class="form-group">
												<label class="col-md-3 control-label">Modified By:</label>
												<div class="col-md-6" style="top:8px">
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>">
												</div>
											</div>
										</div>
										<%
											}
										%>

										<%
											if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
										%>
										<div class="form-body">
											<div class="form-group">
												<label class="col-md-3 control-label">Modified Date:</label>
												<div class="col-md-6" style="top:8px">
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
												</div>
											</div>
										</div>
										<%
											}
										%>

										<%
											if (mybean.status.equals("Add")) {
										%>

										<div class="form-body">
											<div class="form-group">
												<label class="col-md-5 control-label"></label>
												<div class="col-md-6">
													<input name="addbutton" type="submit"
														class="btn btn-success" id="addbutton"
														value="Add SO Waiting Period"
														onClick="return SubmitFormOnce(document.form1, this);" />
													<input type="hidden" name="add_button" id="add_button"
														value="yes" />
												</div>
											</div>
										</div>

										<%
											} else if (mybean.status.equals("Update")) {
										%>

										<div class="form-body">
											<div class="form-group">
												<label class="col-md-4 control-label"></label>
												<div class="col-md-6">
													<input type="hidden" name="update_button"
														id="update_button" value="yes" /> <input
														name="updatebutton" type="submit" class="btn btn-success"
														id="updatebutton" value="Update SO Waiting Period"
														onClick="return SubmitFormOnce(document.form1, this);" />
													<input name="delete_button" type="submit"
														class="btn btn-success" id="delete_button"
														OnClick="return confirmdelete(this)"
														value="Delete SO Waiting Period" />
													<%
														}
													%>
												</div>
											</div>
										</div>
									</div>
								</div>
						</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
</body>
<%@include file="../Library/admin-footer.jsp"%>

<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	</body>
</HTML>
