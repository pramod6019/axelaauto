<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.ManageJobCardBay_Update" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %>     
</head>

<body  onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%> Job Card Bay</h1>
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
							<li><a href="managejobcardcat.jsp?all=yes">List  Job Card Bay</a> &gt;</li>
							<li><a href="managejobcardbay-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Job Card Bay</a><b>:</b></li>
						</ul>

						<!-- END PAGE BREADCRUMBS -->
							<center>
							<font color="#ff0000" ><b><%=mybean.msg%></b></font>
							</center>
							
							<div class="tab-pane" id="">
					<!-- 					BODY START -->
							<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">
									<%=mybean.status%> Job Card Bay 
								</div>
							</div>
							
                           <div class="portlet-body portlet-empty container-fluid">
							<div class="tab-pane" id="">
								<!-- START PORTLET BODY -->
								<center>
								<font size="1">Form fields marked with a red asterisk <b><font color=#ff0000>*</font></b> are required.
		                              </font>
								</center><br>
								
								<form name="form1"  method="post" class="form-horizontal">
								
								<div class="form-element6">
                        			<label>Branch<font color="#ff0000">*</font>:</label>
                    				<select name="dr_branch" class="form-control" id="dr_branch">
										<%=mybean.PopulateBranch(mybean.bay_branch_id, "", "1,3", "", request)%>
                      				</select>
                      			</div>
                      
                       
		                       <div class="form-element6">
		                            <label>Name<font color="#ff0000">*</font>: </label>
		                            <input name ="txt_bay_name" type="txt_bay_name" class="form-control" 
		                            	value="<%=mybean.bay_name%>" maxlength="255"/>
		                       </div>
		                       
                          <div class="row"></div>
                          
	                         <div class="form-element6">
	                          <label>Open:</label>
	                            <input id="chk_bay_open" type="checkbox" name="chk_bay_open" <%=mybean.PopulateCheck(mybean.bay_open)%> />
	                           </div>
	                           
	                          <div class="form-element6">
	                            <label>Active:</label>
	                            <input id="chk_bay_active" type="checkbox" name="chk_bay_active" <%=mybean.PopulateCheck(mybean.bay_active)%> />
	                          </div>
                         
	                          <div class="form-element6">
	                           <label>Notes:</label>
	                           <textarea name="txt_bay_notes" id="txt_bay_notes" cols="70" rows="4" class="form-control"><%=mybean.bay_notes%></textarea>
	                          </div>
	                          
	                          <div class="row"></div>
	                          
                        	<%if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {%>
                        
		                        <div class="form-element6">
		                            <label>Entry By:</label>
		                            <%=mybean.unescapehtml(mybean.bay_entry_by)%>
		                            <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.bay_entry_by%>">
		                        </div>
		                        
		                        <div class="form-element6">
		                            <label>Entry Date:</label>
		                            <%=mybean.entry_date%>
		                            <input type="hidden" id="entry_date" name="entry_date" value="<%=mybean.entry_date%>">
		                        </div>
		                        
		                        <%}%>
		                        <% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {%>
		                       
		                        <div class="form-element6">
		                            <label>Modified By:</label>
		                            <%=mybean.unescapehtml(mybean.bay_modified_by)%>
		                            <input type="hidden" id="modified_by" name="modified_by" value="<%=mybean.bay_modified_by%>">
		                          </div>
		                          
		                        <div class="form-element6">
		                            <label>Modified Date:</label>
		                            <%=mybean.modified_date%>
		                            <input type="hidden" id="modified_date" name="modified_date" value="<%=mybean.modified_date%>">
		                          </div>
		                          
                        		<%}%>
                        		
		                        <center>
		                            <%if(mybean.status.equals("Add")){%>
		                            <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Job Card Bay" onclick="return SubmitFormOnce(document.form1,this);"/>
		                            <input type="hidden" id="add_button" name="add_button" value="yes"/>
		                            <%}else if (mybean.status.equals("Update")){%>
		                            <input type="hidden" id="update_button" name="update_button" value="yes"/>  
		                            <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Job Card Bay" onclick="return SubmitFormOnce(document.form1,this);"/>
		                            <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Job Card Bay" />
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

	</div>
	<!-- END CONTAINER -->
 <%@include file="../Library/admin-footer.jsp" %>
 <%@include file="../Library/js.jsp" %>
<script language="JavaScript" type="text/javascript">
function FormFocus() { //v1.0
  document.form1.txt_bay_name.focus()
}
        </script>
</body>
</html>
