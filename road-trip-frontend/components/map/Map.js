import React, { useEffect, useState, useRef } from "react";
import mapboxgl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";

mapboxgl.accessToken = process.env.NEXT_PUBLIC_MAPBOX_ACCESS_TOKEN;

/**
 * Mapbox map
 *
 * @TODO add directions https://docs.mapbox.com/help/tutorials/getting-started-directions-api/
 *
 * @returns {JSX.Element} Mapboxgl map
 */
const Map = () => {
    /** @type {React.MutableRefObject<JSX.Element>} */
    const mapContainer = useRef(null);
    /** @type {React.MutableRefObject<mapboxgl.Map>} */
    const map = useRef(null);

    // Longitude, Lattitude, Zoom
    const [lng, setLng] = useState(-97.5);
    const [lat, setLat] = useState(40);
    const [zoom, setZoom] = useState(3.4);

    // Initialize mapbox map
    useEffect(() => {
        if (map.current) return;
        map.current = new mapboxgl.Map({
            container: mapContainer.current,
            style: "mapbox://styles/mapbox/outdoors-v11",
            center: [lng, lat],
            zoom: zoom,
        });
    });

    // Update longitude/lattitude/zoom as the user moves around
    useEffect(() => {
        if (!map.current) return;
        map.current.on("move", () => {
            setLng(map.current.getCenter().lng.toFixed(4));
            setLat(map.current.getCenter().lat.toFixed(4));
            setZoom(map.current.getZoom().toFixed(2));
        });
    });

    return (
        <div className="relative">
            <div className="bg-slate-800 bg-opacity-80 py-1.5 px-3 font-mono z-[1] absolute top-12 left-0 m-3 rounded-lg">
                Longitude: {lng} | Latitude: {lat} | Zoom: {zoom}
            </div>
            {/* Map */}
            <div ref={mapContainer} className="h-[100vh]"></div>
        </div>
    );
};

export default Map;
