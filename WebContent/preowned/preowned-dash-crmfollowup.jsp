<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Dash_CRMFollowup" scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@ include file="../Library/css.jsp"%>

</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<%@ include file="../Library/preowned-dash.jsp"%>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<!-- 		<div class="page-content-wrapper"> -->
		<!-- 		<div class="page-content"> -->
		<!-- 				<div class="container-fluid"> -->
		<center>
			<font color="#ff0000"><b><%=mybean.msg%></b></font>
		</center>
		<form name="Frmtasks" method="post" class="form-horizontal">
			<div class="portlet box">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">CRM Follow-up</div>
				</div>
				<div class="tab-pane" id="">
					<div><%=mybean.customerdetail%></div>
					<center>
						<%=mybean.followupHTML%>
					</center>
				</div>
			</div>
		</form>
		<!-- 		</div> -->
		<!-- 			</div> -->
		<!-- 		</div> -->
	</div>
	<%@ include file="../Library/admin-footer.jsp"%>
	
	<%@ include file="../Library/js.jsp"%>

</body>
</html>
