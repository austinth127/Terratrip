import React, { useState } from "react";
import MapNav from "../navigation/MapNav";

const MapLayout = ({ children, ...props }) => {
    return (
        <div className="text-gray-50 bg-slate-900">
            <MapNav />
            <main>{children}</main>
        </div>
    );
};

export default MapLayout;
