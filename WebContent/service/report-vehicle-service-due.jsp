<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_Vehicle_Service_Due" scope="request"/>   
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
		<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
	<link rel="shortcut icon" href="../test/favicon.ico" />
	<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
 <LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../assets/css/footable.core.css">
<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

<script language="JavaScript" type="text/javascript">

function ExeCheck() {
//	 	alert("yudsd");
	var branch_id=document.getElementById("dr_branch_id").value;
	showHint('../service/report-check.jsp?jcpsfexecutive=yes&branch_id=' + GetReplace(branch_id) ,'exeHint');
}

 </script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed" >
<%@include file="../portal/header.jsp" %>
<!-- 	MULTIPLE SELECT END-->	
<!-- 	BODY -->
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
						<h1>Service Due</h1>
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
						<li> <a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-vehicle-service-due.jsp">Service Due</a><b>:</b></li>
						
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="page-content-inner">
					<div class="tab-pane" id="">
<!-- 					BODY START -->
					
					<!-- 	PORTLET -->
					<center><font color="red"><b> <%=mybean.msg%> </b></font></center>
	<div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
						Service Due 
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="tab-pane" id="">
						<!-- START PORTLET BODY -->
					<!-- FORM START -->
					<form method="post" name="frm1"  id="frm1" class="form-horizontal">
<div class="col-md-2">
<label class="control-label col-md-4"> Branch<font color="#ff0000">*</font>: </label>
<div class="col-md-8 col-xs-12" id="emprows">
   <%if(mybean.branch_id.equals("0")){%>
                             <select name="dr_branch_id" class="form-control" id="dr_branch_id" onChange="ExeCheck();"> 
                              <%=mybean.PopulateBranch()%>
                             </select> 
                            <%}else{%>
                            <input type="hidden" name="dr_branch_id" id="dr_branch_id" value="<%=mybean.branch_id%>" />
                           <%=mybean.PopulateBranch()%>
                            <%}%>	
</div>
</div>

<!-- FORM START -->
<div class="col-md-4">
<label class="control-label col-md-4"> Service Advisor: </label>
<div class="col-md-6 col-xs-12" id="emprows">
 <span id="exeHint"><%=mybean.PopulaServiceExecutives(mybean.empservice_id, mybean.comp_id, mybean.ExeAccess) %></span> 	
</div>
</div>

<!-- FORM START -->
<div class="col-md-3">
<label class="control-label col-md-4"> Vehicle Kms: </label>
<div class="col-md-6 col-xs-12" id="emprows">
 <input name ="txt_veh_kms" id="txt_veh_kms" type="text" class="form-control" value="<%=mybean.veh_kms %>" size="12" maxlength="10" />	
</div>
</div>

<!-- FORM START -->
<div class="col-md-2">
<label class="control-label col-md-4"> Months: </label>
<div class="col-md-6 col-xs-12" id="emprows">
 <input name ="txt_months" id ="txt_months" type="text" class="form-control" value="<%=mybean.months %>" size="12" maxlength="10"/>	
</div>
</div>

<!-- FORM START -->
<div class="col-md-1">
<label class="control-label col-md-4">  </label>
<div class="col-md-6 col-xs-12" id="emprows">
 <center><input name="submit_button" type="submit" class="btn btn-success" id="submit_button"  style="margin-top:1px" value="Go" />
                            <input type="hidden" name="submit_button" value="Submit"/></center>	

</div>
</div>

	</div>
</div>
	</div>
<!-- 	PORTLET -->
<center><%=mybean.StrHTML%></center>

</div>
				</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->

<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<%-- <script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script> --%>
<script src="../assets/js/components-date-time-pickers.js"
		type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datepicker.js"
		type="text/javascript"></script>
		<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script src="../assets/js/footable.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>
 <%@include file="../Library/admin-footer.jsp" %></body>
</html>