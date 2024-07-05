var urlAPI = "/api/v1/admin/data-list-add-san-pham";

var dataListMauSac,
    dataListCongNgheManHinh,
    dataListBluetooth,
    dataListCongNghePin,
    dataListCpu,
    dataListGps,
    dataListMatKinhCamUng,
    dataListSeries,
    dataListTinhNangCamera,
    dataListTinhNangDacBiet,
    dataListWifi

//Hiển thị lớp phủ khi chưa load hết thuộc tính
$("#overlay").show();
$(document).ajaxStart(function() {
}).ajaxStop(function() {
    $("#overlay").hide();
});


$(".custom-file-input").on("change", function() {
    let fileName = $(this).val().split("\\").pop();
    $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
});

const cloneData = new Promise(function (resolve, reject) {
    $.ajax({
            url: urlAPI,
            success: function (response) {
                resolve(response);
            },
            error: function (xhr, status, error) {
                reject(status);
            }
        }
    )
})


$(document).ready(function () {
    cloneData.then(function (data) {
        // $('#sp-rom').select2();
        // $("#sp-mau-sac").select2({
        //     data: data.mauSac
        // });
        console.log(data.congNgheManHinh)
        $("#sp-mau-sac").select2({
            data: data.mauSac
        });
        $("#sp-cong-nghe-man-hinh").select2({
            data: data.congNgheManHinh
        });
        $("#sp-series").select2({
            data: data.series
        });
        $("#sp-rom").select2({})
    })

})


$(document).ready(function () {
    $(this).attr("title", "Thêm sản phẩm mới")


    var navListItems = $('div.setup-panel div a'),
        allWells = $('.setup-content'),
        allNextBtn = $('.nextBtn');
    allWells.hide();
    allWells.removeClass("d-none")
    navListItems.click(function (e) {
        e.preventDefault();
        var $target = $($(this).attr('href')),
            $item = $(this);
        if (!$item.hasClass('disabled')) {
            navListItems.removeClass('btn-primary').addClass('btn-secondary');
            $item.removeClass('btn-secondary')
            $item.addClass('btn-primary');
            allWells.hide();
            $target.show();
            $target.find('input:eq(0)').focus();
        }
    });

    allNextBtn.click(function () {
        alert('ok')
        var curStep = $(this).closest(".setup-content"),
            curStepBtn = curStep.attr("id"),
            nextStepWizard = $('div.setup-panel div a[href="#' + curStepBtn + '"]').parent().next().children("a"),
            curInputs = curStep.find("input, select"),
            isValid = true;
        console.log(curInputs)
        curInputs.removeClass("is-invalid")
        for (var i = 0; i < curInputs.length; i++) {
            if (!curInputs[i].validity.valid) {
                isValid = false;
                $(curInputs[i]).addClass("is-invalid");
            } else {
                $(curInputs[i]).addClass("is-valid");
            }
        }

        if (isValid)
            nextStepWizard.removeAttr('disabled').trigger('click');
    });

    $('div.setup-panel div a.btn-primary').trigger('click');
});
//
//
// Đoạn mã JavaScript này được sử dụng để quản lý một form đăng ký đa bước trong trang web. Hãy đi từng bước để giải thích cách JavaScript hoạt động trong đoạn mã này:
//
//     1. Khi tài liệu đã sẵn sàng ($(document).ready()):
// javascript
// Sao chép mã
// $(document).ready(function () {
//     // Các đoạn mã JavaScript được đặt trong này sẽ được thực thi khi toàn bộ tài liệu HTML đã tải xong.
// });
// Đoạn mã bên trong sẽ được thực thi khi toàn bộ tài liệu HTML đã được tải xong.
// 2. Khai báo biến quan trọng:
//     javascript
// Sao chép mã
// var navListItems = $('div.setup-panel div a'),
//     allWells = $('.setup-content'),
//     allNextBtn = $('.nextBtn');
// navListItems: Là danh sách các thành phần <a> nằm trong các <div class="setup-panel">, đại diện cho các nút điều hướng giữa các bước.
//     allWells: Là danh sách tất cả các phần tử có lớp setup-content, đại diện cho các phần nội dung của từng bước.
//     allNextBtn: Là danh sách tất cả các nút có lớp nextBtn, đại diện cho các nút "Next" trong từng bước.
// 3. Ẩn tất cả các phần nội dung bước:
//     javascript
// Sao chép mã
// allWells.hide();
// Ban đầu, ẩn tất cả các phần nội dung của các bước để chỉ hiển thị bước đầu tiên.
// 4. Xử lý khi người dùng nhấn vào các nút điều hướng (navListItems):
// javascript
// Sao chép mã
// navListItems.click(function (e) {
//     e.preventDefault();
//     var $target = $($(this).attr('href')),
//         $item = $(this);
//
//     if (!$item.hasClass('disabled')) {
//         navListItems.removeClass('btn-primary').addClass('btn-default');
//         $item.addClass('btn-primary');
//         allWells.hide();
//         $target.show();
//         $target.find('input:eq(0)').focus();
//     }
// });
// Khi người dùng nhấn vào một trong các navListItems:
//     Ngăn chặn hành vi mặc định của thẻ <a> (chẳng hạn chuyển hướng).
// Tìm phần nội dung tương ứng để hiển thị ($target).
//     Làm cho nút điều hướng hiện tại trở thành nổi bật (btn-primary), các nút khác trở thành mặc định (btn-default).
// Ẩn tất cả các phần nội dung khác và chỉ hiển thị phần nội dung của bước tương ứng.
//     Đặt trỏ chuột vào trường nhập liệu đầu tiên của bước mới hiển thị.
// 5. Xử lý khi người dùng nhấn vào nút "Next" (allNextBtn):
// javascript
// Sao chép mã
// allNextBtn.click(function(){
//     var curStep = $(this).closest(".setup-content"),
//         curStepBtn = curStep.attr("id"),
//         nextStepWizard = $('div.setup-panel div a[href="#' + curStepBtn + '"]').parent().next().children("a"),
//         curInputs = curStep.find("input[type='text'],input[type='url']"),
//         isValid = true;
//
//     $(".form-group").removeClass("has-error");
//     for(var i=0; i<curInputs.length; i++){
//         if (!curInputs[i].validity.valid){
//             isValid = false;
//             $(curInputs[i]).closest(".form-group").addClass("has-error");
//         }
//     }
//
//     if (isValid)
//         nextStepWizard.removeAttr('disabled').trigger('click');
// });

// Khi người dùng nhấn vào một trong các nút "Next":
// Xác định bước hiện tại (curStep) và lấy ID của nó (curStepBtn).
//     Tìm nút điều hướng tới bước tiếp theo (nextStepWizard).
//     Kiểm tra tính hợp lệ của các trường nhập liệu trong bước hiện tại.
//     Nếu tất cả các trường nhập liệu hợp lệ (isValid), hủy bỏ thuộc tính disabled của nút điều hướng tiếp theo và kích hoạt sự kiện click trên nút đó.
// 6. Kích hoạt sự kiện click vào nút điều hướng đầu tiên khi tài liệu được tải xong:
//     javascript
// Sao chép mã
// $('div.setup-panel div a.btn-primary').trigger('click');
// Khi tài liệu đã được tải xong, tự động kích hoạt sự kiện click vào nút điều hướng đầu tiên (nút có lớp btn-primary).
// Tóm lại:
//     Đoạn mã này sử dụng jQuery để tạo ra một form đăng ký đa bước trong trang web. Nó quản lý hiển thị các bước và kiểm soát chuyển đổi giữa các bước dựa trên sự kiện click của người dùng và tính hợp lệ của các trường nhập liệu. Mỗi khi người dùng nhấn vào nút "Next", mã sẽ kiểm tra xem các trường nhập liệu có hợp lệ không và di chuyển sang bước tiếp theo nếu thỏa mãn điều kiện.