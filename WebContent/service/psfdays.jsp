<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.PSFDays" scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</head>


<body class="page-container-bg-solid page-header-menu-fixed">

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
						<h1>List PSF Days</h1>
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
							<li><a href="psfdays.jsp">List PSF Days</a><b>:</b></li>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center></center>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div align="center">
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</div>
							<form class="form-horizontal" name="form1">

								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Search</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-element6 form-element-center">
												<label>Brand:</label>
													<select name="dr_brand" class="form-control"
														id="dr_brand" onChange="document.form1.submit()">
														<%=mybean.PopulateBrand(mybean.comp_id)%>
													</select>
											</div>
											<%
												if (!mybean.brand_id.equals("0")) {
											%>
											<div colspan="2" height="38" align='right'>
												<a href="psfdays-update.jsp?add=yes&amp;dr_branch=<%=mybean.brand_id%>">Add
													PSF Days...</a>
											</div>
											<%
												}
											%>
											<div valign="top" colspan="2" align="center" height="300"><%=mybean.StrHTML%></div>
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

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	
	</body>
</html>
