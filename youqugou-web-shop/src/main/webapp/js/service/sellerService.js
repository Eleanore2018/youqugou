app.service("sellerService", function ($http) {
    this.insertSeller = function (entity) {
        return $http.post("../seller/insertSeller.do", entity);
    }
});