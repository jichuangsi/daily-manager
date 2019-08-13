layui.use(['form', 'table', 'laydate'], function() {
	var form = layui.form,
		table = layui.table,
		laydate = layui.laydate;
	var date = new Date();
	date.setMonth(date.getMonth() - 1);
	var dateStart = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
	var dateEnd = date.getFullYear() + "-" + (date.getMonth() + 2) + "-" + date.getDate();
	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		url: httpUrl() + '/kq/getBB',
		id: 'idTest',
		headers: {
			'content-type': 'application/x-www-form-urlencoded',
			'accessToken': getToken()
		},
		id: 'idTest',
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
					title: '职位',
					align: 'center'
				},

				{
					field: 'kq',
					title: '考勤次数',
					align: 'center',
				}, {
					field: 'qq',
					title: '缺勤次数',
					align: 'center'
				}
			]
		],
		toolbar: '#operation',
		page: true,
		parseData: function(res) {
			var arr;
			var code;
			var total;
			if(res.code == "0010") {
				code = 0;
				arr = res.data;
				total = arr.length;
			}
			return {
				"code": code,
				"msg": res.msg,
				"count": res.pageSize,
				"data": arr
			};
		},
		where: {
			timeStart: dateStart,
			timeEnd: dateEnd,
			name: ''
		},
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		}
	});
	laydate.render({
		elem: '#test',
		range: '~'
	});
	form.on('submit(sreach)', function(data) {
		var param = data.field;
		var date = param.date.split('~');
		if(param.date == '') {
			var date = new Date();
			date.setMonth(date.getMonth() - 1);
			var dateStart = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
			var dateEnd = date.getFullYear() + "-" + (date.getMonth() + 2) + "-" + date.getDate();
		} else {
			var dateStart = date[0];
			var dateEnd = date[1];
		}
		//默认获取一个月前的
		table.reload('idTest', {
			url: httpUrl() + '/kq/getBB',
			header: {
				'content-type': 'application/x-www-form-urlencoded',
				'accessToken': getToken()
			},
			where: {
				timeStart: dateStart,
				timeEnd: dateEnd,
				name: param.name
			}
		});
	});
})