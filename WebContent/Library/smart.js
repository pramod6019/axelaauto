// JavaScript Document
var opp_txt="";
var bool_txt="";
var line_txt="";
var count; 
var x="";   

function FormFocus() { //v1.0
    count = document.getElementById(count+"dr_searchcount").value;
    for(var i=1;i<=count;i++){
        var srcbox=document.getElementById(i+"_txt_value_1");
        if(document.getElementById(i+"_txt_value_1")){
            srcbox.focus();
            break;
        }
    }
}
function GetValues(rcount) {
    if(document.getElementById(rcount+"_txt_value_1")){
        x=document.getElementById(rcount+"_txt_value_1").value
    }
}

function SmartAddRow(smartarropt)
{ 
    count = document.getElementById("dr_searchcount").value;
    if(count=="" || count==null) count="0";
    count = parseInt(count);
    count++;
    document.getElementById("dr_searchcount").value = count;
    var table = document.getElementById("tblsmartsearch");
    var rowCount = table.rows.length;
    var newRow = document.getElementById("tblsmartsearch").insertRow(rowCount );
    ///add 4 cells (<td>) to the new row and set the innerHTML to contain text boxes
    var oCell = newRow.insertCell(0);
    oCell.innerHTML = "<select name=\""+count+"_dr_field\" id=\""+count+"_dr_field\" class=\"form-control\" onchange = SmartPopulateCriteria(this,"+count+");"+
    "SmartPopulateParam1(this,"+count+");BootstrapSelect();>"+smartarropt+"</select>";
    oCell = newRow.insertCell(1); 
    oCell.innerHTML = "<div id=dr"+count+">"+
    "<select name=\""+count+"_dr_param\" id=\""+count+"_dr_param\" class=\"form-control\" onchange = \"SmartPopulateParam1(this,"+count+");SmartPopulateParam2(this,"+count+");\" >"+
    "<option value=0-text>contains</option>"+
    "<option value=1-text>not contains</option>"+
    "<option value=2-text>is equal to</option>"+
    "<option value=3-text>is not equal to</option>"+
    "<option value=4-text>starts with</option>"+
    "<option value=5-text>does not start with</option>"+ 
    "<option value=6-text>ends with</option>"+
    "<option value=7-text>does not end with</option>"+
    "<option value=8-text>is blank</option>"+
    "<option value=9-text>is not blank</option>"+
    "</select>";
    oCell = newRow.insertCell(2);
    bool_txt="<input type='text' name="+count+"_txt_value_1 id="+count+"_txt_value_1 class=\"form-control\" value='"+ x +"' size=30/ onchange='GetValues("+count+");'>";
    oCell.innerHTML = "<div id=booltxt"+count+">"+bool_txt;
    oCell = newRow.insertCell(3);
    oCell.innerHTML = "<select class=\"form-control\" name="+count+"_dr_filter id="+count+"_dr_filter >		    "+"<option value=and>AND</option>"+
    "<option value=or>OR</option></select>";             
    oCell = newRow.insertCell(4);
    var str = "<div class=\"\"><center><div class=\"btn btn-group\"><i class=\"fa fa-minus-square\" style=\"font-size:30px\" id=\"sample_editable_1_new\" onclick=\'SmartRemoveRow(this);FormFocus(); \' onmouseover=\"this.title='Remove Criteria'\"></i> <i class=\"fa fa-plus-square\" style=\"font-size:30px\" id=\"sample_editable_1_new\" onclick=\'LoadRows();BootstrapSelect();\' onmouseover=\"this.title='Add Criteria'\"></i></div></center></div>";
    oCell.innerHTML=str;
    document.getElementById("dr_searchcount_var").value=count;
}

function SmartPopulateCriteria(type, count){
	 
    var split=type.value.split("-");
    var type=split[1];
    if(type=="text")
    {
        
        opp_txt = "<option value=0-text>contains</option>"
              	+ "<option value=1-text>not contains</option>"
        		+ "<option value=2-text>is equal to</option>"
			    + "<option value=3-text>is not equal to</option>"
			    + "<option value=4-text>starts with</option>"
			    + "<option value=5-text>does not start with</option>"
			    + "<option value=6-text>ends with</option>"
			    + "<option value=7-text>does not end with</option>"
			    + "<option value=8-text>is blank</option>"
			    + "<option value=9-text>is not blank</option>";
    }
    else if(type=="numeric")
    {
        opp_txt="<option value=0-numeric>is equal to</option>"+
			    "<option value=1-numeric>is not equal to</option>"+
			    "<option value=2-numeric>is less than</option>"+
			    "<option value=3-numeric>is less than or equal to</option>"+
			    "<option value=4-numeric>is greater than</option>"+
			    "<option value=5-numeric>is greater than or equal to</option>"+
			    "<option value=6-numeric>is between</option>"+
			    "<option value=7-numeric>is not between</option>";
    }
    else if(type=="date")
    {
		
        opp_txt="<option value=0-date>is equal to</option>"+
			    "<option value=1-date>is not equal to</option>"+
			    "<option value=2-date>is less than</option>"+
			    "<option value=3-date>is less than or equal to</option>"+
			    "<option value=4-date>is greater than</option>"+
			    "<option value=5-date>is greater than or equal to</option>"+
			    "<option value=6-date>is between</option>"+
			    "<option value=7-date>is not between</option>"+
			    "<option value=8-date>is blank</option>"+
			    "<option value=9-date>is not blank</option>";
    }
    else if(type=="boolean")
    {
        opp_txt="<option value=0-boolean>is equal to</option>";
    }
    document.getElementById("dr"+count+"").innerHTML = "<select name=\""+count+"_dr_param\" id=\""+count+"_dr_param\" class=\"form-control\" onchange = \"SmartPopulateParam1(this,"+count+");SmartPopulateParam2(this,"+count+");\">"+opp_txt+"</select>";
}
function SmartPopulateParam1(bool,count)
{ 
    var split=bool.value.split("-");
    var param=bool.value;
    var bool=split[1];
    document.getElementById("booltxt"+count+"").innerHTML =bool_txt;
    if(bool=="text")
    {
        bool_txt="<input name="+count+"_txt_value_1 id="+count+"_txt_value_1 type=text class=\"form-control\"  value='"+ x +"'size=30 ></input>";
        document.getElementById("booltxt"+count+"").innerHTML =bool_txt;
    }
    else if(bool=="numeric")
    {
        bool_txt="<input name="+count+"_txt_value_1 id="+count+"_txt_value_1 type=text class=\"form-control\" value='"+ x +"'size=30></input>";
        document.getElementById("booltxt"+count+"").innerHTML =bool_txt;
    }
    else if(bool=="boolean")
    {
        bool_txt="<option value=1>YES</option>"+
        "<option value=0>NO</option>";
        document.getElementById("booltxt"+count+"").innerHTML ="<select class=form-control  name="+count+"_dr_value_1 id="+count+"_dr_value_1>"+bool_txt+"</select>";
    }
    else  if(bool=="date")
    { 
        bool_txt="<input name="+count+"_txt_value_1 id="+count+"_txt_value_1 type=text size=16 class='form-control datetimepicker'  onmouseover=\"$('.datetimepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY HH:mm',switchOnClick : true, clearButton: true });\" maxlength=16 >"
        document.getElementById("booltxt"+count+"").innerHTML =bool_txt;
    }
    return bool_txt;
}

function SmartPopulateParam2(line,count)
{ 
    if(line.value=="6-numeric" || line.value=="7-numeric")
    {
        line_txt="<input type=text class=\"form-control\" name="+count+"_txt_value_2 id="+count+"_txt_value_2 size=30></input>";
        document.getElementById("booltxt"+count+"").innerHTML =bool_txt+"&nbsp"+line_txt;
    }

    else if(line.value=="0-numeric" || line.value=="1-numeric" || line.value=="2-numeric" || line.value=="3-numeric" || line.value=="4-numeric" || line.value=="5-numeric")
    {
        document.getElementById("booltxt"+count+"").innerHTML =bool_txt;
    }
    else if(line.value=="6-date" || line.value=="7-date")
    { 
        line_txt="<input name="+count+"_txt_value_2 id="+count+"_txt_value_2 type=text class='form-control datetimepicker'  onmouseover=\"$('.datetimepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY HH:mm',switchOnClick : true, clearButton: true });\" size=16 maxlength=16 >"
        document.getElementById("booltxt"+count+"").innerHTML =bool_txt+"&nbsp"+line_txt ;
    }
    else if( line.value=="0-date" || line.value=="1-date" || line.value=="2-date" || line.value=="3-date" || line.value=="4-date" || line.value=="5-date")
    { 
        document.getElementById("booltxt"+count+"").innerHTML =bool_txt;
    }
    else
    {
        line_txt="<input type='text' name="+count+"_txt_value_1 class=\"form-control\" size=30/>";
        document.getElementById("booltxt"+count+"").innerHTML =line_txt;
    }
}
//deletes the specified row from the table
function SmartRemoveRow(src)
{
	src.parentNode.parentNode.parentNode.parentNode.parentNode.remove();
}
		
