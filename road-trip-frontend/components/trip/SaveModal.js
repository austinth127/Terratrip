import { useAtom, useAtomValue } from "jotai";
import React, { useState } from "react";
import {
    advLevelAtom,
    editModeAtom,
    locAtom,
    routeAtom,
    showSaveModalAtom,
    tripDateAtom,
    tripIdAtom,
    tripNameAtom,
} from "../../utils/atoms";
import TextInput from "../general/TextInput";
import Alert from "../auth/Alert";
import axios from "axios";
import { Button } from "../general/Buttons";
import { useRouter } from "next/router";

const SaveModal = () => {
    const [show] = useAtom(showSaveModalAtom);
    const [tripName, setTripName] = useAtom(tripNameAtom);

    const [location, setLocation] = useAtom(locAtom);
    const [dates, setDates] = useAtom(tripDateAtom);
    const [advLevel, setAdvLevel] = useAtom(advLevelAtom);
    const [showModal, setShowModal] = useAtom(showSaveModalAtom);

    const [editMode, setEditMode] = useAtom(editModeAtom);
    const [tripId, setTripId] = useAtom(tripIdAtom);

    const [alert, setAlert] = useState("");

    const route = useAtomValue(routeAtom);

    const router = useRouter();

    console.log(route);

    const handleSubmit = (event) => {
        event.preventDefault();
        setAlert("");
        if (!tripName) {
            setAlert("Trip name cannot be empty");
            return;
        }

        if (editMode) {
            handleEdit();
        } else {
            handleSave();
        }
        /**@todo redirect */
    };

    const handleSave = () => {
        axios
            .post("/trip", {
                name: tripName,
                start: {
                    place_name: location.start.place_name,
                    lng: location.start.center[0],
                    lat: location.start.center[1],
                },
                end: {
                    place_name: location.end.place_name,
                    lng: location.end.center[0],
                    lat: location.end.center[1],
                },
                startDate: dates.start,
                endDate: dates.end,
                advLevel: advLevel != "" ? advLevel : "Extreme",
                distance: route.distance,
                duration: route.duration,
            })
            .then(
                (success) => {
                    setAdvLevel("");
                    setLocation({ start: null, end: null });
                    setDates({ start: null, end: null });
                    setTripName("");
                    setShowModal(false);
                    setEditMode(false);
                    setTripId(null);
                    router.push("/trips/list/user");
                },
                (fail) => {
                    setAlert("Trip failed to save");
                }
            );
    };

    const handleEdit = () => {
        axios
            .patch(`/trip/${tripId}`, {
                name: tripName,
                start: {
                    place_name: location.start.place_name,
                    lng: location.start.center[0],
                    lat: location.start.center[1],
                },
                end: {
                    place_name: location.end.place_name,
                    lng: location.end.center[0],
                    lat: location.end.center[1],
                },
                startDate: dates.start,
                endDate: dates.end,
                advLevel: advLevel != "" ? advLevel : "Extreme",
                distance: route.distance,
                duration: route.duration,
            })
            .then(
                (success) => {
                    setAdvLevel("");
                    setLocation({ start: null, end: null });
                    setDates({ start: null, end: null });
                    setTripName("");
                    setShowModal(false);
                    setEditMode(false);
                    setTripId(null);
                    router.push("/trips/list/user");
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
                        Name your Trip:
                    </div>
                    <TextInput
                        value={tripName}
                        onChange={(e) => setTripName(e.target.value)}
                    />
                </div>
                <div className="w-full my-3 flex flex-col text-slate-700 font-light text-sm">
                    <div className="my-1 text-slate-900 font-semibold">
                        Review:
                    </div>
                    <div
                        className={`flex flex-col gap-2 text-sm text-slate-900 pt-2 pb-4`}
                    >
                        <div className="flex flex-row gap-2">
                            <p>{location.start?.place_name ?? ""}</p>
                            <p className="font-semibold text-green-600">to</p>
                            <p>{location.end?.place_name ?? ""}</p>
                        </div>
                        <div className="flex flex-row gap-2">
                            <div className=" text-green-600 font-semibold">
                                Adventure Level:
                            </div>
                            <p>{advLevel ?? "not specified"}</p>
                        </div>
                        <div className="flex flex-row gap-2">
                            <div className=" text-green-600 font-semibold">
                                Dates:
                            </div>
                            <p>{dates?.start ?? "not specified"}</p>
                            <p className="font-semibold text-green-600">to</p>
                            <p>{dates?.end ?? "not specified"}</p>
                        </div>
                    </div>
                </div>
                <div className="absolute bottom-4 right-4">
                    <Button type="submit">Submit</Button>
                </div>
            </form>
        </div>
    );
};

export default SaveModal;
