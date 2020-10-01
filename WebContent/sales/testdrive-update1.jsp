<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.TestDrive_Update1" scope="request"/>
<%mybean.doPost(request, response);%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
     <HEAD>
     <title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
     <meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
     <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
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
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>

	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
     <script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
     <script language="JavaScript" type="text/javascript">
	
function TestDriveCheck()
{
	var veh_id=document.getElementById("dr_vehicle").value;
	var testdrivedate=document.getElementById("txt_testdrive_date").value;
	var branch_id=document.getElementById("branch_id").value;
	
	showHint('../sales/testdrive-check.jsp?testdrive=yes&veh_id='+veh_id+'&testdrivedate='+testdrivedate+'&branch_id='+branch_id+'',  'calHint');
}
    </script>
     <script language="JavaScript" type="text/javascript">
	$(function() {
    	$( "#txt_testdrive_date" ).datetimepicker({
      		showButtonPanel: true,
      		dateFormat: "dd/mm/yy",
			controlType: 'select'
			});
	});
		 
     </script>
      <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

     <body  onLoad="TestDriveCheck()" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
     <%@include file="../portal/header.jsp" %>
     <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
       <TR>
         <TD align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="../sales/index.jsp">Sales</a> &gt; <a href="testdrive.jsp">Test Drives</a> &gt; <a href="testdrive-list.jsp?all=recent">List Test Drives</a> &gt; <a href="testdrive-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Test Drive</a>: </TD>
       </TR>
       <TR>
         <TD align="center" vAlign="top" ><font color="#ff0000" ><b><%=mybean.msg%></b></font><br></TD>
       </TR>
       <TR>
         <TD  align="center" vAlign="top" ><form name="formcontact"  method="post">
             <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0"  class="tableborder">
             <tr valign="middle" >
                 <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable">
                     <tbody>
                     <tr align="middle">
                         <th colspan="4" align="center"><%=mybean.status%> Test Drive</th>
                       </tr>
                     <tr>
                         <td>&nbsp;</td>
                         <td colspan="3" valign="top"><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required. </font></td>
                       </tr>
                     <tr valign="center">
                         <td align="right">Customer:</td>
                         <td><b><a href="../customer/customer-list.jsp?customer_id=<%=mybean.testdrive_customer_id%>"><%=mybean.customer_name%> (<%=mybean.testdrive_customer_id%>)</a></b></td>
                       </tr>
                     <tr valign="center">
                         <td align="right">Oppurtunity Date:</td>
                         <td><%=mybean.strToShortDate(mybean.enquiry_date)%>
                         <input type="hidden" name="branch_id" id="branch_id" value="<%=mybean.enquiry_branch_id%>"></td>
                       </tr>
                     <tr valign="center">
                         <td align="right">Oppurtunity No.:</td>
                         <td><b><a href="enquiry-dash.jsp?enquiry_id=<%=mybean.testdrive_enquiry_id%>" target="_blank"><%=mybean.enquiry_no%></a></b></td>
                       </tr>
                     <tr valign="center">
                         <td align="right" >Sales Consultant:</td>
                         <td><b><a href="../portal/executive-summary.jsp?emp_id=<%=mybean.testdrive_emp_id%>"><%=mybean.executive_name%></a></b></td>
                       </tr>   
                     <tr valign="center">
                         <td align="right" >Model:</td>
                         <td><b><%=mybean.model_name%></b> 
                         </td>
                       </tr>
                     <tr valign="center">
                         <td align="right" >Vehicle<font color="#ff0000">*</font>:</td>
                         <td><select name="dr_vehicle" class="selectbox" id="dr_vehicle" onChange="TestDriveCheck()">
                             <%=mybean.PopulateVehicle()%>
                           </select></td>
                       </tr>
                     <tr valign="center">
                         <td align="right"> Test Drive Date<font color="#ff0000">*</font>: </td>
                         <td><input name="txt_testdrive_date" type="text" class="textbox"  id ="txt_testdrive_date" value = "<%=mybean.testdrivedate%>" size="20" maxlength="16" onChange="TestDriveCheck()"/></td>
                       </tr>
                    <!-- <tr valign="center">
                         <td align="right">Test Drive Time:</td>
                         <td><select name="drop_StartHour" class="textbox" visible="true" >
                             <%//=mybean.PopulateStartHour()%>
                           </select>
                         hours
                         <select name="drop_StartMin" class="textbox" visible="true" >
                             <%//=mybean.PopulateStartMin()%>
                           </select>
                         minutes</td>
                       </tr> -->
                     <tr valign="center">
                         <td align="right"> Location<font color="#ff0000">*</font>: </td>
                         <td><select name="dr_location" class="selectbox" id="dr_location" >
                             <option value=0>Select Location</option>
                             <%=mybean.PopulateLocation()%>
                           </select></td>        
                       </tr>
                     <tr valign="center">
                         <td align="right">Test Drive Type<font color="#ff0000">*</font>:</td>
                         <td><%=mybean.testdrivetypename%>
                         <input type="hidden" id="dr_testdrivetype" name="dr_testdrivetype" value="<%=mybean.testdrive_type%>"/>
                           </td>
                       </tr>
                     <tr valign="center">
                         <td align="right">Confirmed:</td>
                         <td><input id="chk_testdrive_confirmed" type="checkbox" name="chk_testdrive_confirmed" <%=mybean.PopulateCheck(mybean.testdrive_confirmed) %> />
                         <%if (mybean.status.equals("Update")){%>
                         <%//if (mybean.testdrive_confirmed.equals("1")){%>
                         <a href="../sales/testdrive-update.jsp?testdrive_id=<%=mybean.testdrive_id%>&enquiry_id=<%=mybean.testdrive_enquiry_id%>&unconfirm=yes">Unconfirm Test Drive</a>
                         <%//}%>
                         <%}%></td>
                       </tr>
                     <tr valign="center">
                         <td align="right"> Notes: </td>
                         <td><textarea name="txt_testdrive_notes"  cols="70" rows="4" class="textbox" id="txt_testdrive_notes"><%=mybean.testdrive_notes %></textarea>
                         <br>
                         </td>
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
                     <% if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
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
                         <input name="add_button" type="submit" class="button" id="add_button" onclick="return SubmitFormOnce(document.formcontact, this);" value="Add Test Drive" />
                         <input type="hidden" name="add_button" value="yes">
                         <%}else if (mybean.status.equals("Update")){%>
                         <input name="update_button" type="submit" class="button" id="update_button" onclick="return SubmitFormOnce(document.formcontact, this);" value="Update Test Drive" />
                         <input type="hidden" name="update_button" value="yes">
                         <input name="delete_button" type="submit" class="button" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Test Drive" />
                         <%}%></td>
                       </tr>
                     <tr align="middle">
                         <td colspan="2" valign="center"><input type="hidden" name="testdrive_id" value="<%=mybean.testdrive_id%>">
                         <input name="enquiry_id" type="hidden" id="enquiry_id" value="<%=mybean.testdrive_enquiry_id%>"></td>
                       </tr>
                   </tbody>
                 </table></td>
                 <td width="40%" align="center" valign="top" ><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable">
                     <tr>
                     <th align="center" valign="top">Test Drive Calandar</th>
                   </tr>
                     <tr>
                     <td align="center" valign="top"><div id="calHint"><%=mybean.strHTML%></div></td>
                   </tr>
                   </table></td>
               </tr>
           </table>
           </form></TD>
       </TR>
     </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
