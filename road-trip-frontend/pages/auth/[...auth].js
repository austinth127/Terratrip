import { useRouter } from "next/router";
import React from "react";
import LoginForm from "../../components/auth/LoginForm";
import Switch from "../../components/general/Switch";

const UserAuth = () => {
    const { asPath } = useRouter();

    return (
        <div className="flex flex-col justify-center items-center h-screen px-8">
            <div className="w-full lg:w-1/3 sm:w-1/2 h-96 bg-gray-200 rounded-lg text-slate-800 p-4">
                <Switch selection={asPath}>
                    <LoginForm case={"/auth/signin"} />
                    <></>
                </Switch>
            </div>
        </div>
    );
};

export default UserAuth;
