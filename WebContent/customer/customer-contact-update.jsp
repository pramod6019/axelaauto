<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.customer.Customer_Contact_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp" %>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.formcontact.txt_contact_fname.focus();
	}
</script>
 
</HEAD>
<body onLoad="FormFocus();"
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
						<h1><%=mybean.status%> Contact Person </h1>
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
						<li><a href="customer.jsp">Customer</a> &gt;</li>
						<li><a href="customer-contact.jsp">Contacts</a> &gt;</li>
						<li><a href="customer-contact-list.jsp?all=recent">List Contact Persons</a> &gt;</li>
						<li><a href="customer-contact-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Contact Person</a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%> Contact Person
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="formcontact" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br>
                                                                </font><br>
											</center>

											<%
												if (mybean.customer_id.equals("0")) {
											%>
											<div class="form-element6">
												<label>Customer<font color=red>*</font>: </label>
												<input name="txt_customer_name" type="text" class="form-control" maxlength="255"
													 id="txt_customer_name" value="<%=mybean.customer_name%>" size="50" />
											</div>
											<%
												} else {
											%>
											<div class="form-element6">
												<label >Customer:</label>
													<b><a href="customer-list.jsp?customer_id=<%=mybean.customer_id%>" target='_blank'><%=mybean.customer_name%></a></b>
											</div>
											<%
												}
											%>

											<div class="form-element3">
												<label> Type<font color=red>*</font>: </label>
													<select id="dr_contact_contacttype_id"
														name="dr_contact_contacttype_id" class="form-control">
														<%=mybean.PopulateContactType()%>
													</select>
											</div>
											<%
												if (mybean.customer_id.equals("0")) {
											%>
											<div class="form-element3">
												<label> Branch : </label>
													<select name="dr_customer_branch_id" class="form-control"
														id="dr_customer_branch_id" onChange="Displaypaymode();">
														<%=mybean.PopulateBranch(mybean.customer_branch_id, "", "", "", request)%>
													</select>
											</div>
											<%
												}
											%>
											<div class="form-element6">
											<div class="form-element2 form-element">
												<label> Contact Name<font color=red>*</font>: </label>
														<select name="dr_title" class="form-control"
																id="dr_title">
																	<%=mybean.PopulateTitle()%>
															</select> Title
															</div>
															<div class="form-element5 form-element">
															<label>&nbsp;</label>
															<input name="txt_contact_fname" type="text"
																class="form-control" id="txt_contact_fname"
																value="<%=mybean.contact_fname%>" size="35"
																maxlength="255" />First Name
																</div>
															<div class="form-element5 form-element"><label>&nbsp;</label>
															<input name="txt_contact_lname" type="text"
																class="form-control" id="txt_contact_lname"
																value="<%=mybean.contact_lname%>" size="35"
																maxlength="255" />Last Name
																</div>
																</div>
																
										<div class="form-element3">
												<label> Job Title: </label>
													<input name="txt_contact_jobtitle" type="text"
														class="form-control" id="txt_contact_jobtitle"
														value="<%=mybean.contact_jobtitle%>" size="35"
														maxlength="255">
											</div>
											<div class="form-element3">
												<label> Location: </label>
													<input name="txt_contact_location" type="text"
														class="form-control" id="txt_contact_location"
														value="<%=mybean.contact_location%>" size="35"
														maxlength="255">
											</div> 
											<div class="row"></div>
											<div class="form-element3">
												<label> Mobile 1<font color=red>*</font>: </label>
													<input name="txt_contact_mobile1" type="text"
														class="form-control" id="txt_contact_mobile1"
														value="<%=mybean.contact_mobile1%>" size="32"
														maxlength="13" 
														onKeyUp="toPhone('txt_contact_mobile1','Mobile 1')"/> (91-9999999999)
											</div>
											<div class="form-element3">
												<label> Mobile 2: </label>
													<input name="txt_contact_mobile2" type="text"
														class="form-control" id="txt_contact_mobile2"
														value="<%=mybean.contact_mobile2%>" size="32"
														maxlength="13" 
														onKeyUp="toPhone('txt_contact_mobile2','Mobile 2')"/> (91-9999999999)
											</div>
											<div class="form-element3">
												<label> Mobile 3: </label>
													<input name="txt_contact_mobile3" type="text"
														class="form-control" id="txt_contact_mobile3"
														value="<%=mybean.contact_mobile3%>" size="32"
														maxlength="13" 
														onKeyUp="toPhone('txt_contact_mobile3','Mobile 3')"/> (91-9999999999)
											</div>
											<div class="form-element3">
												<label> Mobile 4: </label>
													<input name="txt_contact_mobile4" type="text"
														class="form-control" id="txt_contact_mobile4"
														value="<%=mybean.contact_mobile4%>" size="32"
														maxlength="13" 
														onKeyUp="toPhone('txt_contact_mobile4','Mobile 4')"/> (91-9999999999)
											</div>
											<div class="form-element3">
												<label> Mobile 5: </label>
													<input name="txt_contact_mobile5" type="text"
														class="form-control" id="txt_contact_mobile5"
														value="<%=mybean.contact_mobile5%>" size="32"
														maxlength="13" 
														onKeyUp="toPhone('txt_contact_mobile5','Mobile 5')"/> (91-9999999999)
											</div>
										<div class="form-element3">
												<label> Mobile 6: </label>
													<input name="txt_contact_mobile6" type="text"
														class="form-control" id="txt_contact_mobile6"
														value="<%=mybean.contact_mobile6%>" size="32"
														maxlength="13" 
														onKeyUp="toPhone('txt_contact_mobile6','Mobile 6')"/> (91-9999999999)
											</div>
											<div class="form-element3">
												<label> Phone 1: </label>
													<input name="txt_contact_phone1" type="text"
														class="form-control" id="txt_contact_phone1"
														value="<%=mybean.contact_phone1%>" size="35"
														maxlength="15"
														onKeyUp="toPhone('txt_contact_phone1','Phone 1')"
														/> (91-80-33333333)
											</div>
										<div class="form-element3">
												<label> Phone 2: </label>
													<input name="txt_contact_phone2" type="text"
														class="form-control" id="txt_contact_phone2"
														value="<%=mybean.contact_phone2%>" size="35"
														maxlength="15"
														onKeyUp="toPhone('txt_contact_phone2','Phone 2')"
														/> (91-80-33333333)
											</div>
											<div class="form-element3">
												<label> Email 1: </label>
													<input name="txt_contact_email1" type="text"
														class="form-control" id="txt_contact_email1"
														value="<%=mybean.contact_email1%>" size="35"
														maxlength="255">
											</div>
											<div class="form-element3">
												<label> Email 2: </label>
													<input name="txt_contact_email2" type="text"
														class="form-control" id="txt_contact_email2"
														value="<%=mybean.contact_email2%>" size="35"
														maxlength="255">
											</div>
											
											<div class="form-element3">
												<label> Yahoo: </label>
													<input name="txt_contact_yahoo" type="text"
														class="form-control" id="txt_contact_yahoo"
														value="<%=mybean.contact_yahoo%>" size="35"
														maxlength="255">
											</div>
								                        			<div class="form-element3">
												<label> MSN: </label>
													<input name="txt_contact_msn" type="text"
														class="form-control" id="txt_contact_msn"
														value="<%=mybean.contact_msn%>" size="35" maxlength="255">
											</div>
										<div class="form-element6">
												<label> AOL: </label>
													<input name="txt_contact_aol" type="text"
														class="form-control" id="txt_contact_aol"
														value="<%=mybean.contact_aol%>" size="35" maxlength="255">
											</div>
											<div class="form-element6">
												<label> Skype: </label>
													<input name="txt_contact_skype" type="text"
														class="form-control" id="txt_contact_skype"
														value="<%=mybean.contact_skype%>" size="35"
														maxlength="255">
											</div>
											<div class="form-element6">
												<label> Address<font color=red>*</font>: </label>
													<textarea name="txt_contact_address" cols="40" rows="4"
														class="form-control" id="txt_contact_address"
														onKeyUp="charcount('txt_contact_address', 'span_txt_contact_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.contact_address%></textarea>
													<span id="span_txt_contact_address"> (255
														Characters)</span>
											</div>
										<div class="form-element6">
												<label> Landmark: </label>
													<textarea name="txt_contact_landmark" cols="40" rows="4"
														class="form-control" id="txt_contact_landmark"
														onKeyUp="charcount('txt_contact_landmark', 'span_txt_contact_landmark','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.contact_landmark%></textarea>
													<span id="span_txt_contact_landmark"> (255
														Characters)</span>
											</div>
											<div class="form-element6">
												<label> City<font color=red>*</font>: </label>
													<select class="form-control select2" id="maincity"
														name="maincity">
														<%=mybean.citycheck.PopulateCities(mybean.contact_city_id, mybean.comp_id)%>
													</select>
											</div>
										<div class="form-element3">
												<label> Pin/Zip<font color=red>*</font>: </label>
													<input name="txt_contact_pin" type="text"
														class="form-control" id="txt_contact_pin"
														onKeyUp="toInteger('txt_contact_pin','Pin')"
														value="<%=mybean.contact_pin%>" size="10" maxlength="6" />

											</div>
											<div class="form-element3">
												<label> PAN No.: </label>
													<input name="txt_customer_pan_no" type="text"
														class="form-control" id="txt_customer_pan_no"
														value="<%=mybean.customer_pan_no%>" size="20"
														maxlength="10">
											</div>
											<%
												if (mybean.customer_id.equals("0")) {
											%>
											<div class="form-element6">
												<label> Customer Since<font color=red>*</font>: </label>
													<input name="txt_customer_since" id="txt_customer_since"
														value="<%=mybean.customersince%>" size="12" maxlength="10"
														class="form-control datepicker"
														type="text" />
											</div>
											<div class="form-element6">
												<label> Customer Type<font color=red>*</font>: </label>
													<select name="dr_customer_type" class="form-control"
														id="dr_customer_type">
														<%=mybean.PopulateType()%>
													</select>
											</div>
											<%
												}
											%>
											<div class="form-element3">
													<label>DOB: </label> 
															<select name="drop_bday" class="form-control"
																id="drop_bday">
																<%=mybean.PopulateDay()%>
															</select>
											</div>
											<div class="form-element3">
											<label>&nbsp;</label>
															<select name="drop_bmonth" class="form-control"
																id="drop_bmonth">
																<%=mybean.PopulateMonth()%>
															</select>
											</div>
											<div class="form-element3">
													<label>Anniversary: </label>
															<select name="drop_aday" class="form-control"
																id="drop_aday">
																<%=mybean.PopulateDays()%>
															</select>
											</div>
											<div class="form-element3">
											<label>&nbsp;</label>
															<select name="drop_amonth" class="form-control"
																id="drop_amonth">
																<%=mybean.PopulateMonths()%>
															</select>
											</div>

											

											<div class="form-element6">
												<label> Notes: </label>
													<textarea name="txt_contact_notes" cols="70" rows="4"
														class="form-control" id="txt_contact_notes"><%=mybean.contact_notes%></textarea>

											</div>
											<div class="form-element6 form-element-margin">
												<label>Active:</label>
													<input id="chk_contact_active2" type="checkbox"
														name="chk_contact_active"
														<%=mybean.PopulateCheck(mybean.contact_active)%> />
											</div>
											<div class="row"></div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6">
												<label> Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.entry_by%>">
											</div>
											<div class="form-element6">
												<label>Entry Date: <%=mybean.entry_date%></label>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>">
											</div>
											<% } %>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label> Modified By: <%=mybean.unescapehtml(mybean.modified_by)%></label>
													<input name="modified_by" type="hidden" id="modified_by"
												</div>
											</div>
											<div class="form-element6">
												<label> Modified Date: <%=mybean.modified_date%></label>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
											</div>
											<%
												}
											%>
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												<input name="addbutton" type="submit"
													class="btn btn-success" id="addbutton"
													value="Add Contact Person"
													onClick="return SubmitFormOnce(document.formcontact, this);" />
												<input type="hidden" name="add_button" value="yes">
												<%
													} else if (mybean.status.equals("Update")) {
												%>
												<input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													onClick="return SubmitFormOnce(document.formcontact, this);"
													value="Update Contact Person" /> <input type="hidden"
													name="update_button" value="yes"> <input
													name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" OnClick="return confirmdelete(this)"
													value="Delete Contact Person" />
												<%
													}
												%>
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
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp" %>
</body>
</HTML>
