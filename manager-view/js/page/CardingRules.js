layui.use(['form', 'table', 'laydate'], function() {
	var laydate = layui.laydate,
		form = layui.form,
		table = layui.table;
	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'time',
			format: 'HH:mmH点m分'
		});
	})
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: '../json/date.json',
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'name',
					title: '名称',
					align: 'center'
				},
				{
					field: 'start',
					title: '时间',
					align: 'center'
				}
			]
		],
		page: false,
		parseData: function(res) {
			var arr, code, total;
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
	$(document).on('click', '.Supplier', function() {
		reduceSupplier(this);
	});
	$(document).on('click', '#add', function() {
		setSupplier();
		$('.time').each(function() {
			laydate.render({
				elem: '.time',
				type: 'time',
				format: 'HH:mmH点m分',
				zIndex: 99999999
			});
		})
	});
	var number = 1;

	function setSupplier() {
		var divContent = "";
		number++
		divContent += '<div class="layui-form-item" dataid1="' + number + '">';
		divContent += '<div class="layui-inline">';
		divContent += '<label class="layui-form-label">名称：</label>';
		divContent += '<div class="layui-input-inline widths">';
		divContent += '<input type="text" name="address" class="layui-input">';
		divContent += '</div>';
		divContent += '<label class="layui-form-mid">时间：</label>';
		divContent += '<div class="layui-input-inline" >';
		divContent += '<input type="text" name="address" class="layui-input time" placeholder="选择时间">';
		divContent += '</div>';
		divContent += '<div class="layui-form-mid layui-word-aux"><i class="layui-icon layui-icon-close add Supplier"></i> </div>';
		divContent += '</div>';
		divContent += '</div>';
		$('#card').append(divContent);
	}

	function reduceSupplier(obj) {
		console.log(obj)
		var div = $(obj.parentNode.parentNode.parentNode).attr('dataid1')
		console.log(div)
		var str = $('#card').find('.layui-form-item').length;
		for(var i = 0; i < str; i++) {
			if($('#card').find('.layui-form-item').eq(i).attr('dataid1') == div) {
				console.log(456)
				$('#card').find('.layui-form-item').eq(i).remove()
			}

		}
	}
	table.render({
		elem: '#today',
		method: "get",
		async: false,
		url: '../json/date.json',
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'name',
					title: '名称',
					align: 'center'
				},
				{
					field: 'start',
					title: '时间',
					align: 'center'
				},
				{
					field: 'deptdescribe',
					title: '修改',
					align: 'center',
					toolbar: '#operation'
				}
			]
		],
		page: false,
		parseData: function(res) {
			var arr, code, total;
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