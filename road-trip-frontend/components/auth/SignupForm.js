import React, { useState } from "react";
import { Button } from "../../components/general/Buttons";
import TextInput from "../../components/general/TextInput";
import Alert from "./Alert";
import Userfront, { user } from "@userfront/core";
import axios from "axios";
import { useRouter } from "next/dist/client/router";
import { setupAxios } from "../../utils/axiosSetup";

const SignUpForm = () => {
    const [state, setState] = useState({
        username: "",
        password: "",
        name: "",
        email: "",
        confirmPassword: "",
    });

    const [alert, setAlert] = useState();
    const [confirmation, setConfirmation] = useState();

    const router = useRouter();

    // Automatically update state based on the name attribute of the input
    const handleFormChange = (event) => {
        const name = event.target.name;
        setState((prevState) => ({ ...prevState, [name]: event.target.value }));
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        setAlert();
        setConfirmation();

        const { password, confirmPassword } = state;
        if (password !== confirmPassword) {
            setAlert("Passwords don't match");
            return;
        }

        Userfront.signup({
            method: "password",
            email: state.email,
            password: state.password,
            name: state.name,
            username: state.username,
            redirect: false,
        }).then(
            (signupResponse) => {
                const { username, userId, email, name } = Userfront.user;
                if (userId) {
                    axios
                        .post("/api/user/register", {
                            username,
                            userId,
                            name,
                            email,
                        })
                        .then(() => {
                            setAlert();
                            setConfirmation(
                                "Successfully signed up. Redirecting..."
                            );
                            setupAxios();
                            setTimeout(() => {
                                router.push("/auth/signin");
                            }, 500);
                        })
                        .catch(() => {
                            setAlert(
                                "Failed to access the server, try again later"
                            );
                        });
                }
            },
            (error) => {
                // Format message
                let msg = error.message;
                msg = msg.replace('"email"', "Email field");
                msg = msg.replace('"password"', "Password field");
                setAlert(msg);
            }
        );
    };

    return (
        <>
            <h2 className="text-center font-semibold mb-8">
                Create your account
            </h2>
            <Alert message={alert} className="text-red-600 text-center" />
            <Alert
                message={confirmation}
                className="text-green-600 text-center"
            />
            <form id="signup" onSubmit={handleSubmit}>
                <ul className="p-2 flex flex-col gap-2">
                    <li className="flex flex-row justify-between gap-4 items-center">
                        <label className="px-1 font-light w-32">Name</label>
                        <TextInput
                            type="text"
                            name="name"
                            placeholder="Name"
                            autoComplete="name"
                            onChange={(e) => handleFormChange(e)}
                            value={state.name}
                        />
                    </li>
                    <li className="flex flex-row justify-between gap-4 items-center">
                        <label className="px-1 font-light w-32 items-center">
                            Username
                        </label>
                        <TextInput
                            type="text"
                            name="username"
                            placeholder="Username"
                            autoComplete="username"
                            onChange={(e) => handleFormChange(e)}
                            value={state.username}
                        />
                    </li>
                    <li className="flex flex-row justify-between gap-4 mb-6 items-center">
                        <label className="px-1 font-light w-32 items-center">
                            Email
                        </label>
                        <TextInput
                            name="email"
                            type="email"
                            placeholder="Email"
                            autoComplete="email"
                            onChange={(e) => handleFormChange(e)}
                            value={state.email}
                        />
                    </li>
                    <li className="flex flex-row justify-between gap-4 items-center">
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
                    <li className="flex flex-row justify-between gap-4 items-center">
                        <label className="px-1 font-light w-32 items-center">
                            Confirm Password
                        </label>
                        <TextInput
                            type="password"
                            name="confirmPassword"
                            placeholder="Confirm Password"
                            autoComplete="false"
                            onChange={(e) => handleFormChange(e)}
                            value={state.confirmPassword}
                        />
                    </li>
                    <li>
                        <p className="text-xs text-gray-400 mt-2">
                            Password must be at least 16 characters OR at least
                            8 characters including a number and a letter
                        </p>
                    </li>
                    <li className="flex flex-row justify-center mt-8 mb-4">
                        <Button type="submit">Sign Up</Button>
                    </li>
                </ul>
            </form>
        </>
    );
};

export default SignUpForm;
