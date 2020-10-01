$(function() {
    $( "#txt_quote_date" ).datepicker({
        showButtonPanel: true,
        dateFormat: "dd/mm/yy"
    });

});

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

function GetExecutives(){
	var branch_id = document.getElementById("dr_branch").value;
	showHint("../sales/veh-quote-check.jsp?branch_emp=yes&branch_id="+branch_id,"dr_executive");
}

function GetConfigurationDetails(){
    var item_id = document.getElementById("dr_item_id").value;
	var vehstock_comm_no = document.getElementById("txt_stock_comm_no").value;
	var quote_date = document.getElementById("txt_quote_date").value;
    var branch_id = document.getElementById("dr_branch").value;
    var emp_quote_discountupdate = document.getElementById("emp_quote_discountupdate").value;
    showHint("../sales/veh-quote-check.jsp?get_config=yes&branch_id="+branch_id+"&item_id="+item_id+"&quote_date="+quote_date+"&emp_quote_discountupdate="+emp_quote_discountupdate+"&stock_comm_no="+stock_comm_no,"config_details"); 
   // showHint("../sales/veh-quote-check.jsp?branch_emp=yes&branch_id="+branch_id,"dr_executive");
    
}


function PopulateItem(model_id){
    var branch_id = document.getElementById("txt_branch_id").value;
    var status = document.getElementById("txt_status").value;
    showHint("../sales/veh-quote-check.jsp?list_quote_item=yes&branch_id="+branch_id+"&model_id="+model_id+'&status='+status,"div_item");
}


function CheckItemBasePrice(price, discount){
    var tax_rate = parseFloat(document.getElementById("txt_item_tax_rate").value);
    var disc = CheckNumeric(document.getElementById("txt_item_disc").value);
    var emp_quote_discountupdate = document.getElementById("emp_quote_discountupdate").value;
    var emp_quote_priceupdate = document.getElementById("emp_quote_priceupdate").value;
    if(emp_quote_discountupdate=='1'){
        if(eval(price)<eval(disc)){
            document.getElementById("txt_item_disc").value = parseInt(price);
        }
    }
		
    else if(emp_quote_discountupdate=='0'){
        if(eval(discount)<eval(disc)){
            document.getElementById("txt_item_disc").value = discount;
        }
    }
    disc = CheckNumeric(parseInt(document.getElementById("txt_item_disc").value));
    document.getElementById("div_main_item_total").innerHTML = ((parseFloat(price) - disc)*tax_rate/100+parseFloat(price) - disc).toFixed(2);
    document.getElementById("div_main_item_amount").value = ((parseFloat(price) - disc)*tax_rate/100+parseFloat(price) - disc).toFixed(2);
    //alert(((parseFloat(price) - disc)*tax_rate/100+parseFloat(price) - disc).toFixed(2));   
    CalculateTotal('1');
}


//price should'nt be less than the base price
function CheckBasePrice(group_id, group_item_count, group_item_serial, group_count, aftertax){
    var tax = '';
    if(aftertax=='1'){
        tax = 'at_'
    }else if(aftertax=='0'){
        tax = 'bt_'
    }
    var item_qty = parseFloat(document.getElementById("txt_"+tax+group_count+"_"+group_item_serial+"_qty").value);
    var tax_rate = parseFloat(document.getElementById("txt_"+tax+group_count+"_"+group_item_serial+"_tax_rate").value);
    var base_price = parseFloat(document.getElementById("txt_"+tax+group_count+"_"+group_item_serial+"_baseprice").value);
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
    document.getElementById("div_"+tax+group_count+"_"+group_item_serial+"_amount").value = CheckNumeric(((((base_price - disc)*tax_rate/100)+(base_price - disc)))*item_qty).toFixed(2);
    GroupValue(group_id, group_item_count, group_count, aftertax);
    CalculateTotal(aftertax);
}



function CalculateTotal(aftertax){

	var tax = '';
    var group_value = 0;
    //    var addoff_disc_total = 0;
    //    var addoff_disc_amt = 0;
    var group_item_count = 0;
    var total_disc = parseFloat(CheckNumeric(document.getElementById("txt_item_disc").value));
    //    var addoff_item_count = document.getElementById("txt_addoff_item_count").value;
    //    var addoff_item_check = "";
    var id = "", item = "", value = 0;
    if(aftertax=='1'){
        tax = 'at_'
    }else if(aftertax=='0'){
        tax = 'bt_'
    }
    var total = 0;
    var base_price = 0;
    var group_count = parseInt(document.getElementById("txt_bt_group_count").value);
    for(var j=1;j<=group_count;j++){
        group_value = 0;
        group_item_count = document.getElementById("txt_bt_"+j+"_count").value;
        for(var i=1;i<=group_item_count;i++){
            id = document.getElementById("chk_bt_"+j+"_"+i);
            item = id.checked;
            if(item==true){
                value = parseFloat(document.getElementById("div_bt_"+j+"_"+i+"_total").innerHTML);
                total_disc = parseFloat(total_disc) + parseFloat(CheckNumeric(document.getElementById("txt_bt_"+j+"_"+i+"_disc").value));
                group_value = parseFloat(group_value) + parseFloat(value);
            }
        }
        total = parseFloat(total) + group_value;
    }
    total = parseFloat(document.getElementById("div_main_item_amount").value) + total;
    //    if(addoff_item_count!=0 && addoff_item_count!=''){
    //        for(var i=1; i<=addoff_item_count; i++){
    //            addoff_item_check = document.getElementById("chk_addoff_"+i);
    //            if(addoff_item_check.checked==true){
    //                addoff_disc_amt = parseFloat(CheckNumeric(document.getElementById("txt_addoff_"+i+"_amt").value));
    //                addoff_disc_total = parseFloat(addoff_disc_total) + parseFloat(addoff_disc_amt);
    //                //alert("addoff_disc_amt=="+addoff_disc_amt+" addoff_disc_total=="+addoff_disc_total);
    //                if((parseFloat(addoff_disc_total) > parseFloat(total)) || (addoff_disc_amt == '0')){
    //                    addoff_disc_total = parseFloat(addoff_disc_total) - parseFloat(addoff_disc_amt);
    //                    document.getElementById("txt_addoff_"+i+"_amt").value = '0';
    //                }
    //                total_disc += parseFloat(CheckNumeric(document.getElementById("txt_addoff_"+i+"_amt").value));
    //            }
    //        }
    //    }
    //    if(parseFloat(addoff_disc_total) > parseFloat(total)){
    //        addoff_disc_total = total;
    //    }
    //    total = parseFloat(total) - parseFloat(addoff_disc_total);
    //alert("disc=="+total_disc);
    document.getElementById("txt_item_price").value = parseFloat(total).toFixed(2);
    document.getElementById("txt_bt_total_disc").value = parseFloat(total_disc).toFixed(2);
    document.getElementById("div_item_price").innerHTML = parseFloat(total).toFixed(2);
    setTimeout('EvaluateTax("disc")',200);
}

function CalculateAfterTaxTotal(disc){
    var at_total = 0, total = 0, grand_total = 0;
    var base_price = 0;
    var addoff_disc_total = 0;
    var addoff_disc_amt = 0;
    var group_value = 0;
    var group_item_count = 0;
    var total_disc = 0;
    var addoff_item_count = document.getElementById("txt_addoff_item_count").value;
    var addoff_item_check = "";
    var id = "", item = "", value = 0;
    if(disc=='disc'){
        total_disc = parseFloat(document.getElementById("txt_bt_total_disc").value);
    }
    //alert("disc=="+total_disc);
    var group_count = parseInt(document.getElementById("txt_at_group_count").value);

    for(var j=1;j<=group_count;j++){
        group_value = 0;
        group_item_count = document.getElementById("txt_at_"+j+"_count").value;
        for(var i=1;i<=group_item_count;i++){
            id = document.getElementById("chk_at_"+j+"_"+i);
            item = id.checked;
            if(item==true){
                value = parseFloat(document.getElementById("div_at_"+j+"_"+i+"_total").innerHTML);
                total_disc += parseFloat(CheckNumeric(document.getElementById("txt_at_"+j+"_"+i+"_disc").value));
                group_value = parseFloat(group_value) + parseFloat(value);
            }
        }
        at_total = (parseFloat(at_total) + parseFloat(group_value)).toFixed(2);
    }

    //    alert("total_disc=="+total_disc);
    var bt_total = parseFloat(document.getElementById("div_item_price").innerHTML).toFixed(2);
    total = parseFloat(at_total) + parseFloat(bt_total);

    if(addoff_item_count!=0 && addoff_item_count!=''){
        for(var i=1; i<=addoff_item_count; i++){
            addoff_item_check = document.getElementById("chk_addoff_"+i);
            if(addoff_item_check.checked==true){
                addoff_disc_amt = parseFloat(CheckNumeric(document.getElementById("txt_addoff_"+i+"_amt").value));
                addoff_disc_total = parseFloat(addoff_disc_total) + parseFloat(addoff_disc_amt);
                if((parseFloat(addoff_disc_total) > parseFloat(total)) || (addoff_disc_amt == '0')){
                    addoff_disc_total = parseFloat(addoff_disc_total) - parseFloat(addoff_disc_amt);
                    document.getElementById("txt_addoff_"+i+"_amt").value = '0';
                }
                total_disc += parseFloat(CheckNumeric(document.getElementById("txt_addoff_"+i+"_amt").value));
            }
        }
    }
    
    if(parseFloat(addoff_disc_total) > parseFloat(total)){
        addoff_disc_total = total;
    }

    //alert("addoff_disc_total=="+addoff_disc_total);
    grand_total = parseFloat(total) - parseFloat(addoff_disc_total)
    document.getElementById("txt_total_disc").value = parseFloat(total_disc).toFixed(2);
    document.getElementById("div_total_disc").innerHTML = Math.ceil(parseFloat(total_disc)).toFixed(2);
    document.getElementById("div_total_price").innerHTML = Math.ceil(grand_total).toFixed(2);
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
    for(var j=1;j<=groups_count;j++){
        group_value = 0;
        group_item_count = document.getElementById("txt_"+tax+j+"_count").value;
        for(var i=1;i<=group_item_count;i++){
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
    var at_group_count = parseFloat(document.getElementById("txt_at_group_count").value);
    for(var i=1; i<=at_group_count; i++){
        var group_item_count = parseFloat(document.getElementById("txt_at_"+i+"_count").value);
        for(var j=1; j<=group_item_count; j++){
            var formulae = document.getElementById("txt_at_"+i+"_"+j+"_tax_formulae").value;
            var tax_value = formulae.replace(/exprice/g,bt_total);
            if(tax_value!=''){
                document.getElementById("div_at_"+i+"_"+j+"_price").innerHTML = parseFloat(eval(tax_value)).toFixed(2);
                document.getElementById("div_at_"+i+"_"+j+"_netprice").value = parseFloat(eval(tax_value)).toFixed(2);
                document.getElementById("txt_at_"+i+"_"+j+"_baseprice").value = parseFloat(eval(tax_value)).toFixed(2);
                var disc = CheckNumeric(document.getElementById("txt_at_"+i+"_"+j+"_disc").value);
                //alert("tac=="+parseFloat(eval(tax_value)).toFixed(2)+" disc=="+parseInt(disc));
                var total = ((parseFloat(eval(tax_value)).toFixed(2))-parseInt(disc)).toFixed(2);
                //alert("total=="+total);
                document.getElementById("div_at_"+i+"_"+j+"_total").innerHTML = total;
                document.getElementById("div_at_"+i+"_"+j+"_amount").value = total;
            //alert("last2");
            }
        }
    }
			  
    if(discount=="disc"){
        setTimeout('CalculateAfterTaxTotal("disc")',800);
    }else{
        setTimeout('CalculateAfterTaxTotal("")',800);
    }
}


function CalculateDefault(group_id, group_item_count, count, group_count, aftertax){
    GroupValue(group_id, group_item_count, group_count, aftertax);
    var tax = '';
    if(aftertax=='1'){
        tax = 'at_'
    }else if(aftertax=='0'){
        tax = 'bt_'
    }

    var new_value =  parseFloat(document.getElementById("div_"+tax+group_count+"_"+count+"_total").innerHTML);
    var base_value = "";
    for(var i=1;i<=group_item_count;i++){
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


function CalculateMultiSelect(id, item_price, group_item_count, group_id, group_count, aftertax){
    GroupValue(group_id, group_item_count, group_count, aftertax);
    CalculateTotal(aftertax);
}


function GetTotal(value, target){
    var price = parseFloat(document.getElementById("txt_item_price").value);
    var extra = (eval(price)*eval(value)/100);
    if(target=='tax'){
        document.getElementById("div_road_tax").innerHTML = extra;
    }else if(target=='insurance'){
        document.getElementById("div_insurance").innerHTML = extra;
    }
    document.getElementById("div_toal_price").innerHTML = parseFloat(price) + parseFloat(document.getElementById("div_insurance").innerHTML) + parseFloat(document.getElementById("div_road_tax").innerHTML);
}

function CheckNumeric(num){
    if(isNaN(num) || num=='' || num==null){
        num=0;
    }
    return num;
}