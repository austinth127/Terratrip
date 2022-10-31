import { useAtom } from "jotai";
import React, { useEffect, useState } from "react";
import { notificationAtom, showNotifAtom } from "../../utils/atoms";
import NotificationCard from "./NotificationCard";

const NotificationOverlay = () => {
    const [notifications, setNotifications] = useAtom(notificationAtom);
    const maxLen = 3;
    const [shortNotifs, setShortNotifs] = useState(
        notifications.slice(0, maxLen)
    );
    const [showNotifs, setShowNotifs] = useAtom(showNotifAtom);

    useEffect(() => {
        setShortNotifs(notifications.slice(0, maxLen));
    }, [notifications]);

    if (notifications.length < 1) {
        return <></>;
    }
    return (
        <div className="lg:flex hidden w-fit absolute top-2 right-2 flex-col gap-1">
            <div className="flex flex-row justify-end">
                <button
                    className="w-fit h-fit text-gray-100 text-xs p-2 text-center bg-slate-900 bg-opacity-70 rounded-lg z-40 isolate"
                    onClick={() => setShowNotifs(!showNotifs)}
                >
                    {showNotifs ? (
                        <p>Hide Notifications</p>
                    ) : (
                        <div>
                            <span className="pr-2 text-green-600">
                                {notifications.length}
                            </span>
                            <i className="fa fa-bell fa-solid fa-bounce"></i>
                        </div>
                    )}
                </button>
            </div>
            {showNotifs ? (
                <>
                    {shortNotifs.map((notification, index) => (
                        <NotificationCard
                            notification={notification}
                            index={index}
                            key={notification.id}
                            removeNotif={() => {
                                const toRemove = notifications.findIndex(
                                    (item) => item.id == notification.id
                                );
                                notifications.splice(toRemove, 1);
                                setNotifications([...notifications]);
                            }}
                        ></NotificationCard>
                    ))}
                    {notifications.length > maxLen ? (
                        <div className="w-full h-fit text-gray-100 text-sm p-3 bg-slate-900 bg-opacity-70 rounded-lg z-40 isolate">
                            {notifications.length - maxLen} more
                            notifications...
                        </div>
                    ) : (
                        <></>
                    )}
                </>
            ) : (
                <></>
            )}
        </div>
    );
};

export default NotificationOverlay;
