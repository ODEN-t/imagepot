import jBox from 'jbox';
import 'jbox/dist/jBox.all.css';
import $ from 'jquery/dist/jquery.min';

// Login Modal の設定値
const loginModal = new jBox('Modal', {
    id: 'login',
    width: 405,
    height: 443,
    attach: '#login',
    content: $('#loginModal'),
    overlayClass: 'add-jboxOverRay',
    closeOnClick: false,
    closeButton: 'box',
    createOnInit: true,
});


// Signup Modal の設定値
const signupModal = new jBox('Modal', {
    id: 'signup',
    width: 405,
    height: 573,
    attach: '#signup',
    content: $('#signupModal'),
    overlayClass: 'add-jboxOverRay',
    closeOnClick: false,
    closeButton: 'box',
    createOnInit: true,
});

// Message modal の設定値
const errorMessageModal = new jBox('Modal', {
    id: 'error',
    content: $('.p-top__message-error'),
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


// Message modal の設定値
const successMessageModal = new jBox('Modal', {
    id: 'success',
    content: $('.p-top__message-success'),
    addClass: 'add-jboxSuccessMessage',
    overlay: false,
    closeOnClick: false,
    closeButton: false,
    offset: {
        y: 310
    },
    autoClose: false,
    fade: 250,
    animation: {
        open: 'slide:bottom',
        close: 'slide:bottom'
    }
});


// 表示中のモーダルを閉じ他のモーダルを開く
const modalSwitch = (...buttons) => {
    for (const button of buttons) {
        document.getElementById(button).addEventListener('click', () => {
            loginModal.toggle();
            signupModal.toggle();
        })
    }
}
modalSwitch('signupSub', 'loginSub');


// type=password <=> type=text の切り替え
const inputTypeToggle = (elementClass) => {
    const nodeList = document.querySelectorAll(elementClass);
    for (const node of nodeList) {
        node.addEventListener('click', () => {
            const target = node.previousElementSibling;
            target.type === 'password' ? target.type = 'text' : target.type = 'password';
        })
    }
}
inputTypeToggle('.buttonCTA-show');


// errorメッセージ表示時、エラー発生モーダルをオープン
// loginのエラーメッセージが消えないためsignupモーダルが開かない要修正
const showMessage = (elementClass) => {
    const messageElem = document.querySelectorAll(elementClass);
    if(messageElem.length > 0) {
        const modalType = messageElem[0].dataset.process;
        const messageType = messageElem[0].dataset.message;

        console.log('modalType:' + modalType, ' messageType:' + messageType);

        switch (messageType) {
            case 'success':
                console.log('success modal open')
                successMessageModal.open();
                break;
            case 'error':
                console.log('error modal open')
                loginModal.close();
                signupModal.close();
                modalType === 'login' ? loginModal.open() : signupModal.open();
                errorMessageModal.open();
                break;
        }
    }
}
showMessage('.p-top__message');
