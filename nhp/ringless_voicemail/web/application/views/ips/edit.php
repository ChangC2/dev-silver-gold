<div id="update-ip_modal" class="modal fade" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-xl">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Edit IP Address</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <div class="row">
          <div class="col-12">
            <div class="mb-3">
              <label class="form-label">IP Address</label>
              <input type="text" class="form-control" id="u_ip_address" placeholder="Input IP Address" value="" required>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" data-bs-dismiss="modal" aria-label="Close" class="btn btn-light waves-effect waves-light"><i class="bx bx-x-circle font-size-16 align-middle me-2"></i>Close</button>
        <button type="button" class="btn btn-primary waves-effect waves-light" onclick="updateIP();"><i class="bx bx-save font-size-16 align-middle me-2"></i>Save</button>
      </div>
    </div>
  </div>
</div>