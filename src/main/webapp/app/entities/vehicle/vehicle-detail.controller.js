(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('VehicleDetailController', VehicleDetailController);

    VehicleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vehicle'];

    function VehicleDetailController($scope, $rootScope, $stateParams, previousState, entity, Vehicle) {
        var vm = this;

        vm.vehicle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('truckwashApp:vehicleUpdate', function(event, result) {
            vm.vehicle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
