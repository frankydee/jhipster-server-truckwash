(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('ProductOrServiceController', ProductOrServiceController);

    ProductOrServiceController.$inject = ['ProductOrService'];

    function ProductOrServiceController(ProductOrService) {

        var vm = this;

        vm.productOrServices = [];

        loadAll();

        function loadAll() {
            ProductOrService.query(function(result) {
                vm.productOrServices = result;
                vm.searchQuery = null;
            });
        }
    }
})();
