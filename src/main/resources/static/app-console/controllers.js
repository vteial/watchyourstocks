function messageController($log, $rootScope, $scope, $location) {
    var cmpId = 'messageController', cmpName = 'Home';
    $log.debug(cmpId + ' started...');

    $rootScope.viewName = cmpName;

    var vm = this;
    vm.isReady = false;

    vm.params = $location.search();
    if (vm.params.errorMessage) {
        vm.hasErrorMessage = true;
    }
    else {
        vm.hasErrorMessage = false;
    }

    $log.debug(cmpId + ' finished...');
}
appControllers.controller('messageController', messageController);

