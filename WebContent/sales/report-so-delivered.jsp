<%@ page errorPage="../portal/error-page.jsp" %> 
<jsp:useBean id="mybean" class="axela.sales.Report_SO_Delivered" scope="request"/>
<%mybean.doPost(request,response); %>
<jsp:setProperty name="mybean" property="*" />  
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
						<h1><%=mybean.StrTitle%></h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<!-- END PAGE BREADCRUMBS -->
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
							<li><a href="../sales/report-so-delivered.jsp"><%=mybean.StrTitle%></a><b>:</b></li>

						</ul>

						<center>
							<font color="red"><b> <%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.StrTitle%>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">
										
											<div class="form-element4">
												<label>Start Date<font color=red>*</font>:&nbsp; </label>
												<input name="txt_starttime" id="txt_starttime" value="<%=mybean.start_time%>"
													class="form-control datepicker" type="text" maxlength="10" />
											</div>
											
											<div class="form-element4">
												<label>End Date<font color=red>*</font>:&nbsp; </label>
												<input name="txt_endtime" id="txt_endtime" value="<%=mybean.end_time%>"
													class="form-control datepicker" type="text" maxlength="10" />
											</div>
											
											<div class="form-element4">
												<label >By:&nbsp; </label>
												<div>
													<select name="dr_date" id="dr_date" class="form-control">
														<%=mybean.PopulateDate()%>
													</select>
												</div>
											</div>
											
											<div class="form-element4">
												<label >Status:&nbsp;</label>
												<div>
													<select name="dr_status_id" class="form-control" id="dr_status_id">
														<%=mybean.PopulateStatus()%>
													</select>
												</div>
											</div>
											<div class="form-element4">
												<label >Open:&nbsp;</label>
												<div>
													<select name="dr_so_open" class="form-control" id="dr_so_open">
														<%=mybean.PopulateOpen()%>
													</select>
												</div>
											</div>
											<div class="form-element4">
												<label>Order By:&nbsp; </label>
												<div>
													<select name="dr_order_by" class="form-control" id="dr_order_by">
														<%=mybean.PopulateOrderBy()%>
													</select>
												</div>
											</div>
										
											<div class="form-element3">
												<label>Brands:</label>
												<div >
													<select name="dr_principal" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_principal"
														onChange="PopulateBranches();PopulateModels();PopulateRegion();">
															<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>

											<div class="form-element3">
												<label>Regions:</label>
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
												<label> Model:</label>
												<div id="modelHint">
													<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
												</div>
											</div>
											
											<div class="form-element12" align="center">
												<center>
													<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Go" />
													<input type="hidden" name="submit_button" value="Submit" />
													
													<input name="PrintButton" type="button" style="margin-left: 100px" class="btn btn-success" id="PrintButton" value="Export"
														onClick="remote=window.open('../sales/veh-salesorder-export.jsp?smart=yes','print','');remote.focus();">
												</center>
											</div>

											<%
												if (!mybean.StrHTML.equals("")) {
											%>
											<!-- 	<div class="portlet box  "> -->
											<!-- 		<div class="portlet-title" style="text-align: left"></div> -->
											<!-- 		<div class="portlet-body portlet-empty"> -->
											<!-- 			<div class="tab-pane" id=""> -->
											<!-- START PORTLET BODY -->
											<div><%=mybean.StrHTML%></div>
											<!-- 			</div> -->
											<!-- 		</div> -->
											<!-- 	</div> -->
											<%
												}
											%>
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
	<%@include file="../Library/admin-footer.jsp"%>
	
	<%@include file="../Library/js.jsp"%>

	<script type="text/javascript">

		//This sholuldn't be removed because it is need branch and model
		function PopulateCRMDays() { 
		}
		function PopulateColor() { 
		}
		function PopulateVariants() { 
		}
		function PopulateExecutives() { 
		}
		function PopulateTeams() { 
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

		function PopulateModels() { //v2.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint('../sales/mis-check1.jsp?brand_id=' + brand_id + '&model=yes', 'modelHint');
		}

	</script>
</body>
</HTML>

    
    
