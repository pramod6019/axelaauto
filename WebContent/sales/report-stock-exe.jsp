<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Report_Stock_Exe" scope="request" />
<% mybean.doGet(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>

<%@include file="../Library/css.jsp"%>

</head>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>

	<!--         START -->
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
						<h1>Sales Consultant Stock Status</h1>
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
						<li><a href="../portal/mis.jsp">Sales</a> &gt;</li>
						<li><a href="report-stock-exe.jsp">Sales Consultant Stock Status</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="caption" style="float: none">
								<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
							</div>
							<form name="form1" method="post" class="form-horizontal">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											Sales Consultant Stock Status
										</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->

											<div class="form-element3">
												<label>Brand:&nbsp; </label>
												<div >
													<select name="drop_brand_id" class="form-control" id="drop_brand_id"
														onchange="PopulateBranch(this.value); PopulateModel(this.value,'');PopulateColour(this.value,'');PopulateLocation(this.value,'0');PopulateTeam(this.value,'0');">
														<%=mybean.PopulatePrincipal()%>
													</select>
												</div>
											</div>
												
											<div class="form-element3">
												<label >Branch:&nbsp; </label>
												<div >
													<span id="div_branch">
														<%=mybean.PopulateBranch(mybean.brand_id, mybean.comp_id)%>
													</span>
												</div>
											</div>
											
											<div class="form-element3">
												<label >Delivery Status:&nbsp; </label>
												<div >
													<select name="dr_delstatus_id" class="form-control" id="dr_delstatus_id">
														<%=mybean.PopulateStatus(mybean.comp_id)%>
													</select>
												</div>
											</div>

											<div class="form-element3">
												<label >Pending Delivery:&nbsp; </label>
												<div >
													<select name="dr_pending_delivery_id" class="form-control" id="dr_pending_delivery_id">
														<%=mybean.PopulatePendingdelivery()%>
													</select>
												</div>
											</div>

											<!-- 	 	================= 2start	 -->

											<div class="form-element3">
												<label >Team:&nbsp; </label>
												<div id="hint_team_id" >
												<%=mybean.PopulateTeam(mybean.brand_id, mybean.vehstock_branch_id, mybean.comp_id)%>
												</div>
											</div>
												
											<div class="form-element3">
												<label >Model:&nbsp; </label>
												<div >
													<span id="div_model">
														<%=mybean.PopulateModel(mybean.brand_id, mybean.vehstock_branch_id, mybean.comp_id)%>
													</span>
												</div>
											</div>
											
											<div class="form-element3">
												<label >Variant:&nbsp; </label>
												<div >
													<span id="div_item">
														<%=mybean.PopulateItem(mybean.model_id, mybean.comp_id)%>
													</span>
												</div>
											</div>

											<div class="form-element3">
												<label >Colour:&nbsp; </label>
												<div >
													<span id="div_colour">
														<%=mybean.PopulateColour(mybean.brand_id, mybean.vehstock_branch_id, mybean.comp_id)%>
													</span>
												</div>
											</div>

											<!-- =========== 2end -->

											<!-- =================== 3start -->

											<div class="form-element3">
												<label >Fuel:&nbsp; </label>
												<div >
													<select id="dr_fuel_id" name="dr_fuel_id" class="form-control">
														<%=mybean.PopulateFuelType()%>
													</select>
												</div>
											</div>
											
											<div class="form-element3">
												<label >Blocked:&nbsp; </label>
												<div >
													<select name="dr_blocked" class="form-control" id="dr_blocked">
														<%=mybean.PopulateBlocked()%>
													</select>
												</div>
											</div>
											
											<div class="form-element3">
												<label >Location:&nbsp; </label>
												<div id="hint_loc" >
													<%=mybean.PopulateLocation(mybean.brand_id, mybean.branch_id, mybean.comp_id)%>
												</div>
											</div>

											<div class="form-element3">
												<label >Order By:&nbsp; </label>
												<div >
													<select name="dr_order_by" class="form-control" id="dr_order_by">
														<%=mybean.PopulateOrderBy()%>
													</select>
												</div>
											</div>

											<div class="form-element3">
												<label >Stock Status:&nbsp; </label>
												<div >
													<select id="dr_vehstock_status_id" name="dr_vehstock_status_id" class="form-control">
														<%=mybean.PopulateStockStatus(mybean.comp_id)%>
													</select>
												</div>
											</div>

											<!-- ======================3end -->

											<div class="form-element12">
												<center>
													<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Go" />

													<input name="PrintButton" style="margin-left: 100px;" type="button" class="btn btn-success" id="PrintButton" value="Export"
															onClick="remote=window.open('<%=mybean.LinkExportPage%>','print','');remote.focus();">
													<input type="hidden" name="submit_button" value="Submit">
												</center>
											</div>

										</div>
										<%=mybean.StrHTML%>
									</div>
								</div>

							</form>

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
		function PopulateBranch(principle_id) {
			//alert(principle_id);
			showHint("stock-exe-check.jsp?principle_id=" + principle_id + "&branch=yes", "div_branch");
			// 			showHint("stock-exe-check.jsp?principle_id=" + principle_id + "&model=yes", "div_model");
			// 			showHint("stock-exe-check.jsp?principle_id=" + principle_id + "&colour=yes", "div_colour");
		}
		function PopulateModel(principle_id, branch_id) {
			showHint("stock-exe-check.jsp?principle_id=" + principle_id
					+ "&branch_id=" + branch_id + "&model=yes", "div_model");
		}

		function PopulateItem(model_id) {
			showHint("stock-exe-check.jsp?model_id=" + model_id + "&item=yes", "div_item");
		}

		function PopulateColour(brand_id, branch_id) {
			showHint("stock-exe-check.jsp?principle_id=" + brand_id
					+ "&branch_id=" + branch_id + "&colour=yes", "div_colour");
		}

		function PopulateTeam(brand_id, branch_id) {
			showHint( "stock-exe-check.jsp?principle_id=" + brand_id
					+ "&branch_id=" + branch_id + "&team=yes", "hint_team_id");
		}
		function PopulateLocation(brand_id, branch_id) {
			showHint( "stock-exe-check.jsp?principle_id=" + brand_id
					+ "&branch_id=" + branch_id + "&location=yes", "hint_loc");
		}
	</script>
</body>
</html>
