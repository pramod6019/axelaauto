
<%
Cookie[] cookies = request.getCookies();
for(int i=0; i<cookies.length; i++) {
	if(cookies[i].getName().equals("uuid") || cookies[i].getName().equals("check")|| cookies[i].getName().equals("comp_id")) {
		cookies[i].setMaxAge(0);
		cookies[i].setValue(null);
		response.addCookie(cookies[i]);
	}
}
session.invalidate();
response.sendRedirect("../portal/index.jsp?msg=You have been signed out successfully!");

	
	
%>


