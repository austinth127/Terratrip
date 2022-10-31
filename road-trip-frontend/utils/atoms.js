import { atom } from "jotai";

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

export const advLevelAtom = atom("");

export const startAtom = atom();
export const endAtom = atom();

export const routeAtom = atom();
export const tripNameAtom = atom();

// Map
export const showSaveModalAtom = atom(false);
export const editModeAtom = atom(false);
export const tripIdAtom = atom(null);

// Notifications
/**@todo import from query atom */
export const notificationAtom = atom([
    {
        title: "Your trip '2022 Trip with The Boys' is coming up soon!",
        time: "10/30/2022 11:12",
        body: "Take a look at your planned stops! Feel free to print your itenerary off.",
        id: 1,
    },
    {
        title: "Your trip '2022 Trip with The Boys' is coming up soon!",
        time: "10/30/2022 11:12",
        body: "Take a look at your planned stops! Feel free to print your itenerary off.",
        id: 2,
    },
    {
        title: "Your trip '2022 Trip with The Boys' is coming up soon!",
        time: "10/30/2022 11:12",
        body: "Take a look at your planned stops! Feel free to print your itenerary off.",
        id: 3,
    },
    {
        title: "Your trip '2022 Trip with The Boys' is coming up soon!",
        time: "10/30/2022 11:12",
        body: "Take a look at your planned stops! Feel free to print your itenerary off.",
        id: 4,
    },
]);
export const showNotifAtom = atom(false);
