document.getElementById("registrationForm")
    .addEventListener("submit", (event) => {
        event.preventDefault();

        const name = document.getElementById("name").value;
        const phoneEmail = document.getElementById("phoneEmail").value;
        const password = document.getElementById("password").value;
        const confirmPassword = document.getElementById("confirmPassword").value;
        const errorTextTag = document.getElementById("errorText");
        const errorEmail = document.getElementById("errorEmail");

        if (password !== confirmPassword){
            console.log("teng emas")
            errorTextTag.textContent = "Parollar bir xil emas!";
            errorTextTag.style.display = "block";
            document.getElementById("confirmPassword").style.borderColor = "red";
            document.getElementById("password").style.borderColor = "red";
            return
        }else {
            console.log("parollar teng!")
            errorTextTag.style.display = "none";
            document.getElementById("confirmPassword").style.borderColor = "#ddd";
            document.getElementById("password").style.borderColor = "#ddd";
        }


        const body = {
            "name" : name,
            "username" : phoneEmail,
            "password" : password
        }
        console.log("body = " + body)
        console.log("\n")
        console.log("JSOn = " + JSON.stringify(body))
        fetch("http://localhost:8080/auth/registration",{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)

        })
            .then(response =>{
                if (response.ok){
                    return response.json()
                }else {
                    errorEmail.style.display = "block";
                    document.getElementById("phoneEmail").style.borderColor = "red";
                    document.getElementById("phoneEmail").style.color = "red";
                    errorEmail.textContent = "Username already exists";
                    return Promise.reject(response.text());
                }
            })
            .then(res => {
                console.log(res.data)
                alert("sizning " + phoneEmail + " elektron pochtangizga xabar yuborildi!");
                errorEmail.style.display = "none";
                document.getElementById("phoneEmail").style.borderColor = "#ddd";
                document.getElementById("phoneEmail").style.color = "";
            })
            .catch(error =>{
                error.then(errorMessage =>{

                    console.log(errorMessage)
                })
            })

    });


function checkEmailOrPhone(value) {
    // Regular expression for validating email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    // Regular expression for validating phone numbers
    // Adjust based on your requirements (e.g., country-specific formats)
    const phoneRegex = /^998\d{9}$/; // 998 91 572 1213

    if (emailRegex.test(value)) {
        return "Email";
    } else if (phoneRegex.test(value)) {
        return "Phone";
    } else {
        return "Invalid";
    }
}