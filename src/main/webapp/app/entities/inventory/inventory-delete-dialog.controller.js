(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('InventoryDeleteController',InventoryDeleteController);

    InventoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Inventory'];

    function InventoryDeleteController($uibModalInstance, entity, Inventory) {
        var vm = this;

        vm.inventory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Inventory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
