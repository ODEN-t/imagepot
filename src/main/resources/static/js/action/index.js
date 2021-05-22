import $ from 'jquery/dist/jquery.min';
import 'jbox/dist/jBox.all.css';
import jBox from 'jbox';
import * as module from '../module/index';

const imgModal = new jBox('Modal', {
    id: 'imgModal',
    width: 750,
    maxHeight: "97vh",
    responsiveHeight: true,
    fixed: true,
    content: $('#js-imgModal'),
    addClass: "add-imgModal",
    closeOnClick: "body",
    closeButton: false,
    reposition: true,
    repositionOnOpen: true,
    delayOpen: 300,
    onCloseComplete: function () {
        const parent = document.getElementById('js-imgModal');
        while(parent.firstChild){
            parent.removeChild(parent.firstChild)
        }
    }
});

document.querySelectorAll('.imageIcon').forEach(function (elem){
    elem.addEventListener('click', (e) => {
        const img = e.currentTarget.dataset.url;
        let htmlImageElement = document.createElement('img');
        htmlImageElement.src = img;
        document.getElementById('js-imgModal').appendChild(htmlImageElement);
        imgModal.open();
    })
});


// show result message from backend with modal
module.showResultMessageModal('.c-message', '.c-message-success', '.c-message-error');