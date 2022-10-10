import { useRouter } from "next/router";
import React from "react";
import TripCard from "../../../components/trip/TripCard";

const demoTrips = [
    "2022 Amazing Roadtrip",
    "Road Trip with the Boys",
    "Trip to the Grand Canyon",
];

const UserTrips = () => {
    const router = useRouter();

    return (
        <div className="py-2 pr-2">
            <div className="text-gray-50 bg-slate-900 bg-opacity-20 p-4 rounded-lg">
                <h1 className="text-2xl font-semibold mb-12 text-green-600">
                    My Trips
                </h1>
                <div className="flex flex-col gap-4">
                    {demoTrips.map((trip) => (
                        <TripCard trip={{ name: trip }} key={trip}></TripCard>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default UserTrips;
