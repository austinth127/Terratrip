import React, { useRef } from "react";
import Geocoder from "../../components/map/Geocoder";
import Map from "../../components/map/Map";
import StopSelector from "../../components/map/StopSelector";
const TripMapper = () => {
    return (
        <div>
            <Map></Map>
            <StopSelector />
        </div>
    );
};

TripMapper.usesMapLayout = true;

export default TripMapper;
