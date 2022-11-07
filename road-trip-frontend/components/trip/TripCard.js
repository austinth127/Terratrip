import React, { useState } from "react";
import { DarkOutlineButton } from "../general/Buttons";
import ReactStars from "react-stars";
import { colors } from "../../utils/colors";
import dynamic from "next/dynamic";
import { useAtom } from "jotai";
import { tripAtom } from "../../utils/atoms";
import { useRouter } from "next/router";
import axios from "axios";
import useHasMounted from "../../hooks/useHasMounted";
import { makeTripActive } from "../../utils/trip";

const TripCard = ({ trip, deleteCallback }) => {
    const [rating, setRating] = useState(trip.rating);
    const [activeTrip, setActiveTrip] = useAtom(tripAtom);
    const router = useRouter();
    const hasMounted = useHasMounted();

    const handleViewTrip = () => {
        makeTripActive(trip, setActiveTrip);
        router.push("/trips/map");
    };

    const handleEditTrip = () => {
        makeTripActive(trip, setActiveTrip);
        router.push("/trips");
    };

    const handleRating = () => {
        makeTripActive(trip, setActiveTrip);
        router.push("/trips/rate");
    };

    if (!hasMounted) return null;

    let isComplete = false;
    let inProgress = false;

    if (new Date(trip.endDate) < Date.now()) {
        isComplete = true;
    } else if (new Date(trip.startDate) < Date.now()) {
        inProgress = true;
    }

    return (
        <div className="bg-slate-900 bg-opacity-70 p-4 h-48 text-gray-100 rounded-lg relative">
            <div className="flex flex-row justify-start gap-4">
                <h2 className="font-semibold text-lg">{trip.name}</h2>
                <button
                    onClick={deleteCallback}
                    className="text-slate-400 hover:text-slate-500"
                >
                    <i className="fa-solid fa-trash"></i>
                </button>
            </div>
            <div
                className={`absolute top-4 right-4 text-sm italic ${
                    isComplete
                        ? `text-green-600`
                        : inProgress
                        ? `text-green-700`
                        : `text-slate-300`
                }`}
            >
                {isComplete
                    ? "Completed!"
                    : inProgress
                    ? "In Progress"
                    : "Upcoming"}
            </div>
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
                        edit={false}
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
                <DarkOutlineButton onClick={handleRating}>
                    Rate
                </DarkOutlineButton>
                <DarkOutlineButton onClick={handleViewTrip}>
                    View
                </DarkOutlineButton>
                <DarkOutlineButton onClick={handleEditTrip}>
                    Edit
                </DarkOutlineButton>
            </div>
        </div>
    );
};

export default dynamic(() => Promise.resolve(TripCard), {
    ssr: false,
});
