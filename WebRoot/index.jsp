<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>工作时间管理</title>
<script type="text/javascript">var root='<%=request.getContextPath()%>';</script>
<link href="<%=request.getContextPath()%>/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="<%=request.getContextPath()%>/bootstrap/js/jquery-2.1.3.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/bootstrap/bootstrap3-dialog/bootstrap-dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/bootstrap/js/all.js"></script>
<style type="text/css">
</style>
</head>
<body>
	<div class="modal" style="display: block; margin: 0 auto;">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="ym_title">2015年1月时间表</h4>
				</div>
				<div class="modal-body">
					<table class="table table-hover table-condensed">
						<thead>
							<tr>
								<th width="20%">开始时间</th>
								<th width="10%">项目</th>
								<th width="50%">任务</th>
								<th width="5%">时间</th>
								<th width="10%">操作</th>
							</tr>
						</thead>
						<tbody id="result"></tbody>
					</table>
				</div>
				<div class="modal-footer">
					<table style="width: 100%;">
						<tr>
							<td style="padding-right: 30px;">合计(小时)</td>
							<td width="10%" align="left" id="total">0.0</td>
						</tr>
					</table>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>

	<div class="modal fade" id="saveModal" tabindex="-1" style="background: rgba(80, 80, 80, 0.7);" role="dialog"
		aria-labelledby="saveModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">添加</h4>
				</div>
				<div class="modal-body">
					<form>
						<div class="form-group">
							<label for="exampleInputEmail1">开始时间</label> <input type="datetime-local" class="form-control" id="startTime" placeholder="开始时间">
						</div>
						<div class="form-group">
							<label for="exampleInputPassword1">项目</label> 
							<select class="form-control"  name="project" id="project"></select>
						</div>
						<div class="form-group">
							<label for="exampleInputPassword1">任务</label> <input type="text" class="form-control" id="task" placeholder="任务">
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" onclick="addNewOne()">保存</button>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
