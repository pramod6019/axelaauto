<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.accounting.ManagePayDays_Update" scope="request"/>
<%mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
        <HEAD>
        <title><%=mybean.AppName%></title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
        <LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
        <LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
        <link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
        <link href="../Library/theme<%=mybean.GetTheme(request)%>/font-awesome.css" rel="stylesheet" media="screen" type="text/css" />
        <script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
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
        <script type="text/javascript" src="../Library/Validate.js?target=<%=mybean.jsver%>"></script>
        <script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
       
        <script language="JavaScript" type="text/javascript">
			  

function FormFocus() { //v1.0
  document.form1.txt_priorityoppr_name.focus();
  document.getElementById('refrow').style.display = 'none' ;
  document.getElementById('consignee_row').style.display = 'none' ;

}
function CheckReferenceNo()
{	
		var check=document.form1.ch_vouchertype_ref_no_enable_active.checked;
		var check1=document.form1.ch_vouchertype_ref_no_mandatory_active.checked;	
		
			if(check=="1" ||  check=="on")
            {				
			     displayRow('refrow');
				 if(check1=="1" || check1=="on")
				 {
				  document.form1.ch_vouchertype_ref_no_mandatory_active.checked = 1 ;
				 }else {
				  document.form1.ch_vouchertype_ref_no_mandatory_active.checked = 0 ;
				 }
            }
            if(check=="0" ||  check==" ")
            {				
				hideRow('refrow');				
				document.form1.ch_vouchertype_ref_no_mandatory_active.checked = 0 ;
            }
}

function CheckBillingAddress()
{	
		var check=document.form1.ch_vouchertype_billing_add.checked;
		var check1=document.form1.ch_vouchertype_consignee_add.checked;	
		
			if(check=="1" ||  check=="on")
            {				
			     displayRow('consignee_row');
				 if(check1=="1" || check1=="on")
				 {
				  document.form1.ch_vouchertype_consignee_add.checked = 1 ;
				 }else {
				  document.form1.ch_vouchertype_consignee_add.checked = 0 ;
				 }
            }
            if(check=="0" ||  check==" ") 
            {				
				hideRow('consignee_row');				
				document.form1.ch_vouchertype_consignee_add.checked = 0 ;
            }
}


        </script>
        <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
        <body  onLoad="CheckBillingAddress();CheckReferenceNo();FormFocus()" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <%@include file="../portal/header.jsp" %>
        <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
          <tr>
            <td colspan="2" align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/manager.jsp">Business Manager</a> &gt; <a href="managepaydays.jsp?all=yes">List Pay Days</a> &gt; <a href="managepaydays-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Pay Days</a>:</td>
          </tr>
          <TR>
            <TD>&nbsp;</TD>
          </TR>
          <TR>
            <TD align="center" vAlign="top" ><font color="#ff0000"><b> <%=mybean.msg%> <br>
            </b></font></TD>
          </TR>
          <TR>
            <TD height="300" align="center" vAlign="top"><form name="form1" runat="server" method="post">
                <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
                <tr>
                    <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable formdata">
                        <tr align="center">
                        <th colspan="2"><%=mybean.status%>&nbsp;Pay Days</th>
                      </tr>
                        <tr>
                        <td width="30%">&nbsp;</td>
                        <td align=left><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br>
                          </font></td>
                      </tr>
                        <tr valign="middle">
                        <td align="right" valign="middle">Name<font color="#ff0000">*</font>: </td>
                        <td>
                            <input name="txt_paydays_name" type="text" id="txt_paydays_name"   class="textbox" value="<%=mybean.paydays_name %>"/></td>
                      </tr>
                      <tr valign="middle">
                        <td align="right" valign="middle">Days<font color="#ff0000">*</font>: </td>
                        <td>
                            <input name="txt_paydays_days" type="text" id="txt_paydays_days"  class="textbox" value="<%=mybean.paydays_days %>"/></td>
                      </tr>
                        
                        <% if (mybean.status.equals("Update")&& !(mybean.entry_by == null) && !(mybean.entry_by.equals("")) ) { %>
                        <tr valign="middle">
                        <td align="right">Entry By:</td>
                        <td align=left><%=mybean.unescapehtml(mybean.entry_by)%>
                            <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>"/></td>
                      </tr>
                        <%}%>
                        <% if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals("")) ) { %>
                        <tr valign="middle">
                        <td align="right">Entry Date:</td>
                        <td align=left><%=mybean.entry_date%>
                            <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>"></td>
                      </tr>
                        <%}%>
                        <% if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals("")) ) { %>
                        <tr valign="middle">
                        <td align="right">Modified By:</td>
                        <td align=left><%=mybean.unescapehtml(mybean.modified_by)%>
                            <input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.unescapehtml(mybean.modified_by)%>"></td>
                      </tr>
                        <%}%>
                        <% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
                        <tr valign="middle">
                        <td align="right">Modified Date:</td>
                        <td align=left><%=mybean.modified_date%>
                            <input type="hidden" name="modified_date" value="<%=mybean.modified_date%>"></td>
                      </tr>
                        <%}%>
                       <tr align="center">
                        <td colspan="2" valign="middle"><%if(mybean.status.equals("Add")){%>
                            <input name="addbutton" type="submit" class="button" id="addbutton" value="Add Pay Days" onclick="return SubmitFormOnce(document.form1,this);" />
                            <input type="hidden" name="add_button" value="yes">
                            <%}else if (mybean.status.equals("Update")){%>
                            <input type="hidden" name="update_button" value="yes">
                            <input name="updatebutton" type="submit" class="button" id="updatebutton" value="Update Pay Days" onclick="return SubmitFormOnce(document.form1,this);"/>
                            <input name="delete_button" type="submit" class="button" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Pay Days"/>
                            <%}%></td>
                      </tr>
                      </table></td>
                  </tr>
              </table>
              </form></TD>
          </TR>
        </TABLE>
         <%@include file="../Library/admin-footer.jsp" %>
        </body>
</HTML>
