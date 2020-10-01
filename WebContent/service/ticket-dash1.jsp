<%-- 
    Document : ticket-add
    Created on: Feb 11, 2013
    Author   : Gurumurthy TS
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Ticket_Dash" scope="request"/>
<%mybean.doPost(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
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
		<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />


	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script>
 $(function() {
	 $( "#txt_report_time" ).datetimepicker({
      dateFormat: "dd/mm/yy"
    });     
  });
</script>
<script language="JavaScript" type="text/javascript">
		function FormFocus()
        { //v1.0
	  //document.form1.txt_ticket_subject.focus();          
	}
	function SecurityCheck123(name,obj,hint)
          {
			 
			  var status = document.getElementById("dr_ticket_ticketstatus_id").value;
			 //alert("status--"+status);			   
			  var comment = document.getElementById("txt_comments").value;
			 //alert("comment--"+comment);						 
            var checked='';
            var ticket_id = GetReplacePost(document.form1.ticket_id.value);		
			 		//alert("ticket_id--"+ticket_id);
            var url = "ticket-dash-historycheck.jsp?" ;
		var param="name=" + name + "&status="+ status +"&checked="+checked+"&comment="+comment+"&ticket_id=" + ticket_id;
            var str = "123";
             showHintPost(url+param, str, param, hint);
             setTimeout('RefreshHistory()', 1000);
			
		  }
	
	function SecurityCheck(name,obj,hint)
          {			 
            var value = GetReplacePost(obj.value);		
            var checked='';
            var ticket_id = GetReplacePost(document.form1.ticket_id.value);
            var url = "ticket-dash-historycheck.jsp?" ;
		var param="name=" + name + "&value="+ value +"&checked="+checked+"&ticket_id=" + ticket_id;
            var str = "123";
             showHintPost(url+param, str, param, hint);
			  if(name=='dr_ticket_dept_id' || name=='txt_report_time')
			 {
				 setTimeout('populateduuedate()',1000);
			 }
        } 
		
		function populateduuedate()
		{
			
			var duedate = document.getElementById("duedate").value;
		    document.getElementById("span_duedate").innerHTML = duedate;
		}
		
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %> <%@include file="../Library/ticket-dash.jsp" %><table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" valign="top"><font color="#ff0000" ><b><span id="history_span"><%=mybean.msg%></span></b></font></td>
  </tr>
  <tr>
    <td align="right" valign="top"><form name="form1" id="form1" method="post">
        <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
          <tr>
            <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
                <tr align="center">
                  <th colspan="2">&nbsp;Ticket Details</th>
                </tr>
                <tr>
                  <td width="60%" valign="top"><table width="100%"  border="0" cellpadding="0" cellspacing="0" class="listtable">
                      <%if(!mybean.ticket_contact_id.equals("") && !mybean.ticket_contact_id.equals("0")) {%>
                      <tr>
                        <td align="right" valign="top">Contact:</td>
                        <td valign="top"><%=mybean.client%>
                          <input type="hidden" name="contact_id" id="contact_id" value="<%=mybean.ticket_contact_id%>"/></td>
                      </tr>
                      <tr>
                        <td align="right" valign="top">Customer:</td>
                        <td valign="top"><%=mybean.client1%>
                          <input type="hidden" name="customer_id" id="customer_id" value="<%=mybean.ticket_customer_id%>"/></td>
                      </tr>
                      <%}%>
                      <% if(!mybean.veh_id.equals("0")) {%>
               			 <tr valign="center">
                 		 <td align="right" valign="top" >Vehicle ID<font color="#ff0000">*</font>:</td>
                 		 <td align="leftS" valign="top" ><b><a href="../service/vehicle-list.jsp?veh_id=<%=mybean.veh_id%>"><%=mybean.veh_id%></a> </b></td>
                </tr>
                 <%}%>
                      <% if(!mybean.jc_id.equals("0")) {%>
               			 <tr valign="center">
                 		 <td align="right" valign="top" >JC ID<font color="#ff0000">*</font>:</td>
                 		 <td align="leftS" valign="top" ><b><a href="../service/jobcard-list.jsp?jc_id=<%=mybean.jc_id%>"><%=mybean.jc_id%></a> </b></td>
                </tr>
                 <%}%>
                      <tr>
                        <td width="25%" align="right" valign="top">Subject<font color="#ff0000">*</font>:</td>
                        <td width="75%" valign="top"><input name ="txt_ticket_subject" type="text" class="textbox" value="<%=mybean.ticket_subject %>" size="50" maxlength="255" onChange="SecurityCheck('txt_ticket_subject',this,'hint_txt_ticket_subject');">
                          &nbsp;<a href="../service/kb.jsp?1_dr_field=0-text&1_dr_param=0-text&1_txt_value_1=<%=mybean.ticket_subject %>&1_dr_filter=and&advsearch_button=Search&dr_searchcount=1&dr_searchcount_var=1" target="_blank">KB</a>&nbsp;<a href="ticket-faq-list.jsp?1_dr_field=0-text&1_dr_param=0-text&1_txt_value_1=<%=mybean.ticket_subject %>&1_dr_filter=and&advsearch_button=Search&dr_searchcount=1&dr_searchcount_var=1" target="_blank">FAQ</a><br>
                          <font color="red">
                          <div class="hint" id="hint_txt_ticket_subject"></div>
                          </font></td>
                      </tr>
                      <tr>
                        <td align="right" valign="middle">Description<font color="#ff0000">*</font>:</td>
                        <td colspan="3" valign="top"><textarea name="txt_ticket_desc" cols="57" rows="7" class="textbox" id="txt_ticket_desc" 
                                    onChange="SecurityCheck('txt_ticket_desc',this,'hint_txt_ticket_desc');"><%=mybean.ticket_desc%></textarea>
                          <font color="red">
                          <div class="hint" id="hint_txt_ticket_desc"></div>
                          </font></td>
                      </tr>
                      <tr valign="middle">
                        <td align="right" valign="middle">Solution / Closing Summary<font color="#ff0000">*</font><b></b>:</td>
                        <td><textarea name="txt_comments" cols="57" rows="5" class="textbox" id="txt_comments" onChange="SecurityCheck123('txt_comments',this,'hint_txt_comments');" onKeyUp="charcount('txt_comments', 'span_txt_comments','<font color=red>({CHAR} characters left)</font>', '2000');"><%=mybean.ticket_closed_comments%></textarea>
                          <font color="red">
                          <div class="hint" id="hint_txt_comments"></div>
                          </font></td>
                      </tr>
                      <tr valign="middle">
                        <td align="right">Entry By: </td>
                        <td align="left"><%=mybean.entry_id %></td>
                      </tr>
                      <tr valign="middle">
                        <td align="right">Entry Time: </td>
                        <td align="left"><%=mybean.entry_date %></td>
                      </tr>
                    </table></td>
                  <td width="40%" valign="top"><table width="100%"  border="0" cellpadding="0" cellspacing="0" class="listtable">
                      <tr>
                        <td width="30%" align="right" valign="top">Ticket ID: </td>
                        <td valign="top"><b> <%=mybean.ticket_id%></b>
                          <input name="ticket_id" type="hidden" id="ticket_id" value="<%=mybean.ticket_id%>">
                          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                          <%if(mybean.ticket_customer_id.equals("0") && mybean.ticketParentId.equals("0")) {%>
                          <a href="ticket-add.jsp?add=yes&ticket_parent_id=<%=mybean.ticket_id%>">New Child Ticket</a>
                          <%}else if(mybean.ticketParentId.equals("0")) {%>
                          <a href="ticket-add.jsp?add=yes&ticket_parent_id=<%=mybean.ticket_id%>">New Child Ticket</a>
                          <%}%></td>
                      </tr>
                      <% if(!mybean.childTickets.equals("")) {%>
                      <tr>
                        <td align="right" valign="top">Child Tickets:</td>
                        <td valign="top"><%=mybean.childTickets%></td>
                      </tr>
                      <%}%>
                      <%if(!mybean.ticket_parent_id.equals("")  && !mybean.ticket_parent_id.equals("0")) {%>
                      <tr>
                        <td width="30%" align="right" valign="top">Parent Ticket: </td>
                        <td valign="top"><b><a href="ticket-dash.jsp?ticket_id=<%=mybean.ticket_parent_id%>"><%=mybean.ticket_parent_id%></a></b></td>
                      </tr>
                      <%}%>
                      <tr>
                        <td align="right" valign="top">Report Time<font color="#ff0000">*</font>:</td>
                        <td valign="top"><input  name="txt_report_time" type="text" class="textbox" id="txt_report_time" value="<%=mybean.reporttime%>" size="18" maxlength="14" onChange="SecurityCheck('txt_report_time',this,'hint_txt_report_time');">
                          <font color="red">
                          <div class="hint" id="hint_txt_report_time"></div>
                          </font></td>
                      </tr>
                      <tr>
                        <td align="right" valign="top">Source<font color="#ff0000">*</font>: </td>
                        <td valign="top"><select name="dr_ticket_ticketsource_id" class="selectbox" onChange="SecurityCheck('dr_ticket_ticketsource_id',this,'hint_dr_ticket_ticketsource_id');" >
                            <%=mybean.PopulateSourceType() %>
                          </select>
                          <font color="red">
                          <div class="hint" id="hint_dr_ticket_ticketsource_id"></div>
                          </font></td>
                      </tr>
                      <%if(mybean.config_service_ticket_cat.equals("1")) {%>
                      <tr>
                        <td align="right" valign="top">Category:</td>
                        <td valign="top"><select name="dr_ticket_ticketcat_id" class="selectbox" id="dr_ticket_ticketcat_id" onChange="SecurityCheck('dr_ticket_ticketcat_id',this,'hint_dr_ticket_ticketcat_id');">
                            <%=mybean.PopulateTicketCategory()%>
                          </select>
                         
                          <font color="red">
                          <div class="hint" id="hint_dr_ticket_ticketcat_id"></div>
                          </font></td>
                      </tr>
                      <%}%>
                      <%if(mybean.config_service_ticket_type.equals("1")) {%>
                      <tr>
                        <td width="30%" align="right" valign="top">Type:</td>
                        <td width="68%" valign="top"><select name="dr_ticket_tickettype_id" class="selectbox" id="dr_ticket_tickettype_id" onChange="SecurityCheck('dr_ticket_tickettype_id',this,'hint_dr_ticket_tickettype_id');">
                            <%=mybean.PopulateTicketType()%>
                          </select>
                          
                          <font color="red">
                          <div class="hint" id="hint_dr_ticket_tickettype_id"></div>
                          </font></td>
                      </tr>
                      <%}%>
                      <tr>
                        <td align="right" valign="top">Status<font color="#ff0000">*</font>:</td>
                        <td valign="top"><select name="dr_ticket_ticketstatus_id" id="dr_ticket_ticketstatus_id" class="selectbox" onChange="SecurityCheck123('dr_ticket_ticketstatus_id',this,'hint_dr_ticket_ticketstatus_id');">
                            <%=mybean.PopulateStatus()%>
                          </select>
                          
                          <font color="red">
                          <div class="hint" id="hint_dr_ticket_ticketstatus_id"></div>
                          </font></td>
                      </tr>
                      <tr>
                        <td align="right" valign="top">Priority<font color="#ff0000">*</font>:</td>
                        <td valign="top"><select name="dr_ticket_priorityticket_id" class="selectbox" id="dr_ticket_priorityticket_id" onChange="SecurityCheck('dr_ticket_priorityticket_id',this,'hint_dr_ticket_priorityticket_id');" >
                            <%=mybean.PopulateTicketPrioirty()%>
                          </select>
                          <font color="red">
                          <div class="hint" id="hint_dr_ticket_priorityticket_id"></div>
                          </font></td>
                      </tr>
                      <tr>
                        <td align="right" valign="top">Department<font color="#ff0000">*</font>:</td>
                        <td valign="top"><select name="dr_ticket_dept_id" class="selectbox" id="dr_ticket_dept_id" onChange="SecurityCheck('dr_ticket_dept_id',this,'hint_dr_ticket_dept_id');" >
                            <%=mybean.PopulateDepartment()%>
                          </select>
                          
                          <font color="red">
                          <div class="hint" id="hint_dr_ticket_dept_id"></div>
                          </font></td>
                      </tr>
                      <tr>
                        <td align="right" valign="top">Executive<font color="#ff0000">*</font>:</td>
                        <td valign="top"><select name="dr_ticket_emp_id" class="selectbox" id="dr_ticket_emp_id" onChange="SecurityCheck('dr_ticket_emp_id',this,'hint_dr_ticket_emp_id');" >
                            <%//if(!mybean.ticket_contact_id.equals("") && !mybean.ticket_contact_id.equals("0")) {%>
                            <%//=mybean.PopulateExecutive()%>
                            <%//} else {%>
                            <%=mybean.PopulateAllExecutive()%>
                            <%//}%>
                          </select>
                          
                          <font color="red">
                          <div class="hint" id="hint_dr_ticket_emp_id"></div>
                          </font></td>
                      </tr>
                      <tr>
                        <td align="right" valign="top">Due Date: </td>
                        <td valign="top"><span id="span_duedate"><%=mybean.duedate%></span>
                         </td>
                      </tr>
                      <tr>
                        <td align="right" valign="top">&nbsp;</td>
                        <td valign="top"><a href="ticket-list.jsp?previous_ticket_id=<%=mybean.ticket_id%>">Previous Tickets</a></td>
                      </tr>
                    </table></td>
                </tr>
              </table></td>
          </tr>
        </table>
      </form></td>
  </tr>
</table> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
