<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.insurance.Report_Insurance_Dashboard" scope="request"/>
<%mybean.doPost(request,response); %>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
	
</HEAD>
    <body  class="page-container-bg-solid page-header-menu-fixed" onload="convertmultiselect();">
     <%@include file="../portal/header.jsp" %>
     
   <!-- 	MULTIPLE SELECT END-->	
<!-- 	BODY -->
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
						<h1>Insurance Dashboard</h1>
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
						<li><a href="report-insurance-dashboard.jsp">Insurance Dashboard</a><b>:</b></li>
						
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					
					<div class="tab-pane" id="">
					<center><font color="red"><b><%=mybean.msg%> </b></font></center>
					<!-- 	BODY START -->
					<!-- 	PORTLET -->
	<div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
						Insurance Dashboard 
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="tab-pane" id="">
						<!-- START PORTLET BODY -->
						<form method="post" name="frm1"  id="frm1" class="form-horizontal">
						<div class="container-fluid">
						<div class="form-element1"></div>
						<div class="form-element2">
							<label> Executive<font color=red>*</font>: </label>
							<div>
								<select name="dr_executive" id="dr_executive" class="form-control multiselect-dropdown" multiple="multiple" size=10>
								  <%=mybean.PopulateSalesExecutives()%>
								</select>
							</div>
						</div>

					<div class="form-element4">
						<label> Start Date<font color=red>*</font>:</label>
						<input name ="txt_startdate" id="txt_startdate" type="text" class="form-control datepicker" value="<%=mybean.startdate %>" size="12" maxlength="10" class="form-control date-picker"
							value=""/>	
					</div>

					<div class="form-element4">
						<label> End Date<font color=red>*</font>: </label>
					  <input name ="txt_enddate" id ="txt_enddate" type="text" class="form-control datepicker" value="<%=mybean.enddate %>" size="12" maxlength="10" class="form-control date-picker"
							value=""/>	
					
					</div>

					<div class="form-element12 ">
					 <center><input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Go" /></center>
					 <input type="hidden" name="submit_button" value="Submit">	
					</div>

						
	</div>					
</form>
					</div>
				</div>
			</div>

<!-- 	PORTLET -->
			<center><%=mybean.StrHTML%>	</center>	
						

					</div>
				</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
    <%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
     </body>
    </HTML>
