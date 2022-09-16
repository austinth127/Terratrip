export const tabs = [
    { name: "Home", href: "/" },
    { name: "About", href: "/about" },

    {
        section: "Trips",
        tabs: [
            { name: "Create a Trip", href: "/trips/create" },
            { name: "My Trips", href: "/trips/edit" },
            { name: "Popular Trips", href: "/trips/popular" },
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
            { name: "Favorites", href: "" },
            { name: "High Adventure", href: "" },
            { name: "National Parks", href: "" },
            { name: "Camping", href: "" },
            { name: "RV's", href: "" },
            { name: "Cabins", href: "" },
            { name: "Hotels", href: "" },
        ],
    },
];
