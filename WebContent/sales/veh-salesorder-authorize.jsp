<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Veh_Salesorder_Authorize"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp"%>
</HEAD>
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
						<h1>Sales Order Authorize</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="veh-salesorder.jsp">Sales Orders</a>&gt;</li>
						<li><a href="veh-salesorder-list.jsp?all=yes">List Sales
								Orders</a>&gt;</li>
						<li><a href="veh-salesorder-list.jsp?so_id=<%=mybean.so_id%>">Sales
								Order ID: <%=mybean.so_id%></a> &gt; <a
							href="veh-salesorder-authorize.jsp?so_id=<%=mybean.so_id%>">Sales
								Order Authorize</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center>Authorize Sales Order</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<form name="form1" method="post" class="form-horizontal">
											<div class="form-element6 form-element-center">
												<label>Sales Order No.:</label> <a
													href="veh-salesorder-list.jsp?so_id=<%=mybean.so_id%>"><%=mybean.so_no%></a>
											</div>
											<div class="form-element6 form-element-center">
												<label>Sales Order ID:</label>
												<%=mybean.so_id%>
											</div>
											<div class="form-element6 form-element-center">
												<label>Customer:</label>
												<%=mybean.link_customer_name%>
											</div>
											<div class="form-element6 form-element-center">
												<label>Authorize:</label> <input id="chk_so_auth"
													type="checkbox" name="chk_so_auth"
													<%=mybean.PopulateCheck(mybean.so_auth)%> />
											</div>

											<%
												if (!(mybean.so_auth_date == null) && !(mybean.so_auth_date.equals(""))) {
											%>

											<div class="form-element6 form-element-center">
												<label>Authorized by:</label>
												<%=mybean.unescapehtml(mybean.so_authorized_by)%>
												<input name="so_authorized_by" type="hidden"
													id="so_authorized_by"
													value="<%=mybean.unescapehtml(mybean.so_authorized_by)%>" />
											</div>
											<div class="form-element6 form-element-center">
												<label>Authorized date:</label>
												<%=mybean.so_authdate%>
												<input type="hidden" id="so_authdate" name="so_authdate"
													value="<%=mybean.so_authdate%>" />
											</div>
											<%
												}
											%>
											<center>
												<input type="hidden" name="update_button"
													value="Update Sales Order" /> <input type="hidden"
													id="Update" name="Update" value="yes" /> <input
													name="update_button" id="update_button" type="submit"
													class="btn btn-success" value="Update Sales Order" />
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
	<%@include file="../Library/js.jsp"%>
</body>
</html>
<!-- END CONTAINER -->
