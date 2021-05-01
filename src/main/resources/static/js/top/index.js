import jBox from 'jbox';
import 'jbox/dist/jBox.all.css';
import $ from 'jquery/dist/jquery.min';

// Login Modal の設定値
const loginModal = new jBox('Modal', {
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
const messageModal = new jBox('Modal', {
    content: $('.p-top__message'),
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
const showErrorMessage = (elementClass) => {
    const messageElem = document.querySelectorAll(elementClass);
    if (messageElem.length === 1) {
        const modalType = messageElem[0].dataset.error;
        loginModal.close();
        signupModal.close();
        modalType === 'login' ? loginModal.open() : signupModal.open();
        messageModal.open();
    }
}
showErrorMessage('.p-top__message');
