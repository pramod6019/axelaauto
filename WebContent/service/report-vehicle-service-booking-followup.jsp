<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.service.Report_Vehicle_Service_Booking_Followup"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<style>
#veh_service_booking_modal {
	z-index: 1;
	top: 160px;
	/* position: fixed; */
	right: 10%;
}

@media screen and (max-width: 992px) {
	#universal-search {
		display: none;
	}
	.page-header .page-header-menu .hor-menu .navbar-nav>li.mega-menu-dropdown>.dropdown-menu .mega-menu-content .mega-menu-submenu
		{
		padding: 5px 5px;
	}
}

@media screen and (min-width: 480px) and (max-width: 992px) {
	.inline-menu {
		display: inline-block;
	}
	.extra-large {
		/* 		height: 70px; */
		
	}
	#move-top {
		top: -45px;
		/*  		margin-right: 120px;  */
	}
	.admin-icon {
		text-align: right
	}
}

@media screen and (max-width: 480px) {
	#move-top {
		height: 5px;
		/* 		top: -35px;  */
	}
	.page-head .page-title {
		margin-top: 0px
	}
	#c-logo {
		display: none;
	}
	.mobile-align {
		top: -50px;
		right: 50px;
	}
	.admin-icon {
		text-align: center
	}
}

@media screen and (min-width: 992px) {
	.admin-icon {
		text-align: center
	}
	#veh_service_booking_modal {
		display: block;
	}
	.page-header .page-header-menu .hor-menu .navbar-nav>li>a {
		padding: 16px 8px 12px;
	}
}

.dropdown-content {
	top: 35px;
}

.dropdown {
	position: relative;
	display: inline-block;
}

.dropdown-content {
	display: none;
	position: absolute;
	background-color: #f9f9f9;
	min-width: 160px;
	box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
}

.dropdown-content a {
	color: black;
	padding: 12px 16px;
	text-decoration: none;
	display: block;
}

.page-head .page-title {
	padding: 18px 0px 10px;
}

.dropdown-content a:hover {
	background-color: #f1f1f1
}

.dropdown:hover .dropdown-content {
	display: block;
}
</style>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<!-- 	MULTIPLE SELECT END-->
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
						<h1>Service Booking Follow-up</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
				<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-vehicle-service-booking-followup.jsp">Service
								Booking Follow-up</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<center>
								<font color="red"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Service Booking
										Follow-up</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
												<!-- FORM START -->
												
												<div class="form-element3">
												 <label>Brands:</label>
												<div id="multiprincipal">
													<select name="dr_principal"  class="form-control multiselect-dropdown" multiple="multiple" id="dr_principal"
														onChange="PopulateModels();PopulateBranches();PopulateZone();PopulateRegion();">
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
												<label>Work Shop:</label> 
												<div id="branchHint" > 
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id,request)%>
												</div>
											</div>
												
												<!-- FORM START -->
												<div class="form-element3">
													<label> CRM Executive: </label>
														<span id="exeHint"><%=mybean.PopulateCRMExecutives(mybean.empcrm_id, mybean.comp_id, mybean.ExeAccess)%></span>
												</div>

												

												<div class="form-element3">
													<label> Start Date<font color=red>*</font>: </label>
														<input name="txt_start_time" id="txt_start_time"
															value="<%=mybean.starttime%>"
															class="form-control datepicker" type="text"
															size="12" maxlength="10" />
												</div>
												

												<div class="form-element3">
													<label> End Date<font color=red>*</font>: </label>
														<input name="txt_end_time" id="txt_end_time"
															value="<%=mybean.endtime%>"
															class="form-control datepicker" type="text"
															size="12" maxlength="10" />
												</div>


												<div class="form-element3">
													<label> Days: </label>
														<select name="dr_vehcalltype_id" class="form-control"
															id="dr_vehcalltype_id">
															<%=mybean.PopulateCallTypeDays()%>
														</select>
												</div>
												
												<div class="form-element3">
													<label>Booking Type : </label>
														<select name="dr_vehfollowup_bookingtype_id"
															class="form-control" id="dr_vehfollowup_bookingtype_id"
															visible="true">
															<%=mybean.PopulateServiceBookingType(mybean.comp_id, mybean.vehfollowup_bookingtype_id)%>
														</select>
												</div>
												
												<div class="form-element3 form-element-margin">
													<label> Pending Follow-up: </label>
														<input id="chk_pending_followup"
															name="chk_pending_followup" type="checkbox"
															<%=mybean.PopulateCheck(mybean.pending_followup)%> />
												</div>
												
												<div class="form-element3 form-element-margin">
													<label> Active Booking: </label>
														<input id="chk_active_booking"
															name="chk_active_booking" type="checkbox"
															<%=mybean.PopulateCheck(mybean.active_booking)%> />
												</div>
												
												<div class="form-element12">
												<center>
												<input name="submit_button" type="submit" class="btn btn-success"
												 id="submit_button" value="Go" />
												  <input type="hidden" name="submit_button" value="Submit" /> 
												  <input name="btn_export" id="btn_export"
													type="submit"
													class="btn btn-success" value="Export" />
													 <input name="btn_print" id="btn_print" 
													type="submit" class="btn btn-success" formtarget="_blank"
													value="Print" />
													</center>
													</div>
										</form>
									</div>
								</div>
							</div>
							<!-- 	PORTLET -->
							<center><%=mybean.StrHTML%></center>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->



<%@include file="../Library/js.jsp"%>
	
	<script language="JavaScript" type="text/javascript">
	function PopulateRegion() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region=yes', 'regionHint');
	}
	
	function PopulateBranches() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		var zone_id = outputSelected(document.getElementById("dr_zone").options);

		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region_id=' + region_id + '&zone_id=' + zone_id + '&branch=yes', 'branchHint');
	}

	function PopulateModels() { //v1.0
	}

	function PopulateAdviser() {
	}

	function PopulateTech() {
	}
	
	function PopulateZone() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region_id=' + region_id + '&zone=yes', 'zoneHint');
	}
	
	function PopulateBranch() {
	}
	
	function PopulateCRMDays(){
	}
	
	function Populatepsfdays(){
	}
	
</script>
	
	<script>
		function ExeCheck() {
			// 			 	alert("yudsd");
			var branch_id = document.getElementById("dr_branch_id").value;
			showHint(
					'../service/report-check.jsp?jcpsfexecutive=yes&branch_id='
							+ GetReplace(branch_id), 'exeHint');
		}
	</script>

	<script language="JavaScript" type="text/javascript">

			$('#Hintclicktocall').on('hidden.bs.modal', function() {
				
								var txt_start_time =<%=mybean.start_time%>;
								var txt_end_time =<%=mybean.end_time%>;
								var dr_emp_id =<%=mybean.empcrm_id%>;
								var active=<%=mybean.active_booking%>;
								var pendingfollowup=<%=mybean.pending_followup%>;
								var bookingtype=<%=mybean.vehfollowup_bookingtype_id%>;
								var days=<%=mybean.vehcalltype_id%>;
								var dr_branch_id =outputSelected(document.getElementById("dr_branch_id").options)
								dr_branch_id=dr_branch_id.replace(/,\s*$/,"");
								var brand_id = outputSelected(document.getElementById("dr_principal").options);
								brand_id=brand_id.replace(/,\s*$/,"");
								var region_id = outputSelected(document.getElementById("dr_region").options);
								region_id=region_id.replace(/,\s*$/,"");
								var zone_id = outputSelected(document.getElementById("dr_zone").options);
								zone_id=zone_id.replace(/,\s*$/,"");
								var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
								branch_id=branch_id.replace(/,\s*$/,"");
							
								var url = '../service/report-vehicle-service-booking-followup.jsp?refresh=yes&submit_button=Go&start_time='
										+ txt_start_time
										+ '&end_time=' + txt_end_time
										+ '&active_booking=' + active
										+ '&vehcalltype_id=' + days
										+ '&pending_followup=' + pendingfollowup
										+ '&vehfollowup_bookingtype_id=' + bookingtype
										+ '&dr1_emp_id=' + dr_emp_id
										+ '&dr_principal=' + brand_id
										+ '&dr_region=' + region_id
										+ '&dr_zone=' + zone_id
										+ '&dr1_branch_id=' + dr_branch_id;
								showHint(url, 'test');
								window.location = url;
/// 								setTimeout(function(){window.location.reload(1)}, 5000);
							
		});
	</script>
	<%@include file="../Library/admin-footer.jsp"%>
</body>
</html>

