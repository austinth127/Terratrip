import { useAtom } from "jotai";
import React, { useEffect, useMemo, useState } from "react";
import { advLevelAtom, filtersAtom, rangeAtom } from "../../../utils/atoms";
import { levelOptions } from "../../../utils/stops/filters";
import FilterItem from "./FilterItem";
import Accordion from "../../accordion/Accordion";
import { Button, SmallButton } from "../../general/Buttons";
import { SingleSlider } from "../../general/CompundSlider";
import { colors } from "../../../utils/colors";
import axios from "axios";

const StopFilterChecklist = () => {
    const [level] = useAtom(advLevelAtom);
    const [activeFilters, setActiveFilters] = useAtom(filtersAtom);
    const [range, setRange] = useAtom(rangeAtom);
    const [filters, setFilters] = useState();

    useEffect(() => {
        const getData = async () => {
            const levelNum = levelOptions.findIndex(
                (name) => name.toLowerCase() == level.toLowerCase()
            );
            const res = await axios.get(`/api/category/${levelNum}`);
            console.log(res.data);
            setFilters(res.data);
        };
        getData();
    }, []);

    const [selectedFilters, setSelectedFilters] = useState(new Set());

    if (!filters) {
        return <></>;
    }

    return (
        <div className="w-full h-1/3 pr-2">
            <div className="overflow-y-scroll scrollbar overflow-x-hidden px-2 text-slate-800 mt-2 relative h-full">
                <div className="font-semibold mb-1 text-sm">Filters</div>

                {filters.map((option) => (
                    <div key={option.category} className="text-sm my-1">
                        <Accordion
                            defaultToExpanded={false}
                            header={
                                <h2 className="uppercase font-light">
                                    {option.category}
                                </h2>
                            }
                        >
                            <div className="pl-1 py-2">
                                {option.subcategories.map((subcat) => (
                                    <FilterItem
                                        key={subcat + option.category}
                                        filter={subcat}
                                        onCheck={(isChecked) => {
                                            if (isChecked) {
                                                selectedFilters.add(
                                                    `${option.category}.${subcat}`
                                                );
                                                setSelectedFilters(
                                                    new Set([
                                                        ...selectedFilters,
                                                    ])
                                                );
                                            } else {
                                                selectedFilters.delete(
                                                    `${option.category}.${subcat}`
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
            <div className="absolute bottom-2 right-2">
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
