<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Managecourtesyvehicleoutage_Update" scope="request"/>
<%mybean.doPost(request,response); %>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body  onLoad="FormFocus();">
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
						<h1><%=mybean.status%>&nbsp;Pickup</h1>
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
						<li><a href="../portal/manager.jsp">Business Master</a> &gt;</li>
						<li><a href="../service/managecourtesyvehicleoutage.jsp?all=yes">List Vehicle Outage</a>&gt;</li>
						<li><a href="../service/managecourtesyvehicleoutage-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>  Vehicle Outage</a><b>:</b></li>
					</ul>
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">
												<center>
													<%=mybean.status%>  Vehicle Outage
												</center>
											</div>
										</div>		
								<div class="portlet-body portlet-empty container-fluid">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<form name="formdemo" method="post" class="form-horizontal">
													<center>
														<font size="1">Form fields marked with a red
															asterisk <b><font color="#ff0000">*</font></b> are required.
														</font>
													</center>
													<br />
								<div class="form-element6">
									<label>Branch<font color="#ff0000">*</font>:</label>
									<select name="dr_branch_id" id="dr_branch_id" class="form-control" visible="true" style="width:250px" onChange="showHint('../service/courtesy-check.jsp?vehicleoutage=yes&branch_id=' + GetReplace(this.value),'vehicleHint');">
                        			<%=mybean.PopulateBranch()%>
                      				</select>			
								</div>
								<div class="form-element6">
									<label>Vehicle<font color="#ff0000">*</font>:</label>
									<span id="vehicleHint"> <%=mybean.PopulateVehicle()%> </span>		
								</div>
								<div class="row"></div>
								<div class="form-element6">
									<label>From Time<font color="#ff0000">*</font>:</label>
									<input name="txt_outage_fromtime" type="text" class="form-control datetimepicker"  id ="txt_outage_fromtime" 
									value = "<%=mybean.courtesyoutagefromtime%>" size="20" maxlength="16" />		
								</div>
								<div class="form-element6">
									<label>To Time<font color="#ff0000">*</font>:</label>
									<input name="txt_outage_totime" type="text" class="form-control datetimepicker"  id ="txt_outage_totime" 
									value = "<%=mybean.courtesyoutagetotime%>" size="20" maxlength="16" />		
								</div>
								<div class="form-element6">
									<label>Description:</label>
									<textarea name="txt_outage_desc"  cols="70" rows="5" 
									class="form-control" id="txt_outage_desc"><%=mybean.courtesyoutage_desc %></textarea>	
								</div>
								<div class="form-element6">
									<label>Notes:</label>
									<textarea name="txt_outage_notes"  cols="70" rows="5" 
									class="form-control" id="txt_outage_notes"><%=mybean.courtesyoutage_notes %></textarea>	
								</div>
								<div class="row"></div>
                  <% if (mybean.status.equals("Update") &&!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
                  <div class="form-element6">
					<label>Entry By:</label>
					<%=mybean.entry_by%>
                     <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>">
				</div>
				<div class="form-element6">
					<label>Entry Date:</label>
					<%=mybean.courtesyoutage_entry_date%>
                      <input type="hidden" name="courtesyoutage_entry_date" value="<%=mybean.courtesyoutage_entry_date%>">
				</div>
                  <%}%>
                  <% if (mybean.status.equals("Update") &&!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
                  <div class="form-element6">
					<label>Modified By:</label>
					<%=mybean.modified_by%>
                      <input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>">
				</div>
				<div class="form-element6">
					<label>Modified Date:</label>
					<%=mybean.courtesyoutage_modified_date%>
                      <input type="hidden" name="courtesyoutage_modified_date" value="<%=mybean.courtesyoutage_modified_date%>">
				</div>
                  <%}%>
					<center>	                  
                    <%if(mybean.status.equals("Add")){%>
                      <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Vehicle Outage" onClick="return SubmitFormOnce(document.form1, this);" />
                      <input type="hidden" name="add_button" value="yes">
                      <%}else if (mybean.status.equals("Update")){%>
                      <input type="hidden" name="update_button" value="yes">
                      <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Vehicle Outage"  onClick="return SubmitFormOnce(document.form1, this);"/>
                      <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Vehicle Outage" />
                      <%}%>
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
<%@include file="../Library/js.jsp"%>
<script>
	$(function() {
	$( "#txt_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
    $( "#txt_outage_fromtime" ).datetimepicker({
      addSliderAccess: true,
	  sliderAccessArgs: {touchonly: false},
      dateFormat: "dd/mm/yy",
	  hour: 10,
	  stepMinute: 5,
	  minute: 00
    });
	$( "#txt_outage_totime" ).datetimepicker({
      addSliderAccess: true,
	  sliderAccessArgs: {touchonly: false},
      dateFormat: "dd/mm/yy",
	  hour: 10,
	  stepMinute: 5,
	  minute: 00
    });	
  });
</script>
<script language="JavaScript" type="text/javascript">
function FormFocus() { //v1.0
  document.formdemo.dr_branch_id.focus();
}
    </script>
</body>
</HTML>
