<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Activity_Export" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
<%@include file="header.jsp"%>

	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
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
							<li><a href="../portal/activity.jsp">Activity</a> &gt;</li>
							<li><a href="activity-export.jsp">Export</a><b>:</b></li>
						</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane " id="">
							<div class="container-fluid portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Activity Export</div>
								</div>
								
								<div class="portlet-body">
									<div class="tab-pane" id="">
									
										<form method="get" name="frm1" class="form-horizontal">
										
											<div class="form-element6">
												<label>Report Type<font color=red>*</font>: </label>
												<select class="form-control" name="report" id="report">
													<%=mybean.PopulatePrintOption()%>
												</select>
											</div>

											<div class="form-element6">
												<label> Export Format<font color=red>*</font>: </label>
												<select class="form-control" name="exporttype" id="exporttype">
													<%=mybean.PopulateExport()%>
												</select>
											</div>
											
											<center>
												<input name=btn_export type="submit" class="btn btn-success" id="submit_button" value="Export" />
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
