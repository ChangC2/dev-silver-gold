<div id="update-modal" class="modal fade" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-md">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Edit Voice</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" onclick="oncloseModal()"></button>
      </div>
      <div class="modal-body">
        <div class="row">
          <div class="col-lg-12 col-md-12">
            <input type="text" class="form-control" id="u_name" placeholder="Input Title" value="" required>
          </div>
        </div>

        <div class="row mt-3 <?php echo $permission == 0 ? '' : 'd-none'; ?>">
          <div class="col-lg-12 col-md-12">
            <input type="text" class="form-control" id="u_voice_id" placeholder="Input Cowboy Key" value="">
          </div>
        </div>

        <div class="row mt-3 <?php echo $permission == 0 ? '' : 'd-none'; ?>">
          <div class="col-lg-12 col-md-12">
            <input type="text" class="form-control" id="u_eden_id" placeholder="Input Eden Key" value="">
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