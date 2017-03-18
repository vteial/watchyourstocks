function rateMonitorListController($log, $rootScope, $scope, wydNotifyService, sessionService, rateMonitors, $http, $interval) {
    var cmpId = 'rateMonitorListController', cmpName = 'Rate Monitor';
    $log.debug(cmpId + '...');
    $rootScope.viewName = cmpName;

    var vm = this, intervalId = 0;
    vm.uiState = {isReady: false};
    vm.intervalId = intervalId;

    $scope.items = rateMonitors;

    // vm.rateMonitorLabel = 'Start';
    //
    // vm.startOrStopRateMonitor = function () {
    //     if (intervalId == 0) {
    //         callRateMonitor();
    //         intervalId = $interval(callRateMonitor, (1000 * 120));
    //         vm.rateMonitorLabel = 'Stop';
    //     } else {
    //         $interval.cancel(intervalId);
    //         intervalId = 0;
    //         vm.rateMonitorLabel = 'Start';
    //     }
    //     vm.intervalId = intervalId;
    // };

    function callRateMonitor() {
        $log.debug('sessions/rate-monitor-now call started...');
        var path = 'sessions/rate-monitor-now';
        $http.get(path).success(function (response) {
            $log.debug(response);
            if (response.type != 0) {
                wydNotifyService.showError(response.message);
            }
        });
        $log.debug('sessions/rate-monitor-now call finished...');
    }

    vm.checkNow = callRateMonitor;

    vm.remove = function (model) {
        sweetAlert({
            title: 'Are you sure delete?',
            text: "You won't be able to revert this!",
            type: 'warning',
            showCancelButton: true
        }).then(function () {
            $scope.items.$remove(model).then(function (ref) {
                wydNotifyService.showSuccess(model.code + ' successfully deleted...');
            });
        }, function (dismiss) {
            $log.debug(model.code + ' delete request cancelled...');
        });
    };

}
appControllers.controller('rateMonitorListController', rateMonitorListController);

function rateMonitorAddOrEditController($log, $rootScope, $scope, wydNotifyService, wydFocusService, sessionService, rateMonitors, $routeParams, $http, $location) {
    var cmpId = 'rateMonitorAddOrEditController', cmpName = 'Add/Edit Rate Monitor';
    $log.debug(cmpId + '...');
    $rootScope.viewName = cmpName;

    var vm = this;
    vm.uiState = {isReady: false};

    vm.onLowerValue = function () {
        var value = vm.model.lowerValueS;
        if (value == '' || _.isUndefined(value)) {
            return;
        }
        if (!_.isNumber(value)) {
            value = value.split(',').join('')
            value = parseFloat(value);
        }
        vm.model.lowerValue = value;
    };

    vm.onUpperValue = function () {
        var value = vm.model.upperValueS;
        if (value == '' || _.isUndefined(value)) {
            return;
        }
        if (!_.isNumber(value)) {
            value = value.split(',').join('')
            value = parseFloat(value);
        }
        vm.model.upperValue = value;
    };

    vm.save = function () {
        if (!$scope.form0.$valid) {
            wydNotifyService.showError('Please fix the error fields...');

            var error = $scope.form0.rateMonitorCode.$error;
            if (Object.keys(error).length > 0) {
                wydFocusService('rateMonitorCode');
                return;
            }
            error = $scope.form0.rateMonitorName.$error;
            if (Object.keys(error).length > 0) {
                wydFocusService('rateMonitorName');
                return;
            }
            error = $scope.form0.rateMonitorLowerValue.$error;
            if (Object.keys(error).length > 0) {
                wydFocusService('rateMonitorLowerValue');
                return;
            }
            error = $scope.form0.rateMonitorUpperValue.$error;
            if (Object.keys(error).length > 0) {
                wydFocusService('rateMonitorUpperValue');
                return;
            }
            return;
        }
        $log.debug(vm.model);

        vm.uiState.isBlocked = true;
        if (vm.model.id == 0) {
            var path = 'sessions/next-auto-number/rateMonitorId';
            $http.get(path).success(function (response) {
                $log.debug(response);

                vm.model.id = response.data.value;
                var now = (new Date()).getTime();
                vm.model.createTime = now;
                vm.model.updateTime = now;
                $log.info(vm.model);

                var modelsRef = rateMonitors.$ref();
                modelsRef.child('' + vm.model.id).set(vm.model).then(function () {
                    wydNotifyService.showSuccess(vm.model.code + ' successfully added...');
                    $location.path('/rate-monitors');
                }).catch(function (error) {
                    wydNotifyService.showError(vm.model.code + ' update failed...');
                    $log.error(error);
                });

            });
        } else {
            var now = (new Date()).getTime();
            vm.model.updateTime = now;
            rateMonitors.$save(vm.model).then(function (ref) {
                $log.debug(ref.key + ' == ' + vm.model.$id)
                wydNotifyService.showSuccess(vm.model.code + ' successfully updated...');
                $location.path('/rate-monitors');
            }, function (error) {
                wydNotifyService.showError(vm.model.code + ' update failed...');
                $log.error(error);
            });
        }
    };

    vm.remove = function (model) {
        sweetAlert({
            title: 'Are you sure delete?',
            text: "You won't be able to revert this!",
            type: 'warning',
            showCancelButton: true
        }).then(function () {
            rateMonitors.$remove(model).then(function (ref) {
                wydNotifyService.showSuccess(model.code + ' successfully deleted...');
                $location.path('/rate-monitors');
            });
        }, function (dismiss) {
            $log.debug(model.code + ' delete request cancelled...');
        });
    };

    function init() {
        if ($routeParams.id) {
            vm.model = rateMonitors.$getRecord($routeParams.id);
            vm.model.lowerValueS = vm.model.lowerValue + '';
            vm.model.upperValueS = vm.model.upperValue + '';
            $rootScope.viewName = 'Edit Rate Monitor';
            wydFocusService('rateMonitorCode');
            vm.uiState.isReady = true;
        } else {
            $rootScope.viewName = 'Add Rate Monitor';
            vm.model = {id: 0, value: 0, status: 'no', metTime: null, branchId: 1, createBy: 1, updateBy: 1};
            wydFocusService('rateMonitorCode');
            vm.uiState.isReady = true;
        }
        $log.debug(vm.model);
    }

    init();
}
appControllers.controller('rateMonitorAddOrEditController', rateMonitorAddOrEditController);
