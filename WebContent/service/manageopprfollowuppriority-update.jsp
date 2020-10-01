<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.ManageInsurFollowupPriority_Update" scope="request"/>
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
  document.form1.txt_priorityInsurfollowup_name.focus()
}
        </script>
        <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
        <body  onLoad="FormFocus()" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <%@include file="../portal/header.jsp" %>
        <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
          <tr>
            <td colspan="2" align="left" bgcolor="white"><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/manager.jsp">Business Manager</a> &gt; <a href="manageinsurfollowuppriority.jsp?all=yes">List Insurance Follow-up Priority</a> &gt; <a href="manageinsurfollowuppriority-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Insurance Follow-up Priority</a>:</td>
          </tr>
          <TR>
            <TD align="center" vAlign="top" ><font color="#ff0000"><b> <%=mybean.msg%> <br>
            </b></font></TD>
          </TR>
          <TR>
            <TD height="300" align="center" vAlign="top" ><form name="form1" runat="server" method="post">
                <table width="100%" height="157" border="0" align="center" cellpadding="1" cellspacing="0">
                <tr>
                    <td><table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder" >
                        <tr>
                        <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"  class="listtable">
                            <tr align="center">
                            <th colspan="2"><strong><font color="#ffffff" ><%=mybean.status%> Insurance Follow-up Priority </font></strong></th>
                          </tr>
                            <tr>
                            <td width="40%">&nbsp;</td>
                            <td align=left><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br>
                              </font></td>
                          </tr>
                            <tr valign="middle">
                            <td align="right" valign="middle">Name<font color="#ff0000">*</font>: </td>
                            <td align=left><input name ="txt_priorityInsurfollowup_name" id="txt_priorityInsurfollowup_name" type="text" class="textbox" value="<%=mybean.priorityInsurfollowup_name %>" size="50" maxlength="255"/></td>
                          </tr>
                            <tr valign="middle">
                            <td align="right" valign="middle">Description<font color="#ff0000">*</font>: </td>
                            <td align=left><input name ="txt_priorityInsurfollowup_desc" type="text" class="textbox" value="<%=mybean.priorityInsurfollowup_desc %>" size="50" maxlength="255"/></td>
                          </tr>
                            <tr valign="middle">
                            <td align="right" valign="middle">Due Hours<font color="#ff0000">*</font>: </td>
                            <td align=left><input name ="txt_priorityInsurfollowup_duehrs" type="text" class="textbox" value="<%=mybean.priorityInsurfollowup_duehrs %>" size="50" maxlength="20"/></td>
                          </tr>
                            <tr valign="middle">
                            <td align="right" valign="middle">Level-1 Hours: </td>
                            <td align=left><input name ="txt_priorityInsurfollowup_trigger1_hrs" type="text" class="textbox" value="<%=mybean.priorityInsurfollowup_trigger1_hrs %>" size="10" maxlength="5"/></td>
                          </tr>
                            <tr valign="middle">
                            <td align="right" valign="middle">Level-2 Hours: </td>
                            <td align=left><input name ="txt_priorityInsurfollowup_trigger2_hrs" type="text" class="textbox" value="<%=mybean.priorityInsurfollowup_trigger2_hrs %>" size="10" maxlength="5"/></td>
                          </tr>
                            <tr valign="middle">
                            <td align="right" valign="middle">Level-3 Hours: </td>
                            <td align=left><input name ="txt_priorityInsurfollowup_trigger3_hrs" type="text" class="textbox" value="<%=mybean.priorityInsurfollowup_trigger3_hrs %>" size="10" maxlength="5"/></td>
                          </tr>
                            <tr valign="middle">
                            <td align="right" valign="middle">Level-4 Hours: </td>
                            <td align=left><input name ="txt_priorityInsurfollowup_trigger4_hrs" type="text" class="textbox" value="<%=mybean.priorityInsurfollowup_trigger4_hrs %>" size="10" maxlength="5"/></td>
                          </tr>
                            <tr valign="middle">
                            <td align="right" valign="middle">Level-5 Hours: </td>
                            <td align=left><input name ="txt_priorityInsurfollowup_trigger5_hrs" type="text" class="textbox" value="<%=mybean.priorityInsurfollowup_trigger5_hrs %>" size="10" maxlength="5"/></td>
                          </tr>
                            
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
                                <input name="addbutton" type="submit" class="button" id="addbutton" value="Add Insurance Follow-up Priority" onclick="return SubmitFormOnce(document.form1, this);" />
                                <input type="hidden" name="add_button" value="yes">
                                <%}else if (mybean.status.equals("Update")){%>
                                <input name="updatebutton" type="submit" class="button" id="updatebutton" value="Update Insurance Follow-up Priority" onclick="return SubmitFormOnce(document.form1,this);"/>
                                <input name="update_button" type="hidden" value="yes" />
                                <input name="delete_button" type="submit" class="button" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Insurance Follow-up Priority" />
                                <%}%></td>
                          </tr>
                          </table></td>
                      </tr>
                      </table></td>
                  </tr>
              </table>
              </form></TD>
          </TR>
        </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
