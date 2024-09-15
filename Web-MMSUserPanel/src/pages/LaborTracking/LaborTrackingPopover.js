import {
  Button,
  Col,
  Input,
  message,
  Row,
  Select,
  Spin,
  TimePicker,
} from "antd";
import moment from "moment";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  GetLaborSettings,
  UpdateLaborSetting,
  AddLaborTracking,
} from "services/common/auth_apis";
import "./LaborTrackingPopover.css";
const { Option } = Select;

function LaborTrackingPopover(props) {
  const dispatch = useDispatch();
  const authData = useSelector((x) => x.authService);
  const { langIndex } = useSelector((x) => x.app);
  const {
    description,
    setDescription,
    jobID,
    setJobID,
    category,
    setCategory,
  } = props;

  const [isBusy, setIsBusy] = useState(false);

  const [validStartTime, setValidStartTime] = useState(false);
  const [validEndTime, setValidEndTime] = useState(false);

  const [taskTime, setTaskTime] = useState("00:00:00");
  const [intervalId, setIntervalID] = useState(null);
  const [tick, setTick] = useState(false);

  const [sTime, setSTime] = useState();
  const [eTime, setETime] = useState();

  const [isStartedAuto, setIsStartedAuto] = useState(false);
  const [startTime, setStartTime] = useState(null);

  const timer = () => setTick((t) => !t);

  useEffect(() => {
    setIsStartedAuto(authData["is_started_auto"] === "1" ? true : false);
    setStartTime(
      authData["start_time"] === "" || authData["start_time"] === undefined
        ? moment()
        : moment(authData["start_time"], "HH:mm:ss")
    );
  }, [authData]);

  useEffect(() => {
    if (!isStartedAuto) return;
    let startClockTime = moment(startTime, "HH:mm:ss");

    let now = moment();
    let endClockTime = moment(now, "HH:mm:ss");

    let mins = moment
      .utc(
        moment(endClockTime, "HH:mm:ss").diff(
          moment(startClockTime, "HH:mm:ss")
        )
      )
      .format("HH:mm:ss");
    setTaskTime(mins);
  }, [tick]);

  useEffect(() => {
    GetLaborSettings(authData["user_id"], dispatch, (res) => {});
    setIntervalID(setInterval(timer, 1000));
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  const laborCategories = authData.laborCategories;
  const categoryOptionUI =
    laborCategories == undefined || laborCategories.length == 0
      ? null
      : laborCategories.map((info, index) => {
          return (
            <Option
              key={`category_option_key${index}`}
              value={info.id}
              className="labor-selector-value"
            >
              {info.name}
            </Option>
          );
        });

  const disabledEndHours = () => {
    if (sTime == null) {
      return;
    }
    const hours = [];
    const currentHour = sTime.hour();
    for (let i = 0; i < currentHour; i++) {
      hours.push(i);
    }
    return hours;
  };

  const disabledEndMinutes = (selectedHour) => {
    if (sTime == null) {
      return;
    }
    const minutes = [];
    const currentMinute = sTime.minute();
    if (selectedHour === sTime.hour()) {
      for (let i = 0; i <= currentMinute; i++) {
        minutes.push(i);
      }
    }
    return minutes;
  };

  const onStartAutoTrack = () => {
    UpdateLaborSetting(
      authData["user_id"],
      authData["time_format"],
      "1",
      moment().format("HH:mm:ss"),
      dispatch,
      (res) => {}
    );

    setSTime(null);
    setETime(null);
    setValidStartTime(false);
    setValidEndTime(false);
  };

  const onClickAutoSave = () => {
    if (category === "") {
      message.error("Please select category");
      return;
    }

    UpdateLaborSetting(
      authData["user_id"],
      authData["time_format"],
      "0",
      "",
      dispatch,
      (res) => {}
    );

    const param = {
      user_id: authData["user_id"],
      category: authData["category"],
      description: description,
      jobID: jobID,
      date: moment().format("YYYY-MM-DD"),
      start_time: startTime.format("HH:mm:ss"),
      end_time: moment().format("HH:mm:ss"),
    };

    onSave(param);
  };

  const onClickManualSave = () => {
    if (category === "") {
      message.error("Please select category");
      return;
    }

    const param = {
      user_id: authData["user_id"],
      category: authData["category"],
      description: description,
      jobID: jobID,
      date: moment().format("YYYY-MM-DD"),
      start_time: sTime.format("HH:mm:ss"),
      end_time: eTime.format("HH:mm:ss"),
    };
    onSave(param);
  };

  const onSave = (param) => {
    setIsBusy(true);
    AddLaborTracking(param, (res) => {
      setIsBusy(false);
      if (res["status"] == true) {
        setDescription("");
        setJobID("");
        setSTime(null);
        setETime(null);
        setValidStartTime(false);
        setValidEndTime(false);
        message.success("Success to save.");
      } else {
        message.error("Fail to save");
      }
    });
  };

  const onSelectStart = (time) => {
    setValidStartTime(true);
    setValidEndTime(false);
    setSTime(time);
    setETime(null);
  };

  const onSelectEnd = (time) => {
    setETime(time);
    setValidEndTime(true);
  };

  return (
    <div>
      <Spin spinning={isBusy}>
        <div className="labor-tracking-modal-body-container">
          <div>
            <span className="labor-tracking-label">Category</span>
            <Select
              onChange={(value) => {
                setCategory(value);
              }}
              defaultValue={category}
              value={category}
              className="labor-selector"
              dropdownClassName="labor-selector-dropdown"
            >
              {categoryOptionUI}
            </Select>
          </div>

          <div style={{ marginTop: 10 }}>
            <span className="labor-tracking-label">Job ID</span>
            <Input
              className="labor-tracking-input-style"
              value={jobID}
              onChange={(e) => setJobID(e.target.value)}
            />
          </div>

          <div style={{ marginTop: 10 }}>
            <span className="labor-tracking-label">Description</span>
            <Input
              className="labor-tracking-input-style"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>

          <Row>
            <Col span={12} style={{ paddingRight: 5 }}>
              <Button
                disabled={isStartedAuto}
                ghost
                className="labor-tracking-auto-button"
                onClick={onStartAutoTrack}
              >
                Start
              </Button>
            </Col>

            <Col span={12} style={{ paddingLeft: 5 }}>
              <Button
                disabled={!isStartedAuto}
                ghost
                className="labor-tracking-auto-button"
                onClick={onClickAutoSave}
              >
                Stop And Save
              </Button>
            </Col>
          </Row>

          <Row style={{ marginTop: 15, marginBottom: 15 }} justify="middle">
            <Col span={11} className="labor-tracking-line"></Col>
            <Col span={2} align="center">
              <span className="labor-tracking-or">OR</span>
            </Col>

            <Col span={11} className="labor-tracking-line"></Col>
          </Row>

          <Row justify="middle">
            <Col span={11}>
              {authData["time_format"] === "12hr" ? (
                <TimePicker
                  onSelect={onSelectStart}
                  disabled={isStartedAuto}
                  className="labor-tracking-time-picker"
                  use12Hours
                  format="HH:mm:ss A"
                  placeholder="Start Time"
                  value={sTime}
                />
              ) : (
                <TimePicker
                  onSelect={onSelectStart}
                  disabled={isStartedAuto}
                  className="labor-tracking-time-picker"
                  format="HH:mm:ss"
                  placeholder="Start Time"
                  value={sTime}
                />
              )}
            </Col>

            <Col
              span={2}
              align="center"
              justify="middle"
              style={{ color: "white" }}
            >
              ~
            </Col>

            <Col span={11}>
              {authData["time_format"] === "12hr" ? (
                <TimePicker
                  onSelect={onSelectEnd}
                  disabled={isStartedAuto || !validStartTime}
                  disabledHours={disabledEndHours}
                  disabledMinutes={disabledEndMinutes}
                  className="labor-tracking-time-picker"
                  use12Hours
                  format="HH:mm:ss A"
                  placeholder="End Time"
                  value={eTime}
                />
              ) : (
                <TimePicker
                  onSelect={onSelectEnd}
                  disabled={isStartedAuto || !validStartTime}
                  disabledHours={disabledEndHours}
                  disabledMinutes={disabledEndMinutes}
                  className="labor-tracking-time-picker"
                  format="HH:mm:ss"
                  placeholder="End Time"
                  value={eTime}
                />
              )}
            </Col>
          </Row>
          <Button
            disabled={isStartedAuto || !validStartTime || !validEndTime}
            ghost
            className="labor-tracking-auto-button"
            onClick={onClickManualSave}
          >
            Save Manual Input Time
          </Button>
          {isStartedAuto && (
            <div className="labor-tracking-task-time">{taskTime}</div>
          )}
        </div>
      </Spin>
    </div>
  );
}
export default LaborTrackingPopover;
