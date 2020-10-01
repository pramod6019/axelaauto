<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Inventory_Report_GRN"
	scope="request" />
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
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1>Report GRN</h1>
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
						<li><a href="../inventory/inventory-report-grn.jsp">GRN</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="red"><b><%=mybean.msg%> </b></font> 
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<strong><font color="#ffffff">Search</font></strong>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form method="post" class="form-horizontal" name="frm1"
											id="frm1">
											<div class="form-element3">
														<label> Branch<font color=red>*</font>: </label> <select name="dr_branch" id="dr_branch"
													class="form-control"
													onChange="showHint('inventory-location-check.jsp?branch_id=' + GetReplace(this.value),'dr_location')">
													<%=mybean.PopulateBranch(mybean.branch_id, "", "","", request)%>
												</select>
											</div>
												<div class="form-element3">
														<label> Location<font color=red>*</font>: </label>
														<div id="dr_location">
																<%=mybean.PopulateLocation() %>
															</div>
												</div>
												<div class="form-element3">
														<label> Start Date<font color=red>*</font>: </label>
															<input name="txt_starttime" id="txt_starttime"
																type="text" class="form-control datepicker"
																value="<%=mybean.start_time %>" size="12" maxlength="10" />
													</div>
												<div class="form-element3">
														<label> End Date<font color=red>*</font>: </label>
															<input name="txt_endtime" id="txt_endtime" type="text"
																class="form-control datepicker"
																value="<%=mybean.end_time %>" size="12" maxlength="10" />
												</div>
												<div class="row"></div>
											<div class="form-element3">
														<label> Item Code: </label>
															<input name="txt_item_code" id="txt_item_code"
																type="text" class="form-control"
																value="<%=mybean.item_code %>" size="12" maxlength="10" />
												</div>

												<div class="form-element3">
														<label> Item Name: </label>
															<input name="txt_item_name" id="txt_item_name"
																type="text" class="form-control"
																value="<%=mybean.item_name %>" size="12" maxlength="10" />
												</div>
												<div class="form-element3">
														<label> Supplier ID: </label>
															<input name="txt_supplier_id" id="txt_supplier_id"
																type="text" class="form-control"
																value="<%=mybean.supplier_id %>" size="12"
																maxlength="10" />
												</div>

												<div class="form-element12">
															<center>
																<input name="submit_button" type="submit"
																	class="btn btn-success" id="submit_button" value="Go" />
																<input type="hidden" name="submit_button" value="Submit"></input>
															</center>
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
<script>
  
</script>
	</body>
</HTML>
