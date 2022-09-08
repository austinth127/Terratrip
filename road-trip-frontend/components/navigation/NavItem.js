import React, { useState } from "react";
import Link from "next/link";

/**
 * A single element to be listed on the navbar. Must include an href in this component.
 * @param {Object} props The props passed to this object
 * @returns {React.Component} A single navbar tab
 */
const NavItem = ({ ...props }) => {
    return (
        <li className={`my-1 text-sm w-full`}>
            <Link href={props.href ? props.href : "/"}>
                <a className="flex flex-col items-start w-full">
                    <span className="text-white hover:text-transparent hover:bg-clip-text hover:bg-gradient-to-br hover:from-yellow-400 hover:to-rose-400 font-hubballi w-fit">
                        {props.children}
                    </span>
                </a>
            </Link>
        </li>
    );
};

export default NavItem;
