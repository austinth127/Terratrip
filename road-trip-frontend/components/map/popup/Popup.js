import { useAtom } from "jotai";
import React, { useState } from "react";
import { popupStopAtom, stopsAtom } from "../../../utils/atoms";
import { Button, SmallButton } from "../../general/Buttons";

const Popup = () => {
    const [stop, setStop] = useAtom(popupStopAtom);
    const [stops, setStops] = useAtom(stopsAtom);

    const handleAddStop = () => {
        if (!stops) {
            stops = [];
        }
        setStops([...stops, stop]);
        setStop(null);
    };

    if (!stop) {
        return <></>;
    }

    return (
        <div className="text-left p-4 h-fit text-slate-800 text-xs duration-200 rounded-lg my-1 z-10 absolute top-16 left-[27%] bg-white bg-opacity-90 max-w-[30rem]">
            <button
                className="absolute top-2 right-4 w-fit h-fit "
                onClick={() => setStop(null)}
            >
                <i className="fa fa-x fa-solid text-gray-500 fa-xs"></i>
            </button>
            <p className="text-green-600 font-semibold  ">{stop.place_name}</p>
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
            <p className="">{stop.address ?? ""}</p>
            <p className="text-slate-700 font-light mb-2">
                {stop.phone_contact ?? ""}
            </p>
            <SmallButton onClick={handleAddStop}>Add to Trip</SmallButton>
        </div>
    );
};

export default Popup;
