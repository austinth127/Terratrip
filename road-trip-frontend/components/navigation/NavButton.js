import React from "react";
import { useRouter } from "next/router";

/**
 * A single button to display on the navbar
 * @param {Object} props The props passed to this object
 * @param {any} props.children What to put inside the button
 * @returns {React.Component} A button for the navbar
 */
const NavButton = ({ children, ...props }) => {
    const router = useRouter();
    return (
        <button
            className={`w-16 px-2 py-1 border rounded-lg hover:bg-rose-500 hover:bg-opacity-50 focus:ring focus:ring-rose-500/50 active:bg-rose-600 focus:border-rose-400 active:bg-opacity-50 active:shadow-lg transition duration-150 ease-in-out ${props.className}`}
            onClick={() => router.push(props.href)}
        >
            {children}
        </button>
    );
};

export default NavButton;
