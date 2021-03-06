
<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.service.ManageCourtesyVehicle_Update" scope="request" />
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body onLoad="FormFocus()">
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
						<h1><%=mybean.status%>&nbsp;Pickup
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
							<li><a href="../portal/manager.jsp">Business Manager</a>
								&gt;</li>
							<li><a href="managecourtesyvehicle.jsp?all=yes">List
									Courtesy Vehicless</a>&gt;</li>
							<li><a
								href="managecourtesyvehicle-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
									Courtesy Vehicle</a><b>:</b></li>
						</ul>
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center>
											<%=mybean.status%>
											Courtesy Vehicle
										</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<b><font color="#ff0000">*</font></b> are required.
												</font>
											</center>
											<br />
											<div class="form-element4">
												<label>Branch<b><font color="#ff0000">*</font></b>:
												</label> <select name="dr_branch" class="form-control"
													id="dr_branch">
													<%=mybean.PopulateBranch(mybean.courtesyveh_branch_id,"", "1,3", "", request)%>
												</select>
											</div>
											<div class="form-element4">
												<label>Name<b><font color="#ff0000">*</font></b>:
												</label> <input name="txt_courtesyveh_name" type="text"
													class="form-control" id="txt_veh_name"
													value="<%=mybean.courtesyveh_name %>" size="50"
													maxlength="255" />
											</div>
											<div class="form-element4">
												<label>Registration No.:</label> <input
													name="txt_courtesyveh_regno" type="text"
													class="form-control" id="txt_veh_regno"
													value="<%=mybean.courtesyveh_regno %>" size="20"
													maxlength="20" />
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label>Service Start Date:</label> <input
													name="txt_courtesyveh_service_start_date" type="text"
													class="form-control datepicker"
													id="txt_veh_service_start_date"
													value="<%=mybean.courtesyveh_service_start_date%>"
													size="12" maxlength="10" />
											</div>
											<div class="form-element6">
												<label>Service End Date:</label> <input
													name="txt_courtesyveh_service_end_date" type="text"
													class="form-control datepicker"
													id="txt_veh_service_end_date"
													value="<%=mybean.courtesyveh_service_end_date%>" size="12"
													maxlength="10" />
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label>Notes:</label>
												<textarea name="txt_courtesyveh_notes" cols="50" rows="4"
													class="form-control" id="txt_courtesyveh_notes"><%=mybean.courtesyveh_notes%></textarea>
											</div>
											<div class="form-element6">
												<label>Active:</label> <input type="checkbox"
													name="chk_courtesyveh_active" id="chk_courtesyveh_active"
													<%=mybean.PopulateCheck(mybean.courtesyveh_active)%>>
											</div>
											<div class="row"></div>
											<% if (mybean.status.equals("Update") &&!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
											<div class="form-element6">
												<label>Entry By:</label>
												<%=mybean.unescapehtml(mybean.entry_by)%>
												<input name="entry_by" type="hidden" id="entry_by"
													value="<%=mybean.entry_by%>">
											</div>
											<div class="form-element6">
												<label>Entry Date:</label>
												<%=mybean.courtesyveh_entry_date%>
												<input type="hidden" name="entry_date"
													value="<%=mybean.courtesyveh_entry_date%>">
											</div>
											<%}%>
											<% if (mybean.status.equals("Update") &&!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
											<div class="form-element6">
												<label>Modified By:</label>
												<%=mybean.unescapehtml(mybean.modified_by)%>
												<input name="modified_by" type="hidden" id="modified_by"
													value="<%=mybean.modified_by%>">
											</div>
											<div class="form-element6">
												<label>Modified Date:</label>
												<%=mybean.courtesyveh_modified_date%>
												<input type="hidden" name="modified_date"
													value="<%=mybean.courtesyveh_modified_date%>">
											</div>
											<%}%>
											<center>
												<%if(mybean.status.equals("Add")){%>
												<input name="addbutton" type="submit"
													class="btn btn-success" id="addbutton" value="Add Vehicle"
													onclick="return SubmitFormOnce(document.form1,this);" /> <input
													type="hidden" name="add_button" value="yes">
												<%}else if (mybean.status.equals("Update")){%>
												<input name="update_button" type="hidden" value="yes" /> <input
													name="update_button" type="submit" class="btn btn-success"
													id="update_button" value="Update Vehicle"
													onclick="return SubmitFormOnce(document.form1,this);" /> <input
													name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" OnClick="return confirmdelete(this)"
													value="Delete Vehicle" />
												<%}%>
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
	<script language="JavaScript" type="text/javascript">
            function FormFocus() {
                document.form1.txt_veh_name.focus();
            }
        </script>
</body>
</HTML>