(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('CustomerProductOrServiceDeleteController',CustomerProductOrServiceDeleteController);

    CustomerProductOrServiceDeleteController.$inject = ['$uibModalInstance', 'entity', 'CustomerProductOrService'];

    function CustomerProductOrServiceDeleteController($uibModalInstance, entity, CustomerProductOrService) {
        var vm = this;

        vm.customerProductOrService = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CustomerProductOrService.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
