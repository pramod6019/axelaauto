<%@ page errorPage="../portal/error-page.jsp"%>
<%response.setHeader("Access-Control-Allow-Origin","*"); %>
<%! String requestpage =""; %>
<% if(request.getParameter("page") != null){
	requestpage = request.getParameter("page");
}%>
<%switch(requestpage){		
	case "bookingadd" : %> <jsp:useBean id="servicemybean" class="axela.service.Booking_Enquiry" scope="request" />
						<% servicemybean.doGet(request, response); %>
						<%=servicemybean.msg%>
						<% break;
	default :  response.sendRedirect("../portal/error.jsp?msg=Invalid Request!"); 
					} %>