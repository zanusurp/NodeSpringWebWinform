<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../includes/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Board Register</h1>
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board Read</div>
			<div class="panel-body">
					
					<div class="form-group">
						<label>B.No</label>
						<input class="form-control" name="bno" value='<c:out value="${board.bno }" />' readonly="readonly" />
					</div>
					<div class="form-group">
						<label>writer</label>
						<input class="form-control" name="writer" value='<c:out value="${board.writer }" />' readonly="readonly" />
					</div>
					<div class="form-group">
						<label>Title</label> <input class="form-control" name="title" readonly="readonly" value='<c:out value="${board.title}" />'>
					</div>
					<div class="form-group">
						<label>Text area</label>
						<textarea rows="3" class="form-control" name="content" readonly="readonly" ><c:out value="${board.content }" ></c:out></textarea>
					</div>
					
					
					
					
					<%-- <button class="btn btn-default" data-oper="modify" onclick="location.href='/board/modify?bno=<c:out value="${board.bno }" />'">Modify</button>
					<button class="btn btn-info" data-oper="list" onclick="location.href='/board/list'" >List</button> --%>
					<button class="btn btn-default" data-oper="modify" >Modify</button>
					<button class="btn btn-info" data-oper="list" >List</button>
					
					<form id="operForm" action="/board/modify" method="get">
						<input type="hidden" id="bno" name="bno" value='<c:out value="${board.bno }" />' />
						<input type="hidden" name="pageNum" value='<c:out value="${cri.pageNum }" />' />
						<input type="hidden" name="amount" value='<c:out value="${cri.amount }" />' />
						<input type="hidden" name="type" value="${pageMaker.cri.type }" />
						<input type="hidden" name="keyword" value="${pageMaker.cri.keyword }" />
					</form>
					
			</div>
		</div>
		
	</div>
</div>


<!-- 경계선=============================================================================================== -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">						
			<div class="panel-heading">
				<i class="fa fa-comments fa-fw"></i>Reply
				<button id="addReplyButton" class="btn btn-primary btn-xs pull-right">New</button>
			</div>
<!-- 경계선=============================================================================================== -->			
			<div class="modal fade" id="replyModal" tabindex="-1" role="dialog" aria-labeledby="replyModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							<h4 class="modal-title" id="replyModalLabel">댓글</h4>
						</div>
						<div class="modal-body">
							<div class="form-group">
								<label>Reply</label>
								<input class="form-control" name="reply" value="test new Reply">
							</div>
							<div class="form-group">
								<label>Replyer</label>
								<input class="form-control" name="replyer" value="test Replyer">
							</div>
							<div class="form-group">
								<label>Reply Date</label>
								<input class="form-control" name="replydate" value="">
							</div>
						</div>
						<div class="modal-footer">
							<button id="modalModBtn" type="button" class="btn btn-warning">수정</button>
							<button id="modalRemoveBtn" type="button" class="btn btn-danger">삭제</button>
							<button id="modalRegisterBtn" type="button" class="btn btn-default" data-dismiss="modal">등록</button>
							<button id="modalCloseBtn" type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
						</div>
					</div>
				</div>
			</div>
<!-- 경계선 : 댓글 달리는 곳 =============================================================================================== -->
			<div class="panel-body">
				<ul class="chat">
					<!-- <li class="left clearfix" data-rno='12'>
						<div class="header">
							<strong class="primary-font">데모</strong>
							<small class="pull-right text-muted">2018-01-01 11:11</small>
						</div>
						<p>미리보기용</p>
					</li> -->
				</ul>
			</div>
			<div class="panel-footer">
			
			</div>
		</div>
	</div>	
</div>
<!-- 경계선=============================================================================================== -->
<script type="text/javascript" src="/resources/js/reply.js"></script>
<script type="text/javascript">
	/* console.log("ajax ==========${board.bno}");
	var bnoValue = '${board.bno}';
	replyService.add(
		{reply:"JS test", replyer:"tester",bno:bnoValue},		
		function(result){
			alert("Result:" + result);
		}
	);
	replyService.getList({bno:bnoValue, page:1},function(list){
		for(var i = 0, len = list.length||0; i < len; i++){
			console.log(list[i]);
		}
	});
	replyService.remove(4,function(count){
		console.log(count);
		if(count === "success"){
			alert("removed");
		}
	},function(err){
		alert("error");
	});
	
	replyService.update(
			{rno:5,bno:bnoValue,reply:"Ajax modified again"},
			function(result){
				alert("callback : modified");
			}
	)
	replyService.get(5, function(data){
		console.log(data);
	});
	
	
	console.log("=========="); */
	// 경계선=============================================================================================== 
	$(document).ready(function(){
		var pageNum = 1;
		var replyPageFooter = $(".panel-footer");
		
		function showReplyPage(replyCnt){
			var endNum = Math.ceil(pageNum/10.0)*10;
			var startNum = endNum -9;
			
			var prev = startNum != 1;
			var next = false;
			
			if(endNum*10 >= replyCnt){
				endNum = Math.ceil(replyCnt/10.0);
			}
			if(endNum*10< replyCnt){
				next = true;
			}
			
			let str_page = "<ul class='pagination pull-right'>";
			if(prev){
				str_page += "<li class='page-item'><a class='page-link' href='"+(startNum-1)+"'>Previous</a> </li>"
			}
			for(var i = startNum; i<= endNum; i++){
				var active = pageNum==i?"active":"";
				str_page += "<li class='page-item "+active+" '><a class='page-link' href='"+i+"'>"+i+"</a></li>";
			}
			if(next){
				str_page += "<li class='page-item'><a class='page-link' href='"+(endNum+1)+"'>Next</a></li>";
			}
			
			str_page += "</ul>";
			console.log(str_page);
			
			replyPageFooter.html(str_page);
		}
		//이동
		replyPageFooter.on("click","li a", function(e){
			e.preventDefault();
			console.log("rpl page click");
			var targetPageNum = $(this).attr("href");
			console.log("target : "+targetPageNum);
			pageNum=targetPageNum
			showList(pageNum);
		});
		
		/////////////
		
		var operForm = $("#operForm");
		$("button[data-oper='modify']").on("click",function(e){
			operForm.attr("action","/board/modify").submit();
		});
		$("button[data-oper='list']").on("click",function(e){
			//operForm.attr("action","/board/list").submit();
			operForm.find("#bno").remove();
			operForm.attr("action","/board/list");
			operForm.submit();
		});
		// 경계선=============================================================================================== 
		var bnoValue = '<c:out value="${board.bno}" />';
		var replyUL = $(".chat");
		//댓글 목록 실행
		showList(1);
		//댓글 목록
		function showList(page){
			replyService.getList({bno:bnoValue,page:page||1}, function(replyCnt,list){ //param은 {}으로 묶어보내도록
				//
				console.log("ReplyCnt : "+replyCnt);
				console.log(list);
				if(page==-1){
					pageNum = Math.ceil(replyCnt/10.0);
					showList(pageNum); 
					return;
				}
				//
				var str = "";
				if(list == null || list.length ==0){
					replyUL.html("");
					return;
				}
				for(var i=0,len = list.length || 0; i<len;i++){
					str += "<li class='left clearfix' data-rno='"+list[i].rno+"'>";
					str += "<div><div class='header'><strong class='primary-font'>"+list[i].replyer+"</strong>";
					console.log(list[i].replydate);
					
					str += "<small class='pull-right text-muted'>"+replyService.displayTime(list[i].replydate)+"</small></div>";
					str += "<p>"+list[i].reply+"</p></div></li>";
				}
				replyUL.html(str);
				
				showReplyPage(replyCnt);
				
			});
		}
		//댓글 추가
		var modal = $(".modal");
		var modalInputReply = modal.find("input[name='reply']");
		var modalInputReplyer = modal.find("input[name='replyer']");
		var modalInputReplyDate = modal.find("input[name='replydate']");
		
		var modalModBtn = $("#modalModBtn");
		var modalRemoveBtn = $("#modalRemoveBtn");
		var modalRegisterBtn = $("#modalRegisterBtn");
		
		$("#addReplyButton").on("click",function(e){
			modal.find("input").val("");
			modalInputReplyDate.closest("div").hide();
			modal.find("button[id != 'modalCloseBtn']").hide();
			
			modalRegisterBtn.show();
			
			$(".modal").modal("show");
		});
		
		modalRegisterBtn.on("click", function(e){
			var reply = {
					reply:	modalInputReply.val(),
					replyer: modalInputReplyer.val(),
					bno: bnoValue
				};
			replyService.add(reply, function(result){
				alert(result);
				modal.find("input").val();
				modal.modal("hide");
				
				showList(-1);
			});
		});
		
		$(".chat").on("click","li",function(e){
			var rno = $(this).data("rno");
			console.log(rno);
			
			replyService.get(rno, function(reply){
				console.log(reply);
				modalInputReply.val(reply.reply);
				modalInputReplyer.val(reply.replyer).attr("readonly","readonly");
				modalInputReplyDate.val(replyService.displayTime(reply.replydate)).attr("readonly","readonly");
				modal.data("rno",reply.rno);
				
				modal.find("button[id != 'modalCloseBtn']").hide();
				modalModBtn.show();
				modalRemoveBtn.show();
				$(".modal").modal("show");
			});
		});
		//mod
		modalModBtn.on("click",function(e){
			var reply = {rno:modal.data('rno'), reply:modalInputReply.val()};
			replyService.update(reply, function(result){
				alert(result);
				modal.modal("hide");
				showList(pageNum);
			});
		});
		//del
		modalRemoveBtn.on("click", function(e){
			var rno = modal.data("rno");
			replyService.remove(rno, function(result){
				alert(result+"replcyControllerResponseEntity");
				modal.modal("hide");
				showList(pageNum);
			});
		});
	});
</script>


<%@ include file="../../includes/footer.jsp" %>