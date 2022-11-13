export const formatTitle = (name) => {
    const words = name.split("_");

    for (let i = 0; i < words.length; i++) {
        words[i] = words[i][0].toUpperCase() + words[i].substr(1);
    }

    return words.join(" ");
};

export const getStopOrderText = (stops, order) => {
    console.log(stops, order);
    const len = stops ? stops.length : 0;
    if (order == 0) {
        return "Start";
    } else if (order == len + 1) {
        return "End";
    }
    return order;
};
