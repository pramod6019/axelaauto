<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.inventory.Inventory_Salescat_Update" scope="request" />
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
  			document.form1.txt_salescat_name.focus()
		} 
		        </script>
</HEAD>
<body onLoad="FormFocus()" leftmargin="0" rightmargin="0" topmargin="0"
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
						<h1>Inventory SalesCat Update</h1>
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
						<li><a href="inventory-salescat-list.jsp?all=yes">List
								Sales Categories</a> &nbsp;&gt;</li>
						<li><a
							href="inventory-salescat-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Sales
								Category</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<strong><font color="#ffffff">&nbsp; <%=mybean.status%>&nbsp;Sales
												Category
										</font></strong>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.<br>
											</font>
										</center>
										<form name="form1" class="form-horizontal" method="post">
											<div class="form-element6 form-element-center">
												<label>Name<font color="#ff0000">*</font>: </label>
													<input name="txt_salescat_name" type="text"
														class="form-control" id="txt_salescat_name"
														value="<%=mybean.salescat_name%>" size="50"
														maxlength="255">
											</div>
											<div class="form-element2"></div>
											<div class="form-element3">
												<label> Select Items:</label>
															<select name="inventory_item" size="20"
																style="width: 280px" multiple="multiple"
																class="form-control" id="inventory_item">
																<%=mybean.PopulateItem(mybean.comp_id) %>
															</select>
														</div>

												<div class="form-element2">
														<label>&nbsp; </label>
														</br></br></br></br>
															<input name="Input4" type="button"
																class="btn btn-success"
																onClick="JavaScript:AddItem('inventory_item','inventory_item_trans', '')"
																value="  Add >>"> <br> <input
																	name="Input4" type="button" class="btn btn-success"
																	onClick="JavaScript:DeleteItem('inventory_item_trans')"
																	value="<< Delete">
												</div>
												<div class="form-element3">
														<label>&nbsp; </label>
															<select name="inventory_item_trans" size="20"
																multiple="multiple" style="width: 280px"
																class="form-control" id="inventory_item_trans">
																<%=mybean.PopulateItemTrans(mybean.comp_id) %>
															</select>
												</div>
 
<script language="JavaScript">
function onPress()
{
	for (i=0; i<document.form1.inventory_item_trans.options.length; i++ ){
	document.form1.inventory_item_trans.options[i].selected =true;
	}
}
    </script>
											</div>
											<div class="form-element6 form-element-center">
											<% if (mybean.status.equals("Update") &&!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
											<div class="form-element6 ">
														<label> Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
											</div>
											<%} %>
											<% if (mybean.status.equals("Update") &&!(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>
											<div class="form-element6 ">
														<label> Entry Date: <%=mybean.entry_date%></label>
														</div>
											<%} %>
											<% if (mybean.status.equals("Update") &&!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
											<div class="form-element6 ">
														<label> Modified By: <%=mybean.unescapehtml(mybean.modified_by)%></label>
														</div>
											<%} %>
											<% if (mybean.status.equals("Update") &&!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
											<div class="form-element6 ">
														<label> Modified Date: </label>
															<%=mybean.modified_date%>
													</div>
											<%} %>
</div>
<div class="row"></div>
											<%if(mybean.status.equals("Add")){%>
											<center>
												<input name="button" type="submit" class="btn btn-success"
													id="button" value="Add Sales Category" onClick="onPress()" />
												<input type="hidden" name="add_button"
													value="Add Sales Category" />
											</center>
											<%}else if (mybean.status.equals("Update")){%>
											<center>
												<input type="hidden" name="update_button"
													value="Update Sales Category"> <input name="button"
													type="submit" class="btn btn-success" id="button"
													value="Update Sales Category" onClick="onPress()" /> <input
													name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" onClick="return confirmdelete(this)"
													value="Delete Sales Category" /> <input type="hidden"
													name="Update" value="yes" /></input>
											</center>
											<%}%>
											<input type="hidden" name="entry_by"
												value="<%=mybean.entry_by%>"> <input type="hidden"
												name="entry_date" value="<%=mybean.entry_date%>"> <input
													type="hidden" name="modified_by"
													value="<%=mybean.modified_by%>"> <input
														type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
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
