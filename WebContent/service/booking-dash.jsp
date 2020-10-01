<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Booking_Dash" scope="request"/>
<%mybean.doPost(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>
            <HEAD>
            <title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
            <meta http-equiv="pragma" content="no-cache"/>
            <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
            <link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico"/>
            
            <script type="text/javascript" src="../Library/Validate.js"></script>
            <script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
            <link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
	<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
		<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />


	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
            <script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
            <script type="text/javascript">
                $(function() {
                    $("#tabs").tabs();
                });
			</script>
           
            <script language="JavaScript" type="text/javascript">
		function ApptUpdate(){
						var booking_id = document.getElementById("txt_booking_id").value;
						PopulateApptDash(booking_id);
					}
				
		function PopulateApptDash(booking_id){   
			     $("#div_callman").load('../service/booking-dash.jsp?booking_id='+booking_id, (function() {  $( "#tabs" ).tabs();                        
}));
			}  
			
			function PopulateParking(){
		var booking_id = document.getElementById("txt_booking_id").value;
		var booking_time=document.getElementById("txt_booking_booking_time").value; 
		var branch_id = document.getElementById("dr_booking_branch_id").value;
		showHint('../service/booking-check.jsp?branch_id=' + branch_id+'&booking_id='+booking_id+'&booking_time='+booking_time+'&parking=yes', 'dr_parking_id');          
		showHint('../service/booking-check.jsp?branch_id='+branch_id+'&emp=yes','dr_booking_service_emp_id');
		showHint('../service/pickup-check.jsp?pickup_location=yes&branch_id='+branch_id,'dr_pickup_location_id');
		showHint('../service/courtesy-car-check.jsp?courtesy_veh=yes&branch_id='+branch_id,'dr_vehicle');
		}
		
	function ParkingCheck(){
		var booking_id = document.getElementById("txt_booking_id").value;
		var booking_time=document.getElementById("txt_booking_booking_time").value; 
		var branch_id=document.getElementById("dr_booking_branch_id").value;
		showHint('../service/booking-check.jsp?branch_id=' + branch_id+'&booking_id='+booking_id+'&booking_time='+booking_time+'&parking=yes','dr_parking_id');        
}      
			
			function PickupCheck(){	//PickUp calendar..  
				var pickupdate=document.getElementById("txt_booking_booking_time").value; 
				var branch_id=document.getElementById("dr_booking_branch_id").value;
				var booking_id=document.getElementById("txt_booking_id").value;
    			var driver_id=document.getElementById("dr_pickup_emp_id").value; 
				//for CourtesyCar calendar
				var veh_id=document.getElementById("dr_vehicle").value;
				var courtesydate=document.getElementById("txt_courtesycar_startdate").value;
				//for Pickup calendar 
				showHint('../service/pickup-check.jsp?pickup=yes&driver_id='+driver_id+'&pickupdate='+pickupdate+'&branch_id='+branch_id+'', 'calpickup');  
				//for CourtesyCar calendar  
				showHint('../service/courtesy-car-check.jsp?veh_id='+veh_id+'&courtesydate='+courtesydate+'&branch_id='+branch_id+'&demo=yes', 'courtesycar');    
	 			showHint('../service/call-check.jsp?call_branch_id=' + branch_id+'&booking_id='+booking_id+'&booking_time='+pickupdate+'&parking=yes', 'dr_parking_id');        
			}    
			 
	</script>
     <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
		<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <form name="apptform" method="post" action="booking-dash.jsp?booking_id=<%=mybean.booking_id%>">
            <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
            <tr><td align="center" valign="top"><font color="#ff0000" ><b><%=mybean.msg%></b></font></td></tr>
              <tr valign="top" bgcolor="#FFFFFF">
                <td colspan="4" align="center" height="300"><div id="tabs">
                  <ul>
                    <li><a href="#tabs-1">Booking</a></li>
                    <li><a href="#tabs-2">Pickup</a></li>
                    <li><a href="#tabs-3">Courtesy Car</a></li>
                  </ul>
                    <div id="tabs-1">
                    <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0"  class="tableborder">
                        <tr>
                        <td align="center" valign="top"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
                            <tr align="center">
                            	<th colspan="4">Booking</th>
                          	</tr>
                            <tr>
                            	<td>
                                <input type="hidden" id="txt_call_id" name="txt_call_id" value="<%=mybean.call_id%>" />
                                <input type="hidden" id="txt_booking_id" name="txt_booking_id" value="<%=mybean.booking_id%>" />
                                <input type="hidden" name="txt_veh_id" id="txt_veh_id" value="<%=mybean.veh_id%>"/>
                    			<input type="hidden" name="txt_customer_id" id="txt_customer_id" value="<%=mybean.customer_id%>"/>
                                <input type="hidden" id="txt_pickup_id" name="txt_pickup_id" value="<%=mybean.pickup_id%>" />
                                <input type="hidden" id="txt_courtesycar_id" name="txt_courtesycar_id" value="<%=mybean.courtesycar_id%>" />
                                <input type="hidden" id="txt_call_time" name="txt_call_time" value="<%=mybean.call_time%>" /> 
                                </td>
                            <td align="left" colspan="2"><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br />
                              </font></td>
                          </tr>
                            <tr valign="middle">
                            <td align="right">Branch<font color="#ff0000">*</font>:</td>
                            <td><select name="dr_booking_branch_id" class="selectbox" id="dr_booking_branch_id" onChange="PopulateParking();">
            <%=mybean.PopulateBranch(mybean.booking_branch_id, "", "1,3", "", request)%>
                              </select>
                                </td>
                          </tr>
                            <tr valign="middle">
                            <td align="right" valign="top">Contact<font color="#ff0000">*</font>:</td>
                            <td align="left"><select name="dr_booking_contact_id" class="selectbox" id="dr_booking_contact_id">
                                <%=mybean.PopulateBookingContact(mybean.customer_id)%>
                              </select></td>
                          </tr>
                            <tr>
                            <td align="right" valign="top">Vehicle ID:</td>
                            <td ><b><a href="vehicle-list.jsp?veh_id=<%=mybean.veh_id%>"><%=mybean.veh_id%></a></b></td>
                          </tr>
                            <tr>
                            <td align="right">Vehicle Reg. No.:</td>
                            <td><b><%=mybean.link_vehicle_name%></b></td>
                          </tr>
                            <tr>
                            <td align="right" valign="top">Item:</td>
                            <td><b><%=mybean.link_item_name%></b></td>
                          </tr>
                            <tr>
                            <td align="right">Service Advisor<font color="#ff0000">*</font>:</td>
                            <td align="left"><select id="dr_booking_service_emp_id" name="dr_booking_service_emp_id" class="selectbox">
                                <%=mybean.PopulateServiceExecutive(mybean.booking_branch_id)%>
                              </select>
                                </td>
                          </tr>
                            <tr valign="middle">
                            <td align="right" valign="middle">Booking Time<font color="#ff0000">*</font>:</td>
                            <td align="left"><input name="txt_booking_booking_time" type="text" class="textbox" id ="txt_booking_booking_time" value = "<%=mybean.booking_time%>" size="20" maxlength="16"  onChange="PickupCheck()"/>   
                            <script type="application/javascript">	
$(function() {   
	 
	$( "#txt_courtesycar_startdate" ).datetimepicker({
		dateFormat: 'dd/mm/yy',
		stepMinute: 5
	});	 
    $( "#txt_courtesycar_enddate" ).datetimepicker({
		dateFormat: 'dd/mm/yy',
		stepMinute: 5
	});	
    $( "#txt_booking_booking_time" ).datetimepicker({
		dateFormat: 'dd/mm/yy',
		stepMinute: 5  
	});	
});	
		</script>
                       </td>
                          </tr>
                            <tr>
                            <td align="right" valign="top">Parking<font color="#ff0000">*</font>:</td>
                            <td ><select id="dr_parking_id" name="dr_parking_id" class="selectbox">
                                <%=mybean.PopulateParking(mybean.booking_branch_id, mybean.booking_time, mybean.booking_id)%>
                              </select></td>
                          </tr>
                            <tr>
                            <td align="right" valign="top">Active:</td>
                            <td align="left" ><input id="chk_booking_active" type="checkbox" name="chk_booking_active" <%=mybean.PopulateCheck(mybean.booking_active)%> /></td>
                          </tr>
                          </table></td>
                      </tr>
                      </table>
                  </div>
                    <div id="tabs-2">
                    <table width="100%" border="1" cellpadding="0" cellspacing="0" class="listtable">
                        <tbody>
                        <tr align="middle">
                            <th colspan="2">Add Pickup</th>
                            <th width="50%">Pickup Calendar</th>
                          </tr>
                        <tr>
                            <td align="right" valign="top">Pickup:</td>
                            <td align="left" ><input id="chk_pickup_active" type="checkbox" name="chk_pickup_active" <%=mybean.PopulateCheck(mybean.pickup_active)%> /></td>
                            <td rowspan="10" valign="top" align="center"><div id="calpickup"></div></td>
                          </tr>
                        <tr valign="center">
                            <td align="right">Pickup Type:</td>
                            <td><select name="dr_pickup_pickuptype_id" class="selectbox" id="dr_pickup_pickuptype_id" >
                                <%=mybean.PopulatePickupType()%>
                              </select></td>
                          </tr>
                        <tr valign="center">
                            <td align="right" >Driver:</td>
                            <td align="left"><span id="span_driver">
                              <select id="dr_pickup_emp_id" name="dr_pickup_emp_id" class="selectbox" onChange="PickupCheck();">
                              <%=mybean.PopulateDriver()%>
                            </select>
                              </span>
                            </td>
                          </tr>
                        <tr valign="center">
                            <td align="right"> Location<font color="#ff0000">*</font>: </td>
                            <td><select name="dr_pickup_location_id" class="selectbox" id="dr_pickup_location_id" >
                                <%=mybean.PopulatePickUpLocation(mybean.booking_branch_id)%>
                              </select>
                            </td>
                          </tr>
                        <tr valign="middle">
                            <td align="right" valign="middle"> Contact Person:</td>
                            <td align="left"><input name="txt_pickup_contact_name" type="text" class="textbox"  id ="txt_pickup_contact_name" value = "<%=mybean.pickup_contact_name%>" size="20" maxlength="16"/></td>
                          </tr>
                        <tr valign="middle">
                            <td align="right" valign="middle"> Mobile 1<font color="#ff0000">*</font>:</td>
                            <td align="left"><input name="txt_pickup_mobile1" type="text" class="textbox"  id ="txt_pickup_mobile1" value = "<%=mybean.pickup_mobile1%>" size="20" maxlength="13" /></td>
                          </tr>
                        <tr valign="middle">
                            <td align="right" valign="middle"> Mobile 2:</td>
                            <td align="left"><input name="txt_pickup_mobile2" type="text" class="textbox"  id ="txt_pickup_mobile2" value = "<%=mybean.pickup_mobile2%>" size="20" maxlength="13" /></td>
                          </tr>
                        <tr valign="middle">
                            <td align="right" valign="top">Address<font color="#ff0000">*</font>:</td>
                            <td align="left"><textarea name="txt_pickup_add" cols="40" rows="4" class="textbox" id="txt_pickup_add" onKeyUp="charcount('txt_pickup_add', 'span_pickup_add','<font color=red>({CHAR} characters left)</font>', '255')" style="width:250px"><%=mybean.pickup_add%></textarea>
                            <br/>
                            <span id="span_pickup_add"> (255 Characters)</span><br/></td>
                          </tr>
                        <tr valign="middle">
                            <td align="right" valign="top">Landmark<font color="#ff0000">*</font>:</td>
                            <td align="left"><textarea name="txt_pickup_landmark" cols="40" rows="4" class="textbox" id="txt_pickup_landmark" onKeyUp="charcount('txt_pickup_landmark', 'span_pickup_landmark','<font color=red>({CHAR} characters left)</font>', '255')" style="width:250px"><%=mybean.pickup_landmark%></textarea>
                            <br/>
                            <span id="span_pickup_landmark"> (255 Characters)</span></td>
                          </tr>
                        <tr valign="center">
                            <td align="right"> Instruction: </td>
                            <td><textarea name="txt_pickup_instruction" cols="40" rows="4" class="textbox" id="txt_pickup_instruction" onKeyUp="charcount('txt_pickup_instruction', 'span_pickup_instruction','<font color=red>({CHAR} characters left)</font>', '1000')" style="width:250px"><%=mybean.pickup_instruction%></textarea>
                            <br/>
                            <span id="span_pickup_instruction"> (1000 Characters)</span></td>
                          </tr>
                      </tbody>
                      </table>
                  </div>
                    <div id="tabs-3">
                    <table width="100%" border="1" cellpadding="0" cellspacing="0" class="listtable">
                        <tbody>
                        <tr align="middle">
                            <th colspan="2">Add Courtesy Car</th>
                            <th width="50%">Courtesy Car Calendar</th>
                          </tr>
                        <tr valign="center">
                          <tr>
                            <td align="right" valign="top">Courtesy Car:</td>
                            <td align="left" ><input id="chk_courtesy_active" type="checkbox" name="chk_courtesy_active" <%=mybean.PopulateCheck(mybean.courtesy_active)%> /></td>
                            <td rowspan="10" valign="top" align="center"><div id="courtesycar"></div></td>
                          </tr>
                      <td align="right" >Vehicle<font color="#ff0000">*</font>:</td>
                        <td><select name="dr_vehicle" class="selectbox" id="dr_vehicle" onChange="PickupCheck();">
                            <%=mybean.PopulateCourtesyVehicle(mybean.booking_branch_id)%>
                          </select>
                          </td>
                      </tr>
                        <tr valign="center">
                        <td align="right">Start Time<font color="#ff0000">*</font>: </td>
                        <td><input name="txt_courtesycar_startdate" type="text" class="textbox"  id ="txt_courtesycar_startdate" value = "<%=mybean.courtesycar_time_from%>" size="20" maxlength="14" onChange="PickupCheck();"/></td>
                      </tr>
                        <tr valign="center">
                        <td align="right"> End Time<font color="#ff0000">*</font>: </td>
                        <td><input name="txt_courtesycar_enddate" type="text" class="textbox"  id ="txt_courtesycar_enddate" value = "<%=mybean.courtesycar_time_to%>" size="20" maxlength="14" /></td>
                      </tr>
                        <tr valign="middle">
                        <td align="right" valign="middle"> Contact Person:</td>
                        <td align="left"><input name="txt_courtesycar_contact_name" type="text" class="textbox"  id ="txt_courtesycar_contact_name" value = "<%=mybean.courtesycar_contact_name%>" size="20" maxlength="16" style="width:250px"/></td>
                      </tr>
                        <tr valign="middle">
                        <td align="right" valign="middle"> Mobile 1<font color="#ff0000">*</font>:</td>
                        <td align="left"><input name="txt_courtesycar_mobile1" type="text" class="textbox"  id ="txt_courtesycar_mobile1" value = "<%=mybean.courtesycar_mobile1%>" size="20" maxlength="10" /></td>
                      </tr>
                        <tr valign="middle">
                        <td align="right" valign="middle"> Mobile 2:</td>
                        <td align="left"><input name="txt_courtesycar_mobile2" type="text" class="textbox"  id ="txt_courtesycar_mobile2" value = "<%=mybean.courtesycar_mobile2%>" size="20" maxlength="10" /></td>
                      </tr>
                        <tr valign="middle">
                        <td align="right" valign="top">Address<font color="#ff0000">*</font>:</td>
                        <td align="left"><textarea name="txt_courtesycar_add" cols="40" rows="4" class="textbox" id="txt_courtesycar_add" onKeyUp="charcount('txt_courtesycar_add', 'span_courtesycar_add','<font color=red>({CHAR} characters left)</font>', '255')" style="width:250px"><%=mybean.courtesycar_add%></textarea>
                            <br/>
                            <span id="span_courtesycar_add"> (255 Characters)</span><br/></td>
                      </tr>
                        <tr valign="middle">
                        <td align="right" valign="top">Landmark<font color="#ff0000">*</font>:</td>
                        <td align="left"><textarea name="txt_courtesycar_landmark" cols="40" rows="4" class="textbox" id="txt_courtesycar_landmark" onKeyUp="charcount('txt_courtesycar_landmark', 'span_courtesycar_landmark','<font color=red>({CHAR} characters left)</font>', '255')" style="width:250px"><%=mybean.courtesycar_landmark%></textarea>
                            <br/>
                            <span id="span_courtesycar_landmark"> (255 Characters)</span></td>
                      </tr>
                        <tr valign="center">
                        <td align="right"> Notes: </td>
                        <td><textarea name="txt_courtesycar_notes" cols="40" rows="4" class="textbox" id="txt_courtesycar_notes" style="width:250px"><%=mybean.courtesycar_notes%></textarea></td>
                      </tr>
                          </tbody>
                        
                      </table>
                  </div>
                  </div></td>
              </tr>
              <tr>
                <td align="center"><input type="hidden" id="update_button" name="update_button" value="yes"/>
                  <input name="updatebutton" type="submit" class="button" id="updatebutton" value="Update Booking" onclick="return SubmitFormOnce(document.apptform,this);"/>
                  <input name="delete_button" type="button" class="button" id="delete_button" onclick="return confirmdelete(this)" value="Delete Booking"/></td>
              </tr>
            </table>
            </form>
</body>          
</HTML>