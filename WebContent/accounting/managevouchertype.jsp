<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.ManageVoucherType"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>	
</head>
<body
	<%if (mybean.advSearch.equals(null) || mybean.advSearch.equals("")) {%>
	onLoad="LoadRows();FormFocus();" <%}%>
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>List Voucher Types</h1>
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
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
						<li><a href="managevouchertype.jsp?all=yes">List Voucher Types</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
					<font color="#ff0000"><b> <%=mybean.msg%> 
                   </b></font>
					</center>
					<!-- END PAGE BREADCRUMBS -->
							<!-- 					BODY START -->
							<div class="container-fluid">
								<!-- 		<div class="container-fluid portlet box "> -->
								<!-- 			<div class="portlet-title" style="text-align: center">&nbsp;</div> -->
								<!-- 			<div class="portlet-body"> -->
								<div>
									<center>
										<strong><%=mybean.RecCountDisplay%></strong>
									</center>
								</div>

								<div><%=mybean.StrHTML%></div>

								<!-- 			</div> -->

								<!-- 		</div> -->
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>


	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript" src="../Library/smart.js"></script>
	<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
	<script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});

		$('.datepicker').datetimepicker({
			addSliderAccess : true,
			sliderAccessArgs : {
				touchonly : false
			},
			dateFormat : "dd/mm/yy"
		});
	</script>

</body>
</html>
