(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('InventoryController', InventoryController);

    InventoryController.$inject = ['Inventory'];

    function InventoryController(Inventory) {

        var vm = this;

        vm.inventories = [];

        loadAll();

        function loadAll() {
            Inventory.query(function(result) {
                vm.inventories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
