import Head from "next/head";
import Image from "next/image";

export default function Home() {
    return (
        <>
            <div className="flex flex-col h-screen justify-center items-center">
                <h1 className="text-8xl text-center font-bold uppercase">
                    Road Trip <br /> planner thing
                </h1>
                <h3 className="text-center font-light text-gray-300">
                    Brought to you by Boothverse 2: Electric Bogaloo
                </h3>
            </div>
        </>
    );
}
