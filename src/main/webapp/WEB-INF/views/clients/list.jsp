<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/views/common/head.jsp"%>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <!-- Left side column. contains the logo and sidebar -->
    <%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                Clients
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/clients/create">Ajouter</a>
            </h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="box">
                        <div class="box-body no-padding">
                            <table class="table table-striped">
                                <tr>
                                    <th style="width: 10px">#</th>
                                    <th>Nom</th>
                                    <th>Prenom</th>
                                    <th>Email</th>
                                    <th>Date de naissance</th>
                                    <th>Action</th>
                                </tr>

                                <c:forEach items="${Clients}" var="client">
                                <tr>
                                    <td>${client.id}.</td>
                                    <td>${client.nom}</td>
                                    <td>${client.prenom}</td>
                                    <td>${client.email}</td>
                                    <td>${client.naissance}</td>
                                    <!--<td>John Doe</td>-->
                                    <td>
                                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/clients/details?id=${client.id}">
                                            <i class="fa fa-play"></i>
                                        </a>
                                        <a class="btn btn-success disabled" href="${pageContext.request.contextPath}/clients/edit?id=${client.id}">
                                            <i class="fa fa-edit"></i>
                                        </a>
                                        <a class="btn btn-danger " href="${pageContext.request.contextPath}/clients/delete?id=${client.id}">
                                            <i class="fa fa-trash"></i>
                                        </a>
                                    </td>
                                </tr>
                                </c:forEach>
                            </table>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>
                <!-- /.col -->
            </div>
        </section>
        <!-- /.content -->
    </div>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
<!-- ./wrapper -->

<%@ include file="/WEB-INF/views/common/js_imports.jsp" %>
</body>
</html>
