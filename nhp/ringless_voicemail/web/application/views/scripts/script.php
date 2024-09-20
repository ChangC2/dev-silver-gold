<script>
  new Choices("#user", {
    shouldSort: !1
  });

  $('#user').change(function(e) {
    var selected_user = $("#user :selected").val();
    window.location.href = '<?php echo base_url('scripts?token=' . urlencode($token)); ?>' + '&selected_user=' + selected_user
  });

  new Choices("#salutation", {
    shouldSort: !1
  });

  var uSalutationChoice = new Choices("#u_salutation", {
    shouldSort: !1
  });

  var scriptsTbl;

  var selected_scripts_id;
  var selected_scripts_row;
  var selected_table_row;

  var permission = <?php echo $permission; ?>;
  var scripts_data = <?php echo json_encode($scripts_data); ?>;
  if (permission == 0) {
    scriptsTbl = $('#scripts-table').DataTable({
      pageLength: 5,
      lengthMenu: [
        [5, 10, 20, 50, 100],
        [5, 10, 20, 50, 100]
      ],
      oLanguage: {
        "sEmptyTable": "You do not have a script yet.   Please click on the Script or Upload button to add your first Script"
      },
      autoWidth: false,
      destroy: true,
      data: scripts_data,
      order: [
        [0, 'desc'],
      ],
      columns: [{
        data: 'id'
      }, {
        data: 'name'
      }, {
        data: 'salutation'
      }, {
        data: 'content'
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
        render: function(data, type, row) {
          return "<div class='text-wrap'>" + data + "</div>";
        }
      }, {
        targets: [4],
        orderable: false,
      }, {
        targets: [5],
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
        targets: [6],
        orderable: false,
        createdCell: function(td, cellData, rowData, row, col) {
          $(td).css('width', '0px');
        },
        render: function(data, type, row) {
          var mHtml = '';
          mHtml = mHtml + '<a class="btn btn-outline-info waves-effect waves-light btn-sm" title="Edit Script" onclick="onClickEdit(' + data + ')" style="margin-left:5px;">' +
            ' <i class="fas fa-pencil-alt"></i></a>'
          mHtml = mHtml + '<a class="btn btn-outline-danger waves-effect waves-light btn-sm" title="Delete" onclick="onClickDeleteIcon(' + data + ')" style="margin-left:5px;">' +
            ' <i class="far fa-trash-alt"></i></a>'
          return mHtml;
        }
      }]
    });
  } else {
    scriptsTbl = $('#scripts-table').DataTable({
      pageLength: 5,
      lengthMenu: [
        [5, 10, 20, 50, 100],
        [5, 10, 20, 50, 100]
      ],
      oLanguage: {
        "sEmptyTable": "You do not have a script yet.   Please click on the Script or Upload button to add your first Script"
      },
      autoWidth: false,
      destroy: true,
      data: scripts_data,
      order: [
        [0, 'desc'],
      ],
      columns: [{
        data: 'id'
      }, {
        data: 'name'
      }, {
        data: 'salutation'
      }, {
        data: 'content'
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
        render: function(data, type, row) {
          return "<div class='text-wrap'>" + data + "</div>";
        }
      }, {
        targets: [4],
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
        targets: [5],
        orderable: false,
        createdCell: function(td, cellData, rowData, row, col) {
          $(td).css('width', '0px');
        },
        render: function(data, type, row) {
          var mHtml = '';
          mHtml = mHtml + '<a class="btn btn-outline-info waves-effect waves-light btn-sm" title="Edit Script" onclick="onClickEdit(' + data + ')" style="margin-left:5px;">' +
            ' <i class="fas fa-pencil-alt"></i></a>'
          mHtml = mHtml + '<a class="btn btn-outline-danger waves-effect waves-light btn-sm" title="Delete" onclick="onClickDeleteIcon(' + data + ')" style="margin-left:5px;">' +
            ' <i class="far fa-trash-alt"></i></a>'
          return mHtml;
        }
      }]
    });
  }


  $("#scripts-table tbody").on('click', 'tr', function() {
    $("#scripts-table tbody tr").removeClass('table-success');
    $(this).addClass('table-success');
  });
</script>


<script>
  var show_add_dlg = function() {
    clear_input();
    $('#add-modal').modal('show');
  }

  var clear_input = function() {
    $("#name").val('');
    $("#content").val('');
  }

  var onClickSave = function() {
    var name = $("#name").val().trim();
    var content = $("#content").val().trim();
    if (name === "") {
      show_msg("error", "Enter Name");
      return;
    }
    if (content === "") {
      show_msg("error", "Enter Content");
      return;
    }
    $('#add-modal').modal('toggle');

    var postData = {
      token: "<?php echo $token; ?>",
      name: name,
      content: content,
      salutation: $("#salutation :selected").val() == "7" ? "" : $("#salutation :selected").text(),
      created_at: '<?php echo date('Y-m-d H:i:s') ?>',
    };
    showLoading();
    $.ajax({
      url: "<?php echo base_url('scripts/add'); ?>",
      type: "POST",
      data: postData
    }).done(function(data) {
      var result = $.parseJSON(data);
      if (result['status'] == true) {
        show_msg("success", "Congrats ! You saved your script");
        //window.location.href = "<?php echo base_url('scripts?token=') . $token; ?>";
        add_tbl_row(result['id'], postData);
      } else {
        show_msg("error", "Fail to add script");
      }
    }).fail(function() {
      show_msg("error", "Fail to add script");
    }).always(function() {
      hideLoading();
    });
  }


  var add_tbl_row = function(id, data) {
    var newScript = {
      "id": id,
      "name": data['name'],
      "salutation": data['salutation'],
      "content": data['content'],
      "created_at": data['created_at'],
    };
    scripts_data.push(newScript);
    scriptsTbl.row.add(newScript).draw();
  }

  //edit record
  var onClickEdit = function(id) {
    var data = scripts_data.find((script) => script['id'] == id);
    selected_scripts_row = scripts_data.findIndex((script) => script['id'] == id);
    selected_scripts_id = id;
    $("#u_name").val(data['name']);
    $("#u_content").val(data['content']);

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

    $('#update-modal').modal('show');
  }

  var onClickUpdate = function() {
    var name = $("#u_name").val().trim();
    var content = $("#u_content").val().trim();
    if (name === "") {
      show_msg("error", "Enter Name");
      return;
    }
    if (content === "") {
      show_msg("error", "Enter Content");
      return;
    }
    $('#update-modal').modal('toggle');
    var postData = {
      id: selected_scripts_id,
      name: name,
      salutation: $("#u_salutation :selected").val() == "7" ? "" : $("#u_salutation :selected").text(),
      content: content
    };
    showLoading();
    $.ajax({
      url: "<?php echo base_url('scripts/update'); ?>",
      type: "POST",
      data: postData
    }).done(function(res) {
      var result = $.parseJSON(res);
      if (result['status'] == true) {
        show_msg("success", "Congrats ! You updated your script");
        reload_tbl_row(postData);
      } else {
        show_msg("error", "Fail to update script");
      }
    }).fail(function() {
      show_msg("error", "Fail to update script");
    }).always(function() {
      hideLoading();
    });
  }


  var onClickDeleteButton = function() {
    deleteScript();
  };

  var onClickDeleteIcon = function(id) {
    var data = scripts_data.find((script) => script['id'] == id);
    selected_scripts_row = scripts_data.findIndex((script) => script['id'] == id);
    selected_scripts_id = id;
    deleteScript();
  }

  var deleteScript = function() {
    var postData = {
      id: selected_scripts_id
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
          url: "<?php echo base_url('scripts/delete'); ?>",
          type: "POST",
          data: postData
        }).done(function(data) {
          var result = $.parseJSON(data);
          if (result['status'] == true) {
            Swal.fire("Deleted!", "Script has been deleted.", "success")
            scriptsTbl.row(selected_scripts_row).remove().draw();
          } else {
            Swal.fire("Failed!", result['msg'], "error")
          }
        }).fail(function() {
          Swal.fire("Failed!", "Fail to delete script.", "error")
        }).always(function() {
          hideLoading();
        });
      }
    });
  }

  //reload table row
  var reload_tbl_row = function(data) {
    scripts_data[selected_scripts_row]["name"] = data['name'];
    scripts_data[selected_scripts_row]["salutation"] = data['salutation'];
    scripts_data[selected_scripts_row]["content"] = data['content'];
    var old_data = scriptsTbl.row(selected_scripts_row).data();
    old_data['name'] = data['name'];
    old_data['salutation'] = data['salutation'];
    old_data['content'] = data['content'];
    scriptsTbl.row(selected_scripts_row).data(old_data).draw(false);
  }

  var delete_tbl_row = function(data) {
    const index = scripts_data.findIndex((x) => x.id == selected_scripts_id);
    if (index > -1) { // only splice array when item is found
      scripts_data.splice(index, 1); // 2nd parameter means remove one item only
    }
    scriptsTbl.row(selected_scripts_row).remove().draw();
  }

  function oncloseModal() {}
</script>

<script type="text/javascript">
  $(document).ready(function() {
    $("#menu-scripts").addClass('mm-active');
  });
</script>