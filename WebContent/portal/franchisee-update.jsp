<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Franchisee_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html  PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>

<%@include file="../Library/css.jsp"%>

</head>
<body onload="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>
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
						<h1>Franchisee Update</h1>
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
							<li><a href="home.jsp">Home</a> &gt;</li>
							<li><a href="manager.jsp">Business Master</a> &gt;</li>
							<li><a href="franchisee-list.jsp?all=yes">List Franchisees</a> &gt;</li>
							<li><a href="franchisee-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
									Franchisee</a>:</li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->


						<div class="tab-pane" id="">

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>
										Franchisee
									</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font size="1" color="#ff0000">*</font></b> are required.
											</font>
										</center>

										<br/>
										<form name="form1" method="post" class="form-horizontal">

											<div class="form-element6">
												<label>Franchisee <font color="red">*</font>: </label>
												<input name="txt_franchisee_name" type="text"
													class="form-control" value="<%=mybean.franchisee_name%>"
													maxlength="255" /> (Enter Minimum 3 characters)
											</div>

											<div class="form-element6">
												<label>Contact Person :</label> 
												<input name="txt_franchisee_contact" type="text"
													class="form-control" id="txt_franchisee_contact"
													maxlength="255" value="<%=mybean.franchisee_contact%>"/>(Enter Minimum of 3 characters)
											</div>

											<div class="form-element6">
												<label> Mobile 1<font color="#ff0000">*</font>: </label>
												<input name="txt_franchisee_mobile1" type="text"
													class="form-control" id="txt_franchisee_mobile1"
													value="<%=mybean.franchisee_mobile1%>" maxlength="13"
													onkeyup="toPhone('txt_franchisee_mobile1','Mobile 1')" />
												(91-9999999999)
											</div>

											<div class="form-element6">
												<label> Mobile 2 :</label> 
												<input name="txt_franchisee_mobile2" type="text"
													class="form-control" id="txt_franchisee_mobile2"
													value="<%=mybean.franchisee_mobile2%>" maxlength="13"
													onkeyup="toPhone('txt_franchisee_mobile2','Mobile 2')" />
												(91-9999999999)
											</div>

											<div class="form-element6">
												<label> Phone 1<font color="#ff0000">*</font>: </label>
												<input name="txt_franchisee_phone1" type="text"
													class="form-control" id="txt_franchisee_phone1"
													value="<%=mybean.franchisee_phone1%>" maxlength="14"
													onkeyup="toPhone('txt_franchisee_phone1','Phone 1')" />
												(91-80-33333333)
											</div>

											<div class="form-element6">
												<label> Phone 2 :</label> 
												<input name="txt_franchisee_phone2" type="text"
													class="form-control" id="txt_franchisee_phone2"
													value="<%=mybean.franchisee_phone2%>" maxlength="14"
													onkeyup="toPhone('txt_franchisee_phone2','Phone 2')" />
												(91-80-33333333)
											</div>

											<div class="form-element6">
												<label> Email 1<font color="#ff0000">*</font>: </label>
												<input name="txt_franchisee_email1" type="text"
													class="form-control" value="<%=mybean.franchisee_email1%>"
													maxlength="100" />
											</div>

											<div class="form-element6">
												<label> Email 2 :</label>
												 <input name="txt_franchisee_email2" type="text"
													class="form-control" value="<%=mybean.franchisee_email2%>"
													maxlength="100" />
											</div>

											<div class="form-element6">
												<label> URL :</label> 
												<input name="txt_franchisee_url"
													type="text" class="form-control"
													value="<%=mybean.franchisee_url%>" maxlength="100" />
											</div>


											<div class="form-element6">
												<label>City <font color=red>*</font>: </label>
												<select class="form-control " id="maincity" name="maincity">
													<%=mybean.citycheck.PopulateCities(mybean.franchisee_city_id, mybean.comp_id)%>
												</select> 
												<input tabindex="-1" class="bigdrop select2-offscreen"
													id="maincity" name="maincity" style="width: 250px"
													value="<%=mybean.franchisee_city_id%>" type="hidden"/>
											</div>

											<div class="form-element6">
												<label>Pin :</label> 
												<input name="txt_franchisee_pin"
													type="text" class="form-control" id="txt_franchisee_pin"
													onkeyup="toInteger('txt_franchisee_pin','Pin')"
													value="<%=mybean.franchisee_pin%>" maxlength="6" />
											</div>


											<div class="form-element6">
												<label>Franchisee Type <font color=red>*</font>: </label>
												 <select name="drop_franchisee_franchiseetype_id"
													class="form-control"><%=mybean.PopulateFranchiseeType()%>
												</select>
											</div>




											<div class="form-element6">
												<label>Address <font color=red>*</font>: </label>
												<textarea name="txt_franchisee_add" cols="55" rows="4"
													class="form-control" id="txt_franchisee_add"
													onkeyup="charcount('txt_franchisee_add', 'span_txt_franchisee_add','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.franchisee_add%></textarea>
												<span id="span_txt_franchisee_add"> (255 Characters)</span>
											</div>

											<div class="form-element6">
												<label>Notes :</label>
												<textarea name="txt_franchisee_notes" cols="70" rows="4"
													class="form-control" id="textarea3"><%=mybean.franchisee_notes%></textarea>
											</div>

											<div class="form-element6 col-md-pull-6">
												<br> 
												<label>Active :</label> 
												<input type="checkbox"
													name="ch_franchisee_active"
													<%=mybean.PopulateCheck(mybean.franchisee_active)%> />
											</div>

											<% if (mybean.status.equals("Update") && !(mybean.franchisee_entry_by == null)
														&& !(mybean.franchisee_entry_by.equals(""))) {
											%>
											
											<div class="form-element6">
												<label> Entry By :</label>
												<%=mybean.unescapehtml(mybean.franchisee_entry_by)%>
												<input type="hidden" name="franchisee_entry_by"
													value="<%=mybean.franchisee_entry_by%>">
											</div>

											<% } %>

											<% if (mybean.status.equals("Update") && !(mybean.entry_date == null)
													&& !(mybean.entry_date.equals(""))) {
											%>

											<div class="form-element6">
												<label> Entry Date :</label>
												<%=mybean.entry_date%>
												<input type="hidden" name="entry_date"
													value="<%=mybean.entry_date%>">
											</div>

											<% } %>

											<% if (mybean.status.equals("Update") && !(mybean.franchisee_modified_by == null)
														&& !(mybean.franchisee_modified_by.equals(""))) {
											%>

											<div class="form-element6">
												<label> Modified By :</label>
												<%=mybean.unescapehtml(mybean.franchisee_modified_by)%>
												<input type="hidden" name="franchisee_modified_by"
													value="<%=mybean.franchisee_modified_by%>">
											</div>

											<% } %>

											<% if (mybean.status.equals("Update") && !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>

											<div class="form-element6">
												<label> Modified Date :</label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date"
													value="<%=mybean.modified_date%>">
											</div>

											<% } %>

											
											 <% if (mybean.status.equals("Add")) { %>
											<center>
												<input name="button" type="submit" class="btn btn-success"
													id="button" value="Add Franchisee"
													onclick="return SubmitFormOnce(document.form1, this);" />
													
												<input type="hidden" name="add_button" value="yes" />
											</center>
												<% } %> 
											<% if (mybean.status.equals("Update")) { %>
												<center>
												<input type="hidden" name="update_button" value="yes" />
												<input name="button" type="submit" class="btn btn-success"
													id="button" value="Update Franchisee"
													onclick="return SubmitFormOnce(document.form1, this);" />
												<input name="delete_button" type="submit"
													class="btn btn-success" id="delete_button"
													onclick="return confirmdelete(this)"
													value="Delete Franchisee" />
											     <input type="hidden" name="Update" value="yes" />
											     </center>
											<% } %>
										
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

	<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_franchisee_name.focus()
	}
</script>
</body>
</HTML>
