import React, { useEffect, useState, useRef } from "react";
import mapboxgl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import { useAtom, useAtomValue } from "jotai";
import { flyTo, getRouteWithStops } from "../../utils/map/geometryUtils";
import {
    allLocationsAtom,
    endAtom,
    filtersAtom,
    popupStopAtom,
    recStopAtom,
    routeAtom,
    routeGeoJsonAtom,
    startAtom,
    stopsAtom,
    tripIdAtom,
} from "../../utils/atoms";
import { colors } from "../../utils/colors";
import { getStopOrderText } from "../../utils/stringUtils";
import { useRouter } from "next/router";
import axios from "axios";
import { delay } from "../../utils/delay";

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
    const [recStops, setRecStops] = useAtom(recStopAtom);

    /** @type {React.MutableRefObject<mapboxgl.Layer>} */
    const routerLayer = useRef(null);

    const [markers, setMarkers] = useState([]);
    const popupRef = useRef(new mapboxgl.Popup());
    const [recStopMarkers, setRecStopMarkers] = useState([]);
    const [popupStop, setPopupStop] = useAtom(popupStopAtom);
    const tripId = useAtomValue(tripIdAtom);
    const filters = useAtomValue(filtersAtom);
    const [done, setDone] = useState(false);

    const router = useRouter();

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

            // Add markers to the map.
            const marker = new mapboxgl.Marker(el)
                .setLngLat(location.center)
                .addTo(map.current);

            markers.push(marker);
        });
        setMarkers([...markers]);

        return route;
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
            addRoute().then((route) => {
                if (!route?.geometry?.coordinates) return;
                axios
                    .post("/api/location/recommend", {
                        tripId: tripId,
                        range: 50000,
                        categories: filters,
                        route: route.geometry.coordinates,
                        limit: 50,
                    })
                    .then(async () => {
                        let count = 0;
                        while (!done && map.current && count < 30) {
                            await delay(1000);
                            axios.get("/api/location/recommend?limit=50").then(
                                (res) => {
                                    let newRec = res.data.locations.filter(
                                        (loc) =>
                                            loc.place_name &&
                                            loc.center &&
                                            loc.address
                                    );
                                    setRecStops([...newRec]);
                                    done = res.data.isDone;
                                    setDone(done);
                                    console.log(res.data);
                                },
                                (err) => console.log(err)
                            );
                            count++;
                        }
                    });
            });
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
        if (!map.current) return;
        if (!recStops && recStopMarkers) {
            recStopMarkers.forEach((marker) => marker.remove());
            setRecStopMarkers([]);
            return;
        }

        async function addMarkers() {
            recStopMarkers.forEach((marker) => marker.remove());
            setRecStopMarkers([]);

            recStops
                .filter((stop) => stop.center != null)
                .forEach((stop, index) => {
                    // Add markers to the map.
                    const marker = new mapboxgl.Marker({
                        color: colors.slate800,
                        scale: ".65",
                        style: { cursor: "pointer" },
                    })
                        .setLngLat(stop.center)
                        .addTo(map.current);

                    marker.getElement().addEventListener("click", () => {
                        setPopupStop(stop);
                    });

                    recStopMarkers.push(marker);
                });
            setRecStopMarkers([...recStopMarkers]);
        }
        addMarkers();

        return () => {
            recStopMarkers.forEach((marker) => marker.remove());
            setRecStopMarkers([]);
        };
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

    // prompt the user if they try and leave with unsaved changes
    useEffect(() => {
        const handleWindowClose = (e) => {
            if (markers) {
                markers.forEach((marker) => marker.remove());
            }
            setMarkers([]);
            if (recStopMarkers) {
                recStopMarkers.forEach((marker) => marker.remove());
            }
            setRecStopMarkers([]);
            setPopupStop(null);
            map.current.remove();
        };

        window.addEventListener("beforeunload", handleWindowClose);
        return () => {
            window.removeEventListener("beforeunload", handleWindowClose);
        };
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
