const gulp = require('gulp');
const sass = require('gulp-sass');
const cleanCSS = require('gulp-clean-css');
const plumber = require('gulp-plumber');
const rename = require('gulp-rename');
const autoprefixer = require('autoprefixer');
const postcss = require('gulp-postcss')


gulp.task('sass', (done) => {
    gulp.src('assets/stylesheets/*.scss')
        .pipe(plumber())
        .pipe(postcss([
            autoprefixer()
        ]))
        .pipe(sass({
            outputStyle: 'compressed'
        }))
        .pipe(cleanCSS())
        .pipe(rename({
            extname: '.min.css'
        }))
        .pipe(gulp.dest('src/main/resources/static/css'))
    done();
});

gulp.task('default', () => {
    gulp.watch('assets/stylesheets/*.scss', gulp.task('sass'));
});