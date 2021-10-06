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
    <title>Все конфигурации</title>
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet"/>


    <style>
        <%@ include file = "../css/users.css" %>


        .btn-danger {
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
                    <h5 class="card-title text-uppercase mb-0">Управление конфигурациями</h5>
                </div>

                <div class="card-body">
                    <span class="text">
                        <a href="/admin/configs/create">Добавить новую конфигурацию</a>
                    </span>
                </div>
                <div class="table-responsive">
                    <table class="table no-wrap user-table mb-0">
                        <thead>
                        <tr>
                            <th scope="col" class="border-0 text-uppercase font-medium pl-4">id</th>
                            <th scope="col" class="border-0 text-uppercase font-medium">Название конфигурации</th>
                            <th scope="col" class="border-0 text-uppercase font-medium">Действия</th>
                        </tr>

                        </thead>

                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>

                        <c:forEach var="config" items="${configList}">

                            <tr>

                                <td class="pl-4">${config.id}</td>

                                <td>
                                    <span class="text">${config.name}</span>
                                </td>

                                <td>
                                    <form action="/admin/configs/delete" method="post">
                                        <input type="hidden" name="configId" value="${config.id}"/>
                                        <button type="submit" onclick="return confirm('Вы уверены?')"
                                                class="btn btn-danger">
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