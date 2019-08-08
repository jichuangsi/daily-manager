layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		url: httpUrl() + '/ol/getolrecord1',
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
					align: 'center',
					templet: function(d) {
						if(d.start != 0) {
							return new Date(+new Date(d.start) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
				}, {
					field: 'time',
					title: '请假日期',
					align: 'center',
					templet: function(d) {
						if(d.time != 0) {
							return new Date(+new Date(d.time) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
				}, {
					field: '',
					title: '请假时间(天)',
					align: 'center'
				},
				{
					field: 'msg',
					title: '请假原因',
					align: 'center'
				},
				{
					field: 'stuas',
					title: '状态',
					align: 'center',
					templet: function(d) {
						if(d.stuas == 1) {
							return "待审核"
						} else if(d.stuas == 2) {
							return "已审核"
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
		page: false,
		parseData: function(res) {
			var arr;
			var code;
			var total;
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
})