<script>
  new Choices("#user", {
    shouldSort: !1
  });

  $('#user').change(function(e) {
    var selected_user = $("#user :selected").val();
    window.location.href = '<?php echo base_url('recordings?token=' . urlencode($token)); ?>' + '&selected_user=' + selected_user
  });

  var deviceId = sessionStorage.getItem("input_device");
  var addType = 0; // 0: Use recording, 1: Use uploading
  var audioInputDevices = [];

  var recordingsTbl;

  var selected_recordings_id;
  var selected_recordings_row;
  var selected_table_row;
  var selectedAudio = "";

  //webkitURL is deprecated but nevertheless
  URL = window.URL || window.webkitURL;

  var gumStream; //stream from getUserMedia()
  var rec; //Recorder.js object
  var input; //MediaStreamAudioSourceNode we'll be recording
  var audioBlob;

  // shim for AudioContext when it's not avb.
  var AudioContext = window.AudioContext || window.webkitAudioContext;
  var audioContext; //audio context to help us record
  var permission = <?php echo $permission; ?>;
  var recordings_data = <?php echo json_encode($recordings_data); ?>;
  if (permission == 0) {
    recordingsTbl = $('#recordings-table').DataTable({
      pageLength: 5,
      lengthMenu: [
        [5, 10, 20, 50, 100],
        [5, 10, 20, 50, 100]
      ],
      oLanguage: {
        "sEmptyTable": "You do not have a recording yet.   Please click on the Record or Upload button to add your first Recording"
      },
      autoWidth: false,
      destroy: true,
      data: recordings_data,
      order: [
        [0, 'desc'],
      ],
      columns: [{
        data: 'id'
      }, {
        data: 'name'
      }, {
        data: 'user_name'
      }, {
        data: 'created_at'
      }, {
        data: 'id'
      }],
      columnDefs: [{
        targets: [0],
        createdCell: function(td, cellData, rowData, row, col) {
          $(td).css('width', '0px');
        },
        render: function(data, type, row) {
          return '<i class="bx bxs-microphone" style="color:green;"></i>';
        }
      }, {
        targets: [1],
        orderable: false,
      }, {
        targets: [2],
        orderable: false,
      }, {
        targets: [3],
        orderable: false,
        createdCell: function(td, cellData, rowData, row, col) {
          $(td).css('width', '160px');
        },
        render: function(data, type, row) {
          const dateString = data.substring(0, 10);
          const dateArray = dateString.split("-");
          return dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0] + " " + data.substring(11, 19);
        }
      }, {
        targets: [4],
        orderable: false,
        createdCell: function(td, cellData, rowData, row, col) {
          $(td).css('width', '0px');
        },
        render: function(data, type, row) {
          var mHtml = '';
          mHtml = mHtml + '<a class="btn btn-outline-success waves-effect waves-light btn-sm" title="Play Record" onclick="playRecord(' + data + ')">' +
            ' <i class="fas fa-play"></i></a>'
          mHtml = mHtml + '<a class="btn btn-outline-info waves-effect waves-light btn-sm" title="Edit Record" onclick="onClickEdit(' + data + ')" style="margin-left:5px;">' +
            ' <i class="fas fa-pencil-alt"></i></a>'
          mHtml = mHtml + '<a class="btn btn-outline-danger waves-effect waves-light btn-sm" title="Delete" onclick="onClickDeleteIcon(' + data + ')" style="margin-left:5px;">' +
            ' <i class="far fa-trash-alt"></i></a>'
          return mHtml;
        }
      }]
    });
  } else {
    recordingsTbl = $('#recordings-table').DataTable({
      pageLength: 5,
      lengthMenu: [
        [5, 10, 20, 50, 100],
        [5, 10, 20, 50, 100]
      ],
      oLanguage: {
        "sEmptyTable": "You do not have a recording yet.   Please click on the Record or Upload button to add your first Recording"
      },
      autoWidth: false,
      destroy: true,
      data: recordings_data,
      // order: [
      //   [0, 'desc'],
      // ],
      columns: [{
        data: 'id'
      }, {
        data: 'name'
      }, {
        data: 'created_at'
      }, {
        data: 'id'
      }],
      columnDefs: [{
        targets: [0],
        createdCell: function(td, cellData, rowData, row, col) {
          $(td).css('width', '0px');
        },
        render: function(data, type, row) {
          return '<i class="bx bxs-microphone" style="color:green;"></i>';
        }
      }, {
        targets: [1],
        orderable: false,
      }, {
        targets: [2],
        orderable: false,
        createdCell: function(td, cellData, rowData, row, col) {
          $(td).css('width', '160px');
        },
        render: function(data, type, row) {
          const dateString = data.substring(0, 10);
          const dateArray = dateString.split("-");
          return dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0] + " " + data.substring(11, 19);
        }
      }, {
        targets: [3],
        orderable: false,
        createdCell: function(td, cellData, rowData, row, col) {
          $(td).css('width', '0px');
        },
        render: function(data, type, row) {
          var mHtml = '';
          mHtml = mHtml + '<a class="btn btn-outline-success waves-effect waves-light btn-sm" title="Play Record" onclick="playRecord(' + data + ')">' +
            ' <i class="fas fa-play"></i></a>'
          mHtml = mHtml + '<a class="btn btn-outline-info waves-effect waves-light btn-sm" title="Edit Record" onclick="onClickEdit(' + data + ')" style="margin-left:5px;">' +
            ' <i class="fas fa-pencil-alt"></i></a>'
          mHtml = mHtml + '<a class="btn btn-outline-danger waves-effect waves-light btn-sm" title="Delete" onclick="onClickDeleteIcon(' + data + ')" style="margin-left:5px;">' +
            ' <i class="far fa-trash-alt"></i></a>'
          return mHtml;
        }
      }]
    });
  }


  $("#recordings-table tbody").on('click', 'tr', function() {
    $("#recordings-table tbody tr").removeClass('table-success');
    $(this).addClass('table-success');
  });
</script>


<script>
  var show_add_dlg = function(type) {

    addType = type;
    clear_input();
    if (addType == 0) {
      $("#c_div_upload").hide();
      $("#c_div_record").show();
    } else {
      $("#c_div_upload").show();
      $("#c_div_record").hide();
    }
    $('#add-modal').modal('show');
    $("#agree").prop('checked', false);
    $("#agree").prop('disabled', true);
    $("#c_btn_save").prop('disabled', true);
  }

  var clear_input = function() {
    document.getElementById("c_audio_record").src = "";
    $("#name").val('');
    $("#audio_file").val('');
  }

  var onClickSave = function() {
    var name = $("#name").val().trim();
    if (name === "") {
      show_msg("error", "Enter Name");
      return;
    }
    $("#c_btn_save").prop('disabled', true);
    if (addType == 0) {
      var form = new FormData();
      form.append('audio', audioBlob, "record.wav");
      showLoading();
      $.ajax({
        url: "<?php echo base_url('recordings/audioUpload'); ?>",
        type: 'POST',
        data: form,
        processData: false,
        contentType: false,
        success: function(res) {
          var result = $.parseJSON(res);
          if (result['status'] == true) {
            add("<?php echo base_url('/'); ?>" + result['url'])
          } else {
            show_msg("error", "Fail to add recording");
          }
        },
        error: function() {
          show_msg("error", "Fail to add recording");
        },
        complete: function(file) {
          hideLoading();
        },
      });
    } else {
      add(document.getElementById("c_audio_record").src);
    }
  }

  var add = function(url) {
    $('#add-modal').modal('toggle');
    var name = $("#name").val().trim();

    var postData = {
      token: "<?php echo $token; ?>",
      name: name,
      url: url,
      created_at: '<?php echo date('Y-m-d H:i:s') ?>',
    };
    showLoading();
    $.ajax({
      url: "<?php echo base_url('recordings/add'); ?>",
      type: "POST",
      data: postData
    }).done(function(data) {
      var result = $.parseJSON(data);
      if (result['status'] == true) {
        $("#c_btn_save").prop('disabled', true);
        show_msg("success", "Congrats ! You saved your recording");
        //window.location.href = "<?php echo base_url('recordings?token=') . $token; ?>";
        add_tbl_row(result['id'], postData);
      } else {
        show_msg("error", "Fail to add recording");
      }
    }).fail(function() {
      show_msg("error", "Fail to add recording");
    }).always(function() {
      hideLoading()
    });
  }

  var add_tbl_row = function(id, data) {
    var newRecord = {
      "id": id,
      "name": data['name'],
      "url": data['url'],
      "created_at": data['created_at'],
    };
    recordings_data.push(newRecord);
    recordingsTbl.row.add(newRecord).draw();
  }

  //play record
  var playRecord = function(id) {
    var data = recordings_data.find((order) => order['id'] == id);
    document.getElementById("p_audio_record").src = data['url'];
    //$("#record_title").html(data['name']);
    $('#play-modal').modal('show');
  }

  //edit record
  var onClickEdit = function(id) {
    var data = recordings_data.find((order) => order['id'] == id);
    selected_recordings_row = recordings_data.findIndex((order) => order['id'] == id);
    selected_recordings_id = id;
    $("#u_name").val(data['name']);
    selectedAudio = data['url'];
    document.getElementById("u_audio_record").src = data['url'];
    $('#update-modal').modal('show');
    $("#u_div_upload").hide();
    $("#u_div_record").show();
    $("#u_btn_record").addClass("active");
    $("#u_btn_upload").removeClass("active");
  }

  var onClickEditType = function(type) {
    if (type == 0) {
      $("#u_div_upload").hide();
      $("#u_div_record").show();
      $("#u_btn_record").addClass("active");
      $("#u_btn_upload").removeClass("active");
    } else {
      $("#u_div_upload").show();
      $("#u_div_record").hide();
      $("#u_btn_upload").addClass("active");
      $("#u_btn_record").removeClass("active");
      $("#u_audio_file").val('');
    }
  }

  var onClickUpdate = function() {
    var name = $("#u_name").val().trim();
    if (name === "") {
      show_msg("error", "Enter Name");
      return;
    }
    if (selectedAudio === "") {
      var form = new FormData();
      form.append('audio', audioBlob, "record.wav");
      showLoading();
      $.ajax({
        url: "<?php echo base_url('recordings/audioUpload'); ?>",
        type: 'POST',
        data: form,
        processData: false,
        contentType: false,
        success: function(res) {
          var result = $.parseJSON(res);
          if (result['status'] == true) {
            update("<?php echo base_url('/'); ?>" + result['url'])
          } else {
            show_msg("error", "Fail to add recordings");
          }
        },
        error: function() {
          show_msg("error", "Fail to add recordings");
        },
        complete: function(file) {
          hideLoading();
        },
      });
    } else {
      update(selectedAudio);
    }
  }

  var update = function(url) {
    $('#update-modal').modal('toggle');
    var name = $("#u_name").val().trim();

    var postData = {
      id: selected_recordings_id,
      name: name,
      url: url,
    };
    showLoading();
    $.ajax({
      url: "<?php echo base_url('recordings/update'); ?>",
      type: "POST",
      data: postData
    }).done(function(res) {
      var result = $.parseJSON(res);
      if (result['status'] == true) {
        show_msg("success", "Congrats ! You updated your recording");
        reload_tbl_row(postData);
      } else {
        show_msg("error", "Fail to update recording");
      }
    }).fail(function() {
      show_msg("error", "Fail to update recording");
    }).always(function() {
      hideLoading()
    });
  }

  var onClickDeleteButton = function() {
    deleteRecording();
  };

  var onClickDeleteIcon = function(id) {
    var data = recordings_data.find((record) => record['id'] == id);
    selected_recordings_row = recordings_data.findIndex((record) => record['id'] == id);
    selected_recordings_id = id;
    deleteRecording();
  }

  var deleteRecording = function() {
    var postData = {
      id: selected_recordings_id
    };

    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning",
      showCancelButton: !0,
      confirmButtonColor: "#2ab57d",
      cancelButtonColor: "#fd625e",
      confirmButtonText: "Yes, delete it!"
    }).then(function(e) {
      if (e.value) {
        showLoading();
        $.ajax({
          url: "<?php echo base_url('recordings/delete'); ?>",
          type: "POST",
          data: postData
        }).done(function(data) {
          var result = $.parseJSON(data);
          if (result['status'] == true) {
            Swal.fire("Deleted!", "Recording has been deleted.", "success")
            delete_tbl_row();
          } else {
            Swal.fire("Failed!", result['msg'], "error")
          }
        }).fail(function() {
          Swal.fire("Failed!", "Fail to delete recording.", "error")
        }).always(function() {
          hideLoading();
        });
      }
    });
  }

  //reload table row
  var reload_tbl_row = function(data) {
    recordings_data[selected_recordings_row]["name"] = data['name'];
    recordings_data[selected_recordings_row]["url"] = data['url'];
    var old_data = recordingsTbl.row(selected_recordings_row).data();
    old_data['name'] = data['name'];
    old_data['url'] = data['url'];
    recordingsTbl.row(selected_recordings_row).data(old_data).draw(false);
  }

  //delete table row
  var delete_tbl_row = function(data) {
    const index = recordings_data.findIndex((x) => x.id == selected_recordings_id);
    if (index > -1) { // only splice array when item is found
      recordings_data.splice(index, 1); // 2nd parameter means remove one item only
    }
    recordingsTbl.row(selected_recordings_row).remove().draw();
  }

  function oncloseModal() {
    document.getElementById("p_audio_record").src = "";
    document.getElementById("c_audio_record").src = "";
    document.getElementById("u_audio_record").src = "";
    stopRecording();
  }
</script>

<script type="text/javascript">
  function onClickRecord() {
    if (rec != null && rec != undefined && rec.recording) {
      stopRecording();
    } else {
      startRecording();
    }
  }

  var onClickUpload = function(type) { // 0: Create, 1 : Edit
    var fileSelect = null;
    if (type == 0) {
      fileSelect = document.getElementById("audio_file");
    } else {
      fileSelect = document.getElementById("u_audio_file");
    }
    if (fileSelect.files && fileSelect.files.length == 1) {
      var file = fileSelect.files[0]
      const formData = new FormData();
      formData.set("file", file, file.name);
      showLoading();
      $.ajax({
        url: "<?php echo base_url('recordings/audioUpload'); ?>",
        type: 'POST',
        data: formData,
        success: function(url) {
          if (type == 0) {
            document.getElementById("c_audio_record").src = "<?php echo base_url('/'); ?>" + url;
          } else {
            document.getElementById("u_audio_record").src = "<?php echo base_url('/'); ?>" + url;
            selectedAudio = "<?php echo base_url('/'); ?>" + url;
          }
          $("#agree").prop('disabled', false);
        },
        complete: function(file) {
          hideLoading()
        },
        cache: false,
        contentType: false,
        processData: false
      });
    } else {
      show_msg("error", "Choose file to upload");
    }
  }

  var seconds = 0;
  var intervalId = null;

  function incrementSeconds() {
    seconds += 1;
    $("#c_btn_start").text('Stop Recording (' + seconds + " seconds)");
    $("#u_btn_start").text('Stop Recording (' + seconds + " seconds)");
  }

  function startRecording() {
    if (deviceId == "" || deviceId == null) {
      show_msg("error", "Please choose an audio input device.");
      return;
    }

    var constraints = {
      audio: true,
      video: false
    };

    $("#c_btn_start").removeClass('btn-success');
    $("#c_btn_start").addClass('btn-danger');
    $("#c_btn_start").text('Stop Recording');
    $("#c_btn_save").prop('disabled', true);

    $("#u_btn_start").removeClass('btn-success');
    $("#u_btn_start").addClass('btn-danger');
    $("#u_btn_start").text('Stop Recording');
    $("#u_btn_save").prop('disabled', true);

    $("#agree").prop('disabled', true);

    document.getElementById("u_audio_record").src = "";
    selectedAudio = "";

    navigator.mediaDevices
      .getUserMedia(constraints)
      .then(function(stream) {

        audioContext = new AudioContext();
        gumStream = stream;
        input = audioContext.createMediaStreamSource(stream);
        rec = new Recorder(input, {
          numChannels: 1
        });
        rec.record();
        intervalId = setInterval(incrementSeconds, 1000);
      })
      .catch(function(err) {
        $("#c_btn_start").addClass('btn-success');
        $("#c_btn_start").removeClass('btn-danger');
        $("#c_btn_start").text('Start Recording');
        $("#c_btn_save").prop('disabled', false);

        $("#u_btn_start").addClass('btn-success');
        $("#u_btn_start").removeClass('btn-danger');
        $("#u_btn_start").text('Start Recording');
        $("#u_btn_save").prop('disabled', false);

        $("#agree").prop('disabled', true);
      });
  }

  function stopRecording() {
    $("#c_btn_start").addClass('btn-success');
    $("#c_btn_start").removeClass('btn-danger');
    $("#c_btn_start").text('Start Recording');
    $("#c_btn_save").prop('disabled', true);

    $("#u_btn_start").addClass('btn-success');
    $("#u_btn_start").removeClass('btn-danger');
    $("#u_btn_start").text('Start Recording');

    $("#agree").prop('disabled', false);
    $("#u_btn_save").prop('disabled', false);

    $("#btn_pause").html('Pause');
    $("#u_btn_pause").html('Pause');
    rec.stop();
    gumStream.getAudioTracks()[0].stop();
    rec.exportWAV(createDownloadLink);
    if (intervalId != null) {
      seconds = 0;
      clearInterval(intervalId);
    }
  }

  function createDownloadLink(blob) {
    audioBlob = blob;
    var url = URL.createObjectURL(blob);
    document.getElementById("c_audio_record").src = url;
    document.getElementById("u_audio_record").src = url;
  }
</script>

<script type="text/javascript">
  $(document).ready(function() {
    $("#menu-recordings").addClass('mm-active');
    navigator.mediaDevices.getUserMedia({
        audio: true
      })
      .then(function(stream) {
        if (navigator.mediaDevices && navigator.mediaDevices.enumerateDevices) {
          // Enumerate the available media devices
          navigator.mediaDevices
            .enumerateDevices()
            .then(function(devices) {
              // Filter only the audio input devices

              audioInputDevices = devices.filter(
                (device) => device.kind === "audioinput"
              );

              // Create the HTML select element for the microphone selection
              const selectElement = document.getElementById("input_device");

              // Populate the select element with the available microphones

              audioInputDevices.forEach(function(device) {
                const optionElement = document.createElement("option");
                if (device.deviceId !== "default" && device.deviceId !== "communications") {
                  optionElement.value = device.deviceId;
                  optionElement.text =
                    device.label || "Microphone " + (selectElement.length + 1);
                  if (device.deviceId == deviceId) {
                    optionElement.selected = "selected";
                  }
                  if (device.label) {
                    selectElement.appendChild(optionElement);
                  }
                }
              });
            })
            .catch(function(error) {
              console.error("Error enumerating devices:", error);
            });
        }
      })
      .catch(function(err) {});

    $("#agree").prop('disabled', true);
  });

  $('#input_device').change(function(e) {
    deviceId = $("#input_device :selected").val();
    sessionStorage.setItem("input_device", deviceId);
  });

  $('#agree').change(function(e) {
    if ($('#agree').is(":checked")) {
      $("#c_btn_save").prop('disabled', false);
    } else {
      $("#c_btn_save").prop('disabled', true);
    }
  });

  $("div#file-upload-area").dropzone({
    name: "file",
    maxFilesize: 100,
    acceptedFiles: ".mp3",
    maxFiles: 1,
    addRemoveLinks: true,
  });
</script>