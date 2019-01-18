app.controller("indexController",function($scope,loginService){
	
	$scope.loginInfo = function(){
		loginService.loginInfo().success(function(response){
			$scope.username = response.username;
			$scope.loginTime = response.loginTime;
		});
	}
	
});