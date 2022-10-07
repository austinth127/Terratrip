import { useAtom } from "jotai";
import React, { useState } from "react";
import { Button, DarkOutlineButton } from "../../components/general/Buttons";
import TextInput, { DarkTextInput } from "../../components/general/TextInput";
import Geocoder from "../../components/map/Geocoder";
import { endAtom, startAtom } from "../../utils/atoms";

const levels = ["Relaxed", "Moderate", "High", "Extreme"];

const Create = () => {
    const [start, setStart] = useAtom(startAtom);
    const [end, setEnd] = useAtom(endAtom);
    const [activeLevels, setActiveLevels] = useState([]);

    return (
        <div className="w-full h-full flex flex-row sm:ml-16 pt-12">
            <form className="h-3/4 w-1/2 text-gray-50">
                <h1 className="text-2xl font-semibold mb-2">Plan your Trip.</h1>
                <h4 className="text-base font-light text-gray-100">
                    We just need a bit of info from you to tailor your
                    experience.
                </h4>

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
                            <>
                                {/* This is not properly effectful right now */}
                                {activeLevels.includes(level) ? (
                                    <Button
                                        onClick={() => {
                                            activeLevels.splice(
                                                activeLevels.indexOf(level),
                                                1
                                            );
                                            setActiveLevels(activeLevels);
                                        }}
                                        key={level}
                                    >
                                        {level}
                                    </Button>
                                ) : (
                                    <DarkOutlineButton
                                        onClick={() => {
                                            activeLevels.push(level);
                                            setActiveLevels(activeLevels);
                                        }}
                                        key={level}
                                    >
                                        {level}
                                    </DarkOutlineButton>
                                )}
                            </>
                        ))}
                    </div>
                </div>
            </form>
        </div>
    );
};

Create.usesReducedLayout = true;

export default Create;
