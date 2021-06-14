import $ from 'jquery/dist/jquery.min';
import 'jbox/dist/jBox.all.css';
import jBox from 'jbox';

// Logout Modal の設定値
const signoutPopup = new jBox('Confirm', {
    title: 'Sign Out',
    attach: $('#logout'),
    confirmButton: 'SIGN OUT',
    cancelButton: 'CANCEL',
    overlayClass: 'add-jboxOverRay',
    addClass: 'add-popupCustomStyle',
    onOpen: function () {
        document.getElementById('header').style.paddingRight = '17px';
    },
    onClose: function () {
        document.getElementById('header').style.paddingRight = '0';
    }
});

document.querySelectorAll('.icon').forEach(function (elem) {
    elem.addEventListener('click', function (e){
        document.getElementById('menu').classList.toggle('is-open');
    })
})