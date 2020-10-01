<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Report_Purchase" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
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
						<h1>Purchase Details</h1>
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
						<li> <a href=../inventory/index.jsp> Inventory</a> &gt; </li>
						<li><a href="../inventory/report-purchase.jsp">Purchase Details</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- BODY START -->
							<center>
								<font color="red"><b><%=mybean.msg%></b></font><br />
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Purchase Details</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" class="form-horizontal"
											id="frm1" onsubmit="return itemserach();">
											<div class="form-element6">
												<label>Start Date<font color=#ff0000><b> *</b></font>: </label>
													<input name="txt_starttime" id="txt_starttime" class="form-control datepicker" 
													value="<%=mybean.start_time%>" type="text" maxlength="10" />
												</div>
												
												<div class="form-element6">
														<label>End Date<font color=#ff0000><b> *</b></font>: </label>
															<input name="txt_endtime" id="txt_endtime" class="form-control datepicker"
																value="<%=mybean.end_time%>" type="text" maxlength="10" />
												</div>
												<div class="form-element3">
												 <label>Brands:</label>
												<div id="multiprincipal">
													<select name="dr_principal"  class="form-control multiselect-dropdown" multiple="multiple" id="dr_principal"
														onChange="PopulateBranches();PopulateRegion();">
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
												<label>Branches:</label> 
												<div id="branchHint" > 
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id,request)%>
												</div>
											</div>
												<div class="form-element3">
												<label>Location:</label>
												 <div id="locationHint"> 
												  <%=mybean.mischeck.PopulateInventoryLocation(mybean.branch_id, mybean.location_ids, mybean.comp_id,request)%>
												</div>
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
						<div class="portlet box ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">&nbsp;</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="container-fluid" id="list">
									<div ><%=mybean.StrHTML%></div>
								</div>
							</div>
						</div>
						<%
							}
						%>
					</div>
				</div>
			</div>
		</div>

	</div>

 <%@include file="../Library/admin-footer.jsp" %>
 <%@include file="../Library/js.jsp"%>

<script language="JavaScript" type="text/javascript">
	function PopulateRegion() { //v1.0
		var brand_id = outputSelected(document
				.getElementById("dr_principal").options);
// 		alert("111111------"+brand_id);
		showHint('../inventory/mis-check.jsp?multiple=yes&brand_id='
				+ brand_id + '&region=yes', 'regionHint');
	}

	function PopulateBranches() { //v1.0
		var brand_id = outputSelected(document
				.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		//alert("222------"+region_id);
		showHint('../inventory/mis-check.jsp?multiple=yes&brand_id='
				+ brand_id + '&region_id=' + region_id + '&branch=yes',
				'branchHint');
	}
	function PopulateInventoryLocation() { //v1.0

		var branch_id = outputSelected(document.getElementById("dr_branch").options);
		// 		alert("111111------"+branch_id);
		showHint('../inventory/mis-check.jsp?multiple=yes&branch_id='
				+ branch_id + '&location=yes', 'locationHint');
	}
</script>
</body>
</HTML>
