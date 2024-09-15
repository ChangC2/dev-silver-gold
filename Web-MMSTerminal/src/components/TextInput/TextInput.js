import ReactInputMask from "react-input-mask";

import "./TextInput.css";
function TextInput(props) {
  const { value, setValue, title, input_type, isCenter, disabled } = props;
  return (
    <div className="text-input-container">
      <div className="text-input-title">{title}</div>
      {input_type === "time-setting" ? (
        <ReactInputMask
          className={
            isCenter === true
              ? "text-input-value centered-text"
              : "text-input-value"
          }
          value={value}
          onChange={(e) => setValue(e.target.value)}
          mask="99:99:99"
        />
      ) : (
        <input
          className={
            isCenter === true
              ? "text-input-value centered-text"
              : "text-input-value"
          }
          value={value === undefined ? (input_type === "number" ? 0 : "") : value}
          onChange={(e) => setValue(e.target.value)}
          type={input_type === undefined ? "string" : input_type}
          disabled={disabled ? "disabled" : ""}
          style={{ outlineStyle: "none" }}
        />
      )}
    </div>
  );
}

export default TextInput;
