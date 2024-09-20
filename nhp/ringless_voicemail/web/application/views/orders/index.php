<!-- start main content-->
<div class="main-content">
  <div class="page-content">
    <div class="container-fluid">
      <?php if ($permission != 0) { ?>
        <div class="row mb-3">
          <div class="col-6">
            <h4 class="card-title"><?php echo $page_title ?></h4>
            <p class="card-title-desc">Manage your orders</p>
          </div>

          <div class="col-6 d-md-none d-sm-inline" style="margin-top:5px;">
            <button type="button" class="btn btn-primary waves-effect btn-label waves-light" onclick="sendVoicemails()"><i class=" bx bx-mail-send label-icon"></i> Send Voicemails</button>
          </div>

          <div class="col-6 d-none d-md-inline" style=" text-align: end;margin-top:5px;padding-right:30px;">
            <button type="button" class="btn btn-primary waves-effect btn-label waves-light" onclick="sendVoicemails()"><i class=" bx bx-mail-send label-icon"></i> Send Voicemails</button>
          </div>
        </div>
      <?php } ?>

      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-body">
              <div class="row">
                <?php if ($permission == 0) { ?>
                  <div class="col-lg-3 col-md-3 mt-1">
                    <select class="form-control" data-trigger name="user" id="user" placeholder="Select User">
                      <option value="">Select User</option>
                      <?php
                      foreach ($users as $user) {
                      ?>
                        <option value="<?php echo $user['id']; ?>" <?php if ($selected_user == $user['id']) echo "selected" ?>><?php echo $user['accountname']; ?></option>
                      <?php
                      }
                      ?>
                    </select>
                  </div>

                  <div class="col-lg-5 col-md-5  mt-1">
                    <input class="form-control" style="display: inline;width:calc(50% - 10px);" type="date" value="<?php echo $start_date; ?>" id="start_date" max="<?php echo date('Y-m-d'); ?>" />
                    <div style="display: inline;text-align: center;width:20px;">~</div>
                    <input class="form-control" style="display: inline;width:calc(50% - 10px);" type="date" value="<?php echo $end_date; ?>" id="end_date" max="<?php echo date('Y-m-d'); ?>" />
                  </div>
                  <div class="col-lg-4 col-md-4  mt-1">
                    <input class="form-control" style="display: inline;width:calc(100% - 135px);" type="text" value="<?php echo $search_query; ?>" id="search_query" placeholder="Search..." />
                    <button class="btn btn-primary waves-effect btn-label waves-light" style="width:120px;margin-left:10px;" type="button" onclick="searchOrder()"><i class=" bx bx-search-alt-2 label-icon"></i> Search</button>
                  </div>
                <?php } else { ?>
                  <div style="display: none;"><select data-trigger name="user" id="user" placeholder="Select User">
                      <option value="">Select User</option>
                      <?php
                      foreach ($users as $user) {
                      ?>
                        <option value="<?php echo $user['id']; ?>" <?php if ($selected_user == $user['id']) echo "selected" ?>><?php echo $user['accountname']; ?></option>
                      <?php
                      }
                      ?>
                    </select>
                  </div>

                  <div class="col-lg-7 col-md-7  mt-1">
                    <input class="form-control" style="display: inline;width:calc(50% - 10px);" type="date" value="<?php echo $start_date; ?>" id="start_date" max="<?php echo date('Y-m-d'); ?>" />
                    <div style="display: inline;text-align: center;width:20px;">~</div>
                    <input class="form-control" style="display: inline;width:calc(50% - 10px);" type="date" value="<?php echo $end_date; ?>" id="end_date" max="<?php echo date('Y-m-d'); ?>" />
                  </div>
                  <div class="col-lg-5 col-md-5  mt-1">
                    <input class="form-control" style="display: inline;width:calc(100% - 135px);" type="text" value="<?php echo $search_query; ?>" id="search_query" placeholder="Search..." />
                    <button class="btn btn-primary waves-effect btn-label waves-light" style="width:120px;margin-left:10px;" type="button" onclick="searchOrder()"><i class=" bx bx-search-alt-2 label-icon"></i> Search</button>
                  </div>
                <?php } ?>
              </div>

              <div class="table-responsive mt-1">
                <table id="orders-table" class="table table-bordered dt-responsive nowrap w-100">
                  <thead>
                    <tr>
                      <th></th>
                      <th>Date & Time</th>
                      <th>Order #</th>
                      <th>Farm Name</th>
                      <th>Records</th>
                      <?php if ($page_title == "Delivered Orders") { ?>
                        <th>Queue</th>
                        <th>Success</th>
                        <th>Failed</th>
                        <th>Credits Used</th>
                      <?php } ?>
                      <?php if ($page_title != "Delivered Orders") { ?>
                        <th>Credits Hold</th>
                      <?php } ?>
                      <th>Type</th>
                      <?php if ($permission == 0) { ?>
                        <th>Created By</th>
                      <?php } ?>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                  </tbody>
                </table>
              </div>

              <?php
              $totalRecords = 0;
              $totalSuccess = 0;
              $totalFail = 0;
              foreach ($orders_data as $order) {
                $totalRecords += $order['record_count'];
                $totalSuccess += $order['success'];
                $totalFail += $order['fail'];
              }
              if ($page_title == "Delivered Orders") {
              ?>
                <div class="text-center m-2">
                  <b>
                    <span class="text-info">Total Record Count : <?php echo $totalRecords; ?></span>
                    <span class="text-success ms-2">Total Success : <?php echo $totalSuccess; ?></span>
                    <span class="text-danger ms-2">Total Fail : <?php echo $totalFail; ?></span>
                    <span class="text-info ms-2">Total Credits Used : <?php echo $totalSuccess * 0.1; ?></span>
                  </b>
                </div>
              <?php } ?>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <footer class="footer">
    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-6">
          ©
          <script>
            document.write(new Date().getFullYear())
          </script>
        </div>
        <div class="col-sm-6">
          <div class="text-sm-end d-none d-sm-block">
            Powered by <a href="https://newhomepage.com/" class="text-decoration-underline" target="_blank">NewHomePage</a>
          </div>
        </div>
      </div>
    </div>
  </footer>


</div>
<!-- end main content-->