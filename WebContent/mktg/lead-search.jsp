<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.mktg.Lead_Search" scope="request"/>
<%mybean.doPost(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.ClientName%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu-mobile.css" rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/jquery.js"></script>
<script type="text/javascript" src="../Library/jquery-ui.js"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script type="text/javascript" src="../Library/smart.js"></script>
</HEAD>
<body    leftmargin="0" rightmargin="0" topmargin="0" bottommargin= "0" <%if (mybean.advSearch.equals(null) || mybean.advSearch.equals("")) {%>onLoad="LoadRows();FormFocus();" <%}%> <%=mybean.RefreshForm%>>
<%@include file="../portal/header.jsp" %>
<form name="frm" method="post">
  <TABLE width="98%" cellSpacing="0" cellPadding="0"  border="0" align="center">
    <TBODY>
      <TR>
        <TD><a href="../portal/home.jsp">Home</a> &gt;&nbsp; <a href="../sales/index.jsp">Marketing</a> &gt; <a href="../Mktg/lead.jsp">Leads</a> &gt;&nbsp;<a href="../Mktg/lead-search.jsp">Search Leads</a>:</TD>
      </TR>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <TR>
        <TD align="center" vAlign="top"><font color="#ff0000"><b><%=mybean.msg%> </b></font></TD>
      </TR>
      <TR>
        <TD align="center" valign="top"><table border="1" cellpadding="0" cellspacing="0" class="listtable">
            <tr valign="top">
              <th colspan="4"><%=mybean.Str_Link%></th>
            </tr>
            <%if (mybean.branch_count > 1) {%>
            <tr valign="top">
              <td width="25%"><select name="dr_branch_list" class="selectbox" id="dr_branch_list">
                  <%=mybean.PopulateList(request, "dr_branch_list")%>
                </select>
                &nbsp;Branch</td>
              <td width="25%"><select name="dr_branchtype_list" class="selectbox" id="dr_branchtype_list">
                  <%=mybean.PopulateList(request, "dr_branchtype_list")%>
                </select>
                &nbsp;Branch Type</td>
              <td width="25%"><%if (mybean.franchisee_count > 1) {%>
                <select name="dr_franchisee_list" class="selectbox" id="dr_franchisee_list">
                  <%=mybean.PopulateList(request, "dr_franchisee_list")%>
                </select>
                &nbsp;Franchisee
                <%}%>
                &nbsp;</td>
              <td width="25%"><%if (mybean.franchisee_count > 1) {%>
                <select name="dr_franchiseetype_list" class="selectbox" id="dr_franchiseetype_list">
                  <%=mybean.PopulateList(request, "dr_franchiseetype_list")%>
                </select>
                &nbsp;Franchisee Type
                <%}%>
                &nbsp;</td>
            </tr>
            <tr valign="top">
              <td align="left"><select name="dr_branch" class="textbox" size="12" multiple="multiple"  style="width:220px;">
                  <%=mybean.PopulateBranch(request)%>
                </select></td>
              <td align="left"><select name="dr_branchtype" class="textbox" size="12" multiple="multiple"  style="width:220px;">
                  <%=mybean.PopulateBranchType(request)%>
                </select></td>
              <td align="left"><%if (mybean.franchisee_count > 1) {%>
                <select name="dr_franchisee" class="textbox" size="12" multiple="multiple" style="width:220px;">
                  <%=mybean.PopulateFranchisee(request)%>
                </select>
                <%}%>
                </td>
              <td align="left"><%if (mybean.franchisee_count > 1) {%>
                <select name="dr_franchiseetype" class="textbox" size="12" multiple="multiple"  style="width:220px;">
                  <%=mybean.PopulateFranchiseeType(request)%>
                </select>
                <%}%>
                </td>
            </tr>
            <%}%>
            <tr valign="top">
              <td ><select name="dr_title_list" class="selectbox" id="dr_title_list">
                  <%=mybean.PopulateList(request, "dr_title_list")%>
                </select>
                &nbsp;Title</td>
              <td><select name="dr_employee_list" class="selectbox" id="dr_employee_list">
                  <%=mybean.PopulateList(request, "dr_employee_list")%>
                </select>
                &nbsp;Employee Count</td>
              
              <td>
              <% if (mybean.config_sales_soe.equals("1")) {%>
              <select name="dr_soe_list" class="selectbox" id="dr_soe_list">
                  <%=mybean.PopulateList(request, "dr_soe_list")%>
                </select>
                Source Of Enquiry
                <%}%>
                </td>  
              <td>
               <%if (mybean.config_sales_sob.equals("1")) {%>
              <select name="dr_sob_list" class="selectbox" id="dr_sob_list">
                  <%=mybean.PopulateList(request, "dr_sob_list")%>
                </select>
                &nbsp;Source Of Business
                 <%}%>
                </td>             
            </tr>
            <tr valign="top">
              <td align="left"><select name="dr_title" class="textbox" size="12" multiple="multiple"  style="width:220px;">
                  <%=mybean.PopulateTitle(request)%>
                </select></td>
              <td align="left"><select name="dr_employee" class="textbox" size="12" multiple="multiple" style="width:220px;">
                  <%=mybean.PopulateEmployeeCount(request)%>
                </select></td>
              <td align="left"><%if(mybean.config_sales_soe.equals("1")) {%>
                <select name="dr_soe" class="textbox" size="12" multiple="multiple" style="width:220px;">
                  <%=mybean.PopulateSOE(request)%>
                </select>
                <%}%></td>
              <td align="left"><% if (mybean.config_sales_sob.equals("1")) {%>
                <select name="dr_sob" class="textbox" size="12" multiple="multiple"  style="width:220px;">
                  <%=mybean.PopulateSOB(request)%>
                </select>
                <%}%></td>
            </tr>
            <tr valign="top">
              <td width="25%">
              <%if(mybean.empcount > 1){%>
              <select name="dr_executive_list" class="selectbox" id="dr_executive_list">
                  <%=mybean.PopulateList(request, "dr_executive_list")%>
                </select>
                &nbsp;Executive<%}%></td>
              <td><select name="dr_entryby_list" class="selectbox">
                  <%=mybean.PopulateList(request, "dr_entryby_list")%>
                </select>
                &nbsp;Entry By</td>
              <td><select name="dr_modifiedby_list" class="selectbox">
                  <%=mybean.PopulateList(request, "dr_modifiedby_list")%>
                </select>
                Modified By</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td align="left">
              <%if(mybean.empcount > 1){%>
              <select name="dr_executive" class="textbox" size="12" multiple="multiple"  style="width:220px;">
                  <%=mybean.PopulateExecutive(request)%>
                </select>
                <%}%>
                </td>
              <td align="left"><select name="dr_entryby" size="12" multiple="multiple" class="textbox" id="dr_entryby" style="width:220px;">
                  <%=mybean.PopulateEntryBy(request)%>
                </select></td>
              <td align="left"><select name="dr_modifiedby" size="12" multiple="multiple" class="textbox" id="dr_modifiedby" style="width:220px;">
                  <%=mybean.PopulateModifiedBy(request)%>
                </select></td>
              <td valign="top">&nbsp;</td>
            </tr>
            <tr valign="top">
              <td colspan="4"><%@ include file="../Library/smart-search.jsp" %></td>
            </tr>
          </table></TD>
      </TR>
      <TR>
        <TD align="center" valign="top" bgColor="white">&nbsp;</TD>
      </TR>
    </TBODY>
  </TABLE>
</form><%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
