<!doctype html>
<html lang="en">

<head>

    <meta charset="utf-8" />
    <title>Login | Ringless Voicemail</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta content="Premium Multipurpose Admin & Dashboard Template" name="description" />
    <meta content="Themesbrand" name="author" />
    <!-- App favicon -->
    <link rel="shortcut icon" href="<?php echo base_url('assets/'); ?>images/favicon.ico">

    <!-- preloader css -->
    <link rel="stylesheet" href="<?php echo base_url('assets/'); ?>css/preloader.min.css" type="text/css" />

    <link href="<?php echo base_url('assets/'); ?>libs/sweetalert2/sweetalert2.min.css" rel="stylesheet" type="text/css" />

    <!-- Bootstrap Css -->
    <link href="<?php echo base_url('assets/'); ?>css/bootstrap.min.css" id="bootstrap-style" rel="stylesheet" type="text/css" />
    <!-- Icons Css -->
    <link href="<?php echo base_url('assets/'); ?>css/icons.min.css" rel="stylesheet" type="text/css" />
    <!-- App Css-->
    <link href="<?php echo base_url('assets/'); ?>css/app.min.css" id="app-style" rel="stylesheet" type="text/css" />


    <!-- alertifyjs Css -->
    <link href="<?php echo base_url('assets/'); ?>libs/alertifyjs/build/css/alertify.min.css" rel="stylesheet" type="text/css" />

    <!-- alertifyjs default themes  Css -->
    <link href="<?php echo base_url('assets/'); ?>libs/alertifyjs/build/css/themes/default.min.css" rel="stylesheet" type="text/css" />

    <script src="<?php echo base_url('assets/'); ?>libs/jquery/jquery.min.js"></script>

</head>

<?php include "application/views/terms/header.php"; ?>
<?php include "application/views/terms/part1.php"; ?>
<?php include "application/views/terms/part2.php"; ?>
<?php include "application/views/terms/part3.php"; ?>
<?php include "application/views/terms/sign.php"; ?>
<?php include "application/views/terms/script.php"; ?>

<body>

    <!-- <body data-layout="horizontal"> -->
    <div class="auth-page">
        <div class="container-fluid p-0">
            <div class="row g-0">
                <div class="col-xxl-3 col-lg-4 col-md-5">
                    <div class="auth-full-page-content d-flex p-sm-5 p-4">
                        <div class="w-100">
                            <div class="d-flex flex-column h-100">
                                <div class="mb-4 mb-md-5 text-center">
                                    <a href="index.html" class="d-block auth-logo">
                                        <img src="<?php echo base_url('assets/'); ?>images/logo.svg" alt="" height="28"> <span class="logo-txt">Farming Ringless Voicemail</span>
                                    </a>
                                </div>
                                <div class="auth-content my-auto">
                                    <div class="text-center">
                                        <h5 class="mb-0">Welcome Back !</h5>
                                        <p class="text-muted mt-2">Sign in to continue to Farming Ringless Voicemail.</p>
                                    </div>
                                    <div class="custom-form mt-4 pt-2">
                                        <div class="mb-3">
                                            <label class="form-label">Email</label>
                                            <input type="text" class="form-control" id="email" placeholder="Enter email" value="">
                                        </div>
                                        <div class="mb-3">
                                            <div class="d-flex align-items-start">
                                                <div class="flex-grow-1">
                                                    <label class="form-label">Password</label>
                                                </div>
                                            </div>
                                            <div class="input-group auth-pass-inputgroup">
                                                <input type="password" class="form-control" id="password" placeholder="Enter password" aria-label="Password" value="" aria-describedby="password-addon">
                                                <button class="btn btn-light shadow-none ms-0" type="button" id="password-addon"><i class="mdi mdi-eye-outline"></i></button>
                                            </div>
                                        </div>
                                        <div class="mb-3">
                                            <button class="btn btn-primary w-100 waves-effect waves-light" type="button" onclick="login();">Log In</button>
                                        </div>
                                    </div>
                                </div>
                                <div class="mt-4 mt-md-5 text-center">
                                    <p class="mb-0">© <script>
                                            document.write(new Date().getFullYear())
                                        </script> Powered <i class="mdi mdi-heart text-danger"></i> by NewHomePage</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- end auth full page content -->
                </div>
                <!-- end col -->
                <div class="col-xxl-9 col-lg-8 col-md-7">
                    <div class="auth-bg pt-md-5 p-4 d-flex">
                        <div class="bg-overlay bg-primary"></div>
                        <ul class="bg-bubbles">
                            <li></li>
                            <li></li>
                            <li></li>
                            <li></li>
                            <li></li>
                            <li></li>
                            <li></li>
                            <li></li>
                            <li></li>
                            <li></li>
                        </ul>
                        <!-- end bubble effect -->
                        <div class="row justify-content-center align-items-center">
                            <div class="col-xl-7">
                                <div class="p-0 p-sm-4 px-xl-0">
                                    <div id="reviewcarouselIndicators" class="carousel slide" data-bs-ride="carousel">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- end col -->
            </div>
            <!-- end row -->
        </div>
        <!-- end container fluid -->
    </div>


    <!-- JAVASCRIPT -->
    <script src="<?php echo base_url('assets/'); ?>libs/jquery/jquery.min.js"></script>
    <script src="<?php echo base_url('assets/'); ?>libs/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="<?php echo base_url('assets/'); ?>libs/metismenu/metisMenu.min.js"></script>
    <script src="<?php echo base_url('assets/'); ?>libs/simplebar/simplebar.min.js"></script>
    <script src="<?php echo base_url('assets/'); ?>libs/node-waves/waves.min.js"></script>
    <script src="<?php echo base_url('assets/'); ?>libs/feather-icons/feather.min.js"></script>
    <!-- pace js -->
    <script src="<?php echo base_url('assets/'); ?>libs/pace-js/pace.min.js"></script>

    <!-- Required datatable js -->
    <script src="<?php echo base_url('assets/'); ?>libs/datatables.net/js/jquery.dataTables.min.js"></script>
    <script src="<?php echo base_url('assets/'); ?>libs/datatables.net-bs4/js/dataTables.bootstrap4.min.js"></script>
    <!-- Buttons examples -->
    <script src="<?php echo base_url('assets/'); ?>libs/datatables.net-buttons/js/dataTables.buttons.min.js"></script>
    <script src="<?php echo base_url('assets/'); ?>libs/datatables.net-buttons-bs4/js/buttons.bootstrap4.min.js"></script>

    <script src="<?php echo base_url('assets/'); ?>libs/datatables.net-buttons/js/buttons.html5.min.js"></script>
    <script src="<?php echo base_url('assets/'); ?>libs/datatables.net-buttons/js/buttons.print.min.js"></script>
    <script src="<?php echo base_url('assets/'); ?>libs/datatables.net-buttons/js/buttons.colVis.min.js"></script>

    <!-- Responsive examples -->
    <script src="<?php echo base_url('assets/'); ?>libs/datatables.net-responsive/js/dataTables.responsive.min.js"></script>
    <script src="<?php echo base_url('assets/'); ?>libs/datatables.net-responsive-bs4/js/responsive.bootstrap4.min.js"></script>


    <!-- Sweet Alerts js -->
    <script src="<?php echo base_url('assets/'); ?>libs/sweetalert2/sweetalert2.min.js"></script>

    <script src="<?php echo base_url('assets/'); ?>libs/alertifyjs/build/alertify.min.js"></script>

    <script src="<?php echo base_url('assets/'); ?>js/app.js"></script>

    <script>
        $('#email').keypress(function(event) {
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode == '13') {
                login();
            }
        });
        $('#password').keypress(function(event) {
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode == '13') {
                login();
            }
        });

        var login = function() {
            var email = $("#email").val().trim();
            var password = $("#password").val().trim();

            if (email === "") {
                alertify.error('Enter email');
                return;
            }

            if (password === "") {
                alertify.error('Enter password');
                return;
            }

            var postData = {
                email: email,
                password: password
            };

            $.ajax({
                url: "<?php echo base_url('auth/checkUser'); ?>",
                type: "POST",
                data: postData
            }).done(function(data) {
                var result = $.parseJSON(data);
                if (result['status'] == true) {
                    token = result['token'];
                    if (result['permission'] == 0) {
                        window.location.href = "<?php echo base_url('recordings?token='); ?>" + token;
                    } else {
                        window.location.href = "<?php echo base_url('sendmail?token='); ?>" + token;
                    }
                } else {
                    alert_message("error", "Fail to login");
                }
            }).fail(function() {
                alert_message("error", "Fail to login");
            }).always(function() {

            });
        }

        var alert_message = function(icon, title) {
            Swal.fire({
                position: "top-center",
                icon: icon,
                title: title,
                showConfirmButton: !1,
                timer: 2000
            });
        }
    </script>

</body>

</html>