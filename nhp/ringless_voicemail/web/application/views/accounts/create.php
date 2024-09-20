<!-- Content Wrapper. Contains page content -->
<!-- start main content-->
<div class="main-content">
  <div class="page-content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <div class="row">
                <div class="col-md-9">
                  <h4 class="card-title">Users</h4>
                  <p class="card-title-desc">Create New User</p>
                </div>
              </div>
            </div>
            <div class="card-body">
              <form role="form" action="<?php base_url('accounts/create') ?>" method="post">
                <input type="hidden" class="form-control" id="token" name="token" value="<?php echo $token ?>">
                <div class="card-body">
                  <?php echo validation_errors(); ?>
                  <div class="row">
                    <div class="col-6">
                      <div class="form-group mb-3">
                        <label for="groups">Permission</label>
                        <select class="form-control" id="groups" name="groups" style="width:100%">
                          <option value="">Select Permission</option>
                          <?php foreach ($group_data as $k => $v) : ?>
                            <option value="<?php echo $v['id'] ?>"><?php echo $v['group_name'] ?></option>
                          <?php endforeach ?>
                        </select>
                      </div>
                    </div>
                    <div class="col-6">
                      <div class="form-group mb-3">
                        <label for="accountname">Name</label>
                        <input type="text" class="form-control" id="accountname" name="accountname" placeholder="Name">
                      </div>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-6">
                      <div class="form-group mb-3">
                        <label for="email">Email</label>
                        <input type="email" class="form-control" id="email" name="email" placeholder="Email">
                      </div>
                    </div>
                    <div class="col-6">
                      <div class="form-group mb-3">
                        <label for="mobile_number">Phone</label>
                        <input type="text" class="form-control" id="mobile_number" name="mobile_number" placeholder="Phone">
                      </div>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-6">
                      <div class="form-group mb-3">
                        <label for="password">Password</label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                      </div>
                    </div>
                    <div class="col-6">
                      <div class="form-group mb-3">
                        <label for="cpassword">Confirm password</label>
                        <input type="password" class="form-control" id="cpassword" name="cpassword" placeholder="Confirm Password">
                      </div>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-6">
                      <div class="form-group mb-3">
                        <label for="credits">Credits</label>
                        <input type="text" class="form-control" id="credits" name="credits" placeholder="Credits">
                      </div>
                    </div>
                    <div class="col-6">
                      <div class="mb-3">
                        <label class="form-label">Status</label>
                        <select class="form-control" data-trigger name="status" id="status" placeholder="Status">
                          <option value="">Select Status</option>
                          <option value="0">Inactive</option>
                          <option value="1">Active</option>
                        </select>
                      </div>
                    </div>
                  </div>

                </div>

                <div class="modal-footer">
                  <a href="<?php echo base_url('accounts?token=' . urlencode($token)) ?>" class="btn btn-light waves-effect waves-light"><i class="bx bx-x-circle font-size-16 align-middle me-2"></i>Back</a>
                  <button type="submit" class="btn btn-primary waves-effect waves-light"><i class="bx bx-save font-size-16 align-middle me-2"></i>Save</button>
                </div>
              </form>
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

<script type="text/javascript">
  $(document).ready(function() {
    $("#menu-accounts").addClass('mm-active');
  });

  new Choices("#groups", {
    shouldSort: !1
  });

  new Choices("#status", {
    shouldSort: !1
  });
</script>