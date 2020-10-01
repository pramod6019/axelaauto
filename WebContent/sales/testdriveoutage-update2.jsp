<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.TestDriveOutage_Update" scope="request"/>
<%mybean.doPost(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">   

<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck-post.js"></script>
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
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>

	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>

<script>
	$(function() {
	
    $( "#txt_outage_fromtime" ).datetimepicker({
      addSliderAccess: true,
	  sliderAccessArgs: {touchonly: false},
      dateFormat: "dd/mm/yy",
	  hour: 10,
	  stepMinute: 5,
	  minute: 00
    });
	$( "#txt_outage_totime" ).datetimepicker({
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
  document.formtestdrive.dr_branch_id.focus();
}
    </script>
     <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body  onLoad="FormFocus();" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
  <TR>
    <TD  align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="../sales/index.jsp">Sales</a> &gt; <a href="testdrive.jsp">Test Drive Outage</a> &gt; <a href="testdriveoutage-list.jsp?all=yes"> List Test Drive Outage</a> &gt; <a href="testdriveoutage-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Test Drive Outage</a>: </TD>
  </TR>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <TR>
    <TD align="center" vAlign="top"><font color="#ff0000" ><b><%=mybean.msg%></b></font></TD>
  
    </TR>
    <TR>
    <TD height="400" align="center" vAlign="top" bgColor="white">
    <form name="formtestdrive"  method="post">
   <table width="100%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#000000" >
    <tr valign="middle" >
        <td width="60%" align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellpadding="1" cellspacing="0" class="listtable">
          <tbody>
            <tr align="middle">
              <th colspan="2" align="center"><%=mybean.status%> Vehicle Test Drive</th>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td valign="top"><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required. </font></td>
            </tr>
            <tr valign="center">
              <td align="right" bgcolor="#ffffff">Branch<font color="#ff0000">*</font>:</td>
              <td><select name="dr_branch_id" id="dr_branch_id" class="selectbox" visible="true" style="width:250px" onChange="showHint('../sales/testdrive-vehicle-check.jsp?dr_branch_id=' + GetReplace(this.value) ,'vehicleHint');">
                <%=mybean.PopulateBranch()%>
              </select></td>
            </tr>
            <tr valign="center">
              <td align="right" bgcolor="#ffffff">Vehicle<font color="#ff0000">*</font>:</td>
              <td><span id="vehicleHint">
                <%=mybean.PopulateVehicle()%>
                </span id></td>
            </tr>
            <tr valign="center">
              <td align="right"> From Date<font color="#ff0000">*</font>: </td>
              <td><input name="txt_outage_fromtime" type="text" class="textbox"  id ="txt_outage_fromtime" value = "<%=mybean.outagefromtime%>" size="16" maxlength="16" />
              <input name="txt_outage_fromtime" type="hidden"  id ="txt_outage_fromtime" value = "<%=mybean.outagefromtime%>" />
              </td>
            </tr>
            <tr valign="center">
              <td align="right">To Time<font color="#ff0000">*</font>:</td>
              <td><input name="txt_outage_totime" type="text" class="textbox"  id ="txt_outage_totime" value = "<%=mybean.outagetotime%>" size="16" maxlength="16" />
              <input name="txt_outage_totime" type="hidden"  id ="txt_outage_totime" value = "<%=mybean.outagetotime%>" />
              </td>
            </tr>
            <tr valign="center">
              <td align="right"> Description: </td>
              <td><textarea name="txt_outage_desc"  cols="70" rows="5" class="textbox" id="txt_outage_desc" onKeyUp="charcount('txt_outage_desc', 'span_txt_outage_desc','<font color=red>({CHAR} characters left)</font>', '1000')" style="width:250px"><%=mybean.outage_desc %></textarea>
                <span id="span_txt_outage_desc">(1000 Characters)</span></td>
            </tr>
             <tr>
              <td align="right">Notes:</td>
              <td align="left"><textarea name="txt_outage_notes"  cols="70" rows="5" class="textbox" id="txt_outage_notes" onKeyUp="charcount('txt_outage_notes', 'span_txt_outage_notes','<font color=red>({CHAR} characters left)</font>', '1000')" style="width:250px"><%=mybean.outage_notes %></textarea>
                <span id="span_txt_outage_notes">(1000 Characters)</span></td>
            </tr>
            <% if (mybean.status.equals("Update") &&!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
           
            <tr>
              <td align="right">Entry By:</td>
              <td align="left"><%=mybean.entry_by%>
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
              <td align="left"><%=mybean.modified_by%>
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
                <input name="add_button" type="submit" class="button" id="add_button" value="Add Vehicle Outage" />
                <input type="hidden" name="add" value="yes">
                <%}else if (mybean.status.equals("Update")){%>
                <input name="update_button" type="submit" class="button" id="update_button" value="Update Vehicle Outage" />
                <input name="delete_button" type="submit" class="button" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Vehicle Outage" />
                <%}%></td>
            </tr>
            <tr align="middle">
              <td colspan="2" valign="center"><input type="hidden" name="testdrive_id" value="<%=mybean.outage_id%>">
                </td>
            </tr>
          </tbody>
        </table>
    </table>
    
    </form>
    </TD>
    </TR>
    </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
