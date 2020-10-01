<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Ledger_Add"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" type="image/x-icon" href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<link href="../assets/css/select2.min.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/select2-bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Add Ledger</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../accounting/index.jsp">Accounting</a> &gt;</li>
						<li><a href="../accounting/ledger-list.jsp?all=yes&dialogue=yes">List Ledgers</a> &gt;</li>
						<li><a href="../accounting/ledger-add.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Ledger</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>
										Ledger
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" id="form1"
											method="post">
											<div class="container-fluid " id="dr_branch">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> Branch<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6" style="margin-top: 8px">

															<%
																if (mybean.emp_branch_id.equals("0")) {
															%>
															<select name="dr_branch_id" class="form-control"
																id="dr_branch_id">
																<%=mybean.PopulateBranch(mybean.branch_id, "", "", request)%>
															</select>
															<%
																} else {
															%>
															<%=mybean.branch_name%>
															<%
															 	}
															%>
														</div>
													</div>
												</div>

											</div>

											<div class="container-fluid " id="customer_name">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> <%=mybean.label %>
															Name:
														</label>
														<div class="col-md-6">
															<input name="txt_customer_name" type="text"
																class="form-control" id="txt_customer_name"
																value="<%=mybean.customer_name%>" size="50"
																maxlength="255">
														</div>
													</div>
												</div>

											</div>
											<div class="container-fluid " id="customer_code">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> <%=mybean.label %>
															Code<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_customer_code" type="text"
																class="form-control" id="txt_customer_code"
																value="<%=mybean.customer_code%>" size="50"
																maxlength="255">
														</div>
													</div>
												</div>

											</div>
											
											<div class="container-fluid " id="contact_name"
												style="margin-bottom: 15px">
												<div class="col-md-6 col-sm-6 col-xs-12">
													<div class="control-label col-md-3 col-sm-4 col-xs-12">
														Contact Name<font color="#ff0000">*</font>:
														<input name="span_cont_id" type="hidden"
															id="span_cont_id" value="<%=mybean.contact_id%>">
															<input name="cont_id" type="hidden" id="cont_id"
															value="<%=mybean.customer_contact_id%>">
													</div>
													<div class="col-md-6 col-sm-8">
														<div class="col-md-4 col-xs-12">
															<select name="dr_title" class="form-control"
																id="dr_title">
																<%=mybean.PopulateTitle()%>
															</select> <span> Title </span>
														</div>
														<div class="col-md-4 col-xs-12">
															<input name="txt_contact_fname" type="text"
																class="form-control" id="txt_contact_fname"
																value="<%=mybean.contact_fname%>" size="30"
																maxlength="255" onKeyUp="ShowNameHint()" /><span>First
																Name</span>
														</div>
														<div class="col-md-4 col-xs-12">
															<input name="txt_contact_lname" type="text"
																class="form-control" id="txt_contact_lname"
																value="<%=mybean.contact_lname%>" size="30"
																maxlength="255" onKeyUp="ShowNameHint()" /> <span>Last
																Name</span>
														</div>
													</div>
												</div>
												<div class="col-md-6 col-xs-6">
													<div class="control-label col-md-4 col-sm-4 col-xs-12">
														<label></label>
													</div>
													<div class="col-md-4 col-sm-4 col-xs-12"></div>
												</div>
											</div>
											
											
											<%
														if (mybean.vouchertype_mobile.equals("1")) {
													%>

											<div class="container-fluid " id="contact_mobile">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> Mobile<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_contact_mobile1" type="text"
																class="form-control" id="txt_contact_mobile1"
																value="<%=mybean.contact_mobile1%>" size="20"
																maxlength="13"
																onkeyup="toPhone('txt_contact_mobile1','Mobile')";>
															(91-9999999999) <span id="txtHint1"></span>

														</div>
													</div>
												</div>

												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-2 control-label"> Phone<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">

															<td valign="top" align=left><input
																name="txt_contact_phone1" type="text"
																class="form-control" id="txt_contact_phone1"
																onkeyup="toPhone('txt_contact_phone1','Phone');"
																value="<%=mybean.contact_phone1%>" size="20"
																maxlength="14"> (91-80-33333333)
														</div>
													</div>
												</div>
											</div>
											<%} else {%>

											<div class="container-fluid " id="contact_mobile1">
												<div class="form-body col-md-6 col-xs-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> Phone<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_contact_phone1" type="text"
																class="form-control" id="txt_contact_phone1"
																"toPhone('txt_contact_phone1','Phone');" value="<%=mybean.contact_phone1%>"
																size="10" maxlength="14"> (91-80-33333333)

														</div>
													</div>
												</div>




											</div>
											<%} %>
											<%
														if (mybean.vouchertype_email.equals("1")) {
													%>
											<div class="container-fluid " id="contact_email">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> Email: </label>
														<div class="col-md-6">
															<input name="txt_contact_email1" type="text"
																class="form-control" id="txt_contact_email1"
																value="<%=mybean.contact_email1%>" size="30"
																maxlength="255">

														</div>
													</div>
												</div>

											</div>
											<%} %>
											<div class="container-fluid " id="contact_address">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> Address:
															</td>
														</label>
														<div class="col-md-6">
															<textarea name="txt_contact_address" rows="4"
																class="form-control" id="txt_contact_address"
																onKeyUp="charcount('txt_contact_address', 'span_txt_contact_address','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.contact_address%></textarea>
															<br /> <span id="span_txt_contact_address"> (255
																Characters)</span>

														</div>
													</div>
												</div>
											</div>
											<div class="container-fluid " id="contact_city">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> City<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select class="form-control select2" id="maincity"
																name="maincity">
																<%=mybean.ledgercheck.PopulateLedgers("31", mybean.contact_city_id, mybean.comp_id)%>
															</select>


															<!-- 													<input tabindex="-1" -->
															<!-- 															class="bigdrop select2-offscreen" id="maincity" -->
															<!-- 															name="maincity"  -->
															<%-- 															value="<%=mybean.contact_city_id%>" type="hidden">	 --%>

														</div>
													</div>
												</div>
											</div>
											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> Pin/Zip: </label>
														<div class="col-md-6">
															<input name="txt_contact_pin" type="text"
																class="form-control" id="txt_contact_pin"
																onKeyUp="toInteger('txt_contact_pin','Pin')"
																value="<%=mybean.contact_pin%>" size="10" maxlength="6" />

														</div>
													</div>
												</div>
											</div>
											<%
														if (mybean.vouchertype_dob.equals("0") && mybean.vouchertype_dnd.equals("1")) {
													%>

											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> DND: </label>
														<div class="col-md-6">
															<input id="chk_contact_dnd" type="checkbox"
																name="chk_contact_dnd"
																<%=mybean.PopulateCheck(mybean.contact_dnd)%> /><
														</div>
													</div>
												</div>
											</div>
											<%} %>
											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> PAN: </label>
														<div class="col-md-6">
															<input name="txt_customer_pan_no" type="text"
																class="form-control" id="txt_customer_pan_no"
																value="<%=mybean.customer_pan_no%>" size="20"
																maxlength="15">
														</div>
													</div>
												</div>
											</div>
											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> Credit
															Limit: </label>
														<div class="col-md-6">
															<input name="txt_customer_credit_limit" type="text"
																class="form-control" id="txt_customer_credit_limit"
																value="<%=mybean.customer_credit_limit%>" size="20"
																maxlength="12" onkeyup="toInteger(this.id);">




														</div>
													</div>
												</div>

												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> Payment
															Days<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select name="dr_customer_paydays_id"
																class="form-control" id="dr_customer_paydays_id">
																<%=mybean.PopulatePaymentDays()%>
															</select>
														</div>
													</div>
												</div>


											</div>

											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> C Form: </label>
														<div class="col-md-6" style="top: 8px;">
															<input id="chk_customer_cform" type="checkbox"
																name="chk_customer_cform"
																<%=mybean.PopulateCheck(mybean.customer_disc_perc)%> />
															</td>


														</div>
													</div>
												</div>

												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> F Form:
															</td>
														</label>
														<div class="col-md-6" style="top: 6px;">
															<input id="chk_customer_fform" type="checkbox"
																name="chk_customer_fform"
																<%=mybean.PopulateCheck(mybean.customer_disc_perc)%> />
														</div>
													</div>
												</div>

											</div>
											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> Executive<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select name="dr_executive" id="dr_executive"
																class="form-control">
																<%=mybean.PopulateExecutives(mybean.emp_id, mybean.comp_id)%>
															</select>

														</div>
													</div>
												</div>

											</div>

											<%
												if (mybean.status.equals("Add")) {
											%>
											<center>
												<input name="addbutton" id="addbutton" type="button"
													onClick="return validateclose(SubmitFormOnce(document.form1, this));"
													class="btn btn-success" value="Add Ledger" /> <input
													type="hidden" name="add_button" value="yes" />
											</center>
											<%
 	                          } else if (mybean.status.equals("Update")) {
                                        %>
											<center>
												<input type="hidden" name="update_button" value="yes">
												<input type="hidden" id="Update" name="Update" value="yes">
												<input name="updatebutton" id="updatebutton" type="submit"
													class="btn btn-success" value="Update Ledger"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input name="delete_button" type="submit"
													class="btn btn-success" id="delete_button"
													onClick="return confirmdelete(this)" value="Delete Ledger" /></input>
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
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js"></script>
<script src="../assets/js/select2.full.min.js" type="text/javascript"></script>
<script src="../assets/js/components-select2.min.js" type="text/javascript"></script>
<script src="../assets/js/components-date-time-pickers.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap-datepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/invoice.js"></script>
<script type="application/javascript">
	 
 function validateclose(flag){	
 	
	var msg="<%=mybean.msg%>";
	var ledger="<%=mybean.customer_id%>";
	return flag;	   
  }
function PopulateStateCity(country_id){   
		showHint('../portal/location.jsp?country_id=' + GetReplace(country_id)+'&dr_state_id=dr_contact_state_id&dr_city_id=dr_contact_city_id&span_city_id=contact_city_id' ,'contact_state_id');
		showHint('../portal/location.jsp?state_id=0&dr_city_id=dr_contact_city_id','dr_contact_city_id');
	}
	
	
  

</script>
</body>
</HTML>
