<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <meta name="generator" content="Hugo 0.82.0">
    <title>Главная</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="canonical" href="https://getbootstrap.com/docs/5.0/examples/headers/">


    <!-- Bootstrap core CSS -->
    <link href="../assets/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        <%@ include file='../css/project.css' %>

        body {
            padding-bottom: 120px;
        }

    </style>

</head>
<body>
<%@ include file="../header.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf"
        crossorigin="anonymous"></script>
<script src="../assets/dist/js/bootstrap.bundle.min.js"></script>


<div class="container">
    <div class="card">
        <div class="row">

            <aside class="col-md">
                <article class="card-body p-5">

                    <div class="card-img-top mb-5">
                        <img class="img-fluid img-responsive rounded product-image"
                             src="/img/${project.logo}">
                    </div>
                    <h3 class="title mb-3">${project.name}</h3>

                    <dl class="param param-feature">
                        <dt>Статус:</dt>
                        <dd>${project.status}</dd>
                    </dl>  <!-- item-property-hor .// -->

                    <dl class="param param-feature">
                        <dt>Автор:</dt>
                        <dd><a href="/profile/${project.user.id}">${project.user.username}</a></dd>
                    </dl>  <!-- item-property-hor .// -->

                    <dl class="param param-feature">
                        <dt>Конфигурация запуска:</dt>
                        <dd>${project.projectConfig.name}</dd>
                    </dl>  <!-- item-property-hor .// -->

                    <aside class="col-sm-1 border-left">

                    </aside>
                    <dl class="item-property">
                        <dt>Описание:</dt>
                        <dd>

                            <p>${project.description} </p>
                        </dd>
                    </dl>

                    <hr>
                    <a onclick="document.location='http://localhost:4201/app/1'"  class="btn btn-lg btn-success "> Запустить проект</a>

                    <button class="btn btn-lg btn-warning border-black ml-2"
                            type="button"
                            onclick="history.back();">Назад
                    </button>
                </article> <!-- card-body.// -->
            </aside> <!-- col.// -->

        </div> <!-- row.// -->
    </div> <!-- card.// -->
</div>
<footer class="fixed-bottom">
    <%@ include file="../footer.html" %>
</footer>

</body>

</html>