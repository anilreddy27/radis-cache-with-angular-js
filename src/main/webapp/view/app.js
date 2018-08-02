var app = angular.module('app', ['ui.grid','ui.grid.pagination']);

app.controller('StudentCtrl', ['$scope','StudentService', function ($scope,StudentService) {

    $scope.data = {};
    $scope.dataContent = {"keyName":"123456","noOfObjects":"Random Number","noOfObjectInSize":"Random Size","responseTime":"Random Number"}
   var paginationOptions = {
     pageNumber: 1,
	 pageSize: 5,
	 sort: null
   };

   $scope.putInSession = function(){
        StudentService.putInSession($scope.data.objectCount,$scope.data.objectSize).success(function(data){
         $scope.data = {};
           $scope.getInSessionToLoad(data);

        });;
   };
   $scope.getInSession = function(){

        if($scope.data.sessionId == undefined)
        {
            $scope.isShowErrorMessage = true;
        }else{
        $scope.isShowErrorMessage = false;
        }
        if(!$scope.isShowErrorMessage)
        {
            StudentService.showSession($scope.data.sessionId).success(function(data){
                $scope.dataContent = data;
            });
        }

   };
    $scope.getInSessionToLoad = function(data){
       StudentService.showSession(data.keyName).success(function(data){
            $scope.dataContent = data;
            $scope.data.sessionId = $scope.dataContent.keyName;
       });
   };
   $scope.deleteSession = function(){
        if($scope.data.sessionId == undefined)
        {
            $scope.isShowErrorMessage = true;
        }else{
        $scope.isShowErrorMessage = false;
        }
        if(!$scope.isShowErrorMessage)
        {
         StudentService.deleteSession($scope.data.sessionId).success(function(data){
                $scope.getInSession($scope.data.sessionId);
           });
        }

   };



   $scope.gridOptions = {
    paginationPageSizes: [5, 10, 20],
    paginationPageSize: paginationOptions.pageSize,
    enableColumnMenus:false,
	useExternalPagination: true,
    columnDefs: [
      { name: 'id' },
      { name: 'name' },
      { name: 'gender' },
      { name: 'age' }
    ],
    onRegisterApi: function(gridApi) {
        $scope.gridApi = gridApi;
        gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
          paginationOptions.pageNumber = newPage;
          paginationOptions.pageSize = pageSize;
          StudentService.getStudents(newPage,pageSize).success(function(data){
        	  $scope.gridOptions.data = data.content;
         	  $scope.gridOptions.totalItems = data.totalElements;
          });
        });
     }
  };
  
}]);

app.service('StudentService',['$http', function ($http) {

    function putInSession(key,value) {
        return $http({
           url: '/add?key='+key+'&value='+value,
           method: "POST",
           headers: {
            'Content-type': 'application/json',
           }
           });
    }

    function showSession(key) {
        return  $http({
          method: 'GET',
          url: '/values?keyName='+key
        });
    }

    function deleteSession(key) {
        return  $http({
          method: 'POST',
          url: 'delete?keyName='+key
        });
    }

    return {
    	putInSession:putInSession,
    	showSession:showSession,
    	deleteSession:deleteSession
    };
	
}]);
