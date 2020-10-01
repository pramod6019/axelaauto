<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Configure_Service" scope="request"/>
<%mybean.doGet(request, response);%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</head>
<body  onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
    <%@include file="../portal/header.jsp" %>
    <div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Configure Service</h1>
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
							<li><a href="../portal/home.jsp">Home</a> &gt; </li>
							<li><a href="../portal/manager.jsp#service">Business Manager</a> &gt; </li>
							<li><a href="../portal/manager.jsp#service">Service</a> &gt; </li>
							<li><a href="configure-service.jsp">Configure Service</a><b>:</b></li>
							
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					
					<div class="tab-pane" id="">
<!-- 					BODY START -->
                            <center><font color="#ff0000" ><b><%=mybean.msg%></b></font></center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Configure Service</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
                                         <form name="form1" class="form-horizontal" method="post">
                                         <center><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br>
                                                                </font>
                                         </center><br>
                                                                
											<div class="form-element4">
												<label>Enable Ticket:</label>
													<input type="checkbox" name="chk_config_service_ticket" <%=mybean.PopulateCheck(mybean.config_service_ticket)%>>
											</div>
											
                                             <div class="form-element4">
												<label>Enable Contract:</label>
													<input type="checkbox" name="chk_config_service_contract"  <%=mybean.PopulateCheck(mybean.config_service_contract)%> >
											</div>
											
											<div class="form-element4">
												<label>Enable Ticket Category:</label>
												 <input type="checkbox" name="chk_config_service_ticket_cat"  <%=mybean.PopulateCheck(mybean.config_service_ticket_cat)%> >
											</div>
											
											<div class="form-element4">
												<label>Enable Ticket Type:</label>
													<input type="checkbox" name="chk_config_service_ticket_type"  <%=mybean.PopulateCheck(mybean.config_service_ticket_type)%> >
											</div>
											
											<div class="form-element4">
												<label>Contract Ref No.:</label>
												<input type="checkbox" name="chk_config_service_contract_refno" <%=mybean.PopulateCheck(mybean.config_service_contract_refno)%>>
											</div>
											
											<div class="row"></div>
											
											<div class="form-body">
												<div class="form-element6 form-element-center">
													<label>Automated Tasks:</label>
													<div class="table-bordered" style="overflow-x:auto;">
														<table class="table table-bordered table-hover" data-filter="#filter">
                                                           <thead><tr>
                                                              <th align="center"><b>Task Type</b></th>
                                                              <th align="center"><b>Send Email</b></th>
                                                              <th align="center"><b>Format Email</b></th>
                                                              <th align="center"><b>Send SMS</b></th>
                                                              <th align="center"><b>Format SMS</b></th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            <tr>
                                                              <td>Ticket Contact:</td>
                                                              <td align="center" rowspan="2"><input type="checkbox" name="chk_config_ticket_new_email_enable" <%=mybean.PopulateCheck(mybean.config_ticket_new_email_enable)%>></td>
                                                              <td align="center"><a href="ticket-format.jsp?email=yes&status=Ticket&opt=config_ticket_new_email_format">Format</a></td>
                                                              <td align="center" rowspan="2"><input type="checkbox" name="chk_config_ticket_new_sms_enable" <%=mybean.PopulateCheck(mybean.config_ticket_new_sms_enable)%>></td>
                                                              <td align="center"><a href="ticket-format.jsp?sms=yes&status=Ticket&opt=config_ticket_new_sms_format">Format</a></td>
                                                            </tr>
                                                            
                                                            <tr>
                                                              <td>Ticket Executive:</td>
                                                              <td align="center"><a href="ticket-format.jsp?email=yes&status=Ticket for Executive&opt=config_ticket_new_email_exe_format">Format</a></td>
                                                             
                                                              <td align="center"><a href="ticket-format.jsp?sms=yes&status=Ticket for Executive&opt=config_ticket_new_sms_exe_format">Format</a></td>
                                                            </tr>
                                                            
                                                            <tr>
                                                              <td>Ticket Follow Up:</td>
                                                              <td align="center" rowspan="2"><input type="checkbox" name="chk_config_ticket_followup_email_enable" <%=mybean.PopulateCheck(mybean.config_ticket_followup_email_enable)%>></td>
                                                              <td align="center"><a href="ticket-format.jsp?email=yes&status=Ticket Follow-Up&opt=config_ticket_followup_email_format">Format</a></td>
                                                              <td align="center" rowspan="2"><input type="checkbox" name="chk_config_ticket_followup_sms_enable" <%=mybean.PopulateCheck(mybean.config_ticket_followup_sms_enable)%>></td>
                                                              <td align="center"><a href="ticket-format.jsp?sms=yes&status=Ticket Follow-Up&opt=config_ticket_followup_sms_format">Format</a></td>
                                                            </tr>
                                                            <tr>
                                                              <td>Ticket Follow Up Executive:</td>
                                                              
                                                              <td align="center"><a href="ticket-format.jsp?email=yes&status=Ticket Follow-Up for Executive&opt=config_ticket_followup_email_exe_format">Format</a></td>
                                                              <td align="center"><a href="ticket-format.jsp?sms=yes&status=Ticket Follow-Up for Executive&opt=config_ticket_followup_sms_exe_format">Format</a></td>
                                                            </tr>
                                                            
                                                            <tr>
                                                              <td>Ticket Closed:</td>
                                                              <td align="center" rowspan="2"><input name="chk_config_ticket_closed_email_enable" type="checkbox" id="chk_config_ticket_closed_email_enable" <%=mybean.PopulateCheck(mybean.config_ticket_closed_email_enable)%>></td>
                                                              <td align="center"><a href="ticket-format.jsp?email=yes&status=Ticket Closed&opt=config_ticket_closed_email_format">Format</a></td>
                                                              <td align="center" rowspan="2"><input type="checkbox" name="chk_config_ticket_closed_sms_enable" <%=mybean.PopulateCheck(mybean.config_ticket_closed_sms_enable)%>></td>
                                                              <td align="center"><a href="ticket-format.jsp?sms=yes&status=Ticket Closed&opt=config_ticket_closed_sms_format">Format</a></td>
                                                            </tr>
                                                            
                                                            <tr>
                                                              <td>Ticket Closed Executive:</td>
                                                             
                                                              <td align="center"><a href="ticket-format.jsp?email=yes&status=Ticket Closed for Executive&opt=config_ticket_closed_email_exe_format">Format</a></td>                                                                                                   <td align="center"><a href="ticket-format.jsp?sms=yes&status=Ticket Closed for Executive&opt=config_ticket_closed_sms_exe_format">Format</a></td>
                                                            </tr>
                                                            <tr>
                                                              <td>Ticket CRM:</td>
                                                              <td align="center" ></td>
                                                              <td align="center"><a href="ticket-format.jsp?email=yes&status=Ticket For CRM Executive&opt=config_ticket_crm_email_format">Format</a></td>
                                                              <td align="center" ></td>
                                                              <td align="center"></td>
                                                            </tr>
                                                            <tr>
                                                              <td>Ticket PBF:</td>
                                                              <td align="center" ></td>
                                                              <td align="center"><a href="ticket-format.jsp?email=yes&status=Ticket For PBF Executive&opt=config_ticket_pbf_email_format">Format</a></td>
                                                              <td align="center" ></td>
                                                              <td align="center"></td>
                                                            </tr>
                                                            <tr>
                                                              <td>Ticket PSF:</td>
                                                              <td align="center" ></td>
                                                              <td align="center"><a href="ticket-format.jsp?email=yes&status=Ticket For PSF Executive&opt=config_ticket_psf_email_format">Format</a></td>
                                                              <td align="center" ></td>
                                                              <td align="center"></td>
                                                            </tr>
                                                            
                                                            <tr>
                                                              <td>Ticket JCPSF:</td>
                                                              <td align="center" ></td>
                                                              <td align="center"><a href="ticket-format.jsp?email=yes&status=Ticket For JCPSF Executive&opt=config_ticket_jcpsf_email_format">Format</a></td>
                                                              <td align="center" ></td>
                                                              <td align="center"></td>
                                                            </tr>
                                                            <tr>
                                                              <td>Ticket Pre-Owned CRM:</td>
                                                              <td align="center" ></td>
                                                              <td align="center"><a href="ticket-format.jsp?email=yes&status=Ticket For Pre-Owned CRM Executive&opt=config_ticket_preowned_crm_email_format">Format</a></td>
                                                              <td align="center" ></td>
                                                              <td align="center"></td>
                                                            </tr>
                                                            </tbody>
                                                          </table>
                                                          </div>

												</div>
											</div>
											
											<div class="row"></div>
											
											<center>
												<input name="update_button" type="submit" class="btn btn-success" id="update_button" value="Update" />
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
         <%@include file="../Library/admin-footer.jsp" %>
        <%@include file="../Library/js.jsp"%>s

  </body>
</html>
