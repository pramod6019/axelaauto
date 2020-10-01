<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Report_Ticket_Status"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>

<!-- <style>
.table-bordered > thead > tr > th
{
border: 2.5px solid #e7ecf1;
}
.table-bordered > tbody > tr > td
{
border: 2.5px solid #e7ecf1;
}
</style> -->

</HEAD>

<body onload="ToggleDate();" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Ticket Status</h1>
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
						<li><a href="../service/report-ticket-status.jsp">Ticket Status</a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center><font color="red"><b><%=mybean.msg%></b></font></center>
					
						<div class="tab-pane" id="">

							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Ticket Status
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">
											
										<div class="row">
											<div class="form-element3 ">
												<label>Start Date<font color="red">*</font>:</label>
												<input name="txt_starttime" id="txt_starttime"
													value="<%=mybean.start_time%>"
													class="form-control datepicker" type="text" />
											</div>
											
											<div class="form-element3">
												<label>End Date<font color="red">*</font>:</label>
												<input name="txt_endtime" id="txt_endtime"
													value="<%=mybean.end_time%>"
													class="form-control datepicker" type="text" />
											</div>
											
											<div class="form-element3">
												<label>Total By: </label>
												<select name="dr_totalby" class="form-control" id="dr_totalby">
													<%=mybean.PopulateTotalBy(mybean.comp_id)%>
												</select>
											</div>

											<div class="form-element3">
												<label>Brands:</label>
												<div id="multiprincipal">
													<select name="dr_principal" id="dr_principal"
														class="form-control multiselect-dropdown" multiple="multiple"
														onChange="PopulateModels();PopulateBranches();PopulateZone();PopulateRegion();">
														<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
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
													<%=mybean.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id,request)%>
												</div>
											</div>
											
											<div class="form-element3">
												<label>Ticket Owner:</label> 
												<div id="ticketownerHint" > 
													<%=mybean.mischeck.PopulateTicketOwner(mybean.dr_ticket_owner_ids, mybean.branch_id, mybean.comp_id)%>
												</div>
											</div>
											
											<div class="form-element3">
												<label >Type:</label>
												<div>
													<select name="dr_ticket_tickettype_id" id="dr_ticket_tickettype_id" class="form-control multiselect-dropdown" multiple="multiple"
													onchange="PopulateDays();">
														<%=mybean.PopulateTicketType()%>
													</select>
												</div>
											</div>
											
											<div class="form-element3">
												<label >Days:</label>
												<div id="daysHint">
											<%=mybean.PopulateDays(mybean.comp_id, mybean.ticket_tickettype_id, mybean.brand_id)%>
												</div>
											</div>
											
											<div class="form-element3">
												<label>Source<font color="red">*</font>:</label>
												<div>
													<select name="dr_ticket_ticketsource_id" class="form-control multiselect-dropdown" multiple="multiple"
													onchange="PopulateDays();">
														<%=mybean.PopulateSourceType()%>
													</select>
												</div>
											</div>
											
											<div class="form-element3">
												<label>Priority:</label>
												<div>
													<select name="dr_ticket_priorityticket_id" class="form-control multiselect-dropdown" multiple="multiple" id="dr_ticket_priorityticket_id">
														<%=mybean.PopulateTicketPriority()%>
													</select>
												</div>
											</div>
											
											<div class="form-element3">
												<label>Department:</label>
												<div>
													<select name="dr_ticket_dept_id" class="form-control multiselect-dropdown" multiple="multiple" onchange="populateCatogery(this);" id="dr_ticket_dept_id">
														<%=mybean.PopulateDepartment()%>
													</select>
												</div>
											</div>
											
											<div class="form-element3">
												<label >Category:</label>
												<div id="categoryHint">
													<%=mybean.PopulateTicketCategory(mybean.ticket_ticket_dept_id, mybean.comp_id)%>
												</div>
											</div>
											
											<div class="form-element3 form-element-margin">
												<label>Include Pending Ticket:</label>
												<input type="checkbox" id="chk_include_pending" name="chk_include_pending"
													onchange="ToggleDate();" <%=mybean.PopulateCheck(mybean.include_pending)%> />
											</div>
											
											<div class="form-element3 form-element-margin">
												<label>Ticket Overdue:</label>
												<input type="checkbox" id="chk_overdue" name="chk_overdue" onchange="ToggleDate();"
												<%=mybean.PopulateCheck(mybean.ticket_overdue)%> />
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
	// 		alert("in region");
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			//alert("111111------"+brand_id);
			showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}
		
		function PopulateBranches() { //v1.0
	// 		/* alert("in branch");
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			var zone_id = outputSelected(document.getElementById("dr_zone").options);
	
			showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				 	+ '&region_id=' + region_id + '&zone_id=' + zone_id + '&allbranch=yes', 'branchHint');
		}
	
		function PopulateModels() {
		}
	
		function PopulateZone() { //v1.0
	// 		alert("in zone");
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			//alert("111111------"+brand_id);
			showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
					+ '&region_id=' + region_id + '&zone=yes', 'zoneHint');
		}
		
		function PopulateTicketOwner() {
			var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
			showHint('../service/mis-check1.jsp?ticketOwner=yes&exe_branch_id=' + branch_id, 'ticketownerHint');
		}
		
		function PopulateBranch() {
		}
		
		function ToggleDate(){
			var pending = document.getElementById('chk_include_pending').checked;
			var overdue = document.getElementById('chk_overdue').checked;
			if (pending==true && overdue==true) {
				$("#txt_starttime").prop("disabled", false);
				$("#txt_endtime").prop("disabled", false);
			} else if (pending==true) {
				$("#txt_starttime").prop("disabled", true);
				$("#txt_endtime").prop("disabled", true);
			} 
		}
		
		function populateCatogery() {
			var department_id = $('#dr_ticket_dept_id').val();
			if (department_id == null) {
				department_id = "";
			}
			showHint('../service/report-check.jsp?ticket_category=ticket_addmulti&ticket_dept_id=' + department_id, 'categoryHint');
		}
		
		function PopulateDays() {
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var tickettype_id = outputSelected(document.getElementById("dr_ticket_tickettype_id").options);
// 			var tickettype_id = $('#dr_ticket_tickettype_id').val();
console.log("brand_id=="+brand_id);
			showHint('../service/mis-check1.jsp?multiple=yes&ticketdays=yes&tickettype_id='+tickettype_id+'&ticket_brand_id='+brand_id, 'daysHint');
		    }
		
	</script>
</body>
</HTML>
