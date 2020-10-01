<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Report_PsfFollowup_Analysis" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
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
						<h1>PSF Follow-up Analysis</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-psffollowup-analysis.jsp">PSF Follow-up Analysis</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="tab-pane" id="">
						<!-- 					BODY START -->
						<!-- START PORTLET  -->
						<center>
							<font color="red"><b> <%=mybean.msg%> </b></font>
						</center>
						<div class="portlet box ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">PSF Follow-up Analysis</div>
							</div>

							<div class="portlet-body portlet-empty container-fluid">
								<div class="tab-pane" id="">
									<form method="post" name="frm1" id="frm1" class="form-horizontal">

										<div class="form-element6">
											<label> Start Date<font color=red>*</font>: </label>
											<input name="txt_starttime" id="txt_starttime" value="<%=mybean.start_time%>"
												class="form-control datepicker" type="text" size="12" maxlength="10" />
										</div>

										<div class="form-element6">
											<label> End Date<font color=red>*</font>: </label>
											<input name="txt_endtime" id="txt_endtime" value="<%=mybean.end_time%>" 
												class="form-control datepicker" type="text" size="12" maxlength="10" />
										</div>

										<div class="form-element3">
											<label>Brands:</label>
											<div>
												<select name="dr_principal" size="10" multiple="multiple"
													class="form-control multiselect-dropdown" id="dr_principal"
													onChange="PopulateModels();PopulateBranch();PopulateRegion();">
													<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
												</select>
											</div>
										</div>

										<div class="form-element3">
											<label>Regions:</label>
											<div id="regionHint">
												<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
											</div>
										</div>

										<div class="form-element3">
											<label>Branch:</label>
											<div id="branchHint"> 
												<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.jc_branch_ids, mybean.comp_id,request)%>
											</div>
										</div>

										<div class="form-element3">
											<label>Advisor:</label>
											<div id="multiadvisor">
												<%=mybean.mischeck.PopulateAdviser(mybean.jc_emp_ids, mybean.jc_branch_id, mybean.comp_id, request)%>
											</div>
										</div>


										<div class="form-element3">
											<label>CRM Executive:</label>
											<div id="multicrm">
												<%=mybean.mischeck.PopulateCRM(mybean.crm_emp_ids, mybean.jc_branch_id, mybean.comp_id, request)%>
											</div>
										</div>

										<div class="form-element3">
											<label>Follow-up Days:</label>
											<div id="multipsfdays">
												<%=mybean.mischeck.PopulatePSFDays(mybean.psfdays_ids, mybean.jc_branch_id, mybean.comp_id)%>
											</div>
										</div>

										<div class="form-element3">
											<label>Jobcard Type:</label>
											<div>
												<select name="dr_jc_jctype_id" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_jc_jctype_id">
													<%=mybean.PopulateJobcardType(mybean.comp_id)%>
												</select>
											</div>
										</div>

										<div class="form-element3">
											<label>Model:</label>
											<div id="modelHint">
												<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.jc_branch_id, mybean.comp_id, request)%>
											</div>
										</div>
								</div>

								<div class="form-element12">
									<center>
										<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Go" />
										<input type="hidden" name="submit_button" value="Submit" />
									</center>
								</div>

								</form>
							</div>
						</div>
					</div>

					<%
						if (!mybean.StrHTML1.equals("") || !mybean.StrHTML.equals("") || !mybean.strHTML2.equals("")) {
					%>
					<div class="portlet box">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none"></div>
						</div>
						<div class="portlet-body portlet-empty container-fluid">
							<div class="tab-pane" id="">
								<center>
									<div class="">
										<%=mybean.StrHTML1%><br/>
										<%=mybean.StrHTML%><br/>
										<%=mybean.strHTML2%> 
									</div>
								</center>
							</div>
						</div>
					</div>
					<%} %> 
				</div>
			</div> 
		</div>
	</div>
	<!-- END CONTAINER ----------->

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript">
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}
	
		function PopulateBranch() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		}
	
		function PopulateModels() { 
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
			showHint('../service/mis-check1.jsp?brand_id=' + brand_id +"&branch_id= " + branch_id + '&model=yes', 'modelHint');
		}
	
		function PopulateAdviser() {
			var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
			showHint('../service/mis-check1.jsp?branch_id=' + branch_id + '&advisor=yes', 'multiadvisor');
		}
	
		function PopulateCRM() {
			var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
			showHint('../service/mis-check1.jsp?branch_id=' + branch_id + '&crmexe=yes', 'multicrm');
		}
	
		function Populatepsfdays() {
			var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
			showHint('../service/mis-check1.jsp?branch_id=' + branch_id + '&psfdays=yes', 'multipsfdays');
		}
		
		function PopulateTech(){}
		
		function PopulateCRMDays(){}
	</script>
</body>
</HTML>
