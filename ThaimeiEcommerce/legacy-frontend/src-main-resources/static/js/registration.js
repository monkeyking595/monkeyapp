const signupForm = document.getElementById("signupForm");
const formMessage = document.getElementById("formMessage");
const submitButton = document.getElementById("submitButton");

if (signupForm && formMessage && submitButton) {
    const fields = {
        username: document.getElementById("username"),
        email: document.getElementById("email"),
        password: document.getElementById("password"),
        confirmpassword: document.getElementById("confirmpassword")
    };

    const setMessage = (message, type) => {
        formMessage.textContent = message;
        formMessage.className = `form-message is-visible ${type === "success" ? "is-success" : "is-error"}`;
    };

    const clearMessage = () => {
        formMessage.textContent = "";
        formMessage.className = "form-message";
    };

    const clearFieldErrors = () => {
        Object.values(fields).forEach((field) => field.classList.remove("input-error"));
    };

    const markFieldError = (fieldName) => {
        const field = fields[fieldName];
        if (field) {
            field.classList.add("input-error");
        }
    };

    const validateForm = () => {
        clearFieldErrors();

        const payload = {
            username: fields.username.value.trim(),
            email: fields.email.value.trim(),
            password: fields.password.value,
            confirmpassword: fields.confirmpassword.value
        };

        if (payload.username.length < 3 || payload.username.length > 20) {
            markFieldError("username");
            return { valid: false, message: "Username must be between 3 and 20 characters." };
        }

        if (!payload.email) {
            markFieldError("email");
            return { valid: false, message: "Email is required." };
        }

        if (payload.password.length < 6) {
            markFieldError("password");
            return { valid: false, message: "Password must be at least 6 characters long." };
        }

        if (payload.password !== payload.confirmpassword) {
            markFieldError("password");
            markFieldError("confirmpassword");
            return { valid: false, message: "Passwords do not match." };
        }

        return { valid: true, payload };
    };

    const setLoading = (isLoading) => {
        submitButton.disabled = isLoading;
        submitButton.textContent = isLoading ? "Creating account..." : "Create account";
    };

    signupForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        clearMessage();

        const validation = validateForm();
        if (!validation.valid) {
            setMessage(validation.message, "error");
            return;
        }

        setLoading(true);

        try {
            const response = await fetch("/signup", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify(validation.payload)
            });

            const responseBody = await response.json().catch(() => ({}));

            if (!response.ok) {
                const message = responseBody.error || "Unable to create your account right now.";
                setMessage(message, "error");
                return;
            }

            if (responseBody.token) {
                localStorage.setItem("authToken", responseBody.token);
            }
            if (responseBody.username) {
                localStorage.setItem("username", responseBody.username);
            }

            setMessage("Account created successfully. Redirecting you to login...", "success");
            signupForm.reset();
            setTimeout(() => {
                window.location.href = "/login";
            }, 1200);
        } catch (error) {
            setMessage("Network error. Please check your connection and try again.", "error");
        } finally {
            setLoading(false);
        }
    });

    Object.values(fields).forEach((field) => {
        field.addEventListener("input", () => {
            field.classList.remove("input-error");
            if (formMessage.classList.contains("is-error")) {
                clearMessage();
            }
        });
    });
}
