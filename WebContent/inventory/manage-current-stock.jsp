<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Manage_Current_Stock" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
<%@include file="../portal/header.jsp" %>
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
						<h1>Current Stock</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href=../portal/manager.jsp>Business Manager</a> &gt;</li>
						<li><a href=../inventory/manage-current-stock.jsp>Current Stock</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
					<font color="#ff0000" ><b><%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
					<div class="tab-pane" id="">
<!-- 					BODY START -->
					<div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
					Update Current Stock
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="tab-pane" id="">
						<!-- START PORTLET BODY -->
						<form name="form1"  method="post" class="form-horizontal">
							<div class="form-element3">
								<label>Branches<font color="red">*</font>:</label> 
									<div> 
									<select name="dr_branch" size="10" class="form-control" id="dr_branch">
									<%=mybean.PopulateBranches()%>
									</select>
									</div>
							</div>
							<div class="row"></div>
						<center>
							<input name="update_button" type="submit" class="btn btn-success" id="update_button" value="Update Current Stock" />
						</center>
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
<%@include file="../Library/admin-footer.jsp" %>
<%@include file="../Library/js.jsp" %>
</body>
</HTML>
