app.service("userService", function ($http) {

    this.searchUser = function (currentPage, itemsPerPage) {
        return $http.get("../userManager/searchUser.do?currentPage="+currentPage+"&itemsPerPage="+itemsPerPage);
    }

    this.freeze = function (ids) {
        return $http.post("../userManager/freezeUser.do",ids);
    }

    this.userCount = function () {
        return $http.get("../userManager/userCount.do");
    }
});