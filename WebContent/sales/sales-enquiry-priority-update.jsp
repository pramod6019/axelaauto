<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Sales_Enquiry_Priority_Update" scope="request"/>  
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <HEAD>
    <title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed" onload="FormFocus()">
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
						<h1>Import Inventory Stocks </h1>
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
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="sales.jsp">Sales</a> &gt;</li>
					<li><a href="../sales/sales-profitability-update.jsp">Sales Profitability</a> :</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div>
						<center>
							<font color="#FF0000"><b><%=mybean.msg%></b></font>
						</center>
					</div>
					</div>
				</div>
				</div>
			</div>
		</div>
	</div>
   <%@include file="../Library/admin-footer.jsp" %>
     <%@include file="../Library/js.jsp" %>   
        <script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
 		 document.frmdoc.dr_branch.focus();
			}
        </script>
  </body>
</html>
