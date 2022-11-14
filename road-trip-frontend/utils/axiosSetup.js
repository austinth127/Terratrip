import axios from "axios";
import Logger from "js-logger";
import Userfront from "@userfront/core";

const controller = new AbortController();

axios.interceptors.response.use(
    function (response) {
        try {
            Logger.info(
                `${response.config.method.toLocaleUpperCase()} from ${
                    response.config.url
                }`
            );
            Logger.debug(response.data);
        } catch (e) {
            Logger.warn(
                "Axios response successful, but there was an issue in axios interceptor"
            );
        }
        return response;
    },
    function (error) {
        try {
            Logger.error(error.message);
        } catch (e) {
            Logger.warn("There was an issue in the axios interceptor:", e);
        }

        return Promise.reject(error);
    }
);

axios.interceptors.request.use(
    function (request) {
        if (!request.baseURL) {
            throw new axios.Cancel("No baseURL Set");
        }
        try {
            Logger.info(
                `Sent ${request.method.toLocaleUpperCase()} to ${request.url}`
            );
        } catch (e) {
            Logger.warn(
                "Axios request successful, but there was an issue in axios request interceptor"
            );
        }
        return request;
    },
    function (error) {
        try {
            Logger.error(error);
        } catch (e) {
            Logger.warn(
                "There was an issue in the axios request interceptor:",
                e
            );
        }

        return Promise.reject(error);
    }
);

export const setupAxios = () => {
    axios.defaults.baseURL = process.env.NEXT_PUBLIC_BACKEND_URL;
    console.log(process.env.NEXT_PUBLIC_BACKEND_URL);
    axios.defaults.headers.common["Content-Type"] = "application/json";
    if (Userfront.tokens.accessToken) {
        axios.defaults.headers.common[
            "Authorization"
        ] = `Bearer ${Userfront.tokens.accessToken}`;
    }
};

export const setupLogger = () => {
    Logger.useDefaults({
        defaultLevel: Logger.DEBUG,
        formatter: function (messages, context) {
            messages.unshift(`color : ${colorLog(context.level)}`);
            messages.unshift(`%c[${context.level.name}]: `);
        },
    });

    // LOW -> HIGH: TRACE, DEBUG, INFO, TIME, WARN, ERROR, OFF
    Logger.setLevel(Logger.TRACE);

    function colorLog(level) {
        let color;
        switch (level) {
            case Logger.DEBUG:
                color = "Green";
                break;
            case Logger.INFO:
                color = "DodgerBlue";
                break;
            case Logger.ERROR:
                color = "Red";
                break;
            case Logger.WARN:
                color = "Orange";
                break;
            default:
                color = null;
        }
        return color;
    }
};
