// import * as module from '../module/index';
//
//
// document.addEventListener('DOMContentLoaded', () => {
//     const wrapperTemplate = `<div class="p-home__postedItem js-intersect"></div>`;
//     const childTemplate = `<figure><div class="p-home__postedImg"></div></figure>`;
//     const UNIT = 15; // 一度にrenderする画像の数
//     module.WrapNodesInEachUnit(module.generateImageTemplateList(childTemplate, urlList.length), UNIT, wrapperTemplate);
//
//     const options = {
//         root: null,
//         rootMargin: '0px',
//         threshold: 0.25
//     };
//
//     const intersectionObserver = new IntersectionObserver(renderImage, options);
//     document.querySelectorAll('.js-intersect').forEach((e) => {
//         intersectionObserver.observe(e);
//     });
//
//     // IntersectionObserverのメインロジック
//     // スクロール毎にimgタグを生成し画像を表示
//     function renderImage(entries) {
//         entries.forEach(entry => {
//             if (entry.isIntersecting) {
//                 let parent = entry.target;
//                 let children = parent.querySelectorAll('.p-home__postedImg');
//                 let count = 0;
//                 for (let i = 0; i < children.length; i++) {
//                     const img = document.createElement('img');
//                     img.src = urlList[i];
//                     children[i].appendChild(img);
//                     img.addEventListener('load', () => {
//                         urlList.shift();
//                         count++;
//                         if (count === children.length)
//                             parent.classList.add('is-loaded');
//                     });
//                 }
//                 intersectionObserver.unobserve(entry.target);
//             }
//         })
//     }
// });

