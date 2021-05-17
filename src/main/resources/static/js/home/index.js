import $ from 'jquery/dist/jquery.min';

const generateImageTemplateList = () => {
    let templateList = [];
    const domParser = new DOMParser();

    for (const img of imageList) {
        let template =
            `<figure>
                <div class="p-home__postedList__imgWrap">
                </div>
            </figure>`;
        templateList.push(domParser.parseFromString(template, "text/html").body.firstChild);
    }
    return templateList;
}

/**
 *　引数に渡した配列を指定した数毎にHTMLElementでラップしHTML要素として追加する
 *
 * @param listOfElement HTMLElementを詰めた配列
 * @param unit 何個ずつwrapしたいかを指定
 * @return map
 */
const WrapNodesInUnit = (listOfElement, unit, imageList) => {
    const contents = document.getElementById('js-postedList');
    const NUMBER_OF_ELEMENTS = listOfElement.length;
    const NUMBER_OF_ELEMENTS_IN_SECTION = unit;
    const NUMBER_OF_SECTIONS = Math.ceil(NUMBER_OF_ELEMENTS / NUMBER_OF_ELEMENTS_IN_SECTION);
    const domParser = new DOMParser();
    let section = 0;
    let eachNum = unit;
    let elemNum = 0;
    const map = new Map;
    let tmp = [];

    console.log("NUMBER_OF_SECTIONS -> " + NUMBER_OF_SECTIONS);
    console.log("NUMBER_OF_ELEMENTS -> " + NUMBER_OF_ELEMENTS)

    while (section <= NUMBER_OF_SECTIONS) {
        let template =
            `<div class="p-home__postedList__section" data-interSection="${section}"></div>`;
        let wrapper = domParser.parseFromString(template, "text/html").body.firstChild;

        while (elemNum < eachNum) {
            if (NUMBER_OF_ELEMENTS <= elemNum) break;
            wrapper.appendChild(listOfElement[elemNum]);
            tmp.push(imageList[elemNum]);
            elemNum++;
        }
        map.set(`section${section}`, tmp);
        tmp = [];
        contents.appendChild(wrapper);

        // if(section === 1) break;

        if (NUMBER_OF_ELEMENTS <= elemNum) break;
        eachNum += NUMBER_OF_ELEMENTS_IN_SECTION;
        section++;
    }

    return map;
}

document.addEventListener('DOMContentLoaded', () => {
    const UNIT = 15;
    const map = WrapNodesInUnit(generateImageTemplateList(), UNIT, imageList);
    console.log(map);


    let parent = document.querySelector('[data-intersection="0"]');
    let element = document.querySelectorAll('[data-intersection="0"] .p-home__postedList__imgWrap');
    const NUMBER_OF_IMAGES = element.length;
    let counter = 0;

    console.log(parent.querySelectorAll('.p-home__postedList__imgWrap'));

    const options = {
        root: null,
        rootMargin: '0px',
        threshold: 0.15
    }

    const intersectionObserver = new IntersectionObserver(renderImage, options);
    function renderImage(changes) {
        changes.forEach(change => {
            if(change.isIntersecting)
                console.log("intersect!");
        })
    }

    for (let i = 0; i < NUMBER_OF_IMAGES; i++) {
        const img = document.createElement('img');
        img.src = imageList[i];
        element[i].appendChild(img);
        img.addEventListener('load', function () {
            counter++;
            if(counter === NUMBER_OF_IMAGES) {
                parent.classList.add('is-loaded');
                console.log("load" + counter);
                let elem = document.querySelectorAll('.p-home__postedList__section');
                elem.forEach((e) => {
                    intersectionObserver.observe(e);
                })
            }
        })
    }


})

// const func = () => {
//     const target = document.getElementById('js-masonry');
//     const domList = generateTemplateList();
//     // console.log(domList);
//
//     for(const img of domList) {
//         target.appendChild();
//     }
//
//
//     const options = {
//         root: null,
//         rootMargin: 0,
//         threshold: [0.2, 1.0]
//     }
//
//     const observer = new IntersectionObserver(entries => {
//         entries.forEach(entry => {
//             console.log(entry);
//             console.log(entry.isIntersecting);
//             console.log(entry.intersectionRatio);
//         }, options);
//     });
//
//     observer.observe(target);
//
//
//     // let i = 0;
//     // const max = 30;
//     //
//     // const loadContent = async () => {
//     //
//     //     contents.appendChild(domParser.parseFromString(domList[i], "text/html").body.firstChild);
//     //
//     //     i++;
//     //
//     //     if ( i < max )
//     //         infiniteScrollObserver.observe(contents.lastElementChild);
//     //
//     // };
//
//     //
//     // loadContent();
// }
//
// func();