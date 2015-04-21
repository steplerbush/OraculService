<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<script type="text/javascript"
	src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&language=uk"></script>
<script src="${pageContext.request.contextPath}/js/conrec.js"></script>
<title>Map</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/default.css" />


<script>
	var map;
	var windVectorsArray = [];
	var windPolygonContoursArray = [];
	var windPolylineContoursArray = [];
 	var Us = ${u};
 	var Vs = ${v};
 	var zoomMap = 5;
	var startLat = 0;
	var startLng = 0;
	var step = 1.5;
	var arrayLengthLat = 61;
	var arrayLengthLng = 60;

	function CustomControl(controlDiv, map) {

		// Set CSS styles for the DIV containing the control
		// Setting padding to 5 px will offset the control
		// from the edge of the map
		controlDiv.style.padding = '5px';

		// Set CSS for the control border
		var controlUI = document.createElement('div');
		controlUI.style.backgroundColor = 'white';
		controlUI.style.cursor = 'pointer';
		controlUI.style.textAlign = 'left';
		controlUI.title = 'Select layers to display';
		controlDiv.appendChild(controlUI);

		// Set CSS for the control interior
		var controlSelectDiv = document.createElement('div');
		controlSelectDiv.style.paddingLeft = '4px';
		controlSelectDiv.style.paddingRight = '4px';

		var lblSelectDivText = document.createElement('label');
		lblSelectDivText.innerHTML = '<b>Layers:</b>';

		var chkWindVectors = document.createElement("input");
		chkWindVectors.type = "checkbox";
		chkWindVectors.id = "chkWindVectors";
		chkWindVectors.value = "WindVectors";
		chkWindVectors.checked = true;
		var lblWindVectors = document.createElement('label')
		lblWindVectors.htmlFor = "chkWindVectors";
		lblWindVectors
				.appendChild(document.createTextNode('Wind speed vector'));

		var chkWindPolygonContour = document.createElement("input");
		chkWindPolygonContour.type = "checkbox";
		chkWindPolygonContour.id = "chkWindPolygonContour";
		chkWindPolygonContour.value = "WindPolygonContour";
		chkWindPolygonContour.checked = false;
		var lblWindPolygonContour = document.createElement('label')
		lblWindPolygonContour.htmlFor = "chkWindPolygonContour";
		lblWindPolygonContour.appendChild(document
				.createTextNode('Wind speed polygons'));

		var chkWindPolylineContour = document.createElement("input");
		chkWindPolylineContour.type = "checkbox";
		chkWindPolylineContour.id = "chkWindPolylineContour";
		chkWindPolylineContour.value = "WindLineContour";
		chkWindPolylineContour.checked = false;
		var lblWindPolylineContour = document.createElement('label')
		lblWindPolylineContour.htmlFor = "chkWindPolylineContour";
		lblWindPolylineContour.appendChild(document
				.createTextNode('Wind speed polilynes'));

		controlSelectDiv.appendChild(lblSelectDivText);
		controlSelectDiv.appendChild(document.createElement("BR"));
		controlSelectDiv.appendChild(chkWindVectors);
		controlSelectDiv.appendChild(lblWindVectors);
		controlSelectDiv.appendChild(document.createElement("BR"));
		controlSelectDiv.appendChild(chkWindPolygonContour);
		controlSelectDiv.appendChild(lblWindPolygonContour);
		controlSelectDiv.appendChild(document.createElement("BR"));
		controlSelectDiv.appendChild(chkWindPolylineContour);
		controlSelectDiv.appendChild(lblWindPolylineContour);

		controlUI.appendChild(controlSelectDiv);

		google.maps.event.addDomListener(chkWindVectors, 'change', function() {
			setVisibleOverlays(windVectorsArray, chkWindVectors.checked);
		});

		google.maps.event.addDomListener(chkWindPolygonContour, 'change',
				function() {
					setVisibleOverlays(windPolygonContoursArray,
							chkWindPolygonContour.checked);
				});

		google.maps.event.addDomListener(chkWindPolylineContour, 'change',
				function() {
					setVisibleOverlays(windPolylineContoursArray,
							chkWindPolylineContour.checked);
				});

	}

	function loadFile(uri) {
		var lines;
		var r = new XMLHttpRequest();
		r.open('GET', uri, false);
		r.send(null);
		if (r.status == 200) {
			lines = r.responseText.split('\n');
		}
		return lines;
	}

	function fillArray(value, len) {
		var arr = [];
		for (var i = 0; i < len; i++) {
			arr.push(value);
		}
		;
		return arr;
	}

	//Removes the overlays from the map, but keeps them in the array
	function clearOverlays(overlaysArray) {
		if (overlaysArray) {
			for (i in overlaysArray) {
				overlaysArray[i].setMap(null);
			}
		}
	}

	//Removes the overlays from the map, but keeps them in the array
	function hideOverlays(overlaysArray) {
		if (overlaysArray) {
			for (i in overlaysArray) {
				overlaysArray[i].setMap(null);
			}
		}
	}

	function setVisibleOverlays(overlaysArray, visible) {
		if (overlaysArray) {
			for (i in overlaysArray) {
				overlaysArray[i].setVisible(visible);
			}
		}
	}

	// Shows any overlays currently in the array
	function showOverlays(overlaysArray) {
		if (overlaysArray) {
			for (i in overlaysArray) {
				overlaysArray[i].setMap(map);
			}
		}
	}

	// Deletes all overlays in the array by removing references to them
	function deleteOverlays(overlaysArray) {
		if (overlaysArray) {
			for (i in overlaysArray) {
				overlaysArray[i].setMap(null);
			}
			overlaysArray.length = 0;
		}
	}

	function updateZoom(overlaysArray) {
		if (overlaysArray) {
			for (i in overlaysArray) {
				overlaysArray[i].setOptions({
					strokeWeight : 1 * (zoomMap / 4)
				});
			}
		}
	}

	function calcGradient(value) {
		var max = 70;
		if (value > max) {
			value = max;
		}
		;
		//(A1-(A1-B1)/h*x, A2-(A2-B2)/h*x, A3-(A3-B3)/h*x)
		var color = parseInt(0xFF / max * value) * 0x10000
				+ parseInt(0xFF - 0xFF / max * value) * 0x100;
		return ('#' + color.toString(16));
	}

	function addWindVector(startLat, startLng, endLat, endLng, color) {

		var lineSymbol = {
			path : google.maps.SymbolPath.FORWARD_CLOSED_ARROW
		};
		var lineCoordinates = [ new google.maps.LatLng(startLat, startLng),
				new google.maps.LatLng(endLat, endLng) ];

		var line = new google.maps.Polyline({
			path : lineCoordinates,
			strokeWeight : 1 * (zoomMap / 4),
			strokeColor : color,
			icons : [ {
				icon : lineSymbol,
				offset : '100%',
			} ],
			map : map
		});
		windVectorsArray.push(line);
	}

	function addWind() {

		
		//var arrLimit = parseInt(Math.sqrt(Us.length));
		var vectorCoefficient = 0.03;

		for (var i = 0; i < arrayLengthLat; i++) {
			for (var j = 0; j < arrayLengthLng; j++) {
				addWindVector(startLat + step * i, startLng + step * j,
						startLat + step * i + Vs[(i * arrayLengthLat) + j]
								* vectorCoefficient, startLng + step * j
								+ Us[(i * arrayLengthLat) + j]
								* vectorCoefficient, calcGradient(Math
								.sqrt(Math.pow(Vs[(i * arrayLengthLat) + j], 2)
										+ Math
												.pow(Us[(i * arrayLengthLat)
														+ j], 2))));
			}
		}
	}

	function calcContour(addCliffEdge) {

	
		//var arrLimit = parseInt(Math.sqrt(Us.length));

		var d = new Array(arrayLengthLat); // - matrix of data to contour
		for (var i = 0; i < arrayLengthLat; i++) {
			d[i] = new Array(arrayLengthLng);
			for (var j = 0; j < arrayLengthLng; j++) {
				d[i][j] = Math.sqrt(Math.pow(Vs[(i * arrayLengthLat) + j], 2)
						+ Math.pow(Us[(i * arrayLengthLat) + j], 2));
			}
		}

		//Add a "cliff edge" to force contour lines to close along the border.
		if (addCliffEdge) {
			var cliff = -100;
			d.push(fillArray(cliff, arrayLengthLng));
			d.unshift(fillArray(cliff, arrayLengthLng));
			d.forEach(function(nd) {
				nd.push(cliff);
				nd.unshift(cliff);
			});
		}

		//index bounds of data matrix
		var ilb = 0;
		//var iub = arrLimit-1;
		var jlb = 0;
		//var jub = arrLimit-1;
		//             The following two, one dimensional arrays (x and y) contain
		//             the horizontal and vertical coordinates of each sample points.
		// x  - data matrix column coordinates
		var x = [];
		for (var i = 0; i < d.length; i++) {
			x.push(startLat + i * step + (addCliffEdge ? -step : 0));
		}
		//y  - data matrix row coordinates
		var y = [];
		for (var j = 0; j < d[0].length; j++) {
			y.push(startLng + j * step + (addCliffEdge ? -step : 0));
		}
		//nc   - number of contour levels
		//var nc = 4;
		//z  - contour levels in increasing order.
		var z = [ -10, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120,
				130 ];

		var c = new Conrec();
		c.contour(d, ilb, x.length - 1, jlb, y.length - 1, x, y, z.length, z);
		// c.contours will now contain vectors in the form of doubly-linked lists.
		// c.contourList() will return an array of vectors in the form of arrays.
		return c.contourList();
	}

	function addPolygonContour() {
		var contours = calcContour(true);
		for (i = contours.length - 1; i >= 0; i--) {
			var contourCoords = [];
			for (j = 0; j < contours[i].length; j++) {
				contourCoords.push(new google.maps.LatLng(contours[i][j].x,
						contours[i][j].y));
			}
			var polygon = new google.maps.Polygon({
				paths : contourCoords,
				strokeColor : calcGradient(contours[i].level + 30),
				strokeOpacity : 0.8,
				strokeWeight : 1,
				fillColor : calcGradient(contours[i].level + 30),
				fillOpacity : 0.2,
				map : map
			});
			windPolygonContoursArray.push(polygon);
		}
	}

	function addPolylineContour() {
		var contours = calcContour(false);
		for (i = 0; i < contours.length; i++) {
			var contourCoords = [];
			for (j = 0; j < contours[i].length; j++) {
				contourCoords.push(new google.maps.LatLng(contours[i][j].x,
						contours[i][j].y));
			}
			var polyline = new google.maps.Polyline({
				path : contourCoords,
				strokeColor : calcGradient(contours[i].level + 30),
				strokeOpacity : 0.8,
				strokeWeight : 1.5,
				map : map
			});
			windPolylineContoursArray.push(polyline);
		}
	}

	function initialize() {

		var myLatLng = new google.maps.LatLng(50, 30);
		var mapOptions = {
			zoom : zoomMap,
			center : myLatLng,
			mapTypeId : google.maps.MapTypeId.TERRAIN
		};

		map = new google.maps.Map(document.getElementById('map-canvas'),
				mapOptions);
		addWind();
		addPolygonContour();
		addPolylineContour();

		zoomChangeListener = google.maps.event
				.addListener(
						map,
						'zoom_changed',
						function(event) {
							zoomChangeBoundsListener = google.maps.event
									.addListener(
											map,
											'bounds_changed',
											function(event) {
												zoomMap = map.zoom;
												updateZoom(windVectorsArray)
												google.maps.event
														.removeListener(zoomChangeBoundsListener);
											});
						});

		// Create the DIV to hold the control and
		// call the CustomControl() constructor passing
		// in this DIV.
		var customControlDiv = document.createElement('div');
		var customControl = new CustomControl(customControlDiv, map);

		customControlDiv.index = 1;
		map.controls[google.maps.ControlPosition.RIGHT].push(customControlDiv);
		setVisibleOverlays(windPolygonContoursArray, false);
		setVisibleOverlays(windPolylineContoursArray, false)
	}

	google.maps.event.addDomListener(window, 'load', initialize);
</script>
</head>
<body>
<div id="map-canvas"></div>
</body>

</html>