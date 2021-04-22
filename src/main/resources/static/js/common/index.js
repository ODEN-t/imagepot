import jBox from 'jbox';
import 'jbox/dist/jBox.all.css';
import $ from 'jquery/dist/jquery.min';

// Signout Modal の設定値
const signoutPopup = new jBox('Confirm', {
    title: 'Sign Out',
    attach: '#signout',
    confirmButton: 'SIGN OUT',
    cancelButton: 'CANCEL',
    overlayClass: 'add-jboxOverRay',
    addClass: 'add-popupCustomStyle'
});

// IconSetting Modal の設定値
const iconSettingModal = new jBox('Modal', {
    width: 405,
    height: 573,
    attach: '#icon',
    content: $('#iconSettingContent'),
    overlayClass: 'add-jboxOverRay',
    closeOnClick: false,
    closeButton: 'box',
    createOnInit: true,
});


const iconChange = {
    bin: null,
    renderImage(event) {
        const MAX_IMAGE_SIZE = 100;
        const LIMIT_MB = 5; // 5MB
        const LIMIT = LIMIT_MB * 1024 * 1024; // B to MB
        const file = event.target.files[0];

        if (!(file.type == 'image/jpeg' || 'image/png'))
            return window.alert('Mime type is not image/jpeg or image/png');

        if (file.size > LIMIT)
            return window.alert('File size is too large. The maximum supported file size are' + LIMIT_MB.toString + 'MB.');

        let image = new Image();
        let reader = new FileReader();
        reader.onload = (e) => {
            image.onload = () => {
                let width = MAX_IMAGE_SIZE, height = MAX_IMAGE_SIZE, canvas = document.getElementById('canvas');
                if (image.width > image.height)
                    height = MAX_IMAGE_SIZE * (image.height / image.width);
                else
                    width = MAX_IMAGE_SIZE * (image.width / image.height);

                canvas.setAttribute('width', width);
                canvas.setAttribute('height', height);

                let ctx = canvas.getContext('2d');

                // 描画をクリア
                ctx.clearRect(0, 0, width, height);

                // canvasに縮小画像を描画する
                ctx.drawImage(image,
                    0, 0, image.width, image.height,
                    0, 0, width, height
                );

                // canvasから画像をbase64として取得する
                let base64 = null;
                file.type == 'image/jpeg' ? base64 = canvas.toDataURL('image/jpeg') : base64 = canvas.toDataURL('image/png');
                console.log(base64);

                // base64をバイナリデータに変換
                //const bin = atob(base64.split('base64,')[1]);
                //console.log(bin);
            }
            image.src = e.target.result;
        }
        reader.readAsDataURL(file);
    },
    asyncPostUserIcon(event) {
        event.preventDefault();
        console.log(this.bin);
    }
}


const asyncPostUserIcon = (event) => {
    event.preventDefault();
    const bin = document.getElementById('canvas').toDataURL();
}




document.getElementById('inputImage').addEventListener('change', iconChange.renderImage);
document.getElementById('upload').addEventListener('click', iconChange.asyncPostUserIcon);
