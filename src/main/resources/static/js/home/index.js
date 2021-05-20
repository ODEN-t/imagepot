import 'jbox/dist/jBox.all.css';
import jBox from 'jbox';
import $ from 'jquery/dist/jquery.min';

/***
 * 引数に渡したテンプレートを引数numberの数だけ配列に詰め込み返す
 * @param {String} template 要素を表現したテンプレートリテラル
 * @param {Number} number 量産したいtemplateの数
 * @return []
 */
const generateImageTemplateList = (template, number) => {
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
const WrapNodesInEachUnit = (listOfElement, unit, wrapperTemplate) => {
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

const readableFileSize = (size) => {
    const units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    let i = 0;
    while (size >= 1024) {
        size /= 1024;
        ++i;
    }
    return size.toFixed(1) + ' ' + units[i];
}

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

const inputFormData = (e) => {
    const files = e.target.files;
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
    const templateList = generateImageTemplateList(template, files.length);

    const fileListElement = document.getElementById('js-fileList');
    for (let i = 0; i < files.length; i++) {
        templateList[i].querySelector('.fileName').textContent = files[i].name;
        templateList[i].querySelector('.fileSize').textContent = readableFileSize(files[i].size);
        fileListElement.appendChild(templateList[i]);
    }

    formData = new FormData();
    for (let i = 0; i < files.length; i++) {
        formData.append('images', files[i]);
    }
}

const uploader = (e) => {
    if(!formData)
        return false;
    uploadModal.ajax({
        type: 'POST',
        url: '/home/upload',
        data: formData,
        contentType: false,
        processData: false,
        spinner: true,
        setContent: false,
        success: function () {
            clearFormData();
        }
    })
}

const clearFormData = () => {
    formData = null;
    const parent = document.getElementById('js-fileList');
    while(parent.firstChild){
        parent.removeChild(parent.firstChild)
    }
}


document.getElementById('fileInput').addEventListener('change', inputFormData);
document.getElementById('js-execute').addEventListener('click', uploader);
document.getElementById('js-clear').addEventListener('click', clearFormData);


document.addEventListener('DOMContentLoaded', () => {
    const wrapperTemplate = `<div class="p-home__postedItem js-intersect"></div>`;
    const childTemplate = `<figure><div class="p-home__postedImg"></div></figure>`;
    const UNIT = 15; // 一度にrenderする画像の数
    WrapNodesInEachUnit(generateImageTemplateList(childTemplate, imageList.length), UNIT, wrapperTemplate);

    const options = {
        root: null,
        rootMargin: '0px',
        threshold: 0.25
    };

    const intersectionObserver = new IntersectionObserver(renderImage, options);
    document.querySelectorAll('.js-intersect').forEach((e) => {
        intersectionObserver.observe(e);
    });

    // IntersectionObserverのメインロジック
    // スクロール毎にimgタグを生成し画像を表示
    function renderImage(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                let parent = entry.target;
                let children = parent.querySelectorAll('.p-home__postedImg');
                let count = 0;
                for (let i = 0; i < children.length; i++) {
                    const img = document.createElement('img');
                    img.src = imageList[i];
                    children[i].appendChild(img);

                    img.addEventListener('load', () => {
                        imageList.shift();
                        count++;
                        if (count === children.length)
                            parent.classList.add('is-loaded');
                    });
                }

                intersectionObserver.unobserve(entry.target);
            }
        })
    }
});