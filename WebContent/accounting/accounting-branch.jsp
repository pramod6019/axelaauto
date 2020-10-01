<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.accounting.Accounting_Branch" scope="request"/>
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
        <HEAD>
        <title><%=mybean.AppName%></title>
        <meta name="viewport" content="width=device-width, initial-scale=1"> 
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
        <LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
        <LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
        <link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
        <link href="../Library/theme<%=mybean.GetTheme(request)%>/font-awesome.css" rel="stylesheet" media="screen" type="text/css" />
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
						<h1><%=mybean.heading%></h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
				<%if(mybean.para.equals("prop")){%>
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt; </li>
						<li><a href="index.jsp">Accounting</a> &gt; </li>
						<li><a href="../accounting/accounting-branch.jsp?para=prop"><%=mybean.heading%></a><b>:</b></li>
						
					</ul>
					 <%} else{%>
						<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt; </li>
						<li><a href="index.jsp">Accounting</a> &gt; </li>
						<li><a href="../accounting/accounting-branch.jsp?para=prop"><%=mybean.heading%></a><b>:</b></li>
						
					</ul>
						 <%} %>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="page-content-inner">
					<div class="tab-pane" id="">
<!-- 					BODY START -->
                   <center><font color="#ff0000" ><b><%=mybean.msg%></b></font></center>
					<div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
						<%=mybean.heading%>
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="tab-pane" id="">
						<!-- START PORTLET BODY -->
						<form name="form1" class="form-horizontal" method="post">
						<center><font>Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required. </font></center>
										<div class="form-body">
											<div class="form-group">
												<label class="col-md-3 control-label">Branch<font color="#ff0000">*</font>:</label>
												<div class="col-md-6">
                                   <select name="dr_branch_id" class="form-control" id="dr_branch_id" <%if(mybean.comp_module_inventory.equals("1") &&  !mybean.para.equals("enquiry")){%>onchange="PopulateLocation();"<%}%>>
                                    <%=mybean.PopulateBranch(mybean.branch_id, "", "", "", request)%>
                                   </select>
												</div>
											</div>
										</div>
											<%
												if (mybean.comp_module_inventory.equals("1") && !mybean.para.equals("enquiry") && !mybean.para.equals("prop")) {
											%>
											<div class="form-body">
												<div class="form-group">
													<label class="col-md-3 control-label">Location<font
														color="#ff0000">*</font>:
													</label>
													<div class="col-md-6">
														<span id="span_location"> <Select
															id="dr_location_id" name="dr_location_id"
															class="form-control">
																<%=mybean.PopulateInventoryLocation()%>
														</Select>
														</span>
													</div>
												</div>
											</div>
											<%
												}
											%>
                                       <center><input name="go_button" type="submit" class="btn btn-success" id="go_button" value="GO" /></center>

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
         <%@include file="../Library/admin-footer.jsp" %>
         <script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="../Library/Validate.js?target=<%=mybean.jsver%>"></script>
        
       <script language="JavaScript" type="text/javascript">
            function FormFocus() { //v1.0
                document.form1.dr_branch_id.focus()
            }
			
			function PopulateLocation(){
				var branch_id = document.getElementById("dr_branch_id").value;
				showHint('../accounting/accounting-branch-check.jsp?sales_branch_id='+branch_id+'&branch_location=yes','span_location');     
				}
			function showHint(url, Hint) {
				$('#'+Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
					$.ajax({
						url: url,
						type: 'GET',
						success: function (data){
				//alert("data=="+data);
							if(data.trim() != 'SignIn')
								$('#'+Hint).html('' + data + '');
							else
								window.location.href = "../portal/";
						}
					});
			}
        </script>
         
         </body>
</HTML>
