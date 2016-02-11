
/************************
  ++  Global Variables ++
*************************
*/
// RabbitMQ_Web_Stomp Connection parameters
var rabbitMQ_hostname = window.location.hostname;
var mq_username = "guest",
    mq_password = "guest",
    mq_vhost    = "/",
    mq_url      = 'http://' + rabbitMQ_hostname + ':15674/stomp',

    // The queue we will read. The /topic/ queues are temporary
    // queues that will be created when the client connects, and
    // removed when the client disconnects. They will receive
    // all messages published in the "amq.topic" exchange, with the
    // given routing key, in this case "mymessages"
    mq_xfr_queue    = "xformer-status-webui";
    mq_loc_change_queue    = "location-change-webui";

var mqClient;
var vehicleMarkers =  new Array();

function onXfrStatusMessage(m) {
  /*
  ** Excpected message schema:
  {
    "name":"DTR/SM/01",
    "status":"BAD",
    "statusData":{
        "oil-level-pct":"100",
        "oxidizer-health-pct":"15"
    }
  }
  */
  //toastr.info(m.body);
  var data = JSON.parse(m.body);
  var xfrName = data.name;
  var xfrStatus = data.status;
  var xfrData = data.statusData;
  if(xfrStatus == "BAD"){
    var msg = 'A transformer needs maintenance ('.concat(xfrName).concat(')');
    toastr.error(msg);
  }
  else{
    var msg = 'A transformer status was updated ('.concat(xfrName).concat(')');
    toastr.info(msg);
  }
  // gmap.data.forEach(function(feature){
  //   var featureType = feature.getProperty('type');
  // });
  var xfrFeature = gmap.data.getFeatureById(xfrName); // the "name" property was set as the "identifying" property while loading GeoJson data
  xfrFeature.setProperty("status",xfrStatus);
  xfrFeature.setProperty("statusData",xfrData);
}

function onLocationChangeMessage(m) {
  /**
      Expected message schema:

      {
		    "vehicleName":"TRUCK/91-01",
		    "longitude":"88.67",
		    "latitude":"27.35"
			}

  */
  var data = JSON.parse(m.body);

  var vehicleIcon = 'dist/icons/delivery-truck-32.png';

  var vehLat = parseFloat(data.latitude);
  var vehLng = parseFloat(data.longitude);
  var vehName = data.vehicleName;
  var vehicleMarker = vehicleMarkers[vehName];
  if (vehicleMarker == null){
    vehicleMarker = new google.maps.Marker({
        position: {lat: vehLat, lng: vehLng},
        map: gmap,
        icon: vehicleIcon,
        label: vehName
      });
    vehicleMarkers[vehName] = vehicleMarker;
  }
  else{
    vehicleMarker.setPosition({
      lat: vehLat,
      lng: vehLng
    });
  }
  var msg = 'A vehicle moved to: '.concat(vehLat).concat(',').concat(vehLng).concat(' (').concat(vehName).concat(')');
  toastr.info(msg);

}

// This will be called upon successful connection
function onConnect() {
  toastr.info('Connected to messaging server');
  mqClient.subscribe(mq_xfr_queue, onXfrStatusMessage);
  mqClient.subscribe(mq_loc_change_queue, onLocationChangeMessage);
}

// This will be called upon a connection error
function onConnectError() {
  toastr.error('Error connecting to messaging server');
}

function initMap() {
  //var timer = null;
  //var myPos = {lat: 22.678780, lng: 88.456634}; //28.2119463,71.7398609. 21.1610714,79.0022979
  //window.indiaCoordinates = {lng: 79.0022979, lat: 21.1610714};
  set_MapViewPane_ComputedHeight();
  window.gmap = new google.maps.Map(document.getElementById('mapPortlet'));
}
function doOnLoad(){
  changeJuris("WB");
}

function set_MapViewPane_ComputedHeight(){
  var h = $('#mapCol').height(),
  offsetTop = 120;
  $('#mapPortlet').css('height', (h - offsetTop));
}
$(document).ready(function(){
  loadCities();
  $(window).on('resize',function(){
    set_MapViewPane_ComputedHeight();
  });
  $("#cities").on('click', 'li a', function(){
    //$(".btn:first-child").text($(this).text());
    //$(".btn:first-child").val($(this).text());
    //var msg = "You selected : ".concat($(this).text());
    //alert(msg)
    var selectedCity = $(this).text();//.trim().toUppercase();
    changeCity(selectedCity);
  });
  $("#suburbs").on('click', 'li a', function(){
   //$(".btn:first-child").text($(this).text());
   //$(".btn:first-child").val($(this).text());
   //var msg = "You selected : ".concat($(this).text());
   //alert(msg);
   changeSuburb($(this).text());
  });
  $('#btnObsXfr').on('click', function(){
    addXfrObserveCircleWithAnim();
  });
  // Use SockJS
  Stomp.WebSocketClass = SockJS;
  mqClient = Stomp.client(mq_url);
  // Connect
  mqClient.connect(
    mq_username,
    mq_password,
    onConnect,
    onConnectError,
    mq_vhost
  );
});

function changeJuris(jurisCode){
    var wbengalJuris = {lat: 24.0866538, lng: 88.456634};
    var gmapZoomLevel = 7;
    if ("WB" == jurisCode){
      gmap.setCenter(wbengalJuris);
      gmap.setZoom(gmapZoomLevel);
    }
}

function changeCity(cityCode){
    //var kolkataCoordinates = {lng: 88.0883943, lat: 22.6754807};
    var gmapZoomLevel = 10;
    window.selectedCity = citiesKV[cityCode];
    var cityCoords = {lng: selectedCity.longitude, lat: selectedCity.latitude};
    gmap.setCenter(cityCoords);
    gmap.setZoom(gmapZoomLevel);
    loadSuburbs();
}

function changeSuburb(suburbCode){
    window.selectedSuburbCode = suburbCode;
    showSuburbOnMap(suburbCode);
    showTransformersOnMap(suburbCode);
}

function loadCities(){
  var cities = $("#cities");
  window.citiesKV = new Array();
  $.get("../web-api/city/getall", function(citiesArray, status){
      //alert("Data: " + cities + "\nStatus: " + status);
      for(var i = 0; i < citiesArray.length; i++) {
          var city = citiesArray[i];
          citiesKV[city.name] = city;
          var dropDownItem = "<li><a href='#'>".concat(city.name);
          dropDownItem = dropDownItem.concat("</a></li>");
          cities.append(dropDownItem);
      }
  });
}

function loadSuburbs(){
  var suburbs = $("#suburbs");
  suburbs.empty();
  window.selectedCitySuburbs = new Array();
  var url = "../web-api/suburb/".concat(selectedCity.name);
  $.get(url, function(subUrbsArray, status){
      //alert("Data: " + cities + "\nStatus: " + status);
      selectedCitySuburbs = subUrbsArray;
      for(var i = 0; i < subUrbsArray.length; i++) {
          var subUrb = subUrbsArray[i];
          //suburbsKV[subUrb.name] = subUrb;
          var dropDownItem = "<li><a href='#'>".concat(subUrb);
          dropDownItem = dropDownItem.concat("</a></li>");
          suburbs.append(dropDownItem);
      }
  });
}

function showSuburbOnMap(suburbCode){
    var gmapZoomLevel = 15;
    // this is a hack to show the map centered at Madhyamgram crossing
    // once you have added "center" attribute for UrbanAreas datamodel, remove this
    var mgramCenter = {lat:22.692430, lng:88.465405};
    gmap.setCenter(mgramCenter);
    // end of bad hack
    var geoJsonUrl = "../web-api/suburb/GetGeoJson?city=".concat(selectedCity.name);
    geoJsonUrl = geoJsonUrl.concat("&area=");
    geoJsonUrl = geoJsonUrl.concat(suburbCode);
    gmap.data.loadGeoJson(geoJsonUrl);
    gmap.setZoom(gmapZoomLevel);
}

function showTransformersOnMap(suburbCode){
    var geoJsonUrl = "../web-api/xfr/GetAllAsGeoJson?city=".concat(selectedCity.name);
    geoJsonUrl = geoJsonUrl.concat("&area=");
    geoJsonUrl = geoJsonUrl.concat(suburbCode);
    gmap.data.loadGeoJson(geoJsonUrl,{
      idPropertyName:"name"
    });
    //gmap.setZoom(gmapZoomLevel);
    // Set mouseover event for each feature.
    define_InfoWindow_OnClick_XfrLayer ();
    define_StyleOptions_OnLayersAdd_AllLayers();
    define_InfoWindow_OnRightClick_XfrLayer();
}

function define_InfoWindow_OnClick_XfrLayer(){
  gmap.data.addListener('click', function(event) {
    var featureType = event.feature.getProperty('type');
    if (featureType == 'transformer'){
        var name = event.feature.getProperty('name');
        var status = event.feature.getProperty('status');
        var msg = "Name : ".concat(name);
        msg = msg.concat(", Status : ");
        msg = msg.concat(status);
        //alert(event.feature.getGeometry().getType());
        var xfrPosLatLng = event.feature.getGeometry().getAt(0);
        //alert(xfrPosLatLng.lat());
        //alert(xfrPosLatLng.lng());
        //document.getElementById('info-box').textContent = msg;
        var infoWndPopup = new google.maps.InfoWindow({
            content: msg
        });
        infoWndPopup.setOptions({position: xfrPosLatLng});
        infoWndPopup.open(gmap);
    }
  });
}

function define_StyleOptions_OnLayersAdd_AllLayers(){
  gmap.data.setStyle(function(feature) {
    var featureType = feature.getProperty('type');
    //var style = new google.maps.Data.StyleOptions();
    var style = new Array();
    if (featureType == 'transformer'){
        style.title = feature.getProperty('name').trim();
        var xfrStatus = feature.getProperty('status').trim();
        if (xfrStatus == "OK"){
          style.icon = "http://maps.google.com/mapfiles/ms/icons/green-dot.png";
        }
        else if (xfrStatus == "BAD"){
          style.icon = "http://maps.google.com/mapfiles/ms/icons/red-dot.png";
        }
    }
    else if (featureType == 'urban-area') {
      style.fillColor = 'green';
      style.strokeColor = 'green';
      style.strokeWeight = 1;
      style.fillOpacity = 0.1;
      style.strokeOpacity = 0.5;
    }
    return style;
  });
}

function define_InfoWindow_OnRightClick_XfrLayer(){

  gmap.data.addListener('rightclick', function(event) {
    var featureType = event.feature.getProperty('type');
    if (featureType == 'transformer'){
        var name = event.feature.getProperty('name');
        var status = event.feature.getProperty('status');
        var msg = "Name : ".concat(name);
        msg = msg.concat(", Status : ");
        msg = msg.concat(status);
        window.selectedXfrPosLatLng = event.feature.getGeometry().getAt(0);
        $('#xfrInfoDialog').modal('show');
        $('#xfrInfoDialog').on('show.bs.modal', function (event) {
          var modal = $(this)
          modal.find('#xfr-city').val(selectedCity.name);
        });
        //alert(event.feature.getGeometry().getType());
        //var xfrPosLatLng = event.feature.getGeometry().getAt(0); // Since this geometry will return a "multipoint", and the actual "point" of our interest is the first element of it.
        //alert(xfrPosLatLng.lat());
        //alert(xfrPosLatLng.lng());
        //document.getElementById('info-box').textContent = msg;
        // var infoWndPopup = new google.maps.InfoWindow({
        //     content: msg
        // });
        // infoWndPopup.setOptions({position: xfrPosLatLng});
        // infoWndPopup.open(gmap);
    }
  });

}

function addXfrObserveCircleWithAnim() {
  var circleFillColor = "blue";
  var timeout = 100; // in miilis
  var MAX_FILL_OPACITY = 0.30;
  var MIN_FILL_OPACITY = 0.10;
  var currFillOpacity = MAX_FILL_OPACITY;
  var effect = "FADE";
  var opacityStep = 0.02;
  var observationRadiusInMeters = 500;
  // only display 1 circle at a timeout
  // so check if xfrCircle is already defined and clear it
  // also clear timer used for animation
  if (window.xfrCircle){
      window.xfrCircle.setMap(null);
      window.xfrCircle = null;
      window.clearInterval(window.xfrObsAnimTimer);
  }
  window.xfrCircle = new google.maps.Circle({
      strokeColor: circleFillColor,
      strokeOpacity: 0.8,
      strokeWeight: 2,
      fillColor: circleFillColor,
      fillOpacity: currFillOpacity,
      map: gmap,
      center: window.selectedXfrPosLatLng,
      radius: observationRadiusInMeters
    });
  gmap.fitBounds(xfrCircle.getBounds());
  window.xfrObsAnimTimer = window.setInterval(function() {
    if (currFillOpacity >= MAX_FILL_OPACITY){
      effect = "FADE";
    }
    if (currFillOpacity <= MIN_FILL_OPACITY){
      effect = "GLOW";
    }
    if (effect == "GLOW")
      currFillOpacity = currFillOpacity + opacityStep;
    if (effect == "FADE")
      currFillOpacity = currFillOpacity - opacityStep;
    xfrCircle.setOptions({
      fillOpacity: currFillOpacity,
    });
  }, timeout);
  //toastr.warning('You subscribed to this transformer!')
}
