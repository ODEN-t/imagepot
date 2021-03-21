import jBox from 'jbox';
import 'jbox/dist/jBox.all.css';
import $ from 'jquery/dist/jquery.min';

new jBox('Modal', {
    width: 405,
    height: 573,
    attach: '#signup',
    content: $('#modalContent'),
    overlayClass: 'add-jboxOverRay',
    closeOnClick: false,
    closeButton: 'box'
});