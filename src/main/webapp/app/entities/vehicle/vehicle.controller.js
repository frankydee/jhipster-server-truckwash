(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('VehicleController', VehicleController);

    VehicleController.$inject = ['Vehicle'];

    function VehicleController(Vehicle) {

        var vm = this;

        vm.vehicles = [];

        loadAll();

        function loadAll() {
            Vehicle.query(function(result) {
                vm.vehicles = result;
                vm.searchQuery = null;
            });
        }
    }
})();
