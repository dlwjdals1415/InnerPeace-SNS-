
$(document).ready(function () {
  $('#checkbox_all').change(function () {
    // "이용약관, 개인정보 수집 및 이용, 광고성 정보 수신(선택)" 체크박스가 변경될 때
    if ($(this).prop('checked')) {
      // 체크되었을 경우
      $('[name^="checkbox_"]').prop('checked', true);
    } else {
      // 체크 해제되었을 경우
      $('[name^="checkbox_"]').prop('checked', false);
    }
  });
});
