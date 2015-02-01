module.exports = function(grunt) {

    grunt.initConfig({
        jshint: {
            files: ['Gruntfile.js', 'src/main/webapp/resources-cwsfe-cms/js/cms/**/*.js'],
            options: {
                curly: true,
                eqeqeq: true,
                eqnull: true,
                browser: true,
                globals: {
                    jQuery: true
                },
                // our nice reporter
                reporter: require('jshint-stylish')
            }
        },
        watch: {
            options: {
                livereload: true
            },
            js: {
                files: ['src/main/webapp/resources-cwsfe-cms/js/cms/**/*.js'],
                tasks: ['jshint', 'uglify:production']
            },
            css: {
                files: ['src/main/webapp/resources-cwsfe-cms/css/**/*.css'],
                tasks: ['cssmin']
            }
        },
        uglify: {
            production: {
                files: [{
                    expand: true,
                    cwd: 'src/main/webapp/resources-cwsfe-cms/js',
                    src: '**/*.js',
                    dest: 'build/webapp/resources-cwsfe-cms/js'
                }]
            }
        },
        cssmin: {
            production: {
                expand: true,
                cwd: 'src/main/webapp/resources-cwsfe-cms/css',
                src: ['**/*.css'],
                dest: 'build/webapp/resources-cwsfe-cms/css'
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-cssmin');

    grunt.registerTask('default', [
        'jshint',
        'watch',
        'uglify',
        'cssmin'
    ]);

};
