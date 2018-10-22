(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('SoldItemDeleteController',SoldItemDeleteController);

    SoldItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'SoldItem'];

    function SoldItemDeleteController($uibModalInstance, entity, SoldItem) {
        var vm = this;

        vm.soldItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SoldItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
