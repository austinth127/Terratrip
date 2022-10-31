import React, { useEffect, useState, useRef } from "react";
import mapboxgl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import { useAtom } from "jotai";
import { endAtom, routeAtom, startAtom } from "../../utils/atoms";
import { getPoints, getRoute } from "../../utils/map/geometryUtils";
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

    const [lng, setLng] = useState(-97.141);
    const [lat, setLat] = useState(31.55);
    const [zoom, setZoom] = useState(3.4);

    const [start, setStart] = useAtom(startAtom);
    const [end, setEnd] = useAtom(endAtom);
    const [route, setRoute] = useAtom(routeAtom);

    // Initialize mapbox map
    useEffect(() => {
        if (map.current) return;
        map.current = new mapboxgl.Map({
            container: mapContainer.current,
            style: "mapbox://styles/mapbox/outdoors-v11",
            center: [lng, lat],
            zoom: zoom,
        });

        async function addRoute() {
            const [route, geojson] = await getRoute(start, end);

            setRoute(route);

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
        }

        map.current.on("load", () => {
            addRoute();

            const points = getPoints(start.center, end.center);
            // var popup = new mapboxgl.Popup()
            // .setText('Description')
            // .addTo(map.current);
            // marker = new mapboxgl.Marker()
            //     .setLngLat(points)
            //     .addTo(map.current)
            //     .setPopup(popup);
            map.current.addLayer({
                id: "routeEndpoints",
                type: "circle",
                source: {
                    type: "geojson",
                    data: points,
                },
                paint: {
                    "circle-radius": 10,
                    "circle-color": colors.green600,
                },
            });
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
            <div className="bg-slate-800 bg-opacity-80 py-1.5 px-3 font-mono text-xs z-[1] absolute top-12 right-0 m-3 rounded-lg">
                Longitude: {lng} | Latitude: {lat} | Zoom: {zoom}
            </div>
            {/* Map */}
            <div ref={mapContainer} className="h-[100vh]"></div>
        </div>
    );
};

export default Map;

//when user clicks on the map create a red dot and display route between red dot and Waco
// map.current.on("click", (event) => {
//     const coords = Object.keys(event.lngLat).map(
//         (key) => event.lngLat[key]
//     );

//     getRoute(coords);
// });

// //If this is first click need to create new layer.
// // if (map.current.getLayer("end")) {
// //     map.current.getSource("end").setData(endPoint);
// // } else {

// }
