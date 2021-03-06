<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.ManageCrmConcern_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</head>
<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">

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
						<h1><%=mybean.status%>&nbsp;CRM Concern
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
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="managecrmconcern.jsp?all=yes">List CRM Concern</a> &gt;</li>
							<li><a href="managecrmconcern-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;CRM Concern</a><b>:</b>
							<li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font> <br>
						</center>

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;CRM Concern
									</div>
								</div>
								
								<div class="container-fluid portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br>

										<form name="form1" method="post" class="form-horizontal">

											<div class="form-element6">
												<label> CRM Type<font color=red>*</font>: </label>
												<select name="dr_crmtype" id="dr_crmtype"
														class="form-control" >
														<%=mybean.PopulateCRMType()%>
													</select>
											</div>

											<div class="form-element6">
												<label> CRM Concern Description<font color=red>*</font>: </label>
												<textarea name="txt_crmconcern_desc" cols="70" rows="5"
												class="form-control" id="txt_crmconcern_desc"><%=mybean.crmconcern_desc%></textarea>
											</div>
<div class="row"></div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>

											<div class="form-element6">
												<label> Entry By:</label>
												<%=mybean.unescapehtml(mybean.entry_by)%>
												<input name="entry_by" type="hidden" id="entry_by"
												value="<%=mybean.unescapehtml(mybean.entry_by)%>">
											</div>

											<%
												}
											%>

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>

											<div class="form-element6">
												<label> Entry Date:</label>
												<%=mybean.entry_date%>
												<input type="hidden" name="entry_date"
												value="<%=mybean.entry_date%>">
											</div>

											<%
												}
											%>

											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>

											<div class="form-element6">
												<label> Modified By:</label>
												<%=mybean.unescapehtml(mybean.modified_by)%>
												<input name="modified_by" type="hidden" id="modified_by"
												value="<%=mybean.unescapehtml(mybean.modified_by)%>">
											</div>

											<%
												}
											%>

											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>

											<div class="form-element6">
												<label> Modified Date:</label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date"
												value="<%=mybean.modified_date%>">
											</div>

											<%
												}
											%>

											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												<input name="button" type="submit" class="btn btn-success"
												id="button" value="Add CRM Concern"
												onclick="return SubmitFormOnce(document.form1, this);" />
												<input type="hidden" name="add_button" value="yes">
												<%
													} else if (mybean.status.equals("Update")) {
												%>
												<input type="hidden" name="update_button" value="yes">
												<input name="button" type="submit" class="btn btn-success"
												id="button" value="Update CRM Concern"
												onclick="return SubmitFormOnce(document.form1, this);" />
												<input name="delete_button" type="submit"
												class="btn btn-success" id="delete_button"
												OnClick="return confirmdelete(this)" value="Delete CRM Concern" />
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

	<%@include file="../Library/js.jsp"%>

	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
			document.form1.dr_crmtype.focus()
		}
	</script>
</body>
</html>
