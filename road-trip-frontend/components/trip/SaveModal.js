import { useAtom, useAtomValue } from "jotai";
import React, { useState } from "react";
import { locAtom, showSaveModalAtom, tripDateAtom } from "../../utils/atoms";
import useInput from "../../hooks/useInput";
import TextInput from "../general/TextInput";
import Alert from "../auth/Alert";

const SaveModal = () => {
    const [show] = useAtom(showSaveModalAtom);
    const tripName = useInput();
    const [alert, setAlert] = useState("");

    const location = useAtomValue(locAtom);
    const dates = useAtomValue(tripDateAtom);

    console.log(location, dates);

    const handleSubmit = (event) => {
        event.preventDefault();
        setAlert("");
        if (!tripName.value) {
            setAlert("Trip name cannot be empty");
        }
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
                    <div className="my-1 text-slate-900 font-semibold ml-0.5">
                        Review:
                    </div>
                    <p>{location.start?.place_name ?? ""}</p>
                    <p className="font-semibold text-green-600">to</p>
                    <p>{location.end?.place_name ?? ""}</p>
                </div>
            </form>
        </div>
    );
};

export default SaveModal;
