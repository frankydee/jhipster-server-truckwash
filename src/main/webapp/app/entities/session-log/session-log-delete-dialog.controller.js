(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('SessionLogDeleteController',SessionLogDeleteController);

    SessionLogDeleteController.$inject = ['$uibModalInstance', 'entity', 'SessionLog'];

    function SessionLogDeleteController($uibModalInstance, entity, SessionLog) {
        var vm = this;

        vm.sessionLog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SessionLog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
