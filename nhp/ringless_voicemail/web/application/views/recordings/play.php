<div id="play-modal" class="modal fade" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-md">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Play Recording</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" onclick="oncloseModal()"></button>
      </div>
      <div class="modal-body">
        <div class="row mx-2">
          <div class="col-lg-12 col-md-12 text-center">
            <audio id="p_audio_record" class="w-100" controls playsinline></audio>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" data-bs-dismiss="modal" onclick="oncloseModal()" aria-label="Close" class="btn btn-light waves-effect waves-light"><i class="bx bx-x-circle font-size-16 align-middle me-2"></i>Close</button>
      </div>
    </div>
  </div>
</div>