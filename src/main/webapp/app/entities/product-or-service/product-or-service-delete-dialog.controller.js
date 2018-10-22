(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('ProductOrServiceDeleteController',ProductOrServiceDeleteController);

    ProductOrServiceDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProductOrService'];

    function ProductOrServiceDeleteController($uibModalInstance, entity, ProductOrService) {
        var vm = this;

        vm.productOrService = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProductOrService.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
