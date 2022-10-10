import React, { useState } from "react";
import { DarkOutlineButton } from "../general/Buttons";
import ReactStars from "react-stars";
import { colors } from "../../utils/colors";
import dynamic from "next/dynamic";

const TripCard = ({ trip }) => {
    const [rate, setRate] = useState(0);

    if (typeof window === "undefined") return;
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
                    <p>{trip.dates?.start ?? "not specified"}</p>
                    <p className="font-semibold text-green-600">to</p>
                    <p>{trip.dates?.end ?? "not specified"}</p>
                </div>
            </div>

            {trip.rating ? (
                <div className="absolute bottom-4 left-4">
                    <ReactStars
                        count={5}
                        size={24}
                        color2={colors.green600}
                        half
                        value={trip.rating}
                        edit={false}
                    />
                </div>
            ) : (
                <div className="text-sm text-slate-500 absolute bottom-4 left-4">
                    Not yet rated.
                </div>
            )}
            <div className="absolute bottom-4 right-4 flex flex-row gap-4">
                {!trip.rating ? (
                    <DarkOutlineButton>Rate</DarkOutlineButton>
                ) : (
                    <></>
                )}
                <DarkOutlineButton>View</DarkOutlineButton>
            </div>
        </div>
    );
};

export default dynamic(() => Promise.resolve(TripCard), {
    ssr: false,
});
