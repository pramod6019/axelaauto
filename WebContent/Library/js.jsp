<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
 <script src="../assets/js/bs-select.js" type="text/javascript"></script>

<!-- new timepicker -->
<script type="text/javascript" src="../assets/js/bootstrap-material-datetimepicker.js" async></script>
<!-- new timepicker -->

<!-- SELECT 2 -->
<script src="../assets/js/select2.full.min.js" type="text/javascript"></script>
<script src="../assets/js/components-select2.min.js" type="text/javascript"></script>

<!-- ========== -->
<!-- DROPDOWN LIST MULTI SELECT -->
<script src="../assets/js/bootstrap-multiselect.js" type="text/javascript" ></script>
<!-- ========================= -->
<script src="../assets/js/footable.js" type="text/javascript" async></script>
<script type="text/javascript" src="../Library/Validate.js" async></script>
<script type="text/javascript" src="../Library/dynacheck.js" async></script>
<script>

if($(window).width() > 768)
{	
	var window_width = $(window).width();
	window_width = 0.8*window_width;
	$(".modal-80").css({'width':window_width});
}

$("#Hintclicktocall, #Hintclicktocall80").on('shown.bs.modal',function(){
	   $('.datetimepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY HH:mm', switchOnClick : true, clearButton : true });
	   $('.datepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY', switchOnClick : true,weekStart : 0, time: false,  clearButton : true });
	   $('.timepicker').bootstrapMaterialDatePicker({ format : 'HH:mm', switchOnClick : true, date: false, clearButton : true });
});

 $(function() {
	 $(".hint").hide();
	 FormElements();
	 
	 /* This code is for executive popover arrow to show properly */
	 $(document).ajaxComplete(function(){
		 $(".popovers").next().children().eq(0).css('top','20%');
	 });
});
</script> 