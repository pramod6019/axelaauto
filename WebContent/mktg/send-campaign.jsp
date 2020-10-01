<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.mktg.Send_Campaign" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <HEAD>
    <title><%=mybean.AppName%>-<%=mybean.ClientName%></title>
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
    <script language="JavaScript" type="text/javascript">
        function FormFocus() { //v1.0
           // document.form1.txt_account_name.focus();
        }
		function CampaignCheck() { //v1.0
	var branch_id=document.getElementById("dr_branch").value;
	//var model_id=outputSelected(document.getElementById("dr_model").options);
	showHint('../mktg/campaign-check.jsp?branch_id=' + GetReplace(branch_id) ,'campaignHint');
    }
    </script>
    </HEAD>
    <body onLoad="FormFocus();" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
    <%@include file="../portal/header.jsp" %>
    <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
      <TR>
        <TD align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="campaign.jsp">Campaign</a> &gt; <a href="campaign-list.jsp?all=recent">List Campaign</a> &gt; <a href="send-campaign.jsp?<%=mybean.QueryString%>">Campaign Send Mail</a>: </TD>
      </TR>
      <TR>
     <TD align="center" vAlign="top" ><font color="#ff0000"><b><%=mybean.msg%></b></font><br></TD>
      </TR>
      <TR>
        <TD align="center" vAlign="top" height="300" ><form name="form1"  method="post">
            <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder" >
            <tr valign="middle" >
                <td><table width="100%" border="0" cellpadding="1" cellspacing="0" class="listtable">
                    <tr>
                    <th colspan="2" align="center">Send Campaign</th>
                  </tr>
                    <tr>
                    <td align="right">&nbsp;</td>
                    <td align="left" valign="top"><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.</font></td>
                  </tr>
                    <tr>
                      <td align="right" valign="top">Branch<font color="#ff0000">*</font>:</td>
                      <td align="left" valign="top"><select name="dr_branch" id="dr_branch" class="selectbox" onchange="CampaignCheck();"><%=mybean.PopulateBranch(mybean.campaign_branch_id,"",request)%> 
                      </select></td>
                    </tr>
                    <tr>
                    <td align="right" valign="top">Select Campaign<font color="#ff0000">*</font>:</td>
                    <td align="left" valign="top"><span id='campaignHint'>
                    <%=mybean.PopulateCampaign()%></span></td>
                  </tr>
                    <tr>
                    <td colspan="2" align="center">
                        <input name="submit_button" type="submit" class="button" id="submit_button" value="Send Now" />
                        <input type="hidden" name="submit_button" value="Submit">
                       </td>
                  </tr>
                  </table></td>
              </tr>
          </table>
          </form></TD>
      </TR>
    </TABLE><%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
