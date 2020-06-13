layui.use(['form', 'table', 'laydate'], function() {
	var form = layui.form,
		table = layui.table,
		laydate = layui.laydate;
	laydate.render({
		elem: '#test',
		range: '~'
	});
	//获取登陆者的信息
	function getUser() {
		var user = JSON.parse(sessionStorage.getItem('userInfo'))
		getRoleDep(user);
		if (user.roleName == "M") {
			user.wechat = '';
		}
		if (user.roleName == '员工') {
			$('#name').hide();
		}
		return user;
	}
	var date = new Date();
	//	date.setMonth(date.getMonth() - 1);
	var dateStart = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
	var dateEnd = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();

	var temp1 = '';
	getUser();

	table.render({
		elem: '#demo',
		method: "post",
		id: 'idTest',
		async: false,
		url: httpUrl() + '/kq/getDailyList',
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
					field: 'stuas',
					title: '考勤',
					align: 'center',
					templet: function(d) {
						if (d.stuas == 1) {
							return "上班打卡"
						} else if (d.stuas == 2) {
							return "下班打卡"
						}
					}
				}, {
					field: 'chockinTime',
					title: '打卡时间',
					align: 'center',
					templet: function(d) {
						if (d.chockinTime != 0) {
							return new Date(+new Date(d.chockinTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(
								/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
				},
				{
					field: 'time',
					title: '考勤时间',
					align: 'center',
					templet: function(d) {
						if (d.time != 0) {
							return new Date(+new Date(d.time) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/,
								'')
						} else {
							return "-"
						}
					}
				},
				{
					field: 'stuas2',
					title: '状态',
					align: 'center',
					toolbar: '#colorStatus'
				}
			]
		],
		page: true,
		loading:true,
		toolbar: '#exe',
		defaultToolbar: ['filter', 'exports'],
		parseData: function(res) {
			var arr;
			var code;
			var total;
			if (res.code == "0010") {
				code = 0;
				arr = res.data;
				total = res.pageSize;
			}
			return {
				"code": 0,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		},
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		},
		where: {
			dpid: getUser().deptId,
			openid: getUser().wechat,
			timeStart: dateStart,
			timeEnd: dateEnd
		}
	});
	//下拉查询部门
	form.on('select(roleDept)', function(data) {
		var param = data.value;
		temp1 = setExcel(param, dateStart, dateEnd);
		table.reload('idTest', {
			where: {
				'dpid': param
			}
		});
	});
	form.on('submit(sreach)', function(data) {
		var param = data.field;
		var date = param.date.split('~');
		if (param.date == '') {
			var date = new Date();
			//			date.setMonth(date.getMonth() - 1);
			var dateStart = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
			var dateEnd = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
		} else {
			var dateStart = date[0];
			var dateEnd = date[1];
			var startTime = new Date(dateStart); // 开始时间
			var endTime = new Date(dateEnd); // 结束时间
			if (Math.floor((endTime - startTime) / 1000 / 60 / 60 / 24) > 93) {
				var date = new Date();
				date.setMonth(date.getMonth() - 1);
				var dateStart = date.getFullYear() + "-" + (date.getMonth() - 1) + "-" + date.getDate();
				var dateEnd = date.getFullYear() + "-" + (date.getMonth() + 2) + "-" + date.getDate();
			}
		}

		if (param.roleDept == undefined) {
			temp1 = setExcel(' ', dateStart, dateEnd);
		} else {
			temp1 = setExcel(param.roleDept, dateEnd, dateStart);
		}
		console.log(temp1);
		table.reload('idTest', {
			url: httpUrl() + '/kq/getDailyList',
			header: {
				'accessToken': getToken()
			},
			where: {
				deptId: param.roleDept,
				name: param.name,
				timeEnd: dateEnd,
				timeStart: dateStart
			}
		});
	})
	//根据角色获取角色所管理的部门
	function getRoleDep(user) {
		var id = user.id;
		var options = '';
		var arr = [];
		$('#roleDept').empty();
		$.ajax({
			type: "get",
			async: false,
			url: httpUrl() + '/backrole/getRoleDepartmentByRoleId?roleId=' + id,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if (res.code == '0010') {
					arr = res.data;
					if (arr.length == 0) {
						$('#allDept').empty();
					} else {
						for (var i = 0; i < arr.length; i++) {
							options += '<option value="' + arr[i].deptId + '" >' + arr[i].deptName + '</option>'
						}
						$('#roleDept').append(options);
						$("#roleDept option[value=" + user.deptId + "]").prop("selected", true);
						form.render('select');
						deptId = arr[0].deptId
					}
				} else {
					console.log(res.msg)
				}
			}
		});
	}
	//导出表格
	temp1 = setExcel(getUser().deptId, dateStart, dateEnd);
	//获取全部条件查询的考勤数据
	function setExcel(deptId, dateStart, dateEnd) {

		var url = "/kq/importDailyList?dpid=" + deptId + "&timeStart=" + dateStart + "&timeEnd=" + dateEnd;
		var getWeekUrl = "/kq/importStatisticsByWeek?dpid=" + deptId + "&timeStart=" + dateStart + "&timeEnd=" + dateEnd;
		var getMonthUrl = "/kq/importStatisticsByMonth?dpid=" + deptId + "&timeStart=" + dateStart + "&timeEnd=" + dateEnd;
		var all = getAjaxPostData(url).data;
		var week = getAjaxPostData(getWeekUrl).data.statistics;
		var weekDate = [];
		var month = getAjaxPostData(getMonthUrl).data.statistics;
		var monthDate = [];
		var temp = [];
		//把对象分离成一个；
		for (let key in week) {
			week[key]['tName'] = key;
			//添加排序判断值
			week[key]['index'] = parseInt(key.replace(/[^0-9]/ig, ""));
			// for(let tmp in week[key]){
			// 	console.log(tmp+'----'+ week[key][tmp]);
			// }
			weekDate.push(week[key])
		}
		for (let key in month) {
			month[key]['tName'] = key;
			month[key]['index'] = parseInt(key.replace(/[^0-9]/ig, ""));
			// for(let tmp in month[key]){
			// 	console.log(tmp+'----'+ month[key][tmp]);
			// }
			monthDate.push(month[key])
		}
		mao(weekDate);
		mao(monthDate);
		// weekDate.sort();
		temp.push(all);
		temp.push(weekDate);
		temp.push(monthDate);
		// 	var  temp={
		// 		'all':all,
		// 		'week':week,
		// 		'month':month
		// 	};

		/* $.ajax({
			type: "post",
			url: httpUrl() + "/kq/importDailyList?dpid=" + deptId + "&timeStart=" + dateStart + "&timeEnd=" + dateEnd,
			async: false,
			headers: {
				'content-type': 'application/x-www-form-urlencoded',
				'accessToken': getToken()
			},
			contentType: "application/json",
			dataType: 'json',
			success: function(res) {
				if (res.code == '0010') {
					temp = res.data;
				} else if (res.code == '0031') {
					layui.notice.info("提示信息：权限不足");
				}
			}
		}); */
		return temp;
	}
	//冒泡排序
	function mao(array) {
		var arr = array;
		for (var i = 0; i < arr.length; i++) {
			for (var j = i; j < arr.length; j++) {
				if (arr[i].index > arr[j].index) {
					var tmp = arr[i]; //替换
					arr[i] = arr[j];
					arr[j] = tmp;
				}
			}
		}
		return arr;
	}
	$(document).on('click', '#exportBtn', function() {
		// var data=setExcel(getUser().deptId, dateStart, dateEnd)
		var str = SplicingTable(temp1);
		exporExcel("考勤记录", str);
	});
	//拼接table与获取数据分开
	function SplicingTable(array) {
		var len = array[0].length;
		var data = array[0];
		var week = array[1];
		var month = array[2];
		var tableStr = '<table border="0" cellspacing="" cellpadding="">'
		tableStr += '<caption align="top"><h3>员工每日打卡明细</h3></caption>'
		tableStr += '<tr>';
		tableStr += '<th width="15%">' + '序号' + '</td>';
		tableStr += '<th width="15%">' + '姓名' + '</td>';
		tableStr += '<th width="15%">' + '部门' + '</td>';
		tableStr += '<th width="15%">' + '职位' + '</td>';
		tableStr += '<th width="15%">' + '考勤' + '</td>';
		tableStr += '<th width="15%">' + '打卡时间' + '</td>';
		tableStr += '<th width="15%">' + '考勤时间' + '</td>';
		tableStr += '<th width="15%">' + '状态' + '</td>';
		tableStr += '</tr>';
		var j = 1
		for (var i = 0; i < len; i++) {
			tableStr += '<tr>';
			tableStr += '<td>' + j + '</td>';
			j++;
			tableStr += '<td>' + data[i].peopleName + '</td>';
			tableStr += '<td>' + data[i].department + '</td>';
			tableStr += '<td>' + data[i].jurisdiction + '</td>';
			if (data[i].stuas == 1) {
				tableStr += '<td>' + '上班打卡' + '</td>';
			} else if (data[i].stuas == 2) {
				tableStr += '<td>' + '下班打卡' + '</td>';
			}
			tableStr += '<td>' + new Date(+new Date(data[i].chockinTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(
				/\.[\d]{3}Z/, '') + '</td>'
			tableStr += '<td>' + new Date(+new Date(data[i].time) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(
				/\.[\d]{3}Z/, '') + '</td>'
			if (data[i].stuas2 == 0) {
				tableStr += '<td>' + '正常' + '</td>';
			} else if (data[i].stuas2 == 1) {
				tableStr += '<td style="color:yellow">' + '考勤异常' + '</td>';
			} else if (data[i].stuas2 == 2) {
				tableStr += '<td style="color:red">' + '迟到' + '</td>';
			} else if (data[i].stuas2 == 3) {
				tableStr += '<td style="color:red">' + '早退' + '</td>';
			}
			tableStr += '</tr>';
		}
		if (len == 0) {
			tableStr += '<tr style="text-align: center">';
			tableStr += '<td colspan="8">' + '暂无记录' + '</font></td>';
			tableStr += '</tr>'
		}
		//周
		tableStr += '</table>';
		tableStr += '<table border="0" cellspacing="" cellpadding="">'
		tableStr += '<caption align="top"><h3>部门打卡统计-周</h3></caption>';
		tableStr += '<tr>';
		tableStr += '<th width="15%">' + '序号' + '</td>';
		tableStr += '<th width="15%">' + '名称' + '</td>';
		tableStr += '<th width="15%">' + '部门人数' + '</td>';
		tableStr += '<th width="15%">' + '上班考勤' + '</td>';
		tableStr += '<th width="15%">' + '下班考勤' + '</td>';
		tableStr += '<th width="15%">' + '上班缺勤' + '</td>';
		tableStr += '<th width="15%">' + '下班缺勤' + '</td>';
		tableStr += '<th width="15%">' + '考勤异常' + '</td>';
		tableStr += '</tr>';

		$.each(week, function(index, item) {
			tableStr += '<tr>';
			console.log(item)
			let i = 1
			tableStr += '<td>' + i + '</td>';
			tableStr += '<td>' + item['tName'] + '</td>';
			tableStr += '<td>' + item['peopleCount'] + '</td>';
			tableStr += '<td>' + item['shangkao'] + '</td>';
			tableStr += '<td>' + item['xiakao'] + '</td>';
			tableStr += '<td>' + item['shangLostKao'] + '</td>';
			tableStr += '<td>' + item['xiaLostKao'] + '</td>';
			tableStr += '<td>' + item['yichang'] + '</td>';
			i++;
			tableStr += '</tr>';
		});

		tableStr += '</table>';
		// 月
		tableStr += '<table border="0" cellspacing="" cellpadding="">'
		tableStr += '<caption align="top"><h3>部门打卡统计-月</h3></caption>';
		tableStr += '<tr>';
		tableStr += '<th width="15%">' + '序号' + '</td>';
		tableStr += '<th width="15%">' + '名称' + '</td>';
		tableStr += '<th width="15%">' + '部门人数' + '</td>';
		tableStr += '<th width="15%">' + '上班考勤' + '</td>';
		tableStr += '<th width="15%">' + '下班考勤' + '</td>';
		tableStr += '<th width="15%">' + '上班缺勤' + '</td>';
		tableStr += '<th width="15%">' + '下班缺勤' + '</td>';
		tableStr += '<th width="15%">' + '考勤异常' + '</td>';
		tableStr += '</tr>';
		$.each(month, function(index, item) {
			tableStr += '<tr>';
			let i = 1
			tableStr += '<td>' + i + '</td>';
			tableStr += '<td>' + item['tName'] + '</td>';
			tableStr += '<td>' + item['peopleCount'] + '</td>';
			tableStr += '<td>' + item['shangkao'] + '</td>';
			tableStr += '<td>' + item['xiakao'] + '</td>';
			tableStr += '<td>' + item['shangLostKao'] + '</td>';
			tableStr += '<td>' + item['xiaLostKao'] + '</td>';
			tableStr += '<td>' + item['yichang'] + '</td>';
			i++;
			tableStr += '</tr>';
		});
		tableStr += '</table>';

		return tableStr;
	}

	function exporExcel(FileName, excel) {
		let sheetName = ["sheet1", "sheet2", 'sheet3'];

		var excelFile =
			"<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'>";
		excelFile += '<meta http-equiv="content-type" content="application/vnd.ms-excel; charset=UTF-8">';
		excelFile += '<meta http-equiv="content-type" content="application/vnd.ms-excel';
		excelFile += '; charset=UTF-8">';
		excelFile += "<head>";
		excelFile += "<!--[if gte mso 9]>";
		excelFile += "<xml>";
		excelFile += "<x:ExcelWorkbook>";
		excelFile += "<x:ExcelWorksheets>";
		excelFile += "<x:ExcelWorksheet>";
		excelFile += "<x:Name>";
		excelFile += "{考勤明细}";
		excelFile += "</x:Name>";
		excelFile += "<x:WorksheetOptions>";
		excelFile += "<x:DisplayGridlines/>";
		excelFile += "</x:WorksheetOptions>";
		excelFile += "</x:ExcelWorksheet>";
		excelFile += "</x:ExcelWorksheets>";
		excelFile += "</x:ExcelWorkbook>";
		excelFile += "</xml>";
		excelFile += "<![endif]-->";
		excelFile += "</head>";
		excelFile += "<body>";
		excelFile += excel;
		excelFile += "</body>";
		excelFile += "</html>";

		var uri = 'data:application/vnd.ms-excel;charset=utf-8,' + encodeURIComponent(excelFile);

		var link = document.createElement("a");
		link.href = uri;

		link.style = "visibility:hidden";
		link.download = FileName; //格式默认为.xls

		document.body.appendChild(link);
		link.click();
		document.body.removeChild(link);
	}
})
