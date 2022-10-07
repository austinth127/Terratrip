import React, { useEffect, useState } from "react";
import NavItem from "./NavItem";

import Userfront from "@userfront/core";

const AuthRoutes = () => {
    const [isSignedIn, setIsSignedIn] = useState(false);
    const handleLogout = () => {
        Userfront.logout();
    };

    useEffect(() => {
        setIsSignedIn(Userfront.user && Userfront.user.userId);
    }, [Userfront.user]);

    return (
        <div className="lg:mb-4 lg:mt-2">
            {isSignedIn ? (
                <>
                    <NavItem href={`/profile`} key="profile">
                        Profile
                    </NavItem>
                    <button
                        className="flex flex-col items-start w-full"
                        onClick={handleLogout}
                        key="log out"
                    >
                        <span className="text-slate-800 text-sm font-semibold lg:w-full flex-nowrap grad-txt-rs-yllw font-hubballi w-fit">
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
