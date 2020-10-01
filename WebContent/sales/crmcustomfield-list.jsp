<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.CRMCustomField_List"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>CRM Custom Field List</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
				<div class="container-fluid">
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
									if (!mybean.crmdays_id.equals("0")) {
								%>

								<a href="../sales/crmcustomfield-update.jsp?add=yes&crmdays_id=<%=mybean.crmdays_id%>">Add Custom Field...</a>

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
	<script language="JavaScript" type="text/javascript">
		function populatemodule() {
			// alert("===entr")
			var module_id = document.getElementById('dr_module_id').value;
			var submodule_id = document.getElementById('dr_submodule_id').value;
			//alert("==="+oppr_model_id)
			showHint('../portal/customfield-check.jsp?module=yes&module_id='
					+ module_id + '&submodule_id=' + submodule_id, 'submodule');
		}
	</script>
</body>
</HTML>
