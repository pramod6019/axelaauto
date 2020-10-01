$(function() {
	$("#txt_quote_date").datepicker({
		showButtonPanel : true,
		dateFormat : "dd/mm/yy"
	});

});

// JavaScript Document
$(function() {
	// Dialog
	$('#dialog-modal').dialog({
		autoOpen : false,
		width : 900,
		height : 500,
		zIndex : 200,
		modal : true,
		title : "Select Contact"
	});
	$('#dialog_link').click( function() {
			$ .ajax({
						// url: "home.jsp",
						success : function(data) {
							$('#dialog-modal').html( '<iframe src="../account/account-contact-list.jsp?group=select_quote_contact" width="100%" height="100%" frameborder=0></iframe>');
						}
					});
			$('#dialog-modal').dialog('open');
			return true;
	});
});
var i = 0;
function GetConfigurationDetails() {
	var item_id = document.getElementById("dr_item_id").value;
	var quote_date = document.getElementById("txt_quote_date").value;
	var branch_id = document.getElementById("dr_branch").value;
	var enquiry_id = document.getElementById("txt_enquiry_id").value;
	var emp_quote_discountupdate = document .getElementById("emp_quote_discountupdate").value;
	showHint("../sales/veh-quote-check.jsp?get_config=yes&branch_id=" + branch_id
			+ "&enquiry_id="+enquiry_id
			+ "&item_id=" + item_id + "&quote_date=" + quote_date
			+ "&emp_quote_discountupdate=" + emp_quote_discountupdate, "config_details");
	showHint("../sales/veh-quote-check.jsp?branch_emp=yes&branch_id=" + branch_id,  "sales_executive");
}


function PopulateItem(model_id) {
	var branch_id = document.getElementById("txt_branch_id").value;
	var status = document.getElementById("txt_status").value;
	showHint("../sales/veh-quote-check.jsp?list_quote_item=yes&branch_id="
			+ branch_id + "&model_id=" + model_id + '&status=' + status, 
			"div_item");
}


function CheckItemBasePrice(gsttype, price, discount){
	//alert("11111");
//	alert("price==="+price +"\ndiscount==="+discount);
//	alert("gsttype=="+gsttype);
	if(gsttype=='state'){
		var tax_rate1 = parseFloat(document.getElementById("txt_item_tax_rate1").value);
	    var tax_rate2 = parseFloat(document.getElementById("txt_item_tax_rate2").value);
	}else if(gsttype=='central'){
		var tax_rate3 = parseFloat(document.getElementById("txt_item_tax_rate3").value);
	}
    var tax_rate4 = parseFloat(document.getElementById("txt_item_tax_rate4").value);
	var disc = CheckNumeric(document.getElementById("txt_item_disc").value);
	var emp_quote_discountupdate = document.getElementById("emp_quote_discountupdate").value;	
	var emp_quote_priceupdate = document.getElementById("emp_quote_priceupdate").value;
	if(emp_quote_discountupdate=='1'){
		if(eval(price)<eval(disc)){
			document.getElementById("txt_item_disc").value = parseInt(price);
		}
	}else if(emp_quote_discountupdate=='0'){
		if(eval(discount)<eval(disc)){
			document.getElementById("txt_item_disc").value = discount;		
		}
	}
	disc = CheckNumeric(parseInt(document.getElementById("txt_item_disc").value));
	
	if(gsttype=='state'){
		 document.getElementById("div_main_item_total").innerHTML = (((parseFloat(price) - disc) * tax_rate1/100)
	        		+ ((parseFloat(price) - disc) * tax_rate2/100)
	        		+ ((parseFloat(price) - disc) * tax_rate4/100)
	        		+ parseFloat(price) - disc).toFixed(2);
		document.getElementById("div_main_item_amount").value = (((parseFloat(price) - disc) * tax_rate1/100)
								+ ((parseFloat(price) - disc) * tax_rate2/100)
								+ ((parseFloat(price) - disc) * tax_rate4/100)
								+ parseFloat(price) - disc).toFixed(2);
		document.getElementById("div_main_item_amountwod").value = ((parseFloat(price) * tax_rate1/100)
								+ (parseFloat(price) * tax_rate2/100)
								+ (parseFloat(price) * tax_rate4/100)
								+ parseFloat(price)).toFixed(2);
	}else if(gsttype=='central'){
		 document.getElementById("div_main_item_total").innerHTML = (((parseFloat(price) - disc) * tax_rate3/100)
	        		+ ((parseFloat(price) - disc) * tax_rate4/100)
	        		+ parseFloat(price) - disc).toFixed(2);
	document.getElementById("div_main_item_amount").value = (((parseFloat(price) - disc) * tax_rate3/100)
							+ ((parseFloat(price) - disc) * tax_rate4/100)
							+ parseFloat(price) - disc).toFixed(2);
	document.getElementById("div_main_item_amountwod").value = ((parseFloat(price) * tax_rate3/100)
							+ (parseFloat(price) * tax_rate4/100)
							+ parseFloat(price)).toFixed(2);
	}
	
	clearTimeout(i)
	i=setTimeout('CalculateTotal("1")', 200);
//		CalculateTotal('1');
}


//price should'nt be less than the base price
function CheckBasePrice(group_id, group_item_count, group_item_serial, group_count, aftertax, pricetrans_variable){
	var tax = '';
	
	if(aftertax=='1'){
		  	tax = 'at_';
    }else if(aftertax=='0'){
    		tax = 'bt_';
    }
	var tax_rate = parseFloat(document.getElementById("txt_"+tax+group_count+"_"+group_item_serial+"_tax_rate").value);
	var item_qty = parseFloat(document.getElementById("txt_"+tax+group_count+"_"+group_item_serial+"_qty").value);
  
    
    if(pricetrans_variable=='1')
	{
    	var base_price =parseFloat(document.getElementById("txt_at_"+group_count+"_"+group_item_serial+"_price").value);
	}
	else{
		var base_price = parseFloat(document.getElementById("txt_"+tax+group_count+"_"+group_item_serial+"_baseprice").value);
	}
//    var base_price = parseFloat(document.getElementById("txt_"+tax+group_count+"_"+group_item_serial+"_baseprice").value);
	var net_price = parseFloat(document.getElementById("div_"+tax+group_count+"_"+group_item_serial+"_netprice").value);
    var base_disc = parseInt(document.getElementById("txt_"+tax+group_count+"_"+group_item_serial+"_basedisc").value);
	var disc = CheckNumeric(document.getElementById("txt_"+tax+group_count+"_"+group_item_serial+"_disc").value);
	var emp_quote_discountupdate = document.getElementById("emp_quote_discountupdate").value;
	var emp_quote_priceupdate = document.getElementById("emp_quote_priceupdate").value;
	if(emp_quote_discountupdate=='1'){
		if(eval(base_price)<eval(disc)){
			document.getElementById("txt_"+tax+group_count+"_"+group_item_serial+"_disc").value = parseInt(base_price);
		}
	}else if(emp_quote_discountupdate=='0'){
		if(eval(base_disc)<eval(disc)){
			document.getElementById("txt_"+tax+group_count+"_"+group_item_serial+"_disc").value = base_disc;
		}
	}
	disc = CheckNumeric(document.getElementById("txt_"+tax+group_count+"_"+group_item_serial+"_disc").value);
	document.getElementById("div_"+tax+group_count+"_"+group_item_serial+"_total").innerHTML = CheckNumeric(((((base_price - disc)*tax_rate/100)+(base_price - disc)))*item_qty).toFixed(2);
	if(aftertax=='0'){
		document.getElementById("txt_"+tax+group_count+"_"+group_item_serial+"_totalwod").value = CheckNumeric((((base_price*tax_rate/100)+base_price))*item_qty).toFixed(2);
	}
	document.getElementById("div_"+tax+group_count+"_"+group_item_serial+"_amount").value = CheckNumeric(((((base_price - disc)*tax_rate/100)+(base_price - disc)))*item_qty).toFixed(2);
	if(aftertax=='0'){
		document.getElementById("div_"+tax+group_count+"_"+group_item_serial+"_amountwod").value = CheckNumeric((((base_price*tax_rate/100)+base_price))*item_qty).toFixed(2);
	}
	GroupValue(group_id, group_item_count, group_count, aftertax);
	CalculateTotal(aftertax);
}

function CalculateExeDiscount(option_exediscount, addoffer_at_item_count){
	if(option_exediscount=='1'){
		var exediscount = CheckNumeric(document.getElementById("txt_exediscount_at_id").value);
		var price = CheckNumeric(document.getElementById("txt_addoff_at_" + addoffer_at_item_count + "_amt").value);
		
		if(parseFloat(price) > parseFloat(exediscount)){
			document.getElementById("txt_addoff_at_" + addoffer_at_item_count + "_amt").value = parseFloat(exediscount);
		}
		setTimeout('CalculateTotal("1")', 400);
	}
}

function CalculateTotal(aftertax){
//	alert("CalculateTotal==");
	var tax = '';
	var group_value = 0;
	var group_valuewod = 0;
	var group_item_count = 0;
	var total_disc = parseFloat(CheckNumeric(document.getElementById("txt_item_disc").value));
	var addoff_bt_disc_total = 0;
	var addoff_bt_disc_amt = 0;
	var addoff_bt_item_count = document.getElementById("txt_addoff_bt_item_count").value;
	var addoff_bt_item_check = "";
	if(aftertax=='1'){
		  	tax = 'at_'
		  }else if(aftertax=='0'){
		  	tax = 'bt_'
		  }
	  var total = 0;
	  var totalwod = 0;
	  var base_price = 0;
	  var group_count = parseInt(document.getElementById("txt_bt_group_count").value);

      for(var j=1;j<=group_count;j++){
		  group_value = 0;
		  group_valuewod = 0;
		  group_item_count = document.getElementById("txt_bt_"+j+"_count").value;
	  for(var i=1;i<=group_item_count;i++){
		  id = document.getElementById("chk_bt_"+j+"_"+i);
	      item = id.checked;
		  if(item==true){
			  value = parseFloat(document.getElementById("div_bt_"+j+"_"+i+"_total").innerHTML);
			  valuewod = parseFloat(document.getElementById("txt_bt_"+j+"_"+i+"_totalwod").value);
			  total_disc = parseFloat(total_disc) + parseFloat(CheckNumeric(document.getElementById("txt_bt_"+j+"_"+i+"_disc").value));
			  group_value = parseFloat(group_value) + parseFloat(value);
			  group_valuewod = parseFloat(group_valuewod) + parseFloat(valuewod);
		}
	  }
	  total = parseFloat(total) + group_value;
	  totalwod = parseFloat(totalwod) + group_valuewod;
	  }
	  total = parseFloat(document.getElementById("div_main_item_amount").value) + total;
	  totalwod = parseFloat(document.getElementById("div_main_item_amountwod").value) + totalwod;
	  
	  if(addoff_bt_item_count!=0 && addoff_bt_item_count!=''){
		  for(var i=1; i<=addoff_bt_item_count; i++){
			  addoff_bt_item_check = document.getElementById("chk_addoff_bt_"+i);
			  if(addoff_bt_item_check.checked==true){
			  	addoff_bt_disc_amt = parseFloat(CheckNumeric(document.getElementById("txt_addoff_bt_"+i+"_amt").value));
			  	addoff_bt_disc_total = parseFloat(addoff_bt_disc_total) + parseFloat(addoff_bt_disc_amt);
			  if((parseFloat(addoff_bt_disc_total) > parseFloat(total)) || (addoff_bt_disc_amt == '0')){
				  addoff_bt_disc_total = parseFloat(addoff_bt_disc_total) - parseFloat(addoff_bt_disc_amt);
				  document.getElementById("txt_addoff_bt_"+i+"_amt").value = '0';
			  }
			  total_disc = total_disc + parseFloat(CheckNumeric(document.getElementById("txt_addoff_bt_"+i+"_amt").value));
			  }
			  }
		  }
		  
		  if(parseFloat(addoff_bt_disc_total) > parseFloat(total)){
			  addoff_bt_disc_total = total;
		  }
		 total = parseFloat(total) - parseFloat(addoff_bt_disc_total);
		 totalwod = parseFloat(totalwod);
		 document.getElementById("txt_item_price").value = parseFloat(total) .toFixed(2);
		 document.getElementById("txt_total_disc").value = parseFloat(total_disc) .toFixed(2);
		 document.getElementById("div_item_price").innerHTML = parseFloat(total) .toFixed(2);
		 document.getElementById("txt_expricewod").value = parseFloat(totalwod) .toFixed(2);
		 clearTimeout(i);
		 i = setTimeout('EvaluateTax("disc")', 600);
		}

		function CalculateAfterTaxTotal(disc){
			var total_disc = 0;
			if(disc=='disc'){
	        	total_disc = parseFloat(document.getElementById("txt_total_disc").value);
//	        	alert("total_disc==calaftertaxtotal="+total_disc);
			}
			 clearTimeout(i);
			 i = setTimeout('CalculateAfterTaxTotalTimeout("'+disc+'", "'+total_disc+'")', 1000);
			}

		function CalculateAfterTaxTotalTimeout(disc, total_disc){

			var at_total = 0;
	        var base_price = 0;
			var group_value = 0;
			var group_item_count = 0;
			var addoff_at_disc_total = 0;
			var addoff_at_disc_amt = 0;
			var addoff_at_item_count = document.getElementById("txt_addoff_at_item_count").value;
			var addoff_at_item_check = "";
	        total_disc = parseFloat(total_disc);
			var group_count = parseInt(document.getElementById("txt_at_group_count").value);

			for(var j=1; j<=group_count; j++){
		  		group_value = 0;
		  		group_item_count = document.getElementById("txt_at_"+j+"_count").value;
	  			for(var i=1; i<=group_item_count; i++){
		  			id = document.getElementById("chk_at_"+j+"_"+i);
	      			item = id.checked;
		  			if(item==true){
			  			value = parseFloat(document.getElementById("div_at_"+j+"_"+i+"_total").innerHTML);
			  			total_disc = total_disc + parseFloat(CheckNumeric(document.getElementById("txt_at_"+j+"_"+i+"_disc").value));
//				alert("total_disc-->i="+i+"-j="+j+" "+parseFloat(CheckNumeric(document.getElementById("txt_at_"+j+"_"+i+"_disc").value)));
			  			group_value = parseFloat(group_value) + parseFloat(value);
					}
	  			}
	  		at_total = (parseFloat(at_total) + parseFloat(group_value)).toFixed(2);
//	  		alert("at_total=="+at_total);
	  	}

		var bt_total = parseFloat(document.getElementById("div_item_price").innerHTML).toFixed(2);
//		alert("bt_total==>"+bt_total);
		if(addoff_at_item_count!=0 && addoff_at_item_count!=''){
//			 alert("addoff_at_item_count=="+addoff_at_item_count);
		  for(var i=1; i<=addoff_at_item_count; i++){
			  addoff_at_item_check = document.getElementById("chk_addoff_at_"+i);
//			  alert("addoff_at_item_check==="+addoff_at_item_check);
			  if(addoff_at_item_check.checked==true){
			  	addoff_at_disc_amt = parseFloat(CheckNumeric(document.getElementById("txt_addoff_at_"+i+"_amt").value));
			  	addoff_at_disc_total = parseFloat(addoff_at_disc_total) + parseFloat(addoff_at_disc_amt);
				  if((parseFloat(addoff_at_disc_total) > parseFloat(at_total)) || (addoff_at_disc_amt == '0')){
					  addoff_at_disc_total = parseFloat(addoff_at_disc_total) - parseFloat(addoff_at_disc_amt);
					  document.getElementById("txt_addoff_at_"+i+"_amt").value = '0';
				  }
//				  alert("txt_addoff_at_amt=="+document.getElementById("txt_addoff_at_"+i+"_amt").value);
				  total_disc = total_disc + parseFloat(CheckNumeric(document.getElementById("txt_addoff_at_"+i+"_amt").value));
			  	}
			  }
		  }
		  
		  if(parseFloat(addoff_at_disc_total) > parseFloat(at_total)){
			  addoff_at_disc_total = at_total; 
		  }
		at_total = parseFloat(at_total) - parseFloat(addoff_at_disc_total);
		document.getElementById("txt_total_disc").value = parseFloat(total_disc).toFixed(2);
		document.getElementById("div_total_disc").innerHTML = Math.ceil(parseFloat(total_disc)).toFixed(2);
		document.getElementById("div_total_price").innerHTML = Math.ceil(parseFloat(at_total) + parseFloat(bt_total)).toFixed(2);
			}


  function GroupValue(group_id, group_item_count, group_count, aftertax){   
	  var tax = '';
	  var value = 0;
	  var item = '';
	  var id = '';
	  var multiselect_basetotal = 0;
	  if(aftertax=='1'){
		  	tax = 'at_'
		  }else if(aftertax=='0'){
		  	tax = 'bt_'
		  }
	  var group_value = 0;
	  var groups_count = parseInt(document.getElementById("txt_"+tax+"group_count").value);
	  for(var j=1; j<=groups_count; j++){
		  group_value = 0;
		  group_item_count = document.getElementById("txt_"+tax+j+"_count").value;
		
	  	  for(var i=1; i<=group_item_count; i++){
		  	id = document.getElementById("chk_"+tax+j+"_"+i);
	      	item = id.checked;
		  	if(item==true){
			  	value = parseFloat(document.getElementById("div_"+tax+j+"_"+i+"_total").innerHTML);
			  	group_value = parseFloat(group_value) + parseFloat(value);
			}
	  	  }
	    document.getElementById("txt_"+tax+j+"_value").value = group_value;
	  }

	  if(group_id=='3'){
		  multiselect_basetotal = document.getElementById("txt_"+tax+"multiselect_basetotal").value;
		  group_value = parseFloat(group_value) - parseFloat(multiselect_basetotal);
		  }

	  }

	  function EvaluateTax(discount){
		  var bt_total = parseFloat(document.getElementById("div_item_price").innerHTML);
		  var bt_totalwod = parseFloat(document.getElementById("txt_expricewod").value);
		  var at_group_count = parseFloat(document.getElementById("txt_at_group_count").value);
		  for(var i=1; i<=at_group_count; i++){
			  var group_item_count = parseFloat(document.getElementById("txt_at_"+i+"_count").value);
			  for(var j=1; j<=group_item_count; j++){
				  var formulae = document.getElementById("txt_at_"+i+"_"+j+"_tax_formulae").value;
				  var tax_value = '';
				  if(formulae.indexOf("expricewd") != -1){
				  	tax_value = formulae.replace(/expricewd/g,bt_total);
				  }else if(formulae.indexOf("expricewod") != -1){
				  	tax_value = formulae.replace(/expricewod/g,bt_totalwod);
				  }
				  if(tax_value!=''){
				  	document.getElementById("div_at_"+i+"_"+j+"_price").innerHTML = parseFloat(eval(tax_value)).toFixed(2);
				  	document.getElementById("div_at_"+i+"_"+j+"_netprice").value = parseFloat(eval(tax_value)).toFixed(2);
				  	document.getElementById("txt_at_"+i+"_"+j+"_baseprice").value = parseFloat(eval(tax_value)).toFixed(2);
				  	var disc = CheckNumeric(document.getElementById("txt_at_"+i+"_"+j+"_disc").value);
				  	var total = ((parseFloat(eval(tax_value)).toFixed(2))-parseInt(disc)).toFixed(2);
				  	document.getElementById("div_at_"+i+"_"+j+"_total").innerHTML = total;
				  	document.getElementById("div_at_"+i+"_"+j+"_amount").value = total;
				  	}
				 }
			  }
			  
				clearTimeout(i);
			  if(discount=="disc"){
				i = setTimeout('CalculateAfterTaxTotal("disc")', 800);
			  }else{
				i = setTimeout('CalculateAfterTaxTotal("")', 800);
			  }
		  }


  function CalculateDefault(group_id, group_item_count, count, group_count, aftertax){
//	  alert("CalculateDefault==");
	  GroupValue(group_id, group_item_count, group_count, aftertax);
	  var execute = "1";
//	  if(aftertax=='0'){
//		  var group_stock = document.getElementById("txt_bt_gp_"+group_count+"_stock").value;
//		  alert("group_stock="+group_stock);
//		  if(group_stock=='1'){
//			  alert(document.getElementById("chk_bt_"+group_count+"_"+count));
//			  document.getElementById("chk_bt_"+group_count+"_"+count).checked='';
//			  execute = "0";
//			  return false;
//			  }
//	  }
			  
	  if(execute=='1'){
		  var tax = '';
		  if(aftertax=='1'){
				tax = 'at_';
			  }else if(aftertax=='0'){
				tax = 'bt_';
			  }   
	
		  var new_value =  parseFloat(document.getElementById("div_"+tax+group_count+"_"+count+"_total").innerHTML);
		  var base_value = "";
		  for(var i=1; i<=group_item_count; i++){
			  var selected_item = document.getElementById("chk_"+tax+group_count+"_"+i).checked;
			  if(selected_item==true){
				  base_value = document.getElementById("txt_"+tax+group_id+"_basevalue").value;
				  if(count!=i){
					document.getElementById("chk_"+tax+group_count+"_"+i).checked='';
				  }
				}
			  }
	
			 var diff = parseFloat(new_value) - parseFloat(base_value);
			 var new_group_value = parseFloat(diff);
			 document.getElementById("txt_"+tax+group_count+"_value").value = new_group_value;
			 CalculateTotal(aftertax);
	 	 }
	  }


	  function CalculateMultiSelect(id, item_price, group_item_count, group_id, group_count, aftertax){
		  GroupValue(group_id, group_item_count, group_count, aftertax);
		  CalculateTotal(aftertax);
	  }  

function GetTotal(value, target) {
//	var price = parseFloat(document.getElementById("txt_item_price").value);
//	var extra = (eval(price) * eval(value) / 100);
//	if (target == 'tax') {
//		document.getElementById("div_road_tax").innerHTML = extra;
//	} else if (target == 'insurance') {
//		document.getElementById("div_insurance").innerHTML = extra;
//	}  
//	document.getElementById("div_total_price").innerHTML = parseFloat(price)
//			+ parseFloat(document.getElementById("div_insurance").innerHTML)
//			+ parseFloat(document.getElementById("div_road_tax").innerHTML);
}

function CheckNumeric(num) {
	if (isNaN(num) || num == '' || num == null) {
		num = 0;
	}
	return num;
}