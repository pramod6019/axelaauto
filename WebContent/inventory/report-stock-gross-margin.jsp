<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Report_Stock_Gross_Margin" scope="request" />
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
						<h1>Report Stock Gross Margin</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="../inventory/report-stock-gross-margin.jsp">Report Stock Gross Margin </a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- BODY START -->
							<center>
								<font color="red"><b><%=mybean.msg%></b></font><br />
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Report Stock Gross Margin</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" class="form-horizontal"
											id="frm1">
											<div class="form-element3">
														<label>Start Date<font color=#ff0000><b> *</b></font>: </label>
															<input name="txt_starttime" id="txt_starttime"
																class="form-control datepicker"
																value="<%=mybean.start_time%>"
																type="text" maxlength="10" />
												</div>
												
												<div class="form-element3">
														<label>End Date<font color=#ff0000><b> *</b></font>: </label>
															<input name="txt_endtime" id="txt_endtime"
																class="form-control datepicker"
																value="<%=mybean.end_time%>"
																type="text" maxlength="10" />
												</div>
													<div class="form-element3">
													<label>Total By: </label>
													<select name="dr_totalby" class="form-control" id="dr_totalby">
														<%=mybean.PopulateTotalBy()%>
													</select>
												</div>
												<div class="form-element3">
														<label>Item Name: </label>
															<center>
															
												<input name="txt_search" type="text" class="form-control"
																id="txt_search" value="" size="30" maxlength="255"
																onKeyUp="StockSearch();" />
																<input type="hidden" name="go" value="Go" />
															</center>
													</div>
<!-- 												<div class="form-element4"> -->
<!-- 														<label>Group By: </label> -->
<!-- 															<select name="dr_groupby" class="form-control" id="dr_groupby"> -->
<%-- 																	<%=mybean.PopulateGroupBy(mybean.comp_id)%> --%>
<!-- 																</select> -->
<!-- 												</div> -->
											
										<div class="row"></div>	
											<div class="form-element3" id="multiprincipal">
															<label>Brands:</label>
																<div><select name="dr_principal" size="10"
																	multiple="multiple" class="form-control multiselect-dropdown"
																	id="dr_principal" style="padding: 10px"
																	onChange="PopulateBranches();PopulateModels();PopulateRegion();">
																	<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
																</select></div>
														</div>
												<div class="form-element3">
															<label> Regions: </label><div id="regionHint"> <%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
															</div>
													</div>

													<div class="form-element3">
															<label>Branches:</label> <div id="branchHint"> <%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids,mybean.comp_id, request)%>
															</div>
													</div>
															<div class="form-element3">
															<label>Location:</label> <div id="locationHint"> <%=mybean.mischeck.PopulateInventoryLocation(mybean.branch_id, mybean.location_ids, mybean.comp_id,request)%>
															</div>
												</div>
												
												<div class="form-element3">
															<label>Bill Type:</label>
															 <div id="billtypeHint"> <%=mybean.mischeck.Populatebilltype(mybean.comp_id, request)%>
															</div>
												</div>
												
													<div class="form-element3">
															<label>Model:</label> <div id="modelHint"> <%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
															</div>
													</div>
													
													<div class="form-element3">
															<label>Category:</label> <div id="catHint"> <%=mybean.mischeck.PopulateCategory(mybean.brand_id,mybean.cat_ids, mybean.comp_id, "0", "1", request)%>
															</div>
													</div>
													
														<div class="form-element12">
														<center>
														<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go" onsubmit="StockSearch();"/>
														<input type="hidden" name="submit_button" value="Submit" />
													</center>
													</div>
													</form>
												</div>
											</div>
										</form>
									</div>
								</div>
							</div>
						</div>
					
						<%
							if (!mybean.SummaryHTML.equals("")||(!mybean.list.equals("yes")&&!mybean.list.equals("") )) {
						%>
						<div class="portlet box ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">&nbsp;</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="container-fluid" id="list">
									<div id="tableDate"><%=mybean.SummaryHTML%></div>
								</div>
							</div>
						</div>
						<%
							}
						%>
					
						<div id="list" name="list"></div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript">
	function showHint2(url,Hint,data) {
//		url = url+data;
	$('#'+Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
	
	$.ajax({
		url: url,
		type: 'POST',
		data: data,
		success: function (data){
				if(data.trim() != 'SignIn'){
				$('#'+Hint).fadeIn(500).html('' + data.trim() + '');
				} else{
				window.location.href = "../portal/";
				}
		}
	});
	
}

function loadTable(pageNumber){
	var formdata = $('#frm1').serialize();
	var url="report-gross-margin-check.jsp?PageNavi=yes&PageCurrent=" + pageNumber +"";
	var Hint ="tableDate";
	showHint2(url,Hint,formdata);
	
}
	function StockSearch() {
		var search = document.getElementById("txt_search").value;
		var starttime = document.getElementById("txt_starttime").value;
		var endtime = document.getElementById("txt_endtime").value;
		var i = setTimeout('callAjax("' + starttime + '", "' + endtime + '", "' + search +  '")', 1000);
	}

	function callAjax(starttime,endtime,search ) {
		showHint("../inventory/report-gross-margin-check.jsp?itemsearch=itemsearch&list=yes&search=" + search 
				+ "&txt_starttime=" + starttime + "&txt_endtime=" + endtime, "list");
	}
	function PopulateRegion() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		//alert("111111------"+brand_id);
		showHint('../inventory/mis-check.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
	}
	
	function PopulateBranches() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		//alert("222------"+region_id);
		showHint('../inventory/mis-check.jsp?multiple=yes&brand_id=' + brand_id +'&region_id=' + region_id + '&branch=yes', 'branchHint');
	}
	
	function Populatebilltype() { 
		
		showHint('../inventory/mis-check.jsp?multiple=yes&&billtype=yes', 'billtypeHint');
	}
	
	function PopulateInventoryLocation() { //v1.0
		
		var branch_id = outputSelected(document.getElementById("dr_branch").options);
// 		alert("111111------"+branch_id);
		showHint('../inventory/mis-check.jsp?multiple=yes&branch_id=' + branch_id + '&location=yes', 'locationHint');
	}
	function PopulateModels() { //v1.0

		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		//salert("333------"+brand_id);
		showHint('../inventory/mis-check.jsp?multiple=yes&brand_id=' + brand_id + '&model=yes', 'modelHint');
	}
function PopulateVariants(){
		
	}

</script>

</body>
</HTML>
