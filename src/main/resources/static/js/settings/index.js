import $ from 'jquery/dist/jquery.min';
import 'jbox/dist/jBox.all.css';
import jBox from 'jbox';
import 'cropperjs/dist/cropper.css';
import Cropper from 'cropperjs';
import * as module from '../module/index';


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
            let w = document.querySelector('.cropper-container').clientWidth;
            let h = document.querySelector('.cropper-container').clientWidth;
            this.cropper.setCropBoxData({
                width: w, height: h
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

                    $.ajax('/settings/update/user/icon', {
                        method: 'POST',
                        data: formData,
                        processData: false,
                        contentType: false
                    }).done(() => {
                            window.location.href = "/settings";
                    }).fail(() => {
                            alert('Critical error occurred. Please reload.')
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
    if (!(file.type === 'image/jpeg' || file.type === 'image/png'))
        return window.alert('File type is not image/jpeg or image/png');

    let fileReader = new FileReader();
    fileReader.onload = () => {
        document.getElementById('js-cropImage').src = fileReader.result;
        cropModal.open();
    }
    fileReader.readAsDataURL(file);
});


// type=password <=> type=text の切り替え
module.inputTypeToggle('.buttonCTA-show');

// show result message from backend with modal
module.showResultMessageModal('.c-message', '.c-message-success', '.c-message-error');