<%@ page errorPage="error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.accounting.Report_Payables" scope="request"/>
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
        <HEAD>
        <title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
        </HEAD>
        <body  onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
        <%@include file="../portal/header.jsp" %>
        <div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Payables</h1>
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
						<li><a href="../portal/home.jsp">Home</a> &gt; </li>
						<li><a href=../accounting/index.jsp>Accounting</a> &gt; </li>
						<li><a href=report-payables.jsp>Payables</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="tab-pane" id="">
<!-- 					BODY START -->
					<center><font color="#ff0000" ><b><%=mybean.msg%></b></font></center>
						 <div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
						Payables 
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="container-fluid">
						<!-- START PORTLET BODY -->
						<%=mybean.StrHTML%>
					</div>
				</div>
			</div>
					</div>
				</div>
				</div>
			</div>
		</div>
	</div>
<%@include file="../Library/admin-footer.jsp" %>
<%@include file="../Library/js.jsp"%>
        <script language="JavaScript" type="text/javascript">
            function FormFocus() { //v1.0
//                document.form1.dr_branch_id.focus()
            }
        </script>
</body>
</HTML>
