import { useAtom } from "jotai";
import React from "react";
import {
    popupIsTripStopAtom,
    popupStopAtom,
    stopsAtom,
} from "../../../utils/atoms";
import {
    metersToMileString,
    secondsToTimeString,
} from "../../../utils/map/geometryUtils";
import Accordion from "../../accordion/Accordion";

const TripStopItem = ({ stop, order, leg }) => {
    const [stops, setStops] = useAtom(stopsAtom);
    const [popup, setPopup] = useAtom(popupStopAtom);
    const [isTripStop, setIsTripStop] = useAtom(popupIsTripStopAtom);

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
        <>
            <div
                className={`bg-slate-900 bg-opacity-80 rounded-lg p-2 overflow-x-clip`}
            >
                <div className="flex gap-4 flex-row items-center min-w-full">
                    <div
                        className={`bg-green-600 text-white rounded-full font-bold font-lg h-10 w-10 text-center shrink-0 flex flex-col items-center justify-center ${
                            start || end ? "text-xs" : ""
                        }`}
                    >
                        {start ? "Start" : end ? "End" : order}
                    </div>
                    <div className="text-slate-100 text-xs font-light w-full">
                        <div className="flex flex-row gap-3">
                            <p className="text-green-600 font-semibold text-sm">
                                {stop.place_name}
                            </p>
                            <button
                                onClick={() => {
                                    setPopup(stop);
                                    setIsTripStop(true);
                                }}
                            >
                                <i className="fa-solid fa-eye"></i>
                            </button>
                        </div>

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
                <div className="w-full h-fit relative text-sm my-2 mx-1">
                    {leg && (
                        <Accordion
                            darkBorder={true}
                            defaultToExpanded={false}
                            header={
                                <>
                                    <h2 className="text-green-600">
                                        Directions:
                                    </h2>
                                </>
                            }
                        >
                            <div className="px-1 py-2">
                                {leg?.steps.map((step, index) => (
                                    <div
                                        key={step.maneuver.instruction + index}
                                        className="font-light"
                                    >
                                        <p>
                                            <span className="text-green-600 font-normal">
                                                {index + 1 + ". "}
                                            </span>
                                            {step.maneuver.instruction}
                                        </p>
                                        {index != leg?.steps.length - 1 && (
                                            <div className="flex flex-row my-1 items-center gap-3 w-full mb-4 text-slate-300">
                                                <p>
                                                    {metersToMileString(
                                                        step.distance
                                                    )}
                                                </p>
                                                <p>
                                                    {secondsToTimeString(
                                                        step.duration
                                                    )}
                                                </p>
                                            </div>
                                        )}
                                    </div>
                                ))}
                            </div>
                        </Accordion>
                    )}
                </div>
                {leg && (
                    <div className="flex flex-row my-1 items-center gap-3 w-full text-sm text-slate-300">
                        <p>{metersToMileString(leg.distance)}</p>
                        <p>{secondsToTimeString(leg.duration)}</p>
                    </div>
                )}
            </div>
        </>
    );
};

export default TripStopItem;
