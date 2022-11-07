import axios from "axios";
import { useAtom } from "jotai";
import React, { useEffect, useState } from "react";
import { tripAtom } from "../../utils/atoms";
import LoadingSpinner from "../general/LoadingSpinner";

const RecStopList = () => {
    const [recStops, setRecStops] = useState();
    const [trip, setTrip] = useAtom(tripAtom);
    const [waiting, setWaiting] = useState(true);

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
        setWaiting(false);
        setRecStops(res.data);
    };

    return (
        <div className="w-full h-1/2 px-4 pt-2 text-slate-900">
            <div className="text-sm font-semibold text-green-600">
                Recommended Stops
            </div>
            {waiting ? (
                <LoadingSpinner />
            ) : recStops && recStops.length > 0 ? (
                <></>
            ) : (
                <h2 className="text-xs text-center p-4">
                    No Recommended Stops
                </h2>
            )}
        </div>
    );
};

export default RecStopList;
