/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./pages/**/*.{js,ts,jsx,tsx}",
        "./components/**/*.{js,ts,jsx,tsx}",
    ],
    theme: {
        extend: {
            backgroundImage: {
                "mountain-sun": "url('/photos/pexels-sagui-andrea.jpg')",
            },
        },
    },
    plugins: [],
};
