<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.preowned.Preowned_Stock_Gatepass_Update" scope="request" />
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">
 <%@ include file="../Library/css.jsp" %> 
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
						<h1>Preowned Stock Add Gatepass</h1>
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
						<li><a href="preowned-stock.jsp">Pre Owned Stock</a> &gt;</li>
						<li><a
							href="preowned-stock-list.jsp?preownedstock_id=<%=mybean.preownedstock_id%>">List
								Stock</a> &gt;</li>
						<li><a
							href="preowned-stock-gatepass-list.jsp?preownedstock_id=<%=mybean.preownedstock_id%>">List
								Stock Gate Passes</a> &gt;</li>
						<li><a
							href="preowned-stock-gatepass-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Stock
								Gate Pass</a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="form1" method="post" class="form-horizontal">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"><%=mybean.status%>&nbsp;Gate
											Pass
										</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<input type="hidden"
												name="txt_preownedstockgatepass_from_location_id"
												value="<%=mybean.preownedstockgatepass_from_location_id%>" />
											<input type="hidden"
												name="txt_preownedstockgatepass_from_location_name"
												value="<%=mybean.preownedstockgatepass_from_location_name%>" />
											<input type="hidden" name="txt_branch_name"
												value="<%=mybean.branch_name%>" /> <input type="hidden"
												name="txt_preownedstock_id"
												value="<%=mybean.preownedstock_id%>" /> <input type="hidden"
												name="txt_preowned_branch_id"
												value="<%=mybean.preowned_branch_id%>" /> <input
												type="hidden" name="txt_preowned_variant_id"
												value="<%=mybean.preowned_variant_id%>" /> <input
												type="hidden" name="txt_preownedstock_chassis_no"
												value="<%=mybean.preownedstock_chassis_no%>" /> <input
												type="hidden" name="txt_variant_name"
												value="<%=mybean.variant_name%>" /> <input type="hidden"
												name="txt_preownedstockgatepass_time"
												value="<%=mybean.preownedstockgatepass_time%>" />
											<center>
												Form fields marked with a red asterisk
													<b><font color="#ff0000">*</font></b> are resquired.<br />
											</center></br>
											<div class="form-element6 ">
												<label >Pre Owned
													Stock ID<font color=red>*</font>:
												</label>
													<a href="preowned-stock-list.jsp?preownedstock_id=<%=mybean.preownedstock_id%>"><%=mybean.preownedstock_id%></a>

											</div>
											<div class="form-element6 ">
												<label >Variant: </label>

													<a
														href="../inventory/inventory-item-list.jsp?item_id=<%=mybean.preowned_variant_id%>"><%=mybean.variant_name%></a>

											</div>
											<div class="form-element6 ">
												<label >Chassis No.: </label>
													<a
														href="preowned-stock-list.jsp?preownedstock_id=<%=mybean.preownedstock_id%>"><%=mybean.preownedstock_chassis_no%></a>

											</div>
											<div class="form-element6 ">
												<label > Branch: </label>

													<a
														href="../portal/branch-summary.jsp?branch_id=<%=mybean.preowned_branch_id%>"><%=mybean.branch_name%></a>

											</div>
											<div class="form-element6 ">
												<label >Time: </label>
													<%=mybean.strToLongDate(mybean.preownedstockgatepass_time)%>
											</div>
											<div class="form-element6 ">
												<label >From: </label>
													<%=mybean.preownedstockgatepass_from_location_name%>
											</div>
											<div class="form-element6 ">
												<label >To<font
													color="#ff0000">*</font>:
												</label>
													<select name="dr_preownedstockgatepass_to_location_id"
														class="form-control"
														id="dr_preownedstockgatepass_to_location_id">
														<%=mybean.PopulatePreownedLocation() %>
													</select>
											</div>
											<div class="form-element6 ">
												<label >Driver<font
													color="#ff0000">*</font>:
												</label>
													<select name="dr_preownedstockgatepass_driver_id"
														class="form-control"
														id="dr_preownedstockgatepass_driver_id">
															<%=mybean.PopulatePreownedDriver() %>
													</select>
											</div>
											<div class="form-element6 ">
												<label >Notes: </label>
													<textarea name="txt_preownedstockgatepass_notes" cols="70"
														rows="5" class="form-control"
														id="txt_preownedstockgatepass_notes"><%=mybean.preownedstockgatepass_notes%></textarea>
											</div>
											 <div class="row"></div>	
											<% if (mybean.status.equals("Update") &&!(mybean.preownedstockgatepass_entry_by == null) && !(mybean.preownedstockgatepass_entry_by.equals(""))) { %>

											<div class="form-element6">
												<label >Entry By: </label>
                  
                    <%=mybean.unescapehtml(mybean.preownedstockgatepass_entry_by)%>
											</div>
											<%} %>
											<% if (mybean.status.equals("Update") &&!(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>
											<div class="form-element6">
												<label >Entry Date:</td>
												</label>
                <%=mybean.entry_date%>
											</div>
											<%} %>
											 <% if (mybean.status.equals("Update") &&!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
											<div class="form-element6">
												<label > Modified By:
												</label>
                    <%=mybean.unescapehtml(mybean.preownedstockgatepass_modified_by)%>
											</div>
											<%}%>
											<% if (mybean.status.equals("Update") &&!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
											<div class="form-element6">
												<label >Modified Date:
												</label>
                 
                    <%=mybean.modified_date%>
											</div>
											<%} %>
										
											
                 <%if(mybean.status.equals("Add")){%>
                        <center>
                        <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Gate Pass" onclick="return SubmitFormOnce(document.form1,this);"/>
                        <input type="hidden" name="add_button" value="yes"/>
                        
                        </center>
                        <%}else if (mybean.status.equals("Update")){%>
                        <center>
                        	<div class="form-element12">
                        <input type="hidden" name="update_button" value="yes"/>
                        <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Gate Pass" onclick="return SubmitFormOnce(document.form1,this);"/>
                        <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Gate Pass"/>
                         </div>
                        </center>
                        <%}%>
                  <input type="hidden" name="preownedstockgatepass_entry_by" value="<%=mybean.preownedstockgatepass_entry_by%>"/>
                        <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>"/>
                        <input type="hidden" name="preownedstockgatepass_modified_by" value="<%=mybean.preownedstockgatepass_modified_by%>"/>
                        <input type="hidden" name="modified_date" value="<%=mybean.modified_date%>"/>
										</div>
									</div>
								</div>
							</form>
					</div>
				</div>
				</div>
				</div>
			</div>
		</div>

	</div>
    
     <%@ include file="../Library/admin-footer.jsp" %> 
       <%@ include file="../Library/js.jsp" %> 
    </body>
</HTML>
