import React from "react";

const TextLogo = ({ ...props }) => {
    return (
        <div className={`font-normal ${props.className}`}>
            <span className="text-green-600 font-bold">Terra</span>trip
        </div>
    );
};

export default TextLogo;
