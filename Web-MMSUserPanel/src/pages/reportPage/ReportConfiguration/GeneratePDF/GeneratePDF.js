import React, { useState } from "react";
import { Modal, Button, message } from "antd";
import "./GeneratePDF.css";
import PDFPreviewer from "./PDFPreviewer/PDFPreviewer";
import { GetReportPeriodData } from "../../../../services/common/cnc_apis";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";
function GeneratePDF(props) {
  const { langIndex } = useSelector((x) => x.app);
  const {
    customer_id,
    fromReportDate,
    toReportDate,
    selectedEmails,
    selectedMachine,
    selectedOperator,
    customerInfo,
    machineInfo,
  } = props;

  const { isGeneratingPDF, setIsGeneratingPDF } = props;
  const [showModal, setShowModal] = useState(false);
  const [pdfData, setPdfData] = useState({});
  const onGeneratePDF = () => {
    setIsGeneratingPDF(true);

    GetReportPeriodData(
      customer_id,
      selectedMachine,
      customerInfo["timezone"],
      fromReportDate,
      toReportDate,
      (res) => {
        setIsGeneratingPDF(false);
        if (res.status == false) {
          message.error(lang(langIndex, "msg_something_wrong"));
        } else {
          if (res.data["oee"].length == 0) {
            message.error(lang(langIndex, "msg_no_data_print"));
            return;
          }
          setPdfData({ ...res.data });
          setShowModal(true);
        }
      }
    );
    // GetReportPeriodData
  };

  const handleOk = (e) => {
    setShowModal(false);
  };

  return (
    <div>
      {!showModal ? null : (
        <Modal
          title={null}
          footer={null}
          visible={showModal}
          onOk={handleOk}
          onCancel={handleOk}
          destroyOnClose={true}
          // bodyStyle={{ height: "25.7cm" }}
          width="21cm"
        >
          <PDFPreviewer
            pdfData={pdfData}
            customerInfo={customerInfo}
            machineInfo={machineInfo.length > 0 ? machineInfo[0] : undefined}
            setShowModal={setShowModal}
            selectedEmails={selectedEmails}
          />
        </Modal>
      )}

      <Button
        type="primary"
        disabled={selectedMachine.length != 1}
        loading={isGeneratingPDF}
        onClick={onGeneratePDF}
      >
        {lang(langIndex, "report_generatepdf")}
      </Button>
    </div>
  );
}

export default GeneratePDF;
