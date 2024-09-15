import React, { useState, useEffect } from "react";

import ApkBetaUploader from "./ApkUploader/ApkBetaUploader";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";

function ApkBeta(props) {
  const { version } = props;

  return (
    <div>
      <div className="app-setting-text-input-title">
        Apk Beta Version: {version}
      </div>
      <div>
        <ApkBetaUploader />
      </div>
    </div>
  );
}

export default ApkBeta;
