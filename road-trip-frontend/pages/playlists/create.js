import axios from "axios";
import React, { useEffect, useState } from "react";
import LoadingSpinner from "../../components/general/LoadingSpinner";
import PlaylistTripItem from "../../components/playlist/PlaylistTripItem";
import { useAtom } from "jotai";
import { tripAtom } from "../../utils/atoms";

const Create = () => {
    const [selected, setSelected] = useState([]);
    const [genreOptions, setGenreOptions] = useState();
    const [limit, setLimit] = useState(16);
    const [colors, setColors] = useState();
    const [trips, setTrips] = useState();
    const [activeTrip, setActiveTrip] = useAtom(tripAtom);

    useEffect(() => {
        const getData = async () => {
            const res = await axios.get("/api/playlist/genres");
            setGenreOptions(res.data);

            colors = [];
            for (let i = 0; i < res.data.length; i++) {
                colors.push(randomColor(200, 25));
            }
            setColors(colors);

            const tripRes = await axios.get("/api/trip");
            setTrips(tripRes.data);
        };
        getData();
    }, []);

    if (!genreOptions || !colors || !trips)
        return <LoadingSpinner></LoadingSpinner>;

    return (
        <div className="lg:ml-16 py-16 mx-8 lg:mr-4">
            <h1 className="text-green-600 text-2xl font-semibold">
                Create a Playlist
            </h1>
            <h2 className="text-gray-100 text-lg font-light mt-8 mb-4">
                Select Genres{" "}
                <span className="text-gray-300 text-base">(up to 3)</span>
            </h2>
            <div className="grid grid-cols-4 gap-6 gap-y-6 lg:w-1/2">
                {genreOptions.slice(0, limit).map((choice, ndx) => (
                    <button
                        key={choice}
                        className={`rounded-lg uppercase text-xs font-semibold px-6 py-3 border-gray-800 
                                    ${
                                        selected.find((el) => el == choice)
                                            ? ` ring-white ring`
                                            : ``
                                    }`}
                        style={{ backgroundColor: colors[ndx] }}
                        onClick={() => {
                            if (selected.find((el) => el == choice)) {
                                selected.splice(selected.indexOf(choice), 1);
                                setSelected([...selected]);
                            } else {
                                if (selected.length < 3) {
                                    setSelected([
                                        ...new Set([...selected, choice]),
                                    ]);
                                }
                            }
                        }}
                    >
                        {choice}
                    </button>
                ))}
            </div>
            {/* Handle: Trip has playlist, trip does not, no trips, etc. */}
            <h2 className="text-gray-100 text-lg font-light mt-8 mb-4">
                Select one of your Trips
            </h2>
            <div className="mt-8 h-96 overflow-y-scroll w-fit p-4 dark-scrollbar">
                {trips.map((trip) => (
                    <PlaylistTripItem
                        trip={trip}
                        key={trip.id}
                        isSelected={
                            activeTrip != null && trip.id == activeTrip.id
                        }
                        callback={(trip) => {
                            setActiveTrip(trip);
                        }}
                    ></PlaylistTripItem>
                ))}
            </div>
        </div>
    );
};

function randomColor(maxBright, minBright) {
    function randomChannel(maxBright, minBright) {
        var r = maxBright - minBright;
        var n = 0 | (Math.random() * r + minBright);
        var s = n.toString(16);
        return s.length == 1 ? "0" + s : s;
    }
    return (
        "#" +
        randomChannel(maxBright, minBright) +
        randomChannel(maxBright, minBright) +
        randomChannel(maxBright, minBright)
    );
}

Create.usesReducedLayout = true;

export default Create;
