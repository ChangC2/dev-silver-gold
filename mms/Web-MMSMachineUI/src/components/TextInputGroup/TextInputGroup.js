import ReactInputMask from "react-input-mask";

import "./TextInputGroup.css";
function TextInputGroup(props) {
  const {
    initValue,
    field,
    updateValue,
    title,
    input_type,
    isCenter,
    disabled,
  } = props;
  return (
    <div className="setting-text-input-group-container">
      <div className="setting-text-input-group-title">{title}</div>
      {input_type === "time-setting" ? (
        <ReactInputMask
          className={
            isCenter === true
              ? "setting-text-input-group-value centered-text"
              : "setting-text-input-group-value"
          }
          value={initValue[field]}
          onChange={(e) => updateValue(field, e.target.value)}
          mask="99:99:99"
          style={{ outlineStyle: "none" }}
        />
      ) : (
        <input
          className={
            isCenter === true
              ? "setting-text-input-group-value centered-text"
              : "setting-text-input-group-value"
          }
          value={
            initValue[field] === undefined
              ? input_type === "number"
                ? 0
                : ""
              : initValue[field]
          }
          onChange={(e) => updateValue(field, e.target.value)}
          type={input_type === undefined ? "text" : input_type}
          disabled={disabled ? "disabled" : ""}
          style={{ outlineStyle: "none" }}
        />
      )}
    </div>
  );
}

export default TextInputGroup;
