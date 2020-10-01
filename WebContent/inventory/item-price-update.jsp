<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Item_Price_Update" scope="request" />
<% mybean.doGet(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">    
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
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
						<h1><%=mybean.status%>
							Item Price
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
						<li><a href="index.jsp">Inventory</a> &gt;</li>
						<li><a href="inventory-item-list.jsp?all=yes">List Items</a>
							&gt;</li>
						<li><a
							href="inventory-item-list.jsp?item_id=<%=mybean.price_item_id%>"><%=mybean.item_name%></a>
							&gt;</li>
						<li><a
							href="../inventory/item-price-list.jsp?item_id=<%=mybean.price_item_id%>&price_id=<%=mybean.price_id%>">List
								Item Prices</a> &gt;</li>
						<li><a href="item-price-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
								Item Price</a><b>:</b></li>

					</ul>
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="form1" method="post" class="form-horizontal">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<%=mybean.status%>
											Item Price
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
											<center>
												<font size="">Form fields marked with a red asterisk
													<b><font color="#ff0000">*</font></b> are required.<br></br>
												</font>
											</center>
											<div class="form-element3">
												<label> Rate Class Type<font color=red>*</font>: </label>
													<select name="dr_rateclass_type" id="dr_rateclass_type"
														class="form-control" style="margin-top: 9px;"
														onchange="PopulateBranchClass();"visible="true">
														<%=mybean.PopulateBranchClassType()%>
													</select>
											</div>
											<div class="form-element3">
												<label > Rate Class <font color=red>*</font>: </label>
													<%-- <select name="dr_price_rateclass_id"
														id="dr_price_rateclass_id" class="form-control" 
														style="margin-top: 9px;"> -->
														<%=mybean.PopulateBranchClass()%>
													</select>  --%>
									<span id="rateclass"> <%=mybean.PopulateBranchClass(mybean.rateclass_type, mybean.branch_id,"0")%></span>
											</div>

											<div class="form-element6">
												<label> <%if (mybean.cat_id.equals("1")) {%> Item <%
												 	} else {
												 %> Service <%
												 	}
												 %> <font color=red>*</font>:&nbsp;
												</label>
													<a href="../inventory/inventory-item-list.jsp?item_id=<%=mybean.price_item_id%>"><%=mybean.item_name%></a>

											</div>
<div class="row"></div>
											<div class="form-element6">
												<label> Description:</label>
													<textarea name="txt_price_desc" cols="31" rows="4"
														class="form-control" id="txt_price_desc" maxlength="255"
														onKeyUp="charcount('txt_price_desc', 'span_txt_price_desc','<font color=red>({CHAR} characters left)</font>', '500')"><%=mybean.price_desc%></textarea>
													<span id="span_txt_price_desc"> (500 characters) </span>

											</div>

											<div class="form-element6">
												<label> Amount<font color=red>*</font>: </label>
													<input name="txt_price_amt" type="text"
														class="form-control" id="txt_price_amt"
														onKeyUp="toNumber('txt_price_amt','Amount')"
														value="<%=mybean.price_amt%>" size="15" maxlength="15" />

											</div>

											<div class="form-element6">
												<label>Variable:</label>
													<input type="checkbox" name="chk_price_variable"
														id="chk_price_variable"
														<%=mybean.PopulateCheck(mybean.price_variable)%> />

											</div>
											<div class="row"></div>
											<div class="form-element4">
												<label> Maximum Discount:&nbsp; </label>
													<input name="txt_price_disc" type="text"
														class="form-control" id="txt_price_disc"
														onKeyUp="toNumber('txt_price_disc','Maximum Discount')"
														value="<%=mybean.price_disc%>" size="12" maxlength="10" />
												</div>
												<div class="form-element2"><label>&nbsp;</label>
													<select id="dr_price_disc_type" name="dr_price_disc_type"
														class="form-control" onchange="validatedisc(this.value);">
														<%=mybean.PopulateDiscountType()%>
													</select>

												</div>
<%--
											<%
												if (mybean.rateclass_type.equals("1")) {
											%>
											<%
												if (mybean.comp_module_accounting.equals("1")) {
											%>
											 <div class="form-element6">
												<label class="control-label col-md-4"> Sales Ledger:<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<select id="dr_salescustomer_id" name="dr_salescustomer_id"
														class="form-control">
														<%=mybean.PopulateSalesCustomers()%>
													</select>

												</div>
											</div>


											<div class="form-element6">
												<label class="control-label col-md-4"> Sales
													Discount Ledger:<font color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<select id="dr_salesdiscountcustomer_id"
														name="dr_salesdiscountcustomer_id" class="form-control">
														<%=mybean.PopulateSalesDiscountCustomers()%>
													</select>

												</div>
											</div>
											<div class="form-element6">
												<label class="control-label col-md-4"> Tax 1: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<select name="dr_price_tax1" class="form-control"
														id="dr_price_tax1">
														<%=mybean.PopulatePriceTax(mybean.price_tax1_id)%>
													</select>

												</div>
											</div>
											<div class="form-element6">
												<label class="control-label col-md-4"> Tax 2:</label>
												<div class="col-md-3 col-xs-12" id="emprows">
													<select name="dr_price_tax2" class="form-control"
														id="dr_price_tax2">
														<%=mybean.PopulatePriceTax(mybean.price_tax2_id)%>
													</select>
												</div>
												<div class="txt-align col-md-3 col-xs-12">
													Cal. After Tax1:&nbsp;<input type="checkbox"
														name="chk_price_tax2_after_tax1"
														id="chk_price_tax2_after_tax1"
														<%=mybean.PopulateCheck(mybean.price_tax2_after_tax1)%>
														style="vertical-align: bottom;" />
												</div>
											</div>

											<div class="form-element6">
												<label class="control-label col-md-4"> Tax 3:</label>
												<div class="col-md-3 col-xs-12" id="emprows">
													<select name="dr_price_tax3" class="form-control"
														id="dr_price_tax3">
														<%=mybean.PopulatePriceTax(mybean.price_tax3_id)%>
													</select>
												</div>
												<div class="txt-align col-md-3 col-xs-12">
													Cal. After Tax2:&nbsp;<input type="checkbox"
														name="chk_price_tax3_after_tax2"
														id="chk_price_tax3_after_tax2"
														<%=mybean.PopulateCheck(mybean.price_tax3_after_tax2)%>
														style="vertical-align: bottom;" />

												</div>
											</div>

											<%
												}
											%>
											<%
												}
											%>
 --%>

											<%
												if (mybean.comp_module_dealer.equals("1")) {
											%>
											<div class="form-element6">
												<label> <%=mybean.config_customer_name%> Price:&nbsp; </label>
													<input name="txt_price_customer_price" type="text"
														class="textbox" id="txt_price_customer_price"
														value="<%=mybean.price_customer_price%>" size="16"
														maxlength="11"
														onkeyup="toFloat('txt_price_customer_price','Customer Price')" />

											</div>
											<div class="form-element6">
												<label> Executive Commission:&nbsp;</label>
													<input name="txt_price_exe_comm" type="text"
														class="textbox" id="txt_price_exe_comm"
														value="<%=mybean.price_exe_comm%>" size="16"
														maxlength="11"
														onkeyup="toFloat('txt_price_exe_comm','Exe Comm')" />

											</div>
											<div class="form-element6">
												<label>Technician Commission:&nbsp;</label>
													<input name="txt_price_technician_comm" type="text"
														class="textbox" id="txt_price_technician_comm"
														value="<%=mybean.price_technician_comm%>" size="16"
														maxlength="11"
														onkeyup="toFloat('txt_price_technician_comm','Tech Comm');" />

											</div>
											<div class="form-element6">
												<label>Dealer Executive Commission:</label>
													<input name="txt_price_dealerexe_comm" type="text"
														class="textbox" id="txt_price_dealerexe_comm"
														value="<%=mybean.price_dealerexe_comm%>" size="16"
														maxlength="11"
														onkeyup="toFloat('txt_price_dealerexe_comm','Dealer Sup Comm');" />

											</div>
											<%
												}
											%>
											 
												<div class="form-element4">
													<label>Effective From<font color=red>*</font>: </label>
														<input name="txt_price_effective_from" type="text"
															class="form-control datepicker"
															id="txt_price_effective_from"
															value="<%=mybean.effective_from%>" size="12"
															maxlength="10" />
												</div>
											 

											<%=mybean.GetConfigItemPrice()%>
											
											<div class="form-element2">
												<label>Active:</label>
													<input type="checkbox" name="chk_price_active"
														id="chk_price_active"
														<%=mybean.PopulateCheck(mybean.price_active)%> />

											</div>
<div class="row"></div>
											<%
												if (mybean.status.equals("Update") && !(mybean.price_entry_by == null) && !(mybean.price_entry_by.equals(""))) {
											%>
												<div class="form-element6">
													<label>Entry by: <%=mybean.unescapehtml(mybean.price_entry_by)%></label>
												</div>
											<%
												}
											%>

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>
												<div class="form-element6">
													<label>Entry date: <%=mybean.entry_date%></label>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.price_modified_by == null) && !(mybean.price_modified_by.equals(""))) {
											%>
												<div class="form-element6">
													<label>Modified by: <%=mybean.unescapehtml(mybean.price_modified_by)%></label>
												</div>

											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
											%>
												<div class="form-element6">
													<label>Modified date: <%=mybean.modified_date%></label>
												</div>

											<%
												}
											%>
											<center>
												<div colspan="2" valign="middle">
													<%
														if (mybean.status.equals("Add")) {
													%>
													<input name="addbutton" type="submit"
														class="button btn btn-success" id="addbutton"
														value="Add Price"
														onClick="return SubmitFormOnce(document.form1, this);" />
													<input type="hidden" id="add_button" name="add_button"
														value="yes" />
													<%
														} else if (mybean.status.equals("Update")) {
													%>
													<input type="hidden" id="update_button"
														name="update_button" value="yes" /> <input
														name="updatebutton" type="submit"
														class="button btn btn-success" id="updatebutton"
														value="Update Price"
														onClick="return SubmitFormOnce(document.form1, this);" />
													<input name="delete_button" type="submit"
														class="button btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)" value="Delete Price" />
													<%
														}
													%>
													<input type="hidden" name="price_entry_by"
														value="<%=mybean.price_entry_by%>"> <input
														type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>"> <input
														type="hidden" name="price_modified_by"
														value="<%=mybean.price_modified_by%>"> <input
														type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
											</center>
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

	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>

<script>
	 
	function PopulateBranchClass() {
		  var rateclass_type = document.getElementById('dr_rateclass_type').value;
		  showHint("../inventory/item-price-update-check.jsp?rateclass_type="+rateclass_type+"&rateclasstype=yes", "rateclass");
	  }

	function validatedisc(disctype) {
		var amount = document.getElementById("txt_price_amt").value;
		var disc = document.getElementById("txt_price_disc").value;
		//alert("amount===="+amount);
		//alert("disc===="+disc);
		if (amount != '' && disc != '') {
			if (disctype == 1) {
				if (parseFloat(disc) > parseFloat(amount)) {
					alert("Discount can not be greater than total Amount");
					form1.txt_price_disc.focus();
				}
			}
			if (disctype == 2) {
				if (disc > 100) {
					alert("Discount can not be more than 100%");
					form1.txt_price_disc.focus();
				}
			}
		}
	}
</script>
</body>
</HTML>
