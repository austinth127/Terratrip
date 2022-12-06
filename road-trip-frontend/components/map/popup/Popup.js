import { useAtom } from "jotai";
import React, { useState } from "react";
import {
    popupIsTripStopAtom,
    popupStopAtom,
    startAtom,
    stopsAtom,
} from "../../../utils/atoms";
import { addStopInOrder } from "../../../utils/map/geometryUtils";
import { Button, SmallButton } from "../../general/Buttons";

const Popup = () => {
    const [stop, setStop] = useAtom(popupStopAtom);
    const [isTripStop, setIsTripStop] = useAtom(popupIsTripStopAtom);
    const [stops, setStops] = useAtom(stopsAtom);
    const [start, setStart] = useAtom(startAtom);

    const handleAddStop = () => {
        if (!stops) {
            stops = [];
        }
        addStopInOrder(start, stop, stops, setStops);
        setStop(null);
        setIsTripStop(null);
    };

    if (!stop) {
        return <></>;
    }

    return (
        <div
            className={`text-left p-4 h-fit text-slate-800 text-xs duration-200 rounded-lg my-1 z-10 absolute flex flex-col gap-1 top-16 left-[27%]   max-w-[30rem] min-w-[16rem] ${
                isTripStop ? `bg-gray-100` : `bg-opacity-100 bg-white`
            }`}
        >
            <button
                className="absolute top-2 right-4 w-fit h-fit "
                onClick={() => setStop(null)}
            >
                <i className="fa fa-x fa-solid text-gray-500 fa-xs"></i>
            </button>
            {stop.image && (
                <img
                    src={stop.image}
                    className="bg-cover m-2 rounded-lg w-fit h-48"
                ></img>
            )}
            <p className="text-green-600 font-semibold">{stop.place_name}</p>
            {stop.categories ? (
                <p className="text-slate-600 font-light">
                    Types:{" "}
                    {stop.categories
                        .map((cat) => {
                            let words = cat.split(".");
                            return words[words.length - 1];
                        })
                        .join(", ")}
                </p>
            ) : (
                <></>
            )}
            {stop?.address && <p>{stop.address}</p>}
            {stop?.address && (
                <p className="text-slate-700 font-light">
                    {stop.phone_contact}
                </p>
            )}
            {stop?.description && (
                <p className="text-green-600 font-base line-clamp-4 text-ellipsis">
                    {stop.description}
                </p>
            )}
            {stop?.phoneContact && (
                <p className="text-slate-700 font-light">{stop.phoneContact}</p>
            )}
            {stop?.website && (
                <a
                    className="text-blue-600 hover:underline font-light text-ellipsis"
                    href={stop.website.split(";")[0]}
                    target="_blank"
                >
                    {stop.website.split(";")[0]}
                </a>
            )}

            <div className="mt-2">
                <SmallButton onClick={handleAddStop}>Add to Trip</SmallButton>
            </div>
        </div>
    );
};

export default Popup;
