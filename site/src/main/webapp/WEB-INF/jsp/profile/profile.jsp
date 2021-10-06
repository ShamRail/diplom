<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="generator" content="Hugo 0.82.0">
    <title>Личный профиль</title>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/5.3.45/css/materialdesignicons.css"
          integrity="sha256-NAxhqDvtY0l4xn+YVa6WjAcmd94NNfttjNsDmNatFVc=" crossorigin="anonymous"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.1/css/all.min.css"
          integrity="sha256-2XFplPlrFClt0bIdPgpz8H7ojnk10H69xRqd9+uTShA=" crossorigin="anonymous"/>
    <style>

        <%@include file='../css/profile.css' %>
    </style>


</head>
<body>
<%@ include file="../header.jsp" %>

<div class="container">
    <div class="row">
        <div class="col-lg-4 col-xl-4">
            <div class="card-box text-center">
                <img  src="/img/${user.photo}"
                     class="rounded-circle avatar-xl img-thumbnail" alt="profile-image">

                <h4 class="mb-0">${user.firstName}</h4>
                <p class="text-muted">Статус: ${user.shortMemo}</p>
                <c:if test="${isCurrentUser}">
                    <button type="button"
                            onclick="document.location='/profile/editprofile/<sec:authentication
                                    property="principal.id"/>'"
                            class="btn btn-success btn-xs waves-effect mb-2 waves-light">
                        Редактировать данные
                    </button>
                </c:if>

                <c:if test="${isCurrentUser}">
                        <button type="button"
                                onclick="document.location='/profile/edit/photo'"
                                class="btn btn-success btn-xs waves-effect mb-2 waves-light">
                            Изменить фото
                        </button>
                </c:if>
                <div class="text-left mt-3">
                    <h4 class="font-13 text-uppercase">Обо мне :</h4>
                    <p class="text-muted font-13 mb-3">
                        ${user.memo}
                    </p>
                    <p class="text-muted mb-2 font-13"><strong>Имя и фамилия :</strong> <span
                            class="ml-2">${user.firstName} ${user.lastName}</span></p>

                    <p class="text-muted mb-2 font-13"><strong>Email :</strong> <span
                            class="ml-2 ">${user.email}</span></p>

                    <p class="text-muted mb-1 font-13"><strong>Дата регистрации :</strong> <span
                            class="ml-2"><fmt:formatDate value="${user.registeringDate}"
                                                         pattern="dd.MM.yyyy"/></span></p>
                </div>

            </div> <!-- end card-box -->

        </div> <!-- end col-->

        <div class="col-lg-8 col-xl-8">


            <div class="tab-content">

                <div class="tab-pane show active" id="about-me">

                    <h5 class="mb-3 mt-4 text-uppercase"><i class="mdi mdi-cards-variant mr-1"></i>
                        Проекты</h5>
                    <div class="table-responsive">
                        <table class="table table-borderless mb-0">
                            <thead class="thead-light">
                            <tr>
                                <th>Название</th>
                                <th>Дата создания</th>
                                <th>Конфигурация</th>
                                <th>Статус</th>
                                <th>Действия</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="project" items="${projectList}">
                                <tr>
                                    <td><a href="/project/${project.id}">${project.name}</a></td>
                                    <td><fmt:formatDate value="${project.creatingDate}"
                                                        pattern="dd.MM.yyyy"/></td>

                                    <td>${project.projectConfig.language.name}
                                        + ${project.projectConfig.builder.name}</td>

                                    <td><span class="badge badge-info">Work in Progress</span></td>

                                    <td>
                                        <c:if test="${isCurrentUser}">

                                            <form action="/profile/project/delete" method="post">
                                                <input type="hidden" name="projectId" value="${project.id}"/>

                                                <button onclick="document.location='/project/edit/${project.id}'"
                                                        type="button"
                                                        class="btn-sm btn-dark">
                                                    Редактировать
                                                </button>

                                                <button type="submit" onclick="return confirm('Вы уверены? Проект невозможно будет восстановить!')"
                                                        class="btn-sm btn-danger">
                                                    Удалить
                                                </button>
                                            </form>

                                        </c:if>

                                        <c:if test="${!isCurrentUser}">

                                            <button onclick="document.location='/project/${project.id}'" type="button"
                                                    class="btn-sm btn-dark">
                                                Открыть
                                            </button>


                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                </div>
                <!-- end timeline content-->

            </div> <!-- end tab-content -->
        </div> <!-- end card-box-->

    </div> <!-- end col -->
</div>
</div>
<footer class="fixed-bottom">
    <%@ include file="../footer.html" %>
</footer>
</body>

</html>