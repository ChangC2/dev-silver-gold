<div id="update-modal" class="modal fade" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-md">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Edit Recording</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" onclick="oncloseModal()"></button>
      </div>
      <div class="modal-body">
        <div class="card border">
          <div class="card-header bg-transparent ">
            <div class="row">
              <div class="col-12">
                <div class="d-inline">
                  <div class="d-inline" onclick="onClickEditType(0)">
                    <input class="form-check-input ms-2" style="margin-top: 5px;" type="radio" name="formRadios" id="radioRecording" checked>
                    <label class="form-check-label ms-2" style="font-size: 17px;" for="radioRecording">
                      Record
                    </label>
                  </div>
                  <div class="d-inline" style="margin-left: 40px;" onclick="onClickEditType(1)">
                    <input class="form-check-input" style="margin-top: 5px;" type="radio" name="formRadios" id="radioVoice">
                    <label class="form-check-label ms-2" style="font-size: 17px;" for="radioVoice">
                      Upload
                    </label>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="card-body">

            <div class="row mx-2">
              <div class="col-lg-12 col-md-12 text-center">
                <audio id="u_audio_record" class="w-100" controls playsinline></audio>
              </div>
            </div>

            <div id="u_div_upload" class="row mt-2 mx-2">
              <div class="col-8">
                <input class="form-control" id="u_audio_file" name="u_audio_file" type="file" accept=".mp3" />
              </div>
              <div class="col-4">
                <button type="button" class="btn btn-info waves-effect btn-label waves-light w-100" onclick="onClickUpload(1)"><i class=" bx bx-cloud-upload label-icon"></i> Upload</button>
              </div>
            </div>

            <div id="u_div_record" class="row mt-2 mx-2">
              <div class="col-lg-12 col-md-12 text-center">
                <button id="u_btn_start" type="button" class="btn btn-success waves-effect w-xl waves-light w-100" onclick="onClickRecord();">Start Recording</button>
              </div>
            </div>

            <div class="row mt-3 mx-2">
              <div class="col-lg-12 col-md-12">
                <input type="text" class="form-control" id="u_name" placeholder="Input Title" value="" required>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" data-bs-dismiss="modal" aria-label="Close" class="btn btn-light waves-effect waves-light"><i class="bx bx-x-circle font-size-16 align-middle me-2"></i>Close</button>
          <button id="u_btn_save" type="button" class="btn btn-primary waves-effect waves-light" onclick="onClickUpdate();"><i class="bx bx-save font-size-16 align-middle me-2"></i>Save</button>
          <button type="button" class="btn btn-danger waves-effect waves-light" onclick="onClickDeleteButton();"><i class="bx bx-trash font-size-16 align-middle me-2"></i>Delete</button>
        </div>
      </div>
    </div>
  </div>
</div>