const apiURL = "/api/v1/san-pham-chi-tiet/mau-sac";


function loadData() {
  $('#dataTable').DataTable( {
    "ajax": {
      "url": apiURL,
      "cache": true,
      "dataSrc":""
    },
    "columns": [

      { "data": "id" },
      { "data": "ma" },
      { "data": "ten" },
      { "data": "trangThai",
        "render": function (data, type, row) {
          return data?"Hoạt động":"Ngưng hoạt động"
        }
      },
      {
        "data": null,
        "render": function(data, type, row) {
          return '<div class="d-flex justify-content-end"><button type="button" class="btn btn-sm btn-primary mr-3 btn-edit">Chỉnh sửa</button><button type="button" class="btn btn-sm btn-danger btn-delete">Xóa</button></div>';
        },
        "orderable": false

      }
    ]
  });
}
$(document).ready(loadData()) ;
$('#reload_table').click(function () {
  $('#dataTable').DataTable().ajax.reload();
});


var table = $('#dataTable').DataTable()
$('#dataTable tbody').on('click', '.btn-edit', function () {
  var rowData = table.row($(this).closest('tr')).data();
  if (rowData) {
    // Lấy dữ liệu từ hàng
    let id = rowData.id;
    let ma = rowData.ma;
    let ten = rowData.ten;
    let trangThai=rowData.trangThai;

    // Binding dữ liệu vào modal
    $('#edit_Idms').val(id);
    $('#edit_mms').val(ma);
    $('#edit_tms').val(ten);
    $('#edit_ttms').html(`
    <option  disabled value="">Chọn...</option>
    <option value="true">Hoạt động</option>
    <option ${!trangThai?"selected":""}  value="false">Ngưng hoạt động</option>
    `)

    // Hiển thị modal
    $('#EditModal').modal('show');

  }
});
$('#dataTable tbody').on('click', '.btn-delete', function () {
  var rowData = table.row($(this).closest('tr')).data();
  if (rowData) {
    // Lấy dữ liệu từ hàng
    let id = rowData.id;
    let ma = rowData.ma;


    // Binding dữ liệu vào modal
    $('#edit_Idms').val(id);
    $('#edit_mms').val(ma);
    $('#edit_tms').val(ten);
    $('#edit_ttms').html(`
    <option  disabled value="">Chọn...</option>
    <option value="true">Hoạt động</option>
    <option ${!trangThai?"selected":""}  value="false">Ngưng hoạt động</option>
    `)

    // Hiển thị modal
    $('#EditModal').modal('show');

  }
});

$(document).ready(function () {
  // Fetch all the forms we want to apply custom Bootstrap validation styles to
  var forms = $('#from_edit')

  // Loop over them and prevent submission
  var validation = Array.prototype.filter.call(forms, function(form) {
    form.addEventListener('submit', function(event) {
      if (form.checkValidity() === false) {
        event.preventDefault();
        event.stopPropagation();
      }else {
        event.preventDefault();
        $.ajax({
          url: apiURL,
          type: 'POST',
          data:{
            "id": rowData.id,
            "ma": rowData.ma,
            "ten": rowData.ten,
            "trangThai":rowData.trangThai
          },
          success:function (data) {
            alert(data);
            loadData();
          }
        })
      }
      form.classList.add('was-validated');
    }, false);
  });
});

