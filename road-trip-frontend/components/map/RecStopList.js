import axios from "axios";
import { useAtom } from "jotai";
import React, { useEffect, useState } from "react";
import { tripAtom } from "../../utils/atoms";

const RecStopList = () => {
    const [recStops, setRecStops] = useState();
    const [trip, setTrip] = useAtom(tripAtom);

    useEffect(() => {
        const abortController = new AbortController();
        getData();
        return () => abortController.abort();
    }, []);

    const getData = async () => {
        const res = await axios.post("/location/recommend", {
            tripId: trip.id,
            range: 3000,
            categories: [],
            route: trip.route.geometry.coordinates,
        });
        setRecStops(res.data);
    };
    console.log(recStops);

    return (
        <div className="w-full h-1/2 px-4 pt-2">
            <div className="text-sm font-semibold text-green-600">
                Recommended Stops
            </div>
        </div>
    );
};

export default RecStopList;
