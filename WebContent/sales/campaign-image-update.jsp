<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Campaign_Image_Update" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<script language="JavaScript" type="text/javascript">

 function FormFocus()
{
  document.frmupload.filename.focus()
 }


        </script>
</HEAD>
    <body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0" <%=mybean.RefreshForm()%>>
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
						<h1>Add Image</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<%=mybean.LinkHeader%>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<b>Upload Image for Campaign ID (<%=mybean.campaign_id%>)
								</b>
							</center>
							<center>
								<font color="#ff0000"><b> <%=mybean.msg%><br />
								</b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>
										Image
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form EncType="multipart/form-data" class="form-horizontal"
											name="frmupload" method="post">
											<%if(mybean.displayform.equals("yes")) {%>
											<%if(mybean.img_value!=null && !mybean.img_value.equals("")){%>
											<center>
												<img
													src="../Thumbnail.do?campaignimg=<%=mybean.img_value%>&width=500" />
											</center>
											<br />
											<%}%>
											<div class="form-element6 form-element-center">
												<label> </label> <input NAME="filename" id="filename"
													Type="file" class="btn btn-success"
													value="<%=mybean.img_value%>" size="30" />
											</div>
											<center>Click the Browse button to select the
												document from your computer!</center>
											</br>
											<div class="form-element6 form-element-center">
												<label>Name<font color=red>*:</font>
												</label> <input type="text" name="txt_img_title" id="txt_img_title"
													class="form-control" value="<%=mybean.img_title%>"
													size="20" maxlength="25" />
											</div>
											<div class="form-element6 form-element-center">
												<label>Allowed Formats: </label> <b><%=mybean.ImageFormats%></b>
											</div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6 form-element-center">
												<label> Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
											</div>
											<div class="form-element6 form-element-center">
												<label> Entry Time: <%=mybean.entry_date%></label>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6 form-element-center">
												<label>Modified By: <%=mybean.unescapehtml(mybean.modified_by)%></label>
											</div>
											<div class="form-element6 form-element-center">
												<label>Modified Time: <%=mybean.modified_date%></label>
											</div>
											<%
												}
											%>
											<strong> <%if(mybean.status.equals("Add")){%>
												<center>
													<input type="submit" name="add_button"
														class="btn btn-success" value="Add Image" /> <input
														name="campaign_id" type="hidden" id="campaign_id"
														value="<%=mybean.campaign_id%>" />
												</center> <%}else if (mybean.status.equals("Update")){%>
												<center>
													<input type="submit" name="update_button"
														class="btn btn-success" value="Update Image" /> <input
														type="submit" name="delete_button" class="btn btn-success"
														value="Delete Image" onClick="return confirmdelete(this)" />
												</center> <%}%>
											</strong>

											<%} else {%>
											<center>
												<input name="btn_close" type="button"
													class="btn btn-success" onClick="window.close();"
													value="Close Window" />
											</center>
											<%}%>
										
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
