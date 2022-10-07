import React from "react";
import { useState } from "react";

/**
 * Standard text field for input
 * @param {Object} props
 * @returns {JSX.Element} The text field
 */
const TextInput = ({ ...props }) => {
    const [value, setValue] = useState();

    return (
        <input
            className="block w-full px-2 py-1.5 text-sm font-normal rounded transition ease-in-out m-0
                                    focus:border-lime-600 focus:outline-none bg-inherit border-gray-300 border text-slate-800
                                      focus:bg-gray-50"
            {...props}
        ></input>
    );
};

/**
 * Standard text field for dark backgrounds
 * @param {Object} props
 * @returns {JSX.Element} The text field
 */
export const DarkTextInput = ({ ...props }) => {
    return (
        <input
            className="block w-full px-2 py-1.5 text-sm font-normal rounded transition ease-in-out m-0
                                focus:border-lime-600 focus:outline-none bg-transparent border-gray-500 border text-gray-200
                                  focus:bg-slate-900 focus:bg-opacity-25"
            {...props}
        ></input>
    );
};

export default TextInput;
