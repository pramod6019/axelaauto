<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="export" class="axela.preowned.Report_PreownedCRM_Details" scope="request"/>
<%export.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD> 
<title><%=export.AppName%> - <%=export.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../Library/theme<%=export.GetTheme(request)%>/jquery-ui.css">
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../Library/theme<%=export.GetTheme(request)%>/style.css">
<link href="../Library/theme<%=export.GetTheme(request)%>/menu.css"
	rel="stylesheet" media="screen" type="text/css" />
<link
	href="../Library/theme<%=export.GetTheme(request)%>/menu-mobile.css"
	rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=export.jsver%>"></script>
<link rel="shortcut icon" href="../test/favicon.ico" />


<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<script type="text/javascript"
	src="../Library/theme<%=export.GetTheme(request)%>/menu.js"></script>
<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../assets/css/footable.core.css">
	
	
	
<script>
	$(function() {
		$("#txt_starttime").datepicker({
			showButtonPanel : true,
			dateFormat : "dd/mm/yy"
		});
		$("#txt_endtime").datepicker({
			showButtonPanel : true,
			dateFormat : "dd/mm/yy"
		});
	});
</script>
<style>
@media ( max-width : 1024px) {
	/*    TABLE RESPONSIVE START   */
	td {
		display: block;
	}
	/*    TABLE RESPONSIVE END   */
}
</style>
<link href="../assets/themes/theme<%=export.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />	
	<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>		
		
<script>
function coming(){
	   var temp = $("#dr_crmdays_id").val();
	  // alert("temp-----------"+temp);
	   document.getElementById('txt_dr_crmdays_id').value=temp;
}
	function PopulateCRMDays() {
	var branch_id=document.getElementById("dr_branch").value;
	var crmtype_id=document.getElementById("dr_crmdays_crmtype_id").value;
	showHint('../preowned/mis-check.jsp?crmdaysfollowupsingle=yes&esc=yes&crmtype_id='+crmtype_id+'&exe_branch_id='+branch_id, 'crmdaysHint');
    }
	///
	</script>

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
						<h1>Preowned CRM Follow-up Details</h1>
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
						<li><a href="report-preownedcrm-details.jsp">Preowned CRM Follow-up Details</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
           <center><font color="red"><b> <%=export.msg%></b></font></center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">

							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Preowned CRM Follow-up Details
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
											<div class="container-fluid">
											
											<div class="col-md-2 col-sm-4">
													<label class="control-label col-md-3">Branch: </label>
													<div class="col-md-9">
														
															<%if(export.branch_id.equals("0")){%>
                                                        <select name="dr_branch" id="dr_branch"
															class="form-control">
																<%=export.PopulateBranch(export.dr_branch_id,"","2", "", request)%>
														</select>
															<%}else{%>
															<input type="hidden" name="dr_branch" id="dr_branch"
															value="<%=export.branch_id%>"/>
															<%=export.getBranchName(export.dr_branch_id, export.comp_id)%>
															<%}%>
													</div>
												</div>
												<div class="col-md-2 col-sm-4">
													<label class="control-label col-md-4">Type: </label>
													<div class="col-md-8">
															<select name="dr_crmdays_crmtype_id" class="form-control"
															id="dr_crmdays_crmtype_id">
																<%=export.PopulateCRMType()%>
														</select>
														
													</div>
												</div>
												<div class="col-md-3 col-sm-4">
													<label class="control-label col-md-4">Follow-up Days<font color="red">*</font>: </label>
													<div class="col-md-8">
														<span id="crmdaysHint">
															<%export.rep.precrmfollowupdays_id = export.precrmfollowupdays_id; %>  
															<%=export.rep.PopulatePreownedCRMDays(export.comp_id, export.dr_branch_id)%>
														</span>
														<input type="text" id="txt_dr_crmdays_id" name="txt_dr_crmdays_id" hidden>
													</div>
												</div>
												<div class="col-md-2 col-sm-4">
													<label class="control-label col-md-4">From Date:</label>
													<div class="col-md-8">
														<input name="txt_starttime" id="txt_starttime"
															value="<%=export.from_date%>"
															class="form-control date-picker"
															data-date-format="dd/mm/yyyy" type="text" value="" />
													</div>
												</div>
												<div class="col-md-2 col-sm-4">
													<label class="control-label col-md-4">To Date:</label>
													<div class="col-md-8">
														<input name="txt_endtime" id="txt_endtime"
															value="<%=export.to_date%>"
															class="form-control date-picker"
															data-date-format="dd/mm/yyyy" type="text" value="" />
													</div>
												</div>

												<div class="col-md-1 col-sm-12" align="center">
													<center>
														<input name="btn_export" id="btn_export" style="margin-top: 1px" type="submit" class="btn btn-success" value="Export" />
													</center>
												</div>
											</div>
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
	<%@include file="../Library/admin-footer.jsp"%>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=export.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="../assets/js/footable.js" type="text/javascript"></script>
	<script src="../assets/js/components-date-time-pickers.js"
		type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datepicker.js"
		type="text/javascript"></script>
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

</body>
</html>
