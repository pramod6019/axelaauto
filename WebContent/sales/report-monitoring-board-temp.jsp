<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Report_Monitoring_Board_Temp" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</HEAD>

<body onload="returnpreownedcheck();" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Sales Monitoring Board</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY ----->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="../sales/report-monitoring-board.jsp">Sales Monitoring Board</a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->
           			<center><font color="red"><b> <%=mybean.msg%></b></font></center>
						<div class="tab-pane" id="">

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Monitoring Board
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">
											
<!-- 											<div class="form-element11 form-element"> -->
												<div class="form-element3 ">
													<label>Start Date<font color="red">*</font>: </label>
													<input name="txt_starttime" id="txt_starttime" value="<%=mybean.start_time%>"
														class="form-control datepicker" type="text" />
												</div>
												
												<div class="form-element3">
													<label>End Date<font color="red">*</font>: </label>
													<input name="txt_endtime" id="txt_endtime" value="<%=mybean.end_time%>"
															class="form-control datepicker" type="text"  />
												</div>
													
												<div class="form-element3">
													<label>Total By: </label>
													<select name="dr_totalby" class="form-control" id="dr_totalby">
														<%=mybean.PopulateTotalBy(mybean.comp_id)%>
													</select>
												</div>
														
												<div class="form-element3">
													<label>Order By: </label>
													<div id="divOrderBy">
														<%=mybean.PopulateOrderBy(mybean.comp_id, mybean.include_preowned)%>
													</div>
												</div>
													
													
											<div class="form-element3">
												 <label>Brands:</label>
												<div>
													<select name="dr_principal" size="10" class="form-control multiselect-dropdown" multiple="multiple" id="dr_principal"
														onChange="PopulateModels();PopulateBranches();PopulateRegion();">
														<%=mybean.mischeck.PopulatePrincipal( mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>

											<div class="form-element3">
												<label>Regions:</label> 
												<div id="regionHint" >
													<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element3">
												<label>Branches:</label> 
												<div id="branchHint" > 
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element3">
												<label>Model: </label>
												<div id="modelHint" > 
													<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element3">
												<label>Teams:</label>
												<div id="teamHint" >
													<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
												</div>
											</div>
													
											<div class="form-element3">
												<label>Sales Consultant:</label> 
												<div id="exeHint" > 
													<%=mybean.mischeck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%>
												</div>
											</div>
													
											<div class="form-element3">
												<label>SOE:</label>
												<div >
													<select name="dr_soe" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_soe">
														<%=mybean.PopulateSoe()%>
													</select>
												</div>
											</div>

											<div class="form-element3 form-element-margin">
												<label>Pre-Owned Model: </label>
												<span>
														<input type="checkbox" id="chk_preowned_model" name="chk_preowned_model" onchange="returnpreownedcheck();" <%=mybean.PopulateCheck(mybean.preowned_model) %> "/>
												</span>

											</div>
											
											<div class="row"></div>
											<div class="form-element3 form-element-margin">
												<label>Include Inactive Executive: </label>
												<input type="checkbox" id="chk_include_inactive_exe" name="chk_include_inactive_exe"  <%=mybean.PopulateCheck(mybean.include_inactive_exe) %> "/>
											</div>
											
											<div class="form-element3 form-element-margin">
												<label>Include Pre-Owned: </label>
												<input type="checkbox" id="chk_include_preowned" name="chk_include_preowned" 
												onchange="PopulateOrderBy();"
												 <%=mybean.PopulateCheck(mybean.include_preowned) %> "/>
											</div>
											
											<div class="form-element12">
												<center>
													<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go"/>
													<input type="hidden" name="submit_button" value="Submit" />
												<%
													if (!mybean.emp_copy_access.equals("0") && !mybean.StrHTML.equals("")) {
												%>
												<%}%>
                                               </center>
											</div>

										</form>
									</div>
								</div>
							</div>
							<%
								if(!mybean.StrHTML.equals("")) {
							%>
                     <center><a id="btnExport" onclick="HtmlTableToExcel('table');" >Export</a></center>
							<center><%=mybean.StrHTML%></center>
							<%
								}
							%>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	
	<%@include file="../Library/js.jsp"%>


	<script language="JavaScript" type="text/javascript">
		
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document .getElementById("dr_principal").options);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}
	
		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document .getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
			
		}
	
		function PopulateModels() { //v1.0
			var brand_id = outputSelected(document .getElementById("dr_principal").options);
			showHint('../sales/mis-check1.jsp?brand_id=' + brand_id + '&model=yes', 'modelHint');
		}
	
		function PopulateTeams() {
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint( '../sales/mis-check1.jsp?branch_id=' + branch_id + '&team=yes', 'teamHint');
		}
	
		function PopulateExecutives() { //v1.0
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			var team_id = outputSelected(document.getElementById("dr_team").options);
			showHint('../sales/mis-check1.jsp?exe_branch_id=' + branch_id + '&team_id=' + team_id + '&executives=yes', 'exeHint');
		}
		
		function returnpreownedcheck(){
			 var preownedmodel=document.getElementById('chk_preowned_model').checked;
		    	if(preownedmodel){
		     		$("#modelHint").find('button').prop("disabled",true);
		     		$('#dr_totalby').val('6');
		    	} else{
		    		$("#modelHint").find('button').prop("disabled",false);
		    	}
		    }
		
             
		function PopulateOrderBy() { //v1.0
			var include_preowned = document.getElementById('chk_include_preowned').checked;
			if (include_preowned) {
				include_preowned = 1;
			} else {
				include_preowned = 0;
			}
			showHint('../sales/mis-check1.jsp?salesmb=yes&include_preowned=' + include_preowned, 'divOrderBy');

		}

		//This sholuldn't be removed because it is need branch and model
		function PopulateCRMDays() {

		}
		function PopulateVariants() {

		}
		function PopulateColor() {

		}
	</script>
	<!-- // -->
</body>
</HTML>
