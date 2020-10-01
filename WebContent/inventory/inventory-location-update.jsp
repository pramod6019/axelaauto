<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.inventory.Inventory_Location_Update" scope="request" />
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
<%@include file="../Library/css.jsp"%>
</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Add Location</h1>
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
						<li><a href="inventory-location-list.jsp?all=yes">List
								Locations</a> &gt;&nbsp;</li>
						<li><a
							href="inventory-location-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Location</a>:<b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font> <br />
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Location
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="post">
											<center>
												<font>Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.<br>
												</font>
											</center>
											<div class="form-element6">
													<label>Branch<font color="#ff0000">*</font>: </label>
														<select name="dr_branch" id="dr_branch"
															class="form-control">
															<%=mybean.PopulateBranch(mybean.location_branch_id, "", "", "", request)%>
														</select>
											</div>
											<div class="form-element4">
													<label>Location Name<font color="#ff0000">*</font>: </label>
														 <input name="txt_location_name"
															type="text" class="form-control" id="txt_location_name"
															value="<%=mybean.location_name%>" size="50"
															maxlength="255" />
											</div>
											<div class="form-element2">
													<label>Location Code<font color="#ff0000">*</font>: </label>
														<input name="txt_location_code" type="text"
															class="form-control" id="txt_location_code"
															value="<%=mybean.location_code%>" size="50"
															maxlength="50" />
											</div>	<div class="row"></div>
											<div class="form-element3">
													<label>Mobile 1: </label>
														<input name="txt_location_mobile1" type="text"
															id="txt_location_mobile1"
															onKeyUp="toPhone('txt_location_mobile1','Mobile 1')"
															class="form-control" value="<%=mybean.location_mobile1%>"
															size="13" maxlength="13" /> (91-9999999999)
											</div>
											<div class="form-element3">
													<label>Mobile 2:</label>
														<input name="txt_location_mobile2" type="text"
															class="form-control" id="txt_location_mobile2"
															onKeyUp="toPhone('txt_location_mobile2','Mobile 2')"
															value="<%=mybean.location_mobile2%>" size="13"
															maxlength="13" /> (91-9999999999)
											</div>
											<div class="form-element3">
													<label>Phone 1:</label>
														<input name="txt_location_phone1" type="text"
															class="form-control" id="txt_location_phone1"
															onKeyUp="toPhone('txt_location_phone1','Phone 1')"
															value="<%=mybean.location_phone1%>" size="14"
															maxlength="14" /> (91-80-33333333)
											</div>
											<div class="form-element3">
													<label>Phone 2:</label>
														<input name="txt_location_phone2" type="text"
															class="form-control" id="txt_location_phone2"
															onKeyUp="toPhone('txt_location_phone2','Phone 2')"
															value="<%=mybean.location_phone2%>" size="14"
															maxlength="14" /> (91-80-33333333)
											</div>
												<div class="row"></div>
											<div class="form-element6">
													<label>Address<font color="#ff0000">*</font>: </label>
														<textarea name="txt_location_add" cols="50" rows="5"
															class="form-control" id="txt_location_add"
															onkeyup="charcount('txt_location_add', 'span_txt_location_add','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.location_add%></textarea>
														<span id=span_txt_location_add> 255 characters </span>
											</div>
											<div class="form-element3">
													<label>State<font color="#ff0000">*</font>: </label>
														<span id="state_id"><%=mybean.PopulateState()%></span>
											</div>
											<div class="form-element3">
													<label>City<font color="#ff0000">*</font>: </label>
														<span id="city_id"><%=mybean.PopulateCity()%></span>
											</div>
											<div class="form-element6">
													<label>Pin<font color="#ff0000">*</font>: </label>
														<input name="txt_location_pin" type="text"
															class="form-control" id="txt_location_pin"
															onKeyUp="toInteger('txt_location_pin','Pin')"
															value="<%=mybean.location_pin%>" size="10" maxlength="6" />
											</div>
												<div class="row"></div>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.location_entry_by == null)
														&& !(mybean.location_entry_by.equals(""))) {
											%>
											<div class="form-element6">
													<label>Entry By: <%=mybean.unescapehtml(mybean.location_entry_by)%></label>
														<input type="hidden" name="location_entry_by"
															value="<%=mybean.location_entry_by%>">
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6">
													<label>Entry Date: <%=mybean.entry_date%></label>
														<input type="hidden" name="entry_date"
															value="<%=mybean.entry_date%>">
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.location_modified_by == null)
														&& !(mybean.location_modified_by.equals(""))) {
											%>
											<div class="form-element6">
													<label>Modified By: <%=mybean.unescapehtml(mybean.location_modified_by)%></label>
														<input type="hidden" name="location_modified_by"
															value="<%=mybean.location_modified_by%>">
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-element6">
													<label>Modified Date: <%=mybean.modified_date%></label>
														<input type="hidden" name="modified_date"
															value="<%=mybean.modified_date%>">
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Add")) {
											%>
											<center>
												<input name="button2" type="submit" class="btn btn-success"
													id="button2" value="Add Location" /> <input type="hidden"
													name="add_button" value="Add Location" />
											</center>
											<%
												} else if (mybean.status.equals("Update")) {
											%>
											<center>
												<input type="hidden" name="update_button"
													value="Update Location"> <input name="button"
													type="submit" class="btn btn-success" id="button"
													value="Update Location" /> <input name="delete_button"
													type="submit" class="btn btn-success" id="delete_button"
													onClick="return confirmdelete(this)"
													value="Delete Location" />
											</center>
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
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
		document.form1.txt_location_name.focus()
		}
        </script>

</body>
</HTML>
