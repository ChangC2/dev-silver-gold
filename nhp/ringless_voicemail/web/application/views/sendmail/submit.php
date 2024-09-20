<div id="submit-modal" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Confirm your messages</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" onclick="oncloseModal()"></button>
      </div>
      <div class="modal-body">
        <div class="card bg-warning border-warning text-white-50" style="font-size: 13px;">
          <div class="py-2 px-3">
            <div class="card-text text-white">
              <b>Preview your message.</b>
            </div>
            <div class="card-text text-white ms-3">
              · Make sure it is what you want to send.
            </div>
            <div class="card-text text-white ms-3">
              · There is no stopping it or changing it once you have clicked Send Now.
            </div>
            <div class="card-text text-white mt-1">
              <b>Preview live here is only available for Pro Voices.</b>For Cloned Voices use the Send test which will send to phone on account. This process can take up to an hour. You can preview cloned voices but will play back in Pro Voice.
            </div>
          </div>
        </div>

        <span class="cart-title-desc"></span>
        <div class="mt-2" style="font-size: 15px;">
          <span id="span_farm_name">Farm Name : <?php echo $farm_name ?></span>
        </div>

        <div class="mt-1" style="font-size: 15px;">
          <span id="span_records"># of Records : <?php echo count($contacts) ?></span>
          <a href="#" onclick="showContacts()" style="right: 0;position:absolute;margin-right:30px;">View 1st 10 #s</a>
        </div>

        <div class="mt-1" style="font-size: 15px;">
          <span id="span_forwarding_number">Forwarding Number : <?php echo $forwarding_number; ?></span>
        </div>

        <div class="row my-2">
          <div class="col-lg-12 col-md-12 text-center">
            <span class="text-danger" id="credits_info" style="font-size: 12px;"></span>
          </div>
        </div>

        <div class="row mt-3" id="div_recording">
          <div class="col-lg-12 col-md-12">
            <audio id="s_audio_player" style="width: 100%;" controls playsinline></audio>
          </div>
        </div>
        <div class="row mt-1">
          <div class="col-12">
            <div class="form-check mt-2 ms-3">
              <input id="agree_submit" type="checkbox" class="form-check-input" id="horizontal-customCheck">
              <label class="cart-title-desc">You understand that this ringless voicemail though powerful is not perfect.</label>
              <label class="cart-title-desc">You will get 60% to 80% success.</label>
              <label class="cart-title-desc">All should get a missed call which will forward back to you for 2 days.</label>
              <label class="cart-title-desc">You also understand that this process cannot be stopped or canceled once submitted.</label>
              <label class="cart-title-desc">Delivery on most will be within 15 min but some can take hours due to several attempts.</label>
            </div>
          </div>
        </div>

        <div class="row my-3">
          <div class="col-lg-12 col-md-12 text-center">
            <span class="text-danger" style="font-size: 12px;">*All Voicemail will be queued to send only between the hours of 8am and 9pm according to the state the phone is assigned</span>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button id="btn_test_mail" type="button" class="btn btn-success waves-effect btn-label waves-light" onclick="onClickTest()"><i class=" bx bx-mail-send label-icon"></i> Send Test </button>
        <button id="btn_submit_mail" type="button" class="btn btn-primary waves-effect btn-label waves-light" onclick="onClickSubmit()"><i class=" bx bx-mail-send label-icon"></i> Send Now </button>
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" onclick="oncloseModal()">Close</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div>