<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.ManageJobcardPSFConcern" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="../Library/css.jsp"%>
</HEAD>
        <body   <%if(mybean.advSearch.equals(null)  || mybean.advSearch.equals("")){%>onLoad="LoadRows();FormFocus();" <%}%>leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <%@include file="../portal/header.jsp" %> <%@include file="../Library/list-body.jsp" %> <%@include file="../Library/admin-footer.jsp" %>
        	<%@include file="../Library/js.jsp"%></body>
</HTML>
