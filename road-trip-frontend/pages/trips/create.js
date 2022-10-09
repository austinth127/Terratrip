import { useAtom, useAtomValue } from "jotai";
import React, { useState, useEffect } from "react";
import {
    Button,
    darkOutlineBtnStyle,
    DarkOutlineButton,
    stdBtnStyle,
} from "../../components/general/Buttons";
import TextInput, { DarkTextInput } from "../../components/general/TextInput";
import Geocoder from "../../components/map/Geocoder";
import {
    advLevelAtom,
    endAtom,
    endDateAtom,
    locAtom,
    startAtom,
    startDateAtom,
} from "../../utils/atoms";
import Alert from "../../components/auth/Alert";
import { useRouter } from "next/router";

const levels = ["Relaxed", "Moderate", "High", "Extreme", "All"];

const Create = () => {
    const [start, setStart] = useAtom(startAtom);
    const [end, setEnd] = useAtom(endAtom);
    const [startDate, setStartDate] = useAtom(startDateAtom);
    const [endDate, setEndDate] = useAtom(endDateAtom);
    const [activeLevels, setActiveLevels] = useAtom(advLevelAtom);
    const router = useRouter();

    const [alert, setAlert] = useState();

    console.log(start, end);

    const handleActivityLevel = (level) => {
        if (activeLevels.includes(level)) {
            if (level == "All") {
                activeLevels = [];
            } else {
                activeLevels.splice(activeLevels.indexOf(level), 1);
            }
        } else {
            if (level == "All") {
                activeLevels = levels;
            } else {
                activeLevels.push(level);
            }
        }

        setActiveLevels([...activeLevels]);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        setAlert();
        if (!(start && end)) {
            setAlert("Start and end locations should be set below.");
            return;
        }
        router.push("/trips/map");
    };

    return (
        <div className="w-full h-full flex flex-row sm:ml-16 pt-12">
            <form className="h-3/4 w-1/2 text-gray-50" onSubmit={handleSubmit}>
                <h1 className="text-2xl font-semibold mb-2">Plan your Trip.</h1>
                <h4 className="text-base font-light text-gray-100">
                    We just need a bit of info from you to tailor your
                    experience.
                </h4>

                <Alert
                    message={alert}
                    className="text-left text-red-400 mt-4 -mb-5 w-full"
                />

                {/* Start and End boxes */}
                <div className="mt-12 flex sm:flex-row sm:gap-20 flex-col">
                    <h2 className="text-xl font-semibold w-64 ">
                        <div className="mb-2 ml-1">Start</div>
                        <Geocoder
                            callback={setStart}
                            InputComponent={DarkTextInput}
                            initialValue={start?.place_name ?? ""}
                            dark={true}
                        />
                        <p className="mt-2 ml-1 w-full text-xs text-gray-500">
                            {start?.place_name ?? ""}
                        </p>
                    </h2>
                    <h2 className="text-xl font-semibold w-64">
                        <div className="mb-2 ml-1">End</div>
                        <Geocoder
                            callback={setEnd}
                            InputComponent={DarkTextInput}
                            initialValue={end?.place_name ?? ""}
                            dark={true}
                        />
                        <p className="mt-2 ml-1 w-full text-xs text-gray-500">
                            {end?.place_name ?? ""}
                        </p>
                    </h2>
                </div>

                {/* Adventure Level Section */}
                <div className="mt-12 text-lg">
                    Select an adventure level.
                    <p className="text-xs text-gray-400 mt-1">
                        Help us tailor your trip by selecting how strenuous of
                        outdoor activities that you want to be recommended.
                    </p>
                    <div className="flex flex-row gap-4 mt-4">
                        {levels.map((level) => (
                            <Button
                                onClick={() => handleActivityLevel(level)}
                                key={level}
                                className={
                                    activeLevels.includes(level)
                                        ? stdBtnStyle
                                        : darkOutlineBtnStyle
                                }
                            >
                                {level}
                            </Button>
                        ))}
                    </div>
                    {/* Start and End boxes */}
                    <div className="mt-12 flex sm:flex-row sm:gap-20 flex-col">
                        <h2 className="text-lg font-semibold w-64 ">
                            <div className="mb-2 ml-1">Start Date</div>
                            <DarkTextInput
                                type="date"
                                onChange={(event) =>
                                    setStartDate(event.target.value)
                                }
                            />
                        </h2>
                        <h2 className="text-lg font-semibold w-64">
                            <div className="mb-2 ml-1">End Date</div>
                            <DarkTextInput
                                type="date"
                                onChange={(event) =>
                                    setStartDate(event.target.value)
                                }
                            />
                        </h2>
                    </div>
                    <div className="mt-16 -ml-1">
                        <DarkOutlineButton type="submit">
                            Let's Go &nbsp;&nbsp;&rarr;
                        </DarkOutlineButton>
                    </div>
                </div>
            </form>
        </div>
    );
};

Create.usesReducedLayout = true;

export default Create;
