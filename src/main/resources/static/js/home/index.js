import $ from 'jquery/dist/jquery.min';

const generateImageTemplateList = () => {
    let isFirst = true;
    let templateList = [];
    const domParser = new DOMParser();

    for(const img of imageList) {
        if(isFirst) {
            isFirst = false;
            continue;
        }
        let template =
            `<figure>
                <div class="p-home__masonry__imgWrap">
                    <img src="${img}" alt="">
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
 * @return void
 */
const WrapNodesInUnit = (listOfElement, unit) => {
    const contents = document.getElementById('js-masonry');
    const domList = listOfElement;
    const NUMBER_OF_ELEMENTS = domList.length;
    const NUMBER_OF_ELEMENTS_IN_SECTION = unit;
    const NUMBER_OF_SECTIONS = Math.ceil(NUMBER_OF_ELEMENTS / NUMBER_OF_ELEMENTS_IN_SECTION);
    const domParser = new DOMParser();
    let section = 0;
    let eachNum = 15;
    let elemNum = 0;

    console.log("NUMBER_OF_SECTIONS -> " + NUMBER_OF_SECTIONS);
    console.log("NUMBER_OF_ELEMENTS -> " + NUMBER_OF_ELEMENTS)

    while (section <= NUMBER_OF_SECTIONS) {
        let template =
            `<div data-interSection="${section}"></div>`;
        let wrapper = domParser.parseFromString(template, "text/html").body.firstChild;

        while(elemNum < eachNum) {
            if(NUMBER_OF_ELEMENTS <= elemNum) break;
            wrapper.appendChild(domList[elemNum]);
            elemNum++;
        }

        contents.appendChild(wrapper);

        if(NUMBER_OF_ELEMENTS <= elemNum) break;
        eachNum += NUMBER_OF_ELEMENTS_IN_SECTION;
        section++;
    }
}

WrapNodesInUnit(generateImageTemplateList(), 15);

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