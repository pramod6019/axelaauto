<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.app.Model_Colours_Update" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>

<%@include file="../Library/css.jsp" %>

</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>
							<%=mybean.status%>&nbsp;Colours
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
						<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
						<li><a href="../app/model-list.jsp?all=yes">Models</a> &gt;</li>
						<li><a href="../app/model-colours-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Colour</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Offer
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
										
											<div class="form-element3">
												<label>Colour Name<font color="#ff0000">*</font>: </label>
												<input name="txt_colour_title" type="text" class="form-control" id="txt_colour_title"
													value="<%=mybean.colours_title%>" size="50" maxlength="255" />
											</div>
											
											<div class="form-element3">
												<label>Model<font color="#ff0000">*</font>: </label>
												<div >
													<select name="dr_colours_model_id" class="form-control">
														<%=mybean.PopulateModel()%>
													</select>
												</div>
											</div>
											
											<div class="form-element6">
												<label>Colour Code<font color="#ff0000">*</font>: </label>
												<input name="txt_colours_colour" type="text" class="form-control" id="txt_colours_colour
													value="<%=mybean.colours_colour%>" size="10" maxlength="7" />
											</div>
											
											<div class="form-element12">
											<% if (mybean.status.equals("Update") &&!(mybean.colours_entry_by == null) && !(mybean.colours_entry_by.equals(""))) { %>
											<div class="form-element3">
												<label>Entry By:</label>
												<span>
													<%=mybean.unescapehtml(mybean.colours_entry_by)%>
												</span>
											</div>
											<%}%>
											<% if (mybean.status.equals("Update") &&!(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>
											<div class="form-element3">
												<label>Entry Date:</label>
												<span>
													<%=mybean.entry_date%>
												</span>
											</div>
											<%}%>
											<% if (mybean.status.equals("Update") &&!(mybean.colours_modified_by == null) && !(mybean.colours_modified_by.equals(""))) { %>
											<div class="form-element3">
												<label>Modified By:</label>
												<span>
													<%=mybean.unescapehtml(mybean.colours_modified_by)%>
												</span>
											</div>
											<%}%>
											<% if (mybean.status.equals("Update") &&!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
											<div class="form-element3">
												<label>Modified Date:</label>
												<span>
													<%=mybean.modified_date%>
												</span>
											</div>
											</div>
											<%}%>
											<div class="form-element12">
												<center>
													<%if(mybean.status.equals("Add")){%>
													<input name="addbutton" type="submit" class="btn btn-success" id="addbutton"
														value="Add Colours" onClick="return SubmitFormOnce(document.form1, this);" />
													<input type="hidden" name="add_button" id="add_button" value="yes"/>
													<%}else if (mybean.status.equals("Update")){%>
													<!-- <input type="hidden" name="update_button" id="update_button" value="yes"/>
	                            							<input name="updatebutton" type="submit" class="button" id="updatebutton" value="Update Colours" onClick="return SubmitFormOnce(document.form1, this);"/>
	                            							<input name="delete_button" type="submit" class="button" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Colours"/> -->
													<input type="hidden" name="update_button" id="update_button" value="yes"/>
													<input name="button" type="submit" class="btn btn-success" id="button"
														value="Update Colours" onClick="return SubmitFormOnce(document.form1, this);" />
													<input name="delete_button" type="submit" class="btn btn-success" id="delete_button"
														OnClick="return confirmdelete(this)" value="Delete Colours" />
													<%}%>
													<input type="hidden" name="feature_entry_by" value="<%=mybean.colours_entry_by%>" />
													<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>" />
													<input type="hidden" name="feature_modified_by" value="<%=mybean.colours_modified_by%>" />
													<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>"/>
													
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
	<%@ include file="../Library/admin-footer.jsp" %>
    
	<%@include file="../Library/js.jsp" %>
	 
</body>
</HTML>
