(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('InventoryDialogController', InventoryDialogController);

    InventoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Inventory'];

    function InventoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Inventory) {
        var vm = this;

        vm.inventory = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.inventory.id !== null) {
                Inventory.update(vm.inventory, onSaveSuccess, onSaveError);
            } else {
                Inventory.save(vm.inventory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('truckwashApp:inventoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.readingTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
