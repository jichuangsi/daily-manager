layui.use("form", function() {
	var form = layui.form;
	var admin;
	form.on('submit(update_Pwd)', function(data) {
		var param = data.field;
		if(param.newPwd != param.yesPwd) {
			layer.msg("两次密码不相同!", {
				icon: 2,
				time: 1000,
				end: function() {
					layer.close(index);
				}
			});
			return false;
		} else {
			$.ajax({
				type: "post",
				url: httpUrl() + "/back/user/updatePwd",
				async: false,
				headers: {
					'accessToken': getToken()
				},
				contentType: 'application/json',
				data: JSON.stringify(param),
				success: function(res) {
					if(res.code == '0010') {
						layer.msg('修改成功！', {
							icon: 1,
							time: 1000,
							end: function() {
								layer.close(index);
							}
						});
					} else {
						layer.msg(res.msg, {
							icon: 2,
							time: 1000,
							end: function() {
								layer.close(index);
							}
						});
					}
				}
			});
			return false;
		}
	});
	form.verify({　　　　
		pwd: [/^((?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12})$/, '密码必须为6-12位数字与字母混合']　　
	});

	getUserInfo()

	function getUserInfo() {
		console.log(getToken())
		$.ajax({
			type: "GET",
			url: httpUrl() + "/backuser/getBackuserById",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType:'application/json',
			success: function(res) {
				if(res.code == '0010') {
					if(res.data.roleName != "M") {
						getMenu(res.data.roleId)
					}
					sessionStorage.setItem('userInfo', JSON.stringify(res.data));
				}
			}
		});
	}
	validation();

	function validation() {
		if(!getToken()) {window.location.href = 'login.html';}
	}
	var Urldata = [];

	function getMenu(id) {
		$.ajax({
			type: "get",
			url: httpUrl() + "/backrole/getModelAndStaticPageByRoleId/" + id,
			async: false,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					Urldata = res.data;
					setMenu(Urldata);
				}
			}
		});
	}

	function setMenu(Urldata) {
		var Modular = [];
		for(var i = 0; i < Urldata.length; i++) {
			if(Modular.length == 0) {
				Modular.push({
					id: Urldata[i].modelId,
					name: Urldata[i].modelName
				})
			} else {
				for(var j = 0; j < Modular.length; j++) {
					if(Modular[j].id != Urldata[i].modelId) {
						Modular.push({
							id: Urldata[i].modelId,
							name: Urldata[i].modelName
						})
					}
				}
			}
		}
		$('#nav').empty();
		var content = '';
		for(var i = 0; i < Modular.length; i++) {
			content += "<li>";
			content += '<a href="javascript:;">';
			content += '<i class="iconfont">&#xe6b8;</i>';
			content += '<cite>' + Modular[i].name + '</cite>';
			content += '<i class="iconfont nav_right">&#xe697;</i>';
			content += '</a>';
			content += '<ul class="sub-menu">';
			for(var j = 0; j < Urldata.length; j++) {
				if(Modular[i].id = Urldata[j].modelId) {
					content += '<li>';
					content += '<a _href="page/'+Urldata[j].staticPageUrl+'">';
					content += '<i class="iconfont">&#xe6a7;</i>';
					content += '<cite>'+Urldata[j].staticPageName+'</cite>';
					content += '</a>';
					content += '</li>';
				}
			}
			content += '</ul>';
			content += "</li>";
		}
		$('#nav').append(content);
	}
});