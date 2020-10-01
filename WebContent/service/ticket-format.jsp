<%-- 
    Document : ticket-add
    Created on: Feb 11, 2013
    Author   : Gurumurthy TS
--%>
<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Ticket_Format"
	scope="request" />
<%mybean.doPost(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">

<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />


<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript"
	src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
<script language="JavaScript" type="text/javascript">

            function FormFocus() { //v1.0
                document.form1.txt_format.focus()
            }
        </script>
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>
							Format For
							<%=mybean.status%></h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="ticket.jsp">Ticket</a> &gt;</li>
						<li><a href="ticket-list.jsp?all=yes">List Tickets</a> &gt;</li>
						<li><a href="ticket-format.jsp?<%=mybean.QueryString%>">
								Format For <%=mybean.status%></a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Format For
										<%=mybean.title%></div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="post">
											<%if (mybean.email.equals("yes")) {%>
											<div class="form-body">
												<div class="form-group">
													<label class="col-md-3 control-label">Subject:</label>
													<div class="col-md-6">
														<input name="txt_subject" type="text" class="form-control"
															id="txt_subject" value="<%=mybean.subject%>" size="75"
															maxlength="1000">
													</div>
												</div>
											</div>
											<%} %>
											
											<textarea name="txt_format" cols="70" rows="10"
												class="form-control" id="txt_format"
												<%if (mybean.sms.equals("yes")) {%>
												onKeyUp="charcount('txt_format', 'span_txt_format','<font color=red>({CHAR} characters left)</font>', '1000')"
												<%}%>><%=mybean.format_desc%></textarea>
											<br> <%if (mybean.email.equals("yes")) {%> <script
													type="text/javascript">
                                                                                            CKEDITOR.replace('txt_format',
                                                                                            {
                                                                       
                                                                                                uiColor: hexc($("a:link").css("color")),
                                                                                                height: '300px'
                                                                                            });
                                                                                        </script>
												<%} else {%> <span id="span_txt_format"> 1000
													characters </span> <%}%>
													
												<center>
													<input name="update_button" type="submit"
														class="btn btn-success" id="update_button" value="Update" />
												</center>
												<table width="95%" class="table">
													<tr>
														<th colspan="2" align="center">Substitution Variables
														</th>
													</tr>
													<tr>
														<td width="50%" align="right">Ticket ID:</td>
														<td align="left">[TICKETID]</td>
													</tr>
													<tr>
														<td align="right">Branch Name:</td>
														<td align="left">[BRANCHNAME]</td>
													</tr>
													<%if(!mybean.status.equals("Ticket For JCPSF Executive") && !mybean.status.equals("Ticket For Pre-Owned CRM Executive")){%>
													<tr>
														<td align="right">Enquiry ID:</td>
														<td align="left">[ENQUIRYID]</td>
													</tr>
													<tr>
														<td align="right">Enquiry Date:</td>
														<td align="left">[ENQUIRYDATE]</td>
													</tr>
													<tr>
														<td align="right">SOE:</td>
														<td align="left">[SOE]</td>
													</tr>
													<% }%>
													<%if(mybean.status.equals("Ticket For Pre-Owned CRM Executive")){%>
													<tr>
														<td align="right">Pre-Owned ID:</td>
														<td align="left">[PREOWNEDID]</td>
													</tr>
													<tr>
														<td align="right">Pre-Owned Date:</td>
														<td align="left">[PREOWNEDDATE]</td>
													</tr>
													<% }%>
													<%if(!mybean.status.equals("Ticket For CRM Executive") 
															&& !mybean.status.equals("Ticket For JCPSF Executive") 
															&& !mybean.status.equals("Ticket For Pre-Owned CRM Executive")){%>
													<tr>
														<td align="right">SO Date:</td>
														<td align="left">[SODATE]</td>
													</tr>
													<tr>
														<td align="right">Delivery Date:</td>
														<td align="left">[DELIVERYDATE]</td>
													</tr>
													<%} %>
													<tr>
														<td align="right">Contact ID:</td>
														<td align="left">[CONTACTID]</td>
													</tr>
													<tr>
														<td align="right">Contact Name:</td>
														<td align="left">[CONTACTNAME]</td>
													</tr>


													<tr>
														<td align="right">Contact Mobile1:</td>
														<td align="left">[CONTACTMOBILE1]</td>
													</tr>
													<tr>
														<td align="right">Contact Email1:</td>
														<td align="left">[CONTACTEMAIL1]</td>
													</tr>
													
														<%if(!mybean.status.equals("Ticket For JCPSF Executive") && !mybean.status.equals("Ticket For Pre-Owned CRM Executive")){%>
													<tr>
														<td align="right">Sales Executive:</td>
														<td align="left">[SALESEXE]</td>
													</tr>
													<tr>
														<td align="right">Team Leader:</td>
														<td align="left">[TEAMLEAD]</td>
													</tr>
													<%}%>
														<%if(mybean.status.equals("Ticket For Pre-Owned CRM Executive")){%>
													<tr>
														<td align="right">Pre-Owned Executive:</td>
														<td align="left">[PREOWNEDEXE]</td>
													</tr>
													<%}%>
													<%if(mybean.status.equals("Ticket For CRM Executive") || mybean.status.equals("Ticket For PBF Executive")
															|| mybean.status.equals("Ticket For PSF Executive") || mybean.status.equals("Ticket For Pre-Owned CRM Executive")){ %>
													<tr>
														<td align="right">CRM Day:</td>
														<td align="left">[CRMDAY]</td>
													</tr>
													<%} %>
													
													
													<%if(mybean.status.equals("Ticket For CRM Executive") || mybean.status.equals("Ticket For PBF Executive")
															|| mybean.status.equals("Ticket For PSF Executive") || mybean.status.equals("Ticket For Pre-Owned CRM Executive")){ %>
														<tr>
														<td align="right">CRM Executive:</td>
														<td align="left">[CRMEXE]</td>
													</tr>
													<tr>
														<td align="right">CRM Executive Jobtitle:</td>
														<td align="left">[CRMEXEJOBTITLE]</td>
													</tr>
													<tr>
														<td align="right">CRM Executive Mobile1:</td>
														<td align="left">[CRMEXEMOBILE1]</td>
													</tr>
													<tr>
														<td align="right">CRM Executive Email1:</td>
														<td align="left">[CRMEXEEMAIL1]</td>
													</tr>

													<%}%>
													<%if(!mybean.status.equals("Ticket For CRM Executive") 
															&& !mybean.status.equals("Ticket For PBF Executive")
															&& !mybean.status.equals("Ticket For PSF Executive")
															 && !mybean.status.equals("Ticket For JCPSF Executive")
															 && !mybean.status.equals("Ticket For Pre-Owned CRM Executive")) { %>
													<tr>
														<td align="right">Executive Name:</td>
														<td align="left">[EXECUTIVENAME]</td>
													</tr>

													<tr>
														<td align="right">Executive Job Title:</td>
														<td align="left">[EXEJOBTITLE]</td>
													</tr>
													<tr>
														<td align="right">Executive Mobile1:</td>
														<td align="left">[EXEMOBILE1]</td>
													</tr>
													<tr>
														<td align="right">Executive Mobile2:</td>
														<td align="left">[EXEMOBILE2]</td>
													</tr>
													<tr>
														<td align="right">Executive Email1:</td>
														<td align="left">[EXEEMAIL1]</td>
													</tr>
													<tr>
														<td align="right">Executive Email2:</td>
														<td align="left">[EXEEMAIL2]</td>
													</tr>
													<%} %>

													<%//if (mybean.status.equals("Invoice Registration")) {%>
													<tr>
														<td align="right">Ticket Subject:</td>
														<td align="left">[TICKETSUBJECT]</td>
													</tr>
													<tr>
														<td align="right">VOC:</td>
														<td align="left">[VOC]</td>
													</tr>
													<tr>
														<td align="right">Concern:</td>
														<td align="left">[CONCERN]</td>
													</tr>
													<tr>
														<td align="right">Model Name:</td>
														<td align="left">[MODELNAME]</td>
													</tr>
													<tr>
														<td align="right">Variant Name:</td>
														<td align="left">[VARIANT]</td>
													</tr>
													
													<%if( mybean.status.equals("Ticket For PBF Executive")
															&& !mybean.status.equals("Ticket For PSF Executive")) { %>
													<tr>
														<td align="right">Colour:</td>
														<td align="left">[COLOUR]</td>
													</tr>
													<tr>
														<td align="right">SO No:</td>
														<td align="left">[SONO]</td>
													</tr>
													
													<%} %>
													
													<%if(!mybean.status.equals("Ticket For CRM Executive") 
															&& !mybean.status.equals("Ticket For PBF Executive")
															&& !mybean.status.equals("Ticket For PSF Executive")
															&& !mybean.status.equals("Ticket For Pre-Owned CRM Executive")) { %>
													<tr>
														<td align="right">Reg. No.:</td>
														<td align="left">[REGNO]</td>
													</tr>
													<%if(mybean.status.equals("Ticket For JCPSF Executive")){ %>
													<tr>
														<td align="right">Chassis No.:</td>
														<td align="left">[CHASSISNO]</td>
													</tr>
													<tr>
														<td align="right">Engine No.:</td>
														<td align="left">[ENGINENO]</td>
													</tr>
													<tr>
														<td align="right">PSF Day:</td>
														<td align="left">[PSFDAY]</td>
													</tr>
													<tr>
														<td align="right">JCPSF Executive:</td>
														<td align="left">[JCPSFEXE]</td>
													</tr>
													<tr>
														<td align="right">JCPSF Executive Jobtitle:</td>
														<td align="left">[JCPSFEXEJOBTITLE]</td>
													</tr>
													<tr>
														<td align="right">JCPSF Executive Mobile1:</td>
														<td align="left">[JCPSFEXEMOBILE1]</td>
													</tr>
													<tr>
														<td align="right">JCPSF Executive Email1:</td>
														<td align="left">[JCPSFEXEEMAIL1]</td>
													</tr>
													<tr>
														<td align="right">Service Adviser:</td>
														<td align="left">[SAEXE]</td>
													</tr>
													<tr>
														<td align="right">Service Adviser Jobtitle:</td>
														<td align="left">[SAEXEJOBTITLE]</td>
													</tr>
													<tr>
														<td align="right">Service Adviser Mobile1:</td>
														<td align="left">[SAEXEMOBILE1]</td>
													</tr>
													<tr>
														<td align="right">Service Adviser Email1:</td>
														<td align="left">[SAEXEEMAIL1]</td>
													</tr>
													<tr>
														<td align="right">Technician Name:</td>
														<td align="left">[TECHNICIANNAME]</td>
													</tr>
													<%} %>
													
													<%} %>
													<tr>
														<td align="right">Ticket Time:</td>
														<td align="left">[TICKETTIME]</td>
													</tr>
													<tr>
														<td align="right">Due Time:</td>
														<td align="left">[DUETIME]</td>
													</tr>
													<%if (mybean.status.equals("Ticket for Executive") 
															|| mybean.status.equals("Ticket Follow-Up for Executive") 
															|| mybean.status.equals("Ticket Closed for Executive")
															|| mybean.status.equals("Ticket For CRM Executive")
															|| mybean.status.equals("Ticket For PBF Executive")
															|| mybean.status.equals("Ticket For PSF Executive")
															|| mybean.status.equals("Ticket For JCPSF Executive")
															|| mybean.status.equals("Ticket For Pre-Owned CRM Executive")) {%>
													<tr>
														<td align="right">Ticket Owner:</td>
														<td align="left">[TICKETOWNER]</td>
													</tr>
													<%}%>
													<%//}%>
													<%//if (mybean.status.equals("Receipt Registration")) {%>
													<tr>
														<td align="right">Ticket Status:</td>
														<td align="left">[TICKETSTATUS]</td>
													</tr>

													<tr>
														<td align="right">Ticket Category:</td>
														<td align="left">[TICKETCAT]</td>
													</tr>
													<tr>
														<td align="right">Ticket Type:</td>
														<td align="left">[TICKETTYPE]</td>
													</tr>
													<tr>
														<td align="right">Department:</td>
														<td align="left">[DEPARTMENT]</td>
													</tr>
													<tr>
														<td align="right">Priority:</td>
														<td align="left">[PRIORITY]</td>
													</tr>

													<tr>
														<td align="right">Customer ID:</td>
														<td align="left">[CUSTOMERID]</td>
													</tr>
													<tr>
														<td align="right">Customer Name:</td>
														<td align="left">[CUSTOMERNAME]</td>
													</tr>



													<%if(mybean.status.equals("Ticket Follow-Up") || mybean.status.equals("Ticket Follow-Up for Executive")) {%>
													<tr>
														<td align="right">Follow-up Description:</td>
														<td align="left">[FOLLOWUPDESC]</td>
													</tr>
													<%}%>

												</table>
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
</body>
</HTML>
