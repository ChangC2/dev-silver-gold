import React from "react";
import PropTypes from "prop-types";
import Pdf from "react-to-pdf";

const PDFButton = ({ children, filename, targetRef }) => (
  <Pdf targetRef={targetRef} filename={filename}>
    {({ toPdf }) => <button onClick={toPdf}>{children}</button>}
  </Pdf>
);

PDFButton.propTypes = {
  filename: PropTypes.string,
  targetRef: PropTypes.any
};

PDFButton.defaultProps = {
  filename: "code-example.pdf"
};

export default PDFButton;
