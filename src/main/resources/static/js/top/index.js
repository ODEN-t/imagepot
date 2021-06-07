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
    createOnInit: true,
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
    createOnInit: true,
});

$(".p-hero").slick({
    arrows: true,
    dots: true,
    autoplay: true,
    autoplaySpeed: 10000,
    speed: 400,
    pauseOnFocus: false,
    pauseOnHover: false,
    pauseOnDotsHover: false,
});


// switch modal loginModal and signupModal
const modalSwitch = (...buttons) => {
    for (const button of buttons) {
        document.getElementById(button).addEventListener('click', () => {
            loginModal.toggle();
            signupModal.toggle();
        })
    }
}
modalSwitch('signupSub', 'loginSub');


// toggle input type between text and password
module.inputTypeToggle('.buttonCTA-show');


// show result message from backend with modal
const element = module.showResultMessageModal('.c-message', '.c-message-success', '.c-message-error', true);
if (typeof element !== 'undefined') {
    const result = element.dataset.message;
    const process = element.dataset.process;

    if (result === 'error') {
        process === 'login' ? loginModal.open() : signupModal.open();
    }

    if (result === 'success' && process === 'signup')
        loginModal.open();
}