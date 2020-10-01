<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Advisor_Cal"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
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
					<div class="page-title">
						<h1>Advisor Calendar</h1>
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
						<li><a href="index.jsp">Service</a> &gt;</li>
						<li><a href="booking.jsp">Booking</a> &gt;</li>
						<li><a href="advisor-cal.jsp?<%=mybean.QueryString%>">Advisor
								Calendar</a><b>:</b></li>
					</ul>
					<center>
						<font color="red"><b><%=mybean.msg%></b></font>
					</center>
					<!-- END PAGE BREADCRUMBS -->
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Advisor Calendar</div>

								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br />
										<!-- START PORTLET BODY -->
										<form method="post" name="formemp" id="formemp"
											class="form-horizontal">
											<div class="container-fluid">
											<div class="form-element3">
													<label>Branch<font color="red">*</font>:</label>
														<%
												if (mybean.branch_id.equals("0")) { %> 
												<select name="dr_branch" id="dr_branch" class="form-control" onChange="showHint('../service/booking-check.jsp?calender=yes&exe_branch_id=' + GetReplace(this.value),'exeHint');">
											      <%=mybean.PopulateBranch(mybean.booking_branch_id,"", "1,3", "", request)%>
											                      </select> <%
											 											} else {%> 
											 								<input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>" />
											                        <%=mybean.getBranchName(mybean.booking_branch_id, mybean.comp_id)%>
																						<%
																							}
																						%>
												</div>
											<div class="form-element3">
											<label>Start Date:<font color=#ff0000><b> *</b></font></label>
										<input name="txt_booking_time_from" id="txt_booking_time_from"
										 value="<%=mybean.booking_time_from %>" size="10" maxlength="10"
										 class="form-control datepicker"  type="text" value="" />
											</div>	
												
										<div class="form-element3">
										<label>End Date:<font color=#ff0000><b> *</b></font></label>
											<input name="txt_booking_time_to" id="txt_booking_time_to"
											value="<%=mybean.booking_time_to %>" size="10" maxlength="10"
							 				class="form-control datepicker" type="text" value="" />
									</div>		
												<div class="form-element3">
														<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Submit" />
                                                <input name="submit_button" type="hidden" id="submit_button" value="Submit" />
											</div>
											<div class="row"></div>
											<div class="form-element3">
												<label>Service Advisor:</label> 
												<div>
												<span id="exeHint"> <%=mybean.PopulateExecutive(mybean.booking_branch_id,mybean.comp_id)%></span>
												</div>
											</div>	
											
										<div class="form-element3">
												<label>Model:</label>			 
											<div id="modelHint">
															<select name="dr_model" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_model" onChange="TestDriveCheck()" style="width:250px" >
                       							 <%=mybean.PopulateModel()%>
                  						    </select>
											</div>
										</div>
									</div>	
								</form>
									</div>
								</div>
							</div>
							<center><%=mybean.StrHTML%></center>
						</div>
					</div>
				</div>
			</div>
			
		</div>
	</div>



<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
<script type="text/javascript" src="../Library/smart.js"></script>
<script language="JavaScript" type="text/javascript">

<!--function ApptCheck() { //v1.0
	//var branch_id=document.getElementById("dr_branch").value;
	//var model_id=outputSelected(document.getElementById("dr_model").options);
	//showHint('../service/booking-check.jsp?model_id='+model_id+'&branch_id=' + GetReplace(branch_id),'vehHint');
//}  -->
</SCRIPT>
<script>
	$(function() {
		$("#txt_booking_time_from").datepicker({
			showButtonPanel : true,
			dateFormat : "dd/mm/yy"
		});

		$("#txt_booking_time_to").datepicker({
			showButtonPanel : true,
			dateFormat : "dd/mm/yy"
		});

	});
</script>
</body>
</HTML>
