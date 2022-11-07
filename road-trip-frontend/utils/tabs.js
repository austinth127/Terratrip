export const tabs = [
    {
        section: "Trips",
        tabs: [
            { name: "Create a Trip", href: "/trips" },
            { name: "My Trips", href: "/trips/list/user" },
            { name: "Popular Trips", href: "/trips/list/popular" },
        ],
    },
    {
        section: "Playlists",
        tabs: [
            { name: "Create a Playlist", href: "/playlists/create" },
            { name: "My Playlists", href: "/playlists/edit" },
        ],
    },
    {
        section: "Stops",
        tabs: [
            { name: "Favorites", href: "/stops/favorites" },
            { name: "High Adventure", href: "/stops/high-adventure" },
            { name: "Medium Impact", href: "/stops/medium-impact" },
            { name: "Low Impact", href: "/stops/low-impact" },
            { name: "Lodging", href: "/stops/lodging" },
        ],
    },
];
