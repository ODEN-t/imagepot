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
    width: 725,
    height: 448,
    attach: '#js-upload',
    title: 'Add Images',
    content: $('#js-uploadModal'),
    overlayClass: 'add-jboxOverRay',
    closeOnClick: false,
    closeButton: 'box',
    createOnInit: true,
});

let formData = null;

// インプットしたファイル情報を表示
const inputFormData = (e) => {
    const files = e.target.files;
    const fileSizeElem = document.getElementById('js-totalSize');
    let totalSize = Number.parseInt(fileSizeElem.dataset.rawsize);

    // if(30 < files.length) {
    //     return alert('Please select less than 30 files.');
    // }

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
    console.log(totalSize);
}

// フォームデータをnull & 画面表示からファイル情報を削除
const clearFormData = () => {
    formData = null;
    const parent = document.getElementById('js-fileList');
    while(parent.firstChild){
        parent.removeChild(parent.firstChild)
    }
    const fileSizeElem = document.getElementById('js-totalSize');
    fileSizeElem.textContent = '0';
    fileSizeElem.dataset.rawsize = '0';
}

// バックエンドへアップロードリクエスト
const uploader = (e) => {
    if(!formData)
        return false;
    uploadModal.ajax({
        type: 'POST',
        url: '/action/upload',
        data: formData,
        contentType: false,
        processData: false,
        spinner: true,
        setContent: false,
        success: function () {
            clearFormData();
            alert('Uploaded successfully!');
        },
        error: function (data) {
            alert('Critical error occurred. Please reload.');
        }
    })
}

// 画像モーダル表示クリックイベントを登録
document.querySelectorAll('.imageIcon').forEach(function (elem) {
    elem.addEventListener('click', (e) => {
        const img = e.currentTarget.dataset.url;
        let htmlImageElement = document.createElement('img');
        htmlImageElement.src = img;
        document.getElementById('js-imgModal').appendChild(htmlImageElement);
        imgModal.open();
    })
});

document.getElementById('fileInput').addEventListener('change', inputFormData);
document.getElementById('js-execute').addEventListener('click', uploader);
document.getElementById('js-clear').addEventListener('click', clearFormData);

// 結果メッセージ表示モーダル
module.showResultMessageModal('.c-message', '.c-message-success', '.c-message-error');