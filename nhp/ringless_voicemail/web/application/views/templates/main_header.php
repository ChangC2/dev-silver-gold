<body>
    <!-- Begin page -->
    <div id="layout-wrapper">
        <?php include "spinner.php"; ?>
        <header id="page-topbar">
            <div class="navbar-header">
                <div class="d-flex">
                    <!-- LOGO -->
                    <div class="navbar-brand-box">
                        <a href="" class="logo logo-dark">
                            <span class="logo-sm">
                                <img src="<?php echo base_url('assets/'); ?>images/logo.svg" alt="" height="24">
                            </span>
                            <span class="logo-lg">
                                <img src="<?php echo base_url('assets/'); ?>images/logo.svg" alt="" height="24"> <span class="logo-txt" style="font-size: 16px;">Ringless Voicemail</span>
                            </span>
                        </a>

                        <a href="" class="logo logo-light">
                            <span class="logo-sm">
                                <img src="<?php echo base_url('assets/'); ?>images/logo.svg" alt="" height="24">
                            </span>
                            <span class="logo-lg">
                                <img src="<?php echo base_url('assets/'); ?>images/logo.svg" alt="" height="24"> <span class="logo-txt" style="font-size: 16px;">Ringless Voicemail</span>
                            </span>
                        </a>
                    </div>

                    <button type="button" class="btn btn-sm px-3 font-size-16 header-item" id="vertical-menu-btn">
                        <i class="fa fa-fw fa-bars"></i>
                    </button>

                </div>

                <div class="d-flex">
                    <!-- <div class="dropdown d-none d-sm-inline-block">
                   <button type="button" class="btn header-item" id="mode-setting-btn">
                       <i data-feather="moon" class="icon-lg layout-mode-dark"></i>
                       <i data-feather="sun" class="icon-lg layout-mode-light"></i>
                   </button>
               </div> -->
                    <?php if ($permission == 2 || $permission == 0) { ?>
                        <div class="dropdown d-inline-block">
                            <button type="button" class="btn header-item bg-soft-light border-start border-end" id="page-header-user-dropdown" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="mdi mdi-account font-size-16 align-middle me-1"></i>
                                <span class="d-none d-xl-inline-block ms-1 fw-medium"><?php echo $accountname; ?></span>
                                <i class="mdi mdi-chevron-down d-none d-xl-inline-block"></i>
                            </button>
                            <div class="dropdown-menu dropdown-menu-end">
                                <!-- item-->
                                <a class="dropdown-item" href="<?php echo base_url('auth/logout'); ?>"><i class="fa fa-window-close"></i> Logout</a>
                            </div>
                        </div>
                    <?php } ?>
                </div>
            </div>
        </header>