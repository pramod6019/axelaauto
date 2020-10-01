<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Lost_Case_Move"
	scope="request" />
<%mybean.doPost(request, response);%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
	<%@include file="../Library/css.jsp"%>
</HEAD>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1>Move Lost Case</h1>
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
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="../sales/lost-case-move.jsp">Move Lost Case</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Move Lost Case</div>
								</div>
								
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										
										<form name="formemp" class="form-horizontal" method="post">
										
													<div class="form-element6">
													<center> From:</center>
														<label>Lost Case1<font color="#ff0000">*</font>: </label>
														<select name="dr_enquiry_lostcase1_from_id"
																class="form-control" id="dr_enquiry_lostcase1_from_id"
																onchange="this.form.submit();">
																<%=mybean.PopulateLostCase1(mybean.enquiry_lostcase1_from_id)%>
														</select>
													</div>

													<div class="form-element6">
													<center> To:</center>
														<label>Lost Case1<font color="#ff0000">*</font>: </label>
														<select name="dr_enquiry_lostcase1_to_id"
																class="form-control" id="dr_enquiry_lostcase1_to_id"
																onchange="this.form.submit();">
																<%=mybean.PopulateLostCase1(mybean.enquiry_lostcase1_to_id)%>
														</select>
													</div>


													<div class="form-element6">
														<label>Lost Case2<font color="#ff0000">*</font>: </label>
														<select name="dr_enquiry_lostcase2_from_id"
																class="form-control" id="dr_enquiry_lostcase2_from_id"
																onchange="this.form.submit();">
																<%=mybean.PopulateLostCase2(mybean.enquiry_lostcase1_from_id, mybean.enquiry_lostcase2_from_id)%>

														</select>
													</div>

													<div class="form-element6">
														<label>Lost Case2<font color="#ff0000">*</font>: </label>
														<select name="dr_enquiry_lostcase2_to_id"
																class="form-control" id="dr_enquiry_lostcase2_to_id"
																onchange="this.form.submit();">
																<%=mybean.PopulateLostCase2(mybean.enquiry_lostcase1_to_id, mybean.enquiry_lostcase2_to_id)%>

														</select>
													</div>


											
													<div class="form-element6">
														<label>Lost Case3<font color="#ff0000">*</font>: </label>
														<select name="dr_enquiry_lostcase3_from_id"
																class="form-control" id="dr_enquiry_lostcase3_from_id">
																<%=mybean.PopulateLostCase3(mybean.enquiry_lostcase2_from_id, mybean.enquiry_lostcase3_from_id)%>
														</select>
													</div>

													<div class="form-element6">
														<label>Lost Case3<font color="#ff0000">*</font>: </label>
														<select name="dr_enquiry_lostcase3_to_id"
																class="form-control" id="dr_enquiry_lostcase3_to_id">
																<%=mybean.PopulateLostCase3(mybean.enquiry_lostcase2_to_id, mybean.enquiry_lostcase3_to_id)%>

														</select>
													</div>
												
												
											<center>
												<input type="hidden" name="update_button" value="yes" />
												<input name="movebutton" type="submit" class="btn btn-success"
													id="movebutton" onclick="checkForm();" value="Move" />
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
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
</body>
</HTML>
