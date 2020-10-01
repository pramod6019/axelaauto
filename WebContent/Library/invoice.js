// JavaScript Document
$(function(){
    // Dialog
    $('#dialog-modal').dialog({
        autoOpen: false,
        width: 900,
        height: 500,
        zIndex: 200,
        modal: true,
        title: "Select Contact"
    });
    $('#dialog_link').click(function(){

        $.ajax({
            //url: "home.jsp",
            success: function(data){
                $('#dialog-modal').html('<iframe src="../account/account-contact-list.jsp?group=select_invoice_contact" width="100%" height="100%" frameborder=0></iframe>');
            }
        });
	$('#dialog-modal').dialog('open');
        return true;
    });
});


 $(function() {
    $( "#txt_invoice_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });

  });
  
  $(function() {
    $( "#txt_invoice_payment_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });

  });
  function ShowNameHint(){
				var fname = document.getElementById("txt_contact_fname").value;
				var lname = document.getElementById("txt_contact_lname").value;
                var session_id = document.getElementById("txt_session_id").value;
				showHint('invoice-check.jsp?contact_fname='+fname+'&contact_lname='+lname+'&session_id='+session_id+'&search_name=yes','invoice_details');
				}
//For selecting existing contact
function SelectContact(contact_id, contact_name, account_id, account_name){

    var contact_link = '<a href="../account/account-contact-list.jsp?contact_id='+contact_id+'">'+contact_name+'</a>';
    var account_link = '<a href="../account/account-list.jsp?account_id='+account_id+'">'+account_name+'</a>';
	var status = document.getElementById("txt_status").value;
	var selected_account = document.getElementById("span_acct_id").value;
        var invoice_account = document.getElementById("acct_id").value;
//        alert("selected_account=="+selected_account);
	if(status!='Update' && selected_account==0){
	document.getElementById("account_name").style.display = 'none';
    document.getElementById("contact_name").style.display = 'none';
    document.getElementById("contact_mobile").style.display = 'none';
//    document.getElementById("contact_phone").style.display = 'none';
    document.getElementById("contact_email").style.display = 'none';
	document.getElementById("contact_address1").style.display = 'none';
    document.getElementById("contact_city").style.display = 'none';
    document.getElementById("contact_state").style.display = 'none';
	document.getElementById("selected_contact").style.display = '';
	}
    document.getElementById("span_cont_id").value = contact_id;
    document.getElementById("span_acct_id").value = account_id;
	if(status=='Add' && invoice_account==0){
	document.getElementById("selected_contact_id").innerHTML = contact_link;
    document.getElementById("selected_account_id").innerHTML = account_link;
	}else if(status=='Update' || selected_account!=0){
    document.getElementById("span_invoice_contact_id").innerHTML = contact_link;
    document.getElementById("span_invoice_account_id").innerHTML = account_link;
	}
	GetContactLocationDetails(contact_id);
    var session_id = document.getElementById("txt_session_id").value;
    showHint('invoice-details.jsp?status='+status+'&session_id='+session_id+'&list_cartitems=yes',session_id,'invoice_details');
	$('#dialog-modal').dialog('close');
}
//end




    //function for populating billing address according to the contact
    function GetContactLocationDetails(invoice_contact_id){
        showHint('../invoice/invoice-check.jsp?invoice_contact_id='+invoice_contact_id,invoice_contact_id,'invoice_contact');
    }


//functions for invoice-details.jsp

//For Listing Cart Items
function list_cart_items(){
	var status = document.getElementById("txt_status").value;
    var session_id = document.getElementById("txt_session_id").value;
    showHint('invoice-details.jsp?session_id='+session_id+'&status='+status+'&list_cartitems=yes','invoice_details');
}

//For Deleting cart item
function delete_cart_item(cart_id){
    var session_id = document.getElementById("txt_session_id").value;
	var location_id = document.getElementById("txt_location_id").value;
    showHint('invoice-details.jsp?cart_id='+cart_id+'&location_id='+location_id+'&session_id='+session_id+'&delete_cartitem=yes',cart_id,'invoice_details');
	document.getElementById("mode_button").innerHTML = ' ';
    document.getElementById('txt_item_id').value = 0;
    document.getElementById('txt_item_qty').value = 0;
    document.getElementById('txt_item_price').value = 0;
    document.getElementById('txt_item_disc').value = 0;
    document.getElementById('item_name').innerHTML = '';
    document.getElementById('tax_name').innerHTML = 'Tax';
    document.getElementById('item_tax').innerHTML = 0;
    document.getElementById('txt_item_total').value = 0;
    document.getElementById('item_total').innerHTML = 0;
	document.getElementById('configure-details').innerHTML = '';
	document.getElementById("serial_details").style.display = 'none';
    document.getElementById("txt_search").focus();
}

//close functioms for invoice-detals.jsp


/// functions for invoice items
//function InvoiceItemSearch()
//{
//    //document.getElementById("hint_item_qty").innerHTML = "";
//    var value = document.getElementById("txt_search").value;
//    alert(1);
//	var location_id = document.getElementById("txt_location_id").value;
//    var rateclass_id=  document.getElementById("txt_rateclass_id	").value;
//    var url = "invoice-check.jsp?";
////    var param="q="+ value + "&rateclass_id="+rateclass_id+"&location_id="+location_id;
//    var param="q="+ value + "&rateclass_id="+rateclass_id;
//    var str = "123";
//    showHint(url+param, 'hint_search_item');
//}
function InvoiceItemSearch() {
	var item_serial_id = 0;
	var rateclass_id = 0;
	var i =0;
	var customer_id = CheckNumeric(document
			.getElementById("ledger").value);
	var cart_session_id = CheckNumeric(document
			.getElementById("txt_session_id").value);
	var config_inventory_current_stock = CheckNumeric(document
			.getElementById("txt_config_inventory_current_stock").value);
	var cart_vouchertype_id = CheckNumeric(document
			.getElementById("txt_vouchertype_id").value);
	var value = document.getElementById("txt_search").value;
	var location_id = CheckNumeric(document.getElementById("txt_location_id").value);
//	alert("location_id=="+location_id);
	var branch_id = CheckNumeric(document.getElementById("txt_branch_id").value);
	if (cart_vouchertype_id == 103) {
		item_serial_id = CheckNumeric(document
				.getElementById("drop_item_serial_id").value);
		rateclass_id = 0;
		// alert("rateclass_id=bill="+rateclass_id);
	} else {
		item_serial_id = 0;
		rateclass_id = CheckNumeric(document
				.getElementById("dr_voucher_rateclass_id").value);
		// alert("rateclass_id=!!!bill="+rateclass_id);
	}

	var itemgroup_id = CheckNumeric(document
			.getElementById("drop_item_itemgroup_id").value);

	clearTimeout(i);
	i = setTimeout('callAjax("' + value + '", "' + cart_session_id + '", "'
			+ rateclass_id + '", "' + config_inventory_current_stock + '", "'
			+ branch_id + '", "' + location_id + '", "' + itemgroup_id + '", "'
			+ item_serial_id + '", "' + customer_id + '")', 1000);

	/*
	 * var url = "../accounting/invoice-check.jsp?"; var param="q="+ value +
	 * "&cart_session_id="+cart_session_id+"&rateclass_id="+rateclass_id+"&config_inventory_current_stock="+config_inventory_current_stock+"&branch_id="+branch_id+"&location_id="+location_id+"&itemgroup_id="+itemgroup_id+"&item_serial_id="+item_serial_id;
	 * showHintFootable(url+param, 'hint_search_item');
	 */

}

function callAjax(value, cart_session_id, rateclass_id,
		config_inventory_current_stock, branch_id, location_id, itemgroup_id,
		item_serial_id, customer_id) {
	showHint("../accounting/invoice-check.jsp?q=" + value
			+ "&cart_session_id=" + cart_session_id + "&rateclass_id="
			+ rateclass_id + "&config_inventory_current_stock="
			+ config_inventory_current_stock + "&branch_id=" + branch_id
			+ "&location_id=" + location_id + "&itemgroup_id=" + itemgroup_id
			+ "&item_serial_id=" + item_serial_id 
			+ "&customer_id="+ customer_id, "hint_search_item");
}

//price should'nt be less than the base price
function CheckBasePrice(){
    var base_price = document.getElementById("txt_item_baseprice").value;
    var price_variable = document.getElementById("txt_item_pricevariable").value;
    var price = document.getElementById('txt_item_price').value;
    //if((eval(price) < eval(base_price)) && price_variable!=1){
        //document.getElementById('txt_item_price').value = base_price;
        //price = base_price;
        //CalItemTotal();
    //}

}

//preventing the updation of the item that got configured items
function PreventUpdate(){
	alert("This Item has optional items, hence you can only delete but not update the item!");
	document.getElementById("mode_button").innerHTML = ' ';
    document.getElementById('txt_item_id').value = 0;
	document.getElementById('txt_serial').value = 0;
	document.getElementById("serial_details").style.display = 'none';
	document.getElementById("configure-details").innerHTML = '';
    document.getElementById('txt_item_qty').value = 0;
    document.getElementById('txt_item_price').value = 0;
    document.getElementById('txt_item_disc').value = 0;
    document.getElementById('item_name').innerHTML = '';
    document.getElementById('tax_name').innerHTML = 'Tax';
    document.getElementById('item_tax').innerHTML = 0;
    document.getElementById('txt_item_total').value = 0;
    document.getElementById('item_total').innerHTML = 0;
	document.getElementById("txt_search").focus();
	}

// for getting the item details
//function ItemDetails(item_id, item_type_id, item_ticket_dept_id, item_name, qty, base_price, price, price_variable, disc, tax_id, tax, tax_name, item_serial, cart_item_serial, cart_id, stock_qty, mode){
//	//if(stock_qty>0 || stock_qty=='_'){
//	
//	document.getElementById("txt_stock_qty").value = stock_qty;
//	
//    document.getElementById("txt_item_baseprice").value = base_price;
//	document.getElementById("txt_itemprice_updatemode").value = price;
//    document.getElementById("txt_item_pricevariable").value = price_variable;
//	document.getElementById("txt_item_type_id").value = item_type_id;
//	CheckNumeric(document.getElementById("txt_item_ticket_dept_id")).value = item_ticket_dept_id;
//    document.getElementById("txt_item_qty").readOnly = '';
//    document.getElementById("txt_item_id").value = item_id;
//	document.getElementById("txt_mode").value = mode;
//	document.getElementById("txt_item_serial").value = cart_item_serial;
//    var emp_price_update = document.getElementById("emp_invoice_priceupdate").value;
//    var emp_disc_update = document.getElementById("emp_invoice_discountupdate").value;
//    if(mode=='add'){
//        document.getElementById("mode_button").innerHTML = "<input name=add_button id=add_button type=button class=button value='Add' onClick='AddCartItem();'/>";
//    }
//    else if(mode=='update')	  {
//        document.getElementById("mode_button").innerHTML = "<input name=update_button id=update_button type=button class=button value='Update' onClick='UpdateCartItem();'/>";
//    }
//    if(price_variable!=1 && emp_price_update!=1){
//        document.getElementById("txt_item_price").readOnly = 'readOnly';
//    }
//    if(price_variable!=1 && emp_disc_update!=1){
//        document.getElementById("txt_item_disc").readOnly = 'readOnly';
//    }
//	if((item_serial!=1 || item_serial!='') && (cart_item_serial=='' || cart_item_serial==0)){
//		document.getElementById("serial_details").style.display = 'none';
//		document.getElementById("txt_serial").value = '';
//	}else{
//		document.getElementById("serial_details").style.display = '';
//		document.getElementById("txt_serial").value = '1';
//		document.getElementById("txt_item_qty").readOnly = 'readOnly';
//		if(mode=='update')	  { 
//		document.getElementById("txt_item_serial").readOnly = 'readOnly';
//		  }else if(mode=='add' && item_ticket_dept_id!='0')
//		  {
//		    document.getElementById("serial_details").style.display = 'none';
//			  }else if(mode=='add' && item_ticket_dept_id=='0')
//		  {
//		    document.getElementById("serial_details").style.display = '';
//			  }   
//		}
//    document.getElementById("cart_id").value = cart_id;
//    document.getElementById("txt_item_id").value = item_id;
//    document.getElementById("item_name").innerHTML = item_name;
//    document.getElementById("txt_item_qty").value = qty;
//    document.getElementById("txt_item_price").value = price;
//    document.getElementById("txt_item_disc").value = disc;
//    document.getElementById("txt_item_tax").value = tax;
//    document.getElementById("txt_item_tax_id").value = tax_id;
//    if(tax_name=='') tax_name = 'Tax';
//    document.getElementById("tax_name").innerHTML = tax_name;
//    document.getElementById("item_tax").innerHTML = parseFloat(((price-disc)*tax/100)).toFixed(2);
//    document.getElementById("item_total").innerHTML = parseFloat(((price-disc)*qty*tax/100)+((price-disc)*qty)).toFixed(2);
//    document.getElementById("txt_item_total").value = parseFloat(((price-disc)*qty*tax/100)+((price-disc)*qty)).toFixed(2);
//	
//	if(item_type_id=='1'){
//		showHint('invoice-details.jsp?cart_item_id='+item_id+'&mode='+mode+'&cart_id='+cart_id+'&configure=yes','configure-details')
//		}else{
//			document.getElementById("configure-details").innerHTML = "";
//			}
//    document.getElementById("txt_item_qty").focus();
////}else{
//		//alert("Item Out Of Stock!");
//		//}
//}
function ItemDetails(item_id, alt_qty, uom_id, price_amt, price_disc,
		discpercent, boxtype_size, stock_qty, cart_id, mode, rateclass_id) {
	var cart_session_id = CheckNumeric(document
			.getElementById("txt_session_id").value);
	var config_inventory_current_stock = CheckNumeric(document
			.getElementById("txt_config_inventory_current_stock").value);
	var location_id = CheckNumeric(document.getElementById("txt_location_id").value);
	var branch_id = CheckNumeric(document.getElementById("txt_branch_id").value);

	document.getElementById("cart_id").value = cart_id;
	document.getElementById("uom_id").value = uom_id;
	if (rateclass_id == 0) {
		rateclass_id = CheckNumeric(document
				.getElementById("dr_voucher_rateclass_id").value);
		alert("rate class=="+rateclass_id);
		// alert(rateclass_id);
		if (rateclass_id == 0) {
			alert("Select Rate Class! ");
		}
	}
	if (price_amt == 0 || price_amt == '' || isNaN(price_amt) == true
			|| price_amt == null) {
		price_amt = 0.0;
	}
	if (price_disc == 0 || price_disc == '' || isNaN(price_disc) == true
			|| price_disc == null) {
		price_disc = 0.0;
	}
	if (boxtype_size == 0 || boxtype_size == '' || isNaN(boxtype_size) == true
			|| boxtype_size == null) {
		boxtype_size = 0.0;
	}
	voucherclass_id = CheckNumeric(document
			.getElementById("txt_voucherclass_id").value);
	vouchertype_id = CheckNumeric(document.getElementById("txt_vouchertype_id").value);
	if (item_id != 0 && rateclass_id != 0) {
		showHint('../accounting/invoice-details.jsp?cart_session_id='
				+ cart_session_id + '&branch_id=' + branch_id
				+ '&config_inventory_current_stock='
				+ config_inventory_current_stock + '&location_id='
				+ location_id + '&cart_item_id=' + item_id
				+ '&cart_boxtype_size=' + boxtype_size + '&cart_discpercent='
				+ discpercent + '&cart_vouchertype_id=' + vouchertype_id
				+ '&voucherclass_id=' + voucherclass_id + '&cart_alt_qty='
				+ alt_qty + '&cart_price=' + price_amt + '&disc=' + price_disc
				+ '&cart_uom_id=' + uom_id + '&mode=' + mode + '&cart_id='
				+ cart_id + '&configure1=yes&rateclass_id=' + rateclass_id,
				'itemdetails');
		// setTimeout('document.getElementById("txt_item_qty").focus()', 500);
		// setTimeout('CalculateNewTotal()', 200);
	}
	if (mode == 'add') {
		document.getElementById("searchitem").style.display = 'none';
	}
}

function CalculateTotal(){
	  var total = 0;
	  var base_price = 0;
	  var mode = document.getElementById("txt_mode").value;
	  var group_count = parseInt(document.getElementById("txt_group_count").value);
	  var item_type_id = document.getElementById("txt_item_type_id").value;
	  for(var i=1; i<=group_count; i++){
		  total = parseInt(total) + parseInt(CheckNumeric(document.getElementById("txt_"+i+"_value").value));
		  }
		 if(document.getElementById("txt_group_count")!=null && mode=='update'){
			 base_price = document.getElementById("txt_itemprice_updatemode").value
		 }else{
		 base_price = document.getElementById("txt_item_baseprice").value;
		 }
		 
		 document.getElementById("txt_item_price").value = parseInt(total) + parseInt(base_price);
		 CalItemTotal();
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
	  
	  
	  function CalculateMultiSelect(id, item_price, group_item_count, group_id, group_count){
		  GroupValue(group_id, group_item_count, group_count);
		  CalculateTotal();
	  }
	  
// for calculating item total and tax
function CalItemTotal(){
    var qty = document.getElementById('txt_item_qty').value;
    var price = document.getElementById('txt_item_price').value;
    var disc = document.getElementById('txt_item_disc').value;
    var tax = document.getElementById('txt_item_tax').value;
    if(qty == 0 || qty == '' || isNaN(qty) == true || qty == null) {
        qty = 1;
    }
    price = CheckNumeric(price);
    disc = CheckNumeric(disc);
    if(eval(disc) > eval(price)) {
        document.getElementById('txt_item_disc').value = price;
        disc = price;
    }
    document.getElementById("item_tax").innerHTML = parseFloat(((eval(price)-eval(disc))*tax/100)).toFixed(2);
    document.getElementById("item_total").innerHTML = parseFloat(((eval(price)-eval(disc))*qty*tax/100)+((eval(price)-eval(disc))*qty)).toFixed(2);
    document.getElementById("txt_item_total").value = parseFloat(((eval(price)-eval(disc))*qty*tax/100)+((eval(price)-eval(disc))*qty)).toFixed(2);
}

// for add item to cart  table
function AddCartItem(){
	var location_id = document.getElementById("txt_location_id").value;
	var status = document.getElementById("txt_status").value;
    var session_id=document.getElementById("txt_session_id").value;
    var cart_item_id=document.getElementById("txt_item_id").value;
	var stock_qty=document.getElementById("txt_stock_qty").value;
    var cart_qty=parseInt(document.getElementById("txt_item_qty").value);
    var cart_price=document.getElementById('txt_item_price').value;
    var cart_discount=document.getElementById('txt_item_disc').value;
    var cart_tax=document.getElementById('item_tax').innerHTML;
    var cart_tax_id=document.getElementById('txt_item_tax_id').value;
    var cart_tax_rate=document.getElementById('txt_item_tax').value;
    var cart_total=document.getElementById('txt_item_total').value;
	var cart_item_serial=document.getElementById("txt_item_serial").value;
	var serial = document.getElementById("txt_serial").value;
	var item_type_id = document.getElementById("txt_item_type_id").value;
	var item_ticket_dept_id = document.getElementById("txt_item_ticket_dept_id").value;
	//if(cart_qty<=parseInt(stock_qty) || stock_qty=='_'){
	if(serial=='1' && cart_item_serial=='' && item_ticket_dept_id=='0'){
		alert("Enter Item Serial No.!");
		document.getElementById("txt_item_serial").focus();
		}else{

    if(cart_qty == 0 || cart_qty=='' || cart_qty==null || isNaN(cart_qty)==true || serial=='1'){
        cart_qty = 1;
    }
    showHint('invoice-details.jsp?status='+status+'&session_id='+session_id+'&cart_item_id='+cart_item_id+'&item_ticket_dept_id='+item_ticket_dept_id+'&location_id='+location_id+'&cart_qty='+cart_qty+'&cart_item_serial='+cart_item_serial+'&cart_price='+cart_price+'&cart_discount='+cart_discount+'&cart_tax='+cart_tax+'&cart_tax_id='+cart_tax_id+'&cart_tax_rate='+cart_tax_rate+'&cart_total='+cart_total+'&rowcount=yes&add_cartitem=yes','invoice_details');
	//alert("item_type_id=="+item_type_id);
	if(item_type_id=='1'){
		setTimeout('AddConfiguredItems()',200);
		}
	document.getElementById("txt_search").focus();
		}
	//}else{
	//	alert("Maximum Item Qty in Stock is "+stock_qty+", can't add more than this!");
	//	}
}

//for adding configured items along with the main item
function AddConfiguredItems(){
	var status = document.getElementById("txt_status").value;
	var mode = document.getElementById("txt_mode").value;
	var cart_id = document.getElementById("cart_id").value;
    var cart_qty=parseInt(document.getElementById("txt_item_qty").value);
	var group_count = document.getElementById("txt_group_count").value;
	//alert("group_count=="+group_count)
	for(var i=1;i<=group_count;i++){
		var group_item_count = document.getElementById("txt_"+i+"_count").value;
		//alert("group_item_count=="+group_item_count);
		for(var j=1; j<=group_item_count; j++){
			var item = document.getElementById("chk_"+i+"_"+j);
		if(item.checked==true){
			var opitem_gp_name = document.getElementById("txt_"+i+"_"+j+"_gpname").value;
			var opitem_item_id = document.getElementById("txt_"+i+"_"+j+"_id").value;
			var opitem_qty = document.getElementById("txt_"+i+"_"+j+"_qty").value;
			opitem_qty = CheckNumeric(opitem_qty * cart_qty);
			var opitem_serial = document.getElementById("txt_"+i+"_"+j+"_serial").value;
			var opitem_price = document.getElementById("txt_"+i+"_"+j+"_price").value;
			var session_id = document.getElementById("txt_session_id").value;
			showHint('invoice-details.jsp?status='+status+'&session_id='+session_id+'&cart_item_id='+opitem_item_id+'&cart_option_group='+opitem_gp_name+'&cart_qty='+opitem_qty+'&cart_item_serial='+opitem_serial+'&cart_price='+opitem_price+'&cart_id='+cart_id+'&rowcount=no&add_cartitem=yes','invoice_details');
			}
		}
		}
	if(mode=='update'){
		document.getElementById("configure-details").innerHTML = "";
		}
	}

//for updating cart items
function UpdateCartItem(){
	var location_id = document.getElementById("txt_location_id").value;
	var status = document.getElementById("txt_status").value;
    var session_id=document.getElementById("txt_session_id").value;
    var cart_item_id=document.getElementById("txt_item_id").value;
    var cart_id=document.getElementById("cart_id").value;
    var cart_qty=document.getElementById("txt_item_qty").value;
    var cart_price=document.getElementById('txt_item_price').value;
    var cart_discount=document.getElementById('txt_item_disc').value;
    var cart_tax=document.getElementById('item_tax').innerHTML;
    var cart_tax_id=document.getElementById('txt_item_tax_id').value;
    var cart_tax_rate=document.getElementById('txt_item_tax').value;
    var cart_total=document.getElementById('txt_item_total').value;
	var cart_item_serial=document.getElementById("txt_item_serial").value;
	var serial = document.getElementById("txt_serial").value;
	var item_type_id = document.getElementById("txt_item_type_id").value;
	var item_ticket_dept_id = document.getElementById("txt_item_ticket_dept_id").value;
    if(serial=='1' && (cart_item_serial==''||cart_item_serial=='0')){
		alert("Enter Item Serial No.!");
		}else{

    if(cart_qty == 0 || cart_qty=='' || cart_qty==null || isNaN(cart_qty)==true){
        cart_qty = 1;
    }
    showHint('invoice-details.jsp?status='+status+'&session_id='+session_id+'&cart_id='+cart_id+'&cart_item_id='+cart_item_id+'&item_ticket_dept_id='+item_ticket_dept_id+'&location_id='+location_id+'&cart_qty='+cart_qty+'&cart_item_serial='+cart_item_serial+'&cart_price='+cart_price+'&cart_discount='+cart_discount+'&cart_tax='+cart_tax+'&cart_tax_id='+cart_tax_id+'&cart_tax_rate='+cart_tax_rate+'&cart_total='+cart_total+'&update_cartitem=yes','invoice_details');
	//alert("item_type_id=="+item_type_id);
	if(item_type_id=='1'){
		setTimeout('AddConfiguredItems()',200);
		}
    document.getElementById("mode_button").innerHTML = ' ';
    document.getElementById('txt_item_id').value = 0;
	document.getElementById('txt_serial').value = 0;
	document.getElementById("serial_details").style.display = 'none';
    document.getElementById('txt_item_qty').value = 0;
    document.getElementById('txt_item_price').value = 0;
    document.getElementById('txt_item_disc').value = 0;
    document.getElementById('item_name').innerHTML = '';
    document.getElementById('tax_name').innerHTML = 'Tax';
    document.getElementById('item_tax').innerHTML = 0;
    document.getElementById('txt_item_total').value = 0;
    document.getElementById('item_total').innerHTML = 0;
	document.getElementById("txt_search").focus();
		}
}

//for updating items
function LoadPayment(){
    document.getElementById("mode_button").innerHTML = ' ';
    document.getElementById('txt_item_id').value = 0;
    document.getElementById('txt_item_qty').value = 0;
    document.getElementById('txt_item_price').value = 0;
    document.getElementById('txt_item_disc').value = 0;
    document.getElementById('item_name').innerHTML = '';
    document.getElementById('tax_name').innerHTML = 'Tax';
    document.getElementById('item_tax').innerHTML = 0;
    document.getElementById('txt_item_total').value = 0;
    document.getElementById('item_total').innerHTML = 0;

    var total_invoice = document.getElementById("txt_invoice_grandtotal").value;
    var total_payment = document.getElementById("total_payment").innerHTML;
    var balance = parseFloat(total_invoice)-parseFloat(total_payment);
    document.getElementById("balance").innerHTML = balance;
}
// end of function of invoice items

function CheckNumeric(num){
    if(isNaN(num) || num=='' || num==null)
    {
        num=0;
    }
    return num;
}
