<!-- ========== Left Sidebar Start ========== -->
<div class="vertical-menu">
    <div data-simplebar class="h-100">
        <!--- Sidemenu -->
        <div id="sidebar-menu">
            <!-- Left Menu Start -->
            <ul class="metismenu list-unstyled" id="side-menu">

                <li class="menu-title" data-key="t-quiz-menu">Menu</li>

                <?php if ($permission == 2) { ?>
                    <li id="menu-dashboard">
                        <a href="<?php echo base_url('sendmail?token=' . urlencode($token)); ?>">
                            <i class="bx bx-mail-send label-icon"></i>
                            <span data-key="t-zodiac-weekly">Send Ringless Voicemails</span>
                        </a>
                    </li>

                    <li id="menu-recordings">
                        <a href="<?php echo base_url('recordings?token=' . urlencode($token)); ?>">
                            <i class="bx bxs-microphone"></i>
                            <span data-key="t-zodiac-weekly">Recordings</span>
                        </a>
                    </li>

                    <li class="">
                        <a href="javascript: void(0);" aria-disabled="true">
                            <i class="bx bxs-volume-full"></i>
                            <span data-key="t-pages">AI Avatar Voices</span>
                        </a>
                        <ul class="sub-menu mm-collapse mm-show">
                            <li id="menu-pro-voices"><a href="<?php echo base_url('provoices?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Pro Voices</a></li>
                            <li id="menu-voices"><a href="<?php echo base_url('voices?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Your Cloned Voices</a></li>
                        </ul>
                    </li>

                    <li id="menu-scripts">
                        <a href="<?php echo base_url('scripts?token=' . urlencode($token)); ?>">
                            <i class="bx bx-message-square-detail"></i>
                            <span data-key="t-zodiac-weekly">AI Avatar Scripts</span>
                        </a>
                    </li>

                    <li class="">
                        <a href="javascript: void(0);" aria-disabled="true">
                            <i class="bx bx-mail-send label-icon"></i>
                            <span data-key="t-pages">Past Ringless Voicemails</span>
                        </a>
                        <ul class="sub-menu mm-collapse mm-show">
                            <li id="menu-pending-orders"><a href="<?php echo base_url('orders?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Pending Orders</a></li>
                            <li id="menu-scheduled-orders"><a href="<?php echo base_url('orders/scheduled?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Scheduled Orders</a></li>
                            <li id="menu-delivered-orders"><a href="<?php echo base_url('orders/delivered?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Delivered Orders</a></li>
                        </ul>
                    </li>


                    <li id="menu-contacts">
                        <a href="<?php echo base_url('contacts?token=' . urlencode($token)); ?>">
                            <i class="bx bx-user-circle"></i>
                            <span data-key="t-zodiac-weekly">Contacts</span>
                        </a>
                    </li>
                    <li id="menu-client-prfile">
                        <a href="<?php echo base_url('account?token=' . urlencode($token)); ?>">
                            <i class="fa fa-cog"></i>
                            <span data-key="t-settings">Settings</span>
                        </a>
                    </li>
                <?php } ?>

                <?php if ($permission == 1) { ?>
                    <li id="menu-dashboard">
                        <a href="<?php echo base_url('sendmail?token=' . urlencode($token)); ?>">
                            <i class="bx bx-mail-send label-icon"></i>
                            <span data-key="t-zodiac-weekly">Send Ringless Voicemails</span>
                        </a>
                    </li>
                    <li id="menu-recordings">
                        <a href="<?php echo base_url('recordings?token=' . urlencode($token)); ?>">
                            <i class="bx bxs-microphone"></i>
                            <span data-key="t-zodiac-weekly">Recordings</span>
                        </a>
                    </li>

                    <li class="">
                        <a href="javascript: void(0);" aria-disabled="true">
                            <i class="bx bxs-volume-full"></i>
                            <span data-key="t-pages">AI Avatar Voices</span>
                        </a>
                        <ul class="sub-menu mm-collapse mm-show">
                            <li id="menu-pro-voices"><a href="<?php echo base_url('provoices?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Pro Voices</a></li>
                            <li id="menu-voices"><a href="<?php echo base_url('voices?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Your Cloned Voices</a></li>
                        </ul>
                    </li>

                    <li id="menu-scripts">
                        <a href="<?php echo base_url('scripts?token=' . urlencode($token)); ?>">
                            <i class="bx bx-message-square-detail"></i>
                            <span data-key="t-zodiac-weekly">AI Avatar Scripts</span>
                        </a>
                    </li>

                    <li class="">
                        <a href="javascript: void(0);" aria-disabled="true">
                            <i class="bx bx-mail-send label-icon"></i>
                            <span data-key="t-pages">Past Ringless Voicemails</span>
                        </a>
                        <ul class="sub-menu mm-collapse mm-show">
                            <li id="menu-pending-orders"><a href="<?php echo base_url('orders?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Pending Orders</a></li>
                            <li id="menu-scheduled-orders"><a href="<?php echo base_url('orders/scheduled?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Scheduled Orders</a></li>
                            <li id="menu-delivered-orders"><a href="<?php echo base_url('orders/delivered?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Delivered Orders</a></li>
                        </ul>
                    </li>
                <?php } ?>

                <?php if ($permission == 0) { ?>
                    <li id="menu-recordings">
                        <a href="<?php echo base_url('recordings?token=' . urlencode($token)); ?>">
                            <i class="bx bxs-microphone"></i>
                            <span data-key="t-zodiac-weekly">Recordings</span>
                        </a>
                    </li>

                    <li class="">
                        <a href="javascript: void(0);" aria-disabled="true">
                            <i class="bx bxs-volume-full"></i>
                            <span data-key="t-pages">AI Avatar Voices</span>
                        </a>
                        <ul class="sub-menu mm-collapse mm-show">
                            <li id="menu-pro-voices"><a href="<?php echo base_url('provoices?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Pro Voices</a></li>
                            <li id="menu-voices"><a href="<?php echo base_url('voices?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>User Cloned Voices</a></li>
                        </ul>
                    </li>

                    <li id="menu-scripts">
                        <a href="<?php echo base_url('scripts?token=' . urlencode($token)); ?>">
                            <i class="bx bx-message-square-detail"></i>
                            <span data-key="t-zodiac-weekly">AI Avatar Scripts</span>
                        </a>
                    </li>

                    <li class="">
                        <a href="javascript: void(0);" aria-disabled="true">
                            <i class="bx bx-mail-send label-icon"></i>
                            <span data-key="t-pages">Past Ringless Voicemails</span>
                        </a>
                        <ul class="sub-menu mm-collapse mm-show">
                            <li id="menu-pending-orders"><a href="<?php echo base_url('orders?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Pending Orders</a></li>
                            <li id="menu-scheduled-orders"><a href="<?php echo base_url('orders/scheduled?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Scheduled Orders</a></li>
                            <li id="menu-delivered-orders"><a href="<?php echo base_url('orders/delivered?token=' . urlencode($token)); ?>" data-key="t-starter-page"><i class="bx bx-radio-circle"></i>Delivered Orders</a></li>
                        </ul>
                    </li>

                    <li id="menu-accounts">
                        <a href="<?php echo base_url('accounts?token=' . urlencode($token)); ?>">
                            <i class="bx bx-user-circle"></i>
                            <span data-key="t-zodiac-weekly">Users</span>
                        </a>
                    </li>

                    <li id="menu-ips">
                        <a href="<?php echo base_url('ips?token=' . urlencode($token)); ?>">
                            <i class="bx bx-user-pin"></i>
                            <span data-key="t-zodiac-weekly">Allowed IPs</span>
                        </a>
                    </li>
                <?php } ?>

            </ul>
        </div>
    </div>
</div>
<!-- Left Sidebar End -->