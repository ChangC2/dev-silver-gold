<script>
  var ipTbl;

  var selected_ip_id;
  var selected_ip_row;
  var ip_data = <?php echo json_encode($ips_data); ?>;

  ipTbl = $('#ip-table').DataTable({
    autoWidth: false,
    destroy: true,
    data: ip_data,
    columns: [{
      data: 'ip_address'
    }, {
      data: 'id'
    }],
    columnDefs: [{
      targets: [1],
      createdCell: function(td, cellData, rowData, row, col) {
        $(td).css('width', '0px');
      },
      render: function(data, type, row) {
        var mHtml = '';
        mHtml = mHtml + '<a class="btn btn-outline-info waves-effect waves-light btn-sm edit-ip" title="Edit" >' +
          ' <i class="fas fa-pencil-alt"></i></a>'
        mHtml = mHtml + '<a class="btn btn-outline-danger waves-effect waves-light btn-sm delete-ip" style="margin-left:5px;"  title="Delete" >' +
          ' <i class="far fa-trash-alt"></i></a>'
        return mHtml;
      }
    }]
  });

  $("#ip-table tbody").on('click', 'tr', function() {
    $("#ip-table tbody tr").removeClass('table-success');
    $(this).addClass('table-success');
  });
</script>


<script>
  var show_add_ipdlg = function() {
    clear_ip_input();
    $('#add-ip_modal').modal('show');
  }

  var clear_ip_input = function() {
    $("#ip_address").val('');
  }

  var add_ip = function() {
    var ip_address = $("#ip_address").val().trim();

    if (ip_address === "") {
      show_msg("error", "Enter IP Address");
      return;
    }

    //close add quiz modal
    $('#add-ip_modal').modal('toggle');

    var postData = {
      ip_address: ip_address
    };

    $.ajax({
      url: "<?php echo base_url('ips/add'); ?>",
      type: "POST",
      data: postData
    }).done(function(data) {
      var result = $.parseJSON(data);
      if (result['status'] == true) {
        show_msg("success", "Success to add ip");
        add_ip_tbl_row(result['id'], postData);
        clear_ip_input();
      } else {
        show_msg("error", "Fail to add ip");
      }
    }).fail(function() {
      show_msg("error", "Fail to add ip");
    }).always(function() {});
  }

  var add_ip_tbl_row = function(id, data) {
    ipTbl.row.add({
      "id": id,
      "ip_address": data['ip_address'],
    }).draw();
  }

  //edit row
  $('#ip-table tbody').on('click', 'td .edit-ip', function() {
    var row = ipTbl.row($(this).closest('tr'));
    var data = row.data();
    selected_ip_row = row.index();
    selected_ip_id = data['id'];
    $("#u_ip_address").val(data['ip_address']);
    $('#update-ip_modal').modal('show');
  });

  var updateIP = function() {
    var ip_address = $("#u_ip_address").val().trim();

    if (ip_address === "") {
      show_msg("error", "Enter IP Address");
      return;
    }

    //close add quiz modal
    $('#update-ip_modal').modal('toggle');

    var postData = {
      id: selected_ip_id,
      ip_address: ip_address,
    };

    $.ajax({
      url: "<?php echo base_url('ips/update'); ?>",
      type: "POST",
      data: postData
    }).done(function(data) {
      var result = $.parseJSON(data);
      if (result['status'] == true) {
        show_msg("success", "Success to update ip");
        reload_ip_tbl_row(postData);
      } else {
        show_msg("error", "Fail to update ip");
      }
    }).fail(function() {
      show_msg("error", "Fail to update ip");
    }).always(function() {});
  }


  //delete row
  $('#ip-table tbody').on('click', 'td .delete-ip', function() {
    var row = ipTbl.row($(this).closest('tr'));
    var data = row.data();
    selected_ip_row = row.index();
    selected_ip_id = data['id'];

    var postData = {
      id: selected_ip_id
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
          url: "<?php echo base_url('ips/delete'); ?>",
          type: "POST",
          data: postData
        }).done(function(data) {
          var result = $.parseJSON(data);
          if (result['status'] == true) {
            Swal.fire("Deleted!", "IP has been deleted.", "success")
            row.remove().draw();
          } else {
            Swal.fire("Failed!", result['msg'], "error")
          }
        }).fail(function() {
          Swal.fire("Failed!", "Fail to delete ip.", "error")
        }).always(function() {

        });
      }
    })
  });

  //reload table row
  var reload_ip_tbl_row = function(data) {
    var old_data = ipTbl.row(selected_ip_row).data();
    old_data['ip_address'] = data['ip_address'];
    ipTbl.row(selected_ip_row).data(old_data).draw(false);
  }
</script>

<script type="text/javascript">
  $(document).ready(function() {
    $("#menu-ips").addClass('mm-active');
  });
</script>