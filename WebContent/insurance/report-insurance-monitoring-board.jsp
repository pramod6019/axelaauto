<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Report_Insurance_Monitoring_Board"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<%
	if (!mybean.header.equals("no")) {
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>
</HEAD>

<body onload="convertmultiselect();returnpreownedcheck();" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Insurance Monitoring Board</h1>
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
						<li><a href="../insurance/report-insurance-monitoring-board.jsp">Insurance Monitoring Board</a><b>:</b></li>
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
												<div class="form-element3">
													<label>Start Date<font color="red">*</font>:&nbsp; </label>
														<input name="txt_starttime" id="txt_starttime" value="<%=mybean.start_time%>"
															class="form-control datepicker"
															type="text" maxlength="10" />
												</div>
												
												<div class="form-element3">
														<label>End Date<font color="red">*</font>:&nbsp; </label>
															<input name="txt_endtime" id="txt_endtime" value="<%=mybean.end_time%>"
																class="form-control datepicker"
																type="text" maxlength="10" />
												</div>
												
												<div class="form-element3">
													<label>Total By: &nbsp;</label>
														<select name="dr_totalby" class="form-control" id="dr_totalby">
															<%=mybean.PopulateTotalBy()%>
														</select>
												</div>
													
												<div class="form-element3">
													<label>Order By:&nbsp; </label>
														<select name="dr_orderby" class="form-control" id="dr_orderby">
															<%=mybean.PopulateOrderBy(mybean.comp_id, mybean.dr_orderby)%>
														</select>
												</div>
													
											<div class="form-element3">
												 <label>Brands:</label>
												 <!-- <span style="float: right;">
												 Select All <input type="checkbox" onchange="selectall(this.checked);" id="check" />
												 </span> -->
												<div id="multiprincipal">
													<select name="dr_principal" size="10"
														class="form-control multiselect-dropdown" multiple="multiple" id="dr_principal"
														onChange="PopulateBranches();PopulateRegion();">
														<%=mybean.misCheck.PopulatePrincipal(mybean.brand_id, mybean.brand_ids, mybean.comp_id, request)%>
													</select>
	<!-- 																document.getElementById('check').checked = false; -->
												</div>
											</div>

													<div class="form-element3">
														<label>Regions:</label> 
														<div id="regionHint">
														<%=mybean.misCheck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
														</div>
													</div>

													<div class="form-element3">
														<label>Branches:</label> 
														<div id="branchHint"> 
														<%=mybean.misCheck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
														</div>
													</div>

													<div class="form-element3">
														<label>Manufacturer:</label>
														<div>
															<select name="dr_manufacturer" size="10" multiple="multiple"
															class="form-control multiselect-dropdown" id="dr_manufacturer" onchange="PopulateModels();">
															<%=mybean.misCheck.PopulateManufacturer(mybean.carmanuf_ids, mybean.carmanuf_id, mybean.comp_id, request)%>
															</select>
														</div>
													</div>

													<div class="form-element3">
														<label>Model:</label>
														<div id="modelHint">
															<%=mybean.misCheck.PopulatePreownedModel(mybean.carmanuf_id, mybean.model_ids, mybean.comp_id, request)%>
														</div>
													</div>
													
													<div class="form-element3">
														<label>Insurance Executive:</label> 
														<div id="exeHint"> 
														<%=mybean.misCheck.PopulateInsurEmp(mybean.insur_emp_ids, mybean.branch_id, mybean.comp_id, request)%>
														</div>
													</div>
													
													<div class="form-element3">
														<label>Field Executive:</label>
														<div id="fieldHint">
															<%=mybean.misCheck.PopulateFieldExecutive(mybean.comp_id, mybean.branch_id, mybean.field_emp_ids, request)%>
														</div>
													</div>
													
													<div class="form-element12">
													<center>
														<input type="submit" name="submit_button" id="submit_button" class="btn btn-success"
															style="margin-top: 1px;" value="Go"/>
														<input type="hidden" name="submit_button" value="Submit" />
													</center>
													</div>

										</form>
									</div>
								</div>
							</div>
												<%if(!mybean.StrHTML.equals("")){ %>
													<center><%=mybean.StrHTML%></center>	
												<%} %>	
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	<%
		}
	%>
	<%@include file="../Library/admin-footer.jsp"%>

<%@include file="../Library/js.jsp"%>
<script language="JavaScript" type="text/javascript">


	function PopulateRegion() { //v1.0
		var brand_id = outputSelected(document .getElementById("dr_principal").options);
		showHint('../insurance/mis-check.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
	}

	function PopulateBranches() { //v1.0
		
		var brand_id = outputSelected(document .getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		showHint('../insurance/mis-check.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		
	}

	function PopulateModels() {
		var manufacturer_id = outputSelected(document.getElementById("dr_manufacturer").options);
//			alert(manufacturer_id);
		showHint('../insurance/mis-check.jsp?carmanuf_id=' + manufacturer_id + '&preownedmodel=yes', 'modelHint');
	}

	function PopulateFieldExecutives() {
		var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
		showHint( '../insurance/mis-check.jsp?branch_id=' + branch_id + '&fieldexe=yes', 'fieldHint');
	}

	function PopulateInsurEmp(){ //v1.0
		var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
		showHint('../insurance/mis-check.jsp?branch_id=' + branch_id + '&insurexecutives=yes', 'exeHint');
	}
	
	
</script>
<!-- // -->
</body>
</HTML>
