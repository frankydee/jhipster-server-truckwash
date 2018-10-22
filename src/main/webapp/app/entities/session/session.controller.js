(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('SessionController', SessionController);

    SessionController.$inject = ['Session'];

    function SessionController(Session) {

        var vm = this;

        vm.sessions = [];

        loadAll();

        function loadAll() {
            Session.query(function(result) {
                vm.sessions = result;
                vm.searchQuery = null;
            });
        }
    }
})();
