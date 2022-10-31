import axios from "axios";
import { useAtom } from "jotai";
import React, { useEffect, useState } from "react";
import { showNotifAtom } from "../../utils/atoms";
import LoadingSpinner from "../general/LoadingSpinner";
import NotificationCard from "./NotificationCard";

const NotificationOverlay = () => {
    const [notifications, setNotifications] = useState(null);
    const maxLen = 3;
    const [shortNotifs, setShortNotifs] = useState(null);
    const [showNotifs, setShowNotifs] = useAtom(showNotifAtom);

    useEffect(() => {
        /** @TODO better error handling */
        const getData = async () => {
            const res = await axios.get(`/notification`);
            notifications = res.data;
            setNotifications(notifications);
            setShortNotifs(
                notifications ? notifications.slice(0, maxLen) : null
            );
        };

        getData();
    }, []);

    if (!notifications || notifications.length < 1) {
        return <></>;
    }
    return (
        <div className="lg:flex hidden w-fit absolute top-4 right-4 flex-col gap-1">
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
                                axios.delete(
                                    `/notification/${notification.id}`
                                );
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
