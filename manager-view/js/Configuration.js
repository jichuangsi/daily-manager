var url;
var token;
//修改路径
function httpUrl() {
	url = "http://192.168.31.83:8083"
	//url='http://192.168.5.12:8083'
	return url;
}
//获取token
function getToken() {
	return token = sessionStorage.getItem('token');
}