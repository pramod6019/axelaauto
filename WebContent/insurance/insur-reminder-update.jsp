<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.insurance.Insur_Reminder_Update" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
		<%@include file="../Library/css.jsp"%>
		<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />
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
						<h1><%=mybean.status%>&nbsp;Insurance Reminder </h1>
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
							<li><a href="manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="insur-reminder-list.jsp?all=yes">List Insurance Reminders</a> &gt;</li>
							<li><a href="insur-reminder-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Insurance Reminder</a><b>:</b></li>
	
						</ul>
						<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>
										Insurance Reminder Details
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk 
												<font color="#ff0000"><b>*</b></font> are required. </font>
											</center>
											<!-- START PORTLET BODY -->
									<div class="form-element6">
										<label> Days<font color="#ff0000">*</font>: </label>
												<input name ="txt_insurreminder_days" type="text" class="form-control" 
												id="txt_insurreminder_days" onkeypress = "return allowNegativeNumber(event)"  
												value="<%=mybean.insurreminder_days %>" maxlength="4"/>
								    </div>
	                              
	                              <div class="form-element6 form-element-margin">
						          		<label> Email Enable: </label>
											<input id="chk_insurreminder_email_enable" type="checkbox" name="chk_insurreminder_email_enable" 
											<%=mybean.PopulateCheck(mybean.insurreminder_email_enable) %> />
								    </div>
								    
								    <div class="row"></div>
								    
								    <div class="form-element8 form-element-center">
										<label>Email Subject: </label>
										<input name="txt_insurreminder_email_sub" type="text" class="form-control" 
										id="txt_insurreminder_email_sub" value="<%=mybean.insurreminder_email_sub%>" maxlength="1000">
										</input>
									</div>
										
									<div class="form-element8 form-element-center">
										<label> Email Format: </label>
											<textarea name="txt_insurreminder_email_format" cols="70" rows="4"
											class="form-control summernote_1" id="txt_insurreminder_email_format"><%=mybean.insurreminder_email_format%></textarea>
										</div>
	                            
	                           
		                            <div class="form-element8 form-element-center">
										<label> Sms Format: </label>
											<textarea name="txt_insurreminder_sms_format" cols="70" rows="4"
											class="form-control summernote_1" id="txt_insurreminder_sms_format"
											onKeyUp="charcount('txt_insurreminder_sms_format', 'span_txt_insurreminder_sms_format','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.insurreminder_sms_format%></textarea>
									</div>
								
									<div class="form-element6">
										<label> Sms Enable: </label>
											<input id="chk_insurreminder_sms_enable" type="checkbox" name="chk_insurreminder_sms_enable"
											 <%=mybean.PopulateCheck(mybean.insurreminder_sms_enable) %> />
								    </div>
							    
                              		<table class="table table-hover table-bordered">
												<thead>
													<tr>
														<th colspan="2" align="center">Substitution Variables</th>
													</tr>
												</thead>
												<tr>
                                                    <td align="right">Vehicle ID:</td>
                                                    <td align="left">[VEHICLEID]</td>
                                                  </tr>
                                                    <tr>
                                                    <td align="right">Vehicle Reg. No.:</td>
                                                    <td align="left">[VEHICLEREGISTRATIONNO]</td>
                                                  </tr>
                                                  <tr> 
                                                    <td align="right">Model Name:</td>
                                                    <td align="left">[MODELNAME]</td>
                                                  </tr>
                                                   <tr>
                                                    <td align="right">Variant Name:</td>
                                                    <td align="left">[VARIANTNAME]</td>
                                                  </tr>
                                                  <tr>
                                                    <td align="right">Customer ID:</td>
                                                    <td align="left">[CUSTOMERID]</td>
                                                  </tr>
                                                    <tr>
                                                    <td align="right">Customer Name:</td>
                                                    <td align="left">[CUSTOMERNAME]</td>
                                                  </tr>
                                                    <tr>
                                                    <td align="right"> Contact Name:</td>
                                                    <td align="left">[CONTACTNAME]</td>
                                                  </tr>
                                                    <tr>
                                                    <td align="right"> Contact Job Title:</td>
                                                    <td align="left">[CONTACTJOBTITLE]</td>
                                                  </tr>
                                                    <tr>
                                                    <td align="right"> Contact Mobile1:</td>
                                                    <td align="left">[CONTACTMOBILE1]</td>
                                                  </tr>
                                                    <tr>
                                                    <td align="right"> Contact Phone1:</td>
                                                    <td align="left">[CONTACTPHONE1]</td>
                                                  </tr>
                                                    <tr>
                                                    <td align="right"> Contact Email1:</td>
                                                    <td align="left">[CONTACTEMAIL1]</td>
                                                  </tr>
                                                    <tr>
                                                    <td align="right">Insurance Executive Name:</td>
                                                    <td align="left">[INSUREXENAME]</td>
                                                  </tr>
                                                    <tr>
                                                    <td align="right">Insurance Executive Job Title:</td>
                                                    <td align="left">[INSUREXEJOBTITLE]</td>
                                                  </tr>
                                                    <tr>
                                                    <td align="right">Insurance Executive Mobile1:</td>
                                                    <td align="left">[INSUREXEMOBILE1]</td>
                                                  </tr>
                                                    <tr>
                                                    <td align="right">Insurance Executive Phone1:</td>
                                                    <td align="left">[INSUREXEPHONE1]</td>
                                                  </tr>
                                                    <tr>
                                                    <td align="right">Insurance Executive Email1:</td>
                                                    <td align="left">[INSUREXEEMAIL1]</td>
                                                  </tr>
                                                  </table>
                                                
                              <% if (mybean.status.equals("Update") && !(mybean.insurreminder_entry_by == null) && !(mybean.insurreminder_entry_by.equals(""))) { %>
											
											<div class="form-element6">
												<label>Entry By: </label>
													<%=mybean.unescapehtml(mybean.insurreminder_entry_by)%>
													<input type="hidden" name="insurreminder_entry_by"
														value="<%=mybean.insurreminder_entry_by%>">
											</div>
											
											<% } %>
											<% if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>
									
											<div class="form-element6">
												<label>Entry Date: </label>
													<%=mybean.entry_date%><input type="hidden"
														name="insurreminder_entry_date" value="<%=mybean.entry_date%>">
											</div>

											<% } %>
											<% if (mybean.status.equals("Update") && !(mybean.insurreminder_modified_by == null) && !(mybean.insurreminder_modified_by.equals(""))) { %>
											
											<div class="form-element6">
												<label>Modified By: </label>
													<%=mybean.unescapehtml(mybean.insurreminder_modified_by)%>
													<input type="hidden" name="insurreminder_modified_by"
														value="<%=mybean.insurreminder_modified_by%>">
											</div>

											<% } %>
											<% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>

											<div class="form-element6">
												<label>Modified Date: </label>
													<%=mybean.modified_date%>
													<input type="hidden" name="insurreminder_modified_date"
														value="<%=mybean.modified_date%>">
											</div>

											<% } %>
											
                            					<center>
													<% if (mybean.status.equals("Add")) { %> 
													<input name="button" type="submit" class="btn btn-success"
														id="button" value="Add Insurance Reminder"
														onClick="return SubmitFormOnce(document.form1, this);" />
														<input type="hidden" name="add_button" value="yes">
														<% } else if (mybean.status.equals("Update")) { %>
														 <input type="hidden" name="update_button" value="yes">
														<input name="button" type="submit" class="btn btn-success"
														id="button" value="Update Insurance Reminder"
														onClick="return SubmitFormOnce(document.form1, this);" />
														<input name="delete_button" type="submit"
														class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)" value="Delete Insurance Reminder" />
														<% } %>
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

<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
<script src="../assets/js/components-editors.min.js" type="text/javascript"></script>
 <script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
<script type="text/javascript">
function FormFocus() { ////v1.0
  document.form1.txt_insurreminder_days.focus()
}
function allowNegativeNumber(e){
	var charCode = (e.which)? e.which:event.keyCode
			if(charCode>31 && (charCode<45 || charCode>57)){
				return false;
			}
	return true;
	}
</script>
<script type="text/javascript">
														CKEDITOR
																.replace(
																		'txt_insurreminder_sms_format',
																		{
																			uiColor : hexc($(
																					"a:link")
																					.css(
																							"color")),

																		});
													</script>
</body>
</html>
	
