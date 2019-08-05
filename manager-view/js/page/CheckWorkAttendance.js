layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: '../json/data.json',
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'name',
					title: '姓名',
					align: 'center'
				},
				{
					field: 'department',
					title: '部门',
					align: 'center',
					edit: 'text'
				},
				{
					field: 'role',
					title: '职位',
					align: 'center',
					edit: 'text'
				}, {
					field: 'startStatus',
					title: '考勤',
					align: 'center',
					edit: 'text'
				}, {
					field: 'start',
					title: '打卡时间',
					align: 'center',
					edit: 'text'
				},
				{
					field: 'end',
					title: '下班打卡时间',
					align: 'center',
					edit: 'text'
				}, {
					field: 'workStatus',
					title: '状态',
					align: 'center',
					edit: 'text',
					toolbar: '#colorStatus'
				}
			]
		],
		toolbar: '#operation',
		page: false,
		parseData: function(res) {
			var arr;
			var code;
			var total;
			if(res.code == "0010") {
				code = 0;
				arr = res.data.list;
				total = arr.length;
			}
			return {
				"code": 0,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		}

	});
})