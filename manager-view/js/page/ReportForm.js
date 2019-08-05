layui.use(['form', 'table', 'laydate'], function() {
	var form = layui.form,
		table = layui.table,
		laydate = layui.laydate;
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
					align: 'center',
					edit: 'text'
				},
				{
					field: 'department',
					title: '部门',
					align: 'center',
					edit: 'text'
				},
				{
					field: 'zc',
					title: '考勤次数',
					align: 'center',
					edit: 'text'
				}, {
					field: 'bzc',
					title: '缺勤次数',
					align: 'center',
					edit: 'text'
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
	laydate.render({
		elem: '#test',
		range: '~'
	});
})