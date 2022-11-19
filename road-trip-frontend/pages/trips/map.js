import { useAtom, useAtomValue } from "jotai";
import React, { useEffect } from "react";
import ClientOnly from "../../components/general/ClientOnly";
import Map from "../../components/map/Map";
import StopDisplay from "../../components/map/tripStops/TripStopDisplay";
import StopSelector from "../../components/map/stopFilters/StopSelector";
import SaveModal from "../../components/trip/SaveModal";
import { showSaveModalAtom, tripIdAtom } from "../../utils/atoms";
import Popup from "../../components/map/popup/Popup";
import { useRouter } from "next/router";

import Userfront from "@userfront/core";

const TripMapper = () => {
    const [showModal, setShowModal] = useAtom(showSaveModalAtom);
    const tripId = useAtomValue(tripIdAtom);
    const router = useRouter();

    useEffect(() => {
        if (!tripId) {
            if (!Userfront.user.userId) {
                router.push("/auth/signin");
                return;
            }
            router.push("/trips/list/user");
        }
        setShowModal(false);
    }, []);

    return (
        <ClientOnly>
            <div
                className={`${
                    showModal ? `pointer-events-none` : ``
                } w-full h-full`}
            >
                <Map></Map>
                <StopSelector />
                <StopDisplay />
                <Popup />
                <div
                    className={`w-full h-full absolute top-0 left-0 z-30 duration-200 ease-in-out ${
                        showModal
                            ? `bg-slate-900 bg-opacity-70`
                            : `bg-opacity-0 hidden`
                    }`}
                ></div>
            </div>

            <SaveModal />
        </ClientOnly>
    );
};

TripMapper.usesMapLayout = true;

export default TripMapper;
