<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Leave_Update" scope="request"/>
<%mybean.doPost(request,response); %>
<jsp:setProperty name="mybean" property="*" />
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
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<script>
	$(function() {
	$( "#txt_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
    $( "#txt_leave_fromtime" ).datetimepicker({
      addSliderAccess: true,
	  sliderAccessArgs: {touchonly: false},
      dateFormat: "dd/mm/yy",
	  hour: 10,
	  stepMinute: 5,
	  minute: 00
    });
	$( "#txt_leave_totime" ).datetimepicker({
      addSliderAccess: true,
	  sliderAccessArgs: {touchonly: false},
      dateFormat: "dd/mm/yy",
	  hour: 10,
	  stepMinute: 5,
	  minute: 00
    });	
  });
</script>
<script language="JavaScript" type="text/javascript">
function FormFocus() { //v1.0
  document.formdemo.dr_branch_id.focus();
}
    </script>

<body  onLoad="FormFocus();" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
  <TR>
  <TD  align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href="index.jsp">Service</a> &gt; <a href="appt.jsp">Booking</a> &gt;  <a href="leave-list.jsp?all=yes"> List Leaves</a> &gt; <a href="leave-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Leave</a>:</TD>
  </TR>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <TR>
    <TD align="center" vAlign="top"><font color="#ff0000" ><b><%=mybean.msg%></b></font></TD>
  </TR>
  <TR>
    <TD height="400" align="center" vAlign="top" bgColor="white"><form name="formdemo"  method="post">
        <table width="100%" border="1" align="center" cellpadding="0" cellspacing="1" class="tableborder" >
          <tr valign="middle" >   
            <td width="60%" align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellpadding="1" cellspacing="0" class="listtable">
                <tbody>
                  <tr align="middle">
                    <th colspan="2" align="center"><%=mybean.status%> Leave</th>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td valign="top"><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required. </font></td>
                  </tr>
                  <tr valign="center">
                    <td align="right">Branch<font color="#ff0000">*</font>:</td>
                    <td><select name="dr_branch_id" id="dr_branch_id" class="selectbox" visible="true" style="width:250px" onChange="showHint('../service/booking-check.jsp?dr_branch_id=' + GetReplace(this.value)+'&branch_emp=yes','leaveHint');">
                        <%=mybean.PopulateBranch()%>
                      </select>
                      </td>
                  </tr>  
                  <tr valign="center">
                    <td align="right">Employee<font color="#ff0000">*</font>:</td>
                    <td><span id="leaveHint"> <%=mybean.PopulateExecutive(mybean.emp_branch_id)%> </span id></td>
                  </tr>
                  <tr valign="center">
                    <td align="right"> From Date<font color="#ff0000">*</font>: </td>
                    <td valign="down"><input name="txt_leave_fromtime" type="text" class="textbox"  id ="txt_leave_fromtime" value = "<%=mybean.leavefromtime%>" size="20" maxlength="16" /></td>
                  </tr>
                  <tr valign="center">
                    <td align="right">To Time<font color="#ff0000">*</font>:</td>
                    <td valign="down"><input name="txt_leave_totime" type="text" class="textbox"  id ="txt_leave_totime" value = "<%=mybean.leavetotime%>" size="20" maxlength="16" /></td>
                  </tr>
                  <tr valign="center">
                    <td align="right"> Description: </td>
                    <td><textarea name="txt_leave_desc"  cols="70" rows="5" class="textbox" id="txt_leave_desc" onKeyUp="charcount('txt_leave_desc', 'span_txt_leave_desc','<font color=red>({CHAR} characters left)</font>', '1000')" style="width:250px"><%=mybean.leave_desc %></textarea>
                      <span id="span_txt_leave_desc">(1000 Characters)</span></td>
                  </tr>
                  <tr>
                    <td align="right">Notes:</td>
                    <td align="left"><textarea name="txt_leave_notes"  cols="70" rows="5" class="textbox" id="txt_leave_notes" onKeyUp="charcount('txt_leave_notes', 'span_txt_leave_notes','<font color=red>({CHAR} characters left)</font>', '1000')" style="width:250px"><%=mybean.leave_notes %></textarea>
                      <span id="span_txt_leave_notes">(1000 Characters)</span></td>
                  </tr>
                  <% if (mybean.status.equals("Update") &&!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
                  <tr>
                    <td align="right">Entry By:</td>
                    <td align="left"><%=mybean.unescapehtml(mybean.entry_by)%>
                      <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>"></td>
                  </tr>
                  <tr>
                    <td align="right">Entry Date:</td>
                    <td align="left"><%=mybean.entry_date%>
                      <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>"></td>
                  </tr>
                  <%}%>
                  <% if (mybean.status.equals("Update") &&!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
                  <tr>
                    <td align="right">Modified By:</td>
                    <td align="left"><%=mybean.unescapehtml(mybean.modified_by)%>
                      <input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>"></td>
                  </tr>
                  <tr>
                    <td align="right">Modified Date:</td>
                    <td align="left"><%=mybean.modified_date%>
                      <input type="hidden" name="modified_date" value="<%=mybean.modified_date%>"></td>
                  </tr>
                  <%}%>
                  <tr align="middle">
                    <td colspan="2" align="center" valign="center"><%if(mybean.status.equals("Add")){%>
                      <input name="addbutton" type="submit" class="button" id="addbutton" value="Add Leave" onClick="return SubmitFormOnce(document.formdemo, this);"/>   
                      <input type="hidden" name="add_button" id="add_button"  value="yes"/>
                      <%}else if (mybean.status.equals("Update")){%>
                      <input name="updatebutton" type="submit" class="button" id="updatebutton" value="Update Leave" onClick="return SubmitFormOnce(document.formdemo, this);"/> 
                      <input type="hidden" id="update_button" name="update_button" value="yes" />
                      <input name="delete_button" type="submit" class="button" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Leave" />
                      <%}%></td>
                  </tr>                  
                </tbody>
              </table>
        </table>
      </form></TD>
  </TR>
</TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
