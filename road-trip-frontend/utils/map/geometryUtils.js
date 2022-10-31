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
