<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="generator" content="Hugo 0.82.0">
    <title>Все проекты</title>
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet"/>


    <style>
        <%@ include file = "../css/users.css" %>


        .btn-danger{
            margin-left: 10px;
        }
    </style>

</head>
<body>
<%@ include file="../header.jsp" %>


<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title text-uppercase mb-0">Управление проектами</h5>
                </div>
                <div class="table-responsive">
                    <table class="table no-wrap user-table mb-0">
                        <thead>
                        <tr>
                            <th scope="col" class="border-0 text-uppercase font-medium pl-4">id</th>
                            <th scope="col" class="border-0 text-uppercase font-medium">Название проекта</th>
                            <th scope="col" class="border-0 text-uppercase font-medium">Конфигурация</th>
                            <th scope="col" class="border-0 text-uppercase font-medium">Дата создания</th>
                            <th scope="col" class="border-0 text-uppercase font-medium">Автор</th>
                            <th scope="col" class="border-0 text-uppercase font-medium">Действия</th>

                        </tr>

                        </thead>

                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <c:forEach var="project" items="${projectList}">


                            <tr>

                                <td class="pl-4">${project.id}</td>

                                <td>
                                    <span class="text">${project.name}</span>
                                </td>

                                <td>
                                    <span class="text">${project.projectConfig.name}</span><br>
                                </td>

                                <td>
                                    <span class="text"><fmt:formatDate value="${project.creatingDate}"
                                                                       pattern="dd-MM-yyyy |   HH:mm:ss"/> </span><br>

                                </td>

                                <td>
                                    <span class="text">
                                        <a href="/profile/${project.user.id}">${project.user.username}</a>
                                    </span>
                                    <br>
                                </td>

                                <td>


                                    <form action="/admin/project/delete" method="post">
                                        <input type="hidden" name="projectId" value="${project.id}"/>

                                        <button onclick="document.location='/project/${project.id}'" type="button"
                                                class="btn btn-dark">
                                            Открыть
                                        </button>

                                        <button type="submit" onclick="return confirm('Вы уверены?')" class="btn btn-danger">
                                            Удалить
                                        </button>


                                    </form>

                                </td>
                            </tr>

                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
<footer class="fixed-bottom">
    <%@ include file="../footer.html" %>
</footer>
</html>