import React from "react";
import SideNav from "../../components/navigation/SideNav";
import Footer from "../../components/navigation/Footer";
import { useRouter } from "next/router";

/**
 * This is a layout for all pages, included in _app.js. Nextjs will auto-route
 * pages from appjs and this layout will wrap them so that they include anything in this
 * file (navbar, footer, bg, etc.).
 * @param {Object} props The props passed to this object
 * @param {React.Component[]} props.children The active page
 * @returns {React.Component} The page surrounded by the layout
 */
const Layout = ({ children, ...props }) => {
    const { asPath } = useRouter();
    return (
        <>
            {/* Background */}
            <div className="w-full h-fit absolute bg-cover bg-mountain-sun bg-fixed bg-no-repeat">
                {/* Dimmer */}
                {/* Credit to photographer */}
                <a
                    href="https://www.pexels.com/photo/snow-covered-mountain-during-sunrise-618833/"
                    className="isolate z-50 w-fit h-fit absolute bottom-4 right-4 text-gray-50 font-light"
                >
                    Picture by Sagui Andrea
                </a>
                <div className="h-fit text-gray-50 overflow-x-clip bg-slate-900 bg-opacity-20">
                    <SideNav />
                    <main className="relative min-h-screen">
                        {children}
                        <Footer></Footer>
                    </main>
                </div>
            </div>
        </>
    );
};

export default Layout;
