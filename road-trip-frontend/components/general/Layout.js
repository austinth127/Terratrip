import React, { useEffect } from "react";
import Navbar from "../../components/navigation/Navbar";
import Footer from "../../components/navigation/Footer";
import NotificationOverlay from "../notifications/NotificationOverlay";
import { useSetAtom } from "jotai";
import { clearTripAtom, recStopAtom } from "../../utils/atoms";

import Userfront from "@userfront/core";
import { setupAxios } from "../../utils/axiosSetup";
Userfront.init("wbmrp64n");

/**
 * This is a layout for all pages, included in _app.js. Nextjs will auto-route
 * pages from appjs and this layout will wrap them so that they include anything in this
 * file (navbar, footer, bg, etc.).
 * @param {Object} props The props passed to this object
 * @param {React.Component[]} props.children The active page
 * @returns {React.Component} The page surrounded by the layout
 */
const Layout = ({ children, ...props }) => {
    const clearTrip = useSetAtom(clearTripAtom);

    useEffect(() => {
        setupAxios();
        clearTrip();
    });

    return (
        <>
            {/* Background */}
            <div className="w-full h-fit absolute bg-cover bg-mountain-sun bg-fixed bg-no-repeat">
                {/* Dimmer */}

                <div className="h-fit text-gray-50 overflow-x-clip bg-slate-900 bg-opacity-50">
                    <Navbar />
                    {Userfront.user.userId && <NotificationOverlay />}
                    <main className="relative min-h-screen lg:ml-16">
                        {children}
                        {/* Footer Padding */}
                        <div className="p-24 w-full invisible"></div>
                        <Footer></Footer>
                    </main>
                </div>
            </div>
        </>
    );
};

export default Layout;
