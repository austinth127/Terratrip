import { useAtom } from "jotai";
import React from "react";
import { stopsAtom } from "../../../utils/atoms";

const TripStopItem = ({ stop, order }) => {
    const [stops, setStops] = useAtom(stopsAtom);

    const handleMoveUp = () => {
        const thisIndex = order - 1;
        stops[thisIndex] = stops.splice(thisIndex - 1, 1, stops[thisIndex])[0];
        setStops([...stops]);
    };

    const handleMoveDown = () => {
        const thisIndex = order - 1;
        stops[thisIndex] = stops.splice(thisIndex + 1, 1, stops[thisIndex])[0];
        setStops([...stops]);
    };
    const handleDelete = () => {
        const thisIndex = order - 1;
        stops.splice(thisIndex, 1);
        setStops([...stops]);
    };

    if (!stop) {
        return null;
    }

    const len = stops ? stops.length : 0;
    const start = false,
        end = false;
    if (order == 0) {
        start = true;
    } else if (order == len + 1) {
        end = true;
    }

    return (
        <div
            className={`bg-slate-900 bg-opacity-80 rounded-lg flex flex-row items-center p-2 gap-4`}
        >
            <div
                className={`bg-green-600 text-white rounded-full font-bold font-lg h-10 w-10 text-center shrink-0 flex flex-col items-center justify-center ${
                    start || end ? "text-xs" : ""
                }`}
            >
                {start ? "Start" : end ? "End" : order}
            </div>
            <div className="text-slate-100 text-xs font-light">
                <p className="text-green-600 font-semibold text-sm">
                    {stop.place_name}
                </p>
                {stop.categories ? (
                    <p className="text-slate-300 font-light">
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
                <p className="text-slate-400 font-light">
                    {stop.phone_contact ?? ""}
                </p>
            </div>
            {!start && !end ? (
                <div className="w-fit h-full flex flex-col justify-center text-slate-500 justify-self-end">
                    {order !== 1 ? (
                        <button
                            className="hover:text-slate-300 duration-200"
                            onClick={handleMoveUp}
                        >
                            <i className="fa-solid fa-arrow-up "></i>
                        </button>
                    ) : (
                        <></>
                    )}
                    {order != stops.length ? (
                        <button
                            className="hover:text-slate-300 duration-200"
                            onClick={handleMoveDown}
                        >
                            <i className="fa-solid fa-arrow-down"></i>
                        </button>
                    ) : (
                        <></>
                    )}

                    <button
                        className="hover:text-slate-300 duration-200"
                        onClick={handleDelete}
                    >
                        <i className="fa-solid fa-trash"></i>
                    </button>
                </div>
            ) : (
                <></>
            )}
        </div>
    );
};

export default TripStopItem;
