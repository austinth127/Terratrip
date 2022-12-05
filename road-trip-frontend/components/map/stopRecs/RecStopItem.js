import { useAtom } from "jotai";
import React from "react";
import { popupStopAtom, stopsAtom } from "../../../utils/atoms";

const RecStopItem = ({ stop }) => {
    const [stops, setStops] = useAtom(stopsAtom);
    const [popupStop, setPopupStop] = useAtom(popupStopAtom);

    const handleAddStop = () => {
        setPopupStop(stop);
    };

    return (
        <button
            className="text-left p-1 w-full h-fit text-slate-800 text-xs hover:bg-slate-200 hover:cursor-pointer duration-200 rounded-lg my-1"
            onClick={handleAddStop}
        >
            <p className="text-green-600 font-semibold  ">{stop?.place_name}</p>
            {stop.categories ? (
                <p className="text-slate-600 font-light">
                    Types:{" "}
                    {stop?.categories &&
                        stop.categories.length > 0 &&
                        stop.categories
                            .map((cat) => {
                                let words = cat.split(".");
                                return words[words.length - 1];
                            })
                            .join(", ")}
                </p>
            ) : (
                <></>
            )}
            <p className="">{stop?.address ?? ""}</p>
            <p className="text-slate-700 font-light">
                {stop?.phone_contact ?? ""}
            </p>
        </button>
    );
};

export default RecStopItem;
