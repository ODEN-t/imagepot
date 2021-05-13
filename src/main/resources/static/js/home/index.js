import $ from 'jquery/dist/jquery.min';


console.log('from home');

const showAllImages = () => {
    const domParser = new DOMParser();
    for(const img of imageList) {
        let template =
            `<figure>
                <div class="p-home__masonry__imgWrap">
                    <img src="${img}" alt="">
                </div>
            </figure> `;
        document.getElementById('js-masonry')
            .appendChild(domParser.parseFromString(template, "text/html").body.firstChild);
    }
}

showAllImages();