<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Branch" scope="request"/>
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
        <HEAD>
        <title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
        <link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
        
        <script type="text/javascript" src="../Library/Validate.js"></script>
        <script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
      <link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
	<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

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


	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
        <script language="JavaScript" type="text/javascript">
            function FormFocus() { //v1.0
                //document.form1.dr_branch_id.focus()
            }
        </script>
        <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
        <body  onLoad="FormFocus()" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <%@include file="../portal/header.jsp" %>
    
    	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Pre-Owned Branch</h1>
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
						<li><a href="index.jsp">Pre-Owned</a> &gt;</li>
						<li><b><%=mybean.heading%>:</b></li>
						
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="page-content-inner">
					<div class="tab-pane" id="">
<!-- 					BODY START -->
	<center><font color="#ff0000" ><b><%=mybean.msg%></b></font> <br></center>
		<div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
						Pre-Owned Branch 
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="tab-pane" id="">
						<!-- START PORTLET BODY -->
						<form class="form-horizontal" name="form1"  method="post">
						<center><font>Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required. </font></center>
<!-- 					<form class="form-control"> -->
										
											<div class="form-group">
												<label class="control-label col-md-5 col-xs-12"> Branch<font
													color="#ff0000">*</font>:
												</label>
												<div class="col-md-3 col-xs-12" id="emprows">
													<select name="dr_branch_id" class="form-control"
														id="dr_branch_id">
														<%=mybean.PopulateBranch(mybean.branch_id, "", "2",  "", request)%>
													</select>
												</div>
											</div>

											<!-- <div class="form-group"> -->
<!-- <label class="control-label col-md-5"></label> -->
<!-- <div class="col-md-6 col-xs-12" id="emprows"> -->
	

<center><input name="go_button" type="submit" class="btn btn-success" id="go_button" value="GO" /></center>
<!-- </div> -->
<!-- </div> -->


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
        
        <%@ include file="../Library/admin-footer.jsp" %>
        
        </body>
</HTML>
