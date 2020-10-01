<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.SalesOrder_Dash_FinStatus" scope="request"/>
<%mybean.doPost(request,response); %>
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
		<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
		 
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
		<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
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

			function RefreshHistory()
            {
            	var so_id = document.form1.so_id.value;
			}
		}

		function SecurityCheck(name,obj,hint)
        {
		  var value;
		  var so_id =  GetReplacePost(document.form1.so_id.value);
          var url = "../sales/salesorder-dash-check.jsp?" ;
		  var param="";
		  var str = "123";
		  if(name!="chk_enquiry_avpresent" && name!="chk_enquiry_manager_assist")
		  {
			value = GetReplacePost(obj.value);
		  }
		   else{
				//alert(obj.checked);
				if(obj.checked==true)
				{
					value = "1";
				}
				else
				{
					value = "0";
				}
			}
			//alert("na=="+name);
			param="name=" + name + "&value="+ value +"&so_id=" + so_id;
			showHintPost(url+param, str, param, hint);
			setTimeout('RefreshHistory()', 1000);
		  }
		</script>
		 <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

		<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <%@include file="../portal/header.jsp" %> <%@include file="../Library/so-dash.jsp" %><table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0">
          <TR>
            <TD align="center" vAlign="top" ><font color="#ff0000" ><b><%=mybean.msg%></b></font></TD>
          </TR>
          <tr>
            <td align="center" valign="top" height="300"><form name="form1" id="form1">
                <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
                <tr>
                    <th scope="col" colspan="4">Finance Status</th>
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
                <input name="txt_so_id" type="hidden" id ="txt_so_id"  value="<%=mybean.so_id%>"/>
                </td>
                <td align="right" valign="top">SO Date:</td>
                <td align="left" valign="top"><%=mybean.so_date%></td>
                </tr>
                
                <tr>
                 <td align="right" valign="top">Enquiry ID:</td>
                <td align="left" valign="top"><%=mybean.enquiry_link%>
                <input name="txt_enquiry_enquirytype_id" type="hidden" id ="txt_enquiry_enquirytype_id"  value="<%=mybean.enquiry_enquirytype_id%>"/>
                </td>
                <td align="right" valign="top">Quote ID:</td>
                <td align="left" valign="top"><%=mybean.quote_link%></td>
                </tr>

                  <tr valign="center">
                    <td align="right">Finance Type: </td>
                    <td align="left" colspan="3"><select name="dr_so_fintype_id" id="dr_so_fintype_id" class="selectbox" onchange="PopulateCheck('dr_so_fintype_id',this,'hint_dr_so_fintype_id');">
                        <%=mybean.PopulateFinanceType()%>
                      </select>
                    <br>
                    <div class="hint" id="hint_dr_so_fintype_id"></div></td>
                  </tr>
                <tr valign="center">
                    <td align="right">Finance Status: </td>
                    <td align="left" colspan="3"><select name="dr_so_finstatus_id" id="dr_so_finstatus_id" class="selectbox" onchange="PopulateCheck('dr_so_finstatus_id',this,'hint_dr_so_finstatus_id');">
                        <%=mybean.PopulateFinanceStatus()%>
                      </select>
                    <br>
                    <div class="hint" id="hint_dr_so_finstatus_id"></div>
                    <input type="hidden" id="so_id" name="so_id" value="<%=mybean.so_id%>" /></td>
                  </tr>
                <tr valign="center">
                    <td align="right">Description: </td>
                    <td align="left" colspan="3"><textarea id="txt_so_finstatus_desc" name="txt_so_finstatus_desc" cols="70" rows="4" class="textbox" onchange="SecurityCheck('txt_so_finstatus_desc',this,'hint_txt_so_finstatus_desc');"><%=mybean.so_finstatus_desc%></textarea>
                    <div class="hint" id="hint_txt_so_finstatus_desc"></div></td>
                  </tr>
                <tr valign="center">
                    <td align="right">Loan Amount: </td>
                    <td align="left" colspan="3"><input name="txt_so_finstatus_loan_amt" type="text" class="textbox"  id ="txt_so_finstatus_loan_amt" value = "<%=mybean.so_finstatus_loan_amt%>" size="12" maxlength="10" onchange= "SecurityCheck('txt_so_finstatus_loan_amt',this,'hint_txt_so_finstatus_loan_amt');" onkeyup="toInteger(this.id);"/>
                    <div class="hint" id="hint_txt_so_finstatus_loan_amt"></div></td>
                  </tr>
                <tr valign="center">
                    <td align="right">Tenure: </td>
                    <td align="left" colspan="3"><input name="txt_so_finstatus_tenure" type="text" class="textbox"  id ="txt_so_finstatus_tenure" value = "<%=mybean.so_finstatus_tenure%>" size="12" maxlength="10" onchange="SecurityCheck('txt_so_finstatus_tenure',this,'hint_txt_so_finstatus_tenure');" onkeyup="toInteger(this.id);"/>
                    Months
                    <div class="hint" id="hint_txt_so_finstatus_tenure"></div></td>
                  </tr>
                <tr valign="center">
                    <td align="right">Bank: </td>
                    <td align="left" colspan="3"><input name="txt_so_finstatus_bank" type="text" class="textbox" id="txt_so_finstatus_bank" value="<%=mybean.so_finstatus_bank%>" size="70" maxlength="255" onchange="SecurityCheck('txt_so_finstatus_bank',this,'hint_txt_so_finstatus_bank');"/>
                    <div class="hint" id="hint_txt_so_finstatus_bank"></div></td>
                  </tr>
                <tr valign="center">
                    <td align="right">Scheme: </td>
                    <td align="left" colspan="3"><input name="txt_so_finstatus_scheme" type="text" class="textbox" id="txt_so_finstatus_scheme" value="<%=mybean.so_finstatus_scheme%>" size="70" maxlength="255" onchange="SecurityCheck('txt_so_finstatus_scheme',this,'hint_txt_so_finstatus_scheme');"/>
                    <div class="hint" id="hint_txt_so_finstatus_scheme"></div></td>
                  </tr>
                <tr valign="center">
                    <td align="right">Mode: </td>
                    <td align="left" colspan="3"><input name="txt_so_finstatus_mode" type="text" class="textbox" id="txt_so_finstatus_mode" value="<%=mybean.so_finstatus_mode%>" size="70" maxlength="255" onchange="SecurityCheck('txt_so_finstatus_mode',this,'hint_txt_so_finstatus_mode');"/>
                    <div class="hint" id="hint_txt_so_finstatus_mode"></div></td>
                  </tr>
                <tr valign="center">
                    <td align="right">Subvention Amount: </td>
                    <td align="left" colspan="3"><input name="txt_so_finstatus_subvention" type="text" class="textbox"  id ="txt_so_finstatus_subvention" value = "<%=mybean.so_finstatus_subvention%>" size="12" maxlength="10" onchange="SecurityCheck('txt_so_finstatus_subvention',this,'hint_txt_so_finstatus_subvention');" onkeyup="toInteger(this.id);"/>
                    <div class="hint" id="hint_txt_so_finstatus_subvention"></div></td>
                  </tr>
                <tr valign="center">
                    <td align="right">Payout Rate: </td>
                    <td align="left" colspan="3"><input name="txt_so_finstatus_payout_rate" type="text" class="textbox"  id ="txt_so_finstatus_payout_rate" value = "<%=mybean.so_finstatus_payout_rate%>" size="12" maxlength="10" onchange="SecurityCheck('txt_so_finstatus_payout_rate',this,'hint_txt_so_finstatus_payout_rate');" onkeyup="toInteger(this.id);"/>
                    <div class="hint" id="hint_txt_so_finstatus_payout_rate"></div></td>
                  </tr>
                <tr valign="center">
                    <td align="right">Payback Amount: </td>
                    <td align="left" colspan="3"><input name="txt_so_finstatus_payback" type="text" class="textbox"  id ="txt_so_finstatus_payback" value = "<%=mybean.so_finstatus_payback%>" size="12" maxlength="10" onchange="SecurityCheck('txt_so_finstatus_payback',this,'hint_txt_so_finstatus_payback');" onkeyup="toInteger(this.id);"/>
                    <div class="hint" id="hint_txt_so_finstatus_payback"></div></td>
                  </tr>
                <tr valign="center">
                    <td align="right">Net Income: </td>
                    <td align="left" colspan="3"><input name="txt_so_finstatus_netincome" type="text" class="textbox"  id ="txt_so_finstatus_netincome" value = "<%=mybean.so_finstatus_netincome%>" size="12" maxlength="10" onchange="SecurityCheck('txt_so_finstatus_netincome',this,'hint_txt_so_finstatus_netincome');" onkeyup="toInteger(this.id);"/>
                    <div class="hint" id="hint_txt_so_finstatus_netincome"></div></td>
                  </tr>
                  
              </table>
              </form></td>
          </tr>
          <tr><td>&nbsp;</td></tr>
        </table> <%@include file="../Library/admin-footer.jsp" %>
        <script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
        </body>
</html>