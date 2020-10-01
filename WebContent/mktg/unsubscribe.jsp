<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.mktg.UnSubscribe" scope="request"/>
<%mybean.doGet(request,response); %>
<HTML>
    <HEAD>
    <title><%=mybean.AppName%> - <%=mybean.ClientName%></title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
    <link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
     <LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme1/style.css">
    </HEAD>
    <body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">    
    <TR>
    <TD align="center" vAlign="top" ><form name="form1"  method="post">
        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tableborder" >
          <tr valign="middle" >
            <td><table width="100%" border="0" cellpadding="1" cellspacing="0">
               
                 <tr>
                   <td height="200" align="center" valign="top">&nbsp;</td>
                </tr>
                 <tr>
                  <td align="center" valign="top"><b>You have been unsubscribed successfully! </b></td>
                </tr>
                 <tr>
                   <td align="center" valign="top">&nbsp;</td>
                 </tr>                            
            </table></td>
          </tr>
        </table>
      </form></TD>
  </TR>
    </TABLE></body>
</HTML>
