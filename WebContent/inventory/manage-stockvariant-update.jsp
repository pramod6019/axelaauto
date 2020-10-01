<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.inventory.Manage_StockVariant_Update" scope="request" />
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
<body onLoad="FormFocus();" leftmargin="0" rightmargin="0" topmargin="0"
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
						<h1>Add Stock Variant</h1>
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
						<li><a href="../portal/manager.jsp">Business Manager</a> &gt; </li>
						<li><a href="../inventory/manage-stockvariant.jsp?all=yes">List Stock Variants</a> &gt;</li>
						<li><a href="../inventory/manage-stockvariant-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Stock
								Variant</a>:<b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%><br></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>&nbsp;Stock
										Variant
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="post">
											<div class="form-element6 form-element-center">
													<label>Item<font color="#ff0000">*</font>: </label>
														<select name="dr_stockvariant_item_id" class="form-control"
															id="dr_stockvariant_item_id">
																<%=mybean.PopulateItem(mybean.vehstockvariant_item_id,"",request)%>
														</select>
											</div>
											<div class="form-element6 form-element-center">
													<label>Code<font color="#ff0000">*</font>: </label>
														<input name="txt_stockvariant_code" type="text"
															class="form-control" id="txt_stockvariant_code"
															value="<%=mybean.vehstockvariant_code%>" size="20"
															maxlength="20" />
											</div>
											<div class="row"></div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6 form-element-center">
													<label>Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
													</div>
											<% } %>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6 form-element-center">
													<label>Entry Date: <%=mybean.entry_date%></label>
											</div>
											<% } %>
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
													<label>Modified Date: <%=mybean.modified_date%></label> </div>
											<%
												}
											%>
											<%if(mybean.status.equals("Add")){%>
											<center>
												<input name="button" type="submit" class="btn btn-success"
													id="button"
													onClick="return SubmitFormOnce(document.form1, this);"
													value="Add Variant" /> <input type="hidden"
													name="add_button" value="yes" />
											</center>
											<%}else if (mybean.status.equals("Update")){%>
											<center>
												<input type="hidden" name="update_button" value="yes">
													<input name="button" type="submit" class="btn btn-success"
													id="button"
													onClick="return SubmitFormOnce(document.form1, this);"
													value="Update Variant" /> <input name="delete_button"
													type="submit" class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)" value="Delete Variant" /></input>
											</center>
											<%}%>
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
	<script language="JavaScript" type="text/javascript">
            function FormFocus() {
                document.form1.txt_stockdriver_name.focus();
            }
        </script>
</body>
</HTML>
