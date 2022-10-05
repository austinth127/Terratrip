import React, { useEffect, useState } from "react";
import NavItem from "./NavItem";

import Userfront from "@userfront/core";

const AuthRoutes = () => {
    const user = Userfront.user;
    const [isUser, setIsUser] = useState(null);

    useEffect(() => {
        if (!Userfront.user.userId) return;
        setIsUser(Userfront.user.userId != undefined);
    }, [user]);

    const handleLogout = () => {
        Userfront.logout();
    };

    return (
        <div className="lg:mb-4 lg:mt-2">
            {isUser ? (
                <>
                    <NavItem href={`/profile`} key="profile">
                        Profile
                    </NavItem>
                    <button
                        className="flex flex-col items-start w-full"
                        onClick={handleLogout}
                        key="log out"
                    >
                        <span className="text-slate-800 my-1 text-sm font-semibold lg:w-full flex-nowrap grad-txt-rs-yllw font-hubballi w-fit">
                            Log Out
                        </span>
                    </button>
                </>
            ) : (
                <>
                    <NavItem href="/auth/signin" key="log in">
                        Log In
                    </NavItem>
                    <NavItem href="/auth/signup" key="sign up">
                        Sign Up
                    </NavItem>
                </>
            )}
        </div>
    );
};

export default AuthRoutes;
