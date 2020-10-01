<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Dash_Followup"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@ include file="../Library/css.jsp"%>

<script>
	// 	$(function() {
	// 		$("#txt_followup_time").datetimepicker({
	//  			controlType : 'select',
	// 			stepMinute : 5,
	// 			dateFormat : 'dd/mm/yy',
	// 			timeFormat : 'HH:mm',
	// 			hour : 10,
	// 			minute : 00,
	// 			showButtonPanel : true
	// 		});
	// 	});
 </script> 
</HEAD> 

<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%><%@ include file="../Library/preowned-dash.jsp"%>

	<div> <%=mybean.customerdetail%> </div>
	<div> <%=mybean.followupHTML%> </div>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<div class="page-content">
				<div class="container-fluid">
					<div>
						<% if (!mybean.status.equals("")) { %>

						<form name="Frmtasks" method="post" class="form-horizontal">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%> Follow-up
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<%
											if (mybean.status.equals("Update")) {
										%>
										<div class="form-element6 form-element">
											<div class="form-element12 form-element-margin">
												<label>Current Followup Time<font color=#ff0000><b> *</b></font>:&nbsp; </label>
												<span>
													<%=mybean.current_preownedfollowup_time%>
												</span>
											</div>
	
											<div class="form-element12">
												<label>Current Feedback Type<font color="#ff0000">*</font>: </label>
												<div>
													<select name="dr_feedbacktype" class="form-control" id="dr_feedbacktype" visible="true">
														<%=mybean.PopulateFeedbacktype()%>
													</select>
												</div>
											</div>
										</div>
										
										<div class="form-element6">
											<label>Current Feedback Description<font color="#ff0000">*</font>: </label>
											<textarea name="txt_preownedfollowup_desc" cols="50" rows="4" class="form-control" id="txt_preownedfollowup_desc"
												onKeyUp="charcount('txt_preownedfollowup_desc', 'span_txt_preownedfollowup_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.preownedfollowup_desc%></textarea>
											<span id="span_txt_preownedfollowup_desc">1000 characters</span>
										</div>
										
										<% } %>
										
										<% if (mybean.preowned_preownedstatus_id.equals("1")) { %>

										<div class="form-element6">
											<label>Next Follow-up Time<font color=#ff0000><b>*</b></font>: </label>
											<div>
												<input type="text" size="16" name="txt_followup_time"
													id="txt_followup_time" value="" class="form-control datetimepicker">
											</div>
										</div>

										<div class="form-element6">
											<label>Next Follow-up Type<font color="#ff0000">*</font>: </label>
											<div >
												<select name="dr_followuptype" class="form-control" id="dr_followuptype" visible="true">
													<%=mybean.PopulateFollowuptype()%>
												</select>
											</div>
										</div>
										
										<div class="form-element6">
											<center>
												<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Submit" />
												<input type="hidden" name="preowned_id" id="preowned_id" value="<%=mybean.preowned_id%>">
											</center>
										</div>
										
										<% } %>

										<% } %>
										
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="../Library/admin-footer.jsp"%>
	<%@ include file="../Library/js.jsp"%>
</body>
</html>
