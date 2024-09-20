<script type="text/javascript">
  var signaturePad = null;

  var onTerms = function() {
    signaturePad = new SignaturePad(document.getElementById('signature-pad'), {
      backgroundColor: 'rgba(255, 255, 255, 0)',
      penColor: 'rgb(0, 0, 0)'
    });

    signaturePad.addEventListener("endStroke", () => {
      if ($('#txt_accept').val().toLowerCase() == "i accept" && !signaturePad.isEmpty()) {
        $("#btn_accept").prop('disabled', false);
      }
    }, {
      once: true
    });

    $('#chk_agree1').change(function(e) {
      if ($('#chk_agree1').is(":checked")) {
        $("#btn_show_part2").prop('disabled', false);
      } else {
        $("#btn_show_part2").prop('disabled', true);
      }
    });

    $('#chk_agree2').change(function(e) {
      if ($('#chk_agree2').is(":checked")) {
        $("#btn_show_part3").prop('disabled', false);
      } else {
        $("#btn_show_part3").prop('disabled', true);
      }
    });

    $('#chk_agree3').change(function(e) {
      if ($('#chk_agree3').is(":checked")) {
        $("#btn_show_sign").prop('disabled', false);
      } else {
        $("#btn_show_sign").prop('disabled', true);
      }
    });

    $("#txt_accept").change(function() {
      if ($('#txt_accept').val().toLowerCase() === "i accept" && !signaturePad.isEmpty()) {
        $("#btn_accept").prop('disabled', false);
      }
    });

    $('#header-modal').modal('show');
    //$('#sign-modal').modal('show');
    $('#part1-modal').modal('hide');
    $('#txt_accept').val("");
    onClear();
  }

  var showPart1 = function() {
    $('#header-modal').modal('hide');
    $('#part1-modal').modal('show');
    $('#part2-modal').modal('hide');

    if ($('#chk_agree1').is(":checked")) {
      $("#btn_show_part2").prop('disabled', false);
    } else {
      $("#btn_show_part2").prop('disabled', true);
    }
  }

  var showPart2 = function() {
    $('#part1-modal').modal('hide');
    $('#part2-modal').modal('show');
    $('#part3-modal').modal('hide');

    if ($('#chk_agree2').is(":checked")) {
      $("#btn_show_part3").prop('disabled', false);
    } else {
      $("#btn_show_part3").prop('disabled', true);
    }
  }

  var showPart3 = function() {
    $('#part2-modal').modal('hide');
    $('#part3-modal').modal('show');
    $('#sign-modal').modal('hide');

    if ($('#chk_agree3').is(":checked")) {
      $("#btn_show_sign").prop('disabled', false);
    } else {
      $("#btn_show_sign").prop('disabled', true);
    }
  }

  var showSign = function() {
    $('#part3-modal').modal('hide');
    $('#sign-modal').modal('show');
  }

  var onDecline = function() {
    $('#sign-modal').modal('hide');
    window.location.href = "<?php echo base_url('auth/logout'); ?>";
  }

  var onClear = function() {
    $("#btn_accept").prop('disabled', true);
    signaturePad.clear();
  }
</script>