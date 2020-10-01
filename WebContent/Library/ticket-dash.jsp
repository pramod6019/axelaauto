<table width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<TD><a href="../portal/home.jsp">Home</a> &gt; <a
			href="../service/index.jsp">Service</a> &gt; <a
			href="../service/ticket.jsp">Ticket</a> &gt; <a
			href="../service/ticket-list.jsp?all=yes">List Tickets</a> &gt; <a
			href="../service/ticket-dash.jsp?ticket_id=<%=mybean.ticket_id%>"><%=mybean.ticket_subject%>
				(<%=mybean.ticket_id%>)</a>:</TD>
	</tr>
	<tr>
		<TD>
			<ul id="tabnav">
				<li
					<% if(request.getServletPath().equals("/service/ticket-dash.jsp")){%>
					class="selecttab" <% } %>><a
					href="../service/ticket-dash.jsp?ticket_id=<%=mybean.ticket_id%>">Ticket
						Details</a></li>
				<li
					<% if(request.getServletPath().equals("/service/ticket-dash-followup.jsp")) {%>
					class="selecttab" <% } %>><a
					href="../service/ticket-dash-followup.jsp?ticket_id=<%=mybean.ticket_id%>">Follow-up</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/service/ticket-dash-account.jsp")){%>
					class="selecttab" <% } %>><a
					href="../service/ticket-dash-account.jsp?ticket_id=<%=mybean.ticket_id%>">Account</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/service/ticket-dash-attachment.jsp")){%>
					class="selecttab" <% } %>><a
					href="../service/ticket-dash-attachment.jsp?ticket_id=<%=mybean.ticket_id%>">Documents</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/service/ticket-dash-history.jsp")){%>
					class="selecttab" <% } %>><a
					href="../service/ticket-dash-history.jsp?ticket_id=<%=mybean.ticket_id%>">History</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/service/ticket-dash-summary.jsp")){%>
					class="selecttab" <% } %>><a
					href="../service/ticket-dash-summary.jsp?ticket_id=<%=mybean.ticket_id%>">Summary</a>
				</li>
			</ul>
		</TD>
	</tr>
</table>
