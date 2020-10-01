<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Veh_Salesorder_Din_Update" scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>

<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%>
							DIN
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
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="veh-salesorder-din-update.jsp?so_id=30002"><%=mybean.status%> DIN</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="form1" method="post" class="form-horizontal">
								<div class="portlet box  page-content-inner container-fluid">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<center><%=mybean.status%> DIN </center>
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<div class="container-fluid">
											<!-- 										--- -->
											<center>
												<font size="">Form fields marked with a red asterisk<font color="red">*</font> are required.</font>
											</center>
											
											<div class="form-element2 form-element-margin">
												<label>SO ID:<font color="#ff0000">*</font>: </label>
												<a href="veh-salesorder-list.jsp?so_id=<%=mybean.so_id%>"><%=mybean.so_id%></a>
											</div>
											
											<div class="form-element4 ">
												<label>Delivery Location<font color="#ff0000">*</font>: </label>
												<select name="dr_din_deliveryloctype_id" id="dr_din_deliveryloctype_id" class="form-control">
													<%=mybean.PopulateDeliveryLocationType()%>
												</select>
											</div>
											
											<div class="form-element6">
												<label>Name Of Wife/Husband: </label>
												<input name="txt_so_din_spouse_name" type="text" class="form-control" id="txt_so_din_spouse_name"
													value="<%=mybean.so_din_spouse_name%>" size="10" maxlength="255" />
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center>Accessories Details</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- 										----- -->
										<div class="form-element6">
											<label>Special Occasion: </label>
											<textarea name="txt_so_din_special_remarks" cols="70" rows="4" class="form-control" maxlength="255"
												id="txt_so_din_special_remarks"><%=mybean.so_din_special_remarks%></textarea>
										</div>
										
										<div class="form-element6">
											<label>Promised Delivery Time<b><font color="#ff0000">*</font></b>: </label>
											<input type="text" size="16" name="txt_so_din_promised_time"
												id="txt_so_din_promised_time" class="form-control datepicker"
												value="<%=mybean.promisedDeliveryTime%>" /> 
										</div>
									</div>
								</div>
							</div>
									
							<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<center>Insurance Details</center>
										</div>
									</div>
									
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<div class="container-fluid">
											
											<div class="form-element6">
												<label>Insurance Details: </label>
												<select name="dr_so_din_insurance_details" id="dr_so_din_insurance_details" class="form-control">
													<%=mybean.PopulateInsuranceDetails()%>
												</select>
											</div>
											
											<div class="form-element6">
												<label>Type: </label>
												<input type="text" maxlength="255" name="txt_so_din_insurance_type"
													id="txt_so_din_insurance_type" class="form-control"
													value="<%=mybean.so_din_insurance_type%>" /> 
											</div>
											
											<div class="row"></div>
											
											<div class="form-element6">
												<label>No Claim Bonus: </label>
												<input type="text" maxlength="255" name="txt_so_din_insurance_noclaimbonus"
													id="txt_so_din_insurance_noclaimbonus" class="form-control"
													value="<%=mybean.so_din_insurance_noclaimbonus%>" /> 
											</div>
											
											<div class="form-element6">
												<label>Insurance Nominee Age: </label>
												<input type="text" maxlength="2" name="txt_so_din_insurance_nominee_age"
													id="txt_so_din_insurance_nominee_age" class="form-control"
													onKeyUp="toInteger('txt_so_din_insurance_nominee_age','Age')"
													value="<%=mybean.so_din_insurance_nominee_age%>" /> 
											</div>
											
											<div class="form-element6">
												<label>Name: </label>
												<input type="text" maxlength="255" name="txt_so_din_insurance_nominee_name"
													id="txt_so_din_insurance_nominee_name" class="form-control"
													value="<%=mybean.so_din_insurance_nominee_name%>" /> 
											</div>
											
											<div class="form-element6">
												<label>Relation: </label>
												<input type="text" maxlength="255" name="txt_so_din_insurance_nominee_relation"
													id="txt_so_din_insurance_nominee_relation" class="form-control"
													value="<%=mybean.so_din_insurance_nominee_relation%>" /> 
											</div>
										</div>
									</div>
								</div>
							</div>
							
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<center>Registration Details</center>
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">

											<div class="form-element6">
												<label>Permanent RTO Name: </label>
												<input name="txt_so_din_perm_rto_name" type="text"
													class="form-control" id="txt_so_din_perm_rto_name"
													value="<%=mybean.so_din_perm_rto_name%>" maxlength="255" />
											</div>
											
											<div class="form-element6">
												<label>RTO Code: </label>
												<input name="txt_so_din_rto_code" type="text"
													class="form-control" id="txt_so_din_rto_code"
													value="<%=mybean.so_din_rto_code%>" size="10" maxlength="6" />
											</div>
											
											<div class="form-element6">
												<label>RTO PIN Code: </label>
												<input name="txt_so_din_rto_pincode" type="text" class="form-control"
													 id="txt_so_din_rto_pincode" value="<%=mybean.so_din_rto_pincode%>"
													 onKeyUp="toInteger('txt_so_din_rto_pincode','Pin')" size="10" maxlength="6" />
											</div>
											
											<div class="form-element6">
												<label>RTO: </label>
												<input name="txt_so_din_rto" type="text" class="form-control" id="txt_so_din_rto"
													value="<%=mybean.so_din_rto%>" size="10" maxlength="255" />
											</div>
											
											<div class="form-element6">
												<label>Taxi Permit: </label>
												<select name="dr_so_din_taxi_permit" id="dr_so_din_taxi_permit" class="form-control">
													<%=mybean.PopulateTaxiPermit()%>
												</select>
											</div>
											
											<div class="form-element6">
												<label>Registration Type: </label>
												<input name="txt_so_din_registration_type" type="text"
													class="form-control" id="txt_so_din_registration_type"
													value="<%=mybean.so_din_registration_type%>" size="10" maxlength="255" />
											</div>
											
											<div class="row"></div>
											
											<div class="form-element6">
												<label>Address for Registration Permanent: </label>
												<textarea name="txt_so_din_registration_addr_perm" cols="70" rows="4" class="form-control" maxlength="255"
													id="txt_so_din_registration_addr_perm"><%=mybean.so_din_registration_addr_perm%></textarea>
											</div>
											
											<div class="form-element6">
												<label>Address for Registration Temporary: </label>
												<textarea name="txt_so_din_registration_addr_temp" cols="70" rows="4" class="form-control" maxlength="255"
													id="txt_so_din_registration_addr_temp"><%=mybean.so_din_registration_addr_temp%></textarea>
											</div>
											
											<% if (mybean.status.equals("Update") && (mybean.entry_by != null) && !(mybean.entry_by.equals(""))) { %>
													
											<div class="form-element6">
												<label >Entry By:</label>
												<%=mybean.unescapehtml(mybean.entry_by)%>
												<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>" />
											</div>
											<div class="form-element6">
												<label>Entry Date:</label>
												<%=mybean.entry_date%>
												<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>" />
											</div>
											<% } %>
											<%
											if (mybean.status.equals("Update") && (mybean.modified_by != null) && !(mybean.modified_by.equals(""))) { %>
											<div class="form-element6">
												<label >Modified By:</label>
												<%=mybean.unescapehtml(mybean.modified_by)%>
												<input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>" />
											</div>
											<div class="form-element6">
												<label >Modified Date:</label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>" />
											</div>
											<% } %>
											
											<div class="row"></div>
											<center>
												<% if (mybean.status.equals("Add")) { %>
												
												<% if (mybean.so_invoice_request.equals("0")) { %>
												<input name="button" type="submit" class="btn btn-success" id="button" value="Request For Invoice"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input name="requestinvoice" type="hidden" value="invoice" />
												<% } %>
												<input name="button" type="submit" class="btn btn-success" id="button" value="Request For Delivery"
													onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
												<input type="hidden" name="add_button" value="yes">
												
												<% } else if (mybean.status.equals("Update")) { %>
												<% if (mybean.so_invoice_request.equals("0")) { %>
												<input name="requestinvoice" type="hidden" value="invoice" />
												<input name="button" type="submit" class="btn btn-success" id="button" value="Request For Invoice"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<% } %>
												<input name="update_button" type="hidden" value="yes" />
												<input name="button" type="submit" class="btn btn-success" id="button" value="Request For Delivery"
													onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
												<% } %>
											</center>
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
	<!-- END CONTAINER -->

<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
</body>
</HTML>
	