import React from "react";
import "next/image";
import SpotifyEmbed, { SpotifyEmbedById } from "./SpotifyEmbed";
import { Button, DarkOutlineButton } from "../general/Buttons";
import { useAtom } from "jotai";
import { tripAtom } from "../../utils/atoms";
import { useRouter } from "next/router";

const PlaylistDisplay = ({ trip, ...props }) => {
    const [activeTrip, setActiveTrip] = useAtom(tripAtom);
    const router = useRouter();

    return (
        <div className="relative bg-slate-900 bg-opacity-70 p-4 rounded-lg my-4 w-5/6 mx-8">
            <h2 className="font-semibold text-lg text-green-600">
                {trip.name}
            </h2>
            <div
                className={`flex flex-col gap-2 text-xs text-gray-50 pt-2 pb-4 ${
                    trip.start?.place_name != null ? `` : `hidden`
                }`}
            >
                <div className="flex flex-row gap-2">
                    <p>{trip.start?.place_name ?? ""}</p>
                    <p className="font-semibold text-green-600">to</p>
                    <p>{trip.end?.place_name ?? ""}</p>
                </div>
                <div className="flex flex-row gap-2">
                    <div className=" text-green-600 font-semibold">
                        Adventure Level:
                    </div>
                    <p>{trip.advLevel ?? "not specified"}</p>
                </div>
                <div className="flex flex-row gap-2">
                    <div className=" text-green-600 font-semibold">Dates:</div>
                    <p>{trip.startDate ?? "not specified"}</p>
                    <p className="font-semibold text-green-600">to</p>
                    <p>{trip.endDate ?? "not specified"}</p>
                </div>
            </div>
            {trip.playlistId && (
                <SpotifyEmbedById
                    wide
                    id={trip.playlistId}
                    className="mt-4 mb-1"
                ></SpotifyEmbedById>
            )}
            <div className="my-2 absolute top-4 right-4">
                <DarkOutlineButton
                    onClick={() => {
                        setActiveTrip(trip);
                        router.push("/playlists/create");
                    }}
                >
                    Edit Playlist
                </DarkOutlineButton>
            </div>
        </div>
    );
};

export default PlaylistDisplay;
