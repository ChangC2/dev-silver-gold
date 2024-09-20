<script>
  new Choices("#user", {
    shouldSort: !1
  });

  var pageTitle = "<?php echo $page_title; ?>";
  var ordersTbl;

  var selected_orders_id;
  var selected_orders_row;
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

  var orders_data = <?php echo json_encode($orders_data); ?>;
  var contacts_data;

  var permission = <?php echo $permission; ?>;
  if (permission == 0) {
    if (pageTitle == "Pending Orders") {
      ordersTbl = $('#orders-table').DataTable({
        autoWidth: false,
        destroy: true,
        searching: false,
        pageLength: 5,
        lengthMenu: [
          [5, 10, 20, 50, 100],
          [5, 10, 20, 50, 100]
        ],
        lengthChange: false,
        data: orders_data,
        order: [
          [0, 'desc'],
        ],
        columns: [{
          data: 'id'
        }, {
          data: 'created_at'
        }, {
          data: 'order_no'
        }, {
          data: 'farm_name'
        }, {
          data: 'record_count'
        }, {
          data: 'record_count'
        }, {
          data: 'type'
        }, {
          data: 'user_name'
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
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '145px');
          },
          render: function(data, type, row) {
            const dateString = data.substring(0, 10);
            const dateArray = dateString.split("-");
            return dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0] + " " + data.substring(11, 16);
          }
        }, {
          targets: [2],
          orderable: false,
          render: function(data, type, row) {
            return "<font color='#1285f5'>" + data + "</font>";
          }
        }, {
          targets: [3],
          orderable: false,
        }, {
          targets: [4],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '50px');
          }
        }, {
          targets: [5],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            return (data * 0.1).toFixed(1);
          }
        }, {
          targets: [6],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            return data == 0 ? "<span style='font-size:12px;' class='text-success'>Pre-Recorded Audio</span>" : "<span style='font-size:12px;' class='text-success'>AIValet Voice</span>";
          }
        }, {
          targets: [7],
          orderable: false,
        }, {
          targets: [8],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            var mHtml = '';
            mHtml = mHtml + '<button type="button" class="btn btn-outline-primary waves-effect waves-light btn-sm" onclick="showContent(' + data + ')">View Message</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-success waves-effect waves-light btn-sm ms-1" onclick="viewVoicemail(' + data + ')">View 1st 10 #s</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-danger waves-effect waves-light btn-sm ms-1" onclick="onClickDeleteIcon(' + data + ')">Cancel Order</button>'
            return mHtml;
          }
        }]
      });
    } else if (pageTitle == "Scheduled Orders") {
      ordersTbl = $('#orders-table').DataTable({
        autoWidth: false,
        destroy: true,
        searching: false,
        pageLength: 5,
        lengthMenu: [
          [5, 10, 20, 50, 100],
          [5, 10, 20, 50, 100]
        ],
        lengthChange: false,
        data: orders_data,
        order: [
          [0, 'desc'],
        ],
        columns: [{
          data: 'id'
        }, {
          data: 'send_time'
        }, {
          data: 'order_no'
        }, {
          data: 'farm_name'
        }, {
          data: 'record_count'
        }, {
          data: 'record_count'
        }, {
          data: 'type'
        }, {
          data: 'user_name'
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
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '145px');
          },
          render: function(data, type, row) {
            const dateString = data.substring(0, 10);
            const dateArray = dateString.split("-");
            return dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0] + " " + data.substring(11, 16);
          }
        }, {
          targets: [2],
          orderable: false,
          render: function(data, type, row) {
            return "<font color='#1285f5'>" + data + "</font>";
          }
        }, {
          targets: [3],
          orderable: false,
        }, {
          targets: [4],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '50px');
          }
        }, {
          targets: [5],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            return (data * 0.1).toFixed(1);
          }
        }, {
          targets: [6],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            return data == 0 ? "<span style='font-size:12px;' class='text-success'>Pre-Recorded Audio</span>" : "<span style='font-size:12px;' class='text-success'>AIValet Voice</span>";
          }
        }, {
          targets: [7],
          orderable: false,
        }, {
          targets: [8],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            var mHtml = '';
            mHtml = mHtml + '<button type="button" class="btn btn-outline-primary waves-effect waves-light btn-sm" onclick="showContent(' + data + ')">View Message</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-success waves-effect waves-light btn-sm ms-1" onclick="viewVoicemail(' + data + ')">View 1st 10 #s</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-danger waves-effect waves-light btn-sm ms-1" onclick="onClickDeleteIcon(' + data + ')">Cancel Order</button>'
            return mHtml;
          }
        }]
      });
    } else {
      ordersTbl = $('#orders-table').DataTable({
        autoWidth: false,
        destroy: true,
        searching: false,
        pageLength: 5,
        lengthMenu: [
          [5, 10, 20, 50, 100],
          [5, 10, 20, 50, 100]
        ],
        lengthChange: false,
        data: orders_data,
        order: [
          [0, 'desc'],
        ],
        columns: [{
          data: 'id'
        }, {
          data: 'created_at'
        }, {
          data: 'order_no'
        }, {
          data: 'farm_name'
        }, {
          data: 'record_count'
        }, {
          data: 'queue'
        }, {
          data: 'success'
        }, {
          data: 'fail'
        }, {
          data: 'success'
        }, {
          data: 'type'
        }, {
          data: 'user_name'
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
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '145px');
          },
          render: function(data, type, row) {
            const dateString = data.substring(0, 10);
            const dateArray = dateString.split("-");
            return dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0] + " " + data.substring(11, 16);
          }
        }, {
          targets: [2],
          orderable: false,
          render: function(data, type, row) {
            return "<font color='#1285f5'>" + data + "</font>";
          }
        }, {
          targets: [3],
          orderable: false,
        }, {
          targets: [4],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '50px');
          }
        }, {
          targets: [5],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '50px');
          }
        }, {
          targets: [6],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '50px');
          },
          render: function(data, type, row) {
            return "<span class='text-success'>" + data + "</span>";
          }
        }, {
          targets: [7],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '40px');
          },
          render: function(data, type, row) {
            return "<span class='text-danger'>" + data + "</span>";
          }
        }, {
          targets: [8],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            return (data * 0.1).toFixed(1);
          }
        }, {
          targets: [9],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            return data == 0 ? "<span style='font-size:12px;' class='text-success'>Pre-Recorded Audio</span>" : "<span style='font-size:12px;' class='text-success'>AIValet Voice</span>";
          }
        }, {
          targets: [10],
          orderable: false,
        }, {
          targets: [11],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            var mHtml = '';
            mHtml = mHtml + '<button type="button" class="btn btn-outline-primary waves-effect waves-light btn-sm" onclick="showContent(' + data + ')">View Message</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-success waves-effect waves-light btn-sm ms-1" onclick="viewVoicemail(' + data + ')">View 1st 10 #s</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-warning waves-effect waves-light btn-sm ms-1" onclick="onClickResendAll(' + data + ')">Resend All</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-info waves-effect waves-light btn-sm ms-1" onclick="onClickResendFailed(' + data + ')">Resend Failed</button>'
            return mHtml;
          }
        }]
      });
    }
  } else {
    if (pageTitle == "Pending Orders") {
      ordersTbl = $('#orders-table').DataTable({
        autoWidth: false,
        destroy: true,
        searching: false,
        pageLength: 5,
        lengthMenu: [
          [5, 10, 20, 50, 100],
          [5, 10, 20, 50, 100]
        ],
        lengthChange: false,
        data: orders_data,
        order: [
          [0, 'desc'],
        ],
        columns: [{
          data: 'id'
        }, {
          data: 'created_at'
        }, {
          data: 'order_no'
        }, {
          data: 'farm_name'
        }, {
          data: 'record_count'
        }, {
          data: 'record_count'
        }, {
          data: 'type'
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
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '145px');
          },
          render: function(data, type, row) {
            const dateString = data.substring(0, 10);
            const dateArray = dateString.split("-");
            return dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0] + " " + data.substring(11, 16);
          }
        }, {
          targets: [2],
          orderable: false,
          render: function(data, type, row) {
            return "<font color='#1285f5'>" + data + "</font>";
          }
        }, {
          targets: [3],
          orderable: false,
        }, {
          targets: [4],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '50px');
          }
        }, {
          targets: [5],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            return (data * 0.1).toFixed(1);
          }
        }, {
          targets: [6],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            return data == 0 ? "<span style='font-size:12px;' class='text-success'>Pre-Recorded Audio</span>" : "<span style='font-size:12px;' class='text-success'>AIValet Voice</span>";
          }
        }, {
          targets: [7],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            var mHtml = '';
            mHtml = mHtml + '<button type="button" class="btn btn-outline-primary waves-effect waves-light btn-sm" onclick="showContent(' + data + ')">View Message</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-success waves-effect waves-light btn-sm ms-1" onclick="viewVoicemail(' + data + ')">View 1st 10 #s</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-danger waves-effect waves-light btn-sm ms-1" onclick="onClickDeleteIcon(' + data + ')">Cancel Order</button>'
            return mHtml;
          }
        }]
      });
    } else if (pageTitle == "Scheduled Orders") {
      ordersTbl = $('#orders-table').DataTable({
        autoWidth: false,
        destroy: true,
        searching: false,
        pageLength: 5,
        lengthMenu: [
          [5, 10, 20, 50, 100],
          [5, 10, 20, 50, 100]
        ],
        lengthChange: false,
        data: orders_data,
        order: [
          [0, 'desc'],
        ],
        columns: [{
          data: 'id'
        }, {
          data: 'send_time'
        }, {
          data: 'order_no'
        }, {
          data: 'farm_name'
        }, {
          data: 'record_count'
        }, {
          data: 'record_count'
        }, {
          data: 'type'
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
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '145px');
          },
          render: function(data, type, row) {
            const dateString = data.substring(0, 10);
            const dateArray = dateString.split("-");
            return dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0] + " " + data.substring(11, 16);
          }
        }, {
          targets: [2],
          orderable: false,
          render: function(data, type, row) {
            return "<font color='#1285f5'>" + data + "</font>";
          }
        }, {
          targets: [3],
          orderable: false,
        }, {
          targets: [4],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '50px');
          }
        }, {
          targets: [5],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            return (data * 0.1).toFixed(1);
          }
        }, {
          targets: [6],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            return data == 0 ? "<span style='font-size:12px;' class='text-success'>Pre-Recorded Audio</span>" : "<span style='font-size:12px;' class='text-success'>AIValet Voice</span>";
          }
        }, {
          targets: [7],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            var mHtml = '';
            mHtml = mHtml + '<button type="button" class="btn btn-outline-primary waves-effect waves-light btn-sm" onclick="showContent(' + data + ')">View Message</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-success waves-effect waves-light btn-sm ms-1" onclick="viewVoicemail(' + data + ')">View 1st 10 #s</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-danger waves-effect waves-light btn-sm ms-1" onclick="onClickDeleteIcon(' + data + ')">Cancel Order</button>'
            return mHtml;
          }
        }]
      });
    } else {
      ordersTbl = $('#orders-table').DataTable({
        autoWidth: false,
        destroy: true,
        searching: false,
        pageLength: 5,
        lengthMenu: [
          [5, 10, 20, 50, 100],
          [5, 10, 20, 50, 100]
        ],
        lengthChange: false,
        data: orders_data,
        order: [
          [0, 'desc'],
        ],
        columns: [{
          data: 'id'
        }, {
          data: 'created_at'
        }, {
          data: 'order_no'
        }, {
          data: 'farm_name'
        }, {
          data: 'record_count'
        }, {
          data: 'queue'
        }, {
          data: 'success'
        }, {
          data: 'fail'
        }, {
          data: 'success'
        }, {
          data: 'type'
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
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '145px');
          },
          render: function(data, type, row) {
            const dateString = data.substring(0, 10);
            const dateArray = dateString.split("-");
            return dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0] + " " + data.substring(11, 16);
          }
        }, {
          targets: [2],
          orderable: false,
          render: function(data, type, row) {
            return "<font color='#1285f5'>" + data + "</font>";
          }
        }, {
          targets: [3],
          orderable: false,
        }, {
          targets: [4],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '50px');
          }
        }, {
          targets: [5],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '50px');
          }
        }, {
          targets: [6],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '50px');
          },
          render: function(data, type, row) {
            return "<span class='text-success'>" + data + "</span>";
          }
        }, {
          targets: [7],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '40px');
          },
          render: function(data, type, row) {
            return "<span class='text-danger'>" + data + "</span>";
          }
        }, {
          targets: [8],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            return (data * 0.1).toFixed(1);
          }
        }, {
          targets: [9],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            return data == 0 ? "<span style='font-size:12px;' class='text-success'>Pre-Recorded Audio</span>" : "<span style='font-size:12px;' class='text-success'>AIValet Voice</span>";
          }
        }, {
          targets: [10],
          orderable: false,
          createdCell: function(td, cellData, rowData, row, col) {
            $(td).css('width', '0px');
          },
          render: function(data, type, row) {
            var mHtml = '';
            mHtml = mHtml + '<button type="button" class="btn btn-outline-primary waves-effect waves-light btn-sm" onclick="showContent(' + data + ')">View Message</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-success waves-effect waves-light btn-sm ms-1" onclick="viewVoicemail(' + data + ')">View 1st 10 #s</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-warning waves-effect waves-light btn-sm ms-1" onclick="onClickResendAll(' + data + ')">Resend All</button>'
            mHtml = mHtml + '<button type="button" class="btn btn-outline-info waves-effect waves-light btn-sm ms-1" onclick="onClickResendFailed(' + data + ')">Resend Failed</button>'
            return mHtml;
          }
        }]
      });
    }
  }



  $("#orders-table tbody").on('click', 'tr', function() {
    $("#orders-table tbody tr").removeClass('table-success');
    $(this).addClass('table-success');
  });
</script>


<script>
  var showContent = function(id) {
    var data = orders_data.find((order) => order['id'] == id);
    if (data["type"] == 0) {
      $("#content").addClass("d-none");
      $("#content_title").html("Recording : " + data['record_name'])
      document.getElementById("p_audio_record").src = data['url'];
    } else {
      $("#content").removeClass("d-none");
      $("#content_title").html("AI Avatar Voice : " + data['record_name'])
      document.getElementById("p_audio_record").src = data['url'];
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
      var aiContent = data["content"];

      var aiSalutation = salutations.find((salutation) => aiContent.indexOf(salutation) == 0);
      if (aiSalutation == undefined) {
        $("#content").html("- No salutation -<br><b>Script : </b><br>" + aiContent);
      } else {
        $("#content").html("<b>Salutation : </b>" + aiSalutation + "<br><b>Script : </b><br>" + aiContent.replace(aiSalutation + ".", "").replace(aiSalutation, ""));
      }
    }
    $('#play-modal').modal('show');
  }


  $('#orders-table tbody').on('click', 'td .delete-orders', function() {
    var row = ordersTbl.row($(this).closest('tr'));
    var data = row.data();
    selected_orders_row = row.index();
    selected_orders_id = data['id'];

    var postData = {
      id: selected_orders_id
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
        $.ajax({
          url: "<?php echo base_url('orders/delete'); ?>",
          type: "POST",
          data: postData
        }).done(function(data) {
          var result = $.parseJSON(data);
          if (result['status'] == true) {
            Swal.fire("Deleted!", "Record has been deleted.", "success")
            row.remove().draw();
          } else {
            Swal.fire("Failed!", result['msg'], "error")
          }
        }).fail(function() {
          Swal.fire("Failed!", "Fail to delete orders.", "error")
        }).always(function() {

        });
      }
    })
  });

  var viewVoicemail = function(id) {
    var postData = {
      id: id
    };
    $.ajax({
      url: "<?php echo base_url('orders/viewVoicemail'); ?>",
      type: "POST",
      data: postData
    }).done(function(data) {
      var result = $.parseJSON(data);
      if (result['status'] == true) {
        contacts_data = result['mail_logs'];
        contactsTbl = $('#contacts-table').DataTable({
          pageLength: 10,
          lengthMenu: [
            [5, 10, 20, 50, 100],
            [5, 10, 20, 50, 100]
          ],
          lengthChange: false,
          paging: false,
          autoWidth: false,
          destroy: true,
          data: contacts_data,
          searching: false,
          info: false,
          columns: [{
            data: 'id'
          }, {
            data: 'created_at'
          }, {
            data: 'farm_name'
          }, {
            data: 'forwarding_number'
          }, {
            data: 'receiver'
          }, {
            data: 'status'
          }, {
            data: 'reason'
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
            createdCell: function(td, cellData, rowData, row, col) {
              $(td).css('width', '150px');
            },
            render: function(data, type, row) {
              const dateString = data.substring(0, 10);
              const dateArray = dateString.split("-");
              return dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0] + " " + data.substring(11, 16);
            }
          }, {
            targets: [2],
            orderable: false,
          }, {
            targets: [3],
            orderable: false,
            createdCell: function(td, cellData, rowData, row, col) {
              $(td).css('width', '150px');
            },
            render: function(data, type, row) {
              return formatPhoneNumber(data);
            }
          }, {
            targets: [4],
            orderable: false,
            createdCell: function(td, cellData, rowData, row, col) {
              $(td).css('width', '150px');
            },
            render: function(data, type, row) {
              return formatPhoneNumber(data);
            }
          }, {
            targets: [5],
            orderable: false,
          }, {
            targets: [6],
            orderable: false,
          }, {
            targets: [7],
            orderable: false,
            createdCell: function(td, cellData, rowData, row, col) {
              $(td).css('width', '0px');
            },
            render: function(data, type, row) {
              mHtml = '<a class="btn btn-outline-success waves-effect waves-light btn-sm" title="Show Content" onclick="showMessageContent(' + data + ')">' +
                ' <i class="fas fa-eye"></i></a>'
              return mHtml;
            }
          }]
        });
        $('#contacts-modal').modal('show');
      } else {
        Swal.fire("Failed!", result['msg'], "error")
      }
    }).fail(function() {
      Swal.fire("Failed!", "Fail to delete orders.", "error")
    }).always(function() {

    });
  }


  var showMessageContent = function(id) {
    var data = contacts_data.find((contact) => contact['id'] == id);
    if (data["type"] == 0) {
      $("#content").addClass("d-none");
      $("#content_title").html("Recording : " + data['record_name'])
      document.getElementById("p_audio_record").src = data['url'];
    } else {
      $("#content").removeClass("d-none");
      $("#content_title").html("AI Avatar Voice : " + data['record_name'])
      document.getElementById("p_audio_record").src = data['url'];
      $("#content").html(data["content"]);
    }
    $('#play-modal').modal('show');
  }

  //reload table row
  var reload_tbl_row = function(status) {
    var old_data = ordersTbl.row(selected_orders_row).data();
    old_data['status'] = status;
    ordersTbl.row(selected_orders_row).data(old_data).draw(false);
  }

  function oncloseModal() {
    document.getElementById("p_audio_record").src = "";
  }

  var onClickDeleteIcon = function(id) {
    var data = orders_data.find((order) => order['id'] == id);
    selected_orders_row = orders_data.findIndex((order) => order['id'] == id);
    selected_orders_id = id;
    deleteOrder();
  }

  var deleteOrder = function() {
    var postData = {
      id: selected_orders_id
    };

    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning",
      showCancelButton: !0,
      confirmButtonColor: "#2ab57d",
      cancelButtonColor: "#fd625e",
      confirmButtonText: "Yes, cancel it!"
    }).then(function(e) {
      if (e.value) {
        showLoading();
        $.ajax({
          url: "<?php echo base_url('orders/delete'); ?>",
          type: "POST",
          data: postData
        }).done(function(data) {
          var result = $.parseJSON(data);
          if (result['status'] == true) {
            Swal.fire("Canceled!", "Order has been canceled.", "success")
            delete_tbl_row();
          } else {
            Swal.fire("Failed!", result['msg'], "error")
          }
        }).fail(function() {
          Swal.fire("Failed!", "Fail to cancel Order.", "error")
        }).always(function() {
          hideLoading();
        });
      }
    });
  }

  var delete_tbl_row = function(data) {
    const index = orders_data.findIndex((x) => x.id == selected_orders_id);
    if (index > -1) { // only splice array when item is found
      orders_data.splice(index, 1); // 2nd parameter means remove one item only
    }
    ordersTbl.row(selected_orders_row).remove().draw();
  }

  var onClickResendFailed = function(id) {
    var data = orders_data.find((order) => order['id'] == id);
    if (data["fail"] == 0) {
      show_msg("error", "No Fail");
      return;
    }
    if (!isDateDaysAgo(data["created_at"], 1)) {
      show_msg("error", "You can resend tomorrow");
      return;
    }
    selected_orders_row = orders_data.findIndex((order) => order['id'] == id);
    selected_orders_id = id;
    resendOrder("<?php echo base_url('orders/resend_failed'); ?>");
  }

  var onClickResendAll = function(id) {
    var data = orders_data.find((order) => order['id'] == id);
    if (!isDateDaysAgo(data["created_at"], 7)) {
      show_msg("error", "You can resend after a week");
      return;
    }
    selected_orders_row = orders_data.findIndex((order) => order['id'] == id);
    selected_orders_id = id;
    resendOrder("<?php echo base_url('orders/resend_all'); ?>");
  }


  var resendOrder = function(api) {
    var postData = {
      id: selected_orders_id
    };

    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning",
      showCancelButton: !0,
      confirmButtonColor: "#2ab57d",
      cancelButtonColor: "#fd625e",
      confirmButtonText: "Yes, resend it!"
    }).then(function(e) {
      if (e.value) {
        showLoading();
        $.ajax({
          url: api,
          type: "POST",
          data: postData
        }).done(function(data) {
          var result = $.parseJSON(data);
          if (result['status'] == true) {
            Swal.fire("Resent!", "Order has been resent.", "success")
            delete_tbl_row();
          } else {
            Swal.fire("Failed!", result['msg'], "error")
          }
        }).fail(function() {
          Swal.fire("Failed!", "Fail to resend Order.", "error")
        }).always(function() {
          hideLoading();
        });
      }
    });
  }
</script>

<script type="text/javascript">
  $(document).ready(function() {
    if (pageTitle == "Pending Orders") {
      $("#menu-pending-orders").addClass('mm-active');
    } else if (pageTitle == "Scheduled Orders") {
      $("#menu-scheduled-orders").addClass('mm-active');
    } else {
      $("#menu-delivered-orders").addClass('mm-active');
    }
  });

  set_interval();

  var timer = 0;

  function set_interval() {
    timer = setInterval("reload_page()", 900000);
  }

  function reload_page() {
    window.location.href = "<?php echo base_url('orders') ?>";
  }

  $('#user').change(function(e) {
    searchOrder();
  });

  $('#start_date').change(function(e) {
    $('#end_date').attr('min', $('#start_date').val())
    $('#end_date').val("");
  });

  function searchOrder() {
    var selected_user = $("#user :selected").val();
    var start_date = $("#start_date").val();
    var end_date = $("#end_date").val();
    var search_query = $("#search_query").val();
    if (pageTitle == "Pending Orders") {
      window.location.href = '<?php echo base_url('orders?token=' . urlencode($token)); ?>' +
        '&selected_user=' + selected_user + '&start_date=' + start_date +
        '&end_date=' + end_date + '&search_query=' + search_query
    } else if (pageTitle == "Scheduled Orders") {
      window.location.href = '<?php echo base_url('orders/scheduled?token=' . urlencode($token)); ?>' +
        '&selected_user=' + selected_user + '&start_date=' + start_date +
        '&end_date=' + end_date + '&search_query=' + search_query
    } else {
      window.location.href = '<?php echo base_url('orders/delivered?token=' . urlencode($token)); ?>' +
        '&selected_user=' + selected_user + '&start_date=' + start_date +
        '&end_date=' + end_date + '&search_query=' + search_query
    }
  }
</script>