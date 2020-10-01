<jsp:useBean id="mybeanheader" class="axela.portal.Header" scope="request"/>
<%mybeanheader.doPost(request, response);%>
<%if(!mybeanheader.emp_copy_access.equals("1")) {%>
<script>
 $(document).ready(function()  {
	$("body").css("-webkit-user-select","none");
	$("body").css("-moz-user-select","none");
	$("body").css("-ms-user-select","none");
	$("body").css("-o-user-select","none");
	$("body").css("user-select","none")	;
	$("body").on('contextmenu',false);
	});
</script>
<%}%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="33%" height="70" align="center"><img src="../admin-ifx/ddmotors-logo.jpg" width="118" height="80" /></td>
    <td width="33%" align="center">
    <img src="../admin-ifx/logo.jpg" width="140" height="43" alt="Axela" />
    &nbsp;</td>
    <td width="33%" align="center"><%if (!mybeanheader.GetSession("emp_id", request).equals("")) {
                        out.print("&nbsp;Welcome,<br>" + mybeanheader.emp_name + "");
                    }
            %></td>
  </tr>
</table>
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" align="left">&nbsp;</td>
    <td align="right">&nbsp;<a href="../portal/home.jsp" target="homemain">Home</a> | <a href="../portal/news-list.jsp?all=yes" target="homemain">News</a> | <a href="../portal/system-password.jsp" target="homemain">Manage Password</a> |
      <%//if (mybeanheader.emp_rs_access.equals("1")) {%>
      <%//if (mybeanheader.emp_rs_access.equals("1") && mybeanheader.AppRun().equals("0") ) {%>
      <%if (mybeanheader.AppRun().equals("0")) {%>
      <a href="../portal/remotesupport.jsp" target="homemain">Remote Support</a> |
      <%}%>
      <a href="../portal/signout.jsp" target="_parent">Signout</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
  </tr>
</table>
<div class="menu container">
  <ul id="menu">
    <li class="column"><a href="../portal/home.jsp" class="drop">Home</a><!-- Begin Home Item -->
      <div class="dropdown_columns"><!-- Begin 2 columns container -->
        <div class="col_3">
          <h2>Welcome to Axela Auto!</h2>
        </div>
        <div class="col_3">
          <p>Hi and welcome here! This is a showcase of the endless possibilities in <b>Axela Auto</b>.</p>
        </div>
      </div>
      <!-- End Home Container --> 
    </li>
    <!-- End Home Item --> 
    
    <!-- Start Activities Item  -->
    <li class="column"><a href="../portal/activity.jsp" class="drop">Activities</a>
      <div class="dropdown_columns"><!-- Start activities Container -->
        <div class="col_1">
          <ul>
            <li><a href="../portal/activity-update.jsp?add=yes">Add Activity</a></li>
            <li><a href="../portal/activity-list.jsp?all=yes">List Activities</a></li>
            <li><a href="../portal/activity-search.jsp">Search Activities</a></li>
          </ul>
        </div>
      </div>
      <!--End activities Container --> 
    </li>
    <!-- End Activity Item --> 
    
    <!-- Begin Customers Item -->
    <li class="column"><a href="../customer/index.jsp" class="drop">Customers</a>
      <div class="dropdown_columns"><!-- Begin customers Container -->
        <div class="col_2">
          <div class="col_1"> <a href="../customer/customer.jsp">
            <h3>Customers</h3>
            </a>
            <ul>
              <li><a href="../customer/customer-update.jsp?Add=yes">Add Customer</a></li>
              <li><a href="../customer/customer-list.jsp?all=yes">List Customers</a></li>
              <li><a href="../customer/customer-search.jsp">Search Customers</a></li>
            </ul>
          </div>
          <div class="col_1"> <a href="../customer/customer-contact.jsp">
            <h3>Contacts</h3>
            </a>
            <ul>
              <li><a href="../customer/customer-contact-update.jsp?Add=yes">Add Contacts</a></li>
              <li><a href="../customer/customer-contact-list.jsp?all=yes">List Contacts</a></li>
              <li><a href="../customer/customer-contact-search.jsp">Search Contacts</a></li>
            </ul>
          </div>
        </div>
      </div>
      <!-- End Customer Items --> 
    </li>
    <!-- Customer Container Ends -->
    
    <%if(mybeanheader.autosales==1){%>
    <!-- Begin Sales Item -->
    <li class="column"><a href="../sales/index.jsp" class="drop">Sales</a>
      <div class="dropdown_columns"><!-- Begin Sales 4 columns container -->
        <div class="col_4">
          <div class="col_1"> <a href="../sales/enquiry.jsp">
            <h3>Enquiry</h3>
            </a>
            <ul>
              <li><a href="../sales/enquiry-quickadd.jsp">Add Enquiry</a></li>
              <li><a href="../sales/enquiry-list.jsp?all=yes">List Enquiry</a></li>
              <li><a href="../sales/enquiry-search.jsp">Search Enquiry</a></li>
              <li><a href="../sales/enquiry-import.jsp">Import Enquiry</a></li> 
            </ul>
            <br/>
            <br/>
          </div>
          <div class="col_1"> <a href="../sales/testdrive.jsp">
            <h3>Test Drives</h3>
            </a>
            <ul>
              <li><a href="../sales/testdrive-list.jsp?all=yes">List Test Drives</a></li>
              <li><a href="../sales/testdrive-search.jsp">Search Test Drives</a></li>
              <li><a href="../sales/testdrive-cal.jsp">Test Drive Calendar</a></li>
              <li><a href="../sales/testdriveoutage-list.jsp?all=yes">Test Drive Outage</a></li>
              <li><a href="../sales/managetestdrivevehicle.jsp?all=yes">Test Drive Vehicle</a></li>
            </ul>
          </div>
          
           <%//if(mybeanheader.emp_id.equals("1")){%>
          <div class="col_1"> <a href="../sales/veh-quote.jsp">
            <h3>Quotes</h3>
            </a>
            <ul>
              <li><a href="../sales/veh-quote-list.jsp?all=yes">List Quotes</a></li>
              <li><a href="../sales/veh-quote-search.jsp">Search Quotes</a></li>
              <li><a href="../sales/report-stock-exe.jsp">Executive Stock Status</a></li>
            </ul>
            <br/>
            <br/>
          </div>
          
          <div class="col_1"> <a href="../sales/veh-salesorder.jsp">
            <h3>Sales Orders</h3>
            </a>
            <ul>
              <li><a href="../sales/veh-salesorder-list.jsp?all=yes">List Sales Orders</a></li>
              <li><a href="../sales/veh-salesorder-search.jsp">Search Sales Orders</a></li>
            </ul>
            <br/>
            <br/>
             <br/>
             <br/>
             <br/>
             <br/>
          </div>
          <%//}%>
          <div class="col_1"> <a href="../sales/campaign.jsp">
            <h3>Campaign</h3>
            </a>
            <ul>
              <li><a href="../sales/campaign-update.jsp?add=yes">Add Campaign</a> </li>
              <li><a href="../sales/campaign-list.jsp?all=yes">List Campaign</a></li>
              <li><a href="../sales/campaign-search.jsp">Search Campaign</a></li>
            </ul>
          </div>
           <%//if(mybeanheader.emp_id.equals("1")){%>
          <div class="col_1"> <a href="../sales/target.jsp">
            <h3>Target</h3>
            </a>
            <ul>
              <li><a href="../sales/target-list.jsp">List Targets</a></li>
              <li><a href="../sales/target-search.jsp">Search Targets</a></li>
            </ul>
          </div>
          <%//}%>
          <div class="col_1"> <a href="../sales/team.jsp">
            <h3>Teams</h3>
            </a>
            <ul>
              <li><a href="../sales/team-update.jsp?add=yes">Add Team</a> </li>
              <li><a href="../sales/team-list.jsp?all=yes">List Teams</a></li>
            </ul>
          </div>
        </div>
      </div>
      <!-- End sales 4 columns container --> 
      
    </li>
    <!-- End sales item -->
    <%}%>
    
    
     <%if(mybeanheader.autoservice==1){%>
    <!-- Begin Service Item -->
    <li class="column"><a href="../service/index.jsp" class="drop">Service</a>
      <div class="dropdown_columns"><!-- Begin Service container -->
        <div class="col_4">
          <div class="col_1"> <a href="../service/ticket.jsp">
            <h3>Tickets</h3>
            </a>
            <ul>
              <li><a href="../service/ticket-add.jsp?add=yes">Add Ticket</a></li>
              <li><a href="../service/ticket-list.jsp?all=yes">List Tickets</a></li>
              <li><a href="../service/ticket-search.jsp">Search Tickets</a></li>
              <li><a href="../service/contact.jsp">Search Contacts</a></li>
              <li><a href="../service/holiday-list.jsp?all=yes">List Holidays</a></li>
              <li><a href="../service/ticket-faq-list.jsp?all=yes">List FAQ</a></li>
              <li><a href="../service/ticket-faq-cat-list.jsp?all=yes">List Categories</a></li>
            </ul>
          </div>
          
          <div class="col_1"> <a href="../service/jobcard.jsp">
            <h3>Job Cards</h3>
            </a>
            <ul>
              <li><a href="../service/jobcard-veh-search.jsp">Add Job Card</a></li>
              <li><a href="../service/jobcard-list.jsp?all=yes">List Job Cards</a></li>
              <li><a href="../service/jobcard-search.jsp">Search Job Cards</a></li>
              <li><a href="../service/manhours.jsp">Man Hours</a></li>              
            </ul>
          </div>
          <div class="col_1"> <a href="../service/vehicle.jsp">
            <h3>Vehicles</h3>
            </a>
            <ul>
              <li><a href="../service/vehicle-update.jsp?add=yes">Add Vehicle</a></li>
              <li><a href="../service/vehicle-list.jsp?all=yes">List Vehicles</a></li>
              <li><a href="../service/vehicle-search.jsp">Search Vehicles</a></li>
              <li><a href="../service/movement-add.jsp">Vehicle IN</a></li>             
              <li><a href="../service/movement-list.jsp?all=yes">List Vehicle Movement</a></li>
            </ul>
          </div>
          <div class="col_1"> <a href="../service/call.jsp">
            <h3>Calls</h3>
            </a>
            <ul>
              <li><a href="../service/call-update.jsp?add=yes">Add Call</a></li>
              <li><a href="../service/call-list.jsp?all=yes">List Calls</a></li>
              <li><a href="../service/call-search.jsp">Search Calls</a></li>
              <li><a href="../service/callman.jsp">Call Manager</a></li>             
            </ul>
            
          </div> <div class="col_1"> <a href="../service/booking.jsp">
            <h3>Bookings</h3>
            </a>
            <ul>
              <!--li><a href="../service/booking-update.jsp?add=yes">Add Booking</a></li-->
              <li><a href="../service/booking-list.jsp?all=yes">List Bookings</a></li> 
              <li><a href="../service/advisor-cal.jsp?all=yes">Advisor Calender</a></li>
              <li><a href="../service/leave-list.jsp?all=yes">Executive Leave</a></li>
              <li><a href="../service/parking-list.jsp?all=yes">List Parkings</a></li>  
            </ul>
          </div>
          <div class="col_1"> <a href="../service/pickup.jsp">
            <h3>Pickup</h3>
            </a>
            <ul>
              <li><a href="../service/pickup-update.jsp?add=yes">Add Pickup</a></li>
              <li><a href="../service/pickup-list.jsp?all=yes">List Pickup</a></li>
              <li><a href="../service/pickup-search.jsp">Search Pickup</a></li>
              <li><a href="../service/pickup-cal.jsp">Pickup Calendar</a></li>
            </ul>
          </div>
          <div class="col_1"> <a href="../service/courtesy.jsp">
            <h3>Courtesy Cars</h3>
            </a>
            <ul>
              <li><a href="../service/courtesy-update.jsp?add=yes">Add Courtesy</a></li>
              <li><a href="../service/courtesy-list.jsp?all=yes">List Courtesy</a></li>
              <li><a href="../service/courtesy-search.jsp">Search Courtesy</a></li>
              <li><a href="../service/courtesy-cal.jsp">Courtesy Calendar</a></li>
              <li><a href="../service/managecourtesyvehicle.jsp?all=yes">Courtesy Vehicle</a></li>
              <li><a href="../service/managecourtesyvehicleoutage.jsp?all=yes">Courtesy Vehicle Outage</a></li>
            </ul>
          </div>
          
          <%if(mybeanheader.AppRun().equals("0")){%>
          <div class="col_1"> <a href="../service/courtesy.jsp">
            <h3>Campaign</h3>
            </a>
            <ul>
              <li><a href="../service/courtesy-update.jsp?add=yes">Add Campaign</a></li>
              <li><a href="../service/courtesy-list.jsp?all=yes">List Campaign</a></li>
              <li><a href="../service/courtesy-search.jsp">Search Campaign</a></li>
            </ul>
          </div>
          <div class="col_1"> <a href="../service/contract.jsp">
            <h3>Contracts</h3>
            </a>
            <ul>
              <li><a href="../service/contract-update.jsp?add=yes">Add Contract</a></li>
              <li><a href="../service/contract-list.jsp?all=yes">List Contracts</a></li>
              <li><a href="../service/contract-search.jsp">Search Contracts</a></li>
            </ul>
          </div>
          <%}%>
        </div>
      </div>
      <!-- End service container --> 
    </li>
    <!-- End service item-->
    <%}%>
    
    
    <!-- Begin Inventory Item -->
    <%//if(mybeanheader.autoservice==1){%>
     <%//if(mybeanheader.emp_id.equals("1")){%>
    <li class="column"><a href="../inventory/index.jsp" class="drop">Inventory</a>
      <div class="dropdown_columns"><!--  Begin Inventory container -->
        <div class="col_4">
          <div class="col_1"> <a href="../inventory/index.jsp">
            <h3>Inventory</h3>
            </a>
            <ul>
              <li><a href="../inventory/inventory-item-update.jsp?add=yes" >Add Item</a></li>
              <li><a href="../inventory/inventory-item-list.jsp?all=yes" >List Items</a></li>
              <li><a href="../inventory/inventory-cat-list.jsp?all=yes" >List Item Categories</a></li>
              <li><a href="../inventory/inventory-salescat-list.jsp?all=yes" >List Sales Categories</a></li>
              <%if(mybeanheader.autoservice==1){%>
              <li><a href="../inventory/inventory-adj-list.jsp" >Inventory Adjustments</a></li>
              <li><a href="../inventory/inventory-po-data-list.jsp?all=yes" >List PO Data </a></li>
              <li><a href="../inventory/inventory-location-list.jsp?all=yes" >List Locations</a></li>
              <li><a href="../inventory/inventory-reorderlevel.jsp?status=add" >Reorder Level</a></li>
              <%}%>
            </ul>
          </div>
          <%if(mybeanheader.autoservice==1){%>
          <div class="col_1"> <a href="../accounting/voucher.jsp?add=yes&param3=12">
            <h3>Purchase Orders</h3>
            </a>
            <ul>
              <li><a href="../inventory/inventory-po-update.jsp?add=yes">Add PO</a></li>
              <li><a href="../inventory/inventory-po-list.jsp?all=yes">List PO</a></li>
              <li><a href="../inventory/inventory-po-search.jsp">Search PO</a></li>
              <li><a href="../inventory/inventory-po-list.jsp?auth=yes">Authorise PO</a></li>
              <li><a href="../customer/customer-list.jsp?all=yes&amp;tag=vendors">List Suppliers</a></li>
            </ul>
          </div>
          <%}%>
          <%if(mybeanheader.autosales==1){%>
          <div class="col_1"> <a href="../inventory/stock.jsp">
            <h3>Stock</h3>
            </a>
            <ul>
              <li><a href="../inventory/stock-update.jsp?add=yes" >Add Stock</a></li>
              <li><a href="../inventory/stock-list.jsp?all=yes">List Stock </a></li>
              <li><a href="../inventory/stock-search.jsp">Search Stock</a></li>
              <li><a href="../inventory/stock-import.jsp">Stock Import</a></li>
              <li><a href="../inventory/stock-validate.jsp">Stock Validate</a></li>
            </ul>
          </div>
          <%}%>
          <%if(mybeanheader.autoservice==1){%>
          <div class="col_1"> <a href="../inventory/inventory-grn.jsp">
            <h3>GRN</h3>
            </a>
            <ul>
<!--               <li><a href="../inventory/inventory-grn-list.jsp?all=yes" >List GRN</a></li> -->
              <li><a href="../inventory/inventory-grn-search.jsp" >Search GRN</a></li>
            </ul>
          </div>
          <div class="col_1"> <a href="../inventory/inventory-preturn.jsp">
            <h3>Purchase Return</h3>
            </a>
            <ul>
<!--               <li><a href="../inventory/inventory-preturn-list.jsp?all=yes" >List Returns</a></li> -->
              <li><a href="../inventory/inventory-preturn-search.jsp" >Search Returns</a></li>
            </ul>
          </div>
          <div class="col_1"> <a href="../inventory/inventory-deliverynote.jsp">
            <h3>Delivery Note</h3>
            </a>
            <ul>
              <li><a href="../inventory/inventory-deliverynote-list.jsp?all=yes">List Delivery Note</a></li>
              <li><a href="../inventory/inventory-deliverynote-search.jsp">Search Delivery Note</a></li>
            </ul>
          </div>
          <div class="col_1"> <a href="../inventory/inventory-salesreturn.jsp">
            <h3>Sales Return</h3>
            </a>
            <ul>
              <li><a href="../inventory/inventory-salesreturn-list.jsp?all=yes">List Returns</a></li>
              <li><a href="../inventory/inventory-salesreturn-search.jsp">Search Returns</a></li>
            </ul>
          </div>
          <%//}%>
        </div>
      </div>
      <!-- End Inventory container --> 
    </li>
    <%//}%>
    <!-- End Inventory item--> 
    
    <!-- Begin Invoice Item -->
    <li class="column"><a href="../invoice/index.jsp" class="drop">Invoice</a>
      <div class="dropdown_columns"><!-- Begin customers Container -->
        <div class="col_2">
          <div class="col_1"> <a href="../invoice/quote.jsp">
            <h3>Quotes</h3>
            </a>
            <ul>
              <li><a href="../invoice/quote-update.jsp?add=yes">Add Quote</a></li>
              <li><a href="../invoice/quote-list.jsp?all=yes">List Quotes</a></li>
              <li><a href="../invoice/quote-search.jsp">Search Quotes</a></li>
            </ul>
          </div>
          <div class="col_1"> <a href="../invoice/invoice.jsp">
            <h3>Invoices</h3>
            </a>
            <ul>
              <li><a href="../invoice/invoice-update.jsp?add=yes">Add Invoice</a></li>
              <li><a href="../invoice/invoice-list.jsp?all=yes">List Invoices</a></li>
              <li><a href="../invoice/invoice-search.jsp">Search Invoices</a></li>
            </ul>
          </div>
          <div class="col_1"> <a href="../invoice/receipt.jsp">
            <h3>Receipts</h3>
            </a>
            <ul>
              <li><a href="../invoice/receipt-update.jsp?add=yes">Add Receipt</a></li>
              <li><a href="../invoice/receipt-list.jsp?all=yes">List Receipts</a></li>
              <li><a href="../invoice/receipt-search.jsp">Search Receipts</a></li>
            </ul>
            <br>
            <br>
          </div>
          <div class="col_1"> <a href="../invoice/payment.jsp">
            <h3>Payments</h3>
            </a>
            <ul>
              <li><a href="../invoice/payment-update.jsp?add=yes">Add Payment</a></li>
              <li><a href="../invoice/payment-list.jsp?all=yes">List Payments</a></li>
              <li><a href="../invoice/payment-search.jsp">Search Payments</a></li>
            </ul>
            <br>
            <br>
          </div>
        </div>
      </div>
      <!-- End Customer Items --> 
    </li>
    <%}%>
    <!-- End Invoice Items --> 
    
    <!-- Start MIS item  -->
    
    <%if(mybeanheader.emp_mis_access.equals("1")){%>
    <li class="column"><a href="../portal/mis.jsp" class="drop">MIS</a> 
      
      <!--End MIS container --> 
    </li>
    <%}%>
    
    <!-- End MIS Item--> 
    
    <!-- Begin Settings  Item -->
    <li class="column menu_right"><a href="#" class="drop">Settings</a>
      <div class="dropdown_columns align_right"><!-- Begin Settings  container -->
        <div class="col_1">
          <ul>
            <li><a href="../portal/system-password.jsp">Manage Password</a></li>
            <li><a href="../portal/system-theme.jsp">Manage Theme</a></li>
            <li><a href="../portal/system-user.jsp">User Configuration </a></li>
            <li><a href="../portal/helpdesk.jsp" target="_blank">Emax HelpDesk</a></li>
          </ul>
        </div>
      </div>
      <!-- End settings container --> 
    </li>
    
    <!-- End settings Item -->
    
    <%if(mybeanheader.emp_id.equals("1")){%>
    <li class="column menu_right"><a href="../portal/manager.jsp" class="drop">Manager</a><!-- Begin 5Manager Item -->
      <div class="dropdown_columns align_right"><!-- Begin  Manager container -->
        <div class="col_1">
          <div class="col_1"> <a href="../portal/executives.jsp">
            <h3>Executives</h3>
            </a>
            <ul>
              <li><a href="../portal/executive-list.jsp?all=yes">List Executives</a></li>
              <li><a href="../portal/executives-update.jsp?add=yes">Add Executives</a></li>
            </ul>
          </div>
          
          <!--<ul>
            <li><a href="../portal/executive-list.jsp?all=yes">Manage Executives</a></li>
            <li> <a href="../portal/manage-configure.jsp">Configure Axela</a></li>
          </ul> --> 
        </div>
      </div>
      <!-- End Manager container --> 
      
    </li>
    <% } %>
    <!-- End Manager item --> 
    
    <!--More Section starts here-->
    <li class="column menu_right"><a href="#" class="drop">More</a>
      <div class="dropdown_columns align_right"> 
        <!-- Begin  More container -->
        <div class="col_2">
          <div class="col_1"> <a href="../portal/news.jsp">
            <h3>News</h3>
            </a>
            <ul>
              <li><a href="../portal/news-branch-list.jsp?all=yes">List Branch News</a></li>
              <li><a href="../portal/news-ho-list.jsp?all=yes">List Head Office News</a></li>
            </ul>
          </div>
          <div class="col_1"> <a href="../portal/faq.jsp">
            <h3>FAQ</h3>
            </a>
            <ul>
              <li><a href="../portal/faqexecutive-list.jsp?all=yes">List FAQ</a></li>
              <li><a href="../portal/faqexecutivecat-list.jsp?all=yes">List Categories</a></li>
            </ul>
          </div>
          <div class="col_1"> <a href="../portal/email.jsp">
            <h3>Email</h3>
            </a>
            <ul>
              <li><a href="../portal/email-send.jsp?target=1">All Contacts</a></li>
              <li><a href="../portal/email-send.jsp?target=2">All Customers</a></li>
              <li><a href="../portal/email-send.jsp?target=3">All Suppliers</a></li>
              <li><a href="../portal/email-exe-send.jsp?chk_email_allexe=on">All Executives</a></li>
              <li><a href="../portal/email-list.jsp?all=yes">List Email</a></li>
            </ul>
          </div>
          <div class="col_1"> <a href="../portal/sms.jsp">
            <h3>SMS</h3>
            </a>
            <ul>
              <li><a href="../portal/sms-send.jsp?target=1">All Contacts</a></li>
              <li><a href="../portal/sms-send.jsp?target=2">All Customers</a></li>
              <li><a href="../portal/sms-send.jsp?target=3">All Suppliers</a></li>
              <li><a href="../portal/sms-exe-send.jsp?chk_sms_allexe=on">All Executives</a></li>
              <li><a href="../portal/sms-list.jsp?all=yes">List SMS</a></li>
            </ul>
          </div>
        </div>
      </div>
      <!-- End More container --> 
    </li>
    <!--More Section Ends here-->
  </ul>
</div>
<!-- #menu ends --> 
