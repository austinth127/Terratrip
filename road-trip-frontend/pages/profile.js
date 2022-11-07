import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";

import Userfront from "@userfront/core";
import axios from "axios";
import Link from "next/link";
import { makeTripActive } from "../utils/trip";
import { useSetAtom } from "jotai";
import { tripAtom } from "../utils/atoms";

const Profile = () => {
    // Get the id from the dynamic route segment
    const id = Userfront.user.userId;
    const [user, setUser] = useState();
    const [trips, setTrips] = useState();
    const setActiveTrip = useSetAtom(tripAtom);
    const router = useRouter();

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

    const handleViewTrip = (trip) => {
        makeTripActive(trip, setActiveTrip);
        router.push("/trips/map");
    };

    return (
        <div className="w-full h-fit px-4 pt-14 text-lg text-slate-100">
            <div className="flex flex-col gap-y-1 bg-slate-900 bg-opacity-75 rounded-lg w-full h-3/4 p-8">
                <h2 className="text-left font-bold">{user.name}</h2>
                <p className="text-left font-light text-base">
                    <label className="font-normal text-green-600">
                        Username:
                    </label>{" "}
                    {user.username}
                </p>
                <p className="text-left font-light text-base">
                    <label className="font-normal text-green-600">Email:</label>{" "}
                    {user.emailAddress}
                </p>
                {/** @TODO Add trips and playlist to response data */}
                <div className="mt-12 text-left font-light text-base h-fit">
                    <label className="font-semibold grad-txt-rs-yllw text-green-600">
                        <a href="/trips/list/user"> My Trips:</a>
                    </label>
                    <br />
                    {trips ? (
                        <ul>
                            {trips.map((trip) => (
                                <li
                                    className="hover:text-slate-400 text-slate-100 font-normal w-fit"
                                    key={trip.id ?? trip.name}
                                >
                                    <button
                                        onClick={() => {
                                            handleViewTrip(trip);
                                        }}
                                    >
                                        {trip.name}
                                    </button>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p> This user has no trips.</p>
                    )}
                </div>
                <p className="mt-8 text-left font-light text-base">
                    <label className="font-semibold grad-txt-rs-yllw text-green-600">
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
