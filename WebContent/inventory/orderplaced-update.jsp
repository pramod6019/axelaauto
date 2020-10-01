<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Orderplaced_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
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
						<h1><%=mybean.status %> Orderplaced</h1>
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
							<li><a href="../inventory/index.jsp">Inventory</a>&gt;</li>
							<li><a href="../inventory/orderplaced-list.jsp?all=yes">List Orderplaced</a>&gt;</li>
							<li><a href="../inventory/orderplaced-update.jsp?update=yes&orderplaced_id=<%=mybean.orderplaced_id %>"><%=mybean.status %> Orderplaced </a></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status %> Orderplaced </div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form id="form1" name="form1" method="post" class="form-horizontal">

												<div class="form-element6">
													<label>Branch<font color="#ff0000">*</font>:</label>
													<select class="form-control" id="dr_orderplaced_branch_id" name="dr_orderplaced_branch_id" onchange="PopulateModels();">
														<%=mybean.PopulateBranches(mybean.dr_orderplaced_branch_id, mybean.comp_id)%>
													</select>
												</div>

												<div class="form-element6">
													<label>Model<font color="#ff0000">*</font>:</label>
													<div id="hintmodel" >
														<select id="dr_orderplaced_model_id" name="dr_orderplaced_model_id" class="form-control">
															<!-- <option value='0'>Select Model</option> -->
															<%=mybean.PopulateModel()%>
														</select>
													</div>
												</div>
												
												<div class="row"></div>

												<div class="form-element6">
													<label>FuelType<font color="#ff0000">*</font>:</label>
													<select id="dr_orderplaced_fueltype_id"
														name="dr_orderplaced_fueltype_id" class="form-control">
														<%=mybean.PopulateFuelType()%>
													</select>
												</div>

												<div class="form-element6">
													<label> Date<font color="#ff0000">*</font>:</label>
													<input name="txt_orderplaced_date" id="txt_orderplaced_date" type="text"
														value="<%=mybean.orderplaced_date%>" class="form-control datepicker" />
												</div>
												
												<div class="row"></div>
												<div class="form-element6">
													<label>Count<font color="#ff0000">*</font>:</label>
													<input type="text" name="txt_orderplaced_count" id="txt_orderplaced_count"
														class="form-control" value="<%=mybean.orderplaced_count%>"
														size="10" maxlength="10" onkeyup="toInteger('txt_orderplaced_count','Qty')" />
												</div>
												
												<div class="row"></div>

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
												<div class="form-element3 form-element-margin">
													<label > Entry By: </label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
															value="<%=mybean.unescapehtml(mybean.entry_by)%>">
												</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>
												<div class="form-element3 form-element-margin">
													<label > Entry Date: </label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
												</div>	
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
											%>
												<div class="form-element3 form-element-margin">
													<label > Modified By: </label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
															value="<%=mybean.unescapehtml(mybean.modified_by)%>">
												</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
											%>
												<div class="form-element3 form-element-margin">
													<label > Modified Date: </label>
														<%=mybean.modified_date%>
													<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>">
												</div>
											<%
												}
											%>
											<div class="form-element12">
												<center>
													<%
														if (mybean.status.equals("Add")) {
													%>
														<center>
															<input name="add_button" type="submit" class="button btn btn-success" id="add_button" value="Add Orderplaced" />
														</center>
													<%
														}
													%>
												
													<%
														if (mybean.status.equals("Update")) {
													%>
													<input name="update_button" id="update_button" type="hidden" value="yes" />
													<input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton"
														onClick="return SubmitFormOnce(document.form1, this);" value="Update Orderplaced" />
													<input name="delete_button" type="submit" class="btn btn-success" id="delete_button"
														OnClick="return confirmdelete(this);" value="Delete Orderplaced" />
													<%
														}
													%>
	
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
<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
<script src="../Library/smart.js" type="text/javascript"></script>
	<script type="text/javascript">
		function PopulateModels() { //v1.0
			var branch_id = outputSelected(document.getElementById("dr_orderplaced_branch_id").options);
			showHint('../inventory/inventory-check.jsp?dr_branch_id=' + branch_id + '&model=yes', 'hintmodel');
		}
	</script>
</body>
</HTML>
