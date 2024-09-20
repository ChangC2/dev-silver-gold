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
                  <h4 class="card-title">Contact</h4>
                  <p class="card-title-desc">Manage all contacts</p>
                </div>
                <div class="col-md-3">
                  <button type="button" class="btn btn-primary waves-effect btn-label waves-light" style="float: right; margin: auto;" onclick="show_add_contactdlg();">
                    <i class="bx bx-add-to-queue label-icon"></i> Add Contact
                  </button>
                </div>
              </div>
            </div>
            <div class="card-body">
              <div class="table-responsive">
                <table id="contact-table" class="table align-middle table-bordered nowrap w-100">
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>Phone Number</th>
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