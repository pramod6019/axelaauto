<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.mktg.Campaign_Testmail" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <HEAD>
    <title><%=mybean.AppName%> - <%=mybean.ClientName%></title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
    <link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
    <LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
    <LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
    <link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
    <link href="../Library/theme<%=mybean.GetTheme(request)%>/menu-mobile.css" rel="stylesheet" media="screen" type="text/css" />
    <script type="text/javascript" src="../Library/Validate.js"></script>
    <script type="text/javascript" src="../Library/dynacheck.js"></script>
    <script type="text/javascript" src="../Library/jquery.js"></script>
    <script type="text/javascript" src="../Library/jquery-ui.js"></script>
    <script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
    <script>
 $(function() {
    $( "#txt_account_since" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
});
</script>
    <script language="JavaScript" type="text/javascript">
        function FormFocus() { //v1.0
            document.form1.txt_account_name.focus();
        }
    </script>
    </HEAD>
    <body onLoad="FormFocus();" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
      <TR>
    <TD align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="newsletter.jsp">Campaign</a> &gt; <a href="campaign-list.jsp?all=recent">List Campaign</a> &gt; <a href="campaign-testmail.jsp?<%=mybean.QueryString%>">Campaign Test Mail</a>: </TD>
  </TR>
      <TR>
    <TD align="center" vAlign="top" ><font color="#ff0000"><b><%=mybean.msg%></b></font><br></TD>
  </TR>
      <TR>
    <TD height="400" align="center" vAlign="top" ><form name="form1"  method="post">
        <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder" >
          <tr valign="middle" >
            <td><table width="100%" border="0" cellpadding="1" cellspacing="0" class="listtable">
                <%if(mybean.send.equals("yes")){%>
                <tr>
                  <th colspan="2" align="center">Send Campaign</th>
                </tr>
                <%} else {%>
                <tr>
                  <th colspan="2" align="center">Campaign Test Mail</th>
                </tr>
                <%}%>
                <tr>
                  <td align="right">&nbsp;</td>
                  <td align="left" valign="top"><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.</font></td>
                </tr>
                <tr>
                  <td align="right" valign="top">Campaign ID:</td>
                  <td align="left" valign="top"><a href ="campaign-list.jsp?campaign_id=<%=mybean.campaign_id%>"><%=mybean.campaign_id%></a></td>
                </tr>
                <tr>
                  <td align="right" valign="top">Branch:</td>
                  <td align="left" valign="top"><%=mybean.branch_name%></td>
                </tr>
                <tr>
                  <td align="right" valign="top">Subject:</td>
                  <td align="left" valign="top"><%=mybean.campaign_subject%>
                   </td>
                </tr>
                 <tr>
                  <td align="right" valign="top">Message:</td>
                  <td align="left" valign="top"><%=mybean.campaign_msg%>
                   </td>
                </tr>
                <% if(!(mybean.send.equals("yes"))) { %>
                <tr>
                <td align="right" valign="top">Test Email ID:</td>
                  <td align="left" valign="top"><input name ="txt_email_id" type="text" class="textbox"  id ="txt_email_id" value = "<%=mybean.email_id %>" size="40" MaxLength="100"></td>
                  </tr>
                  <%}%>
                <% if (!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
                <tr>
                  <td align="right" valign="top">Entry by:</td>
                  <td align="left" valign="top"><%=mybean.unescapehtml(mybean.entry_by)%>
                    <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>"></td>
                </tr>
                <tr>
                  <td align="right" valign="top">Entry Date:</td>
                  <td align="left" valign="top"><%=mybean.entry_date%>
                    <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>"></td>
                </tr>
                <%}%>               
                <tr>
                  <td colspan="2" align="center">
                   <% if(!(mybean.send.equals("yes"))) { %>
                  
                   <input name="submit_button" type="submit" class="button" id="submit_button" value="Go" />
            <input type="hidden" name="submit_button" value="Submit">
            <% } else {%>
            <input name="send_button" type="submit" class="button" id="send_button" value="Send" />
            <input type="hidden" name="send_button" value="Send">
            <%}%>
                   </td>
                </tr>
            </table></td>
          </tr>
        </table>
      </form></TD>
  </TR>
    </TABLE><%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
