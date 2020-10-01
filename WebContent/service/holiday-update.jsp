<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Holiday_Update" scope="request"/>
<%mybean.doPost(request, response);%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
     <HEAD>
     <title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
     <meta http-equiv="pragma" content="no-cache">
     <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
      <meta name="viewport" content="width=device-width, initial-scale=1">
		<%@include file="../Library/css.jsp"%>
</HEAD>

     <body class="page-container-bg-solid page-header-menu-fixed">
     
     <%@include file="../portal/header.jsp" %>
     <!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%> &nbsp;Holiday
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
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="ticket.jsp">Tickets</a>&gt;</li>
						<li><a href="holiday-list.jsp?all=recent">List Holidays</a> &gt;</li>
						<li><a href="holiday-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Holiday</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center><%=mybean.status%> Holiday </center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
									<form name="formcontact" method="post" class="form-horizontal">
											<center>
												<font size="">Form fields marked with a red asterisk
													<font color=#ff0000>*</font> are required.
												</font>
												</center><br>
												 <div class="form-element6">
									<label >Branch<font color="#ff0000">*</font>:&nbsp;</label>
						<%if(mybean.branch_id.equals("0") || mybean.update.equals("yes")){%>
                         <select name="dr_branch" class="form-control" id="dr_branch">
                             <%=mybean.PopulateBranch()%>
                           </select>
                         <%}else{%>
                         <input type="hidden" id="dr_branch" name="dr_branch" value="<%=mybean.branch_id%>" />
                         <%=mybean.getBranchName(mybean.branch_id, mybean.comp_id)%>
                         <%}%>
				</div>
				<div class="form-element6">
			<label>Holiday Name<font color="#ff0000">*</font>:&nbsp;</label>
						<input type="text" class="form-control" id ="txt_holiday_name" name ="txt_holiday_name" value="<%=mybean.ticketholi_name%>"  size="20" maxlength="16"/>
				</div>
				<div class="row"></div>
				<div class="row">
				<div class="form-element6">
					<label >Holiday Date<font color=#ff0000><b>*</b></font>:&nbsp;</label>
						<input name="txt_holiday_date" id="txt_holiday_date"
							value = "<%=mybean.ticketholi_date%>" class="form-control datepicker"
						type="text" value="" />
				</div>
				</div>
                     <% if (mybean.status.equals("Update") &&!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
                      <div class="form-element6">
			    <label >Entry By:</label>
						<%=mybean.unescapehtml(mybean.entry_by)%>
                         <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>">
				</div>
				<div class="form-element6">
			    <label >Entry Date:</label>
						<%=mybean.entry_date%>
                         <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
				</div>
                     <%}%>
                     <% if (mybean.status.equals("Update") &&!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
                     <div class="form-element6">
			    <label >Modified By:</label>
						<%=mybean.unescapehtml(mybean.modified_by)%>
                         <input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>">
				</div>
                      <div class="form-element6">
			    <label >Modified Date:</label>
						<%=mybean.modified_date%>
                         <input type="hidden" name="modified_date" value="<%=mybean.modified_date%>">
				</div>
                     <%}%>
                     <%if(mybean.status.equals("Add")){%>
                     <center>
                     <div class="form-element12">
                      <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Holiday" onClick="return SubmitFormOnce(document.formcontact, this);"/>
                         <input type="hidden" id="add_button" name="add_button" value="yes"/></div>
                     </center>
                         <%}else if (mybean.status.equals("Update")){%>
                         <center>
                          <div class="form-element12">
                         <input id="update_button" name="update_button" type="hidden" value="yes"/>
                         <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Holiday" onClick="return SubmitFormOnce(document.formcontact, this);"/>
                         <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Holiday" />
                         </div>
                          </center>
                         <%}%>
                          </form>
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
</body>
</HTML>
                      
