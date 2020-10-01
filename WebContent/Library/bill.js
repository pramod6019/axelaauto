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
                $('#dialog-modal').html('<iframe src="../account/account-contact-list.jsp?group=select_bill_contact" width="100%" height="100%" frameborder=0></iframe>');
            }
        });
        $('#dialog-modal').dialog('open');
        return true;
    });
});

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
    document.getElementById("span_bill_contact_id").innerHTML = contact_link;
    document.getElementById("span_bill_account_id").innerHTML = account_link;
	}
    var session_id = document.getElementById("txt_session_id").value;
    showHint('bill-details.jsp?status='+status+'&session_id='+session_id+'&list_cartitems=yes',session_id,'bill_details');
	$('#dialog-modal').dialog('close');
}
//end

//Date validation
function ValidateCardDate()
{
    var dtmsg = '';
    var card_startdate = document.getElementById("txt_loyaltycard_date").value;
    if (card_startdate.length!=0)
    {
        pat=/^\d{1,2}(\-|\/|\.)\d{1,2}\/\d{4}$/;
        if(pat.test(card_startdate))
        {
            return (dtmsg);
        }
        else
        {
            dtmsg = "Enter valid 'Effective From' date. \nDate should be in the format dd/mm/yyyy!";
            document.getElementById("txt_loyaltycard_date").focus();
            return (dtmsg);
        }
    }
}
	
//For Delivery Date Concept
function updateSelect(cal)
{
    var date= cal.date;
    var selectMonth = document.getElementById("drop_dmonth");
    selectMonth.selectedIndex = (date.getMonth()+1);
    var selectDay = document.getElementById("drop_dday");
    selectDay.selectedIndex = (date.getDate());
    var selectYear = document.getElementById("drop_dyear");
    //selectYear.selectedIndex = (date.getFullYear()-2011);
    var year_option = document.getElementById("txt_year_option").value;
    //alert(""+year_option+"");
    selectYear.selectedIndex = (date.getFullYear()-parseInt(year_option));
    var selectHour = document.getElementById("drop_dhour");
    selectHour.selectedIndex = (date.getHours()+1);
    var selectMinute = document.getElementById("drop_dmin");
    if(date.getMinutes()==0)
    {
        selectMinute.selectedIndex = 1 ;
    }
    else if(date.getMinutes() > 0 && date.getMinutes() <= 15 )
    {
        selectMinute.selectedIndex = 2 ;
    }
    else if(date.getMinutes() > 15 && date.getMinutes() <= 30 )
    {
        selectMinute.selectedIndex = 3 ;
    }
    else if(date.getMinutes() > 30 && date.getMinutes() <= 59 )
    {
        selectMinute.selectedIndex = 4 ;
    }
}
			
function onloadAmount(){
    setTimeout('onLoadAmount()',500);
}

function GetPaidTotal(){
    var cash = document.getElementById('txt_cash1').value;
    var card1 = document.getElementById('txt_card1').value;
    var card2 = document.getElementById('txt_card2').value;
    var cheque = document.getElementById('txt_cheque').value;

    var total_bill=document.getElementById('txt_bill_grandtotal').value;
    total_bill = CheckNumeric(total_bill);
    //document.getElementById('total_bill').innerHTML=total_bill;
    if(txtID=='txt_cash1_amount'){
        if(!isNaN(amount) && amount!=''){
            amount=document.getElementById('txt_cash1_amount').value;
        }
        document.getElementById('cash_amt').innerHTML=amount;
    }
    if(txtID=='txt_card1_amount'){
        if(!isNaN(amount) && amount!=''){
            amount=document.getElementById('txt_card1_amount').value;
        }
        document.getElementById('card1_amt').innerHTML=amount;
    }
    if(txtID=='txt_card2_amount'){
        if(!isNaN(amount) && amount!=''){
            amount=document.getElementById('txt_card2_amount').value;
        }
        document.getElementById('card2_amt').innerHTML=amount;
    }
    if(txtID=='txt_cheque_amount'){
        if(!isNaN(amount) && amount!=''){
            amount=document.getElementById('txt_cheque_amount').value;
        }
        document.getElementById('cheque_amt').innerHTML=amount;
    }
    var total_payment=0;
    //        alert("cash=="+cash+" card1=="+card1+" card2=="+card2+" cheque=="+cheque);
    if(cash=="1"){
        var cash_amt = document.getElementById('cash_amt').innerHTML;
        cash_amt = CheckNumeric(cash_amt);
        total_payment = total_payment + parseFloat(cash_amt);
    }
    if(card1=="1"){
        var card1_amt = document.getElementById('card1_amt').innerHTML;
        card1_amt = CheckNumeric(card1_amt);
        total_payment = total_payment + parseFloat(card1_amt);
    }
    if(card2=="1"){
        var card2_amt = document.getElementById('card2_amt').innerHTML;
        card2_amt = CheckNumeric(card2_amt);
        total_payment = total_payment + parseFloat(card2_amt);
    }
    if(cheque=="1"){
        var cheque_amt = document.getElementById('cheque_amt').innerHTML;
        cheque_amt = CheckNumeric(cheque_amt);
        total_payment = total_payment + parseFloat(cheque_amt);
    }
    //        alert("cash=="+cash_amt+" card1=="+card1_amt+" card2=="+card2_amt+" cheque=="+cheque_amt);
    document.getElementById('total_payment').innerHTML=total_payment;
    var balance=Math.ceil(parseFloat(total_bill)-parseFloat(total_payment));
    document.getElementById('balance').innerHTML=balance;
}


//function for payment transactions
function onLoadAmount(){
    var total_bill=document.getElementById('txt_bill_grandtotal').value;
    var cash = document.getElementById('txt_cash1').value;
    var card1 = document.getElementById('txt_card1').value;
    var card2 = document.getElementById('txt_card2').value;
    var cheque = document.getElementById('txt_cheque').value;
    var credit = document.getElementById('txt_credit').value;
    var status = document.getElementById('txt_status').value;
    total_bill = CheckNumeric(total_bill);
    if(cash=="1" && status=='Add'){
        document.getElementById('txt_cash1_amount').value=Math.ceil(total_bill);
    }
    setTimeout('onkeyupAmount('+Math.ceil(total_bill)+', "txt_cash1_amount")',500);
    var total_payment=0;
    if(cash=="1"){
        var cash_amt = document.getElementById('cash_amt').innerHTML;
        cash_amt = CheckNumeric(cash_amt);
        total_payment = total_payment + parseFloat(cash_amt);
    }
    if(card1=="1"){
        var card1_amt = document.getElementById('card1_amt').innerHTML;
        card1_amt = CheckNumeric(card1_amt);
        total_payment = total_payment + parseFloat(card1_amt);
    }
    if(card2=="1"){
        var card2_amt = document.getElementById('card2_amt').innerHTML;
        card2_amt = CheckNumeric(card2_amt);
        total_payment = total_payment + parseFloat(card2_amt);
    }
    if(cheque=="1"){
        var cheque_amt = document.getElementById('cheque_amt').innerHTML;
        cheque_amt = CheckNumeric(cheque_amt);
        total_payment = total_payment + parseFloat(cheque_amt);
    }
    if(credit=="1"){
        var credit_amt = document.getElementById('credit_amt').innerHTML;
        credit_amt = CheckNumeric(credit_amt);
        total_payment = total_payment + parseFloat(credit_amt);
    }
    document.getElementById('total_payment').innerHTML=total_payment;
    var balance=Math.ceil(parseFloat(total_bill)-parseFloat(total_payment));
    document.getElementById('balance').innerHTML=balance;
    
}
                       
//Calculating tax, amount, grand total after changing price
function onkeyupAmount(amount,txtID){
    var flag=toFloat(txtID);
    var cash = document.getElementById('txt_cash1').value;
    var card1 = document.getElementById('txt_card1').value;
    var card2 = document.getElementById('txt_card2').value;
    var cheque = document.getElementById('txt_cheque').value;
    var credit = document.getElementById('txt_credit').value;

    if(flag==false){
        amount = CheckNumeric(amount);
        var total_bill=document.getElementById('txt_bill_grandtotal').value;
        total_bill = CheckNumeric(total_bill);
        //document.getElementById('total_bill').innerHTML=total_bill;
        if(txtID=='txt_cash1_amount'){
            if(!isNaN(amount) && amount!=''){
                amount=document.getElementById('txt_cash1_amount').value;
            }
            document.getElementById('cash_amt').innerHTML=amount;
        }
        if(txtID=='txt_card1_amount'){
            if(!isNaN(amount) && amount!=''){
                amount=document.getElementById('txt_card1_amount').value;
            }
            document.getElementById('card1_amt').innerHTML=amount;
        }
        if(txtID=='txt_card2_amount'){
            if(!isNaN(amount) && amount!=''){
                amount=document.getElementById('txt_card2_amount').value;
            }
            document.getElementById('card2_amt').innerHTML=amount;
        }
        if(txtID=='txt_cheque_amount'){
            if(!isNaN(amount) && amount!=''){
                amount=document.getElementById('txt_cheque_amount').value;
            }
            document.getElementById('cheque_amt').innerHTML=amount;
        }
        if(txtID=='txt_credit_amount'){
            if(!isNaN(amount) && amount!=''){
                amount=document.getElementById('txt_credit_amount').value;
            }
            document.getElementById('credit_amt').innerHTML=amount;
        }
        var total_payment=0;
        //        alert("cash=="+cash+" card1=="+card1+" card2=="+card2+" cheque=="+cheque);
        if(cash=="1"){
            var cash_amt = document.getElementById('cash_amt').innerHTML;
            cash_amt = CheckNumeric(cash_amt);
            total_payment = total_payment + parseFloat(cash_amt);
        }
        if(card1=="1"){
            var card1_amt = document.getElementById('card1_amt').innerHTML;
            card1_amt = CheckNumeric(card1_amt);
            total_payment = total_payment + parseFloat(card1_amt);
        }
        if(card2=="1"){
            var card2_amt = document.getElementById('card2_amt').innerHTML;
            card2_amt = CheckNumeric(card2_amt);
            total_payment = total_payment + parseFloat(card2_amt);
        }
        if(cheque=="1"){
            var cheque_amt = document.getElementById('cheque_amt').innerHTML;
            cheque_amt = CheckNumeric(cheque_amt);
            total_payment = total_payment + parseFloat(cheque_amt);
        }
        if(credit=="1"){
            var credit_amt = document.getElementById('credit_amt').innerHTML;
            credit_amt = CheckNumeric(credit_amt);
            total_payment = total_payment + parseFloat(credit_amt);
        }
        //        alert("cash=="+cash_amt+" card1=="+card1_amt+" card2=="+card2_amt+" cheque=="+cheque_amt);
        document.getElementById('total_payment').innerHTML = total_payment;
        var balance=Math.ceil(parseFloat(total_bill)-parseFloat(total_payment));
        document.getElementById('balance').innerHTML=balance;
    }
}


//functions for bill-details.jsp
			
//For Listing Cart Items
function list_cart_items(){
	var status = document.getElementById("txt_status").value;
    var session_id = document.getElementById("txt_session_id").value;
    //    //alert(session_id+"==session_id");
    showHint('bill-details.jsp?status='+status+'&session_id='+session_id+'&list_cartitems=yes','bill_details');
}
				
//For Updating cart item
function update_cart_item(cart_id){
    showHint('bill-item.jsp?cart_id='+cart_id+'&update_cartitem=yes',cart_id,'bill_item');
}
			
//For Deleting cart item
function delete_cart_item(cart_id){
    var session_id = document.getElementById("txt_session_id").value;
    showHint('bill-details.jsp?cart_id='+cart_id+'&session_id='+session_id+'&delete_cartitem=yes',cart_id,'bill_details');
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
    setTimeout('LoadPayment()',500);
}
				
//close functioms for bill-detals.jsp	
			
			
//Checking Cash Tender
function  CashTender(){
    var cash_amt  = document.getElementById("txt_cash1_amount").value;
    var tender_amt = document.getElementById("txt_cash1_tender").value;
    var balance=parseInt(tender_amt-cash_amt);
    document.getElementById("div_change").innerHTML = balance;
    document.getElementById("txt_cash1_change").value = balance;
    if(eval(cash_amt)>eval(tender_amt))
    {
        alert("Tender amount can not be less than amount!!!");
        document.getElementById("txt_cash1_tender").focus();
    }
}

//Calculate the change
function GetChange(){
    var cash_amt  = document.getElementById("txt_cash1_amount").value;
    var tender_amt = document.getElementById("txt_cash1_tender").value;
    var balance=parseInt(tender_amt-cash_amt);
    document.getElementById("div_change").innerHTML = balance;
    document.getElementById("txt_cash1_change").value = balance;
}

/// functions for bill items 	
function BillItemSearch()
{ 
    //document.getElementById("hint_item_qty").innerHTML = "";
    var value = document.getElementById("txt_search").value;
    var rateclass_id=  document.getElementById("txt_rateclass_id	").value;
    var url = "../pos/bill-check.jsp?";
    var param="&q="+ value + "&rateclass_id="+rateclass_id;
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
function ItemDetails(item_id, item_type_id, item_name, qty, base_price, price, price_variable, disc, tax_id, tax, tax_name, item_serial, cart_item_serial, cart_id, loyaltycard_id, loyaltycard_no, loyaltycard_date, mode){
    document.getElementById("txt_item_baseprice").value = base_price;
	document.getElementById("txt_itemprice_updatemode").value = price;
    document.getElementById("txt_item_pricevariable").value = price_variable;
	document.getElementById("txt_item_type_id").value = item_type_id;
    document.getElementById("txt_loyaltycard_id").value = loyaltycard_id;
    document.getElementById("txt_item_qty").readOnly = '';
    document.getElementById("txt_item_id").value = item_id;
	document.getElementById("txt_mode").value = mode;
	document.getElementById("txt_item_serial").value = cart_item_serial;
    var emp_price_update = document.getElementById("emp_bill_priceupdate").value;
    var emp_disc_update = document.getElementById("emp_bill_discountupdate").value;
    if(mode=='add'){
        document.getElementById("mode_button").innerHTML = "<input name=add_button id=add_button type=button class=button value='Add' onClick='AddCartItem();'/>";
    }
    else if(mode=='update')	  {
        document.getElementById("mode_button").innerHTML = "<input name=update_button id=update_button type=button class=button value='Update' onClick='UpdateCartItem();'/>";
    }
    if(price_variable==0 && emp_price_update==0){
        document.getElementById("txt_item_price").readOnly = 'readOnly';
    }
    if(price_variable==0 && emp_disc_update==0){
        document.getElementById("txt_item_disc").readOnly = 'readOnly';
    }
    if(loyaltycard_id>0){
        document.getElementById("loyaltycard_details").style.display = '';
        document.getElementById("txt_loyaltycard_no").value = loyaltycard_no;
        document.getElementById("txt_loyaltycard_date").value = loyaltycard_date;
        document.getElementById("txt_item_qty").readOnly = 'readOnly';
    }else if(loyaltycard_id==0 || loyaltycard_id==null || loyaltycard_id==''){
        document.getElementById("loyaltycard_details").style.display = 'none';
    }
	if(item_serial!=1 && cart_item_serial==''){
		document.getElementById("serial_details").style.display = 'none';
		document.getElementById("txt_serial").value = '';
	}else{
		document.getElementById("serial_details").style.display = '';
		document.getElementById("txt_serial").value = '1';
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
	//alert("item_type_id=="+item_type_id);
	if(item_type_id=='1'){
		showHint('bill-details.jsp?cart_item_id='+item_id+'&mode='+mode+'&cart_id='+cart_id+'&configure=yes','configure-details')
		}else{
			document.getElementById("configure-details").innerHTML = "";
			}
    if(loyaltycard_id>0){
        document.getElementById("txt_loyaltycard_no").focus();
    }else{
        document.getElementById("txt_item_qty").focus();
    }
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
    var card_id=document.getElementById('txt_loyaltycard_id').value;
    var card_no=document.getElementById('txt_loyaltycard_no').value;
	var cart_item_serial=document.getElementById("txt_item_serial").value;
	var serial = document.getElementById("txt_serial").value;
	var item_type_id = document.getElementById("txt_item_type_id").value;
    var card_startdate= '';
    var msg = '';
		if(serial=='1' && cart_item_serial==''){
		alert("Enter Item Serial No.!");
		document.getElementById("txt_item_serial").focus();
		}else{
    if(cart_qty == 0 || cart_qty=='' || cart_qty==null || isNaN(cart_qty)==true){
        cart_qty = 1;
    }
    if(card_id>0){
        if((card_no.length)<4 || isNaN(card_no)==true){
            msg = msg + "Enter valid card number!\n";
            document.getElementById("txt_loyaltycard_no").focus();
        }
        msg = msg + ValidateCardDate();
        card_startdate = document.getElementById('txt_loyaltycard_date').value;
    }
    if(msg!=''){
        alert(msg);
    }else{
        showHint('bill-details.jsp?status='+status+'&session_id='+session_id+'&cart_item_id='+cart_item_id+'&cart_qty='+cart_qty+'&cart_item_serial='+cart_item_serial+'&cart_price='+cart_price+'&cart_discount='+cart_discount+'&cart_tax='+cart_tax+'&cart_tax_id='+cart_tax_id+'&cart_tax_rate='+cart_tax_rate+'&cart_total='+cart_total+'&card_no='+card_no+'&card_startdate='+card_startdate+'&rowcount=yes&add_cartitem=yes','bill_details');
		if(item_type_id=='1'){
		setTimeout('AddConfiguredItems()',200);
		}
	    setTimeout("onLoadAmount()",700);
        document.getElementById("txt_search").focus();
    }
		}
}
//for adding configured items along with the main item
function AddConfiguredItems(){
	var status = document.getElementById("txt_status").value;
	var mode = document.getElementById("txt_mode").value;
	var cart_id=document.getElementById("cart_id").value;
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
			var opitem_serial = document.getElementById("txt_"+i+"_"+j+"_serial").value;
			var opitem_price = document.getElementById("txt_"+i+"_"+j+"_price").value;
			var session_id = document.getElementById("txt_session_id").value;
			showHint('bill-details.jsp?status='+status+'&session_id='+session_id+'&cart_item_id='+opitem_item_id+'&cart_option_group='+opitem_gp_name+'&cart_qty='+opitem_qty+'&cart_item_serial='+opitem_serial+'&cart_price='+opitem_price+'&cart_id='+cart_id+'&rowcount=no&add_cartitem=yes','bill_details');
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
    var card_id=document.getElementById('txt_loyaltycard_id').value;
    var card_no=document.getElementById('txt_loyaltycard_no').value;
	var cart_item_serial=document.getElementById("txt_item_serial").value;
	var serial = document.getElementById("txt_serial").value;
	var item_type_id = document.getElementById("txt_item_type_id").value;
    var card_startdate = '';
    var msg = '';
    if(serial=='1' && cart_item_serial==''){
		alert("Enter Item Serial No.!");
		}else{
    if(cart_qty == 0 || cart_qty=='' || cart_qty==null || isNaN(cart_qty)==true){
        cart_qty = 1;
    }
    if(card_id>0){
        if((card_no.length)<4 || isNaN(card_no)==true){
            msg = msg + "Enter valid card number!\n";
            document.getElementById("txt_loyaltycard_no").focus();
        }
        msg = msg + ValidateCardDate();
        card_startdate = document.getElementById('txt_loyaltycard_date').value;
    }
    if(msg!=''){
        alert(msg);
    }else{
        showHint('bill-details.jsp?status='+status+'&session_id='+session_id+'&cart_id='+cart_id+'&cart_item_id='+cart_item_id+'&cart_qty='+cart_qty+'&cart_item_serial='+cart_item_serial+'&cart_price='+cart_price+'&cart_discount='+cart_discount+'&cart_tax='+cart_tax+'&cart_tax_id='+cart_tax_id+'&cart_tax_rate='+cart_tax_rate+'&cart_total='+cart_total+'&card_no='+card_no+'&card_startdate='+card_startdate+'&update_cartitem=yes','bill_details');
		if(item_type_id=='1'){
		setTimeout('AddConfiguredItems()',200);
		}
        setTimeout('LoadPayment()',500);
        document.getElementById("txt_search").focus();
    }
		}
    
}
		
//for updating items 
function LoadPayment(){
    document.getElementById("mode_button").innerHTML = ' ';
    document.getElementById('loyaltycard_details').style.display = 'none';
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
    
    var total_bill = document.getElementById("txt_bill_grandtotal").value;
    var total_payment = document.getElementById("total_payment").innerHTML;
    var balance = parseFloat(total_bill)-parseFloat(total_payment);
    document.getElementById("balance").innerHTML = balance;
}
// end of function of bill items

function CheckNumeric(num){
    if(isNaN(num) || num=='' || num==null)
    {
        num=0;
    }
    return num;
}