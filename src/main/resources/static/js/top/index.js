import 'jbox/dist/jBox.all.css';
import jBox from 'jbox';
import $ from 'jquery/dist/jquery.min';
import * as module from '../module/index';

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
if(typeof element !== 'undefined') {
    const result = element.dataset.message;
    const process = element.dataset.process;

    if(result === 'error') {
        process === 'login' ? loginModal.open() : signupModal.open();
    }

    if(result === 'success' && process === 'signup')
        loginModal.open();
}