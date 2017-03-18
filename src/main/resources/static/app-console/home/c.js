function homeController($log, $rootScope, $scope, wydNotifyService) {
    var cmpId = 'homeController', cmpName = 'Home';
    $log.debug(cmpId + ' started...');

    $rootScope.viewName = cmpName;

    var vm = this;
    vm.isReady = false;

    $log.debug(cmpId + ' finished...');
}
appControllers.controller('homeController', homeController);

function settingsController($log, $rootScope, $scope, sessionService, wydNotifyService, $http) {
    var cmpId = 'settingsController', cmpName = 'Settings';
    $log.debug(cmpId + ' started...');

    $rootScope.viewName = cmpName;

    var vm = this;
    vm.isReady = false;
    vm.isBlocked = false;

/*
    vm.saveApplicationName = function () {
        if (sessionService.context.applicationName == vm.applicationName) {
            wydNotifyService.showInfo('There is no change. Nothing to save.');
            return
        }

        var reqModel = {id: 'applicationName', value: vm.applicationName};

        vm.isBlocked = true;
        var path = '/console/app-configs/app-config'
        $http.post(path, reqModel).success(function (response) {
            $log.debug(response);
            if (response.type === 0) {
                sessionService.context.applicationName = vm.applicationName;
                wydNotifyService.showSuccess('Application name saved successfully...');
            }
            else {
                wydNotifyService.showError(response.message);
            }
            vm.isBlocked = false;
        });
    };

    vm.saveIsWhiteLabeled = function () {
        if (sessionService.context.isWhiteLabeled == vm.isWhiteLabeled) {
            wydNotifyService.showInfo('There is no change. Nothing to save.');
            return
        }

        var reqModel = {id: 'isWhiteLabeled', value: vm.isWhiteLabeled};

        vm.isBlocked = true;
        var path = '/console/app-configs/app-config'
        $http.post(path, reqModel).success(function (response) {
            $log.debug(response);
            if (response.type === 0) {
                sessionService.context.isWhiteLabeled = vm.isWhiteLabeled;
                wydNotifyService.showSuccess('Is White Labeled saved successfully...');
            }
            else {
                wydNotifyService.showError(response.message);
            }
            vm.isBlocked = false;
        });
    };

    $scope.$on('session:properties', function (event, data) {
        init();
    });

    function init() {
        vm.applicationName = sessionService.context.applicationName;
        vm.isWhiteLabeled = sessionService.context.isWhiteLabeled;

        vm.isReady = true;
    }

    if (sessionService.context.applicationName) {
        init();
    } else {
        sessionService.properties();
    }
*/
    $log.debug(cmpId + ' started...');
}
appControllers.controller('settingsController', settingsController);

function signOutController($rootScope, $log, $http, $window, $timeout) {
    var cmpId = 'signOutController', cmpName = 'Sign Out';
    $log.debug(cmpId + ' started...');

    $rootScope.viewName = cmpName;

    var vm = this;
    vm.isReady = false;
    vm.isBlocked = false;

    $timeout(function() {
        $window.location = 'index.html';
    }, 1000);

    // var path = 'sessions/sign-out';
    // $http.get(path).success(function (response) {
    //     $window.location = 'index.html';
    //     // $log.info(response);
    // }).error(function () {
    //     $window.location = 'index.html';
    // });

    $log.debug(cmpId + ' finished...');
}
appControllers.controller('signOutController', signOutController);