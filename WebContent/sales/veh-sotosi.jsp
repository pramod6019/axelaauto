<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Veh_SoToSi" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</HEAD>
<body <%if (mybean.advSearch.equals(null) || mybean.advSearch.equals("")) {%> onLoad="LoadRows();FormFocus();" <%}%>
	class="page-container-bg-solid page-header-menu-fixed">
	<%
		if (mybean.group.equals("")) {
	%>
		<%@include file="../portal/header.jsp"%>
	<%
		}
	%>
	<div class="page-container">
		<!-- 	BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- 	BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD -->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>List SalesOrder</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<%=mybean.LinkHeader%>
			<!-- END PAGE HEAD
			------------BEGIN PAGE CONTENT BODY -------------->
			<div class="page-content">
					<div class="page-content-inner">
				<div class="container-fluid">

						<div class="tab-pane" id="">
							<!-- 	BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font> <br>
							</center>

							<div class="page-content">
								<div class="container-fluid" style="background-color: white">

<!-- 									<div class="page-content-inner"> -->
										<div class="tab-pane" id="">
											<!-- 					BODY START -->

											<form name="form1" class="form-horizontal" method="post">
												<div class="portlet box  ">
													<div class="portlet-title" style="text-align: center">
														<div class="caption" style="float: none">SalesOrder</div>
													</div>
													<div class="portlet-body portlet-empty">
														<div class="tab-pane" id="">
															<!-- START PORTLET BODY -->
															<br>
															<div class="container-fluid">
																<div class="col-md-4">
																	<label class="col-md-4 control-label">Start
																		Date<font color=red>*</font>:
																	</label>
																	<div class="col-md-6">
																		<input name="txt_startdate" id="txt_startdate"
																			type="text" class="form-control date-picker"
																			data-date-format="dd/mm/yyyy"
																			value="<%=mybean.startdate%>" size="12"
																			maxlength="10" />
																	</div>
																</div>
																<div class="col-md-4">
																	<label class="col-md-4 control-label">End Date<font
																		color=red>*</font>:
																	</label>
																	<div class="col-md-6">
																		<input name="txt_enddate" id="txt_enddate" type="text"
																			class="form-control date-picker"
																			data-date-format="dd/mm/yyyy"
																			value="<%=mybean.enddate%>" size="12" maxlength="10" />
																	</div>
																</div>
																<div class="col-md-4">
																	<label class="col-md-4 col-xs-5 control-label"></label>
																	<div class="col-md-6" style="padding: 5px 0">
																		<input name="submit_button" type="submit"
																			class="btn btn-success" id="submit_button" value="Go" />
																	</div>
																</div>
																<br>
																<center>
																	<%
																		if (mybean.TotalRecords > 0) {
																	%>
																	<input type='submit' class='btn btn-success'
																		value='Add Invoive' id='allinvoice' name='allinvoice' />
																	<%
																		}
																	%>
																</center>
															</div>

														</div>
													</div>
												</div>
												<!-- 								<center> -->
												<%-- 					<strong><%=mybean.RecCountDisplay%></strong> --%>
												<!-- 					</center> -->
												<!-- 					<center> -->
												<%-- 						<%=mybean.PageNaviStr%> --%>
												<!-- 					</center> -->
												<!--                             <div class="col-md-12"> -->
												<center>
													<%=mybean.StrHTML%>
												</center>
												<!--                            </div>  -->
												<!-- 					<center> -->
												<%-- 						<%=mybean.PageNaviStr%> --%>
												<!-- 					</center> -->
											</form>

										</div>
<!-- 									</div> -->
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>

	<%@include file="../Library/js.jsp"%>

	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
			//document.form1.dr_branch_id.focus()
		}
	</script>

</body>
</HTML>    
