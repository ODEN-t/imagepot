import jBox from 'jbox';
import $ from 'jquery/dist/jquery.min';


// show result message from backend with modal
export function showResultMessageModal(commonClass, sucMsgClass, errMsgClass, isTop = false) {

    let errMsgModal;
    let sucMsgModal;

    if(isTop) {
        errMsgModal = new jBox('Modal', {
            id: 'errorTop',
            content: $(errMsgClass),
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
            },
            onOpen() {
                $(errMsgClass).addClass('is-opened');
            }
        });

        sucMsgModal = new jBox('Modal', {
            id: 'successTop',
            content: $(sucMsgClass),
            addClass: 'add-jboxSuccessMessage',
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
            },
            onOpen() {
                $(sucMsgClass).addClass('is-opened');
            }
        });
    } else {
        errMsgModal = new jBox('Modal', {
            id: 'error',
            content: $(errMsgClass),
            addClass: 'add-jboxErrorMessage',
            overlay: false,
            closeOnClick: false,
            closeButton: false,
            offset: {
                x: 147.5,
                y: 310
            },
            autoClose: 3000,
            fade: 250,
            animation: {
                open: 'slide:bottom',
                close: 'slide:bottom'
            },
            blockScroll: false,
            adjustTracker: true,
            fixed: true,
            onOpen() {
                $(errMsgClass).addClass('is-opened');
            }
        });

        sucMsgModal = new jBox('Modal', {
            id: 'success',
            content: $(sucMsgClass),
            addClass: 'add-jboxSuccessMessage',
            overlay: false,
            closeOnClick: false,
            closeButton: false,
            offset: {
                x: 147.5,
                y: 310
            },
            autoClose: 3000,
            fade: 250,
            animation: {
                open: 'slide:bottom',
                close: 'slide:bottom'
            },
            blockScroll: false,
            adjustTracker: true,
            fixed: true,
            onOpen() {
                $(sucMsgClass).addClass('is-opened');
            }
        });
    }

    const messageElement = document.querySelectorAll(commonClass);
    if(messageElement.length > 0) {
        const messageType = messageElement[0].dataset.message;
        switch (messageType) {
            case 'success' :
                sucMsgModal.open();
                break;
            case 'error':
                errMsgModal.open();
                break;
        }
        return messageElement[0];
    }
}


// toggle input type between text and password
export function inputTypeToggle(elementClass) {
    const nodeList = document.querySelectorAll(elementClass);
    for (const node of nodeList) {
        node.addEventListener('click', () => {
            const target = node.previousElementSibling;
            target.type === 'password' ? target.type = 'text' : target.type = 'password';
        })
    }
}