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
                $('#dialog-modal').html('<iframe src="../account/account-contact-list.jsp?group=select_quote_contact" width="100%" height="100%" frameborder=0></iframe>');
            }
        });
		$('#dialog-modal').dialog('open');
        return true;
    });
});


function ShowNameHint(){
				var fname = document.getElementById("txt_contact_fname").value;
				var lname = document.getElementById("txt_contact_lname").value;
                var session_id = document.getElementById("txt_session_id").value;
				showHint('quote-check.jsp?contact_fname='+fname+'&contact_lname='+lname+'&session_id='+session_id+'&search_name=yes','quote_details');
				}

//For selecting existing contact
function SelectContact(contact_id, contact_name, account_id, account_name){

    var contact_link = '<a href="../account/account-contact-list.jsp?contact_id='+contact_id+'">'+contact_name+'</a>';
    var account_link = '<a href="../account/account-list.jsp?account_id='+account_id+'">'+account_name+'</a>';
	var status = document.getElementById("txt_status").value;
	var selected_account = document.getElementById("span_acct_id").value;
        var quote_account = document.getElementById("acct_id").value;
        //alert("selected_account=="+selected_account+" quote_account=="+quote_account);
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
	document.getElementById("copy_cont_address_link").innerHTML = "";
	}
    document.getElementById("span_cont_id").value = contact_id;
    document.getElementById("span_acct_id").value = account_id;
	if(status=='Add' && quote_account==0){
	document.getElementById("selected_contact_id").innerHTML = contact_link;
    document.getElementById("selected_account_id").innerHTML = account_link;
	}else if(status=='Update' || selected_account!=0){
    document.getElementById("span_quote_contact_id").innerHTML = contact_link;
    document.getElementById("span_quote_account_id").innerHTML = account_link;
	}
	GetContactLocationDetails(contact_id);
    var session_id = document.getElementById("txt_session_id").value;
    showHint('quote-details.jsp?status='+status+'&session_id='+session_id+'&list_cartitems=yes',session_id,'quote_details');
    $('#dialog-modal').dialog('close');
}
//end

//function to copy contact address in billing address fields
                function CopyContactAddress(){
        var contact_address1 = document.getElementById("txt_contact_address1").value;
        var city_obj = document.getElementById("dr_contact_city_id");
        var state_obj = document.getElementById("dr_contact_state_id");
        var contact_city = city_obj.options[city_obj.selectedIndex].text;
        var contact_pin = document.getElementById("txt_contact_pin").value;
        var contact_state = state_obj.options[state_obj.selectedIndex].text;
        document.getElementById("txt_quote_bill_address").value = contact_address1;
        if(contact_city!='Select'){
            document.getElementById("txt_quote_bill_city").value = contact_city;
        }
        document.getElementById("txt_quote_bill_pin").value = contact_pin;
        if(contact_state!='Select'){
            document.getElementById("txt_quote_bill_state").value = contact_state;
        }
    }


//function for populating billing address according to the contact
function GetContactLocationDetails(quote_contact_id){
        showHint('../invoice/quote-check.jsp?quote_contact_id='+quote_contact_id,quote_contact_id,'quote_contact');

        setTimeout('PopulateContactLocationDetails()',1000);
    }

	function PopulateContactLocationDetails(){
        var contact = document.getElementById('account').value;
		var contact_arr = contact.split('[&%]');
        document.getElementById('txt_quote_bill_address').value=contact_arr[0];
        document.getElementById('txt_quote_bill_city').value=contact_arr[1];
        document.getElementById('txt_quote_bill_pin').value=contact_arr[2];
        document.getElementById('txt_quote_bill_state').value=contact_arr[3];
//alert(supplier_arr[10]);

        //document.getElementById('span_acct_id').value=contact_arr[5];
        //document.getElementById('span_cont_id').value=contact_arr[7];
	}


	//function to copy billing address in shipping address fields
	function CopyBillingAddress(){
		var billing_address = document.getElementById("txt_quote_bill_address").value;
		var billing_city = document.getElementById("txt_quote_bill_city").value;
		var billing_pin = document.getElementById("txt_quote_bill_pin").value;
		var billing_state = document.getElementById("txt_quote_bill_state").value;
		document.getElementById("txt_quote_ship_address").value = billing_address;
		document.getElementById("txt_quote_ship_city").value = billing_city;
		document.getElementById("txt_quote_ship_pin").value = billing_pin;
		document.getElementById("txt_quote_ship_state").value = billing_state;
		}


//functions for quote-details.jsp

//For Listing Cart Items
function list_cart_items(){
	var status = document.getElementById("txt_status").value;
    var session_id = document.getElementById("txt_session_id").value;
    showHint('quote-details.jsp?status='+status+'&session_id='+session_id+'&list_cartitems=yes','quote_details');
}

//For Updating cart item
function update_cart_item(cart_id){
    showHint('quote-item.jsp?cart_id='+cart_id+'&update_cartitem=yes',cart_id,'quote_item');
}

//For Deleting cart item
function delete_cart_item(cart_id){
    var session_id = document.getElementById("txt_session_id").value;
    showHint('quote-details.jsp?cart_id='+cart_id+'&session_id='+session_id+'&delete_cartitem=yes',cart_id,'quote_details');
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
	document.getElementById("configure-details").innerHTML = "";
    document.getElementById("txt_search").focus();
}

//close functioms for quote-detals.jsp


/// functions for quote items
function QuoteItemSearch()
{
    //document.getElementById("hint_item_qty").innerHTML = "";
    var value = document.getElementById("txt_search").value;
    var rateclass_id=  document.getElementById("txt_rateclass_id	").value;
    alert(rateclass_id==+rateclass_id);
    var url = "quote-check.jsp?";
    var param="q="+ value + "&rateclass_id="+rateclass_id;
    var str = "123";
    showHint(url+param, str, 'hint_search_item');
}

//price should'nt be less than the base price
function CheckBasePrice(){
    var base_price = document.getElementById("txt_item_baseprice").value;
    var price_variable = document.getElementById("txt_item_pricevariable").value;
    var price = document.getElementById('txt_item_price').value;
    if((eval(price) < eval(base_price)) && price_variable!=1){
        document.getElementById('txt_item_price').value = base_price;
        price = base_price;
        CalItemTotal();
    }

}

//preventing the updation of the item that got configured items
function PreventUpdate(){
	alert("This Item has optional items, hence you can only delete but not update the item!");
	document.getElementById("mode_button").innerHTML = ' ';
    document.getElementById('txt_item_id').value = 0;
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
function ItemDetails(item_id, item_type_id, item_name, qty, base_price, price, price_variable, disc, tax_id, tax, tax_name, cart_id, mode){
	document.getElementById("txt_item_baseprice").value = base_price;
	document.getElementById("txt_itemprice_updatemode").value = price;
    document.getElementById("txt_item_pricevariable").value = price_variable;
	document.getElementById("txt_item_type_id").value = item_type_id;
    document.getElementById("txt_item_qty").readOnly = '';
    document.getElementById("txt_item_id").value = item_id;
	document.getElementById("txt_mode").value = mode;
    var emp_price_update = document.getElementById("emp_quote_priceupdate").value;
    var emp_disc_update = document.getElementById("emp_quote_discountupdate").value;
    if(mode=='add'){
        document.getElementById("mode_button").innerHTML = "<input name=add_button id=add_button type=button class=button value='Add' onClick='AddCartItem();'/>";
    }
    else if(mode=='update')	  {
        document.getElementById("mode_button").innerHTML = "<input name=update_button id=update_button type=button class=button value='Update' onClick='UpdateCartItem();'/>";
    }
	//alert("price_variable=="+price_variable+" emp_price_update=="+emp_price_update);
    if(price_variable==0 && emp_price_update==0){
        document.getElementById("txt_item_price").readOnly = 'readOnly';
    }
    if(price_variable==0 && emp_disc_update==0){
        document.getElementById("txt_item_disc").readOnly = 'readOnly';
    }
    document.getElementById("cart_id").value = cart_id;
    document.getElementById("txt_item_id").value = item_id;
    document.getElementById("item_name").innerHTML = item_name;
    document.getElementById("txt_item_qty").value = qty;
    document.getElementById("txt_item_price").value = price;
    document.getElementById("txt_item_disc").value = disc;
    document.getElementById("txt_item_tax").value = tax;
    document.getElementById("txt_item_tax_id").value = tax_id;
    if(tax_name=='') tax_name = 'Tax';
    document.getElementById("tax_name").innerHTML = tax_name;
    document.getElementById("item_tax").innerHTML = parseFloat(((price-disc)*tax/100)).toFixed(2);
    document.getElementById("item_total").innerHTML = parseFloat(((price-disc)*qty*tax/100)+((price-disc)*qty)).toFixed(2);
    document.getElementById("txt_item_total").value = parseFloat(((price-disc)*qty*tax/100)+((price-disc)*qty)).toFixed(2);
	if(item_type_id=='1'){
		showHint('quote-details.jsp?cart_item_id='+item_id+'&mode='+mode+'&cart_id='+cart_id+'&configure=yes','configure-details')
		}else{
			document.getElementById("configure-details").innerHTML = "";
			}
    document.getElementById("txt_item_qty").focus();
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
	var status = document.getElementById("txt_status").value;
    var session_id=document.getElementById("txt_session_id").value;
    var cart_item_id=document.getElementById("txt_item_id").value;
    var cart_qty=document.getElementById("txt_item_qty").value;
    var cart_price=document.getElementById('txt_item_price').value;
    var cart_discount=document.getElementById('txt_item_disc').value;
    var cart_tax=document.getElementById('item_tax').innerHTML;
    var cart_tax_id=document.getElementById('txt_item_tax_id').value;
    var cart_tax_rate=document.getElementById('txt_item_tax').value;
    var cart_total=document.getElementById('txt_item_total').value;
	var item_type_id = document.getElementById("txt_item_type_id").value;

if(cart_qty == 0 || cart_qty=='' || cart_qty==null || isNaN(cart_qty)==true){
        cart_qty = 1;
    }
    showHint('quote-details.jsp?status='+status+'&session_id='+session_id+'&cart_item_id='+cart_item_id+'&cart_qty='+cart_qty+'&cart_price='+cart_price+'&cart_discount='+cart_discount+'&cart_tax='+cart_tax+'&cart_tax_id='+cart_tax_id+'&cart_tax_rate='+cart_tax_rate+'&cart_total='+cart_total+'&rowcount=yes&add_cartitem=yes','quote_details');
	if(item_type_id=='1'){
		setTimeout('AddConfiguredItems()',200);
		}
        document.getElementById("txt_search").focus();
}

//for adding configured items along with the main item
function AddConfiguredItems(){
	var status = document.getElementById("txt_status").value;
	var mode = document.getElementById("txt_mode").value;
	var cart_id=document.getElementById("cart_id").value;
    var cart_qty=document.getElementById("txt_item_qty").value;
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
			var opitem_price = document.getElementById("txt_"+i+"_"+j+"_price").value;
			var session_id = document.getElementById("txt_session_id").value;
			showHint('quote-details.jsp?status='+status+'&session_id='+session_id+'&cart_item_id='+opitem_item_id+'&cart_option_group='+opitem_gp_name+'&cart_qty='+opitem_qty+'&cart_price='+opitem_price+'&cart_id='+cart_id+'&rowcount=no&add_cartitem=yes','quote_details');
			}
		}
		}
	if(mode=='update'){
		document.getElementById("configure-details").innerHTML = "";
		}
	}

//for updating cart items
function UpdateCartItem(){
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
	var item_type_id = document.getElementById("txt_item_type_id").value;
	
	if(cart_qty == 0 || cart_qty=='' || cart_qty==null || isNaN(cart_qty)==true){
        cart_qty = 1;
    }
    showHint('quote-details.jsp?status='+status+'&session_id='+session_id+'&cart_id='+cart_id+'&cart_item_id='+cart_item_id+'&cart_qty='+cart_qty+'&cart_price='+cart_price+'&cart_discount='+cart_discount+'&cart_tax='+cart_tax+'&cart_tax_id='+cart_tax_id+'&cart_tax_rate='+cart_tax_rate+'&cart_total='+cart_total+'&update_cartitem=yes','quote_details');
	if(item_type_id=='1'){
		setTimeout('AddConfiguredItems()',200);
		}
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
    document.getElementById("txt_search").focus();
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

    var total_quote = document.getElementById("txt_quote_grandtotal").value;
    var total_payment = document.getElementById("total_payment").innerHTML;
    var balance = parseFloat(total_quote)-parseFloat(total_payment);
    document.getElementById("balance").innerHTML = balance;
}
// end of function of quote items

function CheckNumeric(num){
    if(isNaN(num) || num=='' || num==null)
    {
        num=0;
    }
    return num;
}