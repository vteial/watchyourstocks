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
dependents.push('app.filters');
dependents.push('app.directives');
dependents.push('app.services');
dependents.push('app.controllers');
var app = angular.module('app', dependents), lodash = _;

// app.config(function ($httpProvider) {
//     $httpProvider.interceptors.push('generalHttpInterceptor');
// });

function appConfig($routeProvider, $locationProvider) {
    $routeProvider.when('/', {
        redirectTo: '/home'
    });
    $routeProvider.when('/home', {
        templateUrl: 'app-public/home/t.html',
        controller: 'homeController as vm'
    });
    $routeProvider.when('/sign-in', {
        templateUrl: 'app-public/home/t-signIn.html',
        controller: 'signInController as vm'
    });
    $routeProvider.when('/not-found', {
        templateUrl: 'app-public/zgeneral/t-notFound.html'
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
