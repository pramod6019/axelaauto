<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_Enquiry_Month" scope="request"/>
<%mybean.doPost(request,response); %>
 <%if (!mybean.header.equals("no")) {%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
  
<%@include file="../Library/css.jsp"%>
  
</HEAD>
  
 <body  class="page-container-bg-solid page-header-menu-fixed">
  
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
						<h1>Enquiry By Month</h1>
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
							<li><a href="../sales/report-enquiry-month.jsp">Enquiry
									By Month</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="red"><b> <%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Enquiry By Month
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">

											<div class="form-element6">
												<label >Year:</label>
												<div>
													<select name="dr_year" class="form-control" id="dr_year" >
														<%=mybean.PopulateYears()%>
													</select>
												</div>
											</div>
											
											<div class="form-element6">
												<label >Month:</label>
												<div>
													<span id="month">
														<select name="dr_month" id="dr_month" class="form-control">
															<%=mybean.PopulateMonths()%>
														</select> 
													</span>
												</div>
											</div>
											
											<div class="form-element3">
												<label>Brands:</label>
												<div>
													<select name="dr_principal" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_principal"
														onChange="PopulateBranches();PopulateModels();PopulateRegion();">
															<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>

											<div class="form-element3">
												<label>Regions: </label>
												<div id="regionHint">
													<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element3">
												<label>Branches:</label>
												<div id="branchHint">
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element3">
												<label>Model: </label>
												<div id="modelHint">
													<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
												</div>
											</div>
											<div class="form-element3">
												<label>Teams:</label>
												<div id="teamHint">
													<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element3">
												<label>Sales Consultant:</label>
												<div id="exeHint">
													<%=mybean.mischeck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%>
												</div>
											</div>
											<div class="form-element3">
												<label>SOE: </label>
												<div id="multisoe">
													<select name="dr_soe" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_soe">
														<%=mybean.PopulateSOE()%>
													</select>
												</div>
											</div>
											<div class="form-element3">
												<label>SOB:</label>
												<div id="multisob">
													<select name="dr_sob" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_sob">
														<%=mybean.PopulateSOB()%>
													</select>
												</div>
											</div>
											
											<div class="form-element12" align="center">
												<center>
													<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go" />
													<input type="hidden" name="submit_button" value="Submit" />
												</center>
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
	<%
		}
	%>
	<div><%=mybean.StrHTML%></div>
	
	<%@include file="../Library/admin-footer.jsp"%>
                        
	<%@include file="../Library/js.jsp"%>
	
  	<script type="text/javascript">
		$(document).ready(function(){
			$('#dr_month option:eq(' + (new Date).getMonth()+ ')').prop('selected', true);
			$('#dr_year option:eq(' + (new Date).getFullYear() + ')').prop('selected', true);
		});
			/* function PopulateMonth(){
			alert("a");
			GregorianCalender d = new GregorianCalender();
			//Date d = new Date();
			var curr_month = d.getMonth();
			document.dr_month.options[curr_month].selected=true;
		} */
	
	</script>
	
	<script language="JavaScript" type="text/javascript">

		//This sholuldn't be removed because it is need branch and model
		function PopulateCRMDays() { 
		}
		function PopulateVariants() { 
		}
		function PopulateColor() { 
		}
		//This sholuldn't be removed because it is need branch and model

		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint( '../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}
		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint( '../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		}

		function PopulateModels() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint('../sales/mis-check1.jsp?brand_id=' + brand_id + '&model=yes', 'modelHint');
		}
		function PopulateTeams() {
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?branch_id=' + branch_id + '&team=yes', 'teamHint');
		}

		function PopulateExecutives() { //v1.0
			var team_id = outputSelected(document.getElementById("dr_team").options);
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?team_id=' + team_id + '&exe_branch_id=' + branch_id + '&executives=yes', 'exeHint');
		}
		/* function ExeCheck() { //v1.0
		var branch_id=document.getElementById("dr_branch").value;
		var team_id=outputSelected(document.getElementById("dr_team").options);
		showHint('../sales/mis-check1.jsp?multiple=yes&team_id='+team_id+'&exe_branch_id=' + GetReplace(branch_id),'exeHint');
		} */
	</script>
 </body>
</HTML>

