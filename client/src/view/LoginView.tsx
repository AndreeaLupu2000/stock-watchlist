import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register, login } from "../api/userService";

const LoginView = () => {

    // State variable for username
    const [username, setUsername] = useState("");

    // State variable for password
    const [password, setPassword] = useState("");

    // State variable for error message
    const [errorMessage, setErrorMessage] = useState("");

    // State variable for isRegistering
    const [isRegistering, setIsRegistering] = useState(false);

    // Navigation
    const navigate = useNavigate();

    /**
     * Register a new user
     * @returns void
     */
    const handleRegister = async () => {
        try {
            // Check if username and password are not empty
            if (username === "" || password === "") {
                setErrorMessage("Username and password are required");
                return;
            }
            // Register the user
            const response = await register(username, password);
            // If registration is successful, navigate to the search page
            if (response.status === 201) {
                setIsRegistering(false);
                // Store the username in the local storage
                localStorage.setItem("username", username);

                // Navigate to the search page
                navigate("/search");
            }
        } catch (error: any) {
            // If registration is unsuccessful, set the error message
            setErrorMessage("User already exists");
            console.error(error);
        }
    }

    /**
     * Login a user
     * @returns void
     */
    const handleLogin = async () => {
        try {
            // Check if username and password are not empty
            const response = await login(username, password);
            // If login is successful, navigate to the search page
            if (response.status === 200) {
                // Store the username in the local storage
                localStorage.setItem("username", username);
                // Navigate to the search page
                navigate("/search");
            }
        } catch (error: any) {
            // If login is unsuccessful, set the error message
            setErrorMessage("Invalid user");
            console.error(error);
        }
    }

    return (
        <div className="w-full max-w-5xl mx-auto px-4 sm:px-6 lg:px-8">

            {/* ------------------ Login Card ------------------ */}
            <div className="bg-white p-6 rounded-md shadow-md mb-8">
                <h1 className="text-2xl font-bold mb-6 text-center mb-4">{isRegistering ? "Register" : "Login"}</h1>

                {/* ------------------ Username Input ------------------ */}
                <div className="mb-6 relative justify-start">
                    {/* ------------------ Username Label ------------------ */}
                    <label
                        htmlFor="username"
                        className="block text-lg font-medium text-gray-700 h-5 mb-2 justify-start"
                    >
                        Username
                    </label>

                    {/* ------------------ Username Input ------------------ */}
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => {
                            setUsername(e.target.value);
                        }}
                        placeholder="first.lastname"
                        className={`w-full px-3 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-1 ${errorMessage
                            ? "border-red-500 ring-red-500"
                            : "border-gray-300 focus:ring-indigo-500"
                            }`}
                    />
                </div>

                {/* ------------------ Password Input ------------------ */}
                <div className="mb-6 relative justify-start">
                    {/* ------------------ Password Label ------------------ */}
                    <label
                        htmlFor="password"
                        className="block text-lg font-medium text-gray-700 h-5 mb-2 justify-start"
                    >
                        Password
                    </label>

                    {/* ------------------ Password Input ------------------ */}
                    <input
                        type="password"
                        placeholder="********"
                        value={password}
                        onChange={(e) => {
                            setPassword(e.target.value);
                        }}
                        className={`w-full px-3 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-1 ${errorMessage
                            ? "border-red-500 ring-red-500"
                            : "border-gray-300 focus:ring-indigo-500"
                            }`}
                    />
                </div>

                {/* ------------------ Error Message ------------------ */}
                {errorMessage && <p className="text-red-500 text-center">{errorMessage}</p>}

                {/* ------------------ Login Button ------------------ */}
                <div className="flex justify-center">
                    <button
                        onClick={isRegistering ? handleRegister : handleLogin}
                        className="bg-blue-500 text-white px-4 py-2 rounded-md shadow-sm hover:bg-indigo-600 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                    >
                        {isRegistering ? "Register" : "Login"}
                    </button>
                </div>

                {/* ------------------ Sign Up Link ------------------ */}
                <div className="text-center mt-4">
                    <span className="text-sm text-gray-600">Don't have an account?</span>
                    <button
                        onClick={() => {
                            setIsRegistering(!isRegistering);

                        }}
                        className="text-blue-500 hover:underline ml-1 text-sm"
                    >
                        {isRegistering ? "Login" : "Sign up"}
                    </button>
                </div>
            </div>
        </div>
    )
}

export default LoginView;