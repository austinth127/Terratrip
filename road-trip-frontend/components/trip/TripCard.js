import React, { useState } from "react";
import {
    Button,
    darkOutlineBtnStyle,
    DarkOutlineButton,
    stdBtnStyle,
} from "../general/Buttons";
import ReactStars from "react-stars";
import { colors } from "../../utils/colors";
import dynamic from "next/dynamic";
import { useAtom, useSetAtom } from "jotai";
import {
    advLevelAtom,
    editModeAtom,
    locAtom,
    tripDateAtom,
    tripIdAtom,
    tripNameAtom,
} from "../../utils/atoms";
import { useRouter } from "next/router";
import axios from "axios";

// /trip/{id}/rate post body {rating: Double}

const TripCard = ({ trip }) => {
    const [rating, setRating] = useState(trip.rating);
    const [isEditRating, setIsEditRating] = useState(false);

    const setLoc = useSetAtom(locAtom);
    const setDate = useSetAtom(tripDateAtom);
    const setName = useSetAtom(tripNameAtom);
    const setAdvLevel = useSetAtom(advLevelAtom);
    const setEditMode = useSetAtom(editModeAtom);
    const setTripId = useSetAtom(tripIdAtom);
    const router = useRouter();

    const handleViewTrip = () => {
        setLoc({ start: trip.start, end: trip.end });
        setDate({ start: trip.startDate, end: trip.endDate });
        setName(trip.name);
        setAdvLevel(trip.advLevel);
        setEditMode(true);
        setTripId(trip.id);
        router.push("/trips/map");
    };
    if (typeof window === "undefined") return;

    const handleRating = () => {
        if (rating == null) {
            setRating(0);
        }
    };

    return (
        <div className="bg-slate-900 bg-opacity-70 p-4 h-48 text-gray-100 rounded-lg relative">
            <h2 className="font-semibold text-lg">{trip.name}</h2>
            <div
                className={`flex flex-col gap-2 text-xs text-gray-50 pt-2 pb-4 ${
                    trip.start?.place_name != null ? `` : `hidden`
                }`}
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
                    <div className=" text-green-600 font-semibold">Dates:</div>
                    <p>{trip.startDate ?? "not specified"}</p>
                    <p className="font-semibold text-green-600">to</p>
                    <p>{trip.endDate ?? "not specified"}</p>
                </div>
            </div>

            {rating != null ? (
                <div className="absolute bottom-4 left-4">
                    <ReactStars
                        count={5}
                        size={24}
                        color2={colors.green600}
                        half
                        value={rating}
                        edit={true}
                        onChange={(newVal) => {
                            axios.post(`/trip/${trip.id}/rate`, {
                                rating: newVal,
                            });
                            setRating(rating);
                        }}
                    />
                </div>
            ) : (
                <div className="text-sm text-slate-500 absolute bottom-4 left-4">
                    Not yet rated.
                </div>
            )}
            <div className="absolute bottom-4 right-4 flex flex-row gap-4">
                {rating == null ? (
                    <DarkOutlineButton onClick={handleRating}>
                        Rate
                    </DarkOutlineButton>
                ) : (
                    <></>
                )}
                <DarkOutlineButton onClick={handleViewTrip}>
                    View
                </DarkOutlineButton>
            </div>
        </div>
    );
};

export default dynamic(() => Promise.resolve(TripCard), {
    ssr: false,
});
