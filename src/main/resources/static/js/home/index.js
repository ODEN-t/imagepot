import Masonry from 'masonry-layout/masonry';
import imagesLoaded from 'imagesloaded/imagesloaded';

console.log('from home');

const loadingMask = document.querySelector('.loadingMask');
const container = document.querySelector('.p-home__postedItem');
const imgLoaded = imagesLoaded(container);

imgLoaded.on('always', () => {
    console.log('loaded alwaaays');
    let msnry = new Masonry( '.p-home__postedItem', {
        itemSelector: '.grid-item',
        columnWidth: '.grid-sizer',
        gutter: '.gutter-sizer',
        horizontalOrder: true,
        percentPosition: true,
    });

    msnry.on('layoutComplete', function () {
        loadingMask.classList.add('is-loaded');
    });

    msnry.layout();
});

imgLoaded.on('fail', (instance, image) => {
    console.log('fail');
    console.log(instance)
    console.log(image);
})

imgLoaded.on('progress', function (instance, image) {
    if(!image.isLoaded) {
        image.img.src = "/images/image-not-available.jpg";
    }
})

// imgLoaded.on('done', () => {
//     console.log('done');
//     let msnry = new Masonry( '.p-home__postedItem', {
//         itemSelector: '.grid-item',
//         columnWidth: '.grid-sizer',
//         gutter: '.gutter-sizer',
//         horizontalOrder: true,
//         percentPosition: true,
//     });
//
//     msnry.on('layoutComplete', function () {
//         loadingMask.classList.add('is-loaded');
//     });
//
//     msnry.layout();
// })

