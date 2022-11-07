import axios from "axios";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import TripCard from "../../../components/trip/TripCard";

const UserTrips = () => {
    const router = useRouter();
    const [trips, setTrips] = useState();

    useEffect(() => {
        const abortController = new AbortController();
        getData();

        return () => abortController.abort();
    }, []);

    const getData = async () => {
        const res = await axios.get("/trip");
        setTrips(res.data);
    };

    const handleDeleteTrip = (id) => {
        axios.delete(`/trip/${id}`).then(
            (success) => {
                getData();
            },
            (fail) => {
                /** @todo Handle error */
            }
        );
    };

    if (!trips) {
        return (
            <div className="py-10">
                <h1 className="text-2xl font-semibold mb-12 text-white text-center">
                    Your Trips Will Appear Here
                </h1>
            </div>
        );
    }

    return (
        <div className="py-2 pr-2">
            <div className="text-gray-50 bg-slate-900 bg-opacity-20 p-4 rounded-lg">
                <h1 className="text-2xl font-semibold mb-12 text-green-600">
                    My Trips
                </h1>
                <div className="flex flex-col gap-4">
                    {trips.map((trip) => (
                        <TripCard
                            trip={trip}
                            key={trip.id}
                            deleteCallback={() => {
                                handleDeleteTrip(trip.id);
                            }}
                        ></TripCard>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default UserTrips;

// This is how you can get server side data befre render
// export async function getServerSideProps() {
//     // Fetch data from external API
//     const page = Math.floor(Math.random() * 10) + 1;

//     try {
//         const res = await axios.get(
//             `https://api.pexels.com/v1/search?query=nature&per_page=1&orientation=landscape&page=${page}`,
//             {
//                 headers: {
//                     Authorization:
//                         "563492ad6f91700001000001088d0dbdcca94fb4bd0ca364d64f06b8",
//                 },
//             }
//         );
//         const photo = res.data.photos[0];

//         // Pass data to the page via props
//         return { props: { photo } };
//     } catch (e) {
//         console.log("Error getting server side props from api", e);
//     }
//     return { props: {} };
// }
