window.onload = function () {
    const userDetailJon = localStorage.getItem("userDetail");
    if (!userDetailJon) {
        return;
    }

    const userDetailObj = JSON.parse(userDetailJon);

    document.getElementById("profile_settings_name").value = userDetailObj.name;
    document.getElementById("profile_settings_username").value = userDetailObj.username;
};

function profileDetailUpdate() {
    const name = document.getElementById("profile_settings_name").value
    const jwtToken = localStorage.getItem("jwtToken");
    const nameErrorSpan = document.getElementById("nameErrorSpan")
    if (!name ) {
        return;
    }
    if (!jwtToken) {
        window.location.href = "./login.html";
        return;
    }
    console.log(name)

    const body = {
        "name" : name
    }

    const lang = document.getElementById("current-lang").textContent


    fetch("http://localhost:8080/profile/detail",{
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Accept-Language': lang,
            'Authorization': "Bearer " + jwtToken
        },
        body: JSON.stringify(body)

    })
        .then(response =>{
            if (response.ok){
                return response.json()
            }else {
                return Promise.reject(response.text());
            }
        })
        .then(data => {
            alert(data.data)
            nameErrorSpan.style.display = "block";
            document.getElementById("nameErrorSpan").style.borderColor = "#ddd";
            document.getElementById("nameErrorSpan").style.color = "";
            // userDetail obyektini olish
            const userDetail = JSON.parse(localStorage.getItem("userDetail"));

            // Obyektdagi name maydonini yangilash
            userDetail.name = name;

            // Yangilangan obyektni localStorage'ga qayta yozish
            localStorage.setItem("userDetail", JSON.stringify(userDetail));

            const headerUserNameSpan = document.getElementById("header_user_name_id");
            headerUserNameSpan.textContent = name;

        })
        .catch(error =>{
            error.then(errorMessage =>{
                console.log(errorMessage.toString());
                nameErrorSpan.style.display = "block";
                document.getElementById("nameErrorSpan").style.color = "red";
                nameErrorSpan.textContent = errorMessage.toString();
            })
        })

}

function profilePasswordUpdate() {
    const currentPswd = document.getElementById("profile_settings_current_pswd").value
    const newPswd = document.getElementById("profile_settings_new_pswd").value
    if (!currentPswd || !newPswd) {
        alert("Enter all inputs")
        return;
    }
}

function profileUserNameChange() {
    const username = document.getElementById("profile_settings_username").value

}

function profileUserNameChangeConfirm() {
    const confirmCode = document.getElementById("profileUserNameChaneConfirmInputId").value
    if (!confirmCode) {
        alert("Enter all inputs")
        return;
    }

}

//------------ Change username confirm modal start ------------
const modal = document.getElementById('simpleModalId');

function openModal() {
    modal.style.display = 'block';
}

function closeModal() {
    modal.style.display = "none";
}

window.onclick = (event) => {
    if (event.target === modal) {
        modal.style.display = 'none';
    }
};

//------------ Change username confirm modal end ------------