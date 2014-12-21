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
            scripts: {
                files: ['src/main/webapp/resources-cwsfe-cms/js/cms/**/*.js'],
                tasks: ['jshint'],
                options: {
                    spawn: false
                }
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-watch');

    grunt.registerTask('default', [
        // execute jshint on file
        'jshint',
        'watch'
    ]);

};
