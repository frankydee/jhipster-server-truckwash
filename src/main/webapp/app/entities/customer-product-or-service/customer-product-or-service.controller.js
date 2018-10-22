(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('CustomerProductOrServiceController', CustomerProductOrServiceController);

    CustomerProductOrServiceController.$inject = ['CustomerProductOrService'];

    function CustomerProductOrServiceController(CustomerProductOrService) {

        var vm = this;

        vm.customerProductOrServices = [];

        loadAll();

        function loadAll() {
            CustomerProductOrService.query(function(result) {
                vm.customerProductOrServices = result;
                vm.searchQuery = null;
            });
        }
    }
})();
