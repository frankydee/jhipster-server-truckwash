(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('session', {
            parent: 'entity',
            url: '/session',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'truckwashApp.session.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/session/sessions.html',
                    controller: 'SessionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('session');
                    $translatePartialLoader.addPart('sessionEndingCode');
                    $translatePartialLoader.addPart('sessionStatus');
                    $translatePartialLoader.addPart('loggingLevel');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('session-detail', {
            parent: 'session',
            url: '/session/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'truckwashApp.session.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/session/session-detail.html',
                    controller: 'SessionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('session');
                    $translatePartialLoader.addPart('sessionEndingCode');
                    $translatePartialLoader.addPart('sessionStatus');
                    $translatePartialLoader.addPart('loggingLevel');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Session', function($stateParams, Session) {
                    return Session.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'session',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('session-detail.edit', {
            parent: 'session-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/session/session-dialog.html',
                    controller: 'SessionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Session', function(Session) {
                            return Session.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('session.new', {
            parent: 'session',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/session/session-dialog.html',
                    controller: 'SessionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startTime: null,
                                endTime: null,
                                endingCode: null,
                                status: null,
                                loggingLevel: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('session', null, { reload: 'session' });
                }, function() {
                    $state.go('session');
                });
            }]
        })
        .state('session.edit', {
            parent: 'session',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/session/session-dialog.html',
                    controller: 'SessionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Session', function(Session) {
                            return Session.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('session', null, { reload: 'session' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('session.delete', {
            parent: 'session',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/session/session-delete-dialog.html',
                    controller: 'SessionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Session', function(Session) {
                            return Session.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('session', null, { reload: 'session' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
