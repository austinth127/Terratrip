import { useAtom, useAtomValue } from "jotai";
import React, { useState } from "react";
import {
    advLevelAtom,
    locAtom,
    routeAtom,
    showSaveModalAtom,
    tripDateAtom,
} from "../../utils/atoms";
import useInput from "../../hooks/useInput";
import TextInput from "../general/TextInput";
import Alert from "../auth/Alert";
import axios from "axios";

const SaveModal = () => {
    const [show] = useAtom(showSaveModalAtom);
    const tripName = useInput();
    const [alert, setAlert] = useState("");

    const location = useAtomValue(locAtom);
    const dates = useAtomValue(tripDateAtom);
    const advLevel = useAtomValue(advLevelAtom);

    console.log(location, dates, advLevel);

    const handleSubmit = (event) => {
        event.preventDefault();
        setAlert("");
        if (!tripName.value) {
            setAlert("Trip name cannot be empty");
        }

        axios.post("/trip", {
            name: tripName,
            start: location.start,
            end: location.end,
            startDate: dates.start,
            endDate: dates.end,
            advLevel,
        });
    };

    return (
        <div
            className={`bg-gray-100 text-slate-800 text-sm p-4 w-1/2 h-96 ease-in-out duration-200 z-50 
            absolute top-24 left-1/2 -ml-[25%] rounded-lg border border-gray-200 shadow-xl ${
                show ? `translate-y-0` : `-translate-y-[30rem] bg-opacity-10`
            }`}
        >
            <form onSubmit={handleSubmit}>
                <h2 className="text-green-600 font-semibold text-xl">
                    Save Trip
                </h2>
                <Alert message={alert} className="text-red-600" />
                <div className="w-64 my-3">
                    <div className="my-1 text-slate-900 font-semibold ml-0.5">
                        Name your Trip:
                    </div>
                    <TextInput {...tripName} />
                </div>
                <div className="w-full my-3 flex flex-col text-slate-700 font-light text-sm">
                    <div className="my-1 text-slate-900 font-semibold">
                        Review:
                    </div>
                    <div className="p-1">
                        <p>{location.start?.place_name ?? "not specified"}</p>
                        <p className="font-semibold text-slate-600">to</p>
                        <p>{location.end?.place_name ?? "not specified"}</p>
                        <div className="mt-2 text-green-600 font-semibold">
                            Adventure Levels:
                        </div>
                        <p>{advLevel.join(", ")}</p>
                        <p></p>
                        {dates ? (
                            <>
                                <div className="mt-2 text-green-600 font-semibold">
                                    Dates:
                                </div>
                                <p>{dates.start ?? "not specified"}</p>
                                <p className="font-semibold text-slate-600">
                                    to
                                </p>
                                <p>{dates.end ?? "not specified"}</p>
                            </>
                        ) : (
                            <></>
                        )}
                    </div>
                </div>
            </form>
        </div>
    );
};

export default SaveModal;
