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
        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-md-4">

                    <!-- Profile Image -->
                    <div class="box box-primary">
                        <div class="box-body box-profile">
                            <h3 class="profile-username text-center">Reservation</h3>

                            <ul class="list-group list-group-unbordered">
                                <li class="list-group-item">
                                    <b>Debut</b> <a class="pull-right">${reservation.debut}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Fin</b> <a class="pull-right">${reservation.fin}</a>
                                </li>
                            </ul>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>

                <div class="col-md-4">

                    <!-- Profile Image -->
                    <div class="box box-primary">
                        <div class="box-body box-profile">
                            <h3 class="profile-username text-center">Client</h3>

                            <ul class="list-group list-group-unbordered">
                                <li class="list-group-item">
                                    <b>Nom</b> <a class="pull-right">${reservation.client.nom}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Prenom</b> <a class="pull-right">${reservation.client.prenom}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Email</b> <a class="pull-right">${reservation.client.email}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Date de naissance</b> <a class="pull-right">${reservation.client.naissance}</a>
                                </li>
                            </ul>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>

                <div class="col-md-4">
                    <!-- Profile Image -->
                    <div class="box box-primary">
                        <div class="box-body box-profile">
                            <h3 class="profile-username text-center">Vehicule</h3>

                            <ul class="list-group list-group-unbordered">
                                <li class="list-group-item">
                                    <b>Constructeur</b> <a class="pull-right">${reservation.vehicle.constructeur}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Modele</b> <a class="pull-right">${reservation.vehicle.modele}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Nombre de place</b> <a class="pull-right">${reservation.vehicle.nb_places}</a>
                                </li>
                            </ul>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>



               
                
            </div>
            <!-- /.row -->

        </section>
        <!-- /.content -->
    </div>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
<!-- ./wrapper -->

<%@ include file="/WEB-INF/views/common/js_imports.jsp" %>
</body>
</html>
