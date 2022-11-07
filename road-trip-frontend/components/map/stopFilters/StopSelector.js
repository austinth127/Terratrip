import React, { useState } from "react";
import Geocoder from "../Geocoder";
import TextInput from "../../general/TextInput";
import StopFilters from "../stopFilters/StopFilters";
import RecStopList from "../stopRecs/RecStopList";

const StopSelector = () => {
    const [stop, setStop] = useState();
    return (
        <div
            className="bg-gray-50 border border-gray-100 rounded-lg sm:w-1/4 h-[85vh]
                        fixed z-20 top-14 left-2 isolate flex flex-col gap-1 py-4"
        >
            <div className="border-b border-gray-300 pb-4 px-2 mx-2 text-slate-900">
                <h1 className="mb-2 font-semibold text-green-600 text-sm">
                    Add Stop
                </h1>
                <Geocoder callback={setStop} InputComponent={TextInput} />
            </div>
            <RecStopList />
            <StopFilters />
        </div>
    );
};

export default StopSelector;