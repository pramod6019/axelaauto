<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Configure_Activity"
	scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp" %>
</head>
<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Configure Activity</h1>
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
						<li><a href="manager.jsp">Business Manager</a> &gt;</li>
						<li><a href="jsp#activity">Activity</a> &gt;</li>
						<li><a href="configure-activity.jsp">Configure Activity</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

			
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Configure Activity</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
										<font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br>
                                                                </font><br>
										</center>
										<form name="form1" method="post" class="form-horizontal">
										<center>
												<div class="form-element2 form-element-center">
													<label>Enable Leads:
													</label>
												
														<input type="checkbox" name="chk_config_sales_leads"
															<%=mybean.PopulateCheck(mybean.config_sales_leads)%>>
												
												</div>
												<div class="form-element2 form-element-center">
													<label>Enable
														Enquiry: </label>
													
														<input type="checkbox" name="chk_config_sales_enquiry"
															<%=mybean.PopulateCheck(mybean.config_sales_enquiry)%>>
												
												</div>
												<div class="form-element2 form-element-center">
													<label>Enable Quotes:
													</label>
														<input type="checkbox" name="chk_config_sales_quote" <%=mybean.PopulateCheck(mybean.config_sales_quote)%>>
												</div>
											</center>
											<center>
												
													<input name="update_button" type="submit" class="btn btn-success" id="update_button" value="Update" />
												
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
			document.form1.txt_config_email1.focus()
		}
	/* 	function check() {
			// alert("count6565");
			var count = 0;
			count = count + 1;
			alert("count" + count);

		} */
	</script>

</body>
</html>
