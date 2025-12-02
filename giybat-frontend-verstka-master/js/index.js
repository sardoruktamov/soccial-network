window.onload = function () {
    getPostList();
};

window.addEventListener("DOMContentLoaded", function () {
    getPostList();
});

let currentPage = 1;

function getPostList() {
    const lang = document.getElementById("current-lang").textContent;
    const body = {
        "query": null
    }
    let size = 9;
    fetch('http://localhost:8080/posts/public/filter?page=' + currentPage + "&size=" + size, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept-Language': lang
        },
        body: JSON.stringify(body)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Success:', data);
            if (data.content && data.content.length > 0){
                showMainPost(data.content[0]);
                // data.content.shift()   // remove 0

                showPostList(data.content);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function showMainPost(mainPost) {
    const image = document.getElementById("main-card-imageId");
    if (mainPost.photo && mainPost.photo.url){
        image.src = mainPost.photo.url;
    }else {
        image.src = './images/post-default-img.jpg';
    }

    document.getElementById("main-card-dateId").textContent = mainPost.createdDate;
    document.getElementById("main-card-titleId").textContent.title;
    document.getElementById("main-card-detailBtnId").href = "/.post-detail.html?id=" + mainPost.id;
}

function showPostList(postList){
    const parent = document.getElementById("post_container_id")
    parent.innerHTML = '';
    postList.forEach(postItem => {
        // parent div
        const div = document.createElement("div");
        div.classList.add("post_box");
        //button
        const editButton = document.createElement("a");
        editButton.classList.add("profile_tab_btn");
        editButton.href = "./post-detail.html?id=" + postItem.id

        // image div
        const imageDiv = document.createElement("div");
        imageDiv.classList.add("post_img__box");
        // img
        const img = document.createElement("img");
        if (postItem.photo && !postItem.photo.id) {
            img.src = "./images/default-img.png";
        } else {
            img.src = postItem.photo.url;
        }
        img.alt = "Posts";
        img.classList.add("post_img");

        imageDiv.appendChild(img); // rasmni rasm konteyneriga qo‘shish

        // <h3> title
        const h3 = document.createElement("h3");
        h3.classList.add("post_title");
        h3.textContent = postItem.title;

        // <p> sana
        const p = document.createElement("p");
        p.classList.add("post_text");
        p.textContent = formatDate(postItem.createdDate);

        // a ga qo'shish
        a.appendChild(imageDiv)
        a.appendChild(h3)
        a.appendChild(p)
        // hamma elementlarni div ga qo‘shamiz
        div.appendChild(a)
        parent.appendChild(div);
    });
    console.log(postList)
}

