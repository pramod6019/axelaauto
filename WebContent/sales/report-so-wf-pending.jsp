<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Report_SO_Wf_Pending" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
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
						<li><a href="../sales/report-so-wf-pending.jsp">Sales Order Work Flow Pending Delivery </a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							
								<center>
									<font color="#ff0000"><b><div id="msg"><%=mybean.msg%> </div></b></font>
								</center>
							
							<form name="form1" method="get" class="form-horizontal">

								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Search</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											
											<center>
												<div class="form-element-center">
													<label>Branch<font color=red>*</font>:&nbsp; </label>
													<div>
														<%if(mybean.branch_id.equals("0")){%>
											              <select name="dr_branch" id="dr_branch" class="selectbox" onChange="document.form1.submit()">
											                <%=mybean.PopulateBranch(mybean.dr_branch_id,"", "1,2", "", request)%>
											                </select>
											                <%}else{%>
									                        <input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>" />
									                        <%=mybean.getBranchName(mybean.dr_branch_id, mybean.comp_id)%>
									                        <%}%>
													</div>
												</div>
											</center>
											
											
										</div>
									</div>
								</div>
								
								
								<div>
									<center><%=mybean.StrHTML%></center>
								</div>

							</form>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	
	</body>
</HTML>