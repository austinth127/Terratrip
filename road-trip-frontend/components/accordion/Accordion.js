import React, { useState } from "react";

const Accordion = ({ defaultToExpanded, header, ...props }) => {
    const [expanded, setExpanded] = useState(defaultToExpanded ?? true);
    return (
        <div>
            <div className="border-b relative h-fit">
                {header}
                <div className="absolute top-2 right-2">
                    <div
                        className={`w-fit h-fit ${
                            expanded ? `rotate-180` : ``
                        }`}
                    >
                        <i className="fa-solid fa-angle-up"></i>
                    </div>
                </div>
            </div>
            <div
                className={`${
                    expanded ? `h-fit` : `h-0`
                } duration-200 ease-in-out overflow-clip bg-inherit`}
            >
                {props.children}
            </div>
        </div>
    );
};

export default Accordion;
