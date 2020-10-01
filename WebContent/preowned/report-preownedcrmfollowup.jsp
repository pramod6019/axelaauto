<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.Report_PreownedCRMFollowup"
	scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">

<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
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
	src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<link rel="shortcut icon" href="../test/favicon.ico" />


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
		/* 		display: block; */
		
	}
	/*    TABLE RESPONSIVE END   */
}
</style>

<script type="text/javascript">      
   function PopulateExecutives() {
	    var team_id=document.getElementById("dr_team").value;
	    var branch_id=document.getElementById("dr_branch").value;
		showHint('../preowned/mis-check.jsp?single=yes&executives=yes&exe_branch_id='+branch_id+'&team_id='+team_id,'exeHint');
    }
   function coming(){
	   var temp = $("#dr_precrmfollowupdays_id").val();
	  // alert("temp-----------"+temp);
	   document.getElementById('txt_dr_precrmfollowdays_id').value=temp;
   }
       
	function PopulatePreownedCRMDays() { 
	var branch_id=document.getElementById("dr_branch").value;
	var precrmtype_id=document.getElementById("dr_precrmfollowupdays_precrmtype_id").value;
	showHint('../preowned/mis-check.jsp?crmdaysfollowupsingle=yes&esc=yes&precrmtype_id='+precrmtype_id+'&exe_branch_id='+branch_id,'precrmfollowdaysHint1');    
    }  
</script>

<script type="text/javascript">  
 $(function() {
    $( "#txt_starttime" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
	//
	 $( "#txt_endtime" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
	    
  });
</script>
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
	<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../assets/css/style.css">
</HEAD>


<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Pre-Owned CRM Follow-up</h1>
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
						<li><a href="report-preownedcrmfollowup.jsp">Pre-Owned CRM Follow-up</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="red"><b> <%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Pre-Owned CRM Follow-up</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" class="form-horizontal" name="frm1" id="frm1">
											<div class=" container-fluid">

												<div class="form-body col-md-4 col-sm-6">
													<div class="form-group">
														<label class="col-md-4 control-label"> Branch<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-8">
															<%if(mybean.branch_id.equals("0")){%>
															<select name="dr_branch" class="form-control"
																id="dr_branch"
																onchange="PopulateExecutives();">
																<%=mybean.PopulateBranch(mybean.dr_branch_id, "", "2" , "",request)%>
															</select>
															<%
															} else {
														%>
															<input type="hidden" name="dr_branch" id="dr_branch"
																value="<%=mybean.branch_id%>" />
															<%=mybean.getBranchName(mybean.dr_branch_id, mybean.comp_id)%>
															<%
															}
														%>
														</div>
													</div>
												</div>


<!-- 												<div class="form-body col-md-4 col-sm-6"> -->
<!-- 													<div class="form-group"> -->
<!-- 														<label class="col-md-4 control-label"> Team: </label> -->
<!-- 														<div class="col-md-8"> -->
<%-- 															<span id="teamHint"> <%=mybean.PopulateTeam(mybean.dr_branch_id, mybean.comp_id)%> --%> 
<!-- 															</span> -->
<!-- 														</div> -->
<!-- 													</div> -->
<!-- 												</div> -->
												<div class="form-body col-md-4 col-sm-6">
													<div class="form-group">
														<label class="col-md-4 control-label"> Pre-Owned Consultant:
														</label>
														<div class="col-md-8">
															<span id="exeHint">
																<%=mybean.PopulatePreownedExecutives(mybean.team_id, mybean.comp_id)%>
															</span>
														</div>
													</div>
												</div>
												
												<div class="form-body col-md-4 col-sm-6">
													<div class="form-group">
														<label class="col-md-4 control-label"> Start Date:
														</label>
														<div class="col-md-8">
															<input name="txt_starttime" id="txt_starttime"
																value="<%=mybean.start_time%>"
																class="form-control date-picker"
																data-date-format="dd/mm/yyyy" type="text" value="" />
														</div>
													</div>
												</div>
												
											</div>

											<div class=" container-fluid">

												

												<div class="form-body col-md-4 col-sm-6">
													<div class="form-group">
														<label class="col-md-4 control-label"> End Date: </label>
														<div class="col-md-8">
															<input name="txt_endtime" id="txt_endtime"
																value="<%=mybean.end_time%>"
																class="form-control date-picker"
																data-date-format="dd/mm/yyyy" type="text" value="" />
														</div>
													</div>
												</div>


												<div class="form-body col-md-4 col-sm-6">
													<div class="form-group">
														<label class="col-md-4 control-label">
															Contactable: </label>
														<div class="col-md-8">
															<select name="dr_precrmfeedbacktype_id" class="form-control"
																id="dr_precrmfeedbacktype_id" visible="true">
																<%=mybean.PopulatePreownedCRMFeedbackType()%>
															</select>
														</div>
													</div>


												</div>
												
												<div class="form-body col-md-4 col-sm-6">
													<div class="form-group">
														<label class="col-md-4 control-label"> Type<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-8">
															<select name="dr_precrmfollowupdays_precrmtype_id" class="form-control" id="dr_precrmfollowupdays_precrmtype_id" onchange="PopulatePreownedCRMDays();">
																<%=mybean.PopulatePreownedCRMType()%>
															</select>
														</div>
													</div>
												</div>
											</div>
											
											<div class="container-fluid">

												

												<div class="form-body col-md-4 col-sm-6">
													<div class="form-group">
														<label class="col-md-4 control-label"> Follow-up Days: </label>
														<div class="col-md-8">
															<span id="precrmfollowdaysHint1"> <%=mybean.PopulatePreownedCRMDays(mybean.comp_id, mybean.dr_branch_id)%>
															</span> <input type="text" id="txt_dr_precrmfollowdays_id"
																name="txt_dr_precrmfollowdays_id" hidden>
														</div>
													</div>
												</div>
												
												<div class="form-body col-md-4 col-sm-6">
													<div class="form-group">
														<label class="col-md-4 control-label"> Experience:
														</label>
														<div class="col-md-8">
															<select name="dr_precrmfollowup_satisfied" class="form-control"
																id="dr_precrmfollowup_satisfied">
																<%=mybean.PopulatePreownedCRMSatisfied()%>
															</select>
														</div>
													</div>
												</div>
												</div>
																<div class="container-fluid">
												<div class="form-body col-md-5 col-sm-6">
													<div class="form-group">
														<label class="col-md-5 col-sm-8 col-xs-8 control-label"> Pending
															Follow-up: </label>
														<div class="col-md-2 col-xs-2 col-sm-2">
															<input id="chk_pending_followup" style="margin-top:10px;"
																name="chk_pending_followup" type="checkbox"
																<%=mybean.PopulateCheck(mybean.pending_followup)%> />

														</div>
													</div>
												</div>
 
											<div class="form-body col-md-7 col-sm-6">
													<div class="form-group">
											 
																<input type="submit" name="submit_button"
																	id="submit_button" class="btn btn-success" value="Go" />
																<input type="hidden" name="submit_button" value="Submit" />
								 </div>
								 </div>
											</div>
										</form>			
									</div>

									

								</div>
							</div>

							<%if(!mybean.StrHTML.equals("")){ %>
							<!-- 	<div class="portlet box  "> -->
							<!-- 		<div class="portlet-title" style="text-align: left"></div> -->
							<!-- 		<div class="portlet-body portlet-empty"> -->
							<!-- 			<div class="tab-pane" id=""> -->
							<!-- START PORTLET BODY -->
							<center><%=mybean.StrHTML%></center>
							<!-- 			</div> -->
							<!-- 		</div> -->
							<!-- 	</div> -->
							<%} %>

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
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>

	<script src="../assets/js/components-date-time-pickers.js"
		type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datepicker.js"
		type="text/javascript"></script>
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
</body>
</html>
