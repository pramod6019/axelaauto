// JavaScript Document
function AddCartTax() {
	var tax_bill_sundry = document.getElementById('dr_bill_sundry').value;
	var tax_bill_sundryarr = uom_id.split("-");
	var tax_id = uom_idarr[0];
	var tax_rate = uom_idarr[1];
	showHintFootable( '../accounting/returns-details.jsp?addcart_billsundry=yes', 'po_details');
}
// For selecting existing contact
function SelectContact(contact_id, contact_name, customer_id, customer_name, customer_rateclass_id, customer_code) {
	// alert("customer_rateclass_id===="+customer_rateclass_id);  
	var vouchertype_email = document.getElementById("txt_vouchertype_email").value;
	var vouchertype_mobile = document.getElementById("txt_vouchertype_mobile").value;
	var vouchertype_dob = document.getElementById("txt_vouchertype_dob").value;
	var vouchertype_dnd = document.getElementById("txt_vouchertype_dnd").value;
	var contact_link = '<a href=../customer/customer-contact-list.jsp?contact_id=' + contact_id + '>' + contact_name + '</a>';
	var customer_link = '<a href=../customer/customer-list.jsp?customer_id='
			+ customer_id + '>' + customer_code + ' (' + customer_name + ')'
			+ '</a>';
	var status = document.getElementById("txt_status").value;
	var selected_account = document.getElementById("span_acct_id").value;
	var invoice_account = document.getElementById("acct_id").value;
	if (customer_rateclass_id != 0) {
		document.getElementById("txt_rateclass_id").value = customer_rateclass_id;
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
	showHintFootable('../accounting/invoice-details.jsp?cart_session_id='
			+ cart_session_id + '&status=' + status + '&cart_vouchertype_id='
			+ vouchertype_id + '&list_cartitems=yes', "invoice_details");
	$('#dialog-modal').dialog('close');
}
// end

// / serching customer with contact fname
function ShowNameHint() {
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
	var name = document.getElementById("txt_customer_name").value;
	var fname = document.getElementById("txt_contact_fname").value;
	var lname = document.getElementById("txt_contact_lname").value;
	showHintFootable( '../accounting/ledger-check.jsp?customer_name=' + name
					+ '&contact_fname=' + fname + '&contact_lname=' + lname
					+ '&cart_vouchertype_id=' + vouchertype_id
					+ '&search_customer=yes', 'invoice_details');
}

// / serching customer with contact mobile
function ShowMobileHint() {
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
	var mobile = document.getElementById("txt_contact_mobile1").value;
	// var session_id = document.getElementById("txt_session_id").value;
	showHintFootable(
			'../accounting/ledger-check.jsp?contact_mobile=' + mobile
					+ '&cart_vouchertype_id=' + vouchertype_id
					+ '&search_customer=yes', 'invoice_details');
}

// / serching customer with contact phone
function ShowPhoneHint() {
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
	var phone = document.getElementById("txt_contact_phone1").value;
	// var session_id = document.getElementById("txt_session_id").value;
	showHintFootable(
			'../accounting/ledger-check.jsp?contact_phone=' + phone
					+ '&cart_vouchertype_id=' + vouchertype_id
					+ '&search_customer=yes', 'invoice_details');
}

// / serching customer with contact email
function ShowEmailHint() {
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
	var email = document.getElementById("txt_contact_email1").value;
	// var session_id = document.getElementById("txt_session_id").value;
	showHintFootable(
			'../accounting/ledger-check.jsp?contact_email=' + email
					+ '&cart_vouchertype_id=' + vouchertype_id
					+ '&search_customer=yes', 'invoice_details');
}

// function to copy billing address in delivery address fields
function CopyConsigneeAddress() {
	var billing_address = document.getElementById("txt_voucher_billing_add").value;
	document.getElementById("txt_voucher_consignee_add").value = billing_address;
}

// functions for invoice-details.jsp

//For Listing Cart Items   
function list_cart_items() {  
	var location_id = CheckNumeric(document.getElementById("txt_location_id").value);   
	var status = document.getElementById("txt_status").value;
	var cart_session_id = CheckNumeric(document.getElementById("txt_session_id").value);      
	// From po,git,grn To git,grn,pr   
	var voucherclass_id = CheckNumeric(document.getElementById("txt_voucherclass_id").value);   
	var vouchertype_id = CheckNumeric(document.getElementById("txt_vouchertype_id").value);      
	var voucher_dcr_request_id = CheckNumeric(document.getElementById("txt_voucher_dcr_request_id").value);
	var voucher_dcr_id = CheckNumeric(document.getElementById("txt_voucher_dcr_id").value);     
	var voucher_grn_return_id = CheckNumeric(document.getElementById("txt_voucher_grn_return_id").value);     
	showHintFootable('../accounting/returns-details.jsp?cart_session_id='+ cart_session_id+'&status='+status+'&location_id='+location_id 
			+ '&cart_vouchertype_id='+ vouchertype_id    
			+ '&voucherclass_id='+ voucherclass_id           
			+ '&voucher_dcr_request_id='+voucher_dcr_request_id+ '&voucher_dcr_id='+voucher_dcr_id+'&voucher_grn_return_id='+voucher_grn_return_id 
			+ '&list_cartitems=yes', 'invoice_details');              
}

   
// For Deleting cart item
function delete_cart_item(cart_id) {
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
	var location_id = document.getElementById("txt_location_id").value;
	var cart_session_id = document.getElementById("txt_session_id").value;
	showHintFootable('../accounting/returns-details.jsp?cart_session_id='
			+ cart_session_id + '&cart_id=' + cart_id + '&cart_vouchertype_id='
			+ vouchertype_id + '&location_id=' + location_id   
			+ '&delete_cartitem=yes', 'invoice_details');

	document.getElementById('mode_button').innerHTML = ' ';
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
	showHintFootable('../accounting/returns-details.jsp?cart_session_id='
			+ cart_session_id + '&cart_vouchertype_id=' + vouchertype_id
			+ '&location_id=' + location_id + '&delete_full_cart=yes',   
			'invoice_details');
}

// close functioms for invoice-detals.jsp

// / functions for invoice items
var i = 0;
function InvoiceItemSearch() {
	var item_serial_id = 0;
	var cart_session_id = CheckNumeric(document.getElementById("txt_session_id").value);
//	alert("cart_session_id==="+cart_session_id);     
	var cart_vouchertype_id = CheckNumeric(document.getElementById("txt_vouchertype_id").value);
//	alert("cart_vouchertype_id==="+cart_vouchertype_id);     
	var value = document.getElementById("txt_search").value;
//	alert("value==="+value);     
	var location_id = CheckNumeric(document.getElementById("txt_location_id").value);
//	alert("location_id==="+location_id);     
	var branch_id = CheckNumeric(document.getElementById("txt_branch_id").value);
//	alert("branch_id==="+branch_id);          
	if (cart_vouchertype_id == 103) {
		item_serial_id = CheckNumeric(document
				.getElementById("drop_item_serial_id").value);   
	} else {
		item_serial_id = 0;
		
	}

	var itemgroup_id = CheckNumeric(document.getElementById("drop_item_itemgroup_id").value);   
//	alert("itemgroup_id==="+itemgroup_id);              
	clearTimeout(i);
	i = setTimeout('callAjax("' + value + '", "' + cart_session_id + '",  "' + cart_vouchertype_id + '", "'  
			+ branch_id + '", "' + location_id + '", "' + itemgroup_id + '", "'
			+ item_serial_id + '")', 1000);   

	/*
	 * var url = "../accounting/invoice-check.jsp?"; var param="q="+ value +
	 * "&cart_session_id="+cart_session_id+"&rateclass_id="+rateclass_id+"&config_inventory_current_stock="+config_inventory_current_stock+"&branch_id="+branch_id+"&location_id="+location_id+"&itemgroup_id="+itemgroup_id+"&item_serial_id="+item_serial_id;
	 * showHintFootable(url+param, 'hint_search_item');
	 */

}

function callAjax(value, cart_session_id, cart_vouchertype_id, branch_id, location_id, itemgroup_id,   
		item_serial_id) {    
	showHintFootable("../accounting/returns-check.jsp?q=" + value                   
			+ "&cart_session_id=" + cart_session_id + "&cart_vouchertype_id=" + cart_vouchertype_id + "&branch_id=" + branch_id
			+ "&location_id=" + location_id + "&itemgroup_id=" + itemgroup_id
			+ "&item_serial_id=" + item_serial_id, "hint_search_item");
}

// price should'nt be less than the base price
function CheckBasePrice() {
	var para = 1;
	var base_price = parseFloat(document.getElementById("txt_item_baseprice").value);
	// alert("base_price=="+base_price);
	var uom_ratio = parseFloat(document.getElementById("uom_ratio").value);
	var alt_qty = parseInt(CheckNumeric(document.getElementById('txt_item_qty').value));
	qty = (alt_qty * uom_ratio);
		// alert("uom_ratio=="+uom_ratio);
	var price_variable = document.getElementById("txt_item_pricevariable").value;
	var price = parseFloat(document.getElementById('txt_item_price').value);
	// alert("price=="+price);
	if (uom_ratio != '' || uom_ratio != null) {
		base_price = (price / uom_ratio);
		// alert("base_price==af=="+base_price);
		document.getElementById("txt_item_baseprice").value = base_price;
	}
	if ((eval(price) < eval(base_price)) && price_variable != 1) {
		document.getElementById('txt_item_price').value = base_price;
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
function ItemDetails(inv_multivoucher_id, item_id, item_name, item_code, uom_id, alt_uom_id, alt_qty, qty, price_amt, cart_amount,            
			discpercent, price_discount1_customer_id, discamount, tax_id1, tax_id2, tax_id3, tax1_name, tax2_name, tax3_name, 
			tax_rate1, tax_rate2,  tax_rate3, tax_customer_id1, tax_customer_id2, tax_customer_id3,                
		price_sales_customer_id, cart_id, mode) {   
	var price_tax2_after_tax1 = 0.0, cart_item_serial = '';
	var price_tax3_after_tax2 = 0.0; cart_item_batch = 0, cart_boxtype_size =0;     
	//cart_amount Main item Amount(i.e qty * alt_qty * price) without tax disc    
//	alert("mode==="+mode);                     
//	alert("inv_multivoucher_id==="+inv_multivoucher_id);    
	voucherclass_id = CheckNumeric(document .getElementById("txt_voucherclass_id").value);  
	vouchertype_id = CheckNumeric(document.getElementById("txt_vouchertype_id").value);
	var cart_session_id = CheckNumeric(document.getElementById("txt_session_id").value);   
//	alert("cart_session_id==="+cart_session_id); 
	var location_id = CheckNumeric(document.getElementById("txt_location_id").value);
//	alert("location_id==="+location_id);   
	var branch_id = CheckNumeric(document.getElementById("txt_branch_id").value);
//	alert("branch_id==="+branch_id);        
	var config_inventory_current_stock = CheckNumeric(document.getElementById("txt_config_inventory_current_stock").value);     
//	alert("config_inventory_current_stock==="+config_inventory_current_stock);
	document.getElementById("cart_id").value = cart_id;
	document.getElementById("uom_id").value = uom_id;
	document.getElementById("alt_uom_id").value = alt_uom_id;   
	document.getElementById("cart_multivoucher_id").value = inv_multivoucher_id;    
//	alert("price_amt==="+price_amt);         
	if (discpercent == '' || discpercent == null) {
		discpercent = 0.0;
	}
	   
	if (item_id != 0) {  
//		alert("item_id=="+item_id);
		showHintFootable('../accounting/returns-details.jsp?cart_session_id='   
				+ cart_session_id 
				+ '&branch_id=' + branch_id
				+ '&location_id=' + location_id     
				+ '&cart_vouchertype_id=' + vouchertype_id         
				+ '&voucherclass_id=' + voucherclass_id 
				+ '&cart_multivoucher_id=' + cart_multivoucher_id
				+ '&cart_item_id=' + item_id    
				+ '&item_name='+ item_name    
				+ '&item_code='+ item_code 
				+ '&cart_discpercent=' + discpercent
				+ '&disc=' + discamount  
				+ '&price_discount1_customer_id=' + price_discount1_customer_id
				+ '&tax1_name=' + tax1_name+ '&tax2_name=' + tax2_name + '&tax3_name=' + tax3_name     
				+ '&tax_rate1=' + tax_rate1 + '&tax_rate2='+ tax_rate2 + '&tax_rate3=' + tax_rate3 
				+ '&tax_id1=' + tax_id1+ '&tax_id2=' + tax_id2 + '&tax_id3=' + tax_id3
				+ '&tax_customer_id1=' + tax_customer_id1 + '&tax_customer_id2='
				+ tax_customer_id2 + '&tax_customer_id3=' + tax_customer_id3
				+ '&cart_boxtype_size='+ cart_boxtype_size
				+ '&config_inventory_current_stock='+ config_inventory_current_stock 
				+ '&price_sales_customer_id=' + price_sales_customer_id
				+ '&cart_item_serial=' + cart_item_serial 
				+ '&cart_item_batch='+ cart_item_batch 
				+ '&price_tax2_after_tax1=' + price_tax2_after_tax1
				+ '&price_tax3_after_tax2=' + price_tax3_after_tax2   
				+ '&cart_alt_qty='+ alt_qty 
				+ '&cart_qty='+ qty      
				+ '&cart_price=' + price_amt          
				+ '&cart_amount=' + cart_amount       
				+ '&price_sales_customer_id =' + price_sales_customer_id      
				+ '&cart_uom_id=' + uom_id   
				+ '&cart_alt_uom_id=' + alt_uom_id + '&mode=' + mode + '&cart_id='   
				+ cart_id + '&configure1=yes','itemdetails');      
		   
		// setTimeout('document.getElementById("txt_item_qty").focus()', 500);
		// setTimeout('CalculateNewTotal()', 200);         
	}   
	if(mode == 'add'){
	document.getElementById("hint_search_item").style.display = 'none';
	}    
}
      
function CalPercent(para) {
	var caldiscpercent = 0.00;
	var price = 0;
	var disc = 0.00;
	var price = parseFloat(CheckNumeric(document
			.getElementById('txt_item_price').value));
	var disc = parseFloat(CheckNumeric(document
			.getElementById('txt_item_price_disc').value));
	if (price != 0) {
		caldiscpercent = ((disc * 100) / eval(price));
	}
	if (caldiscpercent != 0) {
		caldiscpercent = caldiscpercent.toFixed(6);
	} else {
		caldiscpercent = caldiscpercent.toFixed(2);
	}
	document.getElementById('txt_item_price_disc_percent_add').value = caldiscpercent
			.toString();
	setTimeout('CalItemTotal(' + para + ')', 3000);
}

function CalAmount(para) {
	var total_price = 0;
	var group_value = 0;
	var price = parseFloat(CheckNumeric(document
			.getElementById('txt_item_price').value));
	var discpercent = parseFloat(CheckNumeric(document
			.getElementById('txt_item_price_disc_percent_add').value));
	var disc_amount = (eval(price) * (discpercent / 100)).toFixed(2);
	document.getElementById('txt_item_price_disc').value = disc_amount;

}

// for calculating item total and tax
function CalItemTotal(para) {
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

	var alt_qty = CheckNumeric(document.getElementById('txt_item_qty').value);
	if (alt_qty == 0) {
		alt_qty = 1.00;
		// document.getElementById('txt_item_qty').value = alt_qty;
	}
	// alert("alt_qty==="+alt_qty);
	var uom_id = document.getElementById('dr_alt_uom_id').value;   
	var uom_idarr = uom_id.split("-");
	var uom_id = uom_idarr[0];
	document.getElementById('alt_uom_id').value = uom_id;    
	var uom_ratio = uom_idarr[1];
//	 alert("uom_ratio==="+uom_ratio);
	document.getElementById('uom_ratio').value = uom_ratio;
	if (uom_ratio == 0 || uom_ratio == '' || isNaN(uom_ratio) == true
			|| uom_ratio == null) {
		uom_ratio = 1.00;
		qty = alt_qty * uom_ratio;
	} else {
		qty = alt_qty * uom_ratio;
	}
	var price = parseFloat(CheckNumeric(document
			.getElementById('txt_item_price').value));
	var unit_price = parseFloat(CheckNumeric(document
			.getElementById('txt_item_baseprice').value));
	// alert("unit_price==="+unit_price);
	// 1. Code for onchange of uom ---cal
	if (uom_ratio == 1.00 && alt_qty == 1.00) {
		// alert("111");
		document.getElementById('txt_item_price').value = unit_price
				* uom_ratio;
		discpercent = parseFloat(CheckNumeric(document
				.getElementById('txt_item_price_disc_percent_add').value));
		disc = parseFloat(CheckNumeric(document
				.getElementById('txt_item_price_disc').value));
		disc = ((unit_price * uom_ratio) * (discpercent / 100));
		document.getElementById('txt_item_price_disc').value = disc.toFixed(2);
	} else if (uom_ratio != 1.00 && alt_qty == 1.00) {
		// alert("222");
		document.getElementById('txt_item_price').value = unit_price
				* uom_ratio;
		discpercent = parseFloat(CheckNumeric(document
				.getElementById('txt_item_price_disc_percent_add').value));
		disc = parseFloat(CheckNumeric(document
				.getElementById('txt_item_price_disc').value));
		disc = ((unit_price * uom_ratio) * (discpercent / 100));
		document.getElementById('txt_item_price_disc').value = disc.toFixed(2);
	} else if (uom_ratio == 1.00 && alt_qty != 1.00) {
		// alert("444");
		document.getElementById('txt_item_price').value = unit_price
				* uom_ratio;
		discpercent = parseFloat(document
				.getElementById('txt_item_price_disc_percent_add').value);
		disc = ((unit_price * uom_ratio) * (discpercent / 100));
		document.getElementById('txt_item_price_disc').value = disc.toFixed(2);
	} else if ((uom_ratio != 1.00) && (alt_qty != 1.00)) {
		// alert("333");
		document.getElementById('txt_item_price').value = unit_price
				* uom_ratio;
		discpercent = parseFloat(CheckNumeric(document
				.getElementById('txt_item_price_disc_percent_add').value));
		// alert("discpercent==="+discpercent);
		disc = ((unit_price * uom_ratio) * (discpercent / 100));
		// alert("disc==="+disc);
		document.getElementById('txt_item_price_disc').value = disc.toFixed(2);
	}
	// /1. Logic of UOM ends ---and related code continued....
	price = parseFloat(CheckNumeric(document.getElementById('txt_item_price').value));
	// alert("price=111="+price);
	disc = parseFloat(CheckNumeric(document
			.getElementById('txt_item_price_disc').value));
	discpercent = parseFloat(CheckNumeric(document
			.getElementById('txt_item_price_disc_percent_add').value));
	var optioncount = parseFloat(CheckNumeric(document
			.getElementById("txt_optioncount").value));
	if (para == 1) {
		total_price = parseFloat(CheckNumeric(document
				.getElementById('txt_item_baseprice').value));
		if (total_price == '' || total_price == null) {
			total_price = 0;
		}
	} else {
		if (optioncount == 1) {
			total_price = parseFloat(CheckNumeric(document
					.getElementById('txt_defaultselected_total').value));
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

	var disc_type = parseFloat(CheckNumeric(document   
			.getElementById('txt_item_price_disc_type').value));
	disctemp = (eval(price) + eval(total_price));
	temptotal = disctemp;

	price = CheckNumeric(unit_price);
	disc = (price * (discpercent / 100));

	if (eval(disc) > eval(disctemp)) {
		alert("Discount can't be greater than price");
		// setTimeout(CalItemTotal(1),3000);
		// document.getElementById('txt_item_price_disc').value = disctemp;
		// document.getElementById('txt_item_price_disc_percent_add').value =
		// 100;
		document.getElementById('txt_item_price_disc').value = 0;
		document.getElementById('txt_item_price_disc_percent_add').value = 0;
		disc = 0;
		discpercent = 0;
	}

	if (eval(discpercent) > 100) {
		alert("Discount can't be greater than 100%");
		// setTimeout(CalItemTotal(1),3000);
		// document.getElementById('txt_item_price_disc_percent_add').value =
		// 100;
		// document.getElementById('txt_item_price_disc').value = disctemp;
		document.getElementById('txt_item_price_disc_percent_add').value = 0;
		document.getElementById('txt_item_price_disc').value = 0;
		disc = 0;
		discpercent = 0;
	}

	// For Taxes cal (without tax amt including for cal 2nd ,3rd tax amts)
	// and (with tax amt including for cal 2nd ,3rd tax amts if
	// price_tax2_after_tax1 or price_tax3_after_tax2 = 1)
	tax_rate1 = CheckNumeric(document
			.getElementById('txt_item_price_tax_rate1').value);
	tax_rate2 = CheckNumeric(document
			.getElementById('txt_item_price_tax_rate2').value);
	tax_rate3 = CheckNumeric(document
			.getElementById('txt_item_price_tax_rate3').value);
	var price_tax2_after_tax1 = CheckNumeric(document
			.getElementById('txt_price_tax2_after_tax1').value);
	var price_tax3_after_tax2 = CheckNumeric(document
			.getElementById('txt_price_tax3_after_tax2').value);
	// alert("price_tax2_after_tax1==="+price_tax2_after_tax1);

	if (tax_rate1 != 0) {
		document.getElementById('txt_item_tax1').value = parseFloat(
				((eval(unit_price) + eval(total_price) - eval(disc))
						* eval(uom_ratio) * eval(tax_rate1 / 100))).toFixed(2);
		tax_value1 = CheckNumeric(document.getElementById('txt_item_tax1').value);
		// alert("tax_value1==="+tax_value1);
	}
	if (tax_rate2 != 0) {
		if ((price_tax2_after_tax1 == 1) && (tax_rate1 != 0)) {
			document.getElementById('txt_item_tax2').value = parseFloat(
					(((eval(unit_price) + eval(total_price) - eval(disc)) * eval(uom_ratio)) + eval(tax_value1))
							* eval(tax_rate2 / 100)).toFixed(2);
			tax_value2 = CheckNumeric(document.getElementById('txt_item_tax2').value);
			// alert("tax_value2==if="+tax_value2);
		} else {
			document.getElementById('txt_item_tax2').value = parseFloat(
					((eval(unit_price) + eval(total_price) - eval(disc))
							* eval(uom_ratio) * eval(tax_rate2 / 100)))
					.toFixed(2);
			tax_value2 = CheckNumeric(document.getElementById('txt_item_tax2').value);
			// alert("tax_value2=else=="+tax_value2);
		}
	}
	if (tax_rate3 != 0) {
		if ((price_tax3_after_tax2 == 1) && (tax_rate1 != 0)
				&& (tax_rate2 != 0)) {
			document.getElementById('txt_item_tax3').value = parseFloat(
					(((eval(unit_price) + eval(total_price) - eval(disc)) * eval(uom_ratio))
							+ eval(tax_value1) + eval(tax_value2))
							* eval(tax_rate3 / 100)).toFixed(2);
			tax_value3 = CheckNumeric(document.getElementById('txt_item_tax3').value);
			// alert("tax_value3==="+tax_value3);
		} else {
			document.getElementById('txt_item_tax3').value = parseFloat(
					((eval(unit_price) + eval(total_price) - eval(disc))
							* eval(uom_ratio) * eval(tax_rate3 / 100)))
					.toFixed(2);
			tax_value3 = CheckNumeric(document.getElementById('txt_item_tax3').value);
			// alert("tax_value3==="+tax_value3);
		}
	}

	//
	// alert("price==="+price);
	// alert("total_price==="+total_price);
	// alert("disc==="+disc);
	// alert("qty==="+qty);
	// alert("total=="+);
	// alert("cart_total=amt=="+((((eval(price)+eval(total_price)-eval(disc))*uom_ratio)
	// + (eval(tax_value1)+eval(tax_value2)+eval(tax_value3)))) * alt_qty);
	// Final Result
	/**
	 * 
	 */
	/**
	 * 
	 */
	/**
	 * 
	 */
	/**
	 * 
	 */
	document.getElementById("item_total").innerHTML =  parseFloat(
			((((eval(price) - eval(disc)) * uom_ratio) + (eval(tax_value1)
					+ eval(tax_value2) + eval(tax_value3))))
					* alt_qty).toFixed(2);        
	// document.getElementById("txt_item_total").value =
	// ((((eval(price) + eval(total_price) - eval(disc)) * uom_ratio) +
	// (eval(tax_value1)
	// + eval(tax_value2) + eval(tax_value3))))
	// * eval(alt_qty);

}

// for add item to cart table
function AddCartItem() {
//	alert("add");   
	// Refer Addcartitem purchase.js---*** -----
	var disc = 0.00;
	var configitems_total = 0;  
	var ledger_id = 0;
	var vouchertype_id = document.getElementById("txt_vouchertype_id").value;    
	var config_inventory_current_stock = CheckNumeric(document.getElementById("txt_config_inventory_current_stock").value);
	var location_id = CheckNumeric(document.getElementById("dr_voucher_location_id").value);
	var branch_id = CheckNumeric(document.getElementById("txt_branch_id").value);
	var status = document.getElementById("txt_status").value;
	var cart_session_id = CheckNumeric(document.getElementById("txt_session_id").value);

	var cart_voucher_id = CheckNumeric(document.getElementById("txt_voucher_id").value);
//	alert("cart_voucher_id==="+cart_voucher_id);
	var cart_uom_id = CheckNumeric(document.getElementById('uom_id').value);
//	alert("cart_uom_id==="+cart_uom_id);
	var cart_alt_uom_id = CheckNumeric(document.getElementById('alt_uom_id').value);
//	alert("cart_alt_uom_id==="+cart_alt_uom_id);            
	var cart_uom_ratio = document.getElementById('uom_ratio').value;
	var disc_type = CheckNumeric(document.getElementById('txt_item_price_disc_type').value);
	var cart_boxtype_size = document.getElementById("txt_boxtype_size").value;
	var cart_vouchertype_id = CheckNumeric(document.getElementById("txt_vouchertype_id").value);
	var cart_multivoucher_id = CheckNumeric(document.getElementById("cart_multivoucher_id").value);   
	var price_sales_customer_id = CheckNumeric(document.getElementById("txt_item_price_sales_customer_id").value);
	var price_discount1_customer_id = CheckNumeric(document.getElementById("txt_item_price_discount1_customer_id").value);
	var tax_rate1 = parseFloat(CheckNumeric(document.getElementById("txt_item_price_tax_rate1").value));
//	 alert("tax_rate1==="+tax_rate1);
	var tax_customer_id1 = CheckNumeric(document .getElementById("txt_item_price_tax_customer_id1").value);
	var tax_id1 = CheckNumeric(document .getElementById("txt_item_price_tax_id1").value);
//	 alert("tax_id1==="+tax_id1);   
	var tax_rate2 = parseFloat(CheckNumeric(document.getElementById("txt_item_price_tax_rate2").value));
	var tax_customer_id2 = CheckNumeric(document.getElementById("txt_item_price_tax_customer_id2").value);
	var tax_id2 = CheckNumeric(document .getElementById("txt_item_price_tax_id2").value);
	var tax_rate3 = parseFloat(CheckNumeric(document.getElementById("txt_item_price_tax_rate3").value));
	var tax_customer_id3 = CheckNumeric(document.getElementById("txt_item_price_tax_customer_id3").value);
	var tax_id3 = CheckNumeric(document .getElementById("txt_item_price_tax_id3").value);
	var price_tax2_after_tax1 = CheckNumeric(document.getElementById('txt_price_tax2_after_tax1').value);
//	 alert("price_tax2_after_tax1==="+price_tax2_after_tax1);
	var price_tax3_after_tax2 = CheckNumeric(document.getElementById('txt_price_tax3_after_tax2').value);
	var cart_item_id = CheckNumeric(document.getElementById("txt_item_id").value);
	var stock_qty = parseInt(document.getElementById("txt_stock_qty").value);
	var cart_alt_qty = CheckNumeric(document.getElementById('txt_item_qty').value);
	if (cart_alt_qty == 0 || cart_alt_qty == '' || cart_alt_qty == null || isNaN(cart_alt_qty) == true) {
		cart_alt_qty = 1;
	}
	var cart_price = parseFloat(CheckNumeric(document .getElementById('txt_item_price').value));
	cart_price = CheckNumeric(cart_price / (cart_uom_ratio));
	var cart_amount = CheckNumeric(cart_price * cart_alt_qty * cart_uom_ratio);
	var serial = 0;
	var batch = 0;
	var optioncount = 0;
	// From so, delnote To delnote, sales return      
	var voucher_dcr_request_id = CheckNumeric(document.getElementById("txt_voucher_dcr_request_id").value);
	var voucher_dcr_id = CheckNumeric(document.getElementById("txt_voucher_dcr_id").value);        
	var voucher_grn_return_id = CheckNumeric(document.getElementById("txt_voucher_grn_return_id").value);   

	var discpercent = parseFloat(CheckNumeric(document.getElementById('txt_item_price_disc_percent_add').value));

//	disc = (eval(cart_price) * (eval(discpercent) / 100));
		// disc = CheckNumeric(disc/(cart_uom_ratio));

	
	cart_item_serial = '';
	cart_item_batch = 0;  
	var item_ticket_dept_id = 0;  
//	alert("add end");     
	// Final Data To Be Sent
	showHintFootable('../accounting/returns-details.jsp?'
			+ 'cart_session_id=' + cart_session_id
			+ '&cart_voucher_id=' + cart_voucher_id
			+ '&cart_multivoucher_id=' + cart_multivoucher_id
			+ '&cart_discpercent=' + discpercent
			+ '&cart_boxtype_size=' + cart_boxtype_size
			+ '&config_inventory_current_stock=' + config_inventory_current_stock
			+ '&branch_id=' + branch_id
			+ '&location_id=' + location_id
			+ '&status=' + status
			+ '&cart_vouchertype_id=' + cart_vouchertype_id
			+ '&price_sales_customer_id=' + price_sales_customer_id
			+ '&cart_uom_id=' + cart_uom_id
			+ '&cart_uom_ratio=' + cart_uom_ratio
//			+ '&disc=' + disc 
			+ '&price_discount1_customer_id=' + price_discount1_customer_id
			+ '&cart_item_serial=' + cart_item_serial
			+ '&cart_item_batch=' + cart_item_batch
			+ '&tax_rate1=' + tax_rate1
			+ '&tax_rate2=' + tax_rate2
			+ '&tax_rate3=' + tax_rate3
			+ '&tax_id1=' + tax_id1
			+ '&tax_id2=' + tax_id2
			+ '&tax_id3=' + tax_id3
			+ '&tax_customer_id1=' + tax_customer_id1
			+ '&tax_customer_id2=' + tax_customer_id2
			+ '&tax_customer_id3=' + tax_customer_id3
			+ '&cart_item_id=' + cart_item_id
			+ '&item_ticket_dept_id=' + item_ticket_dept_id
			+ '&cart_alt_qty=' + cart_alt_qty
			+ '&cart_price=' + cart_price
			+ '&cart_amount=' + cart_amount
			+ '&configitems_total=' + configitems_total
			+ '&voucher_dcr_request_id=' + voucher_dcr_request_id
			+ '&voucher_dcr_id=' + voucher_dcr_id
			+ '&voucher_grn_return_id='+voucher_grn_return_id
			+ '&price_tax2_after_tax1=' + price_tax2_after_tax1
			+ '&price_tax3_after_tax2=' + price_tax3_after_tax2
			+ '&cart_alt_uom_id=' + cart_alt_uom_id    
			+ '&ledger_id=' + ledger_id
			+ '&rowcount=yes'
			+ '&add_cartitem=yes', 'invoice_details');
	// IF Item is Configured with Sub-Items
	// if(optioncount==1){
	// setTimeout('AddConfiguredItems()',200);
	// }
	document.getElementById("txt_search").value = "";    
	document.getElementById("txt_search").focus();
	document.getElementById("getItemDetails").style.display = 'none';
	document.getElementById("hint_search_item").style.display = 'none';    
	document.getElementById("searchitem").style.display = 'none';

	// }else{
	// alert("Maximum Item Qty in Stock is "+stock_qty+", can't add more than
	// this!");
	// }
}

// For updating cart items
function UpdateCartItem() {
//	 alert("update");   
	var disc = 0.00;
	var cart_uom_ratio = 1;
	var config_inventory_current_stock = CheckNumeric(document.getElementById("txt_config_inventory_current_stock").value);   
	var location_id = CheckNumeric(document.getElementById("txt_location_id").value);
	var branch_id = CheckNumeric(document.getElementById("txt_branch_id").value);
	var cart_session_id = CheckNumeric(document.getElementById("txt_session_id").value);
	var cart_vouchertype_id = CheckNumeric(document.getElementById("txt_vouchertype_id").value);
	var cart_boxtype_size = CheckNumeric(document.getElementById("txt_boxtype_size").value);
	var status = CheckNumeric(document.getElementById("txt_status").value);
	var cart_voucher_id = CheckNumeric(document.getElementById("txt_voucher_id").value);
	var cart_multivoucher_id = CheckNumeric(document.getElementById("cart_multivoucher_id").value);   
	// var disc_type =
	// document.getElementById('txt_item_price_disc_type').value;
	var cart_item_id = CheckNumeric(document.getElementById("txt_item_id").value);
	var cart_id = CheckNumeric(document.getElementById("cart_id").value);
	var cart_alt_qty = CheckNumeric(document.getElementById('txt_item_qty').value);
	var cart_uom_id = CheckNumeric(document.getElementById('uom_id').value);
	var cart_alt_uom_id = CheckNumeric(document.getElementById('alt_uom_id').value);
//	alert("cart_alt_uom_id==="+cart_alt_uom_id);   
	var cart_uom_ratio = CheckNumeric(document.getElementById('uom_ratio').value);
//	alert("cart_uom_ratio==="+cart_uom_ratio);       
	var cart_price = CheckNumeric(document.getElementById('txt_item_price').value); // For
	// getting
	// cart_price
	// with
	// box
	// ratio
	// 1 or
	// >1
	cart_price = cart_price * cart_alt_qty; // eg For box type ratio 10 and
	// alt_qty > 1
	cart_unit_price = cart_price / (cart_alt_qty * cart_uom_ratio); // getting
	// cart_price
	// for 1 qty
	// and box
	// ratio=1
	var cart_amount = parseFloat(cart_unit_price * cart_alt_qty
			* cart_uom_ratio);
	var price_sales_customer_id = CheckNumeric(document
			.getElementById("txt_item_price_sales_customer_id").value);
	var discpercent = parseFloat(CheckNumeric(document
			.getElementById('txt_item_price_disc_percent_add').value));
//	alert("discpercent==="+discpercent);     
//	disc = ((eval(cart_unit_price)) * (eval(discpercent) / 100));
	var price_discount1_customer_id = CheckNumeric(document
			.getElementById("txt_item_price_discount1_customer_id").value);
	var tax_rate1 = CheckNumeric(document
			.getElementById("txt_item_price_tax_rate1").value);
	var tax_customer_id1 = CheckNumeric(document
			.getElementById("txt_item_price_tax_customer_id1").value);
	var tax_id1 = CheckNumeric(document
			.getElementById("txt_item_price_tax_id1").value);
//	alert("tax_id1==="+tax_id1);   
	var tax_rate2 = CheckNumeric(document
			.getElementById("txt_item_price_tax_rate2").value);
	var tax_customer_id2 = CheckNumeric(document .getElementById("txt_item_price_tax_customer_id2").value);
	var tax_id2 = CheckNumeric(document .getElementById("txt_item_price_tax_id2").value);
//	alert("tax_id2==="+tax_id2);   
	var tax_rate3 = CheckNumeric(document .getElementById("txt_item_price_tax_rate3").value);
	var tax_customer_id3 = CheckNumeric(document .getElementById("txt_item_price_tax_customer_id3").value);
	var tax_id3 = CheckNumeric(document .getElementById("txt_item_price_tax_id3").value);
//	alert("tax_id3==="+tax_id3);       
	var price_tax2_after_tax1 = CheckNumeric(document .getElementById('txt_price_tax2_after_tax1').value);
	var price_tax3_after_tax2 = CheckNumeric(document .getElementById('txt_price_tax3_after_tax2').value);
	// From so, delnote To delnote, sales return
	var voucher_dcr_request_id = CheckNumeric(document .getElementById("txt_voucher_dcr_request_id").value);
	var voucher_dcr_id = CheckNumeric(document .getElementById("txt_voucher_dcr_id").value);
	var voucher_grn_return_id = CheckNumeric(document.getElementById("txt_voucher_grn_return_id").value);   
	var serial = 0;
	var batch = 0;   
	cart_item_serial = '';
	cart_item_batch = 0;
	var item_ticket_dept_id = 0;

	if (cart_alt_qty == 0 || cart_alt_qty == '' || cart_alt_qty == 'null') {
		cart_alt_qty = 1;
	}
//	 alert("update end");             
	showHintFootable('../accounting/returns-details.jsp?cart_session_id='   
			+ cart_session_id + '&cart_voucher_id=' + cart_voucher_id
			+ '&cart_multivoucher_id=' + cart_multivoucher_id    
			+ '&location_id=' + location_id
			+ '&config_inventory_current_stock='
			+ config_inventory_current_stock + '&branch_id=' + branch_id
			+ '&status=' + status + '&cart_id=' + cart_id
			+ '&cart_boxtype_size=' + cart_boxtype_size + '&cart_item_id='
			+ cart_item_id + '&item_ticket_dept_id=' + item_ticket_dept_id
			+ '&cart_alt_qty=' + cart_alt_qty + '&cart_uom_id=' + cart_uom_id
			+ '&cart_uom_ratio=' + cart_uom_ratio + '&cart_discpercent='
			+ discpercent + '&cart_item_serial=' + cart_item_serial
			+ '&cart_item_batch=' + cart_item_batch + '&cart_price='
			+ cart_unit_price + '&cart_amount=' + cart_amount
			+ '&price_sales_customer_id=' + price_sales_customer_id
			+ '&cart_vouchertype_id=' + cart_vouchertype_id 
//			+ '&disc=' + disc       
			+ '&price_discount1_customer_id=' + price_discount1_customer_id
			+ '&tax_id1=' + tax_id1 + '&tax_id2=' + tax_id2 + '&tax_id3='
			+ tax_id3 + '&tax_rate1=' + tax_rate1 + '&tax_rate2=' + tax_rate2
			+ '&tax_rate3=' + tax_rate3 + '&tax_customer_id1='
			+ tax_customer_id1 + '&tax_customer_id2=' + tax_customer_id2
			+ '&tax_customer_id3=' + tax_customer_id3 
			+ '&voucher_dcr_request_id=' + voucher_dcr_request_id   
			+ '&voucher_dcr_id=' + voucher_dcr_id     
			+ '&voucher_grn_return_id='+voucher_grn_return_id
			+ '&price_tax2_after_tax1=' + price_tax2_after_tax1  
			+ '&price_tax3_after_tax2=' + price_tax3_after_tax2    
			+ '&cart_alt_uom_id=' + cart_alt_uom_id        
			+ '&update_cartitem=yes', 'invoice_details');
	// For Optional items
	// if(optioncount==1){    
	// setTimeout('AddConfiguredItems()',200);
	// }
	document.getElementById("getItemDetails").style.display = 'none';
	//document.getElementById("searchitem").style.display = 'none';
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
