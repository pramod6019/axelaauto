<%@page import="com.mysql.jdbc.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <jsp:useBean id="mybean" class="test.Check" scope="request"></jsp:useBean>
    <%mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<%if(mybean.i == 1){ %>
		<%response.sendRedirect("message.jsp"); %>
	<%}else{ %>
		<h3>Not updated..</h3>
	<%} %>
</body>
</html>