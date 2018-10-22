(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('SessionLogController', SessionLogController);

    SessionLogController.$inject = ['SessionLog'];

    function SessionLogController(SessionLog) {

        var vm = this;

        vm.sessionLogs = [];

        loadAll();

        function loadAll() {
            SessionLog.query(function(result) {
                vm.sessionLogs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
