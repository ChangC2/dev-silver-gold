import { Col, Row, Select } from "antd";
import { useSelector } from "react-redux";
import { sizeMobile } from "../../../../../src/services/common/constants";
import lang from "../../../../../src/services/lang";
import IndicatorItem from "./IndicatorItem";
const { Option } = Select;

function IndicatorContainer(props) {
  const { langIndex } = useSelector((x) => x.app);
  const {
    machineInfo,
    hstInfo,
    screenSize,
    lastGantt,
    shift,
    setShift,
    shifts,
  } = props;

  return screenSize.width >= sizeMobile ? (
    <div className="indicator-container">
      <div
        style={{
          borderBottom: "1px solid white",
          textAlign: "left",
          display: "flex",
          justifyContent: "space-between",
        }}
      >
        <span className="indicator-title">
          {lang(langIndex, "cnc_keyindicator")}
        </span>
        <div className="shift-select">
          <Select
            className="page-changer-style"
            style={{
              width: 200,
              marginLeft: 5,
              textAlign: "left",
            }}
            dropdownClassName="page-changer-style-dropdown"
            value={shift}
            onChange={(e) => setShift(e)}
          >
            <Option className="page-changer-item" key={`shift-0`} value={0}>
              24 Hours
            </Option>
            <Option className="page-changer-item" key={`shift-1`} value={1}>
              Shift 1 ({shifts.length > 0 ? shifts[1] : ""})
            </Option>
            <Option className="page-changer-item" key={`shift-2`} value={2}>
              Shift 2 ({shifts.length > 1 ? shifts[2] : ""})
            </Option>
            <Option className="page-changer-item" key={`shift-3`} value={3}>
              Shift 3 ({shifts.length > 2 ? shifts[3] : ""})
            </Option>
          </Select>
        </div>
      </div>
      <Row gutter={[16, 16]} align="middle">
        <Col span={8} style={{ marginTop: 30 }}>
          <IndicatorItem
            value={Math.round(hstInfo["oee"]) / 100.0}
            id={"oee_" + machineInfo["id"]}
            title={lang(langIndex, "cnc_oee")}
          />
        </Col>

        <Col span={16}>
          <Row style={{ marginTop: 20 }}>
            <Col span={8}>
              <IndicatorItem
                value={Math.round(hstInfo["availability"]) / 100.0}
                id={"ava_" + machineInfo["id"]}
                title={lang(langIndex, "cnc_availability")}
              />
            </Col>
            <Col span={8}>
              <IndicatorItem
                value={Math.round(hstInfo["quality"]) / 100.0}
                id={"qua_" + machineInfo["id"]}
                title={lang(langIndex, "cnc_quality")}
              />
            </Col>
            <Col span={8}>
              <IndicatorItem
                value={Math.round(hstInfo["performance"]) / 100.0}
                id={"per_" + machineInfo["id"]}
                title={lang(langIndex, "cnc_performance")}
              />
            </Col>
          </Row>
          <div style={{ height: 80, paddingTop: 20 }}>
            <Row>
              <Col span={14}>
                <Row>
                  <Col span={14}>
                    <h4 style={{ color: "#eeeeee", textAlign: "right" }}>
                      {lang(langIndex, "cnc_mainprogram")}:&nbsp;
                    </h4>
                  </Col>
                  <Col span={10}>
                    <h4 style={{ color: "#eeeeee", textAlign: "left" }}>
                      {lastGantt["main_program"] === undefined
                        ? ""
                        : lastGantt["main_program"]}
                    </h4>
                  </Col>
                  <Col span={14}>
                    <h4 style={{ color: "#eeeeee", textAlign: "right" }}>
                      {lang(langIndex, "cnc_currentprogram")}:&nbsp;
                    </h4>
                  </Col>
                  <Col span={10}>
                    <h4 style={{ color: "#eeeeee", textAlign: "left" }}>
                      {lastGantt["current_program"] === undefined
                        ? ""
                        : lastGantt["current_program"]}
                    </h4>
                  </Col>
                </Row>
              </Col>
              <Col span={10} style={{ textAlign: "left", paddingLeft: 20 }}>
                <div>
                  <h4 style={{ color: "#30BF78" }}>
                    {lang(langIndex, "cnc_goodparts")}&nbsp;:&nbsp;
                    {hstInfo["goodParts"]}
                  </h4>
                </div>
                <div>
                  <h4 style={{ color: "#F4664A" }}>
                    {lang(langIndex, "cnc_badparts")}&nbsp;&nbsp;&nbsp;:&nbsp;
                    {hstInfo["badParts"]}
                  </h4>
                </div>
              </Col>
            </Row>
          </div>
        </Col>
      </Row>
    </div>
  ) : (
    <div style={{ padding: 10 }}>
      <div
        style={{
          borderBottom: "1px solid white",
          textAlign: "left",
          display: "flex",
          justifyContent: "space-between",
        }}
      >
        <span className="indicator-title">
          {lang(langIndex, "cnc_keyindicator")}
        </span>
        <div className="shift-select">
          <Select
            className="page-changer-style"
            style={{
              width: 200,
              marginLeft: 5,
              textAlign: "left",
            }}
            dropdownClassName="page-changer-style-dropdown"
            value={shift}
            onChange={(e) => setShift(e)}
          >
            <Option className="page-changer-item" key={`shift-0`} value={0}>
              24 Hours
            </Option>
            <Option className="page-changer-item" key={`shift-1`} value={1}>
              Shift 1 ({shifts.length > 0 ? shifts[0] : ""})
            </Option>
            <Option className="page-changer-item" key={`shift-2`} value={2}>
              Shift 2 ({shifts.length > 1 ? shifts[1] : ""})
            </Option>
            <Option className="page-changer-item" key={`shift-3`} value={3}>
              Shift 3 ({shifts.length > 2 ? shifts[2] : ""})
            </Option>
          </Select>
        </div>
      </div>
      <Row gutter={[16, 16]} align="middle">
        <Col span={24}>
          <Row justify={"center"} align={"middle"}>
            <Col span={12}>
              <IndicatorItem
                screenSize={screenSize}
                value={Math.round(hstInfo["oee"]) / 100.0}
                id={"oee_" + machineInfo["id"]}
                title={lang(langIndex, "cnc_oee")}
              />
            </Col>
            <Col span={12} style={{ textAlign: "left" }}>
              <div>
                <h4 style={{ color: "#00FF00" }}>
                  {lang(langIndex, "cnc_goodparts")}&nbsp;:&nbsp;
                  {hstInfo["goodParts"]}
                </h4>
              </div>
              <div>
                <h4 style={{ color: "#FF1B00" }}>
                  {lang(langIndex, "cnc_badparts")}&nbsp;&nbsp;&nbsp;:&nbsp;
                  {hstInfo["badParts"]}
                </h4>
              </div>
            </Col>
          </Row>
        </Col>

        <Col span={24}>
          <Row>
            <Col span={8}>
              <IndicatorItem
                value={Math.round(hstInfo["availability"]) / 100.0}
                id={"ava_" + machineInfo["id"]}
                title={lang(langIndex, "cnc_availability")}
              />
            </Col>
            <Col span={8}>
              <IndicatorItem
                screenSize={screenSize}
                value={Math.round(hstInfo["quality"]) / 100.0}
                id={"qua_" + machineInfo["id"]}
                title={lang(langIndex, "cnc_quality")}
              />
            </Col>
            <Col span={8}>
              <IndicatorItem
                screenSize={screenSize}
                value={Math.round(hstInfo["performance"]) / 100.0}
                id={"per_" + machineInfo["id"]}
                title={lang(langIndex, "cnc_performance")}
              />
            </Col>
          </Row>
          <Col span={24}>
            <div style={{ marginTop: 5 }}>
              <Row>
                <Col span={14}>
                  <h4 style={{ color: "#eeeeee", textAlign: "right" }}>
                    {lang(langIndex, "cnc_mainprogram")}:&nbsp;
                  </h4>
                </Col>
                <Col span={10}>
                  <h4 style={{ color: "#eeeeee", textAlign: "left" }}>
                    {lastGantt["main_program"] === undefined
                      ? ""
                      : lastGantt["main_program"]}
                  </h4>
                </Col>
                <Col span={14}>
                  <h4 style={{ color: "#eeeeee", textAlign: "right" }}>
                    {lang(langIndex, "cnc_currentprogram")}:&nbsp;
                  </h4>
                </Col>
                <Col span={10}>
                  <h4 style={{ color: "#eeeeee", textAlign: "left" }}>
                    {lastGantt["current_program"] === undefined
                      ? ""
                      : lastGantt["current_program"]}
                  </h4>
                </Col>
              </Row>
              {/* <div style={{ textAlign: 'left' }}>
                                    <h4 style={{ color: "#eeeeee" }}>
                                        Production Rate: {((Math.round(hstInfo['oee']) / 1000000.0 + Math.round(hstInfo['quality']) / 100.0) / 2 * 100.0).toFixed(2)}%
                                                    </h4>
                                </div>
                                <Progress
                                    percent={(Math.round(hstInfo['oee']) / 1000000.0 + Math.round(hstInfo['quality']) / 100.0) / 2 * 100.0}
                                    strokeWidth={20}
                                    showInfo={false}
                                    status='active' /> */}
            </div>
          </Col>
        </Col>
      </Row>
    </div>
  );
}

export default IndicatorContainer;
