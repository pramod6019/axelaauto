<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.News_Summary"
	scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>

<script language="JavaScript" type="text/javascript">
function FormFocus() { //v1.0
  //document.form1.txt_search.focus();
}
function disp() { //v1.0
  alert("gdfh");
}
        </script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>News Details</h1>
					</div>
					<!-- END PAGE TITLE -->

				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<!-- BEGIN PAGE BREADCRUMBS -->
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="news.jsp">News</a> &gt;</li>
						<li><a href="news-list.jsp?all=yes">List News</a> &gt;</li>
						<li><a href="news-summary.jsp?news_id=<%=mybean.news_id%>">News
								Details</a></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
				</div>
				<!-- START CHANGE PASSWORD -->
				<div class="container-fluid">
<!-- 					<center> -->
<%-- 						<font color="#FF0000"><b><%=mybean.msg%></b></font> --%>
<!-- 					</center> -->
					<div class="portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">News Details</div>
						</div>
						<div class="portlet-body portlet-empty">
							<div class="tab-pane" id="">
								<%=mybean.StrHTML%>
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
</body>
</HTML>
