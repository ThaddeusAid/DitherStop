/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

// Declare app level module which depends on filters, and services
var app = angular.module('ditherstop', [
  'ngRoute'
])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider
    .when('/', {
      controller: 'mainCtrl',
      templateUrl: 'templates/main.html'
    })
  .otherwise({ redirectTo: '/' });
}]);

app.controller('mainCtrl', function($scope) {
  $scope.name = 'Thad';
});
