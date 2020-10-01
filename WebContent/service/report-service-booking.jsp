<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_Service_Booking" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">   

<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
	<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

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


	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<!-- Start additional plugins -->
<script type="text/javascript" src="../Library/amcharts/amcharts.js"></script>
<script type="text/javascript" src="../Library/amcharts/pie.js"></script>

	<script src="../assets/js/bootstrap-datepicker.js"
		type="text/javascript"></script>
<!-- End Export Image additional plugins -->
<!-- End additional plugins -->
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<script language="JavaScript" type="text/javascript">
   /* function PopulateTeam(){		
        var branch_id=document.getElementById("dr_branch").value;		
	    showHint('../sales/report-team-check.jsp?team=yes&exe_branch_id='+branch_id,  'multiteam');		
	    } */
   /*  function ExeCheck() { //v1.0
	var branch_id=document.getElementById("dr_branch_id").value;
	var emp_id=outputSelected(document.getElementById("dr_emp_id").options);	
	showHint('../sales/mis-check.jsp?multiple=yes&emp_id='+emp_id+'&branch_id=' + GetReplace(branch_id) ,branch_id,'');
    } */
	/* function PopulateCRMFollowupdays(){
		var branch_id=document.getElementById("dr_branch").value;
		//alert(branch_id);
		showHint('../sales/mis-check.jsp?crmfollowupdays=yes&exe_branch_id=' + GetReplace(branch_id),'followupHint');
	} */
	
    </script>
<script>
$(function() {
    $( "#txt_starttime" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
	
	 $( "#txt_endtime" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });

  });
    </script>


<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Service Booking Status</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-service-booking.jsp">Service Booking Status</a><b>:</b></li>
						
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="page-content-inner">
					<div class="tab-pane" id="">
<!-- 					BODY START -->
					<!-- 	PORTLET -->
					<center><font color="red"><b><%=mybean.msg%></b></font></center>
	<div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
						Service Booking Status 
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="tab-pane" id="">
						<!-- START PORTLET BODY -->
						<!-- FORM START -->
						<form method="post" name="frm1"  id="frm1" class="form-horizontal">
<!-- FORM START -->
<div class="container-fluid">
<div class="col-md-5">
<label class="control-label col-md-4"> Start Date<font color=red>*</font>: </label>
<div class="col-md-8 col-xs-12" id="emprows">
<input name ="txt_starttime" id="txt_starttime" type="text"  value="<%=mybean.start_time %>" size="12" maxlength="10" class="form-control date-picker"
							data-date-format="dd/mm/yyyy"  value="" />	

</div>
</div>

<!-- FORM START -->
<div class="col-md-6">
<label class="control-label col-md-4"> End Date<font color=red>*</font>: </label>
<div class="col-md-8 col-xs-12" id="emprows">
 <input name ="txt_endtime" id ="txt_endtime" type="text"  value="<%=mybean.end_time %>" size="12" maxlength="10" class="form-control date-picker"
data-date-format="dd/mm/yyyy"  value=""/>	
</div>
</div>

<!-- FORM START -->
<div class="col-md-1">
<label class="control-label col-md-4">  </label>
<div class="col-md-8 col-xs-12" id="emprows">
 <center><input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Go" /></center>
    <input type="hidden" name="submit_button" value="Submit"/>	

</div>
</div>
</div>
<br><br>
<div class="container-fluid">
<div class="col-md-5">
<label class="control-label col-md-4"> Branch: </label>
<div class="col-md-8 col-xs-12" id="emprows">
   <div id="multiteam">
    <select name="dr_branch_id" size="10" multiple="multiple" class="form-control" id="dr_branch_id" >
    <%=mybean.PopulateBranch()%>
       </select>
 </div>	
</div>
</div>

<!-- FORM START -->
<div class="col-md-5">
<label class="control-label col-md-5"> CRM Executive: </label>
<div class="col-md-7 col-xs-12" id="emprows">
 <div id="multiteam">
   <select name="dr_emp_id" size="10" multiple="multiple" class="form-control" id="dr_emp_id" >
   <%=mybean.PopulateCRM()%>
   </select>
    </div>	

</div>
</div>
</div>


</form>
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

 <%@include file="../Library/admin-footer.jsp" %>
</body>
</HTML>
