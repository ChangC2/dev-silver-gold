// @flow strict

import { Col, Row } from "antd";
import { useState } from "react";
import "./InstallConfig.css";

import TextArea from "antd/lib/input/TextArea";
import RadialInput from "components/RadialInput/RadialInput";
import TextInput from "components/TextInput/TextInput";
import InstallConfigTopLayout from "layouts/InstallConfigTopLayout/InstallConfigTopLayout";
import { useSelector } from "react-redux";
import machinenNameIcon from "../../assets/icons/ic_machine_name.png";
import installConfigIncon from "../../assets/icons/ic_menu_install.png";

function InstallConfig(props) {
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { pages } = appDataStore;

  const [machineName, setMachineName] = useState("");
  const [serialNumber, setSerialNumber] = useState("");
  const [inCycleSignal, setInCycleSignal] = useState("");
  const [cycleStartInterlock, setCycleStartInterlock] = useState("");
  const [cycleStartOnOff, setCycleStartOnOff] = useState(0);
  const [cycleStartCloseOpen, setCycleStartCloseOpen] = useState(0);
  const [pics, setPics] = useState([
    "https://slymms.com/backend/images/photo/20230419123228_20230112163511_20221028111250_20191207233715Visser Logo.jpg",
    "https://slymms.com/backend/images/photo/20230419123228_20230112163511_20221028111250_20191207233715Visser Logo.jpg",
    "https://slymms.com/backend/images/photo/20230419123228_20230112163511_20221028111250_20191207233715Visser Logo.jpg",
    "https://slymms.com/backend/images/photo/20230419123228_20230112163511_20221028111250_20191207233715Visser Logo.jpg",
    "https://slymms.com/backend/images/photo/20230419123228_20230112163511_20221028111250_20191207233715Visser Logo.jpg",
    "https://slymms.com/backend/images/photo/20230419123228_20230112163511_20221028111250_20191207233715Visser Logo.jpg",
    "https://slymms.com/backend/images/photo/20230419123228_20230112163511_20221028111250_20191207233715Visser Logo.jpg",
    "https://slymms.com/backend/images/photo/20230419123228_20230112163511_20221028111250_20191207233715Visser Logo.jpg",
    "https://slymms.com/backend/images/photo/20230419123228_20230112163511_20221028111250_20191207233715Visser Logo.jpg",
    "https://slymms.com/backend/images/photo/20230419123228_20230112163511_20221028111250_20191207233715Visser Logo.jpg",
  ]);

  const onClickSave = () => {
    
  };

  const onDropImage = (e) => {
    const files = Array.from(e.target.files);
    var formData = new FormData();
    formData.append("file", files[0]);
    // postRequest(Urls.UPLOAD_USER_IMAGE, formData, (response) => {
    //   if (response.status === true) {
    //     setImageUrl(response.url);
    //     updateImage(response.url);
    //   } else {
    //     message.error(lang(langIndex, "msg_something_wrong"));
    //   }
    // });
  };

  const imagesUI = pics.map((pic, index) => {
    return (
      <img
        className="install-config-pic"
        src={pic}
        key={"key-" + index}
        alt={"key-" + index}
      />
    );
  });

  return (
    <div
      className={
        pages[pages.length - 1] === 3 ? "install-config-page" : "display-none"
      }
    >
      <InstallConfigTopLayout />
      <div className="install-config-content">
        <Row>
          <Col span={12} className="install-config-grad-background">
            <div>
              <TextInput
                title={"Machine Name"}
                value={machineName}
                setValue={setMachineName}
              />
              <img
                src={machinenNameIcon}
                className="install-config-machine-info-icon"
                alt="machine-name"
              />
            </div>
          </Col>

          <Col span={12} className="install-config-grad-background">
            <div>
              <TextInput
                title={"Serial Number"}
                value={serialNumber}
                setValue={setSerialNumber}
              />
              <img
                src={machinenNameIcon}
                className="install-config-machine-info-icon"
                alt="machine-info"
              />
            </div>
          </Col>

          <Col span={24} className="install-config-grad-background">
            <div className="install-config-title">{"Cyle Settings"}</div>
            <Row>
              <Col span={8}>
                <div className="install-config-incyle-signal">
                  {"In Cyle Signal"}
                </div>
                <TextArea
                  className="install-config-textarea"
                  rows={4}
                  max
                  value={inCycleSignal}
                  onChange={(e) => {
                    setInCycleSignal(e.target.value);
                  }}
                />
              </Col>
              <Col
                span={8}
                style={{ paddingRight: "20px", paddingLeft: "20px" }}
              >
                <div className="install-config-incyle-signal">
                  {"Cyle Start Interlock"}
                </div>
                <RadialInput
                  value={cycleStartOnOff}
                  title="On/Off"
                  setValue={setCycleStartOnOff}
                />
                <RadialInput
                  value={cycleStartCloseOpen}
                  title="Normally Closed/Open"
                  setValue={setCycleStartCloseOpen}
                />
              </Col>
              <Col span={8}>
                <div className="install-config-incyle-signal">
                  {"Cyle Start Interlock Interface"}
                </div>
                <TextArea
                  className="install-config-textarea"
                  rows={4}
                  max
                  value={cycleStartInterlock}
                  onChange={(e) => {
                    setCycleStartInterlock(e.target.value);
                  }}
                />
              </Col>
            </Row>
          </Col>

          <Col span={24}>
            <Row
              align={"middle"}
              className="install-config-save-button"
              onClick={onClickSave}
            >
              <Col span={4} style={{ textAlign: "left" }}>
                <img
                  className="install-config-save-button-icon"
                  src={installConfigIncon}
                  alt="save"
                />
              </Col>
              <Col span={16} style={{ textAlign: "center" }}>
                {"Save Configuration"}
              </Col>
              <Col span={4}></Col>
            </Row>
          </Col>

          <Col span={24} className="install-config-grad-background">
            <div className="install-config-title">{"Pictures"}</div>
            <div className="install-config-images-container">
              <label
                className="install-config-image-picker"
                htmlFor="upload_customer_image"
              >
                {"+"}
                <input
                  type="file"
                  id="upload_customer_image"
                  name="upload_customer_image"
                  style={{ display: "none" }}
                  accept="image/x-png,image/gif,image/jpeg"
                  onChange={onDropImage}
                />
              </label>
              <div className="install-config-images">
                <div>{imagesUI}</div>
              </div>
            </div>
          </Col>
        </Row>
      </div>
    </div>
  );
}

export default InstallConfig;
