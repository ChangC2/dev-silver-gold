<div id="add-modal" class="modal fade" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-md">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Add New Voice</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" onclick="oncloseModal()"></button>
      </div>
      <div class="modal-body">
        <div class="row mt-2 mx-2">
          <div class="col-lg-12 col-md-12">
            <div class="card-text py-2 px-3 border border-info">
              <div>* To clone your voice at its best quality please use a good mic or headset.
                Make sure there is no background noise.
                Talk naturally about anything for 30 seconds.
                We need natural pauses between sentences and don't speak to fast.
                Perhaps tell us a little about yourself and your favorite things.
              </div>
              <div class="card-text">Speak naturally.</div>
            </div>
          </div>
        </div>

        <div class="row mx-2 mt-3">
          <div class="col-lg-12 col-md-12 text-center ">
            <audio id="c_audio_record" class="w-100" controls playsinline></audio>
          </div>
        </div>

        <div id="c_div_upload" class="row mt-2 mx-2">
          <div class="col-8">
            <input class="form-control" id="audio_file" name="audio_file" type="file" accept=".mp3" />
          </div>
          <div class="col-4">
            <button type="button" class="btn btn-info waves-effect btn-label waves-light w-100" onclick="onClickUpload(0)"><i class=" bx bx-cloud-upload label-icon"></i> Upload</button>
          </div>
        </div>

        <div id="c_div_record" class="row mx-2 mt-3">
          <div class="col-lg-12 col-md-12 text-center">
            <button id="c_btn_start" type="button" class="btn btn-success waves-effect w-xl waves-light w-100" onclick="onClickRecord();">Start Recording</button>
          </div>
        </div>

        <div class="row mt-3 mx-2">
          <div class="col-lg-12 col-md-12">
            <input type="text" class="form-control" id="name" placeholder="Input Title" value="" required>
          </div>
        </div>

        <div class="row mt-3 mx-2 <?php echo $permission == 0 ? '' : 'd-none'; ?>">
          <div class="col-lg-12 col-md-12">
            <input type="text" class="form-control" id="voice_id" placeholder="Input Cowboy Key" value="">
          </div>
        </div>

        <div class="row mt-3 mx-2 <?php echo $permission == 0 ? '' : 'd-none'; ?>">
          <div class="col-lg-12 col-md-12">
            <input type="text" class="form-control" id="eden_id" placeholder="Input Eden Key" value="">
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