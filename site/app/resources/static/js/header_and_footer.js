$(document).ready(function() {
		$("#header_and_footer").append(
			`<div class="navbar">
				<h5 style="text-decoration: none" >&copy; UGATU 2021</h5>
			</div>
			<div class="topnav">
				<img href="index.html" src="../img/logo_cut.png" alt="УГАТУ">
				<a href="index.html" id="main">Главная</a>
				<a href="login.html" id="enter">Вход</a>
				<a href="registration.html" id="reg">Регистрация</a>
				<input id="search" type="text" placeholder="Поиск..">
				<a href="#" id="user_name" onclick="openNav()"></a>
			</div>
			<div id="mySidenav" class="sidenav">
				<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
				<a href="profile.html">Мои проекты</a>
				<a href="create_project.html">Создать проект</a>
				<a href="#" id="logout">Выйти</a>
			</div>`);

		let userName = localStorage.getItem('userName')
		if (userName !== null) {
			$("#enter").css('display','none');
			$("#reg").css('display','none');
		}
		$("#user_name").text(userName)

		if (document.title != 'УГАТУ'){
			$("#search").css('display','none');
		}
		$("#logout").click(function (){
			localStorage.clear()
			location.href = "login.html"
		})
});