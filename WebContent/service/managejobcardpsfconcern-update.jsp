<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.ManageJobCardPSFConcern_Update" scope="request"/>
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
  document.form1.txt_stagejc_name.focus()
}
        </script>
        <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
        <body  onLoad="FormFocus()" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <%@include file="../portal/header.jsp" %>
        <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
          <tr>
            <td colspan="2" align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/manager.jsp">Business Manager</a> &gt; <a href="managejobcardpsfconcern.jsp?all=yes">List PSF Concern</a> &gt; <a href="managejobcardstage-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;PSF Concern</a>:</td>
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
                    <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
                        <tr align="center">
                        <th colspan="2"><%=mybean.status%> PSF Concern</th>
                      </tr>  
                        <tr>
                        <td width="40%">&nbsp;</td>
                        <td align=left><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br>
                          </font></td>
                      </tr>                        
                        <tr valign="middle">
                        <td align="right" valign="middle">Description<font color="#ff0000">*</font>:</td>
                        <td align=left><input id="txt_jcpsfconcern_desc" name ="txt_jcpsfconcern_desc" type="text" class="textbox" value="<%=mybean.jcpsfconcern_desc %>" size="50" maxlength="255"/></td>
                      </tr>
                        
                        <% if (mybean.status.equals("Update")&& !(mybean.entry_by == null) && !(mybean.entry_by.equals("")) ) { %>
                        <tr valign="middle">
                        <td align="right">Entry By:</td>
                        <td align=left><%=mybean.unescapehtml(mybean.entry_by)%>
                            <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.unescapehtml(mybean.entry_by)%>"></td>
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
                        <td colspan="3" valign="middle"><%if(mybean.status.equals("Add")){%>
                            <input name="addbutton" type="submit" class="button" id="addbutton" value="Add PSF Concern" onclick="return SubmitFormOnce(document.form1,this);"/>
                            <input type="hidden" id="add_button" name="add_button" value="yes"/>
                            <%}else if (mybean.status.equals("Update")){%>
                            <input name="updatebutton" type="submit" class="button" id="updatebutton" value="Update PSF Concern" onclick="return SubmitFormOnce(document.form1,this);"/>
                            <input id="update_button" name="update_button" type="hidden" value="yes" />
                            <input name="delete_button" type="submit" class="button" id="delete_button" OnClick="return confirmdelete(this)" value="Delete PSF Concern" />
                            <%}%></td>
                      </tr>
                      </table></td>
                  </tr>
              </table>
              </form></TD>
          </TR>
        </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
