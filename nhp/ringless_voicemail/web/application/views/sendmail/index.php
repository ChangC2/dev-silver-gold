<!-- start main content-->
<div class="main-content">
  <div class="page-content">
    <div class="container-fluid px-3">
      <div class="row mt-2">
        <div class="col-12">
          <div class="card px-3">
            <div class="card-header">
              <h4 class="card-title" style="font-size: 21px;">Send Voicemails</h4>
              <p class="card-title-desc">Welcome to our Ringless Voicemail Feature. This feature will allow you to send Ringless Voicemails to your contacts at 10 cents per message dropped.
                Choose your Audio recording from Below and click Send. You will get a confirmation screen showing you all potential costs prior to sending.</p>
            </div><!-- end card header -->

            <div class="card-body">
              <div class="card border ">
                <div class="card-header bg-transparent ">
                  <h5 class="my-0"><i class="bx bxs-microphone me-3"></i>Basic Information</h5>
                </div>
                <div class="card-body">
                  <div class="card-title">Farm Name : <?php echo $farm_name ?></div>
                  <div class="card-title mt-2">
                    <span># of Records : <?php echo $contact_count ?></span>
                    <a href="#" onclick="showContacts()" style="right: 0;position:absolute;margin-right:30px;">View 1st 10 #s</a>
                  </div>
                  <div class="card-title mt-2">
                    Potential Cost : <?php echo $contact_count * 0.1 ?>
                  </div>
                </div>
              </div>

              <div class="card border ">
                <div class="card-header bg-transparent ">
                  <h5 class="my-0"><i class="bx bxs-microphone me-3"></i>Forwarding Number</h5>
                </div>

                <div class="card-body">
                  <div class="row">
                    <div class="col-lg-12 col-md-12">
                      <label class="form-label">Forwarding Number</label>
                      <input type="text" class="form-control" id="forwarding_number" value="<?php echo $forwarding_number; ?>">
                    </div>
                    <div class="col-lg-12 col-md-12 mt-1 ms-3">
                      <span class="text-success" style="font-size: 13px;">*This is the phone number will be sent to if they call back - Should be your number.</span>
                    </div>
                  </div>

                  <div class="row mt-2">
                    <div class="col-lg-12 col-md-12">
                      <label class="form-label">Confirm Forwarding Number</label>
                      <input type="text" class="form-control" id="re_forwarding_number" value="">
                    </div>
                  </div>
                </div>
              </div>

              <div class="card border  mt-3">
                <div class="card-header bg-transparent ">
                  <div class="row">
                    <div class="col-12">
                      <div class="d-inline">
                        <div class="d-inline" onclick="onClickAudioSource(0)">
                          <input class="form-check-input ms-2" style="margin-top: 5px;" type="radio" name="formRadios" id="radioRecording" checked>
                          <label class="form-check-label ms-1" style="font-size: 17px;" for="radioRecording">
                            Pre-Recorded Audio
                          </label>
                        </div>
                        <div class="d-inline" style="margin-left: 40px;" onclick="onClickAudioSource(1)">
                          <input class="form-check-input" style="margin-top: 5px;" type="radio" name="formRadios" id="radioVoice">
                          <label class="form-check-label ms-1" style="font-size: 17px;" for="radioVoice">
                            AI Text to Voice
                          </label>
                        </div>
                      </div>
                    </div>
                  </div>

                </div>
                <div class="card-body">
                  <div class="row" id="div_recording">
                    <div class="col-lg-12 col-md-12">
                      <label class="form-label">The pre-recorded message to deliver into your contact's voicemail inbox.</label>
                      <select class="form-control" data-trigger name="recording" id="recording" placeholder="Select Recording">
                        <option value="">Select Recording</option>
                        <?php
                        foreach ($recordings as $recording) {
                        ?>
                          <option value="<?php echo $recording['id']; ?>"><?php echo $recording['name']; ?></option>
                        <?php
                        }
                        ?>
                      </select>
                    </div>
                  </div>

                  <div class="row d-none" id="div_voice">
                    <div class="col-lg-12 col-md-12">
                      <label class="form-label">AI Avatar Voice</label>
                      <select class="form-control" data-trigger name="voice" id="voice" placeholder="Select AI Avatar Voice">
                        <option value="">Select AI Avatar Voice</option>

                        <optgroup label="--- Pro Voices ---">
                          <?php
                          foreach ($pro_voices as $voice) {
                          ?>
                            <option value="<?php echo $voice['id']; ?>"><?php echo $voice['name']; ?></option>
                          <?php
                          }
                          ?>
                        </optgroup>
                        <?php if (count($user_voices) > 0) { ?>
                          <optgroup label="--- Your Cloned Voices ---">
                            <?php
                            foreach ($user_voices as $voice) {
                            ?>
                              <option value="<?php echo $voice['id']; ?>"><?php echo $voice['name']; ?></option>
                            <?php
                            }
                            ?>
                          </optgroup>
                        <?php } ?>
                      </select>
                    </div>
                    <div class="col-lg-12 col-md-12 mt-1 ms-3">
                      <span class="text-success" style="font-size: 13px;">*AI Avatar Voice to replicate personalized voicemails sending to each of your contacts. Please add your AI Avatar Voices in the AI Avatar Voices page if you want to use your voices.</span>
                    </div>
                  </div>

                  <div class="row mt-3">
                    <div class="col-lg-12 col-md-12">
                      <audio id="audio_player" style="width: 100%;" controls playsinline></audio>
                    </div>
                  </div>

                  <div class="row mt-3 d-none" id="div_content">
                    <div class="col-lg-12 col-md-12 mt-3">
                      <label class="form-label">AI Avatar Script</label>
                      <select class="form-control" data-trigger name="script" id="script" placeholder="Select AI Avatar Script">
                        <option value="">Manual Input</option>
                        <?php
                        foreach ($scripts as $script) {
                        ?>
                          <option value="<?php echo $script['id']; ?>"><?php echo $script['name']; ?></option>
                        <?php
                        }
                        ?>
                      </select>
                    </div>
                    <div class="col-lg-12 col-md-12 mt-1 ms-3">
                      <span class="text-success" style="font-size: 13px;">*You can easily compose your message by using the AI Avatar Script . Please add your AI Avatar Scripts in the AI Avatar Scripts page if you want to use your scripts.</span>
                    </div>

                    <div class="col-lg-12 col-md-12 mt-3">
                      <label class="form-label">Salutation</label>
                      <select class="form-control" data-trigger name="u_salutation" id="u_salutation" placeholder="Select Salutation">
                        <option value="0">First Name</option>
                        <option value="1">Hello First Name</option>
                        <option value="2">Hey First Name</option>
                        <option value="3">Hope all is well First Name</option>
                        <option value="4">Good Afternoon First Name</option>
                        <option value="5">Good Morning First Name</option>
                        <option value="6">Good Evening First Name</option>
                        <option value="7">- No salutation -</option>
                      </select>
                    </div>
                    <div class="col-lg-12 col-md-12 mt-1 mx-3">
                      <div class="text-success" style="font-size: 12px;">*Salutation to incorporate to your message. For example: Good morning First Name, this is..</div>
                    </div>
                    <div class="col-lg-12 col-md-12 mt-3">
                      <label for="basicpill-address-input" class="form-label">Type the text that you want AIValet to read to your contacts using your voice</label>
                      <textarea id="content" class="form-control" rows="3" placeholder="Compose your text to speech message here... "></textarea>
                    </div>
                  </div>
                </div>
              </div>

              <div class="card border  mt-3">
                <div class="card-header bg-transparent ">
                  <div class="row">
                    <div class="col-12">
                      <div class="d-inline">
                        <div class="d-inline" onclick="onClickSchedule(0)">
                          <input class="form-check-input ms-2" style="margin-top: 5px;" type="radio" name="formScheduleRadios" id="radioImmediately" checked>
                          <label class="form-check-label ms-1" style="font-size: 17px;" for="radioImmediately">
                            Send immediately
                          </label>
                        </div>
                        <div class="d-inline" style="margin-left: 40px;" onclick="onClickSchedule(1)">
                          <input class="form-check-input" style="margin-top: 5px;" type="radio" name="formScheduleRadios" id="radioAdvancedTime">
                          <label class="form-check-label ms-1" style="font-size: 17px;" for="radioAdvancedTime">
                            Send on
                          </label>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="card-body d-none" id="div_send_time">
                  <div class="row">
                    <div class="col-lg-6 col-md-6">
                      <label for="datetime-local-input" class="form-label">Date</label>
                      <input class="form-control" type="date" value="<?php echo date('Y-m-d'); ?>" id="date_input" min="<?php echo date('Y-m-d'); ?>" />
                    </div>
                    <div class="col-lg-6 col-md-6">
                      <label for="datetime-local-input" class="form-label">Time</label>
                      <input class="form-control" type="time" value="08:00" id="time_input" min="08:00" max="17:00" required />
                    </div>
                  </div>
                </div>
                <div class="card-body">
                  <div class="row">
                    <div class="col-12">
                      <div class="form-check">
                        <input id="chk_break_up" type="checkbox" class="form-check-input">
                        <label class="form-check-label" for="chk_break_up">Break Up Broadcast</label>
                      </div>
                    </div>
                  </div>
                  <div id='div_break_up' class="row mt-3 d-none">
                    <div class="col-lg-6 col-md-6">
                      <label for="datetime-local-input" class="form-label">Number of Broadcasts</label>
                      <input class="form-control" type="number" value="1" id="broadcasts_number" min="1" />
                      <span id="records_per_day" class="text-info" style="font-size: 13px;font-style: italic;position: absolute;font-size: 13px;margin-top: -30px;margin-right: 50px;right: 0;"></span>
                    </div>
                    <div class="col-lg-6 col-md-6">
                      <label for="datetime-local-input" class="form-label">Days between each</label>
                      <input class="form-control" type="number" value="1" id="between_days" />
                    </div>
                  </div>
                </div>
              </div>
              <div class="row mt-4 mb-3">
                <div class="col-lg-12 col-md-12 text-center">
                  <button type="button" class="btn btn-primary waves-effect btn-label waves-light" style="width: 200px;height:45px;" onclick="onClickSend()"><i class=" bx bx-mail-send label-icon"></i> Preview and Send</button>
                </div>
              </div>
            </div><!-- end card-body -->
          </div><!-- end card -->
        </div><!-- end col -->
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