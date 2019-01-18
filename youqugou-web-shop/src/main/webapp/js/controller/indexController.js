app.controller("indexController",function($scope, loginService){
    $scope.searchEntity = {};//定义搜索对象

	$scope.loginInfo = function(){
		loginService.loginInfo().success(function(response){
			$scope.username = response.username;
			$scope.loginTime = response.loginTime;
		});
	}
	
});