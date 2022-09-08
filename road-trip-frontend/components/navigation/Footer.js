import React from "react";
import Link from "next/link";
import Image from "next/image";

const logos = [
    {
        icon: <i className="fa-brands fa-xl fa-gitlab"></i>,
        href: "https://gitlab.com/fall-2022-group-2/road-trip",
    },
    {
        icon: <i className="fa-brands fa-xl fa-google-drive"></i>,
        href: "https://drive.google.com/drive/folders/1HjYogfFaX9OGy-u4JNg4FfWpooODbqb1?usp=sharing",
    },
    {
        icon: <i className="fa-brands fa-xl fa-spotify"></i>,
        href: "",
    },
    {
        icon: <i className="fa-solid fa-xl fa-map"></i>,
        href: "",
    },
];

const Footer = ({ ...props }) => {
    return (
        <footer className="h-36 w-full bg-transparent ">
            <div className="flex flex-row p-4 justify-center gap-8">
                {logos.map((logo, index) => (
                    <Link href={logo.href} key={index}>
                        <a>{logo.icon}</a>
                    </Link>
                ))}
            </div>
            <div className="flex flex-row justify-center">
                <div className="relative w-48 h-9 m-4">
                    <Image src="/logos/BU_Horz_White.png" layout="fill" />
                </div>
            </div>
        </footer>
    );
};

export default Footer;
