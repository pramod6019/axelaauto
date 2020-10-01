<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.customer.Report_Customer_Birthday" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

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
						<h1>Contact's Birthday</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="../customer/report-customer-birthday.jsp">Contact's Birthday</a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->
      
					
						<div class="tab-pane" id="">
							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"> Contact's Birthday
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										
										<div class="caption" style="float: none">
											<center>
												<font color="#ff0000"><b><%=mybean.msg%></b></font>
											</center>
										</div>

										<form method="post" name="frm1" id="frm1" class="form-horizontal">
											<div class="form-element3">
												<label>Start Date:</label>
												<select name="dr_starttime_day" class="form-control" id="dr_starttime_day">
													<%=mybean.PopulateDay(mybean.starttime_day)%>
												</select>
											</div>
											<div class="form-element3 form-element-margin">
												<select name="dr_starttime_month" class="form-control" id="dr_starttime_month">
													<%=mybean.PopulateMonth(mybean.starttime_month)%>
												</select>
											</div>
											
											<div class="form-element3">
												<label>End Date:</label>
												<select name="dr_endtime_day" class="form-control" id="dr_endtime_day">
													<%=mybean.PopulateDay(mybean.endtime_day)%>
												</select>
											</div>
											<div class="form-element3 form-element-margin">
												<select name="dr_endtime_month" class="form-control" id="dr_endtime_month">
													<%=mybean.PopulateMonth(mybean.endtime_month)%>
												</select>
											</div>
											
											<div class="form-element12">
												<center>
													<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go" />
													<input type="hidden" name="submit_button" value="Submit" />
												</center>
											</div>
										</form>
									</div>
								</div>
							</div>
							<%
								if (!mybean.StrHTML.equals("")) {
							%>
							<!-- START PORTLET BODY -->
							
							<center><%=mybean.StrHTML%></center>	

							<%
								}
							%>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	 
	<%@include file="../Library/js.jsp" %>
</body>
</HTML>
