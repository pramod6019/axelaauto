<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.jobs.FCM" scope="request"/>
<%mybean.doPost(request,response); %>
<html>
<head>
<title>Insert title here</title>
</head>
<body>
<h1>FCM Test</h1>
</body>
</html>