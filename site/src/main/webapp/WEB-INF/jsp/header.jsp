<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="generator" content="Hugo 0.82.0">
    <title>Главная</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">

    <link rel="canonical" href="https://getbootstrap.com/docs/5.0/examples/headers/">


    <!-- Bootstrap core CSS -->
    <link href="../assets/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>

        <%@include file='css/headers.css' %>
        .bd-placeholder-img {
            font-size: 1.125rem;
            text-anchor: middle;
            -webkit-user-select: none;
            -moz-user-select: none;
            user-select: none;
        }

        @media (min-width: 768px) {
            .bd-placeholder-img-lg {
                font-size: 3.5rem;
            }
        }
    </style>


    <!-- Custom styles for this template -->
    <link href="headers.css" rel="stylesheet">
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf"
        crossorigin="anonymous"></script>
<script src="../assets/dist/js/bootstrap.bundle.min.js"></script>

<div class="b-example-divider"></div>
<header class="p-3 bg-dark text-white">
    <div class="container">


        <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
            <a href="/" class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
                <svg class="bi me-2" width="40" height="32">
                    <use xlink:href="#bootstrap"/>
                </svg>
            </a>

            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                <li><a href="/" class="nav-link px-2 text-white">Главная</a></li>
                <li><a href="/allProjects" class="nav-link px-2 text-white">Все проекты</a></li>
                <li><a href="/about" class="nav-link px-2 text-white">О нас</a></li>

                <sec:authorize access="hasRole('ROLE_ADMIN')">
                    <li><a href="/admin/users" class="nav-link px-2 text">Пользователи</a></li>
                    <li><a href="/admin/projects" class="nav-link px-2 text">Проекты</a></li>
<%--                    <li><a href="/admin/configs" class="nav-link px-2 text">Конфигурации</a></li>--%>
<%--                    <li><a href="/admin/languages" class="nav-link px-2 text">Языки</a></li>--%>
<%--                    <li><a href="/admin/builders" class="nav-link px-2 text">Сборщики</a></li>--%>
                </sec:authorize>
            </ul>

            <form class="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3" action="/search" method="post">
                <input type="text" name="searchText" class="form-control form-control-dark" placeholder="Найти проект...">
            </form>


            <div class="text-end">

                <sec:authorize access="!isAuthenticated()">
                    <button onclick="document.location='../login'" type="button" class="btn btn-outline-light me-2">Войти
                    </button>
                    <button onclick="document.location='registration'" type="button" class="btn btn-warning">
                        Зарегистрироваться
                    </button>
                </sec:authorize>

                <sec:authorize access="isAuthenticated() ">
                    <sec:authorize access="hasRole('ROLE_USER')">

                        <button onclick="document.location='/project/create'" type="button"
                                class="btn btn-success me-2">
                            Создать проект
                        </button>
                        <button onclick="document.location='/profile/current'" type="button"
                                class="btn btn-outline-light me-2">${pageContext.request.userPrincipal.name}</button>
                    </sec:authorize>
                    <button onclick="document.location='/logout'" type="button" class="btn btn-warning">Выйти</button>
                </sec:authorize>

            </div>

        </div>
    </div>
</header>
<div class="b-example-divider"></div>
</body>
</html>