
let loginIds = $("#sessoinId").val();
//소켓생성, 소켓요청경로 설정과 stomp 먹여주기
var sockJsForAlarm = new SockJS("/stomp/alarm");
var stompForAlarm = Stomp.over(sockJsForAlarm);
var alarmText = "";

stompForAlarm.connect({},function(sock){
 						
		console.log("알람용 STOMP 연결!");
		console.log("sock :: "+ sock);
		 
		//신고, 관심, 채팅 알람
		stompForAlarm.subscribe("/sub/alarm/chatAlarm", function(data){
						
			var person = JSON.parse(data.body).person;
			var alarmText = JSON.parse(data.body).alarmText;
			
				if(loginIds == person){	
					 $("#alarmArea").empty();
					 printAlarm(alarmText);
				}
		})
		
})



	//(공통)알람 프린트용
	function printAlarm(alarmText){
	
		$("#alarmArea").empty();
		
		var strForAlarm = '<div class="toast align-items-center" role="alert" aria-live="assertive" aria-atomic="true">';
					  strForAlarm += '<div class="d-flex">';
					  strForAlarm += '<div class="toast-body">';
					  strForAlarm += alarmText;
					  strForAlarm += '</div>';
					  strForAlarm += '<button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>';
					  strForAlarm += '</div>';
					  strForAlarm += '</div>'; 
				 
		$("#alarmArea").append(strForAlarm);
				 
		$(".toast").show();
		
	}

	//(공통)알람 닫기버튼
	$(document).on('click', '.btn-close', function () {
        $(this).closest('div.toast').hide(100);
        $(this).remove();
    })
