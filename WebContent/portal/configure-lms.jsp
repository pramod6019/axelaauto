<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.portal.Configure_LMS" scope="request"/>
<%mybean.doGet(request, response);%>
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
<script type="text/javascript" src="../Library/jquery.js"></script>
<script type="text/javascript" src="../Library/jquery-ui.js"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>

<script language="JavaScript" type="text/javascript">

            function FormFocus() { //v1.0
                document.form1.txt_config_email1.focus()
            }
            function check(){
               // alert("count6565");
                var count=0;
                count=count+1;
                alert("count"+count);

            }

        </script>
    <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
    <body  onLoad="FormFocus()" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0"><%@include file="header.jsp" %>
        <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
            <TR>
                <TD align="left"><a href="home.jsp">Home</a> &gt; <a href="manager.jsp#lms">Business Manager</a> &gt; <a href="manager.jsp#lms">LMS</a> &gt; <a href="configure-lms.jsp">Configure LMS</a>: </TD>
            </TR>
            <TR>
                <TD align="center" vAlign="top"><font color="#ff0000" ><b><%=mybean.msg%></b></font></TD>
            </TR>
            <tr><td>&nbsp;</td></tr>
            <TR>
                <TD height="300" align="center" vAlign="top">
                                <form name="form1"  method="post">
                                   <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
                            <tr>
                                <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
                                                    <tbody>
                                                        <tr align="center">
                                                            <th colspan="2">Configure LMS</th>
                                                        </tr>
                                                        <tr>
                                                            <td width="30%">&nbsp;</td>
                                                            <td width="550"><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br>
                                                                </font></td>
                                                        </tr>
                        
                                                        <tr valign="center">
                                                            <td align="right">Enable Leads:</td>
                                                            <td valign="top"><input type="checkbox" name="chk_config_sales_leads" <%=mybean.PopulateCheck(mybean.config_sales_leads)%>></td>
                                                        </tr>


                                                        <tr valign="center">
                                                            <td align="right">Enable Enquiry:</td>
                                                            <td valign="top"><input type="checkbox" name="chk_config_sales_enquiry"  <%=mybean.PopulateCheck(mybean.config_sales_enquiry)%> ></td>
                                                        </tr>

                                                        <tr valign="center">
                                                            <td align="right">Enable Quotes:</td>
                                                            <td valign="top"><input type="checkbox" name="chk_config_sales_quote"  <%=mybean.PopulateCheck(mybean.config_sales_quote)%> ></td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="2" align="center" valign="center">
                                                                <input name="update_button" type="submit" class="button" id="update_button" value="Update" /></td>
                                                        </tr>
                                                    </tbody>
                                      </table></td>
                                        </tr>
                                    </table>
                                </form>
                </TD>
            </TR>
        </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
