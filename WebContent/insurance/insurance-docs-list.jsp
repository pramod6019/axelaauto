<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Insurance_Doc_List"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
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
						<h1>List Insurance Document</h1>
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

						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../insurance/index.jsp">Service</a> &gt;</li>
						<li><a href="../insurance/insurance.jsp">Insurance</a> &gt;</li>
						<li><a href="../insurance/insurance-list.jsp?all=yes">List
								Insurance</a> &gt;</li>
						<li><a
							href="insurance-list.jsp?insurpolicy_id=<%=mybean.insurpolicy_id%>"><%=mybean.name%></a>
							&gt;</li>
						<li><a
							href="insurance-docs-list.jsp?insurpolicy_id=<%=mybean.insurpolicy_id%>">
								List Documents</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div align="right">
								<a
									href="../portal/docs-update.jsp?add=yes&insurpolicy_id=<%=mybean.insurpolicy_id%>">Add
									New Document...</a>
							</div>
							<div align="center">
								<font color="#ff0000"><b> <%=mybean.msg%>
								</b></font>
							</div>
							<div align="center"><%=mybean.RecCountDisplay%></div>
							<div align="center"><%=mybean.PageNaviStr%></div>
							<div align="center"><%=mybean.StrHTML%></div>
							<div align="center"><%=mybean.PageNaviStr%></div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
</body>
</HTML>
