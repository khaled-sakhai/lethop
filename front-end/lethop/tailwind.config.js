/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
 
    // Or if using `src` directory:
    "./src/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    screens: {
      'sm': '640px',
      // => @media (min-width: 640px) { ... }

      'md': '768px',
      // => @media (min-width: 768px) { ... }

      'lg': '1024px',
      // => @media (min-width: 1024px) { ... }

      'xl': '1280px',
      // => @media (min-width: 1280px) { ... }

      '2xl': '1536px',
      // => @media (min-width: 1536px) { ... }
    },
    extend: {
      fontFamily: {
        custom: ['"Segoe UI Historic"', '"Segoe UI"', 'Helvetica', 'Arial', 'sans-serif'],
      },
      colors: {
        'white':'#FFFAF2',
        'white-2':'#FFFCF8',
        'white-3':'#FFFCFA',
        'dark':'#323232',
        'dark-2':'#4A4A4A',
        'green': '#758045',
        'orange':'#FF9525',
        'orange-1':'#FF8D17',
        'orange-2':'#FFFAF2',
        'orange-3':'#FFF0E0',
        'orange-4':'#CB9051',
        'orange-5':'#D8A977',
        'gray':'#B2B2B2',
        'gray-2':'#F0EEEA',
        'gray-3':'#F4F0EB',
        'blue':'#56A4FF',
        'pink':'#FD6B6A'
        

      },
    },
  },
  plugins: [],
}