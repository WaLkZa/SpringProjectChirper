$(document).ready(function () {
    $(".modal-link").on("click", clickFunc);

    function clickFunc() {
        let fullUrl = $(this).prop("href");
        let listRoute = $(this).attr("href").split("/")[2];

        let title = '';
        let emptyListInfo = '';

        if (listRoute === 'followingList') {
            title = 'Following';
            emptyListInfo = 'No users';
        } else if (listRoute === 'followersList') {
            title = 'Followers';
            emptyListInfo = 'No users';
        } else if (listRoute === 'listLikes') {
            title = 'Likes';
            emptyListInfo = 'No likes';
        }

        let titleElement = $('.modal-content .titlebar');
        titleElement.empty();
        titleElement.append(title);

        let usersModal = $('.userbox-modal');
        usersModal.empty();

        fetch(fullUrl)
            .then((response) => response.json())
            .then((json) => {

                if (json.length === 0) {
                    usersModal.append(`<p>${emptyListInfo}`)
                } else {
                    for (let i = 0; i < json.length; i++) {
                        usersModal.append(`<div class="userbox"><div><a href="/profile/${json[i].username}" class="chirp-author">${json[i].username}</a></div></div>`)
                    }
                }
            }).catch(function () {
            usersModal.append(`<p>Something went wrong while fetching data!`)
        });

        return false;
    }
});