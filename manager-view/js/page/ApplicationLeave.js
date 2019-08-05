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
					align: 'center'
				},
				{
					field: 'role',
					title: '岗位',
					align: 'center'
				}, {
					field: 'start',
					title: '申请时间',
					align: 'center'
				},{
					field: 'leave',
					title: '请假日期',
					align: 'center'
				},{
					field: 'leaveDate',
					title: '请假时间(天)',
					align: 'center'
				},
				{
					field: 'schooldel',
					title: '请假原因',
					align: 'center'
				},
				{
					field: 'status',
					title: '状态',
					align: 'center'
				}, {
					field: 'schooldel',
					title: '操作',
					align: 'center',
					toolbar:'#operation'
				}
			]
		],
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