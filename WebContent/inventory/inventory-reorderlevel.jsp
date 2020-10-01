<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Inventory_Reorderlevel"
	scope="request" />
<%mybean.doGet(request,response); %>
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
						<h1>Update Reorder Level</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a>&nbsp;&gt;&nbsp;</li>
						<li><a href="inventory-reorderlevel.jsp?status=add">Reorder
								Level</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Update Reorder
										Level</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<center>
											<font>Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<form name="form1" class="form-horizontal"
											action="inventory-reorderlevel.jsp?status=add" method="post">
											<% if (mybean.status.equals("add")) { %>
											<div class="form-element6 form-element-center">
													<label>Branch<font color="#ff0000">*</font>: </label>
														<select name="dr_branch" id="dr_branch"
															class="form-control"
															onChange="showHint('inventory-location-check.jsp?branch_id=' + GetReplace(this.value),'location')">
															<%=mybean.PopulateBranch(mybean.branch_id, "", "", "", request)%>
														</select>
											</div>
											<div class="form-element6 form-element-center">
													<label>Location<font color="#ff0000">*</font>:&nbsp; </label>
													<div id="location">
														<select name="dr_location" class="form-control"
															id="dr_location" onChange="document.form1.submit();"
															visible="true">
															<option value=0>Select</option>
															<%=mybean.PopulateLocation() %>
														</select>
														</div>
											</div>

											<% if (!mybean.i.equals("NO")) {%>
											<center>
												<input name="update_button" type="submit"
													class="btn btn-success" id="update_button" value="Update" />
											</center>
											<%}%>

											<%} else {%>
											<a href="../inventory/inventory-reorderlevel.jsp?status=add">Reorder
												Level</a>
											<%}%>

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
</body>
</HTML>
