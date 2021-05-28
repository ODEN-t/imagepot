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


/***
 * 引数に渡したテンプレートを引数numberの数だけ配列に詰め込み返す
 * @param {String} template 要素を表現したテンプレートリテラル
 * @param {Number} number 量産したいtemplateの数
 * @return []
 */
export function generateImageTemplateList (template, number) {
    const templateList = [];
    const domParser = new DOMParser();
    let i = 0;
    while (i < number) {
        templateList.push(domParser.parseFromString(template, "text/html").body.firstChild);
        i++;
    }
    return templateList;
}

/**
 *　引数に渡した配列を指定した数毎にラップしHTML要素として追加する
 *
 * @param {Array} listOfElement HTMLElementを詰めた配列
 * @param {Number} unit 何個ずつwrapしたいかを指定
 * @param {String} wrapperTemplate wrapする親要素を表現したテンプレートリテラル
 * @return void
 */
export function WrapNodesInEachUnit(listOfElement, unit, wrapperTemplate) {
    const contents = document.getElementById('js-postedList');
    const NUMBER_OF_ELEMENTS = listOfElement.length; // 要素の数
    const NUMBER_OF_WRAPPERS = Math.ceil(NUMBER_OF_ELEMENTS / unit); // 要素をラップするラッパーの合計数
    const domParser = new DOMParser();
    let wrapNumber = 0;
    let eachNum = unit;　// 各ラッパー内に追加する要素数
    let elemNum = 0;

    while (wrapNumber <= NUMBER_OF_WRAPPERS) {
        let wrapper = domParser.parseFromString(wrapperTemplate, "text/html").body.firstChild;
        while (elemNum < eachNum) {
            if (NUMBER_OF_ELEMENTS <= elemNum) break;
            wrapper.appendChild(listOfElement[elemNum]);
            elemNum++;
        }
        contents.appendChild(wrapper);
        if (NUMBER_OF_ELEMENTS <= elemNum) break;
        eachNum += unit;
        wrapNumber++;
    }
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