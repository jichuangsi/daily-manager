layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		url: httpUrl() + '/sq/getAllUnapprovedSQ',
		id: 'idTest',
		headers: {
			'content-type': 'application/x-www-form-urlencoded',
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'd.sqFlie.uuid',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'd.people.peopleName',
					title: '姓名',
					align: 'center',
					templet: '<div>{{d.people?d.people.peopleName:""}}</div>'
				},
				{
					field: 'd.people.department',
					title: '部门',
					align: 'center',
					templet: '<div>{{d.people?d.people.department:""}}</div>'
				},
				{
					field: 'd.people.jurisdiction',
					title: '岗位',
					align: 'center',
					templet: '<div>{{d.people?d.people.jurisdiction:""}}</div>'
				}, {
					field: 'd.sqFlie.time',
					title: '申诉时间',
					align: 'center',
					templet: function(d) {
						if(d.sqFlie.time != 0) {
							return new Date(+new Date(d.sqFlie.time) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
				}, {
					field: 'd.sqFlie.msg',
					title: '申诉原因',
					align: 'center',
					templet: '<div>{{d.sqFlie?d.sqFlie.msg:""}}</div>'
				},
				{
					field: 'd.sqFlie.stuas',
					title: '状态',
					align: 'center',
					templet: function(d) {
						if(d.sqFlie.stuas == 0) {
							return "待审核"
						} else {
							return "已审核"
						}
					}
				},
				{
					field: 'schooldel',
					title: '详情',
					align: 'center',
					toolbar: '#details'
				}, {
					field: 'schooldel',
					title: '操作',
					align: 'center',
					toolbar: '#operation'
				}

			]
		],
		page: true,
		parseData: function(res) {
			var arr;
			var code;
			var pageNum;
			if(res.code == "0010") {
				code = 0;
				arr = res.data;
				pageNum = arr.length;
			}
			return {
				"code": 0,
				"msg": res.msg,
				"count": pageNum,
				"data": arr
			};
		},
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		}
	});
	table.on('row(demo)', function(data) {
		var param = data.data;
		setImg(param.sqFlie.uuid);

		$(document).on('click', '#Agree', function() {
			param.stuas2 = '1'; //
			AuditApplications("确认要同意该申请吗", param);
		});
		$(document).on('click', '#Reject', function() {
			param.stuas2 = '2'; //不同意
			AuditApplications('确认要驳回该申请吗', param);
		});
	});
	/*审核*/
	function AuditApplications(msg, param) {
		layer.confirm(msg, function(index) {
			$.ajax({
				type: "post",
				url: httpUrl() + "/sq/sssh",
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
						table.reload('idTest');
					} else if(res.code == '0031') {
						layer.close(index);
						layui.notice.info("提示信息：权限不足");
					} else {
						layer.close(index);
						layui.notice.error("提示信息:失败!");
						table.reload('idTest');
					}
				},
				error: function(res) {
					setMsg(res.msg, 2)
				}
			})
		})
	}
	/*根据id获取图片，申请详情*/
	function setImg(id) {
		$.ajax({
			type: "post",
			url: httpUrl() + "/sq/getimg?uuid=" + id,
			async: false,
			headers: {
				'content-type': 'application/x-www-form-urlencoded',
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					var data = res.data;
					//获取数据生成图片
					var content = '<img src="data:image/jpeg;base64,' + data + '" />';
					$("#img").append(content);
				} else {}
			},
			error: function(res) {
				setMsg(res.msg, 2)
			}
		})
	}
	form.on('submit(sreach)', function(data) {
		var param = data.field;
		if(param.statusId == -1) {
			param.statusId = 0
		}
		table.reload('idTest', {
			url: httpUrl() + '/sq/getAllUnapprovedSQ',
			header: {
				'content-type': 'application/x-www-form-urlencoded',
				'accessToken': getToken()
			},
			where: {
				name: param.name,
				sttt: param.statusId
			}
		});
	})
})