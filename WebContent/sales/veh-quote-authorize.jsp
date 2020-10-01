<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Veh_Quote_Authorize"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
	<%@include file="../Library/css.jsp"%>
<link href="../assets/css/multi-select.css" rel="stylesheet"
	type="text/css" />
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
						<h1>Quote Authorize</h1>
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
						<li><a href="veh-quote.jsp">Quote</a>&gt;</li>
						<li><a href="veh-quote-list.jsp?all=yes">List Quotes</a> &gt;</li>
						<li><a
							href="veh-quote-list.jsp?quote_id=<%=mybean.quote_id%>">Quote
								ID: <%=mybean.quote_id%></a> &gt; <a
							href="veh-quote-authorize.jsp?quote_id=<%=mybean.quote_id%>">Quote
								Authorize</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center>Authorize Quote</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											<div class="form-element6 form-element-center">
												<label>Quote No.:</label> <a
													href="veh-quote-list.jsp?quote_id=<%=mybean.quote_id%>"><%=mybean.quote_no%></a>
											</div>
											<div class="form-element6 form-element-center">
												<label>Quote ID:</label> <a
													href="veh-quote-list.jsp?quote_id=<%=mybean.quote_id%>"><%=mybean.quote_no%></a>
											</div>
											<div class="form-element6 form-element-center">
												<label>Customer:</label>
												<%=mybean.link_customer_name%>
											</div>
											<div class="form-element6 form-element-center">
												<label>Authorize:</label> <input id="chk_quote_auth"
													type="checkbox" name="chk_quote_auth"
													<%=mybean.PopulateCheck(mybean.quote_auth)%> />
											</div>

											<%
												if (!(mybean.quote_auth_date == null) && !(mybean.quote_auth_date.equals(""))) {
											%>
											<div class="form-element6 form-element-center">
												<label>Authorized by:</label>
												<%=mybean.unescapehtml(mybean.quote_authorized_by)%>
												<input name="quote_authorized_by" type="hidden"
													id="quote_authorized_by"
													value="<%=mybean.unescapehtml(mybean.quote_authorized_by)%>" />
											</div>
											<div class="form-element6 form-element-center">
												<label>Authorized date:</label>
												<%=mybean.quote_authdate%>
												<input type="hidden" id="quote_authdate"
													name="quote_authdate" value="<%=mybean.quote_authdate%>" />
												</td>
											</div>
											<%
												}
											%>
											<center>
												<input type="hidden" name="update_button"
													value="Update Quote" /> <input type="hidden" id="Update"
													name="Update" value="yes" /> <input name="update_button"
													id="update_button" type="submit" class="btn btn-success"
													value="Update Quote" />
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

		<!-- END CONTAINER -->

		<%@include file="../Library/admin-footer.jsp"%>
		<%@include file="../Library/js.jsp"%>
		<script type="text/javascript"
			<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
</body>
</HTML>

