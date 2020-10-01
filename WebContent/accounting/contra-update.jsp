<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Contra_Update"
	scope="request" />
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
			document.form1.txt_customer_name.focus()
		}

		function DisplayFrom() {
			var str = document.form1.dr_vouchertrans_paymode_from_id.value;
			var paymodearr = str.split("-");
			var paymode_id = paymodearr[1];
			if (paymode_id == "" || paymode_id == null) {

				$('#chequeno').hide();
				$('#chequedate').hide();

			}
			if (paymode_id == "1") {
				$('#chequeno').hide();
				$('#chequedate').hide();

			}
			if (paymode_id == "2") {
				$('#chequeno').show();
				$('#chequedate').show();

			}

		}
	</script>

</HEAD>
<body onLoad="DisplayFrom();FormFocus();" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%>&nbsp;<%=mybean.vouchertype_name%></h1>
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
						<li><a href="index.jsp">Accounting</a> &gt;</li>
						<li><a
							href="../accounting/voucher-list.jsp?all=yes&vouchertype_id=
						<%=mybean.vouchertype_id%>
						&voucherclass_id=<%=mybean.voucherclass_id%>">List
								<%=mybean.vouchertype_name%>s
						</a>&gt;</li>
						<li><a href="contra-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
								<%=mybean.vouchertype_name%></a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<form name="form1" method="post" class="form-horizontal">
											<input name="txt_vouchertype_id" type="hidden" id="txt_vouchertype_id" value="<%=mybean.vouchertype_id%>" />
											<input type="hidden" name="hid_voucher_no" id="hid_voucher_no" value="<%= mybean.voucher_no%>" />
											<input type="hidden" name="txt_vouchertype_authorize" id="txt_vouchertype_authorize" value="<%=mybean.vouchertype_authorize %>" />
											<input type="hidden" name="txt_vouchertype_defaultauthorize" id="txt_vouchertype_defaultauthorize" value="<%=mybean.vouchertype_defaultauthorize %>" />
											<input type="hidden" name="txt_voucher_authorize" id="txt_voucher_authorize" value="<%=mybean.voucher_authorize %>" />
								            <input type="hidden" name="txt_voucher_authorize_id" id="txt_voucher_authorize_id" value="<%=mybean.voucher_authorize_id %>" />
											<input type="hidden" name="txt_voucher_authorize_time" id="txt_voucher_authorize_time" value="<%=mybean.voucher_authorize_time %>" />
												
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<%=mybean.status%>&nbsp;<%=mybean.vouchertype_name%>
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<center>
												Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</center>
											<input type="hidden" name="txt_voucher_id"
												id="txt_voucher_id" value="<%=mybean.voucher_id%>" />
										</div>

										<div class="form-element6">
													<label>Date:
													</label>
														<input name="txt_voucher_date" type="text"
															class="form-control datepicker" id="txt_voucher_date"
															value="<%=mybean.voucherdate%>" size="12" maxlength="10" />
										</div>
										<div class="form-element6">
											<label>Branch<font color="#ff0000">*</font>:</label>
											<select id="dr_voucher_branch_id"
													name="dr_voucher_branch_id" class="form-control">
													<%=mybean.PopulateBranch(mybean.voucher_branch_id, "", "", "", request)%>
												</select>
											</div>
												
										<div class="form-element6">
													<label>From<font color="#ff0000">*</font>:</label>
													<select name="dr_vouchertrans_paymode_from_id"
															value="<%=mybean.vouchertrans_paymode_from_id%>"
															class="form-control" onChange="DisplayFrom();">
															<%=mybean.PopulateFrom()%>
														</select>
										</div>
										<div class="form-element6">
													<label>To<font color="#ff0000">*</font>: </label>
													 <select name="dr_vouchertrans_paymode_to_id"
															value="<%=mybean.vouchertrans_paymode_to_id%>"
															class="form-control">
															<%=mybean.PopulateTo()%>
														</select>
										</div>
										
										<div class='form-element6'>
										<div class="form-element12 form-element" id="chequeno">
													<label>Cheque No/Txn No<font color="#ff0000">*</font>:
													</label>
													<input name="txt_vouchertrans_cheque_no" type="text"
															id="txt_vouchertrans_cheque_no" class="form-control"
															value="<%=mybean.vouchertrans_cheque_no%>" size="12"
															maxlength="10" />
										</div>

										<div class="form-element12 form-element" id="chequedate">
											<label>From Date<font color="#ff0000">*</font>: </label>
													<input name="txt_vouchertrans_cheque_date" type="text"
															class="form-control datepicker" 
															id="txt_vouchertrans_cheque_date"
															value="<%=mybean.vouchertrans_chequedate%>" size="12"
															maxlength="10" />
										</div>
										</div>
										<div class='row'></div>
												<div class="form-element6">
													<label>Amount<font color="#ff0000">*</font>: </label>
												 <input name="txt_voucher_amount" type="text"
															class="form-control" id="txt_voucher_amount"
															value="<%=mybean.voucher_amount%>" size="22"
															maxlength="20" onkeyup="toInteger(this.id);" />
													</div>
												<div class="form-element6">
													<label>Executive<font color="#ff0000">*</font>: </label>
													 <select name="dr_executive"
															value="<%=mybean.voucher_emp_id%>" id="dr_executive"
															class="form-control">
															<%=mybean.PopulateExecutives(mybean.emp_id, mybean.comp_id)%>
														</select>
													 </div>
										<div class="form-element6">
													<label>Reference No.<font color="#ff0000">*</font>: </label>
													 <input name="txt_voucher_ref_no" type="text"
															class="form-control" id="txt_voucher_ref_no"
															value="<%=mybean.voucher_ref_no%>" size="22"
															maxlength="20" />
													</div> 
													<div class="form-element6 form-element-margin">
													<label >Active: </label>
													 <input type="checkbox" name="ch_voucher_active"
															id="ch_voucher_active"
															<%=mybean.PopulateCheck(mybean.voucher_active)%> />
													</div>
													<div class="row"></div>
										<div class="form-element6">
													<label>Narration<font color="#ff0000">*</font>: </label>
												 <textarea name="txt_voucher_narration" cols="70" rows="4"
															class="form-control" id="txt_voucher_narration"
															onkeyup="charcount('txt_voucher_narration', 'span_txt_voucher_narration','<font color=red>({CHAR} characters left)</font>', '5000')"><%=mybean.voucher_narration%></textarea>
														<span id=span_txt_voucher_narration> 5000
															characters</span>
													</div>
												
												<div class="form-element6">
													<label>Notes: </label>
													 <textarea name="txt_voucher_notes" rows="4"
															class="form-control" id="txt_voucher_notes"> <%=mybean.voucher_notes%></textarea>
													 </div>
<div class="row"></div>
										<% if (mybean.status.equals("Update")&& !(mybean.entry_by == null) && !(mybean.entry_by.equals("")) ) { %>
										<div class="form-element6">
													<label style="">Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
														<input name="entry_by" type="hidden" id="entry_by"
															value="<%=mybean.entry_by%>">
													</div>
										<%}%>
										<% if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals("")) ) { %>
										<div class="form-element6">
														<label>Entry Date: <%=mybean.entry_date%></label>
															<input type="hidden" name="entry_date"
																value="<%=mybean.entry_date%>">
														</div>
													 
										<%}%>
										<% if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals("")) ) { %>
										<div class="form-element6">
													<label>Modified By: <%=mybean.unescapehtml(mybean.modified_by)%></label>
														<input name="modified_by" type="hidden" id="modified_by"
															value="<%=mybean.modified_by%>">
													</div>
										<%}%>
										<% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
										<div class="form-element6">
													<label>Modified	Date:  <%=mybean.modified_date%>
														<input type="hidden" name="modified_date"
															value="<%=mybean.modified_date%>">
										</div>
										<%}%>
										 
													<center>
														<%if(mybean.status.equals("Add")){%>
														<input name="addbutton" type="submit"
															class="btn btn-success" id="addbutton" value="Add Contra"
															onclick="return SubmitFormOnce(document.form1,this);" />
														<input type="hidden" name="add_button" value="yes">
															<%}else if (mybean.status.equals("Update")){%> <input
															type="hidden" name="update_button" value="yes"> <input
																name="updatebutton" type="submit"
																class="btn btn-success" id="updatebutton"
																value="Update Contra"
																onclick="return SubmitFormOnce(document.form1,this);" />
																<input name="delete_button" type="submit"
																class="btn btn-success" id="delete_button"
																OnClick="return confirmdelete(this)"
																value="Delete Contra" /> <%}%>
													</center>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
</body>
</HTML>
