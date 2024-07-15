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

const clearForm = (selector) => {
    let formNeedClear=selector?selector:(document);
    console.log('Clear form')
    // Clear input fields
    $(formNeedClear).find('input[type="text"], input[type="password"], input[type="number"], input[type="email"], input[type="url"], input[type="search"], input[type="tel"], input[type="date"], input[type="datetime-local"], input[type="month"], input[type="week"], input[type="time"]').val('');

    // Uncheck all checkboxes and radio buttons
    $(formNeedClear).find('input[type="checkbox"], input[type="radio"]').prop('checked', false);

    // Clear textarea fields
    $(formNeedClear).find('textarea').val('');

    // Reset select fields to default option
    $(formNeedClear).find('select').prop('selectedIndex', 0);


    $(formNeedClear).find('.was-validated').removeClass('was-validated');


    $(formNeedClear).find('.is-valid').removeClass('is-valid');


    $(formNeedClear).find('.is-invalid').removeClass('is-invalid');


}

function dataToJson(event) {
    return new Promise((resolve, reject) => {
        const file = event.target.files[0];
        const reader = new FileReader();

        reader.onload = function (e) {
            try {
                const data = new Uint8Array(e.target.result);
                const workbook = XLSX.read(data, {type: 'array'});

                // Giả sử dữ liệu nằm trong sheet đầu tiên
                const firstSheet = workbook.Sheets[workbook.SheetNames[0]];
                const jsonData = XLSX.utils.sheet_to_json(firstSheet);

                resolve(jsonData)
                console.log(jsonData)
                event.target.files[0].value = ""
            } catch (e) {
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

