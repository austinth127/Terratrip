import { useAtom, useSetAtom } from "jotai";
import React from "react";
import { popupStopAtom, stopsAtom } from "../../../utils/atoms";

const RecStopItem = ({ stop }) => {
    const setPopupStop = useSetAtom(popupStopAtom);

    const handleAddStop = () => {
        setPopupStop(stop);
    };

    return (
        <button
            className="text-left p-1 w-full h-fit text-slate-800 text-xs hover:bg-slate-200 hover:cursor-pointer duration-200 relative rounded-lg my-1"
            onClick={handleAddStop}
        >
            <p className="text-green-600 font-semibold mr-6">
                {stop?.place_name}
            </p>
            {stop.categories ? (
                <p className="text-slate-600 font-light mr-6">
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
                {stop?.phoneContact ?? ""}
            </p>
            {stop?.website && (
                <a
                    className="text-blue-600 hover:underline font-light text-ellipsis"
                    href={stop.website.split(";")[0]}
                    target="_blank"
                >
                    {stop.website.split(";")[0]}
                </a>
            )}

            <div className="absolute top-2 right-2 text-gray-400 flex flex-row gap-2">
                {stop?.image && (
                    <span>
                        <i className="fa fa-image"></i>
                    </span>
                )}
                {stop?.description && (
                    <span>
                        <i className="fa-solid fa-ellipsis"></i>
                    </span>
                )}
            </div>
        </button>
    );
};

export default RecStopItem;
