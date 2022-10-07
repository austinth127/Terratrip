import React, { useEffect } from "react";
import useGeocodeSuggestions from "../../hooks/useGeocodeSuggestions";

/**
 * An input box powered by mapbox geocode autocomplete to provide search
 * suggestions while typing in locations
 * @param {Object} props The props passed to this component
 * @param {Function} props.callback Callback function to set the choice
 * @param {React.Component} props.InputComponent Styled input box
 * @param {boolean} props.dark Whether to use a dark theme
 * @param {boolean} props.initialValue Whether to use an initial value for the geocode box
 * @returns {JSX.Element} an input box with location autocomplete
 */
const Geocoder = ({ callback, InputComponent, ...props }) => {
    const address = useGeocodeSuggestions(props.initialValue ?? "");
    return (
        <div className="text-sm font-light">
            <InputComponent
                placeholder="Address"
                value={address.value}
                onChange={address.onChange}
                onBlur={() => {
                    address.setSuggestions([]);
                }}
            />
            {address.suggestions?.length > 0 && (
                <div
                    className={`${
                        props.dark
                            ? `bg-slate-900 bg-opacity-50`
                            : `bg-white border-y `
                    } absolute w-80 px-2 py-3 rounded-lg`}
                >
                    {address.suggestions.map((suggestion, index) => {
                        return (
                            <p
                                key={index}
                                className={`${
                                    props.dark
                                        ? `hover:bg-slate-800 border-slate-700`
                                        : `hover:bg-gray-100`
                                } cursor-pointer max-w-full border-b px-4 py-1`}
                                onMouseDown={() => {
                                    address.setValue(suggestion.place_name);
                                    callback(suggestion);
                                    address.setSuggestions([]);
                                }}
                            >
                                {suggestion.place_name}
                            </p>
                        );
                    })}
                </div>
            )}
        </div>
    );
};

export default Geocoder;
