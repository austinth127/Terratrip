import axios from "axios";
import { useAtom } from "jotai";
import React, { useEffect, useState } from "react";
import {
    filtersAtom,
    rangeAtom,
    recStopAtom,
    tripAtom,
} from "../../../utils/atoms";
import { delay } from "../../../utils/delay";
import { SingleSlider } from "../../general/CompundSlider";
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
    const [filters, setFilters] = useAtom(filtersAtom);
    const [range, setRange] = useAtom(rangeAtom);

    useEffect(() => {
        let isMounted = true;
        let done = false;

        if (!trip.route) return;
        axios
            .post("/api/location/recommend", {
                tripId: trip.id,
                range: Math.floor((range * 1000) / 1.60934),
                categories: filters,
                route: trip.route.geometry.coordinates,
                limit: 30,
            })
            .then(async () => {
                let count = 0;
                while (!done && isMounted && count < 30) {
                    console.log("here");
                    await delay(1000);
                    axios.get("/api/location/recommend?limit=30").then(
                        (res) => {
                            let newRec = res.data.locations.filter(
                                (loc) =>
                                    loc.center &&
                                    (loc.address || loc.place_name)
                            );
                            setRecStops([...newRec]);
                            done = res.data.isDone;
                        },
                        (err) => console.log(err)
                    );
                    count++;
                }
            });
        return () => (isMounted = false);
    }, [filters]);

    return (
        <div className="h-3/4 w-full pr-2 pt-1 overflow-hidden">
            <div className="w-full max-h-full px-2 pt-2 text-slate-900 overflow-y-scroll scrollbar overflow-x-clip">
                <div className="text-sm font-semibold text-slate-900">
                    Recommended Stops
                    <div className="text-xs italic font-light">
                        Click to add a stop!
                    </div>
                </div>
                {!recStops ? (
                    <LoadingSpinner />
                ) : recStops && recStops.length > 0 ? (
                    recStops.map((stop, index) => (
                        <RecStopDisplay
                            stop={stop}
                            key={
                                stop.geoapify_id ??
                                stop.osm_id ??
                                stop.otm_id ??
                                stop.id ??
                                stop.wikidata_id
                            }
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
