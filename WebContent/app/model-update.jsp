<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.app.Model_Update" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</HEAD>
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
						<h1><%=mybean.status%>&nbsp;Model
						</h1>
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
							<li><a href="../app/home.jsp">App</a> &gt;</li>
							<li><a href="../app/model-list.jsp?all=yes">List Model</a> &gt;</li>
							<li><a href="../app/model-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Model</a>:</li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Model
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
											
											<div class="form-element6">
												<label>Name<font color="#ff0000">*</font>: </label>
												<input name="txt_model_name" type="text" class="form-control" id="txt_model_name"
													value="<%=mybean.model_name %>" size="40" maxlength="255" />
											</div>

											<div class="form-element6">
												<label>Service Code:</label>
												<input name="txt_model_service_code" type="text" class="form-control" id="txt_model_service_code"
													value="<%=mybean.model_service_code %>" size="30" maxlength="20" />
											</div>


											<div class="form-element6">
												<label>Mileage:</label>
												<input name="txt_model_mileage" type="text" class="form-control" id="txt_model_mileage"
													value="<%=mybean.model_mileage %>" size="30" maxlength="30" />
											</div>
											
											<div class="form-element6">
												<label>Engine:</label>
												<input name="txt_model_engine" type="text" class="form-control" id="txt_model_engine"
													value="<%=mybean.model_engine %>" size="30" maxlength="30" />
											</div>
											
											<div class="form-element6">
												<label>Description<font color="#ff0000">*</font>: </label>
												<textarea name="txt_model_desc" cols="50" rows="4" class="form-control" id="txt_model_desc"
													onkeyup="charcount('txt_model_desc', 'span_txt_model_desc','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.model_desc%></textarea>
													&nbsp;<span id="span_txt_model_desc"> (255 characters) </span>
											</div>
											
											<div class="form-element6">
												<label>EMI:</label>
												<input name="txt_model_emi" type="text" class="form-control" id="txt_model_emi"
													value="<%=mybean.model_emi %>" size="30" maxlength="20" />
											</div>

											<div class="form-element6 form-element-margin">
												<label>Active: </label>
												<input type="checkbox" id="chk_model_active" name="chk_model_active"
													<%=mybean.PopulateCheck(mybean.model_active)%>>
											</div>

											<% if (mybean.status.equals("Update")&& !(mybean.entry_by == null) 
													&& !(mybean.entry_by.equals("")) ) { %>
											<div class="form-element12">
												<div class="form-element6">
													<label>Entry By:</label>
													<span><%=mybean.unescapehtml(mybean.entry_by)%>
														<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>">
													</span>
												</div>

											<%}%>
											<% if (mybean.status.equals("Update") && !(mybean.entry_date == null) 
													&& !(mybean.entry_date.equals("")) ) { %>
											
												<div class="form-element6">
													<label>Entry Date:</label>
													<span><%=mybean.entry_date%>
														<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
													</span>
												</div>
											</div>
											<%}%>
											<% if (mybean.status.equals("Update") && !(mybean.modified_by == null) 
													&& !(mybean.modified_by.equals("")) ) { %>
											<div class="form-element12">
												<div class="form-element6">
													<label>Modified By:</label>
													<span><%=mybean.unescapehtml(mybean.modified_by)%>
														<input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>">
													</span>
												</div>
											
											<%}%>
											<% if (mybean.status.equals("Update") && !(mybean.modified_date == null) 
													&& !(mybean.modified_date.equals(""))) { %>
											
												<div class="form-element6">
													<label>Modified Date:</label>
													<span><%=mybean.modified_date%>
														<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>">
													</span>
												</div>
											</div>
											<%}%>
											<div class="form-element12">
												<center>
													<%if(mybean.status.equals("Add")){%>
													<input name="button" type="submit" class="btn btn-success" id="button" value="Add Model"
														onClick="return SubmitFormOnce(document.form1, this);" />
													<input type="hidden" name="add_button" value="yes"/>
													<%}else if (mybean.status.equals("Update")){%>
													<input type="hidden" name="update_button" value="yes"/>
													<input name="button" type="submit" class="btn btn-success" id="button" value="Update Model"
														onClick="return SubmitFormOnce(document.form1, this);" />
													<input name="delete_button" type="submit" class="btn btn-success" id="delete_button"
														OnClick="return confirmdelete(this)" value="Delete Model" />
													<%}%>
												</center>
											</div>
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
	
	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
			document.formcontact.txt_customer_name.focus();
		}
	</script>
</body>
</HTML>
