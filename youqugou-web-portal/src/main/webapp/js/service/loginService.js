app.service("loginService",function($http){
	
	this.loginInfo = function(){
		return $http.get("../login/loginInfo.do");
	}
	
});