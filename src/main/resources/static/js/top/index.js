import jBox from 'jbox';
import 'jbox/dist/jBox.all.css';
import $ from 'jquery/dist/jquery.min';




// $('#signup').on('click', function () {
//     console.log('click!f!');
// })



// let b = document.getElementsByClassName('buttonCTA');
// console.log(b);

new jBox('Modal', {
    width: 470,
    height: 500,
    attach: '#signup',
    content: $('#modalContent'),
    overlayClass: 'add-jboxOverRay',
    closeOnClick: false,
    closeButton: 'box'
});

// console.log("log");