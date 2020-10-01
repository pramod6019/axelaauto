<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.app.Model_Colours_Image_List" scope="request"/>
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />

<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />

<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />

    </HEAD>
    <body   leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <%@include file="../portal/header.jsp" %>
        <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
            <TR>
                <TD>
                </TD>
            </TR>
            <TR>
                <TD  align="center" vAlign="top" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <TBODY>
                            <TR>
                                <TD  align="left" bgColor="white"><a href="../portal/home.jsp">Home</a> &gt; 
<!--                                 <a href="../portal/features-index.jsp">Feature</a> &gt;  -->
<!--                                 <a href="features.jsp">Features</a> &gt; -->
                                 <a href="model-colours-list.jsp?all=yes"> List Colours</a> &gt; 
                                 <a href="model-colours-image-list.jsp?colours_id=<%=mybean.colours_id%>"><%//=mybean.colours_title%> </a> 
                                  <a href="model-colours-image-list.jsp?all=yes">List Images</a> : </TD>
                            </TR>

                            <tr>
                            <tr>
                                <td width="30%" align="right"><a href="model-colours-image-update.jsp?add=yes&amp;model_id=<%=mybean.model_id%>">Add
                                        New Image...</a></td>
                            </tr>

                            <tr>
                                <td align="center"><font color="#ff0000" ><b> <%=mybean.msg%> </b></font></td>
                            </tr>
                            <tr>
                                <td align="center"><strong><%=mybean.RecCountDisplay%></strong></td>
                            </tr>
                            <tr>
                                <td align="center"><%=mybean.PageNaviStr%></td>
                            </tr>
                            <tr>
                                <td height="300" valign="top" align="center"><%=mybean.StrHTML%></td>
                            </tr>
                            <tr>
                                <td align="center"><%=mybean.PageNaviStr%></td>
                            </tr>
                            <tr>
                                <td align="center">&nbsp;</td>
                            </tr>
                    </table></TD>
            </TR>
        </TABLE><%@ include file="../Library/admin-footer.jsp" %>
        <script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js"></script>
        </body>
</HTML>
