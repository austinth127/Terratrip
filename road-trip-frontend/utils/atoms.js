import { atom } from "jotai";
import { atomWithStorage } from "jotai/utils";
import { atomWithQuery } from "jotai/query";
import axios from "axios";

export const locAtom = atom(
    (get) => ({ start: get(startAtom), end: get(endAtom) }),
    (get, set, newLoc) => {
        set(startAtom, newLoc.start);
        set(endAtom, newLoc.end);
    }
);

export const tripDateAtom = atom(
    (get) => ({ start: get(startDateAtom), end: get(endDateAtom) }),
    (get, set, newTrip) => {
        set(startDateAtom, newTrip.start);
        set(endDateAtom, newTrip.end);
    }
);

// Create Trip
export const startDateAtom = atomWithStorage("startDate", null);
export const endDateAtom = atomWithStorage("endDate", null);

export const advLevelAtom = atomWithStorage("advLevel", "");

export const startAtom = atomWithStorage("startLoc", null);
export const endAtom = atomWithStorage("endLoc", null);

export const routeAtom = atomWithStorage("route", null);
export const tripNameAtom = atomWithStorage("tripName", null);

// Map
export const showSaveModalAtom = atom(false);
export const editModeAtom = atomWithStorage("editMode", false);
export const tripIdAtom = atomWithStorage("tripId", null);

// Notifications

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
