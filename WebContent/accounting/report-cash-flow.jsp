<%@ page errorPage="error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Report_Cash_Flow"
	scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</HEAD>

<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
<%@include file="../portal/header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Report Cash Flow</h1>
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
						<li><a href=../accounting/index.jsp>Accounting</a> &gt;</li>
						<li><a href=report-cash-flow.jsp>Cash Flow</a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Cash Flow</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="post">
													<div class="form-element3">
														<label> Start Date<font color=red>*</font>: </label>
															<input name="txt_startdate" id="txt_startdate"
																type="text" class="form-control datepicker"
																value="<%=mybean.startdate %>" size="12" maxlength="10" />
															</td>
															<td align="right" valign="middle" nowrap>
													</div>

												<div class="form-element3">
														<label> End Date<font color=red>*</font>: </label>
															<input name="txt_enddate" id="txt_enddate" type="text"
																class="form-control datepicker"
							                              value="<%=mybean.enddate %>"
																size="12" maxlength="10" />
														</div>
													<div class="form-element3">
														<label> Period<font color="#ff0000">*</font>:&nbsp; </label>
															<select name="dr_period" class="form-control"
																id="dr_period">
																<%=mybean.PopulatePeriod()%>
															</select>
													</div>
															<center>
																<input name="submit_button" type="submit"
																	class="btn btn-success" id="submit_button" value="Go" />
																<input type="hidden" name="submit_button" value="Submit" />
															</center>
											</div>
										</form>

									</div>
								</div>
							</div>
							<%=mybean.StrHTML%>

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
