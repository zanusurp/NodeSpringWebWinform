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
				<form role="form" action="/board/modify" method="POST">
					<div class="form-group">
						<label>B.No</label>
						<input class="form-control" name="bno" value='<c:out value="${board.bno }" />'  readonly="readonly" />
					</div>
					<div class="form-group">
						<label>RegDate</label>
						<input class="form-control" name="regDate" value='<fmt:formatDate pattern='yyyy/MM/dd' value="${board.regdate }" />' readonly="readonly">
					</div>
					
					<div class="form-group">
						<label>writer</label>
						<input class="form-control" name="writer" value='<c:out value="${board.writer }" />' readonly="readonly" />
					</div>
					<div class="form-group">
						<label>Title</label> <input class="form-control" name="title"   value='<c:out value="${board.title}" />'>
					</div>
					<div class="form-group">
						<label>Text area</label>
						<textarea rows="3" class="form-control" name="content"   ><c:out value="${board.content }" ></c:out></textarea>
					</div>
					
					
					<!--  button ======================================================= -->
					<button type="submit" data-oper='modify' class="btn btn-default">Modify</button>
					<button type="submit" data-oper='remove' class="btn btn-danger">Remove</button>
					<button type="submit" data-oper='list' class="btn btn-info">List</button>
					
					<!--  button -->
				</form>
			</div>
		</div>
		
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	var formObj = $("form");
	
	$('button').on("click", function(e){
		e.preventDefault();
		var operation = $(this).data("oper");
		console.log(operation);
		
		if(operation === 'remove'){
			formObj.attr("action","/board/remove");
		}else if(operation === 'list'){
			//self.location="/board/list";
			formObj.attr("action","/board/list").attr("method","get");
			formObj.empty();
			
		}
		formObj.submit();
	});
});

</script>
<%@ include file="../../includes/footer.jsp" %>