const gulp = require('gulp');
const sass = require('gulp-sass');
const cleanCSS = require('gulp-clean-css');
const plumber = require('gulp-plumber');
const rename = require('gulp-rename');
const autoprefixer = require('autoprefixer');
const postcss = require('gulp-postcss')


gulp.task('sass', (done) => {
    gulp.src('assets/stylesheets/common/index.scss')
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
        .pipe(gulp.dest('src/main/resources/static/css/common'))
    done();

    gulp.src('assets/stylesheets/page/*.scss')
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
        .pipe(gulp.dest('src/main/resources/static/css/page'));
    done();
});

gulp.task('default', () => {
    gulp.watch('assets/stylesheets/common/*.scss', gulp.task('sass'));
    gulp.watch('assets/stylesheets/page/*.scss', gulp.task('sass'));
});