<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Inventory_Item_Update" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body onLoad="Displaypaymode();FormFocus();populateAsterisk();" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%>&nbsp;Item
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
						<li><a href="../portal/home.jsp">Home</a> &gt;<a
							href="index.jsp">Inventory</a> &gt;</li>
						<li><a href="inventory-item-list.jsp?all=yes">List Items</a>
							&gt;</li>
						<li><a
							href="inventory-item-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Item</a>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="form1" method="post" class="form-horizontal">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"><%=mybean.status%>&nbsp;Item</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
												<font size="">Form fields marked with a red asterisk
													<b><font color="#ff0000">*</font></b> are required.<br>
												</font>
											</center>
											<br>
											
											<div class="form-element6">
												<label> Name<font color="#ff0000">*</font>: </label>
													<input name="txt_item_name" type="text"
														class="form-control" id="txt_item_name"
														value="<%=mybean.item_name%>" size="50" maxlength="255" />
											</div> 
											

											<div class="form-element6">
												<label>Item Code: </label>
													<input name="txt_item_code" type="text"
														class="form-control" id="txt_item_code"
														value="<%=mybean.item_code%>" size="20" maxlength="20" />
											</div>

											<div class="form-element6">
												<label>Service Code: </label>
													<input name="txt_item_service_code" type="text"
														class="form-control" id="txt_item_service_code"
														value="<%=mybean.item_service_code%>" size="50"
														maxlength="50" />

											</div>
												<div class="form-element6">
												<label>HSN: </label>
													<input name="txt_item_hsn" type="text"
														class="form-control" id="txt_item_hsn"
														value="<%=mybean.item_hsn%>" size="50"
														maxlength="50" />

											</div>
												<div class="form-element6">
												<label>SAC: </label>
													<input name="txt_item_sac" type="text"
														class="form-control" id="txt_item_sac"
														value="<%=mybean.item_sac%>" size="50"
														maxlength="50" />

											</div>

											<div class="form-element6">
												<label> Category<font color="#ff0000">*</font>:
												</label>
													<select name="dr_item_cat_id" id="dr_item_cat_id"
														class="form-control">
														<option value=0>Parent Level</option>
														<%=mybean.PopulateCategoryPop(mybean.item_cat_id, mybean.comp_id, "0", "1", request)%>
													</select>
											</div>

											<div class="form-element6">
												<label>Type<font color="#ff0000">*</font>:
												</label>
													<select id="dr_item_type_id" name="dr_item_type_id"
														class="form-control" onchange="populateAsterisk();" visible="true">
														<%=mybean.PopulateItemType(mybean.comp_id, mybean.item_type_id)%>
													</select>

												</div>
											</div>

											<div class="form-element6">
												<label>Option
													Type: </label>
													<select id="dr_optiontype_id" name="dr_optiontype_id"
														class="form-control">
														<%=mybean.PopulateOptions(mybean.comp_id, mybean.item_optiontype_id)%>
													</select>

											</div>

											<div class="form-element6" id="hide1">
												<label >Model<font color="#ff0000">*</font>:
												<span id="star1"></span>
												</label>
													<select name="drop_item_model_id" class="form-control">
														<%=mybean.PopulateModel(mybean.comp_id, mybean.item_model_id)%>
													</select>

											</div>

											<div class="form-element6" >
												<label >Fuel Type<span id="star"><font color="#ff0000">*</font>:</span></label>
													<select id="dr_item_fueltype_id" name="dr_item_fueltype_id"
														class="form-control">
														<%=mybean.PopulateFuelType(mybean.comp_id, mybean.item_fueltype_id)%>
													</select>

											</div>
											
                                           <div class="row">
											<div class="form-element6">
												<label>
													Small Description: </label>
													<textarea name="txt_item_small_desc" cols="50" rows="5"
														class="form-control" id="txt_item_small_desc"
														onkeyup="charcount('txt_item_small_desc', 'span_txt_item_small_desc','<font color=red>({CHAR} characters left)</font>', '2000')"><%=mybean.item_small_desc%></textarea>
													<span id=span_txt_item_small_desc> 2000 characters </span>
											</div>

											<div class="form-element6">
												<label> Big
													Description: </label>
													<textarea name="txt_item_big_desc" cols="50" rows="5"
														class="form-control" id="txt_item_big_desc"
														onkeyup="charcount('txt_item_big_desc', 'span_txt_item_big_desc','<font color=red>({CHAR} characters left)</font>', '2000')"><%=mybean.item_big_desc%></textarea>
													<span id=span_txt_item_big_desc> 2000 characters</span>
											</div>

											<div class="form-element6">
												<label> URL: </label>
													<input name="txt_item_url" type="text" class="form-control"
														id="txt_item_url" value="<%=mybean.item_url%>" size="72"
														maxlength="255" />

											</div>

											<div class="form-element6 form-element-margin">
												<label>
													Serial: </label>
													<input id="chk_item_serial" type="checkbox" name="chk_item_serial"
														<%=mybean.PopulateCheck(mybean.item_serial)%> />
												
											</div>
											</div>
											<div class="form-element6">
												<label >Unit
													Of Measurement<font color="#ff0000">*</font>:
												</label>
													<select name="dr_item_uom_id" id="dr_item_uom_id" class="form-control"
														onChange="showHint('inventory-altuom-check.jsp?uom_id=' + GetReplace(this.value),'dr_item_alt_uom_id')">
														<%=mybean.PopulateUOM(mybean.comp_id, mybean.item_uom_id)%>
													</select>
											</div>

											<div class="form-element6">
												<label>Alternative
													Unit Of Measurement<font color="#ff0000">*</font>:
												</label>
													<span id="dr_item_alt_uom_id">
														<%=mybean.PopulateAltUOM(mybean.comp_id, mybean.item_uom_id)%>
													</span>
											</div>
									<div class="row">
											<div class="form-element6">
												<label>Economic
													Order Quantity:</label>
													<input name="txt_item_eoq" type="text" class="form-control"
														id="txt_item_eoq" value="<%=mybean.item_eoq%>" size="10"
														maxlength="10" />

											</div>
                                   
											<div class="form-element2 form-element-margin">
												<label>Perishable:</label>
													<input id="chk_item_perishable" type="checkbox"
														name="chk_item_perishable"
														<%=mybean.PopulateCheck(mybean.item_perishable)%> />
											</div>
                                  
											<div class="form-element2 form-element-margin">
												<label> Non
													Stock: </label>
													<input id="chk_item_nonstock" type="checkbox"
														name="chk_item_nonstock"
														<%=mybean.PopulateCheck(mybean.item_nonstock)%> />
											</div>
											
											<div class="form-element2 form-element-margin">
												<label >After
													Tax Calculation: </label>
													<input id="chk_item_aftertaxcal" type="checkbox"
														name="chk_item_aftertaxcal"
														<%=mybean.PopulateCheck(mybean.item_aftertaxcal)%>
														onchange="GetTaxFormulae();" />
											
											</div>

                                     </div>
                                     <div class="row">
											
											<div class="form-element6">
												<label >Tax
													Calculation Formulae<font color="#ff0000">*</font>:
												</label>
<!-- 													<tr valign="middle" id="tax_formulae"> -->
														<textarea name="txt_item_aftertaxcal_formulae" cols="70"
															rows="4" class="form-control"
															id="txt_item_aftertaxcal_formulae"><%=mybean.item_aftertaxcal_formulae%></textarea>
														Substitution Variable for Ex-Showroom Price is exprice.
											</div>
			
											<div class="form-element6">
												<label >
													Notes: </label>
													<textarea name="txt_item_notes" cols="70" rows="4"
														class="form-control" id="txt_item_notes"><%=mybean.item_notes%></textarea>

											</div>

											<div class="form-element12">
												<label>Active:</label>
													<input id="chk_item_active" type="checkbox"
														name="chk_item_active"
														<%=mybean.PopulateCheck(mybean.item_active)%> />

											</div>
</div>
									</div>
								</div>


								<!-- Start sales price								 -->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Sales Price</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<%
									if (mybean.status.equals("Add")) {
											%>
										<div class="row">	
											<div class="form-element6">
												<label>
													Sales Rate Class: </label>
													<select id='sales_rateclass_id' name='sales_rateclass_id'
														class='form-control'>
														<%=mybean.PopulateSalesBranchClass(mybean.comp_id, mybean.salesrateclass_id)%>
													</select>
											</div>


											  <div class="form-element6">
												<label >Sales
													Price: </label>
													<input name="txt_sales_price_amt" type="text"
														class="form-control" id="txt_sales_price_amt"
														onKeyUp="toNumber('txt_sales_price_amt','Amount')"
														value="<%=mybean.sales_price_amt%>" size="12"
														maxlength="10" />

											 </div>
											</div>
											
											<div class="row">	
												<div class="form-element3">
													<label >Maximum
														Discount:</label>
															<input name="txt_sales_price_disc" type="text"
																class="form-control" id="txt_sales_price_disc"
																onKeyUp="toNumber('txt_sales_price_disc','Maximum Discount')"
																value="<%=mybean.sales_price_disc%>" size="12"
																maxlength="10" />
													</div>
														<div class="form-element3 form-element-margin" >
														
															<select id="dr_sales_price_disc_type"
																name="dr_sales_price_disc_type" class="form-control"
																onchange="validatedisc(this.value);">
																<%=mybean.PopulateSalesDiscountType(mybean.comp_id, mybean.sales_price_disc_type)%>
															</select>

														</div>
												
												
											<!-- 											<div class="container-fluid"> -->
											<!-- 												<label class="control-label col-md-4 col-xs-12">Maximum -->
											<!-- 													Discount:</label> -->
											<!-- 												<div class="col-md-4 col-xs-12" > -->
											<!-- 													<input name="txt_sales_price_disc" type="text" -->
											<!-- 														class="form-control" id="txt_sales_price_disc" -->
											<!-- 														onKeyUp="toNumber('txt_sales_price_disc','Maximum Discount')" -->
											<%-- 														value="<%=mybean.sales_price_disc%>" size="12" --%>
											<!-- 														maxlength="10" />  -->

											<!-- 														<select id="dr_sales_price_disc_type" -->
											<!-- 														name="dr_sales_price_disc_type" class="form-control" -->
											<!-- 														onchange="validatedisc(this.value);"> -->
											<%-- 														<%=mybean.PopulateSalesDiscountType(mybean.comp_id, mybean.sales_price_disc_type)%> --%>
											<!-- 													</select> -->

											<!-- 												</div> -->
																						</div>
<%} %>
			
											<div class="form-element6">
												<label >Sales Ledger: </label>
													<select class="form-control select2" id="dr_item_sales_ledger_id" name="dr_item_sales_ledger_id">
																		<%=mybean.ledgercheck.PopulateItemLedgers("0", mybean.item_sales_ledger_id, mybean.comp_id)%>
													</select>
											</div>

											<div class="form-element6">
												<label >Discount Ledger </label>
													<select class="form-control select2" id="dr_item_salesdiscount_ledger_id" name="dr_item_salesdiscount_ledger_id">
																		<%=mybean.ledgercheck.PopulateItemLedgers("0", mybean.item_salesdiscount_ledger_id, mybean.comp_id)%>
													</select>
											</div>

											<div class="form-element6">
													<label >SGST<span id="salessgst"><font color="#ff0000">*</font></span>:</label>
													<select name="dr_item_salestax1_ledger_id" class="form-control"
														id="dr_item_salestax1_ledger_id">
														<%=mybean.PopulateSalesTax(mybean.comp_id, mybean.item_salestax1_ledger_id,"3")%>
													</select>
											</div>

											<div class="form-element6">
												<label>CGST<span id="salescgst"><font color="#ff0000">*</font></span>: </label>
													<select name="dr_item_salestax2_ledger_id" class="form-control"
														id="dr_item_salestax2_ledger_id">
														<%=mybean.PopulateSalesTax(mybean.comp_id, mybean.item_salestax2_ledger_id,"4")%>
													</select>
<!-- 													 Cal. After Tax1: -->
<!-- 													<input type="checkbox" name="chk_item_salestax2_aftertax1" id="chk_item_salestax2_aftertax1" -->
<%-- 														<%=mybean.PopulateCheck(mybean.item_salestax2_aftertax1)%> /> --%>

											</div>

											<div class="form-element6">
												<label>IGST<span id="salesigst"><font color="#ff0000">*</font></span>: </label>
													<select name="dr_item_salestax3_ledger_id" class="form-control"
														id="dr_item_salestax3_ledger_id">
														<%=mybean.PopulateSalesTax(mybean.comp_id, mybean.item_salestax3_ledger_id,"5")%>
													</select>
<!-- 													 Cal. After Tax2:&nbsp; <input type="checkbox" -->
<!-- 														name="chk_item_salestax3_aftertax2" -->
<!-- 														id="chk_item_salestax3_aftertax2" -->
<%-- 														<%=mybean.PopulateCheck(mybean.item_salestax3_aftertax2)%> /> --%>

											</div>
											<div class="form-element6">
												<label>CESS<span id="salescess"><font color="#ff0000">*</font></span>: </label>
													<select name="dr_item_salestax4_ledger_id" class="form-control"
														id="dr_item_salestax4_ledger_id">
														<%=mybean.PopulateSalesTax(mybean.comp_id, mybean.item_salestax4_ledger_id,"6")%>
													</select>
<!-- 													 Cal. After Tax2:&nbsp; <input type="checkbox" -->
<!-- 														name="chk_item_salestax4_aftertax3" -->
<!-- 														id="chk_item_salestax4_aftertax3" -->
<%-- 														<%=mybean.PopulateCheck(mybean.item_salestax4_aftertax3)%> /> --%>

											</div>
										</div>
									</div>


</div>
								<!-- Start purchase price -->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Purchase Price
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
											<%
									if (mybean.status.equals("Add")) {
											%>
											<div class="row">
											<div class="form-element6">
												<label>
													Purchase Rate Class: </label>
													<select id='purchase_rateclass_id'
														name='purchase_rateclass_id' class='form-control'>
														<%=mybean.PopulatePurchaseBranchClass(mybean.comp_id, mybean.purchaserateclass_id)%>
													</select>
											</div>
 </div>
											<div class="form-element6">
												<label>Purchase Price </label>
													<input name="txt_purchase_price_amt" type="text"
														class="form-control" id="txt_purchase_price_amt"
														onKeyUp="toNumber('txt_purchase_price_amt','Amount')"
														value="<%=mybean.purchase_price_amt%>" size="12"
														maxlength="10" />
											</div>
                                         
											<!-- 											<div class="form-group"> -->
											<!-- 												<label class="control-label col-md-4 col-xs-12">Maximum -->
											<!-- 													Discount: </label> -->
											<!-- 												<div class="col-md-6 col-xs-12" > -->
											<!-- 													<input name="txt_purchase_price_disc" type="text" -->
											<!-- 														class="form-control" id="txt_purchase_price_disc" -->
											<!-- 														onKeyUp="toNumber('txt_purchase_price_disc','Maximum Discount')" -->
											<%-- 														value="<%=mybean.purchase_price_disc%>" size="12" --%>
											<!-- 														maxlength="10" />  -->
											<!-- 														<select -->
											<!-- 														id="dr_purchase_price_disc_type" -->
											<!-- 														name="dr_purchase_price_disc_type" class="form-control" -->
											<!-- 														onchange="validatedisc(this.value);"> -->
											<%-- 														<%=mybean.PopulatePurchaseDiscountType(mybean.comp_id, mybean.sales_price_disc_type)%> --%>
											<!-- 													</select> -->

											<!-- 												</div> -->
											<!-- 											</div> -->



                                             <div class="row">
											
												<div class="form-element3">
													<label >Maximum Discount:</label>
															<input name="txt_purchase_price_disc" type="text"
																class="form-control" id="txt_purchase_price_disc"
																onKeyUp="toNumber('txt_purchase_price_disc','Maximum Discount')"
																value="<%=mybean.purchase_price_disc%>" size="12"
																maxlength="10" />
														</div>
														<div class="form-element3 form-element form-element-margin">
															<select id="dr_purchase_price_disc_type"
																name="dr_purchase_price_disc_type" class="form-control"
																onchange="validatedisc(this.value);">
																<%=mybean.PopulatePurchaseDiscountType(mybean.comp_id, mybean.sales_price_disc_type)%>
															</select>
														</div>
															</div>
											<%} %>
										
											
											<div class="form-element6">
												<label >Purchase Ledger: </label>
													<select class="form-control select2" id="dr_item_purchase_ledger_id" name="dr_item_purchase_ledger_id">
																		<%=mybean.ledgercheck.PopulateItemLedgers("0", mybean.item_purchase_ledger_id, mybean.comp_id)%>
													</select>
											</div>
                                           
											<div class="form-element6">
												<label >Discount Ledger: </label>
													<select class="form-control select2" id="dr_item_purchasediscount_ledger_id" name="dr_item_purchasediscount_ledger_id">
																		<%=mybean.ledgercheck.PopulateItemLedgers("0", mybean.item_purchasediscount_ledger_id, mybean.comp_id)%>
													</select>
											</div>

											<div class="form-element6">
												<label>SGST<span id="purchasesgst"><font color="#ff0000">*</font></span>: </label>
													<select name="dr_item_purchasetax1_ledger_id" class="form-control"
														id="dr_item_purchasetax1_ledger_id">
														<%=mybean.PopulatePurchaseTax(mybean.comp_id, mybean.item_purchasetax1_ledger_id,"3")%>
													</select>
											</div>
											
											<div class="form-element6">
												<label>CGST<span id="purchasecgst"><font color="#ff0000">*</font></span>: </label>
													<select name="dr_item_purchasetax2_ledger_id" class="form-control"
														id="dr_item_purchasetax2_ledger_id">
														<%=mybean.PopulatePurchaseTax(mybean.comp_id, mybean.item_purchasetax2_ledger_id,"4")%>
													</select>  
<!-- 													 Cal. After Tax1:&nbsp; -->
<!-- 													<input type="checkbox" name="chk_item_purchasetax2_aftertax1" id="chk_item_purchasetax2_aftertax1" -->
<%-- 														<%=mybean.PopulateCheck(mybean.item_purchasetax2_aftertax1)%> /> --%>
											</div>

											<div class="form-element6">
												<label>IGST<span id="purchaseigst"><font color="#ff0000">*</font></span>: </label>
													<select name="dr_item_purchasetax3_ledger_id" class="form-control"
														id="dr_item_purchasetax3_ledger_id">
														<%=mybean.PopulatePurchaseTax(mybean.comp_id, mybean.item_purchasetax3_ledger_id,"5")%>
													</select>												 
<!-- 													 Cal. After Tax2:&nbsp; -->
<!-- 													 <input type="checkbox" name="chk_item_purchasetax3_aftertax2" id="chk_item_purchasetax3_aftertax2" -->
<%-- 														<%=mybean.PopulateCheck(mybean.item_purchasetax3_aftertax2)%> /> --%>

											</div>
											
											<div class="form-element6">
												<label>CESS<span id="purchasecess"><font color="#ff0000">*</font></span>: </label>
													<select name="dr_item_purchasetax4_ledger_id" class="form-control"
														id="dr_item_purchasetax4_ledger_id">
														<%=mybean.PopulatePurchaseTax(mybean.comp_id, mybean.item_purchasetax4_ledger_id,"6")%>
													</select>												 
<!-- 													 Cal. After Tax2:&nbsp; -->
<!-- 													 <input type="checkbox" name="chk_item_purchasetax4_aftertax3" id="chk_item_purchasetax4_aftertax3" -->
<%-- 														<%=mybean.PopulateCheck(mybean.item_purchasetax4_aftertax3)%> /> --%>

											</div>
											
											<div class="form-element12">
												<label>Variable:</label>
													<input type="checkbox" name="chk_price_variable"
														id="chk_price_variable"
														<%=mybean.PopulateCheck(mybean.price_variable)%> />
											</div>


										
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.item_entry_by == null)
														&& !(mybean.item_entry_by.equals(""))) {
											%>

											<div class="form-element6">
												<label>Entry By:</label>
													<%=mybean.unescapehtml(mybean.item_entry_by)%>
											</div>

											<%
												}
											%>


											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>

											<div class="form-element6">
												<label>Entry
													Date:</label>
												
													<%=mybean.entry_date%>

												
											</div>
											<%
												}
											%>

											<%
												if (mybean.status.equals("Update")
														&& !(mybean.item_modified_by == null)
														&& !(mybean.item_modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label >Modified
													By:</label>
												
													<%=mybean.unescapehtml(mybean.item_modified_by)%>
												
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
												<label >
													Modified Date: </label>
													<%=mybean.modified_date%>
											</div>

											<%
												}
											%>


								<center>
									<%
										if (mybean.status.equals("Add")) {
									%>
									<input name="button2" type="submit"
										class="button btn btn-success" id="button2" value="Add Item"
										onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
									<input type="hidden" name="add_button" value="yes">
									<%
										} else if (mybean.status.equals("Update")) {
									%>
									<input type="hidden" name="update_button" value="yes">
									<input name="button" type="submit"
										class="button btn btn-success" id="button" value="Update Item"
										onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
									<input name="delete_button" type="submit"
										class="button btn btn-success" id="delete_button"
										onClick="return confirmdelete(this)" value="Delete Item" />
									<%
										}
									%>
								</center>
								<input type="hidden" name="item_entry_by"
									value="<%=mybean.item_entry_by%>"> <input type="hidden"
									name="entry_date" value="<%=mybean.entry_date%>"> <input
									type="hidden" name="item_modified_by"
									value="<%=mybean.item_modified_by%>"> <input
									type="hidden" name="modified_date"
									value="<%=mybean.modified_date%>">
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
	<%@include file="../Library/js.jsp" %>
	<%@include file="../Library/admin-footer.jsp"%>
	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
		document.form1.txt_item_name.focus()
		}
	function GetTaxFormulae(){
		var check = document.getElementById("chk_item_aftertaxcal").checked;
		if(check==true){
			document.getElementById("tax_formulae").style.display = '';
			document.getElementById("chk_item_nonstock").checked = 'checked';
			document.getElementById("chk_item_nonstock").disabled = 'disabled';
			}else if(check==false){
				document.getElementById("tax_formulae").style.display = 'none';
				document.getElementById("chk_item_nonstock").disabled = '';
				}
		}
		

   function Displaypaymode()
   {
        var aftertaxcal = '<%=mybean.item_aftertaxcal%>';
		if (aftertaxcal == '1') {
			document.getElementById("tax_formulae").style.display = '';
		}
		var check = document.getElementById("chk_item_aftertaxcal").checked;
		if (check == true) {
			document.getElementById("tax_formulae").style.display = '';
			document.getElementById("chk_item_nonstock").checked = 'checked';
			document.getElementById("chk_item_nonstock").disabled = 'disabled';
		}
	}
    
   function populateAsterisk() {
		var producttype = document.getElementById("dr_item_type_id").value;
		console.log("producttype=="+producttype);
		if (producttype == 1) {
			$('#star').show();
			$('#star1').show();
			$('#salessgst').show();
			$('#salescgst').show();
			$('#salesigst').show();
			$('#salescess').hide();
			$('#purchasesgst').show();
			$('#purchasecgst').show();
			$('#purchaseigst').show();
			$('#purchasecess').hide();
		} else {
			$('#star').hide();
			$('#star1').hide();
			$('#salessgst').hide();
			$('#salescgst').hide();
			$('#salesigst').hide();
			$('#salescess').hide();
			$('#purchasesgst').hide();
			$('#purchasecgst').hide();
			$('#purchaseigst').hide();
			$('#purchasecess').hide();
		}
		
	}
</script>


</body>
</HTML>
