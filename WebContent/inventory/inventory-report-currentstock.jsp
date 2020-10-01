<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.inventory.Inventory_Report_CurrentStock" scope="request" />
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
						<h1>Current Stock</h1>
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
						<li><a href="../inventory/inventory-report-currentstock.jsp">Current
								Stock</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<b>Current Stock AS on <%=mybean.date%></b>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form name="formemp" class="form-horizontal" method="post">
												<div class="form-element3">
														<label> Branch<font color=red>*</font>: </label>
															<select name="dr_branch" class="form-control"
																id="dr_branch" onchange="PopulateLocation();">
																<%=mybean.PopulateBranch(mybean.branch_id, "", "","", request)%>
															</select>
												</div>

												<div class="form-element3">
														<label> Location<font color=red>*</font>: </label>
														<div id="dr_location">
																<%=mybean.PopulateLocation()%>
														</div>
												</div>
												<div class="form-element3">
														<label> Category: </label>
															<select id="dr_cat_id" name="dr_cat_id"
																class="form-control" onChange="StockSearch();">
																<option value='0'>Select</option>
																<%=mybean.PopulateCategoryPop(mybean.cat_id, mybean.comp_id, "0", "1", request)%>
															</select>
												</div>

												<div class="form-element3">
														<label>Item Name<font
															color=red>*</font>: </label>
															<center>
															
												<input name="txt_search" type="text" class="form-control"
																id="txt_search" value="" size="30" maxlength="255"
																onKeyUp="StockSearch();" />
																
																															
<!-- 																<input name="submit_button" type="submit" -->
<!-- 																	class="btn btn-success" id="submit_button" value="Go" /> -->
																<input type="hidden" name="submit_button" value="Submit" />
															</center>
													</div>
										</form>
									</div>
								</div>
							</div>
							<div id="list" name="list"></div>
<%-- 							<%=mybean.StrHTML%> --%>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
<script>

function StockSearch() {
	var search = document.getElementById("txt_search").value;
	var cat_id = document.getElementById("dr_cat_id").value;
	var location_id = document.getElementById("dr_location_id").value;
	var branch_id = document.getElementById("dr_branch").value;

	var i = setTimeout('callAjax("' + cat_id + '", "' + location_id + '", "' + branch_id + '", "' + search +  '")', 1000);

}

function callAjax(cat_id, location_id, branch_id, search ) {
	showHintFootable("../inventory/inventory-stock-check.jsp?go=Go&search=" + search 
			+ "&dr_cat_id=" + cat_id + "&dr_location_id=" + location_id 
			+ "&dr_branch=" + branch_id , "list");
}

</script>
<script language="JavaScript" type="text/javascript">
	function PopulateLocation() { //v1.0
		var branch_id = parseInt(document.getElementById("dr_branch").value);
		showHint('../inventory/inventory-check.jsp?location=yes'
				+ '&stock_branch_id=' + branch_id,'dr_location');
	}
</script>

</body>
</html>
