import React, { useEffect, useState, useRef } from "react";
import mapboxgl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import { useAtom, useAtomValue } from "jotai";
import {
    getPoints,
    getRoute,
    getRouteWithStops,
} from "../../utils/map/geometryUtils";
import {
    allLocationsAtom,
    endAtom,
    routeAtom,
    startAtom,
    stopsAtom,
} from "../../utils/atoms";
import { colors } from "../../utils/colors";

mapboxgl.accessToken = process.env.NEXT_PUBLIC_MAPBOX_ACCESS_TOKEN;

/**
 * Mapbox map
 *
 * @TODO add directions https://docs.mapbox.com/help/tutorials/getting-started-directions-api/
 * @TODO adjust lng/lat/zoom for start/end location
 * @param {Object} props
 * @returns {JSX.Element} Mapboxgl map
 */
const Map = ({ ...props }) => {
    /** @type {React.MutableRefObject<mapboxgl.Map>} */
    const mapContainer = useRef(null);
    /** @type {React.MutableRefObject<mapboxgl.Map>} */
    const map = useRef(null);
    const [isLoaded, setisLoaded] = useState(false);

    const [lng, setLng] = useState(-97.141);
    const [lat, setLat] = useState(31.55);
    const [zoom, setZoom] = useState(3.4);

    const [start, setStart] = useAtom(startAtom);
    const [end, setEnd] = useAtom(endAtom);
    const [route, setRoute] = useAtom(routeAtom);
    const locs = useAtomValue(allLocationsAtom);
    const [markers, setMarkers] = useState([]);

    async function addRoute() {
        if (!map.current) return;
        if (!locs || locs.length < 2) return;
        const [route, geojson] = await getRouteWithStops(locs);

        setRoute(route);
        if (!locs || !route) {
            return;
        }

        //if route layer exists, update with new route, else create the layer and add the route.
        if (map.current.getSource("route")) {
            map.current.getSource("route").setData(geojson);
        } else {
            map.current.addLayer({
                id: "route",
                type: "line",
                source: {
                    type: "geojson",
                    data: geojson,
                },
                layout: {
                    "line-join": "round",
                    "line-cap": "round",
                },
                paint: {
                    "line-color": colors.green600,
                    "line-width": 5,
                    "line-opacity": 0.5,
                },
            });
        }

        markers.forEach((marker) => marker.remove());
        setMarkers([]);

        locs.forEach((location) => {
            // var popup = new mapboxgl.Popup().setText(
            //     marker.properties.title
            // );

            // const el = document.createElement("div");
            // const width = marker.properties.iconSize[0];
            // const height = marker.properties.iconSize[1];
            // el.className = "marker";
            // el.style.backgroundImage = `url(https://placekitten.com/g/${width}/${height}/)`;
            // el.style.width = `${width}px`;
            // el.style.height = `${height}px`;
            // el.style.backgroundSize = "100%";

            // el.addEventListener("click", () => {
            //     window.alert(marker.properties.message);
            // });

            // Add markers to the map.
            const marker = new mapboxgl.Marker()
                .setLngLat(location.center)
                .addTo(map.current);

            const popup = new mapboxgl.Popup();
            popup.addTo(map.current);
            popup.setLngLat(location.center);
            markers.push(marker);
        });
        setMarkers([...markers]);
    }
    // Initialize mapbox map
    useEffect(() => {
        if (map.current) return;
        map.current = new mapboxgl.Map({
            container: mapContainer.current,
            style: "mapbox://styles/mapbox/outdoors-v11",
            center: [lng, lat],
            zoom: zoom,
        });

        map.current.on("load", () => {
            // Add the route to the map between start and end locations
            setisLoaded(true);
            addRoute();
        });
    });

    useEffect(() => {
        if (!isLoaded || !locs) return;
        addRoute();
    }, [locs]);

    // onMove
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
            <div className="bg-slate-800 bg-opacity-80 py-1.5 px-3 font-mono text-xs z-[1] absolute top-12 right-0 m-3 rounded-lg">
                Longitude: {lng} | Latitude: {lat} | Zoom: {zoom}
            </div>
            {/* Map */}
            <div ref={mapContainer} className="h-[100vh]"></div>
        </div>
    );
};

export default Map;

//INCOMPLETE: this would add a marker at each point. (a marker can have text associated, hover etc.)
// stops.features.forEach(function(marker){
//     var popup = new mapboxgl.Popup()
//         .setText(marker.properties.title);
//     new mapboxgl.Marker().setLng(marker.geometry.coordinates[1]).setLat(marker.geometry.coordinates[0]).addTo(map.current);
// });

// // Create a new marker.
// const marker = new mapboxgl.Marker()
//     .setLngLat([30.5, 50.5])
//     .addTo(map.current);

// var popup = new mapboxgl.Popup()
// .setText('Description')
// .addTo(map.current);
// marker = new mapboxgl.Marker()
//     .setLngLat(points)
//     .addTo(map.current)
//     .setPopup(popup);
