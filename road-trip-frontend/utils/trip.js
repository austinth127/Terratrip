export const tripToTripRequest = (trip) => {
    return {
        name: trip.name,
        start: {
            place_name: trip.start.place_name,
            lng: trip.start.center[0],
            lat: trip.start.center[1],
        },
        end: {
            place_name: trip.end.place_name,
            lng: trip.end.center[0],
            lat: trip.end.center[1],
        },
        startDate: trip.startDate,
        endDate: trip.endDate,
        advLevel: trip.advLevel != "" ? trip.advLevel.toUpperCase() : "EXTREME",
        distance: trip.route.distance,
        duration: trip.route.duration,
        stops: trip.stops
            ? trip.stops.map((stop) => ({
                  ...stop,
                  lng: stop.center[0],
                  lat: stop.center[1],
              }))
            : null,
    };
};
