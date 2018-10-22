(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('VehicleDialogController', VehicleDialogController);

    VehicleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Vehicle'];

    function VehicleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Vehicle) {
        var vm = this;

        vm.vehicle = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vehicle.id !== null) {
                Vehicle.update(vm.vehicle, onSaveSuccess, onSaveError);
            } else {
                Vehicle.save(vm.vehicle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('truckwashApp:vehicleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
