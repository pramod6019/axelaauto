<html>
<head>
<link rel="stylesheet" type="text/css" href="../Library/qtip/jquery.qtip.css"/>
<script type="text/javascript" src="../Library/jquery.js"></script>
<script type="text/javascript" src="../Library/qtip/jquery.qtip.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	
	$('.manhours').each(function(){
		$(this).qtip({
			content:$(this).text()+"_"+$(this).attr('id')
		});
	});
	
});
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>


<body>


<table border="1" cellpadding="15">
<tr id="100" class="manhours"><td>aaaa</td><td>bbb</td><td>ccc</td></tr>
<tr id="101" class="manhours"><td>ddd</td><td>eee</td><td>fff</td></tr>
<tr id="102" class="manhours"><td>ggg</td><td>hhh</td><td>iii</td></tr>
</table>

</body>
</html>
