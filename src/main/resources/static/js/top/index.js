import jBox from 'jbox';
import 'jbox/dist/jBox.all.css';
import $ from 'jquery/dist/jquery.min';

const formModal = new jBox('Modal', {
    width: 405,
    height: 573,
    attach: '#signup',
    content: $('#modalContent'),
    overlayClass: 'add-jboxOverRay',
    closeOnClick: false,
    closeButton: 'box'
});

const errorModal = new jBox('Modal', {
    content: $('#formError'),
    addClass: 'add-jboxErrorMessage',
    overlay: false,
    closeOnClick: false,
    closeButton: false,
    offset: {
        y: 310
    },
    autoClose: 3000,
    fade: 250,
    animation: {
        open: 'slide:bottom',
        close: 'slide:bottom'
    }
});

if ($('#formError').length) {
    formModal.open();
    errorModal.open();
}


showToggle('#showButtonPassword');
showToggle('#showButtonConfirm');

function showToggle(elem) {
    $(elem).on('click', function () {
        const target = $(this).prevAll('input').get(0);
        console.log(target);
        target.type == 'password' ? target.type = 'text' : target.type = 'password';
    })
}
