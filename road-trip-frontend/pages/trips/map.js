import { useAtom } from "jotai";
import React, { useEffect } from "react";
import ClientOnly from "../../components/general/ClientOnly";
import Map from "../../components/map/Map";
import StopDisplay from "../../components/map/StopDisplay";
import StopSelector from "../../components/map/StopSelector";
import SaveModal from "../../components/trip/SaveModal";
import { showSaveModalAtom } from "../../utils/atoms";

const TripMapper = () => {
    const [showModal, setShowModal] = useAtom(showSaveModalAtom);

    useEffect(() => {
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
