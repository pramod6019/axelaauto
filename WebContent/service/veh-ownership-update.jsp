<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Veh_Ownership_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
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
						<h1><%=mybean.status%>
							Ownership
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
						<li><a href="vehicle.jsp">Vehicles</a> &gt;</li>
						<li><a href="vehicle-list.jsp?all=yes">List Vehicles</a> &gt;</li>
						<li><a href="vehicle-dash.jsp?veh_id=<%=mybean.veh_id%>"><%=mybean.veh_reg_no%>
								(<%=mybean.veh_id%>)</a>&gt;&nbsp;</li>
						<li><a
							href="veh-ownership-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Ownership</a>:</li>

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
											Ownership
										</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="">Form fields marked with a red asterisk
													<font color=#ff0000>*</font> are required.
												</font>
											</center>

											<div class="form-element6">
												<label >Name<font color="red">*</font>: </label>
													<input name="txt_ownership_name" type="text"
														class="form-control" id="txt_ownership_name"
														value="<%=mybean.ownership_name%>" size="32"
														maxlength="255" />
											</div>
											<div class="form-element6 form-element-margin">
												<label >Vehicle: </label>
													<span id="veh_regno" name="veh_regno"><a
														href="../service/vehicle-list.jsp?veh_id=<%=mybean.veh_id%>">
															<%=mybean.SplitRegNo(mybean.veh_reg_no, 2)%>
													</a></span>
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label >Address<font color="red">*</font>: </label>
													<textarea name="txt_ownership_address"
														id="txt_ownership_address" cols="70" rows="4"
														class="form-control"><%=mybean.ownership_address%></textarea>
											</div>
											<div class="form-element6">
												<label >Mobile<font color="red">*</font>: </label>
													<input name="txt_ownership_mobile" type="text"
														class="form-control" id="txt_ownership_mobile"
														value="<%=mybean.ownership_mobile1%>" size="32"
														maxlength="13"
														onKeyUp="toPhone('txt_ownership_mobile','Mobile 1')" /> <br>
													(91-9999999999)
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label >Email ID: </label>
													<input name="txt_ownership_email1" type="text"
														class="form-control" id="txt_ownership_email1"
														value="<%=mybean.ownership_email1%>" size="32"
														maxlength="50" />
											</div>

											<div class="form-element6">
												<label >Ownership From<font color=#ff0000><b> *</b></font>: </label>
												<input type="text" name="txt_ownership_from"
																	id="txt_ownership_from" value="<%=mybean.ownershipfrom%>"
																	class="form-control datepicker">
											</div>
											<div class="form-element6">
												<label >Ownership To<font color=#ff0000><b> *</b></font>: </label>
													<input name="txt_ownership_to" id="txt_ownership_to"
														class="form-control datepicker"
														  value="<%=mybean.ownershipto%>" size="12" maxlength="14" />
											</div>
											<div class="form-element6">
												<label >Notes: </label>
													<textarea name="txt_ownership_notes"
														id="txt_ownership_notes" cols="70" rows="4"
														class="form-control"><%=mybean.ownership_notes%></textarea>
											</div>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.ownership_entry_date == null)
														&& !(mybean.ownership_entry_date.equals(""))) {
											%>

											<div class="form-element6">
												<label >Entry By: </label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>" />
											</div>
											<div class="form-element6">
												<label >Entry Date: </label>
													<%=mybean.ownership_entry_date%>
													<input type="hidden" id="ownership_entry_date"
														name="ownership_entry_date"
														value="<%=mybean.ownership_entry_date%>" />
											</div>


											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.ownership_modified_date == null)
														&& !(mybean.ownership_modified_date.equals(""))) {
											%>
											<div class="form-element6">
												<label >Modified By: </label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input type="hidden" id="modified_by" name="modified_by"
														value="<%=mybean.unescapehtml(mybean.modified_by)%>">
											</div>
											<div class="form-element6">
												<label >Modified Date: </label>
													<%=mybean.ownership_modified_date%>
													<input type="hidden" id="ownership_modified_date"
														name="ownership_modified_date"
														value="<%=mybean.ownership_modified_date%>">
											</div>
											<%
												}
											%>

											<%
												if (mybean.status.equals("Add")) {
											%>
											<center>
											<div class="form-element12">
												<input name="addbutton" type="submit"
													class="btn btn-success" id="addbutton"
													value="Add Ownership"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input type="hidden" id="add_button" name="add_button"
													value="yes" />
													</div>
											</center>
											<%
												} else if (mybean.status.equals("Update")) {
											%>
											<center>
											<div class="form-element12">
												<input type="hidden" id="update_button" name="update_button"
													value="yes" /> <input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													value="Update Ownership"
													onClick="return SubmitFormOnce(document.form1, this);" />
	
												<input name="delete_button" type="submit"
													class="btn btn-success" id="delete_button"
													onclick="return confirmdelete(this)"
													value="Delete Ownership" />
													</div>
												<center>
													<%
														}
													%>
												
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
	<script language="javascript" type="text/javascript">
		function FormFocus() {
			document.form1.txt_ownership_name.focus();
		}
	</script>
</body>
</HTML>