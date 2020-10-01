<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.PurchaseInvoice_User_Import"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@ include file="../Library/css.jsp"%>
</HEAD>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0"
	onload="FormFocus()">
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
						<h1>Import Purchase Invoices</h1>
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
						<%if(!mybean.branchtype.equals("")){ %>
						<li><a href="../accounting/index.jsp">Accessories</a> &gt;</li>
						<%}else { %>
						<li><a href="../accounting/index.jsp">Accounting</a> &gt;</li>
						<%} %>
							<li><a href="purchaseinvoice-user-import.jsp">Import Purchase Invoices</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Purchase Invoices Import
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<b><%=mybean.StrHTML%></b>
									</div>
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


	<%-- 
        <table width="98%" border="0"  align="center" cellpadding="5" cellspacing="0">
    		<tr>
             <td valign="top"><a href="../portal/home.jsp">Home</a> &gt; <a href="../service/vehicle.jsp">Vehicle</a> &gt;  <a href="../service/jobcard-user-import.jsp">Import Job Cards</a>: </td>
           
           </tr> 
            <tr>
                           <td align="center" height=400 valign=top><b><%=mybean.StrHTML%></b></td>
                           </tr>
           </table> --%>

	<%@ include file="../Library/admin-footer.jsp"%>
	<%@ include file="../Library/js.jsp"%>
</body>
</html>
