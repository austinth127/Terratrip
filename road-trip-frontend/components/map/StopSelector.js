import React, { useState } from "react";
import Geocoder from "./Geocoder";
import TextInput from "../general/TextInput";
import StopFilters from "./StopFilters";

const StopSelector = () => {
    const [stop, setStop] = useState();
    return (
        <div
            className="bg-gray-50 border border-gray-100 rounded-lg sm:w-1/4 h-[85vh]
                        fixed z-20 top-14 left-2 isolate flex flex-col gap-2 py-4"
        >
            <div className="border-b border-gray-300 pb-4 px-2 mx-2 text-slate-900">
                <h1 className="mb-2 font-semibold text-slate-800">Add Stop</h1>
                <Geocoder callback={setStop} InputComponent={TextInput} />
            </div>
            <StopFilters />
        </div>
    );
};

export default StopSelector;
