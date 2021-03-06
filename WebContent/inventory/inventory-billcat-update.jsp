<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Inventory_BillCat_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
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
						<h1>Add Bill Category</h1>
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
						<li><a href="inventory-billcat-list.jsp?all=yes">List Bill Categories</a>&nbsp;&gt;</li>
						<li><a href="inventory-billcat-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Bill Category</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font><br></br>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<strong><font color="#ffffff">&nbsp; <%=mybean.status%>&nbsp;Bill Category
										</font></strong>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="post">
											<center>
												<font>Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.<br></br></font>
											</center>
											<div class="form-element6 form-element-center">
													<label>Brand<b><font color="#ff0000">*</font></b>: </label>
														<select name="dr_billcat_brand_id" id="dr_billcat_brand_id"
															class="dropdown form-control">
															<%=mybean.PopulateBrand(mybean.brand_id)%>
														</select>
												</div>
											<div class="form-element6 form-element-center">
													<label>Type<b><font color="#ff0000">*</font></b>: </label>
														<select name="dr_billcat_type_id" id="dr_billcat_type_id"
															class="dropdown form-control">
															<%=mybean.PopulateTypes()%>
														</select>
											</div>
											<div class="form-element6 form-element-center">
													<label>Name<font color="#ff0000">*</font>: </label>
														<input name="txt_billcat_name" type="text"
															class="form-control" id="txt_billcat_name"
															value="<%=mybean.billcat_name%>" size="50" maxlength="255">
											</div>
											<div class="form-element6 form-element-center">
													<label>Code</font>: </label>
														<input name="txt_billcat_code" type="text"
															class="form-control" id="txt_billcat_code"
															value="<%=mybean.billcat_code%>" size="50" maxlength="255">
											</div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6 form-element-center">
													<label>Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6 form-element-center">
													<label>Entry Date: <%=mybean.entry_date%></label>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6 form-element-center">
													<label>Modified By: <%=mybean.unescapehtml(mybean.modified_by)%></label>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-element6 form-element-center">
													<label>Modified Date: <%=mybean.modified_date%></label>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Add")) {
											%>
											<center>
												<input name="button" type="submit" class="btn btn-success" id="button" value="Add Category" />
												<input type="hidden" name="add_button" value="Add Category" />
											</center>
											<%
												} else if (mybean.status.equals("Update")) {
											%>
											<center>
												<input type="hidden" name="update_button"value="Update Category"> 
												<input name="button" type="submit" class="btn btn-success" id="button"value="Update Category" />
												 <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" onClick="return confirmdelete(this)"
													value="Delete Category" />
												<input type="hidden" name="Update" value="yes" />
											</center>
											<%
												}
											%>
											<input type="hidden" name="entry_by"value="<%=mybean.entry_by%>"/> 
											<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>"/>
											 <input type="hidden" name="modified_by" value="<%=mybean.modified_by%>"/>
											  <input type="hidden" name="modified_date"  value="<%=mybean.modified_date%>"/>
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
</body>
</HTML>
