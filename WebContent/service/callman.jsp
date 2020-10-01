<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Callman" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>
        <HEAD>
        <title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
        <meta http-equiv="pragma" content="no-cache"/>
        <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
        <link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">
        
        <script type="text/javascript" src="../Library/Validate.js?"></script>
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
        <script language="JavaScript" type="text/javascript"> 
 
$(function(){
    // Dialog
});

        $(function() {   
		$("#tabs").tabs();     
            
    	$("#txt_veh_modelyear").datepicker({               
			dateFormat: 'yy',            
			stepMinute: 5                         
		});	
		
	 	$("#txt_veh_sale_date").datepicker({
			dateFormat: 'dd/mm/yy',
			stepMinute: 5
		});
		
		$('#txt_delivery_date').datepicker({
             showButtonPanel: true,        
             dateFormat: 'dd/mm/yy',
             showOn : 'focus'
		});
						
		$('#txt_veh_service_duedate').datepicker({
             showButtonPanel: true,        
             dateFormat: 'dd/mm/yy',
             showOn : 'focus'
		});
		
		$("#txt_insurfollowup_time").datetimepicker({
      		controlType: 'select',
	  		stepMinute: 5,
      		dateFormat: 'dd/mm/yy',
	  		timeFormat: 'HH:mm',
	  		hour: 10,
	  		minute: 00
    	});
		
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
        <script language="JavaScript" type="text/javascript">     
		var i = 0;
		
		function OnFocus(id){
			if(id.value=='Search') { 
				id.value = ''; 
			}
		}
			
		function OnBlur(id){
			if(id.value=='') { 
				id.value = 'Search';                                     
			}
		}
		
		function AddNewContact(customer_id){
			$('#dialog-modal-contact').dialog({
							autoOpen: false,
							width: 900,
							height: 500,
							zIndex: 200,
							modal: true,
							title: "Add New Contact"
						});
						
						//$('#new_contact_link').click(function(){
								var customer_id = document.getElementById("txt_customer_id").value;
							$.ajax({
								success: function(data){
									$('#dialog-modal-contact').html('<iframe src="../customer/customer-contact-update.jsp?Add=yes&customer_id='+customer_id+'&modal=yes" width="100%" height="100%" frameborder=0></iframe>');
								}  
							}); 
							
							$('#dialog-modal-contact').dialog('open');
								//return true;
						//});
						//alert("1");
			}
			
			function CloseModal(customer_id){
				showHintPost('vehicle-dash-check.jsp?customer_details=yes&customer_id='+customer_id,  customer_id, 'tabs-2');
				$('#dialog-modal-contact').dialog('close');
				}
			
		function ContactCheck(name){
			var value = document.getElementById("txt_search").value;
            var url = "../service/contact-check.jsp?";
	    	var param = "q="+ value;     
		   	var str = "123";             
            showHint(url+param, "div_callman");
        }
			
		function PopulateVehicleDash(veh_id){
			$('#div_callman').html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
				$.ajax({
					url: '../service/vehicle-dash.jsp?veh_id='+veh_id+'&modal=yes',
					type: 'GET',
					success: function (data){
						$('#div_callman').fadeIn(500).html('' + data + '');
						$( "#tabs" ).tabs();  
			
						$("#txt_veh_modelyear").datepicker({               
							dateFormat: 'yy'                      
						});	
		
						$("#txt_veh_sale_date").datepicker({
							dateFormat: 'dd/mm/yy'
						});
		
						$('#txt_delivery_date').datepicker({
							 showButtonPanel: true,        
							 dateFormat: 'dd/mm/yy',
							 showOn : 'focus'
						});
						
						$('#txt_veh_service_duedate').datepicker({
							 showButtonPanel: true,        
							 dateFormat: 'dd/mm/yy',
							 showOn : 'focus'  
						});
						
						$("#txt_veh_warranty_expirydate").datepicker({             
							dateFormat: 'dd/mm/yy'
						});
		
						$("#txt_insurfollowup_time").datetimepicker({
							controlType: 'select',
							stepMinute: 5,
							dateFormat: 'dd/mm/yy',
							timeFormat: 'HH:mm',
							hour: 10,
							minute: 00
						});  		
				}
		});
						
						
}   

            function PopulateJCDash(jc_id){
	$('#div_callman').html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
		$.ajax({
			url: '../service/jobcard-dash.jsp?jc_id='+jc_id+'&modal=yes',
			type: 'GET',
			success: function (data){
					$('#div_callman').fadeIn(500).html(data);
					$( "#tabs" ).tabs(); 
			}
		});
}   
			
			function PopulateApptDash(booking_id){  
					$("#div_callman").ready(function(){
							$("#div_callman").html("<iframe id=booking_frame src=booking-dash.jsp?booking_id="+booking_id+" height=800 width=100% frameborder=0 scrolling=no marginleft=0 marginright=0>");
						});
			}
		   
		function CallSearch(param){
			    var search_param = document.getElementById("dr_search_param").value;
				var branch_id = document.getElementById("dr_branch_id").value;
				var emp_id = document.getElementById("dr_emp_id").value;
				var txt_search = '';                             
				var para = '';
				var search_id = document.getElementById("dr_search_id").value;
				if(param=='branch_emp' || param=='search_param'){
					emp_id = 0;
					showHint("../service/callman-check.jsp?branch_emp=yes&branch_id="+branch_id+'&search_param='+search_param, "dr_emp_id"); 
				}
			
				if(search_id=='1'){
						document.getElementById("span_branch").style.display = '';
						document.getElementById("span_emp").style.display = '';
						document.getElementById("span_dr_search").style.display = '';
						txt_search = '';
						para = 'overdue';
					} else if(search_id=='2'){
						document.getElementById("span_branch").style.display = ''; 
						document.getElementById("span_emp").style.display = '';
						document.getElementById("span_dr_search").style.display = '';
						txt_search=document.getElementById("txt_search").value;
						para='date';
					} else if(search_id=='3'){
						document.getElementById("span_branch").style.display = 'none';
						document.getElementById("span_emp").style.display = 'none';
						document.getElementById("span_dr_search").style.display = 'none';
				    	document.getElementById("txt_search").focus();
						txt_search=document.getElementById("txt_search").value;
						para='vehicle';
					} else if(search_id=='4'){
						document.getElementById("span_branch").style.display = 'none';
						document.getElementById("span_emp").style.display = 'none';
						document.getElementById("span_dr_search").style.display = 'none';
				    	document.getElementById("txt_search").focus();
						txt_search=document.getElementById("txt_search").value;
						para='contact';
					} else if(search_id=='5'){
						document.getElementById("span_branch").style.display = '';
						document.getElementById("span_emp").style.display = 'none';
						document.getElementById("span_dr_search").style.display = 'none';
				    	document.getElementById("txt_search").focus();
						txt_search='';
						para='service_due';
					}
				if(search_param=='7'){
					   document.getElementById("span_emp").style.display = 'none';
					}
				clearTimeout(i);
				i = setTimeout('callAjax("'+branch_id+'", "'+emp_id+'", "'+txt_search+'", "'+para+'", "'+search_param+'")', 1000);
			}
			
			
			function callAjax(branch_id, emp_id, txt_search, para, search_param){
					showHint("callman-check.jsp?branch_id="+branch_id+"&emp_id="+emp_id+"&txt_search="+txt_search+"&para="+para+"&search_param="+search_param,"div_callman");
			}
			
			function PopulateItem(branch_id) {
					showHint('vehicle-dash-check.jsp?location=yes&branch_id='+branch_id, 'span_location');
					}
					
			function SearchStockStatus(itemName){
					var location_id = document.getElementById("dr_location_id").value;
					showHint('vehicle-dash-check.jsp?stock_status=yes&location_id='+location_id+'&item_name='+itemName, 'div_stock_status');
					}
			
		    function PopulateSearchBox(){
					var branch_id = document.getElementById("dr_branch_id").value;
					var emp_id = document.getElementById("dr_emp_id").value;
					var dr_search = document.getElementById("dr_search_id").value;
					if(dr_search==1){
						document.getElementById("span_search").innerHTML = "";
					} else if(dr_search==2){
						document.getElementById("span_search").innerHTML = "<input name='txt_search' type='text' class='textbox' id ='txt_search' size='14' maxlength='16' onchange=CallSearch(''); value='<%=mybean.strToShortDate(mybean.ToLongDate(mybean.kknow()))%>'/>";
						$(function() {
                              $("#txt_search").datepicker({
                              	showButtonPanel: true,
                              	dateFormat: "dd/mm/yy"
                             });
	            		});
				} else if(dr_search==3 || dr_search==5 || dr_search==6 || dr_search==7){
						document.getElementById("span_search").innerHTML = "<input name ='txt_search' type='text' class='textbox' id='txt_search' size='42' maxlength='255' onKeyUp=CallSearch(''); value='' onfocus='OnFocus(this.id);' onBlur='OnBlur(this.id);'/>";
				} else if(dr_search==4){ 
						document.getElementById("span_search").innerHTML = "<input name ='txt_search' type='text' class='textbox' id='txt_search' size='42' maxlength='255' onKeyUp=ContactCheck('txt_search'); value='' onfocus='OnFocus(this.id);' onBlur='OnBlur(this.id);'/>";
				}
				CallSearch('');
          }
				
				function GetVehicleDetail(veh_id){
					showHint("callman-check.jsp?veh_id="+veh_id+"&veh_details=yes","div_callman");
					}
		</script>
        <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
        <body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <%@include file="../portal/header.jsp" %>
        <TABLE width="98%" height="300" border="0" align="center" cellPadding="0" cellSpacing="0">
          <TR>
            <TD align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href="../service/index.jsp">Service</a> &gt; <a href="callman.jsp">Call Manager</a>:</TD>
          </TR>
          <TR>
            <TD align="center" vAlign="top"><font color="#ff0000" ><b><%=mybean.msg%></b></font> <br/></TD>
          </TR>
          <tr>
            <td align="right" valign="top"><form name="form1"  method="post">
                <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
                <tr>
                    <th align="center">Call Manager
                    <input type="hidden" id="txt_booking_id" name="txt_booking_id" value="<%=mybean.booking_id%>"/></th>
                  </tr>
                <tr>
                    <td align="center"><span id="span_branch" style="display:none">
                      <select name="dr_branch_id" class="selectbox" id="dr_branch_id" onChange="CallSearch('branch_emp');">
                      <%=mybean.PopulateBranch(mybean.branch_id, "", "1,3", "", request)%>
                    </select>
                      &nbsp;&nbsp;&nbsp; </span> <span id="span_emp" style="display:none">
                      <select name="dr_emp_id" class="selectbox" id="dr_emp_id" onchange="CallSearch('');"   >
                        <%=mybean.PopulateExecutive(mybean.branch_id, "")%>
                      </select>
                      &nbsp;&nbsp;&nbsp; </span> <span id="span_search" name="span_search">
                      <input name="txt_search" type="text" class="textbox" id="txt_search" size="42" maxlength="255" onKeyUp="CallSearch('');" value="" onfocus="OnFocus(this.id);" onBlur="OnBlur(this.id);"/>
                      </span>&nbsp;&nbsp;&nbsp;
                    <select name="dr_search_id" class="selectbox" id="dr_search_id" onchange="PopulateSearchBox();">
                        <%=mybean.PopulateSearch()%>
                      </select>
                    &nbsp;&nbsp;&nbsp; <span id="span_dr_search" style="display:none">
                      <select name="dr_search_param" class="selectbox" id="dr_search_param" onchange="CallSearch('search_param');"  >
                      <%=mybean.PopulateSearchParam()%>
                    </select>
                      </span> &nbsp;&nbsp;&nbsp;
                    <input name="goButton" id="goButton" type="button" onClick="CallSearch('');" class="button" value="Go"/></td>
                  </tr>
              </table>
              </form></td>
          </tr>
          <tr>
            <td align="center" valign="top">&nbsp;</td>
          </tr>
          <tr>
            <td align="center" valign="top" bgcolor="#FFFFFF" height="300"><div id="div_callman"><%=mybean.StrHTML%></div></td>
          </tr>
        </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>