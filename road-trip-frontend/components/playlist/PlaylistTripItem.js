import React from "react";
import { Button } from "../general/Buttons";
import SpotifyEmbed, { SpotifyEmbedById } from "./SpotifyEmbed";

const PlaylistTripItem = ({ trip, isSelected, callback }) => {
    return (
        <div
            className={`w-full bg-slate-900 rounded-lg my-3 hover:cursor-pointer p-4 bg-opacity-80 ${
                isSelected && `ring ring-green-600`
            }`}
            onClick={() => callback(trip)}
        >
            <div className="flex flex-row justify-between items-center">
                <h3 className="font-semibold text-sm text-green-600 py-1">
                    {trip.name}
                    <div className="w-96 flex flex-row justify-start gap-2 text-xs font-light text-gray-400">
                        <p>{trip.start?.place_name ?? ""}</p>
                        <p className="font-semibold text-green-600">to</p>
                        <p>{trip.end?.place_name ?? ""}</p>
                    </div>
                </h3>
                <div
                    className={
                        trip.playlistId
                            ? `font-semibold text-green-500`
                            : `font-light text-gray-300`
                    }
                >
                    {trip.playlistId ? "Has Playlist" : "No Playlist"}
                </div>
            </div>
            {trip.playlistId && (
                <div className="my-2 relative">
                    <SpotifyEmbedById
                        id={trip.playlistId}
                        wide
                    ></SpotifyEmbedById>
                </div>
            )}
        </div>
    );
};

export default PlaylistTripItem;
