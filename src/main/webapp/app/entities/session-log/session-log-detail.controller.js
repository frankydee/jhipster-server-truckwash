(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('SessionLogDetailController', SessionLogDetailController);

    SessionLogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SessionLog'];

    function SessionLogDetailController($scope, $rootScope, $stateParams, previousState, entity, SessionLog) {
        var vm = this;

        vm.sessionLog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('truckwashApp:sessionLogUpdate', function(event, result) {
            vm.sessionLog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
