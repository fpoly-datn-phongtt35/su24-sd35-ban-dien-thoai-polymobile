// const apiURL = "/api/v1/san-pham-chi-tiet/mau-sac";
//
// function loadData() {
//     $('#dataTable').DataTable({
//
//         "language": {
//             "sProcessing": "Đang xử lý...",
//             "sLengthMenu": "Hiển thị _MENU_ bản ghi",
//             "sZeroRecords": "Không tìm thấy dữ liệu",
//             "sInfo": "Hiển thị _START_ đến _END_ của _TOTAL_ bản ghi",
//             "sInfoEmpty": "Hiển thị 0 đến 0 của 0 bản ghi",
//             "sInfoFiltered": "(được lọc từ _MAX_ bản ghi)",
//             "sInfoPostFix": "",
//             "sSearch": "Tìm kiếm:",
//             "sUrl": "",
//             "oPaginate": {
//                 "sFirst": "Đầu",
//                 "sPrevious": "Trước",
//                 "sNext": "Tiếp",
//                 "sLast": "Cuối"
//             }
//         },
//         "ajax": {
//             "url": apiURL,
//             "cache": true,
//             "dataSrc": ""
//         },
//         "columns": [
//
//             {"data": "id"},
//             {"data": "ma"},
//             {"data": "ten"},
//             {
//                 "data": "trangThai",
//                 "render": function (data, type, row) {
//                     return data ? "Hoạt động" : "Ngưng hoạt động"
//                 }
//             },
//             {
//                 "data": null,
//                 "render": function (data, type, row) {
//                     return '<div class="d-flex justify-content-end"><button type="button" class="btn btn-sm btn-primary mr-3 btn-edit">Chỉnh sửa</button><button type="button" class="btn btn-sm btn-danger btn-delete">Xóa</button></div>';
//                 },
//                 "orderable": false
//
//             }
//         ]
//     });
// }
//
// const reloadDataTable = () => {
//     $('#dataTable').DataTable().ajax.reload();
// }
// $(document).ready(loadData());
// $('#reload_table').click(function () {
//     reloadDataTable();
// });
//
// //edit evnt
// var existingCodes
// var table = $('#dataTable').DataTable()
// $('#dataTable tbody').on('click', '.btn-edit', function () {
//     clearForm()
//     var rowData = table.row($(this).closest('tr')).data();
//     if (rowData) {
//         // Lấy dữ liệu từ hàng
//         let id = rowData.id;
//         ma = rowData.ma;
//         let ten = rowData.ten;
//         let trangThai = rowData.trangThai;
//
//         // Binding dữ liệu vào modal
//         $('#edit_Idms').val(id);
//         $('#edit_mms').val(ma);
//         $('#edit_tms').val(ten);
//         $('#edit_ttms').html(`
//     <option  disabled value="">Chọn...</option>
//     <option value="true">Hoạt động</option>
//     <option ${!trangThai ? "selected" : ""}  value="false">Ngưng hoạt động</option>
//     `)
//
//         // Hiển thị modal
//         $('#EditModal').modal('show');
//         existingCodes = $('#dataTable').DataTable().column(1).data().toArray().filter(elm => elm !== ma);
//
//         // validate
//     }
// });
// $('#edit_mms').on('input paste', function () {
//     let mms = $('#edit_mms').val();
//     console.log(existingCodes)
//     $('#edit_mms').val(mms.replace(/\s/g, ''));
//     if (existingCodes.includes(mms) || mms.trim() === "") {
//         $('#edit_mms').addClass('is-invalid');
//         $('#edit_mms').removeClass('is-valid');
//
//     } else {
//         $('#edit_mms').removeClass('is-invalid');
//         $('#edit_mms').addClass('is-valid');
//
//     }
//
// });
// //Xóa evnt
// $('#dataTable tbody').on('click', '.btn-delete', function () {
//     var rowData = table.row($(this).closest('tr')).data();
//     if (rowData) {
//         // Lấy dữ liệu từ hàng
//         let id = rowData.id;
//         let ma = rowData.ma;
//         // Hiển thị modal
//
//         $('#modalDelete').modal('show');
//         $('#delete_ma').html(ma);
//         $('#delete_id').html(id);
//
//
//     }
// });
// // core delete
// $('#delete_confirm').click(function () {
//     let id = $('#delete_id').html();
//     hideModal();
//     $.ajax({
//         url: apiURL + '?id=' + id,
//         type: 'DELETE',
//         success: function () {
//             reloadDataTable();
//             showSuccessToast("Xóa thành công !");
//         },
//         error: () => {
//             showErrorToast("Xóa thất bại")
//         }
//     })
//
// });
// // Core edit
// $(document).ready(function () {
//     // Fetch all the forms we want to apply custom Bootstrap validation styles to
//     var forms = $('#from_edit')
//
//     // Loop over them and prevent submission
//     var validation = Array.prototype.filter.call(forms, function (form) {
//         form.addEventListener('submit', function (event) {
//             if (form.checkValidity() === false) {
//                 event.preventDefault();
//                 event.stopPropagation();
//                 showErrorToast("Cập nhật thất bại !")
//             } else {
//                 hideModal();
//                 event.preventDefault();
//                 $.ajax({
//                     url: apiURL,
//                     type: 'POST',
//                     contentType: "application/json; charset=utf-8",
//                     data: JSON.stringify({
//                         "id": $('#edit_Idms').val(),
//                         "ma": $('#edit_mms').val(),
//                         "ten": $('#edit_tms').val(),
//                         "trangThai": $('#edit_ttms').val()
//                     }),
//                     success: function () {
//                         form.classList.remove('was-validated');
//                         showSuccessToast("Cập nhật thành công !");
//                         reloadDataTable();
//                     },
//                     error: () => {
//                         form.classList.remove('was-validated');
//                         showSuccessToast("Cập nhật thất bại !");
//                         reloadDataTable();
//                     }
//                 })
//             }
//             form.classList.add('was-validated');
//
//         }, false);
//     });
// });
//
// // Lọc
// $(document).ready(function () {
//     // Khởi tạo DataTable
//     var table = $('#dataTable').DataTable();
//
//     // Lắng nghe sự kiện thay đổi của select option
//     $('#statusFilter').on('change', function () {
//         var selectedStatus = $(this).val();
//
//         if (selectedStatus) {
//             // Áp dụng bộ lọc theo cột Status
//             table.column(3).search('^' + selectedStatus + '$', true, false).draw();
//         } else {
//             // Xóa bộ lọc nếu không có gì được chọn
//             table.column(3).search('').draw();
//         }
//     });
// });
// //add event
// $(document).ready(() => {
//     $('#btn-add').on('click', () => {
//         clearForm();
//         $('#modal-add').modal('show');
//     })
//     // validate exitst
//     $('#add-ma').on('input paste', function () {
//         let existingCodes = $('#dataTable').DataTable().column(1).data().toArray();
//         let ma = $('#add-ma').val().trim();
//         $('#add-ma').val(ma.replace(/\s/g, ''))
//         if (existingCodes.includes(ma) || ma.trim() === "") {
//             $('#add-ma').addClass('is-invalid');
//             $('#add-ma').removeClass('is-valid');
//         } else {
//             console.log("Sai");
//             console.log(existingCodes)
//             $('#add-ma').removeClass('is-invalid');
//             $('#add-ma').addClass('is-valid');
//
//         }
//
//     });
// })
// //core add
// $(document).ready(function () {
//     // Fetch all the forms we want to apply custom Bootstrap validation styles to
//     let forms = $('#from-add')
//     let validation = Array.prototype.filter.call(forms, function (form) {
//         form.addEventListener('submit', function (event) {
//             if (form.checkValidity() === false || forms.find('.is-invalid').length > 0) {
//                 event.preventDefault();
//                 event.stopPropagation();
//                 showErrorToast("Thêm mới thất bại !")
//             } else {
//                 hideModal();
//                 event.preventDefault();
//                 $.ajax({
//                     url: apiURL,
//                     type: 'PUT',
//                     contentType: "application/json; charset=utf-8",
//                     data: JSON.stringify([{
//                         "ma": $('#add-ma').val().replace('\s', ''),
//                         "ten": $('#add-ten').val(),
//                         "trangThai": $('#add-trangThai').val()
//                     }]),
//                     success: function () {
//                         form.classList.remove('was-validated');
//                         showSuccessToast("Thêm mới thành công !");
//                         reloadDataTable();
//                     },
//                     error: () => {
//                         form.classList.remove('was-validated');
//                         showErrorToast("Thêm mới thất bại !");
//                         reloadDataTable();
//                     }
//
//
//                 })
//             }
//             form.classList.add('was-validated');
//
//         }, false);
//     });
// });
//
// //Import file evnt
// $(document).ready(function () {
//     $('#btn-import').on("click", (event) => {
//         $('#import-file').click();
//         $('#import-file').val("");
//     })
// });
// $(document).ready(function () {
//     $('#import-file').on("change", (event) => {
//         dataToJson(event)
//             .then(
//                jsonData=>validate_import(jsonData).then(
//                    jsonData=>{
//
//                        showConfirm("Bạn chắc chắn muốn import ?","Các bản ghi sẽ được update nếu có trong file !")
//                            .then(result=>{
//                                console.log('User confirmed:', result);
//                                import_excel(jsonData);
//                            })
//                            .catch(reason => {
//                                console.log("Cancel import")
//                            })
//
//                    }
//                )
//             )
//             .catch(e=>
//                 showErrorToast("Lỗi: "+e)
//             )
//     })
// });
//
// const validate_import = (jsonData) => {
//     return new Promise((resolve, reject) => {
//         for (let obj of jsonData) {
//             // Kiểm tra field
//
//             if (!obj.hasOwnProperty('ma')
//                 || !obj.hasOwnProperty('ten')
//                 || !obj.hasOwnProperty('trangThai')
//             ) {
//                 reject("Lỗi định dạng: Thiếu trường")
//             }
//
//         }
//         resolve(jsonData)
//     })
// }
// //Core import
// const import_excel=(jsonData)=>{
//         $.ajax({
//             url: apiURL,
//             type: 'PUT',
//             contentType: "application/json; charset=utf-8",
//             data: JSON.stringify(jsonData),
//             success: function () {
//                 showSuccessToast("Import thành công !");
//                 reloadDataTable();
//             },
//             error: (jqXHR, textStatus, errorThrown) => {
//                 showErrorToast(`Lỗi: ${textStatus} - ${errorThrown}`);
//                 reloadDataTable();
//             }
//
//
//
//     })
// }