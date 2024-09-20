<!-- start main content-->
<div class="main-content">
  <div class="page-content">
    <div class="container-fluid">
      <div class="row mb-2 <?php echo $permission == 0 ? '' : 'd-none'; ?>">
        <div class="col-md-8">
          <h4 class="card-title">Recordings</h4>
          <p class="card-title-desc">Manage all recordings</p>
        </div>
        <div class="col-md-4 mt-1 ">
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
      </div>

      <?php if ($permission != 0) { ?>
        <div class="row mb-3">
          <div class="col-6">
            <h4 class="card-title">Recordings</h4>
            <p class="card-title-desc">Manage your recordings</p>
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
            <div class="card-header <?php echo $permission == 0 ? 'd-none' : ''; ?>">
              <div class="row">
                <div class="col-md-4 col-sm-12">
                  <select style="display: inline !important; width:100% !important;" class="form-control" data-trigger name="input_device" id="input_device" placeholder="Choose an audio input device">
                    <option value="">Choose an audio input device</option>
                  </select>
                </div>
                <div class="col-md-8 col-sm-12 d-md-none d-sm-inline mt-2">
                  <button type="button" class="btn btn-success waves-effect btn-label waves-light" onclick="show_add_dlg(0)"><i class="bx bxs-microphone label-icon"></i> Record</button>
                  <button type="button" class="btn btn-info waves-effect btn-label waves-light" style="margin-left:10px;" onclick="show_add_dlg(1)"><i class=" bx bx-cloud-upload label-icon"></i> Upload</button>
                </div>
                <div class="col-md-8 col-sm-12 d-none d-md-inline" style="text-align: end;">
                  <button type="button" class="btn btn-success waves-effect btn-label waves-light" onclick="show_add_dlg(0)"><i class="bx bxs-microphone label-icon"></i> Record</button>
                  <button type="button" class="btn btn-info waves-effect btn-label waves-light" onclick="show_add_dlg(1)"><i class=" bx bx-cloud-upload label-icon"></i> Upload</button>
                </div>
              </div>
            </div>

            <div class="card-body">
              <div class="table-responsive">
                <table id="recordings-table" class="table table-bordered dt-responsive nowrap w-100">
                  <thead>
                    <tr>
                      <th></th>
                      <th>Name</th>
                      <?php if ($permission == 0) { ?>
                        <th>Created By</th>
                      <?php } ?>
                      <th>Created Time</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                  </tbody>
                </table>
              </div>
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