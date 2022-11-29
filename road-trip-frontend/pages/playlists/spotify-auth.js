import axios from "axios";
import { useRouter } from "next/router";
import React, { useEffect } from "react";

const SpotifyAuth = () => {
    const router = useRouter();
    const query = router.query;

    useEffect(() => {
        if (!router.isReady) {
            return;
        }
        if (!query || !query.code) {
            console.log("No query");
            return;
        }
        const getData = async () => {
            const userRes = await axios.get("/api/user");
            const userId = userRes.data.id;
            const res = await axios.get(
                "/api/spotify/auth-callback/?code=" +
                    query.code +
                    "&state=" +
                    userId
            );
            router.push("/playlists/edit");
        };
        getData();
    }, [query]);

    return <></>;
};

SpotifyAuth.usesReducedLayout = true;

export default SpotifyAuth;
