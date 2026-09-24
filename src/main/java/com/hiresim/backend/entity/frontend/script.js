const loginTab = document.getElementById("loginTab");
const registerTab = document.getElementById("registerTab");

const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");

const loginMessage = document.getElementById("loginMessage");
const registerMessage = document.getElementById("registerMessage");

// Live Backend URL
const API_BASE_URL = "https://hiresim.onrender.com";


// Login / Register tab switch

loginTab.addEventListener("click", function () {

    loginTab.classList.add("active");
    registerTab.classList.remove("active");

    loginForm.classList.remove("hidden");
    registerForm.classList.add("hidden");

});


// Register tab

registerTab.addEventListener("click", function () {

    registerTab.classList.add("active");
    loginTab.classList.remove("active");

    registerForm.classList.remove("hidden");
    loginForm.classList.add("hidden");

});


// Register

registerForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    const name = document.getElementById("registerName").value;
    const email = document.getElementById("registerEmail").value;
    const password = document.getElementById("registerPassword").value;

    try {

        const response = await fetch(
            `${API_BASE_URL}/auth/register`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    name: name,
                    email: email,
                    password: password
                })
            }
        );

        if (response.ok) {

            registerMessage.textContent =
                "Account created successfully!";

            registerForm.reset();

        } else {

            registerMessage.textContent =
                "Registration failed.";

        }

    } catch (error) {

        registerMessage.textContent =
            "Backend is not reachable.";

    }

});


// Login

loginForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;

    try {

        const response = await fetch(
            `${API_BASE_URL}/auth/login`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    email: email,
                    password: password
                })
            }
        );

        const result = await response.text();

        if (result === "Login successful") {

            window.location.href = "dashboard.html";

        } else {

            loginMessage.textContent = result;

        }

    } catch (error) {

        loginMessage.textContent =
            "Backend is not reachable.";

    }

});