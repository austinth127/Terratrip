import { useRouter } from "next/router";
import React from "react";

import Userfront from "@userfront/core";
Userfront.init("wbmrp64n");

const Profile = () => {
    // Get the id from the dynamic route segment
    const { query } = useRouter();
    const id = query.id;
    const user = Userfront.user;
    console.log(user);

    return (
        <div className="w-full h-full px-8">
            <div className="flex flex-col gap-y-4">
                <div className="bg-white w-full"></div>
            </div>
        </div>
    );
};

export default Profile;
