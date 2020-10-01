<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.portal.Report_Export" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</head>
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
        				<h1>Export Details</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!--- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="enquiry.jsp?all=yes">Enquiry</a> &gt;</li>
						<li><a href="../portal/report-export.jsp">Export Details</a><b>:</b></li>
					</ul>
        <!-- END PAGE BREADCRUMBS -->
					<div class="page-content-inner">
						<div class="tab-pane" id="">

							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Export Details</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
							<!-- 					BODY START -->
										<form name="frm1" id="frm1" class="form-horizontal" method="post">
											<center>
												<font color="#ff0000"><b><%=mybean.msg%></b></font>
											</center>
        
											<div class="form-element3">
													<label>Executives: </label>
															 <%=mybean.PopulateExecutives()%> 
												</div>
												
												<div class="form-element3">
													<label>Document Type: </label>
															 <%=mybean.PopulateDocumentType()%> 
												</div>
											
							                 <div class="form-element3" >
												<label>Start Date<font color=red>*</font>:&nbsp;</label>
							                    <input name ="txt_starttime" id="txt_starttime" type="text" class="form-control datepicker" value="<%=mybean.start_time%>" maxlength="16" />
							                 </div>
							                 
							                 <div class="form-element3" >
												<label>End Date<font color=red>*</font>:&nbsp;</label>
							                    <input name ="txt_endtime" id ="txt_endtime" type="text" class="form-control datepicker" value="<%=mybean.end_time%>" maxlength="16"/>
							                 </div>
							                 
							                 <div class="form-element12" >
							                 <center>
												<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Go" />
							                    <input type="hidden" name="submit_button" value="Submit">
							                 </center>
							                 </div>
								
										</form>
									</div>
								</div>
							</div>
							<% if (!mybean.StrHTML.equals("")) { %>
							<div>
								<center><%=mybean.StrHTML%></center>
							</div>
							<% } %>
						</div>
					</div>
				</div>
			</div>
		</div>
</div>
</div>   
<%-- 							    <td colspan="2" align="center"><strong><%=mybean.RecCountDisplay%></strong></td> --%>
<!-- 							  </tr> -->
<!-- 							  <tr> -->
<%-- 							    <td colspan="2" align="center"><%=mybean.PageNaviStr%></td> --%>
<!-- 							  </tr> -->
<!-- 							  <tr> -->
<%-- 							    <td valign="top"  height="200" colspan="2" align="center"><%=mybean.StrHTML%></td> --%>
<!-- 							  </tr> -->
<!-- 							  <tr> -->
<%-- 							    <td colspan="2"  align="center"><%=mybean.PageNaviStr%></td> --%>
<!-- 							  </tr> -->
<!-- 							  <tr> -->
<!-- 							    <td colspan="2" align="center">&nbsp;</td> -->
<!-- 							  </tr> -->
<!-- 							</TABLE> -->

		 <%@ include file="../Library/admin-footer.jsp" %></body>
		 <%@include file="../Library/js.jsp"%>
</html>
