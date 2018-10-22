(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('SoldItemController', SoldItemController);

    SoldItemController.$inject = ['SoldItem'];

    function SoldItemController(SoldItem) {

        var vm = this;

        vm.soldItems = [];

        loadAll();

        function loadAll() {
            SoldItem.query(function(result) {
                vm.soldItems = result;
                vm.searchQuery = null;
            });
        }
    }
})();
