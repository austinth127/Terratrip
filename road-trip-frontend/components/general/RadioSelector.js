import React, { useState } from "react";
import { Button, darkOutlineBtnStyle, stdBtnStyle } from "./Buttons";
import ClientOnly from "./ClientOnly";

const RadioSelector = ({ options, active, setActive, ...props }) => {
    return (
        <ClientOnly>
            <div
                className={`gap-4 mt-4 grid grid-rows-2 lg:grid-rows-none lg:grid-cols-4 lg:w-3/4 ${props.className}`}
            >
                {options.map((option) => (
                    <Button
                        onClick={() => setActive(option)}
                        key={option}
                        className={
                            active != null &&
                            active.toLowerCase() == option.toLowerCase()
                                ? stdBtnStyle
                                : darkOutlineBtnStyle
                        }
                    >
                        {option}
                    </Button>
                ))}
            </div>
        </ClientOnly>
    );
};

export default RadioSelector;
