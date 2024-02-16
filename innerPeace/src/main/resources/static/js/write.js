$(document).ready(function () {
  var cropper;

  // 파일 입력 필드가 변경되었을 때
  $('#write_file-input').change(function () {
    // Button state change
    readFileInput(this);
    $('#write_fileButton').hide();
    $('#write_cutButton').show();

    var reader = new FileReader();
    reader.onload = function (e) {
      if (cropper) {
        cropper.destroy();
      }

      // Set up image write_preview
      $('#write_preview').attr('src', e.target.result).on('load', function () {
        // Set image height
        var uploadHeight = $('#upload_article').height();
        $(this).height(uploadHeight * 5 / 7);

        cropper = new Cropper(document.getElementById('write_preview'), {
          aspectRatio: 1,
          viewMode: 1, // 크롭 박스가 캔버스 밖으로 나가지 않도록 설정
        });
      });
    }

    reader.readAsDataURL(this.files[0]);
  });

  // 자르기 버튼 클릭 시
  $('#write_cutButton').click(function () {
    if (cropper) {
      var croppedCanvas = cropper.getCroppedCanvas({
        width: 500,
        fillColor: '#fff',
        imageSmoothingQuality: 'high',
      });
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
      $('#write_preview').off('load');
      // 이미지 미리보기 업데이트
      $('#write_preview').attr('src', finalImageDataURL);
      $('#post_img').attr('src', finalImageDataURL);
      $('#post_img_hidden').val(finalImageDataURL.split(',')[1]);
      $('#write_cutButton').css('display', 'none'); // 자르기 버튼 숨기기
      $('#write_submitButton').css('display', 'flex'); // 확인 버튼 보이기
    }
  });

  $('#tag-btn').click(function () {
    var tag = $('#tag').val().trim(); // 공백 제거

    // tag가 비어있지 않은 경우에만 실행
    if (tag) {
      var currentPostTag = $('#post_tag').text(); // .val() 대신 .text() 사용

      // 현재 텍스트가 비어 있지 않으면 태그 앞에 공백 추가
      var newText = currentPostTag ? currentPostTag + ' #' + tag : '#' + tag;

      $('#post_tag').text(newText);
      $('#tag').val('');
      var tags = $('#post_tag').text();
      $('#post_tags_hidden').val(tags);
    }
  });


    $('#post_tag_reset').click(function (){
      $('#post_tag').text('');
      $('#post_tags_hidden').val('');
    });

  $("#create").change(function () {
    // 체크박스가 체크되었는지 확인
    if ($(this).prop("checked")) {
    } else {
      $('#write_fileButton').show();
      $('#write_cutButton').hide();
      $('#write_submitButton').css('display', 'none');
      $(`#post_write`).css('display','none');
      $('#post_write').css('opacity','0');
      $('#write_preview').attr('src', "/img/defualt.png");
      cropper.destroy();
      cropper = undefined;
    }
  });

  function readFileInput(inputElement) {
    var file = inputElement.files[0];
    var reader = new FileReader();

    reader.onload = function (e) {
      $('#write_preview').attr('src', e.target.result);
    };

    reader.readAsDataURL(file);
  }

  $("#post_textarea").on("input", function () {
    // post_content의 값을 post_content_hidden에 할당
    $("#post_content_hidden").val($(this).val());
  });
});