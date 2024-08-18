$(document).ready(function () {
    $('#select-san-pham').select2({
        placeholder: "Tìm kiếm theo mã hoặc theo tên",
        ajax: {
            url: '/api/v1/select2/san-pham',
            delay: 500,
            data: function (params) {
                return {
                    searchKey: params.term,
                    page: params.page || 0,
                    pageSize: 10
                };
            },
            cache: true

        },
        closeOnSelect: false


    });

})

$(document).ready(function () {
    $('#btn-add').on('click', function () {
        $('#select-san-pham').select2('data').forEach(data => {
            addToTable(data.id, data.text, 1)
        })
        $('#select-san-pham').val([]).trigger('change')
    })
})

function addToTable(id, name, quantity) {
    function reindex() {
        $('#table-san-pham tbody tr ').find('td:nth-child(1)').each(
            function (index, elem) {
                $(elem).text(index + 1)
            })
    };
    let index = 1
    $('#table-san-pham tbody tr').find('td:nth-child(2)').each(function (index, elem) {
        if (elem.innerText == id) {
            $(elem).closest('tr').remove()
        }

    })
    $('#table-san-pham tbody').append(`
        <tr onclick="showImei($(this))">
            <td>${index}</td>
            <td class="id-spct">${id}</td>
            <td>${name}</td>
            <td class="sl-spct" contenteditable="true">${quantity}</td>
            <td><button class="btn btn-sm btn-danger" onclick="$(this).closest('tr').remove()">Xóa</button></td>

        </tr>
    `)
    reindex();
    showImei()
}

var _mapImei = new Map();

function showImei($row) {
    if (!$row) {
        $row = $('#table-san-pham tbody tr:last-child');
    }
    $row.closest('tbody').find('.active').removeClass('active')
    $row.addClass('active')
    let idSPCT = $row.find('.id-spct').text();
    let soLuong = 1;
    try {
        soLuong = $row.find('.sl-spct').text()
    } catch (e) {
        showErToast("Lỗi", "Kiểm tra số lượng")
        return;
    }
    let data=_mapImei.get(idSPCT);
    if (!data) {
        data=new Array(soLuong);
    }else{
        if(data.length > soLuong){
            data=data.splice(0,soLuong);
        }else if(data.length < soLuong){
            let newArr=new Array(soLuong-data.length)
            data=[...data,...newArr]
        }
    }
    _mapImei.set(idSPCT, data)
    let dataRow = ""
    for (let i = 0; i < data.length; i++) {
        dataRow += `
                <tr id-spct="${idSPCT}">
                   <td style="vertical-align: middle; text-align: center">${i+1}</td>
                   <td> <input class="form-control" type="text" value=""></td>
                </tr>
            `
    }
    _mapImei.get(idSPCT).forEach((imei, index) => {

    })
    $("#table-imei tbody").html(dataRow);
    console.log(dataRow)


}

