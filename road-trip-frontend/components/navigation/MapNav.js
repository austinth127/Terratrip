import Image from "next/image";
import { useRouter } from "next/router";
import React from "react";
import { Button, OutlineButton } from "../general/Buttons";
import TextLogo from "../general/TextLogo";

const MapNav = ({ ...props }) => {
    const router = useRouter();
    const handleSave = () => {};

    const handleCancel = () => {
        router.push("/");
    };

    return (
        <div
            className={`isolate z-50 h-12 px-4 
                        fixed top-0 left-0 w-full 
                        border-b border-gray-100 drop-shadow-lg border-opacity-80
                        flex flex-row items-center justify-between
                        bg-opacity-90 bg-gray-50
                        text-slate-800 ${props.className}`}
        >
            <div className="flex flex-row pb-1">
                <Image
                    width={32}
                    height={32}
                    layout="intrinsic"
                    src="/logos/mountain.svg"
                />
                <TextLogo className="text-lg text-slate-800 pt-2" />
            </div>
            <div className="flex flex-row justify-evenly gap-2">
                <Button onClick={handleSave}>Save</Button>
                <OutlineButton onClick={handleCancel}>Cancel</OutlineButton>
            </div>
        </div>
    );
};

export default MapNav;
