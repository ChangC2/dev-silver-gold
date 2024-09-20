<!-- start main content-->
<div class="main-content">
  <div class="page-content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <div class="row">
                <div class="col-md-9">
                  <h4 class="card-title">Users</h4>
                  <p class="card-title-desc">Manage all users</p>
                </div>
                <!-- <div class="col-md-3">
                  <a href="<?php echo base_url('accounts/create?token=' . urlencode($token)) ?>" class="btn btn-primary waves-effect btn-label waves-light" style="float: right; margin: auto;"><i class="bx bx-add-to-queue label-icon"></i> Add New User</a>
                  <br /> <br />
                </div> -->
              </div>
            </div>
            <div class="card-body">
              <div class="table-responsive">
                <table id="account-table" class="table align-middle table-bordered nowrap w-100">
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>Email</th>
                      <th>Phone Number</th>
                      <th>Created At</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <footer class="footer">
    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-6">
          ©
          <script>
            document.write(new Date().getFullYear())
          </script>
        </div>
        <div class="col-sm-6">
          <div class="text-sm-end d-none d-sm-block">
            Powered by <a href="https://newhomepage.com/" class="text-decoration-underline" target="_blank">NewHomePage</a>
          </div>
        </div>
      </div>
    </div>
  </footer>

</div>
<!-- end main content-->


<script>
  var accountTbl;
  var selected_account_id;
  var selected_account_row;

  var accounts_data = <?php echo json_encode($accounts_data); ?>;
  accountTbl = $('#account-table').DataTable({
    autoWidth: false,
    destroy: true,
    data: accounts_data,
    order: [
      [3, 'desc']
    ],
    columns: [{
      data: 'accountname'
    }, {
      data: 'email'
    }, {
      data: 'mobile_number'
    }, {
      data: 'created_at'
    }, {
      data: 'id'
    }],
    columnDefs: [{
      targets: [4],
      createdCell: function(td, cellData, rowData, row, col) {
        $(td).css('width', '0px');
      },
      render: function(data, type, row) {
        var mHtml = '';
        // mHtml = mHtml + '<a href="<?php echo base_url('accounts/edit?token=' . urlencode($token) . '&id=') ?>' + data + '" class="btn btn-outline-info waves-effect waves-light btn-sm" title="Edit" >' +
        //   ' <i class="fas fa-pencil-alt"></i></a>'
        mHtml = mHtml + '<a class="btn btn-outline-danger waves-effect waves-light btn-sm delete-account" style="margin-left:5px;"  title="Delete" >' +
          ' <i class="far fa-trash-alt"></i></a>'
        return mHtml;
      }
    }]
  });


  $("#account-table tbody").on('click', 'tr', function() {
    $("#account-table tbody tr").removeClass('table-success');
    $(this).addClass('table-success');
  });
</script>


<script>
  //delete row
  $('#account-table tbody').on('click', 'td .delete-account', function() {
    var row = accountTbl.row($(this).closest('tr'));
    var data = row.data();
    selected_account_row = row.index();
    selected_account_id = data['id'];

    var postData = {
      id: selected_account_id
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
          url: "<?php echo base_url('accounts/delete'); ?>",
          type: "POST",
          data: postData
        }).done(function(data) {
          var result = $.parseJSON(data);
          if (result['status'] == true) {
            Swal.fire("Deleted!", "Account has been deleted.", "success")
            row.remove().draw();
          } else {
            Swal.fire("Failed!", result['msg'], "error")
          }
        }).fail(function() {
          Swal.fire("Failed!", "Fail to delete account.", "error")
        }).always(function() {

        });
      }
    })
  });
</script>


<script type="text/javascript">
  $(document).ready(function() {
    $("#menu-accounts").addClass('mm-active');
  });
</script>