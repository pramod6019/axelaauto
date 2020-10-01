<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_JobCard_ManHours" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     

<script type="text/javascript" src="../Library/Validate.js"></script><script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script><script type="text/javascript" src="../Library/jquery.js"></script><script type="text/javascript" src="../Library/jquery-ui.js"></script><script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
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
    
<script language="JavaScript" type="text/javascript">
  
    function ExeTechnicianCheck() { //v1.0
	var branch_id=document.getElementById("dr_branch").value;
	showHint('../service/report-check.jsp?multiple=yes&technician=yes&branch_id=' + GetReplace(branch_id),'technicianHint');
    }
	</script>
	<script language="JavaScript" type="text/javascript">
 $(function() {
  $( "#txt_starttime" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
	 $( "#txt_endtime" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });       
  });
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0" >
<%@include file="../portal/header.jsp" %>
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
  <TR>
    <TD align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/mis.jsp">MIS</a> &gt; <a href="report-jobcard-manhours.jsp">Job Card Man Hours</a>:</TD>
  </TR>
  <tr>
    <td align="center"><font color="red"><b> <%=mybean.msg%> </b></font><br/></td>
  </tr>
  <tr align="center">
    <td colspan="2" align="center" valign="middle"><form method="post" name="frm1"  id="frm1">
        <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
          <tr>
            <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
                <tbody>
                  <tr align="center">
                    <th colspan="7"><b>Job Card Man Hours</b></th>
                  </tr>
                  <tr valign="left">
                    <td align="center"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable">
                        <tr>
                          <td align="right" valign="top">Branch<font color="#ff0000">*</font>:</td>
                          <td align="left" valign="top"><%if(mybean.branch_id.equals("0")){%>
                            <select name="dr_branch" class="selectbox" id="dr_branch" onChange="ExeTechnicianCheck();">
                              <%=mybean.PopulateBranch(mybean.dr_branch_id,"", "1,3", request)%>
                            </select>
                            <%}else{%>
                            <input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>" />
                            <%=mybean.getBranchName(mybean.dr_branch_id, mybean.comp_id)%>
                          <%}%></td>
                           
                          
                           <td align="right" valign="top" nowrap>Start Date<font color=red>*</font>:</td>
                        <td align="left" valign="top"><input name ="txt_starttime" id="txt_starttime" type="text" class="textbox" value="<%=mybean.start_time %>" size="12" maxlength="10" /></td>
                        
                      <td align="right" valign="top" nowrap>End Date<font color=red>*</font>:</td>
                           <td align="left" valign="top"><input name ="txt_endtime" id ="txt_endtime" type="text" class="textbox" value="<%=mybean.end_time %>" size="12" maxlength="10"/></td>
                            <td align="center" valign="top"><input name="submit_button" type="submit" class="button" id="submit_button" value="Go" />
                            <input type="hidden" name="submit_button" value="Submit"/></td>
                         
                        </tr>
                        <tr>
                         
                            <td align="right" valign="top">Technician:</td>
                        <td align="left" valign="top" colspan="6"><span id="technicianHint"><%=mybean.reportexe.PopulateTechnicians(mybean.dr_branch_id, mybean.technicianexe_ids, mybean.ExeAccess)%></span></td>
                        </tr>
                    </table></td>
                  </tr>
                </tbody>
              </table></td>
          </tr>
        </table>
      </form></td>
  </tr>
  <tr align="center">
    <td colspan="2" align="center" valign="middle">&nbsp;</td>
  </tr>
  <tr align="center">
    <td height="300" colspan="2" align="center" valign="top"><%=mybean.StrHTML%></td>
  </tr>
  <tr align="center">
    <td colspan="2" align="center" valign="middle">&nbsp;</td>
  </tr>
  <tr align="center">
    <td colspan="2" align="center" valign="middle"></td>
  </tr>
</TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</html>