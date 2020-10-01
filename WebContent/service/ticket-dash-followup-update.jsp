<%-- 
    Document : ticket-add
    Created on: Feb 11, 2013
    Author   : Gurumurthy TS
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Ticket_Dash_Followup_Update" scope="request"/>
<%mybean.doGet(request,response); %>
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
<script language="JavaScript" type="text/javascript">

function FormFocus() { //v1.0
  document.form1.txt_tickettrans_followup.focus()
}
        </script>
    <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
    <body  onLoad="FormFocus()" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
     <%@include file="../portal/header.jsp" %> <%@include file="../Library/ticket-dash.jsp" %><TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
            <TR>
                <TD align="center" vAlign="top"><font color="#ff0000" ><b><%=mybean.msg%><br>
                </b></font>
                </TD>
            </TR>
            <TR>
                <TD height="300" align="center" vAlign="top">
                       
								 <form name="form1"  method="post">
                                <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
                            <tr>
                                <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
                                                    <tr align="center">
                                                      <th colspan="2">
                                                        <%=mybean.status%>&nbsp;Follow-up</th>
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                        <td><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b>
                                                                are required.
                                                            </font>
                                                        </td>
                                                    </tr>
                                              <tr valign="middle">
                                                  <td align="right" valign="middle">Description<font color="#ff0000">*</font><b></b>:</td>
                                                    <td><textarea name="txt_tickettrans_followup" cols="60" rows="6" class="textbox" id="txt_tickettrans_followup" onKeyUp="charcount('txt_tickettrans_followup', 'span_txt_tickettrans_followup','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.tickettrans_followup%></textarea>
                                                      <span id="span_txt_tickettrans_followup"> 8000 characters </span></td>
                                                </tr>
                                                <%if(mybean.add.equals("yes") || (mybean.tickettrans_contact_entry_id.equals("0"))) {%>
                                                    <tr valign="middle">
                                                      <td align="right">Private:</td>
                                                      <td align="left"><input id="chk_private" type="checkbox" name="chk_private" <%=mybean.PopulateCheck(mybean.tickettrans_private)%> /></td>
                                              </tr>
                                             <%}%>
      <% if (mybean.status.equals("Update") &&!(mybean.contact_entry_by == null) && !(mybean.contact_entry_by.equals(""))) { %>                                       <tr>
          <td align="right">Customer</td>
          <td colspan="3" align="left"><%=mybean.contact_entry_by%>
            <input type="hidden" name="contact_entry_by" value="<%=mybean.contact_entry_by%>"></td>
        </tr>
           <%}%>
    <% if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
        
        <tr>
          <td align="right">Entry By:</td>
          <td colspan="3" align="left"><%=mybean.unescapehtml(mybean.entry_by)%>
            <input type="hidden" name="entry_by" value="<%=mybean.unescapehtml(mybean.entry_by)%>"></td>
        </tr>
        <tr>
          <td align="right">Entry Date:</td>
          <td colspan="3" align="left"><%=mybean.entry_date%>
            <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>"></td>
        </tr>
        <%}%>
        <% if (mybean.status.equals("Update") &&!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
        <tr>
          <td align="right">Modified By:</td>
          <td colspan="3" align="left"><%=mybean.unescapehtml(mybean.modified_by)%>
            <input type="hidden" name="modified_by" value="<%=mybean.unescapehtml(mybean.modified_by)%>"></td>
        </tr>
        <tr>
          <td align="right">Modified Date:</td>
          <td colspan="3" align="left"><%=mybean.modified_date%>
            <input type="hidden" name="modified_date" value="<%=mybean.modified_date%>"></td>
        </tr>
        <%}%>
                                                    <tr align="center">
                                                        <td colspan="2" valign="middle"><input name="ticket_id" type="hidden" id="ticket_id" value="<%=mybean.ticket_id%>">
                                                            <%if(mybean.status.equals("Add")){%>
                                                            <input name="addbutton" type="submit" class="button" id="addbutton" value="Add Follow-up" />
                                                            <input type="hidden" name="add_button" value="yes">
                                                            <%}else if (mybean.status.equals("Update")){%>
                                                            <input type="hidden" name="update_button" value="yes">
                                                            <input name="updatebutton" type="submit" class="button" id="updatebutton" value="Update Follow-up" />
                                                            <input name="delete_button" type="submit" class="button" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Follow-up"/>
                                                      <%}%></td>
                                                    </tr>
                                                    </table>
                                            </td>
                                        </tr>
                                    </table>  </form>
                </TD>
            </TR>
    </TABLE></body>
</HTML>
