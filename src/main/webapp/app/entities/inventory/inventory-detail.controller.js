(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('InventoryDetailController', InventoryDetailController);

    InventoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Inventory'];

    function InventoryDetailController($scope, $rootScope, $stateParams, previousState, entity, Inventory) {
        var vm = this;

        vm.inventory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('truckwashApp:inventoryUpdate', function(event, result) {
            vm.inventory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
