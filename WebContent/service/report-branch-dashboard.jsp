<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Report_Branch_Dashboard"
	scope="request" />
<% mybean.doPost(request, response); %>
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

<body  class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Branch Dashboard</h1>
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
						<li><a href="../service/report-branch-dashboard.jsp">Branch Dashboard</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center> <font color="red"><b> <%=mybean.msg%></b></font> </center>
					
						<div class="tab-pane" id="">

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Branch Dashboard </div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="form1" id="form1" class="form-horizontal">
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
														<%=mybean.PopulateTotalBy()%>
													</select>
												</div>
														
												<div class="form-element3">
													<label>Order By: </label>
													<select name="dr_orderby" class="form-control" id="dr_orderby">
																<%=mybean.PopulateOrderBy(mybean.comp_id, mybean.dr_orderby)%>
													</select>
												</div>
												<div class="form-element3">
												 <label>Brands:</label>
												<div id="multiprincipal">
													<select name="dr_principal"  class="form-control multiselect-dropdown" multiple="multiple" id="dr_principal"
														onChange="PopulateModels();PopulateBranches();PopulateZone();PopulateRegion();PopulateJcCat();PopulateJcType();">
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
												<label>Zone:</label> 
												<div id="zoneHint" >
													<%=mybean.mischeck.PopulateZone(mybean.brand_id, mybean.region_ids, mybean.zone_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element3">
												<label>Branches:</label> 
												<div id="branchHint" > 
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id,request)%>
												</div>
											</div>

											<div class="form-element3">
												<label>Model: </label>
												<div id="modelHint" > 
												<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.preownedmodel_ids, mybean.branch_id, mybean.comp_id, request)%>
												</div>
											</div>
											<div class="form-element3">
												<label>Service Advisor:</label>
												<div id="exeHint" >
												<%=mybean.mischeck.PopulateAdviser(mybean.advisor_ids, mybean.branch_id, mybean.comp_id, request)%>
												</div>
											</div>
													
											<div class="form-element3">
												<label>Service Technician:</label> 
												<div id="techHint" > 
												<%=mybean.mischeck.PopulateTechnician(mybean.tech_ids, mybean.branch_id, mybean.comp_id,request)%>
												</div>
											</div>
											
											<div class="form-element3">
												<label>Job Card Category:</label> 
												<div id="jccatHint"> 
												<%=mybean.mischeck.PopulateJobCardCategory(mybean.jccat_ids, mybean.brand_id, mybean.comp_id,request)%>
												</div>
											</div>
											
											<div class="form-element3">
												<label>Job Card Type:</label> 
												<div id="jctypeHint"> 
												<%=mybean.mischeck.PopulateJobCardType(mybean.jctype_ids, mybean.brand_id, mybean.comp_id,request)%>
												</div>
											</div>
											
											<div class="form-element3">
												<label>Data Fields:</label> 
												<div>
													<%=mybean.PopulateFields(mybean.dr_field_ids)%>
												</div>
											</div>
											
											<div class="form-element3 form-element-margin">
											<label> In-Active Executive: </label>
												<input id="chk_emp_active" type="checkbox" name="chk_emp_active" <%=mybean.PopulateCheck(mybean.emp_active)%> />
												</div>
												
												<div class="form-element12">
												<center>
													<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go"/>
													<input type="hidden" name="submit_button" value="Submit" />
												</center>
											</div>
										</form>
									</div>
								</div>
							</div>

							<%
								if (!mybean.StrHTML.equals("")) {
							%>
							<!-- START PORTLET BODY -->
							 <center><a id="btnExport" onclick="HtmlTableToExcel('table');" >Export</a></center>
							<center><%=mybean.StrHTML%></center>
							<%
								}
							%>
							<div id="errorMsg"></div>
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
	<%@include file="../Library/js.jsp"%>
	<%@include file="../Library/admin-footer.jsp"%>
	
<script language="JavaScript" type="text/javascript">

	if($(window).width() > 992)
		$('.total-field').prop("colspan","2").prepend("<td></td>");
	else
		$('.total-field').after("<td></td>");
	
	
	function PopulateRegion() { 
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
	}
	
	function PopulateBranches() { 
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		var zone_id = outputSelected(document.getElementById("dr_zone").options);

		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region_id=' + region_id + '&zone_id=' + zone_id + '&branch=yes', 'branchHint');
	}

	function PopulateModels() { 
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
// 		alert("branch_id=="+branch_id);
		showHint('../service/mis-check1.jsp?brand_id=' + brand_id +'&branch_id=' + branch_id + '&model=yes', 'modelHint');
	}

	function PopulateAdviser() {
		var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
		showHint('../service/mis-check1.jsp?advisor=yes&exe_branch_id=' + branch_id, 'exeHint');
	}

	function PopulateTech() {
		var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
		showHint('../service/mis-check1.jsp?technician=yes&exe_branch_id='
				+ branch_id, 'techHint');
	}
	
	function PopulateZone() {
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region_id=' + region_id + '&zone=yes', 'zoneHint');
	}
	
	function PopulateJcCat() { 
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&jccat=yes', 'jccatHint');
	}
	
	function PopulateJcType() { 
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&jctype=yes', 'jctypeHint');
	}
	
	function PopulateBranch() {
	}
	
	function PopulateCRMDays(){
	}
	
	function Populatepsfdays(){
	}
	
// 		$('#form1').submit(function(event){
// 			event.preventDefault();
// 			var formdata = $('#form1').serialize();
// 			var url='report-check.jsp';
// 			var Hint ='errorMsg';
// 			alert(formdata);
// // 			showHint(url+formdata,Hint);
// 		});
	
</script>
</body>
</HTML>
