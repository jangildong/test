<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="mypage_image"></div>
<div class="mypage_select">
	<div class="mypage_userinfo">
		<a href=""><p class="mypage_userinfo_select">회원정보</p></a>
	</div>
</div>
</nav>
<!-- 메인 시작 -->
<main class="mypage">
	<div id="page">
	<input type="hidden" value="${loginInfo.social }" id="mypageSocial">
		<form action="memberSocialSubmit.mp" class="customer_update_form form" method="post" enctype="multipart/form-data">
			<h1>회원 정보 수정</h1>
				<input type="hidden" name="customer_email"
					   value="${loginInfo.customer_email}"
					   class="join_email" id="email" readonly> 
				<input type="text" name="customer_name"
							value="${loginInfo.customer_name}" class="join_name" id="new_name"
							onkeyup="checkName()">
			<div id="nameError"></div>
			<div class="join_company">
				<h3>사업자 이신가요?</h3>
				<div>
					<label><input type="radio" name="admin" value="M"
						<c:if test="${loginInfo.admin eq 'M'}">checked</c:if>>예</label> 
						<label><input type="radio" name="admin" value="C"
										<c:if test="${loginInfo.admin eq 'C'}">checked</c:if>>아니오</label>
					<c:if test="${loginInfo.admin eq 'A'}"> 
						<label><input type="radio" name="admin" value="A" checked>관리자</label>
					</c:if>
				</div>
			</div>
			<div class="join_addr">
				<h3>지역을 선택해주세요.</h3>
				<select name="city" id="city" <c:if test="${!empty loginInfo.city}"></c:if>></select>
				<select name="addr" id="gugun"></select>
			</div>
			<div class="join_profile">
				<h3>프로필 사진</h3>
				<div class="mypage_user_image">
					<img
						src=<c:if test="${loginInfo.customer_picture == null}">
			          	  "resources/pictures/profiles/noImage.png" 
			          	</c:if>
						<c:if test="${loginInfo.customer_picture != null}">
			          	  "${loginInfo.customer_picture}"
			          	</c:if>
						alt="ProfileImage" onclick="choose_image()"
						class="mypage_user_images">
				</div>
			</div>
			<input type="hidden" name="customer_picture" value="${vo.customer_picture}" /> 
			<input type="file" name="image_file" style="display: none" class="image_upload" accept="image/*" />
			<button type="button" onclick="member_update()">수정하기</button>
		</form>
	</div>
</main>
<script>
const image = document.querySelector('.mypage_user_image');
const input_file = document.querySelector('.image_upload');

//이미지 파일 누를 때 input_file도 같이 클릭
function choose_image() {
	image.addEventListener('click', () => {
		input_file.click();
	});
}

//첨부파일 선택시 처리
$(document).on('change', '.image_upload', function() {
	var attached = this.files[0];
	if (attached) { // 첨부된 파일이 있을 경우
		$('.mypage_user_images').html('<img src="" />');
		var reader = new FileReader();
		reader.onload = function(e) {
			$('.mypage_user_images').attr('src', e.target.result);
		}
		reader.readAsDataURL(attached);
	}
})
</script>
<script type="text/javascript">
let email = document.getElementById("email");
let oldPw = document.getElementById("old_pw");
let wrtPw = document.getElementById("written_pw");
let pw    = document.getElementById("new_pw");
let pw2   = document.getElementById("new_pw2");
let name  = document.getElementById("new_name");
let mypageSocial = document.getElementById("mypageSocial");

let wpToken = false;
let pwToken = true;
let p2Token = true;
let nmToken = true;

const regPw    = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}/
const regName  = /^[가-힣]{2,4}$/

function checkName() {
	if(!regName.test(name.value)) {
		document.getElementById("nameError").innerText = "2-4자의 한글만 가능합니다.";
		document.getElementById("nameError").style.color = "red";
		nmToken = false;
	}else {
		document.getElementById("nameError").innerText = "이름이 입력되었습니다.";
		document.getElementById("nameError").style.color = "green";
		nmToken = true;
	}
}

//회원정보 수정 버튼 누를 시
function member_update() {
	if(!nmToken) {
		alert("이름을 입력하세요.");
		name.focus();
	}else {
		$('form').submit();
	}
	
}
</script>