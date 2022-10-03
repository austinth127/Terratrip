import React from "react";

const Alert = ({ ...props }) => {
    if (!props.message) return "";
    return (
        <div id="alert" className="text-sm text-center mb-4 text-red-600">
            {props.message}
        </div>
    );
};

export default Alert;
