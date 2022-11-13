import { useAtom } from "jotai";
import React, { useState } from "react";
import ReactStars from "react-stars";
import { stopsAtom } from "../../../utils/atoms";
import { colors } from "../../../utils/colors";
import ClientOnly from "../../general/ClientOnly";

const RateStopItem = ({ stop, order }) => {
    const [stops, setStops] = useAtom(stopsAtom);
    const [rating, setRating] = useState(stop ? stop.rating ?? 0 : 0);

    const handleMoveUp = () => {
        const thisIndex = order - 1;
        stops[thisIndex] = stops.splice(thisIndex - 1, 1, stops[thisIndex])[0];
        setStops([...stops]);
    };

    const handleMoveDown = () => {
        const thisIndex = order - 1;
        stops[thisIndex] = stops.splice(thisIndex + 1, 1, stops[thisIndex])[0];
        setStops([...stops]);
    };
    const handleDelete = () => {
        const thisIndex = order - 1;
        stops.splice(thisIndex, 1);
        setStops([...stops]);
    };

    if (!stop) {
        return null;
    }

    return (
        <div
            className={`bg-slate-900 bg-opacity-80 rounded-lg flex flex-row justify-between items-center px-8 py-4 gap-4`}
        >
            <div className="flex flex-row gap-4 items-start ">
                <div
                    className={`bg-green-600 text-white rounded-full font-bold font-lg h-10 w-10 text-center shrink-0 flex flex-col items-center justify-center
                   
                `}
                >
                    {order}
                </div>
                <div className="text-slate-100 text-xs font-light">
                    <p className="text-green-600 font-semibold text-sm">
                        {stop.place_name}
                    </p>
                    {stop.categories ? (
                        <p className="text-slate-300 font-light">
                            Types:{" "}
                            {stop.categories
                                .map((cat) => {
                                    let words = cat.split(".");
                                    return words[words.length - 1];
                                })
                                .join(", ")}
                        </p>
                    ) : (
                        <></>
                    )}
                    <p className="">{stop.address ?? ""}</p>
                    <p className="text-slate-400 font-light">
                        {stop.phone_contact ?? ""}
                    </p>
                </div>
            </div>

            <div className="relative w-fit">
                <ClientOnly>
                    <ReactStars
                        count={5}
                        size={24}
                        color2={colors.green600}
                        half
                        value={rating}
                        edit={true}
                        onChange={(newVal) => {
                            // axios.post(`/api/location/${stop.id}`, {
                            //     rating: newVal,
                            // });
                            setRating(rating);
                        }}
                    />
                </ClientOnly>
            </div>
        </div>
    );
};

export default RateStopItem;
