import React, { useState } from "react";
import { formatTitle } from "../../utils/stringUtils";
import { Checkbox } from "../general/Buttons";

/**
 *
 * @param {Object} props
 * @param {Object} props.filter
 * @param {string} props.filter.name
 * @returns
 */
const FilterItem = ({ filter }) => {
    const name = formatTitle(filter.name);
    const [isChecked, setIsChecked] = useState(false);
    return (
        <div
            className="text-slate-600 grad-txt-rs-yllw font-hubballi w-fit h-fit hover:cursor-pointer"
            onClick={() => setIsChecked(!isChecked)}
        >
            <Checkbox isChecked={isChecked} />
            {name}
        </div>
    );
};

export default FilterItem;
