<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_Exe_GPS" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</head>
<body  class="page-container-bg-solid page-header-menu-fixed">
<%@include file="../portal/header.jsp" %>
<!-- 	BODY -->
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
						<h1>GPS Dashboard</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="../sales/report-exe-gps.jsp">GPS Dashboard</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<center>
								<font color="red"><b> <%=mybean.msg%> </b></font>
							</center>
							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">GPS Dashboard</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">

										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">
											<div class="form-element3">
	                							<label>Brands:</label>
				   								<div id="multiprincipal">
													<select name="dr_principal" class="form-control multiselect-dropdown" 
														size="10" multiple="multiple" class="textbox" id="dr_principal" 
														onChange="PopulateBranches()" style="width:250px">
														<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>	
												
											<div class="form-element3">
	                							<label>Branches:</label>
												<div id="branchHint">
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>										
											
											<div class="form-element3">
	                							<label>Teams:</label>
												<div id="teamHint">
													<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
												</div>
											</div>
													
	                    					<div class="form-element3">
	                							<label>Sales Consultant:</label>
												<div id="exeHint">
													<%=mybean.mischeck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%>
												</div>
											</div>
											
	                    					<div class="form-element2">
	                    						<label>Date:</label>
							                    <input name ="txt_gps_time" id="txt_gps_time" type="text" class="form-control datepicker" 
							                    	value="<%=mybean.gps_time %>"  />
							                </div>  
							                
							                <div class="form-element12">
												<center>
													<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go"/>
	                    							<input type="hidden" name="submit_button" value="Submit"/></td>
	                    						</center>
	                    					</div>	  	
      									</form>
									  </div>
								</div>
							</div>
								<!-- 	PORTLET -->
							<center>
									    <%if(mybean.map.equals("yes") ) {%>
									      <div align="center">
									      	<font color="red"><b><%=mybean.no_data_msg%></b></font><br/>
									      </div>
									      <%if(mybean.count!=0){%>
									      <div id="map_canvas" class="mapping" style="width:1000px;height:500px;" ></div>
									      <%} %>
									    <%} %>
									    <% if (mybean.map.equals("no")){%>
									      <font color="#ff0000" ><b>Sales Consultant Not Found</b></font>
									    <%} %>
									    </center>
							
						
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>  
									    
 <%@include file="../Library/admin-footer.jsp" %>
 <%@include file="../Library/js.jsp"%>
 <script language="JavaScript" type="text/javascript">

function PopulateBranches() { //v1.0
	var brand_id=outputSelected(document.getElementById("dr_principal").options);
	showHint('../sales/mis-check1.jsp?multiple=yes&brand_id='+brand_id+'&branch=yes','branchHint');
    }

	
	function PopulateTeams(){		
		var branch_id=outputSelected(document.getElementById("dr_branch").options);	
	    showHint('../sales/mis-check1.jsp?branch_id='+branch_id+'&team=yes', 'teamHint');
	}
		
	function PopulateExecutives() { //v1.0
		var branch_id = outputSelected(document.getElementById("dr_branch").options);
		var team_id = outputSelected(document.getElementById("dr_team").options);
		showHint('../sales/mis-check1.jsp?exe_branch_id=' + branch_id + '&team_id=' + team_id + '&executives=yes', 'exeHint');
	}
    function PopulateCRMDays(){}
</script>

<%if(mybean.map.equals("yes")){%>
<script src="http://maps.googleapis.com/maps/api/js?key=AIzaSyBSSu0kdpWctJRV7iXATZFHfA0eMSSHroQ">
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
        this.setZoom(14);
        google.maps.event.removeListener(boundsListener);
    });
    
}

// google.maps.event.addDomListener(window, 'load', initialize);
</script>
<%} %>

</body>
</html>
