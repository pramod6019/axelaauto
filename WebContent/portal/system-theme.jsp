<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.System_Theme"
	scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp"%>

<style>
@media (min-width: 992px)
{
	.center-align
	{
		margin-left: 245px;
	}	
}
</style>
</HEAD>

<body class="page-container-bg-solid page-boxed page-header-menu-fixed"
	onLoad="PopulateCheckBox();">
	<%@include file="header.jsp"%>
	<div class="page-container">
		<div class="page-content-wrapper">
			<div class="page-head">
				<div class="container-fluid">
					<div class="page-title">
						<h1>Manage Theme</h1>
					</div>
				</div>
			</div>

			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="system-config.jsp">System</a> &gt;</li>
						<li><a href="system-theme.jsp?all=yes">Manage Theme</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
				</div>
				<!-- START ADD TICKET -->
				<center>
					<font color="#ff0000"><b><%=mybean.msg%></b></font>
				</center>
				<form name="frm1" method="post" action="">
					<div class="container-fluid">
						<div class="container-fluid portlet box">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">
									Manage Themes <input type="hidden" class="form-control"
										id="txt_emp_theme_id" name="txt_emp_theme_id"
										value="<%=mybean.theme%>" />
								</div>
							</div>
							
							<div class="portlet-body">
								<div class="container-fluid">
									<!-- START PORTLET BODY -->
									
									<div class="col-md-12">
									<div class="col-md-1"></div>
									<div class="center-align">
									<div class="col-md-1 text-center" onclick="change_theme(1);" style="background-color:#4B77BE;height: 300px;"> <div style="height:280px;"></div> <b style="color: white;">Dream Blue</b> </div>
									<div class="col-md-1 text-center" onclick="change_theme(2);" style="background-color:#22264b;height: 300px;"> <div style="height:280px;"></div><b style="color: white;">Navy Blue</b> </div>
									<div class="col-md-1 text-center" onclick="change_theme(3);" style="background-color:#5e0231;height: 300px;"> <div style="height:280px;"></div> <div><b style="color: white;">WISTERIA</b></div> </div>
									<div class="col-md-1 text-center" onclick="change_theme(4);" style="background-color:#30231d;height: 300px;"> <div style="height:280px;"></div> <b style="color: white;">Coffee Brown</b> </div>
									<div class="col-md-1 text-center" onclick="change_theme(5);" style="background-color:#cf5a00;height: 300px;"> <div style="height:280px;"></div> <b style="color: white;">Carrot</b> </div>
									<div class="col-md-1 text-center" onclick="change_theme(6);" style="background-color:#006666;height: 300px;"> <div style="height:280px;"></div> <b style="color: white;">JADE</b> </div>
									<div class="col-md-1 text-center" onclick="change_theme(7);" style="background-color:#935347;height: 300px;"> <div style="height:280px;"></div> <b style="color: white;">ALIZARIN</b> </div>
									<div class="col-md-1 text-center" onclick="change_theme(8);" style="background-color:#d23176;height: 300px;"> <div style="height:280px;"></div> <b style="color: white;">Azelea</b> </div>
									<div class="col-md-1 text-center" onclick="change_theme(9);" style="background-color:#406378;height: 300px;"> <div style="height:280px;"></div> <b style="color: white;">Blue Berry</b> </div>
									</div>
									<div class="col-md-1"></div>
									</div>

								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<!-- END ADD TICKET -->
			</div>
		</div>
		<!-- END PAGE CONTENT BODY -->
		<!-- END CONTENT BODY -->
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

<script type="text/javascript">
    function change_theme(checkbox_id){	
	document.getElementById("txt_emp_theme_id").value = checkbox_id;
	document.frm1.submit();
	}
	function PopulateCheckBox(){
		var theme_id = document.getElementById("txt_emp_theme_id").value;
		document.getElementById("chk_theme"+theme_id).checked = "checked";	
	}
	
	</script>
</body>
</HTML>
