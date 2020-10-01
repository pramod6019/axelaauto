<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Kms_Update"
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
<body onLoad="FormFocus()"
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
						<h1><%=mybean.status%>Kms
						</h1>
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
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="../service/vehicle.jsp">Vehicle</a>&gt;</li>
						<li><a href="vehicle-list.jsp?all=yes">
								ListVehicles</a> &gt;</li>
						<li><a href="kms-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
								Kms</a>:</li>
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
										<center><%=mybean.status%>
											Kms
										</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<font color=#ff0000>*</font> are required.
												</font>
											</center>
										</br>
											<%
												if (!mybean.veh_contact_id.equals("") && !mybean.veh_contact_id.equals("0")) {
											%>
											<%
												}
											%>
											<div class="form-element6">
												<label >Customer:&nbsp;</label>
													<b><%=mybean.link_customer_name%></b>&nbsp; <input
														name="acct_id" type="hidden" id="acct_id"
														value="<%=mybean.veh_customer_id%>">
											</div>
											<div class="form-element6">
												<label >Contact:&nbsp;</label>
													<b><%=mybean.link_contact_name%></b>&nbsp; <input
														name="cont_id" type="hidden" id="cont_id"
														value="<%=mybean.veh_contact_id%>">
											</div>
											<div class="form-element6" id="div_veh_chassis_no">
												<label >Chassis
													Number:&nbsp;</label>
														<b><%=mybean.veh_chassis_no%></b>&nbsp;
													<input name="txt_veh_chassis_no" type="hidden"
														id="txt_veh_chassis_no" value="<%=mybean.veh_chassis_no%>">
											</div>
											<div class="form-element6" id="div_veh_engine_no">
												<label>Engine No.:&nbsp;</label>
														<b><%=mybean.veh_engine_no%></b>&nbsp;
													<input name="txt_veh_engine_no" type="hidden"
														id="txt_veh_engine_no" value="<%=mybean.veh_engine_no%>">
											</div>
											<div class="form-element6" id="div_veh_reg_no">
												<label >Reg. No.:&nbsp;</label>
														<b><%=mybean.veh_reg_no%></b>&nbsp;
													<input name="txt_veh_reg_no" type="hidden"
														id="txt_veh_reg_no" value="<%=mybean.veh_reg_no%>">
											</div>
											<div class="form-element6">
												<label >Kms<font
													color="#ff0000">*</font>:&nbsp;
												</label>
													<input name="txt_veh_kms" type="text" class="form-control"
														maxlength="9" id="txt_veh_kms" value="<%=mybean.veh_kms%>"
														size="20" maxlength="25" onkeyup="toInteger(this.id);">
											</div>
											<div id="dialog-modal"></div>
											</tr>

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6">
												<label >Entry By: </label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>">
											</div>
											<div class="form-element6">
												<label >Entry Date: </label>
													<%=mybean.entry_date%>
													<input type="hidden" id="entry_date" name="entry_date"
														value="<%=mybean.entry_date%>">
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
											%>

											<div class="form-element6">
												<label ">Modified By: </label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input type="hidden" id="modified_by" name="modified_by"
														value="<%=mybean.modified_by%>">
											</div>
											<div class="form-element6">
												<label >Modified Date:
												</label>
													<%=mybean.modified_date%>
													<input type="hidden" id="modified_date"
														name="modified_date" value="<%=mybean.modified_date%>">
											</div>
											<%
												}
											%>
											<%
														if (mybean.status.equals("Add")) {
													%>
											<center>
												<input name="addbutton" id="addbutton" type="button"
													onClick="return SubmitFormOnce(document.form1, this);"
													class="btn btn-success" value="Add Kms" /> <input
													type="hidden" id="add_button" name="add_button" value="yes" />
											</center>
											<% } else if (mybean.status.equals("Update")) { %>
											<center>
											<div class="form-element12">
												<input type="hidden" id="update_button" name="update_button"
													value="yes" /> <input name="updatebutton"
													id="updatebutton" type="submit" class="btn btn-success"
													value="Update Kms" onclick="return SubmitFormOnce(document.form1,this);" />
													<input name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" onClick="return confirmdelete(this)"
													value="Delete Kms" />
													</div>
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

	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>

</body>
</HTML>