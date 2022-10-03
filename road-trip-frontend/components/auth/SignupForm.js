import React, { useState } from "react";
import { Button } from "../../components/general/Buttons";
import TextInput from "../../components/general/TextInput";

const SignUpForm = () => {
    const [state, setState] = useState({ username: "", password: "" ,name: "",email:"",confirmPassword:""});

    // Automatically update state based on the name attribute of the input
    const handleFormChange = (event) => {
        const name = event.target.name;
        setState((prevState) => ({ ...prevState, [name]: event.target.value }));
    };

    return (
        <>
            <h2 className="text-center font-semibold mb-12">
                Create your account
            </h2>
            <form id="signup">
                <ul className="p-2 flex flex-col gap-2 mb-12">
                    <li className="flex flex-row justify-between gap-4 items-center">
                        <label className="px-1 font-light w-32">Name</label>
                        <TextInput
                            type="text"
                            // id="emailInput"
                            name="name"
                            placeholder="Username"
                            autoComplete="username"
                            onChange={(e) => handleFormChange(e)}
                            value={state.name}
                        />
                    </li>
                    <li className="flex flex-row justify-between gap-4">
                        <label className="px-1 font-light w-32 items-center">
                            Username
                        </label>
                        <TextInput
                            type="text"
                            // id="passwordInput"
                            name="username"
                            placeholder="Username"
                            autoComplete="Username"
                            onChange={(e) => handleFormChange(e)}
                            value={state.username}
                        />
                    </li>
                    <li className="flex flex-row justify-between gap-4">
                        <label className="px-1 font-light w-32 items-center">
                            Email
                        </label>
                        <TextInput
                            name = "email"
                            type = "email"
                            // id = "nameInput"
                            placeholder="Email"
                            // autoComplete="current-name"
                            onChange={(e)=>handleFormChange(e)}
                            value={state.email}
                        />
                    </li>
                    <li className="flex flex-row justify-between gap-4">
                        <label className="px-1 font-light w-32 items-center">
                            Password
                        </label>
                        <TextInput
                            type = "password"
                            name = "password"
                            // id = "nameInput"
                            placeholder="password"
                            // autoComplete="current-name"
                            onChange={(e)=>handleFormChange(e)}
                            value={state.password}
                        />
                    </li>
                    <li className="flex flex-row justify-between gap-4">
                        <label className="px-1 font-light w-32 items-center">
                            Confirm Passowrd
                        </label>
                        <TextInput
                            type = "password"
                            name = "confirmPassword"
                            // id = "nameInput"
                            placeholder="password"
                            // autoComplete="current-name"
                            onChange={(e)=>handleFormChange(e)}
                            value={state.confirmPassword}
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

export default SignUpForm;
