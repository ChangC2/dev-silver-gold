import { Col, Radio, Row, message } from "antd";
import CurrentPartProcessingTimeWidget from "components/CurrentPartProcessingTimeWidget/CurrentPartProcessingTimeWidget";
import GoodBadModal from "components/GoodBadModal/GoodBadModal";
import GoodBadWidget from "components/GoodBadWidget/GoodBadWidget";
import InputModeModal from "layouts/InputModeModal/InputModeModal";
import PartIDInputLayout from "layouts/PartIDInputLayout/PartIDInputLayout";
import PartIDModal from "layouts/PartIDModal/PartIDModal";
import UserInfoLayout from "layouts/UserInfoLayout/UserInfoLayout";
import { useState } from "react";
import moment from "moment";
import "./AssemblyStation136Layout.css";
import { useDispatch, useSelector } from "react-redux";
import {
  appData,
  secondToTime,
  timeToSecond,
  userData,
  factoryData,
} from "services/global";
import { isSpinning, setAppDataStore } from "redux/actions/appActions";
import {
  apiCallForGetBlu136Assembly,
  apiCallForPostBlu136Assembly,
} from "services/apiCall";
import { useEffect } from "react";
import TextInputArrayModal from "components/TextInputArrayModal/TextInputArrayModal";

const AssemblyStation136Layout = (props) => {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const [showInputMode, setShowInputMode] = useState(false);
  const [showPartID, setShowPartID] = useState(false);
  const [partID, setPartID] = useState("");

  const [goodParts, setGoodParts] = useState("0");
  const [badParts, setBadParts] = useState("0");
  const [showGoodBadModal, setShowGoodBadModal] = useState(false);

  const [inputMode, setInputMode] = useState("0");

  const [processingTime, setProcessingTime] = useState("00:00:00");
  const [notes, setNotes] = useState("");

  const [showInputModal1, setShowInputModal1] = useState(false);
  const [showInputModal2, setShowInputModal2] = useState(false);
  const [showInputModal3, setShowInputModal3] = useState(false);
  const [showInputModal4, setShowInputModal4] = useState(false);

  const titles1 = [
    "base_plat11",
    "20199266_fwd_fuze_line",
    "20199367_aft_fuz_line",
    "1265394_fit_chrg_tube",
    "1252629_washer_lock_iternal_tooth2",
    "4902493_retainer_fuz_liner_aft",
    "1123646_nut_fit_charg_tube2",
    "20199361_fwd_chrg_tube",
    "20199361_030_aft_charg_tube",
  ];

  const [values1, setValues1] = useState([
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
  ]);

  const titles2 = [
    "4512424_cap_shipping",
    "nas1149f0832p_flat_washer8",
    "nas568_41_hex_head_bolt8",
    "x20173251_lug_shipping2",
    "20199362_charge_tube_plug",
    "nasm90725_31_screw_cap_hex_head2",
    "ms35338_45_washer_lock_sprg4",
    "mil_dtl_450_bituminous",
    "as3582_236_o_ring_small2",
  ];

  const [values2, setValues2] = useState([
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
  ]);

  const titles3 = [
    "923as694_o_ring_rubber",
    "ms51964_69_set_screw1",
    "a_a_208_ink_marking_stencil",
    "mil_prf_63460_gun_oil",
    "mil_prf_16173_corrision_resistant_grease",
    "sae_as8660_silicone_lubricant",
    "mil_prf_680_degreasing_solvent",
    "shipping_plugs2",
  ];

  const [values3, setValues3] = useState([
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
  ]);

  const titles4 = [
    "job_at",
    "screw_lot6",
    "threadlock_271_lot",
    "set_screw_lot_6",
    "ams_s_8802_lot",
    "two_part_polysulfie_sealant",
  ];

  const [values4, setValues4] = useState([
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
    "0.0",
  ]);

  const [intervalId, setIntervalID] = useState(null);
  const [tick, setTick] = useState(false);
  const timer = () => setTick((t) => !t);
  const [isStarted, setIsStarted] = useState(false);

  useEffect(() => {
    setGoodParts(appData.shiftGoodParts);
    setBadParts(appData.shiftBadParts);
  }, [appDataStore.shiftGoodParts, appDataStore.shiftBadParts]);

  const updateGoodParts = (value) => {
    appData.shiftGoodParts = parseInt(value);
    dispatch(setAppDataStore(appData));
  };

  const updateBadParts = (value) => {
    appData.shiftBadParts = parseInt(value);
    dispatch(setAppDataStore(appData));
  };

  useEffect(() => {
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  useEffect(() => {
    if (isStarted) {
      setProcessingTime(
        moment(processingTime, "HH:mm:ss").add(1, "seconds").format("HH:mm:ss")
      );
    }
  }, [tick]);

  const onPartId = (value) => {
    setPartID(value);
    resetTimers();
    getPartInfo(value);
  };

  const resetTimers = () => {
    setNotes("");
    setProcessingTime("00:00:00");
    setIsStarted(false);

    const updatedValues1 = values1.map((number, i) => {
      if (i === 0) {
        return "";
      }
      return number;
    });
    setValues1(updatedValues1);

    const updatedValue4 = values4.map((number, i) => {
      if (i === 0) {
        return "";
      }
      return number;
    });
    setValues4(updatedValue4);
  };

  const onStartClick = () => {
    if (!isStarted) {
      // Clear Old Timer if exists
      if (intervalId) {
        clearInterval(intervalId);
      }

      setIntervalID(setInterval(timer, 1000));
      setIsStarted(true);
    }
  };

  const onStopClick = () => {
    clearInterval(intervalId);
    setIsStarted(false);
    reportTimes();
  };

  const getPartInfo = (value) => {
    if (value === "") {
      return;
    }
    let param = {
      customer_id: factoryData.accountId,
      part_id: value,
    };
    dispatch(isSpinning(true));
    apiCallForGetBlu136Assembly(param)
      .then((res) => {
        dispatch(isSpinning(false));
        let stationInfo = res["station"];
        setProcessingTime(secondToTime(stationInfo["processing_time"]));
        setValues1([
          stationInfo["base_plat11"],
          stationInfo["20199266_fwd_fuze_line"],
          stationInfo["20199367_aft_fuz_line"],
          stationInfo["1265394_fit_chrg_tube"],
          stationInfo["1252629_washer_lock_iternal_tooth2"],
          stationInfo["4902493_retainer_fuz_liner_aft"],
          stationInfo["1123646_nut_fit_charg_tube2"],
          stationInfo["20199361_fwd_chrg_tube"],
          stationInfo["20199361_030_aft_charg_tube"],
        ]);

        setValues2([
          stationInfo["4512424_cap_shipping"],
          stationInfo["nas1149f0832p_flat_washer8"],
          stationInfo["nas568_41_hex_head_bolt8"],
          stationInfo["x20173251_lug_shipping2"],
          stationInfo["20199362_charge_tube_plug"],
          stationInfo["nasm90725_31_screw_cap_hex_head2"],
          stationInfo["ms35338_45_washer_lock_sprg4"],
          stationInfo["mil_dtl_450_bituminous"],
          stationInfo["as3582_236_o_ring_small2"],
        ]);

        setValues3([
          stationInfo["923as694_o_ring_rubber"],
          stationInfo["ms51964_69_set_screw1"],
          stationInfo["a_a_208_ink_marking_stencil"],
          stationInfo["mil_prf_63460_gun_oil"],
          stationInfo["mil_prf_16173_corrision_resistant_grease"],
          stationInfo["sae_as8660_silicone_lubricant"],
          stationInfo["mil_prf_680_degreasing_solvent"],
          stationInfo["shipping_plugs2"],
        ]);

        setValues4([
          stationInfo["job_at"],
          stationInfo["screw_lot6"],
          stationInfo["threadlock_271_lot"],
          stationInfo["set_screw_lot_6"],
          stationInfo["ams_s_8802_lot"],
          stationInfo["two_part_polysulfie_sealant"],
        ]);
      })
      .catch((err) => {
        dispatch(isSpinning(false));
        //message.error("Fail to get times!");
      });
  };

  const reportTimes = () => {
    if (partID === "") {
      message.warn("Please input Part ID to save times.");
      return;
    }

    if (factoryData.accountId === "") {
      message.warn("Please login with customer id");
      return;
    }

    if (appData.machineName === "") {
      message.warn("Please input machine name in settings page");
      return;
    }

    let param = {
      customer_id: factoryData.accountId,
      machine_id: appData.machineName,
      created_at: moment().format("YYYY-MM-DD HH:mm:ss") + ".000000",
      timestamp: Date.now(),
      operator: userData.username_full,
      part_id: partID,
      date: moment().format("MM/DD/YYYY"),
      time: moment().format("HH:mm:ss"),
      processing_time: timeToSecond(processingTime),
      p_base_plat11: values1[0],
      p_20199266_fwd_fuze_line: values1[1],
      p_20199367_aft_fuz_line: values1[2],
      p_1265394_fit_chrg_tube: values1[3],
      p_1252629_washer_lock_iternal_tooth2: values1[4],
      p_4902493_retainer_fuz_liner_aft: values1[5],
      p_1123646_nut_fit_charg_tube2: values1[6],
      p_20199361_fwd_chrg_tube: values1[7],
      p_20199361_030_aft_charg_tube: values1[8],
      p_4512424_cap_shipping: values2[0],
      p_nas1149f0832p_flat_washer8: values2[1],
      p_nas568_41_hex_head_bolt8: values2[2],
      p_x20173251_lug_shipping2: values2[3],
      p_20199362_charge_tube_plug: values2[4],
      p_nasm90725_31_screw_cap_hex_head2: values2[5],
      p_ms35338_45_washer_lock_sprg4: values2[6],
      p_mil_dtl_450_bituminous: values2[7],
      p_as3582_236_o_ring_small2: values2[8],
      p_923as694_o_ring_rubber: values3[0],
      p_ms51964_69_set_screw1: values3[1],
      p_a_a_208_ink_marking_stencil: values3[2],
      p_mil_prf_63460_gun_oil: values3[3],
      p_mil_prf_16173_corrision_resistant_grease: values3[4],
      p_sae_as8660_silicone_lubricant: values3[5],
      p_mil_prf_680_degreasing_solvent: values3[6],
      p_shipping_plugs2: values3[7],
      p_job_at: values4[0],
      p_screw_lot6: values4[1],
      p_threadlock_271_lot: values4[2],
      p_set_screw_lot_6: values4[3],
      p_ams_s_8802_lot: values4[4],
      p_two_part_polysulfie_sealant: values4[5],
    };

    dispatch(isSpinning(true));
    apiCallForPostBlu136Assembly(param)
      .then((res) => {
        dispatch(isSpinning(false));
        message.success("Success to report times!");
      })
      .catch((err) => {
        dispatch(isSpinning(false));
        message.error("Fail to report times!");
      });
  };

  return (
    <div className="assembly136-station-layout">
      <InputModeModal
        title={"Please Select Input Mode"}
        showModal={showInputMode}
        setShowModal={setShowInputMode}
        setShowInput={setShowPartID}
      />
      <PartIDModal
        showModal={showPartID}
        setShowModal={setShowPartID}
        partID={partID}
        setPartID={onPartId}
      />
      <GoodBadModal
        goodParts={goodParts}
        badParts={badParts}
        setGoodParts={updateGoodParts}
        setBadParts={updateBadParts}
        showModal={showGoodBadModal}
        setShowModal={setShowGoodBadModal}
      />
      <TextInputArrayModal
        showModal={showInputModal1}
        setShowModal={setShowInputModal1}
        values={values1}
        setValues={setValues1}
        titles={titles1}
        title={"Assembly Usage 1"}
        subTitle={"Please input Assembly Usage 1."}
        span={8}
      />
      <TextInputArrayModal
        showModal={showInputModal2}
        setShowModal={setShowInputModal2}
        values={values2}
        setValues={setValues2}
        titles={titles2}
        title={"Assembly Usage 2"}
        subTitle={"Please input Assembly Usage 2."}
        span={8}
      />
      <TextInputArrayModal
        showModal={showInputModal3}
        setShowModal={setShowInputModal3}
        values={values3}
        setValues={setValues3}
        titles={titles3}
        title={"Assembly Usage 3"}
        subTitle={"Please input Assembly Usage 3."}
        span={8}
      />
      <TextInputArrayModal
        showModal={showInputModal4}
        setShowModal={setShowInputModal4}
        values={values4}
        setValues={setValues4}
        titles={titles4}
        title={"Tail Fairing"}
        subTitle={"Please input Tail Fairing."}
        span={8}
      />
      <div className="assembly136-station-title">
        {"ASSEMBLY STATION - BLU 136"}
      </div>
      <div className="assembly136-station-top-left">
        <div className="assembly136-station-part-id">
          <PartIDInputLayout
            partID={partID}
            setPartID={onPartId}
            setShowInputMode={setShowInputMode}
          />
        </div>
        <div className="assembly136-station-user-info">
          <UserInfoLayout />
        </div>
      </div>
      <div className="assembly136-station-top-right">
        <Radio.Group
          // onChange={updateValue}
          value={inputMode}
          className="assembly136-station-option"
          onChange={(e) => {
            setInputMode(e.target.value);
          }}
        >
          <Row align={"middle"} style={{ marginTop: "10px" }}>
            <Col span={24}>
              <Radio value={"0"} style={{ color: "white" }}>
                Barcode
              </Radio>
            </Col>
            <Col span={24}>
              <Radio value={"1"} style={{ color: "white" }}>
                Manual
              </Radio>
            </Col>
          </Row>
        </Radio.Group>
      </div>
      <div className="assembly136-station-content">
        <Row>
          <Col span={12}>
            <CurrentPartProcessingTimeWidget
              time={processingTime}
              setTime={setProcessingTime}
              onStartClick={onStartClick}
              onStopClick={onStopClick}
            />
          </Col>
          <Col span={12} style={{ paddingLeft: "5px" }}>
            <Row>
              <Col span={12}>
                <GoodBadWidget
                  type={0}
                  value={goodParts}
                  setValue={updateGoodParts}
                  setShowModal={setShowGoodBadModal}
                />
              </Col>
              <Col span={12}>
                <GoodBadWidget
                  type={1}
                  value={badParts}
                  setValue={updateBadParts}
                  setShowModal={setShowGoodBadModal}
                />
              </Col>
            </Row>
          </Col>
        </Row>
        <div className="assembly136-station-info">
          <div className="assembly136-station-info-title">
            {"Assembly Info"}
          </div>
          <Row align={"middle"} className="assembly136-station-info-content">
            <Col
              style={{ textAlign: "center" }}
              span={6}
              onClick={() => {
                setShowInputModal1(true);
              }}
            >
              <div className="assembly136-station-info-button">
                {"Assembly Usage1"}
              </div>
            </Col>
            <Col
              span={6}
              onClick={() => {
                setShowInputModal2(true);
              }}
            >
              <div className="assembly136-station-info-button">
                {"Assembly Usage2"}
              </div>
            </Col>
            <Col
              span={6}
              onClick={() => {
                setShowInputModal3(true);
              }}
            >
              <div className="assembly136-station-info-button">
                {"Assembly Usage3"}
              </div>
            </Col>
            <Col
              span={6}
              onClick={() => {
                setShowInputModal4(true);
              }}
            >
              <div className="assembly136-station-info-button">
                {"Tail Fairing"}
              </div>
            </Col>
          </Row>
        </div>
      </div>
    </div>
  );
};

export default AssemblyStation136Layout;
