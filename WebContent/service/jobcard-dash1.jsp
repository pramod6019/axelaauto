<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.JobCard_Dash" scope="request"/>
<%mybean.doPost(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">

<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
	<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
		<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />


	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script>
$(function(){
		 var guage = document.getElementById("txt_jc_fuel_guage").value;
		 $("#div_fuel_guage").slider({
			 range: "min",
			 value: guage,
			 min: 0,
			 max: 100,
			 slide: function(event, ui){
				 $("#div_jc_fuel_guage").html(ui.value);
				 $("#txt_jc_fuel_guage").val(ui.value).change();
				 }
			 });
			 $("#div_jc_fuel_guage").html($("#div_fuel_guage").slider("value"));
			 $("#txt_jc_fuel_guage").val($("#div_fuel_guage").slider("value"));
		 });

 $(function() {
    $( "#txt_jc_time_promised" ).datetimepicker({
		dateFormat: 'dd/mm/yy',
		stepMinute: 5
		});
  });
</script>
<script language="JavaScript" type="text/javascript">
function SecurityCheck(name,obj,hint)
{
	//alert("name=="+name);
	var value;
	if(name!="chk_jc_critical")
			{
				value = GetReplace(obj.value);
				//alert("value=="+GetReplace(obj.value));
			}
			else{
				if(obj.checked==true)
				{
					value = "1";
				}
				else
				{
					value = "0";
				}
			}
	var jc_id =  GetReplace(document.getElementById("jc_id").value);
	//alert(jc_id);
    var url = "../service/jobcard-dash-check.jsp?name=" + name + "&value="+ value +"&jc_id=" + jc_id ;
	//alert(url);
    showHint(url,  hint);
	//alert("name=="+name+" value=="+value);
	//if(name=='dr_jc_stage_id' && (value=='4' || value=='5')){
	//	document.getElementById("dr_bay_id").value = '0';
	//	document.getElementById("dr_bay_id").disabled = 'disabled';
		//}else{
		//	document.getElementById("dr_bay_id").disabled  = '';
		//	}
			if(name=='dr_jc_stage_id' && value=='6')
			 {
				 //alert("hiiii");
				 setTimeout('populatedeliverytime()',1000);
			 }
			 else
			 {
				document.getElementById("span_deliveredtime").innerHTML = "";
			 }

			if(name=='dr_jc_stage_id' && value=='5')
			 {
				 //alert("hii");
				  setTimeout('populatereadytime()',1000);
				// hideRow('starttime');

			}
			else
			 {
				document.getElementById("span_completedtime").innerHTML = "";
			 }
}

function populatedeliverytime()
{
	var deliveredtime = document.getElementById("deltime").value;
	document.getElementById("span_deliveredtime").innerHTML = deliveredtime;
	var completedtime = document.getElementById("comptime").value;
	document.getElementById("span_completedtime").innerHTML = completedtime;
}

function populatecompletedtime()
{
	var completedtime = document.getElementById("comptime").value;
	document.getElementById("span_completedtime").innerHTML = completedtime;
}

function PopulateModelItem(){

    var item_model_id = document.getElementById('dr_jc_model_id').value;

    if(item_model_id!="" && item_model_id!="0")
    {
        showHint('../service/report-check.jsp?model_id='+item_model_id,item_model_id,'modelitem');
    }

}


</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %> <%@include file="../Library/jobcard-dash.jsp" %><table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" valign="top"><font color="#ff0000" ><b><span id="history_span"><%=mybean.msg%></span></b></font></td>
  </tr>
  <tr>
    <td align="right" valign="top"><form name="form1"  id="form1" method="post">
        <%if(mybean.msg.equals("")){%>
        <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
          <tr>
            <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
                <tr align="center">
                  <th colspan="4">&nbsp;Job Card Details</th>
                </tr>
                <tr>
                  <td align="right" valign="top">Job Card No:</td>
                  <td align="left" valign="top"><a href="../service/jobcard_list.jsp?jc_id=<%=mybean.jc_id%>"><b><%=mybean.jc_no%></b></a>
                    <input name="jc_id" type="hidden" id="jc_id" value="<%=mybean.jc_id%>"></td>
                  <td align="right" valign="top">Branch:</td>
                  <td  align="left" valign="top"><a href="../portal/branch-summary.jsp?branch_id=<%=mybean.jc_branch_id%>"><%=mybean.branch_name%></a></td>
                </tr>
                <tr>
                  <td align="right" valign="top">Date:</td>
                  <td  align="left" valign="top"><input name="txt_jc_time_in" type="hidden" class="textbox"  id ="txt_jc_time_in" value = "<%=mybean.date%>" >
                    <%=mybean.date%></td>
                  <td align="right" valign="top">Promised Time<font color="#ff0000">*</font>:</td>
                  <td  align="left" valign="top"><input name="txt_jc_time_promised" type="text" class="textbox"  id ="txt_jc_time_promised" value = "<%=mybean.jc_promisetime%>" size="20" maxlength="16" onChange="SecurityCheck('txt_jc_time_promised',this,'hint_txt_jc_time_promised');" >
                    <div class="hint" id="hint_txt_jc_time_promised"></div></td>
                </tr>
                <tr>
                  <td align="right" valign="middle">Model:</td>
                <td valign="middle"><b><%=mybean.jc_model_name%></b></td>
                  <td align="right" valign="middle">Item:</td>
                  <td valign="middle"><b><%=mybean.jc_item_name%></b></td>
                </tr>
                <tr valign="center" id="contact_link">
                        <td align="right" valign="top">Customer:</td>
                        <td valign="top" align="left" nowrap><b><%=mybean.link_customer_name%></b></td>
                        <td align="right" valign="top">Contact:</td>
                        <td valign="top" align="left" nowrap><b><%=mybean.link_contact_name%></b></td></tr>
                          <tr valign="center">
                        <td align="right" valign="top">Chassis Number:</td>
                        <td colspan="3" valign="top" align="left" nowrap><b><%=mybean.veh_chassis_no%></b></td>
                          </tr>  
                          <tr valign="center">
                        <td align="right" valign="top">Engine No.:</td>
                        <td valign="top" align="left" nowrap><b><%=mybean.veh_engine_no%></b></td>
                        <td align="right" valign="top">Reg. No.:</td>
                        <td valign="top" align="left" nowrap><b><%=mybean.veh_reg_no%></b></td>
                          </tr>
                          <tr>
                                            <td align="right" valign="top">Kms<font color="#ff0000">*</font>:</td>
                            <td valign="top"><input name="txt_jc_kms" type="text" class="textbox"  id ="txt_jc_kms" value = "<%=mybean.jc_kms%>" size="20" maxlength="25" onkeyup="toInteger(this.id);" onchange="SecurityCheck('txt_jc_kms',this,'hint_txt_jc_kms');">
                            <div class="hint" id="hint_txt_jc_kms"></div></td>

 <td align="right" valign="top">Fuel Guage<font color="#ff0000">*</font>:</td>
 <td valign="top" nowrap="nowrap">
 <input type="hidden" id="txt_jc_fuel_guage" name="txt_jc_fuel_guage" value="<%=mybean.jc_fuel_guage%>" onchange="SecurityCheck('txt_jc_fuel_guage',this,'hint_txt_jc_fuel_guage');">
   <b><span name="div_jc_fuel_guage" id="div_jc_fuel_guage"></span>%</b>
                                            <div id="div_fuel_guage" style="width:200px"></div>0%&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;50%&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;100%
                                            <div class="hint" id="hint_txt_jc_fuel_guage"></div></td>

              </tr>
                <tr>
                  <td align="right" valign="middle">Type<font color="#ff0000">*</font>:</td>
                  <td valign="middle"><select name="dr_jc_type_id" id="dr_jc_type_id" class="selectbox" onChange="SecurityCheck('dr_jc_type_id',this,'hint_dr_jc_type_id');" >
                      <%=mybean.PopulateType()%>
                    </select>
                    <div class="hint" id="hint_dr_jc_type_id"></div></td>
                  <td align="right" valign="middle">Category<font color="#ff0000">*</font>:</td>
                  <td valign="middle"><select name="dr_jc_cat_id" id="dr_jc_cat_id" class="selectbox" onChange="SecurityCheck('dr_jc_cat_id',this,'hint_dr_jc_cat_id');" >
                      <%=mybean.PopulateCategory()%>
                    </select>
                    <div class="hint" id="hint_dr_jc_cat_id"></div></td>
                </tr>
                <tr>
                  <td align="right" valign="top">Title<font color="#ff0000">*</font>:</td>
                  <td align="left" valign="top"><input name="txt_jc_title" type="text" class="textbox"  id ="txt_jc_title" value = "<%=mybean.jc_title%>" size="52" maxlength="255"  onchange="SecurityCheck('txt_jc_title',this,'hint_txt_jc_title')"  />
                    <div class="hint" id="hint_txt_jc_title"></div></td>
                  <td align="right" valign="top">&nbsp;</td>
                  <td align="left" valign="top">&nbsp;</td>
                </tr>
                <tr>
                  <td align="right">Customer Voice<font color="#ff0000">*</font>:</td>
                  <td ><textarea name="txt_jc_cust_voice" id="txt_jc_cust_voice" cols="50" rows="5" class="textbox" onChange="SecurityCheck('txt_jc_cust_voice',this,'hint_txt_jc_cust_voice');" ><%=mybean.jc_cust_voice%></textarea>
                    <div class="hint" id="hint_txt_jc_cust_voice"></div></td>
                    <td align="right">Service Advice:</td>
                  <td ><textarea name="txt_jc_advice" id="txt_jc_advice" cols="50" rows="5" class="textbox" onChange="SecurityCheck('txt_jc_advice',this,'hint_txt_jc_advice');" ><%=mybean.jc_advice%></textarea>
                    <div class="hint" id="hint_txt_jc_advice"></div></td>
                </tr>
                <tr>
                  <td align="right">Service Instruction:</td>
                  <td ><textarea name="txt_jc_instr" id="txt_jc_instr" cols="50" rows="5" class="textbox" onChange="SecurityCheck('txt_jc_instr',this,'hint_txt_jc_instr');"><%=mybean.jc_instr%></textarea>
                    <div class="hint" id="hint_txt_jc_instr"></div></td>
                    <td align="right" valign="top">Terms:</td>
                  <td align="left" valign="top"><textarea name="txt_jc_terms" cols="50" rows="4" class="textbox" id="txt_jc_terms" onChange="SecurityCheck('txt_jc_terms',this,'hint_txt_jc_terms')" ><%=mybean.jc_terms%></textarea>
                    <div class="hint" id="hint_txt_jc_terms"></div></td>
                </tr>
                 <tr>
                <th colspan="4" align="center" valign="top">Customer Details</th>
              </tr>
              <tr>
                <td align="right" valign="top">Customer<font color="#ff0000">*</font>:</td>
                <td colspan="3" align="left" valign="top"><input name="txt_jc_customer_name" type="text" class="textbox"  id ="txt_jc_customer_name" value = "<%=mybean.jc_customer_name%>" size="32" maxlength="255"   onChange="SecurityCheck('txt_jc_customer_name',this,'hint_txt_jc_customer_name')"/>
                  <div class="hint" id="hint_txt_jc_customer_name"></div></td>
              </tr>
              <tr valign="center">
                <td align="right" valign="top">Contact<font color="#ff0000">*</font>:</td>
                <td colspan="3" valign="top"><table width="520" border="0" cellpadding="0">
                  <tr>
                    <td><span style="float:left">
                      <select name="dr_title" class="selectbox" id="dr_title" onchange="SecurityCheck('dr_title',this,'hint_dr_title')">
                        <%=mybean.PopulateTitle()%>
                      </select>
                      <br />
                      Title </span>
                      <div class="hint" id="hint_dr_title"></div></td>
                    <td><input name = "txt_contact_fname" type = "text" class="textbox" id= "txt_contact_fname" value = "<%=mybean.contact_fname%>" size="32" maxlength="255" onchange="SecurityCheck('txt_contact_fname',this,'hint_txt_contact_fname')" />
                      <br />
                      First Name
                      <div class="hint" id="hint_txt_contact_fname"></div></td>
                    <td><input name = "txt_contact_lname" type = "text" class="textbox" id= "txt_contact_lname" value = "<%=mybean.contact_lname%>" size="32" maxlength="255"  onchange="SecurityCheck('txt_contact_lname',this,'hint_txt_contact_lname')" />
                      <br />
                      Last Name
                      <div class="hint" id="hint_txt_contact_lname"></div></td>
                  </tr>
                </table></td>
              </tr>
              <tr valign="center">
                <td align="right" valign="top"> Mobile 1<font color="#ff0000">*</font>:</td>
                <td  align="left" valign="top"><input name ="txt_contact_mobile1" type="text" class="textbox"  id ="txt_contact_mobile1" value = "<%=mybean.contact_mobile1%>" size="32" maxlength="13" style="width:250px"  onchange="SecurityCheck('txt_contact_mobile1',this,'hint_txt_contact_mobile1')" />
                  <br />
                  (91-9999999999)
                  <div class="hint" id="hint_txt_contact_mobile1"></div></td>
                <td align="right" valign="top"> Phone 1<font color="#ff0000">*</font>:</td>
                <td  align="left" valign="top"><input name = "txt_contact_phone1" type = "text" class="textbox" id= "txt_contact_phone1" value = "<%=mybean.contact_phone1%>" size="35" maxlength="14" onchange="SecurityCheck('txt_contact_phone1',this,'hint_txt_contact_phone1')" />
                  <br />
                  (91-80-33333333)
                  <div class="hint" id="hint_txt_contact_phone1"></div></td>
              </tr>
              <tr valign="center">
                <td align="right" valign="top">Email 1<font color="#ff0000">*</font>:</td>
                <td align="left" valign="top" colspan="3"><input name = "txt_contact_email1" type = "text" class="textbox" id= "txt_contact_email1" value = "<%=mybean.contact_email1%>" size="35" maxlength="100" style="width:250px"  onchange="SecurityCheck('txt_contact_email1',this,'hint_txt_contact_email1')" />
                  <div class="hint" id="hint_txt_contact_email1"></div></td>
              </tr>
              <tr>
                <td rowspan="2" align="right" valign="top">Address<font color="#ff0000">*</font>:</td>
                <td  align="left" valign="top" rowspan="2"><textarea name="txt_contact_address" cols="40" rows="4" class="textbox" id="txt_contact_address"  onchange="SecurityCheck('txt_contact_address',this,'hint_txt_contact_address')" onkeyup="charcount('txt_contact_address', 'span_txt_contact_address','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"   style="width:250px"><%=mybean.contact_address%></textarea>
                  <br />
                  <span id="span_txt_contact_address"> (255 Characters)</span>
                  <div class="hint" id="hint_txt_contact_address"></div></td>
                <td align="right" valign="top">City<font color="#ff0000">*</font>:</td>
                <td align="left" valign="top"><select name="dr_city_id" id="dr_city_id" class="selectbox"  onchange="SecurityCheck('dr_city_id',this,'hint_dr_city_id')" >
                  <%=mybean.PopulateCity()%>
                </select>
                  <div class="hint" id="hint_dr_city_id"></div></td>
              </tr>
              <tr valign="center">
                <td align="right" valign="top">Pin/Zip<font color="#ff0000">*</font>:</td>
                <td align="left" valign="top"><input name ="txt_contact_pin" type="text" class="textbox" id="txt_contact_pin"    onkeyup="toInteger('txt_contact_pin','Pin')" onchange="SecurityCheck('txt_contact_pin',this,'hint_txt_contact_pin')" value="<%=mybean.contact_pin%>" size="10" maxlength="6"/>
                  <div class="hint" id="hint_txt_contact_pin"></div>
                  <input name="jc_id" type="hidden" id="jc_id" value="<%=mybean.jc_id%>" /></td>
              </tr>
                <tr>
                        <th colspan="4" align="center" valign="top">Status</th>
              </tr>
                 <tr>
                        <td align="right" valign="top">Stage<font color="#ff0000">*</font>:</td>
                        <td align="left" valign="top"><select name="dr_jc_stage_id" id="dr_jc_stage_id" class="selectbox"  onchange="SecurityCheck('dr_jc_stage_id',this,'hint_dr_jc_stage_id')" >
                            <%=mybean.PopulateStage()%>
                          </select>
                            <div class="hint" id="hint_dr_jc_stage_id"></div></td>
                        <td align="right" valign="top">Delivered Time:</td>
                        <td align="left" valign="top"><span id="span_deliveredtime"><%=mybean.jc_timeout%></span></td>
                </tr>
                 <tr>
                   <td align="right" valign="top">Critical:</td>
                   <td align="left" valign="top"><input id="chk_jc_critical" type="checkbox" name="chk_jc_critical" <%=mybean.PopulateCheck(mybean.jc_critical)%> onclick="SecurityCheck('chk_jc_critical',this,'hint_chk_jc_critical');" />
                     <div class="hint" id="hint_chk_jc_critical"></div></td>
                        <td align="right" valign="top">Completed Time:</td>
                        <td align="left" valign="top"><span id="span_completedtime"><%=mybean.jc_completedtime%></span></td>
                </tr>
                <tr>
                  <td align="right" valign="top">Executive<font color="#ff0000">*</font>:</td>
                  <td align="left" valign="top"><select name="dr_jc_emp_id" id="dr_jc_emp_id" class="selectbox" onchange="SecurityCheck('dr_jc_emp_id',this,'hint_dr_jc_emp_id');" >
                      <%=mybean.PopulateExecutive()%>
                    </select>
                    <div class="hint" id="hint_dr_jc_emp_id"></div></td>
                    <td align="right" valign="top">Inventory Location<font color="#ff0000">*</font>:</td>
                       <td>
                      <select name="dr_location"  id="dr_location" class="selectbox" onchange="SecurityCheck('dr_location',this,'hint_dr_location');">
                        <%=mybean.PopulateInventoryLocation()%>
                      </select>

                      <div class="hint" id="hint_dr_location"></div></td>
                         <!--<td align="right" valign="top">Bay:</td>
                        <td align="left" valign="top"><select name="dr_bay_id" id="dr_bay_id" class="textbox"   onchange="SecurityCheck('dr_bay_id',this,'hint_dr_bay_id')" >
                          <%//=mybean.PopulateBay()%>
                        </select>
                   <div class="hint" id="hint_dr_bay_id"></div></td>-->
                </tr>
                <tr>
                  <td align="right"> Ro. No.<font color="#ff0000">*</font>:</td>
                  <td><input name="txt_jc_ro_no2" type="text" class="textbox"  id ="txt_jc_ro_no2" value = "<%=mybean.jc_ro_no%>" size="20" maxlength="20" onchange="SecurityCheck('txt_jc_ro_no',this,'hint_txt_jc_ro_no');" />
                    <div class="hint" id="hint_txt_jc_ro_no"></div></td>
                  <td align="right" >Priority<font color="#ff0000">*</font>:</td>
                  <td align="left" valign="top"><select name="dr_jc_priorityjc_id" class="selectbox" id="dr_jc_priorityjc_id" onchange="SecurityCheck('dr_jc_priorityjc_id',this,'hint_dr_jc_priorityjc_id');">
                    <%=mybean.PopulatePriority()%>
                  </select>
                    <br />
                    <div class="hint" id="hint_dr_jc_priorityjc_id"></div></td>
                </tr>
                <tr align="center">
                  <td align="right" valign="top">Notes:</td>
                  <td colspan="3" align="left" valign="top"><textarea name="txt_jc_notes" cols="70" rows="4" class="textbox" id="txt_jc_notes" onChange="SecurityCheck('txt_jc_notes',this,'hint_txt_jc_notes')" ><%=mybean.jc_notes%></textarea>
                    <div class="hint" id="hint_txt_jc_notes"></div></td>
                </tr>

                <tr>
                  <td align="right">Entry By:</td>
                  <td colspan="3"><%=mybean.entry_by%>
                    <input type="hidden" name="entry_by" value="<%=mybean.entry_by%>"></td>
                </tr>
                <tr>
                  <td align="right">Entry Date:</td>
                  <td colspan="3"><%=mybean.entry_date%>
                    <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>"></td>
                </tr>
                <% if (mybean.modified_by != null && !mybean.modified_by.equals("")) {%>
                <tr>
                  <td align="right">Modified By:</td>
                  <td colspan="3"><%=mybean.modified_by%>
                    <input type="hidden" name="modified_by" value="<%=mybean.modified_by%>"></td>
                </tr>
                <tr>
                  <td align="right">Modified Date:</td>
                  <td colspan="3"><%=mybean.modified_date%>
                    <input type="hidden" name="modified_date" value="<%=mybean.modified_date%>"></td>
                </tr>
                <%}%>
              </table></td>
          </tr>
        </table>
        <%}%>
      </form></td>
  </tr>
</table>
<!-- #EndLibraryItem --> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
