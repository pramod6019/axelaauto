<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.accounting.SalesOrder_Payment_Track" scope="request"/>
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
<%@include file="../Library/css.jsp" %>

</HEAD>
	<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
    <%@include file="../portal/header.jsp" %>
    <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
      <TR>
        <TD align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href="index.jsp">Sales</a> &gt; <a href="voucher-list.jsp?all=yes">List Sales Orders</a> &gt;&nbsp;<a href="salesorder-payment-track.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Payment Track</a>: </TD>
      </TR>
      <TR>
        <TD align="center" vAlign="top"><font color="#ff0000"><b><%=mybean.msg%></b></font><br></TD>
      </TR>
      <TR>
        <TD align="center" vAlign="top"><form name="form1"  id="form1" method="post">
            <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
            <tr>
                <td colspan="2" align="center" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable formdata">
                    <tr>
                    <th scope="col" colspan="2"><%=mybean.status%>&nbsp;Payment Track</th>
                  </tr>
                    <tr>
                    <td align="center" valign="top" scope="col" colspan="2"><table width="100%" border="1" cellpadding="0" cellspacing="0" class="listtable">
                  <tr>
                    <td colspan="5" align="center"><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required. </font></td>
                  </tr>
                        <tr>
                        <td align="right" valign="top" ><input type="hidden" id="txt_voucher_id" name="txt_voucher_id" value="<%=mybean.voucher_id%>" />
                            <input type="hidden" id="txt_voucher_amt" name="txt_voucher_amt" value="<%=mybean.voucher_amt%>" />
                            Sales Order ID:</td>
                        <td valign="top" align=left nowrap><a href="voucher-list.jsp?voucher_id=<%=mybean.voucher_id%>&vouchertype_id=<%=mybean.vouchertype_id%>"><b><%=mybean.voucher_id%></b></a></td>
                        <td align="right" valign="top" nowrap>Sales Order Date:</td>
                        <td valign="top" align=left nowrap><b><%=mybean.voucherdate%></b>
                            <input name="txt_voucher_date" type="hidden" class="textbox"  id ="txt_voucher_date" value = "<%=mybean.voucherdate%>" /></td>
                      </tr>
                        <tr valign="center" id="contact_link">
                        <td align="right" valign="top">Customer:</td>
                        <td valign="top" align=left nowrap><b><span id="span_quote_customer_id" name="span_quote_customer_id"><%=mybean.customer_name%></span></b>&nbsp;
                            <input name="acct_id" type="hidden" id ="acct_id" value ="<%=mybean.customer_id%>" ></td>
                        <td align="right" valign="top">Contact:</td>
                        <td valign="top" align=left nowrap><b><span id="span_quote_contact_id" name="span_quote_contact_id"><%=mybean.contact_name%></span></b>&nbsp;
                            <input name="span_cont_id" type="hidden" id ="span_cont_id" value ="<%=mybean.contact_id%>" >
                            <input name="cont_id" type="hidden" id ="cont_id" value ="<%=mybean.contact_id%>" >
                            <div id="dialog-modal"></div></td>
                      </tr>
                        <tr valign="center" id="contact_link">
                        <td align="right" valign="top">Sales Order Amount:</td>
                        <td valign="top" align=left nowrap><b><%=mybean.IndFormat(Double.toString(mybean.voucher_amt))%></b></td>
                        <input type="hidden" value="<%=mybean.voucher_amt%>" id="txt_voucher_amt" name="txt_voucher_amt" />
                        <td align="right" valign="top">&nbsp;</td>
                        <td valign="top" align=left nowrap>&nbsp;</td>
                      </tr>
                        <tr>
                      <tr valign="center">
                        <td align="right">Calculate Payment from&nbsp;</td>
                        <td align="left" colspan="3"><input name="txt_payment_from" type="text" class="textbox" id="txt_payment_from" value="<%=mybean.voucherdate%>" size="11" maxlength="10"/>
                          for
                          <select name="dr_days" id="dr_days" class="selectbox">
                            <%=mybean.PopulateDays()%>
                          </select>
                          Parts
                          <input input type="submit" class="button" name="calculate_button" id="calculate_button" value="Calculate" onClick="payment_chk_calculate();return SubmitFormOnce(document.form1, this);" />
                          <input id="txt_days" type="hidden" value="<%=mybean.no_days%>">
                        <input type="hidden" name="txt_calculate" id="txt_calculate"></td>
                      </tr>
                        <tr>
                        <td colspan="4" scope="col" align="center"><span id="track_div"> <%=mybean.GetPaymentTrackDetails(mybean.voucher_id,Integer.toString(mybean.no_days),request)%></span></td>
                      </tr>
                      </table></td>
                  </tr>
                    <tr>
                    <td colspan="2" align="center"><%if (mybean.status.equals("Add")) {%>
                        <input name="addbutton" id="addbutton" type="button" onClick="return SubmitFormOnce(document.form1, this);" class="button" value="Add Payment Track"/>
                        <input type="hidden" name="add_button" value="yes">
                      <%}%></td>
                  </tr>
                  </table></td>
              </tr>
          </table>
          <br>
		  <%//=mybean.ListReceiptData()%>
        </form></TD>
      </TR>
    </TABLE>
    <%@include file="../Library/admin-footer.jsp" %>
   <%@include file="../Library/js.jsp" %>
	<script type="text/javascript" src="../Library/veh-quote.js"></script>
	<script type="text/javascript">
	function payment_chk_calculate()
	{
		document.getElementById("txt_calculate").value="Calculate";
			}
			function track_results()
			{
				var result=0;
				var track_voucher_amt=parseFloat(document.getElementById('txt_voucher_amt').value);
						
			for(var i=1;i<=document.getElementById('txt_days').value;i++)
			{
				var track_payment_id='txt_payment_amt'+i;
				
				var newresult=parseFloat(document.getElementById(track_payment_id).value);
				if(newresult=="" || isNaN(newresult)==true)
				{
					newresult=0;
				}
				 result = parseInt(result)+parseInt(newresult);
			}
			
			document.getElementById('track_total').innerHTML= result;
			document.getElementById('track_balance').innerHTML=parseInt(track_voucher_amt)-parseInt(result);
			}
			
			function AddFinanceStatus(){
				document.getElementById("txt_add_status").value = "Add";
				}
	
	</script>
  </body>
</HTML>
