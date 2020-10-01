<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Manage_Configure"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp"%>
</head>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Configure Axela</h1>
					</div>
					<!-- END PAGE TITLE -->

				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<!-- BEGIN PAGE BREADCRUMBS -->
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="home.jsp">Home</a> &gt;</li>
							<li><a href="manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="../portal/manage-configure.jsp">Configure
									Axela</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>

						<div class="container-fluid portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Configure Axela</div>
							</div>
							<div class="portlet-body">
								<div class="tab-pane" id="">
									<center>
										<font size="1">Form fields marked with a red asterisk <b><font
												color="#ff0000">*</font></b> are required.
										</font>
									</center>
									<br />
									<form method="post" name="form1" class="form-horizontal">
										<div class="form-element6">
											<label> Admin Email-Id <font color="#ff0000">*</font>: </label>
											<input name="txt_admin_email" id="txt_admin_email"
												type="text" class="form-control" size="40"
												value="<%=mybean.config_admin_email%>" maxlength="255" />
										</div>

										<div class="form-element6">
											<label>App Version<font color="#ff0000">*</font>: </label>
											<input name="txt_config_app_ver" id="txt_config_app_ver"
												type="text" class="form-control"
												value="<%=mybean.config_app_ver%>" size="12" maxlength="10" />
										</div>
										
										<div class="form-element6">
											<label>Document Size:</label>
											<input name="txt_doc_size" id="txt_doc_size" type="text" class="form-control"
												value="<%=mybean.config_doc_size%>" size="7" maxlength="5"
												onKeyUp="toNumber('txt_doc_size','Document Size');" /> Mb
											(Maximum file size)
										</div>
										
										<div class="form-element6">
											<label>Document Formats:</label>
											<input name="txt_doc_formats" id="txt_doc_formats" type="text"
												class="form-control" value="<%=mybean.config_doc_formats%>"
												size="40" maxlength="255" /> (comma and space seperated for
											each file extension) <br> Eg.: .pdf, .doc, .ppt
										</div>
										
										<div class="form-element6">
											<label>Enable Email:</label>
											<input type="checkbox" name="chk_config_email_enable" id="chk_config_email_enable"
												<%=mybean.PopulateCheck(mybean.config_email_enable)%>>
										</div>
										
										<div class="form-element6">
											<label>Enable SMS:</label>
											<input type="checkbox" name="chk_config_sms_enable" id="chk_config_sms_enable"
												<%=mybean.PopulateCheck(mybean.config_sms_enable)%>>
										</div>
										
										
										<div class="form-element6">
											<div class="form-element12 form-element">
												<label>Contact SMS Enable:</label>
												<input type="checkbox" id="chk_config_contact_sms_enable" name="chk_config_contact_sms_enable"
													<%=mybean.PopulateCheck(mybean.config_contact_sms_enable)%>>
											</div>
										
											<div class="form-element12 form-element">
												<label>Contact SMS Format:</label>
												<textarea name="txt_config_contact_sms_format" cols="40" rows="5"
													class="form-control" id="txt_config_contact_sms_format"
													onKeyUp="charcount('txt_config_contact_sms_format', 'span_config_contact_sms_format','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.config_contact_sms_format%></textarea>
												<span id="span_config_contact_sms_format">(255 Characters)</span>
											</div>
										</div>
										
										<div class="form-element6">
											<div class="form-element12 form-element">
												<label>Executive SMS Enable:</label>
												<input type="checkbox" name="chk_config_exe_sms_enable" id="chk_config_exe_sms_enable"
													<%=mybean.PopulateCheck(mybean.config_exe_sms_enable)%>>
											</div>
										
											<div class="form-element12 form-element">
												<label> Executive SMS Format:&nbsp; </label>
												<textarea name="txt_config_exe_sms_format" cols="40" rows="5"
													class="form-control" id="txt_config_exe_sms_format"
													onKeyUp="charcount('txt_config_exe_sms_format', 'span_config_exe_sms_format','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.config_exe_sms_format%></textarea>
												<span id="span_config_exe_sms_format">(255 Characters)</span>
											</div>
										</div>

											<div class="form-element12">
												<div class="form-element8 form-element-center form-element">
													<table class="table table-hover table-bordered">
														<thead>
															<tr>
																<th colspan="2" align="center">Substitution
																	Variables</th>
															</tr>
														</thead>
														<tr>
															<td align="right">Executive Name:</td>
															<td align="left">[EXENAME]</td>
														</tr>
														<tr>
															<td align="right">Executive Job Title:</td>
															<td align="left">[EXEJOBTITLE]</td>
														</tr>
														<tr>
															<td align="right">Executive Mobile1:</td>
															<td align="left">[EXEMOBILE1]</td>
														</tr>
														<tr>
															<td align="right">Executive Phone1:</td>
															<td align="left">[EXEPHONE1]</td>
														</tr>
														<tr>
															<td align="right">Executive Email1: </td>
															<td align="left">[EXEEMAIL1]</td>
														</tr>
														<tr>
															<td align="right">Branch Name:</td>
															<td align="left">[BRANCHNAME]</td>
														</tr>
														<tr>
															<td align="right">Branch Email1:</td>
															<td align="left">[BRANCHEMAIL1]</td>
														</tr>
														<tr>
															<td align="right">Contact Name:</td>
															<td align="left">[CONTACTNAME]</td>
														</tr>
														<tr>
															<td align="right">Contact Mobile:</td>
															<td align="left">[CONTACTMOBILE]</td>
														</tr>
													</table>
												</div>
											</div>


										<%-- <div class="form-group" id="sms_url">
											 <label class="control-label col-md-4">SMS URL:</label>
											 <div class="col-md-6 col-xs-12">
											 <textarea name="txt_config_sms_url" cols="70" rows="4"
												class="form-control" id="txt_config_sms_url"><%=mybean.config_sms_url%></textarea> 
											 </div>
											 </div> --%>
										<center>
											<input name="update_button" type="submit"
												class="btn btn-success" id="update_button" value="Update" />
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
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
			document.form1.txt_admin_email.focus()
		}
	</script>
</body>
</HTML>
