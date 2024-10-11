/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/main/resources/templates/**/*.html', // Thymeleaf HTML templates
    './src/main/resources/static/js/**/*.js',    // JavaScript files
    './src/main/resources/static/css/**/*.css'   // CSS files
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}

