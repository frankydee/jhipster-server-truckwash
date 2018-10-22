(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('SessionDialogController', SessionDialogController);

    SessionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Session'];

    function SessionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Session) {
        var vm = this;

        vm.session = entity;
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
            if (vm.session.id !== null) {
                Session.update(vm.session, onSaveSuccess, onSaveError);
            } else {
                Session.save(vm.session, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('truckwashApp:sessionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startTime = false;
        vm.datePickerOpenStatus.endTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
