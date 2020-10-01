<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Service_Target_List"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
 <%@include file="../Library/css.jsp" %>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>List Targets</h1>
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
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="service-target.jsp">Target</a> &gt;</li>
						<li><a href="service-target-list.jsp?<%=mybean.QueryString%>">List Targets</a><b>:</b></li>
					</ul>

					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="form1" method="get" class="form-horizontal">
								<div>
									<center>
										<font color="#ff0000"><b><%=mybean.msg%></b></font><br>
									</center>
								</div>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Search</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-element3">
												 <label>Brands:</label>
												<div id="multiprincipal">
													<select name="dr_principal"  class="form-control multiselect-dropdown" multiple="multiple" id="dr_principal"
														onChange="PopulateBranches();PopulateZone();PopulateRegion();">
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
											
											
											
<!-- 											<div class="form-element4"> -->
<!-- 												<label > Branch<font color=red>*</font>:&nbsp; </label> -->
<!-- 													<select name="dr_branch" class="form-control" -->
<!-- 														id="dr_branch" onChange="document.form1.submit();"> -->
<%-- 														<%=mybean.PopulateBranches(mybean.branch_id, mybean.comp_id)%> --%>
<!-- 													</select> -->
<!-- 												</div> -->
												<div class="form-element3">
												<label > Service Advisor<font color=red>*</font>:&nbsp; </label>
													<div id="executive">
														<%=mybean.mischeck.PopulateEmp(mybean.emp_id, mybean.branch_id, mybean.comp_id)%>
													</div>
												</div>
												<div class="form-element3">
												<label >Year<font color=red>*</font>:&nbsp; </label>
													<select name="dr_year" class="form-control" id="dr_year"
														onChange="document.form1.submit()">
														<%=mybean.PopulateYear()%>
													</select>
											</div>

										</div>
									</div>
								</div>
								<!-- START PORTLET BODY -->
								<center><%=mybean.StrHTML%></center>
							</form>
						</div>
					</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	 <%@include file="../Library/js.jsp" %>
	 
	 <script language="JavaScript" type="text/javascript">
	 function PopulateRegion() { //v1.0
//	 		alert("in region");
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			//alert("111111------"+brand_id);
			showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
					+ '&region=yes', 'regionHint');
		}
		
		function PopulateBranches() { //v1.0
//	 		alert("in branch");
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			var zone_id = outputSelected(document.getElementById("dr_zone").options);

			showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
					+ '&region_id=' + region_id + '&zone_id=' + zone_id + '&branch=yes', 'branchHint');
		}


		function PopulateAdviser() {
			var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
// 	 		alert("222------------"+branch_id);
			showHint('../service/mis-check1.jsp?serviceadvisor=yes&branch_id=' + branch_id, 'executive');
		}

		function PopulateZone() { //v1.0
// 	 		alert("in zone");
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			//alert("111111------"+brand_id);
			showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
					+ '&region_id=' + region_id + '&zone=yes', 'zoneHint');
		}
		
		function PopulateTech(){
			
		}
		function PopulateModels(){}
		
		function PopulateBranch() {
		}
	
	function PopulateCRMDays(){
		
	}
	function Populatepsfdays(){
		
	}

	</script>
</body>
</HTML>
