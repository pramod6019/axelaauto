<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.insurance.Insurance_Veh_Add" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>
    <HEAD>
    <title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
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
	// JavaScript Document
$(function(){
    // Dialog
    $('#dialog-modal').dialog({
        autoOpen: false,
        width: 900,
        height: 500,
        zIndex: 200,
        modal: true,
        title: "Select Sales Order"
    });
    $('#dialog_link').click(function(){

        $.ajax({
            //url: "home.jsp",
            success: function(data){
                $('#dialog-modal').html('<iframe src="../customer/customer-contact-list.jsp?group=select_veh_contact" width="100%" height="100%" frameborder=0></iframe>');
            }
        });
		$('#dialog-modal').dialog('open');
        return true;
    });
});
 
$(function(){
    // Dialog
    $('#dialog-modal').dialog({
        autoOpen: false,
        width: 900,
        height: 500,
        zIndex: 200,
        modal: true,
        title: "Select Sales Order"
    });
    $('#veh_so_link').click(function(){

        $.ajax({
            //url: "home.jsp",
            success: function(data){
                $('#dialog-modal').html('<iframe src="../sales/veh-salesorder-list.jsp?group=select_veh_so" width="100%" height="100%" frameborder=0></iframe>');
            }  
        }); 
		$('#dialog-modal').dialog('open');
        return true;
    });
});
	     
   $(function() { 
    $( "#txt_veh_modelyear" ).datepicker({
		dateFormat: 'yy',
		stepMinute: 5    
		});	
	 	$( "#txt_veh_sale_date" ).datepicker({
		dateFormat: 'dd/mm/yy',
		stepMinute: 5
		});
		
	/* 	$( "#txt_veh_warranty_expirydate" ).datepicker({
		dateFormat: 'dd/mm/yy',
		stepMinute: 5
		}); */
		
		/* $( "#txt_veh_service_duedate" ).datepicker({
		dateFormat: 'dd/mm/yy',
		}); */
	/* 	
		txt_veh_lastservice
		$( "#txt_veh_lastservice" ).datepicker({
			dateFormat: 'dd/mm/yy',
		}); */
		
		$( "#txt_veh_renewal_date" ).datepicker({
		dateFormat: 'dd/mm/yy',
		});
	});
</script>
    <script language="JavaScript" type="text/javascript">

function FormFocus() { //v1.0
  //document.form1.txt_stock_name.focus();
}
function PopulateModel(branch_id){
	
	showHint("vehicle-dash-check.jsp?veh_branch_id="+branch_id+"&model=yes", "Hintmodel");
	//alert("branch_id-----"+branch_id);
}
 function PopulateItem(model_id){
				showHint("vehicle-dash-check.jsp?item_model_id="+model_id+"&list_model_item=yes", "model-item");
				}
				
			function ShowNameHint()
				{
					var contact_fname = document.getElementById("txt_contact_fname").value;
					var contact_lname = document.getElementById("txt_contact_lname").value;
					showHint("../service/report-check.jsp?contact_fname="+contact_fname+"&contact_lname="+contact_lname+"&search=yes", "search-contact");
					}    
					
					//For selecting existing contact
function SelectContact(contact_id, contact_name, customer_id, customer_name){
	var contact_link = '<a href="../customer/customer-contact-list.jsp?contact_id='+contact_id+'"> '+contact_name+ '</a>';
    var customer_link = '<a href="../customer/customer-list.jsp?customer_id='+customer_id+'">' +customer_name+ '</a>';
	var status = document.getElementById("txt_status").value;
	var selected_customer = document.getElementById("span_acct_id").value;
        var quote_customer = document.getElementById("acct_id").value;
//        alert("selected_customer=="+selected_customer);
	if(status!='Update' && selected_customer==0){
		document.getElementById("customer_name").style.display = 'none';
    document.getElementById("contact_name").style.display = 'none';
    document.getElementById("contact_mobile").style.display = 'none';
//    document.getElementById("contact_phone").style.display = 'none';
    document.getElementById("contact_email").style.display = 'none';
	document.getElementById("contact_address").style.display = 'none';
    document.getElementById("contact_city").style.display = 'none';
    document.getElementById("contact_state").style.display = 'none';
	document.getElementById("selected_contact").style.display = '';
	}
    document.getElementById("span_cont_id").value = contact_id;
    document.getElementById("span_acct_id").value = customer_id;
	if(status=='Add' && quote_customer==0){
	document.getElementById("selected_contact_id").innerHTML = contact_link;
    document.getElementById("selected_customer_id").innerHTML = customer_link;
	}else if(status=='Update' || selected_customer!=0){
    document.getElementById("span_veh_contact_id").innerHTML = contact_link;
    document.getElementById("span_veh_customer_id").innerHTML = customer_link;
	}
	if(so_id=='0'){
	document.getElementById("search-contact").innerHTML = '';
	}
	$('#dialog-modal').dialog('close'); 
}

function SelectVehSO(so_id, so_no)
{
	document.getElementById("span_so_id").value = so_id;
	document.getElementById("span_veh_so_id").innerHTML = "<a href=../sales/veh-salesorder-list.jsp?so_id="+so_id+">" +so_no+ "</a>";
	$('#dialog-modal').dialog('close'); 
}

function RemoveVehSO(){
	document.getElementById("span_so_id").value = "0";
	document.getElementById("span_veh_so_id").innerHTML = "";
	}
	</script>
    <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
    <body  onLoad="FormFocus()" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
    <%@include file="../portal/header.jsp" %>
    <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
      <TR>
        <TD align="left" vAlign="bottom"><a href="../portal/home.jsp">Home</a> &gt; <a href="../service/vehicle.jsp">Vehicles</a> &gt; <a href="vehicle-list.jsp?all=yes">List Vehicles</a> &gt; <a href="insurance-veh-add.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Insurance Enquiry</a>:</TD>
      </TR>
      <TR>
        <TD align="center" vAlign="top" ><font color="#ff0000" ><b><%=mybean.msg%></b></font> <br/></TD>
      </TR>
      <tr>
        <td valign="top"><form name="form1"  method="post">
            <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder" >
            <tr>
                <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"  class="listtable">
                    <tr align="center">
                    <th colspan="4"> <%=mybean.status%>&nbsp;Insurance Enquiry</th>
                  </tr>
                    <tr>
                    <td><input type="hidden" id="txt_prev_contact_id" name="txt_prev_contact_id" value="<%=mybean.prev_contact_id%>"></td>
                    <td align=left colspan="3"><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br />
                      </font></td>
                  </tr>
                    <%if(mybean.status.equals("Add") && mybean.contact_id.equals("0")){%>
                    <tr valign="middle" id="customer_name">
                    <td align="right" valign="top"> Customer:</td>
                    <td valign="top" align="left"><input name = "txt_customer_name" type = "text" class="textbox" id= "txt_customer_name" value = "<%=mybean.customer_name%>" size="50" maxlength="255" /></td>
                    <td align="right" valign="middle">Branch<font color="#ff0000">*</font>:</td>
                    <td valign="middle"><select name="dr_branch" class="selectbox" id="dr_branch" onchange="PopulateModel(this.value)";>
                        <%=mybean.PopulateBranch(mybean.customer_branch_id,"", "1,3", "", request)%>
                      </select>
                        </td>
                  </tr>
                    <tr valign="middle" id="contact_name">
                    <td align="right" valign="top">Contact
                        Name<font color="#ff0000">*</font>:</td>
                    <td colspan="3" valign="top"><table border="0" cellpadding="0">
                        <tr>
                        <td align="left"><span style="float:left">
                          <select name="dr_title" class="selectbox" id="dr_title" >
                            <%=mybean.PopulateTitle(mybean.comp_id)%>
                          </select>
                          <br />
                          Title </span></td>
                        <td ><input name = "txt_contact_fname" type = "text" class="textbox" id= "txt_contact_fname" value = "<%=mybean.contact_fname%>" size="30" maxlength="255" onkeyup="ShowNameHint()" style="width:250px"/>
                            <br />
                            First Name</td>
                        <td ><input name = "txt_contact_lname" type = "text" class="textbox" id= "txt_contact_lname" value = "<%=mybean.contact_lname%>" size="30" maxlength="255" onkeyup="ShowNameHint()" style="width:250px"/>
                            <br />
                            Last Name</td>
                      </tr>
                      </table></td>
                  </tr>
                    <tr id="contact_mobile">
                    <td align="right" valign="top">Mobile 1<font color="#ff0000">*</font>:</td>
                    <td valign="top" align="left"><input name="txt_contact_mobile1" type="text" class="textbox"  id ="txt_contact_mobile1" value = "<%=mybean.contact_mobile1 %>" size="30" maxlength="13"  onKeyUp="showHint('report-check.jsp?contact_mobile1='+GetReplace(this.value)+'&search=yes','search-contact');toPhone('txt_contact_mobile1','Mobile1');" style="width:250px"/>
                        <br/>
                        (91-9999999999) </td>
                    <td align="right" valign="top">Mobile 2:</td>
                    <td valign="top" align="left"><input name="txt_contact_mobile2" type="text" class="textbox"  id ="txt_contact_mobile2" value = "<%=mybean.contact_mobile2 %>" size="30" maxlength="13"  onKeyUp="showHint('report-check.jsp?contact_mobile2='+GetReplace(this.value)+'&search=yes','search-contact');toPhone('txt_contact_mobile2','Mobile2');"  style="width:250px"/>
                        <br/>
                        (91-9999999999) </td>
                  </tr>
                    <tr valign="center" id="contact_email">
                    <td align="right" valign="top"> Email 1:</td>
                    <td valign="top" align="left"><input name = "txt_contact_email1" type = "text" class="textbox" id= "txt_contact_email1" value = "<%=mybean.contact_email1%>" size="35" maxlength="255"  onKeyUp="showHint('report-check.jsp?contact_email1='+GetReplace(this.value)+'&search=yes','search-contact');" style="width:250px"/></td>
                    <td align="right" valign="top"> Email 2:</td>
                    <td valign="top" align="left"><input name = "txt_contact_email2" type = "text" class="textbox" id= "txt_contact_email2" value = "<%=mybean.contact_email2%>" size="35" maxlength="255"  onKeyUp="showHint('report-check.jsp?contact_email2='+GetReplace(this.value)+'&search=yes','search-contact');" style="width:250px"/></td>
                  </tr>
                    <tr id="contact_address">
                    <td align="right" nowrap>Address<font color="#ff0000">*</font>:</td>
                    <td align="left"><textarea name="txt_contact_address" rows="4" class="textbox" id="txt_contact_address" onKeyUp="charcount('txt_contact_address', 'span_txt_contact_address','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')" style="width:250px"><%=mybean.contact_address%></textarea>
                        <br />
                        <span id="span_txt_contact_address"> (255 Characters)</span></td>
                    <td colspan="2" align="left" valign="top"><span id="search-contact"></span></td>
                  </tr>
                    <tr valign="center" id="contact_city">
                    <td align="right" valign="top">City<font color="#ff0000">*</font>:</td>
                    <td align="left"><span id="contact_city_id"><%=mybean.PopulateCity(mybean.state_id,mybean.contact_city_id,"dr_contact_city_id", mybean.comp_id)%></span>
                        </td>
                    <td align="right" valign="top">Pin/Zip<font color="#ff0000">*</font>:</td>
                    <td align="left"><input name ="txt_contact_pin" type="text" class="textbox" id="txt_contact_pin" onKeyUp="toInteger('txt_contact_pin','Pin')" value="<%=mybean.contact_pin%>" size="10" maxlength="6"/></td>
                  </tr>
                    <tr valign="center" id="contact_state">
                    <td align="right" valign="top">State<font color="#ff0000">*</font>:<b></b></td>
                    <td align="left"><span id="contact_state_id"><%=mybean.PopulateState(mybean.state_id,"contact_city_id","dr_contact_state_id", mybean.comp_id)%></span>
                        </td>
                    <td align="right" valign="top">&nbsp;</td>
                    <td align="left">&nbsp;</td>
                  </tr>
                    <tr id=selected_contact style="display:none">
                    <td align="right" valign="top">Customer:</td>
                    <td align="left" nowrap><b><span id="selected_customer_id" name="selected_customer_id"></span></b>&nbsp; </td>
                    <td valign="top" align="right">Contact: </td>
                    <td><b><span id="selected_contact_id" name="selected_contact_id"></span></b>&nbsp; <a href="#" id="dialog_link" >(Select Contact)</a>
                        <div id="dialog-modal"></div></td>
                  </tr>
                    <%}else{%>
                    <tr valign="center" id="contact_link">
                    <td align="right" valign="top">Customer:</td>
                    <td valign="top" align="left" nowrap><b><span id="span_veh_customer_id" name="span_veh_customer_id"><%=mybean.link_customer_name%></span></b>&nbsp; </td>
                    <td valign="top" align="right">Contact: </td>
                    <td><b><span id="span_veh_contact_id" name="span_veh_contact_id"><%=mybean.link_contact_name%></span></b>&nbsp; <a href="#" id="dialog_link" >(Select Contact)</a>
                        <div id="dialog-modal"></div></td>
                  </tr>
                    <%}%>
                    <%if(mybean.autosales==1){%>
                    <%}%>
                    <tr valign="middle">
                    <td align="right" valign="middle" nowrap="nowrap"> Model:<font color="#ff0000">*</font></td>
                    <td align="left" colspan="3">
                   
                    <!-- <select name="dr_item_model_id" class="selectbox" onchange="PopulateItem(this.value);"> -->
                    <span id="Hintmodel">
                        <%=mybean.PopulateModel(mybean.comp_id, mybean.customer_branch_id)%>
                         </span>
                        <div class="admin-master"><a href="../inventory/item-model.jsp?all=yes" title="Manage Model"></a></div>
                        <input name="span_acct_id" type="hidden" id ="span_acct_id" value ="<%=mybean.customer_id%>" />
                        <input name="acct_id" type="hidden" id ="acct_id" value ="<%=mybean.veh_customer_id%>" />
                        <input name="span_cont_id" type="hidden" id ="span_cont_id" value ="<%=mybean.contact_id%>" />
                        <input name="cont_id" type="hidden" id ="cont_id" value ="<%=mybean.veh_contact_id%>" />
                        <input type="hidden" name="txt_status" id="txt_status" value="<%=mybean.status%>" /></td>
                  </tr>
                    <tr valign="middle">
                    <td align="right" valign="middle">Variant<font color="#ff0000">*</font></td>
                    <td align="left" colspan="3"><span id="model-item"><%=mybean.PopulateItem(mybean.item_model_id, mybean.comp_id)%></span>
                        <div class="admin-master"><a href="../inventory/inventory-variant-list.jsp?all=yes" title="Manage Variant"></a></div></td>
                  </tr>
                    <tr valign="middle">
                    <td align="right" valign="middle">Model Year<font color="#ff0000">*</font>:</td>
                    <td colspan="3"><input name ="txt_veh_modelyear" id ="txt_veh_modelyear" type="text" class="textbox" value="<%=mybean.veh_modelyear%>" size="10" maxlength="4"/></td>
                  </tr>
                    <tr valign="middle">
                    <td align="right" valign="middle">Chassis Number<font color="#ff0000">*</font>:</td>
                    <td colspan="3"><input name ="txt_veh_chassis_no" id ="txt_veh_chassis_no" type="text" class="textbox" value="<%=mybean.veh_chassis_no%>" size="20" maxlength="17"/></td>
                  </tr>
                    <tr valign="middle">
                    <td align="right" valign="middle">Engine Number<font color="#ff0000">*</font>:</td>
                    <td colspan="3"><input name ="txt_veh_engine_no" id ="txt_veh_engine_no" type="text" class="textbox" value="<%=mybean.veh_engine_no%>" size="20" maxlength="14"/></td>
                  </tr>
                    <tr valign="middle">  
                    <td align="right" valign="middle">Reg. Number<font color="#ff0000">*</font>:</td>
                    <td colspan="3"><input name ="txt_veh_reg_no" id ="txt_veh_reg_no" type="text" class="textbox" value="<%=mybean.veh_reg_no%>" size="20" maxlength="20"/></td>
                  </tr>
                    <tr valign="middle">
                    <td align="right" valign="middle">Sale Date<b><font color="#ff0000">*</font></b>:</td>
                    <td colspan="3"><input name ="txt_veh_sale_date" type="text" class="textbox" id="txt_veh_sale_date" value="<%=mybean.vehsaledate%>" size="12" maxlength="10"/></td>
                     
                   <tr valign="middle">
                    <td align="right" valign="middle">Veh. Source<b><font color="#ff0000">*</font></b>:</td>
                    <td colspan="3"><select name ="dr_veh_vehsource_id" type="text" class="selectbox" id="dr_veh_vehsource_id"><%=mybean.PopulateVehSource(mybean.comp_id)%></select></td>
                  </tr>
                    <tr valign="middle">
                    <td align="right" valign="middle">Kms: </td>
                    <td colspan="3"><%if(mybean.add.equals("yes")){%>
                        <input name ="txt_veh_kms" id ="txt_veh_kms" type="text" class="textbox" value="<%=mybean.veh_kms%>" size="20" maxlength="10" onkeyup="toInteger(this.id);"/>
                        <%}
                      if(mybean.update.equals("yes")){%>
                        <input name ="txt_veh_kms" id ="txt_veh_kms" type='hidden' value="<%=mybean.veh_kms%>"/>
                        <%=mybean.veh_kms%>
                        <%}%></td>
                  </tr>
                  <tr>
                        <td align="right">Insurance Executive<font color="#ff0000">*</font>:</td>
                        <td align="left" colspan="3"><select id="dr_veh_insuremp_id" name="dr_veh_insuremp_id" class="selectbox" >
                            <%=mybean.PopulateInsurExecutive(mybean.comp_id)%>
                          </select></td>
                      </tr> 
                                     <tr valign="middle">
                    <td align="right" valign="middle">Insurance Source:</td>
                    <td colspan="3">
                     <select name="dr_veh_insursource_id" class="selectbox" id="dr_veh_insursource_id">
                        <%=mybean.PopulateInsuranceSource(mybean.comp_id)%>
                      </select>
                    </td>
                  </tr>                        
                    <tr valign="middle">
                    <td align="right" valign="middle">Insurance Renewal Date:</td>
                    <td colspan="3"><input name="txt_veh_renewal_date" id="txt_veh_renewal_date" type="text" class="textbox" value="<%=mybean.renewal_date%>"  size="12" maxlength="10"/></td>
                  </tr>
                    <tr valign="middle">
                    <td align="right" valign="middle">Notes:</td>
                    <td colspan="3"><textarea name="txt_veh_notes" cols="70" rows="5" class="textbox" id="txt_veh_notes"><%=mybean.veh_notes%></textarea></td>
                  </tr>
                   <%--  <% if (mybean.status.equals("Update") &&!(mybean.veh_entry_by == null) && !(mybean.veh_entry_by.equals(""))) { %>
                    <tr valign="middle">
                    <td align="right" valign="middle">Entry By:</td>
                    <td colspan="3"><%=mybean.unescapehtml(mybean.veh_entry_by)%></td>
                  </tr>
                    <%}%>
                    <% if (mybean.status.equals("Update") &&!(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>
                    <tr valign="middle">
                    <td align="right" valign="middle">Entry Date:</td>
                    <td colspan="3"><%=mybean.entry_date%></td>
                  </tr>
                    <%}%>
                    <% if (mybean.status.equals("Update") &&!(mybean.veh_modified_by == null) && !(mybean.veh_modified_by.equals(""))) { %>
                    <tr valign="middle">
                    <td align="right" valign="middle">Modified By:</td>
                    <td colspan="3"><%=mybean.unescapehtml(mybean.veh_modified_by)%></td>
                  </tr>
                    <%}%>
                    <% if (mybean.status.equals("Update") &&!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
                    <tr valign="middle">
                    <td align="right" valign="middle">Modified Date:</td>
                    <td colspan="3"><%=mybean.modified_date%></td>
                  </tr>
                    <%}%> --%>
                    <tr align="center">
                    <td colspan="4" valign="middle"><%if(mybean.status.equals("Add")){%>
                        <input name="addbutton" type="submit" class="button" id="addbutton" value="Add Insurance Enquiry" onclick="return SubmitFormOnce(document.form1,this);"/>
                        <input type="hidden" id="add_button" name="add_button" value="yes"/>
                        <%}%><input type="hidden" name="veh_entry_by" value="<%=mybean.veh_entry_by%>" />
                        <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>" />
                        <input type="hidden" name="veh_modified_by" value="<%=mybean.veh_modified_by%>" />
                        <input type="hidden" name="modified_date" value="<%=mybean.modified_date%>" />
                    </td>
                
                  </tr>  
                  </table></td>
              </tr>
          </table>
          </form></td>
      </tr>
    </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>