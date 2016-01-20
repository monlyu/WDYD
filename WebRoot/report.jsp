<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>REPORT FOR <%=request.getAttribute("MONTH") %></title>
<style type="text/css">
table caption {
	text-align: left;
	font-size: 14px;
	padding: 10px 5px;
	color: #0070C0;
	font-weight: bolder;
	font-style: italic;
}

table.report {
	width: 100%;
	font-family: Courier;
	font-size: 14px;
	color: #3A3838;
}

.table>thead>tr>th {
	border-bottom: 2px solid #ddd;
	vertical-align: bottom;
	text-align: left;
	color: #0070C0;
}

.table>tfoot>tr>th {
	border-bottom: 2px solid #ddd;
	border-top: 2px solid #ddd;
	vertical-align: bottom;
	text-align: left;
	padding: 8px;
}

.table>tbody>tr>td, .table>tbody>tr>th, .table>thead>tr>td, .table>thead>tr>th
	{
	border-top: 1px solid #ddd;
	line-height: 1.42857;
	padding: 8px;
	vertical-align: top;
}
</style>
</head>
<body>

	<table class="report table" cellpadding="0" cellspacing="0">
		<caption>REPORT FOR <%=request.getAttribute("MONTH") %></caption>
		<thead>
			<tr>
				<th width="15%">DATE</th>
				<th width="15%">PROJECT</th>
				<th width="10%">HOUR</th>
				<th>TASK</th>
			</tr>
		</thead>
		<tbody><%=request.getAttribute("WORK") %>
		</tbody>
		<tfoot>
			<tr>
				<th>TOTAL</th>
				<th colspan="3"><%=request.getAttribute("TOTAL") %></th>
			</tr>
		</tfoot>
	</table>

</body>
</html>