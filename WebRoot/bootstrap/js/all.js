var all_local_data;
function ym() {
	var date = new Date();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();
	$('#ym_title')
			.html(
					year
							+ '年'
							+ month
							+ '月时间表'
							+ '<button class="btn btn-danger pull-right" onclick="exprot()"><i class="glyphicon glyphicon-share-alt" ></i>'
							+ '<button class="btn btn-warning pull-right" style="margin-right:10px;" onclick="window.location.reload()"><i class="glyphicon glyphicon-repeat" ></i>'
							+ '</button><button class="btn btn-primary pull-right" style="margin-right:10px;" data-toggle="modal" data-target="#saveModal">添加任务</button>');
}
var exprot = function(){
	var date = new Date();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();
	window.location.href='report.do?month='+year+"-"+(month<10 ?'0':'')+month;
}
var timeskeys = {};
function loadvalue() {
	timeskeys = {};
	var date = new Date();
	var day = date.getMonth() + 1;
	var year = date.getFullYear();
	$.getJSON(root + '/act.do?method=worktime&ym=' + year + '-' + (day < 10 ? '0' : '') + day, function(data) {
		var html = '';
		all_local_data = data;
		var hours = 0;
		for (var i = 0; i < data.length; i++) {
			var itm = data[i];
			var hr = itm['hour'];
			hr = hr == "null" ? 0 : parseFloat(hr);
			var r = '<a href="javascript:void(0)" onclick="done('+ itm['uuid'] + ')">完成</a>';
			
			var daystime = itm['start'].substring(0,10);
			if(timeskeys[daystime] == undefined){
				timeskeys[daystime] = true;
			}else{
				itm['start'] = itm['start'].substring(11);
			}


			html += '<tr data="' + itm['uuid'] + '"><td align="right">' + itm['start'] + '</td><td>' + itm['project'] + '</td><td>'
					+ itm['task'] + '</td><td>' + hr + '</td><td>'+ (hr == 0 ? r : '')+'</td></tr>';
			hours += hr
		}
		
		$('#total').text(hours.toFixed(2));
		$('#result').html(html);

	});
}
function done(uuid) {
	
	var itm = null;
	for (var i = 0; i < all_local_data.length; i++) {
		if (all_local_data[i].uuid == uuid) {
			itm = all_local_data[i];
			break;
		}
	}
	
	BootstrapDialog.show({
        title: '完成确认',
        message: '你确定要完成任务:'+itm.task,
        buttons: [{
            label: '确定完成',
            action: function(dialog) {
            	itm["end"] = time();
        		
        		var mins = parseInt((date(itm["end"]).getTime() - date(itm["start"]).getTime()) / 1000 / 60);
        		var hour = (mins / 60).toFixed(2);
        		itm['hour'] =  hour;
        		$.post(root + '/act.do', itm, function(data) {
        			loadvalue();
        			 dialog.close();
        		});
            }
        }, {
            label: '关闭',
            action: function(dialog) {
                dialog.close();
            }
        }]
    });
}

function date(value) {
	var str = value.replace(/-/g, "/");
	return new Date(str);
}

function addNewOne() {
	var startTime = $('#startTime').val();
	var project = $('#project').val();
	var task = $('#task').val();

	var obj = {};
	obj['start'] = startTime;
	obj['project'] = project;
	obj['task'] = task;

	$.post(root + '/act.do', obj, function(data) {
		if (data == 'ok') {
			loadvalue();
			$('#saveModal').modal('hide');
		} else {
			BootstrapDialog.show({
       			 title: '提示',
       			 message: '添加失败'
       			});
		}
	});

}

function loadprojects() {
	$.get(root + '/act.do?method=projects', function(data) {
		$('#project').html(data);
	});
}

function time() {
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var hour = date.getHours();
	var min = date.getMinutes();
	var sec = date.getSeconds();
	return year + '-' + (month < 10 ? '0' : '') + month + "-" + (day < 10 ? '0' : '') + day + " "
			+ (hour < 10 ? '0' : '') + hour + ":" + (min < 10 ? '0' : '') + min + ':' + (sec < 10 ? '0' : '') + sec;
}

$(function() {
	ym();
	loadvalue();
	loadprojects();

	$('#saveModal').on('shown.bs.modal', function() {
		$('#startTime').val(time());
		$('#task').val('');
		$('#endTime').val('');
	})

});