const hideModal = () => {
    $('.modal').each(function () {
        $(this).modal('hide');
    });
}
const showSuccessToast = (message) => {
    let Toast = $(`
        <div class="toast" style="position: fixed; bottom: 5%; right: 0; z-index: 9999999;width: 25%" data-delay="5100"  role="alert" aria-live="assertive" aria-atomic="true">
        <div class="alert alert-success mb-1" role="alert">
        <span>${message}</span>
    </div>
</div>
    `)
    $('body').append(Toast);
    Toast.toast('show');
}
const showErrorToast = (message) => {
    let Toast = $(`
        <div class="toast" style="position: fixed; bottom: 5%; right: 0; z-index: 9999999;width: 25%" data-delay="5100"  role="alert" aria-live="assertive" aria-atomic="true">
        <div class="alert alert-danger mb-1" role="alert">
        <span>${message}</span>
    </div>
</div>
    `)
    $('body').append(Toast);
    Toast.toast('show');
}
const showWarnigToast = (message) => {
    let Toast = $(`
        <div class="toast" style="position: fixed; bottom: 5%; right: 0; z-index: 9999999;width: 25%" data-delay="5100"  role="alert" aria-live="assertive" aria-atomic="true">
        <div class="alert alert-warning mb-1" role="alert">
        <span>${message}</span>
    </div>
</div>
    `)
    $('body').append(Toast);
    Toast.toast('show');
}

const clearForm = () => {
    console.log('Clear form')
    if ($('.was-validated').length) {
        // Nếu có, loại bỏ class '.was-validated' từ tất cả các phần tử
        $('.was-validated').removeClass('was-validated');
    }
    if ($('.is-valid').length) {
        // Nếu có, loại bỏ class '.is-valid' từ tất cả các phần tử
        $('.is-valid').removeClass('is-valid');
    }
    if ($('.is-invalid').length) {
        // Nếu có, loại bỏ class '.is-invalid' từ tất cả các phần tử
        $('.is-invalid').removeClass('is-invalid');
    }
    if ($('input').length) {
        // Nếu có, loại bỏ class 'input' từ tất cả các phần tử
        $('input').val('');
    }
}
function dataToJson(event) {
    return new Promise((resolve, reject) => {
        const file = event.target.files[0];
        const reader = new FileReader();

        reader.onload = function(e) {
            try {
                const data = new Uint8Array(e.target.result);
                const workbook = XLSX.read(data, { type: 'array' });

                // Giả sử dữ liệu nằm trong sheet đầu tiên
                const firstSheet = workbook.Sheets[workbook.SheetNames[0]];
                const jsonData = XLSX.utils.sheet_to_json(firstSheet);

                resolve(jsonData)
                console.log(jsonData)
                event.target.files[0].value=""
            }catch (e) {
                reject(e)
            }
        };


        reader.readAsArrayBuffer(file);
    })
}
const showConfirm = (title, message) => {
    console.log("showConfirm");

    return new Promise((resolve, reject) => {
        $('#modal-confirm-title').html(title);
        $('#modal-confirm-message').html(message);
        $('#modal-confirm').modal('show');

        // Hủy bỏ các sự kiện click trước đó để tránh đăng ký nhiều lần
        $('#modal-confirm-confirm').off('click').on('click', () => {
            console.log("OK");
            $('#modal-confirm').modal('hide');
            resolve(true);
        });

        $('#modal-confirm-cancel').off('click').on('click', () => {
            console.log("Cancel");
            $('#modal-confirm').modal('hide');
            reject(false);
        });
    });
}

