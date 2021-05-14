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
const domList = generateImageTemplateList();
for(const img of domList) {
    const target = document.getElementById('js-masonry');
    target.appendChild(img);
}

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