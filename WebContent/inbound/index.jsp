<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inbound.Index" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

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
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Inbound</h1>
					</div>
					<!-- END PAGE TITLE -->
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
				<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../inbound/index.jsp">Inbound</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					</div>
					</div>
						
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
								<div class="page-content">
										<div class="page-content-inner">
											<div class="tab-pane" id="">
													<div class="form-element4 pull-right"
														style="padding: 5px">
														<div class="portlet box">
															<div class="portlet-title" style="text-align: center">
																<div class="caption" style="float: none">Inbound
																	Reports</div>
															</div>
															<div class="portlet-body portlet-empty container-fluid">
																<div class="tab-pane" id="">
																	<table class="table table-bordered table-hover table-responsive" data-filter="#filter">
																		<tr>
																			<td>
																				<a href="../inbound/report-inbound-monitoring-board.jsp" target=_blank >Inbound Calls</a>
																			</td>
																		</tr>
																	</table>
																</div>
															</div>
														</div>
													</div>
											</div>
										</div>
								</div>
						</div>
	</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

</body>
</html>
