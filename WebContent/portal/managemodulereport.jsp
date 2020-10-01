<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.ManageModuleReport"
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
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</head>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Manage Reports</h1>
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
							<li><a href=managemodulereport.jsp>Manage Reports</a><b>:</b></li>
						</ul>
					</div>
					<!-- END PAGE BREADCRUMBS -->
					<div class="container-fluid">
						<div class="form-element10 form-element">
							<div class="tab-pane" id="">
								<center>
									<font color="#ff0000" size="1"><b><%=mybean.msg%></b></font>
								</center>
								<div class="container-fluid portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Manage Reports</div>
									</div>
									<div class="portlet-body">
										<div class="tab-pane" id="">
											<center>
												<font>Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.
												</font>
											</center>
											<br />
											<form method="post" name="form1" class="form-horizontal">
												<div class="form-element6 form-element-center">
													<label>Module<font color="#ff0000">*</font>:
													</label> <select name="dr_module_id" class="form-control"
														id="dr_module_id" onChange="this.form.submit()">
														<%=mybean.PopulateModule()%>
													</select>
												</div>

											</form>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="form-element2">
							<center>
								<font color="#ff0000"><b><%=mybean.LinkAddPage%></b></font>
							</center>
						</div>

						<div class="form-element12 form-element">
							<%=mybean.StrHTML%>
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
				//document.form1.dr_branch_id.focus()
			}
		</script>
</body>
</HTML>
