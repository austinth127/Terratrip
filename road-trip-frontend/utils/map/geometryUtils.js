import axios from "axios";
import mapboxgl from "mapbox-gl";

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
export const getRouteWithStops = async (...stops) => {
    var bruh = `?steps=true&geometries=geojson&access_token=${mapboxgl.accessToken}`;
    var bruh0 = `https://api.mapbox.com/directions/v5/mapbox/driving/`;
    var count = 0;
    for(let i = 0; i < stops.length; i++){
        bruh0+=stops[i];
    }
    bruh0+=bruh;
    console.log(bruh0);
    const res = await axios.get(
        `https://api.mapbox.com/directions/v5/mapbox/driving/-116.212495,42.629059;-100.869516,36.990465;-93.184457,44.075105;-104.574812,46.012432?steps=true&geometries=geojson&access_token=pk.eyJ1IjoiYXVzdGludGgxMjciLCJhIjoiY2w4c3prOWo1MDJrMjNwazFhMmxpMXViaSJ9.BynXNuEZFLbDBLZtSbaFVg`
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
