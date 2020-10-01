<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_Oppr_Month" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <HEAD>
  <title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
  <meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
  <link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
  
  <script type="text/javascript" src="../Library/Validate.js"></script>
  <script type="text/javascript" src="../Library/dynacheck.js"></script>
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
		$( "#txt_endtime" ).datepicker({
      	showButtonPanel: true,
     	dateFormat: "dd/mm/yy"
    	});
    });
</script>  
<!-- Additional plugins go here -->
<script type="text/javascript" src="../Library/amcharts/amcharts.js"></script>
<script type="text/javascript" src="../Library/amcharts/serial.js"></script>
<!-- Start Export Image additional plugins -->
<script src="../Library/amcharts/exporting/amexport.js" type="text/javascript"></script>
<script src="../Library/amcharts/exporting/rgbcolor.js" type="text/javascript"></script>
<script src="../Library/amcharts/exporting/canvg.js" type="text/javascript"></script>
<script src="../Library/amcharts/exporting/filesaver.js" type="text/javascript"></script>
<!-- End Export Image additional plugins -->
<!-- End additional plugins -->
   <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
  <script language="JavaScript" type="text/javascript">
  function PopulateTeam(){	
		var branch_id=document.getElementById("dr_branch").value;		
	    showHint('../sales/report-team-check.jsp?team=yes&exe_branch_id='+branch_id,'multiteam');		
	    }
    function ExeCheck() { //v1.0
	var branch_id=document.getElementById("dr_branch").value;
	var team_id=outputSelected(document.getElementById("dr_team").options);
	showHint('../sales/mis-check.jsp?multiple=yes&team_id='+team_id+'&exe_branch_id=' + GetReplace(branch_id),'exeHint');
    }
    </script>

  <body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
  <%@include file="../portal/header.jsp" %>
  <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
    <TR>
      <TD align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/mis.jsp">MIS</a> &gt; <a href="../sales/report-oppr-month.jsp">Opportunity By Month</a>:</TD>
    </TR>
          <tr>
          <td align="center"><font color="red"><b><%=mybean.msg%></b></font><br></td>
        </tr>
          <tr>
          <td align="center"><form method="post" name="frm1"  id="frm1" >
              <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
                <tr>
                  <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable" >
                      <tr>
                        <th colspan="7" align="center">Opportunity By Month</th>
                      </tr>
                      <tr>
                        
                        <td align="right" valign="top">Branch:</td>
                        <td align="left" valign="top"><%if(mybean.branch_id.equals("0")){%>
                        <select name="dr_branch" id="dr_branch" class="selectbox" onChange="ExeCheck();PopulateTeam();">
                            <%=mybean.PopulateBranch(mybean.dr_branch_id, "all", "1,2", "", request)%>
                          </select>
                        <%}else{%>
                        <input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>" />
                        <%=mybean.getBranchName(mybean.dr_branch_id)%>
                        <%}%></td>
                        <td align="right" valign="top" nowrap>Year:</td>
                        <td align="left" valign="top">
                        <select name="dr_year" class="selectbox" id="dr_year" style="width:150px">
                           <%=mybean.PopulateYears()%>  
                        </select>
                        </td>
                        
                        
                        <td align="right" valign="top" nowrap id="date">Month:</td>
                        <td valign="top">
                       <span id="month" style="float:left"> <select name="dr_month" id="dr_month" class="selectbox" >
                            	<%=mybean.PopulateMonths()%> 
                            </select> &nbsp;
                            </span>
                        </td>
                        
                        <td align="center" valign="top" nowrap>
                          <input name="submit_button" type="submit" class="button" id="submit_button" value="Go" />
                          <input type="hidden" name="submit_button" value="Submit"></td>
                      </tr>
                      <tr>
                        <td align="right" valign="top">Teams:</td>
                        <td align="left" valign="top"><div id="multiteam" >
                        <select name="dr_team" size="10" multiple="multiple" class="textbox" id="dr_team" onChange="ExeCheck();" style="width:250px">
                            <%=mybean.PopulateTeam()%> 
                          </select></div></td>
                        <td align="right" valign="top">Sales Consultant:</td>
                        <td valign="top"><span id="exeHint">
                        <%=mybean.PopulateSalesExecutives()%>
                       </span></td>
                        <td align="right" valign="top">Model:</td>
                  <td valign="top" colspan="2"><select name="dr_model" size="10" multiple="multiple" class="selectbox" id="dr_model" style="width:250px">
                            <%=mybean.PopulateModel()%>
                          </select></td>
                      </tr>
                      
                        <tr>
                        <td align="right" valign="top">SOE:</td>
                        <td align="left" valign="top"><div id="multisoe" ><select name="dr_soe" size="10" multiple="multiple" class="textbox" id="dr_soe"  style="width:250px">
                            <%=mybean.PopulateSOE()%>
                          </select></div></td>
                           <td align="right" valign="top">SOB:</td>
                          <td align="left" valign="top"><div id="multisob" ><select name="dr_sob" size="10" multiple="multiple" class="textbox" id="dr_sob"  style="width:250px">
                            <%=mybean.PopulateSOB()%>
                          </select></div></td>
                       <td>&nbsp;&nbsp;&nbsp;</td>
                       <td>&nbsp;&nbsp;&nbsp;</td>
                       <td>&nbsp;&nbsp;&nbsp;</td>
                      </tr>
                    </table></td>
                </tr>
              </table>
            </form></td>
        </tr>
         <tr align="center">
          <td  align="center">&nbsp;</td>
        </tr>
        <tr align="center">
          <td align="center" valign="top" height="200"><%=mybean.StrHTML%></td> 
        </tr>
          <tr align="center">
          <td  align="center">&nbsp;</td>
        </tr>
       </TD>
    </TR>
  </TABLE> <%@ include file="../Library/admin-footer.jsp" %></body>
</HTML>
