function sessionService($log, $http, $q, $rootScope, wydNotifyService, $firebaseArray) {
    var basePathS = 'sessions', basePathC = 'console';

    var service = {
        context: {}
    };

    service.context.applicationName = 'Watch Your Stocks';
    service.context.applicationDescription = 'Stock Management Application';

    function processProps(props) {
        $log.debug('processing session properties started...');
        _.assign(service.context, props);
        if (props.sessionDto) {
            $rootScope.xUserId = props.sessionDto.userId;
            // $log.info('Session User Id = ' + $rootScope.xUserId);
        }
        $log.debug('processing session properties finished...');
    }

    service.properties = function () {
        // var path = basePathS + '/properties';
        //
        // var deferred = $q.defer();
        // $http.get(path).success(function (response) {
        //     //$log.debug(response);
        //     if (response.type === 0) {
        //         $log.debug(response.data);
        //         processProps(response.data);
        //         $rootScope.$broadcast('session:properties', 'Session properties updated...');
        //         deferred.resolve(response);
        //     }
        // }).error(function () {
        //     deferred.reject("unable fetch properties...");
        // });
        //
        // return deferred.promise;
    };

    function addOrUpdateCacheY(propName, objectx) {
        var objectsLst = service[propName];
        var objectsMap = service[propName + 'Map'];
        var object = objectsMap[objectx.id];
        if (object) {
            _.assign(object, objectx);
        } else {
            objectsLst.push(objectx);
            objectsMap[objectx.id] = objectx;
        }
    }

    // var rateMonitorsRef = null, rateMonitors = null;
    //
    // service.rateMonitorsRef = function() {
    //     if(rateMonitorsRef == null) {
    //         var ref = firebase.database().ref();
    //         rateMonitorsRef = ref.child('rateMonitorsDev');
    //     }
    //     return rateMonitorsRef;
    // };
    //
    // service.rateMonitors = function() {
    //     if(rateMonitors == null) {
    //         var ref = service.rateMonitorsRef();
    //         rateMonitors = $firebaseArray(ref);
    //     }
    //     return rateMonitors;
    // };

    return service;
}
appServices.factory('sessionService', sessionService);