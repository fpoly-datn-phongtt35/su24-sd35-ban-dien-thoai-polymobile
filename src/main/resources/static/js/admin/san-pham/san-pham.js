// // dependency
// Top
// <link href="/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet"/>
// <script src="/vendor/sweetalert2/sweetalert2.min.js"></script>
// Bottom
// <script src="/vendor/datatables/jquery.dataTables.min.js"></script>
// <script src="/vendor/datatables/dataTables.bootstrap4.min.js"></script>

const apiURL = "/api/v1/san-pham";
let _existingNames;

var lstSeries, lstMauSac;
// fill data to select option

$(document).ready(function () {
    $('#modal-filter').on('show.bs.modal', function (event) {
        //So Luong
        let maxSoLuong = 0;
        maxSoLuong = Math.max.apply(Math, $('#dataTable').DataTable().column(5).data().toArray());
        maxSoLuong = Math.ceil(maxSoLuong / 100) * 100
        console.log(maxSoLuong)


        $("#rangeSlider").ionRangeSlider({
            type: "double",
            min: 0,
            max: maxSoLuong,
            from: 0,
            to: maxSoLuong,
            grid: true,
            prettify: true,
            skin: "round"
        });
    })
})

function loadFilter() {
    //Mau sac
    $.ajax({
            url: "/api/v1/admin/data-list-add-san-pham/mau-sac",
            success: function (response) {
                lstMauSac = response;
                $('#filter-mau-sac').select2({
                    data: lstMauSac.map(ms => {
                        return {id: ms.text, text: ms.text}
                    })
                })
            },
            error: function (xhr, status, error) {
            }
        }
    )
    //Series
    $.ajax({
            url: "/api/v1/admin/data-list-add-san-pham/series",
            success: function (response) {
                lstSeries = response;
                $('#filter-series').select2({
                    data: lstSeries.map(s => {
                        return {id: s.text, text: s.text}
                    })
                })
            },
            error: function (xhr, status, error) {
            }
        }
    )
    //Rom
    $('#filter-rom').select2({})

}

$(document).attr("title", "Quản lý sản phẩm")

function loadData() {

    let table = $('#dataTable').DataTable({

        "language": {
            "sProcessing": "Đang xử lý...",
            "sLengthMenu": "Hiển thị _MENU_ bản ghi",
            "sZeroRecords": "Không tìm thấy dữ liệu",
            "sInfo": "Hiển thị _START_ đến _END_ của _TOTAL_ bản ghi",
            "sInfoEmpty": "Hiển thị 0 đến 0 của 0 bản ghi",
            "sInfoFiltered": "(được lọc từ _MAX_ bản ghi)",
            "sInfoPostFix": "",
            "sSearch": "Tìm kiếm:",
            "sUrl": "",
            "oPaginate": {
                "sFirst": "Đầu",
                "sPrevious": "Trước",
                "sNext": "Tiếp",
                "sLast": "Cuối"
            }
        },
        "ajax": {
            "url": apiURL + "-data-table",
            "cache": true,
            "dataSrc": ""
        },

        "columnDefs": [
            {width: 30, targets: 0},
            {width: 150, targets: 1}
        ],
        "columns": [

            {"data": "id"},
            {"data": "tenSanPham"},
            {"data": "series"},

            {

                "data": "danhSachMauSac",
                "render": function (data, type, row) {

                    if (data == null || data.length == 0)
                        return ""
                    let lstMs = data.split(",");
                    let lstE = "";
                    lstMs.forEach(function (m) {
                        let ten = m.split(":")[1];
                        let ma = m.split(":")[0];
                        lstE += `<a type="button" class="btn btn-sm mr-2" style="background-color: ${ma}"></a>`;
                        lstE += `<span type="button" class="btn btn-sm d-none" >${ten}</span>`
                    })
                    return lstE;

                },
                "orderable": false,


            },
            {"data": "danhSachRom"},
            {"data": "soLuong"},
            {"data": "thoiGianBaoHanh"},
            {"data": "trangThai"}
            // {
            //     "data": "deleted",
            //     "render": function (data, type, row) {
            //         if (data)
            //             return '<div class="d-flex justify-content-end"><button type="button" class="btn btn-sm btn-primary mr-3 btn-edit">Chỉnh sửa</button><button type="button" class="btn btn-sm btn-danger btn-revert">Khôi phục</button></div>';
            //         else
            //             return '<div class="d-flex justify-content-end"><button type="button" class="btn btn-sm btn-primary mr-3 btn-edit">Chỉnh sửa</button><button type="button" class="btn btn-sm btn-danger btn-delete">Xóa</button></div>';
            //
            //     },
            //     "orderable": false
            //
            // }
        ]
    });
    // let selectedStatus = $('#statusFilter').val();
    // if (selectedStatus) {
    //     table.column(3).search('^' + selectedStatus + '$', true, false).draw();
    // } else {
    //     // Xóa bộ lọc nếu không có gì được chọn
    //     table.column(3).search('').draw();
    // }
    // ;
}


//
// const reloadDataTable = () => {
//     $('#dataTable').DataTable().ajax.reload();
// }

//config dataTable
$(document).ready(() => {
        loadData();
        loadFilter()

    }
);
//
// //Reload event
// $(document).ready(() => {
//     $('#btn-reload').on("click", () => {
//         reloadDataTable()
//     })
// })
//
// //Add
// $(document).ready(() => {
//     let existingNames;
//     //Event
//     $('#btn-add').on('click', () => {
//         clearForm();
//         existingNames = $('#dataTable').DataTable().column(1).data().toArray().map(name => name.toLowerCase());
//         $('#modal-add').modal('show');
//         console.log(existingNames)
//     })
//     //Validate exitst
//     $('#add-ten').on('input paste', function () {
//         let ten = $('#add-ten').val().toLowerCase().trim();
//         if (existingNames.includes(ten) || ten.trim() === "") {
//             $('#add-ten').addClass('is-invalid');
//             $('#add-ten').removeClass('is-valid');
//         } else {
//             $('#add-ten').removeClass('is-invalid');
//             $('#add-ten').addClass('is-valid');
//         }
//     });
//     //Core
//     $('#form-add').submit(function (event) {
//         event.preventDefault(); // Ngăn chặn submit mặc định của form
//         const form = $(this);
//         if (!form[0].checkValidity()) {
//             // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
//             event.stopPropagation();
//             form.addClass('was-validated');
//         } else {
//             let formData = {
//                 ten: $('#add-ten').val(),
//                 link: $('#add-link').val()
//             }
//             // Nếu form hợp lệ, gửi dữ liệu form lên server
//             $.ajax({
//                 url: apiURL, // Thay 'URL_API' bằng đường dẫn của API của bạn
//                 method: 'PUT', // Phương thức HTTP
//                 data: JSON.stringify(formData),
//                 contentType: 'application/json',
//                 success: function (response) {
//                     Toast.fire({
//                         icon: "success",
//                         title: "Thêm mới thành công"
//                     })
//                     $('#modal-add').modal('hide');
//                     reloadDataTable();
//                     console.log(response);
//                 },
//                 error: function (xhr, status, error) {
//                     Toast.fire({
//                         icon: "error",
//                         title: "Thêm mới thất bại"
//                     });
//                     reloadDataTable();
//                     console.log(response);
//                     console.error(xhr.responseText);
//                 }
//             });
//
//         }
//     });
// })
// //Edit
// $(document).ready(() => {
//     let existingNames;
//     //Event
//     $('#dataTable tbody').on('click', '.btn-edit', function () {
//         clearForm()
//         let rowData = $('#dataTable').DataTable().row($(this).closest('tr')).data();
//         if (rowData) {
//             // Lấy dữ liệu từ hàng
//             let id = rowData.id;
//             let ten = rowData.ten;
//             let link = rowData.link;
//             let deleted = rowData.deleted;
//
//             // Binding dữ liệu vào modal
//             $('#edit-id').val(id);
//             $('#edit-ten').val(ten);
//             $('#edit-link').val(link);
//             $('#edit-deleted').html(`
//                 <option value="false">Hoạt động</option>
//                 <option ${deleted ? "selected" : ""}  value="true">Đã xóa</option>`)
//             if (deleted) {
//                 $('#edit-deleted').parent(".form-group").removeClass("d-none");
//
//             } else {
//                 $('#edit-deleted').parent(".form-group").addClass("d-none");
//
//             }
//             // Hiển thị modal
//             $('#modal-edit').modal('show');
//             existingNames = $('#dataTable').DataTable().column(1).data().toArray().filter(elm => elm !== ten).map(name => name.toLowerCase().trim());
//             console.log(existingNames)
//
//         }
//
//     });
//     //Validate
//     $('#edit-ten').off();
//     $('#edit-ten').on('input paste', function () {
//         let ten = $('#edit-ten').val().toLowerCase().trim();
//         if (existingNames.includes(ten) || ten.trim() === "") {
//             $('#edit-ten').addClass('is-invalid');
//             $('#edit-ten').removeClass('is-valid');
//         } else {
//             $('#edit-ten').removeClass('is-invalid');
//             $('#edit-ten').addClass('is-valid');
//         }
//     });
//     //Core
//     $('#form-edit').submit(function (event) {
//         event.preventDefault(); // Ngăn chặn submit mặc định của form
//         const form = $(this);
//         if (!form[0].checkValidity()) {
//             // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
//             event.stopPropagation();
//             form.addClass('was-validated');
//         } else {
//             let formData = {
//                 id: $('#edit-id').val(),
//                 ten: $('#edit-ten').val(),
//                 link: $('#edit-link').val(),
//                 deleted: $('#edit-deleted').val()
//             }
//             // Nếu form hợp lệ, gửi dữ liệu form lên server
//             $.ajax({
//                 url: apiURL, // Thay 'URL_API' bằng đường dẫn của API của bạn
//                 method: 'POST', // Phương thức HTTP
//                 data: JSON.stringify(formData),
//                 contentType: 'application/json',
//                 success: function (response) {
//                     Toast.fire({
//                         icon: "success",
//                         title: "Cập nhật thành công"
//                     })
//                     $('#modal-edit').modal('hide');
//                     reloadDataTable();
//                     console.log(response);
//                 },
//                 error: function (xhr, status, error) {
//                     Toast.fire({
//                         icon: "error",
//                         title: "Cập nhật thất bại"
//                     });
//                     reloadDataTable();
//                     console.log(response);
//                     console.error(xhr.responseText);
//                 }
//             });
//
//         }
//     });
// })
//
// //Delete
// $(document).ready(() => {
//     $('#dataTable tbody').on('click', '.btn-delete', function () {
//         let rowData = $('#dataTable').DataTable().row($(this).closest('tr')).data();
//         if (rowData) {
//             // Lấy dữ liệu từ hàng
//             let id = rowData.id;
//             let ten = rowData.ten;
//             // Hiển thị modal
//
//             Swal.fire({
//                 title: `Bạn chắc chắn muốn xóa ${ten}?`,
//                 icon: "warning",
//                 showCancelButton: true,
//                 confirmButtonColor: "#3085d6",
//                 cancelButtonColor: "#d33",
//                 cancelButtonText: "Hủy",
//                 confirmButtonText: "Xác nhận"
//             }).then((result) => {
//                 if (result.isConfirmed) {
//                     $.ajax({
//                         url: apiURL + '?id=' + id,
//                         type: 'DELETE',
//                         success: function () {
//                             Toast.fire({
//                                 icon: "success",
//                                 title: "Xóa thành công"
//                             })
//                             reloadDataTable();
//                         },
//                         error: () => {
//                             Toast.fire({
//                                 icon: "error",
//                                 title: "Xóa thất bại"
//                             })
//                             reloadDataTable();
//                         }
//
//                     })
//                 }
//             });
//
//
//         }
//
//     });
//
//
// })
//
//
// //Revert
// $(document).ready(() => {
//     $('#dataTable tbody').on('click', '.btn-revert', function () {
//         let rowData = $('#dataTable').DataTable().row($(this).closest('tr')).data();
//         if (rowData) {
//             // Lấy dữ liệu từ hàng
//             let id = rowData.id;
//             let ten = rowData.ten;
//             // Hiển thị modal
//             Swal.fire({
//                 title: `Bạn chắc chắn muốn khôi phục ${ten}?`,
//                 icon: "warning",
//                 showCancelButton: true,
//                 confirmButtonColor: "#3085d6",
//                 cancelButtonColor: "#d33",
//                 cancelButtonText: "Hủy",
//                 confirmButtonText: "Xác nhận"
//             }).then((result) => {
//                 if (result.isConfirmed) {
//                     $.ajax({
//                         url: apiURL + '/revert?id=' + id,
//                         type: 'POST',
//                         success: function () {
//                             Toast.fire({
//                                 icon: "success",
//                                 title: "Khôi phục thành công"
//                             })
//                             reloadDataTable();
//                         },
//                         error: () => {
//                             Toast.fire({
//                                 icon: "error",
//                                 title: "Khôi phục thất bại"
//                             })
//                             reloadDataTable();
//                         }
//                     })
//                 }
//             });
//
//
//
//
//         }
//
//     });
//
//
// })
//
//Filter
$(document).ready(function () {
    // Khởi tạo DataTable
    let table = $('#dataTable').DataTable();

    // Lắng nghe sự kiện thay đổi của select option
    $('#modal-filter').on('change', "select", function () {
        let filterTrangThai = $('#filter-trang-thai').val();
        let filterSeries = $('#filter-series').val();
        let filterMauSac = $('#filter-mau-sac').val();
        let filterRom = $('#filter-rom').val();


        if (filterSeries) {
            let regex = "(" + filterSeries.join("|") + ")"
            console.log(regex)
            table.column(2).search('^' + regex + '$', true, false);
        } else {
            // Xóa bộ lọc nếu không có gì được chọn
            table.column(2).search('');
        }
        if (filterRom) {
            let regex = "(" + filterRom.join("|") + ")"
            console.log(regex)
            table.column(4).search('^' + regex + '$', true, false);
        } else {
            // Xóa bộ lọc nếu không có gì được chọn
            table.column(4).search('');
        }
        if (filterMauSac) {
            // let regex="("+filterMauSac.join("|")+")"
            var regex = new RegExp('\b' + "Xám" + '\b', 'i');
            console.log("Ôk1")
            // Lọc dữ liệu trong cột thứ hai (index 1)
            table.column(3).search(function (d) {
                console.log("OK")
                return true;
            }).draw();

        } else {
            // Xóa bộ lọc nếu không có gì được chọn
            table.column(3).search('');
        }


        table.draw();
    });
});
//
// //Import file
// $(document).ready(function () {
//     //Event
//     $('#btn-import').on("click", (event) => {
//         $('#import-file').click();
//         $('#import-file').val("");
//     })
//     //Core
//     $('#import-file').on("change", (event) => {
//         dataToJson(event)
//             .then(jsonData => validate_import(jsonData)
//                 .then(
//                     jsonData => {
//                         Swal.fire({
//                             title: "Bạn chắc chắn chứ ?",
//                             text: "Sau khi import bạn sẽ không thể quay lại!",
//                             icon: "warning",
//                             showCancelButton: true,
//                             confirmButtonColor: "#3085d6",
//                             cancelButtonColor: "#d33",
//                             cancelButtonText: "Hủy",
//                             confirmButtonText: "Xác nhận"
//                         }).then((result) => {
//                             if (result.isConfirmed) {
//                                 import_excel(jsonData);
//
//                             }
//                         });
//
//                     }
//                 )
//             )
//             .catch(e =>
//                 showErrorToast("Lỗi: " + e)
//             )
//     })
//     //Validate
//     const validate_import = (jsonData) => {
//         return new Promise((resolve, reject) => {
//             for (let obj of jsonData) {
//                 // Kiểm tra field
//
//                 if (!obj.hasOwnProperty('ten')) {
//                     reject("Lỗi định dạng: Thiếu tên sản phẩm")
//                 }
//             }
//             resolve(jsonData)
//         })
//     }
//     //Import
//     const import_excel = (jsonData) => {
//         $.ajax({
//             url: apiURL + "/import-excel",
//             type: 'POST',
//             contentType: "application/json; charset=utf-8",
//             data: JSON.stringify(jsonData),
//             success: function (data) {
//                 Toast.fire({
//                     icon: "success",
//                     title: "Nhập excel thành công"
//                 })
//                 Toast.fire({
//                     icon: "success",
//                     title: "Đã thay đổi " + data.length + " bản ghi"
//                 })
//                 reloadDataTable();
//             },
//             error: (jqXHR, textStatus, errorThrown) => {
//                 showErrorToast(`Lỗi: ${textStatus} - ${errorThrown}`);
//                 reloadDataTable();
//             }
//
//
//         })
//     }
//
// });
//
