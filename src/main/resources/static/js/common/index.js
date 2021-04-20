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

// 縦、横、長い方に合わせて比率を保って画像をリサイズ
const getSize = (width, height, MAX_SIZE) => {
    let size = {};
    if (width > height) {
        size.width = MAX_SIZE;
        size.height = MAX_SIZE * (height / width);
    } else {
        size.width = MAX_SIZE * (width / height);
        size.height = MAX_SIZE;
    }
    return size;
}

// canvasでリサイズ後画像を出力
const renderImage = (event) => {
    let file = null;
    let blob = null;
    const MAX_SIZE = 200;

    file = event.target.files[0];
    if (!(file.type == 'image/jpeg' || 'image/png')) return;

    let image = new Image();
    let reader = new FileReader();
    reader.onload = (e) => {
        image.onload = () => {
            let size = getSize(image.width, image.height, MAX_SIZE);
            let width = size.width, height = size.height;
            let canvas = document.getElementById('canvas');
            canvas.width = width;
            canvas.height = height;
            let ctx = canvas.getContext('2d');

            // 描画をクリア
            ctx.clearRect(0, 0, width, height);

            // canvasに縮小画像を描画する
            ctx.drawImage(image,
                0, 0, image.width, image.height,
                0, 0, width, height
            );

            // canvasから画像をbase64として取得する
            var base64 = canvas.toDataURL('image/jpeg');

            // base64から画像データを作成する
            var barr, bin, i, len;
            bin = atob(base64.split('base64,')[1]);
            len = bin.length;
            barr = new Uint8Array(len);
            i = 0;
            while (i < len) {
                barr[i] = bin.charCodeAt(i);
                i++;
            }
            blob = new Blob([barr], { type: 'image/jpeg' });
        }
        image.src = e.target.result;
    }
    reader.readAsDataURL(file);
}
const inputImage = document.getElementById('inputImage');
inputImage.addEventListener('change', renderImage);
