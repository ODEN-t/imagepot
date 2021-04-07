import jBox from 'jbox';
import 'jbox/dist/jBox.all.css';
import $ from 'jquery/dist/jquery.min';

const ass = document.getElementById('signout');
ass.addEventListener('click', () => {
    console.log("click");
})

// Signup Modal の設定値
const signoutPopup = new jBox('Confirm', {
    title: 'Sign Out',
    attach: '#signout',
    confirmButton: 'SIGN OUT',
    cancelButton: 'CANCEL',
    overlayClass: 'add-jboxOverRay',
    addClass: 'add-popupCustomStyle'
});
