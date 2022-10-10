import { atom } from "jotai";
import { atomWithStorage } from "jotai/utils";

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
export const startDateAtom = atom();
export const endDateAtom = atom();

export const advLevelAtom = atom([]);

export const startAtom = atom();
export const endAtom = atom();

export const routeAtom = atom();

// Map
export const showSaveModalAtom = atom(false);