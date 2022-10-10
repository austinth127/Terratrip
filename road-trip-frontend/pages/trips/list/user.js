import axios from "axios";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
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
    const [trips, setTrips] = useState();

    useEffect(() => {
        const getData = async () => {
            const res = await axios.get("/trip");
            // setTrips(res.data);
            console.log(res.data);
            setTrips(res.data);
        };
        getData();
    }, []);

    if (!trips) {
        return <></>;
    }

    return (
        <div className="py-2 pr-2">
            <div className="text-gray-50 bg-slate-900 bg-opacity-20 p-4 rounded-lg">
                <h1 className="text-2xl font-semibold mb-12 text-green-600">
                    My Trips
                </h1>
                <div className="flex flex-col gap-4">
                    {trips.map((trip) => (
                        <TripCard trip={trip} key={trip.id}></TripCard>
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
