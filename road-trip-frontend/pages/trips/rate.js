import { useAtom, useAtomValue, useSetAtom } from "jotai";
import React, { useState, useEffect } from "react";
import { Button, DarkOutlineButton } from "../../components/general/Buttons";
import { clearTripAtom, stopsAtom, tripAtom } from "../../utils/atoms";
import Alert from "../../components/auth/Alert";
import { useRouter } from "next/router";
import ClientOnly from "../../components/general/ClientOnly";
import { v4 as uuid } from "uuid";
import RateStopItem from "../../components/map/tripStops/RateStopItem";
import ReactStars from "react-stars";
import { colors } from "../../utils/colors";
import axios from "axios";

const Rate = () => {
    const trip = useAtomValue(tripAtom);
    const clearTrip = useSetAtom(clearTripAtom);
    const [stops, setStops] = useAtom(stopsAtom);
    const [rating, setRating] = useState(trip.rating);

    const router = useRouter();
    const [alert, setAlert] = useState();

    const handleSubmit = (event) => {
        event.preventDefault();
        setAlert();
        router.back();
    };

    return (
        <div className="w-full h-full flex flex-row sm:ml-16 ml-8 pt-12">
            <form className="h-3/4 w-1/2 text-gray-50" onSubmit={handleSubmit}>
                <h1 className="text-2xl font-semibold mb-2">
                    Rate your Stops.
                </h1>

                <Alert
                    message={alert}
                    className="text-left text-red-400 mt-4 -mb-5 w-full"
                />

                {/* <ClientOnly>
                    <ReactStars
                        count={5}
                        size={24}
                        color2={colors.green600}
                        half
                        value={rating}
                        edit={true}
                        onChange={(newVal) => {
                            axios.post(`/api/trip/${trip.id}/rate`, {
                                rating: newVal,
                            });
                            setRating(rating);
                        }}
                    />
                </ClientOnly> */}

                <ClientOnly>
                    <h4 className="mt-8 font-semibold text-xl mb-2">Stops:</h4>
                    <div className="flex flex-col gap-2 h-fit">
                        {stops ? (
                            stops.map((stop, index) => (
                                <RateStopItem
                                    stop={stop}
                                    order={index}
                                    key={uuid()}
                                />
                            ))
                        ) : (
                            <></>
                        )}
                    </div>
                </ClientOnly>

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
