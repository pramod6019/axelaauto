<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_Pickup_GPS" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
<link href="../Library/theme<%=mybean.GetTheme(request)%>/font-awesome.css" rel="stylesheet" media="screen" type="text/css" />
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

<script language="JavaScript" type="text/javascript">	
function ExeCheck() { 
	var branch_id=document.getElementById("dr_branch").value;
	showHint('../service/report-check.jsp?executive=yes&multiple=yes&pickup=yes&branch_id=' + GetReplace(branch_id),'exeHint');
}
</script>

<%if(mybean.map.equals("yes")){%>
<script src="http://maps.googleapis.com/maps/api/js?key=<%=mybean.google_api_key%>&sensor=false">
</script>

<script type="text/javascript">

function initialize() {
    var map;
    var bounds = new google.maps.LatLngBounds();
    var mapOptions = {
        mapTypeId: 'roadmap'
    };
                    
    // Display a map on the page
    map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
    map.setTilt(45);
        
    // Multiple Markers
    var markers = [<%=mybean.marker%>];
                        
    // Info Window Content
    var infoWindowContent = [<%=mybean.marker_content%>];
        
    // Display multiple markers on a map
    var infoWindow = new google.maps.InfoWindow(), marker, i;


    // Loop through our array of markers & place each one on the map  
    for( i = 0; i < <%=mybean.count%>; i++ ) {

        var position = new google.maps.LatLng(markers[i][0], markers[i][1]);
        bounds.extend(position);
        marker = new google.maps.Marker({
            position: position,
            map: map,
        });

	google.maps.event.addListener(marker, 'mouseover', (function(marker, i) {
	
            return function() {
                infoWindow.setContent(infoWindowContent[i][0]);
                infoWindow.open(map, marker);
	}
            
        })(marker, i));

	google.maps.event.addListener(marker, 'mouseout', (function(marker, i) {
	
            return function() {
                infoWindow.close();
	}
            
        })(marker, i));


 	map.fitBounds(bounds);
        // Automatically center the map fitting all markers on the screen
       
    }

    for( i = 0; i < <%=mybean.count%>; i++ ) {
	google.maps.event.addListener(marker, 'mouseout', (function(marker, i) {
	
            return function() {
                infoWindow.close();
	}
            
        })(marker, i));

    } 

    // Override our map zoom level once our fitBounds function runs (Make sure it only runs once)
    var boundsListener = google.maps.event.addListener((map), 'bounds_changed', function(event) {
        this.setZoom(12);
        google.maps.event.removeListener(boundsListener);
    });
    
}

google.maps.event.addDomListener(window, 'load', initialize);
</script>
<%} %>

<script>
 $(function() {
    $( "#txt_gps_time" ).datetimepicker({
        showButtonPanel: true,
		dateFormat: "dd/mm/yy"
    });
  });
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
  <TR>
    <TD align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/mis.jsp">MIS</a> &gt; <a href="../service/report-pickup-gps.jsp">GPS Dashboard</a>:</TD>
  </TR>
  <tr>
    <td align="center"><font color="red"><b><%=mybean.msg%></b></font><br/></td>
  </tr>
  <tr>
    <td align="center"><form method="post" name="frm1"  id="frm1">
        <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
          <tr>
            <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable" >
                <tr>
                  <th colspan="5" align="center">GPS Dashboard</th>
                </tr>
                <tr>
                  <td align="right" valign="top">Branch:</td>
                    
                  <td align="left" valign="top">
                      <%if(mybean.branch_id.equals("0")){%>
                      <select name="dr_branch" id="dr_branch" class="selectbox" onChange="ExeCheck();">
                        <%=mybean.PopulateBranch(mybean.dr_branch_id, "", "1,3", "", request)%>
                      </select>
                      <%}else{%>
                      <input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>" />
                      <%=mybean.getBranchName(mybean.dr_branch_id, mybean.comp_id)%>
                      <%}%>
                    
                           </td>       
                  <td align="right" valign="top" rowspan="2">Service Advisor<font color=red>*</font>:</td>
                  <td valign="top" rowspan="2"><span id="exeHint"><%=mybean.PopulateServiceExecutives()%></span></td>
                  <td valign="top" rowspan="2"><input type="submit" name="submit_button" id="submit_button" class="button" value="Go"/>
                    <input type="hidden" name="submit_button" value="Submit"/></td>
                </tr>
                 <tr>
                 <td align="right" valign="top">Date:</td>  
                <td align="left" valign="top" >
                      <input name ="txt_gps_time" id="txt_gps_time" type="text" class="textbox" value="<%=mybean.gps_time %>" size="19" maxlength="18" />
                    </td>   
              </table></td>
          </tr>
        </table>
      </form></td>
  </tr>
  <tr align="center">
    <td  align="center">&nbsp;</td>
  </tr>
  <tr align="center">
    <td height="200" align="center" valign="top"><%if(mybean.map.equals("yes") ) {%>
      <div align="center"><font color="red"><b><%=mybean.no_data_msg%></b></font><br/>
      </div>
      <%if(mybean.count!=0){%>
      <div id="map_canvas" class="mapping" style="width:1000px;height:500px;" ></div>
      <%} %>
      <%} %>
      <% if (mybean.map.equals("no")){%>
      <font color="#ff0000" ><b>Service Advisor Not Found</b></font>
      <%} %></td>
  </tr>
  <tr align="center">
    <td  align="center">&nbsp;</td>
  </tr>
</TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
