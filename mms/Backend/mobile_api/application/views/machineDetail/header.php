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

  <link href="<?php echo base_url() ?>assets/css/sidebarStyle.css" rel="stylesheet">

  <link href="<?php echo base_url() ?>assets/css/style.css" rel="stylesheet">
  <!-- <link href="<?php echo base_url() ?>assets/css/mystyle.css" rel="stylesheet"> -->

  <link rel="stylesheet" href="<?php echo base_url() ?>pagination_assets/jquery.pajinatify.css">


  <style>
    body {
      background: #1d1d1d;
    }

    .axis {
      font: 14px sans-serif;
    }

    .chart {
      font-family: Arial, sans-serif;
      font-size: 12px;
    }

    .axis path,
    .axis line {
      fill: none;
      stroke: #f4f4f4;
      shape-rendering: crispEdges;
    }

    .green-bar {
      fill: #4c9d2f
    }

    .red-bar {
      fill: #d8292f
    }

    .grey-bar {
      fill: #8a8a8a
    }



    .bar {
      fill: #8a8a8a;
    }

    .bar-failed {
      fill: #d8292f;
    }

    .bar-running {
      fill: #4c9d2f;
    }

    .bar-succeeded {
      fill: #4c9d2f;
    }

    .bar-idle {
      fill: #f4c400;
    }

    text {
      fill: #f4f4f4;
    }

    .tooltip {
      background: #132e35;
      color: #f4f4f4;
      border: 1px solid #2c6d7c;
      font-size: 12px;
      font-family: Arial, sans-serif;
      left: 130px;
      padding: 10px;
      position: absolute;
      text-align: left;
      top: 95px;
      z-index: 10;
      display: block;
      opacity: 0.5;
    }

    .legend {
      padding: 5px;
      font: 16px;
      font-family: Arial, sans-serif;
      box-shadow: 2px 2px 1px #888;
    }

    .zoom {
      cursor: move;
      fill: none;
      pointer-events: all;

    }

    rect {
      stroke: transparent;
    }

    @media only screen and (min-width:768px) {
      .machine_info {
        float: left;
        border: 1.5px solid;
        border-top: 0;
        border-bottom: 0;
        border-image: linear-gradient(to bottom, rgba(255, 255, 255, 0) 0%, rgba(226, 226, 226, 1) 48%, rgba(255, 255, 255, 0) 100%);
        border-image-slice: 1;
      }
    }

    @media only mobile and (max-width:400) {
      .infolayer {
        display: none;
      }
    }
  </style>
</head>

<body>
    
  <nav class="navbar navbar-default mb-1 navbar navbar-expand-lg navbar-dark z-depth-2" style="background:#282828; width:100%; overflow: hidden; position:fixed; top:0px; z-index:100; " id="headertitle">
    <div class="container-fluid row">

      <div class="navbar-header col-sm-3">
        <button type="button btn-backward" id="sidebarCollapse" class="navbar-btn">
          <a class="fas fa-backward" style="font-size:20px;color:white"> </a>
        </button>
      </div>

      <div class="col-sm-6" style="text-align:center; vertical-align: center;">
        <?php
        if (isset($userdata['logo']) && $userdata['logo'] != "" && $userdata['logo'] != null)
          echo "<img style='max-height:70px' id='title_logo' class='img img-thumbnail' src=" . $userdata['logo'] . ">";
        else
          echo "<a class='text-secondary' style='font-size:30px;font-weight:bold;'>" . $userdata['fullname'] . "</a>"
          ?>
      </div>
      <div class="col-sm-3 collapse navbar-collapse pull-right" id="bs-example-navbar-collapse-1" style="text-align:right">
        <ul class="nav navbar-nav navbar-right navbar-nav ml-auto" style="text-align:right">
          <li><a style="color:white" class="nav-link">
              <i class="fas fa-user"></i> <?php echo $userdata['username']; ?>
            </a>
          </li>
          <li><a style="color:white" class="nav-link" href="<?php echo base_url(); ?>login">SignOut</a></li>
        </ul>
      </div>
    </div>
  </nav>

  <div class="wrapper" style="margin-top:120px;">
  <!-- <button id="btnAdd">Add</button> -->
      <!-- Page Content Holder -->
    <div id="content" style="padding-top: 0px; width:100%">