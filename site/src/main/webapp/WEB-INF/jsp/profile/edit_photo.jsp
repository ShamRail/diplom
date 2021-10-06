<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="generator" content="Hugo 0.82.0">
    <title>Редактирование профиля</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">

    <link rel="canonical" href="https://getbootstrap.com/docs/5.0/examples/checkout/">


    <!-- Bootstrap core CSS -->
    <link href="../assets/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        <%@include file='../css/form-validation.css' %>

        @media (min-width: 768px) {
        }

        .border-black {
            border-color: #212529;
        }

        button {
            margin: 5px 5px 5px 5px;
        }
        body {
            padding-bottom: 120px;
        }


        memo {
            height: auto;
        }
    </style>

</head>

<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf"
        crossorigin="anonymous"></script>

<%@ include file="../header.jsp" %>

<div class="container">
    <main>
        <div class="py-5 ">
            <h2>Редактирование профиля</h2>
        </div>

        <div class="row g-5">
            <div class="col-md-7 col-lg-8">
                <%--@elvariable id="userForm" type=""--%>
                <form action="/profile/edit/photo" enctype="multipart/form-data" method="post">

                    <div class="row g-3">
                        <div class="col-12">

                            <label for="file" class="form-label">Выберите фото</label>

                            <input type="file" id="file" class="form-control w-50" name="file"/>


                        </div>
                        <div class="col-12">

                            <button class="w-50 btn btn-success"
                                    type="submit">Сохранить
                            </button>
                        </div>
                        <div class="col-12">

                            <button class="w-50 btn btn-warning border-black"
                                    type="button"
                                    onclick="history.back();">Назад
                            </button>
                        </div>

                    </div>
                </form>


            </div>
        </div>
    </main>
</div>
<br>
<br>
<br>
<br>
<br>


</body>
<footer class="fixed-bottom mt-5 ">
    <%@ include file="../footer.html" %>
</footer>

</html>
