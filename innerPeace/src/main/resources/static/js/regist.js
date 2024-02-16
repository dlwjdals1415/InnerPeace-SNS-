document.addEventListener('DOMContentLoaded', function () {
  document.getElementById('checkbox_all').addEventListener('change', function () {
    // "이용약관, 개인정보 수집 및 이용, 광고성 정보 수신(선택)" 체크박스가 변경될 때
    var checkboxes = document.querySelectorAll('[name^="checkbox_"]');
    for (var i = 0; i < checkboxes.length; i++) {
      checkboxes[i].checked = this.checked;
    }
    document.getElementById('checkbox_ad_agreement').checked = this.checked;
  });
});