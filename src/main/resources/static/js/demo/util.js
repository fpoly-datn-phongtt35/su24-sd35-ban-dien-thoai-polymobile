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
$(document).ready(() => {
    $('.modal').on('show', clearForm())
})
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


