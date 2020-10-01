<%@ page errorPage="error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.preowned.Preowned_TestDrive_Cust_Fb_Dash" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp" %>
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
						<h1>Pre-Owned Test Drive Customer Feedback</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="preowned-testdrive-cust-fb-dash.jsp">Pre-Owned Test Drive Customer Feedback</a><b>:</b></li>
					</ul>
					<center>
						<font color="red"><b><%=mybean.msg%></b></font>
					</center>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Pre-Owned
										Customer Test Drive Feedback</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">
										
												<div class="form-element6">
														<label>
															Start Date<font color=red>*</font>:</label> <input
																name="txt_starttime" id="txt_starttime"
																value="<%=mybean.start_time%>"
																class="form-control datepicker"
																 type="text" maxlength="10" />
													</div>

													<div class="form-element6">
														<label>
															End Date<font color=red>*</font>:</label> <input
																name="txt_endtime" id="txt_endtime"
																value="<%=mybean.end_time%>"
																class="form-control datepicker"
															 type="text" maxlength="10" />
													</div>

													<div class="form-element4">
														<label >
															Brands:</label>
															<div id="multiprincipal">
																<select name="dr_principal" size="10" multiple="multiple" class="form-control multiselect-dropdown"
																	 id="dr_principal" onChange="PopulateBranches();PopulateRegion();">
																	<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
																</select>
														</div>
													</div>

													<div class="form-element4">
														<label>
															Regions:</label> <div id="regionHint"> 
															<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
														</div>
													</div>

													<div class="form-element4">
														<label>
															Branches: </label><div id="branchHint"> 
															<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
														</div>
													</div>
													
													<div class="form-element4">
														<label>
															Teams: </label><div id="teamHint"> 
															<%=mybean.mischeck.PopulatePreownedTeams(mybean.branch_id,mybean.team_ids, mybean.comp_id,request)%>
															</div>
													</div>
													<div class="form-element4">
														<label >
															Pre-Owned Consultant:</label>
															<div id="exeHint">
																<%=mybean.mischeck.PopulatePreownedExecutives(mybean.branch_id, mybean.team_id, mybean.comp_id)%>
															</div>
													</div>

												
													<div class="form-element4">
														<label > Model: </label>
														<div>
														<select name="dr_model" 
																multiple="multiple" class="form-control multiselect-dropdown" id="dr_model">
																<%=mybean.PopulateModel(mybean.comp_id)%>
															</select>
														</div>
														</div>

												<div class="form-element12">
											<center>
													<input name="submit_button" type="submit"
														class="btn btn-success" id="submit_button" value="Go" />
													<input type="hidden" name="submit_button" value="Submit">
											</center>
												</div>
											</div>
															</div>
							<center><%=mybean.StrHTML%></center>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->

	<%@ include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp" %>
	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
			//document.formcontact.txt_customer_name.focus(); 
		}
		function frmSubmit() {
			document.frm1.submit();
		}
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document
					.getElementById("dr_principal").options);
			showHint('../preowned/mis-check.jsp?multiple=yes&brand_id='
					+ brand_id + '&region=yes', 'regionHint');
		}

		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document
					.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../preowned/mis-check.jsp?multiple=yes&brand_id='
					+ brand_id + '&region_id=' + region_id + '&branch=yes',
					'branchHint');
		}
		function PopulatePreownedTeams() {
			var branch_id = $('#dr_branch').val();
			showHint('../preowned/mis-check.jsp?branch_id=' + branch_id + '&preownedteam=yes', 'teamHint');
		}

		function PopulateExecutives() { //v1.0
			var team_id = $('#dr_preownedteam').val();
			showHint('../preowned/mis-check.jsp?team_id=' + team_id + ',&executives=yes', 'exeHint');
		}
		//Dont remove this methods
		function PopulateAdviser(){
			
		}function  PopulateTech(){
			
		}
		function  Populatepsfdays(){
			
		} function PopulateCRMDays(){
			
		}
		///
	</script>
</body>
</HTML>
