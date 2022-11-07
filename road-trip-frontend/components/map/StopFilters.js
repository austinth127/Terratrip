import { useAtom } from "jotai";
import React, { useMemo, useState } from "react";
import { advLevelAtom } from "../../utils/atoms";
import { filters, levelOptions } from "../../utils/stops/filters";
import FilterItem from "./FilterItem";
import { formatTitle } from "../../utils/stringUtils";
import Accordion from "../accordion/Accordion";

const StopFilterChecklist = () => {
    const [level] = useAtom(advLevelAtom);
    const [options, setOptions] = useState(() => {
        let options = [];
        const levelNum = levelOptions.findIndex(
            (name) => name.toLowerCase() == level.toLowerCase()
        );

        Object.keys(filters).forEach((category) => {
            let subcategories = [];
            Object.keys(filters[category]).forEach((subcategory) => {
                if (filters[category][subcategory].level <= levelNum) {
                    subcategories.push({
                        name: subcategory,
                        level: filters[category][subcategory].level,
                    });
                }
            });
            if (subcategories.length != 0) {
                options.push({
                    category,
                    subcategories,
                    isActive: false,
                });
            }
        });
        return options;
    });
    const [selectedFilters, setSelectedFilters] = useState(new Set());

    return (
        <div className="w-full h-full overflow-y-scroll overflow-x-hidden px-4 text-slate-800">
            {options.map((option) => (
                <div key={option.category} className="text-sm my-3">
                    <Accordion
                        header={
                            <h2 className="uppercase font-light">
                                {formatTitle(option.category)}
                            </h2>
                        }
                    >
                        <div className="pl-1 py-2">
                            {option.subcategories.map((subcat) => (
                                <FilterItem
                                    key={subcat.name}
                                    filter={subcat}
                                    onCheck={() => {
                                        selectedFilters.add(
                                            `${option.category}${subcat.name}`
                                        );
                                    }}
                                ></FilterItem>
                            ))}
                        </div>
                    </Accordion>
                </div>
            ))}
        </div>
    );
};

export default StopFilterChecklist;
