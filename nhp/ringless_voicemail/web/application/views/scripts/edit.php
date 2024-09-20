<div id="update-modal" class="modal fade" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-md">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Edit Script</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" onclick="oncloseModal()"></button>
      </div>
      <div class="modal-body">

        <div class="row mx-2">
          <div class="col-lg-12 col-md-12">
            <label class="form-label">Title</label>
            <input type="text" class="form-control" id="u_name" placeholder="Input Title" value="" required>
          </div>
        </div>

        <div class="row mx-2 mt-3">
          <div class="col-lg-12 col-md-12">
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
        </div>

        <div class="row mt-3 mx-2">
          <div class="col-lg-12 col-md-12">
            <label class="form-label">Content</label>
            <textarea id="u_content" class="form-control" rows="3" placeholder="Input Content..."></textarea>
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