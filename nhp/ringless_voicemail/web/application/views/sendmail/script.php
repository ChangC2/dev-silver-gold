<script>
  var voicemailType = 0; // 0 : pre-recorded voice, 1 : personalized voice 
  var selectedRecording = null;
  var selectedVoice = null;
  var scheduleType = 0; // 0 : send immediately, 1 : send on scheduled time 
  var sendDate = "";
  var sendTime = "";


  var recordings = <?php echo json_encode($recordings); ?>;
  var pro_voices = <?php echo json_encode($pro_voices); ?>;
  var user_voices = <?php echo json_encode($user_voices); ?>;
  var scripts = <?php echo json_encode($scripts); ?>;

  var recordingChoice = new Choices("#recording", {
    shouldSort: !1
  });
  var voiceChoice = new Choices("#voice", {
    shouldSort: !1
  });
  var scriptChoice = new Choices("#script", {
    shouldSort: !1
  });

  var uSalutationChoice = new Choices("#u_salutation", {
    shouldSort: !1
  });

  var onClickAudioSource = function(type) {
    voicemailType = type;
    document.getElementById("audio_player").src = "";

    if (voicemailType == 0) {
      $("#div_recording").removeClass("d-none");
      $("#div_voice").addClass("d-none");
      $("#div_content").addClass("d-none");
      recordingChoice.setChoiceByValue("");
    } else {
      $("#div_recording").addClass("d-none");
      $("#div_voice").removeClass("d-none");
      $("#div_content").removeClass("d-none");
      uSalutationChoice.setChoiceByValue("7");
      scriptChoice.setChoiceByValue("");
      voiceChoice.setChoiceByValue("");
      $("#content").val("");
    }
  }

  var onClickSchedule = function(type) {
    scheduleType = $('#chk_break_up').is(":checked") ? 1 : type;
    if (scheduleType == 0) {
      $("#radioImmediately").prop("checked", true);
      $("#radioAdvancedTime").prop("checked", false);
      $("#div_send_time").addClass("d-none");
    } else {
      $("#radioImmediately").prop("checked", false);
      $("#radioAdvancedTime").prop("checked", true);
      $("#div_send_time").removeClass("d-none");
      $("#date_input").val("<?php echo date('Y-m-d'); ?>");
      $("#time_input").val("08:00");
    }
  }

  $('#recording').change(function(e) {
    var selected_recording_id = $("#recording :selected").val();
    if (selected_recording_id != "") {
      selectedRecording = recordings.find((r) => r['id'] == selected_recording_id);
      document.getElementById("audio_player").src = selectedRecording['url'];
    } else {
      selectedRecording = null;
      document.getElementById("audio_player").src = "";
    }
  });

  $('#voice').change(function(e) {
    const voices = pro_voices.concat(user_voices);
    var selected_voice_id = $("#voice :selected").val();
    if (selected_voice_id != "") {
      selectedVoice = voices.find((v) => v['id'] == selected_voice_id);
      document.getElementById("audio_player").src = selectedVoice['url'];
    } else {
      selectedVoice = null;
      document.getElementById("audio_player").src = "";
    }
  });

  $('#script').change(function(e) {
    var selected_script_id = $("#script :selected").val();
    if (selected_script_id == "") {
      $("#content").val("")
    } else {
      var data = scripts.find((script) => script['id'] == selected_script_id);
      $("#content").val(data["salutation"] == "" ? data["content"] : data["salutation"] + ".\n" + data["content"])
      var salutations = [
        "First Name",
        "Hello First Name",
        "Hey First Name",
        "Hope all is well First Name",
        "Good Afternoon First Name",
        "Good Morning First Name",
        "Good Evening First Name",
        "- No salutation -",
      ];
      var sVal = data['salutation'] == "" ? "7" : salutations.findIndex((salutation) => salutation == data['salutation']);
      uSalutationChoice.setChoiceByValue(sVal.toString());
    }
  });

  $('#u_salutation').change(function(e) {
    var selected_script_id = $("#script :selected").val();
    if (selected_script_id == "") {
      $("#u_salutation :selected").val() == "7" ? $("#content").val("") : $("#content").val($("#u_salutation :selected").text())
    } else {
      var data = scripts.find((script) => script['id'] == selected_script_id);
      $("#content").val($("#u_salutation :selected").val() == "7" ? data["content"] : $("#u_salutation :selected").text() + ".\n" + data["content"])
    }
  });

  $('#time_input').change(function(e) {
    var timeStr = $('#time_input').val();
    if (timeStr < "08:00" || timeStr > "17:00") {
      show_msg("error", "We only accept from 8am to 5pm");
      $('#time_input').val("08:00")
    }
  });

  $('#chk_break_up').change(function(e) {
    if ($('#chk_break_up').is(":checked")) {
      $("#div_break_up").removeClass("d-none");
      onClickSchedule(1);
      setAttempts();
    } else {
      $("#div_break_up").addClass("d-none");
    }
  });

  $('#broadcasts_number').change(function(e) {
    setAttempts();
  });

  var setAttempts = function() {
    let broadcastsNumber = $("#broadcasts_number").val();
    let contactsCount = parseInt("<?php echo $contact_count; ?>");
    var recordCount = parseInt((contactsCount - 1) / broadcastsNumber) + 1;
    $("#records_per_day").html('= ' + recordCount + ' Attempts per day');
  }
</script>


<script>
  var onClickSend = function() {
    var contactCount = <?php echo $contact_count ?>;
    if (contactCount == 0) {
      show_msg("error", "No Contact to send");
      return;
    }
    if (voicemailType == 0) {
      sendPreRecordedVoicemail();
    } else {
      sendPersonalizedVoicemail();
    }
  }

  var sendPreRecordedVoicemail = function() {
    if (selectedRecording == null) {
      show_msg("error", "Please select a recording.");
      return;
    }
    var forwarding_number = $("#forwarding_number").val();
    var re_forwarding_number = $("#re_forwarding_number").val();
    if (forwarding_number == "") {
      show_msg("error", "Please input a forwarding number.");
      return;
    }
    if (forwarding_number != re_forwarding_number) {
      show_msg("error", "Forwarding number mismatch.");
      return;
    }
    document.getElementById("s_audio_player").src = selectedRecording["url"];
    $("#span_forwarding_number").html("Forwarding Number : " + forwarding_number);
    $("#credits_info").html("*" + <?php echo $credits ?> + " Credits Availalbe.You need " + <?php echo $contact_count / 10 ?> + " credits to send this message.");
    $("#agree_submit").prop('checked', false);
    $("#btn_submit_mail").prop('disabled', true);
    $("#div_voice_content").addClass("d-none");
    $('#submit-modal').modal('show');
  }

  var sendPersonalizedVoicemail = function() {
    if (selectedVoice == null) {
      show_msg("error", "Please select a voice.");
      return;
    }
    var forwarding_number = $("#forwarding_number").val();
    var re_forwarding_number = $("#re_forwarding_number").val();
    if (forwarding_number == "") {
      show_msg("error", "Please input a forwarding number.");
      return;
    }
    if (forwarding_number != re_forwarding_number) {
      show_msg("error", "Forwarding number mismatch.");
      return;
    }
    var content = $("#content").val();
    if (content == "") {
      show_msg("error", "Please input a message.");
      return;
    }
    var contacts = <?php echo json_encode($contacts); ?>;
    $("#span_forwarding_number").html("Forwarding Number : " + forwarding_number);
    $("#credits_info").html("*" + <?php echo $credits ?> + " Credits Availalbe.You need " + <?php echo $contact_count / 10 ?> + " credits to send this message.");
    $("#agree_submit").prop('checked', false);
    $("#btn_submit_mail").prop('disabled', true);
    $("#div_voice_content").removeClass("d-none");
    $('#submit-modal').modal('show');
    apiCallForEden(selectedVoice.eden_id, content.replace("First Name", contacts[0].name))
  }

  var apiCallForEden = function(voide_model, content) {
    const options = {
      method: 'POST',
      headers: {
        accept: 'application/json',
        'content-type': 'application/json',
        authorization: 'Bearer <?php echo getenv('EDEN_KEY'); ?>'
      },
      body: JSON.stringify({
        settings: '{"microsoft": "' + voide_model + '" }',
        providers: 'microsoft',
        text: content.replace(/[()-]/g, ' ').replace('&', ' '),
        language: 'en'
      })
    };
    showLoading();
    fetch('https://api.edenai.run/v2/audio/text_to_speech', options)
      .then(response => response.json())
      .then(response => {
        document.getElementById("s_audio_player").src = response['microsoft']['audio_resource_url'];
        hideLoading();
      })
      .catch(err => console.error(err));
  }

  var onClickSubmit = function() {
    var postData;
    if (voicemailType == 0) {
      var forwarding_number = $("#forwarding_number").val().replace(/[()-]/g, '').replace(' ', '');
      postData = {
        token: "<?php echo $token; ?>",
        type: voicemailType,
        audioUrl: selectedRecording["url"],
        forwarding_number: forwarding_number,
        record_name: selectedRecording["name"],
        farm_name: '<?php echo $farm_name ?>',
        content: '',
        voice_id: '',
        send_time: scheduleType == 0 ? "" : $("#date_input").val() + " " + $("#time_input").val() + ":00",
        break_up: $('#chk_break_up').is(":checked"),
        broadcasts_number: $('#broadcasts_number').val(),
        between_days: $('#between_days').val(),
      };
    } else {
      var forwarding_number = $("#forwarding_number").val().replace(/[()-]/g, '').replace(' ', '');
      var content = $("#content").val();
      postData = {
        token: "<?php echo $token; ?>",
        type: voicemailType,
        audioUrl: selectedVoice["url"],
        forwarding_number: forwarding_number,
        record_name: selectedVoice["name"],
        farm_name: '<?php echo $farm_name ?>',
        content: content,
        voice_id: selectedVoice["voice_id"],
        send_time: scheduleType == 0 ? "" : $("#date_input").val() + " " + $("#time_input").val() + ":00",
        break_up: $('#chk_break_up').is(":checked"),
        broadcasts_number: $('#broadcasts_number').val(),
        between_days: $('#between_days').val(),
      };
    }
    showLoading();
    $.ajax({
      url: "<?php echo base_url('sendmail/checkExistInOrder'); ?>",
      type: "POST",
      data: postData
    }).done(function(res) {
      if (res == true) {
        Swal.fire({
          title: "Are you sure?",
          text: "You recently sent a message to this same group within last 24 hours. Please confirm you want to do this by clicking Yes or clicking Cancel.",
          icon: "warning",
          showCancelButton: !0,
          confirmButtonColor: "#2ab57d",
          cancelButtonColor: "#fd625e",
          confirmButtonText: "Yes"
        }).then(function(e) {
          if (e.value) {
            apiCallForSaveMailLog(postData);
          }
        });
      } else {
        apiCallForSaveMailLog(postData);
      }
    }).fail(function() {}).always(function() {
      hideLoading();
    });
  }

  var apiCallForSaveMailLog = function(postData) {
    showLoading();
    $.ajax({
      url: "<?php echo base_url('sendmail/save_mail_log'); ?>",
      type: "POST",
      data: postData
    }).done(function(res) {
      var result = $.parseJSON(res);
      if (result['status'] == true) {
        ids = result['ids'];
        show_msg("success", "Your Voice messages are now in Queue.  You can go to Past Ringless Voicemails on Left menu to check Status.   You will get an email when they are all sent.");
      } else {
        Swal.fire({
          title: "",
          text: result['message'],
          icon: "warning",
          confirmButtonColor: "#5156be",
        });
      }
    }).fail(function() {}).always(function() {
      hideLoading();
      $('#submit-modal').modal('hide');
    });
  }

  var onClickTest = function() {
    Swal.fire({
      title: "Are you sure?",
      text: "The test voicemail will be sent to the phone on account!",
      icon: "warning",
      showCancelButton: !0,
      confirmButtonColor: "#2ab57d",
      cancelButtonColor: "#fd625e",
      confirmButtonText: "Yes, Send Now!"
    }).then(function(e) {
      if (e.value) {
        apiCallForSaveTestMailLog();
      }
    });
    //$('#submit-modal').modal('hide');
  }

  var apiCallForSaveTestMailLog = function() {
    var postData;
    if (voicemailType == 0) {
      var forwarding_number = $("#forwarding_number").val().replace(/[()-]/g, '').replace(' ', '');
      postData = {
        token: "<?php echo $token; ?>",
        type: voicemailType,
        audioUrl: selectedRecording["url"],
        forwarding_number: forwarding_number,
        record_name: selectedRecording["name"],
        farm_name: '<?php echo $farm_name ?>',
        content: '',
        voice_id: '',
      };
    } else {
      var forwarding_number = $("#forwarding_number").val().replace(/[()-]/g, '').replace(' ', '');
      var content = $("#content").val();
      postData = {
        token: "<?php echo $token; ?>",
        type: voicemailType,
        audioUrl: selectedVoice["url"],
        forwarding_number: forwarding_number,
        record_name: selectedVoice["name"],
        farm_name: '<?php echo $farm_name ?>',
        content: content,
        voice_id: selectedVoice["voice_id"],
      };
    }
    showLoading();
    $.ajax({
      url: "<?php echo base_url('sendmail/save_test_mail_log'); ?>",
      type: "POST",
      data: postData
    }).done(function(res) {
      var result = $.parseJSON(res);
      if (result['status'] == true) {
        ids = result['ids'];
        show_msg("success", "Your test message is now in Queue.  You can go to Past Ringless Voicemails on Left menu to check Status. You will get an email when it is sent.");
      } else {
        Swal.fire({
          title: "",
          text: result['message'],
          icon: "warning",
          confirmButtonColor: "#5156be",
        });
      }
    }).fail(function() {}).always(function() {
      hideLoading();
    });
  }
</script>

<script type="text/javascript">
  $(document).ready(function() {
    $("#menu-dashboard").addClass('mm-active');
    //$('#edit_forwarding_number').usPhoneFormat();
    var phones = [{
      "mask": "(###) ###-####"
    }];

    $('#forwarding_number').inputmask({
      mask: phones,
      greedy: false,
      definitions: {
        '#': {
          validator: "[0-9]",
          cardinality: 1
        }
      }
    });

    $('#re_forwarding_number').inputmask({
      mask: phones,
      greedy: false,
      definitions: {
        '#': {
          validator: "[0-9]",
          cardinality: 1
        }
      }
    });


    var signed = <?php echo $signed; ?>;
    if (signed == 0) {
      onTerms();
    }
    $("#agree").prop('disabled', true);
  });


  $('#agree').change(function(e) {
    if ($('#agree').is(":checked")) {
      $("#btn_save").prop('disabled', false);
    } else {
      $("#btn_save").prop('disabled', true);
    }
  });

  $('#agree_submit').change(function(e) {
    if ($('#agree_submit').is(":checked")) {
      $("#btn_submit_mail").prop('disabled', false);
    } else {
      $("#btn_submit_mail").prop('disabled', true);
    }
  });


  var onAccept = function() {
    $('#sign-modal').modal('hide');
    var data = signaturePad.toDataURL('image/jpeg');

    var form = new FormData();
    form.append('data', data);
    form.append('token', "<?php echo $token; ?>");
    showLoading();
    $.ajax({
      url: "<?php echo base_url('auth/signature'); ?>",
      type: 'POST',
      data: form,
      processData: false,
      contentType: false,
      success: function(res) {
        var result = $.parseJSON(res);
        if (result['status'] == true) {
          $('#sign-modal').modal('hide');
        }
      },
      error: function() {},
      complete: function() {
        hideLoading();
      }
    });
  }

  var showContacts = function() {
    var contacts_data = <?php echo json_encode($contacts); ?>;
    contactsTbl = $('#contacts-table').DataTable({
      pageLength: 10,
      lengthMenu: [
        [5, 10, 20, 50, 100],
        [5, 10, 20, 50, 100]
      ],
      autoWidth: false,
      destroy: true,
      data: contacts_data,
      lengthChange: false,
      searching: false,
      paging: false,
      info: false,
      columns: [{
        data: 'name'
      }, {
        data: 'phone'
      }],
      columnDefs: [{
        targets: [1],
        createdCell: function(td, cellData, rowData, row, col) {
          $(td).css('width', '150px');
        },
        render: function(data, type, row) {
          return formatPhoneNumber(data);
        }
      }]
    });
    $('#contacts-modal').modal('show');
  }

  function oncloseModal() {
    document.getElementById("s_audio_player").src = "";
  }
</script>