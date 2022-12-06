import { useAtom, useAtomValue } from "jotai";
import React, { useEffect, useState } from "react";
import {
    allLocationsAtom,
    endAtom,
    maxStopsAtom,
    popupIsTripStopAtom,
    popupStopAtom,
    startAtom,
    stopsAtom,
} from "../../../utils/atoms";
import {
    addStopInOrder,
    getNewOrder,
    getRouteWithStops,
} from "../../../utils/map/geometryUtils";
import Alert from "../../auth/Alert";
import { Button, SmallButton } from "../../general/Buttons";
import { LoadingSpinnerSmall } from "../../general/LoadingSpinner";

const Popup = () => {
    const [stop, setStop] = useAtom(popupStopAtom);
    const [isTripStop, setIsTripStop] = useAtom(popupIsTripStopAtom);
    const [stops, setStops] = useAtom(stopsAtom);
    const [start, setStart] = useAtom(startAtom);
    const [alert, setAlert] = useState();
    const [loading, setLoading] = useState();
    const [end, setEnd] = useAtom(endAtom);
    const locs = useAtomValue(allLocationsAtom);
    const maxStops = useAtomValue(maxStopsAtom);

    useEffect(() => {
        setAlert();
    }, [stop]);
    const handleAddStop = () => {
        if (!stops) {
            stops = [];
        }
        setAlert();

        if (stops.length + 1 > maxStops) {
            setAlert(
                "You have reached the max number of stops. Please remove some and try again."
            );
            return;
        }
        setLoading(true);

        getRouteWithStops([...locs, stop]).then(
            (success) => {
                addStopInOrder(start, stop, stops, setStops);
                setLoading(false);
                setStop(null);
                setIsTripStop(null);
            },
            (err) => {
                setLoading(false);
                setAlert("Cannot find a route through this stop.");
            }
        );
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

            <Alert
                message={alert}
                className={"text-xs text-red-400 leading-tight -mb-1"}
            ></Alert>

            {!isTripStop ? (
                <div className="mt-2 flex flex-row gap-2">
                    <SmallButton onClick={handleAddStop}>
                        Add to Trip
                    </SmallButton>
                    {loading && (
                        <LoadingSpinnerSmall text="Calculating Route..."></LoadingSpinnerSmall>
                    )}
                </div>
            ) : (
                <div className="mt-2 italic font-light text-green-600">
                    This stop is part of your trip!
                </div>
            )}
        </div>
    );
};

export default Popup;
