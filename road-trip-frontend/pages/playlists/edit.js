import axios from "axios";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import { Button } from "../../components/general/Buttons";
import LoadingSpinner from "../../components/general/LoadingSpinner";
import PlaylistDisplay from "../../components/playlist/PlaylistDisplay";
import { setupAxios } from "../../utils/axiosSetup";

const Edit = () => {
    const [uri, setUri] = useState();
    const [user, setUser] = useState();
    const [trips, setTrips] = useState();
    const router = useRouter();

    useEffect(() => {
        const getData = async () => {
            setupAxios();
            let user = await axios.get("/api/user");
            setUser(user.data);
            let res = await axios.get("/api/trip");
            setTrips(res.data);
        };
        getData();
        setUri(process.env.NEXT_PUBLIC_SPOTIFY_AUTH_URI);
    }, []);

    if (!uri || !user) {
        return <LoadingSpinner></LoadingSpinner>;
    }

    return (
        <div className="pt-20">
            <div className="flex flex-col items-center">
                {trips && trips.length != 0 ? (
                    trips
                        .filter((trip) => trip.playlistId != null)
                        .map((trip) => (
                            <PlaylistDisplay
                                key={trip.id}
                                trip={trip}
                            ></PlaylistDisplay>
                        ))
                ) : trips && trips.length == 0 ? (
                    <div className="flex flex-col gap-4 text-center w-3/4 py-8 text-gray-300 font-semibold text-2xl bg-slate-900 p-4 rounded-lg bg-opacity-50">
                        No trips with playlists yet!
                        <div className="w-full flex flex-row justify-center mt-4">
                            <Button
                                onClick={() => router.push("/playlists/create")}
                            >
                                Create a playlist
                            </Button>
                        </div>
                    </div>
                ) : (
                    <LoadingSpinner></LoadingSpinner>
                )}
            </div>

            <div className="flex flex-row justify-center my-16">
                {user.spotifyUserId ? (
                    <div className="text-gray-300">
                        You are connected to Spotify
                        <i className="-mr-2 ml-1 fa-solid fa-check"></i>
                    </div>
                ) : (
                    <a target="_blank" href={uri} rel="noopener noreferrer">
                        <Button>
                            Connect to Spotify{" "}
                            <i className="-mr-2 ml-1 fa-brands fa-xl fa-spotify"></i>
                        </Button>
                    </a>
                )}
            </div>
        </div>
    );
};

export default Edit;
