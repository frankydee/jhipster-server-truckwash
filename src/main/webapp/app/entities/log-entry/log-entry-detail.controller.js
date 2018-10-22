(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('LogEntryDetailController', LogEntryDetailController);

    LogEntryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LogEntry'];

    function LogEntryDetailController($scope, $rootScope, $stateParams, previousState, entity, LogEntry) {
        var vm = this;

        vm.logEntry = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('truckwashApp:logEntryUpdate', function(event, result) {
            vm.logEntry = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
