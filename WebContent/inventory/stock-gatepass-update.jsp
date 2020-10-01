<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Stock_Gatepass_Update" scope="request"/>
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
	<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
    <%@include file="../portal/header.jsp" %>
    
    <div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Update Gate Pass</h1>
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
						<li><a href="../inventory/index.jsp">Inventory</a> &gt; ;</li>
						<li><a href="../inventory/stock.jsp">Stock</a> &gt; </li>
						<li><a href="stock-list.jsp?vehstock_id=<%=mybean.vehstock_id%>">List Stock</a> &gt; </li>
						<li><a href="stock-gatepass-list.jsp?vehstock_id=<%=mybean.vehstock_id%>">List Stock Gate Passes</a> &gt; </li>
						<li><a href="stock-gatepass-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Stock Gate Pass</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="tab-pane" id="">
<!-- 					BODY START -->
<center><font color="#ff0000" ><b><%=mybean.msg%></b></font> <br></center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>&nbsp;Gate Pass</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
                                       <form name="form1" class="form-horizontal" method="post">
                                       <input type="hidden" name="txt_vehstockgatepass_from_location_id" value="<%=mybean.vehstockgatepass_from_location_id%>">
                    <input type="hidden" name="txt_vehstockgatepass_from_location_name" value="<%=mybean.vehstockgatepass_from_location_name%>">
                    <input type="hidden" name="txt_branch_name" value="<%=mybean.branch_name%>">
                    <input type="hidden" name="txt_vehstock_id" value="<%=mybean.vehstock_id%>">
                    <input type="hidden" name="txt_vehstock_branch_id" value="<%=mybean.vehstock_branch_id%>">
                    <input type="hidden" name="txt_vehstock_item_id" value="<%=mybean.vehstock_item_id%>">
                    <input type="hidden" name="txt_vehstock_chassis_no" value="<%=mybean.vehstock_chassis_no%>">
                    <input type="hidden" name="txt_item_name" value="<%=mybean.item_name%>">
                    <input type="hidden" name="txt_vehstockgatepass_time" value="<%=mybean.vehstockgatepass_time%>">
                    </td>
               <center><font>Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br>
                      </font></center>
                                     <div class="form-element6">
														<label> Stock ID: </label>
														<a href="../inventory/stock-list.jsp?vehstock_id=<%=mybean.vehstock_id%>"><%=mybean.vehstock_id%></a>
											</div>
											<div class="form-element6">
														<label> Item: </label>
														<a
															href="../inventory/inventory-item-list.jsp?item_id=<%=mybean.vehstock_item_id%>"><%=mybean.item_name%></a>
											</div>
											<div class="form-element6">
														<label> Chassis No.: </label>
															<a
																href="../inventory/stock-list.jsp?vehstock_id=<%=mybean.vehstock_id%>"><%=mybean.vehstock_chassis_no%></a>
											</div>
											<div class="form-element6">
														<label>Branch: </label>
															<a
																href="../portal/branch-summary.jsp?branch_id=<%=mybean.vehstock_branch_id%>"><%=mybean.branch_name%></a>
											</div>
												<div class="form-element6">
														<label>Time:</label>
                      <%=mybean.strToLongDate(mybean.vehstockgatepass_time)%>
											</div>
												<div class="form-element6">
														<label>From:</label>
															<a
																href="manage-stock-location.jsp?vehstocklocation_id=<%=mybean.vehstockgatepass_from_location_id%>"><%=mybean.vehstockgatepass_from_location_name%></a>
											</div>
											<div class="form-element6">
												<label>To<font color="#ff0000">*</font>:
												</label> <select name="dr_vehstockgatepass_to_location_id"
													class="form-control"
													id="dr_vehstockgatepass_to_location_id">
													<%=mybean.PopulateStockLocation()%>
												</select>

											</div>
											<div class="form-element6">
												<label>Driver<font color="#ff0000">*</font>:
												</label> <select name="dr_vehstockgatepass_stockdriver_id"
													class="form-control"
													id="dr_vehstockgatepass_stockdriver_id">
													<%=mybean.PopulateStockDriver()%>
												</select>
											</div>
											
											<div class="form-element6">
													<label> Out Kms<font color="#ff0000">*</font>: &nbsp;</label>
														<input name="txt_vehstockgatepass_out_kms" type="text"
															class="form-control" id="txt_vehstockgatepass_out_kms"
															value="<%=mybean.vehstockgatepass_out_kms%>" size="18"
															maxlength="10"
															onKeyUp="toFloat('txt_vehstockgatepass_out_kms','Out Kms')">

											</div>
											
											<div class="form-element6">
														<label>Notes: </label>
														<textarea name="txt_vehstockgatepass_notes"
																	cols="70" rows="5" class="form-control"
																	id="txt_vehstockgatepass_notes"><%=mybean.vehstockgatepass_notes%></textarea>
											</div>
											<div class="row"></div>
											<% if (mybean.status.equals("Update") &&!(mybean.vehstockgatepass_entry_by == null) && !(mybean.vehstockgatepass_entry_by.equals(""))) { %>
												<div class="form-element6">
													<label>Entry By: <%=mybean.unescapehtml(mybean.vehstockgatepass_entry_by)%></label>
											</div>
											<%
												}
											%>
											 <% if (mybean.status.equals("Update") &&!(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>
												<div class="form-element6">
													<label>Entry Date: <%=mybean.entry_date%></label>
											</div>
											<%
												}
											%>
											<% if (mybean.status.equals("Update") &&!(mybean.vehstockgatepass_modified_by == null) && !(mybean.vehstockgatepass_modified_by.equals(""))) { %>
												<div class="form-element6">
													<label>Modified By: <%=mybean.unescapehtml(mybean.vehstockgatepass_modified_by)%></label>
											</div>
											<%
												}
											%>
											<% if (mybean.status.equals("Update") &&!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
												<div class="form-element6">
													<label>Modified Date: <%=mybean.modified_date%></label>
											</div>
											<%
												}
											%>
											<%if(mybean.status.equals("Add")){%>
                        <center><input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Gate Pass" onclick="return SubmitFormOnce(document.form1,this);"/>
                        <input type="hidden" name="add_button" value="yes"/></center>
                        <%}else if (mybean.status.equals("Update")){%>
                        <center><input type="hidden" name="update_button" value="yes"/>
                        <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Gate Pass" onclick="return SubmitFormOnce(document.form1,this);"/>
                        <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Gate Pass"/></center>
                        <%}%>
                        <input type="hidden" name="vehstockgatepass_entry_by" value="<%=mybean.vehstockgatepass_entry_by%>">
                        <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
                        <input type="hidden" name="vehstockgatepass_modified_by" value="<%=mybean.vehstockgatepass_modified_by%>">
                        <input type="hidden" name="modified_date" value="<%=mybean.modified_date%>">
											
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
     <%@include file="../Library/admin-footer.jsp" %>
<%@include file="../Library/js.jsp"%>
    </body>
</HTML>
