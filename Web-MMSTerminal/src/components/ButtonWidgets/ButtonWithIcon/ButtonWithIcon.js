import "./ButtonWithIcon.css";
export const ButtonWithIcon = (props) => {
  const { icon, text, onClick, style } = props;
  return (
    <div
      className="button-with-icon custom-button"
      onClick={onClick}
      style={style}
    >
      <div>{icon}</div>
      <div>{text}</div>
    </div>
  );
};

export default ButtonWithIcon;
