layui.use(['form', 'table', 'laydate'], function() {
	var laydate = layui.laydate,
		form = layui.form,
		table = layui.table;
	$('#map').on('click', function() {
		getMap()
	})
	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'time'
		});
	})

	function getMap() {
		index = layer.open({
			type: 2,
			area: ['90%', ' 90% '],
			anim: 2,
			title: '获取坐标(点击获取经纬度)',
			maxmin: true,
			shadeClose: true,
			content: 'Map2.html',
			end: function() {
				getmap()
			}
		});
	}

	function getmap() {
		var map = JSON.parse(sessionStorage.getItem('map'))
		if(map != null) {
			form.val('wifi', {
				'lng': map.lng,
				'lat': map.lat
			})
		}
	}
	getWifiTemplate()

	function getWifiTemplate() {
		$.ajax({
			type: "post",
			url: httpUrl() + "/rule/getrulefatherlist",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					var arr = res.data;
					var longitudeLatitude;
					var list;
					if(arr.length > 0) {
						list = arr[0];
					}
					longitudeLatitude = list.longitudeLatitude;
					var zb = longitudeLatitude.split(',')
					form.val('wifi', {
						wifiName: list.wifiName,
						wucha: list.wucha
					});
				}
			},
			error: function(res) {
				setMsg(res.msg, 2)
			}
		})
	}
	//提交规则
	form.on('submit(add)', function(data) {
		var param = data.field;
		if(param.statusId == -1) {
			setMsg('请选择上下班', 2)
			return false;
		}
		var longitudeLatitude = param.lng + ',' + param.lat;
		var data = {
			time: param.timeString,
			wifiName: param.wifiName,
			longitudeLatitude: longitudeLatitude,
			stuas: param.stuas,
			wucha: param.wucha
		}
		$.ajax({
			type: "post",
			url: httpUrl() + "/rule/ruleset",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			data: JSON.stringify(data),
			success: function(res) {
				if(res.code == '0010') {
					layui.notice.success("提示信息:添加成功!");
				} else if(res.code == '0031') {
					layui.notice.info("提示信息：权限不足");
				} else {
					layui.notice.error("提示信息:添加失败!");
				}
			},
			error: function(res) {
				setMsg(res.msg, 2)
			}
		})
	});
})