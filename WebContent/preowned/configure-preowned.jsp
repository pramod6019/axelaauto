<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.Configure_Preowned"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1 maximum-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp" %>
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />

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
						<h1>Configure Pre-Owned</h1>
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
							<li><a href="../portal/manager.jsp#preowned">Business Manager</a> &gt;</li>
							<li><a href="preowned.jsp">Pre-Owned</a> &gt;</li>
							<li><a href="configure-preowned.jsp">Configure Pre-Owned</a><b>:</b>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Configure Pre-Owned</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.
											</font>
										</center><br>
										
										<form name="form1" method="post" class="form-horizontal">
											<div class="form-element2 form-element-center">
												<label> Enable Source of Enquiry: </label>
													<input type="checkbox" name="chk_config_preowned_soe" <%=mybean.PopulateCheck(mybean.config_preowned_soe)%>>
											</div>
											<div class="form-element2 form-element-center">
												<label> Enable Source of Business: </label>
													<input type="checkbox" name="chk_config_preowned_sob" <%=mybean.PopulateCheck(mybean.config_preowned_sob)%>>
											</div>
											<div class="form-element2 form-element-center">
												<label> Enable Campaigns: </label>
													<input type="checkbox" name="chk_config_preowned_campaign" <%=mybean.PopulateCheck(mybean.config_preowned_campaign)%>>
											</div>
											<div class="form-element2 form-element-center">
												<label>Pre Owned Ref No.:</label>
													<input type="checkbox" name="chk_config_preowned_refno" <%=mybean.PopulateCheck(mybean.config_preowned_refno)%>>
											</div>
											<center>
												<input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update" onClick="return SubmitFormOnce(document.form1, this);" />
											    <input type="hidden" id="update_button" name="update_button" value="yes" />
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
