import { atom } from "jotai";
import { atomWithStorage } from "jotai/utils";

export const tripAtom = atom(
    (get) => {
        return {
            name: get(tripNameAtom),
            start: get(startAtom),
            end: get(endAtom),
            startDate: get(startDateAtom),
            endDate: get(endDateAtom),
            advLevel: get(advLevelAtom),
            route: get(routeAtom),
            stops: get(stopsAtom),
            id: get(tripIdAtom),
            playlistId: get(playlistIdAtom),
        };
    },
    (get, set, newTrip) => {
        set(advLevelAtom, newTrip.advLevel);
        set(startAtom, newTrip.start);
        set(endAtom, newTrip.end);
        set(startDateAtom, newTrip.startDate);
        set(endDateAtom, newTrip.endDate);
        set(tripNameAtom, newTrip.name);
        set(routeAtom, newTrip.route);
        set(stopsAtom, newTrip.stops);
        set(tripIdAtom, newTrip.id);
        set(playlistIdAtom, newTrip.playlistId);
    }
);

export const clearTripAtom = atom(null, (get, set) => {
    set(advLevelAtom, "");
    set(startAtom, null);
    set(endAtom, null);
    set(startDateAtom, null);
    set(endDateAtom, null);
    set(tripNameAtom, null);
    set(routeAtom, null);
    set(stopsAtom, null);
    set(tripIdAtom, null);
    set(filtersAtom, []);
    set(recStopAtom, null);
    set(popupStopAtom, null);
    set(routeGeoJsonAtom, null);
    set(playlistIdAtom, null);
});

// Create Trip
export const startDateAtom = atomWithStorage("startDate", null);
export const endDateAtom = atomWithStorage("endDate", null);

export const advLevelAtom = atomWithStorage("advLevel", "");

export const startAtom = atomWithStorage("startLoc", null);
export const endAtom = atomWithStorage("endLoc", null);

export const routeAtom = atomWithStorage("route", null);
export const routeGeoJsonAtom = atomWithStorage("routeGeoJson", null);
export const tripNameAtom = atomWithStorage("tripName", null);

export const playlistIdAtom = atomWithStorage("playlist", null);

// Map
export const showSaveModalAtom = atom(false);
export const popupStopAtom = atom(null);
export const tripIdAtom = atomWithStorage("tripId", null);
export const stopsAtom = atomWithStorage("stops", []);

export const allLocationsAtom = atom((get) => {
    if (!get(stopsAtom) || get(stopsAtom).length < 1) {
        return [get(startAtom), get(endAtom)];
    }

    return [get(startAtom), ...get(stopsAtom), get(endAtom)];
});

export const editModeAtom = atom((get) => get(tripIdAtom) != null);

export const recStopAtom = atom(null);

// Notifications
// This would only work if add other wrapper code to avoid hydration errors
// Get to backend
// const queryFn = async (path) => {
//     try {
//         const res = await axios.get(path);
//         return res.data.result ?? null;
//     } catch (e) {
//         return e.response ? e.response.data : null;
//     }
// };

// export const notificationAtom = atomWithQuery(() => ({
//     queryKey: [`GET::/notification`],
//     queryFn: () => queryFn("/notification"),
// }));

export const showNotifAtom = atom(false);

export const filtersAtom = atom([]);
export const rangeAtom = atom(50);
