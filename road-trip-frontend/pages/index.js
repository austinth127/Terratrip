import Head from "next/head";
import Image from "next/image";

export default function Home() {
    return (
        <>
            <Head>
                <title>Roadtrippers</title>
                <meta
                    name="description"
                    content="Your new favorite road trip app"
                />
                <link rel="icon" href="/favicon.ico" />
            </Head>
            <div>Hello World!</div>
        </>
    );
}
