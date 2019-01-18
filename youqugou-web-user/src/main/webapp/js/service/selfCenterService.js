app.service("selfCenterService", function ($http) {

    /**
     * 马超  2019.1.2
     * @param entity
     * @returns {*|void}
     */
    this.addselfCenter = function (entity) {
        return $http.post("../self/selfCenter.do",entity)
    }

    this.loginInfo=function () {
        return $http.post("../self/loginInfo.do")
    }
});