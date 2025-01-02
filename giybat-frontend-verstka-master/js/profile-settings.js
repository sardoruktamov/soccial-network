window.onload = function () {
    const userDetailJon = localStorage.getItem("userDetail");
    if (!userDetailJon) {
        return;
    }

    const userDetailObj = JSON.parse(userDetailJon);
    console.log(userDetailObj);

    document.getElementById("profile_settings_name").textContent = userDetailObj.name;
    document.getElementById("profile_settings_username").textContent = userDetailObj.username;
};


function goToProfileDetailEditPage(){
    window.location.href = "./profile-settings-edit.html";
}