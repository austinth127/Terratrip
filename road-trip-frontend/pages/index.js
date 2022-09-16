import axios from "axios";

export default function Home({ photo, ...props }) {
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
                    </div>
                </div>
            </div>
            {/* Second Section */}
            <div className="bg-slate-800 w-full h-screen isolate rounded-md"></div>
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
