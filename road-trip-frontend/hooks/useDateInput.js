import { useState } from "react";

const useDateInput = (initialValue) => {
    const [value, setValue] = useState(initialValue);

    const handleChange = (e) => {
        const newDate = new Date(e.target.value);
        setValue(newDate.toLocaleDateString());
    };

    return {
        value,
        onChange: handleChange,
    };
};

export default useDateInput;
