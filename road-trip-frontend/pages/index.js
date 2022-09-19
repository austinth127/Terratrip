import axios from "axios";
import { useState } from "react";

export default function Home({ photo, ...props }) {
    const [startLocation, setStartLocation] = useState("");
    const [reverseStart, setReverseStart] = useState(startLocation);

    return (
        <div className="flex flex-col items-center">
            {/* Background */}
            <div className="w-full h-full absolute bg-cover bg-mountain-sun bg-fixed bg-no-repeat">
                {/* Dimmer */}
                <div className="w-full h-full bg-slate-900 bg-opacity-20"></div>
                {/* Credit to photographer */}
                <a
                    href="https://www.pexels.com/photo/snow-covered-mountain-during-sunrise-618833/"
                    className="isolate z-50 w-fit h-fit absolute bottom-4 right-4 text-gray-50 font-light"
                >
                    Picture by Sagui Andrea
                </a>
            </div>
            {/* First section */}
            <div className="flex flex-col h-screen justify-center items-center">
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
                        <div className="p-4">
                            <label className="px-1 font-light">Start</label>
                            <input
                                value={startLocation}
                                onChange={(event) => {
                                    setStartLocation(event.target.value);
                                }}
                                className="block w-full px-2 py-1.5 text-sm font-normal rounded transition ease-in-out m-0
                                    focus:border-lime-600 focus:outline-none bg-transparent border-gray-300 border"
                                type="text"
                            ></input>
                        </div>
                        <div className="p-4">
                            <label className="px-1 font-light">End</label>
                            <input
                                className="block w-full px-2 py-1.5 text-sm font-normal rounded transition ease-in-out m-0
                                    focus:border-lime-700 focus:outline-none bg-transparent border-gray-300 border"
                                type="text"
                            ></input>
                        </div>
                        <div className="p-4 flex items-end">
                            <button
                                type="button"
                                className=" px-6 py-2.5 bg-green-600 text-white font-medium text-xs leading-tight uppercase rounded shadow-md hover:bg-green-700 hover:shadow-lg focus:bg-green-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-green-800 active:shadow-lg transition duration-150 ease-in-out"
                                onClick={() => {
                                    // Make a request for a user with a given ID
                                    axios
                                        .get(
                                            "http://localhost:8080/string-reverse",
                                            {
                                                params: {
                                                    value: startLocation,
                                                },
                                            }
                                        )
                                        .then((response) => {
                                            // handle success
                                            setReverseStart(
                                                response.data.reverse
                                            );
                                            console.log(response.data.reverse);
                                        });
                                }}
                            >
                                Submit
                            </button>
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
            <div className="p-24 w-full invislbe"></div>
        </div>
    );
}

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
