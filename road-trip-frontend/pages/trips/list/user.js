import { useRouter } from "next/router";
import React from "react";
import TripCard from "../../../components/trip/TripCard";

const demoTrips = [
    {
        name: "2022 Amazing Roadtrip",
        start: {
            place_name: "Frisco, Texas",
            center: [1, 1],
        },
        end: {
            place_name: "Seattle, Washington",
            center: [2, 3],
        },
        advLevel: "Extreme",
        dates: {
            start: "10/10/2022",
            end: "11/01/2022",
        },
        rating: 3.5,
    },
    {
        name: "Road Trip with the Boys",
        start: {
            place_name: "Frisco, Texas",
            center: [1, 1],
        },
        end: {
            place_name: "Vancouver, Canada",
            center: [2, 3],
        },
        advLevel: "Relaxed",
        dates: {
            start: "12/10/2022",
        },
        rating: 4.5,
    },
    {
        name: "Trip to the Grand Canyon",
        start: {
            place_name: "Waco, Texas",
            center: [1, 1],
        },
        end: {
            place_name: "Grand Canyon, AZ",
            center: [2, 3],
        },
        advLevel: "Moderate",
        dates: {
            end: "12/10/2023",
        },
    },
];

const UserTrips = () => {
    const router = useRouter();

    /** @TODO Call backend for list of trips */
    return (
        <div className="py-2 pr-2">
            <div className="text-gray-50 bg-slate-900 bg-opacity-20 p-4 rounded-lg">
                <h1 className="text-2xl font-semibold mb-12 text-green-600">
                    My Trips
                </h1>
                <div className="flex flex-col gap-4">
                    {demoTrips.map((trip) => (
                        <TripCard trip={trip} key={trip}></TripCard>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default UserTrips;
