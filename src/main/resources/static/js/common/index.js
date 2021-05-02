import jBox from 'jbox';
import 'jbox/dist/jBox.all.css';
import $ from 'jquery/dist/jquery.min';

// Logout Modal の設定値
const signoutPopup = new jBox('Confirm', {
    title: 'Logout',
    attach: '#logout',
    confirmButton: 'LOGOUT',
    cancelButton: 'CANCEL',
    overlayClass: 'add-jboxOverRay',
    addClass: 'add-popupCustomStyle'
});