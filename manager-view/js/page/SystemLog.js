layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	table.render({
		elem: '#demo',
		method: "get",
		id:'idTest',
		async: false,
		url: httpUrl() + '/backuser/getOpLogByNameAndPage',
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
					field: 'opAction',
					title: '行为名称',
					align: 'center'
				},
				{
					field: 'operatorName',
					title: '执行者',
					align: 'center'
				},
				{
					field: 'createdTime',
					title: '执行时间',
					align: 'center',
					templet: function(d) {
						if(d.createdTime != 0) {
							return new Date(+new Date(d.createdTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
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
			var arr, code, total;
			if(res.code == "0010") {
				code = 0;
				arr = res.data.content;
				total=res.data.totalElements;
			} else if(res.code == '0031') {
				code = 0031
			}
			return {
				"code": code,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		},
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		},
	});
	//删除日志
	function DelLog(id) {
		layer.confirm('确认要删除吗？', function(index) {
			$.ajax({
				type: "get",
				url: httpUrl() + "/backuser/deleteOpLog?opId=" + id,
				async: false,
				headers: {
					'accessToken': getToken()
				},
				success: function(res) {
					if(res.code == '0010') {
						table.reload('demo');
						layui.notice.success("提示信息:删除成功!");
						layer.close(index);
					} else if(res.code == '0031') {
						layui.notice.info("提示信息：权限不足");
					} else {
						layui.notice.error("提示信息:添加失败啦!");
					}
				},
				error: function(res) {
					console.log(res.msg)
				}
			})
		});

	}
	table.on('row(demo)', function(data) {
		var param = data.data;
		$(document).on('click', '#delLog', function() {
			DelLog(param.id);
		});

	})
	form.on('submit(sreach)', function(data) {
		var param = data.field;
		table.reload('idTest', {
			url: httpUrl() + '/backuser/getOpLogByNameAndPage',
			header: {
				'accessToken': getToken()
			},
			where: {
				name: param.name
			}
		});
	})
})