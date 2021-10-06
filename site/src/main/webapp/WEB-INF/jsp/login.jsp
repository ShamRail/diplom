<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="generator" content="Hugo 0.83.1">
    <title>Login</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/5.0/examples/sign-in/">

    <!-- Favicons -->
    <link rel="manifest" href="https://getbootstrap.com/docs/5.0/assets/img/favicons/manifest.json">
    <meta name="theme-color" content="#7952b3">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">


    <!-- Custom styles for this template -->

    <style>
        <%@include file='css/signin.css' %>

        @media (min-width: 768px) {
        }

        .black-background {
            background-color: #212529;
        }

        .white {
            color: #fff;
        }

        .border-black {
            border-color: #212529;
        }

        button {
            margin: 5px 5px 5px 5px;
        }
    </style>

</head>

<body class="text-center">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf"
        crossorigin="anonymous"></script>
<sec:authorize access="isAuthenticated()">
    <% response.sendRedirect("/"); %>
</sec:authorize>

<main class="form-signin">

    <form method="POST" action="${pageContext.request.contextPath}/login">
        <h1 class="h3 mb-3 fw-normal">Вход в аккаунт</h1>

        <div class="form-floating">
            <input name="username" class="form-control" type="text" id="floatingInput" placeholder="Логин"
                   autofocus="true"/>
            <label for="floatingInput">Логин</label>
        </div>

        <div class="form-floating">
            <input name="password" class="form-control" type="password" id="floatingPassword" placeholder="Пароль"/>
            <label for="floatingPassword">Пароль</label>
        </div>

        <button class="w-100 btn btn-lg btn-primary black-background white border border-black" type="submit">Войти
        </button>
    </form>

    <button onclick="document.location='registration'" type="button"
            class="w-100 btn btn-lg btn-primary btn-warning border-black">Зарегистрироваться
    </button>


</main>

</body>
</html>