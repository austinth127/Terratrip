import { useAtom, useAtomValue, useSetAtom } from "jotai";
import React, { useState } from "react";
import {
    clearTripAtom,
    editModeAtom,
    showSaveModalAtom,
    tripAtom,
} from "../../utils/atoms";
import Alert from "../auth/Alert";
import axios from "axios";
import { Button, OutlineButton } from "../general/Buttons";
import { useRouter } from "next/router";
import { tripToTripRequest } from "../../utils/trip";

const SaveModal = () => {
    const [show, setShow] = useAtom(showSaveModalAtom);
    const editMode = useAtomValue(editModeAtom);
    const [alert, setAlert] = useState("");
    const trip = useAtomValue(tripAtom);
    const clearTrip = useSetAtom(clearTripAtom);

    const router = useRouter();

    const handleSubmit = (event) => {
        event.preventDefault();
        setAlert("");

        axios.patch(`/api/trip/${trip.id}`, tripToTripRequest(trip)).then(
            (success) => {
                setShow(false);
            },
            (fail) => {
                setAlert("Trip failed to save");
            }
        );
    };

    return (
        <div
            className={`bg-gray-100 text-slate-800 text-sm p-4 w-1/2 h-fit ease-in-out duration-200 z-50 
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
                        Name:
                    </div>
                    <div>
                        <p className="font-semibold text-green-600">
                            {trip.name ?? "Not named"}
                        </p>
                    </div>
                </div>
                <div className="w-full my-3 flex flex-col text-slate-700 font-light text-sm">
                    <div className="my-1 text-slate-900 font-semibold">
                        Review:
                    </div>
                    <div
                        className={`flex flex-col gap-2 text-sm text-slate-900 pt-2 pb-4`}
                    >
                        <div className="flex flex-row gap-2">
                            <p>{trip.start?.place_name ?? ""}</p>
                            <p className="font-semibold text-green-600">to</p>
                            <p>{trip.end?.place_name ?? ""}</p>
                        </div>
                        <div className="flex flex-row gap-2">
                            <div className=" text-green-600 font-semibold">
                                Adventure Level:
                            </div>
                            <p>{trip.advLevel ?? "not specified"}</p>
                        </div>
                        <div className="flex flex-row gap-2">
                            <div className=" text-green-600 font-semibold">
                                Dates:
                            </div>
                            <p>{trip.startDate ?? "not specified"}</p>
                            <p className="font-semibold text-green-600">to</p>
                            <p>{trip.endDate ?? "not specified"}</p>
                        </div>
                    </div>
                </div>
                <div className="absolute bottom-4 right-4 gap-2 flex flex-row">
                    <Button type="submit">Submit</Button>
                    <OutlineButton onClick={() => setShow(false)}>
                        Cancel
                    </OutlineButton>
                </div>
            </form>
        </div>
    );
};

export default SaveModal;
