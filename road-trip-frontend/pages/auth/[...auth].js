import { useRouter } from "next/router";
import React from "react";
import LoginForm from "../../components/auth/LoginForm";
import SignUpForm from "../../components/auth/SignupForm";
import Switch from "../../components/general/Switch";
import Userfront from "@userfront/core";
import { tenantKey } from "../../utils/api/userFrontSetup";

Userfront.init(tenantKey);

/**
 * A wrapper for a given login page
 * @returns {React.Component} The auth page designated by the current path
 */
const UserAuth = () => {
    const { asPath } = useRouter();

    return (
        <div className="flex flex-col justify-center items-center h-screen px-8">
            <div className="w-full lg:w-1/3 sm:w-1/2 min-h-fit bg-gray-200 rounded-lg text-slate-800 p-4">
                <Switch selection={asPath}>
                    <LoginForm case={"/auth/signin"} />
                    <SignUpForm case={"/auth/signup"} />
                    <></>
                </Switch>
            </div>
        </div>
    );
};

export default UserAuth;
