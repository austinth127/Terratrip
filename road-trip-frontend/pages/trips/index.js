import { useAtom, useAtomValue, useSetAtom } from "jotai";
import React, { useState, useEffect } from "react";
import {
    Button,
    darkOutlineBtnStyle,
    DarkOutlineButton,
    stdBtnStyle,
} from "../../components/general/Buttons";
import { DarkTextInput } from "../../components/general/TextInput";
import Geocoder from "../../components/map/Geocoder";
import AdvChange from "../../components/trip/AdvChange";
import {
    advLevelAtom,
    clearTripAtom,
    editModeAtom,
    endAtom,
    endDateAtom,
    routeAtom,
    startAtom,
    startDateAtom,
    tripAtom,
    tripIdAtom,
    tripNameAtom,
    showAdvChangeAtom,
} from "../../utils/atoms";
import Alert from "../../components/auth/Alert";
import { useRouter } from "next/router";
import {
    levelOptions,
    levelOptions as levels,
} from "../../utils/stops/filters";
import { getRoute } from "../../utils/map/geometryUtils";
import ClientOnly from "../../components/general/ClientOnly";
import axios from "axios";
import { tripToTripRequest } from "../../utils/trip";
import RadioSelector from "../../components/general/RadioSelector";
import { LoadingSpinnerSmall } from "../../components/general/LoadingSpinner";

const Create = () => {
    const [show, setShow] = useAtom(showAdvChangeAtom);
    const [start, setStart] = useAtom(startAtom);
    const [end, setEnd] = useAtom(endAtom);
    const [startDate, setStartDate] = useAtom(startDateAtom);
    const [endDate, setEndDate] = useAtom(endDateAtom);
    const [activeLevel, setActiveLevel] = useAtom(advLevelAtom);
    const [route, setRoute] = useAtom(routeAtom);
    const [tripName, setTripName] = useAtom(tripNameAtom);
    const setTripId = useSetAtom(tripIdAtom);
    const trip = useAtomValue(tripAtom);
    const clearTrip = useSetAtom(clearTripAtom);
    const [loading, setLoading] = useState();

    const editMode = useAtomValue(editModeAtom);

    const router = useRouter();
    const [alert, setAlert] = useState();

    const handleSubmit = (event) => {
        event.preventDefault();
        setAlert();
        if (!(start && end)) {
            setAlert("Start and end locations should be set.");
            return;
        }
        if (!startDate || !endDate) {
            setAlert("Start and End Date cannot be empty.");
            return;
        }
        if (startDate > endDate) {
            setAlert("Start date cannot be after end date.");
            return;
        }
        if (!tripName) {
            setAlert("Trip name cannot be empty.");
            return;
        }

        let newAdvLevel = -1;
        let exit = false;

        for (var i = 0; i < levelOptions.length; i++) {
            if (
                activeLevel.toLocaleLowerCase() ==
                levelOptions[i].toLocaleLowerCase()
            ) {
                newAdvLevel = i;
            }
        }

        if (trip.stops) {
            trip.stops.forEach((s) => {
                console.log(s);
                let stopAdvLevel = -1;
                for (var i = 0; i < levelOptions.length; i++) {
                    if (
                        s.adventureLevel?.toLocaleLowerCase() ==
                        levelOptions[i].toLocaleLowerCase()
                    ) {
                        stopAdvLevel = i;
                    }
                }

                if (stopAdvLevel > newAdvLevel) {
                    exit = true;
                    setShow(true);
                }
            });
        }

        /** @todo Make sure trip is possible for given route duration */
        getRoute(start, end).then(
            (success) => {
                setRoute(success[0]);
                trip.route = success[0];

                let drivingDays = Math.ceil(trip.route.duration / 86400.0);
                let requestedTime =
                    new Date(endDate).getTime() - new Date(startDate).getTime();
                let requestedDays = requestedTime / (1000 * 3600 * 24) + 1;

                if (drivingDays > requestedDays) {
                    setAlert("Trip not completable within time frame.");
                } else if (exit) {
                    return;
                } else if (editMode) {
                    setLoading(true);
                    axios
                        .patch(`/api/trip/${trip.id}`, tripToTripRequest(trip))
                        .then(
                            (success) => {
                                clearTrip();
                                setLoading(false);
                                router.push("/trips/list/user");
                            },
                            (fail) => {
                                setLoading(false);
                                setAlert("Trip failed to update");
                            }
                        );
                } else {
                    if (!loading) {
                        setLoading(true);
                        axios.post("/api/trip", tripToTripRequest(trip)).then(
                            (success) => {
                                setLoading(false);
                                setTripId(success.data);
                                setRoute(null);
                                router.push("/trips/map");
                            },
                            (fail) => {
                                setLoading(false);
                                setAlert("Trip failed to save");
                            }
                        );
                    }
                }
            },
            (error) => {
                setLoading(false);
                setAlert(
                    "Cannot find a driving route between these locations."
                );
            }
        );
    };

    return (
        <div>
            <div
                className={`${show ? `pointer-events-none` : ``} w-full h-full`}
            >
                <div className="w-full h-full flex flex-row sm:ml-16 ml-8 pt-12">
                    <form
                        className="h-3/4 w-3/4 text-gray-50"
                        onSubmit={handleSubmit}
                    >
                        <ClientOnly>
                            <h1 className="text-2xl font-semibold mb-2">
                                {editMode
                                    ? "Edit your Trip"
                                    : "Plan your Trip."}
                            </h1>
                            {editMode ? (
                                <></>
                            ) : (
                                <h4 className="text-base font-light text-gray-100">
                                    We just need a bit of info from you to
                                    tailor your experience.
                                </h4>
                            )}
                        </ClientOnly>

                        {/* Start and End boxes */}
                        <ClientOnly>
                            <h2 className="mt-8 text-lg font-semibold min-w-[16rem] max-w-[37rem]">
                                <div className="mb-2 text-green-600">Name</div>
                                <DarkTextInput
                                    value={tripName ?? ""}
                                    onChange={(e) =>
                                        setTripName(e.target.value)
                                    }
                                    placeholder="Name your Trip!"
                                />
                            </h2>
                            <div className="mt-12 flex sm:flex-row sm:gap-20 flex-col">
                                <h2 className="text-xl font-semibold w-64 ">
                                    <div className="mb-2 ">Start</div>
                                    <Geocoder
                                        callback={setStart}
                                        InputComponent={DarkTextInput}
                                        initialValue={start?.place_name ?? ""}
                                        dark={true}
                                    />
                                    <p className="mt-2 w-full text-xs text-gray-500">
                                        {start?.place_name ?? ""}
                                    </p>
                                </h2>
                                <h2 className="text-xl font-semibold w-64">
                                    <div className="mb-2 ">End</div>
                                    <Geocoder
                                        callback={setEnd}
                                        InputComponent={DarkTextInput}
                                        initialValue={end?.place_name ?? ""}
                                        dark={true}
                                    />
                                    <p className="mt-2 w-full text-xs text-gray-500">
                                        {end?.place_name ?? ""}
                                    </p>
                                </h2>
                            </div>
                        </ClientOnly>

                        {/* Adventure Level Section */}
                        <div className="mt-12 text-lg">
                            Select an adventure level.
                            <p className="text-xs text-gray-400 mt-1">
                                Help us tailor your trip by selecting how
                                strenuous of outdoor activities that you want to
                                be recommended.
                            </p>
                            <RadioSelector
                                active={activeLevel}
                                setActive={setActiveLevel}
                                options={levels}
                                className={"max-w-[37rem]"}
                            />
                            {/* Start and End boxes */}
                            <div className="mt-12 flex sm:flex-row sm:gap-20 flex-col">
                                <h2 className="text-lg font-semibold w-64 ">
                                    <div className="mb-2 ">Start Date</div>
                                    <DarkTextInput
                                        type="date"
                                        onChange={(event) =>
                                            setStartDate(event.target.value)
                                        }
                                        defaultValue={startDate}
                                    />
                                </h2>
                                <h2 className="text-lg font-semibold w-64">
                                    <div className="mb-2  mt-4 sm:mt-0">
                                        End Date
                                    </div>
                                    <DarkTextInput
                                        type="date"
                                        onChange={(event) =>
                                            setEndDate(event.target.value)
                                        }
                                        defaultValue={endDate}
                                    />
                                </h2>
                            </div>
                            <ClientOnly>
                                <Alert
                                    message={alert}
                                    className="text-left text-red-400 mt-4 -mb-5 w-full"
                                />
                            </ClientOnly>
                            <ClientOnly>
                                <div className="mt-16 mb-20">
                                    {editMode ? (
                                        <div className="flex flex-row gap-4">
                                            <DarkOutlineButton type="submit">
                                                Save
                                            </DarkOutlineButton>
                                            <DarkOutlineButton
                                                onClick={() => {
                                                    clearTrip();
                                                    router.back();
                                                }}
                                            >
                                                Cancel
                                            </DarkOutlineButton>
                                            {loading && (
                                                <LoadingSpinnerSmall
                                                    className={"mt-2"}
                                                    text="Saving..."
                                                ></LoadingSpinnerSmall>
                                            )}
                                        </div>
                                    ) : (
                                        <div className="flex flex-row gap-4">
                                            <DarkOutlineButton type="submit">
                                                Let's Go &nbsp;&nbsp;&rarr;
                                            </DarkOutlineButton>
                                            {loading && (
                                                <LoadingSpinnerSmall
                                                    className={"mt-2"}
                                                    text="Saving..."
                                                ></LoadingSpinnerSmall>
                                            )}
                                        </div>
                                    )}
                                </div>
                            </ClientOnly>
                        </div>
                    </form>
                </div>
                <div
                    className={`w-full h-full absolute top-0 left-0 z-30 duration-200 ease-in-out ${
                        show
                            ? `bg-slate-900 bg-opacity-70`
                            : `bg-opacity-0 hidden`
                    }`}
                ></div>
            </div>

            <AdvChange />
        </div>
    );
};

Create.usesReducedLayout = true;

export default Create;
