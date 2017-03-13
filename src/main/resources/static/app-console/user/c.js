
function userListController($log, $rootScope, $scope, sessionService, wydNotifyService, $http) {
    var cmpId = 'userListController', cmpName = 'Users';
    $log.debug(cmpId + ' started...');

    $rootScope.viewName = cmpName;

    var vm = this;
    vm.isReady = false;
    vm.isBlocked = false;

    var searchHolder = {};
    vm.searchHolder = searchHolder;

    function reload() {
        searchHolder.hasMoreItems = true;
        searchHolder.curPageNo = -1;
        searchHolder.items = [];
        loadMore();
    }

    vm.reload = reload;

    function loadMore() {
        searchHolder.curPageNo++;
        var path = '/console/users?';
        path += 'pageNo=' + searchHolder.curPageNo;
        $http.get(path).success(function (response) {
            $log.debug(response);
            if (response.data.length > 0) {
                _.forEach(response.data, function (item) {
                    searchHolder.items.push(item);
                });
            }
            else {
                searchHolder.hasMoreItems = false;
                var msg = 'No more users to load...';
                wydNotifyService.showWarning(msg);
            }
        });
    }

    vm.loadMore = loadMore;

    function init() {
        vm.isReady = true;
        reload();
    }

    init();

    $log.debug(cmpId + ' started...');
}
appControllers.controller('userListController', userListController);

