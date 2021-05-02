import $ from 'jquery/dist/jquery.min';
import 'jbox/dist/jBox.all.css';
import jBox from 'jbox';


const userDetailModal = new jBox('Modal', {
    id: 'userDetail',
    width: 810,
    height: 780,
    attach: '.p-admin__userList__data',
    content: $('#js-userDetailModal'),
    overlayClass: 'add-jboxOverRay',
    closeOnClick: false,
    closeButton: 'box',
    createOnInit: true,
    onClose: function () {
        const parent = document.getElementById('js-userDetailTable');
        while (parent.firstChild) {
            parent.removeChild(parent.firstChild);
        }
    }
});

const textEdit = (text) => {
    if(text === 'id')
        return "ID";

    let customText = text.charAt(0).toUpperCase() + text.slice(1);
    switch (customText) {
        case 'Passwordupdatedat':
            customText = 'Password Updated At';
            break;
        case 'Signinmisstimes':
            customText = 'Signin Miss Times';
            break;
        case 'Updatedat':
            customText = 'Updated At';
            break;
        case 'Createdat':
            customText = 'Created At'
            break;
    }
    return customText;
}

const buildUserData = (e) => {
    const data = e.currentTarget.dataset;
    for(const prop in data) {
        let tr = document.createElement('tr');
        let th = document.createElement('th');
        let td = document.createElement('td');
        th.innerText = textEdit(prop);
        td.innerText = data[prop];
        tr.appendChild(th);
        tr.appendChild(td);
        document.getElementById('js-userDetailTable').appendChild(tr);
    }
}


document.querySelectorAll('.p-admin__userList__data').forEach((f) =>{
    f.addEventListener('click', buildUserData)
})