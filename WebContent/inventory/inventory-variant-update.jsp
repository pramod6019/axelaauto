<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.inventory.Inventory_Variant_Update" scope="request" />
<%mybean.doGet(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</head>
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
						<h1><%=mybean.status%>&nbsp;Variant
						</h1>
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
						<li><a href="../inventory/index.jsp">Inventory</a> &gt;&nbsp;</li>
						<li><a href="../inventory/inventory-variant-list.jsp?all=yes">List
								Variants</a> &gt;&nbsp;</li>
						<li><a
							href="../inventory/inventory-variant-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Variant</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>&nbsp;Variant
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="post">
												<div class="form-element6">
													<label>Name<font color="#ff0000">*</font>: </label>
														<input name="txt_item_name" type="text"
															class="form-control" id="txt_item_name"
															value="<%=mybean.item_name%>" maxlength="255" />
												</div>
												<div class="form-element6">
													<label >Code:</label>
														<input name="txt_item_code" type="text"
															class="form-control" id="txt_item_code"
															value="<%=mybean.item_code%>" maxlength="20" />
												</div>
												<div class="form-element6">
													<label>Service Code:</label>
														<input name="txt_item_service_code" type="text"
															class="form-control" id="txt_item_service_code"
															value="<%=mybean.item_service_code%>" 
															maxlength="20" />
												</div>
												<div class="form-element6">
													<label>Model<font color="#ff0000">*</font>: </label>
														<select name="drop_item_model_id" class="form-control">
															<%=mybean.PopulateModel()%>
														</select>
														<div class="admin-master">
															<a
																href="file:///D|/webapp3/axelaauto-silverarrows/web/inventory/item-model.jsp?all=yes"
																title="Manage Model"></a>
														</div>
												</div>
												<div class="form-element6">
													<label>Small Description:</label>
														<textarea name="txt_item_small_desc" cols="50" rows="5"
															class="form-control" id="txt_item_small_desc"
															onkeyup="charcount('txt_item_small_desc', 'span_txt_item_small_desc','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.item_small_desc%></textarea>
														<span id=span_txt_item_small_desc> 255 characters </span>
												</div>
												<div class="form-element6">
													<label>Fuel Type<font color="#ff0000">*</font>: </label>
														<select id="dr_item_fueltype_id"
															name="dr_item_fueltype_id" class="form-control">
															<%=mybean.PopulateFuelType()%>
														</select>
												</div>
												
												<div class="form-element6 form-element-margin">
													<label>Active:</label>
														<input id="chk_item_active" type="checkbox"
															name="chk_item_active"
															<%=mybean.PopulateCheck(mybean.item_active) %> />
												</div>
												
												<div class="row"></div>
												<div class="form-element6">
													<label>Big Description:</label>
														<textarea name="txt_item_big_desc" cols="50" rows="5"
															class="form-control" id="txt_item_big_desc"
															onkeyup="charcount('txt_item_big_desc', 'span_txt_item_big_desc','<font color=red>({CHAR} characters left)</font>', '2000')"><%=mybean.item_big_desc%></textarea>
														<span id=span_txt_item_big_desc> 2000 characters</span>
												</div>
												
												<div class="form-element6">
													<label>Notes:</label>
														<textarea name="txt_item_notes" cols="70" rows="4"
															class="form-control" id="txt_item_notes"><%=mybean.item_notes%></textarea>
												</div>
												
												<div class="row"></div>
												
											<% if (mybean.status.equals("Update") &&!(mybean.item_entry_by == null) && !(mybean.item_entry_by.equals(""))) { %>
												<div class="form-element6">
													<label>Entry By:</label>
														<%=mybean.unescapehtml(mybean.item_entry_by)%>
												</div>
											<%} %>
											<% if (mybean.status.equals("Update") &&!(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>
												<div class="form-element6">
													<label>Entry Date:</label>
														<%=mybean.entry_date%>
												</div>
											<%} %>
											<% if (mybean.status.equals("Update") &&!(mybean.item_modified_by == null) && !(mybean.item_modified_by.equals(""))) { %>
												<div class="form-element6">
													<label>Modified By:</label>
														<%=mybean.unescapehtml(mybean.item_modified_by)%>
												</div>
											<%} %>
											<% if (mybean.status.equals("Update") &&!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
												<div class="form-element6">
													<label>Modified Date:</label> 
														<%=mybean.modified_date%>
												</div>
											<%} %>
											<center>
												<%if(mybean.status.equals("Add")){%>
												<input name="addbutton" type="submit"
													class="btn btn-success" id="addbutton" value="Add Variant"
													onClick="return SubmitFormOnce(document.form1, this);" /> <input
													type="hidden" name="add_button" id="add_button" value="yes">
												<%}else if (mybean.status.equals("Update")){%>
												<input type="hidden" name="update_button" id="update_button"
													value="yes" /> <input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													value="Update Variant"
													onClick="return SubmitFormOnce(document.form1, this);" />
													<input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Variant"/>
												<%}%>
												<input type="hidden" name="item_entry_by"
													value="<%=mybean.item_entry_by%>" /> <input type="hidden"
													name="entry_date" value="<%=mybean.entry_date%>" /> <input
													type="hidden" name="item_modified_by"
													value="<%=mybean.item_modified_by%>" /> <input
													type="hidden" name="modified_date"
													value="<%=mybean.modified_date%>" />
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

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

</body>
</HTML>
