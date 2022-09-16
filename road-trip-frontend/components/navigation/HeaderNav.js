import React from "react";
import { tabs } from "../../utils/tabs";

const HeaderNav = () => {
    return (
        <div
            className={`fixed top-0 left-0 w-screen h-fit overflow-clip z-50 text-white ease-in-out duration-200 ${
                props.className
            } ${navOpen ? `` : `sm:-translate-x-[13rem]`}`}
        >
            <nav
                className={`relative isolate sm:w-64 h-full bg-white shadow-lg ${
                    navOpen ? `bg-opacity-100` : ` bg-opacity-10`
                }`}
            >
                <div className="absolute top-4 right-4 overflow-x-clip">
                    <button
                        className={`transform duration-200 ease-in-out ${
                            navOpen ? `text-slate-900 rotate-90` : ``
                        }`}
                        onClick={() => setNavOpen(!navOpen)}
                    >
                        <i className="fa-solid fa-bars fa-xl"></i>
                    </button>
                </div>
                <ul className={`flex flex-col p-8 ${navOpen ? `` : `hidden`}`}>
                    {tabs.map((tab, index) =>
                        tab.section ? (
                            <div
                                key={tab.section}
                                className="my-3 text-slate-800 text-sm"
                            >
                                <h2 className="uppercase">{tab.section}</h2>
                                {tab.tabs.map((tab, index) => (
                                    <NavItem key={index} href={tab.href}>
                                        {tab.name}
                                    </NavItem>
                                ))}
                            </div>
                        ) : (
                            <NavItem key={index} href={tab.href}>
                                {tab.name}
                            </NavItem>
                        )
                    )}
                    <div className="mt-4">
                        <NavItem href="/auth/signin" key="log in">
                            Log In
                        </NavItem>
                        <NavItem href="/auth/signup" key="sign up">
                            Sign Up
                        </NavItem>
                    </div>
                </ul>
            </nav>
        </div>
    );
};

export default HeaderNav;
