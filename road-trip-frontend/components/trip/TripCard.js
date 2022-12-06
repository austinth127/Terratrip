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
import { LoadingSpinnerSmall } from "../general/LoadingSpinner";

const TripCard = ({ trip, deleteCallback }) => {
    const [rating, setRating] = useState(trip.rating);
    const [activeTrip, setActiveTrip] = useAtom(tripAtom);
    const router = useRouter();
    const hasMounted = useHasMounted();
    const [loading, setLoading] = useState();

    const handleViewTrip = () => {
        setActiveTrip(trip);
        router.push("/trips/map");
    };

    const handleEditTrip = () => {
        setActiveTrip(trip);
        router.push("/trips");
    };

    const handleRating = () => {
        setActiveTrip(trip);
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
            <div className="flex flex-row justify-start gap-4 items-center">
                <h2 className="font-semibold text-lg">{trip.name}</h2>
                {trip.playlistId && (
                    <a
                        href={`https://open.spotify.com/playlist/${trip.playlistId}`}
                        target="_blank"
                    >
                        <i className="fa fa-brands fa-spotify"></i>
                    </a>
                )}
                <button
                    onClick={() => {
                        setLoading(true);
                        deleteCallback();
                    }}
                    className="text-slate-400 hover:text-slate-500"
                >
                    <i className="fa-solid fa-trash"></i>
                </button>
                {loading && (
                    <LoadingSpinnerSmall text="Deleting..."></LoadingSpinnerSmall>
                )}
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

            <div className="absolute bottom-4 left-4">
                <ReactStars
                    count={5}
                    size={24}
                    color2={colors.green600}
                    half
                    value={rating}
                    edit={true}
                    onChange={(newVal) => {
                        axios.post(`/api/trip/${trip.id}/rate`, {
                            rating: newVal,
                        });
                        setRating(newVal);
                    }}
                />
                {rating == null ? (
                    <p className="italic text-xs font-light text-slate-400 -mt-1">
                        Unrated
                    </p>
                ) : (
                    <></>
                )}
            </div>

            <div className="absolute bottom-4 right-4 flex flex-row gap-4">
                {trip.stops && trip.stops.length > 0 ? (
                    <DarkOutlineButton onClick={handleRating}>
                        Rate Stops
                    </DarkOutlineButton>
                ) : (
                    <></>
                )}

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
