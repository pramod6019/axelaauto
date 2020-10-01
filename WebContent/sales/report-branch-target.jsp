<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_Branch_Target" scope="request"/>
<%mybean.doPost(request,response); %>
<%if (!mybean.header.equals("no")) {%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">  
<HEAD>  
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>

<%@include file="../Library/css.jsp"%>

</HEAD> 

<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Branch Target</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="../sales/report-branch-target.jsp">Branch Target</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">

							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Branch Target</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<form method="post" name="frm1" id="frm1" class="form-horizontal">

											<div class="form-element4" >
												<label>Year:</label>
												<select name="dr_year" id="dr_year" class="form-control">
													<%=mybean.PopulateYears()%>
												</select>
											</div>
											
											<div class="form-element4" >
												<label>Month:</label>
												<select name="dr_month" id="dr_month" class="form-control">
													<%=mybean.PopulateMonths()%>
												</select>
											</div>
											
											<div class="form-element4" >
												<label>Total By:</label>
												<select name="dr_totalby" class="form-control" id="dr_totalby">
													<%=mybean.PopulateTotalBy(mybean.comp_id)%>
												</select>
											</div>
											
											<div class="form-element4">
												<label>Brands:</label>
												<div>
													<select name="dr_principal" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_principal"
														onChange="PopulateBranches();PopulateRegion();"><%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>

											<div class="form-element4">
												<label>Regions:</label>
												<div id="regionHint">
													<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element4">
												<label>Branches:</label>
												<div id="branchHint"> 
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>
											
											<div class="form-element12" align="center">
												<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go" />
												<input type="hidden" name="submit_button" value="Submit">
											</div>
											
										</form>
									</div>
								</div>
							</div>
							<%
								if (!mybean.StrHTML.equals("")) {
							%>
							<div>
								<!-- START PORTLET BODY -->
								<center><%=mybean.StrHTML%></center>
							</div>
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
	<%
		}
	%>

	<%@include file="../Library/admin-footer.jsp" %>
 	<%@include file="../Library/js.jsp"%>
 	
 	<script  type="text/javascript">
		
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}
		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		}
	</script>
 	
</body>
</HTML>
