$(document).ready(function () {
  var cropper;

  // 파일 입력 필드가 변경되었을 때
  $('#file-input').change(function () {
    // 버튼 상태 변경
    $('.fileButton').hide();
    $('.cutButton').show();

    var reader = new FileReader();
    reader.onload = function (e) {
      if (cropper) {
        cropper.destroy();
      }

      // 이미지 미리보기 설정
      $('#preview').attr('src', e.target.result).on('load', function () {
        // 이미지 높이 설정
        var uploadHeight = $('.upload').height();
        $(this).height(uploadHeight * 5 / 7); // 5/7을 곱하는 이유는 h1과 button의 높이 비율 1:1을 고려하기 때문입니다.

        cropper = new Cropper(document.getElementById('preview'), {
          aspectRatio: 1,
          data: {
            width: 500,
            fillColor: '#fff',
            imageSmoothingQuality: 'high'
          }
        });
      });
    }

    reader.readAsDataURL(this.files[0]);
  });

  // 자르기 버튼 클릭 시
  $('#cutButton').click(function () {
    if (cropper) {
      var croppedCanvas = cropper.getCroppedCanvas();
      var finalCanvas = document.createElement('canvas');
      finalCanvas.width = finalCanvas.height = Math.max(croppedCanvas.width, croppedCanvas.height);

      var ctx = finalCanvas.getContext('2d');
      ctx.fillStyle = '#FFFFFF'; // 흰색으로 채우기
      ctx.fillRect(0, 0, finalCanvas.width, finalCanvas.height);
      ctx.drawImage(croppedCanvas, (finalCanvas.width - croppedCanvas.width) / 2, (finalCanvas.height - croppedCanvas.height) / 2);
      
      var finalImageDataURL = finalCanvas.toDataURL('image/png');

      // Cropper.js 인스턴스 파괴
      cropper.destroy();
      cropper = undefined;

      // 'load' 이벤트 핸들러 제거
      $('#preview').off('load');

      console.log(finalImageDataURL);
      // 이미지 미리보기 업데이트
      $('#preview').attr('src', finalImageDataURL);
      $('#post_img').attr('src', finalImageDataURL);
      $('#post_img_hidden').val(finalImageDataURL);
      $('.cutButton').css('display', 'none'); // 자르기 버튼 숨기기
      $('.submitButton').css('display', 'flex'); // 확인 버튼 보이기
    }
  });

  $('#add').click(function () {
    var tag = $('#tag').val();
    var currentPostTag = $('#post_tag').val();
    $('#post_tag').val(currentPostTag + ' #' + tag);
    $('#tag').val('');
  });

});