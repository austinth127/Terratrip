import React, { useState } from "react";
import { Button } from "../../components/general/Buttons";
import TextInput from "../../components/general/TextInput";
import Alert from "./Alert";
import Userfront from "@userfront/core";

const ForgotPasswordForm  = () => {
    const [state, setState] = useState({
        email: "",
    });
    const handleFormChange = (event) => {
        const name = event.target.name;
        setState((prevState) => ({ ...prevState, [name]: event.target.value }));
    };
    
    return (
        <>
        <h2 className="text-center font-semibold mb-8 h-10">
            Forgot Your Password?
        </h2>
        <Alert message={alert} />
            <form id="forgot" onSubmit={handleSubmit}>
                <ul className="p-2 flex flex-col gap-2">
                    <li className="flex flex-row justify-between gap-4 items-center">
                        <label className="px-1 font-light w-32">Enter the email associated with your account</label>
                        <TextInput
                            type="email"
                            name="emailOrUsername"
                            placeholder="Email"
                            autoComplete="username"
                            onChange={(e) => handleFormChange(e)}
                            value={state.username}
                        />
                    </li>
                </ul>
            </form>
        </>
    );

};
export default ForgotPasswordForm;