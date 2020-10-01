
<jsp:useBean id="mybean"
	class="axela.inventory.Report_Vehicle_Stock_Variant_Summary"
	scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1>Variant Summary Report</h1>
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
						<li><a
							href="../inventory/report-vehicle-stock-variant-summary.jsp">Variant
								Summary</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="red"><b><%=mybean.msg%></b></font><br />
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Variant Summary
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form method="post" class="form-horizontal" name="frm1"
											id="frm1">
											<div class="form-element2">
												<label> Brands:</label>
												<div id="multiprincipal">
													<select name="dr_principal" size="10" multiple="multiple"
														class="form-control multiselect-dropdown" id="dr_principal"
														style="padding: 10px"
														onChange="PopulateBranches();PopulateModels();">
														<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>
											<div class="form-element2">
												<label> Branches:</label> <div id="branchHint"> <%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>
											<div class="form-element2">
												<label> Teams:</label> <div id="teamHint"> <%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
												</div>
											</div>
											<div class="form-element2">
												<label> Executive: </label><div id="exeHint"><%=mybean.mischeck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%></span>
												</div>
											</div>
											
											<div class="form-element2">
												<label> Model:</label><div id="modelHint"> <%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
												</div>
											</div>
											<div class="form-element2">
												<label> Variants: </label><div id="itemHint"> <%=mybean.mischeck.PopulateVariants(mybean.model_id, mybean.item_ids, mybean.comp_id, request)%>
												</div>
											</div>
											<div class="row"></div>
											<center>
												<input type="submit" name="submit_button" id="submit_button"
													class="btn btn-success" value="Go" /> <input type="hidden"
													name="submit_button" value="Submit" />
											</center>
											<td valign="top">&nbsp;
									</div>
								</div>
							</div>
						</div>
						</form>
					</div>
				</div>
			</div>
			<%=mybean.StrHTML%>

		</div>
	</div>
	</div>
	</div>
	</div>

	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

	<script language="JavaScript" type="text/javascript">
	 function PopulateBranches() { //v1.0
	var brand_id=outputSelected(document.getElementById("dr_principal").options);
	showHint('../sales/mis-check1.jsp?multiple=yes&brand_id='+brand_id+'&branch=yes','branchHint');
   }

	function PopulateModels() { //v1.0
		var brand_id=outputSelected(document.getElementById("dr_principal").options);
		showHint('../sales/mis-check1.jsp?brand_id='+brand_id+'&model=yes','modelHint');
   }
	function PopulateVariants() { //v1.0
		var model_id =outputSelected(document.getElementById("dr_model").options);
	    showHint('../sales/mis-check1.jsp?model_id='+model_id+'&item=yes','itemHint');
   }
	function PopulateTeams(){		
		var branch_id=outputSelected(document.getElementById("dr_branch").options);	
	    showHint('../sales/mis-check1.jsp?branch_id='+branch_id+'&team=yes', 'teamHint');
	}
		
   function PopulateExecutives() { //v1.0
		var team_id=outputSelected(document.getElementById("dr_team").options);
		showHint('../sales/mis-check1.jsp?team_id='+team_id+'&executives=yes', 'exeHint');
   }
   </script>
</body>
</HTML>
