import { Slider, Rail, Handles, Tracks } from "react-compound-slider";

const sliderStyle = {
    // Give the slider some width
    position: "relative",
    width: "200px",
    height: "content",
    border: "none",
};

const railStyle = {
    position: "absolute",
    width: "100%",
    height: 7,
    marginTop: 0,
    borderRadius: 5,
    backgroundColor: "#8B9CB6",
};

export function Handle({ handle: { id, value, percent }, getHandleProps }) {
    return (
        <div
            style={{
                left: `${percent}%`,
                position: "absolute",
                marginLeft: -10,
                marginTop: -7,
                zIndex: 2,
                width: 20,
                height: 20,
                border: 0,
                textAlign: "center",
                cursor: "pointer",
                borderRadius: "50%",
                backgroundColor: "white",
                color: "#333",
            }}
            {...getHandleProps(id)}
        >
            <div
                style={{
                    fontSize: 11,
                    marginTop: -20,
                    color: "gray",
                }}
            >
                {Number.isInteger(value) ? Math.round(value) : value.toFixed(1)}
            </div>
        </div>
    );
}

function Track({ source, target, getTrackProps, color }) {
    return (
        <div
            style={{
                position: "absolute",
                height: 7,
                zIndex: 1,
                marginTop: 0,
                backgroundColor: color ?? "#546C91",
                borderRadius: 5,
                cursor: "pointer",
                left: `${source.percent}%`,
                width: `${target.percent - source.percent}%`,
            }}
            {
                ...getTrackProps() /* this will set up events if you want it to be clickeable (optional) */
            }
        />
    );
}

const DualSlider = ({
    min,
    max,
    minVal,
    maxVal,
    step,
    onChange,
    color,
    ...props
}) => {
    return (
        <div className="relative flex items-center h-16">
            <Slider
                rootStyle={sliderStyle}
                domain={[min ?? 0, max ?? 100]}
                step={step ?? 1}
                mode={2 /* keep from crossing */}
                values={[minVal, maxVal] /* 2 values = 2 handles */}
                onChange={(val) => {
                    onChange(val);
                }}
            >
                <Rail>
                    {({ getRailProps }) => (
                        <div
                            style={props.railStyle ?? railStyle}
                            {...getRailProps()}
                        />
                    )}
                </Rail>
                <Handles>
                    {({ handles, getHandleProps }) => (
                        <div className="slider-handles">
                            {handles.map((handle) => (
                                <Handle
                                    key={handle.id}
                                    handle={handle}
                                    getHandleProps={getHandleProps}
                                />
                            ))}
                        </div>
                    )}
                </Handles>
                <Tracks left={false} right={false}>
                    {({ tracks, getTrackProps }) => (
                        <div className="slider-tracks">
                            {tracks.map(({ id, source, target }) => (
                                <Track
                                    key={id}
                                    source={source}
                                    target={target}
                                    color={color}
                                    getTrackProps={getTrackProps}
                                />
                            ))}
                        </div>
                    )}
                </Tracks>
            </Slider>
        </div>
    );
};

export default DualSlider;
