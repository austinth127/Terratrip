import React from "react";
import "next/image";

const PlaylistDisplay = ({ playlist, ...props }) => {
    return (
        <div className="relative bg-slate-900 bg-opacity-70 p-4 rounded-lg my-2 w-5/6 mx-8 flex flex-row gap-8 items-center">
            <img src={playlist.img} className="w-12 h-12"></img>
            <h3 className="text-cyan-600 font-semibold">{playlist.name}</h3>
            <a href={playlist.url} className="absolute right-4" target="_blank">
                <i className="fa fa-solid fa-arrow-up-right-from-square"></i>
            </a>
        </div>
    );
};

export default PlaylistDisplay;
