// Get the map-section element
var mapSection = $('#map_section');
var latlng; // 전역 변수로 선언
// Add a click event listener to the map-search button
$('#map-search-btn').on('click', function () {
  // Set the z-index of the map-section to 10
  var inputsearchValue = $('#map-search').val();
  mapSection.css({"z-index": "20", "opacity": "1"});
  var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
    mapOption = {
      center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
      level: 3 // 지도의 확대 레벨
    };
  var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

  // 장소 검색 객체를 생성합니다
  var ps = new kakao.maps.services.Places();
  var geocoder = new kakao.maps.services.Geocoder();

  ps.keywordSearch(inputsearchValue, placesSearchCB);

  function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {

      // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
      // LatLngBounds 객체에 좌표를 추가합니다
      var bounds = new kakao.maps.LatLngBounds();

      for (var i = 0; i < data.length; i++) {
        bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
      }

      // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
      map.setBounds(bounds);
    }
  }

  // 지도를 클릭한 위치에 표출할 마커입니다
  var marker = new kakao.maps.Marker(),
    infowindow = new kakao.maps.InfoWindow({ zindex: 1 });
  // 지도에 마커를 표시합니다
  marker.setMap(map);
  searchAddrFromCoords(map.getCenter(), displayCenterInfo);

  // 지도에 클릭 이벤트를 등록합니다
  // 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
  kakao.maps.event.addListener(map, 'click', function (mouseEvent) {
    searchDetailAddrFromCoords(mouseEvent.latLng, function (result, status) {
      if (status === kakao.maps.services.Status.OK) {
        var detailAddr = !!result[0].road_address ? '<div>도로명주소 : ' + result[0].road_address.address_name + '</div>' : '';
        detailAddr += '<div>지번 주소 : ' + result[0].address.address_name + '</div>';

        var content = '<div class="bAddr">' +
          '<span class="title">법정동 주소정보</span>' +
          detailAddr +
          '</div>';

        // 클릭한 위도, 경도 정보를 가져옵니다 
        latlng = mouseEvent.latLng;

        // 마커 위치를 클릭한 위치로 옮깁니다
        marker.setPosition(mouseEvent.latLng);
        marker.setMap(map);
        infowindow.setContent(content);
        infowindow.open(map, marker);

        // latlng 변수 값을 설정한 후에 다음 작업을 수행
        $('#post_map_lat').val(latlng.getLat());
        $('#post_map_lng').val(latlng.getLng());
      };
    });
  });
  kakao.maps.event.addListener(map, 'idle', function () {
    searchAddrFromCoords(map.getCenter(), displayCenterInfo);
  });

  function searchAddrFromCoords(coords, callback) {
    // 좌표로 행정동 주소 정보를 요청합니다
    geocoder.coord2RegionCode(coords.getLng(), coords.getLat(), callback);
  }

  function searchDetailAddrFromCoords(coords, callback) {
    // 좌표로 법정동 상세 주소 정보를 요청합니다
    geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
  }

  // 지도 좌측상단에 지도 중심좌표에 대한 주소정보를 표출하는 함수입니다
  function displayCenterInfo(result, status) {
    if (status === kakao.maps.services.Status.OK) {
      var infoDiv = document.getElementById('centerAddr');

      for (var i = 0; i < result.length; i++) {
        // 행정동의 region_type 값은 'H' 이므로
        if (result[i].region_type === 'H') {
          infoDiv.innerHTML = result[i].address_name;
          break;
        }
      }
    }
  }
  $('#map_section').on('click', '#map-confirm', function () {
    var infoDiv = document.getElementById('address');
    infoDiv.innerHTML = $('#centerAddr').text();
    mapSection.css({"z-index": "0", "opacity": "0"});
  });

  $('#map_section').on('click', '#map-cancel', function () {
    $('#post_map_lat').val('');
    $('#post_map_lng').val('');
    var infoDiv = document.getElementById('address');
    infoDiv.innerHTML = '';
  
    map.relayout();
    map = null;
    mapSection.css({"z-index": "0", "opacity": "0"});
  });
});