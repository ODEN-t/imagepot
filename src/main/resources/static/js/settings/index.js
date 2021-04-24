import 'cropperjs/dist/cropper.css';
import 'jbox/dist/jBox.all.css';
import $ from 'jquery/dist/jquery.min';
import Cropper from 'cropperjs';
import jBox from 'jbox';

console.log('from settings');

// IconSetting Modal の設定値
const cropModal = new jBox('Modal', {
    id: "test",
    width: 450,
    maxHeight: 500,
    responsiveHeight: true,
    fixed: false,
    title: 'Crop your new icon',
    content: $('#js-cropModal'),
    footer: '<button type="submit" class="buttonCTA buttonCTA-fullWidth">Set new icon</button>',
    overlayClass: 'add-jboxOverRay',
    delayOpen: 150,
    closeOnClick: false,
    closeButton: 'box',
    createOnInit: true,
    reposition: true,
    repositionOnOpen: true,
    position: { x: 'center', y: 'center' },
});

document.getElementById('fileInput').addEventListener('change', (e) => {
    const file = e.target.files[0];
    const LIMIT_MB = 1; // 1MB
    const LIMIT = LIMIT_MB * 1024 * 1024; // B to MB

    console.log(file.type);

    if (!(file.type == 'image/jpeg' || file.type == 'image/png'))
        return window.alert('Mime type is not image/jpeg or image/png');

    if (file.size > LIMIT)
        return window.alert('File size is too large. The maximum supported file size are ' + LIMIT_MB.toString() + 'MB.');

    let fileReader = new FileReader();
    fileReader.onload = () => {
        document.getElementById('preview').src = fileReader.result;
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