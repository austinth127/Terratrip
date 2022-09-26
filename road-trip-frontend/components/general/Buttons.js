export const Button = ({ onClick, ...props }) => {
    return (
        <button
            type="button"
            className={`px-6 py-2.5 bg-green-600 text-white font-medium text-xs leading-tight 
                        uppercase rounded shadow-md hover:bg-green-700 hover:shadow-lg focus:bg-green-700 
                        focus:shadow-lg focus:outline-none focus:ring-0 active:bg-green-800 active:shadow-lg 
                        transition duration-150 ease-in-out ${props.className}`}
            onClick={onClick}
        >
            {props.children}
        </button>
    );
};