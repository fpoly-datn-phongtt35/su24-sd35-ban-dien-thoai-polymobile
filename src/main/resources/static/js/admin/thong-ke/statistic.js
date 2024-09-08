// // dependency
// Top
// <link href="/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet"/>
// <script src="/vendor/sweetalert2/sweetalert2.min.js"></script>
// Bottom
// <script src="/vendor/datatables/jquery.dataTables.min.js"></script>
// <script src="/vendor/datatables/dataTables.bootstrap4.min.js"></script>

const apiURL = "/api/v1/san-pham";
let _existingNames;
const TrangThai=new Map([
    ['IN_STOCK',"Còn hàng"],
    ['OUT_OF_STOCK',"Hết hàng"],
    ['TEMPORARILY_OUT_OF_STOCK',"Hết hàng tạm thời"],
    ['COMING_SOON',"Sắp ra mắt"],
    ['DISCONTINUED',"Không kinh doanh"]
])
var lstSeries, lstMauSac;
// fill data to select option

$(document).ready(function () {

})

function loadFilter() {
    //Mau sac
    $.ajax({
            url: "/api/v1/admin/data-list-add-san-pham/mau-sac",
            success: function (response) {
                lstMauSac = response.results;
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
                lstSeries = response.results;
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
            "url": apiURL + "-top-revenue",
            "cache": true,
            "dataSrc": ""
        },

        "columns": [

            {"data": "id", "name": "id", "visible": false},
            {
                "data": "anhSanPham",
                "name": "anhSanPham",
                "orderable": false,
                "searchable": false,


                "render": function (data) {

                    if (data == null || data.length == 0)
                        return ""
                    return `<img src="${data}" class="" style="width: 70px; height:100px;" alt="...">`

                },
            },
            {"data": "tenSanPham", "name": "tenSanPham"},
            {"data": "soLuong", "name": "soLuong"},
            {"data": "trangThai", "name": "trangThai",
                "render": function (data) {
                    return TrangThai.get(data)
                }
            },

            {"data": "formattedRevenue", "name": "formattedRevenue"},

        ]
    });
}


const reloadDataTable = () => {
    $('#dataTable').DataTable().ajax.reload();
}

//config dataTable
$(document).ready(() => {
        loadData();
        loadFilter()

    }
);

//Reload event
$(document).ready(() => {
    $('#btn-reload').on("click", () => {
        reloadDataTable()
    })
})

//Filter
$(document).ready(function () {
    // Khởi tạo DataTable

    let table = $('#dataTable').DataTable();
    let filterCount = 1;

    let filterTrangThai = $('#filter-trang-thai').val();
    let filterSeries = $('#filter-series').val();
    let filterMauSac = $('#filter-mau-sac').val();
    let filterRom = $('#filter-rom').val();
    let filterSoLuong = $('#rangeSlider').val();
    let debounceTimeout1, debounceTimeout2;

    //load filter, init default val
    $('#modal-filter').on('show.bs.modal', function (event) {
        //So Luong
        filterCount = 1;
        let maxSoLuong = 0;
        maxSoLuong = Math.max.apply(Math, $('#dataTable').DataTable().column(7).data().toArray());
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
        $('#filter-confirm').text(`Xác nhận`)

        filterTrangThai = $('#filter-trang-thai').val();
        filterSeries = $('#filter-series').val();
        filterMauSac = $('#filter-mau-sac').val();
        filterRom = $('#filter-rom').val();
        filterSoLuong = $('#rangeSlider').val();

    })


    function applyFilterField() {
        let filterTrangThai = $('#filter-trang-thai').val();
        let filterSoLuong = $('#rangeSlider').val();
        let currentRowsCount = table.rows({search: "removed"}).count();
        console.log('applyFilter')


        console.log(filterSoLuong)

        //Hiển thị nếu cs thay đổi
        if (table.rows({search: "removed"}).count() != currentRowsCount) {
            $('#filter-confirm').text(`Áp dụng (${table.rows({search: "applied"}).count()})`)
        }


    }

    function applyFilterRanger() {
        $.fn.dataTable.ext.search.pop();
        let currentFilterSoLuong = $('#rangeSlider').val();

        console.log('applyFilterRanger')


        $.fn.dataTable.ext.search.push(function (searchStr, data, index) {
            let min = parseInt(currentFilterSoLuong.split(';')[0]);
            let max = parseInt(currentFilterSoLuong.split(';')[1]);
            let soLuong = parseFloat(data[7]) || 0; // use data for the soLuong column

            if (
                (isNaN(min) && isNaN(max)) ||
                (isNaN(min) && soLuong <= max) ||
                (min <= soLuong && isNaN(max)) ||
                (min <= soLuong && soLuong <= max)
            ) {
                return true;
            }

            return false;
        });
        table.draw();
        console.log(currentFilterSoLuong)
        $('#filter-confirm').text(`Áp dụng (${table.rows({search: "applied"}).count()})`)
        $.fn.dataTable.ext.search.pop();
        $.fn.dataTable.ext.search.push(function (searchStr, data, index) {
            let min = parseInt(filterSoLuong.split(';')[0]);
            let max = parseInt(filterSoLuong.split(';')[1]);
            let soLuong = parseFloat(data[7]) || 0; // use data for the soLuong column

            if (
                (isNaN(min) && isNaN(max)) ||
                (isNaN(min) && soLuong <= max) ||
                (min <= soLuong && isNaN(max)) ||
                (min <= soLuong && soLuong <= max)
            ) {
                return true;
            }

            return false;
        });
        table.draw();
        $.fn.dataTable.ext.search.pop();
        $.fn.dataTable.ext.search.push(function (searchStr, data, index) {
            let min = parseInt(currentFilterSoLuong.split(';')[0]);
            let max = parseInt(currentFilterSoLuong.split(';')[1]);
            let soLuong = parseFloat(data[7]) || 0; // use data for the soLuong column

            if (
                (isNaN(min) && isNaN(max)) ||
                (isNaN(min) && soLuong <= max) ||
                (min <= soLuong && isNaN(max)) ||
                (min <= soLuong && soLuong <= max)
            ) {
                return true;
            }

            return false;
        });

    }

    // Lắng nghe sự kiện thay đổi của select option

    $('#modal-filter').on('change', "select", () => {
        clearTimeout(debounceTimeout1)
        debounceTimeout1 = setTimeout(applyFilterField, 10);
    });
    $('#modal-filter').on('change', "input", () => {
        clearTimeout(debounceTimeout2)
        debounceTimeout2 = setTimeout(applyFilterRanger, 500);
    });
    let flagEventClose = true;
    $('#modal-filter').on('hide.bs.modal', function () {
        if (flagEventClose) {
            console.log("default-filter")
            $('#filter-trang-thai').val(filterTrangThai).trigger('change');
            $('#filter-series').val(filterSeries).trigger('change');
            $('#filter-mau-sac').val(filterMauSac).trigger('change');
            $('#filter-rom').val(filterRom).trigger('change');
            console.log(filterSoLuong + "filterSoLuong");
            $('#rangeSlider').data("ionRangeSlider").update({
                from: filterSoLuong.split(";")[0],
                to: filterSoLuong.split(";")[1]
            })
            table.draw();

        }
    })
    $('#modal-filter').on('click', '#filter-confirm', function () {
        flagEventClose = false
        table.draw();
        $('#badge-filter').text(`${filterCount}`)
        $('#modal-filter').modal('hide')
        flagEventClose = true;
    })
    $('#modal-filter').on('click', '#filter-clear', function () {


        console.log("clear filter");
        $('#badge-filter').text('')

        filterTrangThai = []
        filterSeries = []
        filterMauSac = []
        filterRom = []
        let maxSoLuong = 0;
        maxSoLuong = Math.max.apply(Math, $('#dataTable').DataTable().column(7).data().toArray());
        maxSoLuong = Math.ceil(maxSoLuong / 100) * 100
        filterSoLuong = `$0;${maxSoLuong}`

    })

});