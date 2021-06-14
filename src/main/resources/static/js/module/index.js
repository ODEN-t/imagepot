import jBox from 'jbox';
import 'jbox/dist/jBox.all.css';
import $ from 'jquery/dist/jquery.min';


// 実行結果メッセージモーダル表示
export function showResultMessageModal(commonClass, sucMsgClass, errMsgClass) {

    let errMsgModal;
    let sucMsgModal;
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

    const messageElement = document.querySelectorAll(commonClass);
    if (messageElement.length > 0) {
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


// toggle input=text <=> input=password
export function inputTypeToggle(elementClass) {
    const nodeList = document.querySelectorAll(elementClass);
    for (const node of nodeList) {
        node.addEventListener('click', () => {
            const target = node.previousElementSibling;
            target.type === 'password' ? target.type = 'text' : target.type = 'password';
        })
    }
}


/***
 * 引数に渡したテンプレートを引数numberの数だけ配列に詰め込み返す
 * @param {String} template 要素を表現したテンプレートリテラル
 * @param {Number} number 量産したいtemplateの数
 * @return []
 */
export function generateImageTemplateList(template, number) {
    const templateList = [];
    const domParser = new DOMParser();
    let i = 0;
    while (i < number) {
        templateList.push(domParser.parseFromString(template, "text/html").body.firstChild);
        i++;
    }
    return templateList;
}

export function readableFileSize(size) {
    const units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    let i = 0;
    while (size >= 1024) {
        size /= 1024;
        ++i;
    }
    return size.toFixed(1) + ' ' + units[i];
}