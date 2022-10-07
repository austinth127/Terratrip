import React, { useEffect } from "react";
import useGeocodeSuggestions from "../../hooks/useGeocodeSuggestions";
import TextInput from "../general/TextInput";

const Geocoder = () => {
    const address = useGeocodeSuggestions("");
    return (
        <>
            <TextInput
                placeholder="Address"
                value={address.value}
                onChange={address.onChange}
                onBlur={() => address.setSuggestions([])}
            />
            {address.suggestions?.length > 0 && (
                <div className="bg-white absolute w-80 px-2 py-3 border-y rounded-lg">
                    {address.suggestions.map((suggestion, index) => {
                        return (
                            <p
                                key={index}
                                className="cursor-pointer max-w-full  hover:bg-gray-100 border-b px-4"
                                onClick={() => {
                                    address.setValue(suggestion.place_name);
                                    address.setSuggestions([]);
                                }}
                            >
                                {suggestion.place_name}
                            </p>
                        );
                    })}
                </div>
            )}
        </>
    );
};

export default Geocoder;
