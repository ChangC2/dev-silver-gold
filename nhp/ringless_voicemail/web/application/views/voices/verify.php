<!-- start main content-->

<body>
  <div class="">
    <div class="container-fluid px-2">
      <div class="row mt-2">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h4 class="card-title" style="font-size: 21px;">New User Cloned Voice</h4>
              <p class="card-title-desc">
                <?php if ($voice["active"] == 0) {
                  echo "Please enter the api keys and your name and click the approve button to approve for user cloned voice.";
                } else {
                  echo "This user cloned voice has been approved.";
                }; ?>
              </p>
            </div><!-- end card header -->

            <div class="card-body">
              <div class="card border ">
                <div class="card-header bg-transparent ">
                  <h5 class="my-0"><i class="bx bxs-microphone me-3"></i>Voice Information</h5>
                </div>
                <div class="card-body">
                  <div class="card-title"><strong>LM User ID : </strong><?php echo $user_info["request_id"]; ?></div>
                  <div class="card-title"><strong>User name :</strong> <?php echo $user_info["accountname"]; ?></div>
                  <div class="card-title"><strong>User email address: </strong><?php echo $user_info["email"]; ?></div>
                  <div class="card-title"><strong>User phone number : </strong><?php echo $user_info["mobile_number"]; ?></div>
                  <div class="card-title">
                    <strong>Voice : </strong><a href="<?php echo $voice["url"]; ?>" data-linkindex="1"><?php echo $voice["name"]; ?></a>
                  </div>
                  <div class="card-title">
                    <strong>Link to file : </strong><a href="<?php echo $voice["url"]; ?>" data-linkindex="1"><?php echo $voice["url"]; ?></a>
                  </div>
                </div>
              </div>

              <div class="card border d-none">
                <div class="card-header bg-transparent ">
                  <h5 class="my-0"><i class="bx bxs-microphone me-3"></i>API Keys</h5>
                </div>

                <div class="card-body">
                  <div class="row">
                    <div class="col-lg-12 col-md-12">
                      <label class="form-label">Cowboy Key</label>
                      <input type="text" class="form-control" id="voice_id" value="<?php echo $voice["voice_id"]; ?>" <?php if ($voice["active"] == 1) {
                                                                                                                        echo "readonly";
                                                                                                                      } ?>>
                    </div>
                    <div class="col-lg-12 col-md-12 mt-1 ms-3">
                      <span class="text-success" style="font-size: 13px;">*This is the key to call the cowboy api to send the voicemail.</span>
                    </div>
                  </div>

                  <div class="row mt-3">
                    <div class="col-lg-12 col-md-12">
                      <label class="form-label">Eden Key</label>
                      <input type="text" class="form-control" id="eden_id" value="<?php echo $voice["eden_id"]; ?>" <?php if ($voice["active"] == 1) {
                                                                                                                      echo "readonly";
                                                                                                                    } ?>>
                    </div>
                    <div class="col-lg-12 col-md-12 mt-1 ms-3">
                      <span class="text-success" style="font-size: 13px;">*This is the key to preview the your cloned voice.</span>
                    </div>
                  </div>
                </div>
              </div>

              <div class="row mt-3">
                <div class="col-lg-12 col-md-12">
                  <label class="form-label">Approved by</label>
                  <input type="text" class="form-control" id="admin_name" value="<?php echo $voice["admin_name"]; ?>" <?php if ($voice["active"] == 1) {
                                                                                                                        echo "readonly";
                                                                                                                      } ?>>
                </div>
                <div class="col-lg-12 col-md-12 mt-1 ms-3">
                  <span class="text-success" style="font-size: 13px;">*Please enther your name.</span>
                </div>
              </div>

              <?php if ($voice["active"] == 0) { ?>
                <div class="row mt-4 mb-3">
                  <div class="col-lg-12 col-md-12 text-center">
                    <button type="button" class="btn btn-primary waves-effect btn-label waves-light" onclick="onClickApprove()"><i class="bx bxs-microphone label-icon"></i> Approve</button>
                  </div>
                </div>
              <?php }; ?>

            </div><!-- end card-body -->
          </div><!-- end card -->
        </div><!-- end col -->
      </div>
    </div>
  </div>



  <!-- end main content-->

  <script type="text/javascript">
    function onClickApprove() {
      var voice_id = $("#voice_id").val().trim();
      if (voice_id === "") {
        show_msg("error", "No Cowboy Key");
        return;
      }
      var eden_id = $("#eden_id").val().trim();
      if (eden_id === "") {
        show_msg("error", "No Eden Key");
        return;
      }

      var admin_name = $("#admin_name").val().trim();
      if (admin_name === "") {
        show_msg("error", "Enter your name");
        return;
      }
      window.location.href = "<?php echo base_url() . "voices/verify?id=" . $encypted_id; ?>" + "&admin_name=" + admin_name + "&voice_id=" + voice_id + "&eden_id=" + eden_id;
    }

    var show_msg = function(icon, title) {
      Swal.fire({
        position: "top-center",
        icon: icon,
        title: title,
        showConfirmButton: !1,
        timer: 2000
      });
    }
  </script>