import React, { useRef } from "react";
import Geocoder from "../../components/map/Geocoder";
import Map from "../../components/map/Map";
const TripMapper = () => {
    return (
        <div>
            <Map></Map>
        </div>
    );
};

TripMapper.usesMapLayout = true;

export default TripMapper;
