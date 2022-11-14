import axios from "axios";
import mapboxgl from "mapbox-gl";
import { Location } from "../typedefs";

export const getPoints = (...coords) => {
    const points = coords.map((coord) => ({
        type: "Feature",
        properties: {},
        geometry: {
            type: "Point",
            coordinates: coord.center,
        },
    }));

    return {
        type: "FeatureCollection",
        features: points,
    };
};

export const getRoute = async (start, end) => {
    if (!mapboxgl.accessToken) {
        mapboxgl.accessToken = process.env.NEXT_PUBLIC_MAPBOX_ACCESS_TOKEN;
    }
    const res = await axios.get(
        `https://api.mapbox.com/directions/v5/mapbox/driving/${start.center[0]},${start.center[1]};${end.center[0]},${end.center[1]}?steps=true&geometries=geojson&access_token=${mapboxgl.accessToken}`
    );

    const data = res.data.routes[0];
    const route = data;
    const geojson = {
        type: "Feature",
        properties: {},
        geometry: {
            type: "LineString",
            coordinates: data.geometry.coordinates,
        },
    };

    return [route, geojson];
};

/**
 * Get route from mapbox between the start, end, and stops
 * @param {Location[]} stops
 * @returns
 */
export const getRouteWithStops = async (stops) => {
    if (!stops || stops.length < 2) return null;
    const params = `?steps=true&geometries=geojson&access_token=${mapboxgl.accessToken}`;
    const baseURL = `https://api.mapbox.com/directions/v5/mapbox/driving/`;
    let fail = false;
    let stopString = stops
        .map((stop) => {
            if (!stop || !stop.center || stop.center.length < 2) {
                fail = true;
                return;
            }
            return `${stop.center[0]},${stop.center[1]}`;
        })
        .join(";");

    if (fail) {
        console.error("Failed to get route");
        console.log(stops);
        return [null, null];
    }

    const res = await axios.get(`${baseURL}${stopString}${params}`);

    const data = res.data.routes[0];
    const route = data;
    const geojson = {
        type: "Feature",
        properties: {},
        geometry: {
            type: "LineString",
            coordinates: data.geometry.coordinates,
        },
    };

    return [route, geojson];
};

export const flyTo = (map, geoJson) => {
    if (!map.current || !geoJson) return;
    const coordinates = geoJson.geometry.coordinates;

    const bounds = new mapboxgl.LngLatBounds(coordinates[0], coordinates[0]);
    for (const coord of coordinates) {
        bounds.extend(coord);
    }

    map.current.fitBounds(bounds, {
        padding: 200,
    });
};

export const metersToMileString = (meters) => {
    const miles = meters * 0.000621371;
    return miles.toFixed(1) + " mi";
};

export const secondsToTimeString = (seconds) => {
    let hours = seconds / 3600;
    let minutes = seconds / 60;

    if (hours < 1) {
        return Math.ceil(minutes) + " min";
    }

    minutes = hours - Math.floor(hours);
    minutes *= 60;
    hours = Math.floor(hours);

    return hours + " hr, " + Math.ceil(minutes) + " min";
};
