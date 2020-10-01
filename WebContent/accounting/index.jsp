<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Index" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../assets/css/footable.core.css">
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />

<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<div class="page-container">
		<div class="page-content-wrapper">
			<div class="page-head">
				<div class="container-fluid">
					<div class="page-title">
						<h1>Accounting</h1>
					</div>
				</div>
			</div>
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../service/index.jsp">Accounting</a><b>:</b></li>
					</ul>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<div class="container-fluid">
								<div class=" col-md-4 col-xs-12 pull-right" style="padding: 5px">
									<div class="portlet box">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Accounting
												Reports
											</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<%=mybean.ListReports()%>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<%@include file="../Library/admin-footer.jsp"%>
<script src="../assets/js/jquery.min.js" type="text/javascript"></script> 
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script src="../assets/js/footable.js" type="text/javascript"></script>
<script type="text/javascript">

	$(function() {
	 $('table').footable({
		 toggleHTMLElement : '<span>
				<div class="footable-toggle footable-expand" border="0"></div>' + '<div class="footable-toggle footable-contract" border="0"></div></span>'
	 });
		
	});
</script>
	
	</body>
</html>
