import React from "react";
import Navbar from "../../components/navigation/Navbar";
import Footer from "../../components/navigation/Footer";
import { useRouter } from "next/router";

const gradientBackground = {
    backgroundColor: `#27006A`,
    backgroundImage: `radial-gradient(at 69% 76%, #e76f51 , transparent 57%), 
                      radial-gradient(at 35% 78%, #f4a261 0, transparent 82%), 
                      radial-gradient(at 2% 94%, #6A148C, transparent 69%), 
                      radial-gradient(at 92% 46%, rgba(255,66,85, .5) 0, transparent 88%), 
                      radial-gradient(at 13% 86%, rgb(245,245,244) 0, transparent 78%), 
                      radial-gradient(at 85% 4%, #390099 0, transparent 100%), 
                      radial-gradient(at 10% 30%, rgba(255,66,85) 0, transparent 39%)`,
    backgroundSize: "cover",
};

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
            <div
                className="h-fit text-gray-50 overflow-x-clip bg-slate-900"
                // style={gradientBackground}
            >
                <Navbar />
                <main className="min-h-screen">{children}</main>
                <Footer></Footer>
            </div>
        </>
    );
};

export default Layout;
