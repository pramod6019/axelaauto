// JavaScript Document			
	 $(function(){
		 var guage = document.getElementById("txt_jc_fuel_guage").value;
		 $("#div_fuel_guage").slider({
			 range: "min",
			 value: guage,
			 min: 0,
			 max: 100,
			 slide: function(event, ui){
				 $("#div_jc_fuel_guage").html(ui.value);
				 $("#txt_jc_fuel_guage").val(ui.value);
				 }
			 });
			 $("#div_jc_fuel_guage").html($("#div_fuel_guage").slider("value"));
			 $("#txt_jc_fuel_guage").val($("#div_fuel_guage").slider("value"));
		 });
		 
		 function FormFocus() { 
                document.form1.txt_jc_title.focus();
            }
			
            function FormSubmit() {
                document.form1.submit();
            }
		 

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
                $('#dialog-modal').html('<iframe src="../account/account-contact-list.jsp?group=select_jobcard_contact" width="100%" height="100%" frameborder=0></iframe>');
            }
        });
        $('#dialog-modal').dialog('open');
        return true;
    });
});
		
function ShowNameHint(){
    var fname = document.getElementById("txt_contact_fname").value;
    var lname = document.getElementById("txt_contact_lname").value;
    showHint('../service/report-check.jsp?contact_fname='+fname+'&contact_lname='+lname,'jobcard_details');
}
				
//For selecting existing contact
function SelectContact(contact_id, contact_name, account_id, account_name){
    var contact_link = '<a href="../account/account-contact-list.jsp?contact_id='+contact_id+'">'+contact_name+'</a>';
    var status = document.getElementById("txt_status").value;
    var account_link = '<a href="../account/account-list.jsp?account_id='+account_id+'">'+account_name+'</a>';
    var selected_account = document.getElementById("span_acct_id").value;
    var jobcard_account = document.getElementById("acct_id").value;
    if(status!='Update' && selected_account==0){
        document.getElementById("contact_name").style.display = 'none';
        document.getElementById("contact_mobile").style.display = 'none';
        document.getElementById("contact_email").style.display = 'none';
        document.getElementById("contact_address1").style.display = 'none';
        document.getElementById("contact_city").style.display = 'none';
        document.getElementById("contact_state").style.display = 'none';
        document.getElementById("selected_contact").style.display = '';
        document.getElementById("copy_cont_address_link").innerHTML = "";
    }
    document.getElementById("span_cont_id").value = contact_id;
    document.getElementById("span_acct_id").value = account_id;
    if(status=='Add' && jobcard_account==0){
        document.getElementById("selected_contact_id").innerHTML = contact_link;
        document.getElementById("selected_account_id").innerHTML = account_link;
    }else if(status=='Update' || selected_account!=0){
        document.getElementById("span_jobcard_contact_id").innerHTML = contact_link;
        document.getElementById("span_jobcard_account_id").innerHTML = account_link;
    }
    GetContactLocationDetails(contact_id);
    document.getElementById("jobcard_details").innerHTML = "";
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

        document.getElementById("txt_bill_address").value = contact_address1;
        if(contact_city!='Select'){
            document.getElementById("txt_bill_city").value = contact_city;
        }
        document.getElementById("txt_bill_pin").value = contact_pin;
        if(contact_state!='Select'){
            document.getElementById("txt_bill_state").value = contact_state;
        }
    }


//function for populating billing address according to the contact
function GetContactLocationDetails(jobcard_contact_id){
    showHint('../service/report-check.jsp?jobcard_contact_id='+jobcard_contact_id,jobcard_contact_id, jobcard_contact_id, 'jobcard_contact');

    setTimeout('PopulateContactLocationDetails()',800);
}

function PopulateContactLocationDetails(){
    var contact = document.getElementById('account').value;
    var contact_arr = contact.split('[&%]');
    document.getElementById('txt_bill_address').value=contact_arr[0];
    document.getElementById('txt_bill_city').value=contact_arr[1];
    document.getElementById('txt_bill_pin').value=contact_arr[2];
    document.getElementById('txt_bill_state').value=contact_arr[3];
    //        document.getElementById('hid_account_branch_id').value=supplier_arr[5];
    //        document.getElementById('hid_account_branch_name').value=supplier_arr[6];
    //alert(supplier_arr[10]);

    document.getElementById('span_acct_id').value=contact_arr[5];
    document.getElementById('span_cont_id').value=contact_arr[7];
}


//function to copy billing address in delivery address fields
function CopyBillingAddress(){
    var billing_address = document.getElementById("txt_bill_address").value;
    var billing_city = document.getElementById("txt_bill_city").value;
    var billing_pin = document.getElementById("txt_bill_pin").value;
    var billing_state = document.getElementById("txt_bill_state").value;
    document.getElementById("txt_del_address").value = billing_address;
    document.getElementById("txt_del_city").value = billing_city;
    document.getElementById("txt_del_pin").value = billing_pin;
    document.getElementById("txt_del_state").value = billing_state;
}

function PopulateModelItem(){
    var item_model_id = document.getElementById('dr_jc_model').value;
			
    if(item_model_id!="" && item_model_id!="0") {
        showHint('../service/report-check.jsp?model_id='+item_model_id,item_model_id, item_model_id, 'modelitem');
    }			
}

function PopulateBranchExecutive(){	
    var jc_branch_id = document.getElementById('dr_branch').value;
    if(jc_branch_id!="" && jc_branch_id!="0") {
        showHint('../service/report-check.jsp?branch_id='+jc_branch_id+'&branch_supervisor=yes', 'dr_executive');
		showHint('../service/report-check.jsp?branch_id='+jc_branch_id+'&branch_technician=yes', 'dr_jc_technician_emp_id');
    }			
}

function PopulateInventoryLocation(){   	
    var jc_branch_id = document.getElementById('dr_branch').value;
	if(jc_branch_id!="" && jc_branch_id!="0") {
        showHint('../service/report-check.jsp?branch_id='+jc_branch_id+'&jclocation=yes', 'dr_location');
    }		
}

//For Listing item Items
function list_jcitems(){
	var jc_id = document.getElementById("txt_jc_id").value;
    var gsttype=document.getElementById("txt_gst_type").value;
    showHint('jobcard-item-details.jsp?jc_id='+jc_id+'&gsttype='+gsttype+'&list_jcitems=yes', 'jcitem_details');
}

//For Deleting item item
function delete_jcitem(jcitem_id,jctrans_billtype_id, jcitem_item_id){
    var jc_id = document.getElementById("txt_jc_id").value;
	var jc_location_id=document.getElementById("jc_location_id").value;
	var item_type_id = document.getElementById("txt_item_type_id").value;
	var gsttype=document.getElementById("txt_gst_type").value;
    showHint('jobcard-item-details.jsp?jcitem_id='+jcitem_id
    		+'&jctrans_billtype_id='+jctrans_billtype_id
    		+'&gsttype='+gsttype
    		+'&type='+item_type_id
    		+'&jcitem_item_id=' +jcitem_item_id
    		+'&jc_location_id='+jc_location_id
    		+'&jc_id='+jc_id
    		+'&delete_jcitem=yes', 'jcitem_details');
    document.getElementById("mode_button").innerHTML = ' ';
    document.getElementById('txt_item_id').value = 0;
    document.getElementById('txt_serial').value = 0;
    document.getElementById("serial_details").style.display = 'none';
    document.getElementById('txt_item_qty').value = 0;
    document.getElementById('txt_item_price').value = 0;
    document.getElementById('txt_item_disc').value = 0;
    document.getElementById('item_name').innerHTML = '';
//    document.getElementById('item_name1').innerHTML = '';
//    document.getElementById('item_name2').innerHTML = '';
//    document.getElementById('item_name3').innerHTML = '';
    document.getElementById('tax_name1').innerHTML = '';
    document.getElementById('tax_name2').innerHTML = '';
//    document.getElementById('tax_name3').innerHTML = 'Tax3';
    document.getElementById('item_tax1').innerHTML = '';
    document.getElementById('item_tax2').innerHTML = '';
//    document.getElementById('item_tax3').innerHTML = 0;
    document.getElementById('txt_item_total').value = 0;
    document.getElementById('item_total').innerHTML = 0;
    document.getElementById("txt_search").focus();
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

function ItemSearch(bool){
//alert("aaaa");
var value = document.getElementById("txt_search").value;
    if(bool == false && value.length >3){
    	setTimeout(function(){ItemSearch(true)},250);
    }
    if(bool == true ){
    	var jc_branch_id = document.getElementById("txt_branch_id").value;
  		var jc_location_id = document.getElementById("jc_location_id").value;
  		var rateclass_type =  document.getElementById("dr_rateclass_type").value;
  		var jctrans_billtype_id = document.getElementById("dr_jctrans_billtype").value;
  		var rateclass_id =  $("#dr_price_rateclass_id").val();
  	    var url = "report-check.jsp?";
  	    var gsttype = document.getElementById("txt_gst_type").value;
  	    var type = "q=" + value + "&jctrans_billtype_id=" + jctrans_billtype_id + "&rateclass_type=" + rateclass_type + "&rateclass_id=" + rateclass_id + "&branch_id=" + jc_branch_id;
  	    var str = "123";
  	  
  	    if(gsttype==''){
  	        alert("Select Customer City!");
  	    }else if(rateclass_id=="0"){
  	    	alert("Select Rate Class!");
  	    }else{
  	    	if(demoVar != value){
	  	    	demoVar = value;
	  	    	showHint(url+type+'&gsttype='+gsttype+'&jc_location_id='+jc_location_id+'', 'hint_search_item');
  	    	}
  	    }
  	    setTimeout(function(){bool = false},250);
    }
}

// for getting the item details
function ItemDetails(
		item_id,
		jctrans_rateclass_id,
		jctrans_billtype_id,
		item_type_id,
		item_type,
		item_ticket_dept_id,
		item_name,
		qty,
		base_price,
		price,
		price_variable,
		disc,
		tax_id1,
		tax_id2,
		tax1,
		tax2,
		tax_name1,
		tax_name2, 
		item_serial,
		jcitem_item_serial,
		jcitem_id,
		current_qty,
		mode){
	//if(parseInt(current_qty)>0 || current_qty=='_')
	//{	
	
	var billtype_id = "dr_jctrans_billtype option[value='"+jctrans_billtype_id+"']";
	var billtype_name = $("#"+billtype_id).text();
	$("#billtype_name").text("Item Details - "+billtype_name);
	
	document.getElementById("txt_item_baseprice").value = base_price;
	document.getElementById("txt_rateclass_id").value = jctrans_rateclass_id;
	document.getElementById("txt_itemprice_updatemode").value = price;
    document.getElementById("txt_item_pricevariable").value = price_variable;
    document.getElementById("txt_item_qty").readOnly = '';
	document.getElementById("txt_item_type_id").value = item_type_id;
	document.getElementById("txt_item_ticket_dept_id").value = item_ticket_dept_id;
	
	//alert("item_type_id=="+item_type_id);
	document.getElementById("txt_item_id").value = item_id;
	if(mode=='add'){
		document.getElementById("txt_rateclass_id").value = $("#dr_price_rateclass_id").val();
	}else{
		document.getElementById("txt_rateclass_id").value = jctrans_rateclass_id;
	}
	document.getElementById("txt_mode").value = mode;
    document.getElementById("txt_item_serial").value = jcitem_item_serial;
    var emp_price_update = document.getElementById("emp_jc_priceupdate").value;
    var emp_disc_update = document.getElementById("emp_jc_discountupdate").value;
    if(mode=='add'){
        document.getElementById("mode_button").innerHTML = "<input name=add_button id=add_button type=button class='btn btn-success' value='Add' onClick='Addjcitem();'/>";
    }
    else if(mode=='update')	  {
        document.getElementById("mode_button").innerHTML = "<input name=update_button id=update_button type=button class='btn btn-success' value='Update' onClick='Updatejcitem();'/>";
    }
    if(price_variable!=1 && emp_price_update!=1){
        document.getElementById("txt_item_price").readOnly = 'readOnly';
    }
    if(price_variable!=1 && emp_disc_update!=1){
        document.getElementById("txt_item_disc").readOnly = 'readOnly';
    }

    if(item_serial!=1 && jcitem_item_serial==''){
        document.getElementById("serial_details").style.display = 'none';
        document.getElementById("txt_serial").value = '';
    }else{
        document.getElementById("serial_details").style.display = '';
        document.getElementById("txt_serial").value = '1';
		document.getElementById("txt_item_qty").readOnly = 'readOnly';
		if(mode=='update')	  {
		document.getElementById("txt_item_serial").readOnly = 'readOnly';
		
		  }else if(mode=='add'  && item_ticket_dept_id!='0' )
		  {
			document.getElementById("serial_details").style.display = 'none';
			
			  }else if(mode=='add' && item_ticket_dept_id=='0')
		  {
		    document.getElementById("serial_details").style.display = '';
			document.getElementById("txt_item_serial").readOnly = '';
			  }
		
    }
    document.getElementById("jcitem_id").value = jcitem_id;
    
    document.getElementById("txt_jctrans_billtype").value = jctrans_billtype_id;
	document.getElementById("txt_current_qty").value = current_qty;
    document.getElementById("txt_item_id").value = item_id;
    document.getElementById("item_name").innerHTML = item_name.replace(/single_quote/g,"'");
    document.getElementById("txt_item_qty").value = qty;
    document.getElementById("txt_item_price").value = price;
    document.getElementById("txt_item_disc").value = disc;
    document.getElementById("txt_item_tax1").value = tax1;
    document.getElementById("txt_item_tax2").value = tax2;
//    document.getElementById("txt_item_tax3").value = tax3;
    document.getElementById("txt_item_tax_id1").value = tax_id1;
    document.getElementById("txt_item_tax_id2").value = tax_id2;
//    document.getElementById("txt_item_tax_id3").value = tax_id3;
//    if(tax_name1=='') tax_name1 = 'Tax1';
//    if(tax_name2=='') tax_name2 = 'Tax2';
//    if(tax_name3=='') tax_name3 = 'Tax3';
    document.getElementById("tax_name1").innerHTML = tax_name1;
    document.getElementById("tax_name2").innerHTML = tax_name2;
//    document.getElementById("tax_name3").innerHTML = tax_name3;
    tax1=parseFloat(tax1).toFixed(2);
    tax2=parseFloat(tax2).toFixed(2);
//    tax3=parseFloat(tax3).toFixed(2);
    var tax=parseFloat(tax1)+parseFloat(tax2);
    if(tax_name1!=''){
    	document.getElementById("item_tax1").innerHTML = parseFloat(((eval(price)-eval(disc))*tax1/100)).toFixed(2);
    }
    if(tax_name2!=''){
    	document.getElementById("item_tax2").innerHTML = parseFloat(((eval(price)-eval(disc))*tax2/100)).toFixed(2);
    }
//    document.getElementById("item_tax3").innerHTML = parseFloat(((eval(price)-eval(disc))*tax3/100)).toFixed(2);
    document.getElementById("item_total").innerHTML = parseFloat(((price-disc)*qty*tax/100)+((price-disc)*qty)).toFixed(2);
    document.getElementById("txt_item_total").value = parseFloat(((price-disc)*qty*tax/100)+((price-disc)*qty)).toFixed(2);
	if(item_type_id!='0' && item_type_id!=''){
		showHint('jobcard-item-details.jsp?jcitem_item_id='+item_id+'&mode='+mode+'&jcitem_id='+jcitem_id+'&configure=yes',  'configure-details')
	}else{
		document.getElementById("configure-details").innerHTML = "";
	}
	 //}else {
           //alert("OUT OF STOCK!");
          //}
	$("#hint_search_item").html("");
	$("#txt_search").val("");
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
    var tax1 = document.getElementById('txt_item_tax1').value;
    var tax2 = document.getElementById('txt_item_tax2').value;
//    var tax3 = document.getElementById('txt_item_tax3').value;
    
    if(qty == 0 || qty == '' || isNaN(qty) == true || qty == null) {
        qty = 1;
    }
    price = CheckNumeric(price);
    disc = CheckNumeric(disc);
    if(eval(disc) > eval(price)) {
        document.getElementById('txt_item_disc').value = price;
        disc = price;
    }
    tax1=parseFloat(tax1).toFixed(2);
    tax2=parseFloat(tax2).toFixed(2);
//    tax3=parseFloat(tax3).toFixed(2);
    var tax=parseFloat(tax1)+parseFloat(tax2);
    if(tax1!=0.00){
    document.getElementById("item_tax1").innerHTML = parseFloat(((eval(price)-eval(disc))*tax1/100)).toFixed(2);
    }
    if(tax2!=0.00){
    document.getElementById("item_tax2").innerHTML = parseFloat(((eval(price)-eval(disc))*tax2/100)).toFixed(2);
    }
//    document.getElementById("item_tax3").innerHTML = parseFloat(((eval(price)-eval(disc))*tax3/100)).toFixed(2);
    document.getElementById("item_total").innerHTML = parseFloat(((eval(price)-eval(disc))*qty*tax/100)+((eval(price)-eval(disc))*qty)).toFixed(2);
    document.getElementById("txt_item_total").value = parseFloat(((eval(price)-eval(disc))*qty*tax/100)+((eval(price)-eval(disc))*qty)).toFixed(2);
}

// for add item to item  table
function Addjcitem(){
	var status = document.getElementById("txt_status").value;
    var jc_id = document.getElementById("txt_jc_id").value;
    var jctrans_billtype_id = document.getElementById("txt_jctrans_billtype").value;
    var rateclass_id = parseInt(document.getElementById("txt_rateclass_id").value);
    var jcitem_item_id=document.getElementById("txt_item_id").value;
    var jcitem_rateclass_id=document.getElementById("txt_rateclass_id").value;
	var jc_location_id=document.getElementById("jc_location_id").value;
    var jcitem_qty = parseInt(document.getElementById("txt_item_qty").value);
	var current_qty=document.getElementById("txt_current_qty").value;
    var jcitem_price=parseFloat(document.getElementById('txt_item_price').value);
    var jcitem_discount=document.getElementById('txt_item_disc').value;
    var jcitem_tax1=document.getElementById('item_tax1').innerHTML;
    var jcitem_tax2=document.getElementById('item_tax2').innerHTML;
    var jcitem_tax_id1=document.getElementById('txt_item_tax_id1').value;
    var jcitem_tax_id2=document.getElementById('txt_item_tax_id2').value;
    var jcitem_tax_rate1=document.getElementById('txt_item_tax1').value;
    var jcitem_tax_rate2=document.getElementById('txt_item_tax2').value;
    var jcitem_total=document.getElementById('txt_item_total').value;
    var jcitem_item_serial=document.getElementById("txt_item_serial").value;
    var serial = document.getElementById("txt_serial").value;
    var gsttype=document.getElementById("txt_gst_type").value;
	var item_type_id = document.getElementById("txt_item_type_id").value;
	var item_ticket_dept_id = document.getElementById("txt_item_ticket_dept_id").value;
	jcitem_tax1=parseFloat(jcitem_tax1).toFixed(2);
	jcitem_tax2=parseFloat(jcitem_tax2).toFixed(2);
    var gsttype=document.getElementById("txt_gst_type").value;
    
		if(serial=='1' && (jcitem_item_serial=='' || jcitem_item_serial=='0') && item_ticket_dept_id=='0'){
	        alert("Enter Item Serial No.!");
	        document.getElementById("txt_item_serial").focus();
	    }else{
	        if(jcitem_qty == 0 || jcitem_qty=='' || jcitem_qty==null || isNaN(jcitem_qty)==true){
	            jcitem_qty = 1;
	        }
	        if(gsttype==''){
	            alert("Enter Customer City.!");
	        }else if(eval($("#dr_rateclass_type").val())==2 && eval(jcitem_price)==0){
	       	 	alert("Price Can't Be Zero");
	        }else{
				showHint('../service/jobcard-item-details.jsp?jc_id=' + jc_id 
						+ '&jcitem_item_id=' + jcitem_item_id
						+ '&jcitem_rateclass_id=' + jcitem_rateclass_id
						+ '&item_ticket_dept_id=' + item_ticket_dept_id
						+ '&jc_location_id=' + jc_location_id
						+ '&jcitem_qty=' + jcitem_qty
						+ '&jcitem_item_serial=' + jcitem_item_serial
						+ '&rateclass_id=' + rateclass_id
						+ '&jctrans_billtype_id='+jctrans_billtype_id 
						+ '&jcitem_price=' + jcitem_price
						+ '&jcitem_discount=' + jcitem_discount
						+ '&jcitem_tax1=' + jcitem_tax1
						+ '&jcitem_tax_id1=' + jcitem_tax_id1
						+ '&jcitem_tax_rate1=' + jcitem_tax_rate1
						+ '&jcitem_tax2=' + jcitem_tax2 
						+ '&jcitem_tax_id2=' + jcitem_tax_id2
						+ '&jcitem_tax_rate2=' + jcitem_tax_rate2
						+ '&gsttype=' + gsttype
						+ '&jcitem_total=' + jcitem_total
						+ '&gsttype='+gsttype
						+ '&type=' + item_type_id 
						+ '&rowcount=yes'
						+ '&add_jcitem=yes','jcitem_details');
	        }
			if(item_type_id=='0' || item_type_id!=''){
			setTimeout('AddConfiguredItems()',200);
			}
			
			document.getElementById("txt_jctrans_billtype").value = "0";
			document.getElementById("mode_button").innerHTML = ' ';
	        document.getElementById('txt_item_id').value = 0;
	        document.getElementById("txt_rateclass_id").value = 0;
	        document.getElementById('txt_serial').value = 0;
	        document.getElementById("serial_details").style.display = 'none';
	        document.getElementById('txt_item_qty').value = 0;
	        document.getElementById('txt_item_price').value = 0;
	        document.getElementById('txt_item_disc').value = 0;
	        document.getElementById('item_name').innerHTML = '';
	        document.getElementById('tax_name1').innerHTML = '';
	        document.getElementById('tax_name2').innerHTML = '';
	        document.getElementById('item_tax1').innerHTML = '';
	        document.getElementById('item_tax2').innerHTML = '';
	        document.getElementById('txt_item_total').value = 0;
	        document.getElementById('item_total').innerHTML = 0;
	        $("#billtype_name").text("Item Details");
	        document.getElementById("txt_search").focus();
			
	    }
	
    
	//}else{
	//	alert("Insufficient Quantity!");
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

//for adding configured items along with the main item
function AddConfiguredItems(){
	var type = document.getElementById("txt_item_type_id").value;
	var jc_id = document.getElementById("txt_jc_id").value;
	var status = document.getElementById("txt_status").value;
	var mode = document.getElementById("txt_mode").value;
	var jcitem_id=document.getElementById("jcitem_id").value;
	var group_count = document.getElementById("txt_group_count").value;
	for(var i=1;i<=group_count;i++){
		var group_item_count = document.getElementById("txt_"+i+"_count").value;
		for(var j=1; j<=group_item_count; j++){
			var item = document.getElementById("chk_"+i+"_"+j);
		if(item.checked==true){
			var opitem_gp_name = document.getElementById("txt_"+i+"_"+j+"_gpname").value;
		    var opitem_item_id = document.getElementById("txt_"+i+"_"+j+"_id").value;
			var opitem_qty = document.getElementById("txt_"+i+"_"+j+"_qty").value;
			var opitem_serial = document.getElementById("txt_"+i+"_"+j+"_serial").value;
			var opitem_price = document.getElementById("txt_"+i+"_"+j+"_price").value;
			
			showHint('jobcard-item-details.jsp?jc_id='+jc_id+'&status='+status+'&jcitem_item_id='+opitem_item_id+'&jcitem_option_group='+opitem_gp_name+'&jcitem_qty='+opitem_qty+'&jcitem_item_serial='+opitem_serial+'&jcitem_price='+opitem_price+'&jcitem_id='+jcitem_id+'&type='+type+'&rowcount=no&add_jcitem=yes','jcitem_details');
			}
		}
		}
	if(mode=='update'){
		document.getElementById("configure-details").innerHTML = "";
		}
	}

//for updating item items
function Updatejcitem(){
	var status = document.getElementById("txt_status").value;
    var jc_id = document.getElementById("txt_jc_id").value;
    var jctrans_billtype_id = document.getElementById("txt_jctrans_billtype").value;
    var rateclass_id = document.getElementById("txt_rateclass_id").value;
    var jcitem_item_id = document.getElementById("txt_item_id").value;
	var jc_location_id = document.getElementById("jc_location_id").value;
    var jcitem_id = document.getElementById("jcitem_id").value;
    var jcitem_qty = document.getElementById("txt_item_qty").value;
	var current_qty = document.getElementById("txt_current_qty").value;
    var jcitem_price = document.getElementById('txt_item_price').value;
    var jcitem_discount = document.getElementById('txt_item_disc').value;
    var jcitem_tax1=document.getElementById('item_tax1').innerHTML;
    var jcitem_tax2=document.getElementById('item_tax2').innerHTML;
    var jcitem_tax_id1=document.getElementById('txt_item_tax_id1').value;
    var jcitem_tax_id2=document.getElementById('txt_item_tax_id2').value;
    var jcitem_tax_rate1=document.getElementById('txt_item_tax1').value;
    var jcitem_tax_rate2=document.getElementById('txt_item_tax2').value;
    var jcitem_total = document.getElementById('txt_item_total').value;
    var jcitem_item_serial = document.getElementById("txt_item_serial").value;
    var serial = document.getElementById("txt_serial").value;
	var item_type_id = document.getElementById("txt_item_type_id").value;
	var item_ticket_dept_id = document.getElementById("txt_item_ticket_dept_id").value;
	
	var gsttype=document.getElementById("txt_gst_type").value;
	jcitem_tax1=parseFloat(jcitem_tax1).toFixed(2);
	jcitem_tax2=parseFloat(jcitem_tax2).toFixed(2);
	
	if(serial=='1' && jcitem_item_serial=='' || jcitem_item_serial=='0'){
        alert("Enter Item Serial No.!");
    }else{
        if(jcitem_qty == 0 || jcitem_qty=='' || jcitem_qty==null || isNaN(jcitem_qty)==true){
            jcitem_qty = 1;
        }
        if(gsttype==''){
            alert("Select Customer City.!");
        }else{
        showHint('jobcard-item-details.jsp?'
        		+ 'jc_id=' + jc_id 
        		+ '&jcitem_id='+jcitem_id
        		+ '&gsttype=' + gsttype 
        		+ '&jctrans_billtype_id=' + jctrans_billtype_id
        		+ '&jcitem_item_id=' + jcitem_item_id
        		+ '&item_ticket_dept_id=' + item_ticket_dept_id
        		+ '&jc_location_id=' + jc_location_id
        		+ '&jcitem_rateclass_id='+rateclass_id
        		+ '&jcitem_qty='+jcitem_qty 
        		+ '&jcitem_item_serial=' + jcitem_item_serial
        		+ '&jcitem_price='+jcitem_price
        		+ '&jcitem_discount=' + jcitem_discount
        		+ '&jcitem_tax1=' + jcitem_tax1 
        		+ '&jcitem_tax_id1=' + jcitem_tax_id1
				+ '&jcitem_tax_rate1=' + jcitem_tax_rate1
				+ '&jcitem_tax2=' + jcitem_tax2
				+ '&jcitem_tax_id2=' + jcitem_tax_id2
				+ '&jcitem_tax_rate2=' + jcitem_tax_rate2
        		+ '&jcitem_total=' + jcitem_total 
        		+ '&update_jcitem=yes','jcitem_details');
        	} if(item_type_id=='0' && item_type_id!=''){
        		setTimeout('AddConfiguredItems()',200);
        	}
		  }
        document.getElementById("mode_button").innerHTML = ' ';
        document.getElementById("txt_jctrans_billtype").value ="0";
        document.getElementById("txt_rateclass_id").value = 0;
        document.getElementById('txt_item_id').value = 0;
        document.getElementById('txt_serial').value = 0;
        document.getElementById("serial_details").style.display = 'none';
        document.getElementById('txt_item_qty').value = 0;
        document.getElementById('txt_item_price').value = 0;
        document.getElementById('txt_item_disc').value = 0;
        document.getElementById('item_name').innerHTML = '';
//        document.getElementById('item_name1').innerHTML = '';
//        document.getElementById('item_name2').innerHTML = '';
//        document.getElementById('item_name3').innerHTML = '';
        document.getElementById('tax_name1').innerHTML = '';
        document.getElementById('tax_name2').innerHTML = '';
//        document.getElementById('tax_name3').innerHTML = 'Tax3';
        document.getElementById('item_tax1').innerHTML ='' ;
        document.getElementById('item_tax2').innerHTML ='' ;
//        document.getElementById('item_tax3').innerHTML = 0;
        document.getElementById('txt_item_total').value = 0;
        document.getElementById('item_total').innerHTML = 0;
        $("#billtype_name").text("Item Details");
        document.getElementById("txt_search").focus();
		
  //  }else{
	//	alert("Insufficient Quantity!");
	//	}
}

function CheckNumeric(num){
    if(isNaN(num) || num=='' || num==null)
    {
        num=0;
    }
    return num;
}