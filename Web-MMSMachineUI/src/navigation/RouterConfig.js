import {
  ROOT,
  ROUTE_LOGIN
} from "navigation/CONSTANTS";
import { NotFound } from "pages/NotFound";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Route, Switch } from "react-router-dom";

import { setAppDataStore } from "redux/actions/appActions";

import { Spin } from "antd";
import MainLayout from "layouts/MainLayout/MainLayout";
import { appData } from "services/global";

export const RouterConfig = () => {
  const screenSize = useWindowSize();
  const dispatch = useDispatch();
  const { isSpinning } = useSelector((x) => x.appDataStore);

  useEffect(() => {
    if (screenSize === undefined || screenSize.width === undefined) return;
    appData.width = screenSize.width;
    appData.height = screenSize.height;
    dispatch(setAppDataStore(appData));
  }, [screenSize]);

  return (
    <Spin
      size={"large"}
      spinning={isSpinning}
      style={{ marginTop: 150 }}
    >
      <Switch>
        <Route exact path={ROOT} component={MainLayout} />
        {/* List a generic 404-Not Found route here */}
        <Route path="*" component={NotFound} />
      </Switch>
    </Spin>
  );
};

function useWindowSize() {
  // Initialize state with undefined width/height so server and client renders match
  // Learn more here: https://joshwcomeau.com/react/the-perils-of-rehydration/
  const [windowSize, setWindowSize] = useState({
    width: undefined,
    height: undefined,
  });

  useEffect(() => {
    // Handler to call on window resize
    function handleResize() {
      // Set window width/height to state
      setWindowSize({
        width: window.innerWidth,
        height: window.innerHeight,
      });
    }

    // Add event listener
    window.addEventListener("resize", handleResize);

    // Call handler right away so state gets updated with initial window size
    handleResize();

    // Remove event listener on cleanup
    return () => window.removeEventListener("resize", handleResize);
  }, []); // Empty array ensures that effect is only run on mount

  return windowSize;
}
