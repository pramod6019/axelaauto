<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Pickup_Update" scope="request"/>
<%mybean.doPost(request, response);%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
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
						<li><a href="index.jsp">Service</a> &gt;</li>
						<li><a href="pickup-list.jsp?all=yes">List Pickup</a>&gt;</li>
						<li><a href="pickup-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Pickup</a><b>:</b></li>
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
													<%=mybean.status%> Pickup
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
													<div>
													<input type="hidden" id="span_contact_id" name="span_contact_id" value ="<%=mybean.contact_id%>" />
                   								   <input type="hidden" id="span_customer_id" name="span_customer_id" value ="<%=mybean.customer_id%>" />
                     							 <input name="pickup_contact_id" type="hidden" id ="pickup_contact_id" value ="<%=mybean.pickup_contact_id%>" />
                      							<input type="hidden" name="pickup_id" value="<%=mybean.pickup_id%>" >
                     							 <input type="hidden" id="txt_pickup_customer_id" name="txt_pickup_customer_id" value ="<%=mybean.pickup_customer_id%>" />
												</div>
												<br />
												<div class="form-element6">
														<label>Branch<font color="#ff0000">*</font>:</label>
														<!--<select name="dr_branch" class="selectbox" id="dr_branch" onchange="showHint('../service/pickup-check.jsp?branch_id=' + GetReplace(this.value),'pickup_id');" > -->
														<div>														
														<%if(mybean.branch_id.equals("0") || mybean.update.equals("yes")){%>
									                      <select name="dr_branch" class="form-control" id="dr_branch" onchange="BranchCheck();" >
									                        <%=mybean.PopulateBranch()%>
									                      </select>
									                      <%}else{%>
									                      <input type="hidden" id="dr_branch" name="dr_branch" value="<%=mybean.branch_id%>"/>
									                      <%=mybean.getBranchName(mybean.branch_id,mybean.comp_id)%>
									                      <%}%>
									                      </div>
												</div>
												<div class="form-element6">
														<label>Contact<font color="#ff0000">*</font>:</label>
														<span id="span_pickup_contact_id"> <%=mybean.link_contact_name%> </span></b>
									                      <%if(!mybean.pickup_id.equals("0")  && mybean.status.equals("Add")){%>
									                      &nbsp;<a href="#" id="veh_contact_link" ></a>
									                      <%}else{%>
									                      &nbsp;<a href="#" id="veh_contact_link" >(Select Contact)</a>
									                      <%}%>
									                      <div id="dialog-modal"></div>
												</div>
												<div class="form-element6">
														<label><span id="customer_col" style="display:<%=mybean.customer_display%>">Customer:</span> </label>
														<b><span id="span_pickup_customer_id" name="span_pickup_customer_id"> <%=mybean.link_customer_name%> </span></b>&nbsp;&nbsp;
                     									 <input type="hidden" id="txt_pickup_customer_id" name="txt_pickup_customer_id" value="<%=mybean.pickup_customer_id%>" /></b>
												</div>
												<div class="form-element6">
														<label>Vehicle:</span> </label>
														<b><span id="span_pickup_veh_id" name="span_pickup_veh_id"><%=mybean.link_vehicle_name%> </span></b>
							                      <%if(!mybean.pickup_id.equals("0") && mybean.status.equals("Add")){%>
							                      &nbsp;<a href="#" id="veh_pickup_link" ></a>
							                      <%} else{%>
							                      &nbsp;<a href="#" id="veh_pickup_link" >(Select Vehicle)</a>
							                      <%}%>
							                      <div id="vehicle-dialog-modal"></div>
							                      <input name="span_veh_id" type="hidden" id ="span_veh_id" value ="<%=mybean.veh_id%>" />
							                      <input name="txt_veh_id" type="hidden" id ="txt_veh_id" value ="<%=mybean.pickup_veh_id%>" />
							                      <input name="txt_status" type="hidden" id ="txt_status" value ="<%=mybean.status%>" />
                    						  </div>
                 							<div class="form-element6 ">
														<label> Pickup Type:</label>
														<div>
														<select name="dr_pickuptype" class="form-control" id="dr_pickuptype" >
                  									      <%=mybean.PopulatePickupType()%>
                   									   </select>
                   									   </div>
											</div>
												<div class="form-element6 ">
														<label>Pickup Time<font color="#ff0000">*</font>:</label>
														<input name="txt_pickup_date" type="text" class="form-control datetimepicker"  id ="txt_pickup_date" value = "<%=mybean.pickupdate%>" size="20" maxlength="16" onChange="PickupCheck()"/>
												</div>
												<div class="row"></div>
												<div class="form-element6 ">
														<label>Driver/Technician<font color="#ff0000">*</font>:</label>
														<div>
														<span id="pickup_id">
                    									  <select id="dr_pickup_emp_id" name="dr_pickup_emp_id" class="form-control" onChange="PickupCheck()">
                       									 <%=mybean.PopulateDriver()%>
                   										   </select>
                   										   </span>
														</div>
												</div>
												<div class="form-element6 ">
														<label>Location<font color="#ff0000">*</font>:</label>
														<div>
                    									 <select name="dr_location" class="form-control" id="dr_location" >
                        										<option value=0>Select Location</option>
                      											  <%=mybean.PopulateLocation()%>
                    											  </select>
														</div>
												</div>
												<div class="form-element6 ">
														<label>Contact Person<font color="#ff0000">*</font>:</label>
														<input name="txt_pickup_contact_name" type="text" class="form-control"  id ="txt_pickup_contact_name" 
														value = "<%=mybean.pickup_contact_name%>" size="20" maxlength="100" />
												</div>
												<div class="form-element6 ">
														<label>Mobile 1<font color="#ff0000">*</font>:</label>
														<input name="txt_pickup_mobile1" type="text" class="form-control"  id ="txt_pickup_mobile1" 
														value = "<%=mybean.pickup_mobile1%>" size="20" maxlength="13" />
												</div>
												<div class="form-element6 ">
														<label>Mobile 2:</label>
															<input name="txt_pickup_mobile2" type="text" class="form-control"  id ="txt_pickup_mobile2" 
															value = "<%=mybean.pickup_mobile2%>" size="20" maxlength="13" />
												</div>
              									<div class="form-element6 ">
														<label>Address<font color="#ff0000">*</font>:</label>
															<textarea name="txt_pickup_add" class="form-control" id="txt_pickup_add" onKeyUp="charcount('txt_pickup_add', 'span_txt_pickup_add','<font color=red>({CHAR} characters left)</font>', '255')" ><%=mybean.pickup_add %></textarea>
                      										<input type="hidden" id="txt_pickup_add" name="txt_pickup_add" value ="<%=mybean.pickup_add%>" />
               											       <br>
                										      <span id="span_txt_pickup_add">(255 Characters)</span>
												</div>
												<div class="form-element6 ">
														<label>Landmark<font color="#ff0000">*</font>:</label>
															<textarea name="txt_pickup_landmark" cols="40" rows="4" class="form-control" id="txt_pickup_landmark" onKeyUp="charcount('txt_pickup_landmark', 'span_txt_pickup_landmark','<font color=red>({CHAR} characters left)</font>', '255')" ><%=mybean.pickup_landmark %></textarea>
                  										    <br>
                      										<span id="span_txt_pickup_landmark"> (255 Characters)</span>
												</div>
												<div class="form-element6 ">
														<label>Instruction:</label>
															<textarea name="txt_pickup_instruction"  cols="40" rows="4" class="form-control" id="txt_pickup_instruction" onKeyUp="charcount('txt_pickup_instruction', 'span_txt_pickup_instruction','<font color=red>({CHAR} characters left)</font>', '1000')" ><%=mybean.pickup_instruction %></textarea>
									                      <br>
									                      <span id="span_txt_pickup_instruction"> (1000 Characters)</span>
												</div>
												<div class="form-element6 ">
														<label>Active:</label>
														<input id="chk_pickup_active" type="checkbox" 
														name="chk_pickup_active" <%=mybean.PopulateCheck(mybean.pickup_active) %> />
												</div>
												<div class="form-element6 ">
														<label>Notes:</label>
														<textarea name="txt_pickup_notes"  cols="70" rows="4" class="form-control" id="txt_pickup_notes"
														  ><%=mybean.pickup_notes %></textarea>
												</div>
                  
                  <% if (mybean.status.equals("Update") &&!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
                  <div class="form-element6 ">
                    <label>Entry By:</label>
                    <%=mybean.unescapehtml(mybean.entry_by)%>
                      <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>">
                  </div>
                  <div class="form-element6 ">
                   <label>Entry Date:</label>
                    <%=mybean.pickup_entry_date%>
                      <input type="hidden" name="entry_date" value="<%=mybean.pickup_entry_date%>">
                  </div>
                  </tr>
                  <%}%>
                  <% if (mybean.status.equals("Update") &&!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
                  <div class="form-element6 ">
                    <label>Modified By:</label>
                    <%=mybean.unescapehtml(mybean.modified_by)%>
                      <input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>">
                  </div>
                  <div class="form-element6 ">
                    <label>Modified Date:</label>
                    <%=mybean.pickup_modified_date%>
                      <input type="hidden" name="modified_date" value="<%=mybean.pickup_modified_date%>">
                 </div>
                  <%}%>
                
                    <%if(mybean.status.equals("Add")){%>
                    <center>
                      <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Pickup" onClick="return SubmitFormOnce(document.formcontact, this);" />
                      <input type="hidden" id="add_button" name="add_button" value="yes"/></center>
                      <%}else if (mybean.status.equals("Update")){%>
                      <center>
                      <input type="hidden" id="update_button" name="update_button" value="yes"/>
                      <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Pickup" onClick="return SubmitFormOnce(document.formcontact, this);" />
                      <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Pickup" />
                      </center>
                      <%}%>
					</form>
				</div>
			</div>
			</div>
			</div>
			<div class="form-element4 form-element">
									<div class="portlet box">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">
												<center>Pickup Calandar</center>
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
		</div> 
		</div>
     <%@include file="../Library/admin-footer.jsp" %>
      <%@include file="../Library/js.jsp" %>

     	 <script language="JavaScript" type="text/javascript">
	 
	 $(function(){
    // Dialog
    $('#vehicle-dialog-modal').dialog({
        autoOpen: false,
        width: 900,
        height: 500,
        zIndex: 200,
        modal: true,
        title: "Select Vehicle"
    });
    $('#veh_pickup_link').click(function(){
	$.ajax({
            //url: "home.jsp",
            success: function(data){
                $('#vehicle-dialog-modal').html('<iframe src="../service/vehicle-list.jsp?group=select_veh_pickup" width="100%" height="100%" frameborder=0></iframe>');
            }
        });
		$('#vehicle-dialog-modal').dialog('open');    
        return true;
    });
});	 
    
	$(function(){
    // Dialog
    $('#dialog-modal').dialog({
        autoOpen: false,
        width: 900,
        height: 500,
        zIndex: 200,
        modal: true,
        title: "Select Pickup Contact"
    });
		
    $('#veh_contact_link').click(function(){

        $.ajax({
            //url: "home.jsp",
            success: function(data){
                $('#dialog-modal').html('<iframe src="../customer/customer-contact-list.jsp?group=select_pickup_contact" width="100%" height="100%" frameborder=0></iframe>');  
            }
        });
		$('#dialog-modal').dialog('open');  
        return true;
    });
});
function SelectContact(contact_id, contact_name, customer_id, customer_name, contact_address, contact_landmark, contact_mobile1, contact_mobile2, hide){
	document.getElementById("span_contact_id").value = contact_id; 
	document.getElementById("txt_pickup_customer_id").value = customer_id; 
	
	document.getElementById("txt_pickup_contact_name").value = contact_name;
	document.getElementById("txt_pickup_mobile1").value = contact_mobile1;
	document.getElementById("txt_pickup_mobile2").value = contact_mobile2;
	document.getElementById("txt_pickup_add").value = contact_address;
	document.getElementById("txt_pickup_landmark").value = contact_landmark;   
	document.getElementById("span_pickup_contact_id").innerHTML = "<a href=../customer/customer-contact-list.jsp?contact_id="+contact_id+">" +contact_name+ "</a>"; 
	document.getElementById("span_pickup_customer_id").innerHTML = "<a href=../customer/customer-list.jsp?customer_id="+customer_id+">" +customer_name+ "</a>"; 
	document.getElementById("customer_col").style.display = "";
	$('#dialog-modal').dialog('close');   
}

function SelectVehicle(veh_id, veh_reg_no, contact_id, contact_name, customer_id, customer_name, contact_address, contact_landmark, contact_mobile1, contact_mobile2) 
{
	var contact_address = contact_address.replace("single_quote","'");
	document.getElementById("span_veh_id").value = veh_id;          
	document.getElementById("span_pickup_veh_id").innerHTML = "<a href=../service/vehicle-list.jsp?veh_id="+veh_id+">" + veh_reg_no + "</a>";
	
	
	
	SelectContact(contact_id, contact_name, customer_id, customer_name, contact_address, contact_landmark, contact_mobile1, contact_mobile2, '');
	//ajax call for showing vehicle info  
		 
	$('#vehicle-dialog-modal').dialog('close');                       
}  


function PickupCheck()
{
	var pickupdate=document.getElementById("txt_pickup_date").value;
	var branch_id=document.getElementById("dr_branch").value;
	var driver_id=document.getElementById("dr_pickup_emp_id").value;
	//var location_id=document.getElementById("dr_location").value;
	showHint('../service/pickup-check.jsp?pickup=yes&driver_id='+driver_id+'&branch_id='+branch_id+'&pickupdate='+pickupdate+'',  'calHint');
}

function BranchCheck()
{
	var pickupdate=document.getElementById("txt_pickup_date").value;
	var branch_id=document.getElementById("dr_branch").value;
	var driver_id=document.getElementById("dr_pickup_emp_id").value;
	//var location_id=document.getElementById("dr_location").value;
	showHint('../service/pickup-check.jsp?branch_id=' +branch_id+'','pickup_id');
	setTimeout(showHint('../service/pickup-check.jsp?pickup=yes&driver_id='+driver_id+'&branch_id='+branch_id+'&pickupdate='+pickupdate,  'calHint'),3000);
	//showHint('../service/pickup-check.jsp?pickup=yes&driver_id='+driver_id+'&branch_id='+branch_id+'&pickupdate='+pickupdate+'',  'calHint');
}
    </script>
	 <script language="JavaScript" type="text/javascript">
		 $(function() {
    $( "#txt_pickup_date" ).datetimepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy",
    stepMinute: 5
	});
});	 
     </script>
	 <script type="text/javascript">
	 function Displaypaymode(){
		 //alert("hi");
		var cont_id = document.getElementById('pickup_contact_id').value;
		//alert("hi--"+cont_id);
		var cont_id1 = document.getElementById('span_contact_id').value;
		//alert("contact is === "+cont_id1);
		
	    <%if(!mybean.contact_id.equals("0")) {%>
		//alert("ifhi");
			displayRow('customer_row');		
		<%}else {%>
		//alert("elhi");
			hideRow('customer_row');											
		<%}%>
	}
	 </script>
      </body>
     
</HTML>
