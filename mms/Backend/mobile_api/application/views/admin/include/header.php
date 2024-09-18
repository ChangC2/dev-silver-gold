<!DOCTYPE html>
<?php ini_set('default_charset', 'utf-8'); ?>

<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">


    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>SLYTRACKR</title>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.css">
    <!-- Material Design Bootstrap -->
    <link href="<?php echo base_url() ?>assets/css/mdb.min.css" rel="stylesheet">
    <!-- Your custom styles (optional) -->
    <link href="<?php echo base_url() ?>assets/css/addons/datatables.min.css" rel="stylesheet">

    <link href="<?php echo base_url() ?>assets/css/sidebarStyle.css?v=1.0" rel="stylesheet">

    <link href="<?php echo base_url() ?>assets/css/style.css?v=1.04" rel="stylesheet">
    <link href="<?php echo base_url() ?>assets/css/mystyle.css" rel="stylesheet">

    <link rel="stylesheet" href="<?php echo base_url(); ?>pagination_assets/jquery.pajinatify.css">

    <style>
        @media only screen {
            body {
                overflow: hidden;
            }
        }
    </style>
</head>

<body style="background:#1d1d1d;">
    <nav class="navbar navbar-default mb-1 navbar navbar-expand-lg navbar-dark z-depth-2" style="background:#282828; width:100%; margin-bottom:20px" id="top-navbar">
        <div class="container-fluid row">
            <div class="navbar-header col-sm-2">
                <button type="button" id="sidebarCollapse" class="navbar-btn <?php echo $collapsed == 1 ? 'active' : ''; ?>">
                    <span style="background:white"></span>
                    <span style="background:white"></span>
                    <span style="background:white"></span>
                </button>
            </div>

            <div class="col-sm-8" style="text-align:center; vertical-align: center;">
                <!-- <a class="text-secondary" style="font-size: 30px; font-weight: bold; "> -->
                <?php
                if (isset($userdata['logo']) && $userdata['logo'] != "" && $userdata['logo'] != null)
                    echo "<img style='max-height:70px' id='title_logo' class='img img-thumbnail' src='" . $userdata['logo'] . "'>";
                else
                    echo "<a class='text-secondary' style='font-size:30px;font-weight:bold;'>" . $userdata['fullname'] . "</a>"
                    ?>
            </div>

            <div class="col-sm-2 collapse navbar-collapse pull-right" id="bs-example-navbar-collapse-1" style="text-align:right">

                <ul class="nav navbar-nav navbar-right navbar-nav ml-auto" style="text-align:right">
                    <li>
                        <a style="color:white" class="nav-link" id="btnSetting">
                            <i class="fas fa-cog" style="font-size:20px"></i>
                        </a>
                    </li>
                    <li><a style="color:white" class="nav-link" id="title_username">
                            <i class="fas fa-user"></i> <?php echo $userdata['username']; ?>
                        </a>
                    </li>
                    <li><a style="color:white" class="nav-link" href="<?php echo base_url(); ?>login"><i class="fas fa-sign-out-alt" style="font-size:20px"></i></a></li>
                </ul>
            </div>
        </div>
    </nav>
    <!-- <?php echo "COL:=" . $collapsed; ?> -->
    <div class="wrapper" id="main-content">
        <!-- Sidebar Holder -->
        <nav id="sidebar" class="<?php echo $collapsed == 1 ? 'active' : ''; ?>">
            <div class="sidebar-header">
                <img class="img-responsive;" src="<?php echo base_url() ?>assets/img/logo.png" style="display: inline-block; width:100%;" />
            </div>

            <ul class="list-unstyled components">

                <?php
                for ($i = 0; $i < count($userdata['machines']); $i++) {
                    ?>
                    <li class="catType">
                        <a href="<?php echo base_url() ?>plant_page/<?php echo $i; ?>/0">
                            <i class="glyphicon glyphicon-road" style="font-size:20px"></i>
                            <?php echo $userdata['machines'][$i];?>
                        </a>
                    </li>
                <?php
                }
                ?>

            </ul>

            <div class="sidebar-footer" style="width:250px; padding-left:20px; padding-right:20px">
                <div class="row" style="text-align:center;">
                    <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input" style="font-size:20px" id="chkScroll">
                        <label class="custom-control-label" for="chkScroll" style="color:white">&nbsp;AutoScroll</label>
                    </div>
                </div>
                <div class="row; ">
                    <label for="sldSpeed" style="color:white; text-align:left" id="labelSpeed">Scroll Speed</label>
                    <input type="range" class="custom-range" id="sldSpeed" min="10" max="100" step="1" value="50" oninput="setSliderSpeed(this.value)" onchange="setSliderSpeed(this.value)">
                </div>
            </div>
        </nav>
        <style>
            #content {
                overflow: auto;
            }
        </style>
        <!-- Page Content Holder -->
        <div id="content" style="padding-top: 0px; width:100%">
            <div class="row">
                <!-- <a class="text-white" style="font-size: 50px; font-weight: bold; color:white"><?php echo $title; ?></a> -->
            </div>