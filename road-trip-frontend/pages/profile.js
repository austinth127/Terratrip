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
        <div className="w-full h-screen px-4 pt-24 text-gray-900 text-lg">
            <div className="flex flex-col gap-y-1 bg-white bg-opacity-80 rounded-lg w-full h-3/4 p-8">
                <h2 className="text-center font-bold">{user.name}</h2>
                <p className="text-center font-light text-base">
                    <label className="font-normal">Username:</label>{" "}
                    {user.username}
                </p>
                <p className="text-center font-light text-base">
                    <label className="font-normal">Email:</label> {user.email}
                </p>
                {/** @TODO Add trips and playlist to response data */}
                <p className="mt-12 text-left font-light text-base">
                    <label className="font-semibold grad-txt-rs-yllw">
                        <a href="/trips/edit"> My Trips:</a>
                    </label>
                    <br />
                    This user has no trips.
                </p>
                <p className="mt-8 text-left font-light text-base">
                    <label className="font-semibold grad-txt-rs-yllw">
                        <a href="/trips/edit"> My Playlists:</a>
                    </label>
                    <br />
                    This user has no playlists.
                </p>
            </div>
        </div>
    );
};

export default Profile;
