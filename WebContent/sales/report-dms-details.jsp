<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Report_DMS_Details" scope="request" />
<jsp:useBean id="export" class="axela.sales.Report_DMS_Details" scope="request" />
<%
	mybean.doPost(request, response);
%>
<%
	export.doPost(request, response);
%>
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
						<h1>DMS Details</h1>
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
						<li><a href="report-dms-details.jsp">DMS Details</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="red"><b><%=export.msg%></b></font>
							</center>
							<!-- 	PORTLET -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">DMS Details</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form" id="form" method="post" class="form-horizontal">
											<div class="form-element6">
											<div class="form-element3">
												<label > Brands<font color="#ff0000">*</font>: </label>
												<div >
													<select name="dr_principal" class="form-control " id="dr_principal"
														onChange="PopulateBranches();PopulateRegion();">
														<%=mybean.PopulatePrincipal(mybean.brand_id, mybean.comp_id, request)%>
													</select>
												</div>
											</div>
											
											<div class="form-element3">
													<label >Region:</label>
													<div id="regionHint">
														<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
													</div>
												</div>
											<div class="form-element3">
													<label >Branches<font color="#ff0000">*</font>:</label>
													<div id="branchHint">
														<%=mybean.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_id, mybean.comp_id, request)%>
													</div>
												</div>
											
											<div class="form-element3">
												<label > Filter By: </label>
												<div >
													<select name="dr_filterby" class="form-control " id="dr_filterby"/>
														<%=mybean.PopulateFilterBy(mybean.filterby_id, mybean.comp_id)%>
													</select>
												</div>
											</div>
											
											</div>

											<div class="form-element3">
												<label >Start Date<font color=red>*</font>: </label>
												<div >
													<input name="txt_dms_start_date" type="text" class="form-control datetimepicker"
														id="txt_dms_start_date" value="<%=export.dms_start_date%>" size="12" maxlength="14" />
												</div>
											</div>
											
											<div class="form-element3">
												<label >End Date<font color=red>*</font>: </label>
												<div >
													<input name="txt_dms_end_date" type="text" class="form-control datetimepicker"
														id="txt_dms_end_date" value="<%=export.dms_end_date%>" size="12" maxlength="14" />
												</div>
											</div>

												
											<div class="form-element12">
												<center>
													<input name="btn_export" id="btn_export" type="submit" class="btn btn-success" value="Export" />
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
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	
	<%@include file="../Library/js.jsp"%>

	<script type="text/javascript">

		//This sholuldn't be removed because it is need branch and model
		function PopulateExecutives() { 
		}
		function PopulateTeams() { 
		}
		function PopulateCRMDays() { 
		}
		//This sholuldn't be removed because it is need branch and model

		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint( '../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}
		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint( '../sales/mis-check1.jsp?brand_id=' + brand_id + '&region_id=' + region_id + '&branch=dmsdetails', 'branchHint');
		}

	</script>

</body>
</HTML>

