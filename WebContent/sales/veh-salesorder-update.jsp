<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Veh_Salesorder_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp" %>
<link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed" onload="HideDiv();">
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
						<h1><%=mybean.status%>&nbsp;Sales Order
						</h1>
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
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="veh-salesorder.jsp">Sales Orders</a> &gt;</li>
						<li><a href="veh-quote-list.jsp?all=recent">List Sales Orders</a> &gt;</li>
						<li><a href="veh-quote-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Sales Orders</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<div class="caption" style="float: none">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
							</div>
							<!-- 					BODY START -->
							<form name="form1" id="form1" method="post"
								class="form-horizontal">
								<div class="portlet box ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<%=mybean.status%>&nbsp; Sales Order
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
											<center>
												Form fields marked with a red asterisk <b>
												<font color="#ff0000">*</font></b> are required.
											</center>
											<input type="hidden" id="txt_enquiry_enquirytype_id"
												name="txt_enquiry_enquirytype_id" value="<%=mybean.enquiry_enquirytype_id%>">
											<input type="hidden" name="txt_session_id" id="txt_session_id"
												value="<%=mybean.session_id%>" />
											<input type="hidden" name="txt_branch_id" id="txt_branch_id" value="<%=mybean.branch_id%>">
											<input type="hidden" name="txt_status" id="txt_status" value="<%=mybean.status%>">
											<input type="hidden" name="txt_so_grandtotal"
												id="txt_so_grandtotal" value="<%=mybean.so_grandtotal%>" />
											<input type="hidden" name="txt_so_discamt"
												id="txt_so_discamt" value="<%=mybean.so_discamt%>" /> <input
												type="hidden" name="txt_so_netamt" id="txt_so_netamt"
												value="<%=mybean.so_netamt%>" /> <input type="hidden"
												name="txt_so_totaltax" id="txt_so_totaltax"
												value="<%=mybean.so_totaltax%>" /> <input type="hidden"
												name="txt_so_contact_id" id="txt_so_contact_id"
												value="<%=mybean.so_contact_id%>"> <input
												type="hidden" name="txt_rateclass_id	"
												id="txt_rateclass_id	" value="<%=mybean.rateclass_id%>">
											<input type="hidden" id="lead_id" name="lead_id"
												value="<%=mybean.lead_id%>"> <input type="hidden"
												id="txt_so_item_id" name="txt_so_item_id"
												value="<%=mybean.so_item_id%>" /> <input type="hidden"
												id="txt_so_exprice" name="txt_so_exprice"
												value="<%=mybean.so_exprice%>" /> <input type="hidden"
												id="txt_so_quote_id" name="txt_so_quote_id"
												value="<%=mybean.so_quote_id%>" /> <input type="hidden"
												id="txt_enquiry_id" name="txt_enquiry_id"
												value="<%=mybean.enquiry_id%>" /> <input type="hidden"
												id="txt_branch_brand_id" name="txt_branch_brand_id"
												value="<%=mybean.branch_brand_id%>" />
												<div class="form-element6">
														<label>Branch : </label>
															<a
																href="../portal/branch-summary.jsp?branch_id=<%=mybean.branch_id%>"><%=mybean.branch_name%></a>
															<input type="hidden" name="txt_branch_name"
																id="txt_branch_name" value="<%=mybean.branch_name%>">
															<% if (mybean.emp_branch_id.equals("0") && mybean.so_quote_id.equals("0") && mybean.enquiry_id.equals("0") && mybean.add.equals("yes")) { %>
															<a href="sales-branch.jsp?para=so">(Change Branch)</a>
															<%
																								}
																							%>
												</div>

												<div class="form-element6">
														<label>Date<b><font
																color="#ff0000">*</font></b> :
														</label>
															<%
																								if (mybean.status.equals("Add")) {
																							%>
															<input name="txt_so_date" type="text"
																class="form-control datepicker" 
																id="txt_so_date" value="<%=mybean.sodate%>" size="12"
																maxlength="10" />
															<%
																								} else if (mybean.status.equals("Update")
																										&& (mybean.emp_id.equals("1") || mybean.emp_id.equals("88"))) {
																							%>
															<input name="txt_so_date" type="text"
																class="form-control datepicker"  
																id="txt_so_date" value="<%=mybean.sodate%>" size="12"
																maxlength="10" />
															<%
																								} else if (mybean.status.equals("Update")) {
																							%>
															<%=mybean.sodate%>
															<input name="txt_so_date" type="hidden" id="txt_so_date"
																name="txt_so_date" value="<%=mybean.sodate%>" />
															<%
																								}
																							%>
												</div>

											<div class="form-element6">
														<label>Customer : </label>
															<b><span id="span_so_customer_id"
																name="span_so_customer_id"><%=mybean.link_customer_name%></span></b>&nbsp;
															<input name="span_acct_id" type="hidden"
																id="span_acct_id" value="<%=mybean.customer_id%>">
															<input name="acct_id" type="hidden" id="acct_id"
																value="<%=mybean.so_customer_id%>">
												</div>

												<div class="form-element6">
														<label>Contact : </label>
															<b><span id="span_so_contact_id"
																name="span_so_contact_id"><%=mybean.link_contact_name%></span></b>&nbsp;
															<input name="span_cont_id" type="hidden"
																id="span_cont_id" value="<%=mybean.contact_id%>">
															<input name="cont_id" type="hidden" id="cont_id"
																value="<%=mybean.so_contact_id%>">
															<div id="dialog-modal"></div>
													</div>

												<%if (!mybean.enquiry_id.equals("0")) { %>
												<div class="form-element6">
														<label> Enquiry ID:</label>
															<b><a href="enquiry-list.jsp?enquiry_id=<%=mybean.enquiry_id%>"><%=mybean.enquiry_id%></a></b>
												</div>
												<%
																				}
																			%>

												<%
																				if (!mybean.so_quote_id.equals("0")) {
																			%>
												<div class="form-element6">
														<label> Quote ID:</label>
															<a href="veh-quote-list.jsp?quote_id=<%=mybean.so_quote_id%>"><%=mybean.so_quote_id%></a></b>
												</div>
												<%
																				}
																			%>
											<div id="config_details">
												<%=mybean.GetConfigurationDetails(request)%>
											</div>

										<div class="form-element6" style="display:<%=mybean.display%>">
											<label>Description :</label>
												<textarea name="txt_so_desc" cols="70" rows="4"
													class="form-control summernote_1" id="txt_so_desc"
													<%=mybean.readOnly%>><%=mybean.unescapehtml(mybean.so_desc)%></textarea>
											<div>
											</div>
										</div>
										<div class="form-element6" style="display:<%=mybean.display%>">
											<label>Terms &amp; Conditions :</label>
												<textarea name="txt_so_terms" cols="70" rows="4"
													class="form-control summernote_1" id="txt_so_terms"
													<%=mybean.readOnly%>><%=mybean.unescapehtml(mybean.so_terms)%></textarea>
										</div>

										<div class="form-element6">
												<%
																if (mybean.enquiry_enquirytype_id.equals("1")) {
															%>
												<%
																if (!mybean.so_vehstock_id.equals("0")) {
															%>
													<label>Stock ID:</label>
													<input name="txt_so_vehstock_id" type="text"
														id="txt_so_vehstock_id" value="<%=mybean.so_vehstock_id%>"
														onkeyup="toInteger(this.id);" class="form-control" />
														
														
												<%
																}
															%>
												<%
																} else if (mybean.enquiry_enquirytype_id.equals("2")) {
															%>
												<label>Pre-Owned
													Stock ID:</label>
													<input name="txt_so_preownedstock_id" type="text"
														id="txt_so_preownedstock_id"
														value="<%=mybean.so_preownedstock_id%>"
														onkeyup="toInteger(this.id);" class="form-control" />
												<%
																}
															%>
										</div>
										
										<%
											if (!mybean.so_vehstock_comm_no.equals("0")) {
										%>
											<div class="form-element6">
												<label>Comm. No:</label>
												<input name="txt_so_vehstock_comm_no" type="text" id="txt_so_vehstock_comm_no"
													value="<%=mybean.so_vehstock_comm_no%>" onkeyup="toInteger(this.id);" class="form-control" />
											</div>		
										<%
											}
										%>								
										
										<div class="row"></div>
										 <div class="row">
										<div class="form-element6">
												<label>Colour<b><font color="#ff0000">*</font></b>: </label>
													<select id="dr_option_id" name="dr_option_id"
														class="form-control">
														<%=mybean.PopulateColour()%>
													</select>
												</div>
										<div class="form-element6">
												<label>Allotment No.:</label>
													<input name="txt_so_allot_no" type="text"
														id="txt_so_allot_no" value="<%=mybean.so_allot_no%>"
														size="12" maxlength="10" onkeyup="toInteger(this.id);"
														class="form-control" />
										</div>
                                      </div>

										<div class="form-element6">
												<label>Booking Amount<font
													color="#ff0000">*</font></b>:
												</label>
													<input name="txt_so_booking_amount" type="text"
														class="form-control" id="txt_so_booking_amount"
														value="<%=mybean.so_booking_amount%>"
														onkeyup="toInteger('txt_so_booking_amount','Booking Amount')" />
										</div>
										
										<div class="form-element6">
												<label>Finance Type<b><font
														color="#ff0000">*</font></b>:
												</label>
													<select id="dr_so_fintype" name="dr_so_fintype"
														class="form-control" onchange="HideDiv();">
														<%=mybean.PopulateFinanceType()%>
													</select>
												</div>
										<div class="row"></div>
										<div class="row">
										<div class="form-element6" id="financeby">
												<label>Finance By:</label>
													<select name="dr_finance_by" class="form-control"
														id="dr_finance_by">
														<%=mybean.PopulateFinanceBy()%>
													</select>
										</div>
										<div class="form-element6" id="financeamt">
												<label>Finance Amount:</label>
													<input name="txt_so_finance_amt" type="text"
														class="form-control" id="txt_so_finance_amt"
														value="<%=mybean.so_finance_amt%>"
														onkeyup="toInteger('txt_so_finance_amt','Finance Amount')" />
										</div>
										</div>										
										<div class="form-element6">
												<label>Purchase
													Order:</label>
													<input name="txt_so_po" type="text" class="form-control"
														id="txt_so_po" size="50" maxlength="50"
														value="<%=mybean.so_po%>" />
										</div>

										<div class="form-element3">
												<label>Payment Date<b><font
														color="#ff0000">*</font></b>:
												</label>
													<input name="txt_so_payment_date" type="text"
														class="form-control datepicker"
														id="txt_so_payment_date"
														value="<%=mybean.so_paymentdate%>" maxlength="10" />
												</div>
										<div class="form-element3">
												<label>Tentative
													Delivery Date<b><font color="#ff0000">*</font></b>:
												</label>
													<input name="txt_so_promise_date" type="text"
														class="form-control datepicker"
														id="txt_so_promise_date"
														value="<%=mybean.so_promisedate%>" maxlength="10" />
										</div>

										<div class="form-element6">
												<label>Registration
													No.:</label>
													<input name="txt_so_reg_no" type="text"
														class="form-control" id="txt_so_reg_no"
														value="<%=mybean.so_reg_no%>" />
										</div>

										<div class="form-element6">
												<label>Registration
													Date:</label>
													<input name="txt_so_reg_date" type="text"
														class="form-control datepicker"
														id="txt_so_reg_date"
														value="<%=mybean.so_regdate%>" />
										</div>
                                         <div class="form-element6">
												<label>PAN No.
												<%if(mybean.comp_id.equals("1009")){ %><font color="#ff0000">*</font><%} %>
												:</label>
													<input name="txt_so_pan" type="text" class="form-control"
														id="txt_so_pan" value="<%=mybean.so_pan%>" maxlength="10">
										</div>
										<div class="form-element6">
												<label>GST No.:</label>
													<b><input name="txt_so_gst" type="text" class="form-control"
														id="txt_so_gst" value="<%=mybean.so_gst%>" maxlength="15" /></b>
														<span id="gst"></span>
										</div>
										<div class="form-element3 form-element-margin">
												<label>Form 60:</label>
													<input id="chk_so_form60" type="checkbox"
														name="chk_so_form60"
														<%=mybean.PopulateCheck(mybean.so_form60)%> />
										</div>
											<div class="form-element3 form-element-margin">
												<label>Sales Order Open:</label> <input id="chk_so_open"
													type="checkbox" name="chk_so_open"
													<%=mybean.PopulateCheck(mybean.so_open)%> />
											</div>
											<div class="form-element2">
												<label>DOB<font color="#ff0000">*</font>:
												</label> <select name="dr_DOBMonth" class="form-control"
													id="dr_DOBMonth">
													<%=mybean.PopulateMonth(mybean.dr_month)%>
												</select>
											</div>
											<div class="form-element2">
												<label>&nbsp;</label> <select name="dr_DOBDay"
													class="form-control" id="dr_DOBDay">
													<%=mybean.PopulateDay(mybean.dr_day)%>
												</select>
											</div>
											<div class="form-element2">
												<label>&nbsp;</label> <select name="dr_DOBYear"
													class="form-control" id="dr_DOBYear">
													<%=mybean.PopulateYear(mybean.dr_year)%>
												</select>
											</div>
											<div class="form-element6">
												<label> Sales Consultant<font color="#ff0000">*</font>:
												</label> <select <%if (!mybean.emp_id.equals("1")) {%> disabled
													<%}%> name="dr_executive" id="dr_executive"
													class="form-control">
													<%=mybean.PopulateExecutive()%>
												</select>
												<%
													if (!mybean.emp_id.equals("1")) {
												%>
												<input hidden id="dr_executive" name="dr_executive"
													value="<%=mybean.so_emp_id%>" />
												<%
													}
												%>
											</div>
											<%
												if (mybean.config_sales_so_refno.equals("1")) {
											%>
											<div class="form-element6">
												<label>Sales Order Reference No.<font
													color="#ff0000">*</font>:
												</label> <input name="txt_so_refno" type="text" class="textbox"
													id="txt_so_refno" value="<%=mybean.so_refno%>" size="32"
													maxlength="50" />
											</div>
											<%
												}
											%>
											<div class="form-element6">
												<label>Notes:</label>
													<textarea name="txt_so_notes" class="form-control"
														id="txt_so_notes"><%=mybean.so_notes%></textarea>
										</div>
										<div class="row"></div>
										<%
													if (mybean.status.equals("Update") && !(mybean.entry_date == null)
															&& !(mybean.entry_date.equals(""))) {
												%>
										<div class="form-element6">
												<label>Entry By:</label>
													<%=mybean.unescapehtml(mybean.so_entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.so_entry_by)%>" />
										</div>
										<div class="form-element6">
												<label>Entry Date: <%=mybean.entry_date%></label>
													<input type="hidden" id="entry_date" name="entry_date"
														value="<%=mybean.entry_date%>" />
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
												<label>Modified By: <%=mybean.unescapehtml(mybean.so_modified_by)%></label>
													<input type="hidden" id="modified_by" name="modified_by"
														value="<%=mybean.unescapehtml(mybean.so_entry_by)%>" />
										</div>
										<div class="form-element6">
												<label>Modified Date: <%=mybean.modified_date%></label>
													<input type="hidden" id="modified_date"
														name="modified_date" value="<%=mybean.modified_date%>" />
										</div>
										<%
													}
												%>

										<center>
											<%
														if (mybean.status.equals("Add")) {
													%>
											<input name="addbutton" id="addbutton" type="button"
												onClick="return SubmitFormOnce(document.form1, this);"
												class="btn btn-success" value="Add Sales Order" /> <input
												type="hidden" name="add_button" value="yes">
											<%
														} else if (mybean.status.equals("Update")) {
													%>
											<input type="hidden" name="update_button" value="yes">
											<input name="updatebutton" id="updatebutton" type="submit"
												class="btn btn-success" value="Update Sales Order"
												onClick="return SubmitFormOnce(document.form1, this);" /> <input
												name="delete_button" type="submit" class="btn btn-success"
												id="delete_button" onClick="return confirmdelete(this)"
												value="Delete Sales Order" />
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
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp" %>
	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-editors.min.js"
		type="text/javascript"></script>
	<script src="../assets/js/footable.js" type="text/javascript"></script>
	 <script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>
	<script>
	$(function() {
		$("#txt_so_gst").focusout(function(){
			var gst_no=$("#txt_so_gst").val();
			var regex=/^([0-9]{2})([a-zA-Z]{5})([0-9]{4})([a-zA-Z]{1})([a-zA-Z0-9]{3})?$/;
			if(gst_no.length!=0 && gst_no.length < 15){
				$("#gst").html("<br><b><font color='red'>GST No. is invalid</font></b>");
			}else if(gst_no.length!=0 && regex.test(gst_no) == false){
				$("#gst").html("<br><b><font color='red'>GST No. is invalid</font></b>");
			}else{
				$("#gst").html("");
			}
		});
		
 	});
	</script>
	
	<script>
	  $("#txt_so_gst").bind('keyup',function(e){
		  if(e.which >= 97 && e.which <= 122 ){
			  var newKey=e.which-32;
			  e.keyCode=newKey;
			  e.charCode=newKey;
		  }
		  $("#txt_so_gst").val(($("#txt_so_gst").val()).toUpperCase());
	  });
	  
	  function HideDiv(){
		  var financetype = $("#dr_so_fintype").val();
		  console.log(financetype);
		  if(financetype == 3){
			  $("#financeby").hide();
			  $("#financeamt").hide();
			  } else{
				  $("#financeby").show();
				  $("#financeamt").show();
			  }
	  }
	</script>
</body>
</HTML>
