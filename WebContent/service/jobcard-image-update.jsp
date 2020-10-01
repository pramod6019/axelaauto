<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.JobCard_Image_Update" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
    <body   <%=mybean.RefreshForm()%>>
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
					<center><b>Upload Image for Job Card ID (<%=mybean.jc_id%>)</b></center>
					<center><font color="#ff0000"><b>
                            <%=mybean.msg%><br/>
                    </b></font></center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>
                                            Image</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
                                      <form EncType="multipart/form-data" class="form-horizontal" name="frmupload" method="post">
                                     <%if(mybean.displayform.equals("yes")) {%>
                                     <div class="form-element6 form-element-center">
												<label >Name<font
													color=red>*:</font>
												</label>
													<input type="text" name="txt_img_title" id="txt_img_title"
														class="form-control" value="<%=mybean.img_title%>" size="20"
														maxlength="25" />
											</div>
                                      <%if(mybean.img_value!=null && !mybean.img_value.equals("")){%>
                                                <center><img src="../Thumbnail.do?jcimg=<%=mybean.img_value%>&width=500"/></center>
                                                <br/>
                                                <%}%>
                                                
                                    	 <div class="form-element6 form-element-center">
                                    	 <label >Select Document<font color=red>*</font>: </label>
													<input NAME="filename" id="filename" Type="file" class="btn btn-success" value="<%=mybean.img_value%>" size="30" />

											</div>
											<strong>
											<div align="center">
												<font size="">Click the Browse button to select the
													document from your computer!</font>
											</div>
											<div colspan="2" align="center">
												Allowed Formats: <b><%=mybean.ImageFormats%></b>
											</div>
                                           <%if(mybean.status.equals("Add")){%>
                                                            <center><input type="submit" name="add_button" class="btn btn-success" value="Add Image"  />
                                                            <input name="jc_id" type="hidden" id="jc_id" value="<%=mybean.jc_id%>"/></center>
                                           <%}else if (mybean.status.equals("Update")){%>
                                                            <center><input type="submit" name="update_button" class="btn btn-success" value="Update Image" />
                                                            <input type="submit" name="delete_button" class="btn btn-success" value="Delete Image" onClick="return confirmdelete(this)" /></center>
                                                <%}%>
                                                </strong>
                                     
                                    <%} else {%>
                        <center><input name="btn_close" type="button" class="btn btn-success" onClick="window.close();" value="Close Window"/></center>
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
	<script language="JavaScript" type="text/javascript">

 function FormFocus()
{
  document.frmupload.filename.focus()
 }


        </script>
       </body>
</HTML>
