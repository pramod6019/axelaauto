<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_Veh_Duplicate_Status" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>

</head>
<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1>Duplicate Vehicle Report</h1>
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
							<li><a href="../portal/mis.jsp">MIS</a> &gt; </li>
							<li><a href="report-veh-duplicate-status.jsp">Duplicate Vehicle Report</a><b>:</b></li>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					
					<div class="tab-pane" id="">
<!-- 					BODY START -->
                	 	<center><font color="red"><b><%=mybean.msg%>
                                  </b></font><br></br></center>
						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">
									Duplicate Vehicles
								</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="tab-pane" id="">
									<!-- START PORTLET BODY -->
									<form method="post" class="form-horizontal" name="form1"  id="form1">
			                       	<div class="container-fluid ">
											<div class="form-element6">
												<label>Brand<font color=red></font>:&nbsp;</label>
					                      			<select name="dr_brand_id" id="dr_brand_id" class="form-control" 
					                      			onChange="document.form1.submit()">
					                        			<%=mybean.PopulateBrand()%>
					                      			</select>
											</div>
										
											<div class="form-element6">
												<label>Duplicate By<font color=red>*</font>:&nbsp;</label>
					                      			<select name="dr_duplicateby_id" id="dr_duplicateby_id" class="form-control" 
					                      			onChange="document.form1.submit()">
					                        			<%=mybean.PopulateDuplicateBy()%>
					                      			</select>
											</div>
									</div>
									</form><br><br>
								</div>
									<center><%=mybean.StrHTML%></center>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

    <%@include file="../Library/admin-footer.jsp" %>
   	<%@include file="../Library/js.jsp" %>
	
</body>
</html>
