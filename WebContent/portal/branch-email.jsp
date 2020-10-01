<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Branch_Email"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.ClientName%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp"%>

</head>
<body onLoad="FormFocus();DisplayBranch();Displayemp();"
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
						<h1><%=mybean.status%>&nbsp;Email Settings
						</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="branch-list.jsp?all=yes">List Branches</a> &gt;</li>
							<li><a href="branch-email.jsp?branch_id=<%=mybean.branch_id%>">Email Settings</a><b>:</b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Email Settings
									</div>
								</div>
								
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.
											</font>
										</center>
										
										<form name="formemp" method="post" class="form-horizontal">

											<div class="form-element6">
												<label>Email Server <font color=red>*</font> : </label>
												<input name="txt_branch_email_server"
													id="txt_branch_email_server" type="text"
													class="form-control"
													value="<%=mybean.branch_email_server%>" size="50"
													maxlength="255" />
											</div>

											<div class="form-element6">
												<label>Email Username <font color=red>*</font> : </label>
												<input name="txt_branch_email_username"
													id="txt_branch_email_username" type="text"
													class="form-control"
													value="<%=mybean.branch_email_username%>" size="50"
													maxlength="100" />
											</div>

											<div class="form-element6">
												<label>Email Password <font color=red>*</font> : </label>
												<input name="txt_branch_email_password"
													id="txt_branch_email_password" type="text"
													class="form-control"
													value="<%=mybean.branch_email_password%>" size="50"
													maxlength="50" />
											</div>

											<div class="form-element6">
												<label>Port <font color=red>*</font> : </label>
												<input name="txt_branch_email_port" id="txt_branch_email_port" type="text" class="form-control"
													value="<%=mybean.branch_email_port%>" size="50"
													maxlength="3" />
											</div>

											<div class="form-element6">
												<label>SSL : </label> 
												<input type="checkbox" name="chk_branch_email_ssl"
													<%=mybean.PopulateCheck(mybean.branch_email_ssl)%> />
											</div>

											<div class="form-element6">
												<label>TLS : </label> 
												<input type="checkbox" name="chk_branch_email_tls"
													<%=mybean.PopulateCheck(mybean.branch_email_tls)%> />
											</div>

											<%
												if (mybean.status.equals("Update") && !(mybean.branch_entry_by == null) && !(mybean.branch_entry_by.equals(""))) {
											%>

											<div class="form-element6">
												<label>Entry By : </label>
												<%=mybean.unescapehtml(mybean.branch_entry_by)%><input
													type="hidden" name="branch_entry_by"
													value="<%=mybean.branch_entry_by%>">
												
											</div>

											<%
												}
											%>

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>

											<div class="form-element6">
												<label>Entry Date : </label>
												<%=mybean.entry_date%><input type="hidden" name="entry_date"
													value="<%=mybean.entry_date%>">
												
											</div>

											<%
												}
											%>

											<%
												if (mybean.status.equals("Update") && !(mybean.branch_modified_by == null) && !(mybean.branch_modified_by.equals(""))) {
											%>
											
											<div class="form-element6">
												<label>Modified By : </label>
												<%=mybean.unescapehtml(mybean.branch_modified_by)%>
												<input type="hidden" name="branch_modified_by"
													value="<%=mybean.branch_modified_by%>">
											</div>

											<%
												}
											%>


											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
											%>

											<div class="form-element6">
												<label>Modified Date : </label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date"
													value="<%=mybean.modified_date%>">
											</div>

											<%
												}
											%>

											<center>
												<input type="hidden" name="update_button" value="yes" /> <input
													name="updatebutton" type="submit" class="btn btn-success"
													id="updatebutton"
													onclick="onPress();return SubmitFormOnce(document.formemp,this);"
													value="Update" />
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

	<%@include file="../Library/js.jsp"%>

</body>
</html>
