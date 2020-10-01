<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Executives_Access_Log1"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp" %>
</head>

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
					<div class="page-title">
						<h1>Executive Access Log1</h1>
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
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="mis.jsp">MIS</a> &gt;</li>
						<li><a href="executives-access-log1.jsp">Executive Access
								Log1</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
							<center>
								<font color="red"><b> <%=mybean.msg%></b></font>
							</center>
							
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Search</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form class="form-horizontal" method="get" name="frm1">
											<div class="form-element6 form-element-center">
												<label> Branch: </label>
													<select name="dr_branch_id" id="dr_branch_id"
														class="form-control">
														<%=mybean.PopulateBranch()%>
													</select>


											</div>
											<div class="form-element6 form-element-center">
												<label>Date<font color=#ff0000><b> *</b></font>:</label>
													<input name="txt_starttime" id="txt_starttime"
														value="<%=mybean.startdate%>"
														class="form-control datepicker"
														 type="text" value="" />
											</div>
											<center>
												<input name="submit_button" type="submit"
													class="btn btn-success" id="submit_button" value="Go" /> <input
													type="hidden" name="submit_button" value="Submit" />
											</center>
											<center>
												<%
													if (!mybean.StrHTML.equals("")) {
												%>
												<%=mybean.StrHTML%>
												<%
													} else {
												%>
												<font color="red"><strong><br /> <br /> <br />No
														records found!</strong></font>
												<%
													}
												%>
											</center>
											<br></br>

										</form>
									</div>
								</div>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	<!-- END CONTAINER -->
	<%@include file="../Library/js.jsp" %>
	<%@include file="../Library/admin-footer.jsp"%>
	<script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>
</body>
</html>
