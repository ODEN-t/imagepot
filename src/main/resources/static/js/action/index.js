import $ from 'jquery/dist/jquery.min';
import 'jbox/dist/jBox.all.css';
import jBox from 'jbox';
import * as module from '../module/index';

// 画像表示モーダル
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
    onOpen: function () {
        document.getElementById('header').style.paddingRight = '17px';
    },
    onClose: function () {
        document.getElementById('header').style.paddingRight = '0';
    },
    onCloseComplete: function () {
        const parent = document.getElementById('js-imgModal');
        while (parent.firstChild) {
            parent.removeChild(parent.firstChild)
        }
    }
});

// 画像アップロードモーダル
const uploadModal = new jBox('Modal', {
    id: 'upload',
    width: 825,
    height: 510,
    minHeight: 510,
    attach: '#js-uploadModalIcon',
    title: 'Add Images',
    content: $('#js-uploadModal'),
    overlayClass: 'add-jboxOverRay',
    closeOnClick: false,
    closeButton: 'box',
    createOnInit: true,
    onOpen: function () {
        document.getElementById('header').style.paddingRight = '17px';
    },
    onClose: function () {
        document.getElementById('header').style.paddingRight = '0';
    },
});


let formData = null;

// インプットしたファイル情報を表示
const inputFormData = (e) => {
    const files = e.target.files || e.dataTransfer.files;
    const fileSizeElem = document.getElementById('js-totalSize');
    let totalSize = Number.parseInt(fileSizeElem.dataset.rawsize);

    const template =
        `<li class="inputFile">
                <i class="icon-file-picture"></i>
                <div>
                    <div class="fileInfo">
                        <span class="fileName"></span>
                        <span class="fileSize"></span>
                    </div>
                </div>
            </li>`
    const templateList = module.generateImageTemplateList(template, files.length);

    const fileListElement = document.getElementById('js-fileList');
    for (let i = 0; i < files.length; i++) {
        templateList[i].querySelector('.fileName').textContent = files[i].name;
        templateList[i].querySelector('.fileSize').textContent = module.readableFileSize(files[i].size);
        totalSize += Number.parseInt(files[i].size);
        fileListElement.appendChild(templateList[i]);
    }
    formData = new FormData();
    for (let i = 0; i < files.length; i++) {
        formData.append('images', files[i]);
    }
    fileSizeElem.dataset.rawsize = totalSize.toString();
    fileSizeElem.textContent = module.readableFileSize(totalSize);
}

// フォームデータをnull & 画面表示からファイル情報を削除
const clearFormData = () => {
    formData = null;
    const parent = document.getElementById('js-fileList');
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild)
    }
    const fileSizeElem = document.getElementById('js-totalSize');
    fileSizeElem.textContent = '0';
    fileSizeElem.dataset.rawsize = '0';
}


// バックエンドへアップロードリクエスト
const uploader = (e) => {
    if (!formData)
        return false;
    uploadModal.ajax({
        type: 'POST',
        url: '/action/upload',
        data: formData,
        contentType: false,
        processData: false,
        spinner:
            `<img class="c-ajaxLoading" src="../../images/preloader32.gif" alt="">`,
        setContent: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(
                $("meta[name='_csrf_header']").attr("content"),
                $("meta[name='_csrf']").attr("content")
            );
        },
        success: function () {
            clearFormData();
            alert('Uploaded successfully!');
        },
        error: function () {
            alert('Critical error occurred. Please reload.');
        }
    })
}


const displaySwitchIcons = document.getElementsByName('js-displaySwitcher');
const imageIcons = document.querySelectorAll('.imageIcon');
const dropArea = document.getElementById('js-dropArea');
const fileInputBtn = document.getElementById('js-fileInput');
const fileUploadBtn = document.getElementById('js-upload');
const fileInputClearBtn = document.getElementById('js-clear');
const reloadBtn = document.getElementById('js-reload');

// 画像リスト<=>タイル表示
displaySwitchIcons.forEach((icon) => {
    icon.addEventListener('click', (e) => {
        const id = e.currentTarget.dataset.sectionId;
        document.getElementsByName('js-content').forEach((elem) => {
            elem.classList.remove('is-show');
        });
        document.getElementById(id).classList.add('is-show');
    })
})


// 画像モーダル表示クリックイベントを登録
imageIcons.forEach( (icon) => {
    icon.addEventListener('click', (e) => {
        const img = e.currentTarget.dataset.url;
        let htmlImageElement = document.createElement('img');
        htmlImageElement.src = img;
        document.getElementById('js-imgModal').appendChild(htmlImageElement);
        imgModal.open();
    })
});


dropArea.addEventListener('dragover', (e) => {
    e.preventDefault();
    e.currentTarget.classList.add('is-drag');
});
dropArea.addEventListener('dragleave', (e) => {
    e.preventDefault();
    e.currentTarget.classList.remove('is-drag');
})
dropArea.addEventListener('drop', (e) => {
    e.preventDefault();
    e.currentTarget.classList.remove('is-drag');
    inputFormData(e);
});

fileInputBtn.addEventListener('change', inputFormData);
fileUploadBtn.addEventListener('click', uploader);
fileInputClearBtn.addEventListener('click', clearFormData);
reloadBtn.addEventListener('click', () => {
    location.reload();
})

// 結果メッセージ表示モーダル
module.showResultMessageModal('.c-message', '.c-message-success', '.c-message-error', true);