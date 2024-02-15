$(document).ready(function () {
    var cropper;

    // 파일 입력 필드가 변경되었을 때
    $('#modify_file_input').change(function () {
        // Button state change
        readFileInput(this);
        $('#modify_fileButton').hide();
        $('#modify_cutButton').show();
        $('#modify_submitButton').hide();

        var reader = new FileReader();
        reader.onload = function (e) {
            if (cropper) {
                cropper.destroy();
            }

            // Set up image modify_preview
            $('#modify_preview').attr('src', e.target.result).on('load', function () {
                // Set image height
                var uploadHeight = $('.upload').height();
                $(this).height(uploadHeight * 5 / 7);

                cropper = new Cropper(document.getElementById('modify_preview'), {
                    aspectRatio: 1,
                    data: {
                        width: 500,
                        fillColor: '#fff',
                        imageSmoothingQuality: 'high'
                    }
                });
                $('#post_image').val(e.target.result.split(',')[1]);
            });
        }

        reader.readAsDataURL(this.files[0]);
    });

    // 자르기 버튼 클릭 시
    $('#modify_cutButton').click(function () {
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
            $('#modify_preview').off('load');
            // 이미지 미리보기 업데이트
            $('#modify_preview').attr('src', finalImageDataURL);
            $('#post_img').attr('src', finalImageDataURL);
            $('#post_img_hidden').val(finalImageDataURL.split(',')[1]);
            $('#modify_cutButton').css('display', 'none'); // 자르기 버튼 숨기기
            $('#modify_submitButton').css('display', 'flex'); // 확인 버튼 보이기
        }
    });

    $('#tag-btn').click(function () {
        var tag = $('#tag').val().trim(); // 공백 제거

        // tag가 비어있지 않은 경우에만 실행
        if (tag) {
            var currentPostTag = $('#post_tag').val();

            // 현재 태그가 비어있지 않으면 앞에 공백 추가
            if (currentPostTag) {
                currentPostTag += ' ';
            }
            $('#post_tag').val(currentPostTag + '#' + tag);
            $('#tag').val('');
        }
    });

    $('#post_tag').change(function() {
        // 변경된 값을 가져옴
        var updatedValue = $(this).val();

        // '#post_tags_hidden' 필드에 값을 설정
        $('#post_tags_hidden').val(updatedValue);
    });

    $("#modify").change(function () {
        // 체크박스가 체크되었는지 확인
        if ($(this).prop("checked")) {
        } else {
            $('#modify_fileButton').show();
            $('#modify_cutButton').hide();
            $('#modify_submitButton').css('display', 'none');
            $(`#post_modify`).css('display','none');
            $('#post_modify').css('opacity','0');
            $('#modify_preview').attr('src', "data:image/png;base64,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7xMnsluewImRXW924eiIp1udUP3Uf3yqfOz//b/6COgO4hp6ub914jDtWJSlKxZmYfbFC9Q//P4P9VGjuN+8azpKV4dCs098vOP3Bu5uW261VfnGN75ZH80w3/zzl80226xMmDCxXHrZz6sFyzpjb+QQzxvPHx8nPl583Pj4rQ0eMqRsu822Hf6z/+WXX25zO8fHH3+s/lPvM/9885XPzDtvfdS1Jznj5FdskdnS5xbsuGlhQPuIa4Be5PHHHy+PPtp2XMf98fbeJPaXbmvf6e647brbbvXfqu+KF96xD28sjtV8izm5EVojtt++2iv5tDFjyhVXXlWu/9UNZcjQofV7khFhG1toLbfcctU2Y3vvs0+58qqry2233V6OG318tX1iR12pfj/xceLjxceNj/+zSy+r/j4D11ijLLnkkmXXXXZtCMaO8thjj5annnqqPpohFnXrirn9nSFGWSy5xBL1UZMpN95Ynnjiifqos/yr3HnnHeWB+++vj5u836gEoPOJa4BeJObCthwKGC+Gm8X98Xb6po44EfG7++6vou7yyVdMv0VcRWgdfvgRVWTFdk7LLrtsmWeeeeqPTEeIkxoxNPviiydUJzHieK655qrf2j3i48fJlfj7XHTR+HLtdddX+zR3hvvuu2+m6SzhqWlxHYHd0ux8rcfX8je/+a36PbpHXN2PK/2xBWazmKs+YcLF1fZqneWpp54uF114YcNJiThxs+KKK9VHQHcR19DLzd0BC//Myj/feae8807nvUBgzsT8xLha0SwWKNpxp50btmOJt8fjoLd65dXGlfA70sEHH9JmqLW+bb311vV70BEiAttaET3EScGHH3qoPup9Vl31q2XAgFXqoyY/v+yyaou1zgjsF154vhx37LHVCv8txciDL37xi/UR0F3ENfRy887buKdm7LkZq3p3hL++8deGUJt//s92yBY5tM9dd91VHn744fooFkpaunzj618vyy03Y//bePsdrl7Ti7RedOzvbVzdbK/Wi6XRPR584IGGgI5pCC1XL586depMK1/3FjHVYsSIEWWhhRq3fTv7rLPK/vvtV5599tn63pxYbf32224re4wcWW688Yb63iarrLJK2WKLLa2RAj2AuIZe7tOf+nS1t3Wz2DM09g7tCH9+rnHxmViF/MMf9mOjO8TiNVdfffX0YZVzzz13Wfvra1dz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883lgP33q4YRtzR8/eENL3jpDE3B0BzWxx57bHX1ptl3Nt20jNh++zkalh/z9kYdeFDDC9YI7KN+eKQh4vRIsabDtttu2zBEOL4Pjj766GrF/Pfee6++d2bxtlhfIB7b8nsnnmvEdiMatiuk88TPsIsuurBhrvU3vvHNhpE0bVlhhRXKpptuVh81iakLjSdWAHo+cQ19yOKLL152Hzmy4cVpBPY5//VfZbNNv1MuHj++uirdfDUg5ibedNOUss/ee5d9992nWkympXhBFEORzbXufPE5ueyyy2YK6/gcxDDvOHkyp+LK9ff2269h0Zz4GPvt9z2LnPViPXEF9l9cPrnNxZnmVPzbYpRGXO1sFt8PP/jP75eddtqxXH31VeWVl1+u31KqP0+efHnZbtttpoX1UQ3fO3M64oO8iOGW8/nj5N52I0a878in+B2z+RZbNER4fC7HnHZatWMCQG8hrqGPWW+9b5cDDhjVENjh0UcfLUceeUQZPGhQWXKJxauhsCutuELZbdddy7XXXtMwjDIMGjy4HPr973f5PMUPopgXP/aMM8oxreIg4jiGd2c+B/H1cOj3D20In9tvu63stOMOVZT8u6uB0NUismKUxmabb94Q2CEWufrevvuW1Vb76vTh/PHnUQcc0LDdU4j3jaib0xEftF9EcMRw88+w+BxsueVWZcUVV6yO30/8nNthxx0bRts8+eST5aQTTyhPPPFEfQ9AzyauoY+JF5IxR/rII49s99Ydm2zynXLSSSe/7+JZXSX2dD7r7LPbvGLWkbejjj6m/ohd59lnny3777dfOe20UxtOcMS2NQcdfEg1vDsrAvvwIw5v+HqI+ZBnjh1b7p46tb6ne8RiSW19Ljr7FgtA9XUxMuXXt99exp17bjn0kIPL5pttWjZYf3h1Uq05TlveBq25Ztl4ow3LiBHbVVeBYyj2I488MseLImbFKI2DDjq4jBy5x0z7uc+OOLG4//4HlP32279dIz6YcxHW3z/0kCqGmw0bNqxsu9129dHsiROK+37vew0nh+O54+u3L+x/DfR94hr6oAjsGF530fiLq9Ce3ReoX/7yl6vtP44bPbrMa45il3jggfvL/fffVx81ibA+5kfHNmyvlhVbdB03+vjquUNcHTr6mB+V1b72teqYviHWVogRCdtvP6KsucbAssMO25fRo48rl156abWe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");
            cropper.destroy();
            cropper = undefined;
        }
    });

    function readFileInput(inputElement) {
        var file = inputElement.files[0];
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#modify_preview').attr('src', e.target.result);
        };

        reader.readAsDataURL(file);
    }


    $("#modify_post_textarea").on("input", function () {
        // post_content의 값을 post_content_hidden에 할당
        $("#modify_post_content_hidden").val($(this).val());
    });

    $('#modify_tag_btn').click(function () {
        var tag = $('#modify_tag').val().trim(); // 공백 제거

        // tag가 비어있지 않은 경우에만 실행
        if (tag) {
            var currentPostTag = $('#modify_post_tag').text(); // .val() 대신 .text() 사용

            // 현재 텍스트가 비어 있지 않으면 태그 앞에 공백 추가
            var newText = currentPostTag ? currentPostTag + ' #' + tag : '#' + tag;

            $('#modify_post_tag').text(newText);
            $('#modify_tag').val('');
            var tags = $('#modify_post_tag').text();
            $('#modify_post_tags_hidden').val(tags);
        }
    });

    $('#tag_reset').click(function() {
        $('#modify_post_tag').text('');
        $('#modify_post_tags_hidden').val('');
    });
});