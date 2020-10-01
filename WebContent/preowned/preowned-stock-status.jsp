<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Stock_Status"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
	</head>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Pre-Owned Stock Status</h1>
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
						<li><a href="index.jsp">Pre-Owned</a> &gt;</li>
						<li><a href="preowned-stock-status.jsp">Pre-Owned Stock
								Status</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<!-- 	PORTLET -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Pre-Owned Stock
										Status</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
										
										
										<div class="form-element3">
														<lable>Status:</lable>
															 <select
																name="dr_preownedstatus_id" class="form-control "
																id="dr_preownedstatus_id">
																<%=mybean.PopulateStatus(mybean.comp_id)%>
															</select>
														
													</div>

													<div class="form-element3">
														<lable>Pending Delivery:</lable>
															 <select name="dr_pending_delivery_id"
																class="form-control" id="dr_pending_delivery_id">
																<%=mybean.PopulatePendingdelivery()%>
															</select>
														
													</div>
													<div class="form-element3">
															<lable>Blocked:</lable>  <select name="dr_preownedstock_blocked"
																class="form-control" id="dr_preownedstock_blocked">
																<%=mybean.PopulateBlocked()%>
															</select>
													</div>

													<div class="form-element3">
														<lable>Ownership:</lable>
															<select name="dr_preownedownership_id"
																class="form-control" id="dr_preownedownership_id">
																<%=mybean.PopulateOwnership(mybean.comp_id)%>
															</select>
														
													</div>
										
													<div class="form-element3">
															<lable>Brands<font color=red>*</font>:</lable>
															<div id="multiprincipal">
																<select name="dr_principal" size="10"
																	multiple="multiple" class="form-control multiselect-dropdown"
																	 id="dr_principal"
																	onChange="PopulateBranches();PopulateRegion();">
																	<%=mybean.PopulatePrincipal(mybean.brand_ids,mybean.comp_id, request)%>
																</select>
															</div>
														</div>
													

													<div class="form-element3">
														<lable>Regions:</lable>
															 <div id="regionHint">
															 <%=mybean.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
															</div>
														
													</div>

													<div class="form-element3">
														<lable>Branches:</lable>
															 <div id="branchHint">
															  <%=mybean.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id,
					request)%>
															</div>
														
													</div>


													<div class="form-element3">
														<lable>Location:</lable>
														 <div >
															 <select name="dr_location_id"
																id="dr_location_id" size="10" multiple="multiple"
																class="form-control multiselect-dropdown" style="padding: 10px">
																<%=mybean.PopulateLocation(mybean.preownedlocation_ids, mybean.comp_id)%>
															</select>
															</div>
													</div>

											
													
													
													<div class="form-element12">
														<center>
															<input name="submit_button1" type="submit"
																class="button btn btn-success" id="submit_button" value="Go" />
															<input type="hidden" name="submit_button" value="Submit"/>
														</center>
													</div>
<!-- 													<div class="col-md-6 col-sm-6"> -->
<!-- 														<div style="margin: 10px;"> -->
<!-- 															&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <br> -->
<!-- 															<a href="../sales/report-stock-exe.jsp">Pre-Owned Consultant Stock -->
<!-- 																Status</a> -->
<!-- 														</div> -->
<!-- 													</div> -->
												
									
									</form>
									</div>
								</div>
							</div>
							<%
								if (!mybean.StrHTML.equals("")) {
							%>
							<div><%=mybean.StrHTML%></div>
							<%
								}
							%>
						</div>
					</div>
				</div>
			</div>
		</div>
<!-- 	</div> -->

<!-- 	</div> -->

	<%@ include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
    <script language="JavaScript" type="text/javascript">
		function frmSubmit() {
			document.form1.submit();
		}
		function PopulateRegion() {
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint('../preowned/mis-check.jsp?multiple=yes&brand_id=' + brand_id + '&region=regionpss', 'regionHint');
		}
		
		function PopulateAdviser(){
		}
		function PopulateTech(){
		}
		function Populatepsfdays(){
		}
		function PopulateCRMDays(){
		}
		function PopulateExecutives() {
		}
	
		function PopulateBranches() { 
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../preowned/mis-check.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=branchpss', 'branchHint');
		}
	</script>
</body>
</html>
