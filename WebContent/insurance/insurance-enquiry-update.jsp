<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Insurance_Enquiry_Update" scope="request" />
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp"%>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%> Insurnace Enquiry </h1>
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
						<li><a href="../insurance/enquiry.jsp">Insurnace Enquiry</a> &gt;</li>
						<li><a href="insurance-enquiry-list.jsp?all=yes">List Insurnace Enquiry</a>&gt;</li>
						<li><a href="insurance-enquiry-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Insurnace Enquiry</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Insurnace Enquiry
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="post">
											<center>
												<font size="1">Form fields marked with a red asterisk
												<b> <font color="#ff0000">*</font></b> are required. </font>
											</center>
											<br>
											<div class="tab-pane" id="">
											
											<div class="form-element6">
														<label> Customer:&nbsp; </label>
															<b>
<!-- 															<div type="text" id="link_customer_name" name="link_customer_name"  -->
<%-- 															  value="<%=mybean.link_customer_name%>" hidden ></div> --%>
															<span id="span_insurenquiry_customer_id" name="span_insurenquiry_customer_id">
																<%=mybean.link_customer_name%>
															</span></b>&nbsp;
												</div>

												<div class="form-element6">
														<label> Contact:&nbsp; </label>
															<b>
															<span id="span_insurenquiry_contact_id"
																name="span_insurenquiry_contact_id"><%=mybean.link_contact_name%></span></b>&nbsp;
												</div>
											
											<div class="row">
											<div class="form-element6">
												<label> Branch<font color="#ff0000">*</font>:&nbsp; </label>
													<select name="dr_branch" class="form-control" id="dr_branch" onchange="PopulateCampaign(this.value);">
														<%=mybean.PopulateBranch(mybean.insurenquiry_branch_id, "", "6", "", request)%>
													</select>
											</div>
											
											<div class="form-element6">
														<label> Model<font color="#ff0000">*</font>:&nbsp; </label>
														<select class="form-control select2" id="preownedvariant" name="preownedvariant">
															<%=mybean.modelcheck.PopulateVariant(mybean.insurenquiry_variant_id)%>
														</select>
											</div>
											</div>
											
											<div class="row">
											<div class="form-element6 ">
														<label> Model Year<font color="#ff0000">*</font>:&nbsp; </label>
															<input name="txt_insurenquiry_modelyear" id="txt_insurenquiry_modelyear"
															 type="text" class="form-control "
																value="<%=mybean.insurenquiry_modelyear%>" size="10" maxlength="4" /> 
											</div>
											
											<div class="form-element6 ">
														<label> Chassis Number:&nbsp; </label>
															<input name="txt_insurenquiry_chassis_no" id="txt_insurenquiry_chassis_no"
																type="text" class="form-control" value="<%=mybean.insurenquiry_chassis_no%>"
																size="20" maxlength="25" /> 
											</div>
											</div>
											
											<div class="form-element6 ">
														<label> Engine Number:&nbsp; </label>
															<input name="txt_insurenquiry_engine_no" id="txt_insurenquiry_engine_no" type="text" class="form-control"
															value="<%=mybean.insurenquiry_engine_no%>" size="20" maxlength="25" /> 
											</div>
											
											<div class="form-element6 ">
												<label> Reg. Number:&nbsp; </label>
															<input name="txt_insurenquiry_reg_no" id="txt_insurenquiry_reg_no" type="text" class="form-control"
																value="<%=mybean.insurenquiry_reg_no%>" size="20" maxlength="20" /> 
											</div>

											<div class="form-element6 ">
														<label> Sale Date<b>:</b>&nbsp; </label>
															<input name="txt_insurenquiry_sale_date" type="text" class="form-control datepicker"
																data-date-format="dd/mm/yyyy" id="txt_insurenquiry_sale_date"
																value="<%=mybean.insurenquirysaledate%>" size="12" maxlength="10" /> 
											</div>
											
											<div class="form-element6 ">
														<label>Insurance Executive<font color="#ff0000">*</font>:&nbsp; </label>
															<select id="dr_insurenquiry_insuremp_id" name="dr_insurenquiry_insuremp_id" class="form-control">
																<%=mybean.PopulateInsurExecutive(mybean.comp_id)%>
																</select> 
											</div>
											
											<div class="form-element6 ">
														<label>Insurance Renewal Date:&nbsp;</label>
															<input name="txt_insurenquiry_renewal_date" id="txt_insurenquiry_renewal_date" type="text"
																class="form-control datepicker" data-date-format="dd/mm/yyyy"
																value="<%=mybean.insurenquiry_renewal_date%>" size="12" maxlength="10" /> 
											</div>
											
											<div class="form-element6 ">
														<label>Insurance Type<font color="#ff0000">*</font>:&nbsp; </label>
															<select id="dr_insurenquiry_insurtype_id" name="dr_insurenquiry_insurtype_id" class="form-control">
																<%=mybean.PopulateInsurType(mybean.comp_id)%>
															</select> 
											</div>
											
											<div class="form-element6 ">
														<label>Previous Insurance Company Name<b>:&nbsp;</b> </label>
															<input name="txt_insurenquiry_previouscompname" id="txt_insurenquiry_previouscompname"
																 type="text" class="form-control"
																 value="<%=mybean.insurenquiry_previouscompname%>" size="25" maxlength="20" />
											</div>
											
											
											<div class="form-element6 ">
														<label>Previous Gross Premium<b>:&nbsp;</b> </label>
															<input name="txt_insurenquiry_previousgrosspremium" id="txt_insurenquiry_previousgrosspremium"
																 type="text" class="form-control"
																  value="<%=mybean.insurenquiry_previousgrosspremium%>" size="25" maxlength="20" />
											</div>
											
											<div class="row">
											<div class="form-element6 ">
														<label>Previous Plan Name<b>:&nbsp;</b> </label>
															<input name="txt_insurenquiry_previousplanname" id="txt_insurenquiry_previousplanname"
																 type="text" class="form-control"
																 value="<%=mybean.insurenquiry_previousplanname%>" size="25" maxlength="20" />
											</div>
											
											<div class="form-element6 ">
														<label>Insurance Policy Expiry Date:&nbsp;</label>
															<input name="txt_insurenquiry_policyexpirydate" id="txt_insurenquiry_policyexpirydate" type="text"
																class="form-control datepicker" data-date-format="dd/mm/yyyy"
																value="<%=mybean.insurenquiry_policyexpirydate%>" size="12" maxlength="10" /> 
											</div>
											</div>
											
											<div class="form-element6 ">
														<label>Insurance Current IDV<b>:&nbsp;</b> </label>
															<input name="txt_insurenquiry_currentidv" id="txt_insurenquiry_currentidv"
																 type="text" class="form-control"
																 value="<%=mybean.insurenquiry_currentidv%>" size="25" maxlength="20" />
											</div>
											
											<div class="form-element6 ">
														<label>Premium<b>:&nbsp;</b> </label>
															<input name="txt_insurenquiry_premium" id="txt_insurenquiry_premium"
																 type="text" class="form-control"
																 value="<%=mybean.insurenquiry_premium%>" size="25" maxlength="20" />
											</div>
											
											<div class="form-element6 ">
														<label>Premium With Zero Deposite<b>:&nbsp;</b> </label>
															<input name="txt_insurenquiry_premiumwithzerodept" id="txt_insurenquiry_premiumwithzerodept"
																 type="text" class="form-control"
																 value="<%=mybean.insurenquiry_premiumwithzerodept%>" size="25" maxlength="20" />
											</div>
											
											<div class="form-element6 ">
														<label>Company Offered<b>:&nbsp;</b> </label>
															<input name="txt_insurenquiry_compoffered" id="txt_insurenquiry_compoffered"
																 type="text" class="form-control"
																 value="<%=mybean.insurenquiry_compoffered%>" size="25" maxlength="20" />
											</div>
											
											<div class="form-element6 ">
														<label>Plan Suggested<b>:&nbsp;</b> </label>
															<input name="txt_insurenquiry_plansuggested" id="txt_insurenquiry_plansuggested"
																 type="text" class="form-control"
																 value="<%=mybean.insurenquiry_plansuggested%>" size="25" maxlength="20" />
											</div>
											
											<div class="form-element6 ">
														<label>NCB<b>:&nbsp;</b> </label>
															<input name="txt_insurenquiry_ncb" id="txt_insurenquiry_ncb"
																 type="text" class="form-control"
																 value="<%=mybean.insurenquiry_ncb%>" size="25" maxlength="20" />
											</div>
											
											<div class= "row">
											<div class="form-element6 ">
														<label>Address<b>:&nbsp;</b></label>
															<textarea name="txt_insurenquiry_address" cols="70" rows="5"
																class="form-control" id="txt_insurenquiry_address" onKeyUp="charcount('txt_insurenquiry_address', 'span_txt_insurenquiry_address','<font color=red>({CHAR} characters left)</font>', '255')" > <%=mybean.insurenquiry_address%></textarea>
														<span id="span_txt_insurenquiry_address"> (255 Characters)</span>
											</div>
											
											<div class="form-element6 ">
														<label>Source of Enquiry<font color="#ff0000">*</font>:&nbsp;</label>
															<select name="dr_insur_soe_id" id="dr_insur_soe_id"
																class="dropdown form-control" onchange="populateSob();">
																<%=mybean.PopulateSoe(mybean.comp_id)%>
															</select>
											</div>
											
											<div class="form-element6">
												<label> Source of Business<font color="#ff0000">*</font>:&nbsp; </label>
												<span id="dr_insur_sob_id">
													<select name="dr_insur_sob_id" id="dr_insur_sob_id" class="dropdown form-control">
														<%=mybean.PopulateSOB()%>
													</select>
												</span>
											</div>
											</div>
												
											<div class="form-element6">
												<label> Campaign<font color="#ff0000">*</font>:&nbsp; </label>
													<span id="insurcampaign">
														<%=mybean.PopulateCampaign(mybean.insurenquiry_branch_id, mybean.comp_id)%>
													</span>
											</div>
											
											<div class="row">
											<div class="form-element6 ">
														<label>Notes:&nbsp; </label>
															<textarea name="txt_insurenquiry_notes" cols="70" rows="5"
																class="form-control" id="txt_insurenquiry_notes"><%=mybean.insurenquiry_notes%></textarea> 
											</div>
											</div>
											
											<div class="row">
											<% if (mybean.status.equals("Update") && !(mybean.insurenquiry_entry_by == null)
													&& !(mybean.insurenquiry_entry_by.equals(""))) { %>
											<div class="form-element6 ">
														<label > Entry By:&nbsp; </label>
															<%=mybean.unescapehtml(mybean.insurenquiry_entry_by)%> 
											</div>
											<% } %>
											
											<% if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) { %>
											<div class="form-element6 ">
														<label> Entry Date:&nbsp; </label>
															<%=mybean.entry_date%>
											</div>
											<% } %>
											
											</div>
											
											<div class="row">
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-element6 ">
														<label> Modified Date:&nbsp; </label>
															<%=mybean.modified_date%> 
											</div>
											<%
												}
											%>
											
											<% if (mybean.status.equals("Update") && !(mybean.insurenquiry_modified_by == null)
														&& !(mybean.insurenquiry_modified_by.equals(""))) { %>
											<div class="form-element6 ">
														<label> Modified By:&nbsp; </label>
															<%=mybean.unescapehtml(mybean.insurenquiry_modified_by)%> 
											</div>
											<%
												}
											%>
											
											</div>
											<%
												if (mybean.status.equals("Update")) {
											%>
											<center>
												<input type="hidden" id="update_button" name="update_button" value="yes" />
												<input name="updatebutton" type="submit" class="btn btn-success"
												 id="updatebutton" value="Update Insurance Enquiry"
													onclick="return SubmitFormOnce(document.form1,this);" />
												<input name="delete_button" type="submit" class="btn btn-success" id="delete_button"
													 onclick="return confirmdelete(this)" value="Delete Insurance Enquiry" />
											</center>
											<%
												}
											%>
											<input type="hidden" name="veh_entry_by" value="<%=mybean.insurenquiry_entry_by%>" />
											<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>" />
											<input type="hidden" name="veh_modified_by" value="<%=mybean.insurenquiry_modified_by%>" />
											<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>" />
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
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript">
 
		function populateSob() {
			var insur_soe_id = document.getElementById('dr_insur_soe_id').value;
			showHint( "../insurance/mis-check.jsp?insurfollowup_sob_id=yes&insurfollowup_soe_id=" + insur_soe_id, "dr_insur_sob_id");
		}
		function PopulateCampaign(branch_id) {
			showHint("../insurance/mis-check.jsp?insurenquiry_branch_id=" + branch_id + "&insurcampaign=yes", "insurcampaign");
		}
	</script>
</body>
</HTML>