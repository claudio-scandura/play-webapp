$(document).ready(function() {
	loadAllComments();
	$("#postCommentButton").click(function () {
		 $.ajax({
 	        type : 'POST',
 	        url : "/addUserAjax",
 	        contentType : "application/json",
 	        data: JSON.stringify(
 	        			{
	 	        			"username" : $("#username").val(),
	 	        			"comment" : $("#comment").val()
 	        			}
 	        		),
 	        success : function(data) {
 	        			  $('<li></li>').text(data.username+' commented: '+data.comment).appendTo($('#comments'))
 	               		},
 	        complete : function() {
								
 	        			}
 	    });
	});
});

function loadAllComments() {
	$.ajax({
		type : 'GET',
		url : "/allComments",
		success : function(data) {
			var existingComments = data.existingComments;
			$.each(existingComments, function() {
				$('<li></li>').text(this.username+' commented: '+this.comment).appendTo($('#comments'))
			});
		},
		complete : function() {
			
		}
	});
}