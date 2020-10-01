<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.ManagePreownedCRMFollowupDays" scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<%@include file="../Library/css.jsp" %>

</head>
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>List Pre-Owned CRM Follow-up Days</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<div class="page-content-inner">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="managepreownedcrmfollowupdays.jsp">List Pre-Owned CRM Follow-up Days</a><b>:</b></li>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Search</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="get" class="form-horizontal">
											<div class="container-fluid">
												<div class="form-element6 form-element-center">
													<label>Brand:</label>
														<select name="dr_brand" class="form-control" id="dr_brand" onChange="document.form1.submit()">
															<%=mybean.PopulateBrand(mybean.comp_id)%>
														</select>

												</div>
											</div>
											<%if (!mybean.brand_id.equals("0")) {%>
											<div style="float: right">
												<a href="managepreownedcrmfollowupdays-update.jsp?add=yes&amp;dr_brand=<%=mybean.brand_id%>">Add
													Pre-Owned CRM Follow-up Day...</a>
											</div>
											<%}%>
										</form>
									</div>
								</div>
							</div>
							<center><%=mybean.StrHTML%></center>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	<!-- 	CONTAINER-FLUID -->

	<%@ include file="../Library/admin-footer.jsp"%>
	<%@ include file="../Library/js.jsp"%>

	</body>
</html>
