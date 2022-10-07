import React, { useEffect } from "react";
import "../styles/globals.css";

import Script from "next/script";
import Head from "next/head";

// https://fontawesome.com/v5/docs/web/use-with/react
// The following imports prevents a Font Awesome icon server-side rendering bug,
// where the icons flash from a very large icon down to a properly sized one:
import "@fortawesome/fontawesome-svg-core/styles.css";

// Prevent fontawesome from adding its CSS since we did it manually above:
import { config } from "@fortawesome/fontawesome-svg-core";
config.autoAddCss = false;

import Layout from "../components/general/Layout";
import MapLayout from "../components/map/MapLayout";

import { setupAxios, setupLogger } from "../utils/axiosSetup";

import Userfront from "@userfront/core";
import { Provider } from "jotai";
import ReducedLayout from "../components/general/ReducedLayout";
Userfront.init("wbmrp64n");

/**
 * This will be rendered automatically by next js as the root of the react app.
 * https://nextjs.org/docs/basic-features/pages
 *
 * @param {Object} props
 * @param {React.Component} props.Component The content of the current page
 * @param {any} props.pageProps Props to be passed to the page
 * @returns {React.Component} The page to be rendered
 */
function MyApp({ Component, pageProps }) {
    useEffect(() => {
        setupAxios();
        setupLogger();
    }, []);

    return (
        <>
            <Head>
                <title>Terratrip</title>
                <meta
                    name="description"
                    content="A web app to identify, detect, and educate about antipatterns."
                />
            </Head>
            {/* This Handles importing any and all fontawesome icons.*/}
            <Script
                src="https://kit.fontawesome.com/1b232c1fd4.js"
                crossorigin="anonymous"
            ></Script>
            <Provider>
                {Component.usesMapLayout ? (
                    <MapLayout>
                        <Component {...pageProps} />
                    </MapLayout>
                ) : Component.usesReducedLayout ? (
                    <ReducedLayout>
                        <Component {...pageProps} />
                    </ReducedLayout>
                ) : (
                    <Layout>
                        <Component {...pageProps} />
                    </Layout>
                )}
            </Provider>
        </>
    );
}

export default MyApp;
