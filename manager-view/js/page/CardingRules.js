layui.use(['form', 'table', 'laydate'], function() {
	var laydate = layui.laydate,
		form = layui.form,
		table = layui.table;
	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'time'
		});
	})
	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		url: httpUrl() + '/rule/getrulelist',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'stuas',
					title: '名称',
					align: 'center',
					templet: function(d) {
						if(d.stuas == 1) {
							return "上班打卡"
						} else if(d.stuas == 2) {
							return "下班打卡"
						}
					}
				},
				{
					field: 'longitudeLatitude',
					title: '经纬度',
					align: 'center'
				},
				{
					field: 'wifiName',
					title: 'wifi名称',
					align: 'center'
				},
				{
					field: 'wucha',
					title: '偏差(m)',
					align: 'center'
				},
				{
					field: 'time',
					title: '时间',
					align: 'center',
					templet: function(d) {
						if(d.time != 0) {
							return new Date(+new Date(d.time) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '');
						} else {
							return "-"
						}
					}
				},
				{
					field: 'deptdescribe',
					title: '操作',
					align: 'center',
					toolbar: '#operation'
				}
			]
		],
		toolbar: '#operation2',
		page: false,
		parseData: function(res) {
			var arr, code, total;
			if(res.code == "0010") {
				code = 0;
				arr = res.data;
				total = arr.length;
			} else if(res.code == '0031') {
				code = 0031
			}
			return {
				"code": 0,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		}
	});
	table.on('row(demo)', function(data) {
		var param = data.data;
		getWifiTemplate()
		//修改今天的规则 
		var longitudeLatitude = param.longitudeLatitude;
		var zb = longitudeLatitude.split(',')
		form.val('Mwifi', {
			Mid: param.id,
			MwifiName: param.wifiName,
			Mwucha: param.wucha,
			Mlat: zb[1],
			Mlng: zb[0]
		})
		$(document).on('click', '#delTodayRules', function() {
			DelTodayRules(param.id);
		});
	});
	//删除当天的模板
	function DelTodayRules(id) {
		layer.confirm('确认要删除吗？', function(index) {
			$.ajax({
				type: "post",
				url: httpUrl() + "/rule/delrule2?ruleid=" + id,
				async: false,
				headers: {
					'content-type': 'application/x-www-form-urlencoded',
					'accessToken': getToken()
				},
				success: function(res) {
					if(res.code == '0010') {
						table.reload('demo');
						layer.close(index);
						layui.notice.success("提示信息:删除成功!");
					} else if(res.code == '0031') {
						layer.close(index);
						layui.notice.info("提示信息：权限不足");
					} else {
						layer.close(index);
						table.reload('demo');
						layui.notice.error("提示信息:删除失败啦!");
					}
				},
				error: function(res) {
					setMsg(res.msg, 2)
				}
			})
		})

	}
	form.on('submit(MRules)', function(data) {
		var param = data.field;
		if(param.Mstuas == -1) {
			setMsg('请选择上下班', 2)
			return false;
		}
		var longitudeLatitude = param.Mlng + ',' + param.Mlat;
		var data = {
			id: param.Mid,
			time: param.MtimeString,
			wifiName: param.MwifiName,
			longitudeLatitude: longitudeLatitude,
			stuas: param.Mstuas,
			wucha: param.Mwucha
		}
		$.ajax({
			type: "post",
			url: httpUrl() + "/rule/updaterule",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			data: JSON.stringify(data),
			success: function(res) {
				if(res.code == '0010') {
					table.reload('demo');
					layer.close(index);
					layui.notice.success("提示信息:修改成功!");
				} else if(res.code == '0031') {
					layer.close(index);
					layui.notice.info("提示信息：权限不足");
				} else {
					layer.close(index);
					layui.notice.error("提示信息:修改失败!");
				}
			},
			error: function(res) {
				setMsg(res.msg, 2)
			}
		})
		return false;
	})
	//修改今天规则
	init()

	function init() {
		var lat = $('input[name=lat]').val()
		if(lat == '') {
			getWifiTemplate();
		}

	}

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
						wucha: list.wucha,
						lat: zb[1],
						lng: zb[0]
					});
				}
			},
			error: function(res) {
				setMsg(res.msg, 2)
			}
		})
	}
	/*添加今日的规则*/
	form.on('submit(add)', function(data) {
		var param = data.field;
		console.log(param)
		if(param.stuas == -1) {
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
			url: httpUrl() + "/rule/insertrule",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			data: JSON.stringify(data),
			success: function(res) {
				if(res.code == '0010') {
					table.reload('demo');
					layer.close(index);
					layui.notice.success("提示信息:添加成功!");
				} else if(res.code == '0031') {
					layui.notice.info("提示信息：权限不足");
				} else {
					layer.close(index);
					layui.notice.error("提示信息:添加失败!");
				}
			},
			error: function(res) {
				setMsg(res.msg, 2)
			}
		})
		return false;
	});
	//模板规则
	table.render({
		elem: '#today',
		method: "post",
		async: false,
		url: httpUrl() + '/rule/getrulefatherlist',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'stuas',
					title: '名称',
					align: 'center',
					templet: function(d) {
						if(d.stuas == 1) {
							return "上班打卡"
						} else if(d.stuas == 2) {
							return "下班打卡"
						}
					}
				},
				{
					field: 'longitudeLatitude',
					title: '经纬度',
					align: 'center'
				},
				{
					field: 'wifiName',
					title: 'wifi名称',
					align: 'center'
				},
				{
					field: 'wucha',
					title: '偏差(m)',
					align: 'center'
				},
				{
					field: 'time',
					title: '时间',
					align: 'center',
					templet: function(d) {
						if(d.time != 0) {
							var date1 = new Date('1970-01-01 08:00:00')
							var date2 = new Date(new Date(+new Date(d.time) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, ''))
							var s1 = date1.getTime(),
								s2 = date2.getTime();
							var total = (s2 - s1) / 1000;
							var day = parseInt(total / (24 * 60 * 60));
							var afterDay = total - day * 24 * 60 * 60;
							var hour = parseInt(afterDay / (60 * 60));
							var afterHour = total - day * 24 * 60 * 60 - hour * 60 * 60;
							var min = parseInt(afterHour / 60);
							var afterMin = total - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
							return hour + '点' + min + "分" + afterMin + "秒"
						} else {
							return "-"
						}
					}
				},
				{
					field: 'deptdescribe',
					title: '操作',
					align: 'center',
					toolbar: '#del'
				}
			]
		],
		page: false,
		parseData: function(res) {
			var arr, code, total;
			if(res.code == "0010") {
				code = 0;
				arr = res.data;
				total = arr.length;
			} else if(res.code == '0031') {
				code = 0031
			}
			return {
				"code": code,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		}
	});
	table.on('row(today)', function(data) {
		var param = data.data;

		$(document).on('click', '#delRules', function() {
			DelRules(param.id);
		});
		$(document).on('click', '#Start', function() {
			param.qiting = 1
			StartTemplate("确认要启用该模板吗", param);
		});
		$(document).on('click', '#end', function() {
			param.qiting = 2;
			StartTemplate('确认要停用该模板吗', param);
		});
	});
	/*启动模板*/
	function StartTemplate(msg, param) {
		layer.confirm(msg, function(index) {
			$.ajax({
				type: "post",
				url: httpUrl() + "/rule/rulefatherstopandstart",
				async: false,
				headers: {
					'accessToken': getToken()
				},
				contentType: 'application/json',
				data: JSON.stringify(param),
				success: function(res) {
					if(res.code == '0010') {
						layer.close(index);
						layui.notice.success("提示信息:成功!");
						table.reload('today');
					} else if(res.code == '0031') {
						layer.close(index);
						layui.notice.info("提示信息：权限不足");
					} else {
						layer.close(index);
						layui.notice.error("提示信息:失败!");
						table.reload('today');
					}
				},
				error: function(res) {
					setMsg(res.msg, 2)
				}
			})
		})
	}
	/*删除模板规则*/
	function DelRules(id) {
		layer.confirm('确认要删除吗？', function(index) {
			$.ajax({
				type: "post",
				url: httpUrl() + "/rule/delrule?ruleFatherId=" + id,
				async: false,
				headers: {
					'content-type': 'application/x-www-form-urlencoded',
					'accessToken': getToken()
				},
				success: function(res) {
					if(res.code == '0010') {
						table.reload('today');
						layer.close(index);
						layui.notice.success("提示信息:删除成功!");
					} else if(res.code == '0031') {
						layer.close(index);
						layui.notice.info("提示信息：权限不足");
					} else {
						layer.close(index);
						layui.notice.error("提示信息:删除失败啦!");
					}
				},
				error: function(res) {
					setMsg(res.msg, 2)
				}
			})
		})

	}
})