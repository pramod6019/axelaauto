<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.FAQ_Executive"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
	<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<script language="JavaScript">
	function ToggleDisplay(id) {
		var elem = document.getElementById('d' + id);
		var elemsign = document.getElementById('s' + id);
		if (elem) {
			if (elem.style.display != 'block') {
				elem.style.display = 'block';
				elem.style.visibility = 'visible';
				elemsign.innerHTML = "[-]";
			} else {
				elem.style.display = 'none';
				elem.style.visibility = 'hidden';
				elemsign.innerHTML = "[+]";
			}
		}
	}
	function hidestatus() {
		window.status = "";
		return true;
	}
	function FormSubmit() { //v1.0
		document.getElementById("dr_faq_cat_id").value = "0";
		document.form1.submit();
	}
	function FormFocus() { //v1.0
		document.form1.txt_search.focus();
	}
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>FAQ Executive</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="">FAQ Executive</a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Search</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form class="form-horizontal" name="form1" method="get">
											<div class="form-group">
												<label class="control-label col-md-4"> Keyword: <font
													color=red>*</font>
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_search" type="text" class="form-control"
														id="txt_search" value="<%=mybean.txt_search%>" size="32"
														maxlength="255">
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Category: <font
													color=red>*</font>
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<select name="dr_faq_cat_id" class="form-control"
														id="dr_faq_cat_id" visible="true">
														<%=mybean.PopulateCategory()%>
													</select>
												</div>
											</div>
											<center>
												<input name="search_button" type="submit" class="btn btn-success"
													id="search_button" value="Search" /> <input type="hidden"
													name="search" value="yes">
											</center>

											<center>
												<strong><%=mybean.RecCountDisplay%></strong>
											</center>
											<center><%=mybean.PageNaviStr%></center>
											<center><%=mybean.StrHTML%></center>
											<center><%=mybean.PageNaviStr%></center>
										</form>

									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</HTML>
