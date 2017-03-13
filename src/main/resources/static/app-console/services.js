appServices.factory('rateMonitors', ['$firebaseArray',
    function ($firebaseArray) {
        var ref = firebase.database().ref();
        ref = ref.child('rateMonitors');
        return $firebaseArray(ref);
    }
]);