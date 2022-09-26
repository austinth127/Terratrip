import axios from "axios";
import React, { useState } from "react";
import { Button } from "../components/general/Buttons";
import TextInput from "../components/general/TextInput";

/**
 * The home page for the website
 * @param {Object} props The props passed to the object
 * @returns {React.Component} The home page
 */
export default function Home({ ...props }) {
    const [startLocation, setStartLocation] = useState("");
    const [reverseStart, setReverseStart] = useState(startLocation);

    const revStartLocation = () => {
        // Make a request for a user with a given ID
        axios
            .get("http://localhost:8080/string-reverse", {
                params: {
                    value: startLocation,
                },
            })
            .then((response) => {
                // handle success
                setReverseStart(response.data.reverse);
                console.log(response.data.reverse);
            });
    };

    return (
        <div className="flex flex-col items-center">
            {/* First section */}
            <div className="flex flex-col h-screen justify-center items-center px-8">
                {/* Title / Subtitle */}
                <div className="isolate mb-16">
                    <h1 className="text-6xl text-center font-bold uppercase drop-shadow-md">
                        Terratrip
                    </h1>
                    <h3 className="text-center font-light text-lg text-gray-100 drop-shadow-md">
                        Create your outdoor adventure.
                    </h3>
                </div>
                {/* Trip start box */}
                <div className="isolate mt-12 gap-8 p-2 pt-4 rounded-lg bg-gray-200 shadow-lg drop-shadow-lg text-slate-800">
                    <h4 className="px-4 font-semibold">Begin your journey.</h4>
                    <div className="flex flex-row">
                        <div className="py-4 px-2 sm:px-4">
                            <label className="px-1 font-light">Start</label>
                            <TextInput callback={setStartLocation} />
                        </div>
                        <div className="py-4 px-2 sm:px-4">
                            <label className="px-1 font-light">End</label>
                            <TextInput />
                        </div>
                        <div className="p-4 flex items-end">
                            <Button onClick={revStartLocation}>Submit</Button>
                        </div>
                    </div>
                    <div className="flex justify-center">
                        <label>{reverseStart}</label>
                    </div>
                </div>
            </div>
            {/* Second Section */}
            {/* <div className="bg-slate-800 w-full h-screen isolate rounded-md"></div> */}
            {/* Footer padding */}
            <div className="p-24 w-full invisible"></div>
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
