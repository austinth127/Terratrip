import React from "react";
import { useState } from "react";

/**
 * Standard text field for input
 * @param {Object} props
 * @param {Function} props.callback Callback state function
 * @param {string} props.type type of the text input
 * @returns
 */
const TextInput = ({ callback, type, ...props }) => {
    const [value, setValue] = useState();

    return (
        <>
            <input
                value={value}
                onChange={(event) => {
                    setValue(event.target.value);
                    if (callback) {
                        callback(event.target.value);
                    }
                }}
                className="block w-full px-2 py-1.5 text-sm font-normal rounded transition ease-in-out m-0
                                    focus:border-lime-600 focus:outline-none bg-transparent border-gray-300 border"
                type={type}
                {...props}
            ></input>
        </>
    );
};

export default TextInput;
