import React from "react";

/**
 * Object to optionally display an alert string
 * @param {Object} props The props passed to this object
 * @param {String} props.message Message to display
 * @returns {JSX.Element} The alert message
 */
const Alert = ({ message, ...props }) => {
    if (!message) return "";
    return (
        <div id="alert" className={`text-sm mb-4 ${props.className}`}>
            {message}
        </div>
    );
};

export default Alert;
