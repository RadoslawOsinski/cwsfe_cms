var baseConfig = require('./karma-conf.js');

module.exports = function (config) {
    // Load base config
    baseConfig(config);

    // Override base config
    config.set({
        singleRun: true,
        colors:    false,
        autoWatch: false,
        reporters: ['progress', 'junit', 'coverage'],
        preprocessors:    {
            'src/main/webapp/resources-cwsfe-cms/js/cms/**/*.js':   ['coverage']
        },
        browsers:  ['PhantomJS'],
        junitReporter: {
            outputFile: 'reports/junit/TESTS-xunit.xml'
        },
        coverageReporter: {
            type:   'lcov',
            dir:    'reports',
            subdir: 'coverage'
        }
    });
};