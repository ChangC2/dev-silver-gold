import { useSelector } from "react-redux";
import "./ContentLayout.css";

const ContentLayout = (props) => {
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { pages } = appDataStore;

  return (
    <div
      className={
        pages[pages.length - 1] === 0 || pages[pages.length - 1] === 1
          ? "content-layout-with-top"
          : "content-layout-no-top"
      }
    >
      {props.children}
    </div>
  );
};

export default ContentLayout;
