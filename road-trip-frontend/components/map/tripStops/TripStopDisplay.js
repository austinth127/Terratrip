import { useAtom, useAtomValue } from "jotai";
import React, { useEffect, useState } from "react";
import {
    allLocationsAtom,
    routeAtom,
    stopsAtom,
    tripAtom,
} from "../../../utils/atoms";
import TripStopItem from "./TripStopItem";
import { v4 as uuid } from "uuid";

const StopDisplay = () => {
    const [trip, setTrip] = useAtom(tripAtom);
    const [locs, setLocs] = useAtom(allLocationsAtom);
    const [stops, setStops] = useAtom(stopsAtom);
    const route = useAtomValue(routeAtom);

    if (!locs || !route) {
        return null;
    }

    return (
        <div
            className=" h-fit rounded-lg sm:w-1/4 max-h-[80vh] overflow-y-scroll dark-scrollbar
                        fixed z-20 top-14 right-2 isolate flex flex-col gap-2 p-1 mt-12"
        >
            {locs ? (
                locs.map((loc, index) => (
                    <TripStopItem
                        stop={loc}
                        order={index}
                        key={uuid()}
                        leg={index != 0 ? route.legs[index - 1] : null}
                    />
                ))
            ) : (
                <></>
            )}
        </div>
    );
};

export default StopDisplay;
