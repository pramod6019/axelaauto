<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Enquiry_Brochure_Email" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">   
<%@include file="../Library/css.jsp"%>
</HEAD>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1>Email Brochure</h1>
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
						<li><a href="enquiry.jsp">Enquiry</a> &gt;</li>
						<li> <a href="enquiry-list.jsp?all=yes"> List Enquiry</a> &gt; </li>
						<li>	<a href="enquiry-list.jsp?enquiry_id=<%=mybean.enquiry_id%>">Enquiry ID: <%=mybean.enquiry_id%></a> &gt;</li>
						<li><a href="enquiry-brochure-email.jsp">Email Brochure</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center><font color="#ff0000" ><b><%=mybean.msg%></b></font></center>
					<div class="page-content-inner">
					<div class="tab-pane" id="">
<!-- 					BODY START -->
					<div class="portlet box">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
						&nbsp; 
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="container-fluid">
						<!-- START PORTLET BODY -->
					<form name="form1"  method="post" class="form-horizontal">
       <%=mybean.StrHTML%>
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

 <%@include file="../Library/admin-footer.jsp" %>
 <%@include file="../Library/js.jsp"%>
	<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
</body>
</HTML>
