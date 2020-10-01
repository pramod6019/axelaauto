<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_Pickup_GPS_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%//=mybean.StrHTML%>



<%//if(mybean.map.equals("yes")){%>

<script
src="http://maps.googleapis.com/maps/api/js?key=<%=mybean.google_api_key%>&sensor=false">
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
		this.setCenter(new google.maps.LatLng(<%=mybean.selected%>));
        google.maps.event.removeListener(boundsListener);
    });
    
}

google.maps.event.addDomListener(window, 'load', initialize);
</script>

<%//} %>



