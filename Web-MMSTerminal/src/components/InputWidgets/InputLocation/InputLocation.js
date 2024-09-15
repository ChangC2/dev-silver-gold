// @flow strict

import { Col, Input, message, Row, Select, Spin } from "antd";
import React, { useEffect, useState } from "react";
import "./InputLocation.css";
import AsyncSelect from "react-select/async";
import {
  getAddresses,
  getAddressFromCoord,
  getCoordinateFromPlaceID,
} from "services/helper";
import { AimOutlined } from "@ant-design/icons";
import TextButton from "components/ButtonWidgets/TextButton/TextButton";
const customStyles = {
  option: (provided, state) => ({
    ...provided,
    textAlign: "left",
  }),
};
function InputLocation(props) {
  const { location, setLocation, hideMyLocation, inputPlaceholder } = props;
  const [inputValue, setInputValue] = useState("");
  useEffect(() => {
    if (location !== undefined && location !== null) {
      setInputValue(location.address);
    }
  }, []);
  const promiseOptions = (keyword) =>
    new Promise((resolve, reject) => {
      if (keyword === "" || keyword === undefined) {
        reject();
      } else {
        getAddresses(keyword, ["ZA"], "(regions)")
          .then((res) => {
            resolve(
              res.map((info) => ({
                value: info.place_id,
                label: info.description,
              }))
            );
          })
          .catch((err) => reject());
      }
    });

  const onClickGetMyLocation = () => {
    navigator.permissions
      .query({ name: "geolocation" })
      .then(function (result) {
        if (result.state === "granted" || result.state === "prompt") {
          navigator.geolocation.getCurrentPosition(function (position) {
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;
            getAddressFromCoord(latitude, longitude)
              .then((res) => {
                setInputValue(res);
                setLocation({
                  address: res,
                  lat: latitude,
                  lng: longitude,
                });
              })
              .then((err) => {});
          });
        } else if (result.state === "denied") {
          message.error(
            "The permission is denied. Please type yoru address manually."
          );
        }
        result.onchange = function () {};
      });
  };
  return (
    <span className="input-location-widget">
      <Row align={"middle"}>
        <Col flex={"auto"}>
          <AsyncSelect
            cacheOptions
            styles={customStyles}
            defaultOptions
            loadOptions={promiseOptions}
            inputValue={inputValue}
            // inputValue={location.address}
            placeholder={
              inputPlaceholder === undefined
                ? "Your address..."
                : inputPlaceholder
            }
            onInputChange={(e) => {
              setInputValue(e);
            }}
            onChange={(info) => {
              // getCoordinateFromPlaceID
              getCoordinateFromPlaceID(info.value).then((res) => {
                setLocation({
                  address: info.label,
                  place_id: info.value,
                  ...res,
                });
              });
            }}
          />
        </Col>
        {hideMyLocation !== true && (
          <Col flex={"50px"}>
            <TextButton style={{ fontSize: 24 }} onClick={onClickGetMyLocation}>
              <AimOutlined />
            </TextButton>
          </Col>
        )}
      </Row>
    </span>
  );
}

export default InputLocation;
