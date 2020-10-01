<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inbound.Call" scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />

<style>
#tags {
	margin-top: 5px;
	margin-bottom: 5px;
	padding-top: 0px;
	padding-bottom: 0px;
}
</style>
</head>
<body class="page-container-bg-solid page-header-menu-fixed">
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<!-- END PAGE BREADCRUMBS -->

						<% if (mybean.call_callid.equals("0") && mybean.call_no.equals("")) { %>
						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Search</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="tab-pane" id="">
									<!-- START PORTLET BODY -->
									<form name="form1" method="post" class="form-horizontal">
										<center>
											<font color="#ff0000"><b><%=mybean.msg%></b></font>
										</center>
										<div class="form-element6 form-element-center">
											<label> Search <font color=red>*</font>:&nbsp;
											</label> <input name="call_no_search" type="text" size="40"
												maxlength="13" class="form-control" id="call_no_search"
												value="<%=mybean.call_search_no%>"
												placeholder="Enter Mobile Number!" />
										</div>
										<center>
											<input name="search_button" type="submit"
												class="btn btn-success" id="search_button" value="Search" />
										</center>
									</form>

								</div>
							</div>
						</div>
						<% }else if(!mybean.call_callid.equals("0")
								&& !mybean.call_no.equals("")
								&& mybean.contact_count.equals("0")
								&& mybean.singlecontact.equals("")){ %>
						<center><div><font color="red"><b><%=mybean.msg %></b></font></div></center>
						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Add Contact</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="tab-pane" id="">
									<form name="form1" id="form1" class="form-horizontal"
										method="post">

										<div class="row">
											<div class="form-element12">
												<label> Branch<font color="red">*</font>:
												</label> <select name="dr_branch_id" id="dr_branch_id"
													class="dropdown form-control">
													<%=mybean.PopulateBranches(mybean.customer_branch_id, mybean.comp_id)%>
												</select>
											</div>
										</div>


										<div class="row">
											<div class="form-element2 ">
												<label>Contact<font color="red">*</font>:&nbsp;
												</label> <select name="dr_title" class="form-control" id="dr_title">
													<%=mybean.PopulateTitle(mybean.contact_title_id, mybean.comp_id)%>
												</select> <span>Title</span>
											</div>

											<div class="form-element5 form-element-margin">
												<input name="txt_contact_fname" type="text"
													class="form-control" id="txt_contact_fname"
													value="<%=mybean.contact_fname%>" size="30" maxlength="255"
													onkeyup="ShowNameHint();" /> <span>First Name</span>
											</div>

											<div class="form-element5 form-element-margin">
												<input name="txt_contact_lname" type="text"
													class="form-control" id="txt_contact_lname"
													value="<%=mybean.contact_lname%>" size="30" maxlength="255"
													onkeyup="ShowNameHint();" /> <span>Last Name</span>
											</div>
										</div>

										<div class="row">
										<div class="form-element4">
												<label>Mobile 1<font color="#ff0000">*</font>:
												</label> <input name="txt_contact_mobile1" type="text"
													class="form-control" id="txt_contact_mobile1" size="32"
													maxlength="13"
													value="<%=mybean.call_no%>" />(91-9999999999) 
											</div>
											
											<div class="form-element4">
												<label>Phone 1: </label>
												<input name="txt_contact_phone1" type="text" class="form-control" id="txt_contact_phone1"
													value="<%=mybean.contact_phone1%>" size="32" maxlength="15" />(91-80-33333333)
													<span id="showcontactsphone1" class="hint" ></span>
											</div>
										
										
										<div class="form-element4">
												<label>Email 1:</label> <input name="txt_contact_email1"
													type="text" class="form-control" id="txt_contact_email1"
													size="32" maxlength="100"
													onkeyup="showHint('../sales/enquiry-check.jsp?contact_id='+<%=mybean.contact_id%>+'&enquiry_branch_id='+<%=mybean.customer_branch_id%>+'&contact_email=' + GetReplace(this.value),'showcontactsemail');"
													value="<%=mybean.contact_email1%>" />
												<div id="showcontactsemail" class="hint"></div>
											</div>
										</div>
										<div class="row">

											<div class="form-element6">
												<label>Address:</label>
												<textarea name="txt_contact_address" cols="40" rows="4"
													class="form-control" id="txt_contact_address"
													onkeyup="charcount('txt_contact_address', 'span_txt_contact_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.contact_address%></textarea>
												<span id="span_txt_contact_address"> (255 Characters)</span>
											</div>

											<div class="form-element6">
												<label>City:
												</label> <select class="form-control select2" id="maincity"
													name="txt_customer_city">
													<%=mybean.citycheck.PopulateCities(mybean.contact_city_id, mybean.comp_id)%>
												</select>
											</div>
											<div class="form-element6">
												<label>Pin/Zip:</label> <input name="txt_contact_pin"
													type="text" class="form-control" id="txt_contact_pin"
													onkeyup="toInteger('txt_contact_pin','Pin')"
													value="<%=mybean.contact_pin%>" size="10" maxlength="6" />
											</div>
										</div>
										<center>
											<input name="addbutton" id="addbutton" type="submit"
												class="btn btn-success" value="Add Contact" /> <input
												type="hidden" id="add_button" name="add_button" value="Add Contact" />
												 <input type="hidden" id="add" name="add" value="yes" />
										</center>
									</form>
								</div>
							</div>
						</div>
						<%}else if(mybean.singlecontact.equals("single")){ %>
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Incoming Call No.: &nbsp;&nbsp;<%=mybean.call_no%></div>
								</div>

								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<input type="hidden" id="call_no" name="call_no"
											value="<%=mybean.call_no%>" /> <input type="hidden"
											id="customer_id" name="customer_id"
											value="<%=mybean.customer_id%>" /> <input type="hidden"
											id="call_callid" name="call_callid"
											value="<%=mybean.call_callid%>" /> <input type="hidden"
											id="call_id" name="call_id"
											value="<%=mybean.call_id%>" /> <input type="hidden"
											id="contact_id" name="contact_id"
											value="<%=mybean.contact_id%>" /> <input type="hidden"
											id="contact_name" name="contact_name"
											value="<%=mybean.contact_name%>" />

										<div class="table-hover">
											<table class="table table-bordered table-responsive"
												data-filter="#filter">
												<tbody>
													<tr>
														<td align="right"><label> Customer Name:
																&nbsp;&nbsp;</label></td>
														<td align="left"><b><%=mybean.customer_name%></b></td>
														<td align="right"><label> Contact Name:
																&nbsp;&nbsp;</label></td>
														<td align="left"><b><%=mybean.contact_name%></b></td>
													</tr>
													<tr>
														<td align="right"><label> Contact Mobile 1:
																&nbsp;&nbsp;</label></td>
														<td align="left"><b><%=mybean.contact_mobile1%></b></td>
														<td align="right"><label> Contact Mobile 2:
																&nbsp;&nbsp;</label></td>
														<td align="left"><b><%=mybean.contact_mobile2%></b></td>
													</tr>
													<tr>
														<td align="right"><label> Contact Email 1:
																&nbsp;&nbsp;</label></td>
														<td align="left"><b><%=mybean.contact_email1%></b></td>
														<td align="right"><label> Contact Email 2:
																&nbsp;&nbsp;</label></td>
														<td align="left"><b><%=mybean.contact_email2%></b></td>
													</tr>
													<tr>
														<td align="right"><label> Customer Class:
																&nbsp;&nbsp;</label></td>
														<td align="left"><b><font color="red"><%=mybean.customer_class%></font></b>
														</td>
														<td align="right"><label> Account Manager:
																&nbsp;&nbsp;</label></td>
														<td align="left"><b><%=mybean.customer_accountmanager%></b>
														</td>
													</tr>
													<%if(!mybean.customer_tags.equals("")){ %>
													<tr>
														<td align="center" colspan="4"><br></br>
															<div class="form-element12" id="tags">
																<%=mybean.customer_tags%>
															</div></td>
													</tr>
													<%} %>
												</tbody>
											</table>
										</div>

										<div class="table-hover">
											<table class="table table-bordered table-responsive"
												data-filter="#filter">
												<tbody>
													<tr>
														<td align="center">
															<h4 class="margin-h5">
																Enquiry:
																<%=mybean.customer_enquiry_count%></h4> <input name="enquiry"
															type="button" class="btn btn-success" id="enquiry"
															value="Add Enquiry" onclick="check('enquiry');" />

														</td>
														<td align="center"><h4 class="margin-h5">
																Sales Order:
																<%=mybean.customer_so_count%></h4> <input name="preowned"
															type="button" class="btn btn-success" id="preowned"
															value="Add Pre-Owned Enquiry"
															onclick="check('preowned');" /></td>
														<td align="center"><b><h4 class="margin-h4">
																	Pre-Owned:
																	<%=mybean.customer_preowned_count%>
																</h4> <input name="service" type="button"
																class="btn btn-success" id="service"
																value="Add Service Booking" onclick="check('service');" /></td>
														<td align="center"><h4 class="margin-h4">
																Service Booking:
																<%=mybean.customer_servicebooking_count%>
															</h4> 
															
<!-- 															<input name="insurance" type="button" -->
<!-- 															class="btn btn-success" id="insurance" -->
<!-- 															value="Add Insurance Enquiry" -->
<!-- 															onclick="check('insurance');" /> -->
															
															</td>
														<td align="center"><b><h4 class="margin-h4">
																	Insurance:
																	<%=mybean.customer_insurance_count%>
																</h4> <input name="ticket" type="button"
																class="btn btn-success" id="ticket" value="Add Ticket"
																onclick="check('ticket');" /></td>
														<td align="center"><b><h4 class="margin-h4">
																	Job Cards:
																	<%=mybean.customer_jc_count%>
																</h4> <input name="canned_msg" type="button"
																class="btn btn-success" id="canned_msg"
																value="Canned Messages" onclick="check('canned');" /></td>


														</td>
														<td align="center"><b><h4 class="margin-h4">
																	Ticket:
																	<%=mybean.customer_ticket_count%>
																</h4> <input name="call_back" type="button"
																class="btn btn-success" id="call_back" value="Call Back"
																onclick="check('callback');" /></td>
														
													</tr>
												</tbody>
											</table>
										</div>
									</div>
								</div>
							</div>
							<div type="hidden" id="addenquiry"></div>
							<center>
								<font color="#ff0000"><b><div type="hidden"
											id="success"></div></b></font>
							</center>
							<div type="hidden" id="addpreownedenquiry"></div>
							<div type="hidden" id="addservicebooking"></div>
							<div type="hidden" id="addinsuranceenquiry"></div>
							<div type="hidden" id="addticket"></div>
							<div type="hidden" id="addbutton"></div>
						</div>


						<% }else if(mybean.singlecontact.equals("multiple")){ %>
						<%=mybean.StrHTML %>
						<%} %>
						
				</div>
			</div>
		</div>
	</div>
	</div>
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript" src="../Library/inbound-call.js"></script>
	
	<script type="text/javascript">
	
	function populatediv(){
		
	}
	
	
	
	</script>
	
	
	
	
</body>
</html>
