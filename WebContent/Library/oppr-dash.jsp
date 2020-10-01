<table width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<TD><a href="../portal/home.jsp">Home</a> &gt; <a
			href="../sales/index.jsp">Sales</a> &gt; <a href="../sales/oppr.jsp">Opportunities</a>
			&gt; <a href="../sales/enquiry-list.jsp?all=yes">List Opportunities</a>
			&gt; <a
			href="../sales/oppr-dash-opportunity.jsp?pop=yes&oppr_id=<%=mybean.oppr_id%>"><%=mybean.oppr_title%>
				(<%=mybean.oppr_id%>)</a>:</TD>
	</tr>
	<tr>
		<TD>
			<ul id="tabnav">
				<li
					<% if(request.getServletPath().equals("/sales/oppr-dash-opportunity.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/oppr-dash-opportunity.jsp?pop=yes&oppr_id=<%=mybean.oppr_id%>">Opportunity
						Details</a></li>

				<li
					<% if(request.getServletPath().equals("/sales/oppr-dash-followup.jsp")) {%>
					class="selecttab" <% } %>><a
					href="../sales/oppr-dash-followup.jsp?oppr_id=<%=mybean.oppr_id%>">Follow-up</a>
				</li>

				<li
					<% if(request.getServletPath().equals("/sales/oppr-dash-crmfollowup.jsp")) {%>
					class="selecttab" <% } %>><a
					href="../sales/oppr-dash-crmfollowup.jsp?oppr_id=<%=mybean.oppr_id%>">CRM
						Follow-up</a></li>

				<li
					<% if(request.getServletPath().equals("/sales/oppr-dash-account.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/oppr-dash-account.jsp?all=yes&oppr_id=<%=mybean.oppr_id%>">Account</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/sales/oppr-dash-docs.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/oppr-dash-docs.jsp?oppr_id=<%=mybean.oppr_id%>">Documents</a>
				</li>
				<%if(mybean.oppr_opprtype_id.equals("1")) {%>
				<li
					<% if(request.getServletPath().equals("/sales/oppr-dash-testdrive-.jsp")) {%>
					class="selecttab" <% } %>><a
					href="../sales/oppr-dash-testdrive-.jsp?oppr_id=<%=mybean.oppr_id%>">Test Drives</a>
				</li>
				<%}%>
				<%if(mybean.oppr_opprtype_id.equals("2")) {%>
				<li
					<% if(request.getServletPath().equals("/sales/oppr-dash-testdrive--preowned.jsp")) {%>
					class="selecttab" <% } %>><a
					href="../sales/oppr-dash-testdrive--preowned.jsp?oppr_id=<%=mybean.oppr_id%>">Pre Owned
						Test Drives</a></li>
				<%}%>
				<li
					<% if(request.getServletPath().equals("/sales/oppr-dash-quote.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/oppr-dash-quote.jsp?oppr_id=<%=mybean.oppr_id%>">Quotes</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/sales/oppr-dash-salesorder.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/oppr-dash-salesorder.jsp?oppr_id=<%=mybean.oppr_id%>">Sales
						Orders</a></li>
				<li
					<% if(request.getServletPath().equals("/sales/oppr-dash-invoice.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/oppr-dash-invoice.jsp?oppr_id=<%=mybean.oppr_id%>">Invoices</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/sales/oppr-dash-receipt.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/oppr-dash-receipt.jsp?oppr_id=<%=mybean.oppr_id%>">Receipts</a>
				</li>
				<li
					<% if(request.getServletPath().equals("/sales/oppr-dash-history.jsp")){%>
					class="selecttab" <% } %>><a
					href="../sales/oppr-dash-history.jsp?oppr_id=<%=mybean.oppr_id%>">History</a>
				</li>
			</ul>
		</TD>
	</tr>
</table>
