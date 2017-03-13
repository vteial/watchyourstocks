appServices.factory('wydNotifyService', function ($log) {

    return {

        showInfo: function (message, actionText) {
            // $log.info(message);
            Materialize.toast(message, 4000);
        },

        showSuccess: function (message) {
            // $log.info(message);
            Materialize.toast(message, 3000);
        },

        showWarning: function (message) {
            // $log.info(message);
            Materialize.toast(message, 2000);
        },

        showError: function (message, actionText) {
            // $log.info(message);
            Materialize.toast(message, 5000);
        },

        hide: function () {
        }

    };
});
