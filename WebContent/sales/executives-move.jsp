<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Executives_Move"
	scope="request" />
<%mybean.doPost(request, response);%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</head>
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
						<h1>Move Consultants</h1>
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
							<li><a href="../sales/executives-move.jsp">Move Consultants</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->


						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Move Consultants</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<form name="formemp" class="form-horizontal" method="post">

											<div class="form-element4">
												<center>From</center>
												<label> Sales Consultant 1<font color="#ff0000">*</font>: </label>
												<select name="dr_oppr_exe1_from_id" class="form-control"
													id="dr_oppr_exe1_from_id">
													<%=mybean.PopulateExecutive(mybean.oppr_exe1_from_id, mybean.oppr_exe1_module_id)%>
												</select>
											</div>



											<div class="form-element4">
												<center>Module</center>
												<label> Module<font color="#ff0000">*</font>: </label>
												<select name="dr_oppr_exe1_module_id" class="form-control"
													id="dr_oppr_exe1_module_id"
													onChange="document.formemp.submit();">
													<%=mybean.PopulateMove()%>
												</select>
											</div>


											<div class="form-element4">
												<center>To</center>
												
												<label> Sales Consultant 1<font color="#ff0000">*</font>: </label>
												 <select name="dr_oppr_exe1_to_id" class="form-control"
													id="dr_oppr_exe1_to_id">
													<%=mybean.PopulateExecutive(mybean.oppr_exe1_to_id, mybean.oppr_exe1_module_id) %>
												</select> 
												
												<label> Sales Consultant 2: </label> 
												<span id="span_exe2_to"> 
													<select name="dr_oppr_exe2_to_id"
													class="form-control" id="dr_oppr_exe2_to_id"
													onChange="populateExecutives();">
														<%=mybean.PopulateExecutive(mybean.oppr_exe2_to_id, mybean.oppr_exe1_module_id) %>
													</select>
												</span> 
												
												<label> Sales Consultant 3: </label> 
												<span id="span_exe3_to"> 
													<select name="dr_oppr_exe3_to_id"
													class="form-control" id="dr_oppr_exe3_to_id">
														<%=mybean.PopulateExecutive(mybean.oppr_exe3_to_id, mybean.oppr_exe1_module_id) %>
													</select>
												</span> 
												
												<label> Sales Consultant 4: </label>
												 <span id="span_exe4_to"> 
												 	<select name="dr_oppr_exe4_to_id"
													class="form-control" id="dr_oppr_exe4_to_id">
														<%=mybean.PopulateExecutive(mybean.oppr_exe4_to_id, mybean.oppr_exe1_module_id) %>
													</select>
												</span> 
												
												<label> Sales Consultant 5: </label>
												<span id="span_exe5_to">
													<select name="dr_oppr_exe5_to_id"
													class="form-control" id="dr_oppr_exe5_to_id">
														<%=mybean.PopulateExecutive(mybean.oppr_exe5_to_id, mybean.oppr_exe1_module_id) %>
													</select>
												</span>
												
												<label> Sales Consultant 6: </label>
												<span id="span_exe6_to">
													<select name="dr_oppr_exe6_to_id"
														class="form-control" id="dr_oppr_exe6_to_id">
															<%=mybean.PopulateExecutive(mybean.oppr_exe6_to_id, mybean.oppr_exe1_module_id) %>
													</select>
												</span>
												
												 <label> Sales Consultant 7: </label> <span
													id="span_exe7_to"> <select name="dr_oppr_exe7_to_id"
													class="form-control" id="dr_oppr_exe7_to_id">
														<%=mybean.PopulateExecutive(mybean.oppr_exe7_to_id, mybean.oppr_exe1_module_id) %>
												</select>
												</span> 
												
												<label> Sales Consultant 8: </label> 
												<span id="span_exe8_to">
													<select name="dr_oppr_exe8_to_id"
													class="form-control" id="dr_oppr_exe8_to_id">
														<%=mybean.PopulateExecutive(mybean.oppr_exe8_to_id, mybean.oppr_exe1_module_id) %>
													</select>
												</span> 
												
												<label> Sales Consultant 9: </label> 
												<span id="span_exe9_to"> <select name="dr_oppr_exe9_to_id"
													class="form-control" id="dr_oppr_exe9_to_id">
														<%=mybean.PopulateExecutive(mybean.oppr_exe9_to_id, mybean.oppr_exe1_module_id) %>
												</select>
												</span> 
												
												<label> Sales Consultant 10: </label> 
												<span id="span_exe10_to"> <select
													name="dr_oppr_exe10_to_id" class="form-control"
													id="dr_oppr_exe10_to_id">
														<%=mybean.PopulateExecutive(mybean.oppr_exe10_to_id, mybean.oppr_exe1_module_id) %>
												</select>
												</span>
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
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

</body>
</HTML>
