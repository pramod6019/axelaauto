<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.app.ServiceCentre_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.ClientName%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>

<%@ include file="../Library/css.jsp" %>
 
</HEAD>

<body onLoad="FormFocus();" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY ----->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>
							<%=mybean.status%>&nbsp;Service Centre
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
						<li>Service Centre &gt;</li>
						<li><a href="servicecentre-list.jsp?all=yes">List Service Centres</a> &gt;</li>
						<li><a href="servicecentre-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Service Centre</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Service Centre
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal ">
											<center>
												<font>
													Form fields marked with a red asterisk <b>
													<font color="#ff0000">*</font></b> are required.
											</center>
											<!-- START PORTLET BODY -->

											<div class="form-element6">
												<label>Name<font color="#ff0000">*</font>: </label>
												<input name="txt_servicecenter_name" type="text"
													class="form-control" id="txt_servicecenter_name"
													value="<%=mybean.servicecenter_name%>" size="32"
													maxlength="255" /><font color="#ff0000">
												<span id="txtHint"></span></font>
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label>Phone 1<font color="#ff0000">*</font>: </label>
												<input name="txt_servicecenter_phone1" type="text"
													class="form-control" id="txt_servicecenter_phone1"
													onKeyUp="toPhone('txt_servicecenter_phone1','Phone1')"
													value="<%=mybean.servicecenter_phone1%>" size="20"
													maxlength="12" /> (080-33333333)
											</div>
											
											<div class="form-element6">
												<label>Phone 2:</label>
												<input name="txt_servicecenter_phone2" type="text"
													class="form-control" id="txt_servicecenter_phone2"
													onKeyUp="toPhone('txt_servicecenter_phone2','Phone2')"
													value="<%=mybean.servicecenter_phone2%>" size="20"
													maxlength="12" /> (080-33333333)
											</div>
											
											<div class="form-element6">
												<label>Mobile 1<font color="#ff0000">*</font>: </label>
												<input name="txt_servicecenter_mobile1" type="text"
													class="form-control" id="txt_servicecenter_mobile1"
													onKeyUp="toPhone('txt_servicecenter_mobile1','Mobile1')"
													value="<%=mybean.servicecenter_mobile1%>" size="20"
													maxlength="10" /> (9999999999)
											</div>
											
											<div class="form-element6">
												<label>Mobile 2:</label>
												<input name="txt_servicecenter_mobile2" type="text"
													class="form-control" id="txt_servicecenter_mobile2"
													onKeyUp="toPhone('txt_servicecenter_mobile2','Mobile2')"
													value="<%=mybean.servicecenter_mobile2%>" size="20"
													maxlength="10" /> (9999999999)
											</div>

											<div class="form-element6">
												<label>Email-1<font color="#ff0000">*</font>: </label>
												<input name="txt_servicecenter_email1" type="text"
													class="form-control" id="txt_servicecenter_email1"
													value="<%=mybean.servicecenter_email1%>" size="32"
													maxlength="255" />
											</div>
											
											<div class="form-element6">
												<label>Email-2:</label>
												<input name="txt_servicecenter_email2" type="text"
													class="form-control" id="txt_servicecenter_email2"
													value="<%=mybean.servicecenter_email2%>" size="32"
													maxlength="255" />
											</div>
											
											<div class="form-element6">
												<label>Website 1<font color="#ff0000">*</font>: </label>
												<input name="txt_servicecenter_website1" type="text"
													class="form-control" id="txt_servicecenter_website1"
													value="<%=mybean.servicecenter_website1%>" size="32"
													maxlength="255" />
											</div>

											<div class="form-element6">
												<label>Website 2:</label>
												<input name="txt_servicecenter_website2" type="text"
													class="form-control" id="txt_servicecenter_website2"
													value="<%=mybean.servicecenter_website2%>" size="32"
													maxlength="255" />
											</div>
											<div class="row">
												<div class="form-element6">
													<label>Address<font color="#ff0000">*</font>: </label>
													<textarea name="txt_servicecenter_address" cols="50"
														rows="4" class="form-control" id="txt_servicecenter_address" maxlength="255"
														onKeyUp="charcount('txt_servicecenter_address', 'span_txt_servicecenter_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.servicecenter_address%></textarea>
													<span id="span_txt_servicecenter_address">(255 Characters)</span>
												</div>
												
												<div class="form-element6">
													<label>City<font color="#ff0000">*</font>: </label>
													<div>
														<select class="form-control-select select2" id="maincity" name="maincity">
															<%=mybean.citycheck.PopulateCities(mybean.servicecenter_city_id, mybean.comp_id)%>
														</select>
														<%
															if (mybean.emp_role_id.equals("1")) {
														%>
														<div class="admin-master">
															<a href="../portal/managecity.jsp?all=yes" title="Manage City"></a>
														</div>
														<%
															}
														%>
													</div>
												</div>
												
												<div class="form-element6">
													<label>Pin<font color="#ff0000">*</font>: </label>
													<input name="txt_servicecenter_pin" type="text"
														class="form-control" id="txt_servicecenter_pin"
														onKeyUp="toInteger('txt_servicecenter_pin','Pin')"
														value="<%=mybean.servicecenter_pin%>" size="10"
														maxlength="6" />
												</div>
											</div>

											<div class="form-element6">
												<label>Latitude<font color="#ff0000">*</font>: </label>
												<input name="txt_servicecenter_latitude" type="text"
													class="form-control" id="txt_servicecenter_latitude"
													onKeyUp="toFloat('txt_showroom_latitude','Latitude')"
													value="<%=mybean.servicecenter_latitude%>" size="15"
													maxlength="15">
											</div>
											
											<div class="form-element6">
												<label>Longitute<font color="#ff0000">*</font>: </label>
												<input name="txt_servicecenter_longitude" type="text"
													class="form-control" id="txt_servicecenter_longitude"
													onKeyUp="toFloat('txt_showroom_longitude','Longitute')"
													value="<%=mybean.servicecenter_longitude%>" size="15"
													maxlength="15">
											</div>
											
											<div class="form-element12">
												<label>Active:</label>
												<input id="chk_servicecenter_active" type="checkbox" name="chk_servicecenter_active"
													<%=mybean.PopulateCheck(mybean.servicecenter_active)%> />
											</div>

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element3">
												<label>Entry by:</label>
												<%=mybean.unescapehtml(mybean.entry_by)%>
												<input name="entry_by" type="hidden" id="entry_by"
													value="<%=mybean.entry_by%>">
											</div>
											<div class="form-element3">
												<label>Entry Date:</label>
												<%=mybean.entry_date%>
												<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>" />
											</div>


											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element3">
												<label>Modified by:</label>
												<%=mybean.unescapehtml(mybean.modified_by)%>
												<input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>">
											</div>
											<div class="form-element3">
												<label>Modified date:</label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>">
											</div>

											<%
												}
											%>
											<div class="form-element12">
												<center>
													<%
														if (mybean.status.equals("Add")) {
													%>
													<input name="addbutton" type="submit" class="btn btn-success" id="addbutton"
														value="Add Service Centre"
														onClick="onPress();return SubmitFormOnce(document.form1,this);" />
													<input type="hidden" name="add_button" value="yes" />
													<%
														} else if (mybean.status.equals("Update")) {
													%>
													<input type="hidden" name="update_button" value="yes">
													<input name="updatebutton" type="submit"
														class="btn btn-success" id="updatebutton"
														value="Update Service Centre"
														onClick="onPress();return SubmitFormOnce(document.form1,this);" />
													<input name="delete_button" type="submit"
														class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)"
														value="Delete Service Centre" />
													<%
														}
													%>
												</center>
											</div>
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

	<%@ include file="../Library/admin-footer.jsp"%>
	
	<%@ include file="../Library/js.jsp" %>

	<script type="text/javascript">
		function FormFocus() {
			document.form1.txt_servicecenter_name.focus();
		}
	</script>
</body>
</HTML>
