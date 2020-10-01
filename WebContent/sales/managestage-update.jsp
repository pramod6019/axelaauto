<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Managestage_Update"
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
						<h1><%=mybean.status%>&nbsp;Stage
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
							<li><a href="managestage.jsp?all=yes">List Stage</a> &gt;</li>
							<li><a href="managestage-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Stage</a><b>:</b>
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
										<%=mybean.status%>&nbsp;Stage
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
												<label> Stage<font color=red>*</font>: </label>
												<input name="txt_stage_name" type="text"
												class="form-control" id="txt_stage_name"
												value="<%=mybean.stage_name%>" size="50" maxlength="255" />
											</div>

											<div class="form-element6">
												<label> Probability<font color=red>*</font>: </label>
												<input name="txt_stage_probability" type="text"
												class="form-control" id="txt_stage_probability"
												onKeyUp="toInteger('txt_stage_probability')"
												value="<%=mybean.stage_probability%>" size="50"
												maxlength="255" />
											</div>

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
												id="button" value="Add Stage"
												onclick="return SubmitFormOnce(document.form1, this);" />
												<input type="hidden" name="add_button" value="yes">
												<%
													} else if (mybean.status.equals("Update")) {
												%>
												<input type="hidden" name="update_button" value="yes">
												<input name="button" type="submit" class="btn btn-success"
												id="button" value="Update Stage"
												onclick="return SubmitFormOnce(document.form1, this);" />
												<input name="delete_button" type="submit"
												class="btn btn-success" id="delete_button"
												OnClick="return confirmdelete(this)" value="Delete Stage" />
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
			document.form1.txt_stage_name.focus()
		}
	</script>
</body>
</html>
