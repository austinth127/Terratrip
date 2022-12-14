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
                "mountain-logo": "url('/logos/mountain.svg')",
                "wireframe-terrain": "url('/photos/wireframe_terrain.jpg')",
            },
        },
    },
    plugins: [require("@tailwindcss/line-clamp")],
};
