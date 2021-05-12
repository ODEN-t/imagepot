import jBox from 'jbox';
import $ from 'jquery/dist/jquery.min';


// show result message from backend with modal
export function showResultMessageModal(commonClass, successMessageClass, errorMessageClass, isTop = false) {

    let errorMessageModal;
    let successMessageModal;

    if(isTop) {
        errorMessageModal = new jBox('Modal', {
            id: 'errorTop',
            content: $(errorMessageClass),
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

        successMessageModal = new jBox('Modal', {
            id: 'successTop',
            content: $(successMessageClass),
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
            }
        });
    } else {
        errorMessageModal = new jBox('Modal', {
            id: 'error',
            content: $(errorMessageClass),
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
            fixed: true
        });

        successMessageModal = new jBox('Modal', {
            id: 'success',
            content: $(successMessageClass),
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
            fixed: true
        });
    }

    const messageElement = document.querySelectorAll(commonClass);
    if(messageElement.length > 0) {
        const messageType = messageElement[0].dataset.message;
        switch (messageType) {
            case 'success' :
                successMessageModal.open();
                break;
            case 'error':
                errorMessageModal.open();
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