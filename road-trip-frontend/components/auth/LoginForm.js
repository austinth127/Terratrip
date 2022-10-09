import React, { useState } from "react";
import { Button } from "../../components/general/Buttons";
import TextInput from "../../components/general/TextInput";
import Userfront from "@userfront/core";
import Alert from "./Alert";

/**
 * A simple login form nested in "/auth/login" powered by Userfront.
 *
 * Form making guide: https://userfront.com/guide/build-login-form-react
 * How to use JWT: https://userfront.com/test/dashboard/jwt/usage
 *
 * @returns {React.Component} The login form
 */
const LoginForm = () => {
    const [state, setState] = useState({
        emailOrUsername: "",
        password: "",
    });
    const [alert, setAlert] = useState();

    // Automatically update state based on the name attribute of the input
    const handleFormChange = (event) => {
        const name = event.target.name;
        setState((prevState) => ({ ...prevState, [name]: event.target.value }));
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        setAlert();
        Userfront.login({
            method: "password",
            emailOrUsername: state.emailOrUsername,
            password: state.password,
        }).catch((error) => {
            // Format message
            let msg = error.message;
            msg = msg.replace('"emailOrUsername"', "Email/Username field");
            msg = msg.replace('"password"', "Password field");
            setAlert(msg);
        });
    };

    return (
        <>
            <h2 className="text-center font-semibold mb-8">
                Sign in to your account
            </h2>
            <Alert message={alert} className="text-red-600 text-center" />
            <form id="login" onSubmit={handleSubmit}>
                <ul className="p-2 flex flex-col gap-2">
                    <li className="flex flex-row justify-between gap-4 items-center">
                        <label className="px-1 font-light w-32">Email</label>
                        <TextInput
                            type="email"
                            name="emailOrUsername"
                            placeholder="Email or Username"
                            autoComplete="username"
                            onChange={(e) => handleFormChange(e)}
                            value={state.username}
                        />
                    </li>
                    <li className="flex flex-row justify-between gap-4">
                        <label className="px-1 font-light w-32 items-center">
                            Password
                        </label>
                        <TextInput
                            type="password"
                            name="password"
                            placeholder="Password"
                            autoComplete="current-password"
                            onChange={(e) => handleFormChange(e)}
                            value={state.password}
                        />
                    </li>
                    <li className="flex flex-row justify-start mt-2">
                        <a
                            className="font-light text-sm text-green-600 hover:underline"
                            href="/auth/forgot"
                        >
                            Forgot Password?
                        </a>
                    </li>
                    <li className="flex flex-row justify-center mt-12 mb-4">
                        <Button type="submit">Sign In</Button>
                    </li>
                </ul>
            </form>
        </>
    );
};

export default LoginForm;
