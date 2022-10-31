import { useAtom } from "jotai";
import React from "react";
import { notificationAtom } from "../../utils/atoms";

const NotificationCard = ({ notification, removeNotif }) => {
    return (
        <div className=" bg-slate-900 bg-opacity-70 p-4 h-fit text-gray-100 rounded-lg relative text-sm lg:w-80 w-48 flex flex-col gap-2 isolate z-40">
            <button
                className="absolute top-2 right-4 w-fit h-fit "
                onClick={removeNotif}
            >
                <i className="fa fa-x fa-solid text-gray-400 fa-xs"></i>
            </button>
            <h3 className="text-green-600 pr-6">{notification.title}</h3>
            <p>{notification.body}</p>
            <p className="text-gray-400">{notification.time}</p>
        </div>
    );
};

export default NotificationCard;
