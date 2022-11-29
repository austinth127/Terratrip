import axios from "axios";
import React, { useEffect, useState } from "react";
import { Button } from "../../components/general/Buttons";
import LoadingSpinner from "../../components/general/LoadingSpinner";

const Edit = () => {
    const [uri, setUri] = useState();
    const [user, setUser] = useState();

    useEffect(() => {
        const getData = async () => {
            let user = await axios.get("/api/user");
            setUser(user.data);
        };
        getData();
        setUri(process.env.NEXT_PUBLIC_SPOTIFY_AUTH_URI);
    }, []);

    if (!uri || !user) {
        return <LoadingSpinner></LoadingSpinner>;
    }

    return (
        <div className="pt-20">
            <div className="flex flex-row justify-center">
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
