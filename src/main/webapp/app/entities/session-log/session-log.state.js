(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('session-log', {
            parent: 'entity',
            url: '/session-log',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'truckwashApp.sessionLog.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/session-log/session-logs.html',
                    controller: 'SessionLogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sessionLog');
                    $translatePartialLoader.addPart('logType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('session-log-detail', {
            parent: 'session-log',
            url: '/session-log/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'truckwashApp.sessionLog.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/session-log/session-log-detail.html',
                    controller: 'SessionLogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sessionLog');
                    $translatePartialLoader.addPart('logType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SessionLog', function($stateParams, SessionLog) {
                    return SessionLog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'session-log',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('session-log-detail.edit', {
            parent: 'session-log-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/session-log/session-log-dialog.html',
                    controller: 'SessionLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SessionLog', function(SessionLog) {
                            return SessionLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('session-log.new', {
            parent: 'session-log',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/session-log/session-log-dialog.html',
                    controller: 'SessionLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                logType: null,
                                createDate: null,
                                filePath: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('session-log', null, { reload: 'session-log' });
                }, function() {
                    $state.go('session-log');
                });
            }]
        })
        .state('session-log.edit', {
            parent: 'session-log',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/session-log/session-log-dialog.html',
                    controller: 'SessionLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SessionLog', function(SessionLog) {
                            return SessionLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('session-log', null, { reload: 'session-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('session-log.delete', {
            parent: 'session-log',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/session-log/session-log-delete-dialog.html',
                    controller: 'SessionLogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SessionLog', function(SessionLog) {
                            return SessionLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('session-log', null, { reload: 'session-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
