import jBox from 'jbox';
import 'jbox/dist/jBox.all.css';
import $ from 'jquery/dist/jquery.min';

// Signup Modal の設定値
const signupModal = new jBox('Modal', {
    width: 405,
    height: 573,
    attach: '#signup',
    content: $('#signupContent'),
    overlayClass: 'add-jboxOverRay',
    closeOnClick: false,
    closeButton: 'box',
    createOnInit: true,
});


// Signin Modal の設定値
const signinModal = new jBox('Modal', {
    width: 405,
    height: 443,
    attach: '#signin',
    content: $('#signinContent'),
    overlayClass: 'add-jboxOverRay',
    closeOnClick: false,
    closeButton: 'box',
    createOnInit: true,
});

// Error modal の設定値
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
    signupModal.open();
    errorModal.open();
}


$('#signupSub, #signinSub').on('click', function () {
    signinModal.toggle();
    signupModal.toggle();
});



// type=password <=> type=text の切り替え
function showToggle(elem) {
    $(elem).on('click', function () {
        const target = $(this).prevAll('input').get(0);
        console.log(target);
        target.type == 'password' ? target.type = 'text' : target.type = 'password';
    })
}
showToggle('#showButtonPassword-signin');
showToggle('#showButtonPassword');
showToggle('#showButtonConfirm');
