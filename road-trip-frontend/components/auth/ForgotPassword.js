import React, { useState } from "react";
import { Button } from "../../components/general/Buttons";
import TextInput from "../../components/general/TextInput";
import Alert from "./Alert";
import Userfront from "@userfront/core";

const ForgotPasswordForm = () => {
    const [state, setState] = useState({
        email: "",
    });
    const handleFormChange = (event) => {
        const name = event.target.name;
        setState((prevState) => ({ ...prevState, [name]: event.target.value }));
    };
    const handleSubmit = (event) => {
        event.preventDefault();
    };
    const [alert, setAlert] = useState();

    return (
        <>
            <h2 className="text-center font-semibold mb-8">
                Forgot Your Password?
            </h2>
            <Alert message={alert} />
            <form id="forgot" onSubmit={handleSubmit}>
                <div className="p-2 flex flex-col gap-2">
                    <p className="px-1 font-light">
                        Enter the email associated with your account
                    </p>
                    <TextInput
                        type="email"
                        name="email"
                        placeholder="Email"
                        autoComplete="username"
                        onChange={(e) => handleFormChange(e)}
                        value={state.username}
                    />
                    <div className="flex flex-row justify-center mt-12 mb-4">
                        <Button type="submit">Send Reset Link</Button>
                    </div>
                </div>
            </form>
        </>
    );
};
export default ForgotPasswordForm;
