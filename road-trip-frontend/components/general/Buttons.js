export const Button = ({ ...props }) => {
    return (
        <button
            type="button"
            className={`px-6 py-2.5 bg-green-600 text-white font-medium text-xs leading-tight 
                        uppercase rounded shadow-md hover:bg-green-700 hover:shadow-lg focus:bg-green-700 
                        focus:shadow-lg focus:outline-none focus:ring-0 active:bg-green-800 active:shadow-lg 
                        transition duration-150 ease-in-out ${props.className}`}
            {...props}
        >
            {props.children}
        </button>
    );
};

export const OutlineButton = ({ ...props }) => {
    return (
        <button
            type="button"
            className={`px-6 py-2.5 border-green-600 text-green-600 font-medium text-xs leading-tight 
                    uppercase rounded shadow-md hover:bg-gray-100 hover:shadow-lg focus:bg-gray-100 
                    focus:shadow-lg focus:ring-0 active:bg-gray-200 active:shadow-lg 
                    transition duration-150 ease-in-out bg-gray-50 border
                     ${props.className}`}
            {...props}
        >
            {props.children}
        </button>
    );
};

export const DarkOutlineButton = ({ ...props }) => {
    return (
        <button
            type="button"
            className={`px-6 py-2.5 border-green-600 text-white font-medium text-xs leading-tight 
                    uppercase rounded shadow-md hover:bg-gray-800 hover:shadow-lg focus:bg-gray-800 
                    focus:shadow-lg focus:ring-0 active:bg-gray-700 active:shadow-lg 
                    transition duration-150 ease-in-out bg-gray-900 border bg-opacity-50
                     ${props.className}`}
            {...props}
        >
            {props.children}
        </button>
    );
};
