import React, { useEffect, useState, useRef } from "react";
import mapboxgl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import { useAtom, useAtomValue } from "jotai";
import { flyTo, getRouteWithStops } from "../../utils/map/geometryUtils";
import {
    allLocationsAtom,
    endAtom,
    recStopAtom,
    routeAtom,
    routeGeoJsonAtom,
    startAtom,
    stopsAtom,
} from "../../utils/atoms";
import { colors } from "../../utils/colors";
import { getStopOrderText } from "../../utils/stringUtils";
import ReactDOMServer from "react-dom/server";
import RecStopItem from "./stopRecs/RecStopItem";
import RecStopPopup from "./stopRecs/RecStopPopup";

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

    const locs = useAtomValue(allLocationsAtom);
    const stops = useAtomValue(stopsAtom);
    const [start, setStart] = useAtom(startAtom);
    const [end, setEnd] = useAtom(endAtom);

    const [route, setRoute] = useAtom(routeAtom);
    const [routeGeoJson, setRouteGeoJson] = useAtom(routeGeoJsonAtom);
    const recStops = useAtomValue(recStopAtom);

    /** @type {React.MutableRefObject<mapboxgl.Layer>} */
    const routerLayer = useRef(null);

    const [markers, setMarkers] = useState([]);
    const [recStopMarkers, setRecStopMarkers] = useState([]);

    async function addRoute() {
        if (!map.current || !locs || locs.length < 2) return;
        const [route, geojson] = await getRouteWithStops(locs);

        setRoute(route);
        setRouteGeoJson(geojson);

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
                    "line-color": colors.green700,
                    "line-width": 5,
                    "line-opacity": 0.5,
                },
            });
        }

        markers.forEach((marker) => marker.remove());
        setMarkers([]);

        locs.forEach((location, index) => {
            // var popup = new mapboxgl.Popup().setText(
            //     marker.properties.title
            // );

            const el = document.createElement("div");
            const width = 30;
            const height = 30;
            el.className = "trip-marker";
            el.style.width = `${width}px`;
            el.style.height = `${height}px`;
            el.textContent = getStopOrderText(stops, index);
            el.style.fontSize = "10px";
            el.style.lineHeight = "14px";

            // el.addEventListener("click", () => {
            //     window.alert(marker.properties.message);
            // });

            // Add markers to the map.
            const marker = new mapboxgl.Marker(el)
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

    useEffect(() => {
        if (!routeGeoJson || !map.current) return;

        /* add a button for this as well */
        flyTo(map, routeGeoJson);
        // Calculate Appropriate Default Zoom
    }, [routeGeoJson]);

    useEffect(() => {
        if (!map.current || !recStops) return;

        async function addMarkers() {
            recStopMarkers.forEach((marker) => marker.remove());
            setMarkers([]);

            recStops.forEach((stop, index) => {
                const popupElement = ReactDOMServer.renderToStaticMarkup(
                    <RecStopPopup stop={stop} />
                );

                const popup = new mapboxgl.Popup({
                    closeButton: false,
                }).setHTML(popupElement);

                // Add markers to the map.
                const marker = new mapboxgl.Marker({
                    color: colors.slate800,
                    scale: ".65",
                })
                    .setLngLat(stop.center)
                    .setPopup(popup)
                    .addTo(map.current);

                recStopMarkers.push(marker);
            });
            setRecStopMarkers([...recStopMarkers]);
        }
        addMarkers();
    }, [recStops, stops]);

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
