function homeController($log, $rootScope, $scope, wydNotifyService) {
    var cmpId = 'homeController', cmpName = 'Home';
    $log.debug(cmpId + ' started...');

    $rootScope.viewName = cmpName;

    var vm = this;
    vm.isReady = false;

    $log.debug(cmpId + ' finished...');
}
appControllers.controller('homeController', homeController);

function signInController($log, $rootScope, $scope, wydNotifyService, wydFocusService, $http, $window, $timeout) {
    var cmpId = 'signInController', cmpName = 'Sign In';
    $log.debug(cmpId + ' started...');

    $rootScope.viewName = cmpName;

    var vm = this;
    vm.isReady = false;
    vm.isBlocked = false;

    // vm.signIn = function () {
    //     $timeout(function() {
    //         $window.location = '/home.html';
    //     }, 1000);
    // };

    vm.signIn = function () {
        wydNotifyService.hide();

        vm.message = null;

        vm.isBlocked = true;
        var path = 'sessions/sign-in';
        $http.post(path, vm.user).success(function (response) {
            vm.isBlocked = false;
            $log.info(response);
            if (response.type === 0) {
                if (response.data) {
                    $window.location = 'home.html';
                }
                else {
                    $window.location = 'home.html';
                }
            } else {
                vm.user.password = '';
                vm.message = response.message;
                wydNotifyService.showError(vm.message);
                wydFocusService('signInUserUserId');
            }
        });
    };

    // vm.processKeyUp = function (event) {
    //     if (event.keyCode === 13) {
    //         vm.signIn();
    //     }
    // };

    function init() {
        vm.message = null

        vm.user = {
            userId: '',
            password: ''
        };

        wydFocusService('signInUserUserId');

        vm.isReady = true;
    }

    init();

    $log.debug(cmpId + ' finished...');
}
appControllers.controller('signInController', signInController);