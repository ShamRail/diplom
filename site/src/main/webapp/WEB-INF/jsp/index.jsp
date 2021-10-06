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
    <title>Главная</title>

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
        <h1 class="mt-5">О проекте </h1>
        <p class="lead">Данный проект создан с целью запуска алгоритмов решения задач дискретной оптимизации в рамках
            дипломной работы</p>
        <hr>
        <h2 class="mt-5">Требование к ПО</h2>
        <h5 class="mt-5">1. Пути:</h5>
        <p class="lead">
            1) Входные файлы нужно помещать как минимум на один уровень вложенности директории
            Например, in/file.txt, путь задается относительно корня вашего проекта</p>

        <p class="lead">
            2) Выходные файлы нужно также брать как минимум из одного уровня вложенности

            Например, out/file.txt, путь задается относительно корня вашего проекта</p>
        <p class="lead">
            3) Пути должны быть относительные, например in/file.txt, а не /in/file.txt</p>

        <p class="lead">
            4) Все пути задаются через /, а не \\</p>

        <h5 class="mt-5"> 2. Команда запуска</h5>

        <p class="lead">
            1) Для запуска желательно не использовать ключи, динамическая обработка ключей пока не сделана.
            Все, что нужно для вашей программы, можете спросить с консоли. Для запуска лучше просто указать саму команду
            запуска </p>

        <p class="lead">
            2) Флаги типа —flag можете использовать если они нужны</p>

        <h5 class="mt-5"> 3. Для тех кто пишет на C++: </h5>

        <p class="lead">
            1) Нужно использовать компилиторы, поддерживаемые на Linux, например GNU GCC</p>

        <p class="lead">
            2) Если программа состоит из одного файла, то желательно вместо сборки просто его компилировать
            и запускать</p>

        <p class="lead">
            3) Если программа требует сборки, то НУЖНО использовать решения, которые возможно собрать на Linux
            Например, для сборки можно использовать CMake</p>

        <h5 class="mt-5">
            4. Для тех кто пишет на C#, необходимо использовать .NET Core 3.1, чтобы не было проблем</h5>

        <h5 class="mt-5">
            5. Исходный код программы должен быть в zip</h5>

        <p class="lead"> Ограничения с 3 и 4 связаны с тем, что из под Linux нельзя запустить контейнеры Windows, а на
            Windows не
            работает веб-сокет для взаимодействия с вашими приложениями. </p>


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
