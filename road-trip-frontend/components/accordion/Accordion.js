import React, { useState } from "react";

const Accordion = ({ defaultToExpanded, header, ...props }) => {
    const [expanded, setExpanded] = useState(defaultToExpanded ?? true);
    return (
        <div>
            <div className="border-b relative pr-4">
                {header}
                <div className="absolute top-0 right-2">
                    <button
                        className={`w-fit h-fit duration-200 ${
                            expanded ? `rotate-180` : ``
                        }`}
                        onClick={() => setExpanded(!expanded)}
                    >
                        <i className="fa-solid fa-angle-up"></i>
                    </button>
                </div>
            </div>
            <div
                className={`${
                    expanded ? `max-h-96` : `max-h-0`
                } duration-300 overflow-clip bg-inherit `}
            >
                {props.children}
            </div>
        </div>
    );
};

export default Accordion;
