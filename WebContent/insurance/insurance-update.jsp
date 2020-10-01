<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Insurance_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

<script>
	
	$(document).ready(function() {
		$('#txt_start_date').datepicker().on('change', function(selectedDate) {
			var d1 = document.getElementById("txt_start_date").value;
			var selectedDate = document.getElementById("txt_start_date").value;
			var d = selectedDate.split("/");
			var da = d[0];
			var mo = d[1];
			var yr = d[2];
			var myDate = new Date(mo + "/" + da + "/" + yr);
			myDate.setFullYear(myDate.getFullYear() + 1);
			myDate.setDate(myDate.getDate() - 1);
			$("#txt_end_date").datepicker('setDate', myDate);
			/*  $("#txt_end_date").datepicker.formatDate('dd/mm/yy', myDate); */

		});


	});
</script>

<script language="JavaScript" type="text/javascript">
	function FormSubmit() {
		document.form1.submit();
	}

	function Displaypaymode() {
		var str = document.form1.dr_insur_paymode_id.value;
		if (str == "2") {
			$('#chequeno').show();
			$('#chequedate').show();
			$('#chequebank').show();
		}
		else {
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#chequebank').hide();
		}
	}
</script>

	
</HEAD>
<body onLoad="Displaypaymode();"
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
							Insurance
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
						<li><a href="../insurance/index.jsp">Insurance</a> &gt;</li>
						<!-- <li><a href="insurance.jsp">Insurance</a>&gt</li> -->
						<li><a href="insurance-list.jsp?all=yes"> List Insurances</a>
							&gt;</li>
						<li><a href="insurance-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
								Insurance</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					
						<div class="tab-pane" id="">
							
							<!-- BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center><%=mybean.status%> Insurance
										</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">

<!-- 										//for link hidden fields -->
								 <input type="hidden" id="contact_id" name="contact_id" value="<%=mybean.contact_id%>" />
								 <input type="hidden" id="contact_name" name="contact_name" value="<%=mybean.contact_name%>" />
								 <input type="hidden" id="customer_id" name="customer_id" value="<%=mybean.customer_id%>" />
								 <input type="hidden" id="customer_name" name="customer_name" value="<%=mybean.customer_name%>" />
								 <input type="hidden" id="variant_id" name="variant_id" value="<%=mybean.variant_id%>" />
								 <input type="hidden" id="reg_no" name="reg_no" value="<%=mybean.reg_no%>" />
								 <input name="insurenquiry_id" type="hidden" id="insurenquiry_id" value="<%=mybean.insurenquiry_id%>" />
								 <!-- 		hidden fields end here -->
											<center>
												<font size="1">Form fields marked with a red asterisk
													<font color=#ff0000>*</font> are required.<br></br>
												</font>
											</center>
											<div class="form-element6">
												<label>Customer:  </label>
													<b><span id="span_insurance_customer_name"
														name="span_insurance_customer_name">
														<%=mybean.link_customer_name%>
													</span></b>  
											</div>
											
											<div class="form-element6">
												<label>Contact:  </label>
													<b><span id="span_insurance_contact_id"
														name="span_insurance_contact_id"> <%=mybean.link_contact_name%>
													</span></b>
											</div>
                                               <%if(mybean.update.equals("yes")){ %>
											<div class="form-element6">
												<label>Insurance Enquiry ID: &nbsp; </label>
													<b><span id="insurenquiry_id"
														name="insurenquiry_id">
														<%=mybean.link_insurenquiry_name%>
													</span></b>
											</div>
											<%} %>
											
											<div class="form-element6">
												<label>Vehicle:  </label>
													<b><span id="insurance_veh_item" name="insurance_veh_item">
													<%=mybean.modelcheck.PopulateVariant(mybean.variant_id)%>
													</span></b>
											</div>
											
											<div class="form-element6">
												<label>Reg.No.: &nbsp;
												</label>
													<b><span id="span_insurance_veh_regno"
														name="span_insurance_veh_regno">
														<%=mybean.link_insurenquiry_reg_no%>
													</span></b>
											</div>
											
											<div class="form-element6">
												<label>Branch <font
													color="red">*</font>: &nbsp;
												</label>
												
													<input type="hidden" id="dr_branch" name="dr_branch"
														value="<%=mybean.branch_id%>" /><b><%=mybean.getBranchName(mybean.branch_id, mybean.comp_id)%></b>
											</div>
											
											<div class="form-element6">
												<label>Issue Date <font
													color="red">*</font>:
												</label>
													<input name="txt_insur_date" id="txt_insur_date"
														class="form-control datepicker" type="text"
														value="<%=mybean.insurpolicydate%>" />
											</div>
											
												

											<div class="form-element6">
												<label>Start Date <font
													color="red">*</font>:
												</label>
													<input name="txt_start_date" id="txt_start_date"
														class="form-control datepicker" type="text"
														value="<%=mybean.insurstartdate%>" />
											</div>

											<div class="form-element6">
												<label>Expiry Date <font
													color="red">*</font>:
												</label>
													<input name="txt_end_date" id="txt_end_date"
														class="form-control datepicker" type="text"
														value="<%=mybean.insurenddate%>" />
											</div>
											<div class="form-element6">
												<label>Policy<font
													color="red">*</font>:
												</label>
													<select name="dr_insur_insurpolicy_id"
														id="dr_insur_insurpolicy_id" class="form-control">
														<%=mybean.PopulateInsurancePolicy()%>
													</select>
											</div>
											<div class="row">
											<div class="form-element6">
												<label>Company<font
													color="red">*</font>:
												</label>
													<select name="dr_insur_inscomp_id" id="dr_insur_inscomp_id"
														class="form-control">
														<%=mybean.PopulateInsuranceCompany()%>
													</select>
											</div>
											<div class="form-element6">
												<label>Policy No.:</label>
													<input name="txt_insur_policy_no" type="text"
														class="form-control" id="txt_insur_policy_no"
														value="<%=mybean.insurpolicy_policy_no%>"
														MaxLength="255" />
											</div>
											</div>
											<div class="row">
											<div class="form-element6">
												<label>Type<font color="red">*</font>: </label>
													<select name="dr_insur_type" id="dr_insur_type"
														class="form-control">
														<%=mybean.PopulateInsuranceType()%>
													</select>
											</div>
											<div class="form-element6">
												<label>Cover Note No.:</label>
													<input name="txt_insur_covernote_no" type="text"
														class="form-control" id="txt_insur_covernote_no"
														value="<%=mybean.insurpolicy_covernote_no%>" size="20"
														MaxLength="255" />
											</div>
											</div>
											
											<div class="form-element6">
												<label>Premium Amount<font color="red">*</font>: </label>
													<input name="txt_insur_premium_amt"
														id="txt_insur_premium_amt"
														onKeyUp="toFloat('txt_insur_premium_amt','Amount')"
														type="text" class="form-control"
														value="<%=mybean.insurpolicy_premium_amt%>" size="20"
														maxlength="10" />
											</div>
											<div class="form-element6">
												<label>IDV Amount:</label>
													<input name="txt_insur_idv_amt" id="txt_insur_idv_amt"
														onKeyUp="toFloat('txt_insur_idv_amt','Amount')"
														type="text" class="form-control"
														value="<%=mybean.insurpolicy_idv_amt%>" size="20" maxlength="10" />
											</div>
											<div class="form-element6">
												<label>Dealers Discount Amount:</label>
													<input name="txt_insur_od_amt" id="txt_insur_od_amt"
														onKeyUp="toFloat('txt_insur_od_amt','Amount')" type="text"
														class="form-control" value="<%=mybean.insurpolicy_od_amt%>"
														size="20" maxlength="10" />
											</div>
											<div class="form-element6">
												<label>Dealers Discount: %</label>
													<input name="txt_insur_od_discount" id="txt_insur_od_discount"
														onKeyUp="toFloat('txt_insur_od_discount','Amount')"
														type="text" class="form-control"
														value="<%=mybean.insurpolicy_od_discount%>" size="20"
														maxlength="10" />
												
											</div>
											
                                            <div class="form-element6">
												<label>Payout : </label>
													<input name="txt_insur_payout" id="txt_insur_payout"
														class="form-control" type="text" maxlength="10"
															onKeyUp="toFloat('txt_insur_payout','Payout')"
														value="<%=mybean.insurpolicy_payout%>" >
													</input>
											</div>
											 
											<div class="form-element6">
												<label>Payment Mode<font
													color="red">*</font>:
												</label>
													<select name="dr_insur_paymode_id" id="dr_insur_paymode_id"
														class="form-control" onChange="Displaypaymode();">
														<%=mybean.PopulatePaymentMode()%>
													</select>
											</div>
											<div class="form-element6" id="chequeno">
												<label>Cheque No.<font
													color="red">*</font>:
												</label>
													<input name="txt_insur_cheque_no" type="text"
														id="txt_insur_cheque_no"
														onKeyUp="toInteger('txt_insur_cheque_no')"
														class="form-control" value="<%=mybean.insurpolicy_cheque_no%>"
														size="30" maxlength="255" />
											</div>
											<div class="form-element6" id="chequedate">
												<label>Cheque Date<font
													color="red">*</font>:
												</label>
													<input name="txt_insur_cheque_date" type="text"
														class="form-control date-picker"
														data-date-format="dd/mm/yyyy" id="txt_insur_cheque_date"
														value="<%=mybean.insurpolicy_cheque_date%>" size="12"
														maxlength="10" />
											</div>
											<div class="form-element6" id="chequebank">
												<label>Cheque Bank<font
													color="red">*</font>:
												</label>
													<select name="dr_insur_cheque_bank_id"
														id="dr_insur_cheque_bank_id" class="form-control">
														<%=mybean.PopulateChequeBank()%>
													</select>
											</div>

											<div class="form-element6">
												<label>Description:</label>
													<textarea name="txt_insur_desc" id="txt_insur_desc"
														cols="70" rows="4" class="form-control"><%=mybean.insurpolicy_desc%></textarea>
											</div>
											<div class="form-element6">
												<label>Terms:</label>
													<textarea name="txt_insur_terms" id="txt_insur_terms"
														cols="70" rows="4" class="form-control"><%=mybean.insurpolicy_terms%></textarea>
											</div>
											
											<div class="form-element6">
												<label>Executive<font
													color="#ff0000">*</font>:
												</label>
													<select name="dr_executive" id="dr_executive"
														class="form-control">
														<%=mybean.PopulateExecutive(mybean.insur_emp_id)%>
													</select>
											</div>
											
											<div class="form-element6 form-element-margin">
												<label>Active:</label>
													<input id="chk_insur_active" type="checkbox"
														name="chk_insur_active"
														<%=mybean.PopulateCheck(mybean.insurpolicy_active)%> />
											</div>
											<div class="row"></div>
											
											<div class="row">
											<div class="form-element6">
												<label>Notes:</label>
													<textarea name="txt_insur_notes" id="txt_insur_notes"
														cols="50" rows="4" class="form-control"><%=mybean.insurpolicy_notes%></textarea>
											</div> </div>
											
											<div class="row">
											<% if (mybean.status.equals("Update") && !(mybean.entry_by == null)
													&& !(mybean.entry_by.equals(""))) { %>
											<div class="form-element6 ">
														<label > Entry By:&nbsp; </label>
															<%=mybean.unescapehtml(mybean.entry_by)%> 
											</div>
											<% } %>
											
											<% if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) { %>
											<div class="form-element6 ">
														<label> Modified By:&nbsp; </label>
															<%=mybean.unescapehtml(mybean.modified_by)%> 
											</div>
											<%
												}
											%>
											</div>
											
											<div class="row">
											<% if (mybean.status.equals("Update") && !(mybean.insurpolicy_entry_date == null)
														&& !(mybean.insurpolicy_entry_date.equals(""))) { %>
											<div class="form-element6 ">
														<label> Entry Date:&nbsp; </label>
															<%=mybean.insurpolicy_entry_date%>
											</div>
											<% } %>
											
											<%
												if (mybean.status.equals("Update") && !(mybean.insurpolicy_modified_date == null)
														&& !(mybean.insurpolicy_modified_date.equals(""))) {
											%>
											<div class="form-element6 ">
														<label> Modified Date:&nbsp; </label>
															<%=mybean.insurpolicy_modified_date%> 
											</div>
											<%
												}
											%>
											</div>
											
											<div class="form-element12">
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												<input name="addbutton" id="addbutton" type="submit"
													onClick="return SubmitFormOnce(document.form1, this);"
													class="btn btn-success" value="Add Insurance" /> <input
													type="hidden" id="add_button" name="add_button" value="yes" />
												<%
													} else if (mybean.status.equals("Update")) {
												%>
												<input type="hidden" id="update_button" name="update_button"
													value="yes" /> <input name="updatebutton"
													id="updatebutton" type="submit" class="btn btn-success"
													value="Update Insurance"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input name="delete_button" type="submit"
													class="btn btn-success" id="delete_button"
													onClick="return confirmdelete(this)"
													value="Delete Insurance" />
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

	<div class="modal fade" id="Hinttocall" role="dialog"
		aria-labelledby="List Contacts" aria-hidden="true">
		<div class="modal-dialog" style="width:85%">
			<div class="modal-content"
				style="max-height: 900px">
				<div class=modal-header>
					

						<span> &nbsp;&nbsp;Loading... </span> <br> <br>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>

	
<%@include file="../Library/js.jsp"%>
</body>
</HTML>
