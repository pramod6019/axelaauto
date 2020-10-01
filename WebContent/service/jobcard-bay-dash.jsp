<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.JobCard_Bay_Dash" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
        <HEAD>
        <title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
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

function FormFocus() {
 // document.form1.txt_team_name.focus()
}
$(function() {  
$( "#txt_baytrans_start_time" ).datetimepicker({
			controlType: 'select',
		dateFormat: 'dd/mm/yy',
		stepMinute: 5,
		hour: 10,
	  	minute: 00
		});	
		$( "#txt_baytrans_end_time" ).datetimepicker({
			controlType: 'select',
		dateFormat: 'dd/mm/yy',
		stepMinute: 5,
		hour: 10,
	  	minute: 00
		});	
 });
 </script>
        <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
        <body  onLoad="FormFocus()" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
       
        <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
         
          <TR>
            <TD align="center" vAlign="top" ><font color="#ff0000" ><b><%=mybean.msg%></b></font> <br/></TD>
          </TR>
          <TR>
            <TD align="center" vAlign="top"><form name="form1" runat="server" method="post">
                <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
                <tr>
                    <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
                        <tbody>
                        <tr align="center">
                            <th colspan="2"><%=mybean.status%> Man Hours </th>
                          </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td align=left><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.</font></td>
                          </tr>
                        <tr>
                            <td align="right">Technician<font color="#ff0000">*</font>:</td>
                            <td align="left"><select id="dr_baytrans_emp_id" name="dr_baytrans_emp_id" class="selectbox">
                                <%=mybean.PopulateExecutives()%>
                              </select></td>
                          </tr>
                          <tr>     
                            <td align="right" valign="top">Bay:</td>
                            <td valign="top"><select name="dr_bay"  id="dr_bay" class="selectbox" >
                                <%=mybean.PopulateBay()%>
                              </select> </td></tr>
                        <tr>        
                            <td align="right">Start Time<font color="#ff0000">*</font>:</td>
                            <td valign="top"><input name="txt_baytrans_start_time" type="text" class="textbox"  id ="txt_baytrans_start_time" value = "<%=mybean.baytrans_start_time%>" size="20" maxlength="16"></td>    
                          </tr>   
                        <tr>
                            <td align="right">End Time<font color="#ff0000">*</font>:</td>
                            <td valign="top"><input name="txt_baytrans_end_time" type="text" class="textbox"  id ="txt_baytrans_end_time" value = "<%=mybean.baytrans_end_time%>" size="20" maxlength="16"></td>
                          </tr>
                        <% if (mybean.status.equals("Update") && (mybean.entry_by != null) && !(mybean.entry_by.equals(""))) { %>
                        <tr>
                            <td align="right">Entry By:</td>
                            <td><%=mybean.unescapehtml(mybean.entry_by)%>
                            <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>"></td>
                          </tr>
                        <tr>
                            <td align="right">Entry Date:</td>
                            <td><%=mybean.entry_date%>
                            <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>"></td>
                          </tr>
                        <%}%>
                        <% if (mybean.status.equals("Update") && (mybean.modified_by != null) && !(mybean.modified_by.equals(""))) { %>
                        <tr>
                            <td align="right">Modified By:</td>
                            <td><%=mybean.unescapehtml(mybean.modified_by)%>
                            <input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>"></td>
                          </tr>
                        <tr>
                            <td align="right">Modified Date:</td>
                            <td><%=mybean.modified_date%>
                            <input type="hidden" name="modified_date" value="<%=mybean.modified_date%>"></td>
                          </tr>
                        <%}%>
                        <tr align="center">
                            <td colspan="2" valign="middle"><%if(mybean.status.equals("Add")){%>
                            <input name="addbutton" type="submit" class="button" id="addbutton" value="Add Man Hours"  onClick="onPress();return SubmitFormOnce(document.form1, this);"/>
                            <input type="hidden" name="add_button" value="yes">
                            <%}else if (mybean.status.equals("Update")){%>
                            <input name="update_button" type="hidden" value="yes" />
                            <input name="updatebutton" type="submit" class="button" id="updatebutton" value="Update Man Hours"  onClick="onPress();return SubmitFormOnce(document.form1, this);"/>
                            <input name="delete_button" type="submit" class="button" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Man Hours" />
                            <%}%></td>
                          </tr>
                      </tbody>
                      </table></td>
                  </tr>
              </table>
              </form></TD>
          </TR>
        </TABLE>
        
</body>
</HTML>
