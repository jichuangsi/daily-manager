layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	var url = httpUrl() + '/ol/getolrecord1'
	form.on('submit(sreach)', function(data) {
		var param = data.field;
		console.log(param)
		if(param.status == -1) {
			param.status = '';
		}
		table.reload('idTest', {
			url: httpUrl() + '/ol/getolrecord1',
			header: {
				'accessToken': getToken()
			},
			where: {
				sttt: param.status,
				name: param.name
			}
		});
	});
		console.log(getToken());
	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		url: httpUrl() + '/ol/getolrecord1',
		id: 'idTest',
		headers: {
			'content-type': 'application/x-www-form-urlencoded',
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'peopleName',
					title: '姓名',
					align: 'center'
				},
				{
					field: 'department',
					title: '部门',
					align: 'center'
				},
				{
					field: 'jurisdiction',
					title: '岗位',
					align: 'center'
				}, {
					field: 'd.overtimeleave.msg',
					title: '申请原因',
					align: 'center',
					templet: '<div>{{d.overtimeleave?d.overtimeleave.msg:""}}</div>'
				}, {
					field: 'd.overtimeleave.start',
					title: '申请时间',
					align: 'center',
					templet: function(d) {
						if(d.overtimeleave.start != 0) {
							return new Date(+new Date(d.overtimeleave.start) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
				}, {
					field: 'd.overtimeleave.time',
					title: '加班时间',
					align: 'center',
					templet: function(d) {
						if(d.overtimeleave.time != 0) {
							return new Date(+new Date(d.overtimeleave.time) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
				},
				{
					field: 'd.overtimeleave.stuas',
					title: '申请类型',
					align: 'center',
					templet: function(d) {
						if(d.overtimeleave.stuas == 1) {
							return "请假申请"
						} else if(d.overtimeleave.stuas == 2) {
							return "加班申请"
						}
					}
				}, {
					field: 'd.overtimeleave.stuas2',
					title: '状态',
					align: 'center',
					templet: function(d) {
						if(d.overtimeleave.stuas2 == 0) {
							return "待审批"
						} else if(d.overtimeleave.stuas2 == 1) {
							return "已审核"
						} else if(d.overtimeleave.stuas2 == 2) {
							return "已审核"
						}
					}
				},
				{
					field: 'schooldel',
					title: '操作状态',
					align: 'center',
					templet: function(d) {
						if(d.overtimeleave.stuas2 == 0) {
							return '<span class="layui-btn-sm layui-btn" id="Agree">同意</span><span class="layui-btn-sm layui-btn" id="Reject">驳回</span> '
						} else if(d.overtimeleave.stuas2 == 1) {
							return "同意"
						} else if(d.overtimeleave.stuas2 == 2) {
							return "驳回"
						}
					}
				}
			]
		],
		page: true,
		parseData: function(res) {
			var arr;
			var code;
			var total;
			if(res.code == "0010") {
				if(res.data==null){
					res.data=[];
				}
				code = 0;
				arr = res.data;
				total = arr.length;
				
			}
			return {
				"code": 0,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		},
		where: {
			sttt: '',
			name: ''
		},
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		}
	});
	table.on('row(demo)', function(data) {
		var param = data.data;
		var overtimeleave = param.overtimeleave;

		$(document).on('click', '#Agree', function() {
			overtimeleave.stuas2 = '1'; //同意
			AuditApplications("确认要同意该申请吗", overtimeleave);
		});
		$(document).on('click', '#Reject', function() {
			overtimeleave.stuas2 = '2'; //不同意
			AuditApplications('确认要驳回该申请吗', overtimeleave);
		});
	});
	/*审核*/
	function AuditApplications(msg, param) {
		layer.confirm(msg, function(index) {
			$.ajax({
				type: "post",
				url: httpUrl() + "/ol/olsh",
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
})