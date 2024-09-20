<script>
  var contactTbl;

  var selected_contact_id;
  var selected_contact_row;
  var contact_data = <?php echo json_encode($contacts_data); ?>;

  contactTbl = $('#contact-table').DataTable({
    autoWidth: false,
    destroy: true,
    data: contact_data,
    columns: [{
      data: 'name'
    }, {
      data: 'phone'
    }, {
      data: 'id'
    }],
    columnDefs: [{
      targets: [2],
      createdCell: function(td, cellData, rowData, row, col) {
        $(td).css('width', '0px');
      },
      render: function(data, type, row) {
        var mHtml = '';
        mHtml = mHtml + '<a class="btn btn-outline-info waves-effect waves-light btn-sm edit-contact" title="Edit" >' +
          ' <i class="fas fa-pencil-alt"></i></a>'
        mHtml = mHtml + '<a class="btn btn-outline-danger waves-effect waves-light btn-sm delete-contact" style="margin-left:5px;"  title="Delete" >' +
          ' <i class="far fa-trash-alt"></i></a>'
        return mHtml;
      }
    }]
  });

  $("#contact-table tbody").on('click', 'tr', function() {
    $("#contact-table tbody tr").removeClass('table-success');
    $(this).addClass('table-success');
  });
</script>


<script>
  var show_add_contactdlg = function() {
    clear_contact_input();
    $('#add-contact_modal').modal('show');
  }

  var clear_contact_input = function() {
    $("#name").val('');
    $("#phone").val('');
  }

  var add_contact = function() {
    var name = $("#name").val().trim();
    var phone = $("#phone").val().trim();

    if (name === "") {
      show_msg("error", "Enter Name");
      return;
    }
    if (phone === "") {
      show_msg("error", "Enter Phone Number");
      return;
    }


    //close add quiz modal
    $('#add-contact_modal').modal('toggle');

    var postData = {
      user_id: "<?php echo $user_id; ?>",
      name: name,
      phone: phone,
      created_at: '<?php echo date('Y-m-d H:i:s') ?>',
    };

    $.ajax({
      url: "<?php echo base_url('contacts/add'); ?>",
      type: "POST",
      data: postData
    }).done(function(data) {
      var result = $.parseJSON(data);
      if (result['status'] == true) {
        show_msg("success", "Success to add contact");
        add_contact_tbl_row(result['id'], postData);
        clear_contact_input();
      } else {
        show_msg("error", "Fail to add contact");
      }
    }).fail(function() {
      show_msg("error", "Fail to add contact");
    }).always(function() {});
  }

  var add_contact_tbl_row = function(id, data) {
    contactTbl.row.add({
      "id": id,
      "name": data['name'],
      "phone": data['phone'],
    }).draw();
  }

  //edit row
  $('#contact-table tbody').on('click', 'td .edit-contact', function() {
    var row = contactTbl.row($(this).closest('tr'));
    var data = row.data();
    selected_contact_row = row.index();
    selected_contact_id = data['id'];
    $("#u_name").val(data['name']);
    $("#u_phone").val(data['phone']);
    $('#update-contact_modal').modal('show');
  });

  var updateContact = function() {
    var name = $("#u_name").val().trim();
    var phone = $("#u_phone").val().trim();

    if (name === "") {
      show_msg("error", "Enter Name");
      return;
    }
    if (phone === "") {
      show_msg("error", "Enter Phone Number");
      return;
    }

    //close add quiz modal
    $('#update-contact_modal').modal('toggle');

    var postData = {
      id: selected_contact_id,
      name: name,
      phone: phone,
    };

    $.ajax({
      url: "<?php echo base_url('contacts/update'); ?>",
      type: "POST",
      data: postData
    }).done(function(data) {
      var result = $.parseJSON(data);
      if (result['status'] == true) {
        show_msg("success", "Success to update contact");
        reload_contact_tbl_row(postData);
      } else {
        show_msg("error", "Fail to update contact");
      }
    }).fail(function() {
      show_msg("error", "Fail to update contact");
    }).always(function() {});
  }


  //delete row
  $('#contact-table tbody').on('click', 'td .delete-contact', function() {
    var row = contactTbl.row($(this).closest('tr'));
    var data = row.data();
    selected_contact_row = row.index();
    selected_contact_id = data['id'];

    var postData = {
      id: selected_contact_id
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
          url: "<?php echo base_url('contacts/delete'); ?>",
          type: "POST",
          data: postData
        }).done(function(data) {
          var result = $.parseJSON(data);
          if (result['status'] == true) {
            Swal.fire("Deleted!", "Contact has been deleted.", "success")
            row.remove().draw();
          } else {
            Swal.fire("Failed!", result['msg'], "error")
          }
        }).fail(function() {
          Swal.fire("Failed!", "Fail to delete contact.", "error")
        }).always(function() {

        });
      }
    })
  });

  //reload table row
  var reload_contact_tbl_row = function(data) {
    var old_data = contactTbl.row(selected_contact_row).data();
    old_data['name'] = data['name'];
    old_data['phone'] = data['phone'];
    contactTbl.row(selected_contact_row).data(old_data).draw(false);
  }
</script>

<script type="text/javascript">
  $(document).ready(function() {
    $("#menu-contacts").addClass('mm-active');
  });
</script>