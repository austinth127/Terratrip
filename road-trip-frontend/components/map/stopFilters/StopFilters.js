import { useAtom } from "jotai";
import React, { useMemo, useState } from "react";
import { advLevelAtom, filtersAtom } from "../../../utils/atoms";
import { filters, levelOptions } from "../../../utils/stops/filters";
import FilterItem from "./FilterItem";
import { formatTitle } from "../../../utils/stringUtils";
import Accordion from "../../accordion/Accordion";
import { Button, SmallButton } from "../../general/Buttons";

const StopFilterChecklist = () => {
    const [level] = useAtom(advLevelAtom);
    const [activeFilters, setActiveFilters] = useAtom(filtersAtom);
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
        <div className="w-full h-1/4 pr-2">
            <div className="overflow-y-scroll scrollbar overflow-x-hidden px-2 text-slate-800 mt-2 relative h-full">
                <div className="font-semibold mb-1 text-sm">Filters</div>

                {options.map((option) => (
                    <div key={option.category} className="text-sm my-1">
                        <Accordion
                            defaultToExpanded={false}
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
                                        onCheck={(isChecked) => {
                                            if (isChecked) {
                                                selectedFilters.add(
                                                    `${option.category}.${subcat.name}`
                                                );
                                                setSelectedFilters(
                                                    new Set([
                                                        ...selectedFilters,
                                                    ])
                                                );
                                            } else {
                                                selectedFilters.delete(
                                                    `${option.category}.${subcat.name}`
                                                );
                                                setSelectedFilters(
                                                    new Set([
                                                        ...selectedFilters,
                                                    ])
                                                );
                                            }
                                        }}
                                    ></FilterItem>
                                ))}
                            </div>
                        </Accordion>
                    </div>
                ))}
            </div>
            <div className="absolute bottom-2 right-6">
                <SmallButton
                    onClick={() => {
                        setActiveFilters([...selectedFilters]);
                    }}
                >
                    Apply
                </SmallButton>
            </div>
        </div>
    );
};

export default StopFilterChecklist;
