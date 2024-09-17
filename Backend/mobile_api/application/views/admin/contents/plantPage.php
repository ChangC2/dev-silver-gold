<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<style>
    .ImgCenter {
        display: block;
        margin-left: auto;
        margin-right: auto;
    }    
</style>
<div style="margin-left: 15px; text-align:center; width:100%; " id="plantContainer">

    <?php

    $nowRow = 0;
    foreach ($status_list as $item) { ?>

        <div class="navbar navbar-default mb-1 navbar navbar-expand-lg navbar-dark z-depth-2 row machineItem" style="width:100%; margin-bottom:20px; " id="<?php echo $item->id; ?>">
            <div class="col col-sm-12 col-md-4" style="text-align:center; color:white">
                <div class="row">
                    <div class="col col-md-5 col-sm-12" style="text-align:center">
                        <img id="machine_picture_url_<?php echo $item->id; ?>" src="<?php echo $item->machine_picture_url; ?>" class="img img-responsive ImgCenter" style="height:150px" />
                    </div>
                    <div class="col col-md-3 col-sm-12" style="text-align:center">
                        <h5>MACHINE</h5>
                        <h5> <strong id="machine_name_<?php echo $item->id; ?>"><?php echo $item->machine_id; ?></strong> </h5>
                    </div>

                    <div class="col col-md-4 col-sm-12" style="text-align:center">
                        <h5>OPERATOR</h5>
                        <h5> <strong id="operator_<?php echo $item->id; ?>"><?php echo isset($item->Operator) ? $item->Operator : ""; ?></strong> </h5>
                        <div style="display: flex; justify-content: center;">
                            <img id="operator_picture_url_<?php echo $item->id; ?>" src="<?php echo isset($item->Operator) ? $item->operator_picture_url : ""; ?>" class="img img-responsive img-circle" style="height:60px;" align="middle">
                        </div>
                    </div>
                </div>
            </div>
            <div class="col col-sm-12 col-md-2" style="text-align:center">
                <h5 style="color:white">UTILIZATION</h5>
                <div style="display: flex; justify-content: center;">
                    <canvas style="height:120px" id="gauge_<?php echo $item->id; ?>"></canvas>
                </div>
                <div style="font-size:24px; color:white; text-align:center; margin-top:-30px;">
                    <a class="text-white" id="gauge_text_<?php echo $item->id; ?>"></a>
                </div>
                <!-- <div class="progress">
                <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" id="progress_bar_<?php echo $item->id; ?>" style="width:<?php echo isset($item->Utilization) ? $item->Utilization : "0"; ?>%; color:black">
                    <strong id="utilization_<?php echo $item->id; ?>"><?php echo isset($item->Utilization) ? $item->Utilization : "0"; ?>%</strong>
                </div>                
            </div> -->
            </div>
            <div class="col col-sm-12 col-md-5 chartContainer" style="text-align:center; color:white;" id="chartContainer_<?php echo $item->id; ?>">
                <!-- <div id="chart_<?php echo $nowRow ?>" style="width:auto; max-height:45px; margin-top:30px"></div> -->
            </div>
            <div class="col col-sm-12 col-md-1" style="text-align:center; color:white">
                <h5>STATUS</h5>
                <h5> <strong id="status_<?php echo $item->id; ?>"><?php echo $item->status; ?></strong> </h5>
                <div id="status_image_<?php echo $item->id; ?>" style="display: flex; justify-content: center;">
                    <?php
                        switch ($item->status) {
                            case "In Cycle": ?>
                            <img src="<?php echo base_url(); ?>images/status/gruen_39px.png" class="img img-responsive" style="float:center;" align="middle" />
                        <?php
                                break;
                            case "Idle - Uncategorized": ?>
                            <img src="<?php echo base_url(); ?>images/status/rot_39px.png" class="img img-responsive" style="float:center" align="middle" />
                        <?php
                                break;
                            case "Offline": ?>
                            <img src="<?php echo base_url(); ?>images/status/grau_39px.png" class="img img-responsive" style="float:center;" align="middle" />
                        <?php
                                break;
                            default: ?>
                            <img src="<?php echo base_url(); ?>images/status/gelb_39px.png" class="img img-responsive" style="float:center;" align="middle" />
                        <?php
                                break;
                        } ?>
                </div>
            </div>
        </div>
    <?php
        $nowRow++;
    }
    ?>

</div>
<div id='stacked-bar'></div>

<div class="row">
    <div class="col-sm-3"></div>
    <div class="col-sm-6" style="text-align:center">
        <img class="img-responsive;" src="<?php echo base_url() ?>assets/img/logo.png" style="display: inline-block; height:50px;" />
    </div>
</div>

<script>
    var page = 1; // PlantPage

    var statusList = <?php echo json_encode($status_list); ?>;
    //DrawCahrt(statusList);
    //DrawGauge(statusList);
</script>