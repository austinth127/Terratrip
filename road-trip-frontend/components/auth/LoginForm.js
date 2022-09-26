import React, { useState } from "react";
import { Button } from "../../components/general/Buttons";
import TextInput from "../../components/general/TextInput";

const LoginForm = () => {
    const [state, setState] = useState({ username: "", password: "" });

    // Automatically update state based on the name attribute of the input
    const handleFormChange = (event) => {
        const name = event.target.name;
        setState((prevState) => ({ ...prevState, [name]: event.target.value }));
    };

    return (
        <>
            <h2 className="text-center font-semibold mb-12">
                Sign in to your account
            </h2>
            <form id="login">
                <ul className="p-2 flex flex-col gap-2 mb-12">
                    <li className="flex flex-row justify-between gap-4 items-center">
                        <label className="px-1 font-light w-32">Username</label>
                        <TextInput
                            type="email"
                            id="emailInput"
                            name="username"
                            placeholder="Username"
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
                            id="passwordInput"
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
                    <li className="flex flex-row justify-center mt-8">
                        <Button onClick={() => {}}>Sign In</Button>
                    </li>
                </ul>
            </form>
        </>
    );
};

export default LoginForm;
