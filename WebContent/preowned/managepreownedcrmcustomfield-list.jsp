<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.ManagePreownedCRMCustomField_List"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp" %>
</head>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
	<%@include file="../portal/header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>List Pre-Owned CRM Custom Field List</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<div class="page-content-inner">
						<ul class="page-breadcrumb breadcrumb">
							<%=mybean.LinkHeader%>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font><br></br>
							</center>

							<!-- START PORTLET BODY -->
							<div style="float: right">
								<%
									if (!mybean.precrmfollowupdays_id.equals("0")) {
								%>

								<a
									href="../preowned/managepreownedcrmcustomfield-update.jsp?add=yes&precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>">Add
									Custom Field...</a>

								<%
									}
								%>
							</div>
							<br />
							<div>
								<%=mybean.StrCustomField%>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>
	<script language="JavaScript" type="text/javascript">
		function populatemodule() {
		
			var module_id = document.getElementById('dr_module_id').value;
			var submodule_id = document.getElementById('dr_submodule_id').value;
			
			showHint('../portal/customfield-check.jsp?module=yes&module_id='
					+ module_id + '&submodule_id=' + submodule_id, 'submodule');
		}
	</script>
</body>
</html>
