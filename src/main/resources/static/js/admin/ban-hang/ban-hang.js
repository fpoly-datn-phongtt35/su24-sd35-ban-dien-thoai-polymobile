function chuyenCheDoNhap(btn) {

    let status = $('#collapseExample').hasClass('show');
    if (status == true) {
        $('#collapseExample').removeClass('show')
    }
    showSuccessToast("Chuyển chế độ nhập", "Đã chuyển sang chế độ nhập " + (status == "false" ? "thường" : "nhanh"))
}
function showSuccessToast(title, message) {
    $('.toast-container').append(`
       <div class="toast hide " role="alert" aria-live="assertive" aria-atomic="true" data-delay="3000">
            <div class="toast-header bg-primary text-light">
                <strong class="me-auto">${title}</strong>
            </div>
            <div class="toast-body ">
                ${message}
            </div>
        </div>
    `);
    $('.toast-container').find('.toast').last().toast('show')
}

function showErrToast(title, message) {
    $('.toast-container').append(`
       <div class="toast hide " role="alert" aria-live="assertive" aria-atomic="true" data-delay="3000">
            <div class="toast-header bg-danger text-light">
                <strong class="me-auto">${title}</strong>
            </div>
            <div class="toast-body ">
                ${message}
            </div>
        </div>
    `);
    $('.toast-container').find('.toast').last().toast('show')
}
function showWarnToast(title, message) {
    $('.toast-container').append(`
       <div class="toast hide " role="alert" aria-live="assertive" aria-atomic="true" data-delay="3000">
            <div class="toast-header bg-warning text-light">
                <strong class="me-auto">${title}</strong>
            </div>
            <div class="toast-body ">
                ${message}
            </div>
        </div>
    `);
    $('.toast-container').find('.toast').last().toast('show')
}


var invoice = {
    sanPhamChiTiet: [
        {
            sanPhamChiTietId: -1,
            sanPhamTen: '',
            soLuong: -1,
            giaBan: -1
        }
    ]
}
//Chứa danh sách toàn bộ sản phẩm đang trong các hóa đơn;
const danhSachSanPhamDaThem = new Map();
//Chứa danh sách các hóa đơn
var _mapInvoice = new Map();
//Chứa danh sách sản phẩm
var _mapProduct = new Map();
//Chứa danh sách sản phẩm đang được chọn
var _mapProductDetail = new Map();
//Sản phẩm chi tiết đang được chọn
var _selectedId = -1;
//Hóa đơn đang được chọn
var _selectedInvoice = -1;
//Map sản phẩm chi tiết theo rom làm key
var _uniqueProductRoms;
//Chứa đuôi của url filter sản phẩm
let _mapParamFilters= new Map();


$(document).ready(() => {


    function indexInvoice() {
        if ($('#nav-invoice').find('button span').length == 0) {
            $('#add-new-invoice').trigger('click');
        }

        $('#nav-invoice').find('button span').each((index, item) => {
            $(item).text(index + 1)
        })
    }

    function deleteInvoice(invoiceID) {
        let $iv = $(`#nav-invoice [invoice-id='${invoiceID}']`);
        let $closestSibling = $iv.parent().next().find('.fa-x').length != 0 ? $iv.parent().next() : $iv.parent().prev();
        $iv.parent().remove();
        indexInvoice();
        _mapInvoice.delete(invoiceID);
        updateDSSPDC();
        selectInvoice($closestSibling.find('.fa-x').attr('invoice-id'));

    }

    function selectInvoice(invoiceId) {
        let $selectInvoice = $(`#nav-invoice .fa-x[invoice-id='${invoiceId}']`);
        $selectInvoice.parent().parent().find('.active').removeClass('active').attr('aria-selected', 'false');
        $selectInvoice.parent().addClass('active').attr('aria-selected', 'true');
        _selectedInvoice = invoiceId.toString();
        loadInvoice();
    }

    //Tạo id cho hóa đơn đầu tiên
    $('#nav-invoice').find('button .fa-x').first().attr('invoice-id', Date.now())
    _selectedInvoice = $('#nav-invoice').find('button .fa-x').first().attr('invoice-id');
    _mapInvoice.set(_selectedInvoice, new Map());

    $('#nav-invoice').on('click', '.fa-x', function () {
        deleteInvoice($(this).attr('invoice-id'));
    });

    $('#add-new-invoice').on('click', function () {
        let invoice_id = Date.now().toString();

        let newInvoice = $(`
            <button class="nav-link" id="nav-profile-tab" data-toggle="tab" data-target="#nav-profile" type="button" role="tab" aria-controls="nav-profile" aria-selected="false">
                Hóa đơn <span>1</span> <i invoice-id="${invoice_id}" class="fa fa-x ms-2 p-1 rounded-circle" style="font-size: 10px"></i>
            </button>
        `)
        $(this).before(newInvoice);
        _mapInvoice.set(invoice_id, new Map());
        selectInvoice(invoice_id);

        //Xóa hóa đơn
        newInvoice.on('click', '.fa-x', function () {
            deleteInvoice($(this).attr('invoice-id'));
        })
        indexInvoice()
        newInvoice.on('click', function () {
            selectInvoice($(this).find('.fa-x').attr('invoice-id'));
        })
    })
    $('#nav-invoice').find('button.nav-link').on('click', function () {
        selectInvoice($(this).find('.fa-x').attr('invoice-id'));
    })


})
$(document).ready(() => {
    $('#hd-khach-hang').select2({
        placeholder: "Chọn 1 khách hàng",
        ajax: {
            url: '/api/v1/sale/customer',
            delay: 500,
            data: function (params) {
                return {
                    code: params.term,
                    page: params.page || 0,
                    pageSize: 10
                };
            }

        },
        maximumSelectionLength: 1
    });
    $('#hd-voucher').select2({
        placeholder: "Chọn mã giảm giá",
        ajax: {
            url: '/api/v1/sale/promotion',
            delay: 500,
            data: function (params) {
                return {
                    code: params.term,
                    page: params.page || 0,
                    pageSize: 10
                };
            }

        },
        maximumSelectionLength: 1


    });
    $.get('/api/v1/admin/data-list-add-san-pham/series').done(dataSeries=>{
        $('#filter-series').select2({
            placeholder: "Tất cả",
            data:  dataSeries.results,
            closeOnSelect:false
        });
    })

    $('#select-san-pham').select2({
        placeholder: "Chọn khách hàng (F4)"
    });


})
//Filter
$(document).ready(()=>{
    $.get('/api/v1/sale/product')
        .done((data) => {
            let mapData = new Map(data.map(d => [d.id, d]));
            fillDataToTableSanPham(mapData)
            _mapProduct = mapData
        })


    let timeout;
    $('#input-search').on('input', function () {
        let searchKey = $(this).val();
        clearTimeout(timeout);
        _mapParamFilters.set('searchKey',searchKey)
        timeout = setTimeout(applyFilter(), 500); // Thay đổi giá trị này đ
    })
    $('#filter-series').on('change', function () {
        if($(this).val().length>0) {
            $(this).val().forEach(seriesId=>{

            })
        }
    })

})


function applyFilter() {
    let suffixUrl='';
    _mapParamFilters.forEach((value, key) => {
        if(!value||value!='')
        suffixUrl+=`${key}=${value}&`
    })
    if(suffixUrl.length>0){
        suffixUrl.substring(0,suffixUrl.length-2);
    }
    $.get(`/api/v1/sale/product?${suffixUrl}`)
        .done((data) => {
            let mapData = new Map(data.map(d => [d.id, d]));
            fillDataToTableSanPham(mapData)
            _mapProduct = mapData
        })
}
function fillDataToTableSanPham(mapData) {
    let dataSP = '';
    mapData.forEach((sp, id) => {
        let disable = (sp.trangThai != 'IN_STOCK') ? "disable" : "";
        let outOfStock = (sp.soLuong < 1) ? "out-of-stock" : "";
        dataSP += `
                <div sp-id="${sp.id}" class="col-4 p-4 d-flex justify-content-between product ${outOfStock} ${disable}">
                    <img class="w-25" src="${sp.anhUrl}">
                    <div class="ms-3">
                        <span class="w-50" style="font-size: 12px">${sp.tenSanPham}</span><br>
                        <span class="w-50" style="font-size: 12px">Số lượng: ${sp.soLuong}</span>
                    </div>
                </div>
                `
    })
    let $dataSP = $(dataSP);
    $('#tbl-san-pham').html($dataSP)


    $('#tbl-san-pham').on('click', '.product:not(".disable")', function () {
        loadToOffcanvas($(this).attr('sp-id'));
    })
}

function toCurrency(Num) {
    Num = Num <= 0 ? 0 : Num;
    return (numeral(Num).format('0,0') + ' VND').replace(/,/g, ".")
}

function textDD_MM_YYYYtoDate(stringDate) {
    return new Date(stringDate.split('-').reverse().join('-'));
}

function loadToOffcanvas(idSp) {
    idSp = parseInt(idSp)
    let $canvas = $('#sp-add-to-cart');
    $canvas.offcanvas('show');
    $canvas.find('#detail-sp').hide();
    $canvas.find('.spinner-grow').show();
    updateDSSPDC()
    $.get('/api/v1/sale/product/' + idSp).then(function (sanPham) {
        _uniqueProductRoms = new Map();
        sanPham.sanPhamChiTiet.forEach(spct => {

            _mapProductDetail.set(spct.id.toString(), {
                sanPhamChiTietId: spct.id,
                sanPhamTen: sanPham.tenSanPham + " " + spct.rom + " " + spct.mauSac.ten,
                soLuong: spct.soLuong - (danhSachSanPhamDaThem.get(spct.id.toString()) || 0),
                giaBan: spct.dotGiamGia.donvi == '%' ? spct.giaBan - spct.giaBan * spct.dotGiamGia.giaTriGiam / 100 : spct.giaBan - spct.dotGiamGia.giaTriGiam,
                rom: spct.rom,
                mauSacTen: spct.mauSac.ten
            });

            if (_uniqueProductRoms.get(spct.rom) == undefined) {
                _uniqueProductRoms.set(spct.rom, [spct]);
            } else {
                _uniqueProductRoms.set(spct.rom, [..._uniqueProductRoms.get(spct.rom), spct]);
            }

        })
        _uniqueProductRoms = new Map([..._uniqueProductRoms.entries()].sort((a, b) => a[0].replace(/1TB/g, '6').localeCompare(b[0].replace(/1TB/g, '6'))))
        _uniqueProductRoms.forEach((value, key) => {
            value.sort((a, b) => a.giaBan > b.giaBan ? -1 : 0);
            let mapVal = new Map(value.map(i => [i.mauSac.ma, i]));
            _uniqueProductRoms.set(key, mapVal);
        })

        let romBtnHtml = ''
        _uniqueProductRoms.forEach((value, key) => {
            romBtnHtml += `
                <input type="radio" class="btn-check" name="selected-rom" id='${key}' autocomplete="off"/>
                <label class="btn btn-secondary" for="${key}">${key}</label>
                          
`
        })
        $('#container-btn-rom').find('.btn-group').html(romBtnHtml)
        $('#container-btn-rom').on('click', 'input[name="selected-rom"]', function () {
            let mauSacBtnHtml = '';
            let rom = $(this).attr('id');
            _uniqueProductRoms.get(rom).forEach((spct, maMauSac) => {

                mauSacBtnHtml += `

                <input type="radio" class="btn-check" name="selected-mau-sac"  rom-id="${rom}" id='${maMauSac}' spct-id="${spct.id}" autocomplete="off"/>
                <label class="btn btn-outline-secondary rounded-circle mx-2" style="width: 30px;height: 30px; background-color: ${maMauSac}" for="${maMauSac}"></label>
               
`
            })
            $('#container-btn-mau-sac').find('.btn-group').html(mauSacBtnHtml);
            $('#container-btn-mau-sac').on('click', 'input[name="selected-mau-sac"]', function () {
                let rom = $(this).attr('rom-id');
                let mauSac = $(this).attr('id');
                let spct = _uniqueProductRoms.get(rom).get(mauSac);
                $('#ten-mau-sac').html(spct.mauSac.ten);


                // khuyenMai
                {
                    let khuyenMaiHtml = '';
                    spct.khuyenMai.forEach(km => {
                        let status = (new Date() > textDD_MM_YYYYtoDate(km.thoiGianBatDau) && new Date() < textDD_MM_YYYYtoDate(km.thoiGianKetThuc)) ? "" : "(Đã kết thúc)";
                        if (!km.deleted) {
                            khuyenMaiHtml += `
                            <li><h6 class="h6 d-inline ${status == '(Đã kết thúc)' ? 'text-secondary' : 'text-light'}">${status} ${km.ten}</h6><a style="display: inline;font-size: 12px;" href="${km.link}" target="_blank"> (Xem chi tiết)</a></li>
                        `
                        }
                    })
                    if (khuyenMaiHtml == '') {
                        $('.khuyen-mai-ap-dung').parent().hide();
                    } else {
                        $('.khuyen-mai-ap-dung').parent().show();

                        $('.khuyen-mai-ap-dung').html(khuyenMaiHtml);
                    }
                }

                //dot giam gia
                {
                    let giaSanPhamHtml = '';
                    giaSanPhamHtml = `
                    <div class="p-0 d-flex align-items-end">
                    <h5 class="me-3">Giá gốc:</h5><del class="h5 mt-2 price text-15 text-th">${toCurrency(spct.giaBan)}
                        
                    </del>
                    <span class="ml-1"> - ${spct.dotGiamGia.donvi == '%' ? spct.dotGiamGia.giaTriGiam + '%' : toCurrency(spct.dotGiamGia.giaTriGiam)}</span>
                    
                    </div>
                     <div class="p-0 d-flex align-items-end">
                        <h5 class="me-3 p-0 m-0">Giá khuyến mại:</h5><div class="p-0"><span class="h5 mt-2 price font-weight-bold">${toCurrency(spct.dotGiamGia.donvi == '%' ? spct.giaBan - spct.giaBan * spct.dotGiamGia.giaTriGiam / 100 : spct.giaBan - spct.dotGiamGia.giaTriGiam)}</span></div>
                    </div>
`
                    $('#sp-gia').html(giaSanPhamHtml)
                }

                _selectedId = $(this).attr('spct-id');
                $('#sp-add-to-cart').find('[name="so-luong"]').html("Số lượng sản phẩm: <span></span>")
                $canvas.find('[name="so-luong"] span').text(_mapProductDetail.get(_selectedId.toString()).soLuong)
                console.log('selected id' + _selectedId);
            })

        });


        $canvas.find('.spinner-grow').hide();
        $canvas.find('#detail-sp').show();
        $('#container-btn-rom').find('input[name="selected-rom"]').first().trigger('click');
        $('#container-btn-mau-sac').find('input[name="selected-mau-sac"]').first().trigger('click');

    })
    $canvas.find('.offcanvas-title').text(_mapProduct.get(idSp).tenSanPham)
    $canvas.find('[name="so-luong"] span').text(_mapProduct.get(idSp).soLuong)
}

function deleteInvoice(invoiceId) {
    _mapInvoice.delete(invoiceId)

}

function loadInvoice() {
    let dataRow = '';
    let count = 1;
    _mapInvoice.forEach((value, key) => {
        if ($(`#nav-invoice [invoice-id='${key}']`).length == 0) {
            deleteInvoice(key)
        }
    })
    let revertMap = new Map([...(_mapInvoice.get(_selectedInvoice) || [])].reverse())
    revertMap.forEach((spct, spctId) => {
        dataRow += `
            <tr class="bg-white rounded-lg">
                <td>${count++}</td>
                <td><i spct-id="${spctId}" class="fa-solid fa-trash-can"></i></i></td>
                <td>${spctId}</td>
                <td>${spct.sanPhamTen}</td>
                <td><i spct-id="${spctId}" class="fa-solid fa-minus"></i> ${spct.soLuong} <i spct-id="${spctId}" class="fa-solid fa-plus"></i></td>
                <td>${toCurrency(spct.giaBan)}</td>
                <td>${toCurrency(spct.giaBan * spct.soLuong)}</td>
                <td><i class="fa-solid fa-ellipsis-vertical"></i></td>
            </tr>
        `
    })
    $('#tbl-invoice').find('tbody').html(dataRow)

    $('#tbl-invoice').find('.fa-plus').on('click', function () {
        let spctId=$(this).attr('spct-id');
        addToCart(spctId,1,"")
    })
    $('#tbl-invoice').find('.fa-minus').on('click', function () {
        let spctId = $(this).attr('spct-id');
        removeFromCart(spctId,-1,"")
    })



    $('#tbl-invoice').find('.fa-trash-can').on('click', function () {
        let spctId = $(this).attr('spct-id');
        _mapInvoice.get(_selectedInvoice).delete(spctId);
        let soLuong = parseInt($(this).closest('tr').find(':nth-child(4)').text());
        danhSachSanPhamDaThem.set(spctId, danhSachSanPhamDaThem.get(spctId) - soLuong)
        this.closest('tr').remove();
    })

}


function updateDSSPDC() {
    danhSachSanPhamDaThem.clear();
    _mapInvoice.forEach((details, id) => {
        details.forEach(detail => {
            if (danhSachSanPhamDaThem.has(detail.sanPhamChiTietId)) {
                let sl = (danhSachSanPhamDaThem.get(detail.sanPhamChiTietId) + detail.soLuong);
                danhSachSanPhamDaThem.set(detail.sanPhamChiTietId, sl);
            }
            else
            danhSachSanPhamDaThem.set(detail.sanPhamChiTietId, detail.soLuong);
        })
    })
}

function removeFromCart(spctId, soLuongThem, tenSanPham) {

    let mapSpct = _mapInvoice.get(_selectedInvoice);
    if (danhSachSanPhamDaThem.get(spctId)) {
        danhSachSanPhamDaThem.set(spctId, (danhSachSanPhamDaThem.get(spctId) + soLuongThem));
    }

    _mapProductDetail.get(spctId).soLuong -= soLuongThem;

    //Kiểm tra tồn tại sản phẩm
    if (mapSpct.get(spctId)) {
        let spTemp = mapSpct.get(spctId);
        spTemp.soLuong += soLuongThem;
        mapSpct.set(spctId, spTemp);
    }
    if(mapSpct.get(spctId).soLuong<1){
        mapSpct.delete(spctId);
    }
    $('#sp-add-to-cart').find('[name="so-luong"]').html("Số lượng sản phẩm: <span></span>")
    $('#sp-add-to-cart').find('[name="so-luong"] span').text(_mapProductDetail.get(spctId).soLuong)
    _mapInvoice.set(_selectedInvoice, mapSpct);
    console.log(_mapInvoice);
    loadInvoice();
}


function addToCart(spctId, soLuongThem, tenSanPham) {
    //Lấy danh sách các sản phẩm của hóa đơn hiện tại
    let mapSpct = _mapInvoice.get(_selectedInvoice);
    //Kiểm tra tồn tại danh sách sản phẩm
    if (mapSpct) {
    } else {
        mapSpct = new Map();
    }
    if (_mapProductDetail.get(spctId).soLuong < soLuongThem) {
        showWarnToast("Lỗi", "Đã vượt quá số lượng tồn kho bạn cần bổ sung sản phẩm này");
    }
    if (danhSachSanPhamDaThem.get(spctId)) {
        danhSachSanPhamDaThem.set(spctId, (danhSachSanPhamDaThem.get(spctId) + soLuongThem));
    } else {
        danhSachSanPhamDaThem.set(spctId, soLuongThem);
    }

    _mapProductDetail.get(spctId).soLuong -= soLuongThem;

    //Kiểm tra tồn tại sản phẩm
    if (mapSpct.get(spctId)) {
        let spTemp = mapSpct.get(spctId);

        spTemp.soLuong += soLuongThem;


        mapSpct.set(spctId, spTemp);
    } else {
        let spct = _mapProductDetail.get(spctId);
        mapSpct.set(spctId, {
            sanPhamChiTietId: spctId,
            soLuong: soLuongThem,
            sanPhamTen: tenSanPham + " " + spct.rom + " " + spct.mauSacTen,
            giaBan: spct.giaBan
        });
    }

    $('#sp-add-to-cart').find('[name="so-luong"]').html("Số lượng sản phẩm: <span></span>")
    $('#sp-add-to-cart').find('[name="so-luong"] span').text(_mapProductDetail.get(spctId).soLuong)
    _mapInvoice.set(_selectedInvoice, mapSpct);
    console.log(_mapInvoice);
    loadInvoice();
}

$(document).ready(() => {
    $('#add-to-cart').on('click', function () {
        addToCart(_selectedId, 1, $('#sp-add-to-cartLabel').text());
    })
})


$(document).ready(() => {
    $('#check-out').on('click', () => {
        checkout(_mapInvoice.get(_selectedInvoice));
    })
})

function checkout(invoice) {
    if (invoice.size == 0) {
        showErrToast("Lỗi", "Vui lòng chọn hóa đơn có sản phẩm")
    }
    $offcv = $('#offcanvas-check-out');
    $offcv.find('.is-valid,.is-invalid').removeClass('is-invalid').removeClass('is-valid');
    $offcv.offcanvas('show');
    let tongSoSanPham = 0;
    let tongTien = 0;
    let imeiRow = ''
    let tenHoaDon = $(`#nav-invoice [invoice-id='${_selectedInvoice}']`).parent().text();
    invoice.forEach((element) => {
        let listGroup = ''
        for (let i = 0; i < element.soLuong; i++) {
            listGroup += `
            <li class="list-group-item">
                <div class="input-group input-group-sm">
                    <span class="input-group-text">IMEI ${i + 1}</span>
                    <input type="text" spct-id="${element.sanPhamChiTietId}" class="form-control" required>
                     <div class="invalid-feedback">
                        IMEI Không hợp lệ !
                      </div>
            </div>
            </li>
            `
        }
        imeiRow += `
           <div class="card mx-0 px-0">
              <div class="card-header">
                IMEI ${element.sanPhamTen}
              </div>
              <ul class="list-group list-group-flush">
                ${listGroup}
              </ul>
        </div>
        
        `
        tongSoSanPham += element.soLuong;
        tongTien += element.soLuong * element.giaBan;
    })

    $offcv.find('.offcanvas-title').text(tenHoaDon)
    $('#hd-tong-so-luong').text(tongSoSanPham);
    $('#tbl-imei').html(imeiRow);
    $('#hd-tong-so-tien').text(toCurrency(tongTien));
    $('#hd-khach-tra').text(toCurrency(tongTien));
    updateBillStep2(tongTien);
    $('#hd-voucher').val([]).trigger('change');

    $offcv.find('input[spct-id]').on('focus', function ()  {
        $(this)[0].scrollIntoView({ behavior: 'smooth', block: 'center' });
    })
    $offcv.find('input[spct-id]').on('blur', function () {
        let $input=$(this);
        if($input.val()==''){
            $input.addClass('is-invalid').removeClass('is-valid');
            showErrToast("Lỗi","IMEI Không được để trống")
            return;
        }
        $input.parent().find('.fa-plus').parent().remove();
        let spctId = $(this).attr('spct-id');
        let imei = $(this).val();

        if(!checkDuplicate($('#tbl-imei'),'input[spct-id]')){
            showErrToast("Lỗi","IMEI là duy nhất");
            return;
        }
        $.get(`/api/v1/sale/check-imei?imei=${imei}&spctid=${spctId}`,
            function (data,message,jqXhr) {
            switch (jqXhr.status) {
                case 200:{
                    $input.addClass('is-valid').removeClass('is-invalid');
                    break;
                }
                case 201:{
                    $input.addClass('is-invalid');

                    showSuccessToast("Thêm mới ?","IMEI Chưa tồn tại bạn muốn thêm nhanh cho sản phẩm này");
                    let $btnAdd=$(`
                        <span class="input-group-text"><i
                            class="fa fa-plus"
                            aria-hidden="true"></span>
                    `)
                    $input.after($btnAdd);
                    $btnAdd.on('click',function (){
                        $btnAdd.html(`
                            <div class="spinner-border" role="status">
                              <span class="visually-hidden"></span>
                            </div>
                        `)
                        if(imei.length>15){
                            $btnAdd.remove();
                            showErrToast("Thêm mới thất bại",'Độ dài tối đa của imei là 15 ký tự');
                            return;
                        }
                        // addNewImei();
                        $.get(`/api/v1/sale/new-imei?imei=${imei}&spctid=${spctId}`).then(()=>{
                            console.log('Dữ liệu đã được gửi thành công:', spctId);
                            showSuccessToast("Thêm mới thành công",'Thêm mới IMEI: '+imei);
                            $input.addClass('is-valid').removeClass('is-invalid');
                            $btnAdd.remove();
                        }).fail(()=>{
                            $btnAdd.remove();
                            showErrToast("Thêm mới thất bại",'');
                        })
                    })

                    break;
                }
                case 202:{
                    $input.addClass('is-invalid');
                    showErrToast("Lỗi",data)
                    break;
                }
                default:{
                    $input.addClass('is-invalid');
                    break;
                }
            }

        })

    })

    $('#hd-voucher').on('select2:select select2:unselect', function () {
        if($(this).val().length==0){
            applyVoucher(undefined);
            return;
        }
        $.get('/api/v1/sale/promotion/' + $(this).val())
            .then((response) => {
                applyVoucher(response);
            })
    })




    function applyVoucher(response) {

        if(!response){
            $('#hd-giam-gia').text(toCurrency(0))
            $('#hd-khach-tra').text(toCurrency(tongTien));
            updateBillStep2(tongTien);
            return;
        }

        let voucher = {
            "id": 3,
            "code": "ABCD-1234567890",
            "phanTramGiam": 0,
            "giamToiDa": 0,
            "giaTriToiThieu": 0.00,
            "soLuong": 1,
            "thoiGianKetThuc": "2024-06-26T00:00:00Z",
            "thoiGianBatDau": "2024-06-04T00:00:00Z",
            "createAt": "2024-06-09T22:52:43.486Z",
            "updateAt": "2024-06-09T22:54:21.164Z",
            "lastModifiedBy": "",
            "deleted": false
        }
        voucher = response;
        if (voucher.thoiGianBatDau > new Date()) {
            showErrToast("Lỗi", "Chưa tới thời gian bắt đầu");
            $('#hd-voucher').val([]).trigger('change')
            return;
        }
        if (voucher.thoiGianKetThuc < new Date()) {
            showErrToast("Lỗi", "Mã giảm giá đã kết thúc");
            $('#hd-voucher').val([]).trigger('change')
            return;
        }
        if (voucher.soLuong < 1) {
            showErrToast("Lỗi", "Đã hết mã");
            $('#hd-voucher').val([]).trigger('change')
            return;
        }
        if (voucher.deleted == true) {
            showErrToast("Lỗi", "Chương trình đã dừng triển khai");
            $('#hd-voucher').val([]).trigger('change')
            return;
        }
        if (voucher.giaTriToiThieu > tongTien) {
            showErrToast("Lỗi", "Chưa đạt giá trị tối thiểu");
            $('#hd-voucher').val([]).trigger('change')
            return;
        }

        function getGiaTriGiam(tongTien) {
            return tongTien * voucher.phanTramGiam / 100 > voucher.giamToiDa ? voucher.giamToiDa : tongTien * voucher.phanTramGiam /100
        }

        $('#hd-giam-gia').text(toCurrency(getGiaTriGiam(tongTien)))
        $('#hd-khach-tra').text(toCurrency(tongTien - getGiaTriGiam(tongTien)));
        updateBillStep2(tongTien - getGiaTriGiam(tongTien));
    }
}

$(document).ready(function () {
    function showCustomerDetailInOder(customer) {

        if(customer.deleted!=0){
            showWarnToast('Cảnh báo','Khách hàng đã bị xóa');
            return;
        }
        $('#hd-kh-ten').val(customer.ten);
        $('#hd-kh-email').val(customer.email);
        $('#hd-kh-sdt').val(customer.soDienThoai);
    }

    $('#hd-khach-hang').on('select2:select select2:unselect', function () {
        $.get('/api/v1/sale/customer/' + $(this).val())
            .then((response) => {
                showCustomerDetailInOder(response);
            })
    })
})
function updateBillStep2(soTienThanhToan){
    const stk="XUNGCONGQUY";
    const tenTaiKhoan="NGUYEN%20BA%20CHUC";
    const noiDung="ABCXYZ";
    let imgSrc=`https://img.vietqr.io/image/tpbank-${stk}-compact2.jpg?amount=${soTienThanhToan}&addInfo=${noiDung}&accountName=${tenTaiKhoan}`
    $('.qrcode').attr('src',imgSrc)
    $('#httt-tien-mat > span > span:nth-child(2)').text(toCurrency(soTienThanhToan))
}

function checkDuplicate(form,selector){
    let values = [];
    let duplicates = [];
    form.find(selector).each(function() {
        let value = $(this).val().trim();
        if (value && values.includes(value)) {
            duplicates.push($(this));
        } else {
            values.push(value);
        }
    });
    if(duplicates.length > 0){
        duplicates.forEach(duplicateElm=>{duplicateElm.addClass('is-invalid')})
        return false;
    }
    return true;
}

$(document).ready(()=>{

    $('#form-check-out').off();
    $('#form-check-out').on('submit', function (event) {
        event.preventDefault();
        let form=$(this);
        if (!form[0].checkValidity()) {
            form.find(":invalid").removeClass("is-valid").addClass("is-invalid");
            form.find(":valid").prop("valid",false)
            event.stopPropagation();
        }
        if(form.find('.is-invalid').length === 0){
            $('#offcanvas-check-out').offcanvas('hide');
            $('#offcanvas-check-out-step-2').offcanvas('show');

        }
    });
    $('#offcanvas-check-out-step-2 > div.offcanvas-header > button').on('click', function () {
        $('#offcanvas-check-out').offcanvas('show');
        $('#offcanvas-check-out-step-2').offcanvas('hide');
        $('#offcanvas-check-out-step-2').find('form').removeClass('was-validated');
    })

    $('#nav-profile-tab').on('click',()=>{
        setTimeout(()=>{
            $('.qrcode')[0].scrollIntoView({ behavior: 'smooth', block: 'center' });},200);
    })

})


async function showConfirm(parentComponent,title,message){
    let $modal=$(`
        <div class="modal fade" id="ex-modal" tabindex="-1" aria-labelledby="ex-modalLabel" aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="ex-modalLabel">${title}</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
                ${message}
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-primary">Xác nhận</button>
              </div>
            </div>
          </div>
        </div>
    `);
}

$(document).ready(()=>{

    $('#form-check-out-step-2').on('submit', function (event) {
        event.preventDefault();
        let form=$(this);
        if (!form[0].checkValidity()) {
            event.stopPropagation();
        }
        form.addClass('was-validated')
        if(form.find('.is-invalid').length === 0){
            alert('OK')

        }
    });


})