<%@ page errorPage="../portal/error-page.jsp"%>
<%! String requestpage =""; %>
<% if(request.getParameter("page") != null){
	requestpage = request.getParameter("page");
}%>
<%switch(requestpage){		
	case "enquiryadd" : %> <jsp:useBean id="enquiryaddbean" class="axela.sales.Enquiry_Quickadd" scope="request" />
						<% enquiryaddbean.doGet(request, response); %>
						<%=enquiryaddbean.msg%>
						<% break;
						
	case "preownedadd" : %> <jsp:useBean id="preownedaddbean" class="axela.preowned.Preowned_Quickadd" scope="request" />
						<% preownedaddbean.doGet(request, response); %>
						<%=preownedaddbean.msg%>
						<% break;	
						
	case "servicebooking" : %> <jsp:useBean id="servicebookingbean" class="axela.service.Booking_Enquiry" scope="request" />
						<% servicebookingbean.doGet(request, response); %>
						<%=servicebookingbean.msg%>
						<% break;	
						
	case "ticketadd" : %> <jsp:useBean id="ticketaddbean" class="axela.service.Ticket_Add" scope="request" />
						<% ticketaddbean.doGet(request, response); %>
						<%=ticketaddbean.msg%>
						<% break;
						
	case "callback" : %> <jsp:useBean id="callbackbean" class="axela.inbound.Inbound_Contact_Check" scope="request" />
						<%callbackbean.doPost(request,response); %>
						<%=callbackbean.StrHTML%>						
						<% break;
					
	case "canned" : %> <jsp:useBean id="cannedbean" class="axela.inbound.Canned" scope="request" />
						<%cannedbean.doPost(request,response); %>
						<font color=red><%=cannedbean.StrHTML%></font>						
						<% break;
						
	default :  response.sendRedirect("../portal/error.jsp?msg=Invalid Request!"); 
} %>