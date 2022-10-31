import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";

import Userfront from "@userfront/core";
import axios from "axios";
import Link from "next/link";

const Profile = () => {
    // Get the id from the dynamic route segment
    const id = Userfront.user.userId;
    const [user, setUser] = useState();
    const [trips, setTrips] = useState();

    useEffect(() => {
        /** @TODO better error handling */
        const getData = async () => {
            const res = await axios.get(`/user`);
            setUser(res.data);
            res = await axios.get("/trip");
            setTrips(res.data);
        };

        getData();
    }, [id]);

    if (!user || typeof trips == "undefined") {
        return <></>;
    }

    return (
        <div className="w-full h-fit px-4 pt-24 text-gray-900 text-lg">
            <div className="flex flex-col gap-y-1 bg-white bg-opacity-80 rounded-lg w-full h-3/4 p-8">
                <h2 className="text-center font-bold">{user.name}</h2>
                <p className="text-center font-light text-base">
                    <label className="font-normal">Username:</label>{" "}
                    {user.username}
                </p>
                <p className="text-center font-light text-base">
                    <label className="font-normal">Email:</label>{" "}
                    {user.emailAddress}
                </p>
                {/** @TODO Add trips and playlist to response data */}
                <div className="mt-12 text-left font-light text-base h-fit">
                    <label className="font-semibold grad-txt-rs-yllw">
                        <a href="/trips/edit"> My Trips:</a>
                    </label>
                    <br />
                    {trips ? (
                        <ul>
                            {trips.map((trip) => (
                                <li
                                    className="grad-txt-rs-yllw text-slate-400 font-normal"
                                    key={trip.id ?? trip.name}
                                >
                                    {/** @TODO fix this later u nerd */}
                                    <Link href="/">
                                        <a href="/">{trip.name}</a>
                                    </Link>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p> This user has no trips.</p>
                    )}
                </div>
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
