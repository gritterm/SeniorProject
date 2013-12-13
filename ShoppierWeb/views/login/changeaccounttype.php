
   
    <script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBtZjnQXhHLRoOW7edBjdy_i25MAb4W-cQ&sensor=true">
    </script>
    <script type="text/javascript">
      var geocoder;
        var map;
        function initialize() {
          geocoder = new google.maps.Geocoder();
          var latlng = new google.maps.LatLng(42.9612, 85.6557);
          var mapOptions = {
            zoom: 15,
            center: latlng
          }
          map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
          codeAddress();
        }

        function codeAddress() {
          var address = "550 Baldwin St, Jenison, MI ‎ ‎"
          geocoder.geocode( { 'address': address}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
              map.setCenter(results[0].geometry.location);
              var marker = new google.maps.Marker({
                  map: map,
                  position: results[0].geometry.location
              });
            } else {
              alert('Geocode was not successful for the following reason: ' + status);
            }
          });
        }
        google.maps.event.addDomListener(window, 'load', initialize);
    </script>
<div id="map-canvas" style="width: 500px; height: 400px;"></div>
   
    
    
