import $ from 'jquery';
import 'jbox/dist/jBox.all.css';
import JBox from 'jbox/dist/jBox.min';
import 'slick-carousel';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import * as module from '../module/index';

const loginModal = new JBox('Modal', {
    id: 'login',
    width: 440,
    height: 443,
    attach: '#login',
    content: $('#loginModal'),
    overlayClass: 'add-jboxOverRay',
    closeOnClick: false,
    closeButton: 'box',
    fixed: true,
    delayOpen: 200,
    onOpen: function () {
        document.getElementById('header').style.paddingRight = '17px';
    },
    onClose: function () {
        document.getElementById('header').style.paddingRight = '0';
    },
});

const signupModal = new JBox('Modal', {
    id: 'signup',
    width: 440,
    height: 573,
    attach: '#signup',
    content: $('#signupModal'),
    overlayClass: 'add-jboxOverRay',
    closeOnClick: false,
    closeButton: 'box',
    fixed: true,
    delayOpen: 200,
    onOpen: function () {
        document.getElementById('header').style.paddingRight = '17px';
    },
    onClose: function () {
        document.getElementById('header').style.paddingRight = '0';
    }
});

$(".p-hero").slick({
    arrows: false,
    dots: true,
    autoplay: true,
    autoplaySpeed: 10000,
    speed: 400,
    pauseOnFocus: false,
    pauseOnHover: false,
    pauseOnDotsHover: false,
});

// モーダルトグル
const modalSwitch = (...buttons) => {
    for (const button of buttons) {
        document.getElementById(button).addEventListener('click', (e) => {
            loginModal.toggle();
            signupModal.toggle();
        });
    }
}
modalSwitch('signupSub', 'loginSub');


// input=text input=password のトグル
module.inputTypeToggle('.c-buttonCTA-show');


// 実行結果メッセージの表示
const element = module.showResultMessageModal('.c-message', '.c-message-success', '.c-message-error', true);
if (typeof element !== 'undefined') {
    const result = element.dataset.message;
    const process = element.dataset.process;
    if (result === 'error')
        process === 'login' ? loginModal.open() : signupModal.open();
    if (result === 'success' && process === 'signup')
        loginModal.open();
}