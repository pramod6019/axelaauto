<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.inventory.Inventory_Report_Critical" scope="request" />
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
						<h1>Critical Items</h1>
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
						<li><a href="../inventory/inventory-report-critical.jsp">Critical
								Items</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Critical Items</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form class="form-horizontal" name="formemp" method="post">
											<center>
												<b>Critical Items AS on <%=mybean.date%></b>
											</center>
											<div class="form-element4">
														<label> Branch<font color=red>*</font>: </label>
															<select name="dr_branch" class="form-control"
																id="dr_branch" onchange="PopulateLocation();">
																<%=mybean.PopulateBranch(mybean.branch_id, "", "","",  request)%>
															</select>
												</div>

												<div class="form-element3">
														<label> Location<font color=red>*</font>: </label>
														<div id="dr_location_id">
																<%=mybean.PopulateLocation() %>
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
												<div class="form-element2">
														<label> </label>
															<center>
																<input name="submit_button" type="submit"
																	class="btn btn-success" id="submit_button" value="Go" />
																<input type="hidden" name="submit_button" value="Submit" />
															</center>
													</div>
												</div>
											</div>
										</form>
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
<script language="JavaScript" type="text/javascript">
	function PopulateLocation() { //v1.0
var branch_id = parseInt(document.getElementById("dr_branch").value);
showHint('../inventory/inventory-check.jsp?location=yes' + '&vehstock_branch_id=' + branch_id, 'dr_location_id');
	}
</script>
	</body>
</html>
