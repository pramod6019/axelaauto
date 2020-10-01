<!-- <div class="page-container"> -->
<!-- BEGIN CONTENT -->
<!-- 		<div class="page-content-wrapper"> -->
<!-- BEGIN CONTENT BODY -->
<!-- BEGIN PAGE HEAD-->
<div class="page-head">
	<div class="container-fluid">
		<!-- BEGIN PAGE TITLE -->
		<div class="page-title">
			<h1> Pre-Owned Enquiry Dashboard &gt; Pre-Owned ID<b>:</b>&nbsp;<%=mybean.preowned_id%></h1>
		</div>
		<!-- END PAGE TITLE -->
	</div>
</div>
<!-- END PAGE HEAD-->
<!-- BEGIN PAGE CONTENT BODY -->
<!-- 		<div class="page-content"> -->
<div class="container-fluid">
	<ul class="page-breadcrumb breadcrumb">
		<li><a href="../portal/home.jsp">Home</a> &gt;</li>
		<li><a href="../preowned/index.jsp">Pre-Owned</a> &gt;</li>
		<li><a href="../preowned/preowned-list.jsp?all=yes">List Pre-Owned </a> &gt;</li>
		<li><a href="../preowned/preowned-dash.jsp?pop=yes&amp;preowned_id=<%=mybean.preowned_id%>"><%=mybean.preowned_title%>
				(<%=mybean.preowned_id%>)</a><b>:</b></li>

	</ul>
	<!-- END PAGE BREADCRUMBS -->

	<!-- CANNED MSG START -->
	<CENTER>
		<a
			href="../portal/canned.jsp?canned=yes&preowned=yes&preowned_id=<%=mybean.preowned_id%>"
			class="btn btn-success btn-lg" data-target="#Hintclicktocall"
			data-toggle="modal" style="margin-top: 1px;"><large> <span
				style="font-size: 20px; top: 4px"
				class="glyphicon glyphicon-envelope"></span></large>&nbsp; Messages</a>
	</CENTER>
	<!-- CANNED MSG END -->


	<div class="page-content-inner">
		<div class="tab-pane" id="">
			<!-- 					BODY START -->

			<div class="tabbable tabbable-tabdrop">
				<ul class="nav nav-tabs">
					<li
						<% if(request.getServletPath().equals("/preowned/preowned-dash.jsp")){%>
						class="selecttab active" <% } %>><a
						href="../preowned/preowned-dash.jsp?pop=yes&amp;preowned_id=<%=mybean.preowned_id%>">Pre-Owned
							Details</a></li>
					<li
						<% if(request.getServletPath().equals("/preowned/preowned-dash-followup.jsp")) {%>
						class="selecttab active" <% } %>><a
						href="../preowned/preowned-dash-followup.jsp?preowned_id=<%=mybean.preowned_id%>">Follow-up</a></li>
					<li
						<% if(request.getServletPath().equals("/preowned/preowned-dash-crmfollowup.jsp")) {%>
						class="selecttab active" <% } %>><a
						href="../preowned/preowned-dash-crmfollowup.jsp?preowned_id=<%=mybean.preowned_id%>">CRM
							Follow-up</a></li>
					<li
						<% if(request.getServletPath().equals("/preowned/preowned-dash-enquiryfollowup.jsp")) {%>
						class="selecttab active" <% } %>><a
						href="../preowned/preowned-dash-enquiryfollowup.jsp?preowned_id=<%=mybean.preowned_id%>">New
							Enquiry Follow-up</a></li>
					<li
						<% if(request.getServletPath().equals("/preowned/preowned-dash-customer.jsp")){%>
						class="selecttab active" <% } %>><a
						href="../preowned/preowned-dash-customer.jsp?preowned_id=<%=mybean.preowned_id%>">Account</a>
					</li>
					<li
						<% if(request.getServletPath().equals("/preowned/preowned-dash-docs.jsp")){%>
						class="selecttab active" <% } %>><a
						href="../preowned/preowned-dash-docs.jsp?preowned_id=<%=mybean.preowned_id%>">Documents</a>
					</li>
					<li
						<% if(request.getServletPath().equals("/preowned/preowned-dash-image.jsp")){%>
						class="selecttab active" <% } %>><a
						href="../preowned/preowned-dash-image.jsp?preowned_id=<%=mybean.preowned_id%>">Images</a>
					</li>
					<li
						<% if(request.getServletPath().equals("/preowned/preowned-dash-testdrive.jsp")) {%>
						class="selecttab active" <% } %>><a
						href="../preowned/preowned-dash-testdrive.jsp?preowned_id=<%=mybean.preowned_id%>">Test
							Drives</a></li>
					<li
						<% if(request.getServletPath().equals("/preowned/preowned-dash-receipt.jsp")){%>
						class="selecttab active" <% } %>><a
						href="../preowned/preowned-dash-receipt.jsp?preowned_id=<%=mybean.preowned_id%>">Receipts</a>
					</li>
					<li
						<% if(request.getServletPath().equals("/preowned/preowned-dash-history.jsp")){%>
						class="selecttab active" <% } %>><a
						href="../preowned/preowned-dash-history.jsp?preowned_id=<%=mybean.preowned_id%>">History</a>
					</li>
				</ul>
			</div>

		</div>
	</div>
</div>
<script>
// For Sending canned messages 

function SendEmail(email_id){
	var preowned_id = <%=mybean.preowned_id%>;
	showHint('../portal/canned-message-check.jsp?type=2&email=yes&email_id=' + email_id + '&value=' + preowned_id, 'sentmsg');
}

function SendSMS(sms_id){
	var preowned_id = <%=mybean.preowned_id%>;
	showHint('../portal/canned-message-check.jsp?type=2&sms=yes&sms_id=' + sms_id + '&value=' + preowned_id, 'sentmsg');
}
</script>