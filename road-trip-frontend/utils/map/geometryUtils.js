import axios from "axios";
import mapboxgl from "mapbox-gl";
import { Location } from "../typedefs";

export const getPoints = (...coords) => {
    const points = coords.map((coord) => ({
        type: "Feature",
        properties: {},
        geometry: {
            type: "Point",
            coordinates: coord,
        },
    }));

    return {
        type: "FeatureCollection",
        features: points,
    };
};

export const getRoute = async (start, end) => {
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
 * @param {Location} start
 * @param {Location} end
 * @param {Location[]} stops
 * @returns
 */
export const getRouteWithStops = async (start, end, stops) => {
    const params = `?steps=true&geometries=geojson&access_token=${mapboxgl.accessToken}`;
    const baseURL = `https://api.mapbox.com/directions/v5/mapbox/driving/`;
    stops.unshift(start);
    stops.push(end);
    let stopString = stops
        .map((stop) => `${stop.center[0]},${stop.center[1]}`)
        .join(";");

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
