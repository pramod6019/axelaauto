<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.app.ShowRoom_Update"
	scope="request" />
<%mybean.doPost(request, response);%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.ClientName%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</HEAD>

<body onLoad="FormFocus();" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%>&nbsp;Showroom
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
						<li><a href="../home.jsp">Home</a> &gt;</li>
						<li>Showroom&gt;</li>
						<li><a href="showroom-list.jsp?all=yes">List Showrooms</a> &gt;</li>
						<li><a href="showroom-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Showroom</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Showroom
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" id="form1" method="post" class="form-horizontal">
										
											<div class="form-element6">
												<label>Name<font color="#ff0000">*</font>: </label>
												<input name="txt_showroom_name" type="text" class="form-control"
													id="txt_showroom_name" value="<%=mybean.showroom_name%>" />
													<font color="#ff0000"><span id="txtHint"></span></font>
											</div>
											<div class="row">
												<div class="form-element3">
													<label>Phone 1<font color="#ff0000">*</font>: </label>
													<input name="txt_showroom_phone1" type="text" class="form-control"
														id="txt_showroom_phone1" onKeyUp="toPhone('txt_showroom_phone1','Phone1')"
														value="<%=mybean.showroom_phone1%>" size="20" maxlength="12" />(080-33333333)
												</div>
	
												<div class="form-element3">
													<label>Phone 2: </label>
													<input name="txt_showroom_phone2" type="text" class="form-control"
														id="txt_showroom_phone2" onKeyUp="toPhone('txt_showroom_phone2','Phone2')"
														value="<%=mybean.showroom_phone2%>" size="20" maxlength="12" />(080-33333333)
												</div>
											</div>

											<div class="form-element6">
												<label>Mobile 1<font color="#ff0000">*</font>: </label>
												<input name="txt_showroom_mobile1" type="text" class="form-control"
													id="txt_showroom_mobile1" onKeyUp="toPhone('txt_showroom_mobile1','Mobile1')"
													value="<%=mybean.showroom_mobile1%>" size="20" maxlength="10" /> (9999999999)
											</div>

											<div class="form-element6">
												<label>Mobile 2: </label>
												<input name="txt_showroom_mobile2" type="text" class="form-control"
													id="txt_showroom_mobile2" onKeyUp="toPhone('txt_showroom_mobile2','Mobile2') "
													value="<%=mybean.showroom_mobile2%>" size="20" maxlength="10">(9999999999)
											</div>
											
											<div class="form-element6">
												<label>Email 1<font color="#ff0000">*</font>: </label>
												<input name="txt_showroom_email1" type="text"
													class="form-control" id="txt_showroom_email1"
													value="<%=mybean.showroom_email1%>" size="32" maxlength="255">
											</div>
											
											<div class="form-element6">
												<label>Email 2: </label>
												<input name="txt_showroom_email2" type="text" class="form-control"
													id="txt_showroom_email2" value="<%=mybean.showroom_email2%>" size="32"
													maxlength="255" />
											</div>

											<div class="form-element6">
												<label>Website 1<font color="#ff0000">*</font>: </label>
												<input name="txt_showroom_website1" type="text" class="form-control"
													id="txt_showroom_website1" value="<%=mybean.showroom_website1%>" size="32"
													maxlength="255">
											</div>

											<div class="form-element6">
												<label>Website 2: </label>
												<input name="txt_showroom_website2" type="text" class="form-control"
 													id="txt_showroom_website2" value="<%=mybean.showroom_website2%>" size="32"
													maxlength="255">
											</div>

											<div class="row">
												<div class="form-element6">
													<label>Address<font color="#ff0000">*</font>: </label>
													<textarea name="txt_showroom_address" cols="40" rows="4"
														class="form-control" id="txt_showroom_address" maxlength="255"
														onKeyUp="charcount('txt_showroom_address', 'span_txt_showroom_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.showroom_address%></textarea>
														<span id="span_txt_showroom_address">(255 Characters)</span>
												</div>
	
												<div class="form-element6">
													<label>City<font color="#ff0000">*</font>: </label>
													<div>
														<select class="form-control-select" id="maincity" name="maincity">
															<%=mybean.citycheck.PopulateCities(mybean.showroom_city_id, mybean.comp_id)%>
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
													<input name="txt_showroom_pin" type="text" class="form-control"
														id="txt_showroom_pin" onKeyUp="toInteger('txt_showroom_pin','Pin')"
														value="<%=mybean.showroom_pin%>" size="10" maxlength="6" />
												</div>
											</div>

											<div class="form-element6">
												<label>Latitude<font color="#ff0000">*</font>: </label>
												<input name="txt_showroom_latitude" type="text"
													class="form-control" id="txt_showroom_latitude"
													onKeyUp="toFloat('txt_showroom_latitude','Latitude')"
													value="<%=mybean.showroom_latitude%>" size="15" maxlength="15" />
											</div>


											<div class="form-element6">
												<label>Longitute<font color="#ff0000">*</font>: </label>
												<input name="txt_showroom_longitude" type="text" class="form-control"
													id="txt_showroom_longitude" onKeyUp="toFloat('txt_showroom_longitude','Longitute')"
													value="<%=mybean.showroom_longitude%>" size="15" maxlength="15" />
											</div>

											<div class="form-element12">
												<label> Active: </label>
												<input id="chk_showroom_active" type="checkbox" name="chk_showroom_active"
													<%=mybean.PopulateCheck(mybean.showroom_active)%> />
											</div>

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element3">
												<label>Entry by: &nbsp;</label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.entry_by%>" />
											</div>
											<div class="form-element3">
												<label>Entry date:&nbsp;</label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>" />
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
											%>
											
											<div class="form-element3">
												<label>Modified by:&nbsp;</label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>" />
											</div>

											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
											%>
											
											<div class="form-element3">
												<label>Modified Date:&nbsp;</label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>" />
											</div>
											<%
												}
											%>
											<div class="form-element12">
												<center>
													<%
														if (mybean.status.equals("Add")) {
													%>
													<input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Showroom"
														onclick="onPress();return SubmitFormOnce(document.formemp,this);" />
													<input type="hidden" name="add_button" value="yes" />
													<%
														} else if (mybean.status.equals("Update")) {
													%>
													<input type="hidden" name="update_button" value="yes" />
													<input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton"
														onclick="onPress();return SubmitFormOnce(document.formemp,this);" value="Update Showroom" />
													<input name="delete_button" type="submit" class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)" value="Delete Showroom" />
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
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	
	<%@include file="../Library/js.jsp"%>
	
	<script type="text/javascript">
		function FormFocus() { //v1.0
			document.formcontact.txt_customer_name.focus();
		}
	</script>
</body>
</HTML>