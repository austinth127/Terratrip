import React, { useState, useEffect } from "react";
import NavButton from "./NavButton";
import NavItem from "./NavItem";

// These are all of the routes in the nav bar
const tabs = [
    { name: "Home", href: "/" },
    {
        section: "Trips",
        tabs: [
            { name: "Create a Trip", href: "/trips/create" },
            { name: "My Trips", href: "/trips/edit" },
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
            { name: "Popular Stops", href: "" },
            { name: "Favorites", href: "" },
            { name: "Nature", href: "" },
            { name: "Music", href: "" },
            { name: "City", href: "" },
            { name: "Food", href: "" },
        ],
    },
];

/**
 * A navbar to be displayed on the left side of the screen, pops out
 * when clicking on the bars with a small animation.
 * @param {Object} props The props passed to this object
 * @returns {React.Component} The navbar populated with tabs
 */
const Navbar = ({ ...props }) => {
    const [navOpen, setNavOpen] = useState();

    return (
        <div
            className={`fixed top-0 left-0 w-fit h-screen overflow-x-clip text-gray-50 ${props.className}`}
        >
            <nav
                className={`relative bg-opacity-5 sm:w-64 ease-in-out z-50 h-full duration-200 bg-white ${
                    navOpen ? `` : `sm:-translate-x-[13rem]`
                }`}
            >
                <div className="absolute top-4 right-4 overflow-x-clip">
                    <button
                        className={`transform duration-200 ease-in-out ${
                            navOpen ? `rotate-90` : ``
                        }`}
                        onClick={() => setNavOpen(!navOpen)}
                    >
                        <i className="fa-solid fa-bars fa-xl"></i>
                    </button>
                </div>
                <ul className={`flex flex-col p-8 ${navOpen ? `` : `hidden`}`}>
                    {tabs.map((tab, index) =>
                        tab.section ? (
                            <div
                                key={tab.section}
                                className="my-3 text-gray-300 text-sm"
                            >
                                <h2 className="uppercase">{tab.section}</h2>
                                {tab.tabs.map((tab, index) => (
                                    <NavItem key={index} href={tab.href}>
                                        {tab.name}
                                    </NavItem>
                                ))}
                            </div>
                        ) : (
                            <NavItem key={index} href={tab.href}>
                                {tab.name}
                            </NavItem>
                        )
                    )}
                    <li className="flex flex-col gap-2 text-sm font-light py-3 text-white">
                        <NavButton>Log In</NavButton>
                        <NavButton>Sign Up</NavButton>
                    </li>
                </ul>
            </nav>
        </div>
    );
};

export default Navbar;
