import React, { useState } from "react";
import { Button } from "../components/general/Buttons";
import TextInput from "../components/general/TextInput";
import TextLogo from "../components/general/TextLogo";
import Geocoder from "../components/map/Geocoder";
import Image from "next/image";
import { useAtom, useSetAtom } from "jotai";
import { endAtom, startAtom } from "../utils/atoms";
import { useRouter } from "next/router";
import Userfront from "@userfront/core";
/**
 * The home page for the website
 * @param {Object} props The props passed to the object
 * @returns {React.Component} The home page
 */
export default function Home({ ...props }) {
    const setStart = useSetAtom(startAtom);
    const setEnd = useSetAtom(endAtom);
    const router = useRouter();

    console.log(Userfront.tokens.accessToken);
    return (
        <div className="flex flex-col items-center">
            {/* First section */}
            <div className="flex flex-col min-h-screen mt-24 items-center px-8">
                {/* Title / Subtitle */}
                <div className="isolate mb-16">
                    <div className="flex sm:flex-row sm:-ml-8 flex-col">
                        <Image
                            width={128}
                            height={128}
                            layout="intrinsic"
                            src="/logos/mountain.svg"
                        />
                        <TextLogo className="text-6xl drop-shadow-md self-center sm:mt-8" />
                    </div>
                    <h3 className="text-center font-light text-lg text-gray-100 drop-shadow-md mt-2">
                        Create your outdoor adventure.
                    </h3>
                </div>

                {/* Trip start box */}
                <div className="isolate mt-12 gap-8 p-2 pt-4 rounded-lg bg-gray-200 shadow-lg drop-shadow-lg text-slate-800 h-fit">
                    <h4 className="px-4 font-semibold">Begin your journey.</h4>
                    <div className="flex sm:flex-row flex-col">
                        <div className="py-4 px-2 sm:px-4">
                            <label className="px-1 font-light">Start</label>
                            <Geocoder
                                callback={setStart}
                                InputComponent={TextInput}
                            />
                        </div>
                        <div className="py-4 px-2 sm:px-4">
                            <label className="px-1 font-light">End</label>
                            <Geocoder
                                callback={setEnd}
                                InputComponent={TextInput}
                            />
                        </div>
                        <div className="p-4 flex items-end">
                            <Button
                                onClick={() => router.push("/trips/create")}
                            >
                                Submit
                            </Button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

// This is how you can get server side data befre render
// export async function getServerSideProps() {
//     // Fetch data from external API
//     const page = Math.floor(Math.random() * 10) + 1;

//     try {
//         const res = await axios.get(
//             `https://api.pexels.com/v1/search?query=nature&per_page=1&orientation=landscape&page=${page}`,
//             {
//                 headers: {
//                     Authorization:
//                         "563492ad6f91700001000001088d0dbdcca94fb4bd0ca364d64f06b8",
//                 },
//             }
//         );
//         const photo = res.data.photos[0];

//         // Pass data to the page via props
//         return { props: { photo } };
//     } catch (e) {
//         console.log("Error getting server side props from api", e);
//     }
//     return { props: {} };
// }
