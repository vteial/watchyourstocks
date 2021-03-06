appFilters.filter('orderObjectBy', function(){
    return function(input, attribute) {
        if (!angular.isObject(input)) return input;

        var array = [];
        for(var objectKey in input) {
            array.push(input[objectKey]);
        }

        function compare(a,b) {
            if (a[attribute] < b[attribute])
                return -1;
            if (a[attribute] > b[attribute])
                return 1;
            return 0;
        }

        array.sort(compare);
        return array;
    }
});

function rootController($log, $rootScope, $scope, sessionService, $window) {
    var cmpId = 'rootController', cmpName = '-';
    $log.debug(cmpId + ' started...');

    $rootScope.viewName = cmpName;

    $scope.lodash = _;

    sessionService.properties();
    var sessionS = sessionService;
    $scope.sessionS = sessionS;

    $scope.$on('session:properties', function (event, data) {
        $log.debug(cmpId + ' on ' + event.name + ' started...');
        $log.debug(cmpId + ' on ' + event.name + ' finished...');
    });

    $rootScope.$on('session:invalid', function (event, data) {
        $log.debug('session invalid started...');
        sweetAlert({
            title: 'Invalid Session!',
            text: "Your session is invalid. Please sign in and continue.",
            type: 'error'
        }).then(function () {
            $log.info('redirecting to sign in...');
            $window.location = 'index.html#/sign-in';
        });
        $log.debug('session invalid finished...');
    });

    $scope.historyBack = function () {
        $window.history.back();
    };

    $log.debug(cmpId + ' finished...');
}
appControllers.controller('rootController', rootController);

var dependents = ['ngRoute', 'ngSanitize', 'ngMessages'];
dependents.push('ngStorage');
dependents.push('ngclipboard');
dependents.push('green.inputmask4angular');
dependents.push('ngNotify');
dependents.push('firebase');
dependents.push('app.filters');
dependents.push('app.directives');
dependents.push('app.services');
dependents.push('app.controllers');
var app = angular.module('app', dependents), lodash = _, jquery = $;

app.config(function ($httpProvider) {
    $httpProvider.interceptors.push('generalHttpInterceptor');
});

function appConfig($routeProvider, $locationProvider) {
    $routeProvider.when('/', {
        redirectTo: '/home'
    });
    $routeProvider.when('/home', {
        templateUrl: 'app-console/home/t.html',
        controller: 'homeController as vm'
    });
    $routeProvider.when('/rate-monitors', {
        templateUrl: 'app-console/rateMonitor/t.html',
        controller: 'rateMonitorListController as vm'
    });
    $routeProvider.when('/rate-monitors/rate-monitor', {
        templateUrl: 'app-console/rateMonitor/t-createOrEdit.html',
        controller: 'rateMonitorAddOrEditController as vm'
    });
    $routeProvider.when('/rate-monitors/rate-monitor/:id', {
        templateUrl: 'app-console/rateMonitor/t-createOrEdit.html',
        controller: 'rateMonitorAddOrEditController as vm'
    });
    $routeProvider.when('/settings', {
        templateUrl: 'app-console/home/t-settings.html',
        controller: 'settingsController as vm'
    });
    $routeProvider.when('/sign-out', {
        templateUrl: 'app-console/home/t-signOut.html',
        controller: 'signOutController as vm'
    });
    $routeProvider.when('/not-found', {
        templateUrl: 'app-console/zgeneral/t-notFound.html'
    });
    $routeProvider.otherwise({
        redirectTo: '/not-found'
    });
};
app.config(appConfig);

function appInit($log, $rootScope, $location, $sessionStorage) {
    $log.info('initialization started...');

    $log.info('initialization finished...');
}
app.run(['$log', '$rootScope', '$location', '$sessionStorage', appInit]);
