<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Ticket_List_Followup" scope="request"/>
<jsp:useBean id="export" class="axela.customer.Customer_Export" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</head>
<body  class="page-container-bg-solid page-header-menu-fixed">
<%@include file="../portal/header.jsp" %>
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
						<h1>List Ticket Follow-up</h1>
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
						<li><a href="index.jsp">Service</a> &gt;</li>
						<li><a href="ticket.jsp">Ticket</a> &gt;</li>
						<li><a href="ticket-list-followup.jsp">List Ticket Follow-up</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="page-content-inner">
					<div class="tab-pane" id="">
						<%@include file="../Library/landing-branch.jsp" %>
					</div>
				</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
 <%@include file="../Library/admin-footer.jsp" %>
 <%@include file="../Library/css.jsp"%>
</body>
</html>
