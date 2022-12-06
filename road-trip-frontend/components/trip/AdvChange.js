import { useAtom, useAtomValue, useSetAtom } from "jotai";
import React, { useState } from "react";
import { getRoute } from "../../utils/map/geometryUtils";
import {
    advLevelAtom,
    showAdvChangeAtom,
    endAtom,
    endDateAtom,
    routeAtom,
    startAtom,
    startDateAtom,
    tripAtom,
    tripIdAtom,
    tripNameAtom,
    clearTripAtom,
} from "../../utils/atoms";
import { levelOptions } from "../../utils/stops/filters";
import Alert from "../auth/Alert";
import axios from "axios";
import { Button, OutlineButton } from "../general/Buttons";
import { useRouter } from "next/router";
import { tripToTripRequest } from "../../utils/trip";

const SaveModal = () => {
    const [show, setShow] = useAtom(showAdvChangeAtom);

    const [start, setStart] = useAtom(startAtom);
    const [end, setEnd] = useAtom(endAtom);
    const [startDate, setStartDate] = useAtom(startDateAtom);
    const [endDate, setEndDate] = useAtom(endDateAtom);
    const [activeLevel, setActiveLevel] = useAtom(advLevelAtom);
    const [route, setRoute] = useAtom(routeAtom);
    const [tripName, setTripName] = useAtom(tripNameAtom);
    const setTripId = useSetAtom(tripIdAtom);
    const trip = useAtomValue(tripAtom);
    const clearTrip = useSetAtom(clearTripAtom);

    const router = useRouter();
    const [alert, setAlert] = useState();

    const handleSubmit = (event) => {
        event.preventDefault();

        axios.patch(`/api/trip/${trip.id}`, tripToTripRequest(trip)).then(
            (success) => {
                clearTrip();
                router.push("/trips/list/user");
                setShow(false);
            },
            (fail) => {
                setAlert("Trip failed to update");
            }
        );
    };

    const handleRemove = (event) => {
        event.preventDefault();

        let newAdvLevel = -1;
        let exit = false;

        for (var i = 0; i < levelOptions.length; i++) {
            if (
                activeLevel.toLocaleLowerCase() ==
                levelOptions[i].toLocaleLowerCase()
            ) {
                newAdvLevel = i;
            }
        }

        if (trip.stops) {
            for (var si = 0; si < trip.stops.length; si++) {
                let s = trip.stops[si];

                let stopAdvLevel = -1;
                for (var i = 0; i < levelOptions.length; i++) {
                    if (
                        s.adventureLevel?.toLocaleLowerCase() ==
                        levelOptions[i].toLocaleLowerCase()
                    ) {
                        stopAdvLevel = i;
                    }
                }

                if (stopAdvLevel > newAdvLevel) {
                    delete trip.stops[si];
                }
            }
        }

        axios.patch(`/api/trip/${trip.id}`, tripToTripRequest(trip)).then(
            (success) => {
                clearTrip();
                router.push("/trips/list/user");
                setShow(false);
            },
            (fail) => {
                setAlert("Trip failed to update");
            }
        );
    };

    return (
        <div
            className={`bg-gray-100 text-slate-800 text-sm p-4 w-1/2 h-fit ease-in-out duration-200 z-50 
            absolute top-24 left-1/2 -ml-[25%] rounded-lg border border-gray-200 shadow-xl pb-16 ${
                show ? `translate-y-0` : `-translate-y-[30rem] bg-opacity-10`
            }`}
        >
            <form onSubmit={handleSubmit}>
                <h2 className="text-green-600 font-semibold text-xl">
                    Adventure Level Change
                </h2>
                <div className="w-64 my-3">
                    <div className="my-1 text-slate-900 font-semibold ml-0.5">
                        You currently have stops that are above your adventure
                        level. Would you like to keep or remove these stops?
                    </div>
                </div>
                <div className="absolute bottom-4 right-4 gap-2 flex flex-row">
                    <Button type="submit">Keep</Button>
                    <Button onClick={handleRemove}>Remove</Button>
                    <OutlineButton
                        onClick={() => {
                            setShow(false);
                        }}
                    >
                        Cancel
                    </OutlineButton>
                </div>
            </form>
        </div>
    );
};

export default SaveModal;
