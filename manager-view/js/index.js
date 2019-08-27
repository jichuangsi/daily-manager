layui.use(['form', 'element'], function() {
	var form = layui.form,
		element = layui.element;
	var admin;

	form.verify({　　　　
		pwd: [/^((?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12})$/, '密码必须为6-12位数字与字母混合']　　
	});

	getUserInfo()

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
				if(res.code == '0010') {
					if(res.data.roleName != "M") {
						getMenu(res.data.roleId)
					}
					form.val('test', {
						id: res.data.id
					})
					sessionStorage.setItem('userInfo', JSON.stringify(res.data));
				}
			}
		});
	}
	validation();

	function validation() {
		if(!getToken()) {
			window.location.href = 'login.html';
		}
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
		var obj = {};
		var Modular = Modular.reduce((cur, next) => {
			obj[next.id] ? "" : obj[next.id] = true && cur.push(next);
			return cur;
		}, [])
		for(var i = 0; i < Modular.length; i++) {
			content += "<li>";
			content += '<a href="javascript:;">';
			content += '<i class="iconfont"></i>';
			content += '<cite>' + Modular[i].name + '</cite>';
			content += '<i class="iconfont nav_right">&#xe697;</i>';
			content += '</a>';
			content += '<ul class="sub-menu">';
			for(var j = 0; j < Urldata.length; j++) {
				if(Modular[i].id == Urldata[j].modelId) {
					content += '<li>';
					content += '<a _href="page/' + Urldata[j].staticPageUrl + '">';
					content += '<i class="iconfont">&#xe6a7;</i>';
					content += '<cite>' + Urldata[j].staticPageName + '</cite>';
					content += '</a>';
					content += '</li>';
				}
			}
			content += '</ul>';
			content += "</li>";
		}
		if(content == '') {
			$('.left-nav').addClass('jq');
			$('.page-content').addClass('cq')
		}
		$('#nav').append(content);

		var tab = {
			tabAdd: function(title, url, id) {
				element.tabAdd('xbs_tab', {
					title: title,
					content: '<iframe tab-id="' + id + '" frameborder="0" src="' + url + '" scrolling="yes" class="x-iframe"></iframe>',
					id: id
				})
			},
			tabDelete: function(othis) {
				element.tabDelete('xbs_tab', '44');
				othis.addClass('layui-btn-disabled');
			},
			tabChange: function(id) {
				element.tabChange('xbs_tab', id);
			}
		};

		$('.left-nav #nav li').click(function(event) {
			if($(this).children('.sub-menu').length) {
				if($(this).hasClass('open')) {
					$(this).removeClass('open');
					$(this).find('.nav_right').html('&#xe697;');
					$(this).children('.sub-menu').stop().slideUp();
					$(this).siblings().children('.sub-menu').slideUp();
				} else {
					$(this).addClass('open');
					$(this).children('a').find('.nav_right').html('&#xe6a6;');
					$(this).children('.sub-menu').stop().slideDown();
					$(this).siblings().children('.sub-menu').stop().slideUp();
					$(this).siblings().find('.nav_right').html('&#xe697;');
					$(this).siblings().removeClass('open');
				}
			} else {

				var url = $(this).children('a').attr('_href');
				var title = $(this).find('cite').html();
				var index = $('.left-nav #nav li').index($(this));

				for(var i = 0; i < $('.x-iframe').length; i++) {
					if($('.x-iframe').eq(i).attr('tab-id') == index + 1) {
						tab.tabChange(index + 1);
						event.stopPropagation();
						return;
					}
				};

				tab.tabAdd(title, url, index + 1);
				tab.tabChange(index + 1);
			}

			event.stopPropagation();
		})
	}
	startTime();

	function startTime() {
		var today = new Date();
		var h = today.getHours();
		var m = today.getMinutes();
		var s = today.getSeconds();
		m = checkTime(m);
		s = checkTime(s);
		document.getElementById("clock").innerHTML = ":" + h + ":" + m + ":" + s;
		t = setTimeout(function() {
			startTime()
		}, 500);
	}

	function checkTime(i) {
		if(i < 10) {
			i = "0" + i;
		}
		return i;
	}

	form.on('submit(update_Pwd)', function(data) {
		var param = data.field;
		if(param.firstPwd != param.secondPwd) {
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
				url: httpUrl() + "/backuser/updateBackUserPwd",
				async: false,
				headers: {
					'accessToken': getToken()
				},
				contentType: 'application/json',
				data: JSON.stringify(param),
				success: function(res) {
					if(res.code == '0010') {
						layer.msg("修改成功!", {
							icon: 1,
							time: 1000,
							end: function() {
								layer.close(index);
							}
						});
					} else {
						layer.msg("修改失败!", {
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
});