import $ from 'jquery/dist/jquery.min';
import 'jbox/dist/jBox.all.css';
import jBox from 'jbox';

// Logout Modal の設定値
const signoutPopup = new jBox('Confirm', {
    title: 'Logout',
    attach: $('#logout'),
    confirmButton: 'LOGOUT',
    cancelButton: 'CANCEL',
    overlayClass: 'add-jboxOverRay',
    addClass: 'add-popupCustomStyle'
});