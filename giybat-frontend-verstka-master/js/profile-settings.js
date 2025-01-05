window.onload = function () {
    const userDetail = localStorage.getItem("userDetail");
    if (!userDetail) {
        return;
    }

    const userDetailObj = JSON.parse(userDetail);
    console.log(userDetailObj);

    document.getElementById("profile_settings_name").textContent = userDetailObj.name;
    document.getElementById("profile_settings_username").textContent = userDetailObj.username;

};


function goToProfileDetailEditPage(){
    window.location.href = "./profile-settings-edit.html";
}