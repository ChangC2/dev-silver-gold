<?php

if (isset($create)) {
  if (file_exists($create)) {
    include $create;
  } else {
    echo "<!--page $create file load fail Error 404-->";
  }
}

if (isset($edit)) {
  if (file_exists($edit)) {
    include $edit;
  } else {
    echo "<!--page $edit file load fail Error 404-->";
  }
}


if (isset($mail)) {
  if (file_exists($mail)) {
    include $mail;
  } else {
    echo "<!--page $mail file load fail Error 404-->";
  }
}

if (isset($submit)) {
  if (file_exists($submit)) {
    include $submit;
  } else {
    echo "<!--page $submit file load fail Error 404-->";
  }
}

if (isset($contacts_modal)) {
  if (file_exists($contacts_modal)) {
    include $contacts_modal;
  } else {
    echo "<!--page $contacts_modal file load fail Error 404-->";
  }
}

if (isset($play)) {
  if (file_exists($play)) {
    include $play;
  } else {
    echo "<!--page $play file load fail Error 404-->";
  }
}

if (isset($js)) {
  if (file_exists($js)) {
    include $js;
  } else {
    echo "<!--page $js file load fail Error 404-->";
  }
}

if (isset($sign_header)) {
  if (file_exists($sign_header)) {
    include $sign_header;
  } else {
    echo "<!--page $sign_header file load fail Error 404-->";
  }
}

if (isset($sign_part1)) {
  if (file_exists($sign_part1)) {
    include $sign_part1;
  } else {
    echo "<!--page $sign_part1 file load fail Error 404-->";
  }
}

if (isset($sign_part2)) {
  if (file_exists($sign_part2)) {
    include $sign_part2;
  } else {
    echo "<!--page $sign_part2 file load fail Error 404-->";
  }
}

if (isset($sign_part3)) {
  if (file_exists($sign_part3)) {
    include $sign_part3;
  } else {
    echo "<!--page $sign_part3 file load fail Error 404-->";
  }
}

if (isset($sign_script)) {
  if (file_exists($sign_script)) {
    include $sign_script;
  } else {
    echo "<!--page $sign_script file load fail Error 404-->";
  }
}

if (isset($sign)) {
  if (file_exists($sign)) {
    include $sign;
  } else {
    echo "<!--page $sign file load fail Error 404-->";
  }
}

if (isset($fcc)) {
  if (file_exists($fcc)) {
    include $fcc;
  } else {
    echo "<!--page $fcc file load fail Error 404-->";
  }
}
?>

<script>
  var show_msg = function(icon, title) {
    Swal.fire({
      position: "top-center",
      icon: icon,
      title: title,
      showConfirmButton: !1,
      timer: 2000
    });
  }

  var showLoading = function() {
    $("#spinner_overlay").fadeIn(300);
  }

  var hideLoading = function() {
    $("#spinner_overlay").fadeOut(300);
  }

  var formatPhoneNumber = function(phoneNumberString) {
    var cleaned = ('' + phoneNumberString).replace(/\D/g, '');
    var match = cleaned.match(/^(\d{3})(\d{3})(\d{4})$/);
    if (match) {
      return '(' + match[1] + ') ' + match[2] + '-' + match[3];
    }
    return null;
  }

  var isDateDaysAgo = function(dateString, days) {
    const oneWeekAgo = new Date(new Date().getTime() - (days * 24 * 60 * 60 * 1000));
    const givenDate = new Date(dateString);
    return givenDate <= oneWeekAgo;
  }

  var sendVoicemails = function() {
    window.location.href = "<?php echo base_url('sendmail?token=') ?>" + encodeURIComponent("<?php echo $token; ?>");
  }
</script>

<script src="<?php echo base_url('assets/'); ?>js/app.js"></script>
<script src="<?php echo base_url('assets/'); ?>js/recorder.js"></script>

</body>

</html>