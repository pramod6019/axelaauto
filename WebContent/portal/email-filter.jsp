<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.portal.Email_Filter" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
    <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
	 <body  class="page-container-bg-solid page-header-menu-fixed" <%=mybean.RefreshForm%>><%@include file="header.jsp" %>
        <form name="frm"  method="get">		
		 <TABLE width="95%" border="0" align="center" cellPadding="0" cellSpacing="0">
                <TR> 
				    <TD align="center" vAlign="top" bgColor="white"><font color="#ff0000" ><b> 
                    <%=mybean.msg%></b></font></TD>
                </TR>
                <TR>
                    <TD align="center" vAlign="top" bgColor="white">&nbsp;</TD>
                </TR>
                <TR> 
                    <TD align="center" vAlign="top" bgColor="white">&nbsp;
                            <input name="btn_print" type="submit" class="button" value="Print"  />
                    </TD>
                </TR>
                <TR> 
                    <TD align="center" vAlign="top" bgColor="white"><strong><%=mybean.RecCountDisplay%></strong></TD>
                </TR>
                <TR>
                    <TD vAlign="top" bgColor="white"><a href="#" onClick="CheckAll('frm')">Select All</a>&nbsp; <a href="#" onClick="UncheckAll('frm')">Deselect All</a> </TD>
                </TR>
                <TR> 
                    <TD height="300" align="center" vAlign="top" bgColor="white"><%=mybean.StrHTML %></TD>
                </TR>
                <TR>
                    <TD vAlign="top" bgColor="white"><a href="#" onClick="CheckAll('frm')">Select All</a>&nbsp; <a href="#" onClick="UncheckAll('frm')">Deselect All</a> </TD>
                </TR>
            </TABLE>
        </form>
        
        <script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
    </body>
</HTML>