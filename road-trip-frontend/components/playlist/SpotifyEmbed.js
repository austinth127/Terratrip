import React from "react";

const SpotifyEmbed = ({
    link,
    style = {},
    wide = false,
    width = wide ? "100%" : 300,
    height = wide ? 80 : 380,
    frameBorder = 0,
    allow = "autoplay; clipboard-write; encrypted-media; fullscreen; picture-in-picture",
    loading = "lazy",
    ...props
}) => {
    const url = new URL(link);
    return (
        <iframe
            title="Spotify Web Player"
            src={`https://open.spotify.com/embed${url.pathname}?utm_source=generator&theme=0`}
            width={width ?? 512}
            height={height ?? 512}
            frameBorder={frameBorder}
            allow={allow}
            style={{
                borderRadius: 8,
                ...style,
            }}
            {...props}
            onError={() => console.log("failed to do thing")}
        />
    );
};

export const SpotifyEmbedById = ({
    id,
    style = {},
    wide = false,
    width = wide ? "100%" : 300,
    height = wide ? 80 : 380,
    frameBorder = 0,
    allow = "autoplay; clipboard-write; encrypted-media; fullscreen; picture-in-picture",
    loading = "lazy",
    ...props
}) => {
    return (
        <iframe
            title="Spotify Web Player"
            src={`https://open.spotify.com/embed/playlist/${id}?utm_source=generator&theme=0`}
            width={width ?? 512}
            height={height ?? 512}
            frameBorder={frameBorder}
            allow={allow}
            style={{
                borderRadius: 8,
                ...style,
            }}
            {...props}
        />
    );
};

export default SpotifyEmbed;
