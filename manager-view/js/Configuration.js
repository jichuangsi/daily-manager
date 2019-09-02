var url;
var token;
//修改路径
function httpUrl() {
//	url = "https://sample.zaixian.jichuangsi.com";
	//	url='http://192.168.5.10:8083'
	url = 'http://192.168.31.83:8083'
//	url = 'http://192.168.43.42:8083'
	//	url = 'http://192.168.31.145:8083'
	return url;
}
//获取token
function getToken() {
	return token = sessionStorage.getItem('token');
}
pdToken();

function pdToken() {
	if(UrlSearch() != 'login.html') {
		getUserInfo()
	}
}

function UrlSearch() {
	var name, value;
	var str = location.href;
	var num = str.split("/")
	return num[num.length - 1];
}

function getUserInfo() {
	$.ajax({
		type: "GET",
		url: httpUrl() + "/backuser/getBackuserById",
		async: false,
		headers: {
			'accessToken': getToken()
		},
		contentType: 'application/json',
		success: function(res) {
			if(res.code == '0031' || res.msg == 'token校验异常' || res.msg == '账号不存在，或者未被激活') {
				if(UrlSearch() == 'login.html') {

				} else {
					window.location.href = 'login.html';
				}

			}
		}
	});
}