module.exports = function(config){
  config.set({

    basePath : './',

    files : [
        'main/webapp/bower_components/angular/angular.js',
        'main/webapp/bower_components/angular-route/angular-route.js',
        'main/webapp/bower_components/angular-mocks/angular-mocks.js',

        'main/webapp/components/**/*.js',
        'main/webapp/modules/**/*.js',

        // include unit test specs
        'test/js/*.js',

        // include functional test specs
        'functionalTest/js/*.js'
    ],

    // files to exclude
    exclude : [
        'app/lib/angular/angular-loader.js',
        'app/lib/angular/*.min.js',
        'app/lib/angular/angular-scenario.js'
    ],

    autoWatch : true,

    frameworks: ['jasmine'],

    browsers : ['Chrome', 'Firefox', 'PhantomJS'],

    // progress is the default reporter
    reporters: ['progress'],

    // map of preprocessors that is used mostly for plugins
    preprocessors: {
    },

    plugins : [
        'karma-jasmine',
        'karma-chrome-launcher',
        'karma-firefox-launcher',
        'karma-junit-reporter',
        'karma-phantomjs-launcher'
    ]

  });
};
