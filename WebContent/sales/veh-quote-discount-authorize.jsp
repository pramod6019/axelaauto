<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Veh_Quote_Discount_Authorize"
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
						<h1>Quote Discount Authorize</h1>
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
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="veh-quote.jsp">Quote</a> &gt;</li>
						<li><a href="veh-quote-list.jsp?all=yes">List Quotes</a> &gt;</li>
						<li><a href="veh-quote-discount-list.jsp?quote_id=<%=mybean.quote_id%>">Quote ID: <%=mybean.quote_id%></a> &gt;</li> 
						<li><a href="veh-quote-discount-authorize.jsp?quote_id=<%=mybean.quote_id%>&quotediscount_id=<%=mybean.quotediscount_id %>">Quote Discount Authorize</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center>Quote Discount Authorize</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											
											<div class="form-element6">
												<label>Quote ID:</label>
												<a href="veh-quote-list.jsp?quote_id=<%=mybean.quote_id%>"><%=mybean.quote_id%></a>
											</div>
											
											<div class="form-element6">
												<label>Quote Date:</label>
												<%=mybean.quote_date%>
											</div>
											
											<div class="row"></div>
											
											<% if(!mybean.so_id.equals("0") && !mybean.so_active.equals("0")) { %>
											
											<div class="form-element6">
												<label>SO ID:</label>
												<a href="veh-salesorder-list.jsp?so_id=<%=mybean.so_id%>"><%=mybean.so_id%></a>
											</div>
											
											<div class="form-element6">
												<label>SO Date:</label>
												<%=mybean.so_date%>
											</div>
											
											<% } %>	
											
											<div class="row"></div>
											
											<div class="form-element6">
												<label>Branch:</label>
												<a href="../portal/branch-summary.jsp?branch_id=<%=mybean.branch_id%>"/><%=mybean.branch_name%></a>
											</div>
											
											<div class="form-element6">
												<label>Team Manager:</label>
												<a href="../portal/executive-summary.jsp?emp_id=<%=mybean.team_leader_id%>"/><%=mybean.team_leader%></a>
											</div>
											
											<div class="form-element6">
												<label>Quote Executive:</label>
												<a href="../portal/executive-summary.jsp?emp_id=<%=mybean.quote_emp_id%>"/><%=mybean.quote_emp%></a>
											</div>
											
											<div class="form-element6">
												<label>Customer:</label>
												<%=mybean.customer_name%>
											</div>
											
											<div class="form-element6">
												<label>Model:</label>
												<%=mybean.model_name%>
											</div>
											
											<div class="form-element6">
												<label>Item:</label>
												<%=mybean.item_name%>
											</div>
											
											<div class="form-element6">
												<label>Quote Amount:</label>
												<%=mybean.quote_grandtotal%>
											</div>
											
											<div class="form-element6">
												<label>Requested Discount Amount:</label>
												<%=mybean.IndDecimalFormat(mybean.quotediscount_requestedamount)%>
												<input type="hidden" id="txt_quotediscount_requestedamount" name="txt_quotediscount_requestedamount" value="<%=mybean.quotediscount_requestedamount%>" />
											</div>
											
											<div class="form-element6">
												<label>Authorized Discount Amount<font color="#ff0000">*</font>: </label>
												<input type="text" class="form-control"id="txt_quotediscount_authorizedamount"
												 name="txt_quotediscount_authorizedamount"
												 onKeyUp="toFloat('txt_quotediscount_authorizedamount','Amount')"
												 value="<%=mybean.quotediscount_authorizedamount%>" />
											</div>
											
											<div class="row"></div>
											
											<%
												if (!mybean.quotediscount_authorize_time.equals("")) {
											%>
											<div class="form-element6">
												<label>Authorized By:</label>
												<%=mybean.unescapehtml(mybean.quotediscount_authorized_by)%>
												<input name="quotediscount_authorized_by" type="hidden"
													id="quotediscount_authorized_by"
													value="<%=mybean.unescapehtml(mybean.quotediscount_authorized_by)%>" />
											</div>
											<div class="form-element6">
												<label>Authorized Date:</label>
												<%=mybean.quotediscount_authorizetime%>
												<input type="hidden" id="quotediscount_authorizetime" name="quotediscount_authorizetime" value="<%=mybean.quotediscount_authorizetime%>" />
											</div>
											<%
												}else{
											%>
											<div class="form-element6">
												<label>Due By:</label>
												<%=mybean.unescapehtml(mybean.quotediscount_authorized_by)%>
												<input name="quotediscount_authorized_by" type="hidden"
													id="quotediscount_authorized_by"
													value="<%=mybean.unescapehtml(mybean.quotediscount_authorized_by)%>" />
											</div>
											
											<%} %>
											
											
											
											<div class="row"></div>
											<center>
												<input type="hidden" name="accept_button" value="yes">
												<input name="button" type="submit" class="btn btn-success" id="button" value="Authorize" />
												<input name="decline_button" type="submit" class="btn btn-success" id="decline_button" value="Decline" />
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

