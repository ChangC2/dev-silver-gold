<div id="add-modal" class="modal fade" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-md">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Add New Recording</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" onclick="oncloseModal()"></button>
      </div>
      <div class="modal-body">
        <div class="row mx-2">
          <div class="col-lg-12 col-md-12 text-center">
            <audio id="c_audio_record" class="w-100" controls playsinline></audio>
          </div>
        </div>

        <div id="c_div_upload" class="row my-3 mx-2">
          <div class="col-8">
            <input class="form-control" id="audio_file" name="audio_file" type="file" accept=".mp3" />
          </div>
          <div class="col-4">
            <button type="button" class="btn btn-info waves-effect btn-label waves-light w-100" onclick="onClickUpload(0)"><i class=" bx bx-cloud-upload label-icon"></i> Upload</button>
          </div>
        </div>

        <div id="c_div_record" class="row mt-2 mx-2">
          <div class="col-lg-12 col-md-12 text-center">
            <button id="c_btn_start" type="button" class="btn btn-success waves-effect w-xl waves-light w-100" onclick="onClickRecord();">Start Recording</button>
          </div>
        </div>

        <div class="row mt-3 mx-2">
          <div class="col-lg-12 col-md-12">
            <input type="text" class="form-control" id="name" placeholder="Input Title" value="" required>
          </div>
        </div>

        <div class="row">
          <div class="col-12">
            <div class="form-check mt-4 ms-3">
              <input id="agree" type="checkbox" class="form-check-input" id="horizontal-customCheck">
              <label class="form-check-label" for="horizontal-customCheck">I confirm that my recording has the following Attributes</label>
            </div>
          </div>
        </div>

        <div class="row">
          <div class="col-12">
            <div class="form-check mt-1 ms-1">
              <ol style="margin-top:0in" type="1" start="1">
                <li style="color:#495057;margin-top:0in;background:white">
                  <span style="font-size:10.5pt;">At beginning of message I state in my message my identity (name and company)</span><u></u><u></u>
                </li>
                <li style="color:#495057;margin-top:0in;background:white">
                  <span style="font-size:10.5pt;">I
                    state the purpose of my call and what I am trying to
                    sell That the purpose of the call is to sell.</span><u></u><u></u>
                </li>
                <li style="color:#495057;margin-top:0in;background:white">
                  <span style="font-size:10.5pt;">I
                    state the nature of the goods or services offered.</span><u></u><u></u>
                </li>
                <li style="color:#495057;margin-top:0in;background:white">
                  <span style="font-size:10.5pt;">I
                    state that no payment or purchase is necessary to win if
                    a prize promotion is offered.</span><u></u><u></u>
                </li>
                <li style="color:#495057;margin-top:0in;background:white">
                  <span style="font-size:10.5pt;">I
                    state that if they do now want to receive more messages
                    to notify you and you will take them off all future
                    lists.</span><u></u><u></u>
                </li>
              </ol>
            </div>
          </div>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" data-bs-dismiss="modal" aria-label="Close" class="btn btn-light waves-effect waves-light"><i class="bx bx-x-circle font-size-16 align-middle me-2"></i>Close</button>
        <button id="c_btn_save" type="button" class="btn btn-primary waves-effect waves-light" onclick="onClickSave();"><i class="bx bx-save font-size-16 align-middle me-2"></i>Save</button>
      </div>
    </div>
  </div>
</div>