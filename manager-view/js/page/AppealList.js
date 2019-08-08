layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	var url = httpUrl() + '/sq/getAllUnapprovedSQ'
	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		id: 'idTest',
		url: url,
		header: {
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
		},where :{
			sttt:''
		}
	});
	table.on('row(demo)', function(data) {
		var param = data.data;
		setImg(param.sqFlie.uuid);
	});
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
		if(param.statusId==-1){
			param.statusId=0
		}
		table.reload('idTest', {
			url: url,
			header: {
				'content-type': 'application/x-www-form-urlencoded',
				'accessToken': getToken()
			},
			where: {
				name: param.name,
				sttt:param.statusId
			}
		});
	})
})