import React, { useEffect } from "react";
import { setupAxios } from "../../utils/axiosSetup";

/**
 * This is a layout for all pages, included in _app.js. Nextjs will auto-route
 * pages from appjs and this layout will wrap them so that they include anything in this
 * file (navbar, footer, bg, etc.).
 * @param {Object} props The props passed to this object
 * @param {React.Component[]} props.children The active page
 * @returns {React.Component} The page surrounded by the layout
 */
const ReducedLayout = ({ children, ...props }) => {
    useEffect(() => {
        setupAxios();
    });

    return (
        <>
            {/* Background */}
            <div className="w-full h-fit min-h-screen absolute bg-cover bg-wireframe-terrain bg-fixed bg-no-repeat bg-bottom">
                {/* Dimmer */}

                <div className="h-fit min-h-screen text-gray-50 overflow-x-clip bg-slate-900 bg-opacity-95">
                    <main className="relative h-full ">{children}</main>
                    <a
                        href="https://www.vecteezy.com/free-vector/wireframe-terrain"
                        className="absolute text-xs font-mono right-4 bottom-4 text-gray-400"
                    >
                        Wireframe Terrain Vectors by Vecteezy
                    </a>
                </div>
            </div>
        </>
    );
};

export default ReducedLayout;
