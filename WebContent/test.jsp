<%@ page import="java.util.*" %>
<% String path1=request.getRequestURL().toString();%>
<% String path2=request.getServerName();%>
<% String path=request.getRealPath("/");%>
    <%=path2%>