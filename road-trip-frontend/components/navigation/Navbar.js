import React, { useState, useEffect } from "react";
import NavButton from "./NavButton";
import NavItem from "./NavItem";
import { tabs } from "../../utils/tabs";
import { useRouter } from "next/router";
import AuthRoutes from "./AuthRoutes";

/**
 * A navbar to be displayed on the left side of the screen, pops out
 * when clicking on the bars with a small animation.
 * @param {Object} props The props passed to this object
 * @returns {React.Component} The navbar populated with tabs
 */
const Navbar = ({ ...props }) => {
    const [navOpen, setNavOpen] = useState();
    const router = useRouter();

    useEffect(() => {
        const handleRouteChange = () => {
            setNavOpen(false);
        };
        router.events.on("routeChangeComplete", handleRouteChange);

        return () => {
            router.events.on("routeChangeComplete", handleRouteChange);
        };
    }, []);

    return (
        <div
            className={`fixed top-0 left-0 lg:w-fit w-full lg:h-screen h-fit overflow-x-clip z-50 text-white ease-in-out duration-200 ${
                props.className
            } ${navOpen ? `` : `lg:-translate-x-[13rem]`}`}
        >
            <nav
                className={`relative isolate lg:w-64 w-full lg:h-full bg-white shadow-lg duration-200 ease-in-out ${
                    navOpen ? `bg-opacity-95` : ` bg-opacity-5`
                }`}
            >
                <div className="absolute top-4 right-4 overflow-x-clip">
                    <button
                        className={`transform duration-200 ease-in-out ${
                            navOpen ? `text-slate-900 rotate-90` : ``
                        }`}
                        onClick={() => setNavOpen(!navOpen)}
                    >
                        <i className="fa-solid fa-bars fa-xl"></i>
                    </button>
                </div>
                <ul
                    className={`flex lg:flex-col sm:flex-row flex-col justify-between items-start lg:p-8 p-8 pr-16 lg:pr-0 ${
                        navOpen ? `` : `hidden`
                    }`}
                >
                    <div className="lg:mt-4 lg:my-3">
                        <NavItem href="/" key="home">
                            Home
                        </NavItem>
                        <NavItem href="/about" key="about">
                            About
                        </NavItem>
                    </div>
                    <AuthRoutes />
                    {tabs.map((tab, index) => (
                        <div
                            key={tab.section}
                            className="lg:my-3 text-slate-800 text-sm sm:my-0 my-3"
                        >
                            <h2 className="uppercase">{tab.section}</h2>
                            {tab.tabs.map((tab, index) => (
                                <NavItem key={index} href={tab.href}>
                                    {tab.name}
                                </NavItem>
                            ))}
                        </div>
                    ))}
                </ul>
            </nav>
        </div>
    );
};

export default Navbar;
