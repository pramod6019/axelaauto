<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.JobCard_Authorize"
	scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>

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
						<h1>Job Card Authorize</h1>
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
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="../service/jobcard.jsp">Job Cards</a> &gt;</li>
						<li><a href="../service/jobcard-list.jsp?all=yes">List
								Job Cards</a> &gt;</li>
						<li><a href="../service/jobcard-list.jsp?jc_id=<%=mybean.jc_id%>">JC
								ID: <%=mybean.jc_id%></a> &gt;</li>
						<li><a
							href="../service/jobcard-authorize.jsp?jc_id=<%=mybean.jc_id%>">Job
								Card Authorize</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center>Authorize Job Card</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<font color=#ff0000>*</font> are required.
												</font>
											</center>
											</br>
											<div class="form-element2 form-element-center">
												<label >Job Card No.:
												</label>
													<a href="jobcard-list.jsp?jc_id=<%=mybean.jc_id%>"><%=mybean.jc_no%></a>
											</div>
											<div class="form-element2 form-element-center">
												<label >Job Card ID: </label>
													<%=mybean.jc_id%>
											</div>
											<div class="form-element2 form-element-center">
												<label >Customer: </label>
													<%=mybean.link_customer_name%>
											</div>
											<div class="form-element2 form-element-center">
												<label >Authorize: </label>
													<input id="chk_jc_auth" type="checkbox" name="chk_jc_auth"
														<%=mybean.PopulateCheck(mybean.jc_auth)%> />
											</div>

											<%if (!(mybean.jc_auth_date == null) && !(mybean.jc_auth_date.equals(""))) {%>
											<div class="form-element2 form-element-center">
												<label >Authorized by:
												</label>
													<%=mybean.unescapehtml(mybean.jc_authorized_by)%>
													<input name="jc_authorized_by" type="hidden"
														id="jc_authorized_by"
														value="<%=mybean.unescapehtml(mybean.jc_authorized_by)%>" />
											</div>
											<div class="form-element2 form-element-center">
												<label >Authorized
													Date: </label>
													<%=mybean.jc_authdate%>
													<input type="hidden" id="jc_authdate" name="jc_authdate"
														value="<%=mybean.jc_authdate%>" />
											</div>
											<%}%>

											<center>
												<input type="hidden" id="update_button" name="update_button"
													value="yes" /> <input type="hidden" id="Update"
													name="Update" value="yes" /> <input name="updatebutton"
													id="updatebutton" type="submit" class="btn btn-success"
													value="Update Job Card"
													onClick="return SubmitFormOnce(document.form1, this);" />
											</center>
										</form>
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
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>

</body>
</HTML>

