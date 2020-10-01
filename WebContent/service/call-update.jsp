<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Call_Update" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html>
<html>
	<HEAD>
	<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
	<%@include file="../Library/css.jsp"%>
	
	<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
	<body <%if(mybean.add.equals("yes")){%>onLoad="PickupCheck();ListApptCartItem();"<%}%> >
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
						<h1><%=mybean.status%> Call</h1>
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
						<li><a href="call-list.jsp?all=yes">List Calls</a> &gt;</li>
						<li><a href="call-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Call</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
							<!-- 					BODY START -->
				<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center><%=mybean.status%> Call</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<font color=#ff0000>*</font> are required.
												</font>
											</center>
				<div>
                  <input type="hidden" id="txt_status" name="txt_status" value="<%=mybean.status%>">
                  <input type="hidden" id="txt_booking_id" name="txt_booking_id" value="<%=mybean.booking_id%>">
                 </div>
                  <div class="form-element6">
						<label >Branch<font color="#ff0000">*</font>: </label>
					<div>
						<select name="dr_call_branch_id" class="form-control" id="dr_call_branch_id" onChange="BranchCheck();">
                     	 <%=mybean.PopulateBranch(mybean.call_branch_id,"", "1,3", "", request)%>
                    	</select>
                    </div>		
				</div>
				 <div class="form-element6">
						<label >Service Advisor<font color="#ff0000">*</font>:</label>
					<div>
						<span id="span_emp">
                    <select id="dr_call_emp_id" name="dr_call_emp_id" class="form-control">
                      <%=mybean.PopulateExecutive(mybean.call_branch_id, mybean.comp_id)%>
                    </select>
                    </span>
                    </div>		
				</div>
				<div class="form-element6">
						<label>Contact<font color="#ff0000">*</font>:</label>
					<b><span id="span_call_contact_id" name="span_call_contact_id"> <%=mybean.link_contact_name%> </span></b>
                    <%if(!mybean.call_id.equals("0")  && mybean.status.equals("Add")){%>
                    &nbsp;<a href="#" id="veh_contact_link" ></a>
                    <%}else{%>
                    &nbsp;<a href="#" id="veh_contact_link" >(Select Contact)</a>
                    <%}%>  
                    <div id="contact-dialog-modal"></div>		
				</div>
				<div class="form-element6">
						<span id="customer_col" style="display:<%=mybean.customer_display%>">Customer:</span>
					<b><span id="span_call_customer_id" name="span_call_customer_id"> <%=mybean.link_customer_name%> </span></b>&nbsp;&nbsp;
                    <input type="hidden" id="pickup_customer_id" name="pickup_customer_id" value="<%=mybean.pickup_customer_id%>" />
                    <input type="hidden" id="courtesycar_customer_id" name="courtesycar_customer_id" value="<%=mybean.courtesycar_customer_id%>"/>	
				</div>
                <div class="row"></div>
                <%if((!mybean.call_id.equals("0") && !mybean.call_veh_id.equals("0")) || (mybean.call_id.equals("0") && mybean.add.equals("yes")) ||  mybean.update.equals("yes")){%>      
               <div class="form-element6">
						<label>Vehicle:</label>
					<span id="span_call_veh_id" name="span_call_veh_id"><%=mybean.link_vehicle_name%> </span></b>
                    <%if(!mybean.call_id.equals("0") && mybean.status.equals("Add")){%>
                    &nbsp;<a href="#" id="veh_call_link" ></a>
                    <%} else{%>
                    &nbsp;<a href="#" id="veh_call_link" >(Select Vehicle)</a>
                    <%}%>
                    <div id="vehicle-dialog-modal"></div>		
				</div>
               <div class="form-element6">
						<span id="item_col" style="display:<%=mybean.item_display%>">Item:</span>
					<span id="item_name" name="item_name"><b><a href="../inventory/inventory-item-list.jsp?item_id=<%=mybean.item_id%>"><%=mybean.item_name%></a></b></span>&nbsp;&nbsp;
                    <input type="hidden" id="txt_item_name" name="txt_item_name" value="<%=mybean.item_name%>" />		
				</div>
                <%}%>
                <div class="row"></div>
                 <div class="form-element6">
                 <label>Call Time<font color="#ff0000">*</font>:</label>
					<% if(mybean.status.equals("Add")){%>
                    <%=mybean.call_time%>
                    <%}else {%>  
                    <input name="txt_call_time" type="text" class="textbox"  id ="txt_call_time" value = "<%=mybean.call_time%>" size="20" maxlength="16" />
                    <%}%>
                    <input name="txt_call_time" type="hidden"  id ="txt_call_time" value = "<%=mybean.call_time%>">		
				</div>
				<div class="form-element6">
                 <label>Type<font color="#ff0000">*</font>:</label>
					<select name="dr_call_type_id" class="form-control" >
                      <%=mybean.PopulateCallType()%>
                    </select>
                    <input type="hidden" name="txt_status" id="txt_status" value="<%=mybean.status%>" />
                    <input name="span_veh_id" type="hidden" id ="span_veh_id" value ="<%=mybean.veh_id%>" />  
                    <input name="txt_veh_id" type="hidden" id ="txt_veh_id" value ="<%=mybean.call_veh_id%>" />
                    <input name="span_contact_id" type="hidden" id ="span_contact_id" value ="<%=mybean.contact_id%>" />
                    <input name="call_contact_id" type="hidden" id ="call_contact_id" value ="<%=mybean.call_contact_id%>" />
                    <input name="txt_status" type="hidden" id ="txt_status" value ="<%=mybean.status%>" />	
				</div>
                <div class="row"></div>
                	<div class="form-element6">
                 <label>Priority<font color="#ff0000">*</font>:</label>
					<select name="dr_call_prioritycall_id" class="form-control" >
                      <%=mybean.PopulateCallPriority()%>
                    </select>
            	</div>
            	<div class="form-element6">
                 <label>Customer Voice<font color="#ff0000">*</font>:</label>
					<textarea name="call_customer_voice" id="call_customer_voice" cols="70" rows="5"
					 class="form-control"><%=mybean.call_customer_voice%></textarea>
            	</div>
            	<div class="form-element6">
                 <label>Follow-up Time:<font color="#ff0000">*</font>:</label>
					<input name="txt_call_followup_time" type="text" class="form-control"  
                  id ="txt_call_followup_time" value = "<%=mybean.call_followup_time%>" size="20" maxlength="16" />
            	</div>
            	<div class="table table-border table-hover">
            	<table class="table table-hover">
     				<thead>
		                <tr>
		                <th>Booking</th>
		                </tr>
              	  	</thead>
                </table>
                </div>
                
              <div class="form-element6">
              <label>Contact:</label>
             <select name="dr_booking_contact_id" class="form-control" id="dr_booking_contact_id">   
                      <%=mybean.PopulateBookingContact(mybean.customer_id)%>
                    </select>
              </div>
              <div class="form-element6">
              <label>Service Advisor: </label>
                <select name="dr_booking_service_emp_id" class="form-control" id="dr_booking_service_emp_id">   
                      <%=mybean.PopulateServiceExecutive(mybean.call_branch_id, mybean.call_booking_time, mybean.comp_id)%>
                    </select>
              </div>
               <div class="form-element6">
              <label>Booking Time:</label>
                <input name="txt_call_booking_time" type="text" class="form-control"  
                id ="txt_call_booking_time" value = "<%=mybean.call_booking_time%>"
                 size="20" maxlength="16" onChange="PickupCheck();ServiceExeCheck();"/>
              </div>
              
  				 <div class="form-element6">
              <label>Parking Lot:</label>
                <select name="dr_booking_parking_id" class="form-control" id="dr_booking_parking_id">   
                      <%=mybean.PopulateParking(mybean.call_branch_id, mybean.call_booking_time, mybean.booking_id, mybean.comp_id)%>
                    </select>
                   	</div> 
                 
                 <div class="form-element6">
              <label>Instruction:</label>
               <textarea name="txt_booking_instruction" id="txt_booking_instruction" 
               cols="70" rows="5" class="form-control"><%=mybean.booking_instruction%></textarea>
                   	</div>    
              	<div class="row"></div>
                <%if(mybean.status.equals("Add")){%>
                <center><div id="tabs">
                      <ul>
                        <li><a href="#tabs-1">Vehicle Info</a></li>
                        <li><a href="#tabs-a">Customer</a></li>
                        <li><a href="#tabs-stock">Appt. Stock</a></li>
                        <li><a href="#tabs-2">Pickup</a></li>
                        <li><a href="#tabs-3">Courtesy Car</a></li>
                        <li><a href="#tabs-4">Job Cards</a></li>
                        <li><a href="#tabs-5">Insurance</a></li>
                        <li><a href="#tabs-6">Insurance Follow-up</a></li>
                        <li><a href="#tabs-7">Previous Calls</a></li>
                      </ul>
                      </div>
                </center>
                      <div id="tabs-1"><%=mybean.veh_info%></div>
                      <div id="tabs-a"><%=mybean.Customer_dash.CustomerDetails(response, mybean.customer_id, "",  mybean.comp_id)%></div>  
                      <div id="tabs-stock">
                      	<div class="table table-border table-hover">
            	<table class="table table-hover">
     				<thead>
		                <tr>
		                <th>Stock Status</th>
		                </tr>
              	  	</thead>
                </table>
                </div>
                <div class="form-element6">
				<label>Location<font color="#ff0000">*</font>:</label>
				<select name="dr_location_id" class="form-control" id="dr_location_id" 
				OnChange="SearchStockStatus(this.value);">  	
				<%=mybean.PopulateLocation(mybean.call_branch_id, mybean.comp_id)%>
				</select> &nbsp;&nbsp;&nbsp;&nbsp;Enter Search Text:
                <input type="text" id="txt_item" name="txt_item" 
                class="textbox" size="42" onkeyup="SearchStockStatus(this.value);" />
                </div>
                <div class="form-element6">
                   <div id="div_stock_status"> </div>
               </div>       
               <div class="table table-border table-hover">
            	<table class="table table-hover">
     				<thead>
		                <tr>
		                <th>Item Details</th>
		                </tr>
              	  	</thead>
                </table>
                </div>       
                        <div>
                           <div style="display:inline" id="booking_item_name"></div>
                           <input name="txt_item_id" type="hidden" id="txt_item_id"/>
                           <input name="txt_location_id" type="hidden" id="txt_location_id"/>
                           <input name="txt_session_id" type="hidden" id="txt_session_id" value="<%=mybean.session_id%>"/>  
                           <input name="txt_bookingcart_id" type="hidden" id="txt_bookingcart_id"/>                       
                        </div>
                         <div class="form-element6">
                            <label>Quantity:</label>
                              <input name="txt_item_qty" type="text" class="form-control" id="txt_item_qty"  size="10" maxlength="10"/>
                              <span id="uom_name"></span>
                             </div>
                              <!--input name="add_button" id="add_button" type="button" class="button" value="Add" onClick="AddBookingItem();"/-->
                 <div id="bookingitem_details"> </div>
                      </div>
                      <div id="tabs-2">
                      <div class="form-element8">
							<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">
										<center>
													Add Pickup
										</center>
								</div>
							</div>		
							<div class="portlet-body portlet-empty container-fluid">
							   <div class="form-element6">
                            		<label>Pickup Type:</label> 
                              <select name="dr_pickup_pickuptype_id" class="form-control" id="dr_pickup_pickuptype_id" >
                                  <%=mybean.PopulatePickupType()%>
                                </select>
                               </div>
                             <div class="form-element6">
                             <label>Driver/Technician:</label>
                             <span id="span_driver">
                                <select id="dr_pickup_emp_id" name="dr_pickup_emp_id" class="form-control" onChange="PickupCheck();">
                                  <%=mybean.PopulateDriver()%>
                                </select>
                                </span>
                             </div>
                                 <div class="form-element6">
                              <label> Location<font color="#ff0000">*</font>:</label>
                              
                              <select name="dr_pickup_location_id" class="form-control" id="dr_pickup_location_id" >
                                  <%=mybean.PopulatePickUpLocation(mybean.call_branch_id, mybean.comp_id)%>
                                </select>
                          </div>
                           <div class="form-element6">
                              <label> Contact Person:</label>
                             <input name="txt_pickup_contact_name" type="text" 
                             class="form-control"  id ="txt_pickup_contact_name" 
                             value = "<%=mybean.pickup_contact_name%>" size="20" maxlength="16" />
							</div>
							<div class="row"></div>	
							<div class="form-element6">
                              <label> Mobile 1<font color="#ff0000">*</font>:</label>
                              <input name="txt_pickup_mobile1" type="text" 
                              class="form-control"  id ="txt_pickup_mobile1" value = "<%=mybean.pickup_mobile1%>" size="20" maxlength="13" />
                            </div>
                             <div class="form-element6">
                              <label>Mobile 2:</label>
                              <input name="txt_pickup_mobile2" type="text" class="form-control"  
                              id ="txt_pickup_mobile2" value = "<%=mybean.pickup_mobile2%>" size="20" maxlength="13" />
                              </div>
                              <div class="form-element6">
                              <label>Address<font color="#ff0000">*</font>:</label>
                              <textarea name="txt_pickup_add" cols="40" rows="4" class="form-control" id="txt_pickup_add" onKeyUp="charcount('txt_pickup_add', 'span_pickup_add','<font color=red>({CHAR} characters left)</font>', '255')" ><%=mybean.pickup_add %></textarea>
                                <span id="span_pickup_add"> (255 Characters)</span><br>
                                </div>
                     <div class="form-element6">
                              <label>Landmark:<font color="#ff0000">*</font>:</label>
                              <textarea name="txt_pickup_landmark" cols="40" rows="4" class="form-control" id="txt_pickup_landmark" onKeyUp="charcount('txt_pickup_landmark', 'span_pickup_landmark','<font color=red>({CHAR} characters left)</font>', '255')" ><%=mybean.pickup_landmark %></textarea>
                      			  <span id="span_pickup_landmark"> (255 Characters)</span>
                      </div>
                      <div class="form-element">
                              <label>Instruction:</label>  
                              <textarea name="txt_pickup_instruction" cols="40" rows="4" class="form-control" id="txt_pickup_instruction" onKeyUp="charcount('txt_pickup_instruction', 'span_pickup_instruction','<font color=red>({CHAR} characters left)</font>', '1000')" style="width:250px"><%=mybean.pickup_instruction %></textarea>
                                <span id="span_pickup_instruction"> (1000 Characters)</span>
                      </div>
                      </div>
                      </div>
                      </div>
                      <div class="form-element4">
									<div class="portlet box">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">
												<center>Pickup Calendar</center>
											</div>
										</div>
										<div class="portlet-body portlet-empty" style="min-height: 486px;">
										<center>
											<div id="calpickup"></div>
											</center>
										</div>
									</div> 
								</div> 
                      </div>
                      <div id="tabs-3">
                       <div class="form-element8">
							<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">
										<center>
													Add Courtesy Car
										</center>
								</div>
							</div>		
							<div class="portlet-body portlet-empty container-fluid">
							  <div class="form-element6">
                              <label>Vehicle<font color="#ff0000">*</font>:</label>
                              <select name="dr_vehicle" class="form-control" id="dr_vehicle" onchange="PickupCheck();">
                                  <%=mybean.PopulateCourtesyVehicle(mybean.call_branch_id)%>
                                </select>
                                </div>
                                 <div class="form-element6">
                              <label>Start Time<font color="#ff0000">*</font>:</label>   
                              <input name="txt_courtesycar_startdate" type="text" class="form-control"  
                              id ="txt_courtesycar_startdate" value = "<%=mybean.courtesycar_time_from%>" size="20" maxlength="14" onChange="PickupCheck()"/>
                              </div>
                              <div class="row"></div>
                              <div class="form-element6">
                              <label>End Time<font color="#ff0000">*</font>:</label>
                              <input name="txt_courtesycar_enddate" type="text" class="form-control"  
                              id ="txt_courtesycar_enddate" value = "<%=mybean.courtesycar_time_to%>" size="20" maxlength="14" />
                        	</div>
                        	 <div class="form-element6">
                              <label>Contact Person:<font color="#ff0000">*</font>:</label>
                              <input name="txt_courtesycar_contact_name" type="text" class="form-control"
                                id ="txt_courtesycar_contact_name" value = "<%=mybean.courtesycar_contact_name%>" 
                                size="20" maxlength="16" />
                              </div>
                              <div class="form-element6">
                              <label>Mobile 1<font color="#ff0000">*</font>:</label>    
                           <input name="txt_courtesycar_mobile1" type="text" class="form-control"  
                           id ="txt_courtesycar_mobile1" value = "<%=mybean.courtesycar_mobile1%>" size="20" maxlength="10" />
                           </div>
                           <div class="form-element6">
                              <label>Mobile 2</label>
                              <input name="txt_courtesycar_mobile2" type="text" class="form-control"  id ="txt_courtesycar_mobile2" value = "<%=mybean.courtesycar_mobile2%>" size="20" maxlength="10" />
							</div>
							 <div class="form-element6">
                              <label>Address<font color="#ff0000">*</font>:</label>  
                              <textarea name="txt_courtesycar_add" cols="40" rows="4" class="form-control" id="txt_courtesycar_add" onKeyUp="charcount('txt_courtesycar_add', 'span_courtesycar_add','<font color=red>({CHAR} characters left)</font>', '255')" ><%=mybean.courtesycar_add %></textarea>
                                <span id="span_courtesycar_add"> (255 Characters)</span><br></td>
                                </div>
                            <div class="form-element6">
                           <label>Landmark<font color="#ff0000">*</font>:</label>
                              <textarea name="txt_courtesycar_landmark" cols="40" rows="4" class="form-control" 
                              id="txt_courtesycar_landmark" onKeyUp="charcount('txt_courtesycar_landmark', 'span_courtesycar_landmark','<font color=red>({CHAR} characters left)</font>', '255')" ><%=mybean.courtesycar_landmark %></textarea>
                                <span id="span_courtesycar_landmark"> (255 Characters)</span>
                            </div>
                            <div class="form-element6">
                              <label>Notes:</label> 
                              <textarea name="txt_courtesycar_notes" cols="40" rows="4" class="form-control" 
                              id="txt_courtesycar_notes" ><%=mybean.courtesycar_notes %></textarea>
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
											<div id="courtesycar"></div>
											</center>
										</div>
									</div> 
								</div> 
                      </div>
                      </div>
                      <div id="tabs-4">
                      <%if(!mybean.call_veh_id.equals("0")){%>
                      <table cellpadding="0" cellspacing="0" border="0" width="100%">
                      <tr>
                      <td align="right"><a href="jobcard-update.jsp?add=yes&veh_id=<%=mybean.call_veh_id%>">Add New Job Card</a></td>
                      </tr>
                      </table>
                      <%}%>
					  <%=mybean.jobcard_info%>
					  </div>
                      <div id="tabs-5"><%=mybean.insurance_info%></div>
                      <div id="tabs-6"><%=mybean.insurance_followup_info%></div>
                      <div id="tabs-7"><%=mybean.previous_calls_info%></div>
                    </div>
                <%}%>
                  <% if (mybean.status.equals("Update") &&!(mybean.call_entry_by == null) && !(mybean.call_entry_by.equals(""))) { %>
               <div class="form-element6">
                  <label>Entry By:</label>
                  <%=mybean.unescapehtml(mybean.call_entry_by)%>
               </div>
                <%}%>
                <% if (mybean.status.equals("Update") &&!(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>
                <div class="form-element6">
                  <label>Entry Date:</label>
                  <%=mybean.entry_date%>
               </div>
                <%}%>
                <% if (mybean.status.equals("Update") &&!(mybean.call_modified_by == null) && !(mybean.call_modified_by.equals(""))) { %>
                 <div class="form-element6">
                  <label>Modified By:</label>
                  <%=mybean.unescapehtml(mybean.call_modified_by)%>
               </div>
                <%}%>
                <% if (mybean.status.equals("Update") &&!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
              <div class="form-element6">
                  <label>Modified Date:</label>
                  <%=mybean.modified_date%>
               </div>
                <%}%>
                <div class="row"></div>
                <div class="form-element6">
                <input type="hidden" name="call_entry_by" value="<%=mybean.call_entry_by%>" />
                </div>
                <div class="form-element6">  
                    <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>" />
                 </div>
                 <div class="form-element6">
                    <input type="hidden" name="call_modified_by" value="<%=mybean.call_modified_by%>" />
                  </div>
                  <div class="form-element6">  
                    <input type="hidden" name="modified_date" value="<%=mybean.modified_date%>" />
                    </div>
                    <div class="row"></div>
                <center>
                  <%if(mybean.status.equals("Add")){%>
                    <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Call" onclick="return SubmitFormOnce(document.form1,this);"/>  
                    <input type="hidden" id="add_button" name="add_button" value="yes"/>
                    <%}else if (mybean.status.equals("Update")){%>
                    <input type="hidden" id="update_button" name="update_button" value="yes"/>
                    <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Call" onclick="return SubmitFormOnce(document.form1,this);"/>   
                    <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" onclick="return confirmdelete(this)" value="Delete Call"/>
                    <%}%>
                     </center>
                     </div>
                    
                     </div>
                    </form>
                     </div>
                     </div>
                      
                     </div>
                     </div>
    <%@include file="../Library/admin-footer.jsp" %>
    <%@include file="../Library/js.jsp"%>
	<script>  
	$(function() {
		$( "#tabs" ).tabs({
		event: "mouseover"
		});
	});

// JavaScript
$(function(){
    // Dialog
    $('#contact-dialog-modal').dialog({
        autoOpen: false,
        width: 900,
        height: 500,
        zIndex: 200,
        modal: true,
        title: "Select Contact"
    });
    $('#veh_contact_link').click(function(){

        $.ajax({
            //url: "home.jsp",
            success: function(data){
                $('#contact-dialog-modal').html('<iframe src="../customer/customer-contact-list.jsp?group=select_call_contact" width="100%" height="100%" frameborder=0></iframe>');  
            }
        });
		$('#contact-dialog-modal').dialog('open');  
        return true;
    });
});

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
    $('#veh_call_link').click(function(){
	$.ajax({
            //url: "home.jsp",
            success: function(data){
                $('#vehicle-dialog-modal').html('<iframe src="../service/vehicle-list.jsp?group=select_veh_call" width="100%" height="100%" frameborder=0></iframe>');
            }
        });
		$('#vehicle-dialog-modal').dialog('open');    
        return true;
    });
});

$(function() {
	$( "#txt_call_time" ).datetimepicker({
		dateFormat: 'dd/mm/yy',
		stepMinute: 5
		});	 
    $( "#txt_call_booking_time" ).datetimepicker({
		dateFormat: 'dd/mm/yy',
		stepMinute: 5
		});	
    $( "#txt_call_followup_time" ).datetimepicker({
		dateFormat: 'dd/mm/yy',
		stepMinute: 5
		});	
	 
		
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
	<script language="JavaScript" type="text/javascript">
function BranchCheck() {  
	var status = document.getElementById("txt_status").value;
	var pickupdate=document.getElementById("txt_call_booking_time").value;  
	var call_branch_id=document.getElementById("dr_call_branch_id").value;
	var booking_id=document.getElementById("txt_booking_id").value;
	if(status=='Add'){    
		var driver_id=document.getElementById("dr_pickup_emp_id").value;
		//for Courtesy Vehicle According to branch  
		var veh_id=document.getElementById("dr_vehicle").value;    
		showHint('../service/pickup-check.jsp?branch_id='+call_branch_id+'',  'span_driver');
		showHint('../service/pickup-check.jsp?pickup=yes&pickupdate='+pickupdate+'&driver_id='+driver_id+'&branch_id='+call_branch_id+'', 'calpickup'); 
		showHint('../service/pickup-check.jsp?pickup_location=yes&branch_id='+call_branch_id, 'dr_pickup_location_id');
		//for Courtesy Vehicle According to branch      
		showHint('../service/courtesy-car-check.jsp?courtesy_veh=yes&branch_id='+call_branch_id, 'dr_vehicle'); 
	}
	//for PickUp According to branch     
	//alert("call_branch_id===" + call_branch_id);     
	showHint('../service/call-check.jsp?call_branch_id=' + call_branch_id+'&call_emp=yes', 'dr_call_emp_id');  
	showHint('../service/call-check.jsp?call_branch_id=' + call_branch_id+'&booking_time='+pickupdate+'&service_emp=yes', 'dr_booking_service_emp_id');   
	showHint('../service/call-check.jsp?call_branch_id=' + call_branch_id+'&booking_id='+booking_id+'&booking_time='+pickupdate+'&parking=yes', 'dr_booking_parking_id');   
	showHint('../service/call-check.jsp?call_branch_id=' + call_branch_id+'&location=yes', 'dr_location_id'); 
}

function SearchStockStatus(itemName){
					var search_text = document.getElementById("txt_item").value;
					var location_id = document.getElementById("dr_location_id").value;
					showHint('call-check.jsp?stock_status=yes&location_id='+location_id+'&item_name='+itemName+'&search_text='+search_text, 'div_stock_status');
					}
    
function PickupCheck()
{	//PickUp calendar..  
	var status = document.getElementById("txt_status").value;
	var pickupdate=document.getElementById("txt_call_booking_time").value; 
	var branch_id=document.getElementById("dr_call_branch_id").value;
	var booking_id=document.getElementById("txt_booking_id").value;
	if(status=='Add'){    
		var driver_id=document.getElementById("dr_pickup_emp_id").value; 
		//for CourtesyCar calendar
		var veh_id=document.getElementById("dr_vehicle").value;
		var courtesydate=document.getElementById("txt_courtesycar_startdate").value;
		//for Pickup calendar 
		showHint('../service/pickup-check.jsp?pickup=yes&driver_id='+driver_id+'&pickupdate='+pickupdate+'&branch_id='+branch_id+'',  'calpickup');  
		//for CourtesyCar calendar  
		showHint('../service/courtesy-car-check.jsp?veh_id='+veh_id+'&courtesydate='+courtesydate+'&branch_id='+branch_id+'&demo=yes',  'courtesycar');    
	     }     
	//alert("branch_id==="+branch_id);	 
	showHint('../service/call-check.jsp?call_branch_id=' + branch_id+'&booking_id='+booking_id+'&booking_time='+pickupdate+'&parking=yes',  'dr_booking_parking_id');        
}      

function ServiceExeCheck() {
	var pickupdate=document.getElementById("txt_call_booking_time").value; 
	var branch_id=document.getElementById("dr_call_branch_id").value;
	showHint('../service/call-check.jsp?call_branch_id=' + branch_id+'&booking_time='+pickupdate+'&service_emp=yes',  'dr_booking_service_emp_id'); 
}

function FormFocus() { //v1.0
 // document.form1.txt_stock_name.focus();
} 
//For selecting existing contact
function SelectContact(contact_id, contact_name, customer_id, customer_name, contact_address, contact_landmark, contact_mobile1, contact_mobile2, hide){ 
	var contact_address = contact_address.replace("single_quote","'");
	document.getElementById("span_contact_id").value = contact_id;     
	document.getElementById("span_call_contact_id").innerHTML = "<a href=../customer/customer-contact-list.jsp?contact_id="+contact_id+">" +contact_name+ "</a>"; 
	 document.getElementById("span_call_customer_id").innerHTML = "<a href=../customer/customer-list.jsp?customer_id="+customer_id+">" +customer_name+ "</a>"; 
	document.getElementById("customer_col").style.display = "";
	var status = document.getElementById("txt_status").value;
	if(status=='Add'){
	showHint('../service/customer-dash-check.jsp?customer_id='+customer_id+'&customerinfo=yes','tabs-a'); 
	}
	
	showHint('../service/call-check.jsp?customer_id='+customer_id+'&contact=yes','dr_booking_contact_id');
	
	var x = document.getElementById("txt_status").value;
	if(x == 'Add') {              
	//Populating PickUp  
	document.getElementById("txt_pickup_contact_name").value = contact_name;   
	document.getElementById("txt_pickup_mobile1").value = contact_mobile1;
	document.getElementById("txt_pickup_mobile2").value = contact_mobile2;
	document.getElementById("txt_pickup_add").value = contact_address;
    document.getElementById("txt_pickup_landmark").value = contact_landmark;
	//Populating Courtesy  
	document.getElementById("txt_courtesycar_contact_name").value = contact_name;   
	document.getElementById("txt_courtesycar_mobile1").value = contact_mobile1;
	document.getElementById("txt_courtesycar_mobile2").value = contact_mobile2;
	document.getElementById("txt_courtesycar_add").value = contact_address;
	//alert();
    document.getElementById("txt_courtesycar_landmark").value = contact_landmark; 
	}      
	document.getElementById("courtesycar_customer_id").value = customer_id; 
	document.getElementById("pickup_customer_id").value = customer_id;
	if(hide=='hide'){          
	document.getElementById("span_veh_id").value = '0';   
	document.getElementById("txt_veh_id").value = '0'; 
	document.getElementById("span_call_veh_id").innerHTML = "";
	document.getElementById("txt_item_name").value = "";  
	document.getElementById("item_col").style.display = "none";
	document.getElementById("item_name").innerHTML = "";
	var status = document.getElementById("txt_status").value;
	if(status=='Add'){
	showHint('../service/vehicle-dash-check.jsp?veh_id=0&vehicleinfo=yes','tabs-1');   
	showHint('../service/vehicle-dash-jobcard-check.jsp?veh_id=0&jobcardinfo=yes','tabs-4');  
	showHint('../service/vehicle-dash-insurance-check.jsp?veh_id=0&insuranceinfo=yes','tabs-5');  
	showHint('../service/vehicle-dash-insurance-followup-check.jsp?veh_id=0&followupinfo=yes','tabs-6');
	}
	}      
	$('#contact-dialog-modal').dialog('close');     
	$('#vehicle-dialog-modal').dialog('close');  
}

function SelectVehicle(veh_id, veh_reg_no, contact_id, contact_name, customer_id, customer_name, item_name, item_id, contact_address, contact_landmark, contact_mobile1, contact_mobile2) {
	var contact_address = contact_address.replace("single_quote","'");
	document.getElementById("span_veh_id").value = veh_id;          
	document.getElementById("span_call_veh_id").innerHTML = "<a href=../service/vehicle-list.jsp?veh_id="+veh_id+">" + veh_reg_no + "</a>";
	document.getElementById("txt_item_name").value = item_name;
	document.getElementById("item_col").style.display = "";
	document.getElementById("item_name").innerHTML = "<b><a href=../inventory/inventory-item-list.jsp?item_id="+item_id+">" +item_name+ "</a></b>";
	SelectContact(contact_id, contact_name, customer_id, customer_name, contact_address, contact_landmark, contact_mobile1, contact_mobile2, '');
	//ajax call for showing vehicle info  
	var status = document.getElementById("txt_status").value;
	if(status=='Add'){
		showHint('../service/vehicle-dash-check.jsp?veh_id='+veh_id+'&vehicleinfo=yes','tabs-1');   
		showHint('../service/vehicle-dash-jobcard-check.jsp?veh_id='+veh_id+'&jobcardinfo=yes','tabs-4');  
		showHint('../service/vehicle-dash-insurance-check.jsp?veh_id='+veh_id+'&insuranceinfo=yes','tabs-5');  
		showHint('../service/vehicle-dash-insurance-followup-check.jsp?veh_id='+veh_id+'&followupinfo=yes','tabs-6');
	}
	$('#contact-dialog-modal').dialog('close'); 
	$('#vehicle-dialog-modal').dialog('close');                       
}  

function PopulateDetails(item_id, item_name, item_qty, locatoin_id, uom_name, cart_id, mode){
		document.getElementById("booking_item_name").innerHTML = item_name.replace(/single_quote/g,"'");
		document.getElementById("uom_name").innerHTML = uom_name;
		document.getElementById("txt_item_id").value = item_id;
		document.getElementById("txt_location_id").value = locatoin_id;
		document.getElementById("txt_bookingcart_id").value = cart_id;
		document.getElementById("txt_item_qty").value = item_qty;
		if(mode=='add'){
        	document.getElementById("mode_button").innerHTML = "<input name=add_button id=add_button type=button class=button value='Add' onClick='AddBookingItem();'/>";
    	}else if(mode=='update')	  {
        	document.getElementById("mode_button").innerHTML = "<input name=update_button id=update_button type=button class=button value='Update' onClick='UpdateBookingItem();'/>";
    	}
	}     

// for add item to appt cart table
function AddBookingItem(){
	var apptcart_item_id=document.getElementById("txt_item_id").value;
	var booking_location_id=document.getElementById("dr_location_id").value;
    var apptcart_qty=parseInt(document.getElementById("txt_item_qty").value);
	var session_id = document.getElementById("txt_session_id").value;
	//if((parseInt(jcitem_qty) <= parseInt(current_qty)) || current_qty=='_'){
   if(apptcart_qty == 0 || apptcart_qty=='' || apptcart_qty==null || isNaN(apptcart_qty)==true){
            apptcart_qty = 1;
        }
		
		 showHint('booking-item-details.jsp?apptcart_item_id='+apptcart_item_id+'&booking_location_id='+booking_location_id+'&session_id='+session_id+'&apptcart_qty='+apptcart_qty+'&add_cartitem=yes','bookingitem_details');
		document.getElementById('mode_button').innerHTML = ' ';
        document.getElementById('txt_item_id').value = 0;
        document.getElementById('txt_location_id').value = 0;
        document.getElementById('txt_item_qty').value = 0;
        document.getElementById('booking_item_name').innerHTML = '';
        document.getElementById('txt_item').focus();
	}
	
	// for add item to appt cart table
function UpdateBookingItem(){
	var apptcart_item_id=document.getElementById("txt_item_id").value;
	var booking_location_id=document.getElementById("txt_location_id").value;
	var session_id = document.getElementById("txt_session_id").value;
	var apptcart_id = document.getElementById("txt_bookingcart_id").value;
	var apptcart_qty = parseInt(document.getElementById("txt_item_qty").value);
	//if((parseInt(jcitem_qty) <= parseInt(current_qty)) || current_qty=='_'){
    if(apptcart_qty == 0 || apptcart_qty=='' || apptcart_qty==null || isNaN(apptcart_qty)==true){
            apptcart_qty = 1;
        }
		
		 showHint('booking-item-details.jsp?apptcart_id='+apptcart_id+'&apptcart_item_id='+apptcart_item_id+'&booking_location_id='+booking_location_id+'&session_id='+session_id+'&apptcart_qty='+apptcart_qty+'&update_cartitem=yes','bookingitem_details');
		document.getElementById('mode_button').innerHTML = ' ';
        document.getElementById('txt_item_id').value = 0;
        document.getElementById('txt_location_id').value = 0;
        document.getElementById('txt_item_qty').value = 0;
        document.getElementById('booking_item_name').innerHTML = '';
        document.getElementById('txt_item').focus();
	}
	
	function ListApptCartItem(){
		var session_id = document.getElementById("txt_session_id").value;
		showHint('booking-item-details.jsp?session_id='+session_id+'&list_cartitems=yes','bookingitem_details');
		}
	
	function delete_cart_item(apptcart_id){
		var session_id = document.getElementById("txt_session_id").value;
		showHint('booking-item-details.jsp?session_id='+session_id+'&apptcart_id='+apptcart_id+'&delete_cartitem=yes','bookingitem_details');
		}
	
	</script>
    </body>
</HTML>
