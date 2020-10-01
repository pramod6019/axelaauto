<table width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<TD><a href="../portal/home.jsp">Home</a> &gt; <a
			href="../service/index.jsp">Service</a> &gt; <a
			href="../service/jobcard.jsp">Job Card</a> &gt; <a
			href="../service/jobcard-list.jsp?all=recent">List Job Cards</a> &gt;
			<a href="../service/jobcard-dash.jsp?jc_id=<%=mybean.jc_id%>"><%=mybean.jc_title%>
				(<%=mybean.jc_id%>)</a>:</TD>
	</tr>
	<tr>
		<TD>
			<ul id="tabnav">
				<li
					<% if(request.getServletPath().equals("/service/jobcard-dash.jsp")){%>
					class="selecttab" <% } %>><a
					href="../service/jobcard-dash.jsp?jc_id=<%=mybean.jc_id%>">Job
						Card</a></li>
				<li
					<% if(request.getServletPath().equals("/service/jobcard-dash-account.jsp")) {%>
					class="selecttab" <% } %>><a
					href="../service/jobcard-dash-account.jsp?jc_id=<%=mybean.jc_id%>">Account</a>
				</li>
				<li
					<% if((request.getServletPath() + "?" + request.getQueryString()).equals("/service/jobcard-dash-items.jsp?jc_id="+mybean.jc_id)){%>
					class="selecttab" <% } %>><a
					href="../service/jobcard-dash-items.jsp?jc_id=<%=mybean.jc_id%>">Parts</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/service/jobcard-dash-checklist.jsp")){%>
					class="selecttab" <% } %>><a
					href="../service/jobcard-dash-checklist.jsp?jc_id=<%=mybean.jc_id%>">Check
						List</a></li>
				<li
					<% if(request.getServletPath().equals("/service/jobcard-dash-inventory.jsp")){%>
					class="selecttab" <% } %>><a
					href="../service/jobcard-dash-inventory.jsp?jc_id=<%=mybean.jc_id%>">Car
						Inventory</a></li>
				<li
					<% if(request.getServletPath().equals("/service/jobcard-dash-image.jsp")){%>
					class="selecttab" <% } %>><a
					href="../service/jobcard-dash-image.jsp?jc_id=<%=mybean.jc_id%>">Images</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/service/jobcard-dash-docs.jsp")){%>
					class="selecttab" <% } %>><a
					href="../service/jobcard-dash-docs.jsp?jc_id=<%=mybean.jc_id%>">Documents</a>
				</li>
				<li
					<% if((request.getServletPath() + "?" + request.getQueryString()).equals("/service/jobcard-dash-manhours.jsp?jc_id="+mybean.jc_id)){%>
					class="selecttab" <% } %>><a
					href="../service/jobcard-dash-manhours.jsp?jc_id=<%=mybean.jc_id%>">Man
						Hours</a></li>
				<li
					<% if(request.getServletPath().equals("/service/jobcard-dash-history.jsp")){%>
					class="selecttab" <% } %>><a
					href="../service/jobcard-dash-history.jsp?jc_id=<%=mybean.jc_id%>">History</a>
				</li>

			</ul>
		</TD>
	</tr>
</table>
