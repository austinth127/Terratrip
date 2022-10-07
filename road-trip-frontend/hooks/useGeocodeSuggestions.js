import axios from "axios";
import { useState } from "react";

const accessToken = process.env.NEXT_PUBLIC_MAPBOX_ACCESS_TOKEN;

/**
 * A utility to get a list of mapbox location suggestions while typing in a textbox
 * @param {string} initialValue
 * @returns
 */
const useGeocodeSuggestions = (initialValue) => {
    const [value, setValue] = useState(initialValue);
    const [suggestions, setSuggestions] = useState([]);
    const [prevInputLength, setPrevInputLength] = useState(0);

    const handleChange = async (event) => {
        setValue(event.target.value);
        let length = event.target.value.length;
        if (length >= 4 && Math.abs(length - prevInputLength) >= 3) {
            try {
                const endpoint = `https://api.mapbox.com/geocoding/v5/mapbox.places/${event.target.value}.json?access_token=${accessToken}&autocomplete=true`;
                const response = await axios.get(endpoint);
                const results = response.data;
                setSuggestions(results?.features);
                setPrevInputLength(length);
            } catch (error) {
                console.log("Error fetching data, ", error);
            }
        }
    };

    return {
        value,
        onChange: handleChange,
        setValue,
        suggestions,
        setSuggestions,
    };
};

export default useGeocodeSuggestions;
