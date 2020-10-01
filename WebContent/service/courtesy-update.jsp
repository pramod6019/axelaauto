<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Courtesy_Update" scope="request"/>
<%mybean.doPost(request, response);%>
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
     <body onLoad="CourtestyCarCheck()">
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
						<h1><%=mybean.status%>&nbsp;Courtesy Car</h1>
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
						<li><a href="index.jsp">Service</a> &gt;</li>
						<li><a href="courtesy.jsp">Courtesy</a>&gt;</li>
						<li><a href="courtesy-list.jsp?all=yes">List Courtesy Cars</a>&gt;</li>
						<li><a href="courtesy-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Courtesy Car</a><b>:</b></li>
					</ul>
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
     				<div class="tab-pane" id="">
							<!-- 					BODY START -->
						<div class="form-element8">
							<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">
										<center>
													<%=mybean.status%> Courtesy Car
										</center>
								</div>
							</div>		
							<div class="portlet-body portlet-empty container-fluid">
								<div class="tab-pane" id="">
									<!-- START PORTLET BODY -->
									<form name="formcontact" method="post" class="form-horizontal">
									<center>
										<font size="1">Form fields marked with a red
										asterisk <b><font color="#ff0000">*</font></b> are required.
										</font>
									</center>
									<br />
								<div>
								<div>
                         <input type="hidden" id="span_contact_id" name="span_contact_id" />
                         <input name="courtesycar_contact_id" type="hidden" id ="courtesycar_contact_id" value ="<%=mybean.courtesycar_contact_id%>" />
                         <input type="hidden" id ="customer_id" name ="customer_id" value="<%=mybean.customer_id%>" />
                         </div>
                         <div class="form-element6">
			<label>Branch<font color="#ff0000">*</font>:</label>
				<!--<select name="dr_branch" class="selectbox" id="dr_branch" onchange="showHint('../service/pickup-check.jsp?branch_id=' + GetReplace(this.value),'pickup_id');" > -->
			<div>														
			<%if(mybean.branch_id.equals("0") || mybean.update.equals("yes")){%>
                         <select name="dr_branch" class="selectbox" id="dr_branch" onchange=" showHint('../service/courtesy-car-check.jsp?branch_id=' + GetReplace(this.value),GetReplace(this.value),'dr_vehicle');" >
                             <%=mybean.PopulateBranch()%>
                           </select>
                         <%}else{%>
                         <input type="hidden" id="dr_branch" name="dr_branch" value="<%=mybean.branch_id%>" />   
                         <%=mybean.getBranchName(mybean.branch_id,  mybean.comp_id)%>
                         <%}%>
								                         </div>
												</div>
												
												
						<div class="form-element6">
								<label>Contact<font color="#ff0000">*</font>:</label>
										<!--<select name="dr_branch" class="selectbox" id="dr_branch" onchange="showHint('../service/pickup-check.jsp?branch_id=' + GetReplace(this.value),'pickup_id');" > -->
									<b><span id="span_courtesy_contact_id" name="span_courtesy_contact_id"> <%=mybean.link_contact_name%> </span></b>
			                         <%if(!mybean.courtesycar_id.equals("0")  && mybean.status.equals("Add")){%>
			                         &nbsp;<a href="#" id="veh_contact_link"></a>
			                         <%}else{%>
			                         &nbsp;<a href="#" id="veh_contact_link">(Select Contact)</a>
			                         <%}%>
			                         <div id="dialog-modal"></div>
					</div>
					<div class="form-element6">
							<span id="customer_col" style="display:<%=mybean.customer_display%>">Customer:</span>
							<div>
							<span id ="span_courtesy_customer_name" name ="span_courtesy_customer_name"><%=mybean.link_customer_name%></span>
							</div>
					</div>
					<div class="form-element6">
							<label>Vehicle<font color="#ff0000">*</font>:</label>
							<select name="dr_vehicle" class="form-control" id="dr_vehicle" onChange="CourtestyCarCheck()">
                           <%=mybean.PopulateVehicle()%>
                         </select>
					</div>
					
					<div class="form-element6">
							<label>Start Date<font color="#ff0000">*</font>:</label>
							<input name="txt_courtesycar_startdate" type="text" class="form-control datetimepicker" 
							 id ="txt_courtesycar_startdate" value = "<%=mybean.courtesycar_time_from%>" size="20" maxlength="16" onChange="CourtestyCarCheck();"/>
					</div>
					<div class="form-element6">
							<label>End Date<font color="#ff0000">*</font>:</label>
							<input name="txt_courtesycar_enddate" type="text" class="form-control datetimepicker"  id ="txt_courtesycar_enddate" 
							value = "<%=mybean.courtesycar_time_to%>" size="20" maxlength="16" />
					</div>
					<div class="form-element6">
							<label>Contact Name<font color="#ff0000">*</font>:</label>
							<input type="text" class="form-control" id ="txt_contact_name" name ="txt_contact_name" 
							value="<%=mybean.contact_name%>"  size="20" maxlength="16" />
					</div>
					<div class="form-element6">
							<label>Mobile 1<font color="#ff0000">*</font>:</label>
							<input type="text" class="form-control" id="txt_courtesycar_mobile1"
							 name="txt_courtesycar_mobile1" value="<%=mybean.mobile1%>" size="20" maxlength="13"/>
					</div>
					<div class="form-element6">
							<label>Mobile 2 :</label>
							<input type="text" class="form-control" id="txt_courtesycar_mobile2" 
							name="txt_courtesycar_mobile2" value="<%=mybean.mobile2%>" size="20" maxlength="13"/>
					</div>
					<div class="form-element6">
							<label>Landmark<font color="#ff0000">*</font>: </label>
							<textarea name="txt_courtesycar_landmark"  cols="70" rows="5" 
							class="form-control" id="txt_courtesycar_landmark" 
							onKeyUp="charcount('txt_courtesycar_landmark', 'span_txt_courtesycar_landmark','<font color=red>({CHAR} characters left)</font>', '255')" >
							<%=mybean.courtesycar_landmark %></textarea>
                         <input type="hidden" id="txt_courtesycar_landmark" name="txt_courtesycar_landmark" value ="<%=mybean.courtesycar_landmark%>" />
                         <span id="span_txt_courtesycar_landmark">(255 Characters)</span>
					</div>
					<div class="form-element6">
							<label>Address<font color="#ff0000">*</font>: </label>
							<textarea name="txt_courtesycar_address"  cols="70" rows="5" class="form-control" 
							id="txt_courtesycar_address" onKeyUp="charcount('txt_courtesycar_address', 'span_txt_courtesycar_address','<font color=red>({CHAR} characters left)</font>', '255')" >
							<%=mybean.courtesycar_address %></textarea>
                         <input type="hidden" id="txt_courtesycar_address" name="txt_courtesycar_address" value ="<%=mybean.courtesycar_address%>" />
                         <span id="span_txt_courtesycar_address">(255 Characters)</span>
					</div>
					
					<div class="form-element6">
							<label>Active:</label>
							<input id="chk_courtesycar_active" 
							type="checkbox" name="chk_courtesycar_active" <%=mybean.PopulateCheck(mybean.courtesycar_active) %> />
					</div>
					<div class="row"></div>
         				<div class="form-element6">
							<label>Notes:</label>
							<textarea name="txt_courtesycar_notes"  
							cols="70" rows="5" class="form-control" id="txt_courtesycar_notes" ><%=mybean.courtesycar_notes %></textarea>
					</div>
					<div class="row"></div>
                     <% if (mybean.status.equals("Update") &&!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
                    
                    <div class="row"></div>
         				<div class="form-element6">
							<label>Entry By:</label>
							<%=mybean.unescapehtml(mybean.entry_by)%>
                         <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>">
					</div>
					<div class="form-element6">
							<label>Entry Date:</label>
							<%=mybean.entry_date%>
                         <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
					</div>
                     <%}%>
                     <% if (mybean.status.equals("Update") &&!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
                    <div class="form-element6">
							<label>Modified By:</label>
							<%=mybean.unescapehtml(mybean.modified_by)%>
                         <input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>">
					</div>
					 <div class="form-element6">
							<label>Modified Date:</label>
							<%=mybean.modified_date%>
                         <input type="hidden" name="modified_date" value="<%=mybean.modified_date%>">
					</div>                    
                     <%}%>
                     <center>
   						<%if(mybean.status.equals("Add")){%>
                         <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Courtesy Car" onClick="return SubmitFormOnce(document.formcontact, this);"/>   
                         <input type="hidden" id="add_button" name="add_button" value="yes"/>
                         <%}else if (mybean.status.equals("Update")){%>
                         <input id="update_button" name="update_button" type="hidden" value="yes"/>     
                         <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Courtesy Car" onClick="return SubmitFormOnce(document.formcontact, this);"/>
                         <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Courtesy Car" />  
                         <%}%>
                      </center>   
                      <div>
                        <input type="hidden" name="courtesycar_id" value="<%=mybean.courtesycar_id%>">
                        </div>
                      
				</div>
				</form>
                      </div>
                      
			</div>
						</div>
							</div> 
							<div class="form-element4">
									<div class="portlet box">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">
												<center>Courtesy Car Calendar</center>
											</div>
										</div>
										<div class="portlet-body portlet-empty" style="min-height: 486px;">
											<center>
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<div id="calHint"><%=mybean.strHTML%></div> 
												</div>
											</center>
										</div>
									</div> 
								</div> 
				</div>
			</div>
		</div> 
		</div>
     <%@include file="../Library/admin-footer.jsp" %>
     <%@include file="../Library/js.jsp"%>
     <script language="JavaScript" type="text/javascript">
	
function CourtestyCarCheck()
{
	var veh_id=document.getElementById("dr_vehicle").value;
	var courtesydate=document.getElementById("txt_courtesycar_startdate").value;
	var branch_id=document.getElementById("dr_branch").value;
	showHint('../service/courtesy-car-check.jsp?veh_id='+veh_id+'&courtesydate='+courtesydate+'&branch_id='+branch_id+'&demo=yes', 'calHint');
} 
// Model Window (Contact)
$(function(){
    // Dialog
    $('#dialog-modal').dialog({
        autoOpen: false,
        width: 900,
        height: 500,
        zIndex: 200,
        modal: true,
        title: "Select Courtesy Contact"
    });
    $('#veh_contact_link').click(function(){

        $.ajax({
            success: function(data){
                $('#dialog-modal').html('<iframe src="../customer/customer-contact-list.jsp?group=select_courtesycar_contact" width="100%" height="100%" frameborder=0></iframe>');  
            }
        });
		$('#dialog-modal').dialog('open');  
        return true;
    });
});
function SelectContact(contact_id, contact_name, mobile1, mobile2, courtesycar_landmark,courtesycar_address, customer_id, customer_name, hide)
{
	document.getElementById("customer_id").value = customer_id; 
	document.getElementById("span_contact_id").value = contact_id;
	document.getElementById("txt_contact_name").value = contact_name; 
	document.getElementById("span_courtesy_customer_name").value = customer_name; 
	document.getElementById("txt_courtesycar_mobile1").value = mobile1; 
	document.getElementById("txt_courtesycar_mobile2").value = mobile2; 
	document.getElementById("txt_courtesycar_landmark").value = courtesycar_landmark; 
	document.getElementById("txt_courtesycar_address").value = courtesycar_address;  
	  
	document.getElementById("span_courtesy_contact_id").innerHTML = "<a href=../customer/customer-contact-list.jsp?contact_id="+contact_id+">" +contact_name+ "</a>";
	document.getElementById("span_courtesy_customer_name").innerHTML = "<a href=../customer/customer-list.jsp?customer_id="+customer_id+">" +customer_name+ "</a>";
	document.getElementById("customer_col").style.display = "";
$('#dialog-modal').dialog('close');	 
} 	
   $(function() {
		$( "#txt_courtesycar_startdate" ).datetimepicker({
		  showButtonPanel: true,
		  dateFormat: "dd/mm/yy"
		});
		
		$( "#txt_courtesycar_enddate" ).datetimepicker({
		  showButtonPanel: true,
		  dateFormat: "dd/mm/yy"
		});	
	});
     </script>
     </body>
</HTML>
