import { atom } from "jotai";
import { atomWithStorage } from "jotai/utils";

export const locAtom = atom(
    (get) => ({ start: get(startAtom), end: get(endAtom) }),
    (get, set, newLoc) => {
        set(startAtom, newLoc.start);
        set(endAtom, newLoc.end);
    }
);
export const startAtom = atom();
export const endAtom = atom();
