<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Report_Vehicle_Stock_Status_Summary" scope="request"/>
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
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1>Stock Status Summary</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a>&gt;</li>
						<li><a href="report-stock-status-summary.jsp">Stock Status Summary</a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font> <br></br>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Search</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="post">
											<div class="form-element6">
												<label>
													Stock Status Summary For Branch<font color="#ff0000">*</font>:
												</label>
												<div>
													<select name="dr_vehstock_branch" class="form-control"
														id="dr_vehstock_branch" onChange="document.form1.submit();">
														<%=mybean.PopulateBranch(mybean.vehstock_branch_id, "", "","", request)%>
													</select>
												</div>
											</div>
										</form>
									</div>
								</div>
							</div>
							<% if(!mybean.vehstock_branch_id.equals("0") && !mybean.vehstock_branch_id.equals("")) {%>

								<%=mybean.StrHTML%>

							<% } %>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@include file="../Library/admin-footer.jsp" %>
	<%@include file="../Library/js.jsp"%>
        <script language="JavaScript" type="text/javascript">
			function Subjectlist(){
            	document.form1.submit();
           	}
        </script>
	</body>
</HTML>
