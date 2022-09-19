import { useRouter } from "next/router";
import React from "react";

const Stops = () => {
    const { asPath } = useRouter();
    const name = asPath
        .replace("/stops", "")
        .replace("/", "")
        .split("-")
        .map((str) => (str = str.charAt(0).toLocaleUpperCase() + str.slice(1)))
        .join(" ");

    return <div className="text-center">{name}</div>;
};

export default Stops;
