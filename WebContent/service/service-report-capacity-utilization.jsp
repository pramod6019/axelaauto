<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.service.Service_Report_Capacity_Utilization"
	scope="request" />
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
						<h1>Capacity Utilization</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="service-report-capacity-utilization.jsp">Capacity
								Utilization</a><b>:</b></li>


					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="red"><b><%=mybean.msg%> </b></font><br></br>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Capacity
										Utilization</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" class="form-horizontal" name="frm1"
											id="frm1">
												<div class="form-element4">
													<label> Branch<font color=red>*</font>:&nbsp; </label>
														<%if(mybean.branch_id.equals("0")){%>
														<select name="dr_bay_branch_id" id="dr_bay_branch_id"
															class="form-control">
															<%=mybean.PopulateBranch(mybean.jc_branch_id, "", "3", "", request)%>
														</select>
														<%}else{%>
														<input type="hidden" name="dr_branch" id="dr_branch"
															value="<%=mybean.branch_id%>" />
														<%=mybean.getBranchName(mybean.jc_branch_id, mybean.comp_id)%>
														<%}%>
														<div class="hint" id="hint_dr_bay_id"></div>
												</div>

												<div class="form-element4">
													<label> Start Date<font color=red>*</font>:&nbsp; </label>
														<input name="txt_startdate" id="txt_startdate" type="text"
															class="form-control datepicker"
															value="<%=mybean.startdate %>" size="12" maxlength="10" />
												</div>
											
												<div class="form-element4">
													<label> End Date<font color=red>*</font>:&nbsp; </label>
														<input name="txt_enddate" id="txt_enddate" type="text"
															class="form-control datepicker"
															value="<%=mybean.enddate %>" size="12" maxlength="10" />
												</div>
												
												<div class="form-element12">
														<center>
															<input name="submit_button" type="submit"
																class="btn btn-success" id="submit_button" value="Go" />
															<input type="hidden" name="submit_button" value="Submit"></input>
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
