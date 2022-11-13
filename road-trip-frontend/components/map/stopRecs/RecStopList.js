import axios from "axios";
import { useAtom } from "jotai";
import React, { useEffect, useState } from "react";
import { filtersAtom, recStopAtom, tripAtom } from "../../../utils/atoms";
import LoadingSpinner from "../../general/LoadingSpinner";
import RecStopDisplay from "./RecStopItem";

/**
 *  @todo Range slider
 *  @todo refresh
 */
const RecStopList = () => {
    const [recStops, setRecStops] = useAtom(recStopAtom);
    const [sent, setSent] = useState(false);
    const [trip, setTrip] = useAtom(tripAtom);
    const [waiting, setWaiting] = useState(true);
    const [filters, setFilters] = useAtom(filtersAtom);

    useEffect(() => {
        const abortController = new AbortController();
        getData();
        return () => abortController.abort();
    }, [filters]);

    useEffect(() => {
        if (recStops || sent) return;
        setSent(true);
        getData();
    }, [trip.route]);

    const getData = async () => {
        if (!trip.route) return;
        setWaiting(true);
        const res = await axios.post("/location/recommend", {
            tripId: trip.id,
            range: 5000,
            categories: filters,
            route: trip.route.geometry.coordinates,
        });
        setWaiting(false);
        setRecStops(res.data);
    };

    return (
        <div className="h-3/4 w-full pr-2 pt-1 overflow-hidden">
            <div className="w-full max-h-full px-2 pt-2 text-slate-900 overflow-y-scroll scrollbar overflow-x-clip">
                <div className="text-sm font-semibold text-slate-900">
                    Recommended Stops
                    <div className="text-xs italic font-light">
                        Click to add a stop!
                    </div>
                </div>
                {waiting ? (
                    <LoadingSpinner />
                ) : recStops && recStops.length > 0 ? (
                    recStops.map((stop, index) => (
                        <RecStopDisplay
                            stop={stop}
                            key={stop.address + stop.place_name + index}
                        />
                    ))
                ) : (
                    <h2 className="text-xs text-center p-4">
                        No Recommended Stops
                    </h2>
                )}
            </div>
        </div>
    );
};

export default RecStopList;
