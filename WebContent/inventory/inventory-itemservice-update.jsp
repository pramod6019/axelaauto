<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.inventory.Inventory_ItemService_Update" scope="request" />
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

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body onLoad="Displaypaymode();FormFocus();"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
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
						<h1><%=mybean.status%>&nbsp;Service
						</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="index.jsp">Inventory</a> &gt;&nbsp;</li>
						<li><a href="inventory-item-list.jsp?all=yes">List Items</a>
							&gt;&nbsp;</li>
						<li><a
							href="inventory-itemservice-list.jsp?item_id=<%=mybean.itemservice_item_id%>"><%=mybean.item_name%></a>
							&gt;&nbsp;</li>
						<li><a
							href="inventory-itemservice-list.jsp?item_id=<%=mybean.itemservice_item_id%>">List
								Service</a> &gt;&nbsp;</li>
						<li><a
							href="inventory-itemservice-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Service</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Service
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.<br>
											</font>
										</center>
										<form name="form1" method="post" class="form-horizontal">
											<div class="form-group">
												<label class="control-label col-md-4">Item : </label>
												<div class="txt-align">
													<a
														href="../inventory/inventory-item-list.jsp?item_id=<%=mybean.itemservice_item_id%>"><%=mybean.item_name%></a>
													<input type="hidden" name="txt_item_name"
														value="<%=mybean.item_name%>"> <input
														type="hidden" name="txt_item_id"
														value="<%=mybean.itemservice_item_id%>">
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Service Type<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<select id="itemservice_jctype_id"
														name="itemservice_jctype_id" class="form-control">
														<%=mybean.PopulateServiceType()%>
													</select>

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Kilometer<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<input name="txt_itemservice_kms" type="text"
														class="form-control"
														onKeyUp="toInteger('txt_itemservice_kms','Kilometer')"
														id="txt_itemservice_kms"
														value="<%=mybean.itemservice_kms%>" size="20"
														maxlength="20" />
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Days<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<input name="txt_itemservice_days" type="text"
														class="form-control"
														onKeyUp="toInteger('txt_itemservice_days','Days')"
														id="txt_itemservice_days"
														value="<%=mybean.itemservice_days%>" size="10"
														maxlength="10" />
												</div>
											</div>
											<%
												if (mybean.status.equals("Update") && !(mybean.itemservice_entry_by == null) && !(mybean.itemservice_entry_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4"> Entry By: </label>
												<div class="txt-align">
													<%=mybean.unescapehtml(mybean.itemservice_entry_by)%>
													<input type="hidden" name="itemservice_entry_by"
														value="<%=mybean.itemservice_entry_by%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4"> Entry Date: </label>
												<div class="txt-align">
												<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.itemservice_modified_by == null) && !(mybean.itemservice_modified_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4"> Modified By:
												</label>
												<div class="txt-align">
													<%=mybean.unescapehtml(mybean.itemservice_modified_by)%>
													<input type="hidden" name="itemservice_modified_by"
														value="<%=mybean.itemservice_modified_by%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4"> Modified
													Date: </label>
												<div class="txt-align">
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
												</div>
											</div>
											<%
												}
											%>
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												<input name="button2" type="submit" class="btn btn-success"
													id="button2" value="Add Service" /> <input type="hidden"
													name="add_button" value="Add Service"> <%
 	} else if (mybean.status.equals("Update")) {
 %>
													<input type="hidden" name="update_button"
													value="Update Service"> <input name="button"
														type="submit" class="btn btn-success" id="button"
														value="Update Service" /> <input name="delete_button"
														type="submit" class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)"
														value="Delete Service" /> <%}%>
											</center>
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
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
		document.form1.txt_item_name.focus()
		}
		</script>
</body>
</HTML>
