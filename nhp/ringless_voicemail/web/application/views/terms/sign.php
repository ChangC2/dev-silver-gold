<div id="sign-modal" class="modal fade bs-example-modal-lg" data-bs-backdrop="static" tabindex="-1" aria-labelledby="myLargeModalLabel" style="display: none;" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="text-center mt-4 border-bottom pb-3">
        <h5 class="modal-title">Signature</h5>
      </div>

      <div class="modal-body">
        <p>I agree with Parts I, II and III of these Terms and Conditions. By typing "I ACCEPT" below I am confirming that I have read, understand and agree to these Terms and Conditions.</p>
        <div class="flex-row">
          <input type="text" style="width: 200px;display:inline;" class="form-control me-3" id="txt_accept" name="txt_accept" placeholder="I Accept" value="I Accept" required>
          Type <span class="text-primary">I ACCEPT</span> here if you agree with these Terms and Conditions.
        </div>
      </div>

      <div class="row mx-2">
        <div class="col auto mt-2">Sign Here</div>
        <div class="col text-end"><button type="button" class="btn btn-link waves-effect waves-light" onclick="onClear();">Clear</button></div>
      </div>
      <div class="sign-wrapper text-center bg-secondary mx-3 pt-2">
        <canvas id="signature-pad" class="signature-pad bg-light" width="400" height="200"></canvas>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-light waves-effect waves-light" onclick="showPart3();">Previous</button>
        <button type="button" id="btn_accept" class="btn btn-success waves-effect waves-light" onclick="onAccept();">Accept the Terms and Conditions</button>
        <button type="button" class="btn btn-danger waves-effect waves-light" onclick="onDecline();">Decline the Terms and Conditions</button>
      </div>
    </div>
  </div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->
</div>