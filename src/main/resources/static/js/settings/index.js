console.log('from settings');

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