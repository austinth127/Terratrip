import axios from "axios";
import { useAtom, useAtomValue } from "jotai";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import { redirectAfterSpotify } from "../../utils/atoms";

const SpotifyAuth = () => {
    const router = useRouter();
    const query = router.query;
    const redirectAfter = useAtomValue(redirectAfterSpotify);

    useEffect(() => {
        if (!router.isReady) {
            return;
        }
        if (!query || !query.code) {
            console.error("No query");
            router.push(redirectAfter ?? "/playlists/edit");
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
            router.push(redirectAfter ?? "/playlists/edit");
        };
        getData();
    }, [query]);

    return <></>;
};

SpotifyAuth.usesReducedLayout = true;

export default SpotifyAuth;
