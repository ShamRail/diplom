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

        memo {
            height: auto;
        }
        body {
            padding-bottom: 120px;
        }

    </style>

</head>

<body >
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
                <h4 class="mb-3">Введите свои данные</h4>
                <%--@elvariable id="userForm" type=""--%>
                <form:form method="POST" modelAttribute="userForm" class="needs-validation">
                    <div class="row g-3">

                        <div class="col-sm-6">
                            <label for="firstName" class="form-label">Имя</label>
                            <form:input type="text"
                                        path="firstName"
                                        class="form-control"
                                        id="firstName"/>
                        </div>

                        <div class="col-sm-6">
                            <label for="lastName" class="form-label">Фамилия</label>
                            <form:input type="text"
                                        path="lastName"
                                        class="form-control"
                                        id="lastName"/>
                        </div>

                        <div class="col-12">
                            <label for="shortMemo" class="form-label memo">Статус</label>
                            <form:textarea type="text"
                                           path="shortMemo"
                                           class="form-control"
                                           id="shortMemo"/>
                        </div>

                        <div class="col-12">
                            <label for="memo" class="form-label memo">О себе</label>
                            <form:textarea type="text"
                                           path="memo"
                                           class="form-control"
                                           id="memo"/>
                        </div>

                        <div class="col-12">
                            <label for="username" class="form-label">Логин</label>
                            <div class="input-group has-validation">
                                <form:input type="text"
                                            path="username"
                                            class="form-control"
                                            placeholder="Логин"
                                            id="username"
                                            autofocus="true"/>

                                <form:errors path="username">
                                    <div class="invalid-feedback">
                                        Логин занят
                                    </div>
                                </form:errors>
                                    ${usernameError}
                            </div>
                        </div>

                        <div class="col-12">
                            <label for="email" class="form-label">Email</label>
                            <form:input path="email"
                                        type="email"
                                        class="form-control"
                                        id="email"
                                        placeholder="you@example.com"/>
                        </div>

                        <div>
                            <label for="password" class="form-label">Пароль</label>
                            <form:input id="password"
                                        type="password"
                                        path="password"
                                        class="form-control"
                                        placeholder="Введите пароль"/>
                        </div>
                        <div>
                            <label for="passwordConfirm" class="form-label">Подтвердите пароль</label>
                            <form:input type="password"
                                        path="passwordConfirm"
                                        value="${userForm.password}"
                                        placeholder="Введите пароль еще раз"
                                        id="passwordConfirm"
                                        class="form-control"/>

                            <form:errors path="password"/>
                                ${passwordError}
                        </div>
                        <button class="w-100 btn btn-success"
                                type="submit">Изменить профиль
                        </button>

                        <button class="w-100 btn btn-warning border-black"
                                type="button"
                                onclick="history.back();">Назад
                        </button>
                    </div>
                </form:form>

            </div>
        </div>
    </main>
</div>
<footer class="fixed-bottom mt-lg-5">
    <%@ include file="../footer.html" %>
</footer>
</body>

</html>
