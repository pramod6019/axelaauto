<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Stock_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>


</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%>
							Stock
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
							<li><a href="preowned-stock.jsp">Stock</a> &gt;</li>
							<li><a href="preowned-stock-list.jsp?all=yes">List Stock</a></li>
							<li><a
								href="preowned-stock-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
									Stock</a>:</li>
						</ul>
						<!----- END PAGE BREADCRUMBS -->


						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="form1" method="post" class="form-horizontal">

								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>

								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<%=mybean.status%>&nbsp;<%=mybean.ReturnPreOwnedName(request)%>
											Stock
										</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
												<font>Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.<br>
											</center>
											</br>
											<div class="form-element6">
												<label>Pre Owned: </label>
												<input type="hidden" id="txt_preowned_title" name="txt_preowned_title"
													value="<%=mybean.preowned_title%>" /> 
												<a href="preowned-list.jsp?preowned_id=<%=mybean.preowned_id%>"><%=mybean.preowned_title%>
													(<%=mybean.preowned_id%>)</a>
											</div>

											<div class="form-element6">
												<label> Model: </label>
												<input type="hidden" id="txt_preownedmodel_name" name="txt_preownedmodel_name"
													value="<%=mybean.preownedmodel_name%>" />
												<input type="hidden" id="txt_preownedmodel_id" name="txt_preownedmodel_id"
													value="<%=mybean.preownedmodel_id%>" />
												<a href="managepreownedmodel.jsp?preownedmodel_id=<%=mybean.preownedmodel_id%>"><%=mybean.preownedmodel_name%>
													(<%=mybean.preownedmodel_id%>)</a>
											</div>

											<div class="form-element6">
												<label>Variant: </label>
												<input type="hidden" id="txt_variant_name" name="txt_variant_name"
													value="<%=mybean.variant_name%>" />
												<input type="hidden" id="txt_variant_id" name="txt_variant_id"
													value="<%=mybean.variant_id%>" />
												<a href="managepreownedvariant.jsp?variant_id=<%=mybean.variant_id%>"><%=mybean.variant_name%>
													(<%=mybean.variant_id%>)</a>
											</div>

											<div class="form-element6">
												<label>Date<b><font color="#ff0000">*</font></b>: </label>
												<%
													if (mybean.ReturnPerm(mybean.comp_id, "emp_role_id", request).equals("1")) {
												%>
												<input name="txt_preownedstock_date" id="txt_preownedstock_date"
													value="<%=mybean.preownedstock_date%>" maxlength="10" 
													class="form-control datepicker" type="text" size="11" />
												<% } else { %>
												<input name="txt_preownedstock_date" id="txt_preownedstock_date" value="<%=mybean.preownedstock_date%>" type="hidden"  />
												<%=mybean.preownedstock_date%>
												<% } %>
											</div>


											<div class="form-element6">
												<label>Put To Sale Date<b><font color="#ff0000">*</font></b>: </label>
												<input name="txt_preownedstock_puttosale_date"
													id="txt_preownedstock_puttosale_date"
													class="form-control datepicker" type="text"
													value="<%=mybean.preownedstock_puttosale_date%>" size="11"
													maxlength="10" /> 
											</div>

											<div class="form-element6">
												<label>Status<b><font color="#ff0000">*</font></b>: </label>
												<select name="dr_preownedstock_status" class="form-control" id="dr_preownedstock_status">
													<%=mybean.PopulatePreownedStockStatus()%>
												</select>
											</div>
											<div class="row">
												<div class="form-element6">
													<label> Type<b><font color="#ff0000">*</font></b>: </label>
													<select name="dr_preownedstock_preownedtype"
														class="form-control" id="dr_preownedstock_preownedtype">
														<%=mybean.PopulatePreownedType()%>
													</select>

												</div>

												<div class="form-element6">
													<label>Purchase Amount<b><font color="#ff0000">*</font></b>: </label>
													<input name="txt_preownedstock_purchase_amt" id="txt_preownedstock_purchase_amt"
														onKeyUp="toFloat('txt_preownedstock_purchase_amt','amt')"
														type="text" class="form-control" size="20" 	maxlength="15"
														value="<%=mybean.preownedstock_purchase_amt%>" /> 
												</div>
											</div>
											<div class="form-element6">
												<label>Refurbishment Amount:</label>
												<input name="txt_preownedstock_refurbish_amt" id="txt_preownedstock_refurbish_amt"
													onKeyUp="toInteger('txt_preownedstock_refurbish_amt','amt')"
													type="text" class="form-control" maxlength="10"
													value="<%=mybean.preownedstock_refurbish_amt%>" size="20" />

											</div>

											<div class="form-element6">
												<label> Selling Price<b><font color="#ff0000">*</font></b>: </label>
												<input name="txt_preownedstock_selling_price"
													id="txt_preownedstock_selling_price"
													onKeyUp="toFloat('txt_preownedstock_selling_price','price')"
													type="text" class="form-control" maxlength="15" 
													value="<%=mybean.preownedstock_selling_price%>" size="20" /> 
											</div>

											<div class="form-element6">
												<label>Engine Number<b><font color="#ff0000">*</font></b>: </label>
												<input name="txt_preownedstock_engine_no"
													id="txt_preownedstock_engine_no" type="text" class="form-control"
													value="<%=mybean.preownedstock_engine_no%>" size="20" maxlength="20" /> 
											</div>

											<div class="form-element6">
												<label>Chassis Number<b><font color="#ff0000">*</font></b>: </label>
												<input name="txt_preownedstock_chassis_no"
													id="txt_preownedstock_chassis_no" type="text" class="form-control"
													value="<%=mybean.preownedstock_chassis_no%>" size="20" maxlength="20" /> 
											</div>

											<!-- <div class="form-group"> -->
											<!-- <label>Commission Number:</label> -->
											<!-- <div class="col-md-6 col-xs-12" id="emprows"> -->
											<%-- <input name ="txt_preownedstock_comm_no" id ="txt_preownedstock_comm_no" type="text" class="form-control" value="<%=mybean.preownedstock_comm_no%>" size="20" maxlength="20"/> --%>

											<!-- </div> -->
											<!-- </div> -->

											<div class="form-element6">
												<label>Trade-in Contract:</label> <input
													id="chk_preownedstock_check_tradein_contract"
													type="checkbox"
													name="chk_preownedstock_check_tradein_contract"
													<%=mybean.PopulateCheck(mybean.preownedstock_check_tradein_contract)%> />
											</div>

											<div class="form-element6">
												<label>R.C.(Original):</label> <input
													id="chk_preownedstock_check_original_rc" type="checkbox"
													name="chk_preownedstock_check_original_rc"
													<%=mybean.PopulateCheck(mybean.preownedstock_check_original_rc)%> />
											</div>
											<div class="row">
												<div class="form-element12">
													<label>Insurance(Policy + Cover Note) Minimum Third
														Party insurance required in case of NCB/insurance transfer
														case and application for insurance transfer: </label> <input
														id="chk_preownedstock_check_insurance" type="checkbox"
														name="chk_preownedstock_check_insurance"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_insurance)%> />
												</div>
											</div>
											<div class="form-element6">
												<div>
													<b>Forms:</b>
												</div>
												<div class="row">
													<div class="form-element3 ">
														<label>a. 28*3:</label> <input
															id="chk_preownedstock_check_forms_283" type="checkbox"
															name="chk_preownedstock_check_forms_283"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_forms_283)%> />
													</div>
												</div>
												<div class="row">
													<div class="form-element3">
														<label>b. 29*2:</label> <input
															id="chk_preownedstock_check_forms_292" type="checkbox"
															name="chk_preownedstock_check_forms_292"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_forms_292)%> />
													</div>
												</div>
												<div class="row">
													<div class="form-element3">
														<label>c. 30*2:</label> <input
															id="chk_preownedstock_check_forms_302" type="checkbox"
															name="chk_preownedstock_check_forms_302"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_forms_302)%> />
													</div>
												</div>
												<div class="row">
													<div class="form-element8">
														<label>d. Form 35*2 with NOC of Bank in case of
															Hypothecation: </label>
														<input id="chk_preownedstock_check_forms_352" type="checkbox"
															name="chk_preownedstock_check_forms_352"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_forms_352)%> />
													</div>
												</div>
											</div>

											<div class="form-element6">
												<label> <b>Affidavits:</b>
												</label>
												<div class="row">
													<div class="form-element6">
														<label>a. For Sale: </label> <input
															id="chk_preownedstock_check_aff_sale" type="checkbox"
															name="chk_preownedstock_check_aff_sale"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_aff_sale)%> />
													</div>
												</div>
												<div class="row">
													<div class="form-element6">
														<label>b. For Hypothecation Removal for R/C: </label> <input
															id="chk_preownedstock_check_aff_hypo" type="checkbox"
															name="chk_preownedstock_check_aff_hypo"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_aff_hypo)%> />
													</div>
												</div>
												<div class="row">
													<div class="form-element6">
														<label>c. For NOC From RTO: </label> <input
															id="chk_preownedstock_check_aff_noc" type="checkbox"
															name="chk_preownedstock_check_aff_noc"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_aff_noc)%> />
													</div>
												</div>
												<div class="row">
													<div class="form-element6">
														<label>d. Power of Attorney: </label> <input
															id="chk_preownedstock_check_aff_poa" type="checkbox"
															name="chk_preownedstock_check_aff_poa"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_aff_poa)%> />

													</div>
												</div>
												<div class="row">
													<div class="form-element6">
														<label>Three Photographs: </label> <input
															id="chk_preownedstock_check_photographs" type="checkbox"
															name="chk_preownedstock_check_photographs"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_photographs)%> />
													</div>
												</div>
											</div>
											<div class=" form-element3 form-element-center ">
												<b>Residence/Address Proof Copies:</b>
											</div>
											<div class="form-element6">

												<div class="row">
													<div class="form-element6 ">
														<label>a. Ration Card/Voter Card/PassPort:</label> <input
															id="chk_preownedstock_check_rationcard" type="checkbox"
															name="chk_preownedstock_check_rationcard"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_rationcard)%> />
													</div>
												</div>
												<div class="row">
													<div class="form-element6">
														<label>b. Telephone/Electricity/Water Bill: </label> <input
															id="chk_preownedstock_check_telebill" type="checkbox"
															name="chk_preownedstock_check_telebill"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_telebill)%> />
													</div>
												</div>
												<div class="row">
													<div class="form-element8">
														<label>PAN No. Copy(self attested) or form 60 &
															61(2 copies each): </label> 
															<input id="chk_preownedstock_check_pancopy" type="checkbox"
															name="chk_preownedstock_check_pancopy"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_pancopy)%> />

													</div>
												</div>
												<div class="row">
													<div class="form-element6">
														<label>Mileage Verification Certificate: </label> <input
															id="chk_preownedstock_check_mileage_verification"
															type="checkbox"
															name="chk_preownedstock_check_mileage_verification"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_mileage_verification)%> />
													</div>
												</div>
											</div>
											<div class="form-element6 ">
												<div class="row">
													<div class="form-element12">
														<label>Second Key Of Car,Tool Kit,Spare
															Wheel,Service Book,Chassis Print:</label> <input
															id="chk_preownedstock_check_servicebook" type="checkbox"
															name="chk_preownedstock_check_servicebook"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_servicebook)%> />
													</div>
												</div>
												<div class="row">
													<div class="form-element12">
														<label>Authority Letter For NOC From Bank in case
															of Finance:</label> <input
															id="chk_preownedstock_check_noc_authority"
															type="checkbox"
															name="chk_preownedstock_check_noc_authority"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_noc_authority)%> />
													</div>
												</div>
												<div class="row">
													<div class="form-element12">
														<label>F.O.P Report For Registration From
															Authority and N.C.R.B Report From Police Deptt.: </label> <input
															id="chk_preownedstock_check_fop_ncrb" type="checkbox"
															name="chk_preownedstock_check_fop_ncrb"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_fop_ncrb)%> />
													</div>
												</div>
												<div class="row">
													<div class="form-element12">
														<label>Copy of Partnership Deed if car is of
															company & having Partnership & no. objection by remaining
															partners for selling Company's Car: </label> <input
															id="chk_preownedstock_check_partnerdeed" type="checkbox"
															name="chk_preownedstock_check_partnerdeed"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_partnerdeed)%> />
													</div>
												</div>
											</div>
											<div class="row">
												<div class="form-element12">
													<label>Original Board Resolution & Memorandum of
														Article if Company is limited/Private Limited & 3 Original
														copies of Authorization Letter By Remaining Director for
														selling Company's Car duly signed & stamped: </label> <input
														id="chk_preownedstock_check_moa" type="checkbox"
														name="chk_preownedstock_check_moa"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_moa)%> />
												</div>
											</div>

											<div class="row">
												<div class="form-element6">
													<label>Location<b><font color="#ff0000">*</font></b>:
													</label> <select name="dr_preownedstock_preownedlocation"
														class="form-control"
														id="dr_preownedstock_preownedlocation">
														<%=mybean.PopulatePreownedLocation()%>
													</select>
												</div>

												<div class="form-element6">
													<label>SO ID:</label> <input name="txt_preownedstock_so_id"
														type="text" class="form-control"
														id="txt_preownedstock_so_id"
														onKeyUp="toInteger('txt_preownedstock_so_id','so_id')"
														value="<%=mybean.preownedstock_so_id%>" size="11"
														maxlength="10" />
												</div>
											</div>

											<div class="form-element6 form-element">
												<div class="form-element12">
													<label> Blocked: </label> <input
														id="chk_preownedstock_blocked" type="checkbox"
														name="chk_preownedstock_blocked"
														<%=mybean.PopulateCheck(mybean.preownedstock_blocked)%> />
												</div>
												<div class="form-element12">
													<label>Pre-Owned Consultant<b><font color="#ff0000">*</font></b>: </label>
													<select name="dr_preownedstock_executive"
														class="form-control" id="dr_preownedstock_executive">
														<%=mybean.PopulatePreownedStockExecutive()%>
													</select>

												</div>
											</div>

											<div class="form-element6">
												<label> Notes:</label>
												<textarea name="txt_preownedstock_notes" cols="40" rows="4"
													class="form-control" id="txt_preownedstock_notes"><%=mybean.preownedstock_notes%></textarea> 
											</div>



											<%
												if (mybean.status.equals("Update") && !(mybean.stock_entry_by == null) && !(mybean.stock_entry_by.equals(""))) {
											%>
											<div class="form-element6">
												<label>Entry By: </label>
												<%=mybean.unescapehtml(mybean.stock_entry_by)%>
											</div>
											<%
												}
											%>

											<%
												if (mybean.status.equals("Update") && !(mybean.preownedstock_entry_date == null) && !(mybean.preownedstock_entry_date.equals(""))) {
											%>
											<div class="form-element6">
												<label> Entry Date:</label>
												<%=mybean.preownedstock_entry_date%>
											</div>
											<%
												}
											%>

											<%
												if (mybean.status.equals("Update") && !(mybean.stock_modified_by == null) && !(mybean.stock_modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label> Modified By: </label>
												<%=mybean.unescapehtml(mybean.stock_modified_by)%>
											</div>
											<%
												}
											%>


											<%
												if (mybean.status.equals("Update") && !(mybean.preownedstock_modified_date == null) && !(mybean.preownedstock_modified_date.equals(""))) {
											%>
											<div class="form-element6">
												<label>Modified Date:</label>
												<%=mybean.preownedstock_modified_date%>
											</div>
											<%
												}
											%>

											<%
												if (mybean.status.equals("Add")) {
											%>
											<div class="form-element12">
												<center>
													<input name="addbutton" type="submit"
														class="button btn btn-success" id="addbutton" value="Add Stock"
														onclick="return SubmitFormOnce(document.form1,this);" />
													<input type="hidden" name="add_button" value="yes" />
												</center>
												<%
													} else if (mybean.status.equals("Update")) {
												%>
												<input type="hidden" name="update_button" value="yes" />
												<center>
													<input name="updatebutton" type="submit"
														class="button btn btn-success" id="updatebutton" value="Update Stock"
														onclick="return SubmitFormOnce(document.form1,this);" />
													<input name="delete_button" type="submit"
														class="button btn btn-success" id="delete_button"
														OnClick="return confirmdelete(this)" value="Delete Stock" />
												</center>
												<%
													}
												%>
												<input type="hidden" name="stock_entry_by"
													value="<%=mybean.stock_entry_by%>"> <input
													type="hidden" name="preownedstock_entry_date"
													value="<%=mybean.preownedstock_entry_date%>"> <input
													type="hidden" name="stock_modified_by"
													value="<%=mybean.stock_modified_by%>"> <input
													type="hidden" name="preownedstock_modified_date"
													value="<%=mybean.preownedstock_modified_date%>">
											</div>
										</div>
									</div>
								</div>


							</form>



						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	<%@ include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>


</body>
</HTML>
