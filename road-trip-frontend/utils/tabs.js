export const tabs = [
    {
        section: "Trips",
        tabs: [
            { name: "Create a Trip", href: "/trips" },
            { name: "My Trips", href: "/trips/list/user" },
        ],
    },
    {
        section: "Playlists",
        tabs: [
            { name: "Create a Playlist", href: "/playlists/create" },
            { name: "My Playlists", href: "/playlists/edit" },
        ],
    },
];

export const publicRoutes = ["/", "/auth/[auth]", "/about"];
