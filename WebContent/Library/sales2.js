// JavaScript Document
function AddCartTax() {
	var tax_bill_sundry = document.getElementById('dr_bill_sundry').value;
	var tax_bill_sundryarr = uom_id.split("-");
	var tax_id = uom_idarr[0];
	var tax_rate = uom_idarr[1];
	showHintAccount('../accounting/purchase-details.jsp?addcart_billsundry=yes', 'po_details');
}
// For selecting existing contact
function SelectContact(contact_id, contact_name, customer_id, customer_name, customer_rateclass_id, customer_code) {
	// alert("customer_rateclass_id ===="+customer_rateclass_id );
	var vouchertype_email = document.getElementById("txt_vouchertype_email").value;
	var vouchertype_mobile = document.getElementById("txt_vouchertype_mobile").value;
	var vouchertype_dob = document.getElementById("txt_vouchertype_dob").value;
	var vouchertype_dnd = document.getElementById("txt_vouchertype_dnd").value;
	var contact_link = '<a href=../customer/customer-contact-list.jsp?contact_id=' + contact_id + '>' + contact_name + '</a>';
	var customer_link = '<a href=../customer/customer-list.jsp?customer_id=' + customer_id + '>' + customer_code + ' (' + customer_name + ')' + '</a>';
	var status = document.getElementById("txt_status").value;
	var selected_account = document.getElementById("span_acct_id").value;
	var invoice_account = document.getElementById("acct_id").value;
	if (customer_rateclass_id != 0) {
		document.getElementById("txt_rateclass_id	").value = customer_rateclass_id;
	}
	if (status != 'Update' && selected_account == 0) {
		document.getElementById("customer_name").style.display = 'none';
		document.getElementById("contact_name").style.display = 'none';
		if (vouchertype_mobile == 1) {
			document.getElementById("contact_mobile").style.display = 'none';
		} else if (vouchertype_mobile == 0) {
			document.getElementById("contact_mobile1").style.display = 'none';
		}
		if (vouchertype_email == 1) {
			document.getElementById("contact_email").style.display = 'none';
		}
		document.getElementById("contact_address").style.display = 'none';
		document.getElementById("contact_city").style.display = 'none';
		document.getElementById("contact_zone").style.display = 'none';

		if (vouchertype_dnd == 1) {
			document.getElementById("contact_dnd").style.display = 'none';
		}
		document.getElementById("selected_contact").style.display = '';
	}
	document.getElementById("span_cont_id").value = contact_id;
	document.getElementById("span_acct_id").value = customer_id;
	document.getElementById("span_customer_id").value = customer_id;
	document.getElementById("span_customer_name").value = customer_link;
	document.getElementById("span_contact_name").value = contact_link;
	if (status == 'Add' && invoice_account == 0) {
		document.getElementById("selected_contact_id").innerHTML = contact_link;
		document.getElementById("selected_customer_id").innerHTML = customer_link;
	} else if (status == 'Update' || selected_account != 0) {
		document.getElementById("span_bill_contact_id").innerHTML = contact_link;
		document.getElementById("span_bill_customer_id").innerHTML = customer_link;
	}
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
	var cart_session_id = document.getElementById("txt_session_id").value;
	showHintAccount('../accounting/invoice-details2.jsp?cart_session_id=' + cart_session_id + '&status=' + status + '&cart_vouchertype_id=' + vouchertype_id + '&list_cartitems=yes', "invoice_details");
	$('#dialog-modal').dialog('close');
}
// end

// / serching customer with contact fname
function ShowNameHint() {
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
	var name = document.getElementById("txt_customer_name").value;
	var fname = document.getElementById("txt_contact_fname").value;
	var lname = document.getElementById("txt_contact_lname").value;
	showHintAccount(
			'../accounting/ledger-check2.jsp?customer_name=' + name + '&contact_fname=' + fname + '&contact_lname=' + lname + '&cart_vouchertype_id=' + vouchertype_id + '&search_customer=yes',
			'invoice_details');
}

// / serching customer with contact mobile
function ShowMobileHint() {
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
	var mobile = document.getElementById("txt_contact_mobile1").value;
	// var session_id = document.getElementById("txt_session_id").value;
	showHintAccount('../accounting/ledger-check2.jsp?contact_mobile=' + mobile + '&cart_vouchertype_id=' + vouchertype_id + '&search_customer=yes', 'invoice_details');
}

// / serching customer with contact phone
function ShowPhoneHint() {
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
	var phone = document.getElementById("txt_contact_phone1").value;
	// var session_id = document.getElementById("txt_session_id").value;
	showHintAccount('../accounting/ledger-check2.jsp?contact_phone=' + phone + '&cart_vouchertype_id=' + vouchertype_id + '&search_customer=yes', 'invoice_details');
}

// / serching customer with contact email
function ShowEmailHint() {
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
	var email = document.getElementById("txt_contact_email1").value;
	// var session_id = document.getElementById("txt_session_id").value;
	showHintAccount('../accounting/ledger-check2.jsp?contact_email=' + email + '&cart_vouchertype_id=' + vouchertype_id + '&search_customer=yes', 'invoice_details');
}

// function to copy billing address in delivery address fields
function CopyConsigneeAddress() {
	var billing_address = document.getElementById("txt_voucher_billing_add").value;
	document.getElementById("txt_voucher_consignee_add").value = billing_address;
}

// functions for invoice-details2.jsp

// For Listing Cart Items
function list_cart_items() {
	var location_id = document.getElementById("txt_location_id").value;
	var status = document.getElementById("txt_status").value;
	var cart_session_id = document.getElementById("txt_session_id").value;
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
	var voucher_so_id = document.getElementById("txt_soid").value;
	if (voucher_so_id != '0') {
		showHintAccount('../accounting/invoice-details2.jsp?' + 'voucher_so_id=' + voucher_so_id + '&status=' + status + '&location_id=' + location_id + '&cart_session_id=' + cart_session_id
				+ '&cart_vouchertype_id=' + vouchertype_id + '&list_cartitems=yes', 'invoice_details');
	} else {
		showHintAccount('../accounting/invoice-details2.jsp?' + 'status=' + status + '&location_id=' + location_id + '&cart_session_id=' + cart_session_id + '&cart_vouchertype_id=' + vouchertype_id
				+ '&list_cartitems=yes', 'invoice_details');
	}
}

// For Deleting cart item
function delete_cart_item(cart_id) {
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
	var location_id = document.getElementById("txt_location_id").value;
	var cart_session_id = document.getElementById("txt_session_id").value;
	showHintAccount('../accounting/invoice-details2.jsp?cart_session_id=' + cart_session_id + '&cart_id=' + cart_id + '&cart_vouchertype_id=' + vouchertype_id + '&location_id=' + location_id
			+ '&delete_cartitem=yes', 'invoice_details');

	// document.getElementById('mode_button').innerHTML = ' ';
	document.getElementById('txt_item_id').value = 0;
	document.getElementById('txt_item_qty').value = 0;
	document.getElementById('txt_item_price').value = 0;
	document.getElementById('txt_item_total').value = 0;
	document.getElementById('item_total').innerHTML = 0;
	document.getElementById('configure-details').innerHTML = ' ';
	document.getElementById("serial_details").style.display = 'none';
	document.getElementById("batch_details").style.display = 'none';
	document.getElementById("txt_search").focus();
}
// To Delete all cart items
function DeleteFullCart() {
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
	var location_id = document.getElementById("txt_location_id").value;
	var cart_session_id = document.getElementById("txt_session_id").value;
	showHintAccount('../accounting/invoice-details2.jsp?cart_session_id=' + cart_session_id + '&cart_vouchertype_id=' + vouchertype_id + '&location_id=' + location_id + '&delete_full_cart=yes',
			'invoice_details');
}

// close functioms for invoice-detals.jsp

// / functions for invoice items
var i = 0;
function InvoiceItemSearch(i) {
	var item_serial_id = 0;
	var rateclass_id = 0;
	var id = "txt_itemname_" + i;
	var gst_type = document.getElementById("txt_gst_type").value;
	var cart_session_id = CheckNumeric(document.getElementById("txt_session_id").value);
	var config_inventory_current_stock = CheckNumeric(document.getElementById("txt_config_inventory_current_stock").value);
	var cart_vouchertype_id = CheckNumeric(document.getElementById("txt_vouchertype_id").value);
	var value = document.getElementById(id).value;
	var location_id = CheckNumeric(document.getElementById("dr_location_id").value);
	var branch_id = CheckNumeric(document.getElementById("txt_branch_id").value);
	item_serial_id = 0;
	rateclass_id = CheckNumeric(document.getElementById("dr_voucher_rateclass_id").value);
	// var item_cat_id =
	// CheckNumeric(document.getElementById("dr_item_cat_id").value);

	if (gst_type != "") {
		i = setTimeout('callAjax("' + i + '", "' + value + '", "' + cart_session_id + '", "' + rateclass_id + '", "' + config_inventory_current_stock + '", "' + branch_id + '", "' + location_id
				+ '", "' + item_serial_id + '" )', 1000);
	} else {
		alert('Customer City Not Updated!');
		// var idtr = "searchresult_"+i+"_tr";
		// $("#"+idtr).html("<font color='#ff000'><b>Customer City Not
		// Updated!</b></font>")
	}
}

function callAjax(i, value, cart_session_id, rateclass_id, config_inventory_current_stock, branch_id, location_id, item_serial_id) {
	var id = "searchresult_" + i;
	var idtr = "searchresult_" + i + "_tr";
	showHintAccount("../accounting/invoice-check2.jsp?" + "row_no=" + i + "&q=" + value + "&cart_session_id=" + cart_session_id + "&rateclass_id=" + rateclass_id + "&config_inventory_current_stock="
			+ config_inventory_current_stock + "&branch_id=" + branch_id + "&location_id=" + location_id + "&item_serial_id=" + item_serial_id, id);
	$("#" + idtr).show();
}

// price should'nt be less than the base price
function CheckBasePrice(para) {
	var para = 1;
	var base_price = parseFloat(document.getElementById("txt_item_baseprice_" + para).value);
	// alert("base_price=="+base_price);
	// var id2 = "uom_ratio_"+para;
	var uom_ratio = 1;
	// uom_ratio = parseFloat(document.getElementById(id2).value);

	var alt_qty = parseInt(CheckNumeric(document.getElementById('txt_item_qty_' + para).value));
	var qty = (alt_qty * uom_ratio);
	// alert("uom_ratio=="+uom_ratio);
	var price_variable = document.getElementById("txt_item_pricevariable_" + para).value;
	var price = parseFloat(document.getElementById('txt_item_price_' + para).value);
	// alert("price=="+price);
	if (uom_ratio != '' && uom_ratio != null) {
		base_price = (price / uom_ratio);
		// alert("base_price==af=="+base_price);
		document.getElementById('txt_item_baseprice_' + para).value = base_price;
	}
	if ((eval(price) < eval(base_price)) && price_variable != 1) {
		document.getElementById('txt_item_price_' + para).value = base_price;
		price = base_price;
		CalItemTotal(para);
	}

}

// code on click of cart item to be highlighted
function setColorById(id) {
	var updatecount = document.getElementById("txt_updatecount").value;
	var elem;
	var i;
	for (i = 1; i <= updatecount; i++) {
		if (id == i) {
			elem = document.getElementById(id);
			elem.style.color = "blue";
		}
		if (id != i) {
			if (i != 0) {
				elem = document.getElementById(i);
				elem.style.color = "black";
			}
		}
	}
}
// for getting the item details
function ItemDetails(i, item_id, alt_qty, uom_id, price_amt, price_disc, discpercent, boxtype_size, stock_qty, cart_id, mode, rateclass_id) {
	var cart_session_id = CheckNumeric(document.getElementById("txt_session_id").value);
	var config_inventory_current_stock = CheckNumeric(document.getElementById("txt_config_inventory_current_stock").value);
	var location_id = CheckNumeric(document.getElementById("txt_location_id").value);
	var gst_type = document.getElementById("txt_gst_type").value;
	var branch_id = CheckNumeric(document.getElementById("txt_branch_id").value);
	document.getElementById("cart_id").value = cart_id;
	document.getElementById("uom_id").value = uom_id;
	rateclass_id = CheckNumeric(rateclass_id);
	if (rateclass_id == 0) {
		rateclass_id = CheckNumeric(document.getElementById("dr_voucher_rateclass_id").value);
		// alert(rateclass_id);
		if (rateclass_id == 0) {
			alert("Select Rate Class!");
		}
	}
	if (price_amt == 0 || price_amt == '' || isNaN(price_amt) == true || price_amt == null) {
		price_amt = 0.0;
	}
	if (price_disc == 0 || price_disc == '' || isNaN(price_disc) == true || price_disc == null) {
		price_disc = 0.0;
	}
	if (boxtype_size == 0 || boxtype_size == '' || isNaN(boxtype_size) == true || boxtype_size == null) {
		boxtype_size = 0.0;
	}
	voucherclass_id = CheckNumeric(document.getElementById("txt_voucherclass_id").value);
	vouchertype_id = CheckNumeric(document.getElementById("txt_vouchertype_id").value);
	$('.searchresult').hide();
	var voucher_so_id = CheckNumeric(document.getElementById("txt_voucher_so_id").value);
	if (item_id != 0 && rateclass_id != 0) {
		var id = 'itemindex_' + i;
		console.log('../accounting/invoice-details2.jsp?' 
				+ 'cart_session_id=' + cart_session_id 
				+ '&voucher_so_id=' + voucher_so_id 
				+ '&row_no=' + i 
				+ '&branch_id=' + branch_id 
				+ '&gst_type=' + gst_type 
				+ '&config_inventory_current_stock=' + config_inventory_current_stock 
				+ '&location_id=' + location_id 
				+ '&cart_item_id=' + item_id 
				+ '&cart_boxtype_size=' + boxtype_size
				+ '&cart_discpercent=' + discpercent 
				+ '&cart_vouchertype_id=' + vouchertype_id 
				+ '&voucherclass_id=' + voucherclass_id 
				+ '&cart_alt_qty=' + alt_qty 
				+ '&cart_price=' + price_amt
				+ '&disc=' + price_disc 
				+ '&cart_uom_id=' + uom_id 
				+ '&mode=' + mode 
				+ '&cart_id=' + cart_id 
				+ '&configure1=yes' 
				+ '&rateclass_id=' + rateclass_id);
		showHintAddItem('../accounting/invoice-details2.jsp?' 
				+ 'cart_session_id=' + cart_session_id 
				+ '&voucher_so_id=' + voucher_so_id 
				+ '&row_no=' + i 
				+ '&branch_id=' + branch_id 
				+ '&gst_type=' + gst_type 
				+ '&config_inventory_current_stock=' + config_inventory_current_stock 
				+ '&location_id=' + location_id 
				+ '&cart_item_id=' + item_id 
				+ '&cart_boxtype_size=' + boxtype_size
				+ '&cart_discpercent=' + discpercent 
				+ '&cart_vouchertype_id=' + vouchertype_id 
				+ '&voucherclass_id=' + voucherclass_id 
				+ '&cart_alt_qty=' + alt_qty 
				+ '&cart_price=' + price_amt
				+ '&disc=' + price_disc 
				+ '&cart_uom_id=' + uom_id 
				+ '&mode=' + mode 
				+ '&cart_id=' + cart_id 
				+ '&configure1=yes' 
				+ '&rateclass_id=' + rateclass_id, id);

		// setTimeout('document.getElementById("txt_item_qty").focus()', 500);
		// setTimeout('CalculateNewTotal()', 200);
	}
}
/*------- Use when main item contains optional items as in Zenica, Axelaauto...--------------------

 //preventing the updation of the item that got configured items
 function PreventUpdate(){
 alert("This Item has optional items, hence you can only delete but not update the item!");
 document.getElementById("mode_button").innerHTML = ' ';
 document.getElementById('txt_item_id').value = 0;
 document.getElementById('txt_serial').value = 0;
 document.getElementById('txt_batch').value = 0;
 document.getElementById("serial_details").style.display = 'none';
 document.getElementById("batch_details").style.display = 'none';
 document.getElementById("configure-details").innerHTML = ' ';
 document.getElementById('txt_item_qty').value = 0;
 document.getElementById('txt_item_price').value = 0;
 document.getElementById('txt_item_total').value = 0;
 document.getElementById('item_total').innerHTML = 0;  
 document.getElementById("txt_search").focus();  
 }

 function CalculateNewTotal(){  
 var defaultselected_total = parseFloat(document.getElementById("txt_defaultselected_total").value);
 var price = parseFloat(document.getElementById("txt_item_baseprice").value);
 var qty = parseInt(CheckNumeric(document.getElementById('txt_item_qty').value));    
 var disc = parseFloat(document.getElementById("txt_item_price_disc").value);
 var tax_rate1 = parseFloat(document.getElementById("txt_item_price_tax_rate1").value);
 var tax_rate2 = parseFloat(document.getElementById("txt_item_price_tax_rate2").value);
 var tax_rate3 = parseFloat(document.getElementById("txt_item_price_tax_rate3").value);
 document.getElementById("item_total").innerHTML = parseFloat((((price + defaultselected_total)-disc)*qty*tax1/100) + (((price + defaultselected_total)-disc)*qty*tax2/100) + (((price + defaultselected_total)-disc)*qty*tax3/100) + (((price + defaultselected_total)-disc)*qty));
 document.getElementById("txt_item_total").value = parseFloat((((price + defaultselected_total)-disc)*qty*tax1/100)+(((price + defaultselected_total)-disc)*qty*tax2/100) + (((price + defaultselected_total)-disc)*qty*tax3/100) + (((price + defaultselected_total)-disc)*qty));
 }




 function CalculateTotal(){
 var para=1;
 var total = 0;
 var group_count = parseInt(document.getElementById("txt_group_count").value);
 var optioncount = document.getElementById("txt_optioncount").value;
 for(var i=1; i<=group_count; i++){
 total = parseInt(total) + parseInt(CheckNumeric(document.getElementById("txt_"+i+"_value").value));
 }
 if(document.getElementById("txt_group_count")!=null){
 var defaultselected_total = parseFloat(document.getElementById("txt_defaultselected_total").value);
 }

 document.getElementById("txt_item_baseprice").value = parseInt(total) + parseInt(defaultselected_total);
 CalItemTotal(para);
 }   

 function GroupValue(group_id, group_item_count, group_count){
 var group_value = 0;
 for(var i=1;i<=group_item_count;i++){
 var id = document.getElementById("chk_"+group_count+"_"+i);
 var item = id.checked;
 if(item==true){
 group_value = parseInt(group_value) + parseInt(id.value);
 }
 }
 if(group_id=='3'){
 var multiselect_basetotal = document.getElementById("txt_multiselect_basetotal").value;
 group_value = parseInt(group_value) - parseInt(multiselect_basetotal);
 document.getElementById("txt_new_value").value = group_value;

 }
 document.getElementById("txt_"+group_count+"_value").value = group_value;
 }


 function CalculateDefault(group_id, group_item_count, new_value, count, group_count){
 GroupValue(group_id, group_item_count, group_count);
 var base_value = "";
 for(var i=1;i<=group_item_count;i++){
 var selected_item = document.getElementById("chk_"+group_count+"_"+i).checked;
 if(selected_item==true){
 base_value = document.getElementById("txt_"+group_id+"_basevalue").value;
 if(count!=i){
 document.getElementById("chk_"+group_count+"_"+i).checked='';
 }
 }
 }
 var diff = parseInt(new_value) - parseInt(base_value);
 var new_group_value = parseInt(diff);
 document.getElementById("txt_"+group_count+"_value").value = new_group_value;
 CalculateTotal();
 }


 //for adding configured items along with the main item
 function AddConfiguredItems(){
 var location_id = document.getElementById("txt_location_id").value;
 var branch_id = document.getElementById("txt_branch_id").value;
 //var status = document.getElementById("txt_status").value;
 //var mode = document.getElementById("txt_mode").value;
 var cart_id=document.getElementById("cart_id").value;
 var cart_alt_qty=parseInt(CheckNumeric(document.getElementById('txt_item_qty').value));    
 var group_count = document.getElementById("txt_group_count").value;  
 var cart_vouchertype_id = document.getElementById("txt_vouchertype_id").value;

 for(var i=1;i<=group_count;i++){
 var group_item_count = document.getElementById("txt_"+i+"_count").value;
 for(var j=1; j<=group_item_count; j++){
 var item = document.getElementById("chk_"+i+"_"+j);
 if(item.checked==true){
 var opitem_gp_name = document.getElementById("txt_"+i+"_"+j+"_gpname").value;
 var opitem_item_id = document.getElementById("txt_"+i+"_"+j+"_id").value;
 var opitem_qty = document.getElementById("txt_"+i+"_"+j+"_qty").value;
 opitem_qty = CheckNumeric(opitem_qty * cart_alt_qty);
 var opitem_serial = document.getElementById("txt_"+i+"_"+j+"_serial").value;
 opitem_serial = 0;//
 var opitem_batch = document.getElementById("txt_"+i+"_"+j+"_batch").value;
 opitem_batch = 0;//
 var opitem_price = document.getElementById("txt_"+i+"_"+j+"_price").value;
 var opitem_netprice = eval(opitem_qty) * eval(opitem_price);
 var cart_session_id = document.getElementById("txt_session_id").value;
 showHintAccount('../accounting/invoice-details2.jsp?cart_session_id='+cart_session_id+'&location_id='+location_id+'&cart_vouchertype_id='+cart_vouchertype_id+'&cart_opitem_item_id='+opitem_item_id+'&cart_option_group='+opitem_gp_name+'&cart_opitem_qty='+opitem_qty+'&cart_opitem_price='+opitem_price+'&cart_opitem_netprice='+opitem_netprice+'&cart_id='+cart_id+'&rowcount=no&add_cartitem=yes', 'invoice_details');		
 }
 }
 }

 //document.getElementById("configure-details").innerHTML = "";

 } 
 function CalculateMultiSelect(id, item_price, group_item_count, group_id, group_count){

 GroupValue(group_id, group_item_count, group_count);
 CalculateTotal();

 }   
 -------------------END.......----------------------------------------------------
 */

// ---*** Refer Purchase.js
/*
 * function CalAmount(para){ var total_price = 0; var group_value = 0; var price = parseFloat(CheckNumeric(document.getElementById('txt_item_price').value)); var discpercent =
 * parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_percent_add').value)); var optioncount = document.getElementById("txt_optioncount").value; if(para==1){ total_price =
 * parseFloat(document.getElementById('txt_item_baseprice').value); if(total_price=='' || total_price==null){ total_price = 0; } } if(para != 1){ if(optioncount==1){ total_price =
 * parseFloat(document.getElementById('txt_defaultselected_total').value); if(total_price=='' || total_price==null){ total_price = 0; } group_value = document.getElementById('txt_new_value').value;
 * if(group_value=='' || group_value==null){ group_value = 0; } }else{ total_price = 0; group_value = 0; } total_price = eval(total_price) + eval(group_value); } temptotal = (eval(price)+
 * eval(total_price)); var disc_amount = (eval(temptotal) * (discpercent/100)).toFixed(4); document.getElementById('txt_item_price_disc').value = disc_amount; }
 */
function CalPercent(para) {
	// var caldiscpercent = 0.00;
	// var price = 0;
	// var disc = 0.00;
	// var price =
	// parseFloat(CheckNumeric(document.getElementById('txt_item_price_'+para).value));
	// var disc =
	// parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_'+para).value));
	// var disc_per =
	// parseFloat(CheckNumeric(document.getElementById('txt_item_price_dic_per_'+para).value));
	// var emp_disc_update =
	// parseInt(document.getElementById('txt_emp_invoice_discountupdate_'+para).value);
	// if (disc <= price) {
	// if (price != 0) {
	// caldiscpercent = ((disc * 100) / eval(price));
	// }
	// if (caldiscpercent != 0) {
	// caldiscpercent = caldiscpercent.toFixed(6);
	// } else {
	// caldiscpercent = caldiscpercent.toFixed(6);
	// }
	// document.getElementById('txt_item_price_disc_percent_add_'+para).value =
	// caldiscpercent.toString();
	// } else {
	// if (emp_disc_update == 0) {
	// document.getElementById('txt_item_price_disc_'+para).value =
	// disc_per.toString();
	// document.getElementById('txt_item_price_disc_percent_add_'+para).value =
	// disc_per.toString();
	// } else {
	// document.getElementById('txt_item_price_disc_'+para).value = "0.00";
	// document.getElementById('txt_item_price_disc_percent_add_'+para).value =
	// "0.00";
	// }
	// }
	setTimeout('CalItemTotal(' + para + ')', 1000);
}

// function CalAmount(para) {
// var total_price = 0;
// var group_value = 0;
// var price =
// parseFloat(CheckNumeric(document.getElementById('txt_item_price_'+para).value));
// var disc_per =
// parseFloat(CheckNumeric(document.getElementById('txt_item_price_dic_per_'+para).value));
// var discpercent =
// parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_percent_add_'+para).value));
// var emp_disc_update =
// parseInt(document.getElementById('txt_emp_invoice_discountupdate_'+para).value);
// var alt_qty =
// CheckNumeric(document.getElementById('txt_item_qty_'+para).value);
// if (alt_qty == 0) {
// alt_qty = 1.00;
// }
//
// if (discpercent <= disc_per) {
// var disc_amount = (eval(price) * (discpercent / 100)).toFixed(4);
// } else {
// if (emp_disc_update == 0) {
// var disc_amount = (eval(price) * (disc_per / 100)).toFixed(4);
// } else {
// var disc_amount = "0.00";
// }
// }
// disc_amount = eval(disc_amount * alt_qty);
// document.getElementById('txt_item_price_disc_'+para).value = disc_amount;
// }

// for calculating item total and tax
function CalItemTotal(para) {
	// alert('para==='+para);

	// CalAmount(para);

	var disctemp = 0;
	var total_price = 0;
	var group_value = 0;
	var tax_rate1 = 0;
	var tax_rate2 = 0;
	var tax_rate3 = 0;
	var tax_value1 = 0.0;
	var tax_value2 = 0.0;
	var tax_value3 = 0.0;
	var disc = 0.00;
	var discpercent = 0.00;
	var uom_id = 0;
	var qty = 0.00;
	var disc_per = 0.00, disc_amt = 0.00;
	var alt_qty = CheckNumeric(document.getElementById('txt_item_qty_' + para).value);
	if (alt_qty == 0) {
		alt_qty = 1.00;
		document.getElementById('txt_item_qty_' + para).value = "1"
	}
	// alert("alt_qty==="+alt_qty);
	var emp_disc_update = parseInt(document.getElementById('txt_emp_invoice_discountupdate_' + para).value);
	var uom_id = document.getElementById('dr_alt_uom_id_' + para).value;
	var uom_idarr = uom_id.split("-");
	var uom_id = uom_idarr[0];
	// document.getElementById('uom_id').value = uom_id;
	var uom_ratio = uom_idarr[1];
	// alert("uom_ratio==="+uom_ratio);
	// document.getElementById('uom_ratio').value = uom_ratio;
	if (uom_ratio == 0 || uom_ratio == '' || isNaN(uom_ratio) == true || uom_ratio == null) {
		uom_ratio = 1.00;
		qty = alt_qty * uom_ratio;
	} else {
		qty = alt_qty * uom_ratio;
	}

	var price = parseFloat(CheckNumeric(document.getElementById('txt_item_price_' + para).value));
	// var unit_price =
	// parseFloat(CheckNumeric(document.getElementById('txt_item_baseprice_'+para).value));
	var unit_price = parseFloat(CheckNumeric(document.getElementById('txt_item_price_' + para).value));
	disc_amt = parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_' + para).value));
	disc_per = parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_percent_add_' + para).value));

	if (uom_ratio == 1.00 && alt_qty == 1.00) {
		document.getElementById('txt_item_price_' + para).value = unit_price * uom_ratio;
		discpercent = parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_percent_add_' + para).value));
		if (emp_disc_update == 0) {
			if (discpercent <= disc_per) {
				disc = parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_' + para).value));
				disc = ((unit_price * uom_ratio) * (discpercent / 100));
				document.getElementById('txt_item_price_disc_' + para).value = (disc * alt_qty).toFixed(4);
			} else {
				document.getElementById('txt_item_price_disc_percent_add_' + para).value = disc_per.toFixed(4);
				document.getElementById('txt_item_price_disc_' + para).value = (disc * alt_qty).toFixed(4);
			}
		} else {
			if (eval(discpercent) <= eval(100.00)) {
				disc = parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_' + para).value));
				disc = ((unit_price * uom_ratio) * (discpercent / 100));
				document.getElementById('txt_item_price_disc_' + para).value = (disc).toFixed(4);
			} else {
				document.getElementById('txt_item_price_disc_percent_add_' + para).value = "0.00";
				document.getElementById('txt_item_price_disc_' + para).value = "0.00";
			}
		}
	} else if (uom_ratio != 1.00 && alt_qty == 1.00) {
		document.getElementById('txt_item_price_' + para).value = unit_price * uom_ratio;
		discpercent = parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_percent_add_' + para).value));
		if (discpercent <= disc_per) {
			disc = parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_' + para).value));
			disc = ((unit_price * uom_ratio) * (discpercent / 100));
			document.getElementById('txt_item_price_disc_' + para).value = disc.toFixed(4);
		} else {
			document.getElementById('txt_item_price_disc_percent_add_' + para).value = disc_per.toFixed(4);
			document.getElementById('txt_item_price_disc_' + para).value = disc_amt.toFixed(4);
		}
	} else if (uom_ratio == 1.00 && alt_qty != 1.00) {
		document.getElementById('txt_item_price_' + para).value = unit_price * uom_ratio;
		discpercent = parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_percent_add_' + para).value));
		if (discpercent <= disc_per) {
			disc = ((unit_price * uom_ratio) * (discpercent / 100));
			document.getElementById('txt_item_price_disc_' + para).value = disc.toFixed(4);
		} else {
			document.getElementById('txt_item_price_disc_percent_add_' + para).value = disc_per.toFixed(4);
			document.getElementById('txt_item_price_disc_' + para).value = disc_amt.toFixed(4);
		}
	} else if ((uom_ratio != 1.00) && (alt_qty != 1.00)) {
		document.getElementById('txt_item_price_' + para).value = unit_price * uom_ratio;
		discpercent = parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_percent_add_' + para).value));
		if (discpercent <= disc_per) {
			disc = ((unit_price * uom_ratio) * (discpercent / 100));
			document.getElementById('txt_item_price_disc_' + para).value = disc.toFixed(4);
		} else {
			document.getElementById('txt_item_price_disc_percent_add_' + para).value = disc_per.toFixed(4);
			document.getElementById('txt_item_price_disc_' + para).value = disc_amt.toFixed(4);
		}
	}
	// /1. Logic of UOM ends ---and related code continued....
	price = parseFloat(CheckNumeric(document.getElementById('txt_item_price_' + para).value));
	disc = parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_' + para).value));
	discpercent = parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_percent_add_' + para).value));
	var optioncount = parseFloat(CheckNumeric(document.getElementById('txt_optioncount_' + para).value));
	if (para == 1) {
		total_price = parseFloat(CheckNumeric(document.getElementById('txt_item_baseprice_' + para).value));
		if (total_price == '' || total_price == null) {
			total_price = 0;
		}
	} else {
		if (optioncount == 1) {
			total_price = parseFloat(CheckNumeric(document.getElementById('txt_defaultselected_total').value));
			if (total_price == '' || total_price == null) {
				total_price = 0;
			}
			group_value = document.getElementById('txt_new_value').value;
			if (group_value == '' || group_value == null) {
				group_value = 0;
			}
		} else {
			total_price = 0;
			group_value = 0;
		}
		total_price = eval(total_price) + eval(group_value);
	}
	var disc_type = parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_type_' + para).value));
	disctemp = (eval(price) + eval(total_price));
	temptotal = disctemp;

	price = CheckNumeric(unit_price);
	disc = (price * (discpercent / 100));
	if (eval(disc) > eval(disctemp)) {
		alert("Discount can't be greater than price");
		document.getElementById('txt_item_price_disc_' + para).value = 0;
		document.getElementById('txt_item_price_disc_percent_add_' + para).value = 0;
		disc = 0;
		discpercent = 0;
	}
	if (eval(discpercent) > 100) {
		alert("Discount can't be greater than 100%");
		document.getElementById('txt_item_price_disc_percent_add_' + para).value = 0;
		document.getElementById('txt_item_price_disc_' + para).value = 0;
		disc = 0;
		discpercent = 0;
	}
	// For Taxes cal (without tax amt including for cal 2nd ,3rd tax amts)
	// and (with tax amt including for cal 2nd ,3rd tax amts if
	// price_tax2_after_tax1 or price_tax3_after_tax2 = 1)
	tax_rate1 = CheckNumeric(document.getElementById('txt_item_price_tax_rate1_' + para).value);
	tax_rate2 = CheckNumeric(document.getElementById('txt_item_price_tax_rate2_' + para).value);
	tax_rate3 = CheckNumeric(document.getElementById('txt_item_price_tax_rate3_' + para).value);
	var price_tax2_after_tax1 = CheckNumeric(document.getElementById('txt_price_tax2_after_tax1_' + para).value);
	var price_tax3_after_tax2 = CheckNumeric(document.getElementById('txt_price_tax3_after_tax2_' + para).value);
	// alert("price_tax2_after_tax1==="+price_tax2_after_tax1);
	if (tax_rate1 != 0) {
		document.getElementById('txt_item_tax1_' + para).value = parseFloat((((eval(unit_price) - eval(disc)) * eval(uom_ratio) * eval(tax_rate1 / 100))) * alt_qty).toFixed(4);
		tax_value1 = CheckNumeric(document.getElementById('txt_item_tax1_' + para).value);
		// alert("tax_value1==="+tax_value1);
	}
	if (tax_rate2 != 0) {
		if ((price_tax2_after_tax1 == 1) && (tax_rate1 != 0)) {
			document.getElementById('txt_item_tax2_' + para).value = parseFloat((((eval(unit_price) - eval(disc)) * eval(uom_ratio)) + eval(tax_value1)) * eval(tax_rate2 / 100)).toFixed(4);
			tax_value2 = CheckNumeric(document.getElementById('txt_item_tax2_' + para).value);
			// alert("tax_value2==if="+tax_value2);
		} else {
			document.getElementById('txt_item_tax2_' + para).value = parseFloat((((eval(unit_price) - eval(disc)) * eval(uom_ratio) * eval(tax_rate2 / 100))) * alt_qty).toFixed(4);
			tax_value2 = CheckNumeric(document.getElementById('txt_item_tax2_' + para).value);
			// alert("tax_value2=else=="+tax_value2);
		}
	}
	if (tax_rate3 != 0) {
		if ((price_tax3_after_tax2 == 1) && (tax_rate1 != 0) && (tax_rate2 != 0)) {
			document.getElementById('txt_item_tax3_' + para).value = parseFloat((((eval(unit_price) - eval(disc)) * eval(uom_ratio)) + eval(tax_value1) + eval(tax_value2)) * eval(tax_rate3 / 100))
					.toFixed(4);
			tax_value3 = CheckNumeric(document.getElementById('txt_item_tax3_' + para).value);
			// alert("tax_value3==="+tax_value3);
		} else {
			document.getElementById('txt_item_tax3_' + para).value = parseFloat((((eval(unit_price) - eval(disc)) * eval(uom_ratio) * eval(tax_rate3 / 100))) * alt_qty).toFixed(4);
			tax_value3 = CheckNumeric(document.getElementById('txt_item_tax3_' + para).value);
			// alert("tax_value3==="+tax_value3);
		}
	}
	// Final Result
	var total_tax = eval(tax_value1) + eval(tax_value2) + eval(tax_value3);
	var after_disc = (eval(price) - eval(disc)) * uom_ratio;

	document.getElementById('item_total_' + para).value = (parseFloat(eval(after_disc * alt_qty) + eval(total_tax))).toFixed(4);
	GrandTotal();
}

function GrandTotal() {
	TotalTax();
	var inputs = $('.item_total');
	var GrandTotal = 0;
	var round_off = $('#txt_vouchertype_roundoff').val();
	var round_off_amt = 0;

	for (var i = 0; i < inputs.length; i++) {
		var total = 0;
		var input_id = $(inputs[i]).attr('id');
		var item_selected = "chk_itemlist_" + input_id.split('_')[2];
		if ($("#" + item_selected).is(':checked')) {
			GrandTotal = eval(GrandTotal) + eval($("#" + input_id).val());
		}
	}

	// this block is to calculate Round-Off if it is active in
	// VoucherType-Update
	if (eval(round_off) != 0) {
		round_off_amt = (Math.round(GrandTotal.toFixed(4)) - GrandTotal.toFixed(4)).toFixed(4);
		if (eval(round_off_amt) != 0) {
			var tax_data = $('#total_tax').html()
					+ "<div class='col-md-8'></div><label class='col-md-2 text-right' style='margin-top:8px;'>Round Off: </label><div class='col-md-2 text-right'><b><input type='text' name='txt_round_off_amount' id='txt_round_off_amount' class='form-control text-right' value='"
					+ round_off_amt + "' readonly='readonly' /></b></div>";
			$('#total_tax').html(tax_data);
			GrandTotal = eval(GrandTotal) + eval(round_off_amt);
		}
	}
	// ===========================================================================

	document.getElementById('txt_grand_total').value = GrandTotal.toFixed(4);
}

function TotalTax() {
	var tax_name = [];
	var tax_total_value = [];
	var inputs = $('.tax_name');
	for (var i = 0; i < inputs.length; i++) {
		var j = 0;
		var input_id = $(inputs[i]).attr('id');
		var add_index = 0;

		// this is to check for item selected checkbox
		var item_selected = "chk_itemlist_" + input_id.split('_')[3];
		if ($("#" + item_selected).is(':checked')) {
			// ---------------------------------------

			if (tax_name.length != 0) {
				for (j = 0; j < tax_name.length; j++) {
					if (tax_name[j] == $("#" + input_id).val()) {
						add_index = j;
						break;
					}
				}
				var id = $("#" + input_id).val().replace('@', '').replace('%', '').replace(' ', '') + "_" + input_id.split("_")[3];
				if (j == tax_name.length) {
					tax_name[j] = $("#" + input_id).val();
					tax_total_value[j] = eval($("." + id).val());
				} else {
					tax_total_value[add_index] = eval(tax_total_value[add_index]) + eval($("." + id).val());
				}
			} else {
				tax_name[0] = $("#" + input_id).val();
				var id = tax_name[0].replace('@', '').replace('%', '').replace(' ', '') + "_" + input_id.split("_")[3];
				tax_total_value[0] = eval($("." + id).val());
			}

			// -----------------------------
		}
		// -----------------------------
	}
	var tax_data = "";
	for (var k = 0; k < tax_name.length; k++) {
		tax_data = tax_data + "<div class='col-md-8'></div><label class='col-md-2 text-right' style='margin-top:8px;'>" + tax_name[k]
				+ ": </label><div class='col-md-2 text-right'><b><input type='text' name='" + tax_name[k] + "' id='" + tax_name[k] + "' class='form-control text-right' value='"
				+ tax_total_value[k].toFixed(4) + "' readonly='readonly' /></b></div>";
	}
	$('#total_tax').html(tax_data);
}

function DiscountModel(url_link) {
	var a_href = "<a href=" + url_link + " data-target='#Hintclicktocall' data-toggle='modal' id='discount_link'>discount_link</a>";
	$("#discount_model_link").html(a_href);
	$('#discount_link').click();

	setTimeout(function() {
		$('#dis_row_no').val(url_link.split("=")[1]);
	}, 300);
}

// for updating items
function LoadPayment() {
	document.getElementById("mode_button").innerHTML = ' ';
	document.getElementById('txt_item_id').value = 0;
	document.getElementById('txt_item_qty').value = 0;
	document.getElementById('txt_item_price').value = 0;
	document.getElementById('txt_item_total').value = 0;
	document.getElementById('item_total').innerHTML = 0;

	var total_invoice = document.getElementById("txt_invoice_grandtotal").value;
	var total_payment = document.getElementById("total_payment").innerHTML;
	var balance = parseFloat(total_invoice) - parseFloat(total_payment);
	document.getElementById("balance").innerHTML = balance;
}
// end of function of invoice items

function CheckNumeric(num) {
	if (isNaN(num) || num == '' || num == null) {
		num = 0;
	}
	return num;
}

$('#accountingcustomer').on('change', function() {
	var Hint = "txt_gst_type";
	var url = '../accounting/ledger-check2.jsp?state=yes&customer_id=' + $(this).val() + '&branch_id=' + $("#txt_branch_id").val();
	$('#' + Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
	$.ajax({
		url : url,
		type : 'GET',
		success : function(data) {
			if (data.trim() != 'SignIn')
				$('#' + Hint).val(data.trim());
			else
				window.location.href = "../portal/";
		}
	});
	setTimeout(function(){itemRepopulate();}, 500);
});


function itemRepopulate(){
	var item_ids = "";
	var item_qtys = "";
	var item_discounts = "";
	var gst_type = document.getElementById("txt_gst_type").value;
	var rateclass_id = document.getElementById("dr_voucher_rateclass_id").value;
	var ids = $.map($(".item_ids"), function(elt) {return elt.id;})+"";
	
	    if(ids != ""){
	    	var j = ids.split(",").length;
	    	var i = 1;
	    	for( i = 1; i<=j ; i++ ){
	    		ItemDetails(i, $("#txt_item_id_"+i).val(), $("#txt_item_qty_"+i).val() , 1 , $("#txt_item_price_"+i).val() ,   $("#txt_item_price_disc_"+i).val()   ,    $("#txt_item_price_disc_percent_add_"+i).val() , 0, 0, 0, 'add', rateclass_id);
	    	}
	    }
}

function showHintAccount(url, Hint) {
	$('#' + Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
	$.ajax({
		url : url,
		type : 'GET',
		success : function(data) {
			if (data.trim() != 'SignIn') {
				$('#' + Hint).show();
				$('#' + Hint).fadeIn(500).html('' + data.trim() + '');
				GrandTotal();
				FormElements();
				// alert(Hint);
				// $('#txt_item_desc_'+row_no).focus();
			} else {
				window.location.href = "../portal/";
			}
		}
	});
}

function showHintAddItem(url, Hint) {
	$('#' + Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
	$.ajax({
		url : url,
		type : 'GET',
		success : function(data) {
			if (data.trim() != 'SignIn') {
				$('#' + Hint).show();
				$('#' + Hint).fadeIn(500).html('' + data.trim() + '');
				GrandTotal();
				FormElements();
				var row_no = Hint.split('_')[1];
				$('#txt_item_desc_' + row_no).focus();
			} else {
				window.location.href = "../portal/";
			}
		}
	});
}
// ================================================================================

$('#Hintclicktocall').on('hidden.bs.modal', function() {
	var row_no = $('#dis_row_no').val();
	$('#txt_item_price_disc_' + row_no).val($('#discount_amount').val());
	$('#txt_item_price_disc_percent_add_' + row_no).val($('#discount_per').val());
	CalItemTotal(row_no);
	row_no = eval(row_no) + 1;
	$('#chk_itemlist_' + row_no).focus();
	$("#modal-body").html("<span> &nbsp;&nbsp;Loading... </span> <br> <br>");
});

$('#Hintclicktocall').on('show.bs.modal', function() {
	setTimeout(function() {

		var row_no = $('#dis_row_no').val();
		$('#item_unitprice').val(eval($('#txt_item_price_' + row_no).val()).toFixed(3));
		$('#item_qty').val($('#txt_item_qty_' + row_no).val());
		$('#discount_amount').val((eval($('#txt_item_price_disc_' + row_no).val()) * eval($('#txt_item_qty_' + row_no).val())).toFixed(3));
		$('#discount_per').val($('#txt_item_price_disc_percent_add_' + row_no).val());

	}, 500);

	setTimeout(function() {
		$('#discount_amount').focus();
		var net_amount = eval($('#item_qty').val()) * eval($('#item_unitprice').val());
		var after_dis = net_amount - eval($('#discount_amount').val());
		$('#net_amount').val(net_amount.toFixed(4));
		$('#total').val(after_dis.toFixed(4));
	}, 700);

});

$(".deleteitem").on('click', function() {
	$('.itemlist:checkbox:checked').parents("tr").remove();
});

$(".additem").on('click', function() {
	AddItemList();
});

$(".item_name").on('focus', function() {
	$(".searchresult").hide();
});

var i = 2;

function AddItemList() {
	for (var x = 1; x <= 5; x++) {

		var oldvalue = document.getElementById("txt_itemlistcount").value;
		if (oldvalue > 1) {
			i = document.getElementById("txt_itemlistcount").value;
			i++;
		}
		document.getElementById("txt_itemlistcount").value = i;
		var data = "<tr id='itemindex_" + i + "'>"
		// check box
		+ "<td valign=top align=center> <input class='itemlist' id='chk_itemlist_" + i + "'" + " type='checkbox' name='chk_itemlist_" + i + "' />" + "</td>"
		// Item Name
		+ "<td><input type='text' class='form-control item_name' onkeyup='InvoiceItemSearch(" + i + ");' id='txt_itemname_" + i + "'" + " name='txt_itemname_" + i + "'" + " value=''></td>"
		// QTY
		+ "<td><input type='text' class='form-control' id='txt_item_qty_" + i + "'" + " name='txt_item_qty_" + i + "'" + " value=''></td>"
		// UOM
		+ "<td><input type='text' class='form-control' id='txt_item_uom_" + i + "'" + " name='txt_item_uom_" + i + "'" + " value=''></td>"
		// Price
		+ "<td><input type='text' class='form-control' id='txt_itemprice_" + i + "'" + " name='txt_itemprice_" + i + "'" + " value=''></td>"
		// Discount
		+ "<td><input type='text' class='form-control' id='txt_itemdiscount_" + i + "'" + " name='txt_itemdiscount_" + i + "'" + " value=''></td>"
		// Tax
		+ "<td class='tax-row'><input type='text' class='form-control' id='txt_itemtax_" + i + "'" + " name='txt_itemtax_" + i + "'" + " value=''></td>"
		// Total
		+ "<td><input type='text' class='form-control' id='txt_itemtotal_" + i + "'" + " name='txt_itemtotal_" + i + "'" + " value=''></td>"

		+ "</tr>" + "<tr class='searchresult' id='searchresult_" + i + "_tr' style='display: none;'><td colspan='8' id='searchresult_" + i + "'></td></tr>";
		$('#item_row').append(data);
	}
}

$(function() {
	$('#Hintclicktocall').on(
			'hidden.bs.modal',
			function() {
				var vouchertype_id = document.getElementById('vouchertype_id').value;
				var cart_session_id = document.getElementById('cart_session_id').value;
				var voucher_id = document.getElementById('voucher_id').value;
				showHintFootable('../accounting/invoice-details.jsp?so=yes&cart_session_id=' + cart_session_id + '&cart_vouchertype_id=' + vouchertype_id + '&cart_voucher_id=' + voucher_id
						+ '&list_cartitems=yes&refresh=no', 'invoice_details');
			});

});

// This is to hide the TAX ROW from ITEM LIST
 setInterval(function(){
 $('.tax-row').hide();
 }, 100);
