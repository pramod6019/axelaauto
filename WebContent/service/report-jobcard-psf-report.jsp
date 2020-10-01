<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_JobCard_PSF_Report" scope="request"/>
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

<script language="JavaScript" type="text/javascript">
	 function ExeCheck() {
		 //alert("yudsd");
	var branch_id=document.getElementById("dr_branch").value;
	showHint('../service/report-check.jsp?jcpsfexecutive=yes&branch_id=' + GetReplace(branch_id),'exeHint');
    }
	
	function PopulatePSFDays() {
		//alert("---");
	var branch_id=document.getElementById("dr_branch").value;
	showHint('../service/report-check.jsp?psfdays=yes&branch_id=' + GetReplace(branch_id),'psfdaysHint');
    }
	</script><script language="JavaScript" type="text/javascript">
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
    <TD align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/mis.jsp">MIS</a> &gt; <a href="report-jobcard-psf-report.jsp">PSF Report</a>:</TD>
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
                    <th colspan="9"><b>PSF Report</b></th>
                  </tr>
                  <tr valign="left">
                    <td align="center"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable">
                        <tr>
                          <td align="right">Branch<font color="#ff0000">*</font>:</td>
                          <td align="left"><%if(mybean.branch_id.equals("0")){%>
                            <select name="dr_branch" class="selectbox" id="dr_branch" onChange="ExeCheck();PopulatePSFDays();">
                              <%=mybean.PopulateBranch(mybean.dr_branch_id,"", "1,3", "", request)%>
                            </select>
                            <%}else{%>
                            <input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>" />
                            <%=mybean.getBranchName(mybean.dr_branch_id, mybean.comp_id)%>
                            <%}%></td>
                          
                           <td align="right" nowrap>Month<font color=red>*</font>:</td>
                        <td><select name="dr_startmonth" class="selectbox" id="dr_startmonth">
                      <%=mybean.PopulateStartMonth() %> 
                    </select></td>
                        
                     
                          <td align="center"><input name="submit_button" type="submit" class="button" id="submit_button" value="Go" />
                            <input type="hidden" name="submit_button" value="Submit"/></td>
                         
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