<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.preowned.ManagePreownedManufacturer_Update" scope="request"/>
<%mybean.doGet(request,response); %>    
<!DOCTYPE html>
<html>
    <head>
        <title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
        <meta http-equiv="pragma" content="no-cache">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
        <%@include file="../Library/css.jsp" %>
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
						<h1><%=mybean.status%>
							Pre-Owned Manufacturer
						</h1>
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
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="managepreownedmanufacturer.jsp?all=yes">List Pre-Owned Manufacturer</a> &gt;</li>
							<li><a href="managepreownedmanufacturer-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Pre-Owned Manufacturer</a><b>:</b></li>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					
						<div class="tab-pane" id="">
							<!-- BODY START -->
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center><%=mybean.status%> Pre-Owned Manufacturer</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<font color="red">*</font> are required.
												</font>
											</center>
											<div class="form-element6 form-element-center">
												<label>Manufacturer<font color="#ff0000">*</font>:</label>
													<input type="text" name="txt_carmanuf_name" class="form-control" id="txt_carmanuf_name" value="<%=mybean.carmanuf_name %>"/>      
											</div>
											<div class="form-element6 form-element-center">
											 	<%if (mybean.status.equals("Update") && !(mybean.carmanuf_entry_date == null) && !(mybean.carmanuf_entry_date.equals(""))) {%>
							                            <div class="form-element6">
							                             <label>Entry By:</label>
							                           <%=mybean.unescapehtml(mybean.carmanuf_entry_by)%>
							                           <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.carmanuf_entry_by%>"/>
							                            </div>
							                             <div class="form-element6">
							                             <label>Entry Date:</label>
							                           <%=mybean.carmanuf_entry_date%>
							                            <input type="hidden" name="entry_date" value="<%=mybean.carmanuf_entry_date%>"/>
							                            </div>
							                  	<%}%>  
												<% if (mybean.status.equals("Update") && !(mybean.carmanuf_modified_date == null) && !(mybean.carmanuf_modified_date.equals(""))) {%>
							                            <div class="form-element6">
							                             <label>Modified By:</label>
							                            <%=mybean.unescapehtml(mybean.carmanuf_modified_by)%>
							                                <input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.carmanuf_modified_by%>">
							                            </div>
							                             <div class="form-element6">
							                             <label>Modified Date:</label>
							                            <%=mybean.carmanuf_modified_date%>
							                                <input type="hidden" name="modified_date" value="<%=mybean.carmanuf_modified_date%>">
							                            </div>
							                  <%}%>  
							                  
							                  <%if(mybean.status.equals("Add")){%>
							                  <center>
								                  <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Pre-Owned Manufacturer" onClick="return SubmitFormOnce(document.form1, this);"/>
								                  <input type="hidden" name="add_button" id="add_button" value="yes">
							                  </center>
							                  <%}else if (mybean.status.equals("Update")){%>
							                   <center>
								                    <input type="hidden" name="update_button" id="update_button" value="yes">
								                    <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Pre-Owned Manufacturer" onClick="return SubmitFormOnce(document.form1, this);"/>
								                    <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Pre-Owned Manufacturer"/>
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
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
</body>
</html> 
