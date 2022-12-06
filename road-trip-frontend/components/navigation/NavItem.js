import React, { useState } from "react";
import Link from "next/link";
import { useRouter } from "next/router";

/**
 * A single element to be listed on the navbar. Must include an href in this component.
 * @param {Object} props The props passed to this object
 * @returns {React.Component} A single navbar tab
 */
const NavItem = ({ ...props }) => {
    const router = useRouter();
    return (
        <li className={`my-1 text-sm font-semibold lg:w-full flex-nowrap`}>
            <Link href={props.href ? props.href : "/"}>
                <a className="flex flex-col items-start w-full">
                    <span
                        className={`text-slate-800 grad-txt-rs-yllw font-hubballi w-fit ${
                            router.asPath == props.href && `text-green-600`
                        }`}
                    >
                        {props.children}
                    </span>
                </a>
            </Link>
        </li>
    );
};

export default NavItem;
