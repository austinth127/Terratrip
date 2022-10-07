import { useRouter } from "next/router";
import React, { useEffect } from "react";
import LoginForm from "../../components/auth/LoginForm";
import SignUpForm from "../../components/auth/SignupForm";
import Switch from "../../components/general/Switch";
import Userfront from "@userfront/core";
import ForgotPasswordForm from "../../components/auth/ForgotPassword";

/**
 * A wrapper for a given login page
 * @returns {React.Component} The auth page designated by the current path
 */
const UserAuth = () => {
    const router = useRouter();
    return (
        <div className="flex flex-col justify-center items-center h-screen px-8">
            <div className="w-full lg:w-1/3 sm:w-1/2 min-h-fit bg-gray-200 rounded-lg text-slate-800 p-4">
                <Switch selection={router.asPath}>
                    <LoginForm case={"/auth/signin"} />
                    <SignUpForm case={"/auth/signup"} />
                    <ForgotPasswordForm case={"/auth/forgot"} />
                    <></>
                </Switch>
            </div>
        </div>
    );
};

export default UserAuth;
