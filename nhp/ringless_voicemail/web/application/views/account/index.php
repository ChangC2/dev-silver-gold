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
                  <h4 class="card-title">Settings</h4>
                  <p class="card-title-desc">Edit Settings</p>
                </div>
              </div>
            </div>
            <div class="card-body">
              <form role="form" action="<?php base_url('account/manage') ?>" method="post">
                <div>
                  <?php echo validation_errors(); ?>
                  <div class="card p-3">
                    <div class="row">
                      <div class="col-12">
                        <div class="form-group mb-3">
                          <label for="accountname">Account Name</label>
                          <input type="text" class="form-control" id="accountname" name="accountname" placeholder="Accountname" value="<?php echo $account_data['accountname'] ?>">
                        </div>
                      </div>
                    </div>

                    <div class="row">
                      <div class="col-12">
                        <div class="form-group mb-3">
                          <label for="email">Email</label>
                          <input type="email" class="form-control" id="email" name="email" placeholder="Email" value="<?php echo $account_data['email'] ?>" <?php echo $permission == 0 ? '' : 'readonly'; ?>>
                        </div>
                      </div>
                    </div>

                    <div class="row">
                      <div class="col-12">
                        <div class="form-group mb-3">
                          <label for="email">Phone</label>
                          <input type="text" class="form-control" id="mobile_number" name="mobile_number" placeholder="Phone" value="<?php echo $account_data['mobile_number'] ?>">
                        </div>
                      </div>
                    </div>

                    <div class="row">
                      <div class="col-12" <?php echo ($permission == 2) ? '' : 'hidden'; ?>>
                        <div class="form-group mb-3">
                          <label for="caller_id">Caller ID</label>
                          <input type="text" class="form-control" id="caller_id" name="caller_id" placeholder="Caller ID" value="<?php echo $account_data['caller_id'] ?>">
                        </div>
                      </div>
                    </div>

                    <div class="row">
                      <div class="col-12" <?php echo ($permission == 2) ? '' : 'hidden'; ?>>
                        <div class="form-group mb-3">
                          <label for="credits">Credits</label>
                          <input type="text" class="form-control" id="credits" name="credits" value="<?php echo $account_data['credits'] ?>" placeholder="Credits">
                        </div>
                      </div>
                    </div>

                    <div class="row">
                      <div class="col-12" <?php echo ($permission == 2) ? '' : 'hidden'; ?>>
                        <div class="form-group mb-3">
                          <label for="credits">Farm</label>
                          <input type="text" class="form-control" id="farm_name" name="farm_name" value="<?php echo $account_data['farm_name'] ?>" placeholder="Farm">
                        </div>
                      </div>
                    </div>

                    <div class="form-group">
                      <div class="alert alert-info alert-dismissible" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        Leave the password field empty if you don't want to change.
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
                  </div>

                  <div class="card p-3" <?php echo ($permission == 0) ? '' : 'hidden'; ?>>
                    <div class="row">
                      <div class="col-12">
                        <div class="form-group mb-3">
                          <label for="stripe_pk">Stripe Publishable Key</label>
                          <input type="text" class="form-control" id="stripe_pk" name="stripe_pk" value="<?php echo getenv('STRIPE_PUBLISHABLE_KEY') ?>">
                        </div>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-12">
                        <div class="form-group mb-3">
                          <label for="stripe_sk">Stripe Secret Key</label>
                          <input type="text" class="form-control" id="stripe_sk" name="stripe_sk" value="<?php echo getenv('STRIPE_SECRET_KEY') ?>">
                        </div>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-12">
                        <div class="form-group mb-3">
                          <label for="min_credits">Min credits amount to purchase at once</label>
                          <input type="text" class="form-control" id="min_credits" name="min_credits" value="<?php echo getenv('MIN_CREDITS_TO_PURCHASE') ?>">
                        </div>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-12">
                        <div class="form-group mb-3">
                          <label for="credit_cost">Credit Price ($)</label>
                          <input type="text" class="form-control" id="credit_cost" name="credit_cost" value="<?php echo getenv('CREDIT_COST') ?>">
                        </div>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-12">
                        <div class="form-group mb-3">
                          <label for="voicemail_credits">Credits amount needed for one voicemail</label>
                          <input type="text" class="form-control" id="voicemail_credits" name="voicemail_credits" value="<?php echo getenv('CREDITS_TO_SEND') ?>">
                        </div>
                      </div>
                    </div>
                  </div>


                  <div class="modal-footer">
                    <button type="button" class="btn btn-primary waves-effect waves-light" onclick="updateAccount();"><i class="bx bx-save font-size-16 align-middle me-2"></i>Save</button>
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
    $("#menu-client-prfile").addClass('mm-active');
  });

  var phones = [{
    "mask": "(###) ###-####"
  }];

  $('#mobile_number').inputmask({
    mask: phones,
    greedy: false,
    definitions: {
      '#': {
        validator: "[0-9]",
        cardinality: 1
      }
    }
  });

  var updateAccount = function() {
    var accountname = $("#accountname").val().trim();
    var email = $("#email").val().trim();
    var mobile_number = $("#mobile_number").val().trim();
    var farm_name = $("#farm_name").val().trim();
    var caller_id = $("#caller_id").val().trim();
    var credits = $("#credits").val().trim();
    var password = $("#password").val().trim();
    var cpassword = $("#cpassword").val().trim();

    var stripe_pk = $("#stripe_pk").val().trim();
    var stripe_sk = $("#stripe_sk").val().trim();
    var min_credits = $("#min_credits").val().trim();
    var credit_cost = $("#credit_cost").val().trim();
    var voicemail_credits = $("#voicemail_credits").val().trim();

    if (accountname === "") {
      show_msg("error", "Enter Name");
      return;
    }

    if (email === "") {
      show_msg("error", "Enter Email");
      return;
    }

    if (mobile_number === "") {
      show_msg("error", "Enter Mobile Number");
      return;
    }

    if (password != "" && password != cpassword) {
      show_msg("error", "Mismatch password");
      return;
    }

    var postData = {
      token: "<?php echo $token; ?>",
      accountname: accountname,
      email: email,
      mobile_number: mobile_number,
      credits: credits,
      farm_name: farm_name,
      caller_id: caller_id,
      stripe_pk: stripe_pk,
      stripe_sk: stripe_sk,
      min_credits: min_credits,
      credit_cost: credit_cost,
      voicemail_credits: voicemail_credits,
    };
    if (password != "") {
      postData["password"] = password;
    }
    showLoading();
    $.ajax({
      url: "<?php echo base_url('account/update'); ?>",
      type: "POST",
      data: postData
    }).done(function(data) {
      var result = $.parseJSON(data);
      if (result['status'] == true) {
        show_msg("success", "Success to update");
      } else {
        show_msg("error", "Fail to update");
      }
    }).fail(function() {
      show_msg("error", "Fail to update");
    }).always(function() {
      hideLoading()
    });
  }
</script>