import React, { useState, useEffect } from "react";

import ApkProductUploader from "./ApkUploader/ApkProductUploader";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";
function ApkProduct(props) {
  const { version } = props;

  return (
    <div>
      <div className="app-setting-text-input-title">
        Apk Product Version: {version}
      </div>
      <div>
        <ApkProductUploader />
      </div>
    </div>
  );
}

export default ApkProduct;
