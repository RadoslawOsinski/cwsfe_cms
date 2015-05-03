var gulp = require('gulp');
var gulpLoadPlugins = require('gulp-load-plugins');
var plugins = gulpLoadPlugins();
var minifyCSS = require('gulp-minify-css');
//var mocha  = require('gulp-mocha');
//var mochaPhantomJS = require('gulp-mocha-phantomjs');

gulp.task('jsDevelopment', function () {
    //copy all js files
    gulp.src('src/main/webapp/resources-cwsfe-cms/js/**/*.js')
        .pipe(gulp.dest('build/webapp/resources-cwsfe-cms/js'));
    //count jsHints only on project sources
    return gulp.src('src/main/webapp/resources-cwsfe-cms/js/cms/**/*.js')
        .pipe(plugins.jshint())
        .pipe(plugins.jshint.reporter('default'));
});

gulp.task('cssDevelopment', function () {
    return gulp.src('src/main/webapp/resources-cwsfe-cms/css/**/*.css')
        .pipe(gulp.dest('build/webapp/resources-cwsfe-cms/css'));
});

gulp.task('watch', function () {
    gulp.watch('src/main/webapp/resources-cwsfe-cms/js/cms/**/*.js', ['jsDevelopment']);
    gulp.watch('src/main/webapp/resources-cwsfe-cms/css/**/*.css', ['cssDevelopment']);
});

//gulp.task('test', function() {
//    return gulp
//        .src('test/js/cms/*.js')
//        .pipe(mochaPhantomJS({reporter: 'spec', dump:'test.log'}));
//});

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
