<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.inventory.Inventory_Report_ReorderLevel" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="shortcut icon" href="../test/favicon.ico" />
<%@include file="../Library/css.jsp"%>
</HEAD>

<body bgColor="#ffffff" leftmargin="0" rightmargin="0" topmargin="0"
	bottommargin="0">
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
						<h1>Report Reorder Level</h1>
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
						<li><a href="index.jsp">Inventory</a> &gt;</li>
						<li><a href="../inventory/inventory-report-reorderlevel.jsp">Reorder
								Level</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Reorder Level</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<center>
											<b>Reorder Level AS on <%=mybean.date%></b>
										</center>
										
										<form name="formemp" class="form-horizontal" method="post">
												<div class="form-element4">
														<label> Branch<font color=red>*</font>: </label>
															<select name="dr_branch" class="form-control"
																id="dr_branch" onchange="PopulateLocation();">
																<%=mybean.PopulateBranch(mybean.branch_id, "", "","", request)%>
															</select>
													</div>

												<div class="form-element3">
														<label> Location<font color=red>*</font>:</label>
														<div id="dr_vehstock_location_id">
																<%=mybean.PopulateLocation(mybean.comp_id, mybean.branch_id, mybean.location_id) %>
															</div>
												</div>
												
												<div class="form-element3">
														<label> Category: </label>
															<select id="dr_cat_id" name="dr_cat_id"
																class="form-control">
																<option value='0'>Select</option>
																<%=mybean.PopulateCategoryPop(mybean.cat_id, mybean.comp_id, "0", "1", request)%>
															</select>
												</div>

												<div class="form-element2 form-element">
														<label> </label>
															<center>
																<input name="submit_button" type="submit"
																	class="btn btn-success" id="submit_button" value="Go" />
																<input type="hidden" name="submit_button" value="Submit" />
															</center>
												</div>
												</form>
											
											</div>
										
									</div>
								</div>
							</div>
							<%=mybean.StrHTML%>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
   
					<script language="JavaScript" type="text/javascript">
	function PopulateLocation() { //v1.0
		var branch_id = parseInt(document.getElementById("dr_branch").value);
		showHint('../inventory/inventory-check.jsp?location=yes'
				+ '&stock_branch_id=' + branch_id, 'dr_vehstock_location_id');
	}
	
	function SubmitFormAndUpdatre(id) {   
		var auto = 0;
		auto = document.getElementById("chk_stock_reorder_auto_" + id).checked;
		if (auto == true) {
			auto = 1;
		} else {
			auto = 0;
		}
		var leaddays = document.getElementById("txt_stock_reorder_leaddays_"
				+ id).value;
		var reorderlevel = document.getElementById("txt_stock_reorderlevel_"
				+ id).value;
		var location_id = document.getElementById("dr_vehstock_location_id").value;
		
		setTimeout(CallAjax(id, auto, leaddays, reorderlevel, location_id), 200);
	}
	function CallAjax(id, auto, leaddays, reorderlevel, location_id){
		showHint('../inventory/inventory-reorderlevel-check.jsp?update=yes&item_id=' + id
				+ '&auto=' + auto + '&leaddays=' + leaddays + '&reorderlevel='
				+ reorderlevel + '&dr_vehstock_location_id=' + dr_vehstock_location_id, 'txt_result');
	}
</script>
	</body>
</html>
