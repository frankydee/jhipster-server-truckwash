(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('LogEntryController', LogEntryController);

    LogEntryController.$inject = ['LogEntry'];

    function LogEntryController(LogEntry) {

        var vm = this;

        vm.logEntries = [];

        loadAll();

        function loadAll() {
            LogEntry.query(function(result) {
                vm.logEntries = result;
                vm.searchQuery = null;
            });
        }
    }
})();
