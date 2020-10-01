<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.SalesOrder_Dash" scope="request"/>
<%mybean.doPost(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<HEAD>
	<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
	<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">  
	<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
    <link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
	
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript" src="../Library/dynacheck-post.js"></script>
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
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>

	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
	<script language="javascript" type="text/javascript">
        $(function() {
		 $( "#txt_so_payment_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
		 $( "#txt_so_promise_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
	$( "#txt_so_delivered_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
	 $( "#txt_so_cancel_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
  });
  </script>
	<script language="JavaScript" type="text/javascript">
         function PopulateCheck(name,obj,hint)
		{
			 var value;
		  var so_id =  GetReplacePost(document.form1.so_id.value);
            var url = "../sales/salesorder-dash-check.jsp?" ;
			var param="";
		   var str = "123";

			 value = GetReplacePost(obj.value);
				var stage_id;
				if(value==0)
				{
					stage_id=1;
			    } else{
					stage_id=2;
				}
				param="name="+ name + "&value="+ value +"&so_id="+ so_id +"&stage_id="+stage_id;
				 showHintPost(url+param, str, param, hint);
			     setTimeout('RefreshHistory()', 1000);
		}

		function SecurityCheck(name,obj,hint)
          {
		    var value;
		   	var so_id =  GetReplacePost(document.form1.so_id.value);
			var item_id = document.getElementById("txt_item_id").value;
            var url = "../sales/salesorder-dash-check.jsp?" ;
			var param="";
		    var str = "123";
			if(name!="chk_so_open" && name!="chk_so_critical" && name!="chk_so_active")
			{
				value = GetReplacePost(obj.value);
				/*if(value == "" && name == "txt_so_booking_amount")
				{
					alert("hi 2"+value);
					document.getElementById("txt_so_booking_amount").value = "0";
				}*/
			}
			else{
				var date = new Date();
			var day = date.getDate();
			var year = date.getFullYear();
			var month= date.getMonth()+1;
				if(obj.checked==true)
				{
					value = "1";
				}
				else
				{
					value = "0";
				}
				if(name=="chk_so_active"){
					if(value=='0'){
						document.getElementById("txt_so_cancel_date").value = day + "/" + month + "/" + year;
						} else {
							document.getElementById("txt_so_cancel_date").value = "";
							}
					}   
			}
			param="name=" + name + "&value="+ value +"&so_id=" + so_id+"&item_id="+item_id;
			showHintPost(url+param, str, param, hint);
			 setTimeout('RefreshHistory()', 1000);
		}

		//////////////////// eof security check /////////////////////
		function RefreshHistory()
        {
            var so_id = document.form1.so_id.value;
        }
	</script>
	 <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>   
	<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
     <%@include file="../portal/header.jsp" %> <%@include file="../Library/so-dash.jsp" %><table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0">
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
                  <th colspan="4">&nbsp;Sales Order Details</th>
                </tr>
                <tr>
                 <td align="right" valign="top">Customer:</td>
                <td align="left" valign="top"><%=mybean.link_customer_name%></td>
                <td align="right" valign="top">Contact:</td>
                <td align="left" valign="top"><%=mybean.link_contact_name%></td>
                </tr>
                <tr>
                <td align="right" valign="top">SO ID:</td>
                <td align="left" valign="top"><%=mybean.so_id%>
                <input name="txt_item_id" type="hidden" id ="txt_item_id"  value="<%=mybean.item_id%>"/>  
                </td>
                <td align="right" valign="top">SO Date:</td>
                <td align="left" valign="top"><%=mybean.sodate%></td>
                </tr>
                
                <tr>
                 <td align="right" valign="top">Enquiry ID:</td>
                <td align="left" valign="top"><%=mybean.enquiry_link%>
                
                <input type="hidden" id="so_id" name="so_id" value="<%=mybean.so_id%>" />
                </td>
                <td align="right" valign="top">Quote ID:</td>
                <td align="left" valign="top"><%=mybean.quote_link%></td>
                </tr>
               
                <!--<tr>
                        <td align="right" valign="top">Stock Comm. No.:</td>
                        <td align=left colspan="3"><input name="txt_so_vehstock_id" type="hidden" id ="txt_so_vehstock_id"  value="<%//=mybean.so_vehstock_id%>"/>
						<input name="txt_stock_comm_no" type="text" class="textbox" id ="txt_stock_comm_no"  value="<%//=mybean.stock_comm_no%>"onchange="SecurityCheck('txt_stock_comm_no',this,'hint_txt_stock_comm_no');"/>
                    <div class="hint" id="hint_txt_stock_comm_no"></div></td>
                      </tr> -->
                   
                <tr>
                  <td align="right" valign="top">Finance By:</td><input name="txt_so_vehstock_id" type="hidden" id ="txt_so_vehstock_id"  value="<%=mybean.so_vehstock_id%>"/>
                  <td align="left" colspan="3"><select name="dr_finance_by" class="selectbox" id="dr_finance_by" onchange="PopulateCheck('dr_finance_by',this,'hint_dr_finance_by');">
                      <%=mybean.PopulateFinanceBy(mybean.comp_id)%>   
                    </select>
                    <div class="hint" id="hint_dr_finance_by"></div></td>
                </tr>
                <tr>
                  <td align="right" valign="middle">Finance Amount:</td>
                  <td align="left" colspan="3"><input name= "txt_so_finance_amt" type="text" class="textbox"  id ="txt_so_finance_amt" value ="<%=mybean.so_finance_amt %>" size="20" maxlength="255" style="width:250px" onchange="SecurityCheck('txt_so_finance_amt',this,'hint_txt_so_finance_amt');" onkeyup="toInteger('txt_so_finance_amt','Finance Amount');"/>
                    <div class="hint" id="hint_txt_so_finance_amt"></div></td>
                </tr>
                
                
               
                <tr>
                  <td align="right" valign="middle">Booking Amount:</td>
                  <td align="left" colspan="3"><input name ="txt_so_booking_amount" type="text" class="textbox" id="txt_so_booking_amount" size="12" maxlength="10" value="<%=mybean.so_booking_amount%>" onchange="SecurityCheck('txt_so_booking_amount',this,'hint_txt_so_booking_amount');" onkeyup="toInteger('txt_so_booking_amount','Booking Amount');"/>
                  <div class="hint" id="hint_txt_so_booking_amount"></div></td>
                </tr>

                <tr>
                  <td align="right" valign="middle">Payment Date<b><font color="#ff0000">*</font></b>:</td>
                  <td valign="middle" align="left" colspan="3"><input name="txt_so_payment_date" type="text" class="textbox"  id ="txt_so_payment_date" value = "<%=mybean.so_paymentdate%>" size="12" maxlength="10" onchange="SecurityCheck('txt_so_payment_date',this,'hint_txt_so_payment_date');"/>
                    <br>
                    <div class="hint" id="hint_txt_so_payment_date"></div></td>
                </tr>
                <tr>
                  <td align="right" valign="middle">Tentative Delivery Date<b><font color="#ff0000">*</font></b>:</td>
                  <td valign="middle" align="left" colspan="3"><input name="txt_so_promise_date" type="text" class="textbox"  id ="txt_so_promise_date" value = "<%=mybean.so_promisedate%>" size="12" maxlength="10" onchange="SecurityCheck('txt_so_promise_date',this,'hint_txt_so_promise_date');"/>
                  <div class="hint" id="hint_txt_so_promise_date"></div></td>
                </tr>

                    <tr>
                      <td align="right" valign="middle">Delivered Date:</td>
                      <td valign="middle" align=left colspan="3"><input name="txt_so_delivered_date" type="text" class="textbox"  id ="txt_so_delivered_date" value = "<%=mybean.so_delivereddate%>" size="12" maxlength="10" onchange="SecurityCheck('txt_so_delivered_date',this,'hint_txt_so_delivered_date');"/>
                  <div class="hint" id="hint_txt_so_delivered_date"></div></td>
                    </tr>
               <tr>
                  <td align="right" valign="middle">Sales Order Open:</td>
                  <td align="left" colspan="3"><input id="chk_so_open" type="checkbox" name="chk_so_open" <%=mybean.PopulateCheck(mybean.so_open)%> onchange="SecurityCheck('chk_so_open',this,'hint_chk_so_open');"/>
                  <div class="hint" id="hint_chk_so_open"></div></td>
                </tr>
                
                <tr>
                  <td align="right">Sales Order Reference No.<font color="#ff0000">*</font>:</td>
                  <td align="left" colspan="3"><input name="txt_so_refno" type="text" class="textbox"  id ="txt_so_refno"  value = "<%=mybean.so_refno%>" size="32" maxlength="50" onchange="SecurityCheck('txt_so_refno',this,'hint_txt_so_refno');" onkeyup="toInteger('txt_so_refno','Ref No.');"/>
                  <div class="hint" id="hint_txt_so_refno"></div></td>
                </tr>
                 <tr>
                      <td align="right" valign="middle">Cancel Date:</td>
                      <td valign="middle" align=left colspan="3"><input name="txt_so_cancel_date" type="text" class="textbox"  id ="txt_so_cancel_date" value = "<%=mybean.so_canceldate%>" size="12" maxlength="10" onchange="SecurityCheck('txt_so_cancel_date',this,'hint_txt_so_cancel_date');"/>
                  <div class="hint" id="hint_txt_so_cancel_date"></div></td>
                    </tr>
                   <tr>
                  <td align="right" valign="middle">Active:</td>
                  <td align="left" colspan="3"><input id="chk_so_active" type="checkbox" name="chk_so_active" <%=mybean.PopulateCheck(mybean.so_active)%> onchange="SecurityCheck('chk_so_active',this,'hint_chk_so_active');"/>
                  <div class="hint" id="hint_chk_so_active"></div></td>
                </tr>
                <tr>
                  <td align="right" valign="middle">Notes:</td>
                  <td align="left" colspan="3"><textarea name="txt_so_notes" cols="70" rows="4" class="textbox" id="txt_so_notes" onchange="SecurityCheck('txt_so_notes',this,'hint_txt_so_notes')"><%=mybean.so_notes%></textarea>
                  <div class="hint" id="hint_txt_so_notes"></div>
                  </td>
                </tr>
                <tr>
                        <td align="right">Entry By:</td>
                        <td colspan="3" align="left"><%=mybean.so_entry_by%>
                            <input type="hidden" name="entry_by" value="<%=mybean.so_entry_by%>"></td>
                      </tr>
                        <tr>
                        <td align="right">Entry Date:</td>
                        <td colspan="3" align="left"><%=mybean.so_entry_date%>
                            <input type="hidden" name="so_entry_date" value="<%=mybean.so_entry_date%>"></td>
                      </tr>
                <% if (mybean.so_modified_by != null && !mybean.so_modified_by.equals("")) {%>
                <tr>
                  <td align="right">Modified By:</td>
                  <td colspan="3" align="left"><%=mybean.so_modified_by%>
                    <input type="hidden" name="modified_by" value="<%=mybean.so_modified_by%>"></td>
                </tr>
                <tr>
                  <td align="right">Modified Date:</td>
                  <td colspan="3" align="left"><%=mybean.so_modified_date%>
                    <input type="hidden" name="so_modified_date" value="<%=mybean.so_modified_date%>"></td>
                </tr>
                <%}%>
              </table></td>
          </tr>
        </table>
        <%}%>
      </form></td>
  </tr>
   <tr><td>&nbsp;</td></tr>
    </table> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
