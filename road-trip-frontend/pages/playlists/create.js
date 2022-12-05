import axios from "axios";
import React, { useEffect, useState } from "react";
import LoadingSpinner, {
    LoadingSpinnerSmall,
} from "../../components/general/LoadingSpinner";
import PlaylistTripItem from "../../components/playlist/PlaylistTripItem";
import { useAtom, useAtomValue, useSetAtom } from "jotai";
import { clearTripAtom, playlistIdAtom, tripAtom } from "../../utils/atoms";
import { Button, DarkOutlineButton } from "../../components/general/Buttons";
import Alert from "../../components/auth/Alert";
import { useRouter } from "next/router";
import Accordion from "../../components/accordion/Accordion";
import DualSlider from "../../components/general/CompundSlider";
import SpotifyEmbed from "../../components/playlist/SpotifyEmbed";
import { delay } from "../../utils/delay";

const initSliders = [
    { name: "Energy", bounds: [0.0, 1.0], minMax: [0.0, 1.0], step: 0.1 },
    { name: "Danceability", bounds: [0.0, 1.0], minMax: [0.0, 1.0], step: 0.1 },
    {
        name: "Instrumentalness",
        bounds: [0.0, 1.0],
        minMax: [0.0, 1.0],
        step: 0.1,
    },
    { name: "Acousticness", bounds: [0.0, 1.0], minMax: [0.0, 1.0], step: 0.1 },
    { name: "Popularity", bounds: [0.0, 1.0], minMax: [0.0, 1.0], step: 0.1 },
    { name: "Happiness", bounds: [0.0, 1.0], minMax: [0.0, 1.0], step: 0.1 },
    { name: "Tempo", bounds: [0, 300], minMax: [0, 300], step: 10 },
    {
        name: "Desired length (min)",
        bounds: [0, 300],
        minMax: [0, 300],
        step: 10,
    },
];
const Create = () => {
    const [selected, setSelected] = useState([]);
    const [genreOptions, setGenreOptions] = useState();
    const [limit, setLimit] = useState(16);
    const [colors, setColors] = useState();
    const [trips, setTrips] = useState();
    const [activeTrip, setActiveTrip] = useAtom(tripAtom);
    const [alert, setAlert] = useState();
    const [showLoading, setShowLoading] = useState(false);
    const [user, setUser] = useState();
    const [sliders, setSliders] = useState(initSliders);
    const clearTrip = useSetAtom(clearTripAtom);
    const [playlist, setPlaylist] = useState();
    const router = useRouter();

    const handleSubmit = () => {
        setAlert();
        if (!activeTrip.id) {
            setAlert("You must chose a trip to attatch the playlist to!");
            return;
        }
        if (!selected || selected.length < 1) {
            setAlert("Please choose at least 1 genre for your playlist!");
            return;
        }
        setShowLoading(true);

        const req = {
            tripId: activeTrip.id,
            genres: selected.length > 0 ? selected : null,
            energy: sliders[0].minMax,
            danceability: sliders[1].minMax,
            instrumentalness: sliders[2].minMax,
            acousticness: sliders[3].minMax,
            popularity: sliders[4].minMax,
            happiness: sliders[5].minMax,
            tempo: sliders[6].minMax,
            target_duration: Math.floor(
                Math.random() * (sliders[7].minMax[1] - sliders[7].minMax[0]) +
                    sliders[7].minMax[0]
            ),
        };
        console.log(req);
        axios.post("/api/playlist/generate", req).then(
            (res) => {
                console.log(res);
                delay(1000).then(() => {
                    setShowLoading(false);
                    setPlaylist(res.data);
                    setActiveTrip({ ...activeTrip, playlistId: res.data.id });
                    axios.get("/api/trip").then((res) => {
                        setTrips(res.data);
                    });
                });
            },
            (error) => {
                setShowLoading(false);
                setAlert("Failed to generate playlist.");
            }
        );
    };

    useEffect(() => {
        const getData = async () => {
            const res = await axios.get("/api/playlist/genres");
            setGenreOptions(res.data);

            colors = [];
            for (let i = 0; i < res.data.length; i++) {
                colors.push(randomColor(190, 10));
            }
            setColors(colors);

            const tripRes = await axios.get("/api/trip");
            setTrips(tripRes.data);

            let user = await axios.get("/api/user");
            setUser(user.data);
        };
        getData();
    }, []);

    if (!genreOptions || !colors || !user)
        return <LoadingSpinner></LoadingSpinner>;

    return (
        <div className="lg:ml-16 py-16 mx-8 lg:mr-4 lg:w-3/4">
            <h1 className="text-green-600 text-2xl font-semibold">
                Create a Playlist
            </h1>
            <h2 className="text-gray-100 text-lg font-light mt-8 mb-4">
                Select Genres{" "}
                <span className="text-gray-300 text-base">(up to 3)</span>
            </h2>
            <div className="grid grid-cols-4 gap-6 gap-y-6 px-2">
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
            <div className="flex flex-row justify-end mt-2">
                <button
                    onClick={() => setLimit(limit + 16)}
                    className={`hover:underline hover:text-gray-400 active:text-gray-300 text-gray-500 active:underline ${
                        limit >= genreOptions.length && `hidden`
                    }`}
                >
                    Show more
                </button>
            </div>
            {/* Handle: Trip has playlist, trip does not, no trips, etc. */}
            {trips && (
                <div>
                    <h2 className="text-gray-100 text-lg font-light mt-12 mb-4">
                        Select one of your Trips
                    </h2>
                    <div className="h-96 overflow-y-scroll p-4 dark-scrollbar">
                        {trips.map((trip) => (
                            <PlaylistTripItem
                                trip={trip}
                                key={trip.id}
                                isSelected={
                                    activeTrip != null &&
                                    trip.id == activeTrip.id
                                }
                                callback={(trip) => {
                                    setActiveTrip(trip);
                                }}
                            ></PlaylistTripItem>
                        ))}
                    </div>
                </div>
            )}

            <div className="my-16 w-1/2">
                <Accordion
                    defaultToExpanded={false}
                    header={
                        <h3 className="text-gray-300 text-lg">
                            Advanced Settings
                        </h3>
                    }
                    darkBorder={true}
                >
                    <div className="w-full py-4">
                        {sliders.map((slider, index) => (
                            <div
                                key={slider.name + "slider"}
                                className="flex flex-row gap-12 items-center"
                            >
                                <span className="w-32 font-light text-gray-400">
                                    {slider.name}
                                </span>
                                <DualSlider
                                    min={slider.bounds[0]}
                                    max={slider.bounds[1]}
                                    onChange={(val) => {
                                        let newSlider = slider;
                                        newSlider.minMax = val;
                                        sliders[index] = newSlider;
                                        setSliders(sliders);
                                        console.log(sliders);
                                    }}
                                    step={slider.step}
                                    minVal={slider.minMax[0]}
                                    maxVal={slider.minMax[1]}
                                    color={colors[index]}
                                ></DualSlider>
                            </div>
                        ))}
                    </div>
                </Accordion>
            </div>

            <div className="mt-12">
                <h2 className="text-gray-100 text-lg font-light mb-4">
                    Review
                </h2>
                <Alert message={alert} className="text-red-500 text-left" />
                <div className="flex flex-row justify-start gap-12 w-full mb-8">
                    <div
                        className={`text-green-600 text-lg w-2/3 ${
                            !activeTrip.id ? `hidden` : ``
                        }`}
                    >
                        <div>
                            <div className="flex flex-row justify-start gap-4">
                                <h2 className="font-extralight text-green-600">
                                    {activeTrip.name}
                                </h2>
                            </div>
                            <div
                                className={`flex flex-col gap-2 text-xs text-gray-200 pt-2 pb-4 ${
                                    activeTrip.start?.place_name != null
                                        ? ``
                                        : `hidden`
                                }`}
                            >
                                <div className="flex flex-row gap-2">
                                    <p>{activeTrip.start?.place_name ?? ""}</p>
                                    <p className="font-semibold text-green-600">
                                        to
                                    </p>
                                    <p>{activeTrip.end?.place_name ?? ""}</p>
                                </div>
                                <div className="flex flex-row gap-2">
                                    <div className=" text-green-600 font-semibold">
                                        Adventure Level:
                                    </div>
                                    <p>
                                        {activeTrip.advLevel ?? "not specified"}
                                    </p>
                                </div>
                                <div className="flex flex-row gap-2">
                                    <div className=" text-green-600 font-semibold">
                                        Dates:
                                    </div>
                                    <p>
                                        {activeTrip.startDate ??
                                            "not specified"}
                                    </p>
                                    <p className="font-semibold text-green-600">
                                        to
                                    </p>
                                    <p>
                                        {activeTrip.endDate ?? "not specified"}
                                    </p>
                                </div>
                                <div className="text-red-400 font-base text-sm italic mt-2">
                                    {activeTrip.playlistId &&
                                        "This trip has a playlist already. Generating a new one will overwrite the current one."}
                                </div>
                            </div>
                        </div>
                    </div>

                    {selected?.length > 0 && (
                        <div className="w-1/3">
                            <h4 className="text-green-600 text-lg font-light">
                                Genres
                            </h4>
                            <div className="text-gray-50 uppercase text-sm font-light">
                                {selected &&
                                    selected.map((genre) => (
                                        <h6
                                            key={genre + "review"}
                                            className="py-2"
                                            style={{
                                                textDecorationLine: "underline",
                                                textDecorationColor:
                                                    colors[
                                                        genreOptions.indexOf(
                                                            genre
                                                        )
                                                    ],
                                                textDecorationThickness: "2px",
                                            }}
                                        >
                                            {genre}
                                        </h6>
                                    ))}
                            </div>
                        </div>
                    )}
                </div>

                {/* TODO if active trip has playlist, notify */}
                <div className="flex flex-row gap-8 w-fit items-center">
                    <Button onClick={handleSubmit}>Generate</Button>
                    <DarkOutlineButton
                        onClick={() => {
                            clearTrip();
                            router.push("/");
                        }}
                    >
                        Cancel
                    </DarkOutlineButton>
                    {showLoading && <LoadingSpinnerSmall />}
                </div>
            </div>
            {playlist?.url && (
                <div className="lg:w-3/4 pt-16">
                    <h4 className="font-semibold text-green-600 text-lg">
                        Here is your playlist!
                    </h4>
                    <div className="my-4">
                        <SpotifyEmbed wide link={playlist.url} height={512} />
                    </div>
                    <p className="text-gray-400 text-xs">
                        Don't like it? Change some settings around and try
                        again! If the playlist has too few songs, it is possible
                        that your settings were too narrow.
                    </p>
                </div>
            )}

            <div className="py-16">
                <Button onClick={() => router.push("/playlists/edit")}>
                    Done
                </Button>
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
