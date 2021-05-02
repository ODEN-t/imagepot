import 'jbox/dist/jBox.all.css';
import jBox from 'jbox';
import 'cropperjs/dist/cropper.css';
import Cropper from 'cropperjs';

console.log('from settings');

// cropper の設定値
const crop = {
    crop: null,
    init(image) {
        this.crop = new Cropper(image, {
            aspectRatio: 1,
            viewMode: 1,
            background: false,
            guides: false,
            modal: true,
            highlight: false,
            movable: false,
            rotatable: false,
            scalable: false,
            zoomable: false,
            cropBoxResizable: false,
        });
        // 画像 & cropperロード後発火、cropエリア固定
        image.addEventListener('ready', function () {
            let width = document.querySelector('.cropper-container').clientWidth;
            let height = document.querySelector('.cropper-container').clientWidth;
            this.cropper.setCropBoxData({
                width: width, height: height
            });
            document.getElementById('js-newIconSubmit').addEventListener('click', () => {
                const cropBoxData = this.cropper.getCropBoxData();
                const cropedCanvas = this.cropper.getCroppedCanvas({
                    width: cropBoxData.width,
                    height: cropBoxData.height
                });
                cropedCanvas.toBlob((b) => {
                    const formData = new FormData();
                    formData.append('croppedImage', b);
                    console.log(formData);
                    $.ajax('/settings/upload/newicon', {
                        method: 'POST',
                        data: formData,
                        processData: false,
                        contentType: false,
                        success() {
                            console.log('upload success');
                        },
                        error() {
                            console.log('upload error');
                        }
                    })
                });
            })
        })
    },
    destroy() {
        this.crop.destroy();
    }
}

// jBox の設定値
const cropModal = new jBox('Modal', {
    id: "cropModal",
    width: 450,
    maxHeight: 500,
    responsiveHeight: true,
    fixed: true,
    title: 'Crop your new icon',
    content: $('#js-cropModal'),
    footer: $('#js-newIconSubmit'),
    overlayClass: 'add-jboxOverRay',
    delayOpen: 150,
    closeOnClick: false,
    closeButton: 'box',
    createOnInit: true,
    reposition: true,
    repositionOnOpen: true,
    onOpen: function () {
        let image = document.getElementById('js-cropImage');
        crop.init(image);
    },
    onClose: function () {
        crop.destroy();
    }
});


document.getElementById('fileInput').addEventListener('change', (e) => {
    const file = e.target.files[0];
    const LIMIT_MB = 1; // 1MB
    const LIMIT = LIMIT_MB * 1024 * 1024; // B to MB

    if (!(file.type == 'image/jpeg' || file.type == 'image/png'))
        return window.alert('Mime type is not image/jpeg or image/png');

    if (file.size > LIMIT)
        return window.alert('File size is too large. The maximum supported file size are ' + LIMIT_MB.toString() + 'MB.');

    let fileReader = new FileReader();
    fileReader.onload = () => {
        document.getElementById('js-cropImage').src = fileReader.result;
        cropModal.open();
    }
    fileReader.readAsDataURL(file);
});



// type=password <=> type=text の切り替え
const inputTypeToggle = (elementClass) => {
    const nodeList = document.querySelectorAll(elementClass);
    for (const node of nodeList) {
        node.addEventListener('click', () => {
            const target = node.previousElementSibling;
            target.type == 'password' ? target.type = 'text' : target.type = 'password';
        })
    }
}
inputTypeToggle('.buttonCTA-show');