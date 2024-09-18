const cloneData = function (url) {
    return new Promise((resolve, reject) => {
        $.ajax({
                url: url,
                success: function (response) {
                    resolve(response);
                },
                error: function (xhr, status, error) {
                    reject(status);
                }
            }
        )
    })

}

//Clone data từ api
$(document).ready(function(){
    let lstProduct=[];
    let params= new URLSearchParams(new URL(window.location.href).search)
    if(params.size>0){

    }else{
        lstProduct=cloneData('/api/v2/san-pham').then(function(response){
            lstProduct=response.content;
            fillData(response)
        });
    }

})



$(document).ready(() => {
    $("#rangeSlider").ionRangeSlider({
        type: "double",
        min: 10000000,
        max: 50000000,
        from: 10000000,
        to: 50000000,
        prettify: true,
        prettify_separator: ".",
        postfix: " VND",
        skin: "round",
        onChange: function () {
            $('#rangeSlider').closest(".panel").find("input[type=checkbox]").prop("checked", false);
        }
    });
})



const TrangThai = new Map([
    ['IN_STOCK', ""],
    ['OUT_OF_STOCK', "Hết hàng"],
    ['TEMPORARILY_OUT_OF_STOCK', "Hết hàng tạm thời"],
    ['COMING_SOON', "Sắp ra mắt"],
    ['DISCONTINUED', "Không kinh doanh"]
])
function caclPrice(price,dotGiamGia){

}
function toCurrency(Num) {
    Num = Num <= 0 ? 0 : Num;
    return (numeral(Num).format('0,0') + ' VND').replace(/,/g, ".")
}

function fillData(response) {
    let productContainer = "";
    let mapProduct = new Map();

    response.content.forEach(sp => {
        let uniqueProductRoms = new Map();
        sp.sanPhamChiTiet.forEach(spct => {
            if (uniqueProductRoms.get(spct.rom) == undefined || uniqueProductRoms.get(spct.rom).giaBan > spct.giaBan) {
                uniqueProductRoms.set(spct.rom, spct);
            }

        })
        uniqueProductRoms = new Map([...uniqueProductRoms.entries()].sort((a, b) => a[0].replace(/1TB/g, '6').localeCompare(b[0].replace(/1TB/g, '6'))))
        let romBtn = "";
        let giaBan = 99999999999;
        let giaSanPhamHtml = '';
        let firstBtnFlag = true;
        uniqueProductRoms.forEach((spct, rom) => {
            if (firstBtnFlag) {
                romBtn += `<button class="btn btn-outline-secondary btn-sm m-1 ${firstBtnFlag ? 'selected' : ''} ">${spct.rom}</button>`;



            } else {
                romBtn += `<button class="btn btn-outline-secondary btn-sm m-1 ${firstBtnFlag ? 'selected' : ''} ">${spct.rom}</button>`;

            }
            try {
                if (!spct.dotGiamGia.deleted&&new Date()<new Date( spct.dotGiamGia.thoiGianKetThuc)&&new Date()>new Date(spct.dotGiamGia.thoiGianBatDau)) {
                    giaSanPhamHtml = `
                    <div class="p-0 d-flex">
                    <del class="text-white h-5 mt-2 price text-15 text-th">${toCurrency(spct.giaBan)}
                    </del>
                    
 
                    </div>
                    <div><span class="">-${spct.dotGiamGia.donvi == '%' ? spct.dotGiamGia.giaTriGiam + '%' : toCurrency(spct.dotGiamGia.giaTriGiam)}</span>
                    </div>
                    <div class="p-0"><span class="text-white h-5 mt-2 price font-weight-bold">${toCurrency(spct.dotGiamGia.donvi == '%' ? spct.giaBan - spct.giaBan * spct.dotGiamGia.giaTriGiam / 100 : spct.giaBan - spct.dotGiamGia.giaTriGiam)}</span></div>
                `
                } else {
                    giaSanPhamHtml = `
                    <div class="p-0"><span class="text-white h-5 mt-2 price font-weight-bold">${toCurrency(spct.giaBan)}</span></div>
                    `
                }
            }catch (e) {
                giaSanPhamHtml = `
                    <div class="p-0"><span class="text-white h-5 mt-2 price font-weight-bold">${toCurrency(spct.giaBan)}</span></div>
                    `
            }



            firstBtnFlag = false;
        })
        giaBan = toCurrency(giaBan)
        productContainer += `
            <div class="product mb-5" sp-id="${sp.id}" style="width: 30%">
                <a class="" href="/iphone/${sp.id}">
                    <label>Bảo hành ${sp.thoiGianBaoHanh}</label>
                    <div><img src="${sp.anhUrl}" alt="${sp.anhName}" srcset=""></div>
                    <div class="p-0"><span >${TrangThai.get(sp.trangThai)}</span></div>
                    <div class="d-flex">${romBtn}</div>
                    
                    <div class="p-0"><span class="text-white">${sp.tenSanPham}</span></div>
<!--                 Giá sản phẩm   /-->
                    <div class="giaSanPham">${giaSanPhamHtml}</div>
                </a>
            </div>
        `

        mapProduct.set(sp.id, uniqueProductRoms);

    })


    $('.product-container').first().html(productContainer);

    $('.product-container').on('click', 'button', function (event) {
        event.stopPropagation()
        event.preventDefault()
        let product = $(this).closest('.product');
        let spId = parseInt($(this).closest('.product').attr('sp-id'));
        let uniqueProductRoms = mapProduct.get(spId);
        product.find('button').removeClass('selected');
        $(this).addClass('selected');
        let spct = uniqueProductRoms.get($(this).text());
        let giaSanPhamHtml = '';
        try {
            if (!spct.dotGiamGia.deleted&&new Date()<new Date( spct.dotGiamGia.thoiGianKetThuc)&&new Date()>new Date(spct.dotGiamGia.thoiGianBatDau)) {
                giaSanPhamHtml = `
                    <div class="p-0 d-flex">
                    <del class="text-white h-5 mt-2 price text-15 text-th position-relative">${toCurrency(spct.giaBan)}
                    </del>
                    
                    </div>
                    <div>
                         <span class="">-${spct.dotGiamGia.donvi == '%' ? spct.dotGiamGia.giaTriGiam + '%' : toCurrency(spct.dotGiamGia.giaTriGiam)}</span>
                    </div>
                    <div class="p-0"><span class="text-white h-5 mt-2 price font-weight-bold">${toCurrency(spct.dotGiamGia.donvi == '%' ? spct.giaBan - spct.giaBan * spct.dotGiamGia.giaTriGiam / 100 : spct.giaBan - spct.dotGiamGia.giaTriGiam)}</span></div>
                `
            } else {
                giaSanPhamHtml = `
                    <div class="p-0"><span class="text-white h-5 mt-2 price font-weight-bold">${toCurrency(spct.giaBan)}</span></div>
                    `
            }
        }catch (e) {
            giaSanPhamHtml = `
                    <div class="p-0"><span class="text-white h-5 mt-2 price font-weight-bold">${toCurrency(spct.giaBan)}</span></div>
                    `
        }
        product.find('.giaSanPham').html(giaSanPhamHtml);

    })
}

//Load data filter
$(document).ready(() => {
    let promiseTinhNangDacBiet = $.get('api/v1/admin/data-list-add-san-pham/tinh-nang-dac-biet')
    let promiseSeries = $.get('api/v1/admin/data-list-add-san-pham/series')
    let promiseCongNgheSac = $.get('api/v1/admin/data-list-add-san-pham/ho-tro-sac-toi-da')
    let promiseCamera = $.get('api/v1/admin/data-list-add-san-pham/tinh-nang-camera')
    let promiseCongNgheManHinh = $.get('api/v1/admin/data-list-add-san-pham/cong-nghe-man-hinh')
    Promise.all([promiseTinhNangDacBiet,promiseSeries, promiseCongNgheSac, promiseCamera, promiseCongNgheManHinh])
        .then(([lstTinhNangDacBiet,lstSeries, lstHoTroSacToiDa, lstCamera, lstCongNgheManHinh]) => {
            lstTinhNangDacBiet.results.forEach((item) => {
                $('#filter-tinhNangDacBiet').next().append(`
                    <div class="form-check mb-2">
                    <input type="checkbox" class="form-check-input" data="${item.id}" id="tndb-${item.id}">
                    <label class="form-check-label" for="tndb-${item.id}" style="margin-top: 2px;">
                    ${item.text}
                   </label>
                    </div>
                `)
            });
            lstHoTroSacToiDa.results.forEach((item) => {
                $('#filter-congNgheSac').next().append(`
                    <div class="form-check mb-2">
                                <input type="checkbox" class="form-check-input" data="${item.text}" id="cns-${item.text}" checked>
                                <label class="form-check-label" for="cns-${item.text}" style="margin-top: 2px;">${item.text}</label>
                    </div>
                `)
            })
            lstCamera.results.forEach((item) => {
                $('#filter-camera').next().append(`
                    <div class="form-check mb-2">
                                <input type="checkbox" class="form-check-input" data="${item.id}" id="camera-${item.id}">
                                <label class="form-check-label" for="camera-${item.id}" style="margin-top: 2px;">${item.text}</label>
                    </div>
                `)
            })
            lstSeries.results.forEach((item) => {
                $('#filter-series').next().append(`
                    <div class="form-check mb-2">
                                <input type="checkbox" class="form-check-input" data="${item.id}" id="series-${item.id}" checked>
                                <label class="form-check-label" for="series-${item.id}" style="margin-top: 2px;">${item.text}</label>
                    </div>
                `)
            })
            lstCongNgheManHinh.results.forEach((item) => {
                $('#filter-congNgheManHinh').next().append(`
                    <div class="form-check mb-2">
                                <input type="checkbox" class="form-check-input" data="${item.id}" id="cnmh-${item.id}">
                                <label class="form-check-label" for="cnmh-${item.id}" style="margin-top: 2px;">${item.text}</label>
                    </div>
                `)
            })

            $('input[id$="-All"]').on('change', function () {
                $(this).closest('.panel').find('input').prop('checked', $(this).prop('checked'));
            })
            $('input:gt(0)').on('change', function () {
                if ($(this).closest('.panel').find('input[type="checkbox"]:gt(0):not(:checked)').length === 0)
                    $(this).closest('.panel').find('input').prop('checked', true);
                else
                    $(this).closest('.panel').find('input[id$="-All"]').prop('checked', false);
            })

            let params= new URLSearchParams(new URL(window.location.href).search)
            if(params.size>0){
                loadFilter()
                applyFillter()
            }


        })
})

//Apply fillter
$(document).ready(() => {
    $("#select-order-by").on("change", function () {
        applyFillter();
    })
    $('.search-model-form').on('submit', function (e) {
        e.preventDefault();
        $('.search-model').css("display","none")
        applyFillter();
    })
})

function loadFilter(){
    let urlSearchParams= new URLSearchParams(new URL(window.location.href).search)

    if(urlSearchParams.get("orderBy")){
        $('#select-order-by').val(urlSearchParams.get("orderBy"));
    }
    if(urlSearchParams.get("searchKey")){
        $('#search-input').val(urlSearchParams.get("searchKey"));
        $('#filter-bar').append(`
            <span class="text-light">Từ khóa: </span>
            <div class="btn-group" role="group" aria-label="Basic example">
           
                <button type="button" class="btn btn-sm btn-secondary">${urlSearchParams.get("searchKey")}</button>
                <button class="btn btn-secondary btn-sm" aria-label="Delete" onclick="$('#search-input').val(''); $(this).closest('#filter-bar').html('')">
                    ✖
                </button>
            </div>
        `)
    }
    if(urlSearchParams.get("price")){
        let priceIds=urlSearchParams.get("price").split(',')
        $('[id^="price-"]').prop('checked', false);
        priceIds.forEach(e=>{
            $(`#price-${e}`).prop('checked', true);
        })

    }
    if(urlSearchParams.get("series")){
        let seriesIds=urlSearchParams.get("series").split(',')
        $('[id^="series-"]').prop('checked', false);
        seriesIds.forEach(e=>{
            $(`#series-${e}`).prop('checked', true);
        })

    }
    if(urlSearchParams.get("rom")){
        let romIds=urlSearchParams.get("rom").split(',')
        $('[id^="rom-"]').prop('checked', false);
        romIds.forEach(e=>{
            $(`#rom-${e}`).prop('checked', true);
        })

    }
    if(urlSearchParams.get("tndb")){
        let tndbIds=urlSearchParams.get("tndb").split(',')
        $('[id^="tndb-"]').prop('checked', false);
        tndbIds.forEach(e=>{
            $(`#tndb-${e}`).prop('checked', true);
        })

    }
    if(urlSearchParams.get("tncmr")){
        let tncmrIds=urlSearchParams.get("tncmr").split(',')
        $('[id^="tncmr-"]').prop('checked', false);
        tncmrIds.forEach(e=>{
            $(`#tncmr-${e}`).prop('checked', true);
        })

    }
    if(urlSearchParams.get("manHinh")){
        let manHinhIds=urlSearchParams.get("manHinh").split(',')
        $('[id^="manHinh-"]').prop('checked', false);
        manHinhIds.forEach(e=>{
            $(`#manHinh-${e}`).prop('checked', true);
        })

    }
    if(urlSearchParams.get("cns")){
        let cnsIds=urlSearchParams.get("cns").split(',')
        $('[id^="cns-"]').prop('checked', false);
        cnsIds.forEach(e=>{
            $(`#cns-${e}`).prop('checked', true);
        })

    }

}

function applyFillter(){

    // skeleton-load
    {
        $('.product').addClass('skeleton')
    }


    let lstProduct=[];
    let urlSearchParams= new URLSearchParams(new URL(window.location.href).search)
    urlSearchParams.set("orderBy",$("#select-order-by").val());
    function intiFilter(param,val){
        if(val)
            if(Array.isArray(val)){
                urlSearchParams.set(param,val.join(','))
            }else{
                urlSearchParams.set(param,val)
            }
        else{
            urlSearchParams.delete(param)
        }

    }




    //Giá bán
    if(!$("#price-All").prop("checked")){
        let prices=$("#price-All").closest(".panel").find('input[type="checkbox"]:gt(0):checked').map((i,e)=>{
           return $(e).attr("data")
        })
        intiFilter('price',prices.toArray());
    }else{
        intiFilter("price")
    }

    //Series
    if(!$("#series-All").prop("checked")){
        let series=$("#series-All").closest(".panel").find('input[type="checkbox"]:gt(0):checked').map((i,e)=>{
            return $(e).attr("data")
        })
        intiFilter('series',series.toArray());
    }else{
        intiFilter("series")
    }
    //Rom
    if(!$("#rom-All").prop("checked")){
        let roms=$("#rom-All").closest(".panel").find('input[type="checkbox"]:gt(0):checked').map((i,e)=>{
            return $(e).attr("data")
        })
        intiFilter('rom',roms.toArray());
    }else{
        intiFilter("rom")
    }
    //Tinh nang dac biet
    if(!$("#tndb-All").prop("checked")){
        let tndbs=$("#tndb-All").closest(".panel").find('input[type="checkbox"]:gt(0):checked').map((i,e)=>{
            return $(e).attr("data")
        })
        intiFilter('tndb',tndbs.toArray());
    }else{
        intiFilter("tndb")
    }

    //Tinh nang camera
    if(!$("#tncmr-All").prop("checked")){
        let tncmrs=$("#tncmr-All").closest(".panel").find('input[type="checkbox"]:gt(0):checked').map((i,e)=>{
            return $(e).attr("data")
        })
        intiFilter('tncmr',tncmrs.toArray());
    }else{
        intiFilter("tncmr")
    }

    if(!$("#manHinh-All").prop("checked")){
        let manHinhs=$("#manHinh-All").closest(".panel").find('input[type="checkbox"]:gt(0):checked').map((i,e)=>{
            return $(e).attr("data")
        })
        intiFilter('manHinh',manHinhs.toArray());
    }else{
        intiFilter("manHinh")
    }

    if(!$("#cns-All").prop("checked")){
        let cnss=$("#cns-All").closest(".panel").find('input[type="checkbox"]:gt(0):checked').map((i,e)=>{
            return $(e).attr("data")
        })
        intiFilter('cns',cnss.toArray());
    }else{
        intiFilter("cns")
    }

    //Tìm kiếm
    intiFilter("searchKey",$('#search-input').val());

    window.history.pushState({}, '', `${window.location.pathname}?${urlSearchParams.toString()}`);


    lstProduct=cloneData('/api/v2/san-pham?'+urlSearchParams.toString()).then(function(response){
        lstProduct=response.content;
        fillData(response)
    });

}
