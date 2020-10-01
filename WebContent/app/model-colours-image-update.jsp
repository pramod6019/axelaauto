<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.app.Model_Colours_Image_Update" scope="page"/>
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">   
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu-mobile.css" rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />

<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
</HEAD>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
  <TR>
    <TD  align="left" bgColor="white"><a href="../portal/home.jsp">Home</a> &gt; <a href="index.jsp">App</a> &gt; 
     <a href="model-colours-image-list.jsp?all=yes"> List Images</a> &gt;
    <a href="model-colours-image-update.jsp?update=yes&colours_id=<%=mybean.colours_id%>">Update Image</a>:</TD>
  </TR>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <TR>
    <TD align="center" vAlign="top" bgColor="white"><font color="#ff0000" ><b><%=mybean.msg%></b></font></TD>
  </TR>
  <TR>
    <TD align="center" vAlign="top" bgColor="white"><form name="frmdoc" enctype="MULTIPART/FORM-DATA" method="post">
        <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
          <tr>
            <td><table width="100%" border="0" valign="top" cellpadding="0" cellspacing="0" class="listtable">
                <tbody>
                  <tr align="center">
                            <th colspan=2><%=mybean.status%> Image</th>
                          </tr>
                  <tr>
                    <td width="25%">&nbsp;</td>
                    <td><font size="1">Form fields marked with a red asterisk <font color=#ff0000>*</font> are required. </font></td>
                  </tr>
                  <tr>
                    <td colspan="2" align="center"><%if (mybean.colours_value != null && !mybean.colours_value.equals("")) {%>
                      <img src="../Thumbnail.do?modelcoloursimg=<%=mybean.colours_value%>"> <br>
                      <%}%></td>
                  </tr>
                  <tr>
                    <td align="right">Select Image<font color="#FF0000">*</font>:&nbsp; </td>
                    <td align="left"><strong>
                      <input NAME="filename" Type="file" class="button" id="filename" value="<%=mybean.colours_value%>" size="30">
                      </strong></td>
                  </tr>
                  
                  <tr>
                    <td align="right">&nbsp;</td>
                    <td><font size="1">Click the Browse button to select the image from your computer!</font></td>
                  </tr>
                  
                  <tr>
                   <td align="right">Title<font color="#FF0000">*</font>:&nbsp; </td>
                   <td><input name="txt_img_name" type="text" class="textbox" id="txt_img_name" value="<%=mybean.colours_title%>" size="50" maxlength="255"></td>
                   </tr>
                  
                  <tr>
                    <td align="right">Allowed Formats:</td>
                    <td align="left"><b><%=mybean.ImageFormats%></b></td>
                  </tr>
                  <% if (mybean.status.equals("Update") && !(mybean.colours_entry_by == null) && !(mybean.colours_entry_by.equals(""))) {%>
                                                              <tr valign="middle">
                                                <td align="right" valign="middle">Entry By<b>:</b>&nbsp;</td>
                                                <td><%=mybean.colours_entry_by%></td>
                                            </tr>
                  <%}%>
                  <% if (mybean.status.equals("Update") && !(mybean.colours_entry_date == null) && !(mybean.colours_entry_date.equals(""))) {%>
                                                             <tr valign="middle">
                                                <td align="right" valign="middle">Entry Date<b>:</b>&nbsp;</td>
                                                <td><%=mybean.colours_entry_date%></td>
                                            </tr>
                  <%}%>
                  <% if (mybean.status.equals("Update") && !(mybean.colours_modified_by == null) && !(mybean.colours_modified_by.equals(""))) {%>
                  <                                           <tr valign="middle">
                                                <td align="right" valign="middle">Modified By<b>:</b>&nbsp;</td>
                                                <td><%=mybean.colours_modified_by%></td>
                                            </tr>
                  <%}%>
                  <% if (mybean.status.equals("Update") && !(mybean.colours_modified_date == null) && !(mybean.colours_modified_date.equals(""))) {%>
                                                              <tr valign="middle">
                                                <td align="right" valign="middle">Modified Date<b>:</b>&nbsp;</td>
                                                <td><%=mybean.colours_modified_date%></td>
                                            </tr>
                  <%}%>
                  <tr>
                    <td colspan="2" align="center">&nbsp;</td>
                  </tr>
				       <tr>
                            <td colspan="2" align="center"><strong>
                              <%if (mybean.status.equals("Add")) {%>
                              <input name="add_button" type="submit" class="button" value="Add Image"/>
                              <input type="hidden" name="add" value="yes">
                              <input type="hidden" name="colours_id" value="<%=mybean.colours_id%>">
                              <input type="hidden" name="model_id" value="<%=mybean.model_id%>">
                              <%} else if (mybean.status.equals("Update")) {%>
                              <input name="update_button" type="submit" class="button"  value="Update Image"/>
                              <input type="hidden" name="update" value="yes">
                              <input type="hidden" name="colours_id" value="<%=mybean.colours_id%>">
                              <input type="hidden" name="model_id" value="<%=mybean.model_id%>">
                              <input name="delete_button" type="submit" class="button" OnClick="return confirmdelete(this)" value="Delete Image"/>
                              <%}%>
                              </strong></td>
                          </tr>
              </table></td>
          </tr>
        </table>
      </form></TD>
  </TR>
</TABLE>
 <%@include file="../Library/admin-footer.jsp" %>
</body>
</HTML>
