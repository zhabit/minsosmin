//hbuilder编辑器
var ma_params = {
	account: localStorage.getItem("accountName")
};

var currurl = "//eos.h3c.com/";
//var baseurl = "//api.eos-ts.h3c.com/user/v1.0/"; //"//api.eos-ts.h3c.com/user/v1.0/"
var baseurl = "//api.eos.h3c.com/devops/v1.0/";
//var baseurl = "http://127.0.0.1:8020/";

var isBug = true;

function getToken() {
	var token = '';
	if (window.localStorage) {
		token = localStorage.getItem("token")
	} else {
		token = $.cookie("token");
	}
	if (token != null && token != "") {
		return token;
	}
	return 'h3c-web';
}

function getDevopsAccountName() {
	var devOpsAccountName = '';
	if (window.localStorage) {
		devOpsAccountName = localStorage.getItem("devOpsAccountName")
	} else {
		devOpsAccountName = $.cookie("devOpsAccountName");
	}
	if (devOpsAccountName != null && devOpsAccountName != "") {
		return devOpsAccountName;
	}
	return 'h3c';
}

function dataFunction(response) {

	if (response.flag == false) {
		alert("请求异常,请稍后再试");
		if (!isBug) {
			top.location.href = currurl + "index.html"
		}
		return;
	}
	if (response.code == 20204) {
		alert(response.flag + " : " + response.message);
		if (!isBug) {
			top.location.href = currurl + "login.html"
		}
		return;
	}
}

function loginFunction() {
	top.location.href = currurl + "login.html"
}

function logoutFunction() {

	if (window.localStorage) {
		//浏览器支持localstorage
		localStorage.setItem("devopsUserName", '');
	} else {
		//浏览器不支持localstorage
		$.cookie('devopsUserName', '');
	}

	top.location.href = currurl + "login.html"
}

function errorFunction() {
	alert("请求异常,请稍后再试");
	if (!isBug) {
		top.location.href = currurl + "index.html"
	}
}

function ajaxGet(action, callbackFn) {
	var URL = baseurl + action;
	$.ajax({
		type: 'get',
		headers: {
			'Authorization': 'Bearer ' + getToken(),
			'Content-Type': 'application/json'
		},
		url: URL,
		cache: false,
		processData: false,
		contentType: false
	}).success(function(response) {
		dataFunction(response);
		callbackFn(response);
	}).error(function(e) {
		errorFunction();
	});
}

function ajaxPostFormData(action, callbackFn, data) {
	var URL = baseurl + action;
	$.ajax({
		type: 'post',
		headers: {
			'Authorization': 'Bearer ' + getToken(),
			'Content-Type': 'application/json'
		},
		url: URL,
		data: data,
		cache: false,
		processData: false,
		contentType: false
	}).success(function(response) {
		dataFunction(response);
		callbackFn(response);
	}).error(function() {
		errorFunction();
	});
}


function ajaxPost(action, callbackFn, data, curl) {
	var curl = arguments[3] ? arguments[3] : baseurl;
	var URL = curl + action;
	$.ajax({
		type: 'post',
		headers: {
			'Authorization': 'Bearer ' + getToken(),
			'Content-Type': 'application/json'
		},
		url: URL,
		data: JSON.stringify(data),
		cache: false,
		processData: false,
		contentType: false
	}).success(function(response) {
		dataFunction(response);
		callbackFn(response);
	}).error(function(event, xhr, options, exc) {
		errorFunction();
	});
}

function ajaxPostParam(action, callbackFn, data) {
	var network = true;
	if (network) {
		var URL = baseurl + action;
		$.ajax({
			type: 'POST',
			headers: {
				'Authorization': 'Bearer ' + getToken()
			},
			url: URL,
			data: data,
			datatype: "json",
			contentType: "application/x-www-form-urlencoded",
		}).success(function(response) {
			dataFunction(response);
			callbackFn(response.data);
		}).error(function(event, xhr, options, exc) {
			errorFunction();
		});
	}
}

function isString(str) {
	return (typeof str == 'string') && str.constructor == String;
}

//增加一天
function addDate(time) {
	//加一天
	var timestamp = Date.parse(new Date(time));
	timestamp = timestamp / 1000;
	timestamp += 86400; //加一天
	var newTime = new Date(timestamp * 1000).format('yyyy-MM-dd');
	return newTime;
}

//日期格式
Date.prototype.format = function(format) {
	var date = {
		"M+": this.getMonth() + 1,
		"d+": this.getDate(),
		"h+": this.getHours(),
		"m+": this.getMinutes(),
		"s+": this.getSeconds(),
		"q+": Math.floor((this.getMonth() + 3) / 3),
		"S+": this.getMilliseconds()
	};
	if (/(y+)/i.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
	}
	for (var k in date) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ?
				date[k] : ("00" + date[k]).substr(("" + date[k]).length));
		}
	}
	return format;
}

function getMonthFirstday() {
	var curr_time = new Date();
	var strDate = curr_time.getFullYear() + "-";
	var month = curr_time.getMonth() + 1;
	month = month < 10 ? '0' + month : '' + month; // 如果是1-9月，那么前面补0
	day = "01";
	strDate += month + "-";
	strDate += day + " ";
	return strDate;
}

function getTomorrowday() {

	var curr_time = new Date();
	var nowTime = curr_time.getTime();
	var ms = 24 * 3600 * 1000 * 1;
	curr_time.setTime(parseInt(nowTime + ms));

	var strDate = curr_time.getFullYear() + "-";
	var month = curr_time.getMonth() + 1;
	month = month < 10 ? '0' + month : '' + month; // 如果是1-9月，那么前面补0
	day = curr_time.getDate();
	strDate += month + "-";
	strDate += day + " ";
	return strDate;
}

function getToday() {

	var curr_time = new Date();
	var nowTime = curr_time.getTime();
	var strDate = curr_time.getFullYear() + "-";
	var month = curr_time.getMonth() + 1;
	var day = curr_time.getDate();
	month = month < 10 ? '0' + month : '' + month; // 如果是1-9月，那么前面补0
	day = day < 10 ? '0' + day : '' + day;
	strDate += month + "-";
	strDate += day + " ";
	return strDate;
}

function getCurrTime() {
	var curr_time = new Date();
	var strDate = curr_time.getFullYear() + "-";
	var month = curr_time.getMonth() + 1;
	var day = curr_time.getDate();
	var hour = curr_time.getHours();
	var min = curr_time.getMinutes();
	month = month < 10 ? '0' + month : '' + month; // 如果是1-9月，那么前面补0
	day = day < 10 ? '0' + day : '' + day;
	hour = hour < 10 ? '0' + hour : '' + hour;
	min = min < 10 ? '0' + min : '' + min;
	strDate += month + "-";
	strDate += day + " ";
	strDate += hour + ":";
	strDate += min;
	return strDate;
}

function IsInArray(arr, val) {
	var testStr = ',' + arr.join(",") + ",";
	return testStr.indexOf("," + val + ",") != -1;
};

$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [o[this.name]];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};
