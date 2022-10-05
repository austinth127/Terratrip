import React, { useEffect, useState, useRef } from "react";
import mapboxgl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import { getRouteMatcher } from "next/dist/shared/lib/router/utils/route-matcher";
import axios from "axios";

mapboxgl.accessToken = process.env.NEXT_PUBLIC_MAPBOX_ACCESS_TOKEN;

/**
 * Mapbox map
 *
 * @TODO add directions https://docs.mapbox.com/help/tutorials/getting-started-directions-api/
 *
 * @returns {JSX.Element} Mapboxgl map
 */
const Map = () => {
    /** @type {React.MutableRefObject<mapboxgl.Map>} */
    const mapContainer = useRef(null);
    /** @type {React.MutableRefObject<mapboxgl.Map>} */
    const map = useRef(null);
    const [lng, setLng] = useState(-97.141); // Longitude
    const [lat, setLat] = useState(31.55); // Lattitude
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
        const start = [lng,lat]
        /**
         * This function makes a request to the MapBox API to find the route between Waco and the parameter end
         * @param {*} end the location to route to.
         */
        async function getRoute(end) {
            const res = await axios.get(`https://api.mapbox.com/directions/v5/mapbox/driving/${lng},${lat};${end[0]},${end[1]}?steps=true&geometries=geojson&access_token=${mapboxgl.accessToken}`)
            
            const data = res.data.routes[0];
            const route = data.geometry.coordinates;
            const geojson = {
                type: 'Feature',
                properties: {},
                geometry: {
                type: 'LineString',
                coordinates: route
                }
            };
            //if route layer exists, update with new route, else create the layer and add the route.
            if (map.current.getSource('route')){
                map.current.getSource('route').setData(geojson);
            }
            else{
                map.current.addLayer({
                    id: 'route',
                    type: 'line',
                    source:{
                        type: 'geojson',
                        data: geojson
                    
                    },
                    layout:{
                        'line-join': 'round',
                        'line-cap': 'round'
                    },
                    paint: {
                        'line-color': '#3887be',
                        'line-width': 5,
                        'line-opacity': 0.75
                    }
                });
            }
        }
        
        map.current.on('load',()=>{
            getRoute(start)
            map.current.addLayer({
                id: 'point',
                type: 'circle',
                source: {
                    type: 'geojson',
                    data: {
                        type: 'FeatureCollection',
                        features: [
                            {
                                type: 'Feature',
                                properties: {},
                                geometry: {
                                    type: 'Point',
                                    coordinates: start
                                }
                            }
                        ]
                    }
                },
                paint: {
                    'circle-radius': 10,
                    'circle-color': '#3887be'
                }
            });

        });
        map.current.on('click', (event) => {
            const coords = Object.keys(event.lngLat).map((key) => event.lngLat[key]);
            const end = {
                 type: 'FeatureCollection',
                 features: [
                     {
                        type: 'Feature',
                        properties: {},
                        geometry: {
                            type: 'Point',
                            coordinates: coords
                        }
                     }
                 ]
            };
            if (map.current.getLayer('end')){
                map.current.getSource('end').setData(end);
            }else{
                map.current.addLayer({
                    id: 'end',
                    type: 'circle',
                    source: {
                        type: 'geojson',
                        data: {
                            type: 'FeatureCollection',
                            features: [
                                {
                                    type: 'Feature',
                                    properties: {},
                                    geometry: {
                                        type: 'Point',
                                        coordinates: coords
                                    }
                                }
        
                            ]

                        }

                    },
                    paint: {
                        'circle-radius': 10,
                        'circle-color': '#f30'
                    }
                });
            }
            getRoute(coords);


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




    // function getRouteMatcher(){
    //     const end = [-84.518399,39.134126]
    //     axios.get(`https://api.mapbox.com/directions/v5/mapbox/cycling/${lng},${lat};${end[0]},${end[1]}?steps=true&geometries=geojson&access_token=${mapboxgl.accessToken}`)
    //     .then(res=>{
    //         console.log(res.data);
    //     })
    // }
    // getRouteMatcher();


    return (
        <div className="relative">
            <div className="bg-slate-800 bg-opacity-80 py-1.5 px-3 font-mono z-[1] absolute top-12 left-0 m-3 rounded-lg">
                Longitude: {lng} | Latitude: {lat} | Zoom: {zoom}
            </div>
            <div ref={mapContainer} className="h-[100vh]"></div>
        </div>
    );
};

export default Map;
