var gulp = require('gulp');
var gulpLoadPlugins = require('gulp-load-plugins');
var plugins = gulpLoadPlugins();
var minifyCSS = require('gulp-minify-css');

gulp.task('jsDevelopment', function () {
    return gulp.src('src/main/webapp/resources-cwsfe-cms/js/cms/**/*.js')
        .pipe(plugins.jshint())
        .pipe(plugins.jshint.reporter('default'))
        .pipe(gulp.dest('build/webapp/resources-cwsfe-cms/js'));
});

gulp.task('cssDevelopment', function () {
    return gulp.src('src/main/webapp/resources-cwsfe-cms/css/**/*.css')
        .pipe(gulp.dest('build/webapp/resources-cwsfe-cms/css'));
});

gulp.task('watch', function () {
    gulp.watch('src/main/webapp/resources-cwsfe-cms/js/cms/**/*.js', ['jsDevelopment']);
    gulp.watch('src/main/webapp/resources-cwsfe-cms/css/**/*.css', ['cssDevelopment']);
});

gulp.task('release', ['minifyJS', 'minCss']);

gulp.task('minifyJS', function () {
    var src = 'src/main/webapp/resources-cwsfe-cms/js/cms/**/*.js',
        dst = 'build/webapp/resources-cwsfe-cms/js';
        gulp.src(src)
            .pipe(plugins.changed(dst))
            .pipe(plugins.jshint())
            .pipe(plugins.jshint.reporter('default'))
            .pipe(plugins.uglify())
            .pipe(gulp.dest(dst));
        //.pipe(plugins.changed('build/webapp/resources-cwsfe-cms/js', {hasChanged: plugins.changed.compareSha1Digest}))
});

gulp.task('minCss', function () {
    return gulp.src('src/main/webapp/resources-cwsfe-cms/css/**/*.css')
        .pipe(minifyCSS())
        .pipe(gulp.dest('build/webapp/resources-cwsfe-cms/css'));
});
