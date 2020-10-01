<%
String serach = "";
serach = request.getParameter("serach");
String value = "";
value = request.getParameter("value");

if(serach.equals("1"))
response.sendRedirect("../stud/student-list.jsp?search=yes&txt_search="+value+"&dr_search=1&search_button=Search");

if(serach.equals("2"))
response.sendRedirect("../stud/student-list.jsp?search=yes&txt_search="+value+"&dr_search=2&search_button=Search");

if(serach.equals("3"))
response.sendRedirect("../stud/student-list.jsp?search=yes&txt_search="+value+"&dr_search=3&search_button=Search");

else if(serach.equals("4"))
response.sendRedirect("enquiry-list.jsp?search=yes&txt_search="+value+"&dr_search=1&search_button=Search");

else if(serach.equals("5"))
response.sendRedirect("../stud/enquiry-list.jsp?search=yes&txt_search="+value+"&dr_search=2&search_button=Search");

else if(serach.equals("6"))
response.sendRedirect("invoice-list.jsp?search=yes&txt_search="+value+"&dr_search=1&search_button=Search");

else if(serach.equals("7"))
response.sendRedirect("invoice-list.jsp?search=yes&txt_search="+value+"&dr_search=2&search_button=Search");

else if(serach.equals("8"))
response.sendRedirect("receipt-list.jsp?search=yes&txt_search="+value+"&dr_search=1&search_button=Search");

else if(serach.equals("9s"))
response.sendRedirect("receipt-list.jsp?search=yes&txt_search="+value+"&dr_search=2&search_button=Search");
%>