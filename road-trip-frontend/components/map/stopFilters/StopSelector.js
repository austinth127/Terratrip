import React, { useEffect, useState } from "react";
import Geocoder from "../Geocoder";
import TextInput from "../../general/TextInput";
import StopFilters from "../stopFilters/StopFilters";
import RecStopList from "../stopRecs/RecStopList";
import { useAtom } from "jotai";
import { popupStopAtom, stopsAtom } from "../../../utils/atoms";
import { getRoute, getRouteWithStops } from "../../../utils/map/geometryUtils";
import Alert from "../../auth/Alert";

const StopSelector = () => {
    const [mapboxStop, setMapboxStop] = useState();
    const [alert, setAlert] = useState();
    const [popupStop, setPopupStop] = useAtom(popupStopAtom);

    const [stops, setStops] = useAtom(stopsAtom);

    useEffect(() => {
        if (!mapboxStop) return;
        console.log(mapboxStop);
        const stop = {
            place_name: mapboxStop.text,
            address: mapboxStop.place_name,
            center: mapboxStop.center,
        };
        if (!stops) {
            stops = [];
        }
        setAlert();
        getRouteWithStops([...stops, stop]).then(
            (success) => {
                setPopupStop(stop);
            },
            (err) => {
                setAlert("Cannot find a route through this stop.");
            }
        );
    }, [mapboxStop]);

    return (
        <div
            className="bg-gray-50 border border-gray-100 rounded-lg sm:w-1/4 h-[85vh]
                        fixed z-20 top-14 left-2 isolate flex flex-col gap-1 py-4"
        >
            <div className="border-b border-gray-300 pb-4 px-2 mx-2 text-slate-900">
                <h1 className="mb-2 font-semibold text-green-600 text-sm">
                    Add Stop
                </h1>
                <Alert message={alert} className="text-xs text-red-500"></Alert>
                <Geocoder callback={setMapboxStop} InputComponent={TextInput} />
            </div>
            <RecStopList />
            <StopFilters />
        </div>
    );
};

export default StopSelector;
