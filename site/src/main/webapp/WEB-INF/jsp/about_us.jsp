<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!doctype HTMl>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="generator" content="Hugo 0.82.0">
    <title>О нас</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/5.0/examples/sticky-footer/">


    <!-- Bootstrap core CSS -->
    <link href="../assets/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        <%@ include file = "css/sticky-footer.css" %>

        .bd-placeholder-img {
            font-size: 1.125rem;
            text-anchor: middle;
            -webkit-user-select: none;
            -moz-user-select: none;
            user-select: none;
        }

        body {
            padding-bottom: 120px;
        }


        @media (min-width: 768px) {
            .bd-placeholder-img-lg {
                font-size: 3.5rem;
            }
        }
    </style>


    <!-- Custom styles for this template -->
</head>
<body class="d-flex flex-column h-100">
<%@ include file="header.jsp" %>

<!-- Begin page content -->
<main class="flex-shrink-0">
    <div class="container">
        <%--        <h1 class="mt-5">О проекте </h1>--%>
        <%--        <p class="lead">Данный проект создан с целью запуска алгоритмов решения задач дискретной оптимизации в рамках--%>
        <%--            дипломной работы</p>--%>
        <%--        <hr>--%>
        <h2 class="mt-5">Разработчики</h2>
        <p class="lead">Сулейманов Сулейман, студент УГАТУ группы ПРО-416 </p>
        <p class="lead">Раиль Шамсемухаметов, студент УГАТУ группы ПРО-417</p>
        <h2 class="mt-5">Дипломный руководитель</h2>
        <p class="lead">Валиахметова Юлия Ильясовна</p>

    </div>
</main>

<br>
<br>
<br>
<br>
<br>


</body>
<footer class="fixed-bottom mt-5 ">
    <%@ include file="footer.html" %>
</footer>

</html>
