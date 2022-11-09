import { useAtom, useAtomValue, useSetAtom } from "jotai";
import React, { useState, useEffect } from "react";
import {
    Button,
    darkOutlineBtnStyle,
    DarkOutlineButton,
    OutlineButton,
    stdBtnStyle,
} from "../../components/general/Buttons";
import { DarkTextInput } from "../../components/general/TextInput";
import Geocoder from "../../components/map/Geocoder";
import { clearTripAtom, stopsAtom, tripAtom } from "../../utils/atoms";
import Alert from "../../components/auth/Alert";
import { useRouter } from "next/router";
import { levelOptions as levels } from "../../utils/stops/filters";
import { getRoute } from "../../utils/map/geometryUtils";
import ClientOnly from "../../components/general/ClientOnly";
import axios from "axios";

const Rate = () => {
    const trip = useAtomValue(tripAtom);
    const clearTrip = useSetAtom(clearTripAtom);
    const [stops, setStops] = useAtom(stopsAtom);

    const router = useRouter();
    const [alert, setAlert] = useState();

    const handleSubmit = (event) => {
        event.preventDefault();
        setAlert();
    };

    return (
        <div className="w-full h-full flex flex-row sm:ml-16 ml-8 pt-12">
            <form className="h-3/4 w-1/2 text-gray-50" onSubmit={handleSubmit}>
                <h1 className="text-2xl font-semibold mb-2">Rate your Trip.</h1>

                <Alert
                    message={alert}
                    className="text-left text-red-400 mt-4 -mb-5 w-full"
                />

                {/* Start and End boxes */}
                <ClientOnly></ClientOnly>

                <div className="mt-16 mb-20 flex flex-row gap-4">
                    <Button type="submit">Save</Button>
                    <DarkOutlineButton
                        onClick={() => {
                            clearTrip();
                            router.back();
                        }}
                    >
                        Cancel
                    </DarkOutlineButton>
                </div>
            </form>
        </div>
    );
};

Rate.usesReducedLayout = true;

export default Rate;
