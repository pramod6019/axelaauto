<table width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<TD><a href="../portal/home.jsp">Home</a> &gt; <a
			href="../sales/index.jsp">Sales</a> &gt; <a
			href="../sales/veh-salesorder.jsp">Sales Orders</a> &gt; <a
			href="../sales/veh-salesorder-list.jsp?all=yes">List Sales Orders</a>
			&gt; <a
			href="../sales/salesorder-dash.jsp?pop=yes&so_id=<%=mybean.so_id%>"><%=mybean.so_desc%>
				(<%=mybean.so_id%>)</a>:</TD>
	</tr>
	<tr>
		<TD>
			<ul id="tabnav">
				<li
					<% if(request.getServletPath().equals("/sales/salesorder-dash.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/salesorder-dash.jsp?pop=yes&so_id=<%=mybean.so_id%>">Sales
						Order Details</a></li>

				<li
					<% if(request.getServletPath().equals("/sales/salesorder-dash-finstatus.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/salesorder-dash-finstatus.jsp?so_id=<%=mybean.so_id%>">Finance
						Status</a></li>
				<li
					<% if(request.getServletPath().equals("/sales/salesorder-dash-registration.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/salesorder-dash-registration.jsp?so_id=<%=mybean.so_id%>">Registration</a>
				</li>

				<li
					<% if(request.getServletPath().equals("/sales/salesorder-dash-account.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/salesorder-dash-account.jsp?all=yes&so_id=<%=mybean.so_id%>">Account</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/sales/salesorder-dash-docs.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/salesorder-dash-docs.jsp?so_id=<%=mybean.so_id%>">Documents</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/sales/salesorder-dash-invoice.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/salesorder-dash-invoice.jsp?so_id=<%=mybean.so_id%>">Invoices</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/sales/salesorder-dash-receipt.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/salesorder-dash-receipt.jsp?so_id=<%=mybean.so_id%>">Receipts</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/sales/salesorder-dash-history.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/salesorder-dash-history.jsp?so_id=<%=mybean.so_id%>">History</a>
				</li>
			</ul>
		</TD>
	</tr>
</table>
