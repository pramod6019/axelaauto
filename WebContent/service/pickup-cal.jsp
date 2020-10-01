<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Pickup_Cal" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html>
<html>
    <HEAD>
    <title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>    
</HEAD>
    <body>
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
						<h1>Pickup Calendar</h1>
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
							<li><a href="../service/pickup.jsp">Pickup</a>&gt;</li>
							<li><a href="../service/pickup-cal.jsp">Courtesy Calendar</a><b>:</b></li>
						</ul>
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="formemp" class="form-horizontal " method="post">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
    					<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Pickup Calendar</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
											<!--     start customer details -->
											<center>
												<font size="1">Form fields marked with a red asterisk
													<b><font color="#ff0000">*</font></b> are required.
												</font>
											</center>
											<br />
    
 									<div class="form-element3">
												<label>Branch<font color="#ff0000">*</font>:&nbsp;</label>
												  <%if(mybean.branch_id.equals("0")){%>
                  								  <select name="dr_branch" id="dr_branch" class="form-control"
                     								onChange="PickupCheck();showHint('../service/pickup-check.jsp?multiple=yes&branch_id=' + GetReplace(this.value),'driverHint');">
                    					  <%=mybean.PopulateBranch(mybean.pickup_branch_id,"", "1,3", "", request)%>
                   					 </select>
               						 <%}else{%>
             						<input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>" />                      
             							<%=mybean.getBranchName(mybean.pickup_branch_id,mybean.comp_id)%>
                  						  <%}%>
									</div>
 								<div class="form-element3">
									<label>Start Date<font color=#ff0000><b> *</b></font></label>
									<input name ="txt_pickup_time_from" type="text" class="form-control datepicker" 
									id="txt_pickup_time_from" value="<%=mybean.pickup_time_from %>" size="15" maxlength="10" />										
								</div>
								<div class="form-element3">
									<label>End Date<font color=#ff0000><b> *</b></font></label>
									<input name ="txt_pickup_time_to" type="text" class="form-control datepicker" 
									id="txt_pickup_time_to" value="<%=mybean.pickup_time_to%>" size="15" maxlength="10"/>									
								</div>
             					<div class="form-element3">
									<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Submit" />
                        			<input name="submit_button" type="hidden" id="submit_button" value="Submit" />									
								</div>
								<div class="row"></div>
								<div class="form-element3">
									<label>Driver<font color=#ff0000><b> *</b></font></label>
									<div>
									<span id="driverHint"><%=mybean.PopulateDriver()%></span>									
									</div>
								</div>
								<div class="form-element3">
									<label>Model:</label>
									<div>
									<select name="dr_model" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_model" onChange="TestDriveCheck()" style="width:250px" >
                      				  <%=mybean.PopulateModel()%>
                      				  </select>					
                      				  </div>			
								</div>
       								</div>
									</div>
								</div>
							</form>
						</div>
					</div>
                  <div class="portlet-body">
						<div class="tab-pane" id="">
						<%=mybean.StrHTML%>
				</div>
					</div>
				</div>
			</div>
		</div>
	</div>
    <%@include file="../Library/admin-footer.jsp" %>
    <%@include file="../Library/js.jsp"%>
     <script type="text/javascript" src="../Library/smart.js"></script>
    <script language="JavaScript" type="text/javascript">	
    function PickupCheck() { 
	var branch_id=document.getElementById("dr_branch").value;
	    } 
    </script>
    <script>
$(function() {
    $( "#txt_pickup_time_from" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
	
	 $( "#txt_pickup_time_to" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });

  });
    </script>
    </body>
</HTML>
